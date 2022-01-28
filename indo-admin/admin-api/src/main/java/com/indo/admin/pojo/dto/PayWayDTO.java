package com.indo.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @des:支付方式请求参数
 * @Author: puff
 */
@Data
@ApiModel
public class PayWayDTO {

    @ApiModelProperty(value = "支付通道id")
    private Long payChannelId;

    @ApiModelProperty(value = "支付方式名称")
    private String wayName;

    @ApiModelProperty(value = "最小金额")
    private Integer minAmount;

    @ApiModelProperty(value = "最大金额")
    private Integer maxAmount;

    @ApiModelProperty(value = "最大金额")
    private Integer todayAmount;

    @ApiModelProperty(value = "状态 0关闭  1开启")
    private Integer status;
}
