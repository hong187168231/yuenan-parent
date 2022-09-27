package com.indo.game.service.saba.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.game.pojo.dto.saba.*;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.saba.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.saba.SabaCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SabaCallbackServiceImpl implements SabaCallbackService {

    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;

    //取得用户的余额
    public Object getBalance(SabaCallBackReq<SabaCallBackParentReq> sabaCallBackReq) {
        SabaCallBackParentReq sabaCallBackParentReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackParentReq.class);

        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackParentReq.getUserId());
        SabaCallBackGetBalanceResp sabaCallBackGetBalanceResp = new SabaCallBackGetBalanceResp();

        if (null == memBaseinfo) {
            sabaCallBackGetBalanceResp.setStatus("203");
            sabaCallBackGetBalanceResp.setMsg("Account Existed");
        } else {
            sabaCallBackGetBalanceResp.setStatus("0");
            sabaCallBackGetBalanceResp.setMsg("Success");
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
            sabaCallBackGetBalanceResp.setBalance(memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            sabaCallBackGetBalanceResp.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT1));
            sabaCallBackGetBalanceResp.setUserId(sabaCallBackParentReq.getUserId());
        }

        return sabaCallBackGetBalanceResp;
    }


    //投注
    public Object placeBet(SabaCallBackReq<SabaCallBackPlaceBetReq> sabaCallBackReq) {
        SabaCallBackPlaceBetReq sabaCallBackPlaceBetReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackPlaceBetReq.class);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackPlaceBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();

        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        } else {
            BigDecimal balance = memBaseinfo.getBalance();
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            BigDecimal betAmount = null!=sabaCallBackPlaceBetReq.getDebitAmount()?sabaCallBackPlaceBetReq.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            if (balance.compareTo(betAmount) == -1) {
                sabaCallBackRespError.setStatus("502");
                sabaCallBackRespError.setMsg("Player Has Insufficient Funds");
                return sabaCallBackRespError;
            } else {
                SabaCallBackPlaceBetResp sabaCallBackPlaceBetResp = new SabaCallBackPlaceBetResp();
                balance = balance.subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                sabaCallBackPlaceBetResp.setStatus("0");
                sabaCallBackPlaceBetResp.setRefId(sabaCallBackPlaceBetReq.getRefId());
                String rePlatformTxId = GeneratorIdUtil.generateId();
                sabaCallBackPlaceBetResp.setLicenseeTxId(rePlatformTxId);

                Txns txns = new Txns();
//                BeanUtils.copyProperties(sabaCallBackPlaceBetReq, txns);
                //游戏商注单号
                txns.setPlatformTxId(sabaCallBackPlaceBetReq.getOperationId());
                txns.setRoundId(sabaCallBackPlaceBetReq.getRefId());
                txns.setRePlatformTxId(rePlatformTxId);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(true);
                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());;
                //平台代码
                txns.setPlatform(gameParentPlatform.getPlatformCode());
                //平台英文名称
                txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
                //平台中文名称
                txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
                //平台游戏类型
                txns.setGameType(gameCategory.getGameType());
                //游戏分类ID
                txns.setCategoryId(gameCategory.getId());
                //游戏分类名称
                txns.setCategoryName(gameCategory.getGameName());
                //平台游戏代码
                txns.setGameCode(gamePlatform.getPlatformCode());
                //游戏名称
                txns.setGameName(gamePlatform.getPlatformEnName());
                //下注金额
                txns.setBetAmount(betAmount);
                txns.setWinningAmount(betAmount.negate());
                txns.setWinAmount(betAmount);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                //操作名称
                txns.setMethod("Place Bet");
                txns.setStatus("Running");
                txns.setGameMethod("placeBet");
                //余额
                txns.setBalance(balance);
                txnsMapper.insert(txns);
                return sabaCallBackPlaceBetResp;
            }
        }
    }

    //确认投注查询
    public Object confirmBet(SabaCallBackReq<SabaCallBackConfirmBetReq<TicketInfoReq>> sabaCallBackReq) {
        SabaCallBackConfirmBetReq<TicketInfoReq> sabaCallBackConfirmBetReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackConfirmBetReq.class);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackConfirmBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        } else {
            List<TicketInfoReq> list = sabaCallBackConfirmBetReq.getTxns();
            BigDecimal balance = memBaseinfo.getBalance();
            for (int i=0;i<list.size();i++){
                TicketInfoReq t = JSONObject.parseObject(JSONObject.toJSONString(list.get(i)),TicketInfoReq.class);
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackConfirmBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackConfirmBetReq.getUserId());
//                wrapper.eq(Txns::getRePlatformTxId,sabaCallBackConfirmBetReq.getOperationId());
                wrapper.eq(Txns::getRoundId, t.getRefId());
                wrapper.eq(Txns::getMethod, "Place Bet");
                wrapper.eq(Txns::getStatus, "Running");
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null==oldTxns) {
                    BigDecimal betAmount = null!=t.getDebitAmount()?t.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                    balance = balance.subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

                    Txns txns = new Txns();
//                BeanUtils.copyProperties(sabaCallBackPlaceBetReq, txns);
                    //游戏商注单号
                    txns.setPlatformTxId(sabaCallBackConfirmBetReq.getOperationId());
                    txns.setRoundId(t.getRefId());
                    txns.setRePlatformTxId(t.getLicenseeTxId());
                    //此交易是否是投注 true是投注 false 否
                    txns.setBet(true);
                    //玩家 ID
                    txns.setUserId(memBaseinfo.getAccount());
                    //玩家货币代码
                    txns.setCurrency(gameParentPlatform.getCurrencyType());;
                    //平台代码
                    txns.setPlatform(gameParentPlatform.getPlatformCode());
                    //平台英文名称
                    txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
                    //平台中文名称
                    txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
                    //平台游戏类型
                    txns.setGameType(gameCategory.getGameType());
                    //游戏分类ID
                    txns.setCategoryId(gameCategory.getId());
                    //游戏分类名称
                    txns.setCategoryName(gameCategory.getGameName());
                    //平台游戏代码
                    txns.setGameCode(gamePlatform.getPlatformCode());
                    //游戏名称
                    txns.setGameName(gamePlatform.getPlatformEnName());
                    //下注金额
                    txns.setBetAmount(betAmount);
                    txns.setWinningAmount(betAmount.negate());
                    txns.setWinAmount(betAmount);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    txns.setCreateTime(dateStr);
                    //操作名称
                    txns.setMethod("Place Bet");
                    txns.setStatus("Running");
                    txns.setGameMethod("confirmBet");
                    //余额
                    txns.setBalance(balance);
                    txnsMapper.insert(txns);
                }
            }
            SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
            sabaCallBackConfirmBetResp.setStatus("0");
            sabaCallBackConfirmBetResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            return sabaCallBackConfirmBetResp;
        }
    }

    //取消投注
    public Object cancelBet(SabaCallBackReq<SabaCallBackCancelBetReq<TradingInfoReq>> sabaCallBackReq) {

        SabaCallBackCancelBetReq<TradingInfoReq> sabaCallBackCancelBetReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackCancelBetReq.class);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackCancelBetReq.getUserId());
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
        SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
        sabaCallBackConfirmBetResp.setStatus("0");
        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        } else {
            List<TradingInfoReq> list = sabaCallBackCancelBetReq.getTxns();
            BigDecimal balance = memBaseinfo.getBalance();
            for (int i=0;i<list.size();i++){
                TradingInfoReq t = JSONObject.parseObject(JSONObject.toJSONString(list.get(i)),TradingInfoReq.class);
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackCancelBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackCancelBetReq.getUserId());
                wrapper.eq(Txns::getRoundId, t.getRefId());
                wrapper.eq(Txns::getMethod, "Place Bet");
                wrapper.eq(Txns::getStatus, "Running");
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if(null!=oldTxns) {
                    BigDecimal creditAmount = t.getCreditAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需增加在玩家的金额。
                    BigDecimal debitAmount = t.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需从玩家扣除的金额。
                    BigDecimal amount = BigDecimal.ZERO;
                    if(BigDecimal.ZERO.compareTo(creditAmount)==-1) {
                        gameCommonService.updateUserBalance(memBaseinfo, creditAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                        balance = balance.add(creditAmount);
                        amount = amount.add(creditAmount);
                    }
                    if(BigDecimal.ZERO.compareTo(debitAmount)==-1) {
                        gameCommonService.updateUserBalance(memBaseinfo, debitAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
                        balance = balance.subtract(debitAmount);
                        amount = amount.subtract(debitAmount);
                    }
                    String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setBalance(balance);
                    txns.setId(null);
                    txns.setStatus("Running");
                    txns.setWinAmount(amount);
                    txns.setMethod("Cancel Bet");
                    txns.setCreateTime(dateStr);
                    txns.setGameMethod("Cancel Bet");
                    txnsMapper.insert(txns);

                    oldTxns.setStatus("Cancel Bet");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }else {
                    sabaCallBackConfirmBetResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                    return sabaCallBackConfirmBetResp;
                }
            }

            sabaCallBackConfirmBetResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            return sabaCallBackConfirmBetResp;
        }

    }

    //结算投注
    public Object settle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq> sabaCallBackSettleReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackSettleReq.class);

        List<SettleTradingInfoReq> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        for ( int i=0;i< settleTradingInfoReqList.size();i++) {
            SettleTradingInfoReq settleTradingInfoReq = JSONObject.parseObject(JSONObject.toJSONString(settleTradingInfoReqList.get(i)),SettleTradingInfoReq.class);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(settleTradingInfoReq.getUserId());
            SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
            if (null == memBaseinfo) {
                sabaCallBackRespError.setStatus("203");
                sabaCallBackRespError.setMsg("Account Existed");
                return sabaCallBackRespError;
            }
            BigDecimal balance = memBaseinfo.getBalance();
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackCancelBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackCancelBetReq.getUserId());
            wrapper.eq(Txns::getRoundId, settleTradingInfoReq.getRefId());
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null!=oldTxns) {
                if(!"Place Bet".equals(oldTxns.getMethod())){
                    return sabaCallBackParentResp;
                }
                BigDecimal creditAmount = settleTradingInfoReq.getCreditAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需增加在玩家的金额。
                BigDecimal debitAmount = settleTradingInfoReq.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需从玩家扣除的金额。
                BigDecimal amount = BigDecimal.ZERO;
                if(BigDecimal.ZERO.compareTo(creditAmount)==-1) {
                    gameCommonService.updateUserBalance(memBaseinfo, creditAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                    balance = balance.add(creditAmount);
                    amount = amount.add(creditAmount);
                }
                if(BigDecimal.ZERO.compareTo(debitAmount)==-1) {
                    gameCommonService.updateUserBalance(memBaseinfo, debitAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                    balance = balance.subtract(debitAmount);
                    amount = amount.subtract(debitAmount);
                }
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setBalance(balance);
                txns.setId(null);
                txns.setStatus("Running");
                txns.setWinAmount(amount);
                txns.setWinningAmount(amount);
                txns.setMethod("Settle");
                txns.setCreateTime(dateStr);
                txns.setGameMethod("settle");
                txnsMapper.insert(txns);

                oldTxns.setStatus("Settle");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }else {
//                sabaCallBackRespError.setStatus("203");
//                sabaCallBackRespError.setMsg("Account Existed");
//                return sabaCallBackRespError;
            }
        }
        return sabaCallBackParentResp;
    }

    //重新结算投注
    public Object resettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq> sabaCallBackSettleReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackSettleReq.class);

        List<SettleTradingInfoReq> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        for ( int i=0;i< settleTradingInfoReqList.size();i++) {
            SettleTradingInfoReq settleTradingInfoReq = JSONObject.parseObject(JSONObject.toJSONString(settleTradingInfoReqList.get(i)),SettleTradingInfoReq.class);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(settleTradingInfoReq.getUserId());
            SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
            if (null == memBaseinfo) {
                sabaCallBackRespError.setStatus("203");
                sabaCallBackRespError.setMsg("Account Existed");
                return sabaCallBackRespError;
            }
            BigDecimal balance = memBaseinfo.getBalance();
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackCancelBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackCancelBetReq.getUserId());
            wrapper.eq(Txns::getRoundId, settleTradingInfoReq.getRefId());
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            BigDecimal creditAmount = settleTradingInfoReq.getCreditAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需增加在玩家的金额。
            BigDecimal debitAmount = settleTradingInfoReq.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需从玩家扣除的金额。
            BigDecimal amount = BigDecimal.ZERO;
            if(null!=oldTxns) {
                if("Settle".equals(oldTxns.getMethod())){
                    if("resettle".equals(oldTxns.getGameMethod())){
                        return sabaCallBackParentResp;
                    }
                    BigDecimal winAmount = oldTxns.getWinAmount();
                    if(BigDecimal.ZERO.compareTo(winAmount)==-1){//回退之前结算
                        gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                        balance = balance.subtract(winAmount);
                    }else {
                        gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        balance = balance.add(winAmount);
                    }
                }
                if(BigDecimal.ZERO.compareTo(creditAmount)==-1) {
                    gameCommonService.updateUserBalance(memBaseinfo, creditAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                    balance = balance.add(creditAmount);
                    amount = amount.add(creditAmount);
                }
                if(BigDecimal.ZERO.compareTo(debitAmount)==-1) {
                    gameCommonService.updateUserBalance(memBaseinfo, debitAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                    balance = balance.subtract(debitAmount);
                    amount = amount.subtract(debitAmount);
                }
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setBalance(balance);
                txns.setId(null);
                txns.setStatus("Running");
                txns.setWinAmount(amount);
                txns.setWinningAmount(amount);
                txns.setMethod("Settle");
                txns.setCreateTime(dateStr);
                txns.setGameMethod("resettle");
                txnsMapper.insert(txns);

                oldTxns.setStatus("Settle");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);

            }else {
                sabaCallBackRespError.setStatus("203");
                sabaCallBackRespError.setMsg("Account Existed");
                return sabaCallBackRespError;
            }
        }
        return sabaCallBackParentResp;
    }

    //撤销结算投注
    public Object unsettle(SabaCallBackReq<SabaCallBackSettleReq<SettleTradingInfoReq>> sabaCallBackReq) {
        SabaCallBackSettleReq<SettleTradingInfoReq> sabaCallBackSettleReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackSettleReq.class);

        List<SettleTradingInfoReq> settleTradingInfoReqList = sabaCallBackSettleReq.getTxns();
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        for ( int i=0;i< settleTradingInfoReqList.size();i++) {
            SettleTradingInfoReq settleTradingInfoReq = JSONObject.parseObject(JSONObject.toJSONString(settleTradingInfoReqList.get(i)),SettleTradingInfoReq.class);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(settleTradingInfoReq.getUserId());
            SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
            if (null == memBaseinfo) {
                sabaCallBackRespError.setStatus("203");
                sabaCallBackRespError.setMsg("Account Existed");
                return sabaCallBackRespError;
            }
            BigDecimal balance = memBaseinfo.getBalance();
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackCancelBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackCancelBetReq.getUserId());
            wrapper.eq(Txns::getRoundId, settleTradingInfoReq.getRefId());
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            BigDecimal creditAmount = settleTradingInfoReq.getCreditAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需增加在玩家的金额。
            BigDecimal debitAmount = settleTradingInfoReq.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro());// Y (decimal) 需从玩家扣除的金额。
            BigDecimal amount = BigDecimal.ZERO;
            if(null!=oldTxns) {
                if("Settle".equals(oldTxns.getMethod())){
                    BigDecimal winAmount = oldTxns.getWinAmount();
                    if(BigDecimal.ZERO.compareTo(winAmount)==-1){//回退之前结算
                        gameCommonService.updateUserBalance(memBaseinfo, debitAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                        balance = balance.subtract(debitAmount);
                        amount = amount.subtract(debitAmount);

                    }else {
                        gameCommonService.updateUserBalance(memBaseinfo, creditAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        balance = balance.add(creditAmount);
                        amount = amount.add(creditAmount);
                    }
                    String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setBalance(balance);
                    txns.setId(null);
                    txns.setStatus("Running");
                    txns.setWinAmount(amount);
                    txns.setWinningAmount(amount);
                    txns.setMethod("Place Bet");
                    txns.setCreateTime(dateStr);
                    txns.setGameMethod("unsettle");
                    txnsMapper.insert(txns);

                    oldTxns.setStatus("Settle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }
            }else {
                sabaCallBackRespError.setStatus("203");
                sabaCallBackRespError.setMsg("Account Existed");
                return sabaCallBackRespError;
            }
        }
        return sabaCallBackParentResp;
    }

    //投注-仅支援欧洲盘
    public Object placeBetParlay(SabaCallBackReq<SabaCallBackPlaceBetParlayReq> sabaCallBackReq) {

        SabaCallBackPlaceBetParlayReq sabaCallBackPlaceBetParlayReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackPlaceBetParlayReq.class);
        List<ComboInfo> comboInfoList = sabaCallBackPlaceBetParlayReq.getTxns();
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackPlaceBetParlayReq.getUserId());
        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        }
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        List<TicketInfoMapping> ticketInfoMappingList = new ArrayList<>();
        BigDecimal balance = memBaseinfo.getBalance();
        for (ComboInfo comboInfo : comboInfoList) {
            TicketInfoMapping ticketInfoMapping = new TicketInfoMapping();
            String rePlatformTxId = GeneratorIdUtil.generateId();
            BigDecimal betAmount = null!=comboInfo.getDebitAmount()?comboInfo.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            if (balance.compareTo(betAmount) == -1) {
                sabaCallBackRespError.setStatus("502");
                sabaCallBackRespError.setMsg("Player Has Insufficient Funds");
                return sabaCallBackRespError;
            } else {
                balance = balance.subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackConfirmBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackConfirmBetReq.getUserId());
//                wrapper.eq(Txns::getRePlatformTxId,sabaCallBackConfirmBetReq.getOperationId());
                wrapper.eq(Txns::getRoundId, comboInfo.getRefId());
                wrapper.eq(Txns::getMethod, "Place Bet");
                wrapper.eq(Txns::getStatus, "Running");
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null!=oldTxns) {
                    sabaCallBackRespError.setStatus("203");
                    sabaCallBackRespError.setMsg("Account Existed");
                    return sabaCallBackRespError;
                }

                Txns txns = new Txns();
