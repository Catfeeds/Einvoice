package com.invoice.bean.db;

import java.util.Date;

public class PreInvoiceAsk {
	private String entid;
	private String sheetid;
	private String sheettype;
	private String recvemail;
	private String recvphone;
	private int flag;
	private String gmftax;
	private String gmfname;
	private String gmfadd;
	private String gmfbank;
	private String gmfno;
	private String invoicelx;
	private Date createtime;
	private Date processtime;
	private String openid;
	
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

	public String getRecvemail() {
		return recvemail;
	}
	public void setRecvemail(String recvemail) {
		this.recvemail = recvemail;
	}
	public String getRecvphone() {
		return recvphone;
	}
	public void setRecvphone(String recvphone) {
		this.recvphone = recvphone;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getGmftax() {
		return gmftax;
	}
	public void setGmftax(String gmftax) {
		this.gmftax = gmftax;
	}
	public String getGmfname() {
		return gmfname;
	}
	public void setGmfname(String gmfname) {
		this.gmfname = gmfname;
	}
	public String getGmfadd() {
		return gmfadd;
	}
	public void setGmfadd(String gmfadd) {
		this.gmfadd = gmfadd;
	}
	public String getGmfbank() {
		return gmfbank;
	}
	public void setGmfbank(String gmfbank) {
		this.gmfbank = gmfbank;
	}
	public String getGmfno() {
		return gmfno;
	}
	public void setGmfno(String gmfno) {
		this.gmfno = gmfno;
	}
	public String getInvoicelx() {
		return invoicelx;
	}
	public void setInvoicelx(String invoicelx) {
		this.invoicelx = invoicelx;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getProcesstime() {
		return processtime;
	}
	public void setProcesstime(Date processtime) {
		this.processtime = processtime;
	}
	public String getSheettype() {
		return sheettype;
	}
	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
