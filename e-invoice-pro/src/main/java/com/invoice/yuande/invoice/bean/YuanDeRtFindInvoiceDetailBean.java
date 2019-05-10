package com.invoice.yuande.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPXX")
public class YuanDeRtFindInvoiceDetailBean {
	
	/*合计不含税金额*/
	private double hjbhsje;
	/*开票日期*/
	private String kprq;
	/*发票代码*/
	private String fp_dm;
	/*发票号码*/
	private String fp_hm;
	
	/*单据状态 1保存  2删除  3.开票  4.作废*/
	private String status;
	
	private String pdf_url;// PDF_URL
	/*销售清单标识*/
	private String xhqdbz;
	/*合计税额*/
	private double hjse;
	
	@XmlElement(name = "HJBHSJE")  
	public double getHjbhsje() {
		return hjbhsje;
	}
	public void setHjbhsje(double hjbhsje) {
		this.hjbhsje = hjbhsje;
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
	@XmlElement(name = "STATUS")  
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@XmlElement(name = "PDF_URL")  
	public String getPdf_url() {
		return pdf_url;
	}
	public void setPdf_url(String pdf_url) {
		this.pdf_url = pdf_url;
	}
	@XmlElement(name = "XHQDBZ")  
	public String getXhqdbz() {
		return xhqdbz;
	}
	public void setXhqdbz(String xhqdbz) {
		this.xhqdbz = xhqdbz;
	}
	@XmlElement(name = "HJSE")  
	public double getHjse() {
		return hjse;
	}
	public void setHjse(double hjse) {
		this.hjse = hjse;
	}
	
	
}
