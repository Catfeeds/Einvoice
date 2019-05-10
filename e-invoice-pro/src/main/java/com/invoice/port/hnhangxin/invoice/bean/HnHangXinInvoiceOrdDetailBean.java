package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPKJXX_DDMXXX")
public class HnHangXinInvoiceOrdDetailBean {
   private String ddmc;//<DDMC>订单名称</DDMC> 

   private String dw;// <DW>单位</DW> 

   private String ggxh;//<GGXH>规格型号</GGXH> 

   private String sl;//<SL>数量</SL> 

   private String dj;//<DJ>单价</DJ> 

   private String je;//<JE>金额</JE> 

   private String byzd1;//<BYZD1>备用字段</BYZD1> 

   private String byzd2;//<BYZD2>备用字段</BYZD2> 

   private String byzd3;// <BYZD3>备用字段</BYZD3> 

   private String byzd4;// <BYZD4>备用字段</BYZD4> 

   private String byzd5;//<BYZD5>备用字段</BYZD5> 

   @XmlElement(name = "DDMC")  
	public String getDdmc() {
		return ddmc;
	}
	
	public void setDdmc(String ddmc) {
		this.ddmc = ddmc;
	}
	@XmlElement(name = "DW")  
	public String getDw() {
		return dw;
	}
	
	public void setDw(String dw) {
		this.dw = dw;
	}
	@XmlElement(name = "GGXH")  
	public String getGgxh() {
		return ggxh;
	}
	
	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}
	@XmlElement(name = "SL")  
	public String getSl() {
		return sl;
	}
	
	public void setSl(String sl) {
		this.sl = sl;
	}
	@XmlElement(name = "DJ")  
	public String getDj() {
		return dj;
	}
	
	public void setDj(String dj) {
		this.dj = dj;
	}
	@XmlElement(name = "JE")  
	public String getJe() {
		return je;
	}
	
	public void setJe(String je) {
		this.je = je;
	}
	@XmlElement(name = "BYZD1")  
	public String getByzd1() {
		return byzd1;
	}
	
	public void setByzd1(String byzd1) {
		this.byzd1 = byzd1;
	}
	@XmlElement(name = "BYZD2")  
	public String getByzd2() {
		return byzd2;
	}
	
	public void setByzd2(String byzd2) {
		this.byzd2 = byzd2;
	}
	@XmlElement(name = "BYZD3")  
	public String getByzd3() {
		return byzd3;
	}
	
	public void setByzd3(String byzd3) {
		this.byzd3 = byzd3;
	}
	@XmlElement(name = "BYZD4")  
	public String getByzd4() {
		return byzd4;
	}
	
	public void setByzd4(String byzd4) {
		this.byzd4 = byzd4;
	}
	@XmlElement(name = "BYZD5")  
	public String getByzd5() {
		return byzd5;
	}
	
	public void setByzd5(String byzd5) {
		this.byzd5 = byzd5;
	}
	   
   
}
