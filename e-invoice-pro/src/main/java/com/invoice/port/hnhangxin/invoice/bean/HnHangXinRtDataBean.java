package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
public class HnHangXinRtDataBean {
	
	private HnHangXinRtDataDescriptionBean dataDescription;
	
	private String content;

	public HnHangXinRtDataDescriptionBean getDataDescription() {
		return dataDescription;
	}

	public void setDataDescription(HnHangXinRtDataDescriptionBean dataDescription) {
		this.dataDescription = dataDescription;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
