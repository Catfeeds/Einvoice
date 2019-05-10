package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="group")
public class BwjfDzRtpdf_groupBean {
	private String url;
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
