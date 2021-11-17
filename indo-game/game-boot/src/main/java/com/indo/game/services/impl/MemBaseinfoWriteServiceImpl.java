package com.indo.game.services.impl;

import com.indo.common.exception.BadRequestException;
import com.indo.game.game.RedisLock;
import com.indo.game.common.constant.Constants;
import com.indo.game.common.enums.GoldchangeEnum;
import com.indo.game.pojo.dto.MemGoldchangeDO;
import com.indo.game.pojo.entity.MemBaseinfo;
import com.indo.game.pojo.entity.MemBaseinfoExample;
import com.indo.game.pojo.entity.MemGoldchange;
import com.indo.game.services.MemBaseinfoService;
import com.indo.game.services.MemBaseinfoWriteService;
import com.indo.game.services.MemGoldchangeService;
import com.indo.game.utils.JsonUtil;
import com.indo.game.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static com.indo.game.utils.ViewUtil.getTradeOffAmount;

/**
 * @author lzy
 * @create 2018-08-27 9:52
 **/
@Service
public class MemBaseinfoWriteServiceImpl implements MemBaseinfoWriteService {
    private static final Logger logger = LoggerFactory.getLogger(MemBaseinfoWriteServiceImpl.class);
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private MemBaseinfoService memBaseinfoService;
    @Autowired
    private MemGoldchangeService memGoldchangeService;

    /**
     * 修改用户余额
     *
     * @param change 改变记录
     */
    @Override
    public boolean updateUserBalance(MemGoldchangeDO change) throws RuntimeException {
        if (ObjectUtils.isEmpty(change)) {
            logger.error("{}.updateUserBalance 参数为空", getClass().getName());
            throw new BadRequestException("请求参数错误");
        }
        if ((change.getUserId() == null || change.getUserId() < 1) && StringUtils.isBlank(change.getAccount())) {
            logger.error("{}.updateUserBalance 参数不正确：{}", getClass().getName(), JsonUtil.toJson(change));
            throw new BadRequestException("请求参数错误");
        }
        if (change.getUserId() == null || change.getUserId() < 1) {
            MemBaseinfo m = memBaseinfoService.getUserByAccno(change.getAccount());
            if (ObjectUtils.isEmpty(m)) {
                logger.error("{}.updateUserBalance 对应用户不存在：{}", getClass().getName(), JsonUtil.toJson(change));
                throw new BadRequestException("对应用户不存在");
            }
            change.setUserId(m.getMemid().intValue());
        }

        logger.info("{} updateUserBalance change info:{}", change.getUserId(), change);
        String userId = Integer.toString(change.getUserId());
        long begin = System.currentTimeMillis();
        RReadWriteLock lock = redissonClient.getReadWriteLock(RedisLock.UPDATE_USER_BALANCE_ + userId);

        try {
            // 写锁（等待时间10s，超时时间10S[自动解锁]，单位：秒）【设定超时时间，超时后自动释放锁，防止死锁】
            boolean bool = lock.writeLock().tryLock(100, 20, TimeUnit.SECONDS);
            // 判断是否获取到锁
            if (bool) {
                begin = System.currentTimeMillis();
                logger.info("用户修改余额拿到锁{}", userId);
                MemBaseinfo memBaseinfo = memBaseinfoService.selectByPrimaryKey(Long.valueOf(change.getUserId()));
                change.setAccno(memBaseinfo.getAccno());
                if (memBaseinfo == null) {
                    logger.info("{} updateUserBalance member is null,memBaseinfo: {}", userId, memBaseinfo);
                    throw new BadRequestException("用户不存在");
                }
                // 获取变动金额（余额）
                BigDecimal currentBalance = memBaseinfo.getGoldnum();
                BigDecimal changeMoney = change.getQuantity() == null ? BigDecimal.ZERO : change.getQuantity();
                BigDecimal newBalance = currentBalance.add(changeMoney);
                // 获取变动记录值（只用户记录账变记录）
                BigDecimal showChange = change.getShowChange();
                showChange = showChange == null ? changeMoney : showChange;
                // 余额变动
                BigDecimal amount = changeMoney;
                //默认金额
                BigDecimal defaultAmount = getTradeOffAmount(null);
                // 待开奖金额 变动
                BigDecimal waitAmount = defaultAmount;
                // 投注变动
                BigDecimal bamount = defaultAmount;
                // 不可提变动
                BigDecimal namount = defaultAmount;
                // 提现变动
                BigDecimal wamount = defaultAmount;
                // 充值变动
                BigDecimal pamount = defaultAmount;
                // 充值附送变动
                BigDecimal giveGold = defaultAmount;

                if (change.getNoWithdrawalAmount() != null && change.getNoWithdrawalAmount().compareTo(BigDecimal.ZERO) != 0) {
                    namount = getTradeOffAmount(change.getNoWithdrawalAmount());
                }
                // 修改累计充值金额
                if (change.getPayAmount() != null && change.getPayAmount().compareTo(BigDecimal.ZERO) != 0) {
                    pamount = getTradeOffAmount(change.getPayAmount());
                }
                // 修改累计投注金额
                if (change.getBetAmount() != null && change.getBetAmount().compareTo(BigDecimal.ZERO) != 0) {
                    bamount = getTradeOffAmount(change.getBetAmount());
                }
                // 修改累计提现金额
                BigDecimal withdrawalAmount = change.getWithdrawalAmount();
                if (withdrawalAmount != null && withdrawalAmount.compareTo(BigDecimal.ZERO) != 0) {
                    wamount = getTradeOffAmount(withdrawalAmount);
                }
                waitAmount = getTradeOffAmount(change.getWaitAmount());
                if (waitAmount == null) {
                    // 待开奖金额 变动
                    waitAmount = getTradeOffAmount(null);
                }
                // 修改余额 如果为加减打码量，则 账变金额为0
                if (showChange.compareTo(BigDecimal.ZERO) != 0 || GoldchangeEnum.getThreeBetList().contains(change.getChangetype())) {
                    // 添加账变记录
                    MemGoldchange c = new MemGoldchange();
                    BeanUtils.copyProperties(change, c);
                    c.setQuantity(getTradeOffAmount(amount.multiply(new BigDecimal(Constants.CHONGZHIBILIE))));
                    c.setGoldnum(getTradeOffAmount(currentBalance));
                    c.setRecgoldnum(getTradeOffAmount(newBalance));
                    if (memBaseinfo.getNoWithdrawalAmount().compareTo(BigDecimal.ZERO) < 0) {
                        c.setPreCgdml(getTradeOffAmount(null));
                    } else {
                        c.setPreCgdml(getTradeOffAmount(memBaseinfo.getNoWithdrawalAmount()));
                    }
                    BigDecimal aftdml = memBaseinfo.getNoWithdrawalAmount().add(namount).compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : memBaseinfo.getNoWithdrawalAmount().add(namount);
                    c.setAfterCgdml(getTradeOffAmount(aftdml));
                    c.setSnowSn(SnowflakeIdWorker.generateId());
                    begin = System.currentTimeMillis();
                    memGoldchangeService.insertSelective(c);
                    logger.info("{} updateUserBalance memGoldchangeMapper.insertSelective 充值帐变 耗时 time, {}", change.getUserId(), System.currentTimeMillis() - begin);
                }
                //过滤投注打码两字段不更新，在结算时再去更新
                if (GoldchangeEnum.LIVEROOM_BET.getValue().equals(change.getChangetype())
                        || GoldchangeEnum.LOTTERY_BETTING.getValue().equals(change.getChangetype())) {
                    namount = getTradeOffAmount(null);
                }
                // 获取打码量
                BigDecimal consumeAcmount = getTradeOffAmount(caclConsumeAmount(namount, change.getChangetype()));
                calcRechargeInfo(memBaseinfo.getAccno(), amount, change.getChangetype());
                int i = memBaseinfoService.updateMemberAmount(amount, pamount, bamount, namount, consumeAcmount, wamount, waitAmount, memBaseinfo.getAccno(), memBaseinfo.getMemid());
                if (i != 1) {
                    logger.error("{} updateUserBalance updateMemberAmount 更新余额失败. return:{}", change.getUserId(), i);
                    throw new BadRequestException("操作失败");
                }
                logger.info("{} updateUserBalance updateMemberAmount 耗时 time, {}", change.getUserId(), System.currentTimeMillis() - begin);
                return true;
            } else {
                logger.error("{} updateUserBalance 用户修改余额没拿到锁, 记录修改余额变动对象 {}", userId, change);
                throw new BadRequestException("操作失败");
            }
        } catch (Exception e) {
            logger.error("{} updateUserBalance occur error. change info:{}", change.getUserId(), change, e);
            throw new RuntimeException("用户修改余额出错", e.getCause());
        } finally {
            // 释放锁
            lock.writeLock().unlock();
            logger.info("{} updateUserBalance 用户修改余额释放锁", userId);
        }
    }

