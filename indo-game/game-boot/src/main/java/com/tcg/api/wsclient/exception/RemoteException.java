package com.tcg.api.wsclient.exception;

import java.util.Date;

/**
 * <CODE> RemoteException </CODE>
 * 端口异常处理
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class RemoteException extends RuntimeException {

	private static final long serialVersionUID = -7873733291888489890L;

	public final String code;
	public final String desc;
	public final Date time;

	public RemoteException(String code, String desc) {
		this(code, desc, new Date(System.currentTimeMillis()));
	}

	public RemoteException(String code, String desc, Date time) {
		super(time + " - " + code + " - " + desc);
		this.code = code;
		this.desc = desc;
		this.time = time;
	}
}
