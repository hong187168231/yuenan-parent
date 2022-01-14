package com.indo.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.enums.ThirdPayChannelEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.SnowflakeIdWorker;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayRechargeOrder;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.pay.mapper.PayChannelConfigMapper;
import com.indo.pay.mapper.PayWayConfigMapper;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.factory.OnlinePaymentService;
import com.indo.pay.mapper.PayRechargeOrderMapper;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.RechargeDto;
import com.indo.pay.pojo.req.HuaRenPayReq;
import com.indo.pay.pojo.req.RechargeReq;
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

    @Autowired
    private PayRechargeOrderMapper payRechargeOrderMapper;

    @Autowired
    private PayWayConfigMapper payWayConfigMapper;

    @Autowired
    private PayChannelConfigMapper payChannelConfigMapper;


    public Result paymentRequestByUser(LoginInfo loginInfo, RechargeReq rechargeReq) {
        Result result = Result.failed();
        PayChannelConfig payChannelConfig = payChannelConfigMapper.selectById(rechargeReq.getPayChannelId());
        PayWayConfig payWayCfg = payWayConfigMapper.selectById(rechargeReq.getPayWayId());
        ThirdPayChannelEnum payChannel = ThirdPayChannelEnum.valueOf(payChannelConfig.getChannelCode());
        switch (payChannel) {
            case HUAREN:
                result = huarenPay(loginInfo, rechargeReq.getAmount(), payChannelConfig, payWayCfg);
                break;
            case DILEI:
                break;
            default:
                throw new BizException("请选择正确的支付方式");
        }
        return result;
    }


    public boolean insertPayment(RechargeDto rechargeDTO) {

        PayRechargeOrder rechargeOrder = new PayRechargeOrder();
        rechargeOrder.setMemId(rechargeDTO.getMemId());
        rechargeOrder.setOrderNo(rechargeDTO.getOrderNo());
//        rechargeOrder.setPayWayId(payRequestVo.getChannelWay());
        //实际金额
        BigDecimal amount = new BigDecimal(rechargeDTO.getAmount());
        rechargeOrder.setOldAmount(amount);
        rechargeOrder.setTotalAmount(amount);
        rechargeOrder.setRealAmount(amount);
        rechargeOrder.setOrderStatus(PayConstants.PAY_RECHARGE_STATUS_PROCESS);
        return payRechargeOrderMapper.insert(rechargeOrder) > 0;

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
     * 众宝支付
     */
    private Result huarenPay(LoginInfo loginInfo, BigDecimal amount, PayChannelConfig payChannelConfig, PayWayConfig payWayCfg) {
        HuaRenPayReq req = new HuaRenPayReq();
        try {
            log.info("paylog huaren支付 创建订单");
            req.setMemId(loginInfo.getId());
            req.setMerchantNo(payChannelConfig.getMerchantNo());
            req.setNotifyUrl(payChannelConfig.getNotifyUrl());
            req.setPageUrl(payChannelConfig.getPageUrl());
            req.setPayType(payChannelConfig.getChannelType() + "");
            req.setPayUrl(payChannelConfig.getPayUrl());
            req.setSecretKey(payChannelConfig.getSecretKey());
            req.setMerchantOrderNo("RC" + SnowflakeIdWorker.createOrderSn());
            req.setTradeAmount(amount);
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

}
