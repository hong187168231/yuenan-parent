package com.indo.pay.pojo.req;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 支付回调参数基类
 **/
@Data
public class BaseCallBackReq implements Serializable {

    private String notifyIp; // 回调ip
    private String mchId; //商户号
    private String mchOrderNo; //商户订单号(商户生成)
    private String oriAmount; //原始金额
    private String amount; //实付金额
    private String transactionNo; //支付订单号(第三方支付生成)
    private String status; //订单状态

}
