package com.indo.admin.pojo.req;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PayRechargeReq extends BaseDTO {

    @ApiModelProperty(value = "充值渠道id")
    private Long payChannelId;

    @ApiModelProperty(value = "充值方式id")
    private Long payWayId;

    @ApiModelProperty(value = "起始金额")
    private Integer beginAmount;

    @ApiModelProperty(value = "结束金额")
    private Integer endAmount;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "用户id")
    private Long memId;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
