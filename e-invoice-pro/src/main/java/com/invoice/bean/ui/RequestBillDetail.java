package com.invoice.bean.ui;

/**
 * 可开票清单查询条件
 * @author Baij
 */
public class RequestBillDetail {
	/** 企业id **/
	protected String entid;
	/**用户ID 微信端为用户ID，网页端为登录账号*/
	protected String userID;
	/** 开票渠道 wx或app **/
	protected String channel;
	
	protected String sheetid;
	/** 单据类型 **/
	protected String sheettype;
	/** 门店 **/
	protected String shopid;
	
	/**交易日期开始*/
	protected String mintradedate;
	/**交易日期结束*/
	protected String maxtradedate;
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSheetid() {
		return sheetid;
	}
	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}
	public String getSheettype() {
		return sheettype;
	}
	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getMintradedate() {
		return mintradedate;
	}
	public void setMintradedate(String mintradedate) {
		this.mintradedate = mintradedate;
	}
	public String getMaxtradedate() {
		return maxtradedate;
	}
	public void setMaxtradedate(String maxtradedate) {
		this.maxtradedate = maxtradedate;
	}
	
}
