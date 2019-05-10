package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="REQUEST_FPKJXX")
public class HnHangXinInterfaceBean {
	
	private HnHangXinInvoiceSaleHeadBean salehead;
	private HnHangXinInvoiceSaleDetailList saleDetaillist;
	private HnHangXinInvoiceOrdHeadBean ordhead;
	private HnHangXinInvoiceOrdDetaillist orddetaillist;
	private HnHangXinInvoiceOrdPayBean ordpay;
	private HnHangXinInvoiceWlBean wl;
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "REQUEST_FPKJXX";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	
	@XmlElement(name = "FPKJXX_FPTXX")  
	public HnHangXinInvoiceSaleHeadBean getSalehead() {
		return salehead;
	}
	public void setSalehead(HnHangXinInvoiceSaleHeadBean salehead) {
		this.salehead = salehead;
	}
	@XmlElement(name = "FPKJXX_XMXXS")
	public HnHangXinInvoiceSaleDetailList getSaleDetaillist() {
		return saleDetaillist;
	}
	public void setSaleDetaillist(HnHangXinInvoiceSaleDetailList saleDetaillist) {
		this.saleDetaillist = saleDetaillist;
	}
	@XmlElement(name = "FPKJXX_DDXX")  
	public HnHangXinInvoiceOrdHeadBean getOrdhead() {
		return ordhead;
	}
	public void setOrdhead(HnHangXinInvoiceOrdHeadBean ordhead) {
		this.ordhead = ordhead;
	}
	@XmlElement(name = "FPKJXX_DDMXXXS")  
	public HnHangXinInvoiceOrdDetaillist getOrddetaillist() {
		return orddetaillist;
	}
	public void setOrddetaillist(HnHangXinInvoiceOrdDetaillist orddetaillist) {
		this.orddetaillist = orddetaillist;
	}
	@XmlElement(name = "FPKJXX_ZFXX")  
	public HnHangXinInvoiceOrdPayBean getOrdpay() {
		return ordpay;
	}
	public void setOrdpay(HnHangXinInvoiceOrdPayBean ordpay) {
		this.ordpay = ordpay;
	}
	@XmlElement(name = "FPKJXX_WLXX")  
	public HnHangXinInvoiceWlBean getWl() {
		return wl;
	}
	public void setWl(HnHangXinInvoiceWlBean wl) {
		this.wl = wl;
	}
	
	
}
