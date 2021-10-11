package com.live.pay.pojo.dto;

import lombok.Data;

@Data
public class RechargeRequestDTO {
	/**
	 * 用户ID
	 */
	private Long memId;
	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 充值渠道编码
	 */
	private String channelCode;

	/**
	 * 充值渠道支付方式
	 */
	private String channelWay;

	/**
	 * 支付类型
	 */
	private String typeCode;

	/**
	 * 金额
	 */
	private String amount;

	/**
	 * 支付设置ID
	 */
	private Long  payWayId;


	private String source;


}
