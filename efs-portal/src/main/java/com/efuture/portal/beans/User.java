/*
 * Copyright (C), 1996-2015
 * FileName: User.java
 * Author:   王华君
 * Date:     Feb 9, 2015 3:26:54 PM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.beans;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class User {

	private int id;
	private String token;
	private String companyid;
	private String companyname;
	private String crmflag;
	private String loginid;
	private String username;
	private String shopid;
	private String wxcode;
	private String wxopenid;
	private Date lasttime;
	private String cardno;
	private String partnerid="";
	private boolean isPartner;
	private String area;
	private String entid;
	private String kpd;
	private String jpzz;
	protected List<Map<String, Object>> shoplist;
	
	
	
	
	public String getJpzz() {
		return jpzz;
	}
	public void setJpzz(String jpzz) {
		this.jpzz = jpzz;
	}
	public String getKpd() {
		return kpd;
	}
	public void setKpd(String kpd) {
		this.kpd = kpd;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getCrmflag() {
		return crmflag;
	}
	public void setCrmflag(String crmflag) {
		this.crmflag = crmflag;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getWxcode() {
		return wxcode;
	}
	public void setWxcode(String wxcode) {
		this.wxcode = wxcode;
	}
	public String getWxopenid() {
		return wxopenid;
	}
	public void setWxopenid(String wxopenid) {
		this.wxopenid = wxopenid;
	}
	public Date getLasttime() {
		return lasttime;
	}
	public void setLasttime(Date lasttime) {
		this.lasttime = lasttime;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public boolean isPartner() {
		return isPartner;
	}
	public void setPartner(boolean isPartner) {
		this.isPartner = isPartner;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public List<Map<String, Object>> getShoplist() {
		return shoplist;
	}
	public void setShoplist(List<Map<String, Object>> shoplist) {
		this.shoplist = shoplist;
	}
}
