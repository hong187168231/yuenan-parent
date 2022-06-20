package com.indo.game.controller.pg;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.cq.CqBetCallBackReq;
import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;
import com.indo.game.service.cq.CqCallbackService;
import com.indo.game.service.pg.PgCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Object getBalance(PgVerifyCallBackReq pgVerifyCallBackReq,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cqCallBack {} getBalance 回调,params:{}", JSONObject.toJSONString(pgVerifyCallBackReq));
        Object object = pgCallbackService.pgBalanceCallback(pgVerifyCallBackReq, ip);
        logger.info("cqCallBack {} getBalance 回调下注返回数据 params:{}", object);
        return object;
    }

    /**
     * 令牌验证
     */
    @RequestMapping(value = "/VerifySession", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object verifySession(PgVerifyCallBackReq pgVerifyCallBackReq,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack {} verifySession回调,params:{}", JSONObject.toJSONString(pgVerifyCallBackReq));
        Object object = pgCallbackService.pgVerifyCallback(pgVerifyCallBackReq, ip);
        logger.info("pgCallBack {} verifySession回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 投付
     */
    @RequestMapping(value = "/Cash/TransferIn", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object transferIn(PgVerifyCallBackReq pgVerifyCallBackReq,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack {} TransferIn回调,params:{}", JSONObject.toJSONString(pgVerifyCallBackReq));
        Object object = pgCallbackService.pgTransferInCallback(pgVerifyCallBackReq, ip);
        logger.info("pgCallBack {} TransferIn回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 投付
     */
    @RequestMapping(value = "/Cash/Adjustment", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object adjustment(PgVerifyCallBackReq pgVerifyCallBackReq,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack {} adjustment回调,params:{}", JSONObject.toJSONString(pgVerifyCallBackReq));
        Object object = pgCallbackService.pgAdjustmentCallback(pgVerifyCallBackReq, ip);
        logger.info("pgCallBack {} adjustment回调返回数据 params:{}", object);
        return object;
    }
}
