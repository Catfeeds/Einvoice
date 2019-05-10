package com.invoice.port.bwjf.invoice.bean;
//发票作废需要的内容
public class BwjfInvoiceInValidBean {
	
	private String kpzdbs;//	开票终端标识
	private String fplxdm;//	发票类型代码
	private String zflx;//	作废类型
	private String fpdm;//	发票代码
	private String fphm;//	发票号码
	private String hjje;//	合计金额
	private String zfr;//	作废人
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
	public String getZflx() {
		return zflx;
	}
	public void setZflx(String zflx) {
		this.zflx = zflx;
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
	public String getHjje() {
		return hjje;
	}
	public void setHjje(String hjje) {
		this.hjje = hjje;
	}
	public String getZfr() {
		return zfr;
	}
	public void setZfr(String zfr) {
		this.zfr = zfr;
	}
	
	
	
}
