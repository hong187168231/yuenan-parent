package com.indo.admin.pojo.dto;

import com.indo.common.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PayWayBankQueryDTO extends BaseDTO {

	@ApiModelProperty(value = "支付通道id")
	private Long payWayId;

	@ApiModelProperty(value = "支付渠道id")
	private Long payChannelId;
}
