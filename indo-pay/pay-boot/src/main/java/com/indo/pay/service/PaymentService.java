package com.indo.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.indo.pay.pojo.entity.PayRechargeOrder;
import com.indo.pay.common.constant.PayConstants;
import com.indo.pay.factory.OnlinePaymentService;
import com.indo.pay.mapper.PayRechargeOrderMapper;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.RechargeRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author puff
 * @Description: 支付业务类
 * @date 2021/9/5 15:57
 */
@Component
@Slf4j
public class PaymentService {


    @Resource(name = "diLeiOnlinePaymentService")
    private OnlinePaymentService diLeiOnlinePaymentService;

    @Autowired
    private PayRechargeOrderMapper payRechargeOrderMapper;


    public boolean insertPayment(RechargeRequestDTO payRequestVo) {

        Date nowDate = new Date();
        PayRechargeOrder rechargeOrder = new PayRechargeOrder();
        rechargeOrder.setUserId(payRequestVo.getMemId());
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
    private Result<JSONObject> createPaySuccess(Integer code, String url) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("url", url);
        return Result.success(jsonObject);
    }

}
