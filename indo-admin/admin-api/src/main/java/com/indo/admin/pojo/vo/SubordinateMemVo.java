package com.indo.admin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class SubordinateMemVo implements Serializable {

    @ApiModelProperty(value = "用户Id")
    private Long memId;

    @ApiModelProperty(value = "代理ID")
    private Long agentId;

    @ApiModelProperty(value = "会员等级")
    private String level;

    @ApiModelProperty(value = "账号类型")
    private String accountType;

    @ApiModelProperty(value = "上级代理账号")
    private String superiorAgent;

    @ApiModelProperty(value = "团队数")
    private Integer teamNum;

    @ApiModelProperty(value = "总存款")
    private BigDecimal totalDeposit;

    @ApiModelProperty(value = "总取款")
    private BigDecimal totalEnchashment;

    @ApiModelProperty(value = "总投注")
    private BigDecimal totalBet;


    @ApiModelProperty(value = "注册时间")
    private Date createTime;



    private static final long serialVersionUID = -5649061999296161199L;
}
