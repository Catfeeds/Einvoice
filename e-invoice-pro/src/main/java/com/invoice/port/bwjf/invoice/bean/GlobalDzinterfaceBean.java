package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="business")

public class GlobalDzinterfaceBean {
	
	private BwjfDzRtInvoiceBean rtbean;
	
	private BwjfDzRtpdf_bodyBean bodybean;
	
	private BwjfDzRtInvoiceBean rterrorbean;
	
	 @XmlElement(name = "RESPONSE_COMMON_FPKJ") 
	public BwjfDzRtInvoiceBean getRtbean() {
		return rtbean;
	}

	public void setRtbean(BwjfDzRtInvoiceBean rtbean) {
		this.rtbean = rtbean;
	}
	
	
	 @XmlElement(name = "RESPONSE_COMMON_") 
	public BwjfDzRtInvoiceBean getRterrorbean() {
		return rterrorbean;
	}

	public void setRterrorbean(BwjfDzRtInvoiceBean rterrorbean) {
		this.rterrorbean = rterrorbean;
	}

	public BwjfDzRtpdf_bodyBean getBodybean() {
		return bodybean;
	}
	@XmlElement(name = "body") 
	public void setBodybean(BwjfDzRtpdf_bodyBean bodybean) {
		this.bodybean = bodybean;
	}
	
	
	
}
