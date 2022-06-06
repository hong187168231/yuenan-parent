package com.indo.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.mapper.MemGoldChangeMapper;
import com.indo.core.mapper.MemTradingMapper;
import com.indo.core.pojo.bo.MemGoldInfoBO;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.MemGoldChange;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.core.util.BusinessRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
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
public class MemGoldChangeServiceImpl extends SuperServiceImpl<MemGoldChangeMapper, MemGoldChange> implements IMemGoldChangeService {


    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MemTradingMapper memTradingMapper;

    /**
     * 需要修改可提现金额的账变类型
     */
    private static final List<GoldchangeEnum> enumList = new ArrayList<>();

    static {
        enumList.add(GoldchangeEnum.CANCEL_BET);

    }


    private static final List<GoldchangeEnum> profitAndLossList = new LinkedList<>();

    private static final List<GoldchangeEnum> tzList = new LinkedList<>();
    private static final List<GoldchangeEnum> rechargeList = new LinkedList<>();
    private static final List<GoldchangeEnum> takeCashList = new LinkedList<>();


    static {
        // 投注
        tzList.add(GoldchangeEnum.CANCEL_BET);
        // 充值
        rechargeList.add(GoldchangeEnum.CZ);
        rechargeList.add(GoldchangeEnum.DSFZF);
        // 提现
        takeCashList.add(GoldchangeEnum.TXKK);

    }


    /**
     * 修改用户账变信息
     *
     * @param change 账变信息
     * @return
     */
    @Override
    public boolean updateMemGoldChange(MemGoldChangeDTO change) throws RuntimeException {
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
                MemGoldInfoBO memBaseinfo = memTradingMapper.findMemGoldInfo(userId);
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

                //默认金额
                BigDecimal defaultAmount = getTradeOffAmount(null);
                // 投注变动
                BigDecimal betAmount = defaultAmount;
                // 充值变动
                BigDecimal rechargeAmount = defaultAmount;
                // 提现变动
                BigDecimal cashAmount = defaultAmount;

                GoldchangeEnum goldchangeEnum = change.getGoldchangeEnum();
                if (tzList.contains(goldchangeEnum)) {
                    betAmount = betAmount.add(changeAmount);
                } else if (rechargeList.contains(goldchangeEnum)) {
                    rechargeAmount = rechargeAmount.add(changeAmount);
                } else if (takeCashList.contains(goldchangeEnum)) {
                    cashAmount = cashAmount.add(changeAmount);
                }

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
                    memGoldChange.setMemId(change.getUserId());
                    memGoldChange.setRefId(change.getRefId());
                    memGoldChange.setSerialNo(change.getSerialNo());
                    if (StringUtils.isBlank(change.getSerialNo())) {
                        memGoldChange.setSerialNo(change.getGoldchangeEnum().name() +
                                GeneratorIdUtil.generateId());
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
                int row = updateMemberAmount(changeAmount,
                        changeCanAmount,
                        betAmount,
                        rechargeAmount,
                        cashAmount,
                        memBaseinfo.getMemId());
                if (row != 1) {
                    log.error("{} updateUserBalance updateMemberAmount 更新余额失败. return:{}", userId, row);
                    throw new BizException("操作失败");
                }
                // 删除用户信息缓存
                BusinessRedisUtils.deleteMemBaseInfoByAccount(memBaseinfo.getAccount());
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
    private BigDecimal calculateChangeCanAmount(MemGoldChangeDTO change) {
        BigDecimal changeAmount = change.getChangeAmount();
        //  如果是收益且设置不需要打码
        if (change.getIncomeCodeFlag() == false || enumList.contains(change.getGoldchangeEnum())) {
            return changeAmount;
        } else {
            return getTradeOffAmount(null);
        }
    }


    public int updateMemberAmount(BigDecimal amount,
                                  BigDecimal canAmount,
                                  BigDecimal betAmount,
                                  BigDecimal rechargeAmount,
                                  BigDecimal cashAmount,
                                  Long userId) {
        int row = baseMapper.updateMemberAmount(amount, canAmount, betAmount, rechargeAmount, cashAmount, userId);
        RedisUtils.del(RedisKeys.APP_MEMBER + userId);
        return row;
    }

}
