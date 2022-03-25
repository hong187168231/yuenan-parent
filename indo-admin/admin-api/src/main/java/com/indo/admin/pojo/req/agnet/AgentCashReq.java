package com.indo.admin.pojo.req.agnet;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class AgentCashReq extends BaseDTO {

    @ApiModelProperty("会员ID")
    private Long memId;

    @ApiModelProperty("用固话等级")
    private Integer memLevel;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("订单状态 0-处理中 1-提现成功 2 提现失败")
    private Integer orderStatus;

    @ApiModelProperty("开始金额")
    private BigDecimal beginAmount;

    @ApiModelProperty("结束金额")
    private BigDecimal endAmount;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("现金状态")
    private Integer cashStatus;
}
