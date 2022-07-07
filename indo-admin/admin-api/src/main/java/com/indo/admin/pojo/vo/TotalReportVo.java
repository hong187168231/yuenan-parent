package com.indo.admin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalReportVo {
    @ApiModelProperty(value = "新注册人数")
    private Integer memNewlyNum;

    @ApiModelProperty(value = "登录人数")
    private Integer memLoginNum;

    @ApiModelProperty(value = "当日注册并充值")
    private Integer RegisterAndRechargeNum;

    @ApiModelProperty(value = "首充人数")
    private Integer firstRechargeNum;

    @ApiModelProperty(value = "总充值人数")
    private Integer totalFirstRechargeNum;

    @ApiModelProperty(value = "首充金额")
    private BigDecimal firstChargeMoney;

    @ApiModelProperty(value = "充值总金额")
    private BigDecimal totalChargeMoney;

    @ApiModelProperty(value = "取款总金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "投注金额")
    private BigDecimal bettingAmount;

    @ApiModelProperty(value = "投注人数")
    private Integer bettingPeopleNum;

    @ApiModelProperty(value = "返点金额")
    private BigDecimal totalRebateAmount;

    @ApiModelProperty(value = "盈利金额")
    private BigDecimal companyProfit;

    @ApiModelProperty(value = "活动彩金")
    private BigDecimal activityAmount;
}
