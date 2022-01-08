package com.indo.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.SnowflakeIdWorker;
import com.indo.common.web.exception.BizException;
import com.indo.core.mapper.MemGoldChangeMapper;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.core.pojo.dto.MemGoldChangeDto;
import com.indo.core.pojo.entity.MemGoldChange;
import com.indo.core.service.IMemGoldChangeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.indo.common.utils.ViewUtil.getTradeOffAmount;

/**
 * <p>
 * 会员账变记录表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-12-07
 */
@Service
@Slf4j
public class MemGoldChangeServiceImpl extends ServiceImpl<MemGoldChangeMapper, MemGoldChange> implements IMemGoldChangeService {


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MemGoldChangeMapper memGoldChangeMapper;

    /**
     * 需要修改可提现金额的账变类型
     */
    private static final List<GoldchangeEnum> enumList = new ArrayList<>();

    static {
        enumList.add(GoldchangeEnum.CANCEL_BET);

    }


    @Override
    public MemBaseinfoBo findMemBaseInfoById(Long memId) {
        return null;
    }

    /**
     * 修改用户账变信息
     *
     * @param change 账变信息
     * @return
     */
    @Override
    public boolean updateMemGoldChange(MemGoldChangeDto change) throws RuntimeException {
        if (null == change || null == change.getChangeAmount() ||
                null == change.getUserId() || null == change.getGoldchangeEnum()) {
            log.error("{}.updateUserBalance 参数不正确：{}", getClass().getName(), JSON.toJSONString(change));
            throw new BizException("请求参数错误");
        }
        log.info("{} updateUserBalance change info:{}", change.getUserId(), change);
        Long userId = change.getUserId();
        long begin;
        RReadWriteLock lock = redissonClient.getReadWriteLock(GlobalConstants.UPDATE_USER_BALANCE_ + change.getUserId());
        try {
            // 写锁
            boolean bool = lock.writeLock().tryLock(100, 20, TimeUnit.SECONDS);
            if (bool) {
                begin = System.currentTimeMillis();
                log.info("用户修改余额拿到锁{}", change.getUserId());
                MemBaseinfoBo memBaseinfo = memGoldChangeMapper.findMemBaseInfoById(Long.valueOf(userId));
                if (memBaseinfo == null) {
                    log.info("{} updateUserBalance member is null,memBaseinfo: {}", change.getUserId(), memBaseinfo);
                    throw new BizException("用户不存在");
                }
                // 获取会员当前余额
                BigDecimal currentBalance = getTradeOffAmount(memBaseinfo.getBalance());
                // 变动金额
                BigDecimal changeAmount = getTradeOffAmount(change.getChangeAmount());
                if (change.getTradingEnum().equals(TradingEnum.SPENDING)) {
                    changeAmount = changeAmount.negate();
                } else {
                    changeAmount = changeAmount.abs();
                }
                change.setChangeAmount(changeAmount);
                // 变动后的余额
                BigDecimal newBalance = currentBalance.add(changeAmount);
                // 获取会员当前可提金额
                BigDecimal canAmount = getTradeOffAmount(memBaseinfo.getCanAmount());
                // 获取会员变动后可提金额
                BigDecimal changeCanAmount = calculateChangeCanAmount(change);
                BigDecimal afterCodeAmount = canAmount.add(changeCanAmount);
                // 添加账变记录
                if (changeAmount.compareTo(BigDecimal.ZERO) != 0) {
                    MemGoldChange memGoldChange = new MemGoldChange();
                    memGoldChange.setChangeType(change.getGoldchangeEnum().getCode());
                    memGoldChange.setTradingType(change.getTradingEnum().getCode());
                    memGoldChange.setAmount(changeAmount);
                    memGoldChange.setBeforeAmount(currentBalance);
                    memGoldChange.setAfterAmount(newBalance);
                    memGoldChange.setBeforeCodeAmount(canAmount);
                    memGoldChange.setAfterCodeAmount(afterCodeAmount);
                    memGoldChange.setUserId(change.getUserId());
                    memGoldChange.setRefId(change.getRefId());
                    memGoldChange.setSerialNo(change.getSerialNo());
                    if (StringUtils.isBlank(change.getSerialNo())) {
                        memGoldChange.setSerialNo(change.getGoldchangeEnum().name() +
                                SnowflakeIdWorker.generateShortId());
                    }
                    memGoldChange.setCreateUser(change.getUpdateUser());
                    memGoldChange.setUpdateUser(change.getUpdateUser());
                    begin = System.currentTimeMillis();
                    int changeRow = baseMapper.insert(memGoldChange);
                    if (changeRow != 1) {
                        log.error("{} updateUserBalance insertSelective 插入账变信息余额失败. return:{}", userId, changeRow);
                        throw new BizException("操作失败");
                    }
                }
                int row = this.updateMemberAmount(changeAmount, changeCanAmount, memBaseinfo.getId());
                if (row != 1) {
                    log.error("{} updateUserBalance updateMemberAmount 更新余额失败. return:{}", userId, row);
                    throw new BizException("操作失败");
                }
                log.info("{} updateUserBalance updateMemberAmount 耗时 time, {}", userId, System.currentTimeMillis() - begin);
                return true;
            } else {
                log.error("{} updateUserBalance 用户修改余额没拿到锁, 记录修改余额变动对象 {}", userId, change);
                throw new BizException("操作失败");
            }
        } catch (Exception e) {
            log.error("{} updateUserBalance occur error. change info:{}", userId, change, e);
            throw new RuntimeException("用户修改余额出错", e.getCause());
        } finally {
            // 释放锁
            lock.writeLock().unlock();
            log.info("{} updateUserBalance 用户修改余额释放锁", userId);
        }


    }


    /**
     * 根据类型获取变动可提金额
     *
     * @param change
     * @return
     */
    private BigDecimal calculateChangeCanAmount(MemGoldChangeDto change) {
        BigDecimal changeAmount = change.getChangeAmount();
        //  如果是收益且设置不需要打码
        if (change.getIncomeCodeFlag() == false || enumList.contains(change.getGoldchangeEnum())) {
            return changeAmount;
        } else {
            return getTradeOffAmount(null);
        }
    }


    @Override
    public int updateMemberAmount(BigDecimal amount, BigDecimal canAmount, Long userId) {
        int row = baseMapper.updateMemberAmount(amount, canAmount, userId);
        RedisUtils.del(RedisKeys.APP_MEMBER + userId);
        return row;
    }
}
