package com.invoice.yuande.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RetData")
public class YuanDeDzRntDataBean {
	
	private String RetCode;
	private String RetMsg;
	private List<YuanDeRtDzInvoiceBean> invoice;
	
	
	@XmlElement(name = "Invoice")  
	public List<YuanDeRtDzInvoiceBean> getInvoice() {
		return invoice;
	}
	public void setInvoice(List<YuanDeRtDzInvoiceBean> invoice) {
		this.invoice = invoice;
	}
	@XmlElement(name = "RetCode")  
	public String getRetCode() {
		return RetCode;
	}
	public void setRetCode(String retCode) {
		RetCode = retCode;
	}
	
	@XmlElement(name = "RetMsg")  
	public String getRetMsg() {
		return RetMsg;
	}
	public void setRetMsg(String retMsg) {
		RetMsg = retMsg;
	}

 
	
}
