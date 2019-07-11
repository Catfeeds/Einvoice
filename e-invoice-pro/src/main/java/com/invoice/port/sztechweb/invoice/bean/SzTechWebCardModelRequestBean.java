package com.invoice.port.sztechweb.invoice.bean;

public class SzTechWebCardModelRequestBean {
	String appid;//商户公众号appid
	String full_name;//收款方全称
	String short_name;//收款方简称
	String type;//发票类型，默认为“增值税电子普通发票”
	String logo_url;//发票商家LOGO，不填默认为发票儿LOGO
	String description;//发票使用说明，默认为“增值税电子普通发票”
	String custom_url_name;//开票平台自定义入口名称，如“查看发票”
	String custom_url;//开票平台自定义入口跳转外链
	String custom_url_sub_title;//自定义入口右侧的tips，如“查看发票”
	String promotion_url_name;//营销场景自定义入口名称
	String promotion_url;//营销场景自定义入口跳转外链
	String promotion_url_sub_title;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLogo_url() {
		return logo_url;
	}
	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCustom_url_name() {
		return custom_url_name;
	}
	public void setCustom_url_name(String custom_url_name) {
		this.custom_url_name = custom_url_name;
	}
	public String getCustom_url() {
		return custom_url;
	}
	public void setCustom_url(String custom_url) {
		this.custom_url = custom_url;
	}
	public String getCustom_url_sub_title() {
		return custom_url_sub_title;
	}
	public void setCustom_url_sub_title(String custom_url_sub_title) {
		this.custom_url_sub_title = custom_url_sub_title;
	}
	public String getPromotion_url_name() {
		return promotion_url_name;
	}
	public void setPromotion_url_name(String promotion_url_name) {
		this.promotion_url_name = promotion_url_name;
	}
	public String getPromotion_url() {
		return promotion_url;
	}
	public void setPromotion_url(String promotion_url) {
		this.promotion_url = promotion_url;
	}
	public String getPromotion_url_sub_title() {
		return promotion_url_sub_title;
	}
	public void setPromotion_url_sub_title(String promotion_url_sub_title) {
		this.promotion_url_sub_title = promotion_url_sub_title;
	}
	
}
