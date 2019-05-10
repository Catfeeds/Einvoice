package com.invoice.yuande.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RESULT")
public class YuanDeResultBean {
	
	private String requestid;
	private String rntcode;
	private String rntmsg;
	
	private YuanDeRntDataBean rntdata;
	
	
	@XmlElement(name = "RTNDATA")  
	public YuanDeRntDataBean getRntdata() {
		return rntdata;
	}

	public void setRntdata(YuanDeRntDataBean rntdata) {
		this.rntdata = rntdata;
	}

	@XmlElement(name = "REQUESTID")  
	public String getRequestid() {
		return requestid;
	}
	
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	
	@XmlElement(name = "RTNCODE")  
	public String getRntcode() {
		return rntcode;
	}
	public void setRntcode(String rntcode) {
		this.rntcode = rntcode;
	}
	
	@XmlElement(name = "RTNMSG")  
	public String getRntmsg() {
		return rntmsg;
	}
	public void setRntmsg(String rntmsg) {
		this.rntmsg = rntmsg;
	}

	
	
}
