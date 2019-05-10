package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="returnStateInfo")
public class HnHangXinRtStateInfoBean {
	 private String  returnCode;//<returnCode>0000</returnCode>
	 private String  returnMessage;//<returnMessage>5Lia5Yqh5aSE55CG5oiQ5Yqf</returnMessage>
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
