package com.indo.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "代理统计")
public class SubordinateVo {

    @ApiModelProperty(value = "代理ID")
    private Long agentId;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "团队人数")
    private Integer teamNumber;

    @ApiModelProperty(value = "总存款")
    private BigDecimal totalDeposit;

    @ApiModelProperty(value = "总取款")
    private BigDecimal totalWithdraw;
}
