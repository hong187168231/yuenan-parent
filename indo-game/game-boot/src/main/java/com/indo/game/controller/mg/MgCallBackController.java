package com.indo.game.controller.mg;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.DateUtils;
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
import javax.servlet.http.HttpServletResponse;
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
    public Object getBalance(@RequestBody JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
//        X-MGP-REQ-ID 請求的唯一識別碼（字串，GUID 格式）
        String reqId = request.getHeader("X-MGP-REQ-ID");
//      API 令牌，用以認證 API 的存取（請參照上述 “ 共享 API 令牌或自定義 API 令牌”）
        String reqToken = request.getHeader("X-MGP-TOKEN");
//        請求的時間戳（數字格式，當前 UTC 時間，以 毫秒為單位）
        String reqTime = request.getHeader("X-MGP-REQUEST-TIME");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("mgCallBack getBalance 回调,params:{},X-MGP-REQ-ID:{},X-MGP-TOKEN:{},X-MGP-REQUEST-TIME:{},IP:{}", JSONObject.toJSONString(jsonObject),reqId ,reqToken, reqTime,ip);
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
        response.setHeader("X-MGP-REQ-ID",reqId);
        Long time = Long.valueOf(reqTime)- DateUtils.getGMT8TimeLength10();
        response.setHeader("X-MGP-RESPONSE-TIME",String.valueOf(time));
        logger.info("mgCallBack 回调返回数据/getBalance params:{},X-MGP-REQ-ID:{},X-MGP-RESPONSE-TIME:{}", object,reqId,time);
        return object;
    }

    /**
     * 令牌验证
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object verifySession(@RequestBody JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response) {
//        X-MGP-REQ-ID 請求的唯一識別碼（字串，GUID 格式）
        String reqId = request.getHeader("X-MGP-REQ-ID");
//      API 令牌，用以認證 API 的存取（請參照上述 “ 共享 API 令牌或自定義 API 令牌”）
        String reqToken = request.getHeader("X-MGP-TOKEN");
//        請求的時間戳（數字格式，當前 UTC 時間，以 毫秒為單位）
        String reqTime = request.getHeader("X-MGP-REQUEST-TIME");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("mgCallBack verifySession回调/login,params:{},X-MGP-REQ-ID:{},X-MGP-TOKEN:{},X-MGP-REQUEST-TIME:{},IP:{}", JSONObject.toJSONString(jsonObject),reqId ,reqToken, reqTime,ip);
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
        response.setHeader("X-MGP-REQ-ID",reqId);
        Long time = Long.valueOf(reqTime)- DateUtils.getGMT8TimeLength10();
        response.setHeader("X-MGP-RESPONSE-TIME",String.valueOf(time));
        logger.info("mgCallBack verifySession回调返回数据/login params:{},X-MGP-REQ-ID:{},X-MGP-RESPONSE-TIME:{}", object,reqId,time);
        return object;
    }


    /**
     * 投付
     */
    @RequestMapping(value = "/updatebalance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object adjustment(@RequestBody JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response) {
//        X-MGP-REQ-ID 請求的唯一識別碼（字串，GUID 格式）
        String reqId = request.getHeader("X-MGP-REQ-ID");
//      API 令牌，用以認證 API 的存取（請參照上述 “ 共享 API 令牌或自定義 API 令牌”）
        String reqToken = request.getHeader("X-MGP-TOKEN");
//        請求的時間戳（數字格式，當前 UTC 時間，以 毫秒為單位）
        String reqTime = request.getHeader("X-MGP-REQUEST-TIME");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("mgCallBack adjustment回调/updatebalance,params:{},X-MGP-REQ-ID:{},X-MGP-TOKEN:{},X-MGP-REQUEST-TIME:{},IP:{}", JSONObject.toJSONString(jsonObject),reqId ,reqToken, reqTime,ip);
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
        Long time = Long.valueOf(reqTime)- DateUtils.getGMT8TimeLength10();
        response.setHeader("X-MGP-RESPONSE-TIME",String.valueOf(time));
        logger.info("mgCallBack adjustment()回调返回数据/updatebalance params:{},X-MGP-REQ-ID:{},X-MGP-RESPONSE-TIME:{}", object,reqId,time);
        return object;
    }

    /**
     * 當需要返回到上一筆交易結果時使用。使用回復交易時，該筆回復交易的交易金額
     *  須與待回復交易的交易金額相同，不支援回復部分的交易紀錄
     */
    @RequestMapping(value = "/rollback", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object rollback(@RequestBody JSONObject jsonObject,HttpServletRequest request, HttpServletResponse response) {
//        X-MGP-REQ-ID 請求的唯一識別碼（字串，GUID 格式）
        String reqId = request.getHeader("X-MGP-REQ-ID");
//      API 令牌，用以認證 API 的存取（請參照上述 “ 共享 API 令牌或自定義 API 令牌”）
        String reqToken = request.getHeader("X-MGP-TOKEN");
//        請求的時間戳（數字格式，當前 UTC 時間，以 毫秒為單位）
        String reqTime = request.getHeader("X-MGP-REQUEST-TIME");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("mgCallBack rollback回调/rollback,params:{},X-MGP-REQ-ID:{},X-MGP-TOKEN:{},X-MGP-REQUEST-TIME:{},IP:{}", JSONObject.toJSONString(jsonObject),reqId ,reqToken, reqTime,ip);
        MgCallBackReq mgCallBackReq = new MgCallBackReq();
        mgCallBackReq.setPlayerId(jsonObject.getString("playerId"));
        mgCallBackReq.setTxnId(jsonObject.getString("txnId"));
        Object object = mgCallbackService.rollback(mgCallBackReq, ip);
        Long time = Long.valueOf(reqTime)- DateUtils.getGMT8TimeLength10();
        response.setHeader("X-MGP-RESPONSE-TIME",String.valueOf(time));
        logger.info("mgCallBack 回调返回数据/rollback params:{},X-MGP-REQ-ID:{},X-MGP-RESPONSE-TIME:{}", object,reqId,time);
        return object;
    }
}
