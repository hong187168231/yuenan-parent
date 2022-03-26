package com.indo.game.controller.dj;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.dj.DjCallBackParentReq;
import com.indo.game.pojo.dto.ps.PsCallBackParentReq;
import com.indo.game.service.dj.DjCallbackService;
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
@RequestMapping("/s128/callBack")
public class DjCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DjCallbackService djCallbackService;


    /**
     * 令牌验证
     */
    @RequestMapping(value = "/get_balance.aspx", method = RequestMethod.GET)
    public Object getBalance(DjCallBackParentReq djCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("DJCallBack {} verifyToken回调,params:{}", JSONObject.toJSONString(djCallBackParentReq));
        Object object = djCallbackService.getBalance(djCallBackParentReq, ip);
        logger.info("DJCallBack {} verifyToken回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 投注
     */
    @RequestMapping(value = "/bet.aspx", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object bet(DjCallBackParentReq djCallBackParentReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} psBet回调,params:{}", JSONObject.toJSONString(djCallBackParentReq));
        Object object = djCallbackService.djBetCallback(djCallBackParentReq, ip);
        logger.info("psCallBack {} psBet回调返回数据 params:{}", object);
        return object;
    }




    /**
     * 返还押注
     */
    @RequestMapping(value = "/cancel_bet.aspx", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object refund(DjCallBackParentReq djCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack {} psRefundt回调,params:{}", JSONObject.toJSONString(djCallBackParentReq));
        Object object = djCallbackService.djRefundtCallback(djCallBackParentReq, ip);
        logger.info("psCallBack {} psRefundt回调返回数据 params:{}", object);
        return object;
    }


}
