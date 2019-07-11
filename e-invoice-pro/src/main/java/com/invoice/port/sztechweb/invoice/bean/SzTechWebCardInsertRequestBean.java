package com.invoice.port.sztechweb.invoice.bean;

public class SzTechWebCardInsertRequestBean {
	String order_id;//订单id ，[参见订单号生成规则]
	String provider_id;//服务商appid
	String tax_no;//商户税号
	String tax_name;//商户名称
	String store_name;//门店名称
	String industry_code;//行业简称， 见[附录：行业分类字典]
	String appid;//微信公众号appid（同 授权接口传入的appid值，为空则都为空，谨记）
	String buyer_title_type;//发票抬头类型, 1:个人 ,2:企业
	String buyer_title;//抬头名称
	String buyer_taxcode;//购货方识别号(税号)
	String buyer_address;//购买公司地址
	String buyer_address_phone;//购货方公司电话
	String buyer_bank_name;//购买方开户银行
	String buyer_bank_account;//银行账号
	String buyer_phone;//购买方的手机号
	String buyer_email;//购买方的邮箱
	String ticket_sn;//发票号码
	String ticket_code;//发票代码
	String ticket_date;//开票日期，为10位时间戳（utc+8）
	String pdf_url;//发票pdf的url地址
	String fee;//发票的金额(元)
	String fee_without_tax;//不含税金额(元)
	String tax;//税额(元)
	String check_code;//发票校验码
	String card_id;//微信卡券ID(通过创建卡券模板接口获取)
	String goods_name;//商品名称
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getProvider_id() {
		return provider_id;
	}
	public void setProvider_id(String provider_id) {
		this.provider_id = provider_id;
	}
	public String getTax_no() {
		return tax_no;
	}
	public void setTax_no(String tax_no) {
		this.tax_no = tax_no;
	}
	public String getTax_name() {
		return tax_name;
	}
	public void setTax_name(String tax_name) {
		this.tax_name = tax_name;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getIndustry_code() {
		return industry_code;
	}
	public void setIndustry_code(String industry_code) {
		this.industry_code = industry_code;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getBuyer_title_type() {
		return buyer_title_type;
	}
	public void setBuyer_title_type(String buyer_title_type) {
		this.buyer_title_type = buyer_title_type;
	}
	public String getBuyer_title() {
		return buyer_title;
	}
	public void setBuyer_title(String buyer_title) {
		this.buyer_title = buyer_title;
	}
	public String getBuyer_taxcode() {
		return buyer_taxcode;
	}
	public void setBuyer_taxcode(String buyer_taxcode) {
		this.buyer_taxcode = buyer_taxcode;
	}
	public String getBuyer_address() {
		return buyer_address;
	}
	public void setBuyer_address(String buyer_address) {
		this.buyer_address = buyer_address;
	}
	public String getBuyer_address_phone() {
		return buyer_address_phone;
	}
	public void setBuyer_address_phone(String buyer_address_phone) {
		this.buyer_address_phone = buyer_address_phone;
	}
	public String getBuyer_bank_name() {
		return buyer_bank_name;
	}
	public void setBuyer_bank_name(String buyer_bank_name) {
		this.buyer_bank_name = buyer_bank_name;
	}
	public String getBuyer_bank_account() {
		return buyer_bank_account;
	}
	public void setBuyer_bank_account(String buyer_bank_account) {
		this.buyer_bank_account = buyer_bank_account;
	}
	public String getBuyer_phone() {
		return buyer_phone;
	}
	public void setBuyer_phone(String buyer_phone) {
		this.buyer_phone = buyer_phone;
	}
	public String getBuyer_email() {
		return buyer_email;
	}
	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}
	public String getTicket_sn() {
		return ticket_sn;
	}
	public void setTicket_sn(String ticket_sn) {
		this.ticket_sn = ticket_sn;
	}
	public String getTicket_code() {
		return ticket_code;
	}
	public void setTicket_code(String ticket_code) {
		this.ticket_code = ticket_code;
	}
	public String getTicket_date() {
		return ticket_date;
	}
	public void setTicket_date(String ticket_date) {
		this.ticket_date = ticket_date;
	}
	public String getPdf_url() {
		return pdf_url;
	}
	public void setPdf_url(String pdf_url) {
		this.pdf_url = pdf_url;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getFee_without_tax() {
		return fee_without_tax;
	}
	public void setFee_without_tax(String fee_without_tax) {
		this.fee_without_tax = fee_without_tax;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getCheck_code() {
		return check_code;
	}
	public void setCheck_code(String check_code) {
		this.check_code = check_code;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
}
