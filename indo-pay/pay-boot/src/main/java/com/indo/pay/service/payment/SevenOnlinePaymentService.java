package com.indo.pay.service.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.web.util.http.HttpClient;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.pay.factory.AbstractOnlinePaymentService;
import com.indo.pay.mapper.PayChannelMapper;
import com.indo.pay.mapper.RechargeMapper;
import com.indo.pay.pojo.dto.PayCallBackDTO;
import com.indo.pay.pojo.dto.RechargeDTO;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.BasePayReq;
import com.indo.pay.pojo.req.SevenPayReq;
import com.indo.pay.pojo.resp.SevenCallbackReq;
import com.indo.pay.pojo.resp.SevenPayResp;
import com.indo.pay.util.SignMd5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * 777支付service
 **/
@Slf4j
@Service("sevenOnlinePaymentService")
public class SevenOnlinePaymentService extends AbstractOnlinePaymentService {

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
        SevenPayReq sevenPayReq;
        if (req instanceof SevenPayReq) {
            sevenPayReq = (SevenPayReq) req;
        } else {
            log.error("paylog 777Pay支付 创建订单 {} callPayService 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return null;
        }
        // 生成验签
        Map<String, String> reqMap = new TreeMap<>();

        reqMap.put("userid", sevenPayReq.getMerchantNo());
        reqMap.put("orderid", sevenPayReq.getMerchantOrderNo());
        reqMap.put("type", sevenPayReq.getPayType());
        reqMap.put("amount", sevenPayReq.getTradeAmount().toString());
        reqMap.put("notifyurl", sevenPayReq.getNotifyUrl());
        reqMap.put("returnurl", sevenPayReq.getPageUrl());
        reqMap.put("note", sevenPayReq.getNote());

        reqMap.put("type", sevenPayReq.getType());
        String sign = createSign(sevenPayReq.getSecretKey(), sevenPayReq.getMerchantOrderNo(), sevenPayReq.getTradeAmount());
        reqMap.put("sign", sign);

        // http请求
        JSONObject httpJsonObj = new JSONObject();
        SevenPayResp sevenPayResp = new SevenPayResp();
        /*
          {
            "code": 1,
            "data": "{" +
            "\"amount\":\"50001.0000\"," +
            "\"bamount\":null," +
            "\"orderid\":\"HSBxpKBp4c2x1647505032703\",\
            "pageurl\":\"http://120.77.76.146:9080/t5/index.html?ticket=25a3c10ded4ddd7d5ab1430ad3a6d326\",\
            "sign\":\"f17a81b2cff61818fcb848070796e5c3\"," +
            "\"ticket\":\"25a3c10ded4ddd7d5ab1430ad3a6d326\",\
            "type\":\"Momo\"," +
            "\"userid\":\"4d492429445c4d1698647dcdab8d87c0\"}",

            "msg": "success"
           }
        */
        try {
            httpJsonObj = HttpClient.doPost(sevenPayReq.getPayUrl() + "/api/create", JSON.toJSONString(reqMap));
            log.info("paylog 777支付 创建订单请求 {} callPayService {}", this.getClass().getName(), reqMap);
        } catch (Exception e) {
            log.error("paylog 777支付 创建订单失败 {} callPayService {}", this.getClass().getName(), reqMap, e);
            sevenPayResp.setFlag(false);
            sevenPayResp.setMsg("支付异常 " + httpJsonObj);
            return (T) sevenPayResp;
        }
        log.info("paylog 777支付 创建订单返回 {} callPayService {}", this.getClass().getName(), httpJsonObj);
        if (!ObjectUtils.isEmpty(httpJsonObj) && httpJsonObj.get("code") != null) {
           String msg = httpJsonObj.getString("msg");
            if (httpJsonObj.get("code").equals(1)) {
                JSONObject dataObj = httpJsonObj.getJSONObject("data");
                sevenPayResp.setFlag(true);
                sevenPayResp.setMsg("成功");
                sevenPayResp.setHtml(dataObj.getString("pageurl"));
                return (T) sevenPayResp;
            } else {
                sevenPayResp.setFlag(false);
                sevenPayResp.setMsg(msg);
                return (T) sevenPayResp;
            }
        } else {
            log.error("paylog 777支付 创建订单返回错误 {} callPayService {}",
                    this.getClass().getName(), JSON.toJSONString(httpJsonObj));
            sevenPayResp.setFlag(false);
            return (T) sevenPayResp;
        }
    }

