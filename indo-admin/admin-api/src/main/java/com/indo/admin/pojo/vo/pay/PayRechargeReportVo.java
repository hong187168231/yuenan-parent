package com.indo.admin.pojo.vo.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayRechargeReportVo {
    @ApiModelProperty(value = "日期")
    private String dataTime;

    @ApiModelProperty(value = "首充人数")
    private Integer firstNum;

    @ApiModelProperty(value = "首充金额")
    private Integer firstMoney;

    @ApiModelProperty(value = "二次充值人数")
    private Integer secondNum;

    @ApiModelProperty(value = "二次充值金额")
    private Integer secondMoney;

    @ApiModelProperty(value = " 三次充值人数")
    private Integer thirdNum;

    @ApiModelProperty(value = " 三次充值金额")
    private Integer thirdMoney;
}
