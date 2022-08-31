package com.indo.game.service.dg.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.dto.dg.DgCallBackReq;
import com.indo.game.pojo.dto.dg.DgMemberCallBackReq;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.dg.DgCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * DG
 *
 * @author
 */
@Service
public class DgCallbackServiceImpl implements DgCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Object dgBalanceCallback(String agentName, DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip) {
        DgMemberCallBackReq dgMemberCallBackReq = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(dgCallBackReq.getMember())),DgMemberCallBackReq.class);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(dgMemberCallBackReq.getUsername());
        JSONObject dataJson = new JSONObject();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.DG_PLATFORM_CODE);
        if (null == memBaseinfo) {
            dataJson.put("codeId", "1");
            dataJson.put("token", dgCallBackReq.getToken());
            return dataJson;
        } else {
            dataJson.put("codeId", "0");
            dataJson.put("token", dgCallBackReq.getToken());
            JSONObject memberJson = new JSONObject();
            memberJson.put("username", dgMemberCallBackReq.getUsername());
            memberJson.put("balance", memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            dataJson.put("member", memberJson);
            return dataJson;
        }
    }

    @Override
    public Object dgTransferCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName) {
        DgMemberCallBackReq dgMemberCallBackReq = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(dgCallBackReq.getMember())),DgMemberCallBackReq.class);
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.DG_PLATFORM_CODE);
        GamePlatform gamePlatform ;
        if(OpenAPIProperties.DG_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.DG_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
        }else {
            gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(dgCallBackReq.getGametype(),gameParentPlatform.getPlatformCode());
        }
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(dgMemberCallBackReq.getUsername());
        JSONObject dataJson = new JSONObject();
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, dgCallBackReq.getData());
        
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns&&"Settle".equals(oldTxns.getMethod())) {
            dataJson.put("codeId", "98");
            dataJson.put("message", "操作失败！");
            return dataJson;
        }

        BigDecimal amount = null!=dgMemberCallBackReq.getAmount()?dgMemberCallBackReq.getAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
