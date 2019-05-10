package com.efuture.portal.beans;

public class RegisterBean {
	private String entid;
	private String rule;
	private int taxnonum;
	private int shopnum;
	private String enddate;
	private String sdate;
	private String registerid;
	
	
	
	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getRegisterid() {
		return registerid;
	}

	public void setRegisterid(String registerid) {
		this.registerid = registerid;
	}

	public RegisterBean(){
		taxnonum  =0;
		shopnum = 0;
	}
	
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public int getTaxnonum() {
		return taxnonum;
	}
	public void setTaxnonum(int taxnonum) {
		this.taxnonum = taxnonum;
	}
	public int getShopnum() {
		return shopnum;
	}
	public void setShopnum(int shopnum) {
		this.shopnum = shopnum;
	}

	@Override
	public String toString() {
		return "RegisterBean [entid=" + entid + ", rule=" + rule + ", taxnonum=" + taxnonum + ", shopnum=" + shopnum
				+ ", enddate=" + enddate + "]";
	}
	
	
	
}
