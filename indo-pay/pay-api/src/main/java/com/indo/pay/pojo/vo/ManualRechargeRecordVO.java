package com.indo.pay.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 银行支付配置返回
 */
@Data
public class ManualRechargeRecordVO {

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "操作金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "操作前金额")
    private Integer beforeAmount;

    @ApiModelProperty(value = "操作后金额")
    private Integer afterAmount;

    @ApiModelProperty(value = "操作类型 1 加款 2 减款")
    private Integer operateType;

    @ApiModelProperty(value = "操作类型 1 加款 2 减款")
    private String createUser;

    @ApiModelProperty(value = "操作类型 1 加款 2 减款")
    private String createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
