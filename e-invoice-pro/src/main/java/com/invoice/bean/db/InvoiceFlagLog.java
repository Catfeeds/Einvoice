package com.invoice.bean.db;

import java.util.Date;

public class InvoiceFlagLog {
	
	 private String entid;// varchar(20) NOT NULL COMMENT '企业代码',
	 private String  sheetid;// varchar(18) NOT NULL COMMENT '小票提取码',
	 private String  islock;// varchar(20) NOT NULL COMMENT '是否锁定',
	 private String  flag;// int(11) NOT NULL DEFAULT '0' COMMENT '状态 0未解锁 ，1=已解锁 -1重新提取',
	 private String  userid;// varchar(20) NOT NULL COMMENT '操作人',
	 private String  username;// varchar(20) DEFAULT NULL COMMENT '操作人姓名',
	 private Date  processTime;// datetime DEFAULT NULL COMMENT '操作时间',
	 private String invoicetype;
	 
	 
	 
	public String getInvoicetype() {
		return invoicetype;
	}
	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getSheetid() {
		return sheetid;
	}
	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}
	public String getIslock() {
		return islock;
	}
	public void setIslock(String islock) {
		this.islock = islock;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	 
	 
}
