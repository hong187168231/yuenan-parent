package com.indo.game.service.sbo.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.sbo.*;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.sbo.SboCallBackCommResp;
import com.indo.game.pojo.vo.callback.sbo.SboCallBackDeductResp;
import com.indo.game.pojo.vo.callback.sbo.SboCallBackGetBetStatusResp;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.sbo.SboCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SboCallbackServiceImpl implements SboCallbackService {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;

    //取得用户的余额
    public Object getBalance(SboCallBackParentReq sboCallBackParentReq,String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setBalance(BigDecimal.ZERO);
        sboCallBackCommResp.setAccountName(sboCallBackParentReq.getUsername());
        if(null == sboCallBackParentReq.getUsername() || "".equals(sboCallBackParentReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackParentReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackParentReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
        } else {
            sboCallBackCommResp.setBalance(memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            sboCallBackCommResp.setErrorCode(0);
            sboCallBackCommResp.setErrorMessage("No Error");
        }

        return sboCallBackCommResp;
    }


    //扣除投注金额
    public Object deduct(SboCallBackDeductReq sboCallBackDeductReq,String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackDeductResp sboCallBackCommResp = new SboCallBackDeductResp();
        sboCallBackCommResp.setBalance(BigDecimal.ZERO);
        sboCallBackCommResp.setAccountName(sboCallBackDeductReq.getUsername());
        sboCallBackCommResp.setBetAmount(sboCallBackDeductReq.getAmount());
        if(null == sboCallBackDeductReq.getUsername() || "".equals(sboCallBackDeductReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackDeductReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackDeductReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
            return sboCallBackCommResp;
        } else {
            BigDecimal betAmount = null!=sboCallBackDeductReq.getAmount()?sboCallBackDeductReq.getAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal balance = memBaseinfo.getBalance();
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
                sboCallBackCommResp.setErrorCode(5);
                sboCallBackCommResp.setErrorMessage("Not enough balance");
                return sboCallBackCommResp;
            } else {
                //        1	Sports Book	体育博彩
                //        3	SBO Games	电子游戏
                //        5	Virtual Sports	虚拟运动
                //        7	SBO Live Casino	真人赌场
                //        9	Seamless Game Provider	无缝游戏
                //        10	Live Coin	線上貨幣
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, sboCallBackDeductReq.getTransferCode());
                if(9==sboCallBackDeductReq.getProductType()) {//无缝游戏完美真人赌场，相同的transferCode且相同的transactionId不能被扣除投注金额两次，但相同的transferCode可以搭配不同的transactionId再扣除投注金额
                    wrapper.eq(Txns::getRoundId, sboCallBackDeductReq.getTransactionId());
                }
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
//        wrapper.eq(Txns::getUserId, userId);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if(null!=oldTxns){
                    if("Settle".equals(oldTxns.getMethod())||"Cancel Bet".equals(oldTxns.getMethod())){
                        sboCallBackCommResp.setErrorCode(5003);
                        sboCallBackCommResp.setErrorMessage("Bet With Same RefNo Exists");
                        return sboCallBackCommResp;
                    }
//                    if("Settle".equals(oldTxns.getMethod())){
//                        sboCallBackCommResp.setErrorCode(2001);
//                        sboCallBackCommResp.setErrorMessage("Bet Already Settled");
//                        return sboCallBackCommResp;
//                    }
//                    if("Cancel Bet".equals(oldTxns.getMethod())){
//                        sboCallBackCommResp.setErrorCode(2002);
//                        sboCallBackCommResp.setErrorMessage("Bet Already Canceled");
//                        return sboCallBackCommResp;
//                    }
                    if(1==sboCallBackDeductReq.getProductType()||5==sboCallBackDeductReq.getProductType()||9==sboCallBackDeductReq.getProductType()) {
                        //体育博彩，相同的transferCode不能被扣除投注金额两次。
                        //虚拟体育，相同的transferCode不能被扣除投注金额两次。
                        //无缝游戏完美真人赌场，相同的transferCode且相同的transactionId不能被扣除投注金额两次，但相同的transferCode可以搭配不同的transactionId再扣除投注金额
                        sboCallBackCommResp.setErrorCode(5003);
                        sboCallBackCommResp.setErrorMessage("Bet With Same RefNo Exists");
                        return sboCallBackCommResp;
                    }else if(7==sboCallBackDeductReq.getProductType()) {
                        if (betAmount.compareTo(oldTxns.getBetAmount()) != 1) {//真人赌场，相同的transferCode可以被扣除投注金额两次，但第二次扣除投注金额必须比第一次大。
                            sboCallBackCommResp.setErrorCode(7);
                            sboCallBackCommResp.setErrorMessage("Internal Error");
                            return sboCallBackCommResp;
                        }
                    }else if(3==sboCallBackDeductReq.getProductType()){
                        if (betAmount.compareTo(oldTxns.getBetAmount()) != 1) {//电子游戏
                            sboCallBackCommResp.setErrorCode(7);
                            sboCallBackCommResp.setErrorMessage("Internal Error");
                            return sboCallBackCommResp;
                        }
                    }
                }
                String platformCode = this.getpPlatformCode(sboCallBackDeductReq.getProductType());

                GamePlatform gamePlatform;
                if(OpenAPIProperties.SBO_IS_PLATFORM_LOGIN.equals("Y")){
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.SBO_PLATFORM_CODE,OpenAPIProperties.SBO_PLATFORM_CODE);
                }else {
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode, OpenAPIProperties.SBO_PLATFORM_CODE);
                }
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                if(null!=oldTxns){
                    balance = balance.subtract(betAmount.subtract(oldTxns.getBetAmount()));
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount.subtract(oldTxns.getBetAmount()), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                }else {
                    balance = balance.subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                }
                sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                sboCallBackCommResp.setErrorCode(0);
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                txns.setUserId(sboCallBackDeductReq.getUsername());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
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
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(betAmount.negate());
                txns.setWinAmount(betAmount);
                txns.setPlatformTxId(sboCallBackDeductReq.getTransferCode());
                txns.setRoundId(sboCallBackDeductReq.getTransactionId());
                txns.setBetTime(sboCallBackDeductReq.getBetTime());
                txns.setGameType(String.valueOf(sboCallBackDeductReq.getGameType()));
                txns.setBalance(balance);
                txns.setMethod("Place Bet");
                txns.setStatus("Running");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                if(null!=oldTxns){
                    oldTxns.setStatus("Place Bet");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                }
            }

        }

        return sboCallBackCommResp;

    }

    //结算投注
    public Object settle(SboCallBackSettleReq sboCallBackSettleReq,String ip) {
        //同一个赌注发出多次API请求,这意味着我们要求该次赌注重新结算

        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setBalance(BigDecimal.ZERO);
        sboCallBackCommResp.setAccountName(sboCallBackSettleReq.getUsername());
        if(null == sboCallBackSettleReq.getUsername() || "".equals(sboCallBackSettleReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackSettleReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackSettleReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
            return sboCallBackCommResp;
        } else {
            BigDecimal balance = memBaseinfo.getBalance();
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            String platformCode = this.getpPlatformCode(sboCallBackSettleReq.getProductType());
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, sboCallBackSettleReq.getTransferCode());

//            wrapper.eq(Txns::getUserId, sboCallBackSettleReq.getUsername());
            wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
//            wrapper.eq(Txns::getGameType, sboCallBackSettleReq.getGameType());
            List<Txns> list = txnsMapper.selectList(wrapper);
//            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(9==sboCallBackSettleReq.getProductType()){
                boolean b = false;
                for (Txns oldTxns : list) {
                    if (!"Cancel Bet".equals(oldTxns.getMethod())&&!"Settle".equals(oldTxns.getMethod())) {
                        b = true;
                    }
                }
                if(b){
                    for (Txns oldTxns : list) {
                        if ("Cancel Bet".equals(oldTxns.getMethod()) || "Settle".equals(oldTxns.getMethod())) {
                            list.remove(oldTxns);
                        }
                    }
                }
            }
            if (null == list || list.size() <= 0) {
                sboCallBackCommResp.setBalance(balance);
                sboCallBackCommResp.setErrorCode(6);
                sboCallBackCommResp.setErrorMessage("Bet not exists");
                return sboCallBackCommResp;
            }
            for (Txns oldTxns : list) {
                if ("Cancel Bet".equals(oldTxns.getMethod())) {
                    sboCallBackCommResp.setErrorCode(2002);
                    sboCallBackCommResp.setErrorMessage("Bet Already Canceled");
                    return sboCallBackCommResp;
                }
                if ("Settle".equals(oldTxns.getMethod())) {
                    sboCallBackCommResp.setErrorCode(2001);
                    sboCallBackCommResp.setErrorMessage("Bet Already Settled");
                    return sboCallBackCommResp;
                }
            }
            BigDecimal winLoss = null!=sboCallBackSettleReq.getWinLoss()?sboCallBackSettleReq.getWinLoss().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;

            Double average = winLoss.doubleValue()/list.size();
            Double remainder = winLoss.doubleValue()%list.size();
            for (int i=0;i<list.size();i++) {
                Txns oldTxns = list.get(i);
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setUserId(sboCallBackSettleReq.getUsername());
                txns.setPlatformTxId(sboCallBackSettleReq.getTransferCode());

                txns.setResultType(sboCallBackSettleReq.getResultType());

                if(i==list.size()-1){
                    average = average + remainder;
                }
                if (0==sboCallBackSettleReq.getResultType()||2==sboCallBackSettleReq.getResultType()) {//赢 或者 平手   赢:0,输:1,平手:2
                    balance = balance.add(BigDecimal.valueOf(average));
                    gameCommonService.updateUserBalance(memBaseinfo, BigDecimal.valueOf(average), GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                }
//            if ("1".equals(sboCallBackSettleReq.getResultType())) {//输
//                BigDecimal realBetAmount = sboCallBackSettleReq.getWinLoss().subtract(betAmount);
//                balance = memBaseinfo.getBalance().subtract(realBetAmount);
//                gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
//            }
                if ("2".equals(sboCallBackSettleReq.getResultType()) || "0".equals(sboCallBackSettleReq.getResultType())) {//平手 或 赢
                    txns.setWinningAmount(BigDecimal.valueOf(average));
                } else {
                    txns.setWinningAmount(BigDecimal.valueOf(average).negate());
                }
                txns.setWinAmount(BigDecimal.valueOf(average));
                txns.setBalance(balance);
                txns.setStatus("Running");
                txns.setMethod("Settle");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);

                oldTxns.setStatus("Settle");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            sboCallBackCommResp.setErrorCode(0);
            sboCallBackCommResp.setErrorMessage("No Error");
        }

        return sboCallBackCommResp;

    }

    //回滚
    public Object rollback(SboCallBackRollbackReq sboCallBackRollbackReq,String ip) {

        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setBalance(BigDecimal.ZERO);
        sboCallBackCommResp.setAccountName(sboCallBackRollbackReq.getUsername());
        if(null == sboCallBackRollbackReq.getUsername() || "".equals(sboCallBackRollbackReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackRollbackReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackRollbackReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
            return sboCallBackCommResp;
        } else {
            BigDecimal balance = memBaseinfo.getBalance();
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getPlatformTxId, sboCallBackRollbackReq.getTransferCode());
            wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
//            wrapper.eq(Txns::getUserId, sboCallBackRollbackReq.getUsername());
//            wrapper.eq(Txns::getPlatform, sboCallBackRollbackReq.getProductType());
//            wrapper.eq(Txns::getGameType, sboCallBackRollbackReq.getGameType());
            List<Txns> list = txnsMapper.selectList(wrapper);
//            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if (null == list || list.size() <= 0) {
                sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                sboCallBackCommResp.setErrorCode(6);
                sboCallBackCommResp.setErrorMessage("Bet not exists");
                return sboCallBackCommResp;
            }
            for (Txns oldTxns : list) {
                if ("Place Bet".equals(oldTxns.getMethod())) {
                    sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                    sboCallBackCommResp.setErrorCode(2003);
                    sboCallBackCommResp.setErrorMessage("Bet Already Rollback");
                    return sboCallBackCommResp;
                }
                BigDecimal winLoss = oldTxns.getWinAmount();
                if ("Cancel Bet".equals(oldTxns.getMethod())) {
                    winLoss = oldTxns.getBetAmount();
                    balance = balance.subtract(winLoss);
                    gameCommonService.updateUserBalance(memBaseinfo, winLoss, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                } else {
                    if (0 == oldTxns.getResultType() || 2 == oldTxns.getResultType()) {//赢 或者 平手
                        balance = balance.subtract(winLoss);
                        gameCommonService.updateUserBalance(memBaseinfo, winLoss, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                    }
                }
//            if ("1".equals(oldTxns.getResultType())) {//输
//                BigDecimal realBetAmount = oldTxns.getWinAmount().subtract(betAmount);
//                balance = memBaseinfo.getBalance().add(realBetAmount);
//                gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
//            }

                sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                sboCallBackCommResp.setErrorCode(0);
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setWinningAmount(winLoss.negate());
                txns.setWinAmount(winLoss);
                txns.setBalance(balance);
                txns.setStatus("Running");
                txns.setMethod("Place Bet");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Rollback");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }
        }

        return sboCallBackCommResp;

    }

    //取消投注
    public Object cancel(SboCallBackCancelReq sboCallBackCancelReq,String ip) {

        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setBalance(BigDecimal.ZERO);
        sboCallBackCommResp.setAccountName(sboCallBackCancelReq.getUsername());
        if(null == sboCallBackCancelReq.getUsername() || "".equals(sboCallBackCancelReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackCancelReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackCancelReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
        } else {
            BigDecimal balance = memBaseinfo.getBalance();
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Settle").or().eq(Txns::getMethod, "Cancel Bet"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, sboCallBackCancelReq.getTransferCode());
            wrapper.eq(Txns::getRoundId, sboCallBackCancelReq.getTransactionId());
            wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
//        wrapper.eq(Txns::getUserId, userId);
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null==oldTxns){
                sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                sboCallBackCommResp.setErrorCode(6);
                sboCallBackCommResp.setErrorMessage("Bet not exists");
                return sboCallBackCommResp;
            }
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                sboCallBackCommResp.setErrorCode(2002);
                sboCallBackCommResp.setErrorMessage("Bet Already Canceled");
                return sboCallBackCommResp;

            }
            if(9==sboCallBackCancelReq.getProductType()&&"Settle".equals(oldTxns.getMethod())){
                LambdaQueryWrapper<Txns> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(Txns::getMethod, "Settle");
                wrapper1.eq(Txns::getStatus, "Running");
                wrapper1.eq(Txns::getPlatformTxId, sboCallBackCancelReq.getTransferCode());
                wrapper1.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
                List<Txns> list = txnsMapper.selectList(wrapper1);
                for (Txns oldTxns9 : list) {
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns9, txns);

                    BigDecimal realBetAmount = oldTxns9.getWinAmount();
                    if (0 == oldTxns9.getResultType() || 2 == oldTxns9.getResultType()) {//赢 或者 平手
                        realBetAmount = realBetAmount.subtract(oldTxns9.getBetAmount());
                        balance = balance.subtract(realBetAmount);
                        gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
                    }
                    if (1 == oldTxns9.getResultType()) {//输
                        realBetAmount = realBetAmount.subtract(oldTxns9.getBetAmount());
                        balance = balance.add(realBetAmount);
                        gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                    }
                    txns.setStatus("Running");
                    txns.setMethod("Cancel Bet");

                    txns.setId(null);
                    txns.setWinAmount(realBetAmount);
                    txns.setBalance(balance);

                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns9.setStatus("Cancel");
                    oldTxns9.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns9);
                }
            }else {
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);

                BigDecimal realBetAmount = oldTxns.getWinAmount();
                if ("Place Bet".equals(oldTxns.getMethod())) {
                    balance = balance.add(realBetAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);

                } else {
                    if (0 == oldTxns.getResultType() || 2 == oldTxns.getResultType()) {//赢 或者 平手
                        realBetAmount = realBetAmount.subtract(oldTxns.getBetAmount());
                        balance = balance.subtract(realBetAmount);
                        gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
                    }
                    if (1 == oldTxns.getResultType()) {//输
                        realBetAmount = realBetAmount.subtract(oldTxns.getBetAmount());
                        balance = balance.add(realBetAmount);
                        gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                    }
//                txns.setStatus("Running");
//                txns.setMethod("Place Bet");
                }
                txns.setStatus("Running");
                txns.setMethod("Cancel Bet");

                txns.setId(null);
                txns.setWinAmount(realBetAmount);
                txns.setBalance(balance);

                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Cancel");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            sboCallBackCommResp.setErrorCode(0);
            sboCallBackCommResp.setErrorMessage("No Error");
        }

        return sboCallBackCommResp;

    }

    //小费
    public Object tip(SboCallBackTipReq sboCallBackTipReq,String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackTipReq.getUsername());
        if(null == sboCallBackTipReq.getUsername() || "".equals(sboCallBackTipReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackTipReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackTipReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
            return sboCallBackCommResp;
        } else {
            BigDecimal balance = memBaseinfo.getBalance();
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getMethod, "Tip");
            wrapper.eq(Txns::getPlatformTxId, sboCallBackTipReq.getTransferCode());
            wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null!=oldTxns){
                sboCallBackCommResp.setErrorCode(5003);
                sboCallBackCommResp.setErrorMessage("Bet With Same RefNo Exists");
                return sboCallBackCommResp;
            }
            BigDecimal betAmount = null!=sboCallBackTipReq.getAmount()?sboCallBackTipReq.getAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;

            if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
                sboCallBackCommResp.setErrorCode(5);
                sboCallBackCommResp.setErrorMessage("Not enough balance");
            } else {
                String platformCode = this.getpPlatformCode(sboCallBackTipReq.getProductType());
                balance = balance.subtract(betAmount);
                GamePlatform gamePlatform;
                if(OpenAPIProperties.SBO_IS_PLATFORM_LOGIN.equals("Y")){
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.SBO_PLATFORM_CODE,OpenAPIProperties.SBO_PLATFORM_CODE);
                }else {
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode, OpenAPIProperties.SBO_PLATFORM_CODE);
                }
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.TIP, TradingEnum.SPENDING);
                sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                sboCallBackCommResp.setErrorCode(0);
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                txns.setUserId(sboCallBackTipReq.getUsername());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
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
                txns.setBetAmount(betAmount);
                txns.setWinningAmount(betAmount.negate());
                txns.setWinningAmount(betAmount);
                txns.setPlatformTxId(sboCallBackTipReq.getTransferCode());
                txns.setRoundId(sboCallBackTipReq.getTransactionId());
                txns.setBetTime(sboCallBackTipReq.getTipTime());
                txns.setGameType(String.valueOf(sboCallBackTipReq.getGameType()));
                txns.setBalance(balance);
                txns.setStatus("Running");
                txns.setMethod("Tip");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
            }

        }

        return sboCallBackCommResp;
    }

    //红利
    public Object bonus(SboCallBackBonusReq sboCallBackBonusReq,String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackBonusReq.getUsername());
        if(null == sboCallBackBonusReq.getUsername() || "".equals(sboCallBackBonusReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackBonusReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackBonusReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
            return sboCallBackCommResp;
        } else {
            BigDecimal betAmount = null!=sboCallBackBonusReq.getAmount()?sboCallBackBonusReq.getAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal balance = memBaseinfo.getBalance().add(betAmount);
            sboCallBackCommResp.setBalance(balance);
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getMethod, "Bonus");
            wrapper.eq(Txns::getPlatformTxId, sboCallBackBonusReq.getTransferCode());
            wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if(null!=oldTxns){
                sboCallBackCommResp.setErrorCode(5003);
                sboCallBackCommResp.setErrorMessage("Bet With Same RefNo Exists");
                return sboCallBackCommResp;
            }
            String platformCode = this.getpPlatformCode(sboCallBackBonusReq.getProductType());

            GamePlatform gamePlatform;
            if(OpenAPIProperties.SBO_IS_PLATFORM_LOGIN.equals("Y")){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.SBO_PLATFORM_CODE,OpenAPIProperties.SBO_PLATFORM_CODE);
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode, OpenAPIProperties.SBO_PLATFORM_CODE);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            sboCallBackCommResp.setErrorCode(0);
            sboCallBackCommResp.setErrorMessage("No Error");

            Txns txns = new Txns();
            txns.setUserId(sboCallBackBonusReq.getUsername());
            //玩家货币代码
            txns.setCurrency(gameParentPlatform.getCurrencyType());
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
            txns.setBetAmount(betAmount);
            txns.setWinningAmount(betAmount);
            txns.setWinningAmount(betAmount);
            txns.setPlatformTxId(sboCallBackBonusReq.getTransferCode());
            txns.setRoundId(sboCallBackBonusReq.getTransactionId());
            txns.setBetTime(sboCallBackBonusReq.getBonusTime());
            txns.setGameType(String.valueOf(sboCallBackBonusReq.getGameType()));
            txns.setBalance(balance);
            txns.setStatus("Running");
            txns.setMethod("Bonus");
            String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

        }

        return sboCallBackCommResp;
    }

    //归还注额
    public Object returnStake(SboCallBackReturnStakeReq sboCallBackBonusReq,String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackBonusReq.getUsername());
        if(null == sboCallBackBonusReq.getUsername() || "".equals(sboCallBackBonusReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackBonusReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackBonusReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
        } else {
            BigDecimal balance = memBaseinfo.getBalance();
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, sboCallBackBonusReq.getTransferCode());
            wrapper.eq(Txns::getRoundId, sboCallBackBonusReq.getTransactionId());
            wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
//            wrapper.eq(Txns::getUserId, sboCallBackBonusReq.getUsername());
//            wrapper.eq(Txns::getPlatform, sboCallBackBonusReq.getProductType());
//            wrapper.eq(Txns::getGameType, sboCallBackBonusReq.getGameType());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            if(null==oldTxns){
                sboCallBackCommResp.setErrorCode(6);
                sboCallBackCommResp.setErrorMessage("Bet not exists");
                return sboCallBackCommResp;
            }
            if(oldTxns.getMethod().equals("Cancel Bet")){
                sboCallBackCommResp.setErrorCode(5003);
                sboCallBackCommResp.setErrorMessage("Bet With Same RefNo Exists");
                return sboCallBackCommResp;
            }
            if(null!=oldTxns.getRealWinAmount()&&oldTxns.getRealWinAmount().compareTo(BigDecimal.ZERO) == 1){
                sboCallBackCommResp.setErrorCode(5008);
                sboCallBackCommResp.setErrorMessage("Bet Already Returned Stake");
                return sboCallBackCommResp;
            }
            BigDecimal betAmount = oldTxns.getBetAmount();

            //真实返还金额
            BigDecimal realWinAmount = null!=sboCallBackBonusReq.getCurrentStake()?sboCallBackBonusReq.getCurrentStake().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
            balance = balance.add(realWinAmount);
            gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.REFUND, TradingEnum.INCOME);

            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            sboCallBackCommResp.setErrorCode(0);
            sboCallBackCommResp.setErrorMessage("No Error");

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setId(null);
            txns.setBetAmount(betAmount.subtract(realWinAmount));
            txns.setWinningAmount(betAmount.subtract(realWinAmount).negate());
            txns.setWinAmount(betAmount.subtract(realWinAmount));
            txns.setRealWinAmount(realWinAmount);
            txns.setBalance(balance);
            txns.setStatus("Running");
            txns.setMethod("Place Bet");
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
    public Object getBetStatus(SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq,String ip) {
        SboCallBackGetBetStatusResp sboCallBackGetBetStatusResp = new SboCallBackGetBetStatusResp();
        sboCallBackGetBetStatusResp.setTransactionId(sboCallBackGetBetStatusReq.getTransactionId());
        sboCallBackGetBetStatusResp.setTransferCode(sboCallBackGetBetStatusReq.getTransferCode());
        if(null == sboCallBackGetBetStatusReq.getUsername() || "".equals(sboCallBackGetBetStatusReq.getUsername())){
            sboCallBackGetBetStatusResp.setErrorCode(3);
            sboCallBackGetBetStatusResp.setErrorMessage("Username empty");
            return sboCallBackGetBetStatusResp;
        }
        if(!verifyKey(sboCallBackGetBetStatusReq.getCompanyKey())){
            sboCallBackGetBetStatusResp.setErrorCode(4);
            sboCallBackGetBetStatusResp.setErrorMessage("CompanyKey Error");
            return sboCallBackGetBetStatusResp;
        }
        if(!checkIp(ip)){
            sboCallBackGetBetStatusResp.setErrorCode(2);
            sboCallBackGetBetStatusResp.setErrorMessage("Invalid Ip");
            return sboCallBackGetBetStatusResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackGetBetStatusReq.getUsername());

        if (null == memBaseinfo) {
            sboCallBackGetBetStatusResp.setErrorCode(1);
            sboCallBackGetBetStatusResp.setErrorMessage("Member not exist");
        } else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, sboCallBackGetBetStatusReq.getTransferCode());
            if(9==sboCallBackGetBetStatusReq.getProductType()) {//无缝游戏完美真人赌场，相同的transferCode且相同的transactionId不能被扣除投注金额两次，但相同的transferCode可以搭配不同的transactionId再扣除投注金额
                wrapper.eq(Txns::getRoundId, sboCallBackGetBetStatusReq.getTransactionId());
            }
            wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            String status = "";
            if (null == oldTxns) {
                sboCallBackGetBetStatusResp.setErrorCode(6);
                sboCallBackGetBetStatusResp.setErrorMessage("Member not exist");
                return sboCallBackGetBetStatusResp;
            } else {
                if("Place Bet".equals(oldTxns.getMethod())){
                    status = "running";
                }
                if("Settle".equals(oldTxns.getMethod())){
                    status = "settled";
                }
                if("Cancel Bet".equals(oldTxns.getMethod())){
                    status = "void";
                }
                sboCallBackGetBetStatusResp.setStatus(status);
                sboCallBackGetBetStatusResp.setStake(null == oldTxns.getBetAmount() ? "" : oldTxns.getBetAmount().toString());
                sboCallBackGetBetStatusResp.setWinloss(null == oldTxns.getWinAmount() ? "" : oldTxns.getWinAmount().toString());
                sboCallBackGetBetStatusResp.setErrorCode(0);
                sboCallBackGetBetStatusResp.setErrorMessage("No Error");
            }
        }

        return sboCallBackGetBetStatusResp;
    }

