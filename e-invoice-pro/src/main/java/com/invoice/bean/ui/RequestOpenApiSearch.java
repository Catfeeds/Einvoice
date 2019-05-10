package com.invoice.bean.ui;

import java.util.List;
import java.util.Map;

public class RequestOpenApiSearch {
	private String gno;
	private String shopid;
	private String startdate;
	private String enddate;
	private int needsign;
	
	private String entid;
	
	public String getGno() {
		return gno;
	}
	public void setGno(String gno) {
		this.gno = gno;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public int getNeedsign() {
		return needsign;
	}
	public void setNeedsign(int needsign) {
		this.needsign = needsign;
	}
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
}
