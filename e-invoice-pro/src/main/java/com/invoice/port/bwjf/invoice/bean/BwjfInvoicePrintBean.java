package com.invoice.port.bwjf.invoice.bean;

public class BwjfInvoicePrintBean {
	private String kpzdbs;//	开票终端标识
	private String fplxdm;//	发票类型代码
	private String fpdm;//	发票代码
	private String fphm;//	发票号码
	private String dylx;//	打印类型
	private String Dyfs;//	打印方式
	private String printername;//	打印机名称
	
	public String getKpzdbs() {
		return kpzdbs;
	}
	public void setKpzdbs(String kpzdbs) {
		this.kpzdbs = kpzdbs;
	}
	public String getFplxdm() {
		return fplxdm;
	}
	public void setFplxdm(String fplxdm) {
		this.fplxdm = fplxdm;
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
	public String getDylx() {
		return dylx;
	}
	public void setDylx(String dylx) {
		this.dylx = dylx;
	}
	public String getDyfs() {
		return Dyfs;
	}
	public void setDyfs(String dyfs) {
		Dyfs = dyfs;
	}
	public String getPrintername() {
		return printername;
	}
	public void setPrintername(String printername) {
		this.printername = printername;
	}
	
	
	
}
