package com.live.pay.pojo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentVastDto {
    private BigDecimal price;// 价格
    private String orderNo;// 商户自定义订单号
    private String transactionNo;// 第三方订单号
}
