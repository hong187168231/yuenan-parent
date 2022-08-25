package com.indo.admin.pojo.vo.mem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemBetInfoVo {
    @ApiModelProperty(value = "会员ID")
    private Long memId;

    @ApiModelProperty(value = "会员账号")
    private String memAccount;

    @ApiModelProperty(value = "总充值金额")
    private BigDecimal totalRechargeAmount;

    @ApiModelProperty(value = "总打码金额")
    private BigDecimal totalBetAmount;

    @ApiModelProperty(value = "总礼金金额")
    private BigDecimal totalGiftAmount;

    @ApiModelProperty(value = "提现所需打码量")
    private BigDecimal withdrawalBetAmount;

    @ApiModelProperty(value = "差额")
    private BigDecimal lack;
}
