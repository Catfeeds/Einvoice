package com.invoice.port.nbbwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
public class NbBwjfRtDataBean {
	

	private NbBwjfRtDataDescriptionBean dataDescription;
	
	private String content;
	
	@XmlElement(name = "dataDescription") 
	public NbBwjfRtDataDescriptionBean getDataDescription() {
		return dataDescription;
	}

	public void setDataDescription(NbBwjfRtDataDescriptionBean dataDescription) {
		this.dataDescription = dataDescription;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
