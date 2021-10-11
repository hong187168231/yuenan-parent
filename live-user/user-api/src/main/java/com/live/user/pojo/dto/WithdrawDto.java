package com.live.user.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现对象 recharge
 */
@Data
@ApiModel(value = "提现请求参数类")
public class WithdrawDto {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "状态：0 提现中 1 提现拒绝 2提现通过")
    private Integer status;

    @ApiModelProperty(value = "账户")
    private String bankCard;

    @ApiModelProperty(value = "用户名")
    private String account;

    @ApiModelProperty(value = "用户等级")
    private Long levelId;

    @ApiModelProperty(value = "最高金额")
    private BigDecimal highAmount;

    @ApiModelProperty(value = "最低金额")
    private BigDecimal lowAmount;

    @ApiModelProperty(value = "出款开始时间")
    private String beginTime;

    @ApiModelProperty(value = "出款结束时间")
    private String endTime;

    @ApiModelProperty(value = "申请开始时间")
    private String applyBeginTime;

    @ApiModelProperty(value = "申请结束时间")
    private String applyEndTime;

    @ApiModelProperty(value = "页数" , required = true)
    private Integer page;

    @ApiModelProperty(value = "条数" , required = true)
    private Integer limit;

    @ApiModelProperty(value = "ids")
    private List<Long> ids;
}
