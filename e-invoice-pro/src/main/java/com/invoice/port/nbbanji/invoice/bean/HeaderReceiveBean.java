package com.invoice.port.nbbanji.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="INTERFACE")
public class HeaderReceiveBean {
	private String taxNo;
	private String conTent;
	
	@XmlElement(name = "NSRSBH")
	public void setTaxNo(String taxNo) {
	    this.taxNo = taxNo;
	}

	public String getTaxNo() {
	    return taxNo;
	}
	
	@XmlElement(name = "CONTENT")
	public void setConTent(String conTent) {
	    this.conTent = conTent;
	}

	public String getConTent() {
	    return conTent;
	}
}
