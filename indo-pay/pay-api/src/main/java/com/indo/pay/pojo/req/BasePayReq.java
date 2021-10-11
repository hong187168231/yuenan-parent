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
    private String orderNo; //商户订单号
    private Long uid;//支付方式id
    private Long payWayId;//支付方式id
    private String payWayCode; //支付通道
    private BigDecimal amount; //支付金额
    private String merchantNo; //商户号
    private String shopUrl;//标识
    private String notifyUrl; //异步回调地址
    private String callBackUrl; //支付结束跳转地址
    private String acclogin; //用户标识
    private String typeCode;//方式类型 NETBANK 网银转账  WECHAT 微信收款  ALIPAY 支付宝支付
    private String secretKey;

}
