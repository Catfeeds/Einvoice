package com.invoice.bean.db;/******************************************************************************* * javaBeans * privatepara --> Privatepara  * <table explanation> * @author 2017-07-11 14:47:00 *  */	public class Privatepara implements java.io.Serializable {	//field	/** 企业ID **/	private String entid;	/** 代码 **/	private String pparaid;	/** 描述 **/	private String pparaname;	/** 参数值 **/	private String pparavalue;	/** 状态 **/	private String pparastatus;	/** 备注 **/	private String pparamemo;	//method	public String getEntid() {		return entid;	}	public void setEntid(String entid) {		this.entid = entid;	}	public String getPparaid() {		return pparaid;	}	public void setPparaid(String pparaid) {		this.pparaid = pparaid;	}	public String getPparaname() {		return pparaname;	}	public void setPparaname(String pparaname) {		this.pparaname = pparaname;	}	public String getPparavalue() {		return pparavalue;	}	public void setPparavalue(String pparavalue) {		this.pparavalue = pparavalue;	}	public String getPparastatus() {		return pparastatus;	}	public void setPparastatus(String pparastatus) {		this.pparastatus = pparastatus;	}	public String getPparamemo() {		return pparamemo;	}	public void setPparamemo(String pparamemo) {		this.pparamemo = pparamemo;	}	//return String[] filed; 	public String[] getField() {		return new String[]{"entid","pparaid","pparaname","pparavalue","pparastatus","pparamemo"};	}}