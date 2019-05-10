package com.invoice.bean.ui;

import java.util.List;

public class RequestBillInfo {
	/** 企业id **/
	protected String entid;
	/**用户ID 微信端为用户ID，网页端为登录账号*/
	protected String userID;
	/** 开票渠道 wx或app **/
	protected String channel;
	
	/** 小票唯一码字符串 **/
	protected String ticketQC;
	
	protected String sheetid;
	/** 单据类型 **/
	protected String sheettype; 
	/** 门店 **/
	protected String shopid;
	/** 收银机ID **/
	protected String syjid;
	/** 小票流水 **/
	protected String billno;
	/** 金额 **/
	protected Double je;
	
	protected String remark;
	
	protected Integer flag;
	protected String iqseqno;
	
	protected int rowno;
	
	protected String fplxdm;
	
	
	/** 取单时的动作，当值为update时，表示更新对应的单据信息*/
	protected String action;
	
	protected String gmfno;
	
	protected Integer iqstatus;
	
	protected List<RequestBillItem> requestBillItem;
	
	
	
	public String getFplxdm() {
		return fplxdm;
	}
	public void setFplxdm(String fplxdm) {
		this.fplxdm = fplxdm;
	}
	public String getBillno() {
		return billno;
	}
	public String getChannel() {
		return channel;
	}
	public String getEntid() {
		return entid;
	}
	public String getShopid() {
		return shopid;
	}
	public String getSyjid() {
		return syjid;
	}
	public String getTicketQC() {
		return ticketQC;
	}
	public String getUserID() {
		return userID;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public void setSyjid(String syjid) {
		this.syjid = syjid;
	}
	public void setTicketQC(String ticketQC) {
		this.ticketQC = ticketQC;
	}
	public void setUserID(String userID) {
		this.userID = userID;
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
	public String getSheettype() {
		return sheettype;
	}
	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<RequestBillItem> getRequestBillItem() {
		return requestBillItem;
	}
	public void setRequestBillItem(List<RequestBillItem> requestBillItem) {
		this.requestBillItem = requestBillItem;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getRowno() {
		return rowno;
	}
	public void setRowno(int rowno) {
		this.rowno = rowno;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getIqseqno() {
		return iqseqno;
	}
	public void setIqseqno(String iqseqno) {
		this.iqseqno = iqseqno;
	}
	public String getGmfno() {
		return gmfno;
	}
	public void setGmfno(String gmfno) {
		this.gmfno = gmfno;
	}
	public Integer getIqstatus() {
		return iqstatus;
	}
	public void setIqstatus(Integer iqstatus) {
		this.iqstatus = iqstatus;
	}
}
