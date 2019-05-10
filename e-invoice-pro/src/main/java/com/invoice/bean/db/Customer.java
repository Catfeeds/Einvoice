package com.invoice.bean.db;

public class Customer {
	private String loginid;
	private String phone;
	private String email;

	private String taxname;
	private String taxno;
	private String taxaddr;
	private String taxbank;
	private String entid;
	private String shopid;
	private String bz;//1是标准版
	
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTaxname() {
		return taxname;
	}
	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}
	public String getTaxno() {
		return taxno;
	}
	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}
	public String getTaxaddr() {
		return taxaddr;
	}
	public void setTaxaddr(String taxaddr) {
		this.taxaddr = taxaddr;
	}
	public String getTaxbank() {
		return taxbank;
	}
	public void setTaxbank(String taxbank) {
		this.taxbank = taxbank;
	}

}
