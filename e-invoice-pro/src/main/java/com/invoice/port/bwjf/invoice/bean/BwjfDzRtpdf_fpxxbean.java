package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fpxx")
public class BwjfDzRtpdf_fpxxbean {
	
	private BwjfDzRtpdf_groupBean groupbean;

	@XmlElement(name = "group") 
	public BwjfDzRtpdf_groupBean getGroupbean() {
		return groupbean;
	}

	public void setGroupbean(BwjfDzRtpdf_groupBean groupbean) {
		this.groupbean = groupbean;
	}
	
	
}
