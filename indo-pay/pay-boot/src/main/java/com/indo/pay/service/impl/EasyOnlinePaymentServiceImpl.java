package com.indo.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.util.http.HttpClient;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.factory.AbstractOnlinePaymentService;
import com.indo.pay.mapper.PayChannelMapper;
import com.indo.pay.mapper.RechargeMapper;
import com.indo.pay.pojo.dto.RechargeCallBackDTO;
import com.indo.pay.pojo.dto.RechargeDTO;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.BasePayReq;
import com.indo.pay.pojo.req.EasyPayReq;
import com.indo.pay.pojo.req.HuaRenPayReq;
import com.indo.pay.pojo.resp.EasyCallbackReq;
import com.indo.pay.pojo.resp.HuaRenCallbackReq;
import com.indo.pay.pojo.resp.HuaRenPayResp;
import com.indo.pay.service.PaymentCallBackService;
import com.indo.pay.service.PaymentService;
import com.indo.pay.util.SignMd5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author puff
 * easyPay支付service
 **/
@Slf4j
@Service("easyOnlinePaymentService")
public class EasyOnlinePaymentServiceImpl extends AbstractOnlinePaymentService {

    @Resource
    private RechargeMapper rechargeMapper;

    @Resource
    private PaymentService paymentService;
    @Resource
    private PaymentCallBackService paymentCallBackService;

    @Resource
    private PayChannelMapper payChannelConfigMapper;

