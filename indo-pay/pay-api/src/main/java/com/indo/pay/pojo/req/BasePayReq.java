package com.indo.pay.pojo.req;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName BaseReq
 * @Description 支付参数基类
 * @Author puff
 * @Version 1.0
 **/

@Data
public class BasePayReq implements Serializable {

    private Long memId;//会员id
    private Long payWayId;//支付方式id
    private String payType; //支付通道编码
    private BigDecimal tradeAmount; //支付金额
    private String merchantNo; //商户号
    private String merchantOrderNo; //商户订单号
    private String payUrl;//支付地址
    private String notifyUrl; //异步通知地址
    private String pageUrl; //支付结束跳转地址
    private String secretKey;
}