    /**
     * 根据不可提变动金额和帐变类型，计算 打码量(即消费金额)
     *
     * @param namount    不可提金额
     * @param changeType 帐变类型
     * @return 打码量
     */
    private BigDecimal caclConsumeAmount(BigDecimal namount, Integer changeType) {
        if (namount == null || namount.compareTo(BigDecimal.ZERO) != -1) {
            return BigDecimal.ZERO;
        }
        if (changeType.equals(GoldchangeEnum.MANUALLY_SUB_DAMALIANG.getValue())) {
            return BigDecimal.ZERO;
        }
        return namount.multiply(BigDecimal.valueOf(-1));
    }

    /**
     * 处理充值、代理充值信息 首充，充值次数，最大充值金额
     *
     * @param accno
     * @param tradeOffAmount
     */
    private void calcRechargeInfo(String accno, BigDecimal tradeOffAmount, Integer changeType) {
        if (changeType == null || (!changeType.equals(GoldchangeEnum.RECHARGE.getValue()) && !changeType.equals(GoldchangeEnum.REPAY_INCOME_ORDER.getValue()))) {
            return;
        }
        MemBaseinfoExample membaseinfoExample = new MemBaseinfoExample();
        membaseinfoExample.createCriteria().andAccnoEqualTo(accno);
        MemBaseinfo membaseinfo = memBaseinfoService.selectOneByExample(membaseinfoExample);
        if (membaseinfo.getPayFirst() == null || membaseinfo.getPayFirst().compareTo(BigDecimal.ZERO) == 0) {
            membaseinfo.setPayFirst(tradeOffAmount);
        }
        if (membaseinfo.getPayMax() == null || membaseinfo.getPayMax().compareTo(tradeOffAmount) == -1) {
            membaseinfo.setPayMax(tradeOffAmount);
        }
        membaseinfo.setPayNum(membaseinfo.getPayNum() == null ? 0 : membaseinfo.getPayNum() + 1);
        memBaseinfoService.updateByPrimaryKeySelective(membaseinfo);
    }
}
