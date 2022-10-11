package com.tcg.api.wsclient.exception;

/**
 * <CODE> TransportException </CODE>
 * 传输异常处理
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class TransportException extends RuntimeException {

	private static final long serialVersionUID = -320484205576658104L;

	public TransportException(String message, Throwable cause) {
		super(message, cause);
	}
}
