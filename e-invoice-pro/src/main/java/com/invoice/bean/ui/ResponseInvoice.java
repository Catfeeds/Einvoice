package com.invoice.bean.ui;

import java.util.List;

public class ResponseInvoice {
	private String channel;
	private String entid;
	private String sheettype;
	
	/**申请人员ID*/
	private String userid;
	
	/**操作人员-开票人*/
	private String admin;
	/** 收款人 **/
	private String iqpayee;
	/** 复核人 **/
	private String iqchecker;
	
	private String xsfNsrsbh;
	private String xsfMc;
	private String xsfDzdh;
	private String xsfYhzh;
	
	/**开票点*/
	private String kpd;

	/**卷票纸张*/
	private String jpzz;
	
	private Long fpdm;
	private Long fphm;
	
	private String gmfNsrsbh;
	private String gmfMc;
	private String gmfDzdh;
	private String gmfYhzh;
	
	/**总金额*/
	private Double amount;
	/**总金额大写*/
	private String amountDX;
	/**总税额*/
	private Double taxAmount;
	
	/**顾客接收电话*/
	private String recvPhone;
	private String recvEmail;
	
	/**原发票号，红冲票用*/
	private String yfpdm;
	private String yfphm;
	//004:专票 007:普票 026:电子票
	private String iqfplxdm;
	
	private List<ResponseInvoiceItem> invoicePreviewItem;
	
	private List<ResponseBillInfo> billInfoList;

	private String iqmemo;
	
	
	
	public String getJpzz() {
		return jpzz;
	}

	public void setJpzz(String jpzz) {
		this.jpzz = jpzz;
	}

	public String getIqmemo() {
		return iqmemo;
	}

	public void setIqmemo(String iqmemo) {
		this.iqmemo = iqmemo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getEntid() {
		return entid;
	}

	public void setEntid(String entid) {
		this.entid = entid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getXsfNsrsbh() {
		return xsfNsrsbh;
	}

	public void setXsfNsrsbh(String xsfNsrsbh) {
		this.xsfNsrsbh = xsfNsrsbh;
	}

	public String getXsfMc() {
		return xsfMc;
	}

	public void setXsfMc(String xsfMc) {
		this.xsfMc = xsfMc;
	}

	public String getXsfDzdh() {
		return xsfDzdh;
	}

	public void setXsfDzdh(String xsfDzdh) {
		this.xsfDzdh = xsfDzdh;
	}

	public String getXsfYhzh() {
		return xsfYhzh;
	}

	public void setXsfYhzh(String xsfYhzh) {
		this.xsfYhzh = xsfYhzh;
	}

	public String getGmfNsrsbh() {
		return gmfNsrsbh;
	}

	public void setGmfNsrsbh(String gmfNsrsbh) {
		this.gmfNsrsbh = gmfNsrsbh;
	}

	public String getGmfMc() {
		return gmfMc;
	}

	public void setGmfMc(String gmfMc) {
		this.gmfMc = gmfMc;
	}

	public String getGmfDzdh() {
		return gmfDzdh;
	}

	public void setGmfDzdh(String gmfDzdh) {
		this.gmfDzdh = gmfDzdh;
	}

	public String getGmfYhzh() {
		return gmfYhzh;
	}

	public void setGmfYhzh(String gmfYhzh) {
		this.gmfYhzh = gmfYhzh;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAmountDX() {
		return amountDX;
	}

	public void setAmountDX(String amountDX) {
		this.amountDX = amountDX;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getRecvPhone() {
		return recvPhone;
	}

	public void setRecvPhone(String recvPhone) {
		this.recvPhone = recvPhone;
	}

	public String getRecvEmail() {
		return recvEmail;
	}

	public void setRecvEmail(String recvEmail) {
		this.recvEmail = recvEmail;
	}

	public List<ResponseInvoiceItem> getInvoicePreviewItem() {
		return invoicePreviewItem;
	}

	public void setInvoicePreviewItem(List<ResponseInvoiceItem> invoicePreviewItem) {
		this.invoicePreviewItem = invoicePreviewItem;
	}

	public List<ResponseBillInfo> getBillInfoList() {
		return billInfoList;
	}

	public void setBillInfoList(List<ResponseBillInfo> billInfoList) {
		this.billInfoList = billInfoList;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getYfpdm() {
		return yfpdm;
	}

	public void setYfpdm(String yfpdm) {
		this.yfpdm = yfpdm;
	}

	public String getYfphm() {
		return yfphm;
	}

	public void setYfphm(String yfphm) {
		this.yfphm = yfphm;
	}

	public String getKpd() {
		return kpd;
	}

	public void setKpd(String kpd) {
		this.kpd = kpd;
	}

	public String getSheettype() {
		return sheettype;
	}

	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}

	public String getIqfplxdm() {
		return iqfplxdm;
	}

	public void setIqfplxdm(String iqfplxdm) {
		this.iqfplxdm = iqfplxdm;
	}

	public Long getFpdm() {
		return fpdm;
	}

	public void setFpdm(Long fpdm) {
		this.fpdm = fpdm;
	}

	public Long getFphm() {
		return fphm;
	}

	public void setFphm(Long fphm) {
		this.fphm = fphm;
	}

	public String getIqpayee() {
		return iqpayee;
	}

	public void setIqpayee(String iqpayee) {
		this.iqpayee = iqpayee;
	}

	public String getIqchecker() {
		return iqchecker;
	}

	public void setIqchecker(String iqchecker) {
		this.iqchecker = iqchecker;
	}
	
}
