package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @des:支付方式查询参数
 * @Author: puff
 */
@Data
@ApiModel
public class PayWayQueryDTO extends BaseDTO {

    @ApiModelProperty(value = "支付方式名称")
    private String wayName;

    @ApiModelProperty(value = "支付渠道id")
    private Long payChannelId;
}
