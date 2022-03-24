package com.indo.pay.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 提取方式配置返回
 */
@Data
public class RechargeRecordVO implements Serializable {

    @ApiModelProperty(value = "充值id")
    private Long rechargeId;

    @ApiModelProperty(value = "充值渠道名称")
    private String channelName;

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "充值金额")
    private String realAmount;

    @ApiModelProperty(value = "订单状态：默认1，充值订单状态 1=待确认 2=已确认 3=取消")
    private Integer orderStatus;

    @ApiModelProperty(value = "充值时间")
    private String createTime;

    @ApiModelProperty(value = "充值到账时间")
    private String payTime;

    @ApiModelProperty(value = "订单号")
    private String orderNo;
}
