package com.indo.pay.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel(value = "保险箱金额修改参数")
public class SafeboxModifyReq implements Serializable {

    @ApiModelProperty(value = "转入转出金额", required = true)
    private BigDecimal amount;
}
