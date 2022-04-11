package com.indo.game.controller.bti;


import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.utils.IPAddressUtil;
import com.indo.game.common.util.PostRawParamsParseUtil;
import com.indo.game.common.util.XmlUtil;
import com.indo.game.pojo.dto.bti.BtiCreditRequest;
import com.indo.game.pojo.dto.bti.BtiReserveBetsRequest;
import com.indo.game.service.bti.BtiCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bti/callBack")
public class BtiCallbackController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BtiCallbackService btiCallbackService;

    @RequestMapping(value = "/ValidateToken", method = RequestMethod.GET)
    @AllowAccess
    public Object validateToken(@RequestParam("auth_token") String authToken, HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        logger.info("BtiCallback validateToken 回调, params:{}", authToken);
        Object object = btiCallbackService.validateToken(authToken, ip);
        logger.info("BtiCallback validateToken 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/Reserve", method = RequestMethod.POST)
    @AllowAccess
    public Object reserve(HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        BtiReserveBetsRequest reserveBetsRequest = null;
        try {
            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
        } catch (Exception e) {
            logger.error("BtiCallback reserve 转换失败", e);
        }
        logger.info("BtiCallback reserve 回调, params:{}", JSONObject.toJSONString(reserveBetsRequest));
        Object object = btiCallbackService.reserve(reserveBetsRequest, ip);
        logger.info("BtiCallback reserve 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/DebitReserve", method = RequestMethod.POST)
    @AllowAccess
    public Object debitReserve(HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        String reqId = request.getParameter("req_id");
        BtiReserveBetsRequest reserveBetsRequest = null;
        try {
            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
        } catch (Exception e) {
            logger.error("BtiCallback debitReserve 转换失败", e);
        }
        logger.info("BtiCallback debitReserve 回调, params:{}", JSONObject.toJSONString(reserveBetsRequest));
        Object object = btiCallbackService.debitReserve(reserveBetsRequest, ip, reqId);
        logger.info("BtiCallback debitReserve 回调返回数据 params:{}", object);
        return object;
    }


    @RequestMapping(value = "/CancelReserve", method = RequestMethod.GET)
    @AllowAccess
    public Object cancelReserve(HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        BtiReserveBetsRequest reserveBetsRequest = null;
        try {
            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
        } catch (Exception e) {
            logger.error("BtiCallback cancelReserve 转换失败", e);
        }
        logger.info("BtiCallback cancelReserve 回调, params:{}", JSONObject.toJSONString(reserveBetsRequest));
        Object object = btiCallbackService.cancelReserve(reserveBetsRequest, ip);
        logger.info("BtiCallback cancelReserve 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/CommitReserve", method = RequestMethod.GET)
    @AllowAccess
    public Object commitReserve(HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        BtiReserveBetsRequest reserveBetsRequest = null;
        try {
            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
            reserveBetsRequest = (BtiReserveBetsRequest) XmlUtil.convertXmlStrToObject(BtiReserveBetsRequest.class, params);
        } catch (Exception e) {
            logger.error("BtiCallback commitReserve 转换失败", e);
        }
        logger.info("BtiCallback commitReserve 回调, params:{}", JSONObject.toJSONString(reserveBetsRequest));
        Object object = btiCallbackService.commitReserve(reserveBetsRequest, ip);
        logger.info("BtiCallback commitReserve 回调返回数据 params:{}", object);
        return object;
    }

    @RequestMapping(value = "/DebitCustomer", method = RequestMethod.POST)
    @AllowAccess
    public Object debitCustomer(HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        String reqId = request.getParameter("req_id");
        BtiCreditRequest btiCreditRequest = null;
        try {
            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
            btiCreditRequest = (BtiCreditRequest) XmlUtil.convertXmlStrToObject(BtiCreditRequest.class, params);
        } catch (Exception e) {
            logger.error("BtiCallback debitCustomer 转换失败", e);
        }
        logger.info("BtiCallback debitCustomer 回调, params:{}", JSONObject.toJSONString(btiCreditRequest));
        Object object = btiCallbackService.debitCustomer(btiCreditRequest, ip, reqId);
        logger.info("BtiCallback debitCustomer 回调返回数据 params:{}", object);
        return object;

    }

    @RequestMapping(value = "/CreditCustomer", method = RequestMethod.POST)
    @AllowAccess
    public Object creditCustomer(HttpServletRequest request) {
        String ip = IPAddressUtil.getIpAddress(request);
        String reqId = request.getParameter("req_id");
        BtiCreditRequest btiCreditRequest = null;
        try {
            String params = PostRawParamsParseUtil.getRequestPostBytes(request);
            btiCreditRequest = (BtiCreditRequest) XmlUtil.convertXmlStrToObject(BtiCreditRequest.class, params);
        } catch (Exception e) {
            logger.error("BtiCallback creditCustomer 转换失败", e);
        }
        logger.info("BtiCallback creditCustomer 回调, params:{}", JSONObject.toJSONString(btiCreditRequest));
        Object object = btiCallbackService.creditCustomer(btiCreditRequest, ip, reqId);
        logger.info("BtiCallback creditCustomer 回调返回数据 params:{}", object);
        return object;
    }
}
