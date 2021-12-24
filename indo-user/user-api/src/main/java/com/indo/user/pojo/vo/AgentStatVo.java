package com.indo.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "代理统计")
public class AgentStatVo {

    @ApiModelProperty(value = "代理ID")
    private Long id;

    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;

    @ApiModelProperty(value = "团队人数")
    private Integer teamNumber;

    @ApiModelProperty(value = "团队充值")
    private BigDecimal teamRcg;

    @ApiModelProperty(value = "日新增")
    private Integer dayGrowth;

    @ApiModelProperty(value = "月新增")
    private Integer monthGrowth;

    @ApiModelProperty(value = "团队投注")
    private BigDecimal teamBet;
}
