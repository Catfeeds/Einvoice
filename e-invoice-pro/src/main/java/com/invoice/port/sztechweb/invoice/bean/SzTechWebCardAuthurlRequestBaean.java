package com.invoice.port.sztechweb.invoice.bean;

public class SzTechWebCardAuthurlRequestBaean {
	String appid;//微信公众号appid, （需传入时须进行公众号授权操作[参见公众号授权]）
	String order_id;//订单id, [参见订单号生成规则]
	String money;//订单金额（以元为单位,最多保留两位小数）
	String timestamp;//时间戳(为10位时间戳)
	String source;//[app, web, wxa]开票来源，app：app开票，web：微信h5开票，wxa：小程序开发票
	String redirect_url;//授权成功后跳转页面地址
	String type;//[0,1,2]授权类型，0：开票授权，1：填写字段开票授权，2：领票授权
	String callback_url;//授权完成回调地址
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRedirect_url() {
		return redirect_url;
	}
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCallback_url() {
		return callback_url;
	}
	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}
}
