package com.indo.pay.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author justin
 * @since 1.0.0
 */
@Data
public class PayWayBankVO {

	@ApiModelProperty(value = "银行编码")
	private String bankCode;

	@ApiModelProperty(value = "最小金额")
	private Long minAmount;

	@ApiModelProperty(value = "最大金额")
	private Long maxAmount;
}
