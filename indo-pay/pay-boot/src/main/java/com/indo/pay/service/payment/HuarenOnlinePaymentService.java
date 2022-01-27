package com.indo.pay.service.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.util.http.HttpClient;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.factory.AbstractOnlinePaymentService;
import com.indo.pay.mapper.PayChannelMapper;
import com.indo.pay.mapper.RechargeMapper;
import com.indo.pay.pojo.dto.PayCallBackDTO;
import com.indo.pay.pojo.dto.RechargeDTO;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.BasePayReq;
import com.indo.pay.pojo.req.HuaRenPayReq;
import com.indo.pay.pojo.resp.HuaRenCallbackReq;
import com.indo.pay.pojo.resp.HuaRenPayResp;
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
public class HuarenOnlinePaymentService extends AbstractOnlinePaymentService {

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
        reqMap.put("mch_id", huaRenPayReq.getMerchantNo());
        reqMap.put("notify_url", huaRenPayReq.getNotifyUrl());
        reqMap.put("page_url", huaRenPayReq.getPageUrl());
        reqMap.put("mch_order_no", req.getMerchantOrderNo());
        reqMap.put("pay_type", huaRenPayReq.getPayType());
        reqMap.put("trade_amount", huaRenPayReq.getTradeAmount().toString());
        reqMap.put("order_date", DateUtils.getNewFormatDateString(new Date()));
        reqMap.put("bank_code", "IDPT0001");
        reqMap.put("goods_name", "indo_pay");
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
        RechargeDTO rechargeDto = new RechargeDTO();
        rechargeDto.setAmount(huaRenPayReq.getTradeAmount().toString());
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
        HuaRenCallbackReq huaRenCallbackReq = new HuaRenCallbackReq();
        if (req instanceof HuaRenCallbackReq) {
            huaRenCallbackReq = (HuaRenCallbackReq) req;
        } else {
            log.error("paylog hr支付 回调参数验证 {} checkCallBackParams 入口参数错误", this.getClass().getName());
            return false;
        }
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRecharge> query = new QueryWrapper<>();
        query.lambda().eq(PayRecharge::getOrderNo, req.getMchOrderNo());
        PayRecharge payRecharge = rechargeMapper.selectOne(query);
        if (ObjectUtils.isEmpty(payRecharge)) {
            log.info("众宝支付参数验证异常,查无此订单,req={}", JSON.toJSONString(req));
            return false;
        }
        PayChannelConfig payChannelConfig = payChannelConfigMapper.selectById(payRecharge.getChannelId());
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
        String signStr = SignMd5Utils.createSmallSign(metaSignMap, payChannelConfig.getSecretKey());
        if (!signStr.equals(huaRenCallbackReq.getSign())) {
            log.error("hr支付宝签名不在确=={}", signStr);
            return false;
        }
        if (GlobalConstants.PAY_RECHARGE_STATUS_COMPLETE.equals(payRecharge.getOrderStatus())) {
            log.error("hr支付此单号已经处理=={}", huaRenCallbackReq.getTransactionNo());
            return false;
        }
        // 验证金额
        BigDecimal payAmt = payRecharge.getRealAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (new BigDecimal(huaRenCallbackReq.getAmount()).compareTo(payAmt) != 0) {
            log.error("paylog hr支付 回调参数验证 {} checkCallBackParams 金额不一致 {} {}", this.getClass().getName(), huaRenCallbackReq.getAmount(), payAmt);
            return false;
        }
        return true;
    }
}