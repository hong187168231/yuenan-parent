package com.indo.game.controller.ps;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;
import com.indo.game.pojo.dto.ps.PsCallBackParentReq;
import com.indo.game.pojo.vo.callback.ps.PsCallBackResponse;
import com.indo.game.service.pg.PgCallbackService;
import com.indo.game.service.ps.PsCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/playstar/callBack")
public class PsCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PsCallbackService psCallbackService;


    /**
     * 令牌验证
     */
    @RequestMapping(value = "/VerifySession", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public PsCallBackResponse verifySession(PsCallBackParentReq psVerifyCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} verifyToken回调,params:{}", JSONObject.toJSONString(psVerifyCallBackReq));
        PsCallBackResponse psCallBackResponse = psCallbackService.psVerifyCallback(psVerifyCallBackReq, ip);
        logger.info("psCallBack {} verifyToken回调返回数据 params:{}", psCallBackResponse);
        return psCallBackResponse;
    }


    /**
     * 投注
     */
    @RequestMapping(value = "/api/bet", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object bet(PsCallBackParentReq psbetCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} psBet回调,params:{}", JSONObject.toJSONString(psbetCallBackReq));
        Object object = psCallbackService.psBetCallback(psbetCallBackReq, ip);
        logger.info("psCallBack {} psBet回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 结算
     */
    @RequestMapping(value = "/api/result", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object result(PsCallBackParentReq psbetCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} psResult回调,params:{}", JSONObject.toJSONString(psbetCallBackReq));
        Object object = psCallbackService.psResultCallback(psbetCallBackReq, ip);
        logger.info("psCallBack {} psResult回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 返还押注
     */
    @RequestMapping(value = "/api/refund", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object refund(PsCallBackParentReq psbetCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} psRefundt回调,params:{}", JSONObject.toJSONString(psbetCallBackReq));
        Object object = psCallbackService.psRefundtCallback(psbetCallBackReq, ip);
        logger.info("psCallBack {} psRefundt回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 红利
     */
    @RequestMapping(value = "/api/bonusaward", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object bonus(PsCallBackParentReq psbetCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} psBonus回调,params:{}", JSONObject.toJSONString(psbetCallBackReq));
        Object object = psCallbackService.psBonusCallback(psbetCallBackReq, ip);
        logger.info("psCallBack {} psBonus回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 红利
     */
    @RequestMapping(value = "/api/getbalance", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object getBalance(PsCallBackParentReq psbetCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} psGetBalance回调,params:{}", JSONObject.toJSONString(psbetCallBackReq));
        Object object = psCallbackService.psGetBalanceCallback(psbetCallBackReq, ip);
        logger.info("psCallBack {} psGetBalance回调返回数据 params:{}", object);
        return object;
    }
}
