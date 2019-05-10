package com.invoice.bean.db;

import java.util.Date;

/**
 * @author Administrator
 * bj-4
 */
public class BillSaleLog {
	private int logid;
	private String entid;
	private String shopid;
	private Date sdate;
	private int flag;
	private Date createtime;
	private Date processtime;
	private String processmsg;
	
	
	public int getLogid() {
		return logid;
	}
	public void setLogid(int logid) {
		this.logid = logid;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public Date getSdate() {
		return sdate;
	}
	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getProcesstime() {
		return processtime;
	}
	public void setProcesstime(Date processtime) {
		this.processtime = processtime;
	}
	public String getProcessmsg() {
		return processmsg;
	}
	public void setProcessmsg(String processmsg) {
		this.processmsg = processmsg;
	}
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
}
