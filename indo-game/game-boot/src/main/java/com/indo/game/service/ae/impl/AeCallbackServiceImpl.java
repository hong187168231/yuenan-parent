package com.indo.game.service.ae.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.encrypt.MD5;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.ae.AeCallBackParentReq;
import com.indo.game.pojo.dto.ae.AeCallBackTransferReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.ae.AeCallBackRespFail;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceResp;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceRespSuccess;
import com.indo.game.service.ae.AeCallbackService;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.core.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Base64;
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
    @Autowired
    private CptOpenMemberService externalService;

    @Override
    public Object aeBalanceCallback(AeCallBackParentReq aeApiRequestData, String ip) {
        //进行秘钥
        StringBuilder builder = new StringBuilder();
        builder.append(aeApiRequestData.getMerchantId()).append(aeApiRequestData.getCurrentTime());
        builder.append(aeApiRequestData.getAccountId()).append(aeApiRequestData.getCurrency());
        builder.append(Base64.getEncoder().encodeToString(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
        String sign = MD5.md5(builder.toString());
        if (aeApiRequestData.getSign().equalsIgnoreCase(sign)) {
//            String accountId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
            // 验证且绑定（AE-CPT第三方会员关系）
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(aeApiRequestData.getAccountId(), OpenAPIProperties.AE_PLATFORM_CODE);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getPassword());
            if (null == memBaseinfo) {
                AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
                callBacekFail.setCode("2300");
                callBacekFail.setMsg("用户帐号参数错误");
                return JSONObject.toJSON(callBacekFail);
            } else {
                AeGetBalanceRespSuccess getBalanceSuccess = new AeGetBalanceRespSuccess();
                getBalanceSuccess.setCode("0");
                getBalanceSuccess.setMsg("成功");
                AeGetBalanceResp data = new AeGetBalanceResp();
                data.setBalance(memBaseinfo.getBalance());
                data.setCurrency(aeApiRequestData.getCurrency());
                data.setBonusBalance(new BigDecimal(0));
                getBalanceSuccess.setData(data);
                return JSONObject.toJSON(getBalanceSuccess);
            }
        }
        AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
        callBacekFail.setCode("9100");
        callBacekFail.setMsg("单一钱包不存在或无法取得");
        return JSONObject.toJSON(callBacekFail);
    }

    @Override
    public Object aeTransfer(AeCallBackTransferReq aeApiRequestData, String ip) {
        try {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            if (!checkIp(ip)) {
                callBacekFail.setCode("1029");
                callBacekFail.setMsg("invalid IP address.");
                return JSONObject.toJSON(callBacekFail);
            }
//            String userId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
            CptOpenMember cptOpenMember = externalService.getCptOpenMember(aeApiRequestData.getAccountId(), OpenAPIProperties.AE_PLATFORM_CODE);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getPassword());
            if (null == memBaseinfo) {
                callBacekFail.setCode("2300");
                callBacekFail.setMsg("用户帐号参数错误");
                return JSONObject.toJSON(callBacekFail);
            }

            //进行秘钥
//            StringBuilder builder = new StringBuilder();
//            builder.append(aeApiRequestData.getMerchantId()).append(aeApiRequestData.getCurrentTime());
//            builder.append(aeApiRequestData.getAccountId()).append(aeApiRequestData.getCurrency());
//            builder.append(aeApiRequestData.getTxnId()).append(aeApiRequestData.getTxnTypeId());
//            builder.append(aeApiRequestData.getGameId());
//            builder.append(Base64.getEncoder().encodeToString(OpenAPIProperties.AE_MERCHANT_KEY.getBytes()));
//            String sign = MD5.md5(builder.toString());
//            if (aeApiRequestData.getSign().equalsIgnoreCase(sign)) {
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
//            } else {
//                callBacekFail.setCode("9200");
//                callBacekFail.setMsg("MD5验证失败");
//                return JSONObject.toJSON(callBacekFail);
//            }
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
        wrapper.eq(Txns::getMethod, "Bonus");
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPromotionTxId, aeApiRequestData.getTxnId());
//        wrapper.eq(Txns::getUserId, memBaseinfo.getAccount());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1038");
            callBacekFail.setMsg("Duplicate transaction.");
            return JSONObject.toJSON(callBacekFail);
        }
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal amount = aeApiRequestData.getAmount();
        balance = balance.add(amount);
        gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        txns.setBalance(balance);
        txns.setMethod("Bonus");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        int number = txnsMapper.insert(txns);
        if (number <= 0) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1201");
            callBacekFail.setMsg("钱包余额过低");
            return JSONObject.toJSON(callBacekFail);
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
        return JSONObject.toJSON(getBalanceSuccess);

    }

    @Override
    public Object query(AeCallBackTransferReq aeApiRequestData, String ip) {
        if (!checkIp(ip)) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1029");
            callBacekFail.setMsg("invalid IP address.");
            return JSONObject.toJSON(callBacekFail);
        }
