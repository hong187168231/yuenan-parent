package com.indo.game.service.sbo.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.sbo.*;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.callback.sbo.SboCallBackCommResp;
import com.indo.game.pojo.vo.callback.sbo.SboCallBackGetBetStatusResp;
import com.indo.game.pojo.vo.callback.sbo.SboCallBackGetTransferStatusResp;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.sbo.SboCallbackService;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SboCallbackServiceImpl implements SboCallbackService {

    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;

    //取得用户的余额
    public Object getBalance(SboCallBackParentReq sboCallBackParentReq) {

        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackParentReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackParentReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            sboCallBackCommResp.setBalance(memBaseinfo.getBalance().toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");
        }

        return sboCallBackCommResp;
    }


    //扣除投注金额
    public Object deduct(SboCallBackDeductReq sboCallBackDeductReq) {
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackDeductReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackDeductReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            BigDecimal betAmount = BigDecimal.valueOf(Double.valueOf(sboCallBackDeductReq.getAmount()));
            BigDecimal balance = memBaseinfo.getBalance().subtract(betAmount);
            if(memBaseinfo.getBalance().compareTo(betAmount) == -1){
                sboCallBackCommResp.setErrorCode("5");
                sboCallBackCommResp.setErrorMessage("Not enough balance");
            }else {
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                sboCallBackCommResp.setBalance(balance.toString());
                sboCallBackCommResp.setErrorCode("0");
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                txns.setUserId(sboCallBackDeductReq.getUserName());
                txns.setBetAmount(betAmount);
                txns.setPlatformTxId(sboCallBackDeductReq.getTransferCode());
                txns.setRoundId(sboCallBackDeductReq.getTransactionId());
                txns.setBetTime(sboCallBackDeductReq.getBetTime());
                txns.setPlatform(sboCallBackDeductReq.getProductType());
                txns.setGameType(sboCallBackDeductReq.getGameType());
                txns.setBalance(balance);
                txns.setStatus("Running");
                txns.setMethod("Deduct");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
            }

        }

        return sboCallBackCommResp;

    }

    //结算投注
    public Object settle(SboCallBackSettleReq sboCallBackSettleReq) {
        //同一个赌注发出多次API请求,这意味着我们要求该次赌注重新结算

        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackSettleReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackSettleReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus,"Running");
            wrapper.eq(Txns::getPlatformTxId,sboCallBackSettleReq.getTransferCode());
            wrapper.eq(Txns::getUserId,sboCallBackSettleReq.getUserName());
            wrapper.eq(Txns::getPlatform,sboCallBackSettleReq.getProductType());
            wrapper.eq(Txns::getGameType,sboCallBackSettleReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);

            BigDecimal betAmount = oldTxns.getBetAmount();
            BigDecimal balance = BigDecimal.valueOf(0D);
            if ("2".equals(sboCallBackSettleReq.getResultType())){//平手
                balance = memBaseinfo.getBalance().add(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }
            if ("0".equals(sboCallBackSettleReq.getResultType())){//赢
                balance = memBaseinfo.getBalance().add(BigDecimal.valueOf(Double.valueOf(sboCallBackSettleReq.getWinloss())));
                gameCommonService.updateUserBalance(memBaseinfo, BigDecimal.valueOf(Double.valueOf(sboCallBackSettleReq.getWinloss())), GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }
            if ("1".equals(sboCallBackSettleReq.getResultType())){//输
                BigDecimal realBetAmount = BigDecimal.valueOf(Double.valueOf(sboCallBackSettleReq.getWinloss())).subtract(betAmount);
                balance = memBaseinfo.getBalance().subtract(realBetAmount);
                gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }

            sboCallBackCommResp.setBalance(balance.toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");

            Txns txns = new Txns();
            txns.setUserId(sboCallBackSettleReq.getUserName());
            txns.setBetAmount(oldTxns.getBetAmount());
            txns.setPlatformTxId(sboCallBackSettleReq.getTransferCode());
            txns.setBetTime(sboCallBackSettleReq.getResultTime());
            txns.setPlatform(sboCallBackSettleReq.getProductType());
            txns.setGameType(sboCallBackSettleReq.getGameType());
            txns.setResultType(sboCallBackSettleReq.getResultType());
            txns.setWinAmount(BigDecimal.valueOf(Double.valueOf(sboCallBackSettleReq.getWinloss())));
            txns.setBalance(balance);
            txns.setStatus("Settled");
            txns.setMethod("Settle");
            String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);
            oldTxns.setStatus("Settled");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }

        return sboCallBackCommResp;

    }

    //回滚
    public Object rollback(SboCallBackRollbackReq sboCallBackRollbackReq) {

        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackRollbackReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackRollbackReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus,"Settled");
            wrapper.eq(Txns::getMethod,"settle");
            wrapper.eq(Txns::getPlatformTxId,sboCallBackRollbackReq.getTransferCode());
            wrapper.eq(Txns::getUserId,sboCallBackRollbackReq.getUserName());
            wrapper.eq(Txns::getPlatform,sboCallBackRollbackReq.getProductType());
            wrapper.eq(Txns::getGameType,sboCallBackRollbackReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);

            BigDecimal betAmount = oldTxns.getBetAmount();
            BigDecimal balance = BigDecimal.valueOf(0D);
            if ("2".equals(oldTxns.getResultType())){//平手
                balance = memBaseinfo.getBalance().subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
            }
            if ("0".equals(oldTxns.getResultType())){//赢
                balance = memBaseinfo.getBalance().subtract(oldTxns.getWinAmount());
                gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getWinAmount(), GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
            }
            if ("1".equals(oldTxns.getResultType())){//输
                BigDecimal realBetAmount = oldTxns.getWinAmount().subtract(betAmount);
                balance = memBaseinfo.getBalance().add(realBetAmount);
                gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
            }

            sboCallBackCommResp.setBalance(balance.toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");

            Txns txns = new Txns();
            txns.setUserId(oldTxns.getUserId());
            txns.setBetAmount(oldTxns.getBetAmount());
            txns.setPlatformTxId(oldTxns.getPlatformTxId());
            txns.setBetTime(oldTxns.getBetTime());
            txns.setPlatform(oldTxns.getPlatform());
            txns.setGameType(oldTxns.getGameType());
            txns.setBalance(balance);
            txns.setStatus("Running");
            txns.setMethod("Rollback");
            String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);
            oldTxns.setStatus("Rollback");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }

        return sboCallBackCommResp;

    }

    //取消投注
    public Object cancel(SboCallBackCancelReq sboCallBackCancelReq) {

        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackCancelReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackCancelReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getStatus,"Settled").or().eq(Txns::getStatus,"Running"));
            wrapper.eq(Txns::getPlatformTxId,sboCallBackCancelReq.getTransferCode());
            wrapper.eq(Txns::getUserId,sboCallBackCancelReq.getUserName());
            wrapper.eq(Txns::getPlatform,sboCallBackCancelReq.getProductType());
            wrapper.eq(Txns::getGameType,sboCallBackCancelReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);

            BigDecimal betAmount = oldTxns.getBetAmount();
            BigDecimal balance = BigDecimal.valueOf(0D);
            if("Running".equals(oldTxns.getStatus())){
                balance = memBaseinfo.getBalance().add(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }else {
                if ("0".equals(oldTxns.getResultType())) {//赢
                    BigDecimal realBetAmount = oldTxns.getWinAmount().subtract(betAmount);
                    balance = memBaseinfo.getBalance().subtract(realBetAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
                }
                if ("1".equals(oldTxns.getResultType())) {//输
                    balance = memBaseinfo.getBalance().add(oldTxns.getWinAmount());
                    gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getWinAmount(), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                }
            }

            sboCallBackCommResp.setBalance(balance.toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns,txns);
            txns.setId(null);
            txns.setBalance(balance);
            txns.setStatus("Void");
            txns.setMethod("Cancel");
            String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);
            oldTxns.setStatus("Cancel");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }

        return sboCallBackCommResp;

    }

    //小费
    public Object tip(SboCallBackTipReq sboCallBackTipReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackTipReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackTipReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            BigDecimal betAmount = BigDecimal.valueOf(Double.valueOf(sboCallBackTipReq.getAmount()));
            BigDecimal balance = memBaseinfo.getBalance().subtract(betAmount);
            if(memBaseinfo.getBalance().compareTo(betAmount) == -1){
                sboCallBackCommResp.setErrorCode("5");
                sboCallBackCommResp.setErrorMessage("Not enough balance");
            }else {
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.TIP, TradingEnum.SPENDING);
                sboCallBackCommResp.setBalance(balance.toString());
                sboCallBackCommResp.setErrorCode("0");
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                txns.setUserId(sboCallBackTipReq.getUserName());
                txns.setBetAmount(betAmount);
                txns.setPlatformTxId(sboCallBackTipReq.getTransferCode());
                txns.setBetTime(sboCallBackTipReq.getTipTime());
                txns.setPlatform(sboCallBackTipReq.getProductType());
                txns.setGameType(sboCallBackTipReq.getGameType());
                txns.setBalance(balance);
                txns.setStatus("Tip");
                txns.setMethod("Tip");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
            }

        }

        return sboCallBackCommResp;
    }

    //红利
    public Object bonus(SboCallBackBonusReq sboCallBackBonusReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackBonusReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackBonusReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            BigDecimal betAmount = BigDecimal.valueOf(Double.valueOf(sboCallBackBonusReq.getAmount()));
            BigDecimal balance = memBaseinfo.getBalance().add(betAmount);
            if(memBaseinfo.getBalance().compareTo(betAmount) == -1){
                sboCallBackCommResp.setErrorCode("5");
                sboCallBackCommResp.setErrorMessage("Not enough balance");
            }else {
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
                sboCallBackCommResp.setBalance(balance.toString());
                sboCallBackCommResp.setErrorCode("0");
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                txns.setUserId(sboCallBackBonusReq.getUserName());
                txns.setBetAmount(betAmount);
                txns.setPlatformTxId(sboCallBackBonusReq.getTransferCode());
                txns.setBetTime(sboCallBackBonusReq.getBonusTime());
                txns.setPlatform(sboCallBackBonusReq.getProductType());
                txns.setGameType(sboCallBackBonusReq.getGameType());
                txns.setBalance(balance);
                txns.setStatus("Bonus");
                txns.setMethod("Bonus");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
            }

        }

        return sboCallBackCommResp;
    }

    //归还注额
    public Object returnStake(SboCallBackReturnStakeReq sboCallBackBonusReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackBonusReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackBonusReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus,"Running");
            wrapper.eq(Txns::getPlatformTxId,sboCallBackBonusReq.getTransferCode());
            wrapper.eq(Txns::getUserId,sboCallBackBonusReq.getUserName());
            wrapper.eq(Txns::getPlatform,sboCallBackBonusReq.getProductType());
            wrapper.eq(Txns::getGameType,sboCallBackBonusReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);

            BigDecimal betAmount = oldTxns.getBetAmount();
            BigDecimal balance = BigDecimal.valueOf(0D);
            //真实返还金额
            BigDecimal realWinAmount = betAmount.subtract(BigDecimal.valueOf(Double.valueOf(sboCallBackBonusReq.getCurrentStake())));
            balance = memBaseinfo.getBalance().add(realWinAmount);
            gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.REFUND, TradingEnum.INCOME);

            sboCallBackCommResp.setBalance(balance.toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");

            Txns txns = new Txns();
            txns.setUserId(sboCallBackBonusReq.getUserName());
            txns.setBetAmount(oldTxns.getBetAmount());
            txns.setPlatformTxId(sboCallBackBonusReq.getTransferCode());
            txns.setBetTime(sboCallBackBonusReq.getReturnStakeTime());
            txns.setPlatform(sboCallBackBonusReq.getProductType());
            txns.setGameType(sboCallBackBonusReq.getGameType());
            txns.setWinAmount(BigDecimal.valueOf(Double.valueOf(sboCallBackBonusReq.getCurrentStake())));
            txns.setBalance(balance);
            txns.setStatus("Running");
            txns.setMethod("ReturnStake");
            String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);
            oldTxns.setStatus("ReturnStake");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }

        return sboCallBackCommResp;
    }

    //取得投注状态
    public Object getBetStatus(SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackGetBetStatusReq.getUserName());
        SboCallBackGetBetStatusResp sboCallBackGetBetStatusResp = new SboCallBackGetBetStatusResp();
        sboCallBackGetBetStatusResp.setTransactionId(sboCallBackGetBetStatusReq.getTransactionId());
        sboCallBackGetBetStatusResp.setTransferCode(sboCallBackGetBetStatusReq.getTransferCode());
        if (null == memBaseinfo) {
            sboCallBackGetBetStatusResp.setErrorCode("1");
            sboCallBackGetBetStatusResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getStatus,"Settled").or().eq(Txns::getStatus,"Running"));
            wrapper.eq(Txns::getPlatformTxId,sboCallBackGetBetStatusReq.getTransferCode());
            wrapper.eq(Txns::getUserId,sboCallBackGetBetStatusReq.getUserName());
            wrapper.eq(Txns::getPlatform,sboCallBackGetBetStatusReq.getProductType());
            wrapper.eq(Txns::getGameType,sboCallBackGetBetStatusReq.getGameType());
            wrapper.eq(Txns::getRoundId,sboCallBackGetBetStatusReq.getTransactionId());
            Txns oldTxns = txnsMapper.selectOne(wrapper);

            if(null==oldTxns){
                sboCallBackGetBetStatusResp.setErrorCode("6");
                sboCallBackGetBetStatusResp.setErrorMessage("Member not exist");
            }else {
                sboCallBackGetBetStatusResp.setStatus(oldTxns.getStatus());
                sboCallBackGetBetStatusResp.setStake(null==oldTxns.getBetAmount()?"":oldTxns.getBetAmount().toString());
                sboCallBackGetBetStatusResp.setWinloss(null==oldTxns.getWinAmount()?"":oldTxns.getWinAmount().toString());
                sboCallBackGetBetStatusResp.setErrorCode("0");
                sboCallBackGetBetStatusResp.setErrorMessage("No Error");
            }
        }

        return sboCallBackGetBetStatusResp;
    }

    //转帐交易
    public Object transfer(SboCallBackTransferReq sboCallBackTransferReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackTransferReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackTransferReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus,"Transfer");
            wrapper.eq(Txns::getPlatformTxId, sboCallBackTransferReq.getTransferRefno());
            wrapper.eq(Txns::getRoundId, sboCallBackTransferReq.getGpid());
            wrapper.eq(Txns::getUserId, sboCallBackTransferReq.getUserName());
            wrapper.eq(Txns::getPlatform, sboCallBackTransferReq.getProductType());
            wrapper.eq(Txns::getGameType, sboCallBackTransferReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            BigDecimal balance = BigDecimal.valueOf(0D);
            if(null==oldTxns){
                BigDecimal amount = BigDecimal.valueOf(Double.valueOf(sboCallBackTransferReq.getAmount()));
                if("131".equals(sboCallBackTransferReq.getTransferType())){//131转入
                    balance = memBaseinfo.getBalance().add(amount);
                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
                }else {//130转出
                    if(memBaseinfo.getBalance().compareTo(amount) == -1){
                        sboCallBackCommResp.setBalance(null==memBaseinfo.getBalance()?"":memBaseinfo.getBalance().toString());
                        sboCallBackCommResp.setErrorCode("5");
                        sboCallBackCommResp.setErrorMessage("Not enough balance");
                        return sboCallBackCommResp;
                    }else {
                        balance = memBaseinfo.getBalance().subtract(amount);
                        gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
                    }
                }

                Txns txns = new Txns();
                txns.setUserId(sboCallBackTransferReq.getUserName());
                txns.setBetAmount(amount);
                txns.setPlatformTxId(sboCallBackTransferReq.getTransferRefno());
                txns.setRoundId(sboCallBackTransferReq.getGpid());
                txns.setBetTime(sboCallBackTransferReq.getTransferTime());
                txns.setPlatform(sboCallBackTransferReq.getProductType());
                txns.setGameType(sboCallBackTransferReq.getGameType());
                txns.setBalance(balance);
//                txns.setTransferType(sboCallBackTransferReq.getTransferType());
                txns.setStatus("Transfer");
                txns.setMethod("Transfer");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);

                sboCallBackCommResp.setBalance(balance.toString());
                sboCallBackCommResp.setErrorCode("0");
                sboCallBackCommResp.setErrorMessage("No Error");
            }else {
                sboCallBackCommResp.setBalance(null==memBaseinfo.getBalance()?"":memBaseinfo.getBalance().toString());
                sboCallBackCommResp.setErrorCode("6");
                sboCallBackCommResp.setErrorMessage("Member not exist");
            }
        }

        return sboCallBackCommResp;
    }

    //转帐交易回滚
    public Object rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackRollbackTransferReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackRollbackTransferReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getPlatformTxId, sboCallBackRollbackTransferReq.getTransferRefno());
            wrapper.eq(Txns::getRoundId, sboCallBackRollbackTransferReq.getGpid());
            wrapper.eq(Txns::getStatus,"Transfer");
            wrapper.eq(Txns::getUserId, sboCallBackRollbackTransferReq.getUserName());
            wrapper.eq(Txns::getPlatform, sboCallBackRollbackTransferReq.getProductType());
            wrapper.eq(Txns::getGameType, sboCallBackRollbackTransferReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            BigDecimal balance = BigDecimal.valueOf(0D);
            if(null!=oldTxns){
                BigDecimal amount = oldTxns.getBetAmount();
//                if("131".equals(oldTxns.getTransferType())){//131转入
//                    balance = memBaseinfo.getBalance().subtract(amount);
//                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
//
//                }else {//130转出
//                    balance = memBaseinfo.getBalance().add(amount);
//                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
//                }

                Txns txns = new Txns();
                txns.setUserId(sboCallBackRollbackTransferReq.getUserName());
                txns.setBetAmount(amount);
                txns.setPlatformTxId(sboCallBackRollbackTransferReq.getTransferRefno());
                txns.setRoundId(sboCallBackRollbackTransferReq.getGpid());
                txns.setBetTime(oldTxns.getBetTime());
                txns.setPlatform(sboCallBackRollbackTransferReq.getProductType());
                txns.setGameType(sboCallBackRollbackTransferReq.getGameType());
                txns.setBalance(balance);
                txns.setStatus("RollbackTransfer");
                txns.setMethod("RollbackTransfer");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("RollbackTransfer");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);

                sboCallBackCommResp.setBalance(balance.toString());
                sboCallBackCommResp.setErrorCode("0");
                sboCallBackCommResp.setErrorMessage("No Error");
            }else {
                sboCallBackCommResp.setBalance(null==memBaseinfo.getBalance()?"":memBaseinfo.getBalance().toString());
                sboCallBackCommResp.setErrorCode("6");
                sboCallBackCommResp.setErrorMessage("Member not exist");
            }
        }

        return sboCallBackCommResp;
    }

    //取得转帐交易状态
    public Object getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackGetTransferStautsReq.getUserName());
        SboCallBackGetTransferStatusResp sboCallBackGetTransferStatusResp = new SboCallBackGetTransferStatusResp();
        sboCallBackGetTransferStatusResp.setAccountName(sboCallBackGetTransferStautsReq.getUserName());
        sboCallBackGetTransferStatusResp.setBalance(null==memBaseinfo.getBalance()?"":memBaseinfo.getBalance().toString());
        if (null == memBaseinfo) {
            sboCallBackGetTransferStatusResp.setErrorCode("1");
            sboCallBackGetTransferStatusResp.setErrorMessage("Member not exist");
        }else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getPlatformTxId, sboCallBackGetTransferStautsReq.getTransferRefno());
            wrapper.eq(Txns::getRoundId, sboCallBackGetTransferStautsReq.getGpid());
            wrapper.eq(Txns::getStatus,"Transfer");
            wrapper.eq(Txns::getUserId, sboCallBackGetTransferStautsReq.getUserName());
            wrapper.eq(Txns::getPlatform, sboCallBackGetTransferStautsReq.getProductType());
            wrapper.eq(Txns::getGameType, sboCallBackGetTransferStautsReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null!=oldTxns){
                sboCallBackGetTransferStatusResp.setTransferStatus("2");
                sboCallBackGetTransferStatusResp.setErrorCode("0");
                sboCallBackGetTransferStatusResp.setAmount(null==oldTxns.getBetAmount()?"":oldTxns.getBetAmount().toString());
                sboCallBackGetTransferStatusResp.setErrorMessage("No Error");
            }else {
                sboCallBackGetTransferStatusResp.setTransferStatus("1");
                sboCallBackGetTransferStatusResp.setErrorCode("0");
                sboCallBackGetTransferStatusResp.setErrorMessage("No Error");
            }
        }

        return sboCallBackGetTransferStatusResp;
    }

    //LiveCoin購買
    public Object liveCoinTransaction(SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq){
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(sboCallBackLiveCoinTransactionReq.getUserName());
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackLiveCoinTransactionReq.getUserName());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode("1");
            sboCallBackCommResp.setErrorMessage("Member not exist");
        }else {
            BigDecimal betAmount = BigDecimal.valueOf(Double.valueOf(sboCallBackLiveCoinTransactionReq.getAmount()));
            BigDecimal balance = memBaseinfo.getBalance().subtract(betAmount);
            if(memBaseinfo.getBalance().compareTo(betAmount) == -1){
                sboCallBackCommResp.setErrorCode("5");
                sboCallBackCommResp.setErrorMessage("Not enough balance");
            }else {
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);
                sboCallBackCommResp.setBalance(balance.toString());
                sboCallBackCommResp.setErrorCode("0");
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                txns.setUserId(sboCallBackLiveCoinTransactionReq.getUserName());
                txns.setBetAmount(betAmount);
                txns.setPlatformTxId(sboCallBackLiveCoinTransactionReq.getTransferCode());
                txns.setRoundId(sboCallBackLiveCoinTransactionReq.getTransactionId());
                txns.setBetTime(sboCallBackLiveCoinTransactionReq.getTranscationTime());
                txns.setPlatform(sboCallBackLiveCoinTransactionReq.getProductType());
                txns.setGameType(sboCallBackLiveCoinTransactionReq.getGameType());
                txns.setBalance(balance);
                txns.setStatus("LiveCoin");
                txns.setMethod("LiveCoin");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
            }

        }

        return sboCallBackCommResp;
    }
}
