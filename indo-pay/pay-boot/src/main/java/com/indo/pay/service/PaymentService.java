package com.indo.pay.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.enums.ThirdPayChannelEnum;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.DateUtils;
import com.indo.common.web.exception.BizException;
import com.indo.pay.mapper.PayChannelConfigMapper;
import com.indo.pay.mapper.PayWayConfigMapper;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.entity.PayRechargeOrder;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.factory.OnlinePaymentService;
import com.indo.pay.mapper.PayRechargeOrderMapper;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.RechargeRequestDTO;
import com.indo.pay.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.req.HuaRenPayReq;
import com.indo.pay.pojo.req.RechargeReq;
import com.indo.pay.pojo.resp.BasePayResp;
import com.indo.pay.util.SignMd5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

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


    public void paymentRequestByUser(LoginInfo loginInfo, RechargeReq rechargeReq) {
        PayChannelConfig payChannelConfig = payChannelConfigMapper.selectById(rechargeReq.getPayChannelId());
        PayWayConfig payWayCfg = payWayConfigMapper.selectById(rechargeReq.getPayWayId());
        ThirdPayChannelEnum payChannel = ThirdPayChannelEnum.valueOf(payChannelConfig.getChannelCode());
        switch (payChannel) {
            case HUAREN:
                 huarenPay(loginInfo, rechargeReq.getAmount(), payChannelConfig, payWayCfg);
            break;
            case DILEI:
                break;
            default:
                throw new BizException("请选择正确的支付方式");
        }
    }


    public boolean insertPayment(RechargeRequestDTO payRequestVo) {

        Date nowDate = new Date();
        PayRechargeOrder rechargeOrder = new PayRechargeOrder();
        rechargeOrder.setUserId(payRequestVo.getUserId());
        rechargeOrder.setOrderNo(payRequestVo.getOrderNo());
        rechargeOrder.setCreateTime(nowDate);
//        rechargeOrder.setPayWayId(payRequestVo.getChannelWay());
        //实际金额
        BigDecimal amount = new BigDecimal(payRequestVo.getAmount());
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
    private Result huarenPay(LoginInfo loginInfo, BigDecimal price, PayChannelConfig payChannelConfig, PayWayConfig payWayCfg) {
        HuaRenPayReq req = new HuaRenPayReq();
        try {
            log.info("paylog 地雷支付 创建订单");

            Map<String, String> metaSignMap = new TreeMap<>();
            metaSignMap.put("version", "1.0");
            metaSignMap.put("mch_id", payChannelConfig.getMerchantNo());
            metaSignMap.put("notify_url", payChannelConfig.getNotifyUrl());
            metaSignMap.put("page_url", payChannelConfig.getPageUrl());
            metaSignMap.put("mch_order_no", IdUtil.simpleUUID());
            metaSignMap.put("pay_type", payChannelConfig.getChannelType() + "");
            metaSignMap.put("trade_amount", price.toString());
            metaSignMap.put("order_date", DateUtils.getNewFormatDateString(new Date()));
            metaSignMap.put("bank_code", "IDPT0001");
            metaSignMap.put("goods_name", "indo_pay");
            metaSignMap.put("payer_phone", "855973559275");
            String sign = SignMd5Utils.createSmallSign(metaSignMap, payChannelConfig.getSecretKey());
            metaSignMap.put("sign_type", "MD5");
            metaSignMap.put("sign", sign);


            // 支付请求
            BasePayResp huarenPayResp = huaRenOnlinePaymentService.onlinePayment(req);
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
