package com.indo.admin.pojo.vo.agent;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class AgentSubVO {

    @ApiModelProperty(value = "代理编号")
    private Long agentId;

    @ApiModelProperty(value = "用户ID")
    private Long memId;

    @ApiModelProperty(value = "会员账号")
    private String account;

    @ApiModelProperty(value = "会员等级")
    private Integer memLevel;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    @ApiModelProperty(value = "上级代理")
    private String superior;

    @ApiModelProperty(value = "账号类型")
    private Integer accType;

    @ApiModelProperty(value = "总存款")
    private BigDecimal totalDeposit;

    @ApiModelProperty(value = "总取款")
    private BigDecimal totalWithdraw;

    @ApiModelProperty(value = "总投注")
    private BigDecimal totalBet;

    @ApiModelProperty(value = "注册时间")
    private String registerTime;

    @ApiModelProperty(value = "下级ID列表,用逗号分开")
    private String subUserIds;

}
