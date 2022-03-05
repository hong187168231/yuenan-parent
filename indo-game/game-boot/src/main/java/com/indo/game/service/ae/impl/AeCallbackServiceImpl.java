package com.indo.game.service.ae.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.encrypt.Base64;
import com.indo.common.utils.encrypt.MD5;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.ae.AeCallBackTransferReq;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.callback.ae.AeCallBackRespFail;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceResp;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceRespSuccess;
import com.indo.game.service.ae.AeCallbackService;
import com.indo.game.service.common.GameCommonService;
import com.indo.user.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;


/**
 * AE电子
 *
 * @author
 */
@Service
public class AeCallbackServiceImpl implements AeCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object aeBalanceCallback(AeCallBackTransferReq aeApiRequestData, String ip) {
        //进行秘钥
        StringBuilder builder = new StringBuilder();
        builder.append(aeApiRequestData.getMerchantId()).append(aeApiRequestData.getCurrentTime());
        builder.append(aeApiRequestData.getAccountId()).append(aeApiRequestData.getCurrency());
        builder.append(Base64.encode(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
        String sign = MD5.md5(builder.toString());
        if (aeApiRequestData.getSign().equalsIgnoreCase(sign)) {
            String accountId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(accountId);
            if (null == memBaseinfo) {
                AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
                callBacekFail.setCode("2300");
                callBacekFail.setMsg("用户帐号参数错误");
                return JSONObject.toJSONString(callBacekFail);
            } else {
                AeGetBalanceRespSuccess getBalanceSuccess = new AeGetBalanceRespSuccess();
                getBalanceSuccess.setCode("0");
                getBalanceSuccess.setMsg("成功");
                AeGetBalanceResp data = new AeGetBalanceResp();
                data.setBalance(memBaseinfo.getBalance());
                data.setCurrency(aeApiRequestData.getCurrency());
                data.setBonusBalance(new BigDecimal(0));
                getBalanceSuccess.setData(data);
                return JSONObject.toJSONString(getBalanceSuccess);
            }
        }
        AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
        callBacekFail.setCode("9100");
        callBacekFail.setMsg("单一钱包不存在或无法取得");
        return JSONObject.toJSONString(callBacekFail);
    }

    @Override
    public Object aeTransfer(AeCallBackTransferReq aeApiRequestData, String ip) {
        try {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            if (!checkIp(ip)) {
                callBacekFail.setCode("1029");
                callBacekFail.setMsg("invalid IP address.");
                return JSONObject.toJSONString(callBacekFail);
            }
            String userId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
            if (null == memBaseinfo) {
                callBacekFail.setCode("2300");
                callBacekFail.setMsg("用户帐号参数错误");
                return JSONObject.toJSONString(callBacekFail);
            }

            //进行秘钥
            StringBuilder builder = new StringBuilder();
            builder.append(aeApiRequestData.getMerchantId()).append(aeApiRequestData.getCurrentTime());
            builder.append(aeApiRequestData.getAccountId()).append(aeApiRequestData.getCurrency());
            builder.append(aeApiRequestData.getTxnId()).append(aeApiRequestData.getTxnTypeId());
            builder.append(aeApiRequestData.getGameId());
            builder.append(Base64.encode(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
            String sign = MD5.md5(builder.toString());
            if (aeApiRequestData.getSign().equalsIgnoreCase(sign)) {
                //Bet(100), Adjust(200), LuckyDraw(300), Tournament(400)
                if (aeApiRequestData.getTxnTypeId() == 100) {
                    //下注及直接结算
                    return bet(aeApiRequestData, memBaseinfo, ip);
                } else if (aeApiRequestData.getTxnTypeId() == 200) {
                    //调整
                    return adjustBet(aeApiRequestData, memBaseinfo);
                } else if (aeApiRequestData.getTxnTypeId() == 300) {
                    //彩金
                    return luckyDraw(aeApiRequestData, memBaseinfo);
                }
            }
        } catch (Exception e) {
            logger.error("aeTransfer error", e);
            e.printStackTrace();
        }
        AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
        callBacekFail.setCode("1201");
        callBacekFail.setMsg("钱包余额过低");
        return JSONObject.toJSONString(callBacekFail);
    }

    private Object luckyDraw(AeCallBackTransferReq aeApiRequestData, MemTradingBO memBaseinfo) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Give");
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPromotionTxId, aeApiRequestData.getTxnId());
        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1038");
            callBacekFail.setMsg("Duplicate transaction.");
            return JSONObject.toJSONString(callBacekFail);
        }
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal amount = BigDecimal.valueOf(aeApiRequestData.getAmount());
        balance = balance.add(amount);
        gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        txns.setBalance(balance);
        txns.setMethod("Give");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        int number = txnsMapper.insert(txns);
        if (number <= 0) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1201");
            callBacekFail.setMsg("钱包余额过低");
            return JSONObject.toJSONString(callBacekFail);
        }
        AeGetBalanceRespSuccess getBalanceSuccess = new AeGetBalanceRespSuccess();
        getBalanceSuccess.setMsg("成功");
        getBalanceSuccess.setCode("0");
        AeGetBalanceResp data = new AeGetBalanceResp();
        data.setCurrency(aeApiRequestData.getCurrency());
        data.setBalance(balance);
        data.setTxnId(aeApiRequestData.getTxnId());
        data.setBonusBalance(new BigDecimal(0));
        data.setEventTime(dateStr);
        getBalanceSuccess.setData(data);
        return JSONObject.toJSONString(getBalanceSuccess);

    }

    @Override
    public Object query(AeCallBackTransferReq aeApiRequestData, String ip) {
        if (!checkIp(ip)) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1029");
            callBacekFail.setMsg("invalid IP address.");
            return JSONObject.toJSONString(callBacekFail);
        }
        String userId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
        if (null == memBaseinfo) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("2300");
            callBacekFail.setMsg("用户帐号参数错误");
            return JSONObject.toJSONString(callBacekFail);
        }

        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getPlatformTxId, aeApiRequestData.getTxnId());
        wrapper.eq(Txns::getPlatform, aeApiRequestData.getMerchantId());
        wrapper.eq(Txns::getUserId, userId);
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("2301");
            callBacekFail.setMsg("订单编号参数错误");
            return JSONObject.toJSONString(callBacekFail);
        }

        AeGetBalanceRespSuccess getBalanceSuccess = new AeGetBalanceRespSuccess();
        getBalanceSuccess.setCode("0");
        getBalanceSuccess.setMsg("成功");
        AeGetBalanceResp data = new AeGetBalanceResp();
        data.setBalance(oldTxns.getBalance());
        data.setCurrency(aeApiRequestData.getCurrency());
        data.setAmount(new BigDecimal(aeApiRequestData.getAmount()));
        data.setAccountId(aeApiRequestData.getAccountId());
        data.setTxnId(aeApiRequestData.getTxnId());
        data.setBonusBalance(new BigDecimal(0));
        data.setEventTime(oldTxns.getCreateTime());
        getBalanceSuccess.setData(data);
        return JSONObject.toJSONString(getBalanceSuccess);
    }


    /**
     * 下注
     */
    private Object bet(AeCallBackTransferReq aeApiRequestData, MemTradingBO memBaseinfo, String ip) {
        String userId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(aeApiRequestData.getMerchantId());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal betAmount = new BigDecimal(aeApiRequestData.getBetAmount());
        if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1018");
            callBacekFail.setMsg("Not Enough Balance");
            return JSONObject.toJSONString(callBacekFail);
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, aeApiRequestData.getTxnId());
        wrapper.eq(Txns::getPlatform, aeApiRequestData.getMerchantId());
        wrapper.eq(Txns::getUserId, userId);
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
                callBacekFail.setCode("1043");
                callBacekFail.setMsg("Bet has canceled");
                return JSONObject.toJSONString(callBacekFail);
            } else {
                AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
                callBacekFail.setCode("1038");
                callBacekFail.setMsg("Duplicate transaction.");
                return JSONObject.toJSONString(callBacekFail);
            }
        }

        if (new BigDecimal(aeApiRequestData.getWinAmount()).compareTo(BigDecimal.ZERO) == 1) {//赢
            balance = balance.add(betAmount);
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
        }
        if (new BigDecimal(aeApiRequestData.getWinAmount()).compareTo(BigDecimal.ZERO) == -1) {//输
            if (memBaseinfo.getBalance().compareTo(betAmount.abs()) == -1) {
                AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
                callBacekFail.setCode("1201");
                callBacekFail.setMsg("钱包余额过低");
                return JSONObject.toJSONString(callBacekFail);
            }
            balance = balance.subtract(betAmount.abs());
            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
        }
        balance = balance.subtract(betAmount);
        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(aeApiRequestData.getTxnId());
        //此交易是否是投注 true是投注 false 否
        //玩家 ID
        txns.setUserId(userId);
        //玩家货币代码
        txns.setCurrency(aeApiRequestData.getCurrency());
        //平台代码
        txns.setPlatform(aeApiRequestData.getMerchantId());
        //平台英文名称
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
        txns.setBetAmount(new BigDecimal(aeApiRequestData.getBetAmount()));
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(new BigDecimal(aeApiRequestData.getWinAmount()));
        //玩家下注时间
        txns.setBetTime(DateUtils.format(aeApiRequestData.getBetTime(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(new BigDecimal(aeApiRequestData.getAmount()));
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(new BigDecimal(aeApiRequestData.getWinAmount()));
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        if (new BigDecimal(aeApiRequestData.getWinAmount()).compareTo(BigDecimal.ZERO) == 0) {
            resultTyep = 2;
        } else if (new BigDecimal(aeApiRequestData.getWinAmount()).compareTo(BigDecimal.ZERO) == 1) {
            resultTyep = 0;
        } else {
            resultTyep = 1;
        }
        txns.setResultType(resultTyep);
        //有效投注金额 或 投注面值
        txns.setTurnover(new BigDecimal(aeApiRequestData.getAmount()));
        //辨认交易时间依据
        txns.setTxTime(null != aeApiRequestData.getBetTime() ? DateUtils.format(aeApiRequestData.getBetTime(), DateUtils.newFormat) : "");
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
        if (oldTxns != null) {
            txns.setStatus("Settle");
            txnsMapper.updateById(oldTxns);
        }
        int num = txnsMapper.insert(txns);
        if (num <= 0) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1201");
            callBacekFail.setMsg("钱包余额过低");
            return JSONObject.toJSONString(callBacekFail);
        }
        AeGetBalanceRespSuccess getBalanceSuccess = new AeGetBalanceRespSuccess();
        getBalanceSuccess.setCode("0");
        getBalanceSuccess.setMsg("成功");
        AeGetBalanceResp data = new AeGetBalanceResp();
        data.setBalance(balance);
        data.setCurrency(aeApiRequestData.getCurrency());
        data.setTxnId(aeApiRequestData.getTxnId());
        data.setBonusBalance(new BigDecimal(0));
        data.setEventTime(dateStr);
        getBalanceSuccess.setData(data);
        return JSONObject.toJSONString(getBalanceSuccess);

    }

    private Object adjustBet(AeCallBackTransferReq aeApiRequestData, MemTradingBO memBaseinfo) {
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getPlatform, aeApiRequestData.getMerchantId());
        wrapper.eq(Txns::getPlatformTxId, aeApiRequestData.getSourceTxnId());
        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("2301");
            callBacekFail.setMsg("订单编号参数错误");
            return JSONObject.toJSONString(callBacekFail);
        } else if ("Cancel Bet".equals(oldTxns.getMethod())) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1043");
            callBacekFail.setMsg("Bet has canceled.");
            return JSONObject.toJSONString(callBacekFail);
        }

        BigDecimal realWinAmount = BigDecimal.valueOf(Double.valueOf(aeApiRequestData.getAmount()));
        balance = balance.add(realWinAmount);
        gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.ADJUST_BET, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        txns.setBalance(balance);
        txns.setId(null);
        txns.setStatus("Running");
        txns.setMethod("Adjust Bet");
        txns.setRealWinAmount(realWinAmount);//真实返还金额
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);
        oldTxns.setStatus("Adjust");
        oldTxns.setUpdateTime(dateStr);
        txnsMapper.updateById(oldTxns);


        AeGetBalanceRespSuccess getBalanceSuccess = new AeGetBalanceRespSuccess();
        getBalanceSuccess.setCode("0");
        getBalanceSuccess.setMsg("成功");
        AeGetBalanceResp data = new AeGetBalanceResp();
        data.setBalance(balance);
        data.setCurrency(aeApiRequestData.getCurrency());
        data.setBonusBalance(new BigDecimal(0));
        data.setTxnId(aeApiRequestData.getTxnId());
        data.setEventTime(dateStr);
        getBalanceSuccess.setData(data);
        return JSONObject.toJSONString(getBalanceSuccess);
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode("AE");
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }
}

