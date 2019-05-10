package com.invoice.bean.db;

public class PurchaserInfo {
	private String name; 				//企业名称
	private String taxId;				//企业税号
	private String bankAndAccount;		//银行+账号
	private String addressAndPhone;		//地址+电话
	private String bank;				//开户银行
	private String bankAccount;			//开户账号
	private String location;			//企业地址
	private String mobilePhone;			//联系电话
	private String city;				//市
	private String country;				//区
	private String province;			//省
	private String companyIndex;		//公司索引
	private String taxAuthorityCode;	//税务机关代码
	private String taxAuthorityName;	//税务机关名称
	private String frequency;			//使用频率
	private String score;				//相似度
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getBankAndAccount() {
		return bankAndAccount;
	}
	public void setBankAndAccount(String bankAndAccount) {
		this.bankAndAccount = bankAndAccount;
	}
	public String getAddressAndPhone() {
		return addressAndPhone;
	}
	public void setAddressAndPhone(String addressAndPhone) {
		this.addressAndPhone = addressAndPhone;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCompanyIndex() {
		return companyIndex;
	}
	public void setCompanyIndex(String companyIndex) {
		this.companyIndex = companyIndex;
	}
	public String getTaxAuthorityCode() {
		return taxAuthorityCode;
	}
	public void setTaxAuthorityCode(String taxAuthorityCode) {
		this.taxAuthorityCode = taxAuthorityCode;
	}
	public String getTaxAuthorityName() {
		return taxAuthorityName;
	}
	public void setTaxAuthorityName(String taxAuthorityName) {
		this.taxAuthorityName = taxAuthorityName;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
}
