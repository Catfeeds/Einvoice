package com.invoice.port.hnhangxin.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPKJXX_DDMXXXS")
public class HnHangXinInvoiceOrdDetaillist {
	
	private List<HnHangXinInvoiceOrdDetailBean> ordDetaillist;
	
	private String aa;
	
	private int size;
	
	
	@XmlAttribute(name = "size")
	public int getSize() {
		return 0;//ordDetaillist.size();
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPKJXX_DDMXXX;";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	@XmlElement(name = "FPKJXX_DDMXXX")  
	public List<HnHangXinInvoiceOrdDetailBean> getOrdDetaillist() {
		return ordDetaillist;
	}

	public void setOrdDetaillist(List<HnHangXinInvoiceOrdDetailBean> ordDetaillist) {
		this.ordDetaillist = ordDetaillist;
	}

 
	
}
