package com.indo.pay.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName
 * @Description zb支付请求
 * @Version 1.0
 **/
@Data
@ApiModel(value = "支付方式请求参数")
public class PayWayReq extends BasePayReq {

    @ApiModelProperty(value = "支付渠道id", required = true)
    private Long payChannelId;


}

