package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="FPKJXX_WLXX")
public class HnHangXinInvoiceWlBean {
	private String cygs;//<CYGS>承运公司</CYGS> 

	private String shsj;//<SHSJ>送货时间</SHSJ> 

	private String wldh;//<WLDH>物流单号</WLDH> 

	private String shdz;//<SHDZ>送货地址</SHDZ>
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPKJXX_WLXX";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	@XmlElement(name = "CYGS")  
	public String getCygs() {
		return cygs;
	}
	public void setCygs(String cygs) {
		this.cygs = cygs;
	}
	@XmlElement(name = "SHSJ")  
	public String getShsj() {
		return shsj;
	}
	public void setShsj(String shsj) {
		this.shsj = shsj;
	}
	@XmlElement(name = "WLDH")  
	public String getWldh() {
		return wldh;
	}
	public void setWldh(String wldh) {
		this.wldh = wldh;
	}
	@XmlElement(name = "SHDZ")  
	public String getShdz() {
		return shdz;
	}
	public void setShdz(String shdz) {
		this.shdz = shdz;
	}
	
	
}
