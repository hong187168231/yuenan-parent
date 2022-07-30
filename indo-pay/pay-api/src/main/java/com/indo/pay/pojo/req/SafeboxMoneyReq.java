package com.indo.pay.pojo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel(value = "用户目前金额")
public class SafeboxMoneyReq {

    @ApiModelProperty(value = "保险箱金额")
    private BigDecimal userSafemoney;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;
}
