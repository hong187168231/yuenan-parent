package com.indo.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.web.exception.BizException;
import com.indo.pay.pojo.resp.HuaRenCallbackReq;
import com.indo.pay.service.PaymentCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 支付回调
 *
 * @author puff
 */
@Slf4j
@RestController
@RequestMapping("/callback")
public class CallBackController {


    @Resource
    private PaymentCallBackService paymentCallBackService;


    @AllowAccess
    @RequestMapping("/huaRenCallback")
    public String dileiCallback(HttpServletRequest request) {
        log.info("进入hr支付回调接口==============================");

        String result = "";
        HuaRenCallbackReq huaRenCallbackReq = new HuaRenCallbackReq();
        try {
            // 订单状态
            String tradeResult = request.getParameter("tradeResult");
                log.info("进入hr支付回调接口2=============================="+tradeResult);
            // 商户号
            String mchId = request.getParameter("mchId");
            // 商家订单号
            String mchOrderNo = request.getParameter("mchOrderNo");
            // 原始订单金额
            String oriAmount = request.getParameter("oriAmount");
            // 交易金额
            String amount = request.getParameter("amount");
            // 订单时间
            String orderDate = request.getParameter("orderDate");
            // 平台支付订单号
            String orderNo = request.getParameter("orderNo");
            // 透传参数
            String merRetMsg = request.getParameter("merRetMsg");
            // 签名
            String sign = request.getParameter("sign");
            // 签名方式
            String signType = request.getParameter("signType");

            huaRenCallbackReq.setMchId(mchId);
            huaRenCallbackReq.setMchOrderNo(mchOrderNo);
            huaRenCallbackReq.setTransactionNo(orderNo);
            huaRenCallbackReq.setStatus(tradeResult);
            huaRenCallbackReq.setOriAmount(oriAmount);
            huaRenCallbackReq.setAmount(amount);
            huaRenCallbackReq.setOrderTime(orderDate);
            huaRenCallbackReq.setMerRetMsg(merRetMsg);
            huaRenCallbackReq.setSign(sign);
            huaRenCallbackReq.setSignType(signType);
            log.info("进入hr支付回调接口3=============================="+ JSONObject.toJSONString(huaRenCallbackReq));

            result = paymentCallBackService.huaRenCallback(huaRenCallbackReq);
        } catch (BizException e) {
            log.error("{}.huaRenCallback 失败:{},params:{}", this.getClass().getName(), e.getMessage(), JSONObject.toJSON(huaRenCallbackReq), e);
        } catch (Exception e) {
            log.error("{}.huaRenCallback 出错:{},params:{}", this.getClass().getName(), e.getMessage(), JSONObject.toJSON(huaRenCallbackReq), e);
        }
        return result;

    }


}