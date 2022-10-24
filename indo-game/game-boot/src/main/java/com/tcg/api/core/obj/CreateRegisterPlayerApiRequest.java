package com.tcg.api.core.obj;

/**
 * <CODE> CreateRegisterPlayerApiRequest </CODE>
 * 创建/确认玩家接口请求物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class CreateRegisterPlayerApiRequest extends TCGBaseRequest {

	/**
	 * 玩家帐号
	 */
	private String username;
	
	/**
	 * 玩家密码
	 */
	private String password;
	
	/**
	 * 使用币别
	 */
	private String currency;

	/**
	 * 创见或确认玩家接口所需要的一个常数 cm 
	 */
	public CreateRegisterPlayerApiRequest() {
		setMethod("cm");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
