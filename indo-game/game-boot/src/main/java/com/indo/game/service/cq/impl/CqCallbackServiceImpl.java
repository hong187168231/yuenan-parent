package com.indo.game.service.cq.impl;

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
import com.indo.game.pojo.dto.cq.CqBetCallBackReq;
import com.indo.game.pojo.dto.cq.CqCallBackParentReq;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.vo.callback.ae.AeCallBackRespFail;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceResp;
import com.indo.game.pojo.vo.callback.ae.AeGetBalanceRespSuccess;
import com.indo.game.pojo.vo.callback.cq.CqDataResp;
import com.indo.game.pojo.vo.callback.cq.CqRespSuccess;
import com.indo.game.pojo.vo.callback.cq.CqStatusResp;
import com.indo.game.service.ae.AeCallbackService;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cq.CqCallbackService;
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
 * CQ9
 *
 * @author
 */
@Service
public class CqCallbackServiceImpl implements CqCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    /**
     * 下注
     */
    public Object cqBetCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cqApiRequestData.getAccount());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.CQ_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(cqApiRequestData.getGamecode(),gameParentPlatform.getPlatformCode());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal betAmount = new BigDecimal(cqApiRequestData.getAmount());
        if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
            return commonReturnFail();
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPromotionTxId, cqApiRequestData.getMtcode());
        wrapper.eq(Txns::getPlatform, cqApiRequestData.getPlatform());
        wrapper.eq(Txns::getUserId, memBaseinfo.getAccount());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                return commonReturnFail();
            } else {
                return commonReturnFail();
            }
        }

        balance = balance.subtract(betAmount);
        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(cqApiRequestData.getMtcode());
        //混合码
        txns.setRoundId(cqApiRequestData.getRoundid());
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
        txns.setBetAmount(new BigDecimal(cqApiRequestData.getAmount()));
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(cqApiRequestData.getEventTime(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(new BigDecimal(cqApiRequestData.getAmount()));
        //返还金额 (包含下注金额)
        //有效投注金额 或 投注面值
        txns.setTurnover(new BigDecimal(cqApiRequestData.getAmount()));
        //辨认交易时间依据
        txns.setTxTime(null != cqApiRequestData.getEventTime() ? DateUtils.formatByString(cqApiRequestData.getEventTime(), DateUtils.newFormat) : "");
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
            return commonReturnFail();
        }

        return commonReturnSuccess(balance);

    }

    @Override
    public Object cqPayOffCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cqApiRequestData.getAccount());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getMethod, "Give");
        ;
        wrapper.eq(Txns::getPromotionTxId, cqApiRequestData.getMtcode());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            return commonReturnFail();
        }
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal amount = BigDecimal.valueOf(cqApiRequestData.getAmount());
        balance = balance.add(amount);
        gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        //游戏商注单号
        txns.setPlatformTxId(cqApiRequestData.getMtcode());
        //混合码
        txns.setRoundId(cqApiRequestData.getRoundid());
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
        return commonReturnSuccess(balance);

    }

    @Override
    public Object cqBonusCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cqApiRequestData.getAccount());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Give");
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPromotionTxId, cqApiRequestData.getMtcode());
        ;
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            return commonReturnFail();
        }
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal amount = BigDecimal.valueOf(cqApiRequestData.getAmount());
        balance = balance.add(amount);
        gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        //游戏商注单号
        txns.setPlatformTxId(cqApiRequestData.getMtcode());
        //混合码
        txns.setRoundId(cqApiRequestData.getRoundid());
        ;
        txns.setBalance(balance);
        txns.setMethod("Bonus");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        int number = txnsMapper.insert(txns);
        if (number <= 0) {
            return commonReturnFail();
        }
        return commonReturnSuccess(balance);
    }

    @Override
    public Object cqCreditCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cqApiRequestData.getAccount());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPromotionTxId, cqApiRequestData.getMtcode());
        ;
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            return commonReturnFail();
        }

        BigDecimal balance = memBaseinfo.getBalance().add(new BigDecimal(cqApiRequestData.getAmount()));
        gameCommonService.updateUserBalance(memBaseinfo, new BigDecimal(cqApiRequestData.getAmount()), GoldchangeEnum.REFUND, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        //游戏商注单号
        txns.setPlatformTxId(cqApiRequestData.getMtcode());
        //混合码
        txns.setRoundId(cqApiRequestData.getRoundid());
        txns.setBalance(balance);
        txns.setId(null);
        txns.setMethod("Refund");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);

        return commonReturnSuccess(balance);
    }

    @Override
    public Object cqDebitCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cqApiRequestData.getAccount());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPromotionTxId, cqApiRequestData.getMtcode());
        ;
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            return commonReturnFail();
        }

        BigDecimal balance = memBaseinfo.getBalance().subtract(new BigDecimal(cqApiRequestData.getAmount()));
        gameCommonService.updateUserBalance(memBaseinfo, new BigDecimal(cqApiRequestData.getAmount()), GoldchangeEnum.REFUND, TradingEnum.INCOME);
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        //游戏商注单号
        txns.setPlatformTxId(cqApiRequestData.getMtcode());
        //混合码
        txns.setRoundId(cqApiRequestData.getRoundid());
        txns.setBalance(balance);
        txns.setId(null);
        txns.setMethod("Refund");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);

        return commonReturnSuccess(balance);
    }

    @Override
    public Object cqRollinCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cqApiRequestData.getAccount());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Place Bet");
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, cqApiRequestData.getMtcode());
        ;
        Txns oldTxns = txnsMapper.selectOne(wrapper);

        BigDecimal betAmount = oldTxns.getBetAmount();
        BigDecimal balance = BigDecimal.valueOf(0D);
        if (new BigDecimal(cqApiRequestData.getWin()).compareTo(BigDecimal.ZERO) == 1) {//赢
            balance = balance.add(betAmount);
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
        }
        if (new BigDecimal(cqApiRequestData.getWin()).compareTo(BigDecimal.ZERO) == -1) {//输
            if (memBaseinfo.getBalance().compareTo(betAmount.abs()) == -1) {
                return commonReturnFail();
            }
            balance = balance.subtract(betAmount.abs());
            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
        }

        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        txns.setId(null);
        txns.setBetAmount(oldTxns.getBetAmount());
        //游戏商注单号
        txns.setPlatformTxId(cqApiRequestData.getMtcode());
        //混合码
        txns.setRoundId(cqApiRequestData.getRoundid());
        txns.setGameType(cqApiRequestData.getGametype());
        int resultTyep;
        if (new BigDecimal(cqApiRequestData.getWin()).compareTo(BigDecimal.ZERO) == 0) {
            resultTyep = 2;
        } else if (new BigDecimal(cqApiRequestData.getWin()).compareTo(BigDecimal.ZERO) == 1) {
            resultTyep = 0;
        } else {
            resultTyep = 1;
        }
        txns.setResultType(resultTyep);
        txns.setWinAmount(new BigDecimal(cqApiRequestData.getWin()));
        txns.setBalance(balance);
        txns.setStatus("Running");
        txns.setMethod("Settle");
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);
        oldTxns.setStatus("Settle");
        oldTxns.setUpdateTime(dateStr);
        txnsMapper.updateById(oldTxns);
        return commonReturnSuccess(balance);
    }

    @Override
    public Object cqBalanceCallback(String account, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
        if (null == memBaseinfo) {
            return commonReturnFail();
        } else {
            return commonReturnSuccess(memBaseinfo.getBalance());
        }
    }

    @Override
    public Object cqCheckPlayerCallback(String account, String ip, String wtoken) {
        if (!checkIp(ip) && !OpenAPIProperties.CQ_API_TOKEN.equals(wtoken)) {
            return commonReturnFail();
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(account);
        if (null == memBaseinfo) {
            return commonReturnFail();
        } else {
            String dataStr = DateUtils.format(new Date(), DateUtils.RFC3339_DATE_FORMAT);
            JSONObject jsonStruts = new JSONObject();
            jsonStruts.put("code", "0");
            jsonStruts.put("message", "Success");
            jsonStruts.put("datetime", dataStr);

            JSONObject jsonData = new JSONObject();
            jsonData.put("data", true);
            jsonData.put("status", jsonStruts);
            return jsonData;
        }
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.CQ_PLATFORM_CODE);
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

    private Object commonReturnSuccess(BigDecimal balance) {
        //返回
        CqRespSuccess getBalanceSuccess = new CqRespSuccess();
        CqStatusResp statusResp = new CqStatusResp();
        statusResp.setCode("0");
        statusResp.setMessage("Success");
        String dataStr = DateUtils.format(new Date(), DateUtils.RFC3339_DATE_FORMAT);
        statusResp.setDatetime(dataStr);
        CqDataResp dataResp = new CqDataResp();
        dataResp.setBalance(balance);
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.CQ_PLATFORM_CODE);
        dataResp.setCurrency(gameParentPlatform.getCurrencyType());
        getBalanceSuccess.setData(dataResp);
        getBalanceSuccess.setStatus(statusResp);
        return getBalanceSuccess;
    }

    private Object commonReturnFail() {
        //返回
        CqRespSuccess getBalanceSuccess = new CqRespSuccess();
        CqStatusResp statusResp = new CqStatusResp();
        statusResp.setCode("1");
        statusResp.setMessage("FAIL");
        String dataStr = DateUtils.format(new Date(), DateUtils.RFC3339_DATE_FORMAT);
        statusResp.setDatetime(dataStr);
        CqDataResp dataResp = new CqDataResp();
        getBalanceSuccess.setData(dataResp);
        getBalanceSuccess.setStatus(statusResp);
        return getBalanceSuccess;
    }
}

