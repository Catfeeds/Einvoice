package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="FPKJXX_DDXX")
public class HnHangXinInvoiceOrdHeadBean {
	
    private String ddh;//<DDH>订单号</DDH> 

    private String thdh;//<THDH>退货单号</THDH> 

    private String dddate;//<DDDATE>订单时间</DDDATE> 
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPKJXX_DDXX";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	@XmlElement(name = "DDH")  
	public String getDdh() {
		return ddh;
	}
	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	@XmlElement(name = "THDH")  
	public String getThdh() {
		return thdh;
	}
	public void setThdh(String thdh) {
		this.thdh = thdh;
	}
	@XmlElement(name = "DDDATE")
	public String getDddate() {
		return dddate;
	}
	public void setDddate(String dddate) {
		this.dddate = dddate;
	}
	
	
	
}
