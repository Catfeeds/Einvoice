package com.invoice.port.nbbwjf.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="returnStateInfo")
public class NbBwjfRtStateInfoBean {
	private String returnCode;
	private String returnMessage;
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	
	
}
