package com.invoice.bean.ui;

public class RequestInvoiceItem {
	protected String sheetid;
	/** 门店 **/
	protected String shopid;
	/** 收银机ID **/
	protected String syjid;
	/** 小票流水 **/
	protected String billno;
	/** 金额 **/
	protected Double je;
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getSyjid() {
		return syjid;
	}
	public void setSyjid(String syjid) {
		this.syjid = syjid;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public Double getJe() {
		return je;
	}
	public void setJe(Double je) {
		this.je = je;
	}
	public String getSheetid() {
		return sheetid;
	}
	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}
}
