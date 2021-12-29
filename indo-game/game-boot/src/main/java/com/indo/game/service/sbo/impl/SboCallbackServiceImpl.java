package com.indo.game.service.sbo.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.game.mapper.sbo.SboOperInfoMapper;
import com.indo.game.pojo.entity.sbo.*;
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
    private SboOperInfoMapper sboOperInfoMapper;

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

                SboOperInfo sboOperInfo = new SboOperInfo();
                sboOperInfo.setUserName(sboCallBackDeductReq.getUserName());
                sboOperInfo.setBetAmount(betAmount);
                sboOperInfo.setTransferCode(sboCallBackDeductReq.getTransferCode());
                sboOperInfo.setTransactionId(sboCallBackDeductReq.getTransactionId());
                sboOperInfo.setResultTime(sboCallBackDeductReq.getBetTime());
                sboOperInfo.setProductType(sboCallBackDeductReq.getProductType());
                sboOperInfo.setGameType(sboCallBackDeductReq.getGameType());
                sboOperInfo.setBalance(balance);
                sboOperInfo.setStatus("Running");
                sboOperInfo.setOperType("Deduct");
                sboOperInfo.setCreateTime(new Date());
                sboOperInfoMapper.insert(sboOperInfo);
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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SboOperInfo::getStatus,"Running");
            wrapper.eq(SboOperInfo::getTransferCode,sboCallBackSettleReq.getTransferCode());
            wrapper.eq(SboOperInfo::getUserName,sboCallBackSettleReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType,sboCallBackSettleReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType,sboCallBackSettleReq.getGameType());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);

            BigDecimal betAmount = oldSboOperInfo.getBetAmount();
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

            SboOperInfo sboOperInfo = new SboOperInfo();
            sboOperInfo.setUserName(sboCallBackSettleReq.getUserName());
            sboOperInfo.setBetAmount(oldSboOperInfo.getBetAmount());
            sboOperInfo.setTransferCode(sboCallBackSettleReq.getTransferCode());
            sboOperInfo.setResultTime(sboCallBackSettleReq.getResultTime());
            sboOperInfo.setProductType(sboCallBackSettleReq.getProductType());
            sboOperInfo.setGameType(sboCallBackSettleReq.getGameType());
            sboOperInfo.setResultType(sboCallBackSettleReq.getResultType());
            sboOperInfo.setWinloss(BigDecimal.valueOf(Double.valueOf(sboCallBackSettleReq.getWinloss())));
            sboOperInfo.setBalance(balance);
            sboOperInfo.setStatus("Settled");
            sboOperInfo.setOperType("Settle");
            sboOperInfo.setCreateTime(new Date());
            sboOperInfoMapper.insert(sboOperInfo);
            oldSboOperInfo.setStatus("Settled");
            oldSboOperInfo.setUpdateTime(new Date());
            sboOperInfoMapper.updateById(oldSboOperInfo);
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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SboOperInfo::getStatus,"Settled");
            wrapper.eq(SboOperInfo::getOperType,"settle");
            wrapper.eq(SboOperInfo::getTransferCode,sboCallBackRollbackReq.getTransferCode());
            wrapper.eq(SboOperInfo::getUserName,sboCallBackRollbackReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType,sboCallBackRollbackReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType,sboCallBackRollbackReq.getGameType());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);

            BigDecimal betAmount = oldSboOperInfo.getBetAmount();
            BigDecimal balance = BigDecimal.valueOf(0D);
            if ("2".equals(oldSboOperInfo.getResultType())){//平手
                balance = memBaseinfo.getBalance().subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
            }
            if ("0".equals(oldSboOperInfo.getResultType())){//赢
                balance = memBaseinfo.getBalance().subtract(oldSboOperInfo.getWinloss());
                gameCommonService.updateUserBalance(memBaseinfo, oldSboOperInfo.getWinloss(), GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
            }
            if ("1".equals(oldSboOperInfo.getResultType())){//输
                BigDecimal realBetAmount = oldSboOperInfo.getWinloss().subtract(betAmount);
                balance = memBaseinfo.getBalance().add(realBetAmount);
                gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
            }

            sboCallBackCommResp.setBalance(balance.toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");

            SboOperInfo sboOperInfo = new SboOperInfo();
            sboOperInfo.setUserName(oldSboOperInfo.getUserName());
            sboOperInfo.setBetAmount(oldSboOperInfo.getBetAmount());
            sboOperInfo.setTransferCode(oldSboOperInfo.getTransferCode());
            sboOperInfo.setResultTime(oldSboOperInfo.getResultTime());
            sboOperInfo.setProductType(oldSboOperInfo.getProductType());
            sboOperInfo.setGameType(oldSboOperInfo.getGameType());
            sboOperInfo.setBalance(balance);
            sboOperInfo.setStatus("Running");
            sboOperInfo.setOperType("Rollback");
            sboOperInfo.setCreateTime(new Date());
            sboOperInfoMapper.insert(sboOperInfo);
            oldSboOperInfo.setStatus("Rollback");
            oldSboOperInfo.setUpdateTime(new Date());
            sboOperInfoMapper.updateById(oldSboOperInfo);
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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(SboOperInfo::getStatus,"Settled").or().eq(SboOperInfo::getStatus,"Running"));
            wrapper.eq(SboOperInfo::getTransferCode,sboCallBackCancelReq.getTransferCode());
            wrapper.eq(SboOperInfo::getUserName,sboCallBackCancelReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType,sboCallBackCancelReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType,sboCallBackCancelReq.getGameType());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);

            BigDecimal betAmount = oldSboOperInfo.getBetAmount();
            BigDecimal balance = BigDecimal.valueOf(0D);
            if("Running".equals(oldSboOperInfo.getStatus())){
                balance = memBaseinfo.getBalance().add(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }else {
                if ("0".equals(oldSboOperInfo.getResultType())) {//赢
                    BigDecimal realBetAmount = oldSboOperInfo.getWinloss().subtract(betAmount);
                    balance = memBaseinfo.getBalance().subtract(realBetAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
                }
                if ("1".equals(oldSboOperInfo.getResultType())) {//输
                    balance = memBaseinfo.getBalance().add(oldSboOperInfo.getWinloss());
                    gameCommonService.updateUserBalance(memBaseinfo, oldSboOperInfo.getWinloss(), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                }
            }

            sboCallBackCommResp.setBalance(balance.toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");

            SboOperInfo sboOperInfo = new SboOperInfo();
            BeanUtils.copyProperties(oldSboOperInfo,sboOperInfo);
            sboOperInfo.setId(null);
            sboOperInfo.setBalance(balance);
            sboOperInfo.setStatus("Void");
            sboOperInfo.setOperType("Cancel");
            sboOperInfo.setCreateTime(new Date());
            sboOperInfoMapper.insert(sboOperInfo);
            oldSboOperInfo.setStatus("Cancel");
            oldSboOperInfo.setUpdateTime(new Date());
            sboOperInfoMapper.updateById(oldSboOperInfo);
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

                SboOperInfo sboOperInfo = new SboOperInfo();
                sboOperInfo.setUserName(sboCallBackTipReq.getUserName());
                sboOperInfo.setBetAmount(betAmount);
                sboOperInfo.setTransferCode(sboCallBackTipReq.getTransferCode());
                sboOperInfo.setResultTime(sboCallBackTipReq.getTipTime());
                sboOperInfo.setProductType(sboCallBackTipReq.getProductType());
                sboOperInfo.setGameType(sboCallBackTipReq.getGameType());
                sboOperInfo.setBalance(balance);
                sboOperInfo.setStatus("Tip");
                sboOperInfo.setOperType("Tip");
                sboOperInfo.setCreateTime(new Date());
                sboOperInfoMapper.insert(sboOperInfo);
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

                SboOperInfo sboOperInfo = new SboOperInfo();
                sboOperInfo.setUserName(sboCallBackBonusReq.getUserName());
                sboOperInfo.setBetAmount(betAmount);
                sboOperInfo.setTransferCode(sboCallBackBonusReq.getTransferCode());
                sboOperInfo.setResultTime(sboCallBackBonusReq.getBonusTime());
                sboOperInfo.setProductType(sboCallBackBonusReq.getProductType());
                sboOperInfo.setGameType(sboCallBackBonusReq.getGameType());
                sboOperInfo.setBalance(balance);
                sboOperInfo.setStatus("Bonus");
                sboOperInfo.setOperType("Bonus");
                sboOperInfo.setCreateTime(new Date());
                sboOperInfoMapper.insert(sboOperInfo);
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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SboOperInfo::getStatus,"Running");
            wrapper.eq(SboOperInfo::getTransferCode,sboCallBackBonusReq.getTransferCode());
            wrapper.eq(SboOperInfo::getUserName,sboCallBackBonusReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType,sboCallBackBonusReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType,sboCallBackBonusReq.getGameType());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);

            BigDecimal betAmount = oldSboOperInfo.getBetAmount();
            BigDecimal balance = BigDecimal.valueOf(0D);
            //真实返还金额
            BigDecimal realWinAmount = betAmount.subtract(BigDecimal.valueOf(Double.valueOf(sboCallBackBonusReq.getCurrentStake())));
            balance = memBaseinfo.getBalance().add(realWinAmount);
            gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.REFUND, TradingEnum.INCOME);

            sboCallBackCommResp.setBalance(balance.toString());
            sboCallBackCommResp.setErrorCode("0");
            sboCallBackCommResp.setErrorMessage("No Error");

            SboOperInfo sboOperInfo = new SboOperInfo();
            sboOperInfo.setUserName(sboCallBackBonusReq.getUserName());
            sboOperInfo.setBetAmount(oldSboOperInfo.getBetAmount());
            sboOperInfo.setTransferCode(sboCallBackBonusReq.getTransferCode());
            sboOperInfo.setResultTime(sboCallBackBonusReq.getReturnStakeTime());
            sboOperInfo.setProductType(sboCallBackBonusReq.getProductType());
            sboOperInfo.setGameType(sboCallBackBonusReq.getGameType());
            sboOperInfo.setWinloss(BigDecimal.valueOf(Double.valueOf(sboCallBackBonusReq.getCurrentStake())));
            sboOperInfo.setBalance(balance);
            sboOperInfo.setStatus("Running");
            sboOperInfo.setOperType("ReturnStake");
            sboOperInfo.setCreateTime(new Date());
            sboOperInfoMapper.insert(sboOperInfo);
            oldSboOperInfo.setStatus("ReturnStake");
            oldSboOperInfo.setUpdateTime(new Date());
            sboOperInfoMapper.updateById(oldSboOperInfo);
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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(SboOperInfo::getStatus,"Settled").or().eq(SboOperInfo::getStatus,"Running"));
            wrapper.eq(SboOperInfo::getTransferCode,sboCallBackGetBetStatusReq.getTransferCode());
            wrapper.eq(SboOperInfo::getUserName,sboCallBackGetBetStatusReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType,sboCallBackGetBetStatusReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType,sboCallBackGetBetStatusReq.getGameType());
            wrapper.eq(SboOperInfo::getTransactionId,sboCallBackGetBetStatusReq.getTransactionId());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);

            if(null==oldSboOperInfo){
                sboCallBackGetBetStatusResp.setErrorCode("6");
                sboCallBackGetBetStatusResp.setErrorMessage("Member not exist");
            }else {
                sboCallBackGetBetStatusResp.setStatus(oldSboOperInfo.getStatus());
                sboCallBackGetBetStatusResp.setStake(null==oldSboOperInfo.getBetAmount()?"":oldSboOperInfo.getBetAmount().toString());
                sboCallBackGetBetStatusResp.setWinloss(null==oldSboOperInfo.getWinloss()?"":oldSboOperInfo.getWinloss().toString());
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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SboOperInfo::getStatus,"Transfer");
            wrapper.eq(SboOperInfo::getTransferCode, sboCallBackTransferReq.getTransferRefno());
            wrapper.eq(SboOperInfo::getTransactionId, sboCallBackTransferReq.getGpid());
            wrapper.eq(SboOperInfo::getUserName, sboCallBackTransferReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType, sboCallBackTransferReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType, sboCallBackTransferReq.getGameType());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);
            BigDecimal balance = BigDecimal.valueOf(0D);
            if(null==oldSboOperInfo){
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

                SboOperInfo sboOperInfo = new SboOperInfo();
                sboOperInfo.setUserName(sboCallBackTransferReq.getUserName());
                sboOperInfo.setBetAmount(amount);
                sboOperInfo.setTransferCode(sboCallBackTransferReq.getTransferRefno());
                sboOperInfo.setTransactionId(sboCallBackTransferReq.getGpid());
                sboOperInfo.setResultTime(sboCallBackTransferReq.getTransferTime());
                sboOperInfo.setProductType(sboCallBackTransferReq.getProductType());
                sboOperInfo.setGameType(sboCallBackTransferReq.getGameType());
                sboOperInfo.setBalance(balance);
                sboOperInfo.setTransferType(sboCallBackTransferReq.getTransferType());
                sboOperInfo.setStatus("Transfer");
                sboOperInfo.setOperType("Transfer");
                sboOperInfo.setCreateTime(new Date());
                sboOperInfoMapper.insert(sboOperInfo);

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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SboOperInfo::getTransferCode, sboCallBackRollbackTransferReq.getTransferRefno());
            wrapper.eq(SboOperInfo::getTransactionId, sboCallBackRollbackTransferReq.getGpid());
            wrapper.eq(SboOperInfo::getStatus,"Transfer");
            wrapper.eq(SboOperInfo::getUserName, sboCallBackRollbackTransferReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType, sboCallBackRollbackTransferReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType, sboCallBackRollbackTransferReq.getGameType());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);
            BigDecimal balance = BigDecimal.valueOf(0D);
            if(null!=oldSboOperInfo){
                BigDecimal amount = oldSboOperInfo.getBetAmount();
                if("131".equals(oldSboOperInfo.getTransferType())){//131转入
                    balance = memBaseinfo.getBalance().subtract(amount);
                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);

                }else {//130转出
                    balance = memBaseinfo.getBalance().add(amount);
                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
                }

                SboOperInfo sboOperInfo = new SboOperInfo();
                sboOperInfo.setUserName(sboCallBackRollbackTransferReq.getUserName());
                sboOperInfo.setBetAmount(amount);
                sboOperInfo.setTransferCode(sboCallBackRollbackTransferReq.getTransferRefno());
                sboOperInfo.setTransactionId(sboCallBackRollbackTransferReq.getGpid());
                sboOperInfo.setResultTime(oldSboOperInfo.getResultTime());
                sboOperInfo.setProductType(sboCallBackRollbackTransferReq.getProductType());
                sboOperInfo.setGameType(sboCallBackRollbackTransferReq.getGameType());
                sboOperInfo.setBalance(balance);
                sboOperInfo.setStatus("RollbackTransfer");
                sboOperInfo.setOperType("RollbackTransfer");
                sboOperInfo.setCreateTime(new Date());
                sboOperInfoMapper.insert(sboOperInfo);
                oldSboOperInfo.setStatus("RollbackTransfer");
                oldSboOperInfo.setUpdateTime(new Date());
                sboOperInfoMapper.updateById(oldSboOperInfo);

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
            LambdaQueryWrapper<SboOperInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SboOperInfo::getTransferCode, sboCallBackGetTransferStautsReq.getTransferRefno());
            wrapper.eq(SboOperInfo::getTransactionId, sboCallBackGetTransferStautsReq.getGpid());
            wrapper.eq(SboOperInfo::getStatus,"Transfer");
            wrapper.eq(SboOperInfo::getUserName, sboCallBackGetTransferStautsReq.getUserName());
            wrapper.eq(SboOperInfo::getProductType, sboCallBackGetTransferStautsReq.getProductType());
            wrapper.eq(SboOperInfo::getGameType, sboCallBackGetTransferStautsReq.getGameType());
            SboOperInfo oldSboOperInfo = sboOperInfoMapper.selectOne(wrapper);
            if(null!=oldSboOperInfo){
                sboCallBackGetTransferStatusResp.setTransferStatus("2");
                sboCallBackGetTransferStatusResp.setErrorCode("0");
                sboCallBackGetTransferStatusResp.setAmount(null==oldSboOperInfo.getBetAmount()?"":oldSboOperInfo.getBetAmount().toString());
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

                SboOperInfo sboOperInfo = new SboOperInfo();
                sboOperInfo.setUserName(sboCallBackLiveCoinTransactionReq.getUserName());
                sboOperInfo.setBetAmount(betAmount);
                sboOperInfo.setTransferCode(sboCallBackLiveCoinTransactionReq.getTransferCode());
                sboOperInfo.setTransactionId(sboCallBackLiveCoinTransactionReq.getTransactionId());
                sboOperInfo.setResultTime(sboCallBackLiveCoinTransactionReq.getTranscationTime());
                sboOperInfo.setProductType(sboCallBackLiveCoinTransactionReq.getProductType());
                sboOperInfo.setGameType(sboCallBackLiveCoinTransactionReq.getGameType());
                sboOperInfo.setBalance(balance);
                sboOperInfo.setStatus("LiveCoin");
                sboOperInfo.setOperType("LiveCoin");
                sboOperInfo.setCreateTime(new Date());
                sboOperInfoMapper.insert(sboOperInfo);
            }

        }

        return sboCallBackCommResp;
    }
}
