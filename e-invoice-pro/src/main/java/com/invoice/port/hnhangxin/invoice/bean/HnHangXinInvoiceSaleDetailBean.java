package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPKJXX_XMXX")
public class HnHangXinInvoiceSaleDetailBean {
	private String xmmc;//<XMMC>项目名称</XMMC> 

	private String xmdw;// <XMDW>项目单位</XMDW> 

	private String ggxh;// <GGXH>规格型号</GGXH> 

	private String xmsl;// <XMSL>项目数量</XMSL> 

	private String hsbz;// <HSBZ>含税标志</HSBZ> 
	 
	private String xmdj;// <XMDJ>项目单价</XMDJ> 

	private String fphxz;// <FPHXZ>发票行性质</FPHXZ> 

	private String spbm;// <SPBM>商品编码</SPBM> 

	private String zxbm;// <ZXBM>自行编码</ZXBM> 

	private String yhzcbs;// <YHZCBS>优惠政策标识</YHZCBS> 

	private String lslbs;// <LSLBS>零税率标识<LSLBS/> 

	private String zzstsgl;// <ZZSTSGL>增值税特殊管理</ZZSTSGL> 

	private String kce;//<KCE>扣除额</KCE> 

	private String xmje;// <XMJE>项目金额</XMJE> 

	private String sl;//<SL>税率</SL> 

	private String se;//<SE>税额</SE> 

	private String byzd1;//<BYZD1>备用字段</BYZD1> 

	private String byzd2;//<BYZD2>备用字段</BYZD2> 

	private String byzd3;//<BYZD3>备用字段</BYZD3> 

	private String byzd4;//<BYZD4>备用字段</BYZD4> 

	private String byzd5;//<BYZD5>备用字段</BYZD5> 
	
	@XmlElement(name = "XMMC")
	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	@XmlElement(name = "XMDW")
	public String getXmdw() {
		return xmdw;
	}
	
	public void setXmdw(String xmdw) {
		this.xmdw = xmdw;
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
	@XmlElement(name = "HSBZ")
	public String getHsbz() {
		return hsbz;
	}

	public void setHsbz(String hsbz) {
		this.hsbz = hsbz;
	}
	@XmlElement(name = "XMDJ")
	public String getXmdj() {
		return xmdj;
	}

	public void setXmdj(String xmdj) {
		this.xmdj = xmdj;
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
	@XmlElement(name = "KCE")
	public String getKce() {
		return kce;
	}

	public void setKce(String kce) {
		this.kce = kce;
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
