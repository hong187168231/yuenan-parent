package com.indo.game.controller.ka;

import com.alibaba.fastjson.JSONArray;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @RequestMapping(value = "/start", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object startGame(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {
        String json = JSONObject.toJSONString(map);
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, json)) {
            return initFailureHash();
        }
        KACallbackCommonReq kaCallbackCommonReq = JSONObject.parseObject(json, KACallbackCommonReq.class);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback callBackQuerypoint 回调,IP: {}, hash: {} params:{}",ip, hash, map.toString());
        Object object = kaCallbackService.startGame(kaCallbackCommonReq, ip);
        logger.info("KACallback startGame 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * KA 游戏下注
     */
    @RequestMapping(value = "/play", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object palyGame(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {
        String json = JSONObject.toJSONString(map);
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, json)) {
            return initFailureHash();
        }

        KACallbackPlayReq kaCallbackPlayReq = JSONObject.parseObject(json, KACallbackPlayReq.class);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback callBackQuerypoint 回调,IP: {}, hash: {} params:{}",ip, hash, map.toString());
        Object object = kaCallbackService.palyGame(kaCallbackPlayReq, ip);
        logger.info("KACallback palyGame 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 游戏派彩
     */
    @RequestMapping(value = "/credit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object credit(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {
        String json = JSONObject.toJSONString(map);
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, json)) {
            return initFailureHash();
        }

        KACallbackCreditReq kaCallbackCreditReq = JSONObject.parseObject(json, KACallbackCreditReq.class);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback callBackQuerypoint 回调,IP: {}, hash: {} params:{}",ip, hash, map.toString());
        Object object = kaCallbackService.credit(kaCallbackCreditReq, ip);
        logger.info("KACallback credit 回调返回数据, params:{}", object);
        return object;
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(3.22);
        System.out.println(Double.parseDouble(a.toString()));
    }

    /**
     * 取消交易
     */
    @RequestMapping(value = "/revoke", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object revoke(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {

        String json = JSONObject.toJSONString(map);
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, json)) {
            return initFailureHash();
        }

        KACallbackRevokeReq kaCallbackRevokeReq = JSONObject.parseObject(json, KACallbackRevokeReq.class);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback callBackQuerypoint 回调,IP: {}, hash: {} params:{}",ip, hash, map.toString());
        Object object = kaCallbackService.revoke(kaCallbackRevokeReq, ip);
        logger.info("KACallback revoke 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 查询玩家余额
     */
    @RequestMapping(value = "/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object balance(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {
        String json = JSONObject.toJSONString(map);
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, json)) {
            return initFailureHash();
        }

        KACallbackCommonReq kaCallbackCommonReq = JSONObject.parseObject(json, KACallbackCommonReq.class);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback callBackQuerypoint 回调,IP: {}, hash: {} params:{}",ip, hash, map.toString());
        Object object = kaCallbackService.balance(kaCallbackCommonReq, ip);
        logger.info("KACallback balance 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 玩家退出
     */
    @RequestMapping(value = "/end", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object end(@RequestBody LinkedHashMap<String, Object> map, HttpServletRequest request) {
        String json = JSONObject.toJSONString(map);
        String hash = request.getParameter("hash");
        // hash验证
        if (!checkHash(hash, json)) {
            return initFailureHash();
        }
        KACallbackCommonReq kaCallbackCommonReq = JSONObject.parseObject(json, KACallbackCommonReq.class);
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("KACallback callBackQuerypoint 回调,IP: {}, hash: {} params:{}",ip, hash, map.toString());
        Object object = kaCallbackService.end(kaCallbackCommonReq, ip);
        logger.info("KACallback end 回调返回数据, params:{}", object);
        return object;
    }

    /**
     * hash 验证失败返回
     *
     * @return Object
     */
    private Object initFailureHash() {
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
