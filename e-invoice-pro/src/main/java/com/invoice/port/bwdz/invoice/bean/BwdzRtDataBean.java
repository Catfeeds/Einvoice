package com.invoice.port.bwdz.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
public class BwdzRtDataBean {
	

	private BwdzRtDataDescriptionBean dataDescription;
	
	private String content;
	
	@XmlElement(name = "dataDescription") 
	public BwdzRtDataDescriptionBean getDataDescription() {
		return dataDescription;
	}

	public void setDataDescription(BwdzRtDataDescriptionBean dataDescription) {
		this.dataDescription = dataDescription;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