//                BeanUtils.copyProperties(sabaCallBackPlaceBetReq, txns);
                //游戏商注单号
                txns.setPlatformTxId(sabaCallBackPlaceBetParlayReq.getOperationId());
                txns.setRoundId(comboInfo.getRefId());
                txns.setRePlatformTxId(rePlatformTxId);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(true);
                //玩家 ID
                txns.setUserId(memBaseinfo.getAccount());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
                ;
                //平台代码
                txns.setPlatform(gameParentPlatform.getPlatformCode());
                //平台英文名称
                txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
                //平台中文名称
                txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
                //平台游戏类型
                txns.setGameType(gameCategory.getGameType());
                //游戏分类ID
                txns.setCategoryId(gameCategory.getId());
                //游戏分类名称
                txns.setCategoryName(gameCategory.getGameName());
                //平台游戏代码
                txns.setGameCode(gamePlatform.getPlatformCode());
                //游戏名称
                txns.setGameName(gamePlatform.getPlatformEnName());
                //下注金额
                txns.setBetAmount(betAmount);
                txns.setWinningAmount(betAmount.negate());
                txns.setWinAmount(betAmount);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                //操作名称
                txns.setMethod("Place Bet");
                txns.setStatus("Running");
                txns.setGameMethod("placeBetParlay");
                //余额
                txns.setBalance(balance);
                txnsMapper.insert(txns);
            }


            ticketInfoMapping.setRefId(comboInfo.getRefId());
            ticketInfoMapping.setLicenseeTxId(rePlatformTxId);
            ticketInfoMappingList.add(ticketInfoMapping);
        }
        SabaCallBackPlaceBetParlayResp sabaCallBackPlaceBetParlayResp = new SabaCallBackPlaceBetParlayResp();
        sabaCallBackPlaceBetParlayResp.setStatus("0");
        sabaCallBackPlaceBetParlayResp.setTxns(ticketInfoMappingList);
        return sabaCallBackPlaceBetParlayResp;
    }

    //当沙巴体育通过 PlaceBetParlay 方法收到成功结果，沙巴体育将会呼叫 ConfirmBetParlay
    public Object confirmBetParlay(SabaCallBackReq<SabaCallBackConfirmBetParlayReq> sabaCallBackReq) {
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
        SabaCallBackConfirmBetParlayReq sabaCallBackConfirmBetParlayReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()),SabaCallBackConfirmBetParlayReq.class);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackConfirmBetParlayReq.getUserId());
        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("203");
            sabaCallBackRespError.setMsg("Account Existed");
            return sabaCallBackRespError;
        }
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        BigDecimal balance = memBaseinfo.getBalance();
        List<ConfirmBetParlayTicketInfoReq> txnsList = sabaCallBackConfirmBetParlayReq.getTxns();
        for (ConfirmBetParlayTicketInfoReq confirmBetParlayTicketInfoReq : txnsList) {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackConfirmBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackConfirmBetReq.getUserId());
