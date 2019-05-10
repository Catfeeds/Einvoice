package com.invoice.bean.ui;

public class ResponseInvoiceItem {
	private String taxitemid;
	private String goodsName;
	private Double price;
	private Double amount;
	private Double qty;
	private Double taxFee;
	private Double taxRate;
	private String unit;
	public String getTaxitemid() {
		return taxitemid;
	}
	public void setTaxitemid(String taxitemid) {
		this.taxitemid = taxitemid;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getTaxFee() {
		return taxFee;
	}
	public void setTaxFee(Double taxFee) {
		this.taxFee = taxFee;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
}
