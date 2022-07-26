package com.indo.game.controller.pg;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.pg.PgAdjustmentOutCallBackReq;
import com.indo.game.pojo.dto.pg.PgGetBalanceCallBackReq;
import com.indo.game.pojo.dto.pg.PgTransferInOutCallBackReq;
import com.indo.game.pojo.dto.pg.PgVerifySessionCallBackReq;
import com.indo.game.service.pg.PgCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pg/callBack")
public class PgCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PgCallbackService pgCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/Cash/Get", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(PgGetBalanceCallBackReq pgGetBalanceCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cqCallBack getBalance 回调,params:{}", JSONObject.toJSONString(pgGetBalanceCallBackReq));
        Object object = pgCallbackService.pgBalanceCallback(pgGetBalanceCallBackReq, ip);
        logger.info("cqCallBack getBalance 回调下注返回数据 params:{}", object);
        return object;
    }

    /**
     * 令牌验证
     */
    @RequestMapping(value = "/VerifySession", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object verifySession(PgVerifySessionCallBackReq pgVerifySessionCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack verifySession回调,params:{}", JSONObject.toJSONString(pgVerifySessionCallBackReq));
        Object object = pgCallbackService.pgVerifyCallback(pgVerifySessionCallBackReq, ip);
        logger.info("pgCallBack verifySession回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 投付
     */
    @RequestMapping(value = "/Cash/TransferInOut", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object transferInOut(PgTransferInOutCallBackReq pgTransferInOutCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack transferInOut回调,params:{}", JSONObject.toJSONString(pgTransferInOutCallBackReq));
        Object object = pgCallbackService.pgTransferInCallback(pgTransferInOutCallBackReq, ip);
        logger.info("pgCallBack transferInOut回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 余额调整
     */
    @RequestMapping(value = "/Cash/Adjustment", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object adjustment(PgAdjustmentOutCallBackReq pgAdjustmentOutCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack adjustment回调,params:{}", JSONObject.toJSONString(pgAdjustmentOutCallBackReq));
        Object object = pgCallbackService.pgAdjustmentCallback(pgAdjustmentOutCallBackReq, ip);
        logger.info("pgCallBack adjustment回调返回数据 params:{}", object);
        return object;
    }

}
