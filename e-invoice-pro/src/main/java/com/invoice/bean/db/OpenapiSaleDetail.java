package com.invoice.bean.db;

import java.util.Date;

public class OpenapiSaleDetail implements java.io.Serializable {
	private String entid;
	private String sheetid;
	private String sheettype;
	
	
	private Integer rowno;
	
	private String categoryid;
	/** 商品编码 **/
	private String itemid;
	/** 商品名称 **/
	private String itemname;
	private String spec;
	
	/** 数量 **/
	private Double qty;
	/** 单位 **/
	private String unit;
	/** 单价 */
	private Double amt;
	private Double taxrate;
	
	/**
	 * 指定税目
	 */
	private String taxitemid;
	
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getSheetid() {
		return sheetid;
	}
	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}
	public String getSheettype() {
		return sheettype;
	}
	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}
	public Integer getRowno() {
		return rowno;
	}
	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getItemid() {
		return itemid;
	}
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public Double getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}
	public String getTaxitemid() {
		return taxitemid;
	}
	public void setTaxitemid(String taxitemid) {
		this.taxitemid = taxitemid;
	}

}