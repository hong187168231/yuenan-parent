package com.indo.pay.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.util.http.HttpClient;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.factory.AbstractOnlinePaymentService;
import com.indo.pay.mapper.PayChannelConfigMapper;
import com.indo.pay.mapper.PayRechargeOrderMapper;
import com.indo.pay.mapper.PayWayConfigMapper;
import com.indo.pay.pojo.dto.PaymentVastDto;
import com.indo.pay.pojo.dto.RechargeRequestDTO;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.entity.PayRechargeOrder;
import com.indo.pay.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.BasePayReq;
import com.indo.pay.pojo.req.HuaRenPayReq;
import com.indo.pay.pojo.resp.DiLeiCallbackReq;
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
import java.util.*;

/**
 * @Description
 * @Author puff
 * HrPay支付service
 **/
@Slf4j
@Service("huaRenOnlinePaymentService")
public class HuarenOnlinePaymentServiceImpl extends AbstractOnlinePaymentService {

    @Resource
    private PayRechargeOrderMapper payRechargeOrderMapper;

    @Resource
    private PaymentService paymentService;
    @Resource
    private PaymentCallBackService paymentCallBackService;

    @Resource
    private PayChannelConfigMapper payChannelConfigMapper;

    @Override
    protected <T> T callPayService(BasePayReq req, Class<T> clazz) {
        // 检验参数
        HuaRenPayReq huaRenPayReq;
        if (req instanceof HuaRenPayReq) {
            huaRenPayReq = (HuaRenPayReq) req;
        } else {
            log.error("paylog HrPay支付 创建订单 {} callPayService 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return null;
        }
        // 生成验签
        Map<String, String> reqMap = new TreeMap<>();
        reqMap.put("version", "1.0");
        reqMap.put("mch_id", "101103004");
        reqMap.put("notify_url", "www.baidu.com");
        reqMap.put("page_url", "www.baidu.com");
        reqMap.put("mch_order_no", IdUtil.simpleUUID());
        reqMap.put("pay_type", "102");
        reqMap.put("trade_amount", "100");
        reqMap.put("order_date", DateUtils.getNewFormatDateString(new Date()));
        reqMap.put("bank_code", "IDPT0001");
        reqMap.put("goods_name", "indotest");
        reqMap.put("payer_phone", "855973559275");
        String sign = SignMd5Utils.createSmallSign(reqMap, "47f57ddf74bb4e00b61b0ef96130fb8b");
        reqMap.put("sign_type", "MD5");
        reqMap.put("sign", sign);
        // http请求
        String httpRet = "";
        Map<String, Object> responseMap = new HashMap<>();
        HuaRenPayResp huaRenPayResp = new HuaRenPayResp();
        try {
            httpRet = HttpClient.formPost(huaRenPayReq.getPayUrl(), reqMap);
            log.info("paylog hr支付 创建订单请求 {} callPayService {}", this.getClass().getName(), reqMap);
        } catch (Exception e) {
            log.error("paylog hr支付 创建订单失败 {} callPayService {}", this.getClass().getName(), reqMap, e);
            huaRenPayResp.setFlag(false);
            huaRenPayResp.setMsg("支付异常 " + httpRet);
            return (T) huaRenPayResp;
        }
        log.info("paylog hr支付 创建订单返回 {} callPayService {}", this.getClass().getName(), responseMap);
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (httpRet.contains("respCode")) {
            String respCode = jsonObject.getString("respCode");
            String tradeMsg = jsonObject.getString("tradeMsg");
            if ("FAIL".equals(respCode)) {
                huaRenPayResp.setFlag(false);
                huaRenPayResp.setMsg(tradeMsg);
                return (T) huaRenPayResp;
            }
        }
        // 请求结果
        huaRenPayResp.setFlag(true);
        huaRenPayResp.setMsg("成功");
        huaRenPayResp.setHtml(jsonObject.getString("payInfo"));
        return (T) huaRenPayResp;
    }


    @Override
    protected <R extends BasePayReq> boolean insertPayment(R req) {
        // 检验参数
        HuaRenPayReq huaRenPayReq;
        if (req instanceof HuaRenPayReq) {
            huaRenPayReq = (HuaRenPayReq) req;
        } else {
            log.error("paylog hr支付 持久化订单 {} insertPayment 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return false;
        }
        // 请求参数
        RechargeRequestDTO payRequestVo = new RechargeRequestDTO();
        payRequestVo.setAmount(huaRenPayReq.getTradeAmount().toString());
        payRequestVo.setOrderNo(req.getMerchantOrderNo());
        payRequestVo.setUserId(req.getUserId());
        return paymentService.insertPayment(payRequestVo);
    }

    @Override
    protected <R extends BaseCallBackReq> boolean accountRecharge(R req) {
        // 平台订单号、交易金额、商户订单号
        PaymentVastDto paymentVastVo = new PaymentVastDto();
        paymentVastVo.setTransactionNo(req.getTransactionNo());
        paymentVastVo.setPrice(new BigDecimal(req.getAmount()));
        paymentVastVo.setOrderNo(req.getMchOrderNo());
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRechargeOrder> query = new QueryWrapper<>();
        query.lambda().eq(PayRechargeOrder::getOrderNo, req.getMchOrderNo());
        PayRechargeOrder rechargeOrder = payRechargeOrderMapper.selectOne(query);
        // 完结订单，更新订单信息
        boolean SuccessFlag = paymentCallBackService.paymentSuccess(paymentVastVo, rechargeOrder);
        return SuccessFlag;
    }

    /**
     * 参数验证
     */
    @Override
    protected <R extends BaseCallBackReq> boolean checkCallBackParams(R req) {
        // 检验参数
        HuaRenCallbackReq huaRenCallbackReq = new HuaRenCallbackReq();
        if (req instanceof HuaRenCallbackReq) {
            huaRenCallbackReq = (HuaRenCallbackReq) req;
        } else {
            log.error("paylog hr支付 回调参数验证 {} checkCallBackParams 入口参数错误", this.getClass().getName());
            return false;
        }
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRechargeOrder> query = new QueryWrapper<>();
        query.lambda().eq(PayRechargeOrder::getOrderNo, "");
        PayRechargeOrder rechargeOrder = payRechargeOrderMapper.selectOne(query);
        if (ObjectUtils.isEmpty(rechargeOrder)) {
            log.info("众宝支付参数验证异常,查无此订单,req={}", JSON.toJSONString(req));
            return false;
        }
        PayChannelConfig payChannelConfig = payChannelConfigMapper.selectById(rechargeOrder.getPayChannelId());
        if (ObjectUtils.isEmpty(payChannelConfig)) {
            log.info("hr支付参数验证异常,商户无,req={}", JSON.toJSONString(req));
            return false;
        }
        // 生成验签
        Map<String, String> metaSignMap = new TreeMap<>();
        metaSignMap.put("tradeResult", huaRenCallbackReq.getStatus());
        metaSignMap.put("mchId", huaRenCallbackReq.getMchId());
        metaSignMap.put("mchOrderNo", huaRenCallbackReq.getMchOrderNo());
        metaSignMap.put("oriAmount", huaRenCallbackReq.getOriAmount());
        metaSignMap.put("amount", huaRenCallbackReq.getAmount());
        metaSignMap.put("orderDate", huaRenCallbackReq.getOrderTime());
        metaSignMap.put("orderNo", huaRenCallbackReq.getTransactionNo());
        if (StringUtils.isNotBlank(huaRenCallbackReq.getMerRetMsg())) {
            metaSignMap.put("merRetMsg", huaRenCallbackReq.getMerRetMsg());
        }

        // 商户key
        String signStr = SignMd5Utils.createSign(metaSignMap, payChannelConfig.getSecretKey());
        if (!signStr.equals(huaRenCallbackReq.getSign())) {
            log.error("hr支付宝签名不在确=={}", signStr);
            return false;
        }
        if (PayConstants.PAY_RECHARGE_STATUS_COMPLETE.equals(rechargeOrder.getOrderStatus())) {
            log.error("hr支付此单号已经处理=={}", huaRenCallbackReq.getTransactionNo());
            return false;
        }
        // 验证金额
        BigDecimal payAmt = rechargeOrder.getRealAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (new BigDecimal(huaRenCallbackReq.getAmount()).compareTo(payAmt) != 0) {
            log.error("paylog hr支付 回调参数验证 {} checkCallBackParams 金额不一致 {} {}", this.getClass().getName(), huaRenCallbackReq.getAmount(), payAmt);
            return false;
        }
        return true;
    }
}