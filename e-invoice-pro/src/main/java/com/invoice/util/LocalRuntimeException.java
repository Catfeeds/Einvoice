package com.invoice.util;

public class LocalRuntimeException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1399365268407427195L;

	/**
	 * 金额不对
	 */
	final public static int DAT_AMT_ERROR=-2; 
	
	/**
	 * 数据处理错误
	 */
	final public static int DATA_PROCESS_ERROR=-3;
	

	/**
	 * 数据格式错误
	 */
	final public static int DATA_FORMAT_ERROR=-4;
	
	/**
	 * 基础数据错误
	 */
	final public static int DATA_BASE_ERROR=-5;	
	
	/**
	 * 数据不存在
	 */
	final public static int DATA_NOT_FOUND=-6;
	
	/**
	 * 错误消息
	 */
	final public static int DATA_MESSAGE=-9;
	
	private String localMessage;
	private int localCode = -1;
	
	public LocalRuntimeException(String message) {
		super(message);
	}
	
	public LocalRuntimeException(String message,String localMessage) {
		super(message);
		this.localMessage = localMessage;
	}
	
	public LocalRuntimeException(String message,String localMessage,int localCode) {
		super(message);
		this.localMessage = localMessage;
		this.localCode=localCode;
	}
	
	public String getLocalMessage() {
		return localMessage;
	}
	public void setLocalMessage(String localMessage) {
		this.localMessage = localMessage;
	}
	public int getLocalCode() {
		return localCode;
	}
	public void setLocalCode(int localCode) {
		this.localCode = localCode;
	}
}
