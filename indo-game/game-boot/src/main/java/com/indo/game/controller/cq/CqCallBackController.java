package com.indo.game.controller.cq;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.dto.cq.CqBetCallBackReq;
import com.indo.game.service.cq.CqCallbackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cq")
public class CqCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CqCallbackService cqCallbackService;


    /**
     * 查询余额
     */
    @RequestMapping(value = "/callBack/transaction/balance", method = RequestMethod.GET)
    @AllowAccess
    public Object getBalance(@RequestParam(value = "account") String account,HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack {} getBalance 回调,下注params:{}", JSONObject.toJSONString(account));
        Object object = cqCallbackService.cqBalanceCallback(account, ip, wtoken);
        logger.info("cqCallBack {} getBalance 回调下注返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 投注
     */
    @RequestMapping(value = "/callBack/transaction/game/bet", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object bet(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack {} bet 回调,下注params:{}", JSONObject.toJSONString(cqApiRequestData));
        Object object = cqCallbackService.cqBetCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack {} bet 回调下注返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 活動派彩
     */
    @RequestMapping(value = "/callBack/transaction/user/payoff", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object payOff(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack {} payOff 回调,params:{}", JSONObject.toJSONString(cqApiRequestData));
        Object object = cqCallbackService.cqPayOffCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack {} payOff 回调下注返回数据,取得用户的余额 params:{}", object);
        return object;
    }

    /**
     * 遊戲紅利
     */
    @RequestMapping(value = "/callBack/transaction/game/bonus", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object bonus(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack {} bonus 回调,params:{}", JSONObject.toJSONString(cqApiRequestData));
        Object object = cqCallbackService.cqBonusCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack {} bonus 回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 針對完成的訂單做補款
     */
    @RequestMapping(value = "/callBack/transaction/game/credit", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object credit(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack {} credit 回调,params:{}", JSONObject.toJSONString(cqApiRequestData));
        Object object = cqCallbackService.cqCreditCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack {} credit 回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 針對完成的訂單做扣款
     */
    @RequestMapping(value = "/callBack/transaction/game/debit", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object debit(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack {} debit 回调,params:{}", JSONObject.toJSONString(cqApiRequestData));
        Object object = cqCallbackService.cqDebitCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack {} debit 回调返回数据 params:{}", object);
        return object;
    }


    /**
     * 遊戲结算
     */
    @RequestMapping(value = "/callBack/transaction/game/rollin", method = RequestMethod.POST)
    @ResponseBody
    @AllowAccess
    public Object rollin(CqBetCallBackReq cqApiRequestData, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        String wtoken = request.getHeader("wtoken");
        logger.info("cqCallBack {} rollin 回调,params:{}", JSONObject.toJSONString(cqApiRequestData));
        Object object = cqCallbackService.cqRollinCallback(cqApiRequestData, ip, wtoken);
        logger.info("cqCallBack {} rollin 回调返回数据 params:{}", object);
        return object;
    }

}
