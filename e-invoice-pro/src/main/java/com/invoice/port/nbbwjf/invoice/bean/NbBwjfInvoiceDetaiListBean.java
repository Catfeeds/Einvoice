package com.invoice.port.nbbwjf.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="COMMON_FPKJ_XMXXS")
public class NbBwjfInvoiceDetaiListBean {
	
	private List<NbBwjfInvoiceDetaiBean> detail;
	@XmlAttribute(name = "class")
	public String getAa() {
		return "COMMON_FPKJ_XMXX";
	}
	@XmlAttribute(name = "size")
	public int getSize() {
		return detail.size();
	}

	@XmlElement(name = "COMMON_FPKJ_XMXX")  
	public List<NbBwjfInvoiceDetaiBean> getDetail() {
		return detail;
	}
	public void setDetail(List<NbBwjfInvoiceDetaiBean> detail) {
		this.detail = detail;
	}
	
	

 
	 
	 
}
