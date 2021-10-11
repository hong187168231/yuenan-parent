package com.live.pay.pojo.req;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeReq {

    private BigDecimal amount;

    private Long payWayId;

}
