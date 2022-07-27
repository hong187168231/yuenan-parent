package com.indo.admin.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author justin
 * @description 账号枚举
 */
@Getter
public enum AccountTypeEnum {

	/**
	 * 玩家
	 */
	PLAYER(1, "玩家"),

	/**
	 * 代理
	 */
	AGENT(2, "代理")
	;

	private Integer value;
	private String desc;

	AccountTypeEnum(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
}
