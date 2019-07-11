package com.invoice.port.nbbanji.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//阪急项目
@XmlRootElement(name ="business")
public class OrderQueryReceiveBean {
	private String taxNo;//税号
	private String entId;//企业
	private String clientId;//终端
	private String shopId;//门店
	private String sheetId;//小票号
	private String retFlag;//退货标志　空：全部；0：非退货；1:退货
	private String dpType;//纸票/电票
	private String ddRQq;
	private String ddRQz;
	
	//查询日期段
	public String getDdRQq() {
		return ddRQq;
	}

	@XmlElement(name = "DDRQQ")
	public void setDdRQq(String ddRQq) {
		this.ddRQq = ddRQq;
	}

	public String getDdRQz() {
		return ddRQz;
	}
	
	@XmlElement(name = "DDRQZ")
	public void setDdRQz(String ddRQz) {
		this.ddRQz = ddRQz;
	}
	
	//退货标志
	public String getRetFlag() {
		return retFlag;
	}
	
	@XmlElement(name = "THBZ")
	public void setRetFlag(String retFlag) {
		this.retFlag = retFlag;
	}

	public String getDpType() {
		return dpType;
	}
	
	@XmlElement(name = "DPZP")
	public void setDpType(String dpType) {
		this.dpType = dpType;
	}
	
	//税号
	public String getTaxNo() {
	    return taxNo;
	}
	
	@XmlElement(name = "NSRSBH")
	public void setTaxNo(String taxNo) {
	    this.taxNo = taxNo;
	}
	
	//企业号
	public String getEntId() {
	    return entId;
	}
	
	@XmlElement(name = "QYH")
	public void setEntId(String entId) {
	    this.entId = entId;
	}
	
	//终端号
	public String getClientId() {
		return clientId;
	}
	
	@XmlElement(name = "ZDH")
	public void setClientId(String clientId) {
	    this.clientId = clientId;
	}

	//门店号
	public String getShopId() {
	    return shopId;
	}
	
	@XmlElement(name = "BY")
	public void setShopId(String shopId) {
	    this.shopId = shopId;
	}

	//发票流水号
	public String getSheetId() {
	    return sheetId;
	}

	@XmlElement(name = "FPQQLSH")
	public void setSheetId(String sheetId) {
	    this.sheetId = sheetId;
	}
	
}
