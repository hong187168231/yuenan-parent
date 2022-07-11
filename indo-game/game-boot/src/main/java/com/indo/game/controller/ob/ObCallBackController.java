package com.indo.game.controller.ob;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.ae.AeCallBackParentReq;
import com.indo.game.pojo.dto.ae.AeCallBackTransferReq;
import com.indo.game.pojo.dto.ob.ObCallBackParentReq;
import com.indo.game.service.ae.AeCallbackService;
import com.indo.game.service.ob.ObCallbackService;

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
@RequestMapping("/yabo/callBack")
public class ObCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObCallbackService obCallbackService;

    /**
     * 回调 余额查询
     */
    @RequestMapping(value = "/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(ObCallBackParentReq obCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("obCallBack GetBalance 回调,取得用户的余额 params:{}", JSONObject.toJSONString(obCallBackParentReq));
        Object object = obCallbackService.obBalanceCallback(obCallBackParentReq, ip);
        logger.info("obCallBack GetBalance 回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }


    /**
     * 回调 单一钱包 额度转换
     */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object transfer(ObCallBackParentReq obCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("obCallBack transfer回调,取得用户的余额 params:{}", JSONObject.toJSONString(obCallBackParentReq));
        Object object = obCallbackService.obTransfer(obCallBackParentReq, ip);
        logger.info("obCallBack transfer回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 回调 单一钱包 额度转换查询
     */
    @RequestMapping(value = "/transferstatus", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object transferStatus(ObCallBackParentReq obCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("aeCallBack transferStatus回调,取得用户的余额 params:{}", JSONObject.toJSONString(obCallBackParentReq));
        Object object = obCallbackService.transferStatus(obCallBackParentReq, ip);
        logger.info("aeCallBack transferStatus回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }
}
