package com.invoice.port.bwdz.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="interface")
public class BwzdInterfaceBean {
	
	
	private BwdzRtGlobalInfoBean globalInfo;
	
	private BwdzRtStateInfoBean returnStateInfo;
	
	private BwdzRtDataBean data;

	 @XmlElement(name = "globalInfo")  
	public BwdzRtGlobalInfoBean getGlobalInfo() {
		return globalInfo;
	}

	public void setGlobalInfo(BwdzRtGlobalInfoBean globalInfo) {
		this.globalInfo = globalInfo;
	}
	 @XmlElement(name = "returnStateInfo")  
	public BwdzRtStateInfoBean getReturnStateInfo() {
		return returnStateInfo;
	}

	public void setReturnStateInfo(BwdzRtStateInfoBean returnStateInfo) {
		this.returnStateInfo = returnStateInfo;
	}
	 @XmlElement(name = "Data")  
	public BwdzRtDataBean getData() {
		return data;
	}

	public void setData(BwdzRtDataBean data) {
		this.data = data;
	}
	
	
}
