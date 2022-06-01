package com.indo.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 国家枚举
 *
 * @since 1.0.0
 */
public enum CountryEnum {
	IN("IN", "印度", "INR", "en-IN"),
	VN("VN", "越南", "VND", "vi-VN");

	private final String code;
	private final String name;
	private final String language;
	private final String currency;

	CountryEnum(String code, String name, String currency, String language) {
		this.code = code;
		this.name = name;
		this.currency = currency;
		this.language = language;
	}


	public static CountryEnum findByCode(String code) {
		if (StringUtils.isBlank(code)) {
			return null;
		}
		for (CountryEnum e : CountryEnum.values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public String getCurrency() {
		return currency;
	}

}