package com.live.pay.pojo.req;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 支付回调参数基类
 **/
@Data
public class BaseCallBackReq implements Serializable {

    private String notifyIp; // 回调ip
    private String orderNo; //商户订单号(商户生成)
    private String channelType; //支付通道
    private BigDecimal amount; //实付金额
    private String currency; //货币类型：CNY
    private String transactionNo; //支付订单号(第三方支付生成)
    private String status; //支付状态

}
