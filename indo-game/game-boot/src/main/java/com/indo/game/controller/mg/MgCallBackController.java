package com.indo.game.controller.mg;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.mg.MgCallBackReq;
import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;
import com.indo.game.service.mg.MgCallbackService;
import com.indo.game.service.pg.PgCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/mg/callBack")
public class MgCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MgCallbackService mgCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/getbalance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cqCallBack {} getBalance 回调,params:{}", JSONObject.toJSONString(jsonObject));
        MgCallBackReq mgCallBackReq = new MgCallBackReq();
        mgCallBackReq.setPlayerId(jsonObject.getString("playerId"));
//        mgCallBackReq.setTxnType(jsonObject.getString("txnType"));
//        mgCallBackReq.setTxnEventType(jsonObject.getString("txnEventType"));
//        mgCallBackReq.setAmount(jsonObject.getBigDecimal("amount"));
//        mgCallBackReq.setCurrency(jsonObject.getString("currency"));
//        mgCallBackReq.setTxnId(jsonObject.getString("txnId"));
//        mgCallBackReq.setCreationTime(jsonObject.getString("creationTime"));
//        mgCallBackReq.setBetId(jsonObject.getString("betId"));
//        mgCallBackReq.setRoundId(jsonObject.getString("roundId"));
//        mgCallBackReq.setCompleted(jsonObject.getBoolean("completed"));
        Object object = mgCallbackService.mgBalanceCallback(mgCallBackReq, ip);
        logger.info("cqCallBack {} getBalance 回调下注返回数据 params:{}", object);
        return object;
    }

    /**
     * 令牌验证
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object verifySession(@RequestBody JSONObject jsonObject,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack {} verifySession回调,params:{}", JSONObject.toJSONString(jsonObject));
        MgCallBackReq mgCallBackReq = new MgCallBackReq();
        mgCallBackReq.setPlayerId(jsonObject.getString("playerId"));
//        mgCallBackReq.setTxnType(jsonObject.getString("txnType"));
//        mgCallBackReq.setTxnEventType(jsonObject.getString("txnEventType"));
//        mgCallBackReq.setAmount(jsonObject.getBigDecimal("amount"));
//        mgCallBackReq.setCurrency(jsonObject.getString("currency"));
//        mgCallBackReq.setTxnId(jsonObject.getString("txnId"));
//        mgCallBackReq.setCreationTime(jsonObject.getString("creationTime"));
//        mgCallBackReq.setBetId(jsonObject.getString("betId"));
//        mgCallBackReq.setRoundId(jsonObject.getString("roundId"));
//        mgCallBackReq.setCompleted(jsonObject.getBoolean("completed"));
        Object object = mgCallbackService.mgVerifyCallback(mgCallBackReq, ip);
        logger.info("pgCallBack {} verifySession回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 投付
     */
    @RequestMapping(value = "/updatebalance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object adjustment(@RequestBody JSONObject jsonObject,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack {} adjustment回调,params:{}", JSONObject.toJSONString(jsonObject));
        MgCallBackReq mgCallBackReq = new MgCallBackReq();
        mgCallBackReq.setPlayerId(jsonObject.getString("playerId"));
        mgCallBackReq.setTxnType(jsonObject.getString("txnType"));
        mgCallBackReq.setTxnEventType(jsonObject.getString("txnEventType"));
        mgCallBackReq.setAmount(jsonObject.getBigDecimal("amount"));
        mgCallBackReq.setCurrency(jsonObject.getString("currency"));
        mgCallBackReq.setTxnId(jsonObject.getString("txnId"));
        mgCallBackReq.setCreationTime(jsonObject.getString("creationTime"));
        mgCallBackReq.setBetId(jsonObject.getString("betId"));
        mgCallBackReq.setRoundId(jsonObject.getString("roundId"));
        mgCallBackReq.setCompleted(jsonObject.getBoolean("completed"));
        Object object = mgCallbackService.mgUpdatebalanceCallback(mgCallBackReq, ip);
        logger.info("pgCallBack {} adjustment回调返回数据 params:{}", object);
        return object;
    }
}
