package com.einvoice.sell.bean;


public class ShopConnect {
	private String shopid;
	private String shoptype;
	private String connectid;
	private String url;
	private String username;
	private String password;
	private String driver;
	private String dbcharcode;
	private Integer maxactive;
	private Integer initsize;
	private Integer maxwait;
	private Integer minidle;
	private Integer maxidle;
	
	private String lastActiveDate;
	private String lastMsg;
	private int queryCount;
	private boolean active;
	
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getConnectid() {
		return connectid;
	}
	public void setConnectid(String connectid) {
		this.connectid = connectid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDbcharcode() {
		return dbcharcode;
	}
	public void setDbcharcode(String dbcharcode) {
		this.dbcharcode = dbcharcode;
	}
	public Integer getMaxactive() {
		return maxactive;
	}
	public void setMaxactive(Integer maxactive) {
		this.maxactive = maxactive;
	}
	public Integer getInitsize() {
		return initsize;
	}
	public void setInitsize(Integer initsize) {
		this.initsize = initsize;
	}
	public Integer getMaxwait() {
		return maxwait;
	}
	public void setMaxwait(Integer maxwait) {
		this.maxwait = maxwait;
	}
	public Integer getMinidle() {
		return minidle;
	}
	public void setMinidle(Integer minidle) {
		this.minidle = minidle;
	}
	public Integer getMaxidle() {
		return maxidle;
	}
	public void setMaxidle(Integer maxidle) {
		this.maxidle = maxidle;
	}
	public String getLastMsg() {
		return lastMsg;
	}
	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}
	public String getLastActiveDate() {
		return lastActiveDate;
	}
	public void setLastActiveDate(String lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	public int getQueryCount() {
		return queryCount;
	}
	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
	public String getShoptype() {
		return shoptype;
	}
	public void setShoptype(String shoptype) {
		this.shoptype = shoptype;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean isActive) {
		this.active = isActive;
	}
}
