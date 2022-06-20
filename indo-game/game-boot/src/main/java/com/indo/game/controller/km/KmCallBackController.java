package com.indo.game.controller.km;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.dg.DgCallBackReq;
import com.indo.game.service.km.KmCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/kingmaker/callBack")
public class KmCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KmCallbackService kmCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/wallet/balance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(@RequestBody JSONObject jsonObject,
                             HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("kmCallBack {} getBalance 回调,下注params:{}", jsonObject);
        Object object = kmCallbackService.kmBalanceCallback(jsonObject, ip);
        logger.info("kmCallBack {} getBalance 回调下注返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 扣款接口
     */
    @RequestMapping(value = "/wallet/debit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object debit(@RequestBody JSONObject jsonObject,
                           HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("kmCallBack {} debit回调,params:{}", jsonObject, ip);
        Object object = kmCallbackService.kmDebitCallback(jsonObject, ip);
        logger.info("kmCallBack {} debit回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 扣款接口
     */
    @RequestMapping(value = "/wallet/credit", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object credit(@RequestBody JSONObject jsonObject,
                        HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("kmCallBack {} credit回调,params:{}", jsonObject, ip);
        Object object = kmCallbackService.kmCreditCallback(jsonObject, ip);
        logger.info("kmCallBack {} credit回调返回数据, params:{}", object);
        return object;
    }

}
