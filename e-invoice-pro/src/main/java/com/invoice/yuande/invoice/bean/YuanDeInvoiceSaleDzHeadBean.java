package com.invoice.yuande.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="InfoMaster")
public class YuanDeInvoiceSaleDzHeadBean {
	private String number;//	业务流水号
	private String busno;//	订单编号
	private String organ;//	组织机构代码
	private String clientname;//	购方名称
	private String clienttaxcode;//	购方税号
	private String clientbankaccount;//	购方银行及账号
	private String clientaddressphone;//	购方地址及电话
	private String clientphone;//	客户手机号
	private String clientmail;//	客户邮箱
	private String billtype;//	发票性质
	private String infokind;//	发票类型
	private String notes;//	发票备注
	private String invoicecode;//	蓝字发票代码
	private String invoiceno;//	蓝字发票号码
	private String invoicer;//	开票人
	private String checker;//	复核人
	private String cashier;//	收款人
	private String allmoney;//	是否全部现金
	private String summoney;//	现金总金额
	
	
	private List<YuanDeInvoiceSaleDzDetailBean> details;
	
	
	@XmlElement(name = "InfoDetail")  
	public List<YuanDeInvoiceSaleDzDetailBean> getDetails() {
		return details;
	}
	public void setDetails(List<YuanDeInvoiceSaleDzDetailBean> details) {
		this.details = details;
	}
	@XmlElement(name = "Number")  
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@XmlElement(name = "BusNo")  
	public String getBusno() {
		return busno;
	}
	public void setBusno(String busno) {
		this.busno = busno;
	}
	
	@XmlElement(name = "Organ")  
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
	}
	
	@XmlElement(name = "ClientName")  
	public String getClientname() {
		return clientname;
	}
	public void setClientname(String clientname) {
		this.clientname = clientname;
	}
	
	@XmlElement(name = "ClientTaxCode")  
	public String getClienttaxcode() {
		return clienttaxcode;
	}
	public void setClienttaxcode(String clienttaxcode) {
		this.clienttaxcode = clienttaxcode;
	}
	
	@XmlElement(name = "ClientBankAccount")  
	public String getClientbankaccount() {
		return clientbankaccount;
	}
	public void setClientbankaccount(String clientbankaccount) {
		this.clientbankaccount = clientbankaccount;
	}
	
	@XmlElement(name = "ClientAddressPhone")  
	public String getClientaddressphone() {
		return clientaddressphone;
	}
	public void setClientaddressphone(String clientaddressphone) {
		this.clientaddressphone = clientaddressphone;
	}
	
	@XmlElement(name = "ClientPhone")  
	public String getClientphone() {
		return clientphone;
	}
	public void setClientphone(String clientphone) {
		this.clientphone = clientphone;
	}
	
	@XmlElement(name = "ClientMail")  
	public String getClientmail() {
		return clientmail;
	}
	public void setClientmail(String clientmail) {
		this.clientmail = clientmail;
	}
	
	@XmlElement(name = "BillType")  
	public String getBilltype() {
		return billtype;
	}
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	
	@XmlElement(name = "InfoKind")  
	public String getInfokind() {
		return infokind;
	}
	public void setInfokind(String infokind) {
		this.infokind = infokind;
	}
	
	@XmlElement(name = "Notes")  
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@XmlElement(name = "InvoiceCode")  
	public String getInvoicecode() {
		return invoicecode;
	}
	public void setInvoicecode(String invoicecode) {
		this.invoicecode = invoicecode;
	}
	
	@XmlElement(name = "InvoiceNo")  
	public String getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}
	
	@XmlElement(name = "Invoicer")  
	public String getInvoicer() {
		return invoicer;
	}
	public void setInvoicer(String invoicer) {
		this.invoicer = invoicer;
	}
	
	@XmlElement(name = "Checker")  
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	
	@XmlElement(name = "Cashier")  
	public String getCashier() {
		return cashier;
	}
	public void setCashier(String cashier) {
		this.cashier = cashier;
	}
	
	@XmlElement(name = "AllMoney")  
	public String getAllmoney() {
		return allmoney;
	}
	
	public void setAllmoney(String allmoney) {
		this.allmoney = allmoney;
	}
	
	@XmlElement(name = "SumMoney")  
	public String getSummoney() {
		return summoney;
	}
	public void setSummoney(String summoney) {
		this.summoney = summoney;
	}
	
	
	
}
