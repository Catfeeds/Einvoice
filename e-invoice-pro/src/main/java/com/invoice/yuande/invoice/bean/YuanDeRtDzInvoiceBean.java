package com.invoice.yuande.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Invoice")
public class YuanDeRtDzInvoiceBean {
	
	private String clientName; 
	private String infoTypeCode; 
	private String infoNumber; 
	private String money; 
	private String taxAmount; 
	private String time; 
	private String pdfurl; 
	private String pdfMsg;
	
	@XmlElement(name = "ClientName")  
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	@XmlElement(name = "InfoTypeCode")  
	public String getInfoTypeCode() {
		return infoTypeCode;
	}
	public void setInfoTypeCode(String infoTypeCode) {
		this.infoTypeCode = infoTypeCode;
	}
	
	@XmlElement(name = "InfoNumber")  
	public String getInfoNumber() {
		return infoNumber;
	}
	public void setInfoNumber(String infoNumber) {
		this.infoNumber = infoNumber;
	}
	
	@XmlElement(name = "Money")  
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
	@XmlElement(name = "TaxAmount")  
	public String getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	@XmlElement(name = "Time")  
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	@XmlElement(name = "Pdfurl")  
	public String getPdfurl() {
		return pdfurl;
	}
	public void setPdfurl(String pdfurl) {
		this.pdfurl = pdfurl;
	}
	
	@XmlElement(name = "PdfMsg")  
	public String getPdfMsg() {
		return pdfMsg;
	}
	public void setPdfMsg(String pdfMsg) {
		this.pdfMsg = pdfMsg;
	} 
	
	
	
}
