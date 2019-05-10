package com.invoice.port.nbbwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "COMMON_FPKJ_XMXX")
public class NbBwjfInvoiceDetaiBean {

	private String fphxz;// 发票行性质
	private String spbm;// 商品编码
	private String zxbm;

	private String yhzcbs;// 优惠政策标识
	private String lslbs;// 零税率标识
	private String zzstsgl;// 增值税特殊管理
	private String xmmc;// 项目名称
	private String ggxh;// 规格型号
	private String dw;// 单位
	private String xmsl;// 项目数量
	private String xmdj;// 项目单价
	private String xmje;// 项目金额
	private String sl;// 税率
	private String se;// 税额

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

	@XmlElement(name = "XMMC")
	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	@XmlElement(name = "GGXH")
	public String getGgxh() {
		return ggxh;
	}

	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}

	@XmlElement(name = "DW")
	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
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

}
