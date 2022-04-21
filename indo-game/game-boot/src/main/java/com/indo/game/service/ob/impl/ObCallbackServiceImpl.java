package com.indo.game.service.ob.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.ob.ObCallBackParentReq;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.ob.ObCallbackService;
import com.indo.user.pojo.bo.MemTradingBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, obCallBackParentReq.getTransferId());
        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        JSONObject dataJson = new JSONObject();
        if (null != oldTxns) {
            dataJson.put("code", "3");
            dataJson.put("message", "订单重复操作");
            return dataJson;
        }
        if ("1".equals(obCallBackParentReq.getBizType())) {
            balance = balance.add(new BigDecimal(obCallBackParentReq.getAmount()));
            gameCommonService.updateUserBalance(memBaseinfo, new BigDecimal(obCallBackParentReq.getAmount()), GoldchangeEnum.DSFYXZZ, TradingEnum.INCOME);

        } else if ("2".equals(obCallBackParentReq.getBizType())) {
            balance = balance.subtract(new BigDecimal(obCallBackParentReq.getAmount()));
            gameCommonService.updateUserBalance(memBaseinfo, new BigDecimal(obCallBackParentReq.getAmount()), GoldchangeEnum.DSFYXZZ, TradingEnum.SPENDING);
        }

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(obCallBackParentReq.getTransferId());
        //玩家 ID
        txns.setUserId(memBaseinfo.getId().toString());

        //平台代码
        txns.setPlatform("BOLE");
        //下注金额
        txns.setBetAmount(new BigDecimal(obCallBackParentReq.getAmount()));
        //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
        txns.setWinningAmount(new BigDecimal(obCallBackParentReq.getAmount()));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(new BigDecimal(obCallBackParentReq.getAmount()));
        //真实返还金额,游戏赢分
        txns.setRealWinAmount(new BigDecimal(obCallBackParentReq.getAmount()));
        //返还金额 (包含下注金额)
        //赌注的结果 : 赢:0,输:1,平手:2
        int resultTyep;
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(obCallBackParentReq.getTimestamp(), DateUtils.newFormat));
        //有效投注金额 或 投注面值
        txns.setTurnover(new BigDecimal(obCallBackParentReq.getAmount()));
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
            dataJson.put("code", "-1");
            dataJson.put("msg", "失败");
            return dataJson;
        }

        JSONObject dataObject = new JSONObject();
        JSONObject statusObject = new JSONObject();
        dataObject.put("code", 0000);
        dataObject.put("msg", "成功！");
        statusObject.put("balance", memBaseinfo.getBalance());
        dataJson.put("data", dataObject);
        return dataJson;
    }

    @Override
    public Object transferStatus(ObCallBackParentReq obCallBackParentReq, String ip) {
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(obCallBackParentReq.getUserName());
        BigDecimal balance = memBaseinfo.getBalance();
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, obCallBackParentReq.getTransferId());
        wrapper.eq(Txns::getUserId, memBaseinfo.getId());
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        JSONObject dataJson = new JSONObject();
        if (null != oldTxns) {
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

