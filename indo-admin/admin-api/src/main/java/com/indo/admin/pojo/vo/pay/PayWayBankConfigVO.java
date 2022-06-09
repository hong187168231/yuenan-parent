package com.indo.admin.pojo.vo.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通道银行配置
 *
 * @author teman@cg.app
 * @since 1.0.0
 */
@Data
public class PayWayBankConfigVO {

	@ApiModelProperty(value = "主键")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiModelProperty(value = "支付方式id")
	private Long payWayId;

	@ApiModelProperty(value = "支付通道id")
	private Long payChannelId;

	@ApiModelProperty(value = "银行名称")
	private String bankName;

	@ApiModelProperty(value = "银行编码")
	private String bankCode;

	@ApiModelProperty(value = "最小金额")
	private Long minAmount;

	@ApiModelProperty(value = "最大金额")
	private Long maxAmount;

	@ApiModelProperty(value = "当前存款")
	private Long deposit;

	@ApiModelProperty(value = "状态 0关闭  1开启")
	private Integer status;

	@ApiModelProperty(value = "排序")
	private Integer sortBy;

	@ApiModelProperty(value = "创建人")
	private String createUser;

	@ApiModelProperty(value = "更新人")
	private String updateUser;

	@ApiModelProperty(value = "备注")
	private String remark;

}
