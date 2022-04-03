package com.indo.game.controller.wm;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.service.wm.WmCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wm/callBack")
public class WmCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WmCallbackService wmCallbackService;


    @RequestMapping(value = "/CallBalance", method = RequestMethod.GET)
    @AllowAccess
    public Object callBalance(@RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("wmCallback getBalance 回调查询余额, params:{}", params);
        Object object = wmCallbackService.getBalance(params, ip);
        logger.info("wmCallback getBalance 回调查询余额返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/PointInout", method = RequestMethod.POST)
    @AllowAccess
    public Object pointInout(@RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("wmCallback pointInout 回调下注params:{}", params);
        Object object = wmCallbackService.pointInout(params, ip);
        logger.info("wmCallback pointInout 回调下注返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/TimeoutBetReturn", method = RequestMethod.POST)
    @AllowAccess
    public Object timeoutBetReturn(@RequestBody JSONObject params, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("wmCallback timeoutBetReturn 回调回退余额 params:{}", params);
        Object object = wmCallbackService.timeoutBetReturn(params, ip);
        logger.info("wmCallback timeoutBetReturn 回调回退余额返回数据 params:{}", object);
        return object;
    }

}
