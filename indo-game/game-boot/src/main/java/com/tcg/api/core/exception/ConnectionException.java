package com.tcg.api.core.exception;


/**
 * <CODE> ConnectionException </CODE>
 * 连接异常处理
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class ConnectionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;
	private int code;
	
	public ConnectionException() {
		super();
	}
	
	public ConnectionException(String message, Throwable t) {
		super(message, t);
		this.message = message;
	}
	
	public ConnectionException(String message, int code) {
		super(message);
		this.message = message;
		this.code = code;
	}

	public ConnectionException(Throwable t) {
		super(t);
	}
	
	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
	
}
