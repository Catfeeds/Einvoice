package com.invoice.bean.ui;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.invoice.bean.db.Taxinfo;

public class Token {
	
	public static Token getToken(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			HttpSession session = request.getSession(false);
			if (session != null) {
				return (Token) session.getAttribute("token");
			}else{
				return new Token();
			}
		} catch (Exception e) {
			return new Token();
		}
	}
	
	protected String tokenid;
	/**
	 * 登录账号
	 */
	protected String loginid;
	/**
	 * 用户中文名称
	 */
	protected String username;
	
	/**
	 * 用户企业名称
	 */
	protected String entid;
	
	/**默认门店*/
	protected String shopid;
	
	/**开票点*/
	protected String kpd;
	
	/**卷票纸张*/
	protected String jpzz;
	
	/**门店纳税号*/
	protected Taxinfo taxinfo;
	
	/**
	 * 用户的门店范围
	 */
	protected List<Map<String, Object>> shoplist;
	
	protected String channel;
	
	
	
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

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public Taxinfo getTaxinfo() {
		return taxinfo;
	}

	public void setTaxinfo(Taxinfo taxinfo) {
		this.taxinfo = taxinfo;
	}
	
}
