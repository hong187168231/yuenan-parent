package com.indo.game.controller.jili;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.jili.JiliCallbackBetReq;
import com.indo.game.pojo.dto.jili.JiliCallbackSessionBetReq;
import com.indo.game.service.jili.JiliCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * jili 回调服务
 */
@RestController
@RequestMapping("/jili/callBack")
public class JiliCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JiliCallbackService jiliCallbackService;

    /**
     * 权限验证
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object auth(@RequestParam("token") String token, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("JiliCallback auth 回调,IP:" + ip + " params:{}", token);
        Object object = jiliCallbackService.auth(token, ip);
        logger.info("JiliCallback auth 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 下注
     */
    @RequestMapping(value = "/bet", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object bet(@RequestBody JiliCallbackBetReq jiliCallbackBetReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("JiliCallback bet 回调,IP:" + ip + " params:{}", JSONObject.toJSONString(jiliCallbackBetReq));
        Object object = jiliCallbackService.bet(jiliCallbackBetReq, ip);
        logger.info("JiliCallback bet 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 取消订单
     */
    @RequestMapping(value = "/cancelBet", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object cancelBet(@RequestBody JiliCallbackBetReq jiliCallbackBetReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("JiliCallback cancelBet 回调,IP:" + ip + " params:{}", JSONObject.toJSONString(jiliCallbackBetReq));
        Object object = jiliCallbackService.cancelBet(jiliCallbackBetReq, ip);
        logger.info("JiliCallback cancelBet 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 牌局下注
     */
    @RequestMapping(value = "/sessionBet", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object sessionBet(@RequestBody JiliCallbackSessionBetReq jiliCallbackSessionBetReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("JiliCallback sessionBet 回调,IP:" + ip + " params:{}", JSONObject.toJSONString(jiliCallbackSessionBetReq));
        Object object = jiliCallbackService.sessionBet(jiliCallbackSessionBetReq, ip);
        logger.info("JiliCallback sessionBet 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 取消订单
     */
    @RequestMapping(value = "/cancelSessionBet", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object cancelSessionBet(@RequestBody JiliCallbackSessionBetReq jiliCallbackSessionBetReq, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("JiliCallback cancelSessionBet 回调,IP:" + ip + " params:{}", JSONObject.toJSONString(jiliCallbackSessionBetReq));
        Object object = jiliCallbackService.cancelSessionBet(jiliCallbackSessionBetReq, ip);
        logger.info("JiliCallback cancelSessionBet 回调返回数据, params:{}", object);
        return object;
    }
}
