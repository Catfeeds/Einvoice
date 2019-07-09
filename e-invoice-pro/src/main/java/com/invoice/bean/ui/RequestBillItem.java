package com.invoice.bean.ui;

import java.util.Date;

public class RequestBillItem {
	/**业务单据流水*/
	private String serialid;
	/** 企业id **/
	private String entid;
	private String sheetid;
	private String sheettype;
	private Integer rowno;
	private Date tradedate;
	/** 商品编码 **/
	private String goodsid;
	/** 商品名称 **/
	private String goodsname;
	/** 税目 **/
	private String taxitemid;
	/** 数量 **/
	private Double qty;
	/** 单位 **/
	private String unit;
	/** 单价 */
	private Double price;
	/** 发票金额（不含税） **/
	private Double amount;
	/** 税率 **/
	private Double taxrate;
	/** 税额 **/
	private Double taxfee;
	/** 价税合计金额 */
	private Double amt;
	
	private String taxpre;//是否享受税收优惠政策0 不享受，1 享受',
	private String taxprecon;//优惠政策类型
	private String zerotax;//零税率标识：空 非零税率，0：出口退税，1：免税，2：不征收，3：普通零税率',
	private String isInvoice;
	private String categoryid;
	
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getIsInvoice() {
		return isInvoice;
	}
	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
	}
	public String getTaxpre() {
		return taxpre;
	}
	public void setTaxpre(String taxpre) {
		this.taxpre = taxpre;
	}
	public String getTaxprecon() {
		return taxprecon;
	}
	public void setTaxprecon(String taxprecon) {
		this.taxprecon = taxprecon;
	}
	public String getZerotax() {
		return zerotax;
	}
	public void setZerotax(String zerotax) {
		this.zerotax = zerotax;
	}
	public String getSerialid() {
		return serialid;
	}
	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}
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
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getTaxitemid() {
		return taxitemid;
	}
	public void setTaxitemid(String taxitemid) {
		this.taxitemid = taxitemid;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}
	public Double getTaxfee() {
		return taxfee;
	}
	public void setTaxfee(Double taxfee) {
		this.taxfee = taxfee;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public Date getTradedate() {
		return tradedate;
	}
	public void setTradedate(Date tradedate) {
		this.tradedate = tradedate;
	}
	
}
