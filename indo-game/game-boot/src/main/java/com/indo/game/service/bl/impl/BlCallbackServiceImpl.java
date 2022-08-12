package com.indo.game.service.bl.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.bl.BlCallBackReq;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.bl.BlCallbackService;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.core.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;


/**
 * BL
 *
 * @author
 */
@Service
public class BlCallbackServiceImpl implements BlCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object blBlanceCallback(BlCallBackReq blCallBackReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(blCallBackReq.getPlayer_account());
        GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.BL_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        JSONObject statusJson = new JSONObject();
        if (null == memBaseinfo) {
            dataJson.put("resp_msg", statusJson);
            JSONArray array = new JSONArray();
            statusJson.put("code", "-1");
            statusJson.put("message", "失败");
            statusJson.put("errors", array);
            return dataJson;
        } else {
            JSONObject dataObject = new JSONObject();
            JSONObject statusObject = new JSONObject();
            statusObject.put("code", 0);
            statusObject.put("msg", "success");
            dataObject.put("currency", platformGameParent.getCurrencyType());
            dataObject.put("balance", memBaseinfo.getBalance());
            dataJson.put("data", dataObject);
            dataJson.put("status", statusObject);
            return dataJson;
        }
    }

    @Override
    public Object blPlayerCallback(BlCallBackReq blCallBackReq, String ip) {
        JSONObject dataJson = new JSONObject();
        JSONObject statusJson = new JSONObject();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(blCallBackReq.getPlayer_account());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.BL_PLATFORM_CODE);
        GamePlatform gamePlatform;
        if(OpenAPIProperties.BL_IS_PLATFORM_LOGIN.equals("Y")){
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(gameParentPlatform.getPlatformCode(),gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(blCallBackReq.getGame_code(), gameParentPlatform.getPlatformCode());

        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, blCallBackReq.getReport_id());
        wrapper.eq(Txns::getUserId, memBaseinfo.getAccount());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (blCallBackReq.getType() == 10 || blCallBackReq.getType() == 12) {
            if (null != oldTxns) {
                dataJson.put("resp_msg", statusJson);
                JSONArray array = new JSONArray();
                statusJson.put("code", "3");
                statusJson.put("message", "订单重复操作");
                statusJson.put("errors", array);
                return dataJson;
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(blCallBackReq.getCost_info());
        if (blCallBackReq.getType() == 10) {
            if (memBaseinfo.getBalance().compareTo(jsonObject.getBigDecimal("bet_num")) == -1) {
                dataJson.put("resp_msg", statusJson);
                JSONArray array = new JSONArray();
                statusJson.put("code", "1");
                statusJson.put("message", "余额不足");
                statusJson.put("errors", array);
                return dataJson;
            }
            balance = balance.subtract(jsonObject.getBigDecimal("bet_num"));
            gameCommonService.updateUserBalance(memBaseinfo, jsonObject.getBigDecimal("bet_num"), GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);

        } else if (blCallBackReq.getType() == 11) {
            balance = balance.add(jsonObject.getBigDecimal("bet_num"));
            gameCommonService.updateUserBalance(memBaseinfo, jsonObject.getBigDecimal("bet_num"), GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);

        } else if (blCallBackReq.getType() == 12) {
            balance = balance.add(jsonObject.getBigDecimal("bet_num"));
            gameCommonService.updateUserBalance(memBaseinfo, jsonObject.getBigDecimal("bet_num"), GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);
        }

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(blCallBackReq.getReport_id());
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
        //游戏代码
        txns.setGameCode(gamePlatform.getPlatformCode());
        //游戏名称
        txns.setGameName(gamePlatform.getPlatformEnName());
        //下注金额
        txns.setBetAmount(jsonObject.getBigDecimal("bet_num"));
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(jsonObject.getBigDecimal("bet_num"));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(jsonObject.getBigDecimal("bet_num"));
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(jsonObject.getBigDecimal("bet_num"));
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(blCallBackReq.getTime(), DateUtils.newFormat));
        //有效投注金额 或 投注面值
        txns.setTurnover(jsonObject.getBigDecimal("bet_num"));
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
            dataJson.put("resp_msg", statusJson);
            JSONArray array = new JSONArray();
            statusJson.put("code", "-1");
            statusJson.put("message", "失败");
            statusJson.put("errors", array);
            return dataJson;
        }

        JSONObject dataObject = new JSONObject();
        JSONObject statusObject = new JSONObject();
        statusObject.put("code", 0);
        statusObject.put("msg", "success");
        dataObject.put("balance", memBaseinfo.getBalance());
        dataObject.put("currency", gameParentPlatform.getCurrencyType());
        dataJson.put("data", dataObject);
        dataJson.put("status", statusObject);
        return dataJson;
    }
}


