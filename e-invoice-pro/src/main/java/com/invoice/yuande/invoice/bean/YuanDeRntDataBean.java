package com.invoice.yuande.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RTNDATA")
public class YuanDeRntDataBean {
	
	private YuanDeInvoiceSaleHeadBean salehead;
	
	@XmlElement(name = "REQUEST_FPKJ")  
	public YuanDeInvoiceSaleHeadBean getSalehead() {
		return salehead;
	}

	public void setSalehead(YuanDeInvoiceSaleHeadBean salehead) {
		this.salehead = salehead;
	}
	
	
}
