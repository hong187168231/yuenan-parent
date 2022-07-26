package com.indo.game.controller.bti;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.PostRawParamsParseUtil;
import com.indo.game.common.util.XmlUtil;
import com.indo.game.pojo.dto.bti.*;
import com.indo.game.service.bti.BtiCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bti/callBack")
public class BtiCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BtiCallbackService btiCallbackService;

    @RequestMapping(value = "/ValidateToken", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object validateToken(@RequestParam("auth_token") String authToken, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("BtiCallback validateToken 回调, authToken:{},ip:{}", authToken, ip);
        Object object = btiCallbackService.validateToken(authToken, ip);
        logger.info("BtiCallback validateToken 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/reserve", method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object reserve(HttpServletRequest request, BtiReserveRequst btiReserveRequst) {
        String ip = IPAddressUtil.getIpAddress(request);
//        try {
//            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
//            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
//        } catch (Exception e) {
//            logger.error("BtiCallback reserve 转换失败", e);
//        }
        logger.info("BtiCallback reserve 回调, params:{},ip:{}", JSONObject.toJSONString(btiReserveRequst), ip);
        Object object = btiCallbackService.reserve(btiReserveRequst, ip);
        logger.info("BtiCallback reserve 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/debitreserve", method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object debitReserve(HttpServletRequest request, BtiDebitReserveRequst btiDebitReserveRequst) {
        String ip = IPAddressUtil.getIpAddress(request);
//        BtiReserveBetsRequest reserveBetsRequest = null;
//        try {
//            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
//            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
//        } catch (Exception e) {
//            logger.error("BtiCallback debitReserve 转换失败", e);
//        }
        logger.info("BtiCallback debitReserve 回调, params:{},ip:{}", JSONObject.toJSONString(btiDebitReserveRequst));
        Object object = btiCallbackService.debitReserve(btiDebitReserveRequst, ip);
        logger.info("BtiCallback debitReserve 回调返回数据 params:{}", object);
        return object;
    }


    @RequestMapping(value = "/cancelreserve", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object cancelReserve(HttpServletRequest request, BtiCancelReserveRequst btiCancelReserveRequst) {
        String ip = IPAddressUtil.getIpAddress(request);
//        BtiReserveBetsRequest reserveBetsRequest = null;
//        try {
//            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
//            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
//        } catch (Exception e) {
//            logger.error("BtiCallback cancelReserve 转换失败", e);
//        }
        logger.info("BtiCallback cancelReserve 回调, params:{},ip:{}", JSONObject.toJSONString(btiCancelReserveRequst));
        Object object = btiCallbackService.cancelReserve(btiCancelReserveRequst, ip);
        logger.info("BtiCallback cancelReserve 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/commitreserve", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object commitReserve(HttpServletRequest request,BtiCommitReserveRequst btiCommitReserveRequst) {
        String ip = IPAddressUtil.getIpAddress(request);
//        BtiReserveBetsRequest reserveBetsRequest = null;
//        try {
//            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
//            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
//        } catch (Exception e) {
//            logger.error("BtiCallback commitReserve 转换失败", e);
//        }
        logger.info("BtiCallback commitReserve 回调, params:{},ip:{}", JSONObject.toJSONString(btiCommitReserveRequst), ip);
        Object object = btiCallbackService.commitReserve(btiCommitReserveRequst, ip);
        logger.info("BtiCallback commitReserve 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/debitcustomer", method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object debitCustomer(HttpServletRequest request,BtiDebitCustomerRequst btiDebitCustomerRequst) {
        String ip = IPAddressUtil.getIpAddress(request);
//        BtiCreditRequest btiCreditRequest = null;
//        try {
//            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
//            btiCreditRequest = (BtiCreditRequest) XmlUtil.convertXmlStrToObject(BtiCreditRequest.class, params);
//        } catch (Exception e) {
//            logger.error("BtiCallback debitCustomer 转换失败", e);
//        }
        logger.info("BtiCallback debitCustomer 回调, params:{},ip:{}", JSONObject.toJSONString(btiDebitCustomerRequst), ip);
        Object object = btiCallbackService.debitCustomer(btiDebitCustomerRequst, ip);
        logger.info("BtiCallback debitCustomer 回调返回数据 params:{}", object);
        return object;

    }

    @RequestMapping(value = "/creditcustomer", method = RequestMethod.POST,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object creditCustomer(HttpServletRequest request,BtiCreditCustomerRequst btiCreditCustomerRequst) {
        String ip = IPAddressUtil.getIpAddress(request);
//        String reqId = request.getParameter("req_id");
//        BtiCreditRequest btiCreditRequest = null;
//        try {
//            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
//            btiCreditRequest = (BtiCreditRequest) XmlUtil.convertXmlStrToObject(BtiCreditRequest.class, params);
//        } catch (Exception e) {
//            logger.error("BtiCallback creditCustomer 转换失败", e);
//        }
        logger.info("BtiCallback creditCustomer 回调, params:{},ip:{}", JSONObject.toJSONString(btiCreditCustomerRequst), ip);
        Object object = btiCallbackService.creditCustomer(btiCreditCustomerRequst, ip);
        logger.info("BtiCallback creditCustomer 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object status(@RequestParam("token") String token, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("BtiCallback status 回调, token:{},ip:{}", token, ip);
        Object object = btiCallbackService.status(token, ip);
        logger.info("BtiCallback status 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET,produces = "text/plain;charset=UTF-8")
    @AllowAccess
    public Object refresh(@RequestParam("token") String token, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("BtiCallback refresh 回调, token:{},ip:{}", token, ip);
        Object object = btiCallbackService.refresh(token, ip);
        logger.info("BtiCallback refresh 回调返回数据 params:{}", object);
        return object;
    }
}
