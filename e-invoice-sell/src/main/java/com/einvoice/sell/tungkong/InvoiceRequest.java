package com.einvoice.sell.tungkong;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="INTERFACE")
public class InvoiceRequest implements Serializable{
	private static final long serialVersionUID = 1L;
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
