package com.indo.game.controller.mg;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.mg.MgCallBackReq;
import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;
import com.indo.game.service.mg.MgCallbackService;
import com.indo.game.service.pg.PgCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mg/callBack")
public class MgCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MgCallbackService mgCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/getbalance", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object getBalance(MgCallBackReq mgCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("cqCallBack {} getBalance 回调,params:{}", JSONObject.toJSONString(mgCallBackReq));
        Object object = mgCallbackService.mgBalanceCallback(mgCallBackReq, ip);
        logger.info("cqCallBack {} getBalance 回调下注返回数据 params:{}", object);
        return object;
    }

    /**
     * 令牌验证
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object verifySession(MgCallBackReq mgCallBackReq,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack {} verifySession回调,params:{}", JSONObject.toJSONString(mgCallBackReq));
        Object object = mgCallbackService.mgVerifyCallback(mgCallBackReq, ip);
        logger.info("pgCallBack {} verifySession回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 投付
     */
    @RequestMapping(value = "/updatebalance", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object adjustment(MgCallBackReq mgCallBackReq,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("pgCallBack {} adjustment回调,params:{}", JSONObject.toJSONString(mgCallBackReq));
        Object object = mgCallbackService.mgUpdatebalanceCallback(mgCallBackReq, ip);
        logger.info("pgCallBack {} adjustment回调返回数据 params:{}", object);
        return object;
    }
}