//创建时间
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        Txns txns = new Txns();
        if (oldTxns != null&&!"Cancel Bet".equals(oldTxns.getMethod())) {
            if (amount.compareTo(BigDecimal.ZERO) == -1) {//小于零
                balance = balance.subtract(amount.abs());
                gameCommonService.updateUserBalance(memBaseinfo, amount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }
            if (amount.compareTo(BigDecimal.ZERO) == 1) {//大于零
                balance = balance.add(amount);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }
            txns.setId(null);
            txns.setBalance(balance);
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            oldTxns.setStatus("Settle");
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount);
            txns.setWinAmount(amount);
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                dataJson.put("codeId", "98");
                dataJson.put("message", "操作失败！");
                return dataJson;
            }
        }else {
            if (amount.compareTo(BigDecimal.ZERO) == -1) {//小于零
                balance = balance.subtract(amount.abs());
                gameCommonService.updateUserBalance(memBaseinfo, amount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
            }
            if (amount.compareTo(BigDecimal.ZERO) == 1) {//大于零
                balance = balance.add(amount);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            }
            //游戏商注单号
            txns.setPlatformTxId(dgCallBackReq.getData());
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());

            txns.setRoundId(dgCallBackReq.getTicketId());
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
            txns.setBetAmount(amount);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount);
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
            //操作名称
            txns.setMethod("Place Bet");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);

            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP

            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                dataJson.put("codeId", "98");
                dataJson.put("message", "操作失败！");
                return dataJson;
            }
        }
        dataJson.put("codeId", "0");
        dataJson.put("token", dgCallBackReq.getToken());
        dataJson.put("data", dgCallBackReq.getData());
        JSONObject memberJson = new JSONObject();
        memberJson.put("username", dgMemberCallBackReq.getUsername());
        memberJson.put("amount", amount.divide(gameParentPlatform.getCurrencyPro()));
        memberJson.put("balance", balance.divide(gameParentPlatform.getCurrencyPro()));
        dataJson.put("member", memberJson);
        return dataJson;
    }

    @Override
    public Object dgCheckTransferCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName) {
        JSONObject dataJson = new JSONObject();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, dgCallBackReq.getData());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            dataJson.put("codeId", "98");
            dataJson.put("message", "操作失败！");
            return dataJson;
        }
        dataJson.put("codeId", "0");
        dataJson.put("token", dgCallBackReq.getToken());
        return dataJson;
    }

    @Override
    public Object djInformCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName) {
        DgMemberCallBackReq dgMemberCallBackReq = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(dgCallBackReq.getMember())),DgMemberCallBackReq.class);
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(dgMemberCallBackReq.getUsername());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.DG_PLATFORM_CODE);
        JSONObject dataJson = new JSONObject();
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, dgCallBackReq.getData());
        
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        BigDecimal amount = null!=dgMemberCallBackReq.getAmount()?dgMemberCallBackReq.getAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            if (null != oldTxns) {
                balance = balance.add(amount);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            } else {
                dataJson.put("codeId", "0");
                dataJson.put("token", dgCallBackReq.getToken());
                dataJson.put("data", dgCallBackReq.getData());
                JSONObject memberJson = new JSONObject();
                memberJson.put("username", dgMemberCallBackReq.getUsername());
                memberJson.put("balance", balance.divide(gameParentPlatform.getCurrencyPro()));
                dataJson.put("member", memberJson);
                return dataJson;
            }
        } else if (amount.compareTo(BigDecimal.ZERO) == 1) {
            if (null == oldTxns) {
                balance = balance.add(amount);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            } else {
                dataJson.put("codeId", "0");
                dataJson.put("data", dgCallBackReq.getData());
                dataJson.put("token", dgCallBackReq.getToken());
                JSONObject memberJson = new JSONObject();
                memberJson.put("balance", balance.divide(gameParentPlatform.getCurrencyPro()));
                memberJson.put("username", dgMemberCallBackReq.getUsername());
                dataJson.put("member", memberJson);
                return dataJson;
            }
        }
        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        txns.setId(null);
        txns.setBalance(balance);
        String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
        txns.setCreateTime(dateStr);
        //操作名称
        txns.setMethod("Cancel Bet");
        txns.setStatus("Running");
        txnsMapper.insert(txns);
        oldTxns.setStatus("Cancel");
        oldTxns.setUpdateTime(dateStr);
        txnsMapper.updateById(oldTxns);

        dataJson.put("codeId", "0");
        dataJson.put("token", dgCallBackReq.getToken());
        dataJson.put("data", dgCallBackReq.getData());
        JSONObject memberJson = new JSONObject();
        memberJson.put("balance", balance.divide(gameParentPlatform.getCurrencyPro()));
        memberJson.put("username", dgMemberCallBackReq.getUsername());
        dataJson.put("member", memberJson);
        return dataJson;
    }

    @Override
    public Object mgOrderCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName) {
        DgMemberCallBackReq dgMemberCallBackReq = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(dgCallBackReq.getMember())),DgMemberCallBackReq.class);
        JSONObject dataJson = new JSONObject();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(dgMemberCallBackReq.getUsername());
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getRoundId, dgCallBackReq.getTicketId());
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.DG_PLATFORM_CODE);
        List<Txns> oldTxns = txnsMapper.selectList(wrapper);
        JSONArray jsonArray = new JSONArray();
        if (oldTxns.size() > 0) {
            for (Txns txns : oldTxns) {
                JSONObject json = new JSONObject();
                json.put("username", memBaseinfo.getAccount());
                json.put("ticketId", dgCallBackReq.getTicketId());
                json.put("serial", txns.getPlatformTxId());
                json.put("amount", txns.getBetAmount().divide(gameParentPlatform.getCurrencyPro()));
                jsonArray.add(json);
            }
            dataJson.put("codeId", "0");
            dataJson.put("token", dgCallBackReq.getToken());
            dataJson.put("ticketId", dgCallBackReq.getTicketId());
            dataJson.put("list", jsonArray);
            return dataJson;
        }
        dataJson.put("codeId", "0");
        dataJson.put("token", dgCallBackReq.getToken());
        dataJson.put("ticketId", dgCallBackReq.getTicketId());
        dataJson.put("list", jsonArray);
        return dataJson;
    }

    @Override
    public Object mgUnsettleCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName) {
        JSONObject dataJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        dataJson.put("codeId", "0");
        dataJson.put("token", dgCallBackReq.getToken());
        dataJson.put("list", jsonArray);
        return dataJson;
    }
}

