package com.indo.game.service.sgwin.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.sgwin.*;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.sgwin.SGWinCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SGWinCallbackServiceImpl implements SGWinCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object getVerifyApi(SGWinVerifyApiCallBackReq sgWinVerifyApiCallBackReq, String ip) {
        GameParentPlatform gameParentPlatform = getGameParentPlatform();
        SGWinVerifyApiResponseSucce sgWinVerifyApiResponseSucce = new SGWinVerifyApiResponseSucce();
        sgWinVerifyApiResponseSucce.setSuccess(false);
        // 校验IP
        if (checkIp(ip, gameParentPlatform)) {
            return sgWinVerifyApiResponseSucce;
        }
        sgWinVerifyApiResponseSucce.setSuccess(true);
        return sgWinVerifyApiResponseSucce;
    }

    @Override
    public Object getUserBalance(SGWinGetBalanceCallBackReq sgWinGetBalanceCallBackParentReq, String ip) {
        SGWinGetBalanceResponseSucce sgWinGetBalanceResponseSucce = new SGWinGetBalanceResponseSucce();
        try {

            GameParentPlatform gameParentPlatform = getGameParentPlatform();

            sgWinGetBalanceResponseSucce.setCode(0);
            sgWinGetBalanceResponseSucce.setMessage("Successful.");
            sgWinGetBalanceResponseSucce.setCurrency(sgWinGetBalanceCallBackParentReq.getCurrency());
            sgWinGetBalanceResponseSucce.setMemberId(sgWinGetBalanceCallBackParentReq.getMemberId());
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                sgWinGetBalanceResponseSucce.setCode(9999);
                sgWinGetBalanceResponseSucce.setMessage("System Error");
                return sgWinGetBalanceResponseSucce;
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sgWinGetBalanceCallBackParentReq.getMemberId());
            if (null == memBaseinfo) {
                sgWinGetBalanceResponseSucce.setCode(300);
                sgWinGetBalanceResponseSucce.setMessage("Missing Member");
                return sgWinGetBalanceResponseSucce;
            }
            sgWinGetBalanceResponseSucce.setBalance(memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            return sgWinGetBalanceResponseSucce;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sgWinGetBalanceResponseSucce.setCode(9999);
            sgWinGetBalanceResponseSucce.setMessage("System Error");
            return sgWinGetBalanceResponseSucce;
        }
    }

    // 下注
    @Override
    public Object sgwinBetCallback(SGWinBetsCallBackReq sgWinBetsCallBackReq, String ip) {
        SGWinBetResponseError error = new SGWinBetResponseError();
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            error.setCode(400);
            error.setMessage("Insufficient fund to be deduct.");
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                error.setCode(9999);
                error.setMessage("System Error");
                return error;
            }
            if (null==sgWinBetsCallBackReq.getMemberId()||"".equals(sgWinBetsCallBackReq.getMemberId())){
                error.setCode(200);
                error.setMessage("Invalid Parameter");
                return error;
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(sgWinBetsCallBackReq.getMemberId());
            if (null == memBaseinfo) {
                error.setCode(300);
                error.setMessage("Missing Member");
                return error;
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 下注金额
            BigDecimal betAmount = null!=sgWinBetsCallBackReq.getAmount()?sgWinBetsCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            // 下注金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                error.setCode(200);
                error.setMessage("Invalid Parameter");
                return error;
            }

            if (balance.compareTo(betAmount) < 0) {
                error.setCode(400);
                error.setMessage("Insufficient Fund");
                return error;
            }
            balance = balance.subtract(betAmount);
            String txnid = sgWinBetsCallBackReq.getTransactionId();
            SGWinBetResponseSucce succe = new SGWinBetResponseSucce();
            succe.setCode(0);
            succe.setMessage("Success");
            succe.setTransactionId(txnid);
            succe.setBalance(balance.divide(platformGameParent.getCurrencyPro()));
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount.subtract(betAmount),
                    GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);


            // 查询用户请求订单
            Txns oldTxns = getTxns(platformGameParent, txnid);
            if (null != oldTxns && "Place Bet".equals(oldTxns.getMethod())) {
                return succe;
            }

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(),platformGameParent.getPlatformCode());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(txnid);

            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台英文名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            //平台中文名称
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
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
            txns.setWinAmount(betAmount);
            txns.setWinningAmount(betAmount.negate());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(freeWinAmount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByString(sgWinBetsCallBackReq.getTransactionTime(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
//            txns.setRealWinAmount(freeWinAmount);
            //返还金额 (包含下注金额)
//            txns.setWinAmount(freeWinAmount);
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //操作名称
            txns.setMethod("Place Bet");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            int num = txnsMapper.insert(txns);

            succe.setBalance(balance.divide(platformGameParent.getCurrencyPro()));
            return succe;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            error.setCode(9999);
            error.setMessage("System Error");
            return error;
        }
    }

    // 结算注单,更新余额中奖
    @Override
    public Object notifySettle(SGWinNotifySettleCallBackReq sgWinNotifySettleCallBackReq, String ip) {
        SGWinNotifySettleResponseSucce succe = new SGWinNotifySettleResponseSucce();
        succe.setCode(9999);
        succe.setMessage("System Error");
        succe.setTransactionId(sgWinNotifySettleCallBackReq.getTransactionId());
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return succe;
            }
            String txnid = sgWinNotifySettleCallBackReq.getTransactionId();
            // 查询用户请求订单
            Txns oldTxns = getTxns(platformGameParent, txnid);
            if (null == oldTxns) {
                return succe;
            }
            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(oldTxns.getUserId());
            if (null == memBaseinfo) {
                succe.setCode(300);
                succe.setMessage("Missing Member");
                return succe;
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            BigDecimal amount = null!=sgWinNotifySettleCallBackReq.getIsResettle()?sgWinNotifySettleCallBackReq.getIsResettle().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            if(oldTxns.getBetAmount().compareTo(amount)==0){
                if("Settle".equals(oldTxns.getMethod())) {//取消结算
                    if (balance.compareTo(oldTxns.getWinAmount()) < 0) {
                        succe.setCode(400);
                        succe.setMessage("Insufficient Fund");
                        return succe;
                    }
                    balance = balance.subtract(oldTxns.getWinAmount());
                    gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getWinAmount(), GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setRoundId(txnid);
                    txns.setMethod("Place Bet");
                    txns.setStatus("Running");
                    txns.setWinningAmount(oldTxns.getBetAmount().negate());
                    txns.setWinAmount(oldTxns.getBetAmount());
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Settle");
                    String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                    oldTxns.setUpdateTime(dateStr);
                    int num = txnsMapper.updateById(oldTxns);
                }
            }else {//结算
                if("Settle".equals(oldTxns.getMethod())) {//重新结算
                    if(oldTxns.getWinAmount().compareTo(amount)!=0){
                        if(oldTxns.getWinAmount().compareTo(amount)==1) {
                            BigDecimal diff = oldTxns.getWinAmount().subtract(amount);
                            if (balance.compareTo(diff) < 0) {
                                succe.setCode(400);
                                succe.setMessage("Insufficient Fund");
                                return succe;
                            }
                            balance = balance.subtract(diff);
                            gameCommonService.updateUserBalance(memBaseinfo, diff, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                        }else {
                            balance = balance.add(amount.subtract(oldTxns.getWinAmount()));
                            gameCommonService.updateUserBalance(memBaseinfo, amount.subtract(oldTxns.getWinAmount()), GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        }
                        Txns txns = new Txns();
                        BeanUtils.copyProperties(oldTxns, txns);
                        txns.setRoundId(txnid);
                        txns.setMethod("Settle");
                        txns.setStatus("Running");
                        txns.setWinningAmount(amount);
                        txns.setWinAmount(amount);
                        txnsMapper.insert(txns);
                        oldTxns.setStatus("Settle");
                        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                        oldTxns.setUpdateTime(dateStr);
                        int num = txnsMapper.updateById(oldTxns);
                    }
                }else {
                    balance = balance.add(amount);
                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setRoundId(txnid);
                    txns.setMethod("Settle");
                    txns.setStatus("Running");
                    txns.setWinningAmount(amount);
                    txns.setWinAmount(amount);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Settle");
                    String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                    oldTxns.setUpdateTime(dateStr);
                    int num = txnsMapper.updateById(oldTxns);
                }

            }
            succe.setCode(0);
            succe.setMessage("Success");
            return succe;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return succe;
        }
    }

    @Override
    public Object refund(SGWinRefundCallBackReq<SGWinTransactionsCallBackReq> sgWinRefundCallBackReq, String ip) {
        SGWinRefundResponse sgWinRefundResponse = new SGWinRefundResponse();
        List list = new ArrayList<>();
        GameParentPlatform platformGameParent = getGameParentPlatform();
        for(SGWinTransactionsCallBackReq transactionsCallBackReq:sgWinRefundCallBackReq.getTransactions()) {
            SGWinRefundResponseSucce succe = new SGWinRefundResponseSucce();
            succe.setCode(0);
            succe.setMessage("Success");
            succe.setTransactionId(transactionsCallBackReq.getTransactionId());
            SGWinRefundResponseError error = new SGWinRefundResponseError();
            error.setCode(9999);
            error.setMessage("System has failed to process your request.");
            error.setTransactionId(transactionsCallBackReq.getTransactionId());
            // 校验IP
            if (checkIp(ip, platformGameParent)) {

                list.add(error);
                continue;
            }
            if (null==transactionsCallBackReq.getMemberId()||"".equals(transactionsCallBackReq.getMemberId())){
                error.setCode(200);
                error.setMessage("Invalid Parameter");
                list.add(error);
                continue;
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(transactionsCallBackReq.getMemberId());
            if (null == memBaseinfo) {
                error.setCode(300);
                error.setMessage("Missing Member");
                list.add(error);
                continue;
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 退回金额
            BigDecimal amount = null!=transactionsCallBackReq.getAmount()?transactionsCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;

            String refTransactionId = transactionsCallBackReq.getRefTransactionId();
            String txnid = transactionsCallBackReq.getTransactionId();
            // 查询用户请求订单
            Txns oldTxns = getTxns(platformGameParent, refTransactionId);
            if (null == oldTxns||(null != oldTxns&&"Cancel Bet".equals(oldTxns.getMethod()))) {
                succe.setBalance(balance.divide(platformGameParent.getCurrencyPro()));
                list.add(succe);
                continue;
            }

            balance = balance.add(amount);
            gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setRoundId(txnid);
            txns.setMethod("Cancel Bet");
            txns.setStatus("Running");
            txnsMapper.insert(txns);
            oldTxns.setStatus("Cancel");
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            oldTxns.setUpdateTime(dateStr);
            int num = txnsMapper.updateById(oldTxns);
            succe.setBalance(balance.divide(platformGameParent.getCurrencyPro()));
            list.add(succe);
            continue;
        }
        sgWinRefundResponse.setResponse(list);
        return sgWinRefundResponse;
    }

    /**
     * 查询第三方订单是否存在
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private Txns getTxns(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 查询IP是否被封
     *
     * @param ip ip
     * @return boolean
     */
    private boolean checkIp(String ip, GameParentPlatform platformGameParent) {
        if (null == platformGameParent) {
            return true;
        } else if (null == platformGameParent.getIpAddr() || "".equals(platformGameParent.getIpAddr())) {
            return false;
        }
        return !platformGameParent.getIpAddr().equals(ip);

    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.SGWIN_PLATFORM_CODE);
    }
}
