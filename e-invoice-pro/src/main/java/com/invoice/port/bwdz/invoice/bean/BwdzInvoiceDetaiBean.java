package com.invoice.port.bwdz.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="COMMON_FPKJ_XMXX")
public class BwdzInvoiceDetaiBean {
	
	private String xmmc;//项目名称
	private String dw;//单位
	private String ggxh;//规格型号
	private String xmsl;//项目数量
	private String xmdj;//项目单价
	private String xmje;//项目金额
	private String sl;//税率
	private String se;//税额
	private String fphxz;//发票行性质
	private String spbm;//商品编码
	private String zxbm;// 自行编码
	private String yhzcbs;//优惠政策标识
	private String lslbs;//零税率标识
	private String zzstsgl;//增值税特殊管理
	private String by1;//备用字段1
	private String by2;//备用字段2
	private String by3;//备用字段3
	private String by4;//备用字段4
	private String by5;//备用字段5
	
	
	
	 @XmlElement(name = "XMMC")  
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
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
	 @XmlElement(name = "XMSL")  
	public String getXmsl() {
		return xmsl;
	}
	public void setXmsl(String xmsl) {
		this.xmsl = xmsl;
	}
	 @XmlElement(name = "XMDJ")  
	public String getXmdj() {
		return xmdj;
	}
	public void setXmdj(String xmdj) {
		this.xmdj = xmdj;
	}
	 @XmlElement(name = "XMJE")  
	public String getXmje() {
		return xmje;
	}
	public void setXmje(String xmje) {
		this.xmje = xmje;
	}
	 @XmlElement(name = "SL")  
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	 @XmlElement(name = "SE")  
	public String getSe() {
		return se;
	}
	public void setSe(String se) {
		this.se = se;
	}
	 @XmlElement(name = "FPHXZ")  
	public String getFphxz() {
		return fphxz;
	}
	public void setFphxz(String fphxz) {
		this.fphxz = fphxz;
	}
	 @XmlElement(name = "SPBM")  
	public String getSpbm() {
		return spbm;
	}
	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}
	 @XmlElement(name = "ZXBM")  
	public String getZxbm() {
		return zxbm;
	}
	public void setZxbm(String zxbm) {
		this.zxbm = zxbm;
	}
	 @XmlElement(name = "YHZCBS")  
	public String getYhzcbs() {
		return yhzcbs;
	}
	public void setYhzcbs(String yhzcbs) {
		this.yhzcbs = yhzcbs;
	}
	 @XmlElement(name = "LSLBS")  
	public String getLslbs() {
		return lslbs;
	}
	public void setLslbs(String lslbs) {
		this.lslbs = lslbs;
	}
	 @XmlElement(name = "ZZSTSGL")  
	public String getZzstsgl() {
		return zzstsgl;
	}
	public void setZzstsgl(String zzstsgl) {
		this.zzstsgl = zzstsgl;
	}
	@XmlElement(name = "BY1")  
	public String getBy1() {
		return by1;
	}
	public void setBy1(String by1) {
		this.by1 = by1;
	}
	@XmlElement(name = "BY2")
	public String getBy2() {
		return by2;
	}
	public void setBy2(String by2) {
		this.by2 = by2;
	}
	@XmlElement(name = "BY3")
	public String getBy3() {
		return by3;
	}
	public void setBy3(String by3) {
		this.by3 = by3;
	}
	@XmlElement(name = "BY4")
	public String getBy4() {
		return by4;
	}
	public void setBy4(String by4) {
		this.by4 = by4;
	}
	@XmlElement(name = "BY5")
	public String getBy5() {
		return by5;
	}
	public void setBy5(String by5) {
		this.by5 = by5;
	}

	
}
