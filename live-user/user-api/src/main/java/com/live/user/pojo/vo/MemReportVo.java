package com.live.user.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员包表返回参数
 */
@Data
public class MemReportVo {
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "会员类型 0 普通会员 1 代理会员")
    private Integer IdentityType;

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "等级id")
    private Long levelId;

    @ApiModelProperty(value = "上级代理账号")
    private String superiorAgent;

    @ApiModelProperty(value = "余额")
    private Long balance;

    @ApiModelProperty(value = "盈利")
    private BigDecimal profit;

    @ApiModelProperty(value = "盈利")
    private BigDecimal profitRatio;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty(value = "充值次数")
    private Integer rechargeCount;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "活动礼金")
    private BigDecimal activityAmount;

    @ApiModelProperty(value = "返点金额")
    private BigDecimal rebateAmount;

    @ApiModelProperty(value = "注册时间")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CreateTime;

}
