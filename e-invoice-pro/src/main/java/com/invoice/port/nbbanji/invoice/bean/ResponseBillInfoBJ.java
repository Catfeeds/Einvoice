package com.invoice.port.nbbanji.invoice.bean;

import java.util.ArrayList;
import java.util.List;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSalePay;

public class ResponseBillInfoBJ {
	/** 企业id **/
	private String entid;
	/** 小票id **/
	private String sheetid;
	/** 小票类型 **/
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
	/** 是否已开票 0=未开票 1=已开票*/
	private int flag;
	/** 回传标志*/
	private int backflag;
	/** 开票申请流水*/
	private String iqseqno;
	/** 开票状态*/
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
	/** 接收Email*/
	private String recvemail;
	/** 接收短信手机号*/
	private String recvphone;
	/** 发票类型*/
	private String invoicelx;
	/** 开票点*/
	private String kpd;
	/** 接口类型:1百望，2航信 **/
	private String taxmode;
	
	private String paynote;
	
	private String pdf;
	
	private String isauto;

	private List<InvoiceSaleDetail> invoiceSaleDetail;
	
	private List<InvoiceSalePay> invoiceSalePay;
	
	/** 新增字段  **/
	private String hzfpxxbbh;//红字信息表编号
	private String kpr;//开票人
	private String skr;//收款人
	private String fhr;//复核人
	private String yskt;//原收款台
	private String ymdbh;//原门店编号
	private String yfpdm;//原发票代码
	private String yfphm;//原发票号码
	private String refsheetid; //原小票号
	private String status; //状态
	private String rtfpdm; //发票代码 
	private String rtfphm; //发票号码 
	private String rtkprq; //发票日期
	private String gmfno; //红字信息表编号
	
	public String getGmfno() {
		return gmfno;
	}
	public void setGmfno(String gmfno) {
		this.gmfno = gmfno;
	}
	public String getRtfpdm() {
		return rtfpdm;
	}
	public void setRtfpdm(String rtfpdm) {
		this.rtfpdm = rtfpdm;
	}
	public String getRtfphm() {
		return rtfphm;
	}
	public void setRtfphm(String rtfphm) {
		this.rtfphm = rtfphm;
	}
	public String getRtkprq() {
		return rtkprq;
	}
	public void setRtkprq(String rtkprq) {
		this.rtkprq = rtkprq;
	}
	public String getRefsheetid() {
		return refsheetid;
	}
	public void setRefsheetid(String refsheetid) {
		this.refsheetid = refsheetid;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getHzfpxxbbh() {
		return hzfpxxbbh;
	}
	
	public void setHzfpxxbbh(String hzfpxxbbh) {
		this.hzfpxxbbh = hzfpxxbbh;
	}
	
	public String getKpr() {
		return kpr;
	}
	
	public void setKpr(String kpr) {
		this.kpr = kpr;
	}
	
	public String getSkr() {
		return skr;
	}
	
	public void setSkr(String skr) {
		this.skr = skr;
	}
	
	public String getFhr() {
		return fhr;
	}
	
	public void setFhr(String fhr) {
		this.fhr = fhr;
	}
	
	public String getYskt() {
		return yskt;
	}
	
	public void setYskt(String yskt) {
		this.yskt = yskt;
	}
	
	public String getYmdbh() {
		return ymdbh;
	}
	
	public void setYmdbh(String ymdbh) {
		this.ymdbh = ymdbh;
	}
	
	public String getYfpdm() {
		return yfpdm;
	}
	
	public void setYfpdm(String yfpdm) {
		this.yfpdm = yfpdm;
	}
	
	public String getYfphm() {
		return yfphm;
	}
	
	public void setYfphm(String yfphm) {
		this.yfphm = yfphm;
	}
	
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
