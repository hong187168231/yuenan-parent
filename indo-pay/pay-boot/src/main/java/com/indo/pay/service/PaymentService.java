package com.indo.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.enums.ThirdPayChannelEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.GeneratorIdUtil;
import com.indo.common.web.exception.BizException;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.factory.OnlinePaymentService;
import com.indo.common.result.Result;
import com.indo.pay.pojo.bo.PayChannel;
import com.indo.pay.pojo.bo.RechargeBO;
import com.indo.pay.pojo.bo.PayWay;
import com.indo.pay.pojo.dto.RechargeDTO;
import com.indo.pay.pojo.req.EasyPayReq;
import com.indo.pay.pojo.req.HuaRenPayReq;
import com.indo.pay.pojo.req.RechargeReq;
import com.indo.pay.pojo.resp.EasyPayResp;
import com.indo.pay.pojo.resp.HuaRenPayResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author puff
 * @Description: 支付业务类
 * @date 2021/9/5 15:57
 */
@Component
@Slf4j
public class PaymentService {

    @Resource(name = "huaRenOnlinePaymentService")
    private OnlinePaymentService huaRenOnlinePaymentService;
    @Resource(name = "easyOnlinePaymentService")
    private OnlinePaymentService easyOnlinePaymentService;


    @Autowired
    private rechargeService rechargeService;


    public Result paymentRequestByUser(LoginInfo loginInfo, RechargeReq rechargeReq) {
        Result result = Result.failed();
        // 业务逻辑校验
        RechargeBO rechargeBO = rechargeService.logicConditionCheck(rechargeReq, loginInfo);
        ThirdPayChannelEnum payChannel = ThirdPayChannelEnum.valueOf(rechargeBO.getPayChannel().getChannelCode());
        switch (payChannel) {
            case HUAREN:
                result = huarenPay(loginInfo, rechargeReq.getAmount(), rechargeBO.getPayChannel(), rechargeBO.getPayWay());
                break;
            case EASY:
                result = easyPay(loginInfo, rechargeReq.getAmount(), rechargeBO.getPayChannel(), rechargeBO.getPayWay());
                break;
            default:
                throw new BizException("请选择正确的支付方式");
        }
        return result;
    }


    public boolean insertPayment(RechargeDTO rechargeDTO) {
        return rechargeService.saveRechargeRecord(rechargeDTO);
    }


    /**
     * 创建支付成功返回url
     */
    private Result createPaySuccess(Integer code, String url) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("url", url);
        return Result.success(jsonObject);
    }


    /**
     * huaren支付
     */
    private Result huarenPay(LoginInfo loginInfo, BigDecimal amount, PayChannel payChannel, PayWay payWay) {
        HuaRenPayReq req = new HuaRenPayReq();
        try {
            log.info("paylog huaren支付 创建订单");
            req.setMemId(loginInfo.getId());
            req.setMerchantNo(payChannel.getMerchantNo());
            req.setNotifyUrl(payChannel.getNotifyUrl());
            req.setPageUrl(payChannel.getPageUrl());
            req.setPayType(payChannel.getChannelType() + "");
            req.setPayUrl(payChannel.getPayUrl());
            req.setSecretKey(payChannel.getSecretKey());
            req.setMerchantOrderNo(GeneratorIdUtil.generateId());
            req.setTradeAmount(amount);
            req.setPayChannelId(payChannel.getPayChannelId());
            req.setPayWayId(payWay.getPayWayId());
            // 支付请求
            HuaRenPayResp huarenPayResp = huaRenOnlinePaymentService.onlinePayment(req, HuaRenPayResp.class);
            // 请求结果 flag为false表示异常
            if (!huarenPayResp.getFlag()) {
                return Result.failed("huaren支付失败：" + huarenPayResp.getMsg());
            }
            return createPaySuccess(PayConstants.PAY_RETURN_CODE_ZERO, huarenPayResp.getHtml());
        } catch (Exception e) {
            log.error("paylog huaren支付 创建订单失败 {} huarenPay {}", this.getClass().getName(), JSON.toJSONString(req), e);
        }
        return Result.failed("huaren支付失败");
    }


    /**
     * easy支付
     */
    private Result easyPay(LoginInfo loginInfo, BigDecimal amount, PayChannel payChannel, PayWay payWay) {
        EasyPayReq req = new EasyPayReq();
        try {
            log.info("paylog easyPay支付 创建订单");
            req.setMemId(loginInfo.getId());
            req.setMerchantNo(payChannel.getMerchantNo());
            req.setNotifyUrl(payChannel.getNotifyUrl());
            req.setPageUrl(payChannel.getPageUrl());
            req.setPayUrl(payChannel.getPayUrl());
            req.setSecretKey(payChannel.getSecretKey());
            req.setMerchantOrderNo(GeneratorIdUtil.generateId());
            req.setTradeAmount(amount);
            req.setPayChannelId(payChannel.getPayChannelId());
            req.setPayWayId(payWay.getPayWayId());
            // 支付请求
            EasyPayResp easyPayResp = easyOnlinePaymentService.onlinePayment(req, EasyPayResp.class);
            // 请求结果 flag为false表示异常
            if (!easyPayResp.getFlag()) {
                return Result.failed("easy支付失败：" + easyPayResp.getMsg());
            }
            return createPaySuccess(PayConstants.PAY_RETURN_CODE_ZERO, easyPayResp.getHtml());
        } catch (Exception e) {
            log.error("paylog easy支付 创建订单失败 {} easyPay {}", this.getClass().getName(), JSON.toJSONString(req), e);
        }
        return Result.failed("easy支付失败");
    }

}
