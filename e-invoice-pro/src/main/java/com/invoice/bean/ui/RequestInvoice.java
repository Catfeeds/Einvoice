package com.invoice.bean.ui;

import java.util.List;

public class RequestInvoice {
	/** 开票渠道 wx或app **/
	private String channel;
	private String userid;
	/**开票数据来源 1小票、2批发、3储值卡售卡、4供应商费用*/
	private String sheettype;
	
	/**操作人员-开票人*/
	private String admin;
	/** 收款人 **/
	private String iqpayee;
	/** 复核人 **/
	private String iqchecker;
	
	private String iqmemo;
	
	private String entid;
	private String gmfNsrsbh;
	private String gmfMc;
	private String gmfDzdh;
	private String gmfYhzh;
	private String recvPhone;
	private String recvEmail;
	//004:专票 007:普票 026:电子票
	private String iqfplxdm;
	private List<RequestInvoiceItem> requestInvoicePreviewItem;
	
	
	public String getIqmemo() {
		return iqmemo;
	}
	public void setIqmemo(String iqmemo) {
		this.iqmemo = iqmemo;
	}
	public String getIqfplxdm() {
		return iqfplxdm;
	}
	public void setIqfplxdm(String iqfplxdm) {
		this.iqfplxdm = iqfplxdm;
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
	public List<RequestInvoiceItem> getRequestInvoicePreviewItem() {
		return requestInvoicePreviewItem;
	}
	public void setRequestInvoicePreviewItem(
			List<RequestInvoiceItem> requestInvoicePreviewItem) {
		this.requestInvoicePreviewItem = requestInvoicePreviewItem;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	public String getSheettype() {
		return sheettype;
	}
	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
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
