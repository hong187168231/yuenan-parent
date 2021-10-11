package com.jndo.Pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jndo.Pay.common.constant.PayConstants;
import com.jndo.Pay.factory.AbstractOnlinePaymentService;
import com.jndo.Pay.mapper.PayRechargeOrderMapper;
import com.jndo.Pay.util.SignMd5Utils;
import com.indo.common.utils.DateUtils;
import com.indo.common.web.util.http.HttpClient;
import com.indo.pay.pojo.dto.PaymentVastDto;
import com.indo.pay.pojo.dto.RechargeRequestDTO;
import com.indo.pay.pojo.entity.PayRechargeOrder;
import com.indo.pay.pojo.req.BaseCallBackReq;
import com.indo.pay.pojo.req.BasePayReq;
import com.indo.pay.pojo.req.DiLeiPayReq;
import com.indo.pay.pojo.resp.DiLeiCallbackReq;
import com.indo.pay.pojo.resp.DiLeiPayResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description
 * @Author puff 地雷支付service
 **/
@Slf4j
@Service("diLeiOnlinePaymentService")
public class DiLeiOnlinePaymentServiceImpl extends AbstractOnlinePaymentService {

    @Resource
    private PayRechargeOrderMapper payRechargeOrderMapper;

    @Resource
    private PaymentService paymentService;
    @Resource
    private PaymentCallBackService paymentCallBackService;


