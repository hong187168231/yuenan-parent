package com.indo.admin.pojo.vo.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息官方地址
 *
 * @author justin
 */
@Data
public class MsgAddressVO {

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "地址")
	private String address;

	@ApiModelProperty(value = "创建时间")
	private String createTime;

	@ApiModelProperty(value = "修改时间")
	private String updateTime;
}
