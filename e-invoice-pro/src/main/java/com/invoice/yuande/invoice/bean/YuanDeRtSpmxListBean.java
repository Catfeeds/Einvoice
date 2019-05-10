package com.invoice.yuande.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SPMXS")
public class YuanDeRtSpmxListBean {
	
	private List<YuanDeRtSpmxBean> details;
	
	@XmlElement(name = "SPMX")  
	public List<YuanDeRtSpmxBean> getDetails() {
		return details;
	}

	public void setDetails(List<YuanDeRtSpmxBean> details) {
		this.details = details;
	}
	
	
}
