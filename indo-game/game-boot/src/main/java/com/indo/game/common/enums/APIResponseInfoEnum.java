package com.indo.game.common.enums;

public enum APIResponseInfoEnum {
	/**
	 * AG
	 */
	SUCCESS("0", "SUCCESS"),
	INVALID_BALANCE("2", "INVALID_BALANCE"),// 无效转账金额
	ORDER_NOT_PROCESSED("1", "ORDER_NOT_PROCESSED"),// 订单未处理

	KEY_ERROR("1001", "KEY_ERROR"),
	NETWORK_ERROR("1002", "NETWORK_ERROR"),
	DUPLICATE_TRANSFER("1003", "DUPLICATE_TRANSFER"),// 重复转账
	
	
	ERROR("3", "ERROR");
	
	private String keyName;
	private String keyValue;
	
	private APIResponseInfoEnum(String keyName, String keyValue) {
		this.keyName = keyName;
		this.keyValue = keyValue;
	}
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
}
