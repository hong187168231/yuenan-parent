package com.tcg.api.wsclient;

import com.tcg.api.core.obj.TCGBaseResponse;
import com.tcg.api.wsclient.exception.ProcessException;
import com.tcg.api.wsclient.exception.RemoteException;
import com.tcg.api.wsclient.exception.TransportException;

/**
 * <CODE> TCGamingAPICommon </CODE>
 * 天成共用接口介面
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public interface TCGamingAPICommon {

	/**
	 * 2.1.	Create / Register Player API
	 * 2.1.	创建/确认玩家接口
	 * @param username 玩家帳號
	 * @param password 玩家密碼
	 * @param currency 玩家幣別
	 * @return
	 * @throws RemoteException 
	 * @throws ProcessException
	 * @throws TransportException
	 */
	public TCGBaseResponse registerMember(String username, String password, String currency) throws RemoteException, ProcessException, TransportException;
	
}