    @Override
    protected <T> T callPayService(BasePayReq req, Class<T> clazz) {
        // 检验参数
        EasyPayReq easyPayReq;
        if (req instanceof EasyPayReq) {
            easyPayReq = (EasyPayReq) req;
        } else {
            log.error("paylog HrPay支付 创建订单 {} callPayService 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return null;
        }
        // 生成验签
        Map<String, String> reqMap = new TreeMap<>();

        reqMap.put("merchantId", easyPayReq.getMerchantNo());
        reqMap.put("orderId", easyPayReq.getMerchantOrderNo());
        reqMap.put("coin", easyPayReq.getTradeAmount().toString());
        reqMap.put("productId", "1000");
        reqMap.put("goods", "indo_pay");
        reqMap.put("attach", "1");
        reqMap.put("notifyUrl", easyPayReq.getNotifyUrl());
        reqMap.put("redirectUrl", easyPayReq.getPageUrl());
        String sign = SignMd5Utils.createSmallSign(reqMap, easyPayReq.getSecretKey());
        reqMap.put("sign", sign.toUpperCase());

        // http请求
        JSONObject httpJsonObj = new JSONObject();
        HuaRenPayResp huaRenPayResp = new HuaRenPayResp();
        try {
            httpJsonObj = HttpClient.doPost(easyPayReq.getPayUrl() + "/v1/pay/createOrder", JSON.toJSONString(reqMap));
            log.info("paylog easy支付 创建订单请求 {} callPayService {}", this.getClass().getName(), reqMap);
        } catch (Exception e) {
            log.error("paylog easy支付 创建订单失败 {} callPayService {}", this.getClass().getName(), reqMap, e);
            huaRenPayResp.setFlag(false);
            huaRenPayResp.setMsg("支付异常 " + httpJsonObj);
            return (T) huaRenPayResp;
        }
        log.info("paylog easy支付 创建订单返回 {} callPayService {}", this.getClass().getName(), httpJsonObj);
        if (!ObjectUtils.isEmpty(httpJsonObj) && httpJsonObj.get("code") != null) {
            String msg = httpJsonObj.getString("message");
            if (httpJsonObj.get("code").equals(0)) {
                JSONObject dataObj = httpJsonObj.getJSONObject("data");
                huaRenPayResp.setFlag(true);
                huaRenPayResp.setMsg("成功");
                huaRenPayResp.setHtml(dataObj.getString("url"));
                return (T) huaRenPayResp;
            } else {
                huaRenPayResp.setFlag(false);
                huaRenPayResp.setMsg(msg);
                return (T) huaRenPayResp;
            }
        } else {
            log.error("paylog easy支付 创建订单返回错误 {} callPayService {}",
                    this.getClass().getName(), JSON.toJSONString(httpJsonObj));
            huaRenPayResp.setFlag(false);
            return (T) huaRenPayResp;
        }
    }


    @Override
    protected <R extends BasePayReq> boolean insertPayment(R req) {
        // 检验参数
        HuaRenPayReq huaRenPayReq;
        if (req instanceof HuaRenPayReq) {
            huaRenPayReq = (HuaRenPayReq) req;
        } else {
            log.error("paylog easy支付 持久化订单 {} insertPayment 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return false;
        }
        // 请求参数
        RechargeDTO rechargeDto = new RechargeDTO();
        rechargeDto.setAmount(huaRenPayReq.getTradeAmount().toString());
        rechargeDto.setOrderNo(req.getMerchantOrderNo());
        rechargeDto.setMemId(req.getMemId());
        return paymentService.insertPayment(rechargeDto);
    }

    @Override
    protected <R extends BaseCallBackReq> boolean accountRecharge(R req) {
        // 平台订单号、交易金额、商户订单号
        RechargeCallBackDTO callBackDto = new RechargeCallBackDTO();
        callBackDto.setTransactionNo(req.getTransactionNo());
        callBackDto.setAmount(new BigDecimal(req.getAmount()));
        callBackDto.setOrderNo(req.getMchOrderNo());
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRecharge> query = new QueryWrapper<>();
        query.lambda().eq(PayRecharge::getOrderNo, req.getMchOrderNo());
        PayRecharge payRecharge = rechargeMapper.selectOne(query);
        // 完结订单，更新订单信息
        boolean SuccessFlag = paymentCallBackService.paymentSuccess(callBackDto, payRecharge);
        return SuccessFlag;
    }

    /**
     * 参数验证
     */
    @Override
    protected <R extends BaseCallBackReq> boolean checkCallBackParams(R req) {
        // 检验参数
        EasyCallbackReq easyCallbackReq = new EasyCallbackReq();
        if (req instanceof EasyCallbackReq) {
            easyCallbackReq = (EasyCallbackReq) req;
        } else {
            log.error("paylog easy支付 回调参数验证 {} checkCallBackParams 入口参数错误", this.getClass().getName());
            return false;
        }
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRecharge> query = new QueryWrapper<>();
        query.lambda().eq(PayRecharge::getOrderNo, req.getMchOrderNo());
        PayRecharge payRecharge = rechargeMapper.selectOne(query);
        if (ObjectUtils.isEmpty(payRecharge)) {
            log.info("easy支付参数验证异常,查无此订单,req={}", JSON.toJSONString(req));
            return false;
        }
        PayChannelConfig payChannelConfig = payChannelConfigMapper.selectById(payRecharge.getChannelId());
        if (ObjectUtils.isEmpty(payChannelConfig)) {
            log.info("easy支付参数验证异常,商户无,req={}", JSON.toJSONString(req));
            return false;
        }
        // 生成验签
        Map<String, String> metaSignMap = new TreeMap<>();
        metaSignMap.put("code", easyCallbackReq.getCode() + "");
        metaSignMap.put("message", easyCallbackReq.getMessage());
        metaSignMap.put("merchantId", easyCallbackReq.getMerchantId());
        metaSignMap.put("outTradeNo", easyCallbackReq.getOutTradeNo());
        metaSignMap.put("coin", easyCallbackReq.getCoin());
        // 商户key
        String signStr = SignMd5Utils.createSign(metaSignMap, payChannelConfig.getSecretKey());
        if (!signStr.equals(easyCallbackReq.getSign())) {
            log.error("easy支付签名不在确=={}", signStr);
            return false;
        }
        if (PayConstants.PAY_RECHARGE_STATUS_COMPLETE.equals(payRecharge.getOrderStatus())) {
            log.error("easy支付此单号已经处理=={}", easyCallbackReq.getTransactionNo());
            return false;
        }
        // 验证金额
        BigDecimal payAmt = payRecharge.getRealAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (new BigDecimal(easyCallbackReq.getCoin()).compareTo(payAmt) != 0) {
            log.error("paylog easy支付 回调参数验证 {} checkCallBackParams 金额不一致 {} {}", this.getClass().getName(), easyCallbackReq.getCoin(), payAmt);
            return false;
        }
        return true;
    }
}