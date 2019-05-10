package com.invoice.yuande.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class YuanDeInvoiceSaleDzDetailList {
	
	private List<YuanDeInvoiceSaleDzDetailBean> details;
	
	@XmlElement(name = "InfoDetail")  
	public List<YuanDeInvoiceSaleDzDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<YuanDeInvoiceSaleDzDetailBean> details) {
		this.details = details;
	}
	
	
}
