package com.invoice.yuande.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FP_KJMXS")
public class YuanDeInvoiceSaleDetailList {
	
	private List<YuanDeInvoiceSaleDetailBean> details;
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FP_KJMX;";
	}
	
	private int size;
	
	
	@XmlAttribute(name = "size")
	public int getSize() {
		return details.size();
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setAa(String aa) {
		this.aa = aa;
	}

	@XmlElement(name = "FP_KJMX")  
	public List<YuanDeInvoiceSaleDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<YuanDeInvoiceSaleDetailBean> details) {
		this.details = details;
	}
	
	
}
