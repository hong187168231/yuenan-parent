package com.tcg.api.core.obj;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <CODE> TCGBaseResponse </CODE>
 * 天成接口基础响应物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class TCGBaseResponse {

	private int status;
	@JsonProperty("error_message")
	private String errorMessage;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return status == 0;
	}

}
