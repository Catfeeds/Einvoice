package com.invoice.bean.db;

public class BillTax implements java.io.Serializable {
	
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -7945986475209403983L;
	private String entid;
	 private String  taxitemid;
	 private String  taxitemname;
	 private float  taxRate;
	 private String goodsName;
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getTaxitemid() {
		return taxitemid;
	}
	public void setTaxitemid(String taxitemid) {
		this.taxitemid = taxitemid;
	}
	public String getTaxitemname() {
		return taxitemname;
	}
	public void setTaxitemname(String taxitemname) {
		this.taxitemname = taxitemname;
	}
	public float getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(float taxRate) {
		this.taxRate = taxRate;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	 
	 
}
