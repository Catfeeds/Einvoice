package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RESPONSE_COMMON_FPKJ ")
public class BwjfDzRtInvoiceBean {
	private String fpqqlsh;//            发票请求流水号
	private String jqbh;//                税控设备编号  
	private String fp_dm;//               发票代码      
	private String fp_hm;//               发票号码      
	private String kprq;//                开票日期      
	private String fp_mw;//               发票密文      
	private String jym;//                 发票校验码    
	private String ewm;//                 二维码        
	private String bz;//                  备注          
	private String returncode;//          返回代码      
	private String returnmsg;//           返回信息      
	
	@XmlElement(name = "FPQQLSH") 
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	@XmlElement(name = "JQBH") 
	public String getJqbh() {
		return jqbh;
	}
	public void setJqbh(String jqbh) {
		this.jqbh = jqbh;
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
	@XmlElement(name = "FP_MW") 
	public String getFp_mw() {
		return fp_mw;
	}
	public void setFp_mw(String fp_mw) {
		this.fp_mw = fp_mw;
	}
	@XmlElement(name = "JYM") 
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	@XmlElement(name = "EWM") 
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	@XmlElement(name = "BZ") 
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	@XmlElement(name = "RETURNCODE") 
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	@XmlElement(name = "RETURNMSG") 
	public String getReturnmsg() {
		return returnmsg;
	}
	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}
	
	
}
