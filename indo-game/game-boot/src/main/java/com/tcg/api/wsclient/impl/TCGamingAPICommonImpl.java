package com.tcg.api.wsclient.impl;

import java.net.URLEncoder;

import com.tcg.api.core.common.DESEncrypt;
import com.tcg.api.core.common.HashUtil;
import com.tcg.api.core.common.HttpUtils;
import com.tcg.api.core.common.JsonUtil;
import com.tcg.api.core.obj.CreateRegisterPlayerApiRequest;
import com.tcg.api.core.obj.TCGBaseResponse;
import com.tcg.api.core.obj.TCGamingConfigObj;
import com.tcg.api.wsclient.TCGamingAPICommon;
import com.tcg.api.wsclient.exception.ProcessException;
import com.tcg.api.wsclient.exception.RemoteException;
import com.tcg.api.wsclient.exception.TransportException;


/**
 * 天成 COMMON API 共用接口 实现类别
 * @author PHOENIX WU
 */

/**
 * <CODE> TCGamingAPICommonImpl </CODE>
 * 天成共用接口实作类别
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class TCGamingAPICommonImpl implements TCGamingAPICommon{

	// 用以判断错误
	private static final String UNKNOWN_TRANS_STATUS = "UNKNOWN";
	
	// 连接接口的设定物件
	private TCGamingConfigObj configObj;
	
	/**
	 * 在需告类别时，需要配置此物件
	 * @param configObj 接口连接配置物件
	 */
	public TCGamingAPICommonImpl(TCGamingConfigObj configObj) {
		this.configObj = configObj;
	}
	
	/**
	 * 
	 */
	@Override
	public TCGBaseResponse registerMember(String username,String password,String currency) throws RemoteException, ProcessException, TransportException {
		
		CreateRegisterPlayerApiRequest request = new CreateRegisterPlayerApiRequest();
		request.setUsername(username);
		request.setPassword(password);
		request.setCurrency(currency);
		
		String result = doRequest(configObj, JsonUtil.toJson(request));
		System.out.println("Register member result: "+result);
		
		TCGBaseResponse response = JsonUtil.fromJson(result, TCGBaseResponse.class);		
		
		if(!response.isSuccess()){
			throw new ProcessException("Failed on TCG , "+ response.getErrorMessage(),null);
		}
		
		return response;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 呼叫天成接口
	 * @param json 参数
	 * @return 结果字串
	 */
	protected String doRequest(TCGamingConfigObj configObj, String json){

		System.out.println("json :\n " + json);

		try {  
			// 参数加密
			DESEncrypt des = new DESEncrypt(configObj.getDesKey());
			String encryptedParams = des.encrypt(json);
			
			//签名档加密
			String sign = HashUtil.sha256(encryptedParams+configObj.getSha256Key());

			//组连接字串
			String data = "merchant_code="+URLEncoder.encode(configObj.getMerchantCode(),"UTF-8")
						  + "&params="+URLEncoder.encode(encryptedParams,"UTF-8")
						  +"&sign="+URLEncoder.encode(sign,"UTF-8");

	
			System.out.println("data :\n " + data);
			
			//传送
			return HttpUtils.newPost(configObj.getApiUrl(), data).execute();
		} catch (Exception e) { 
			throw new RuntimeException(e);
		}
	}
}
