package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="body")
public class BwjfDzRtpdf_bodyBean {
	
	private BwjfDzRtpdf_fpxxbean fpxxbean;
	private String returncode;
	private String returnmsg;
	
	@XmlElement(name = "fpxx") 
	public BwjfDzRtpdf_fpxxbean getFpxxbean() {
		return fpxxbean;
	}
	public void setFpxxbean(BwjfDzRtpdf_fpxxbean fpxxbean) {
		this.fpxxbean = fpxxbean;
	}
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	public String getReturnmsg() {
		return returnmsg;
	}
	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}
	
	
}
