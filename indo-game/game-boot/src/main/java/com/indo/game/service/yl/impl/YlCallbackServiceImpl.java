package com.indo.game.service.yl.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.ps.PsCallBackParentReq;
import com.indo.game.pojo.dto.yl.YlCallBackReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ps.PsCallbackService;
import com.indo.game.service.yl.YlCallbackService;
import com.indo.user.pojo.bo.MemTradingBO;

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
public class YlCallbackServiceImpl implements YlCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Object ylGetBalanceCallback(JSONObject jsonObject) {
        JSONObject dataJson = new JSONObject();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(jsonObject.getString("userId"));
        if (null != memBaseinfo) {
            dataJson.put("status", 500);
            dataJson.put("msg", "system busy");
            return dataJson;
        }
        dataJson.put("status", 200);
        dataJson.put("balance", memBaseinfo.getBalance());
        return dataJson;
    }

    @Override
    public Object psBetCallback(JSONObject jsonObject) {
        JSONObject dataJson = new JSONObject();
        try {
            String userId = jsonObject.getString("userId");
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(jsonObject.getString("userId"));
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.YL_PLATFORM_CODE);
            GamePlatform gamePlatform;
            if("Y".equals(OpenAPIProperties.YL_IS_PLATFORM_LOGIN)){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCode(jsonObject.getString("gameId"));
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            BigDecimal betMoney = jsonObject.getBigDecimal("requireAmount");
            BigDecimal balance = memBaseinfo.getBalance();
            BigDecimal betAmount = jsonObject.getBigDecimal("validbet");
            if (memBaseinfo.getBalance().compareTo(betMoney) == -1) {
                dataJson.put("status", 500);
                dataJson.put("msg", "code:9003");
                return dataJson;
            }
            String txId = jsonObject.getString("txId");
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
            wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getPlatformTxId, txId);
            wrapper.eq(Txns::getUserId, memBaseinfo.getId());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if (null != oldTxns) {
                if ("Cancel Bet".equals(oldTxns.getMethod())) {
                    dataJson.put("status", 500);
                    dataJson.put("msg", "code:9002");
                    return dataJson;
                } else {
                    dataJson.put("status", 500);
                    dataJson.put("msg", "code:9002");
                    return dataJson;
                }
            }
            BigDecimal winAmount = jsonObject.getBigDecimal("profit");
            if (winAmount.compareTo(BigDecimal.ZERO) == 1) {//赢
                balance = balance.add(winAmount).add(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, winAmount.add(betAmount), GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            }
            if (memBaseinfo.getBalance().compareTo(betAmount.abs()) == -1) {
                dataJson.put("status", 500);
                dataJson.put("msg", "code:9003");
                return dataJson;
            }
            balance = balance.subtract(betAmount.abs());
            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

            Txns txns = new Txns();
            if(null!=oldTxns) {
                BeanUtils.copyProperties(oldTxns, txns);
            }else {
                //游戏商注单号
                txns.setPlatformTxId(txId);
                //此交易是否是投注 true是投注 false 否
                //玩家 ID
                txns.setUserId(userId);
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
                //游戏名称
                txns.setGameName(gamePlatform.getPlatformEnName());
                //下注金额
                txns.setBetAmount(betAmount);
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(winAmount);
                //玩家下注时间transTime
                txns.setBetTime(DateUtils.format(jsonObject.getDate("transTime"), DateUtils.newFormat));
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
                txns.setTurnover(betAmount);
                //辨认交易时间依据
                txns.setTxTime(null != jsonObject.getDate("transTime") ? DateUtils.format(jsonObject.getDate("transTime"), DateUtils.newFormat) : "");
                //操作名称
                txns.setMethod("Place Bet");
                txns.setStatus("Running");
                //余额
                txns.setBalance(balance);
                //创建时间
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                txns.setCreateTime(dateStr);
            }
            if (oldTxns != null) {
                //操作名称
                txns.setMethod("Settle");
                oldTxns.setStatus("Settle");
                txnsMapper.updateById(oldTxns);
            }
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                dataJson.put("status", 500);
                dataJson.put("msg", "code:9003");
                return dataJson;
            }
            dataJson.put("status", 200);
            dataJson.put("balance", balance);
            return dataJson;
        } catch (Exception e) {
            logger.error("YL捕鱼回调处理异常：", e);
            e.printStackTrace();
        }
        dataJson.put("status", 500);
        dataJson.put("msg", "code:9003");
        return dataJson;
    }

    @Override
    public Object ylVoidFishBetCallback(JSONObject jsonObject) {
        JSONObject dataJson = new JSONObject();
        try {
            String txId = jsonObject.getString("txId");
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(jsonObject.getString("userId"));
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getPromotionTxId, txId);
            wrapper.eq(Txns::getPlatform, jsonObject.getString("gameId"));
            wrapper.eq(Txns::getUserId, memBaseinfo.getId());
            Txns oldTxns = txnsMapper.selectOne(wrapper);
            if (null == oldTxns) {
                dataJson.put("status_code", "500");
                dataJson.put("message", "1029 settled transaction id");
                return dataJson;
            }
            BigDecimal balance = memBaseinfo.getBalance().add(oldTxns.getAmount());
            gameCommonService.updateUserBalance(memBaseinfo, oldTxns.getAmount(), GoldchangeEnum.REFUND, TradingEnum.INCOME);
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            //游戏商注单号
            txns.setPlatformTxId(txId);
            //混合码
            txns.setBalance(balance);
            txns.setId(null);
            txns.setMethod("Cancel Bet");
            txns.setStatus("Running");
            txns.setCreateTime(dateStr);

            txnsMapper.insert(txns);
            dataJson.put("status", 200);
            dataJson.put("balance", balance);
            return dataJson;
        } catch (Exception e) {
            logger.error("YL捕鱼取消捕鱼注單回调处理异常：", e);
            e.printStackTrace();
        }
        dataJson.put("status_code", "500");
        dataJson.put("message", "1029 settled transaction id");
        return dataJson;
    }

}

