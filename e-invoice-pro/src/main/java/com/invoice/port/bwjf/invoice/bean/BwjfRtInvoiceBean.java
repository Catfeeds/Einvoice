package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//开票返回的发票内容
@XmlRootElement(name="returndata")
public class BwjfRtInvoiceBean {
	
	private String fpdm;//	发票代码
	private String fphm;//	发票号码
	private String kprq;//	开票日期
	private String skm;//	税控码
	private String 	jym;//	校验码
	private String ewm;//	二维码
	
	private BwjfRtKpxxBean kpxx;
	
	
	@XmlElement(name = "kpxx")  
	public BwjfRtKpxxBean getKpxx() {
		return kpxx;
	}
	public void setKpxx(BwjfRtKpxxBean kpxx) {
		this.kpxx = kpxx;
	}
	public String getFpdm() {
		return fpdm;
	}
	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
	}
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	public String getSkm() {
		return skm;
	}
	public void setSkm(String skm) {
		this.skm = skm;
	}
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	
	

}
