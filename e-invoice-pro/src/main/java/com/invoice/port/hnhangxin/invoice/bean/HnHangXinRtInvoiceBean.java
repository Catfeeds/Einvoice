package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="REQUEST_FPKJXX_FPJGXX_NEW")
public class HnHangXinRtInvoiceBean {
	   private String fpqqlsh;// <FPQQLSH>Q0180725092237fcc36</FPQQLSH>
	   private String ddh;// <DDH/>
	   private String kplsh;// <KPLSH>15000352888859804874</KPLSH>
	   private String fwm;// <FWM>72938067322670941480</FWM>
	   private String ewm;// <EWM></EWM>
	   private String fpzl_dm;// <FPZL_DM>51</FPZL_DM>
	   private String fp_dm;// <FP_DM>150003528888</FP_DM>
	   private String fp_hm;// <FP_HM>59804874</FP_HM>
	   private String kprq;// <KPRQ>2018-07-25 09:23:48</KPRQ>
	   private String kplx;// <KPLX>1</KPLX>
	   private String hjbhsje;// <HJBHSJE></HJBHSJE>
	   private String kphjse;// <KPHJSE></KPHJSE>
	   private String pdf_file;// <PDF_FILE></PDF_FILE>
	   private String pdf_url;// <PDF_URL></PDF_URL>
	   private String czdm;// <CZDM>10</CZDM>
	   private String returncode;// <RETURNCODE>0000</RETURNCODE>
	   private String returnmessage;// <RETURNMESSAGE></RETURNMESSAGE>
	   
	   private String aa;
	   
	   @XmlAttribute(name = "class")
		public String getAa() {
			return "REQUEST_FPKJXX_FPJGXX_NEW";
		}

		public void setAa(String aa) {
			this.aa = aa;
		}
	@XmlElement(name = "FPQQLSH")    
	public String getFpqqlsh() {
		return fpqqlsh;
	}

	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	 @XmlElement(name = "DDH")  
	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	 @XmlElement(name = "KPLSH")  
	public String getKplsh() {
		return kplsh;
	}

	public void setKplsh(String kplsh) {
		this.kplsh = kplsh;
	}
	 @XmlElement(name = "FWM")  
	public String getFwm() {
		return fwm;
	}

	public void setFwm(String fwm) {
		this.fwm = fwm;
	}
	 @XmlElement(name = "EWM")  
	public String getEwm() {
		return ewm;
	}

	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	 @XmlElement(name = "FPZL_DM")  
	public String getFpzl_dm() {
		return fpzl_dm;
	}

	public void setFpzl_dm(String fpzl_dm) {
		this.fpzl_dm = fpzl_dm;
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
	 @XmlElement(name = "KPRQ")  
	public String getKprq() {
		return kprq;
	}

	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	 @XmlElement(name = "KPLX")  
	public String getKplx() {
		return kplx;
	}

	public void setKplx(String kplx) {
		this.kplx = kplx;
	}
	 @XmlElement(name = "HJBHSJE")  
	public String getHjbhsje() {
		return hjbhsje;
	}

	public void setHjbhsje(String hjbhsje) {
		this.hjbhsje = hjbhsje;
	}
	 @XmlElement(name = "KPHJSE")  
	public String getKphjse() {
		return kphjse;
	}

	public void setKphjse(String kphjse) {
		this.kphjse = kphjse;
	}
	 @XmlElement(name = "PDF_FILE")  
	public String getPdf_file() {
		return pdf_file;
	}

	public void setPdf_file(String pdf_file) {
		this.pdf_file = pdf_file;
	}
	 @XmlElement(name = "PDF_URL")  
	public String getPdf_url() {
		return pdf_url;
	}

	public void setPdf_url(String pdf_url) {
		this.pdf_url = pdf_url;
	}
	 @XmlElement(name = "CZDM")  
	public String getCzdm() {
		return czdm;
	}

	public void setCzdm(String czdm) {
		this.czdm = czdm;
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


	   
	   
}
