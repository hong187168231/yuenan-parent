package com.tcg.api.wsclient.exception;

/**
 * <CODE> ProcessException </CODE>
 * 执行序异常处理
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public class ProcessException extends RuntimeException {

	private static final long serialVersionUID = 2989760020521615040L;

	public ProcessException(String message, Throwable cause) {
		super(message, cause);
	}
}
