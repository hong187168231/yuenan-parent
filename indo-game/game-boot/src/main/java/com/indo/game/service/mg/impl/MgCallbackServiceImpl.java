package com.indo.game.service.mg.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.mg.MgCallBackReq;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.pg.PgCallBackResponse;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.mg.MgCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;


/**
 * MG
 *
 * @author
 */
@Service
public class MgCallbackServiceImpl implements MgCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object mgBalanceCallback(MgCallBackReq mgCallBackReq, String ip) {
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
        //进行秘钥
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(mgCallBackReq.getPlayerId());
        JSONObject dataJson = new JSONObject();
        if (null == memBaseinfo) {
            dataJson.put("code", "401");
            dataJson.put("message", "API 令牌過期或無效");
            return dataJson;
        } else {
            dataJson.put("balance", memBaseinfo.getBalance());
            dataJson.put("currency", platformGameParent.getCurrencyType());
            return dataJson;
        }
    }


    private boolean checkIp(String ip) {
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

    @Override
    public Object mgVerifyCallback(MgCallBackReq mgCallBackReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(mgCallBackReq.getPlayerId());
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        if (memBaseinfo == null) {
            dataJson.put("code", "401");
            dataJson.put("message", "API 令牌過期或無效");
            return dataJson;
        }
        dataJson.put("balance", memBaseinfo.getBalance());
        dataJson.put("currency", platformGameParent.getCurrencyType());
        return dataJson;
    }



    @Override
    public Object mgUpdatebalanceCallback(MgCallBackReq mgCallBackReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(mgCallBackReq.getPlayerId());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
        GamePlatform gamePlatform ;
        if(OpenAPIProperties.MG_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.MG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.MG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        JSONObject dataJson = new JSONObject();
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, mgCallBackReq.getTxnId());
        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
        }
        Txns txns = new Txns();
        BigDecimal amount = mgCallBackReq.getAmount();
        if ("CREDIT".equals(mgCallBackReq.getTxnType())) {//赢
            balance = balance.add(amount);
            if (null != oldTxns) {
                txns.setBet(false);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }else {
                txns.setBet(true);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            }
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount);
        }
        if ("DEBIT".equals(mgCallBackReq.getTxnType())) {//输
            if (memBaseinfo.getBalance().compareTo(amount) == -1) {
                dataJson.put("code", "402");
                dataJson.put("message", "余额不足");
                return dataJson;
            }
            balance = balance.subtract(amount);
            if (null != oldTxns) {
                txns.setBet(false);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }else {
                txns.setBet(true);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
            }
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount.negate());
        }



        //游戏商注单号
        txns.setPlatformTxId(mgCallBackReq.getTxnId());
        //此交易是否是投注 true是投注 false 否
        //玩家 ID
        txns.setUserId(memBaseinfo.getAccount());
        //玩家货币代码
        txns.setCurrency(gameParentPlatform.getCurrencyType());
        //平台代码
        txns.setPlatform(gameParentPlatform.getPlatformCode());
        //平台名称
        txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
        txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
        //平台游戏类型
        txns.setGameType(gameCategory.getGameType());
        //游戏分类ID
        txns.setCategoryId(gameCategory.getId());
        //游戏分类名称
        txns.setCategoryName(gameCategory.getGameName());
        //平台游戏代码
        txns.setGameCode(gamePlatform.getPlatformCode());

        txns.setWinAmount(amount);
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(amount);
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(amount);
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        //有效投注金额 或 投注面值
        txns.setTurnover(amount);
        //辨认交易时间依据
        txns.setTxTime(DateUtils.formatByString(mgCallBackReq.getCreationTime(),DateUtils.newFormat));
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
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            oldTxns.setStatus("Settle");
            oldTxns.setUpdateTime(dateStr);
            //下注金额
            txns.setBetAmount(oldTxns.getBetAmount());
            txnsMapper.updateById(oldTxns);
        }else {
            //下注金额
            txns.setBetAmount(mgCallBackReq.getAmount());
        }
        int num = txnsMapper.insert(txns);
        if (num <= 0) {
            dataJson.put("code", "400");
            dataJson.put("message", "无效请求");
            return dataJson;
        }
        dataJson.put("currency", mgCallBackReq.getCurrency());
        dataJson.put("balance_amount", balance);
        return dataJson;
    }

    @Override
    public Object rollback(MgCallBackReq mgCallBackReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(mgCallBackReq.getPlayerId());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.MG_PLATFORM_CODE);
//        GamePlatform gamePlatform ;
//        if(OpenAPIProperties.MG_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
//            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.MG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
//        }else {
//            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.MG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
//        }
//        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
//        PgCallBackResponse pgCallBackRespFail = new PgCallBackResponse();
        JSONObject dataJson = new JSONObject();
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, mgCallBackReq.getTxnId());
        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        if (oldTxns.getWinAmount().compareTo(BigDecimal.ZERO) == -1) {//小于0
            balance = balance.subtract(oldTxns.getWinAmount());
            if(oldTxns.getBet()) {
                gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getWinAmount(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
            }else {
                gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getWinAmount(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }
        }else {
            balance = balance.add(oldTxns.getWinAmount());
            if(oldTxns.getBet()) {
                gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getWinAmount(), GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            }else {
                gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getWinAmount(), GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }
        }

        txns.setMethod("Cancel Bet");
        txns.setStatus("Running");
        //余额
        txns.setBalance(balance);
        //创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        txns.setCreateTime(dateStr);
        //投注 IP
        txns.setBetIp(ip);//  string 是 投注 IP
        if (oldTxns != null) {
            //操作名称
            oldTxns.setStatus("Cancel Bet");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
        }
        int num = txnsMapper.insert(txns);
        if (num <= 0) {
            dataJson.put("code", "400");
            dataJson.put("message", "无效请求");
            return dataJson;
        }
        dataJson.put("currency", mgCallBackReq.getCurrency());
        dataJson.put("balance_amount", balance);
        return dataJson;
    }
}

