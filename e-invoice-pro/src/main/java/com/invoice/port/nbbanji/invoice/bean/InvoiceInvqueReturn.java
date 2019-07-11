package com.invoice.port.nbbanji.invoice.bean;

public class InvoiceInvqueReturn {
	private String entid;           	 //企业号
	private String sheetid;              //小票唯一号,只记录退货小票,按整单退                  
	private String sheettype;            //单据类型，1:电票；7:费用                      
	private String invoicelx;            //发票类型，电子票写死为026                      
	private String status;               //状态 0=待提取 10=已提取 100=已开票             
	private String shopid;               //门店号                                 
	private String syjid;                //收银机号                                
	private String billno;               //小票号                                 
	private String sdate;                //售卖时间                                
	private String rtfpdm;               //发票代码                                
	private String rtfphm;               //发票号码                                
	private String rtkprq;               //开票日期                                
	private String iqgmftax;             //购方税号                                
	private String iqgmfname;            //购方名称                                
	private String iqgmfadd;             //购方地址、电话                             
	private String iqgmfbank;            //购方银行、账号                             
	private String iqfplxdm;             //发票种类                                
	private String iqtype;               //开票类型                                
	private String iqstatus;             //开票状态                                
	private String iqmsg;                //开票结果                                
	private String zsfs;                 //发票状态                                
	private String extsheetid;           //相关单号，填空                             
	private String refsheetid;           //原小票号,如果值为0表示无单退货                    
	private String refshopid;            //原门店号                                
	private String refsyjid;             //原收银机号                               
	private String refbillno;            //原小票号                                
	private String refinvoicecode;       //原发票代码                               
	private String refinvoiceno;         //原发票号码                               
	private String refinvoicedate;       //原开票日期                               
	private String flagmsg;              //提取信息                                
	private String createtime;           //数据写入时间  
	private String hzfpxxbbh;			 //红字信息表编号
	
	public String getHzfpxxbbh() {
		return hzfpxxbbh;
	}
	public void setHzfpxxbbh(String hzfpxxbbh) {
		this.hzfpxxbbh = hzfpxxbbh;
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
	public String getInvoicelx() {
		return invoicelx;
	}
	public void setInvoicelx(String invoicelx) {
		this.invoicelx = invoicelx;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
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
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
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
	public String getIqgmftax() {
		return iqgmftax;
	}
	public void setIqgmftax(String iqgmftax) {
		this.iqgmftax = iqgmftax;
	}
	public String getIqgmfname() {
		return iqgmfname;
	}
	public void setIqgmfname(String iqgmfname) {
		this.iqgmfname = iqgmfname;
	}
	public String getIqgmfadd() {
		return iqgmfadd;
	}
	public void setIqgmfadd(String iqgmfadd) {
		this.iqgmfadd = iqgmfadd;
	}
	public String getIqgmfbank() {
		return iqgmfbank;
	}
	public void setIqgmfbank(String iqgmfbank) {
		this.iqgmfbank = iqgmfbank;
	}
	public String getIqfplxdm() {
		return iqfplxdm;
	}
	public void setIqfplxdm(String iqfplxdm) {
		this.iqfplxdm = iqfplxdm;
	}
	public String getIqtype() {
		return iqtype;
	}
	public void setIqtype(String iqtype) {
		this.iqtype = iqtype;
	}
	public String getIqstatus() {
		return iqstatus;
	}
	public void setIqstatus(String iqstatus) {
		this.iqstatus = iqstatus;
	}
	public String getIqmsg() {
		return iqmsg;
	}
	public void setIqmsg(String iqmsg) {
		this.iqmsg = iqmsg;
	}
	public String getZsfs() {
		return zsfs;
	}
	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
	}
	public String getExtsheetid() {
		return extsheetid;
	}
	public void setExtsheetid(String extsheetid) {
		this.extsheetid = extsheetid;
	}
	public String getRefsheetid() {
		return refsheetid;
	}
	public void setRefsheetid(String refsheetid) {
		this.refsheetid = refsheetid;
	}
	public String getRefshopid() {
		return refshopid;
	}
	public void setRefshopid(String refshopid) {
		this.refshopid = refshopid;
	}
	public String getRefsyjid() {
		return refsyjid;
	}
	public void setRefsyjid(String refsyjid) {
		this.refsyjid = refsyjid;
	}
	public String getRefbillno() {
		return refbillno;
	}
	public void setRefbillno(String refbillno) {
		this.refbillno = refbillno;
	}
	public String getRefinvoicecode() {
		return refinvoicecode;
	}
	public void setRefinvoicecode(String refinvoicecode) {
		this.refinvoicecode = refinvoicecode;
	}
	public String getRefinvoiceno() {
		return refinvoiceno;
	}
	public void setRefinvoiceno(String refinvoiceno) {
		this.refinvoiceno = refinvoiceno;
	}
	public String getRefinvoicedate() {
		return refinvoicedate;
	}
	public void setRefinvoicedate(String refinvoicedate) {
		this.refinvoicedate = refinvoicedate;
	}
	public String getFlagmsg() {
		return flagmsg;
	}
	public void setFlagmsg(String flagmsg) {
		this.flagmsg = flagmsg;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}  
}
