package com.live.user.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理报表返回参数
 */
@Data
public class AgentReportVo {
    @ApiModelProperty(value = "代理ID")
    private Long id;

    @ApiModelProperty(value = "会员类型 0 普通会员 1 代理会员")
    private Integer IdentityType;

    @ApiModelProperty(value = "代理账号")
    private String account;

    @ApiModelProperty(value = "注册人数")
    private Integer register;

    @ApiModelProperty(value = "首充人数")
    private Integer levelId;

    @ApiModelProperty(value = "代理返点")
    private Integer superiorAgent;

    @ApiModelProperty(value = "团队盈利")
    private BigDecimal profit;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "注册时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CreateTime;

}
