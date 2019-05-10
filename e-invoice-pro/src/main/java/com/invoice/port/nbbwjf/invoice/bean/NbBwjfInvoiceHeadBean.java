package com.invoice.port.nbbwjf.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "REQUEST_COMMON_FPKJ")
public class NbBwjfInvoiceHeadBean {
	private String fplxdm;// 发票类型代码
	private String fpqqlsh;// 发票请求流水号
	private int kplx;// 开票类型
	private String bmb_bbh;// 编码表版本号
	private String zsfs;// 征税方式
	private String xsf_nsrsbh;// 销售方纳税人识别号
	private String xsf_mc;// 销售方名称
	private String xsf_dzdh;// 销售方地址电话
	private String xsf_yhzh;// 销售方银行账户
	private String gmf_nsrsbh;// 购买方纳税人识别号
	private String gmf_mc;// 购买方名称
	private String gmf_dzdh;// 购买方地址电话
	private String gmf_yhzh;// 购买方银行账户
	private String gmf_sjh;// 购买方手机号
	private String gmf_dzyx;// 购买方电子邮箱
	private String kpr;// 开票人
	private String skr;// 收款人
	private String fhr;// 复核人
	private String yfp_dm;// 原发票代码
	private String yfp_hm;// 原发票号码
	private double jshj;// 价税合计
	private double hjje;// 合计金额
	private double hjse;// 合计税额
	private double kce;// 扣除额
	private String bz;// 备注

	private NbBwjfInvoiceDetaiListBean detaillist;

	@XmlElement(name = "COMMON_FPKJ_XMXXS")
	public NbBwjfInvoiceDetaiListBean getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(NbBwjfInvoiceDetaiListBean detaillist) {
		this.detaillist = detaillist;
	}

	@XmlAttribute(name = "class")
	public String getAa() {
		return "REQUEST_COMMON_FPKJ";
	}

	@XmlElement(name = "FPLXDM")
	public String getFplxdm() {
		return fplxdm;
	}

	public void setFplxdm(String fplxdm) {
		this.fplxdm = fplxdm;
	}

	@XmlElement(name = "FPQQLSH")
	public String getFpqqlsh() {
		return fpqqlsh;
	}

	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}

	@XmlElement(name = "KPLX")
	public int getKplx() {
		return kplx;
	}

	public void setKplx(int kplx) {
		this.kplx = kplx;
	}

	@XmlElement(name = "BMB_BBH")
	public String getBmb_bbh() {
		return bmb_bbh;
	}

	public void setBmb_bbh(String bmb_bbh) {
		this.bmb_bbh = bmb_bbh;
	}

	@XmlElement(name = "ZSFS")
	public String getZsfs() {
		return zsfs;
	}

	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
	}

	@XmlElement(name = "XSF_NSRSBH")
	public String getXsf_nsrsbh() {
		return xsf_nsrsbh;
	}

	public void setXsf_nsrsbh(String xsf_nsrsbh) {
		this.xsf_nsrsbh = xsf_nsrsbh;
	}

	@XmlElement(name = "XSF_MC")
	public String getXsf_mc() {
		return xsf_mc;
	}

	public void setXsf_mc(String xsf_mc) {
		this.xsf_mc = xsf_mc;
	}

	@XmlElement(name = "XSF_DZDH")
	public String getXsf_dzdh() {
		return xsf_dzdh;
	}

	public void setXsf_dzdh(String xsf_dzdh) {
		this.xsf_dzdh = xsf_dzdh;
	}

	@XmlElement(name = "XSF_YHZH")
	public String getXsf_yhzh() {
		return xsf_yhzh;
	}

	public void setXsf_yhzh(String xsf_yhzh) {
		this.xsf_yhzh = xsf_yhzh;
	}

	@XmlElement(name = "GMF_NSRSBH")
	public String getGmf_nsrsbh() {
		return gmf_nsrsbh;
	}

	public void setGmf_nsrsbh(String gmf_nsrsbh) {
		this.gmf_nsrsbh = gmf_nsrsbh;
	}

	@XmlElement(name = "GMF_MC")
	public String getGmf_mc() {
		return gmf_mc;
	}

	public void setGmf_mc(String gmf_mc) {
		this.gmf_mc = gmf_mc;
	}

	@XmlElement(name = "GMF_DZDH")
	public String getGmf_dzdh() {
		return gmf_dzdh;
	}

	public void setGmf_dzdh(String gmf_dzdh) {
		this.gmf_dzdh = gmf_dzdh;
	}

	@XmlElement(name = "GMF_YHZH")
	public String getGmf_yhzh() {
		return gmf_yhzh;
	}

	public void setGmf_yhzh(String gmf_yhzh) {
		this.gmf_yhzh = gmf_yhzh;
	}

	@XmlElement(name = "GMF_SJH")
	public String getGmf_sjh() {
		return gmf_sjh;
	}

	public void setGmf_sjh(String gmf_sjh) {
		this.gmf_sjh = gmf_sjh;
	}

	@XmlElement(name = "GMF_DZYX")
	public String getGmf_dzyx() {
		return gmf_dzyx;
	}

	public void setGmf_dzyx(String gmf_dzyx) {
		this.gmf_dzyx = gmf_dzyx;
	}

	@XmlElement(name = "KPR")
	public String getKpr() {
		return kpr;
	}

	public void setKpr(String kpr) {
		this.kpr = kpr;
	}

	@XmlElement(name = "SKR")
	public String getSkr() {
		return skr;
	}

	public void setSkr(String skr) {
		this.skr = skr;
	}

	@XmlElement(name = "FHR")
	public String getFhr() {
		return fhr;
	}

	public void setFhr(String fhr) {
		this.fhr = fhr;
	}

	@XmlElement(name = "YFP_DM")
	public String getYfp_dm() {
		return yfp_dm;
	}

	public void setYfp_dm(String yfp_dm) {
		this.yfp_dm = yfp_dm;
	}

	@XmlElement(name = "YFP_HM")
	public String getYfp_hm() {
		return yfp_hm;
	}

	public void setYfp_hm(String yfp_hm) {
		this.yfp_hm = yfp_hm;
	}

	@XmlElement(name = "JSHJ")
	public double getJshj() {
		return jshj;
	}

	public void setJshj(double jshj) {
		this.jshj = jshj;
	}

	@XmlElement(name = "HJJE")
	public double getHjje() {
		return hjje;
	}

	public void setHjje(double hjje) {
		this.hjje = hjje;
	}

	@XmlElement(name = "HJSE")
	public double getHjse() {
		return hjse;
	}

	public void setHjse(double hjse) {
		this.hjse = hjse;
	}

	@XmlElement(name = "KCE")
	public double getKce() {
		return kce;
	}

	public void setKce(double kce) {
		this.kce = kce;
	}

	@XmlElement(name = "BZ")
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

}
