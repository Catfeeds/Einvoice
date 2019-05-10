package com.invoice.port.nbbwjf.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="interface")
public class NbBwjfInterfaceBean {
	
	
	private NbBwjfRtGlobalInfoBean globalInfo;
	
	private NbBwjfRtStateInfoBean returnStateInfo;
	
	private NbBwjfRtDataBean data;

	 @XmlElement(name = "globalInfo")  
	public NbBwjfRtGlobalInfoBean getGlobalInfo() {
		return globalInfo;
	}

	public void setGlobalInfo(NbBwjfRtGlobalInfoBean globalInfo) {
		this.globalInfo = globalInfo;
	}
	 @XmlElement(name = "returnStateInfo")  
	public NbBwjfRtStateInfoBean getReturnStateInfo() {
		return returnStateInfo;
	}

	public void setReturnStateInfo(NbBwjfRtStateInfoBean returnStateInfo) {
		this.returnStateInfo = returnStateInfo;
	}
	 @XmlElement(name = "Data")  
	public NbBwjfRtDataBean getData() {
		return data;
	}

	public void setData(NbBwjfRtDataBean data) {
		this.data = data;
	}
	
	
}
