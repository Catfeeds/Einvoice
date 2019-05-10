package com.invoice.port.szyipiaoyun.invoice.bean;

public class YiPiaoYunFindRequestBean {
	private String orderNo;
	private String serialNo;
	private String invoiceCode;
	private String invoiceNum;
	private String invoiceNumEnd;
	private String invoiceType;
	private String status;
	private String buyerMobile;
	private String invoiceDate;
	private String invoiceDateEnd;
	private String drawer;
	private String depName;
	private String rp;
	private String page;
	
	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getSerialNo() {
		return serialNo;
	}
	
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getInvoiceCode() {
		return invoiceCode;
	}
	
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	
	public String getInvoiceNum() {
		return invoiceNum;
	}
	
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	
	public String getInvoiceNumEnd() {
		return invoiceNumEnd;
	}
	
	public void setInvoiceNumEnd(String invoiceNumEnd) {
		this.invoiceNumEnd = invoiceNumEnd;
	}
	
	public String getInvoiceType() {
		return invoiceType;
	}
	
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBuyerMobile() {
		return buyerMobile;
	}
	
	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}
	
	public String getInvoiceDate() {
		return invoiceDate;
	}
	
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public String getInvoiceDateEnd() {
		return invoiceDateEnd;
	}
	
	public void setInvoiceDateEnd(String invoiceDateEnd) {
		this.invoiceDateEnd = invoiceDateEnd;
	}
	
	public String getDrawer() {
		return drawer;
	}
	
	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}
	
	public String getDepName() {
		return depName;
	}
	
	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	public String getRp() {
		return rp;
	}
	
	public void setRp(String rp) {
		this.rp = rp;
	}
	
	public String getPage() {
		return page;
	}
	
	public void setPage(String page) {
		this.page = page;
	}
	
	public String getRequestText()
	{
		String result = "";
		
		if (getOrderNo() != null && getOrderNo().trim().length() > 0)
			result = "orderNo=" + getOrderNo().trim();
		
		if (getSerialNo() != null && getSerialNo().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "serialNo=" + getSerialNo();
			
		if (getInvoiceCode() != null && getInvoiceCode().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "invoiceCode=" + getInvoiceCode();
		
		if (getInvoiceNum() != null && getInvoiceNum().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "invoiceNum=" + getInvoiceNum();
		
		if (getInvoiceNumEnd() != null && getInvoiceNumEnd().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "invoiceNumEnd=" + getInvoiceNumEnd();
		
		if (getInvoiceType() != null && getInvoiceType().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "invoiceType=" + getInvoiceType();
		
		if (getStatus() != null && getStatus().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "status=" + getStatus();
		
		if (getInvoiceDate() != null && getInvoiceDate().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "invoiceDate=" + getInvoiceDate();
		
		if (getInvoiceDateEnd() != null && getInvoiceDateEnd().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "invoiceDateEnd=" + getInvoiceDateEnd();
		
		if (getRp() != null && getRp().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "rp=" + getRp();
		
		if (getPage() != null && getPage().trim().length() > 0)
			result = result + (result.length()>0?"&":"") + "page=" + getPage();
		
		return result;		
	}
}
