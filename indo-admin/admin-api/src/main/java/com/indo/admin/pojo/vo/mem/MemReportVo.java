package com.indo.admin.pojo.vo.mem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemReportVo {
    @ApiModelProperty(value = "日期")
    private String dataTime;

    @ApiModelProperty(value = "会员总人数")
    private Integer memTotalNum;

    @ApiModelProperty(value = "会员新增人数")
    private Integer memNewlyNum;

    @ApiModelProperty(value = "会员首充人数")
    private Integer memRechargeNum;

    @ApiModelProperty(value = "存款人数")
    private Integer depositPersons;

    @ApiModelProperty(value = "存款金额")
    private BigDecimal depositAmount;

    @ApiModelProperty(value = "取款金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "投注金额")
    private BigDecimal bettingAmount;

    @ApiModelProperty(value = "中奖金额")
    private BigDecimal prizeAmount;

    @ApiModelProperty(value = "活动礼金")
    private BigDecimal activityAmount;

}
