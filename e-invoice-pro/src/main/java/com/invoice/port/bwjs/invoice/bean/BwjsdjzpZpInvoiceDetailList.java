package com.invoice.port.bwjs.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fyxm")
public class BwjsdjzpZpInvoiceDetailList {
	private String count;
	private List<BwjsdjzpInvoiceDetailBean> detailList;
	
	@XmlAttribute(name = "count")
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	@XmlElement(name = "group")  
	public List<BwjsdjzpInvoiceDetailBean> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<BwjsdjzpInvoiceDetailBean> detailList) {
		this.detailList = detailList;
	}
	
}
