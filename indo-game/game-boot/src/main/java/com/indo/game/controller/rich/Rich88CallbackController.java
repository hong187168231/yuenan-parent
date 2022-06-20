package com.indo.game.controller.rich;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.JDBAESDecrypt;
import com.indo.game.pojo.dto.rich.Rich88ActivityReq;
import com.indo.game.pojo.dto.rich.Rich88TransferReq;
import com.indo.game.service.rich.Rich88CallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * RICH88 回调服务
 */
@RestController
@RequestMapping("/RICH88/callBack")
public class Rich88CallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Rich88CallbackService rich88CallbackService;

    /**
     * 获取平台SESSION ID
     */
    @RequestMapping(value="/rich88/session_id",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getSessionId(HttpServletRequest request) {
        String apiKey = request.getHeader("api-key");
        String pfId = request.getHeader("pf-id");
        String timestamp = request.getHeader("timestamp");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("Rich88Callback {} getSessionId 回调,IP:"+ip+" params:{}, {}",apiKey, timestamp);
        Object object = rich88CallbackService.getSessionId(apiKey,pfId, timestamp, ip);
        logger.info("Rich88Callback {} getSessionId 回调返回数据, params:{}",object);
        return object;
    }

    /**
     * 获取余额
     */
    @RequestMapping(value="/rich88/balance/{account}",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@PathVariable(name = "account") String account, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("Rich88Callback {} getBalance 回调,IP:"+ip+" account:{}",account);
        Object object = rich88CallbackService.getBalance(authorization, account,ip);
        logger.info("Rich88Callback {} getBalance 回调返回数据, params:{}",object);
        return object;
    }

    /**
     * 交易
     */
    @RequestMapping(value="/rich88/transfer",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object transfer(@RequestBody Rich88TransferReq rich88TransferReq, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("Rich88Callback {} transfer 回调,IP:"+ip+" params:{}", JSONObject.toJSONString(rich88TransferReq));
        Object object = rich88CallbackService.transfer(rich88TransferReq,authorization,ip);
        logger.info("Rich88Callback {} transfer 回调返回数据, params:{}",object);
        return object;
    }

    /**
     * 活动派奖
     */
    @RequestMapping(value="/rich88/award_activity",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object awardActivity(@RequestBody Rich88ActivityReq rich88ActivityReq, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("Rich88Callback {} awardActivity 回调,IP:"+ip+" params:{}", JSONObject.toJSONString(rich88ActivityReq));
        Object object = rich88CallbackService.awardActivity(rich88ActivityReq, authorization,ip);
        logger.info("Rich88Callback {} awardActivity 回调返回数据, params:{}",object);
        return object;
    }
}
