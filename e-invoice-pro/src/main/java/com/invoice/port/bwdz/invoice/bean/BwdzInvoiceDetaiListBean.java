package com.invoice.port.bwdz.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="COMMON_FPKJ_XMXXS")
public class BwdzInvoiceDetaiListBean {
	
	private List<BwdzInvoiceDetaiBean> detail;
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "COMMON_FPKJ_XMXX";
	}
	
	private int size;
	
	@XmlAttribute(name = "size")
	public int getSize() {
		return detail.size();
	}

	@XmlElement(name = "COMMON_FPKJ_XMXX")  
	public List<BwdzInvoiceDetaiBean> getDetail() {
		return detail;
	}


	public void setDetail(List<BwdzInvoiceDetaiBean> detail) {
		this.detail = detail;
	}
	
	

 
	 
	 
}
