package com.tcg.api.core.obj;

/**
 * <CODE> TCGamingConfigObj </CODE>
 * 天成接口基础连线设定物件
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class TCGamingConfigObj {

	/**
	 * 商戶代碼
	 */
	private String merchantCode;
	
	/**
	 * Des 金鑰
	 */
	private String desKey;
	
	/**
	 * SHA256 金鑰
	 */
	private String sha256Key;
	
	/**
	 * 接口連接
	 */
	private String apiUrl;

	/**
	 * 取得 商戶代碼
	 * @return
	 */
	public String getMerchantCode() {
		return merchantCode;
	}

	/**
	 * 設置 商戶代碼
	 * @param merchantCode
	 */
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * 取得 Des 金鑰
	 * @return
	 */
	public String getDesKey() {
		return desKey;
	}

	/**
	 * 設置 Des 金鑰
	 * @param desKey
	 */
	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}

	/**
	 * 取得 接口連接
	 * @return
	 */
	public String getApiUrl() {
		return apiUrl;
	}

	/**
	 * 設置 接口連接
	 * @param apiUrl
	 */
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	/**
	 * 取得 SHA256 金鑰
	 * @return
	 */
	public String getSha256Key() {
		return sha256Key;
	}

	/**
	 * 設置 SHA256 金鑰
	 * @param sha256Key
	 */
	public void setSha256Key(String sha256Key) {
		this.sha256Key = sha256Key;
	}

}
