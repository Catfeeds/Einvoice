package com.invoice.bean.ui;

import java.util.ArrayList;
import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSalePay;

public class ResponseBillInfo {
	/** 企业id **/
	private String entid;
	
	private String sheetid;
	private String sheettype;
	/** 对应invoice_sale_head 流水*/
	private String serialid;
	/** 明细行号，按明细开票时使用*/
	private List<Integer> rownoList = new ArrayList<Integer>();
	
	/** 门店号 **/
	private String shopid;
	private String shopname;
	/** 收银机id **/
	private String syjid;
	/** 小票号 **/
	private String billno;
	/** 小票总金额 **/
	private Double totalamount;
	/** 可开票金额 **/
	private Double invoiceamount;
	/** 总税额 **/
	private Double totaltaxfee;
	/** 交易日期 **/
	private String tradedate;
	/** 备注 **/
	private String remark;
	
	/**是否已开票 0=未开票 1=已开票*/
	private int flag;
	
	private int backflag;
	
	/**开票申请流水*/
	private String iqseqno;
	
	private int iqstatus;
	
	/** 纳税人识别号 **/
	private String taxno;
	/** 名称 **/
	private String taxname;
	/** 地址电话 **/
	private String taxadd;
	/** 银行帐号 **/
	private String taxbank;
	/** 法人 **/
	private String taxperson;
	
	
	/** 购买方纳税人识别号 **/
	private String gmftax;
	/** 购买方名称 **/
	private String gmfname;
	/** 购买方地址电话 **/
	private String gmfadd;
	/** 购买方银行帐号 **/
	private String gmfbank;
	
	private String recvemail;
	private String recvphone;
	private String invoicelx;
	
	private String kpd;
	
	/** 接口类型:1百望，2航信 **/
	private String taxmode;
	
	private String paynote;
	
	private String pdf;
	
	private String isauto;
	
	private List<InvoiceSaleDetail> invoiceSaleDetail;
	
	private List<InvoiceSalePay> invoiceSalePay;
	
	
	
	public String getIsauto() {
		return isauto;
	}
	public void setIsauto(String isauto) {
		this.isauto = isauto;
	}
	public String getSerialid() {
		return serialid;
	}
	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getSyjid() {
		return syjid;
	}
	public void setSyjid(String syjid) {
		this.syjid = syjid;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public Double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}
	public Double getTotaltaxfee() {
		return totaltaxfee;
	}
	public void setTotaltaxfee(Double totaltaxfee) {
		this.totaltaxfee = totaltaxfee;
	}
	public String getTradedate() {
		return tradedate;
	}
	public void setTradedate(String tradedate) {
		this.tradedate = tradedate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getIqseqno() {
		return iqseqno;
	}
	public void setIqseqno(String iqseqno) {
		this.iqseqno = iqseqno;
	}
	public String getTaxno() {
		return taxno;
	}
	public void setTaxno(String taxno) {
		this.taxno = taxno;
	}
	public String getTaxname() {
		return taxname;
	}
	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}
	public String getTaxadd() {
		return taxadd;
	}
	public void setTaxadd(String taxadd) {
		this.taxadd = taxadd;
	}
	public String getTaxbank() {
		return taxbank;
	}
	public void setTaxbank(String taxbank) {
		this.taxbank = taxbank;
	}
	public String getTaxperson() {
		return taxperson;
	}
	public void setTaxperson(String taxperson) {
		this.taxperson = taxperson;
	}
	public String getTaxmode() {
		return taxmode;
	}
	public void setTaxmode(String taxmode) {
		this.taxmode = taxmode;
	}
	public Double getInvoiceamount() {
		return invoiceamount;
	}
	public void setInvoiceamount(Double invoiceamount) {
		this.invoiceamount = invoiceamount;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getSheetid() {
		return sheetid;
	}
	public void setSheetid(String sheetid) {
		this.sheetid = sheetid;
	}
	public String getSheettype() {
		return sheettype;
	}
	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}
	public String getKpd() {
		return kpd;
	}
	public void setKpd(String kpd) {
		this.kpd = kpd;
	}
	public List<InvoiceSaleDetail> getInvoiceSaleDetail() {
		return invoiceSaleDetail;
	}
	public void setInvoiceSaleDetail(List<InvoiceSaleDetail> invoiceSaleDetail) {
		this.invoiceSaleDetail = invoiceSaleDetail;
	}
	public List<InvoiceSalePay> getInvoiceSalePay() {
		return invoiceSalePay;
	}
	public void setInvoiceSalePay(List<InvoiceSalePay> invoiceSalePay) {
		this.invoiceSalePay = invoiceSalePay;
	}
	public String getPaynote() {
		return paynote;
	}
	public void setPaynote(String paynote) {
		this.paynote = paynote;
	}
	public String getGmftax() {
		return gmftax;
	}
	public void setGmftax(String gmftax) {
		this.gmftax = gmftax;
	}
	public String getGmfname() {
		return gmfname;
	}
	public void setGmfname(String gmfname) {
		this.gmfname = gmfname;
	}
	public String getGmfadd() {
		return gmfadd;
	}
	public void setGmfadd(String gmfadd) {
		this.gmfadd = gmfadd;
	}
	public String getGmfbank() {
		return gmfbank;
	}
	public void setGmfbank(String gmfbank) {
		this.gmfbank = gmfbank;
	}
	public List<Integer> getRownoList() {
		return rownoList;
	}
	public void setRownoList(List<Integer> rownoList) {
		this.rownoList = rownoList;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public String getRecvemail() {
		return recvemail;
	}
	public void setRecvemail(String recvemail) {
		this.recvemail = recvemail;
	}
	public String getRecvphone() {
		return recvphone;
	}
	public void setRecvphone(String recvphone) {
		this.recvphone = recvphone;
	}
	public String getInvoicelx() {
		return invoicelx;
	}
	public void setInvoicelx(String invoicelx) {
		this.invoicelx = invoicelx;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public int getBackflag() {
		return backflag;
	}
	public void setBackflag(int backflag) {
		this.backflag = backflag;
	}
	public int getIqstatus() {
		return iqstatus;
	}
	public void setIqstatus(int iqstatus) {
		this.iqstatus = iqstatus;
	}
}
