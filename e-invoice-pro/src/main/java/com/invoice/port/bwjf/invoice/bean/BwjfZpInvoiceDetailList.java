package com.invoice.port.bwjf.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fyxm")
public class BwjfZpInvoiceDetailList {
	private String count;
	private List<BwjfInvoiceDetailBean> detailList;
	
	@XmlAttribute(name = "count")
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	@XmlElement(name = "group")  
	public List<BwjfInvoiceDetailBean> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<BwjfInvoiceDetailBean> detailList) {
		this.detailList = detailList;
	}
	
}
