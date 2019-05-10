package com.efuture.portal.beans;

public class Enterprise  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2555687498926530491L;
	
	/** 企业id **/
	private String entid;
	/** 企业名称 **/
	private String entname;
	/** 联系电话 **/
	private String phone;
	/** 地址 **/
	private String address;
	/** 对应税号 **/
	private String taxid;
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getEntname() {
		return entname;
	}
	public void setEntname(String entname) {
		this.entname = entname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	@Override
	public String toString() {
		return "Enterprise [entid=" + entid + ", entname=" + entname + ", phone=" + phone + ", address=" + address
				+ ", taxid=" + taxid + "]";
	}
	
	
}
