package com.indo.game.controller.sgwin;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.pojo.vo.callback.sgwin.*;
import com.indo.game.service.sgwin.SGWinCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/SGWin/callBack")
public class SgwinCallBackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SGWinCallbackService sgWinCallbackService;

    @RequestMapping(value = "/VerifyApi", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object VerifyApi(SGWinVerifyApiCallBackReq sgWinVerifyApiCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("SgwinCallBack  VerifyApi回调params:{},wtoken:{}", JSONObject.toJSONString(sgWinVerifyApiCallBackReq));
        Object object = sgWinCallbackService.getVerifyApi(sgWinVerifyApiCallBackReq, ip);
        logger.info("SgwinCallBack  VerifyApi回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/GetBalance", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object getBalance(SGWinGetBalanceCallBackReq sgWinGetBalanceCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("SgwinCallBack  获取余额getBalance回调params:{},wtoken:{}", JSONObject.toJSONString(sgWinGetBalanceCallBackParentReq));
        Object object = sgWinCallbackService.getUserBalance(sgWinGetBalanceCallBackParentReq, ip);
        logger.info("SgwinCallBack  获取余额getBalance回调返回数据 params:{}", object);
        return object;
    }

    /**
     * 下注
     */
    @RequestMapping(value = "/PlaceBet", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object bets(SGWinBetsCallBackReq sgWinBetsCallBackParentReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("SgwinCallBack  下注PlaceBet回调params:{},wtoken:{}", JSONObject.toJSONString(sgWinBetsCallBackParentReq));
        Object object = sgWinCallbackService.sgwinBetCallback(sgWinBetsCallBackParentReq, ip);
        logger.info("SgwinCallBack  下注PlaceBet回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/NotifySettle", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object notifySettle(SGWinNotifySettleCallBackReq sgWinNotifySettleCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("SgwinCallBack  结算notifySettle回调params:{},wtoken:{}", JSONObject.toJSONString(sgWinNotifySettleCallBackReq));
        Object object = sgWinCallbackService.notifySettle(sgWinNotifySettleCallBackReq, ip);
        logger.info("SgwinCallBack  结算notifySettle回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/Refund", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    @AllowAccess
    public Object refund(SGWinRefundCallBackReq<SGWinTransactionsCallBackReq> sgWinRefundCallBackReq, HttpServletRequest request) {

        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("SgwinCallBack  refund回调params:{},wtoken:{}", JSONObject.toJSONString(sgWinRefundCallBackReq));
        Object object = sgWinCallbackService.refund(sgWinRefundCallBackReq, ip);
        logger.info("SgwinCallBack  refund回调返回数据 params:{}", object);
        return object;
    }

}
