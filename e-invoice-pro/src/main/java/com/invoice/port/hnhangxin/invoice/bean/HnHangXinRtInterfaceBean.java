package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="interface")
public class HnHangXinRtInterfaceBean {
		
	private HnHangXinRtGlobalInfoBean globalInfo;
	
	private HnHangXinRtStateInfoBean returnStateInfo;
	
	private HnHangXinRtDataBean data;
	
	 @XmlElement(name = "globalInfo")  
	public HnHangXinRtGlobalInfoBean getGlobalInfo() {
		return globalInfo;
	}

	public void setGlobalInfo(HnHangXinRtGlobalInfoBean globalInfo) {
		this.globalInfo = globalInfo;
	}
	 @XmlElement(name = "returnStateInfo")  
	public HnHangXinRtStateInfoBean getReturnStateInfo() {
		return returnStateInfo;
	}

	public void setReturnStateInfo(HnHangXinRtStateInfoBean returnStateInfo) {
		this.returnStateInfo = returnStateInfo;
	}
	 @XmlElement(name = "Data")  
	public HnHangXinRtDataBean getData() {
		return data;
	}

	public void setData(HnHangXinRtDataBean data) {
		this.data = data;
	}
	
	
	
 
}
