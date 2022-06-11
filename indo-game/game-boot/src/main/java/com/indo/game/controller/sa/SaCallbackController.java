package com.indo.game.controller.sa;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.web.util.http.HttpUtils;
import com.indo.game.common.util.FCHashAESDecrypt;
import com.indo.game.common.util.PostRawParamsParseUtil;
import com.indo.game.common.util.SAJDecryption;
import com.indo.game.service.sa.SaCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@RestController
@RequestMapping("/sa/callBack")
public class SaCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SaCallbackService saCallbackService;


    // 获取用户余额
    @RequestMapping(value = "/GetUserBalance.aspx", method = RequestMethod.POST)
    @AllowAccess
    private Object getUserBalance(HttpServletRequest request) {
        String params = null;
        try{
            params = PostRawParamsParseUtil.getRequestPostBytes(request);
            logger.info("SaCallbackController getUserBalance 回调查询余额, params:{}", params);
            params = SAJDecryption.decrypt(URLDecoder.decode(params), OpenAPIProperties.SA_ENCRYPT_KEY);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        Object object = saCallbackService.getUserBalance(params, ip);
        logger.info("SaCallbackController getUserBalance 回调查询余额返回数据 params:{}", object);
        return object;
    }

    // 下注
    @RequestMapping(value = "/PlaceBet.aspx", method = RequestMethod.POST)
    @AllowAccess
    private Object placeBet(HttpServletRequest request) {
        String params = null;
        try{
            params = PostRawParamsParseUtil.getRequestPostBytes(request);
            logger.info("SaCallbackController placeBet 回调下注, params:{}", params);
            params = SAJDecryption.decrypt(URLDecoder.decode(params), OpenAPIProperties.SA_ENCRYPT_KEY);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        Object object = saCallbackService.placeBet(params, ip);
        logger.info("SaCallbackController placeBet 回调下注返回数据 params:{}", object);
        return object;
    }

    // 派奖中奖
    @RequestMapping(value = "/PlayerWin.aspx", method = RequestMethod.POST)
    @AllowAccess
    private Object playerWin(HttpServletRequest request) {
        String params = null;
        try{
            params = PostRawParamsParseUtil.getRequestPostBytes(request);
            logger.info("SaCallbackController playerWin 回调中奖开奖, params:{}", params);
            params = SAJDecryption.decrypt(URLDecoder.decode(params), OpenAPIProperties.SA_ENCRYPT_KEY);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        Object object = saCallbackService.playerWin(params, ip);
        logger.info("SaCallbackController playerWin 回调中奖开奖返回数据 params:{}", object);
        return object;
    }

    // 更新下注结果没有余额变动
    @RequestMapping(value = "/PlayerLost.aspx", method = RequestMethod.POST)
    @AllowAccess
    private Object playerLost(HttpServletRequest request) {
        String params = null;
        try{
            params = PostRawParamsParseUtil.getRequestPostBytes(request);
            logger.info("SaCallbackController playerLost 回调开奖, params:{}", params);
            params = SAJDecryption.decrypt(URLDecoder.decode(params), OpenAPIProperties.SA_ENCRYPT_KEY);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        Object object = saCallbackService.playerLost(params, ip);
        logger.info("SaCallbackController playerLost 回调开奖返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/PlaceBetCancel.aspx", method = RequestMethod.POST)
    @AllowAccess
    private Object placeBetCancel(HttpServletRequest request) {
        String params = null;
        try{
            params = PostRawParamsParseUtil.getRequestPostBytes(request);
            logger.info("SaCallbackController placeBetCancel 回调取消下注 params:{}", params);
            params = SAJDecryption.decrypt(URLDecoder.decode(params), OpenAPIProperties.SA_ENCRYPT_KEY);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String ip = IPAddressUtil.getIpAddress(request);
        Object object = saCallbackService.placeBetCancel(params, ip);
        logger.info("SaCallbackController placeBetCancel 回调取消下注返回数据 params:{}", object);
        return object;
    }

}
