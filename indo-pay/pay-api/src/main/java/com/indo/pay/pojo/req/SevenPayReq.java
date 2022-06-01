package com.indo.pay.pojo.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName
 * @Description 777pay支付请求
 * @Version 1.0
 **/
@Data
@ApiModel(value = "777pay支付请求参数")
public class SevenPayReq extends BasePayReq {

	/**
	 * 类型：
	 * zalo
	 * momo
	 */
	private String type;

	/**
	 * 平台通知或商户查询时原文返回
	 */
	private String note;

	/**
	 * 订单其他数据 json格式字符串
	 */
	private String payload;
}