//                wrapper.eq(Txns::getRePlatformTxId,sabaCallBackConfirmBetReq.getOperationId());
            wrapper.eq(Txns::getRoundId, confirmBetParlayTicketInfoReq.getRefId());
            wrapper.eq(Txns::getMethod, "Place Bet");
            wrapper.eq(Txns::getStatus, "Running");
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if (null==oldTxns) {
                BigDecimal betAmount = null!=confirmBetParlayTicketInfoReq.getDebitAmount()?confirmBetParlayTicketInfoReq.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                if (balance.compareTo(betAmount) == -1) {
                    sabaCallBackRespError.setStatus("502");
                    sabaCallBackRespError.setMsg("Player Has Insufficient Funds");
                    return sabaCallBackRespError;
                } else {
                    balance = balance.subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                    Txns txns = new Txns();

//                BeanUtils.copyProperties(sabaCallBackPlaceBetReq, txns);
                    //游戏商注单号
                    txns.setPlatformTxId(sabaCallBackConfirmBetParlayReq.getOperationId());
                    txns.setRoundId(confirmBetParlayTicketInfoReq.getRefId());
                    txns.setRePlatformTxId(confirmBetParlayTicketInfoReq.getLicenseeTxId());
                    //此交易是否是投注 true是投注 false 否
                    txns.setBet(true);
                    //玩家 ID
                    txns.setUserId(memBaseinfo.getAccount());
                    //玩家货币代码
                    txns.setCurrency(gameParentPlatform.getCurrencyType());
                    ;
                    //平台代码
                    txns.setPlatform(gameParentPlatform.getPlatformCode());
                    //平台英文名称
                    txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
                    //平台中文名称
                    txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
                    //平台游戏类型
                    txns.setGameType(gameCategory.getGameType());
                    //游戏分类ID
                    txns.setCategoryId(gameCategory.getId());
                    //游戏分类名称
                    txns.setCategoryName(gameCategory.getGameName());
                    //平台游戏代码
                    txns.setGameCode(gamePlatform.getPlatformCode());
                    //游戏名称
                    txns.setGameName(gamePlatform.getPlatformEnName());
                    //下注金额
                    txns.setBetAmount(betAmount);
                    txns.setWinningAmount(betAmount.negate());
                    txns.setWinAmount(betAmount);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    txns.setCreateTime(dateStr);
                    //操作名称
                    txns.setMethod("Place Bet");
                    txns.setStatus("Running");
                    txns.setGameMethod("confirmBetParlay");
                    //余额
                    txns.setBalance(balance);
                    txnsMapper.insert(txns);
                }
            }
        }
        SabaCallBackConfirmBetResp sabaCallBackConfirmBetResp = new SabaCallBackConfirmBetResp();
        sabaCallBackConfirmBetResp.setStatus("0");
        sabaCallBackConfirmBetResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
        return sabaCallBackConfirmBetResp;
    }

    public Object adjustbalance(SabaCallBackReq<SabaCallBackAdjustbalanceReq<SabaCallBackAdjustbalanceInfoReq>> sabaCallBackReq){
        SabaCallBackAdjustbalanceReq<SabaCallBackAdjustbalanceInfoReq> sabaCallBackAdjustbalanceParlayReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackReq.getMessage()), SabaCallBackAdjustbalanceReq.class);
        SabaCallBackAdjustbalanceInfoReq sabaCallBackAdjustbalanceInfoReq = JSONObject.toJavaObject(JSONObject.parseObject(sabaCallBackAdjustbalanceParlayReq.getBalanceInfo()), SabaCallBackAdjustbalanceInfoReq.class);
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        SabaCallBackRespError sabaCallBackRespError = new SabaCallBackRespError();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackAdjustbalanceParlayReq.getUserId());
        String rePlatformTxId = GeneratorIdUtil.generateId();
        if (null == memBaseinfo) {
            sabaCallBackRespError.setStatus("904");
            sabaCallBackRespError.setMsg("Request Timeout");
            return sabaCallBackRespError;
        }
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SABA_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal creditAmount = null!=sabaCallBackAdjustbalanceInfoReq.getCreditAmount()?sabaCallBackAdjustbalanceInfoReq.getCreditAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;// Y (decimal) 需增加在玩家的金额。
        BigDecimal debitAmount = null!=sabaCallBackAdjustbalanceInfoReq.getDebitAmount()?sabaCallBackAdjustbalanceInfoReq.getDebitAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;// Y (decimal) 需从玩家扣除的金额
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//                wrapper.eq(Txns::getPlatformTxId, sabaCallBackConfirmBetReq.getOperationId());
//                wrapper.eq(Txns::getUserId, sabaCallBackConfirmBetReq.getUserId());
//                wrapper.eq(Txns::getRePlatformTxId,sabaCallBackConfirmBetReq.getOperationId());
        wrapper.eq(Txns::getRoundId, sabaCallBackAdjustbalanceParlayReq.getRefId());
        wrapper.eq(Txns::getMethod, "Adjust Bet");
        wrapper.eq(Txns::getStatus, "Running");
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if(null!=oldTxns) {
            if (BigDecimal.ZERO.compareTo(creditAmount) == -1) {
                balance = balance.add(creditAmount);
                gameCommonService.updateUserBalance(memBaseinfo, creditAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
            }
            if (BigDecimal.ZERO.compareTo(debitAmount) == -1) {
                balance = balance.add(debitAmount);
                gameCommonService.updateUserBalance(memBaseinfo, debitAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.SPENDING);
            }
            Txns txns = new Txns();

//                BeanUtils.copyProperties(sabaCallBackPlaceBetReq, txns);
            //游戏商注单号
            txns.setPlatformTxId(sabaCallBackAdjustbalanceParlayReq.getOperationId());
            txns.setRoundId(sabaCallBackAdjustbalanceParlayReq.getRefId());
            txns.setRePlatformTxId(rePlatformTxId);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(gameParentPlatform.getCurrencyType());
            ;
            //平台代码
            txns.setPlatform(gameParentPlatform.getPlatformCode());
            //平台英文名称
            txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
            //平台中文名称
            txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
            //平台游戏类型
            txns.setGameType(gameCategory.getGameType());
            //游戏分类ID
            txns.setCategoryId(gameCategory.getId());
            //游戏分类名称
            txns.setCategoryName(gameCategory.getGameName());
            //平台游戏代码
            txns.setGameCode(gamePlatform.getPlatformCode());
            //游戏名称
            txns.setGameName(gamePlatform.getPlatformEnName());
            //下注金额
            if (BigDecimal.ZERO.compareTo(creditAmount) != 0) {
                txns.setBetAmount(creditAmount);
                txns.setWinningAmount(creditAmount);
                txns.setWinAmount(creditAmount);
            }
            if (BigDecimal.ZERO.compareTo(debitAmount) != 0) {
                txns.setBetAmount(debitAmount);
                txns.setWinningAmount(debitAmount);
                txns.setWinAmount(debitAmount);
            }
            String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
            txns.setCreateTime(dateStr);
            //操作名称
            txns.setMethod("Adjust Bet");
            txns.setStatus("Running");
            txns.setGameMethod("adjustbalance");
            //余额
            txns.setBalance(balance);
            txnsMapper.insert(txns);
        }
        return sabaCallBackParentResp;
    }

    //厂商提供此方法，沙巴体育通过呼叫此方法提供下注细节给厂商。本方法支持快乐彩、彩票、桌面游戏产品
    public Object placeBet3rd(SabaCallBackReq<SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq>> sabaCallBackReq) {
        return null;
//        SabaCallBackPlaceBet3rdReq<PlaceBet3rdTicketInfoReq> sabaCallBackPlaceBet3rdReq = sabaCallBackReq.getMessage();
//
//        List<PlaceBet3rdTicketInfoReq> placeBet3rdTicketInfoReqList = sabaCallBackPlaceBet3rdReq.getTicketList();
//        for (PlaceBet3rdTicketInfoReq placeBet3rdTicketInfoReq : placeBet3rdTicketInfoReqList) {
//
//        }
//
//        SabaCallBackPlaceBet3rdResp<TicketInfoMapping> sabaCallBackPlaceBet3rdResp = new SabaCallBackPlaceBet3rdResp<>();
//        sabaCallBackPlaceBet3rdResp.setStatus("0");
//        return sabaCallBackPlaceBet3rdResp;
    }

    public Object confirmBet3rd(SabaCallBackReq<SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq>> sabaCallBackReq) {
        return null;
//        SabaCallBackConfirmBet3rdReq<ConfirmBet3rdTicketInfoReq> sabaCallBackConfirmBet3rdReq = sabaCallBackReq.getMessage();
//
//        SabaCallBackConfirmBet3rdResp sabaCallBackConfirmBet3rdResp = new SabaCallBackConfirmBet3rdResp();
//        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sabaCallBackConfirmBet3rdReq.getUserId());
//
//        if (null == memBaseinfo) {
//            sabaCallBackConfirmBet3rdResp.setStatus("203");
//            sabaCallBackConfirmBet3rdResp.setMsg("Account Existed");
//        } else {
//            sabaCallBackConfirmBet3rdResp.setStatus("0");
//            sabaCallBackConfirmBet3rdResp.setBalance(memBaseinfo.getBalance());
//        }
//        return sabaCallBackConfirmBet3rdResp;
    }

    //当 Cash Out 交易被接受后，沙巴体育将会通过此方法传输交易
    public Object cashOut(SabaCallBackReq<SabaCallBackCashOutReq<CashOutTicketInfoReq>> sabaCallBackReq) {
        return null;
//        SabaCallBackCashOutReq<CashOutTicketInfoReq> sabaCallBackCashOutReq = sabaCallBackReq.getMessage();
//        List<CashOutTicketInfoReq> cashOutTicketInfoReqList = sabaCallBackCashOutReq.getTxns();
//        for (CashOutTicketInfoReq cashOutTicketInfoReq : cashOutTicketInfoReqList) {
//
//        }
//        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
//        sabaCallBackParentResp.setStatus("0");
//        return sabaCallBackParentResp;
    }

    //因 Cashout 票的异动造成原 Cashout 的主票发生变化，沙巴体育将会透过这支 API 传送原 Cashout 主票的信息。
    public Object updateBet(SabaCallBackReq<SabaCallBackCashOutReq<UpdateBetTicketInfoReq>> sabaCallBackReq) {
        return null;
//        SabaCallBackCashOutReq<UpdateBetTicketInfoReq> sabaCallBackCashOutReq = sabaCallBackReq.getMessage();
//        List<UpdateBetTicketInfoReq> updateBetTicketInfoReqList = sabaCallBackCashOutReq.getTxns();
//        for (UpdateBetTicketInfoReq updateBetTicketInfoReq : updateBetTicketInfoReqList) {
//
//        }
//
//
//        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
//        sabaCallBackParentResp.setStatus("0");
//        return sabaCallBackParentResp;
    }

    //检查沙巴体育与厂商之间的连结是否有效。
    public Object healthcheck(SabaCallBackReq<HealthCheckReq> sabaCallBackReq) {
        SabaCallBackParentResp sabaCallBackParentResp = new SabaCallBackParentResp();
        sabaCallBackParentResp.setStatus("0");
        return sabaCallBackParentResp;
    }
}
