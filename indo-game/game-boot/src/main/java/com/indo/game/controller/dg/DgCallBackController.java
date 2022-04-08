package com.indo.game.controller.dg;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.cq.CqBetCallBackReq;
import com.indo.game.pojo.dto.dg.DgCallBackReq;
import com.indo.game.service.cq.CqCallbackService;
import com.indo.game.service.dg.DgCallbackService;

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
@RequestMapping("/dg/callBack")
public class DgCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DgCallbackService dgCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/user/getBalance/{agentName}", method = RequestMethod.POST)
    @AllowAccess
    public Object getBalance(@PathVariable(name = "agentName") String agentName, @RequestBody DgCallBackReq dgCallBackReq,
                             HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("dgCallBack {} getBalance 回调,下注params:{}", JSONObject.toJSONString(dgCallBackReq));
        Object object = dgCallbackService.dgBalanceCallback(agentName, dgCallBackReq, ip);
        logger.info("dgCallBack {} getBalance 回调下注返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 存取款接口
     */
    @RequestMapping(value = "/account/transfer/{agentName}", method = RequestMethod.POST)
    @AllowAccess
    public Object transfer(@PathVariable(name = "agentName") String agentName, @RequestBody DgCallBackReq dgCallBackReq,
                              HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("dgCallBack {} transfer回调,params:{}", JSONObject.toJSONString(dgCallBackReq), agentName);
        Object object = dgCallbackService.dgTransferCallback(dgCallBackReq, ip, agentName);
        logger.info("dgCallBack {} transfer回调返回数据, params:{}", object);
        return object;
    }

    /**
     * 确认存取款结果接口
     */
    @RequestMapping(value = "/account/checkTransfer/{agentName} ", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object checkTransfer(@PathVariable(name = "agentName") String agentName, @RequestBody DgCallBackReq dgCallBackReq,
                      HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("dgCallBack {} checkTransfer回调params:{}", JSONObject.toJSONString(dgCallBackReq), agentName);
        Object object = dgCallbackService.dgCheckTransferCallback(dgCallBackReq, ip, agentName);
        logger.info("dgCallBack {} checkTransfer回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 请求回滚转账事务
     */
    @RequestMapping(value = "/account/inform/{agentName} ", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object inform(@PathVariable(name = "agentName") String agentName, @RequestBody DgCallBackReq dgCallBackReq,
                         HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("dgCallBack {} inform回调,params:{}", JSONObject.toJSONString(dgCallBackReq), agentName);
        Object object = dgCallbackService.djInformCallback(dgCallBackReq, ip, agentName);
        logger.info("dgCallBack {} inform回调返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 遊戲紅利
     */
    @RequestMapping(value = "/account/order/{agentName}", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object order(@PathVariable(name = "agentName") String agentName, @RequestBody DgCallBackReq dgCallBackReq,
                         HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cqCallBack {} bonus回调,params:{}", JSONObject.toJSONString(dgCallBackReq), agentName);
        Object object = dgCallbackService.mgOrderCallback(dgCallBackReq, ip, agentName);
        logger.info("cqCallBack {} bonus回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 針對完成的訂單做補款
     */
    @RequestMapping(value = "/account/unsettle/{agentName} ", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object unsettle(@PathVariable(name = "agentName") String agentName, @RequestBody DgCallBackReq dgCallBackReq,
                        HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cqCallBack {} credit回调,params:{}", JSONObject.toJSONString(dgCallBackReq), agentName);
        Object object = dgCallbackService.mgUnsettleCallback(dgCallBackReq, ip, agentName);
        logger.info("cqCallBack {} credit回调返回数据 params:{}", object);
        return object;
    }

}
