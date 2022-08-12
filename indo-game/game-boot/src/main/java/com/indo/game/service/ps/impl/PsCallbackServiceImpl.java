package com.indo.game.service.ps.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.ps.PsCallBackParentReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ps.PsCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.annotation.Resource;


/**
 * PS
 *
 * @author
 */
@Service
public class PsCallbackServiceImpl implements PsCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;

    private static final DecimalFormat format = new DecimalFormat("#");

    @Override
    public Object psVerifyCallback(PsCallBackParentReq psCallBackParentReq, String ip) {
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(psCallBackParentReq.getAccess_token(), OpenAPIProperties.PS_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (cptOpenMember == null) {
            dataJson.put("code", 1);
            dataJson.put("message", "Token 无效");
            return dataJson;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        if (cptOpenMember == null) {
            dataJson.put("code", 1);
            dataJson.put("message", "Token 无效");
            return dataJson;
        }
        String signPrice = format.format(memBaseinfo.getBalance().multiply(new BigDecimal(100)));
        dataJson.put("status_code", 0);
        dataJson.put("member_id", cptOpenMember.getId() + "");
        dataJson.put("member_name", cptOpenMember.getUserName());
        dataJson.put("balance", Integer.parseInt(signPrice));
        return dataJson;
    }

    @Override
    public Object psBetCallback(PsCallBackParentReq psbetCallBackReq, String ip) {
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(psbetCallBackReq.getAccess_token(), OpenAPIProperties.PS_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (cptOpenMember == null) {
            dataJson.put("status_code", "1");
            dataJson.put("message", "Token 无效");
            return dataJson;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PS_PLATFORM_CODE);
        GamePlatform gamePlatform;
        if("Y".equals(OpenAPIProperties.PS_IS_PLATFORM_LOGIN)){
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(String.valueOf(psbetCallBackReq.getGame_id()), gameParentPlatform.getPlatformCode());

        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        BigDecimal balance = memBaseinfo.getBalance();
//        BigDecimal betAmount = new BigDecimal(psbetCallBackReq.getTotal_bet()).divide(new BigDecimal(100));
        BigDecimal betAmount = new BigDecimal(psbetCallBackReq.getTotal_bet());
        if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
            dataJson.put("status_code", "3");
            dataJson.put("message", "馀额不足");
            return dataJson;
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, psbetCallBackReq.getTxn_id());
        wrapper.eq(Txns::getUserId, memBaseinfo.getAccount());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                dataJson.put("status_code", "2");
                dataJson.put("message", "单号无效");
                return dataJson;
            } else {
                dataJson.put("status_code", "2");
                dataJson.put("message", "单号无效");
                return dataJson;
            }
        }

        balance = balance.subtract(betAmount);
        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(psbetCallBackReq.getTxn_id());
        //混合码
        txns.setRoundId(psbetCallBackReq.getTxn_id());
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
        txns.setWinningAmount(betAmount.negate());
        txns.setWinAmount(betAmount);
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(psbetCallBackReq.getTs(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(betAmount);
        //返还金额 (包含下注金额)
        //有效投注金额 或 投注面值
        txns.setTurnover(betAmount);
        //辨认交易时间依据
        txns.setTxTime(null != psbetCallBackReq.getTs() ? DateUtils.formatByString(psbetCallBackReq.getTs(), DateUtils.newFormat) : "");
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
            dataJson.put("status_code", "2");
            dataJson.put("message", "单号无效");
            return dataJson;
        }
        String signPrice = format.format(balance);
        dataJson.put("status_code", 0);
        dataJson.put("balance", Integer.parseInt(signPrice));
        return dataJson;
    }

    @Override
    public Object psResultCallback(PsCallBackParentReq psbetCallBackReq, String ip) {
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(psbetCallBackReq.getAccess_token(), OpenAPIProperties.PS_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (cptOpenMember == null) {
            dataJson.put("status_code", "1");
            dataJson.put("message", "Token 无效");
            return dataJson;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getPlatformTxId, psbetCallBackReq.getTxn_id());
        wrapper.eq(Txns::getUserId, memBaseinfo.getAccount());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            dataJson.put("status_code", "2");
            dataJson.put("message", "单号无效");
            return dataJson;
        }
//        BigDecimal money = new BigDecimal(psbetCallBackReq.getTotal_win()).subtract(new BigDecimal(psbetCallBackReq.getBonus_win()));
        BigDecimal money = new BigDecimal(psbetCallBackReq.getTotal_win());
//        BigDecimal winAmount = money.divide(new BigDecimal(100));
        BigDecimal winAmount = money;
        BigDecimal balance = memBaseinfo.getBalance();
        balance = balance.add(winAmount);
        gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);

        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        txns.setId(null);
        txns.setBetAmount(oldTxns.getBetAmount());
        //游戏商注单号
        txns.setPlatformTxId(psbetCallBackReq.getTxn_id());
        int resultTyep = 0;
        txns.setResultType(resultTyep);
        txns.setWinningAmount(winAmount);
        txns.setWinAmount(winAmount);
        txns.setBalance(balance);
        txns.setStatus("Running");
        txns.setMethod("Settle");
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);
        oldTxns.setStatus("Settle");
        oldTxns.setUpdateTime(dateStr);
        txnsMapper.updateById(oldTxns);
        String signPrice = format.format(balance);
        dataJson.put("status_code", 0);
        dataJson.put("balance", Integer.parseInt(signPrice));
        return dataJson;
    }

    @Override
    public Object psRefundtCallback(PsCallBackParentReq psbetCallBackReq, String ip) {

        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(psbetCallBackReq.getAccess_token(), OpenAPIProperties.PS_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (cptOpenMember == null) {
            dataJson.put("status_code", "1");
            dataJson.put("message", "Token 无效");
            return dataJson;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, psbetCallBackReq.getTxn_id());
        wrapper.eq(Txns::getUserId, memBaseinfo.getAccount());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            dataJson.put("status_code", "2");
            dataJson.put("message", "单号无效");
            return dataJson;
        }
        BigDecimal balance = memBaseinfo.getBalance().add(oldTxns.getAmount());
        gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getAmount(), GoldchangeEnum.REFUND, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        //游戏商注单号
        txns.setPlatformTxId(psbetCallBackReq.getTxn_id());
        txns.setWinningAmount(oldTxns.getAmount());
        //混合码
        txns.setBalance(balance);
        txns.setId(null);
        txns.setMethod("Refund");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);
        String signPrice = format.format(balance);
        dataJson.put("status_code", 0);
        dataJson.put("balance", Integer.parseInt(signPrice));
        return dataJson;
    }

    @Override
    public Object psBonusCallback(PsCallBackParentReq psbetCallBackReq, String ip) {
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(psbetCallBackReq.getAccess_token(), OpenAPIProperties.PS_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (cptOpenMember == null) {
            dataJson.put("status_code", "1");
            dataJson.put("message", "Token 无效");
            return dataJson;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Give");
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, psbetCallBackReq.getTxn_id());
        wrapper.eq(Txns::getUserId, memBaseinfo.getAccount());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            dataJson.put("status_code", "2");
            dataJson.put("message", "单号无效");
            return dataJson;
        }
        BigDecimal balance = memBaseinfo.getBalance();
//        BigDecimal amount = new BigDecimal(psbetCallBackReq.getBonus_reward()).divide(new BigDecimal(100));
        BigDecimal amount = new BigDecimal(psbetCallBackReq.getBonus_reward());
        balance = balance.add(amount);
        gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        //游戏商注单号
        txns.setPlatformTxId(psbetCallBackReq.getTxn_id());
        //混合码
        txns.setRoundId(psbetCallBackReq.getTxn_id());
        txns.setWinningAmount(amount);
        txns.setBalance(balance);
        txns.setMethod("Bonus");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        int number = txnsMapper.insert(txns);
        if (number <= 0) {
            dataJson.put("status_code", "2");
            dataJson.put("message", "单号无效");
            return dataJson;
        }
        String signPrice = format.format(balance);
        dataJson.put("status_code", 0);
        dataJson.put("balance", Integer.parseInt(signPrice));
        return dataJson;
    }

    @Override
    public Object psGetBalanceCallback(PsCallBackParentReq psbetCallBackReq, String ip) {
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(psbetCallBackReq.getAccess_token(), OpenAPIProperties.PS_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (cptOpenMember == null) {
            dataJson.put("status_code", "1");
            dataJson.put("message", "Token 无效");
            return dataJson;
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        String signPrice = format.format(memBaseinfo.getBalance().multiply(new BigDecimal(100)));
        dataJson.put("status_code", 0);
        dataJson.put("balance", Integer.parseInt(signPrice));
        return dataJson;
    }
}

