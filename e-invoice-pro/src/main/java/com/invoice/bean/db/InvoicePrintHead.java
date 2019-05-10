package com.invoice.bean.db;

import java.util.List;

public class InvoicePrintHead {
	private String seqno;
	private String ewm;
	private String itfskpbh;
	private String fpdm;
	private String fphm;
	private String fprq;
	private String fpjym;
	private String gmfname;
	private String gmftax;
	private String gmfaddr;
	private String gmfbank;
	private String fpskm;
	private String sumamount;
	private String sumtaxfee;
	private String amtdx;
	private String amt;
	private String xsfname;
	private String xsftax;
	private String xsfaddr;
	private String xsfbank;
	private String note;
	private String weixinskr;
	private String weixinfhr;
	private String weixinkpr;
	
	private List<InvoicePrintDetail> details;
	
	private Taxprintinfo taxprintinfo;
	
	
	
	public Taxprintinfo getTaxprintinfo() {
		return taxprintinfo;
	}

	public void setTaxprintinfo(Taxprintinfo taxprintinfo) {
		this.taxprintinfo = taxprintinfo;
	}
	
	
	
	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getEwm() {
		return ewm;
	}

	public void setEwm(String ewm) {
		this.ewm = ewm;
	}

	public String getItfskpbh() {
		return itfskpbh;
	}

	public void setItfskpbh(String itfskpbh) {
		this.itfskpbh = itfskpbh;
	}

	public String getFpdm() {
		return fpdm;
	}

	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}

	public String getFphm() {
		return fphm;
	}

	public void setFphm(String fphm) {
		this.fphm = fphm;
	}

	public String getFprq() {
		return fprq;
	}

	public void setFprq(String fprq) {
		this.fprq = fprq;
	}

	public String getFpjym() {
		return fpjym;
	}

	public void setFpjym(String fpjym) {
		this.fpjym = fpjym;
	}

	public String getGmfname() {
		return gmfname;
	}

	public void setGmfname(String gmfname) {
		this.gmfname = gmfname;
	}

	public String getGmftax() {
		return gmftax;
	}

	public void setGmftax(String gmftax) {
		this.gmftax = gmftax;
	}

	public String getGmfaddr() {
		return gmfaddr;
	}

	public void setGmfaddr(String gmfaddr) {
		this.gmfaddr = gmfaddr;
	}

	public String getGmfbank() {
		return gmfbank;
	}

	public void setGmfbank(String gmfbank) {
		this.gmfbank = gmfbank;
	}

	public String getFpskm() {
		return fpskm;
	}

	public void setFpskm(String fpskm) {
		this.fpskm = fpskm;
	}

	public String getSumamount() {
		return sumamount;
	}

	public void setSumamount(String sumamount) {
		this.sumamount = sumamount;
	}

	public String getSumtaxfee() {
		return sumtaxfee;
	}

	public void setSumtaxfee(String sumtaxfee) {
		this.sumtaxfee = sumtaxfee;
	}

	public String getAmtdx() {
		return amtdx;
	}

	public void setAmtdx(String amtdx) {
		this.amtdx = amtdx;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getXsfname() {
		return xsfname;
	}

	public void setXsfname(String xsfname) {
		this.xsfname = xsfname;
	}

	public String getXsftax() {
		return xsftax;
	}

	public void setXsftax(String xsftax) {
		this.xsftax = xsftax;
	}

	public String getXsfaddr() {
		return xsfaddr;
	}

	public void setXsfaddr(String xsfaddr) {
		this.xsfaddr = xsfaddr;
	}

	public String getXsfbank() {
		return xsfbank;
	}

	public void setXsfbank(String xsfbank) {
		this.xsfbank = xsfbank;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getWeixinskr() {
		return weixinskr;
	}

	public void setWeixinskr(String weixinskr) {
		this.weixinskr = weixinskr;
	}

	public String getWeixinfhr() {
		return weixinfhr;
	}

	public void setWeixinfhr(String weixinfhr) {
		this.weixinfhr = weixinfhr;
	}

	public String getWeixinkpr() {
		return weixinkpr;
	}

	public void setWeixinkpr(String weixinkpr) {
		this.weixinkpr = weixinkpr;
	}

	public List<InvoicePrintDetail> getDetails() {
		return details;
	}

	public void setDetails(List<InvoicePrintDetail> details) {
		this.details = details;
	}
	
	
}
