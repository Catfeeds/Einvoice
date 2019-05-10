package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="FPKJXX_ZFXX")
public class HnHangXinInvoiceOrdPayBean {
    private String zffs;//<ZFFS>支付方式</ZFFS> 

    private String zflsh;// <ZFLSH>支付流水号</ZFLSH> 

    private String zfpt;//<ZFPT>支付平台</ZFPT> 
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPKJXX_ZFXX";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	@XmlElement(name = "ZFFS")  
	public String getZffs() {
		return zffs;
	}
	public void setZffs(String zffs) {
		this.zffs = zffs;
	}
	@XmlElement(name = "ZFLSH")  
	public String getZflsh() {
		return zflsh;
	}
	public void setZflsh(String zflsh) {
		this.zflsh = zflsh;
	}
	@XmlElement(name = "ZFPT")  
	public String getZfpt() {
		return zfpt;
	}
	public void setZfpt(String zfpt) {
		this.zfpt = zfpt;
	}
	
	
}
