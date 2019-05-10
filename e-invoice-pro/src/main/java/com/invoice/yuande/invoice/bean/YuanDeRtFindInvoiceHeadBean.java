package com.invoice.yuande.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RESPONSE_FPFH")
public class YuanDeRtFindInvoiceHeadBean {
	/*返回代码*/
	private String returncode;
	/*返回描述*/
	private String returnmessage;
	
	private String fpqqlsh;
	
	private List<YuanDeRtFindInvoiceDetailBean> details;
	
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "RESPONSE_FPFH";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	
	@XmlElement(name = "RETURNCODE")  
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	@XmlElement(name = "RETURNMESSAGE")  
	public String getReturnmessage() {
		return returnmessage;
	}
	public void setReturnmessage(String returnmessage) {
		this.returnmessage = returnmessage;
	}
	@XmlElement(name = "FPQQLSH")  
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	@XmlElement(name = "FPXX")  
	public List<YuanDeRtFindInvoiceDetailBean> getDetails() {
		return details;
	}
	public void setDetails(List<YuanDeRtFindInvoiceDetailBean> details) {
		this.details = details;
	}
	
	
	
}
