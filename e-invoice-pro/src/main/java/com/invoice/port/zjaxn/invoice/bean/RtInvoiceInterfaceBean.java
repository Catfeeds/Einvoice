package com.invoice.port.zjaxn.invoice.bean;

import java.util.List;

public class RtInvoiceInterfaceBean {
	private String result;
	private String errorMsg;
	private List<RtInvoiceHeadBean> list;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public List<RtInvoiceHeadBean> getList() {
		return list;
	}
	public void setList(List<RtInvoiceHeadBean> list) {
		this.list = list;
	}
 
	
	
	
}
