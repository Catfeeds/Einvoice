package com.invoice.bean.ui;

public class ResponseBillDetail {
	/** 企业id **/
	private String entid;
	private String sheetid;
	private String sheettype;
	/** 对应invoice_sale_head 流水*/
	private String serialid;
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
	
	private String kpd;
	
	/** 接口类型:1百望，2航信 **/
	private String taxmode;
	
	private String paynote;
	
	
	private int rowno;
	private String goodsid;
	private String goodsname;
	private String taxitemid;
	private Integer qty;
	private String unit;
	private Double price;
	private Double amount;
	private Double taxfee;
	private Double taxrate;
	private Double amt;
	private Double oldamt;
	private String taxpre;
	private String taxprecon;
	private String zerotax;
	private String isinvoice;
	/**是否已开票，取自明细表 0=未开票 1=已开票*/
	private int flag;
	/**开票申请流水*/
	private String iqseqno;
	
	
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
	public String getPaynote() {
		return paynote;
	}
	public void setPaynote(String paynote) {
		this.paynote = paynote;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public int getRowno() {
		return rowno;
	}
	public void setRowno(int rowno) {
		this.rowno = rowno;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getTaxitemid() {
		return taxitemid;
	}
	public void setTaxitemid(String taxitemid) {
		this.taxitemid = taxitemid;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getTaxfee() {
		return taxfee;
	}
	public void setTaxfee(Double taxfee) {
		this.taxfee = taxfee;
	}
	public Double getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public Double getOldamt() {
		return oldamt;
	}
	public void setOldamt(Double oldamt) {
		this.oldamt = oldamt;
	}
	public String getTaxpre() {
		return taxpre;
	}
	public void setTaxpre(String taxpre) {
		this.taxpre = taxpre;
	}
	public String getTaxprecon() {
		return taxprecon;
	}
	public void setTaxprecon(String taxprecon) {
		this.taxprecon = taxprecon;
	}
	public String getZerotax() {
		return zerotax;
	}
	public void setZerotax(String zerotax) {
		this.zerotax = zerotax;
	}
	public String getIsinvoice() {
		return isinvoice;
	}
	public void setIsinvoice(String isinvoice) {
		this.isinvoice = isinvoice;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
}
