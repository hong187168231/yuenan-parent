package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @des:支付渠道查询参数
 * @Author: puff
 */
@Data
@ApiModel
public class PayChannelQueryDTO extends BaseDTO {

    @ApiModelProperty(value = "支付渠道名称")
    private String channelName;
}
