package com.invoice.bean.db;/******************************************************************************* * javaBeans * category --> Category  * <table explanation> * @author 2017-07-11 14:46:58 *  */	public class Category implements java.io.Serializable {	//field	/** 企业ID **/	private String entid;	/** 商品品类ID **/	private String categoryid;	/** 商品品类名称 **/	private String categoryname;	/** 品类级别：1=大类2=中类3=小类4=细类 **/	private int deptlevelid;	/** 上级品类ID **/	private String headcatid;	/** 品类状态：0=正常状态、1=淘汰状态 **/	private int categorystatus;		/** shu名称 **/	private String title;	/** 商品品类名称 **/	private String type;	//method	public Category(){}	public Category(String entid,String categoryid,String categoryname,int deptlevelid,String headcatid,int categorystatus){		this.entid = entid;		this.categoryid = categoryid;		this.categoryname = categoryname;		this.deptlevelid = deptlevelid;		this.headcatid = headcatid;		this.categorystatus = categorystatus;	}	public String getEntid() {		return entid;	}	public String getTitle() {		return title;	}	public void setTitle(String title) {		this.title = title;	}	public String getType() {		return type;	}	public void setType(String type) {		this.type = type;	}	public void setEntid(String entid) {		this.entid = entid;	}	public String getCategoryid() {		return categoryid;	}	public void setCategoryid(String categoryid) {		this.categoryid = categoryid;	}	public String getCategoryname() {		return categoryname;	}	public void setCategoryname(String categoryname) {		this.categoryname = categoryname;	}	public int getDeptlevelid() {		return deptlevelid;	}	public void setDeptlevelid(int deptlevelid) {		this.deptlevelid = deptlevelid;	}	public String getHeadcatid() {		return headcatid;	}	public void setHeadcatid(String headcatid) {		this.headcatid = headcatid;	}	public int getCategorystatus() {		return categorystatus;	}	public void setCategorystatus(int categorystatus) {		this.categorystatus = categorystatus;	}}