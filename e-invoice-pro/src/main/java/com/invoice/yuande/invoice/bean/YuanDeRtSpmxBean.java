package com.invoice.yuande.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SPMX")
public class YuanDeRtSpmxBean {
	private int zxbm;
	
	@XmlElement(name = "ZXBM")  
	public int getZxbm() {
		return zxbm;
	}

	public void setZxbm(int zxbm) {
		this.zxbm = zxbm;
	}
	
	
}
