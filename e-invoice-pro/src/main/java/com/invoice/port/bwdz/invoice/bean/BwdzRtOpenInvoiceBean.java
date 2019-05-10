package com.invoice.port.bwdz.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RESPONSE")
public class BwdzRtOpenInvoiceBean {
	private String fpqqlsh;//发票请求流水号
	private String fp_dm;//发票代码
	private String fp_hm;//发票号码
	private String jym;//校验码
	private String kprq;//开票日期
	private String pdf_url;//PDF 下载地址
	private String sp_url;//收票地址
	
	 @XmlElement(name = "FPQQLSH")  
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
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
	 @XmlElement(name = "JYM")  
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	 @XmlElement(name = "KPRQ")  
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	 @XmlElement(name = "PDF_URL")  
	public String getPdf_url() {
		return pdf_url;
	}
	public void setPdf_url(String pdf_url) {
		this.pdf_url = pdf_url;
	}
	 @XmlElement(name = "SP_URL")  
	public String getSp_url() {
		return sp_url;
	}
	public void setSp_url(String sp_url) {
		this.sp_url = sp_url;
	}
	
	
}
