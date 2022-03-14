package com.indo.game.controller.t9;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.JDBAESDecrypt;
import com.indo.game.common.util.T9AESDecrypt;
import com.indo.game.service.t9.T9CallbackService;
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
 * T9 回调服务
 */
@RestController
@RequestMapping("/t9")
public class T9CallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private T9CallbackService t9CallbackService;

    /**
     * T9回调查询玩家余额
     */
    @RequestMapping(value="/querypoint",method= RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object callBackQuerypoint(String x, HttpServletRequest request) {
        String jsonStr = null;
        try {
//            x = "FKgMpX13N9XpRt89ZknMOa_T_u_LkqWO2bzTuiXz8QE";
            jsonStr = T9AESDecrypt.decrypt(x, OpenAPIProperties.T9_API_KEY,OpenAPIProperties.T9_API_IV);
        } catch (Exception e) {
            logger.info("T9Callback {} callBackQuerypoint 解密失败",e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("T9Callback {} callBackQuerypoint 回调,IP:"+ip+" params:{}",jsonStr);
        Object object = t9CallbackService.queryPoint(jsonStr,ip);
        logger.info("T9Callback {} callBackQuerypoint 回调返回数据, params:{}",object);
        return object;
    }

    /**
     * T9回调提取点数
     */
    @RequestMapping(value="/withdrawal",method= RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object callBackWithdrawal(String x, HttpServletRequest request) {
        String jsonStr = null;
        try {
//            x = "Ym-gLO1IcHMJ9Y_NY2mguOqhjYxAly8StzVF0hCnz_BzIu1-bawAH2CFiZedrLrKW9iVg56Np2DwsYxmkxyLsHiUvFFC14JtfvcstBAiWhQ";
            jsonStr = JDBAESDecrypt.decrypt(x, OpenAPIProperties.T9_API_KEY,OpenAPIProperties.T9_API_IV);
        } catch (Exception e) {
            logger.info("T9Callback {} callBackWithdrawal 解密失败",e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("T9Callback {} callBackWithdrawal 回调,IP:"+ip+" params:{}",jsonStr);
        Object object = t9CallbackService.withdrawal(jsonStr,ip);
        logger.info("T9Callback {} callBackWithdrawal 回调返回数据, params:{}",object);
        return object;
    }

    /**
     * T9回调存入点数
     */
    @RequestMapping(value="/deposit",method= RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object callBackDeposit(String x, HttpServletRequest request) {
        String jsonStr = null;
        try {
//            x = "Ym-gLO1IcHMJ9Y_NY2mguOqhjYxAly8StzVF0hCnz_CTlrzkDPAz0-zZBuTC0FZx448M_3IRdwnm23B2fHYLkYoP3ZjAbYKxLk003YdSiec";
            jsonStr = JDBAESDecrypt.decrypt(x, OpenAPIProperties.T9_API_KEY,OpenAPIProperties.T9_API_IV);
        } catch (Exception e) {
            logger.info("T9Callback {} callBackDeposit 解密失败",e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("T9Callback {} callBackDeposit 回调,IP:"+ip+" params:{}",jsonStr);
        Object object = t9CallbackService.deposit(jsonStr,ip);
        logger.info("T9Callback {} callBackDeposit 回调返回数据, params:{}",object);
        return object;
    }

    /**
     * T9回调取消交易
     */
    @RequestMapping(value="/canceltransfer",method= RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object callBackCanceltransfer(String x, HttpServletRequest request) {
        String jsonStr = null;
        try {
//            x = "Ym-gLO1IcHMJ9Y_NY2mguOqhjYxAly8StzVF0hCnz_ACi8PVpFdETLicyN6fSbdk";
            jsonStr = JDBAESDecrypt.decrypt(x, OpenAPIProperties.T9_API_KEY,OpenAPIProperties.T9_API_IV);
        } catch (Exception e) {
            logger.info("T9Callback {} callBackCanceltransfer 解密失败",e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("T9Callback {} callBackCanceltransfer 回调,IP:"+ip+" params:{}",jsonStr);
        Object object = t9CallbackService.canceltransfer(jsonStr,ip);
        logger.info("T9Callback {} callBackCanceltransfer 回调返回数据, params:{}",object);
        return object;
    }
}
