package com.indo.game.controller.ae;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.ae.AeCallBackParentReq;
import com.indo.game.pojo.dto.ae.AeCallBackTransferReq;
import com.indo.game.service.ae.AeCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ae")
public class AeCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AeCallbackService aeCallbackService;

    /**
     * 回调 余额查询
     */
    @RequestMapping(value = "/callBack/single/wallet/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@RequestBody JSONObject jsonObject , HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("aeCallBack {} GetBalance 回调,取得用户的余额 params:{}", JSONObject.toJSONString(jsonObject));
        AeCallBackParentReq aeApiRequestData = JSONObject.toJavaObject(jsonObject,AeCallBackParentReq.class);
        Object object = aeCallbackService.aeBalanceCallback(aeApiRequestData, ip);
        logger.info("aeCallBack {} GetBalance 回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }


    /**
     * 回调 单一钱包 额度转换
     */
    @RequestMapping(value = "/callBack/single/wallet/fund/transfer", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object transfer(@RequestBody JSONObject jsonObject , HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("aeCallBack {} transfer 回调,取得用户的余额 params:{}", JSONObject.toJSONString(jsonObject));
        AeCallBackTransferReq aeApiRequestData = JSONObject.toJavaObject(jsonObject,AeCallBackTransferReq.class);
        Object object = aeCallbackService.aeTransfer(aeApiRequestData, ip);
        logger.info("aeCallBack {} transfer 回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 回调 单一钱包 额度转换查询
     */
    @RequestMapping(value = "/callBack/single/wallet/fund/query", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object query(@RequestBody JSONObject jsonObject , HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("aeCallBack {} query 回调,取得用户的余额 params:{}", JSONObject.toJSONString(jsonObject));
        AeCallBackTransferReq aeApiRequestData = JSONObject.toJavaObject(jsonObject,AeCallBackTransferReq.class);
        Object object = aeCallbackService.query(aeApiRequestData, ip);
        logger.info("aeCallBack {} query 回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }
}
