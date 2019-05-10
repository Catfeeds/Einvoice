package com.invoice.yuande.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="InfoDetail" )
public class YuanDeInvoiceSaleDzDetailBean {

	private String goodsname;//	货物名称
	private String standard;//	规格型号
	private String unit;//	计量单位
	private String num;//	数量
	private String price;//	含税单价
	private String amount;//	含税金额
	private String taxrate;//	税率
	private String taxamount;//	税额
	private String aigo;//	折扣金额
	private String aigotax;//	折扣税额
	private String goodsgroup;//	货物类别
	private String goodsnover;//	编码版本号
	private String goodstaxno;//	税收分类编码
	private String taxpre;//	是否享受优惠政策
	private String taxprecon;//	优惠政策类型
	private String zerotax;//	零税率标识
	private String goodstaxname;//	税收分类编码简称
	
	@XmlElement(name = "GoodsName")  
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	
	@XmlElement(name = "Standard")  
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	
	@XmlElement(name = "Unit")  
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@XmlElement(name = "Num")  
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	
	@XmlElement(name = "Price")  
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	@XmlElement(name = "Amount")  
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	@XmlElement(name = "TaxRate")  
	public String getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}
	
	@XmlElement(name = "TaxAmount")  
	public String getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(String taxamount) {
		this.taxamount = taxamount;
	}
	
	@XmlElement(name = "Aigo")  
	public String getAigo() {
		return aigo;
	}
	public void setAigo(String aigo) {
		this.aigo = aigo;
	}
	
	@XmlElement(name = "AigoTax")  
	public String getAigotax() {
		return aigotax;
	}
	public void setAigotax(String aigotax) {
		this.aigotax = aigotax;
	}
	
	@XmlElement(name = "GoodsGroup")  
	public String getGoodsgroup() {
		return goodsgroup;
	}
	public void setGoodsgroup(String goodsgroup) {
		this.goodsgroup = goodsgroup;
	}
	
	@XmlElement(name = "GoodsNoVer")  
	public String getGoodsnover() {
		return goodsnover;
	}
	public void setGoodsnover(String goodsnover) {
		this.goodsnover = goodsnover;
	}
	
	@XmlElement(name = "GoodsTaxNo")  
	public String getGoodstaxno() {
		return goodstaxno;
	}
	public void setGoodstaxno(String goodstaxno) {
		this.goodstaxno = goodstaxno;
	}
	
	@XmlElement(name = "TaxPre")  
	public String getTaxpre() {
		return taxpre;
	}
	public void setTaxpre(String taxpre) {
		this.taxpre = taxpre;
	}
	
	@XmlElement(name = "TaxPreCon")  
	public String getTaxprecon() {
		return taxprecon;
	}
	public void setTaxprecon(String taxprecon) {
		this.taxprecon = taxprecon;
	}
	
	@XmlElement(name = "ZeroTax")  
	public String getZerotax() {
		return zerotax;
	}
	public void setZerotax(String zerotax) {
		this.zerotax = zerotax;
	}
	
	@XmlElement(name = "GoodsTaxName")  
	public String getGoodstaxname() {
		return goodstaxname;
	}
	public void setGoodstaxname(String goodstaxname) {
		this.goodstaxname = goodstaxname;
	}
	
	
}
