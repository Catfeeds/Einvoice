package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="group")
public class BwjfRtmxGroupBean {
	
	private String fphxz;//	发票行性质
	private String xm;//	项目
	private double sl;//	数量
	private double bhsdj;//	不含税单价
	private double bhsje;//	不含税金额
	private double hsdj;//	含税单价
	private double hsje;//	含税金额
	private double zsl;//	税率
	private double se;//	税额
	private String spbm;//	商品编码
	private String zxbm;//	纳税人自行编码
	private String yhzcbs;//	优惠政策标识
	private String lslbs;//	零税率标识
	private String zzstsgl;//	增值税特殊管理
	
	private int xh;

	public String getFphxz() {
		return fphxz;
	}

	public void setFphxz(String fphxz) {
		this.fphxz = fphxz;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public double getSl() {
		return sl;
	}

	public void setSl(double sl) {
		this.sl = sl;
	}

	public double getBhsdj() {
		return bhsdj;
	}

	public void setBhsdj(double bhsdj) {
		this.bhsdj = bhsdj;
	}

	public double getBhsje() {
		return bhsje;
	}

	public void setBhsje(double bhsje) {
		this.bhsje = bhsje;
	}

	public double getHsdj() {
		return hsdj;
	}

	public void setHsdj(double hsdj) {
		this.hsdj = hsdj;
	}

	public double getHsje() {
		return hsje;
	}

	public void setHsje(double hsje) {
		this.hsje = hsje;
	}

	public double getZsl() {
		return zsl;
	}

	public void setZsl(double zsl) {
		this.zsl = zsl;
	}

	public double getSe() {
		return se;
	}

	public void setSe(double se) {
		this.se = se;
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
	
	@XmlAttribute(name = "xh")
	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}
	
	
	
}
