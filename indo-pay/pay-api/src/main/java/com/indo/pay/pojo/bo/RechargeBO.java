package com.indo.pay.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "支付请求参数")
public class RechargeBO {


    @ApiModelProperty(value = "支付方式id", required = true)
    private PayChannel payChannel;

    @ApiModelProperty(value = "支付渠道id", required = true)
    private PayWay payWay;

}
