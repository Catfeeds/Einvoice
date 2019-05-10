package com.invoice.port.hnhangxin.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPKJXX_XMXXS")
public class HnHangXinInvoiceSaleDetailList {
	
	private List<HnHangXinInvoiceSaleDetailBean> saleDetail;
	
	private String aa;
	
	private int size;
	
	
	@XmlAttribute(name = "size")
	public int getSize() {
		return saleDetail.size();
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPKJXX_XMXX;";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	@XmlElement(name = "FPKJXX_XMXX")  
	public List<HnHangXinInvoiceSaleDetailBean> getSaleDetail() {
		return saleDetail;
	}

	public void setSaleDetail(List<HnHangXinInvoiceSaleDetailBean> saleDetail) {
		this.saleDetail = saleDetail;
	}
	
	
}
