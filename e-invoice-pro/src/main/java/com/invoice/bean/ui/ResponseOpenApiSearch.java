package com.invoice.bean.ui;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResponseOpenApiSearch {
	private String sheetid;
	private String sheettype;
	private String operation;
	private String shopid;
	private String shopname;
	private Date sdate;
	private String editor;
	private double amt;
	private String mtaxno;
	private String mname;
	private String maddr;
	private String mbank;
	private String gno;
	private String gtaxno;
	private String gname;
	private String gaddr;
	private String gbank;
	private String invoicecode;
	private String invoiceno;
	private String invoicedate;
	private String invoicepdf;
	private String remark;
	private String invoiceid;
	private List<Map<String,Object>> sheetdetail;
	
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
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public String getMtaxno() {
		return mtaxno;
	}
	public void setMtaxno(String mtaxno) {
		this.mtaxno = mtaxno;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getMaddr() {
		return maddr;
	}
	public void setMaddr(String maddr) {
		this.maddr = maddr;
	}
	public String getMbank() {
		return mbank;
	}
	public void setMbank(String mbank) {
		this.mbank = mbank;
	}
	public String getGno() {
		return gno;
	}
	public void setGno(String gno) {
		this.gno = gno;
	}
	public String getGtaxno() {
		return gtaxno;
	}
	public void setGtaxno(String gtaxno) {
		this.gtaxno = gtaxno;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getGaddr() {
		return gaddr;
	}
	public void setGaddr(String gaddr) {
		this.gaddr = gaddr;
	}
	public String getGbank() {
		return gbank;
	}
	public void setGbank(String gbank) {
		this.gbank = gbank;
	}
	public String getInvoicecode() {
		return invoicecode;
	}
	public void setInvoicecode(String invoicecode) {
		this.invoicecode = invoicecode;
	}
	public String getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}
	public String getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(String invoicedate) {
		this.invoicedate = invoicedate;
	}
	public String getInvoicepdf() {
		return invoicepdf;
	}
	public void setInvoicepdf(String invoicepdf) {
		this.invoicepdf = invoicepdf;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInvoiceid() {
		return invoiceid;
	}
	public void setInvoiceid(String invoiceid) {
		this.invoiceid = invoiceid;
	}
	public List<Map<String, Object>> getSheetdetail() {
		return sheetdetail;
	}
	public void setSheetdetail(List<Map<String, Object>> sheetdetail) {
		this.sheetdetail = sheetdetail;
	}
}