    private String createSign(String apikey, String orderid, BigDecimal amount) {
        String calcStr = apikey + orderid + amount.toString();
        return SignMd5Utils.MD5Hex(calcStr, false);
    }

    @Override
    protected <R extends BasePayReq> boolean insertPayment(R req) {
        // 检验参数
        SevenPayReq sevenPayReq;
        if (req instanceof SevenPayReq) {
            sevenPayReq = (SevenPayReq) req;
        } else {
            log.error("paylog 777支付 持久化订单 {} insertPayment 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return false;
        }
        // 请求参数
        RechargeDTO rechargeDto = new RechargeDTO();
        rechargeDto.setAmount(sevenPayReq.getTradeAmount().toString());
        rechargeDto.setOrderNo(req.getMerchantOrderNo());
        rechargeDto.setMemId(req.getMemId());
        return paymentService.insertPayment(rechargeDto);
    }

    @Override
    protected <R extends BaseCallBackReq> boolean accountRecharge(R req) {
        // 平台订单号、交易金额、商户订单号
        PayCallBackDTO callBackDto = new PayCallBackDTO();
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
        SevenCallbackReq sevenCallbackReq = new SevenCallbackReq();
        if (req instanceof SevenCallbackReq) {
            sevenCallbackReq = (SevenCallbackReq) req;
        } else {
            log.error("paylog 777支付 回调参数验证 {} checkCallBackParams 入口参数错误", this.getClass().getName());
            return false;
        }
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRecharge> query = new QueryWrapper<>();
        query.lambda().eq(PayRecharge::getOrderNo, req.getMchOrderNo());
        PayRecharge payRecharge = rechargeMapper.selectOne(query);
        if (ObjectUtils.isEmpty(payRecharge)) {
            log.info("777支付参数验证异常,查无此订单,req={}", JSON.toJSONString(req));
            return false;
        }
        PayChannelConfig payChannelConfig = payChannelConfigMapper.selectById(payRecharge.getChannelId());
        if (ObjectUtils.isEmpty(payChannelConfig)) {
            log.info("777支付参数验证异常,商户无,req={}", JSON.toJSONString(req));
            return false;
        }
        // 生成验签
        Map<String, String> metaSignMap = new TreeMap<>();
        metaSignMap.put("code", sevenCallbackReq.getCode() + "");
        metaSignMap.put("message", sevenCallbackReq.getMessage());
        metaSignMap.put("merchantId", sevenCallbackReq.getMerchantId());
        metaSignMap.put("outTradeNo", sevenCallbackReq.getOutTradeNo());
        metaSignMap.put("coin", sevenCallbackReq.getCoin());
        // 商户key
        String signStr = SignMd5Utils.createSign(metaSignMap, payChannelConfig.getSecretKey());
        if (!signStr.equals(sevenCallbackReq.getSign())) {
            log.error("777支付签名不在确=={}", signStr);
            return false;
        }
        if (GlobalConstants.PAY_RECHARGE_STATUS_COMPLETE.equals(payRecharge.getOrderStatus())) {
            log.error("777支付此单号已经处理=={}", sevenCallbackReq.getTransactionNo());
            return false;
        }
        // 验证金额
        BigDecimal payAmt = payRecharge.getRealAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (new BigDecimal(sevenCallbackReq.getCoin()).compareTo(payAmt) != 0) {
            log.error("paylog 777支付 回调参数验证 {} checkCallBackParams 金额不一致 {} {}", this.getClass().getName(), sevenCallbackReq.getCoin(), payAmt);
            return false;
        }
        return true;
    }
}