package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;

//开具发票需要的明细信息
public class BwjfInvoiceDetailBean {
	private String fphxz;//	发票行性质 券票
	private String spmc;//	商品名称
	private String spsm;//	商品税目
	private String ggxh;//	规格型号
	private String dw;//	单位
	private String spsl;//	商品数量
	private String dj;//	单价
	private String je;//	金额
	private String sl;//	税率   卷票中为--数量
	private String se;//	税额 券票
	private String hsbz;//	含税标志
	private String spbm;//	商品编码   券票
	private String zxbm;//	纳税人自行编码 券票
	private String yhzcbs;//	优惠政策标识 券票
	private String lslbs;//	零税率标识  券票
	private String zzstsgl;//	增值税特殊管理  券票
	//卷票独有字段
	private String xm;//	项目
	private String bhsdj;//	不含税单价
	private String bhsje;//	不含税金额
	private String hsdj;//	含税单价
	private String hsje;//	含税金额
	private String zsl;//	税率
	
	private String xh;
	
	@XmlAttribute(name = "xh")
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getFphxz() {
		return fphxz;
	}
	public void setFphxz(String fphxz) {
		this.fphxz = fphxz;
	}
	public String getSpmc() {
		return spmc;
	}
	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}
	public String getSpsm() {
		return spsm;
	}
	public void setSpsm(String spsm) {
		this.spsm = spsm;
	}
	public String getGgxh() {
		return ggxh;
	}
	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getSpsl() {
		return spsl;
	}
	public void setSpsl(String spsl) {
		this.spsl = spsl;
	}
	public String getDj() {
		return dj;
	}
	public void setDj(String dj) {
		this.dj = dj;
	}
	public String getJe() {
		return je;
	}
	public void setJe(String je) {
		this.je = je;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getSe() {
		return se;
	}
	public void setSe(String se) {
		this.se = se;
	}
	public String getHsbz() {
		return hsbz;
	}
	public void setHsbz(String hsbz) {
		this.hsbz = hsbz;
	}
	public String getSpbm() {
		return spbm;
	}
	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}
	public String getZxbm() {
		return zxbm;
	}
	public void setZxbm(String zxbm) {
		this.zxbm = zxbm;
	}
	public String getYhzcbs() {
		return yhzcbs;
	}
	public void setYhzcbs(String yhzcbs) {
		this.yhzcbs = yhzcbs;
	}
	public String getLslbs() {
		return lslbs;
	}
	public void setLslbs(String lslbs) {
		this.lslbs = lslbs;
	}
	public String getZzstsgl() {
		return zzstsgl;
	}
	public void setZzstsgl(String zzstsgl) {
		this.zzstsgl = zzstsgl;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getBhsdj() {
		return bhsdj;
	}
	public void setBhsdj(String bhsdj) {
		this.bhsdj = bhsdj;
	}
	public String getBhsje() {
		return bhsje;
	}
	public void setBhsje(String bhsje) {
		this.bhsje = bhsje;
	}
	public String getHsdj() {
		return hsdj;
	}
	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}
	public String getHsje() {
		return hsje;
	}
	public void setHsje(String hsje) {
		this.hsje = hsje;
	}
	public String getZsl() {
		return zsl;
	}
	public void setZsl(String zsl) {
		this.zsl = zsl;
	}
	
	
	
}
