package com.invoice.yuande.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RESPONSE_FPFH")
public class YuanDeRtInvoiceBean {
	
	/*返回代码*/
	private String returncode;
	/*返回描述*/
	private String returnmessage;
	/*合计不含税金额*/
	private double hjbhsje;
	/*开票日期*/
	private String kprq;

	/*发票代码*/
	private String fp_dm;
	/*发票号码*/
	private String fp_hm;
	/*销售清单标识*/
	private String xhqdbz;
	/*成功失败code*/
	private String retcode;
	/*防伪密文*/
	private String fwmw;
	/*校验码*/
	private String jym;
	/*签名值*/
	private String szqm;
	/*二维码*/
	private String ewm;
	
	/*单据状态 1保存  2删除  3.开票  4.作废*/
	private String status;
	
	
	private String pdf_url;// PDF_URL
	
	/* 购货方名称*/
	private String ghfmc;
	/*购货方税号*/
	private String ghf_nsrsbh;
	/*付款方银行及账号*/
	private String fkfkhyh_fkfyhzh;
	/*付款方地址电话*/
	private String fkfdz_fkfdh;
	/*销货方银行及账号*/
	private String xhfkhyh_skfyhzh;
	/*销货方地址电话*/
	private String xhfdz_xhfdh;
	/*发票种类代码*/
	private String fpzl_dm;
	/*原发票代码*/
	private String yfp_dm;
	/*原发票号码*/
	private String yfp_hm;
	/*备注*/
	private String bz;
	/*开票人*/
	private String kpy;
	/*复核人*/
	private String fhr;
	/*收款人*/
	private String sky;
	/*清单标识*/
	private String xhqd;
	/*小票提取码*/
	private String fpqqlsh;
	/*
	 * 小票流水号
	 * */
	private String serialid;
	
	private String sheettype;
	/*开票类型*/
	private int kplx;
	/*价税合计金额*/
	private double jshj;
	/*合计金额*/
	private double hjje;
	/*合计税额*/
	private double hjse;
	/*编码表版本号*/
	private String bmb_bbh;
	/*小票交易金额*/
	private double xpjyje;
	/*登陆人员名称*/
	private String userid;
	/*业务类型  1服务台  2 微信 3 特殊开具*/
	private String ywlx;
	/*门店ID*/
	private String shopid;
	/*小票交易时间*/
	private String xpjysj;
	/*购买方邮箱*/
	private String email;
	/*备注（支付方式）*/
	private String memo;
	
