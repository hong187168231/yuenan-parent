package com.indo.game.controller.ka;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.KAHashAESEncrypt;
import com.indo.game.pojo.dto.ka.KAApiResponseData;
import com.indo.game.pojo.dto.ka.KACallbackCommonReq;
import com.indo.game.pojo.dto.ka.KACallbackCreditReq;
import com.indo.game.pojo.dto.ka.KACallbackPlayReq;
import com.indo.game.pojo.dto.ka.KACallbackRevokeReq;
import com.indo.game.service.ka.KaCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * KA 回调服务
 */
@RestController
@RequestMapping("/ka/callBack")
public class KACallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KaCallbackService kaCallbackService;

    /**
     * 启动游戏权限验证
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object startGame(String params, HttpServletRequest request) {
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, params)) {
            return initFailureHash();
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback callBackQuerypoint 回调,IP:" + ip + " params:{}", params);
        Object object = kaCallbackService.startGame(JSONObject.parseObject(params, KACallbackCommonReq.class), ip);
        logger.info("KACallback startGame 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * KA 游戏下注
     */
    @RequestMapping(value = "/play", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object palyGame(String params, HttpServletRequest request) {
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, params)) {
            return initFailureHash();
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback palyGame 回调,IP:" + ip + " params:{}", params);
        Object object = kaCallbackService.palyGame(JSONObject.parseObject(params, KACallbackPlayReq.class), ip);
        logger.info("KACallback palyGame 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 游戏派彩
     */
    @RequestMapping(value = "/credit", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object credit(String params, HttpServletRequest request) {
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, params)) {
            return initFailureHash();
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback credit 回调,IP:" + ip + " params:{}", params);
        Object object = kaCallbackService.credit(JSONObject.parseObject(params, KACallbackCreditReq.class), ip);
        logger.info("KACallback credit 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 取消交易
     */
    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object revoke(String params, HttpServletRequest request) {
        params = "{\"transactionId\":\"76a5d8bcc406710d652a9cffa0397f34\",\"betAmount\":6000,\"winAmount\":0,\"jpc\":0,\"selections\":5,\"betPerSelection\":300,\"freeGames\":false,\"round\":0,\"roundsRemaining\":0,\"complete\":true,\"timestamp\":1645501617408,\"sessionId\":\"df58bb81ceb748196d97f3414bc69b42\",\"playerId\":\"swuserid\",\"currency\":\"USD\",\"action\":\"play\",\"gameId\":\"SuperShot2\",\"playerIp\":\"0:0:0:0:0:0:0:1\",\"token\":\"bd7ac51e8f71398216d08addaa710061244296a8c5bd23b0c0a608e4ebdc0c70\",\"partnerPlayerId\":\"swuserid\"}";

        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, params)) {
            return initFailureHash();
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback revoke 回调,IP:" + ip + " params:{}", params);
        Object object = kaCallbackService.revoke(JSONObject.parseObject(params, KACallbackRevokeReq.class), ip);
        logger.info("KACallback revoke 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 查询玩家余额
     */
    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object balance(String params, HttpServletRequest request) {
        params = "{\"transactionId\":\"76a5d8bcc406710d652a9cffa0397f34\",\"betAmount\":6000,\"winAmount\":0,\"jpc\":0,\"selections\":5,\"betPerSelection\":300,\"freeGames\":false,\"round\":0,\"roundsRemaining\":0,\"complete\":true,\"timestamp\":1645501617408,\"sessionId\":\"df58bb81ceb748196d97f3414bc69b42\",\"playerId\":\"swuserid\",\"currency\":\"USD\",\"action\":\"play\",\"gameId\":\"SuperShot2\",\"playerIp\":\"0:0:0:0:0:0:0:1\",\"token\":\"bd7ac51e8f71398216d08addaa710061244296a8c5bd23b0c0a608e4ebdc0c70\",\"partnerPlayerId\":\"swuserid\"}";
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, params)) {
            return initFailureHash();
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback balance 回调,IP:" + ip + " params:{}", params);
        Object object = kaCallbackService.balance(JSONObject.parseObject(params, KACallbackCommonReq.class), ip);
        logger.info("KACallback balance 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 玩家退出
     */
    @RequestMapping(value = "/end", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object end(String params, HttpServletRequest request) {
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, params)) {
            return initFailureHash();
        }
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback end 回调,IP:" + ip + " params:{}", params);
        Object object = kaCallbackService.end(JSONObject.parseObject(params, KACallbackCommonReq.class), ip);
        logger.info("KACallback end 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * hash 验证失败返回
     * @return Object
     */
    private Object initFailureHash(){
        KAApiResponseData ppCommonResp = new KAApiResponseData();
        ppCommonResp.setStatusCode(3);
        ppCommonResp.setStatus("哈希码安全验证失败");
        return JSONObject.toJSONString(ppCommonResp);
    }

    /**
     * 校验HASH
     *
     * @param hash  传递hash
     * @param param 参数
     * @return boolean
     */
    private boolean checkHash(String hash, String param) {
        return hash.equals(KAHashAESEncrypt.encrypt(param, OpenAPIProperties.KA_API_SECRET_KEY));
    }

}