//    //转帐交易
//    public Object transfer(SboCallBackTransferReq sboCallBackTransferReq) {
//        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackTransferReq.getUsername());
//        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
//        sboCallBackCommResp.setAccountName(sboCallBackTransferReq.getUsername());
//        if (null == memBaseinfo) {
//            sboCallBackCommResp.setErrorCode(1);
//            sboCallBackCommResp.setErrorMessage("Member not exist");
//        } else {
//            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(Txns::getStatus, "Transfer");
//            wrapper.eq(Txns::getPlatformTxId, sboCallBackTransferReq.getTransferRefno());
//            wrapper.eq(Txns::getRoundId, sboCallBackTransferReq.getGpid());
//            wrapper.eq(Txns::getUserId, sboCallBackTransferReq.getUsername());
//            wrapper.eq(Txns::getPlatform, sboCallBackTransferReq.getProductType());
//            wrapper.eq(Txns::getGameType, sboCallBackTransferReq.getGameType());
//            Txns oldTxns = txnsMapper.selectOne(wrapper);
//            BigDecimal balance = BigDecimal.valueOf(0D);
//            if (null == oldTxns) {
//                BigDecimal amount = BigDecimal.valueOf(Double.valueOf(sboCallBackTransferReq.getAmount()));
//                if ("131".equals(sboCallBackTransferReq.getTransferType())) {//131转入
//                    balance = memBaseinfo.getBalance().add(amount);
//                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
//                } else {//130转出
//                    if (memBaseinfo.getBalance().compareTo(amount) == -1) {
//                        sboCallBackCommResp.setBalance(null == memBaseinfo.getBalance() ? "" : memBaseinfo.getBalance().toString());
//                        sboCallBackCommResp.setErrorCode(5);
//                        sboCallBackCommResp.setErrorMessage("Not enough balance");
//                        return sboCallBackCommResp;
//                    } else {
//                        balance = memBaseinfo.getBalance().subtract(amount);
//                        gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
//                    }
//                }
//
//                Txns txns = new Txns();
//                txns.setUserId(sboCallBackTransferReq.getUsername());
//                txns.setBetAmount(amount);
//                txns.setPlatformTxId(sboCallBackTransferReq.getTransferRefno());
//                txns.setRoundId(sboCallBackTransferReq.getGpid());
//                txns.setBetTime(sboCallBackTransferReq.getTransferTime());
//                txns.setPlatform(sboCallBackTransferReq.getProductType());
//                txns.setGameType(sboCallBackTransferReq.getGameType());
//                txns.setBalance(balance);
////                txns.setTransferType(sboCallBackTransferReq.getTransferType());
//                txns.setStatus("Transfer");
//                txns.setMethod("Transfer");
//                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
//                txns.setCreateTime(dateStr);
//                txnsMapper.insert(txns);
//
//                sboCallBackCommResp.setBalance(balance.toString());
//                sboCallBackCommResp.setErrorCode(0);
//                sboCallBackCommResp.setErrorMessage("No Error");
//            } else {
//                sboCallBackCommResp.setBalance(null == memBaseinfo.getBalance() ? "" : memBaseinfo.getBalance().toString());
//                sboCallBackCommResp.setErrorCode(6);
//                sboCallBackCommResp.setErrorMessage("Member not exist");
//            }
//        }
//
//        return sboCallBackCommResp;
//    }
//
//    //转帐交易回滚
//    public Object rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq) {
//        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackRollbackTransferReq.getUsername());
//        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
//        sboCallBackCommResp.setAccountName(sboCallBackRollbackTransferReq.getUsername());
//        if (null == memBaseinfo) {
//            sboCallBackCommResp.setErrorCode(1);
//            sboCallBackCommResp.setErrorMessage("Member not exist");
//        } else {
//            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(Txns::getPlatformTxId, sboCallBackRollbackTransferReq.getTransferRefno());
//            wrapper.eq(Txns::getRoundId, sboCallBackRollbackTransferReq.getGpid());
//            wrapper.eq(Txns::getStatus, "Transfer");
//            wrapper.eq(Txns::getUserId, sboCallBackRollbackTransferReq.getUsername());
//            wrapper.eq(Txns::getPlatform, sboCallBackRollbackTransferReq.getProductType());
//            wrapper.eq(Txns::getGameType, sboCallBackRollbackTransferReq.getGameType());
//            Txns oldTxns = txnsMapper.selectOne(wrapper);
//            BigDecimal balance = BigDecimal.valueOf(0D);
//            if (null != oldTxns) {
//                BigDecimal amount = oldTxns.getBetAmount();
////                if("131".equals(oldTxns.getTransferType())){//131转入
////                    balance = memBaseinfo.getBalance().subtract(amount);
////                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
////
////                }else {//130转出
////                    balance = memBaseinfo.getBalance().add(amount);
////                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
////                }
//
//                Txns txns = new Txns();
//                txns.setUserId(sboCallBackRollbackTransferReq.getUsername());
//                txns.setBetAmount(amount);
//                txns.setPlatformTxId(sboCallBackRollbackTransferReq.getTransferRefno());
//                txns.setRoundId(sboCallBackRollbackTransferReq.getGpid());
//                txns.setBetTime(oldTxns.getBetTime());
//                txns.setPlatform(sboCallBackRollbackTransferReq.getProductType());
//                txns.setGameType(sboCallBackRollbackTransferReq.getGameType());
//                txns.setBalance(balance);
//                txns.setStatus("RollbackTransfer");
//                txns.setMethod("RollbackTransfer");
//                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
//                txns.setCreateTime(dateStr);
//                txnsMapper.insert(txns);
//                oldTxns.setStatus("RollbackTransfer");
//                oldTxns.setUpdateTime(dateStr);
//                txnsMapper.updateById(oldTxns);
//
//                sboCallBackCommResp.setBalance(balance.toString());
//                sboCallBackCommResp.setErrorCode(0);
//                sboCallBackCommResp.setErrorMessage("No Error");
//            } else {
//                sboCallBackCommResp.setBalance(null == memBaseinfo.getBalance() ? "" : memBaseinfo.getBalance().toString());
//                sboCallBackCommResp.setErrorCode(6);
//                sboCallBackCommResp.setErrorMessage("Member not exist");
//            }
//        }
//
//        return sboCallBackCommResp;
//    }
//
//    //取得转帐交易状态
//    public Object getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq) {
//        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackGetTransferStautsReq.getUsername());
//        SboCallBackGetTransferStatusResp sboCallBackGetTransferStatusResp = new SboCallBackGetTransferStatusResp();
//        sboCallBackGetTransferStatusResp.setAccountName(sboCallBackGetTransferStautsReq.getUsername());
//        sboCallBackGetTransferStatusResp.setBalance(null == memBaseinfo.getBalance() ? "" : memBaseinfo.getBalance().toString());
//        if (null == memBaseinfo) {
//            sboCallBackGetTransferStatusResp.setErrorCode(1);
//            sboCallBackGetTransferStatusResp.setErrorMessage("Member not exist");
//        } else {
//            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(Txns::getPlatformTxId, sboCallBackGetTransferStautsReq.getTransferRefno());
//            wrapper.eq(Txns::getRoundId, sboCallBackGetTransferStautsReq.getGpid());
//            wrapper.eq(Txns::getStatus, "Transfer");
//            wrapper.eq(Txns::getUserId, sboCallBackGetTransferStautsReq.getUsername());
//            wrapper.eq(Txns::getPlatform, sboCallBackGetTransferStautsReq.getProductType());
//            wrapper.eq(Txns::getGameType, sboCallBackGetTransferStautsReq.getGameType());
//            Txns oldTxns = txnsMapper.selectOne(wrapper);
//            if (null != oldTxns) {
//                sboCallBackGetTransferStatusResp.setTransferStatus("2");
//                sboCallBackGetTransferStatusResp.setErrorCode(0);
//                sboCallBackGetTransferStatusResp.setAmount(null == oldTxns.getBetAmount() ? "" : oldTxns.getBetAmount().toString());
//                sboCallBackGetTransferStatusResp.setErrorMessage("No Error");
//            } else {
//                sboCallBackGetTransferStatusResp.setTransferStatus(1);
//                sboCallBackGetTransferStatusResp.setErrorCode(0);
//                sboCallBackGetTransferStatusResp.setErrorMessage("No Error");
//            }
//        }
//
//        return sboCallBackGetTransferStatusResp;
//    }

    //LiveCoin購買
    public Object liveCoinTransaction(SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq,String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        SboCallBackCommResp sboCallBackCommResp = new SboCallBackCommResp();
        sboCallBackCommResp.setAccountName(sboCallBackLiveCoinTransactionReq.getUsername());
        if(null == sboCallBackLiveCoinTransactionReq.getUsername() || "".equals(sboCallBackLiveCoinTransactionReq.getUsername())){
            sboCallBackCommResp.setErrorCode(3);
            sboCallBackCommResp.setErrorMessage("Username empty");
            return sboCallBackCommResp;
        }
        if(!verifyKey(sboCallBackLiveCoinTransactionReq.getCompanyKey())){
            sboCallBackCommResp.setErrorCode(4);
            sboCallBackCommResp.setErrorMessage("CompanyKey Error");
            return sboCallBackCommResp;
        }
        if(!checkIp(ip)){
            sboCallBackCommResp.setErrorCode(2);
            sboCallBackCommResp.setErrorMessage("Invalid Ip");
            return sboCallBackCommResp;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sboCallBackLiveCoinTransactionReq.getUsername());
        if (null == memBaseinfo) {
            sboCallBackCommResp.setErrorCode(1);
            sboCallBackCommResp.setErrorMessage("Member not exist");
        } else {
            BigDecimal betAmount = BigDecimal.valueOf(Double.valueOf(sboCallBackLiveCoinTransactionReq.getAmount())).multiply(gameParentPlatform.getCurrencyPro());
            BigDecimal balance = memBaseinfo.getBalance();
            sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
                sboCallBackCommResp.setErrorCode(5);
                sboCallBackCommResp.setErrorMessage("Not enough balance");
            } else {
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Txns::getMethod, "Place Bet");
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, sboCallBackLiveCoinTransactionReq.getTransferCode());
                wrapper.eq(Txns::getRoundId, sboCallBackLiveCoinTransactionReq.getTransactionId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.SBO_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if(null!=oldTxns){
                    sboCallBackCommResp.setErrorCode(5003);
                    sboCallBackCommResp.setErrorMessage("Bet With Same RefNo Exists");
                    return sboCallBackCommResp;
                }
                String platformCode = this.getpPlatformCode(sboCallBackLiveCoinTransactionReq.getProductType());

                GamePlatform gamePlatform;
                if(OpenAPIProperties.SBO_IS_PLATFORM_LOGIN.equals("Y")){
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.SBO_PLATFORM_CODE,OpenAPIProperties.SBO_PLATFORM_CODE);
                }else {
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode, OpenAPIProperties.SBO_PLATFORM_CODE);
                }
                GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                balance = balance.subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);
                sboCallBackCommResp.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
                sboCallBackCommResp.setErrorCode(0);
                sboCallBackCommResp.setErrorMessage("No Error");

                Txns txns = new Txns();
                txns.setUserId(sboCallBackLiveCoinTransactionReq.getUsername());
                //玩家货币代码
                txns.setCurrency(gameParentPlatform.getCurrencyType());
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
                txns.setBetAmount(betAmount);
                txns.setWinningAmount(betAmount.negate());
                txns.setWinAmount(betAmount);
                txns.setPlatformTxId(sboCallBackLiveCoinTransactionReq.getTransferCode());
                txns.setRoundId(sboCallBackLiveCoinTransactionReq.getTransactionId());
                txns.setBetTime(sboCallBackLiveCoinTransactionReq.getTranscationTime());
                txns.setGameType(String.valueOf(sboCallBackLiveCoinTransactionReq.getGameType()));
                txns.setBalance(balance);
                txns.setStatus("running");
                txns.setMethod("Place Bet");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
            }

        }

        return sboCallBackCommResp;
    }

    private boolean verifyKey(String key){
        if(null!=key && !"".equals(key)){
            if(OpenAPIProperties.SBO_KEY.equals(key)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SBO_PLATFORM_CODE);
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

    private String getpPlatformCode(int productType){
//        1	Sports Book	体育博彩
//        3	SBO Games	电子游戏
//        5	Virtual Sports	虚拟运动
//        7	SBO Live Casino	真人赌场
//        9	Seamless Game Provider	无缝游戏
//        10	Live Coin	線上貨幣
        String platformCode = "";
        if (1==productType){
            platformCode = "Sports Book";//体育博彩
        }
        if (3==productType){
            platformCode = "SBO Games";//电子游戏
        }
        if (5==productType){
            platformCode = "Virtual Sports";//虚拟运动
        }
        if (7==productType){
            platformCode = "SBO Live Casino";//真人赌场
        }
        if (9==productType){
            platformCode = "Seamless Game Provider";//无缝游戏
        }
        if (10==productType){
            platformCode = "Live Coin";//線上貨幣
        }
        return platformCode;
    }
}
