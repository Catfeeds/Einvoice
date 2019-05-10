package com.invoice.bean.db;

import java.util.Date;

/*******************************************************************************
 * javaBeans invoice_sale_detail --> InvoiceSaleDetail
 * <table explanation>
 * 
 * @author 2017-07-11 14:46:59
 * 
 */
public class InvoiceSaleDetail implements java.io.Serializable {
	// field
	/**业务单据流水*/
	private String serialid;
	/** 企业id **/
	private String entid;
	private String sheetid;
	private String sheettype;
	
	private String gmftax;
	private String gmfname;
	private String gmfadd;
	private String gmfbank;
	
	private String taxpre;
	private String taxprecon;
	private String zerotax;
	
	private Date tradedate;
	
	private Integer rowno;
	
	private String categoryid;
	/** 商品编码 **/
	private String goodsid;
	/** 商品名称 **/
	private String goodsname;
	/** 税目 **/
	private String taxitemid;
	
	/**
	 * 税目名称
	 */
	private String taxitemname;
	
	/**
	 * 规格
	 */
	private String spec;
	
	/** 数量 **/
	private Double qty;
	/** 单位 **/
	private String unit;
	/** 单价 */
	private Double price;
	/** 发票金额 **/
	private Double amount;
	/** 税率 **/
	private Double taxrate;
	/** 税额 **/
	private Double taxfee;
	/** 计算后金额 */
	private Double amt;
	/** 原始金额 */
	private Double oldamt;
	/** 是否开票 */
	private String isinvoice;
	
	private int flag;
	private String iqseqno;
	private String backflag;
	
	private String seqno;
	
	
	/**
	 * 发票性质行
	 */
	private String fphxz;
	/**
	 * 折扣行和被折扣行对应的行号
	 */
	private Integer zhdyhh;

	
	
	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
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

	public String getSheettype() {
		return sheettype;
	}

	public void setSheettype(String sheettype) {
		this.sheettype = sheettype;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getTaxitemid() {
		return taxitemid;
	}

	public void setTaxitemid(String taxitemid) {
		this.taxitemid = taxitemid;
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

	public Double getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(Double taxrate) {
		this.taxrate = taxrate;
	}

	public Double getTaxfee() {
		return taxfee;
	}

	public void setTaxfee(Double taxfee) {
		this.taxfee = taxfee;
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

	public Double getOldamt() {
		return oldamt;
	}

	public void setOldamt(Double oldamt) {
		this.oldamt = oldamt;
	}

	public String getIsinvoice() {
		return isinvoice;
	}

	public void setIsinvoice(String isinvoice) {
		this.isinvoice = isinvoice;
	}

	public Integer getRowno() {
		return rowno;
	}

	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}
	
	public boolean equals(InvoiceSaleDetail o){
		return o.getRowno()==this.rowno;
	}

	public String getSerialid() {
		return serialid;
	}

	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}

	public Date getTradedate() {
		return tradedate;
	}

	public void setTradedate(Date tradedate) {
		this.tradedate = tradedate;
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

	public String getBackflag() {
		return backflag;
	}

	public void setBackflag(String backflag) {
		this.backflag = backflag;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public String getTaxitemname() {
		return taxitemname;
	}

	public void setTaxitemname(String taxitemname) {
		this.taxitemname = taxitemname;
	}

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getFphxz() {
		return fphxz;
	}

	public void setFphxz(String fphxz) {
		this.fphxz = fphxz;
	}

	public Integer getZhdyhh() {
		return zhdyhh;
	}

	public void setZhdyhh(Integer zhdyhh) {
		this.zhdyhh = zhdyhh;
	}


}