//        String userId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
        CptOpenMember cptOpenMember = externalService.getCptOpenMember(aeApiRequestData.getAccountId(), OpenAPIProperties.AE_PLATFORM_CODE);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getPassword());
        if (null == memBaseinfo) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("2300");
            callBacekFail.setMsg("用户帐号参数错误");
            return JSONObject.toJSON(callBacekFail);
        }

        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getPlatformTxId, aeApiRequestData.getTxnId());
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.AE_PLATFORM_CODE);
//        wrapper.eq(Txns::getUserId, userId);
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("2301");
            callBacekFail.setMsg("订单编号参数错误");
            return JSONObject.toJSON(callBacekFail);
        }

        AeGetBalanceRespSuccess getBalanceSuccess = new AeGetBalanceRespSuccess();
        getBalanceSuccess.setCode("0");
        getBalanceSuccess.setMsg("成功");
        AeGetBalanceResp data = new AeGetBalanceResp();
        data.setBalance(oldTxns.getBalance());
        data.setCurrency(aeApiRequestData.getCurrency());
        data.setAmount(aeApiRequestData.getAmount());
        data.setAccountId(aeApiRequestData.getAccountId());
        data.setTxnId(aeApiRequestData.getTxnId());
        data.setBonusBalance(new BigDecimal(0));
        data.setEventTime(oldTxns.getCreateTime());
        getBalanceSuccess.setData(data);
        return JSONObject.toJSON(getBalanceSuccess);
    }


    /**
     * 下注
     */
    private Object bet(AeCallBackTransferReq aeApiRequestData, MemTradingBO memBaseinfo, String ip) {
//        String userId = aeApiRequestData.getAccountId().substring(aeApiRequestData.getAccountId().indexOf("_") + 1);
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AE_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(aeApiRequestData.getGameId(),gameParentPlatform.getPlatformCode());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal betAmount = aeApiRequestData.getBetAmount();
        BigDecimal winAmount = aeApiRequestData.getWinAmount();
        BigDecimal amount = winAmount.subtract(betAmount);
;
        if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1018");
            callBacekFail.setMsg("Not Enough Balance");
            return JSONObject.toJSON(callBacekFail);
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, aeApiRequestData.getTxnId());
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.AE_PLATFORM_CODE);
//        wrapper.eq(Txns::getUserId, userId);
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
                callBacekFail.setCode("1043");
                callBacekFail.setMsg("Bet has canceled");
                return JSONObject.toJSON(callBacekFail);
            } 
        }
        if (amount.compareTo(BigDecimal.ZERO) != 0) {
            if (amount.compareTo(BigDecimal.ZERO) == 1) {//赢
                balance = balance.add(amount);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }else {
                if (memBaseinfo.getBalance().compareTo(amount.abs()) == -1) {
                    AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
                    callBacekFail.setCode("1201");
                    callBacekFail.setMsg("钱包余额过低");
                    return JSONObject.toJSON(callBacekFail);
                }
                balance = balance.subtract(amount.abs());
                gameCommonService.updateUserBalance(memBaseinfo, amount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }
        }

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(aeApiRequestData.getTxnId());
        //此交易是否是投注 true是投注 false 否
        //玩家 ID
        txns.setUserId(memBaseinfo.getAccount());
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
        txns.setWinningAmount(amount);
        txns.setWinAmount(amount);
        //玩家下注时间
        txns.setBetTime(DateUtils.format(aeApiRequestData.getBetTime(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(betAmount);
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(winAmount);
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        if (winAmount.compareTo(BigDecimal.ZERO) == 1) {
            resultTyep = 0;
        } else {
            resultTyep = 1;
        }
        txns.setResultType(resultTyep);
        //有效投注金额 或 投注面值
        txns.setTurnover(aeApiRequestData.getAmount());
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
        int num = txnsMapper.insert(txns);
        if (num <= 0) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1201");
            callBacekFail.setMsg("钱包余额过低");
            return JSONObject.toJSON(callBacekFail);
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
        return JSONObject.toJSON(getBalanceSuccess);

    }

    private Object adjustBet(AeCallBackTransferReq aeApiRequestData, MemTradingBO memBaseinfo) {
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.AE_PLATFORM_CODE);
        wrapper.eq(Txns::getPlatformTxId, aeApiRequestData.getSourceTxnId());
//        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("2301");
            callBacekFail.setMsg("订单编号参数错误");
            return JSONObject.toJSON(callBacekFail);
        } else if ("Cancel Bet".equals(oldTxns.getMethod())) {
            AeCallBackRespFail callBacekFail = new AeCallBackRespFail();
            callBacekFail.setCode("1043");
            callBacekFail.setMsg("Bet has canceled.");
            return JSONObject.toJSON(callBacekFail);
        }

        BigDecimal realWinAmount = aeApiRequestData.getAmount();
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
        return JSONObject.toJSON(getBalanceSuccess);
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AE_PLATFORM_CODE);
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

