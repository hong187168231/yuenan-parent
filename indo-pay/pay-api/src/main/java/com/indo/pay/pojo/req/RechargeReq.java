package com.indo.pay.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "支付请求参数")
public class RechargeReq {

    @ApiModelProperty(value = "充值金额", required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "支付渠道id", required = true)
    private Long payChannelId;

    @ApiModelProperty(value = "支付方式id", required = true)
    private Long payWayId;


}