	private YuanDeRtSpmxListBean detaillist;
	
	
	@XmlElement(name = "SPMXS")  
	public YuanDeRtSpmxListBean getDetaillist() {
		return detaillist;
	}
	public void setDetaillist(YuanDeRtSpmxListBean detaillist) {
		this.detaillist = detaillist;
	}
	@XmlElement(name = "SHEETTYPE")  
	public String getSheettype() {
		return sheettype;
	}
	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}
	@XmlElement(name = "SERIALID")  
	public String getSerialid() {
		return serialid;
	}
	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}
	@XmlElement(name = "GHFMC")  
	public String getGhfmc() {
		return ghfmc;
	}
	public void setGhfmc(String ghfmc) {
		this.ghfmc = ghfmc;
	}
	@XmlElement(name = "GHF_NSRSBH")  
	public String getGhf_nsrsbh() {
		return ghf_nsrsbh;
	}
	public void setGhf_nsrsbh(String ghf_nsrsbh) {
		this.ghf_nsrsbh = ghf_nsrsbh;
	}
	@XmlElement(name = "FKFKHYH_FKFYHZH")  
	public String getFkfkhyh_fkfyhzh() {
		return fkfkhyh_fkfyhzh;
	}
	public void setFkfkhyh_fkfyhzh(String fkfkhyh_fkfyhzh) {
		this.fkfkhyh_fkfyhzh = fkfkhyh_fkfyhzh;
	}
	@XmlElement(name = "FKFDZ_FKFDH")  
	public String getFkfdz_fkfdh() {
		return fkfdz_fkfdh;
	}
	public void setFkfdz_fkfdh(String fkfdz_fkfdh) {
		this.fkfdz_fkfdh = fkfdz_fkfdh;
	}
	@XmlElement(name = "XHFKHYH_SKFYHZH")  
	public String getXhfkhyh_skfyhzh() {
		return xhfkhyh_skfyhzh;
	}
	public void setXhfkhyh_skfyhzh(String xhfkhyh_skfyhzh) {
		this.xhfkhyh_skfyhzh = xhfkhyh_skfyhzh;
	}
	@XmlElement(name = "XHFDZ_XHFDH")  
	public String getXhfdz_xhfdh() {
		return xhfdz_xhfdh;
	}
	public void setXhfdz_xhfdh(String xhfdz_xhfdh) {
		this.xhfdz_xhfdh = xhfdz_xhfdh;
	}
	@XmlElement(name = "FPZL_DM")  
	public String getFpzl_dm() {
		return fpzl_dm;
	}
	public void setFpzl_dm(String fpzl_dm) {
		this.fpzl_dm = fpzl_dm;
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
	@XmlElement(name = "BZ")  
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	@XmlElement(name = "KPY")  
	public String getKpy() {
		return kpy;
	}
	public void setKpy(String kpy) {
		this.kpy = kpy;
	}
	@XmlElement(name = "FHR")  
	public String getFhr() {
		return fhr;
	}
	public void setFhr(String fhr) {
		this.fhr = fhr;
	}
	@XmlElement(name = "SKY")  
	public String getSky() {
		return sky;
	}
	public void setSky(String sky) {
		this.sky = sky;
	}
	@XmlElement(name = "XHQD")  
	public String getXhqd() {
		return xhqd;
	}
	public void setXhqd(String xhqd) {
		this.xhqd = xhqd;
	}
	@XmlElement(name = "KPLX")  
	public int getKplx() {
		return kplx;
	}
	public void setKplx(int kplx) {
		this.kplx = kplx;
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
	@XmlElement(name = "BMB_BBH")  
	public String getBmb_bbh() {
		return bmb_bbh;
	}
	public void setBmb_bbh(String bmb_bbh) {
		this.bmb_bbh = bmb_bbh;
	}
	@XmlElement(name = "XPJYJE")  
	public double getXpjyje() {
		return xpjyje;
	}
	public void setXpjyje(double xpjyje) {
		this.xpjyje = xpjyje;
	}
	@XmlElement(name = "USERID")  
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@XmlElement(name = "YWLX")  
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	@XmlElement(name = "SHOPID")  
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	@XmlElement(name = "XPJYSJ")  
	public String getXpjysj() {
		return xpjysj;
	}
	public void setXpjysj(String xpjysj) {
		this.xpjysj = xpjysj;
	}
	@XmlElement(name = "EMAIL")  
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@XmlElement(name = "MEMO")  
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@XmlElement(name = "FPQQLSH")  
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	@XmlElement(name = "PDF_URL")  
	public String getPdf_url() {
		return pdf_url;
	}
	public void setPdf_url(String pdf_url) {
		this.pdf_url = pdf_url;
	}
	@XmlElement(name = "STATUS")  
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@XmlElement(name = "RETURNCODE")  
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	 @XmlElement(name = "RETURNMESSAGE")  
	public String getReturnmessage() {
		return returnmessage;
	}
	public void setReturnmessage(String returnmessage) {
		this.returnmessage = returnmessage;
	}
	 @XmlElement(name = "HJBHSJE")  
	public double getHjbhsje() {
		return hjbhsje;
	}
	public void setHjbhsje(double hjbhsje) {
		this.hjbhsje = hjbhsje;
	}
	 @XmlElement(name = "HJSE")  
	public double getHjse() {
		return hjse;
	}
	public void setHjse(double hjse) {
		this.hjse = hjse;
	}
	 @XmlElement(name = "KPRQ")  
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}

	 @XmlElement(name = "FP_DM")  
	public String getFp_dm() {
		return fp_dm;
	}
	public void setFp_dm(String fp_dm) {
		this.fp_dm = fp_dm;
	}
	 @XmlElement(name = "FP_HM")  
	public String getFp_hm() {
		return fp_hm;
	}
	public void setFp_hm(String fp_hm) {
		this.fp_hm = fp_hm;
	}
	 @XmlElement(name = "XHQDBZ")  
	public String getXhqdbz() {
		return xhqdbz;
	}
	public void setXhqdbz(String xhqdbz) {
		this.xhqdbz = xhqdbz;
	}
	 @XmlElement(name = "RETCODE")  
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	 @XmlElement(name = "FWMW")  
	public String getFwmw() {
		return fwmw;
	}
	public void setFwmw(String fwmw) {
		this.fwmw = fwmw;
	}
	 @XmlElement(name = "JYM")  
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	 @XmlElement(name = "SZQM")  
	public String getSzqm() {
		return szqm;
	}
	public void setSzqm(String szqm) {
		this.szqm = szqm;
	}
	 @XmlElement(name = "EWM")  
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	
	
}
