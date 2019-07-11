package com.invoice.port.sztechweb.invoice.bean;

public class SzTechWebCardCancelRequestBean {
	String card_id;//卡包模板ID
	String card_code;//发票CODE
	String status;//发票状态 init 发票初始状态，未锁定，可提交报销；lock 发票已锁定，无法重复提交报销；closure 发票已核销，从用户卡包中移除；cancel 发票被冲红，从用户卡包中移除；

	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCard_code() {
		return card_code;
	}
	public void setCard_code(String card_code) {
		this.card_code = card_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
