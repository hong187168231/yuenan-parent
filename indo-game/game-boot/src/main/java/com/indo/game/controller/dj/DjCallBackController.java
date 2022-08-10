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
     * 获取余额
     */
    @RequestMapping(value = "/Balance", method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(DjCallBackParentReq djCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("DJCallBack  getBalance回调,params:{},IP:{}", JSONObject.toJSONString(djCallBackParentReq),ip);
        Object object = djCallbackService.getBalance(djCallBackParentReq, ip);
        logger.info("DJCallBack getBalance回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 投注
     */
    @RequestMapping(value = "/Bet", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bet(DjCallBackParentReq djCallBackParentReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack bet回调,params:{},IP:{}", JSONObject.toJSONString(djCallBackParentReq),ip);
        Object object = djCallbackService.djBetCallback(djCallBackParentReq, ip);
        logger.info("psCallBack bet回调返回数据 params:{}", object);
        return object;
    }




    /**
     * 返还押注
     */
    @RequestMapping(value = "/CancelBet", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object refund(DjCallBackParentReq djCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("psCallBack refund回调,params:{},IP:{}", JSONObject.toJSONString(djCallBackParentReq),ip);
        Object object = djCallbackService.djRefundtCallback(djCallBackParentReq, ip);
        logger.info("psCallBack refund回调返回数据 params:{}", object);
        return object;
    }


}
