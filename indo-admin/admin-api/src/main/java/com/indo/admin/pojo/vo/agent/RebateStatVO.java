package com.indo.admin.pojo.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class RebateStatVO {

    @ApiModelProperty(value = "我的佣金")
    private BigDecimal rebateAmount;

    @ApiModelProperty(value = "团队人数")
    private Integer teamNum;

    @ApiModelProperty(value = "团队充值")
    private BigDecimal teamRecharge;

    @ApiModelProperty(value = "团队投注")
    private BigDecimal teamBet;

    @ApiModelProperty(value = "日新增")
    private Integer dayAddNum;

    @ApiModelProperty(value = "月新增")
    private Integer monthAddNum;



}
