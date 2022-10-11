package com.tcg.api.core.obj;

/**
 * <CODE> TCGBaseRequest </CODE>
 * 天成接口基础请求物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class TCGBaseRequest {

	/**
	 * 接口请求方法名称
	 */
	private String method;

	/**
	 * 取得 接口请求方法名称 
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置 接口请求方法名称
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
}
