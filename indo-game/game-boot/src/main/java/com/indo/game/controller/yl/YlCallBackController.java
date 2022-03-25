package com.indo.game.controller.yl;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.ps.PsCallBackParentReq;
import com.indo.game.pojo.dto.yl.YlCallBackReq;
import com.indo.game.service.ps.PsCallbackService;
import com.indo.game.service.yl.YlCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/YL/callBack")
public class YlCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private YlCallbackService ylCallbackService;




    /**
     * 下注及结算
     */
    @RequestMapping(value = "/settleFishBet", method = RequestMethod.GET)
    @ResponseBody
    @AllowAccess
    public Object bet(YlCallBackReq ylCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ylCallBack {} ylSettleFishBet回调,params:{}", JSONObject.toJSONString(ylCallBackReq));
        Object object = ylCallbackService.psBetCallback(ylCallBackReq, ip);
        logger.info("ylCallBack {} ylSettleFishBet回调返回数据 params:{}", object);
        return object;
    }



    /**
     * 返还押注
     */
    @RequestMapping(value = "/voidFishBet", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object voidFishBet(YlCallBackReq ylCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ylCallBack {} voidFishBet回调,params:{}", JSONObject.toJSONString(ylCallBackReq));
        Object object = ylCallbackService.ylVoidFishBetCallback(ylCallBackReq, ip);
        logger.info("ylCallBack {} voidFishBet回调返回数据 params:{}", object);
        return object;
    }



    /**
     * 获取余额
     */
    @RequestMapping(value = "/GetBalance", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object getBalance(YlCallBackReq ylCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("ylCallBack {} ylGetBalance回调,params:{}", JSONObject.toJSONString(ylCallBackReq));
        Object object = ylCallbackService.ylGetBalanceCallback(ylCallBackReq, ip);
        logger.info("ylCallBack {} ylGetBalance回调返回数据 params:{}", object);
        return object;
    }
}
