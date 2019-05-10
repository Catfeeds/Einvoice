package com.einvoice.sell.tungkong;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="business")
public class InvoiceReqDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	private String taxNo;
	private String entId;
	private String clientId;
	private String shopId;
	private String sheetId;
	
	//税号
	@XmlElement(name = "NSRSBH")
	public void setTaxNo(String taxNo) {
	    this.taxNo = taxNo;
	}

	public String getTaxNo() {
	    return taxNo;
	}
	
	//企业号
	@XmlElement(name = "QYH")
	public void setEntId(String entId) {
	    this.entId = entId;
	}

	public String getEntId() {
	    return entId;
	}
	
	//终端号
	@XmlElement(name = "ZDH")
	public void setClientId(String clientId) {
	    this.clientId = clientId;
	}

	public String getClentId() {
	    return clientId;
	}
	
	//门店号
	@XmlElement(name = "BY")
	public void setShopId(String shopId) {
	    this.shopId = shopId;
	}

	public String getShopId() {
	    return shopId;
	}
	
	//发票流水号
	@XmlElement(name = "FPQQLSH")
	public void setSheetId(String sheetId) {
	    this.sheetId = sheetId;
	}

	public String getSheetId() {
	    return sheetId;
	}
}