    @Override
    protected <T> T callPayService(BasePayReq req, Class<T> clazz) {
        // 检验参数
        DiLeiPayReq diLeiPayReq;
        if (req instanceof DiLeiPayReq) {
            diLeiPayReq = (DiLeiPayReq) req;
        } else {
            log.error("paylog 众宝支付 创建订单 {} callPayService 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return null;
        }
        // 生成验签
        LinkedHashMap<String, String> reqMap = new LinkedHashMap<String, String>();
        reqMap.put("pay_memberid", diLeiPayReq.getMerchantNo());
        reqMap.put("pay_orderid", diLeiPayReq.getOrderNo());
        reqMap.put("pay_amount", diLeiPayReq.getAmount().toString());
        reqMap.put("pay_applydate", DateUtils.getDateString(new Date()));
        reqMap.put("pay_bankcode", "111");
        reqMap.put("pay_notifyurl", diLeiPayReq.getNotifyUrl());
        reqMap.put("pay_callbackurl", diLeiPayReq.getCallBackUrl());
        reqMap.put("pay_attach", "test");
        reqMap.put("pay_productname", "shop");
        String signStr = SignMd5Utils.createSign(reqMap, diLeiPayReq.getSecretKey());
        reqMap.put("pay_md5sign", signStr);
        // http请求
        String httpRet = "";
        Map<String, Object> responseMap = new HashMap<>();
        DiLeiPayResp zbPayResp = new DiLeiPayResp();
        try {
            httpRet = HttpClient.formPost(diLeiPayReq.getShopUrl(), reqMap);
            log.info("paylog ai支付 创建订单请求 {} callPayService {}", this.getClass().getName(), reqMap);
        } catch (Exception e) {
            log.error("paylog ai支付 创建订单失败 {} callPayService {}", this.getClass().getName(), reqMap, e);
            zbPayResp.setFlag(false);
            zbPayResp.setMsg("支付异常 " + httpRet);
            return (T) zbPayResp;
        }
        log.info("paylog 众宝支付 创建订单返回 {} callPayService {}", this.getClass().getName(), responseMap);
        if (httpRet.contains("status") && httpRet.contains("msg")) {
            JSONObject jsonObject = JSONObject.parseObject(httpRet);
            zbPayResp.setFlag(false);
            zbPayResp.setMsg(jsonObject.getString("msg"));
            return (T) zbPayResp;
        }
        // 请求结果
        zbPayResp.setFlag(true);
        zbPayResp.setMsg("成功");
        zbPayResp.setHtml(httpRet);
        return (T) zbPayResp;
    }


    /**
     * 持久化订单
     */
    @Override
    protected boolean insertPayment(BasePayReq req) {
        // 检验参数
        DiLeiPayReq diLeiPayReq;
        if (req instanceof DiLeiPayReq) {
            diLeiPayReq = (DiLeiPayReq) req;
        } else {
            log.error("paylog 众宝支付 持久化订单 {} insertPayment 入口参数错误 {}", this.getClass().getName(), JSON.toJSONString(req));
            return false;
        }
        // 请求参数
        RechargeRequestDTO payRequestVo = new RechargeRequestDTO();
        payRequestVo.setAmount(diLeiPayReq.getAmount().toString());
        payRequestVo.setOrderNo(req.getOrderNo());
        payRequestVo.setMemId(req.getUid());
        return paymentService.insertPayment(payRequestVo);
    }

    /**
     * 客户充值
     */
    @Override
    protected <R extends BaseCallBackReq> boolean accountRecharge(R req) {
        // 平台订单号、交易金额、商户订单号
        PaymentVastDto paymentVastVo = new PaymentVastDto();
        paymentVastVo.setTransactionNo(req.getTransactionNo());
        paymentVastVo.setPrice(req.getAmount());
        paymentVastVo.setOrderNo(req.getOrderNo());
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRechargeOrder> query = new QueryWrapper<>();
        query.lambda().eq(PayRechargeOrder::getOrderNo, req.getOrderNo());
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
        DiLeiCallbackReq diLeiCallbackReq = new DiLeiCallbackReq();
        if (req instanceof DiLeiCallbackReq) {
            diLeiCallbackReq = (DiLeiCallbackReq) req;
        } else {
            log.error("paylog 众宝支付 回调参数验证 {} checkCallBackParams 入口参数错误", this.getClass().getName());
            return false;
        }
        // 根据商户订单号，查询订单信息
        QueryWrapper<PayRechargeOrder> query = new QueryWrapper<>();
        query.lambda().eq(PayRechargeOrder::getOrderNo, req.getOrderNo());
        PayRechargeOrder rechargeOrder = payRechargeOrderMapper.selectOne(query);
        if (ObjectUtils.isEmpty(rechargeOrder)) {
            log.info("众宝支付参数验证异常,查无此订单,req={}", JSON.toJSONString(req));
            return false;
        }
//        PayWayCfg payWayCfg = payWayCfgMapper.selectById(rechargeOrder.getPayWayId());
//        if (ObjectUtils.isEmpty(payWayCfg)) {
//            log.info("众宝支付参数验证异常,商户无,req={}", JSON.toJSONString(req));
//            return false;
//        }
        // 生成验签
        Map<String, String> metaSignMap = new TreeMap<>();
        metaSignMap.put("memberid", diLeiCallbackReq.getMemberid());
        metaSignMap.put("orderid", diLeiCallbackReq.getOrderid());
        metaSignMap.put("amount", diLeiCallbackReq.getAmount().toString());
        metaSignMap.put("transaction_id", diLeiCallbackReq.getTransaction_id());
        metaSignMap.put("datetime", diLeiCallbackReq.getDatetime());
        metaSignMap.put("returncode", diLeiCallbackReq.getReturncode());
        // 商户key
//        String signStr = SignMd5Utils.createSign(metaSignMap, payWayCfg.getSecretKey());
//        if (!signStr.equals(diLeiCallbackReq.getSign())) {
//            log.error("地雷支付宝签名不在确=={}", signStr);
//            return false;
//        }
        if (PayConstants.PAY_RECHARGE_STATUS_COMPLETE.equals(rechargeOrder.getOrderStatus())) {
            log.error("地雷支付此单号已经处理=={}", diLeiCallbackReq.getTransaction_id());
            return false;
        }
        // 验证金额
        BigDecimal payAmt = rechargeOrder.getRealAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        if (diLeiCallbackReq.getAmount().compareTo(payAmt) != 0) {
            log.error("paylog 众宝支付 回调参数验证 {} checkCallBackParams 金额不一致 {} {}", this.getClass().getName(), diLeiCallbackReq.getAmount(), payAmt);
            return false;
        }
        if (!diLeiCallbackReq.getReturncode().equals("00")) {
            log.error("paylog 众宝支付 回调参数验证 {} checkCallBackParams 支付状态为失败 {} {}", this.getClass().getName(), diLeiCallbackReq.getAmount(), payAmt);
            return true;
        }
        return true;
    }
}
