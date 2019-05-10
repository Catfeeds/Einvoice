package com.invoice.bean.ui;

public class RequestInvoiceDetail {
	/** 开票渠道 wx或app **/
	private String channel;
	private String userid;
	/**开票数据来源 1小票、2批发、3储值卡售卡、4供应商费用*/
	private String sheettype;
	private String entid;
	
	//发票类型004:专票 007:普票 026:电子票
	private String iqfplxdm;
	private String recvPhone;
	private String recvEmail;
	
	private Long fpdm;
	private Long fphm;

	/*明细部分*/
	private String serialid;
	private String sheetid;
	private int rowno;
	/*明细部分*/
	
	public String getIqfplxdm() {
		return iqfplxdm;
	}
	public void setIqfplxdm(String iqfplxdm) {
		this.iqfplxdm = iqfplxdm;
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
	public String getSerialid() {
		return serialid;
	}
	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}
	public String getSheetid() {
		return sheetid;
	}
	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}
	public int getRowno() {
		return rowno;
	}
	public void setRowno(int rowno) {
		this.rowno = rowno;
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
}
