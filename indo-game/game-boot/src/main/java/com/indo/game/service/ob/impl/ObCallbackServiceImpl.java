package com.indo.game.service.ob.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.controller.ob.ObCallBackTransferstatusReq;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.ob.ObCallBackParentReq;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ob.ObCallbackService;
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
 * OB体育
 *
 * @author
 */
@Service
public class ObCallbackServiceImpl implements ObCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Object obBalanceCallback(ObCallBackParentReq obCallBackParentReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(obCallBackParentReq.getUserName());
        JSONObject dataJson = new JSONObject();
        if (null == memBaseinfo) {
            dataJson.put("code", "1034");
            dataJson.put("msg", "无效请求");
            return dataJson;
        }
        dataJson.put("code", "0000");
        dataJson.put("msg", "成功");
        dataJson.put("data", memBaseinfo.getBalance());
        dataJson.put("serverTime", System.currentTimeMillis());
        dataJson.put("status", true);
        return dataJson;
    }

    @Override
    public Object obTransfer(ObCallBackParentReq obCallBackParentReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(obCallBackParentReq.getUserName());
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, obCallBackParentReq.getTransferId());
//        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        JSONObject dataJson = new JSONObject();
        if (null != oldTxns) {
            dataJson.put("code", "3");
            dataJson.put("message", "订单重复操作");
            return dataJson;
        }
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.OB_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.OB_PLATFORM_CODE,OpenAPIProperties.OB_PLATFORM_CODE);
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        String bizType = obCallBackParentReq.getBizType();
        String transferType = obCallBackParentReq.getTransferType();//账变类型(1加款,2扣款)
        BigDecimal amount = obCallBackParentReq.getAmount();
        if ("1".equals(transferType)) {//加款
            balance = balance.add(amount);
            //bizType	Y	String	业务类型,账变来源(1投注,2结算派彩,3撤单,4撤单回滚,5结算回滚,6拒单)
            if("1".equals(bizType)){
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
            }
            if("2".equals(bizType)){
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }
            if("3".equals(bizType)||"4".equals(bizType)){
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }
            if("5".equals(bizType)){
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
            }
            if("6".equals(bizType)){
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }


        } else if ("2".equals(transferType)) {//扣款
            balance = balance.subtract(amount);
            if("1".equals(bizType)) {
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
            }
            if("2".equals(bizType)) {
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }
            if("3".equals(bizType)||"4".equals(bizType)){
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
            }
            if("5".equals(bizType)) {
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
            }
            if("6".equals(bizType)) {
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
            }
        }

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(obCallBackParentReq.getTransferId());
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
        if("1".equals(bizType)) {
            //下注金额
            txns.setBetAmount(amount);
        }
        if ("1".equals(transferType)) {//加款
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount);
        }else {
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount.negate());
        }
        txns.setWinAmount(amount);
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(amount);
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(amount);
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(obCallBackParentReq.getTimestamp(), DateUtils.newFormat));
        //有效投注金额 或 投注面值
        txns.setTurnover(amount);
        if("1".equals(bizType)) {
            //操作名称
            txns.setMethod("Place Bet");
        }else
        if("2".equals(bizType)) {
            //操作名称
            txns.setMethod("Settle");
        }else {
            txns.setMethod("Settle");
        }
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
            dataJson.put("code", "-1");
            dataJson.put("msg", "失败");
            return dataJson;
        }

        JSONObject dataObject = new JSONObject();
        JSONObject statusObject = new JSONObject();
        dataObject.put("code", "0000");
        dataObject.put("msg", "成功！");
        statusObject.put("balance", balance);
        dataObject.put("data", statusObject);
        return dataObject;
    }

    @Override
    public Object transferStatus(ObCallBackTransferstatusReq obCallBackTransferstatusReq, String ip) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, obCallBackTransferstatusReq.getTransferId());
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.OB_PLATFORM_CODE);
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        JSONObject dataJson = new JSONObject();
        if (null != oldTxns) {
            if("0".equals(obCallBackTransferstatusReq.getStatus())){
                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(oldTxns.getUserId());
                BigDecimal amount = oldTxns.getWinningAmount();
                BigDecimal balance = memBaseinfo.getBalance();
                BigDecimal WinAmount = amount;
                if(amount.compareTo(BigDecimal.ZERO)==1) {
                    balance = balance.subtract(amount.abs());
                    WinAmount = amount.negate();
                    gameCommonService.updateUserBalance(memBaseinfo, amount.abs(), GoldchangeEnum.VOID_BET, TradingEnum.SPENDING);
                }
                if(amount.compareTo(BigDecimal.ZERO)==-1) {
                    balance = balance.add(amount.abs());
                    WinAmount = amount.abs();
                    gameCommonService.updateUserBalance(memBaseinfo, amount.abs(), GoldchangeEnum.VOID_BET, TradingEnum.INCOME);
                }
                txns.setWinningAmount(WinAmount);
                txns.setWinAmount(amount);
                txns.setBalance(balance);
                txns.setStatus("Running");
                txns.setMethod("Cancel Bet");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);

                oldTxns.setStatus("Cancel Bet");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }
            dataJson.put("code", "0000");
            dataJson.put("msg", "成功");
            dataJson.put("timestamp", System.currentTimeMillis() + "");
            return dataJson;
        }
        dataJson.put("code", "0");
        dataJson.put("msg", "失败");
        dataJson.put("timestamp", System.currentTimeMillis() + "");
        return dataJson;
    }
}

