package com.invoice.port.szyipiaoyun.invoice.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class YiPiaoYunFindResponseBean {
	@JSONField(name="operateCode")
    private String operatecode;
    private String message;
    private List<Datas> datas;
    @JSONField(name="pageAndSort")
    private Pageandsort pageandsort;
    
    public void setOperatecode(String operatecode) {
         this.operatecode = operatecode;
    }
    
    public String getOperatecode() {
         return operatecode;
    }

    public void setMessage(String message) {
         this.message = message;
    }
    
    public String getMessage() {
         return message;
    }

    public void setDatas(List<Datas> datas) {
         this.datas = datas;
    }
    
    public List<Datas> getDatas() {
         return datas;
    }

    public void setPageandsort(Pageandsort pageandsort) {
         this.pageandsort = pageandsort;
    }
    
    public Pageandsort getPageandsort() {
         return pageandsort;
    }
     
    public class Pageandsort {
	    private int page;
	    private int rp;
	    @JSONField(name="rowCount")
	    private int rowcount;
	    @JSONField(name="startIndex")
	    private int startindex;
	    @JSONField(name="sortName")
	    private String sortname;
	    @JSONField(name="sortOrder")
	    private String sortorder;
	    @JSONField(name="totalPage")
	    private int totalpage;
	    @JSONField(name="sortString")
	    private String sortstring;
	    
	    public void setPage(int page) {
	         this.page = page;
	    }
	    
	    public int getPage() {
	         return page;
	    }

	    public void setRp(int rp) {
	         this.rp = rp;
	    }
	    
	    public int getRp() {
	         return rp;
	    }

	    public void setRowcount(int rowcount) {
	         this.rowcount = rowcount;
	    }
	    
	    public int getRowcount() {
	         return rowcount;
	    }

	    public void setStartindex(int startindex) {
	         this.startindex = startindex;
	    }
	    
	    public int getStartindex() {
	         return startindex;
	    }

	    public void setSortname(String sortname) {
	         this.sortname = sortname;
	    }
	    
	    public String getSortname() {
	         return sortname;
	    }

	    public void setSortorder(String sortorder) {
	         this.sortorder = sortorder;
	    }
	    
	    public String getSortorder() {
	         return sortorder;
	    }

	    public void setTotalpage(int totalpage) {
	         this.totalpage = totalpage;
	    }
	    
	    public int getTotalpage() {
	         return totalpage;
	    }

	    public void setSortstring(String sortstring) {
	         this.sortstring = sortstring;
	    }
	    
	    public String getSortstring() {
	         return sortstring;
	    }
    }    
     
    public class Datas {
	    @JSONField(name="buyerName")
	    private String buyername;
	    @JSONField(name="buyerTaxNo")
	    private String buyertaxno;
	    @JSONField(name="taxNo")
	    private String taxno;
	    private String usrno;
	    @JSONField(name="orderNo")
	    private String orderno;
	    @JSONField(name="buyerAddr")
	    private String buyeraddr;
	    @JSONField(name="buyerMobile")
	    private String buyermobile;
	    private Invoice invoice;
	    @JSONField(name="buyerEmail")
	    private String buyeremail;
	    @JSONField(name="machineNo")
	    private String machineno;
	    private String type;
	    @JSONField(name="buyerBankName")
	    private String buyerbankname;
	    @JSONField(name="buyerBankNum")
	    private String buyerbanknum;
	    @JSONField(name="buyerTele")
	    private String buyertele;
	    private String drawer;
	    private String payee;
	    private String reviewer;
	    private String remarks;
	    
	    public void setBuyername(String buyername) {
	         this.buyername = buyername;
	    }
	    
	    public String getBuyername() {
	         return buyername;
	    }

	    public void setBuyertaxno(String buyertaxno) {
	         this.buyertaxno = buyertaxno;
	    }
	    
	    public String getBuyertaxno() {
	         return buyertaxno;
	    }

	    public void setTaxno(String taxno) {
	         this.taxno = taxno;
	    }
	    
	    public String getTaxno() {
	         return taxno;
	    }

	    public void setUsrno(String usrno) {
	         this.usrno = usrno;
	    }
	    
	    public String getUsrno() {
	         return usrno;
	    }

	    public void setOrderno(String orderno) {
	         this.orderno = orderno;
	    }
	    
	    public String getOrderno() {
	         return orderno;
	    }

	    public void setBuyeraddr(String buyeraddr) {
	         this.buyeraddr = buyeraddr;
	    }
	    
	    public String getBuyeraddr() {
	         return buyeraddr;
	    }

	    public void setBuyermobile(String buyermobile) {
	         this.buyermobile = buyermobile;
	    }
	    
	    public String getBuyermobile() {
	         return buyermobile;
	    }

	    public void setInvoice(Invoice invoice) {
	         this.invoice = invoice;
	    }
	    
	    public Invoice getInvoice() {
	         return invoice;
	    }

	    public void setBuyeremail(String buyeremail) {
	         this.buyeremail = buyeremail;
	    }
	    
	    public String getBuyeremail() {
	         return buyeremail;
	    }

	    public void setMachineno(String machineno) {
	         this.machineno = machineno;
	    }
	    
	    public String getMachineno() {
	         return machineno;
	    }

	    public void setType(String type) {
	         this.type = type;
	    }
	    
	    public String getType() {
	         return type;
	    }

	    public void setBuyerbankname(String buyerbankname) {
	         this.buyerbankname = buyerbankname;
	    }
	    
	    public String getBuyerbankname() {
	         return buyerbankname;
	    }

	    public void setBuyerbanknum(String buyerbanknum) {
	         this.buyerbanknum = buyerbanknum;
	    }
	    
	    public String getBuyerbanknum() {
	         return buyerbanknum;
	    }

	    public void setBuyertele(String buyertele) {
	         this.buyertele = buyertele;
	    }
	    
	    public String getBuyertele() {
	         return buyertele;
	    }

	    public void setDrawer(String drawer) {
	         this.drawer = drawer;
	    }
	    
	    public String getDrawer() {
	         return drawer;
	    }

	    public void setPayee(String payee) {
	         this.payee = payee;
	    }
	    
	    public String getPayee() {
	         return payee;
	    }

	    public void setReviewer(String reviewer) {
	         this.reviewer = reviewer;
	    }
	    
	    public String getReviewer() {
	         return reviewer;
	    }

	    public void setRemarks(String remarks) {
	         this.remarks = remarks;
	    }
	    
	    public String getRemarks() {
	         return remarks;
	    }
	}
     
    public class Invoice {
	    @JSONField(name="serialNo")
	    private String serialno;
	    private List<Details> details;
	    @JSONField(name="projectName")
	    private String projectname;
	    @JSONField(name="taxTotal")
	    private String taxtotal;
	    @JSONField(name="noTaxTotal")
	    private String notaxtotal;
	    @JSONField(name="priceTotal")
	    private String pricetotal;
	    @JSONField(name="invoiceCode")
	    private String invoicecode;
	    @JSONField(name="invoiceNum")
	    private String invoicenum;
	    @JSONField(name="invoiceDate")
	    private Long invoicedate;
	    @JSONField(name="pdfUrl")
	    private String pdfurl;
	    @JSONField(name="invoiceType")
	    private String invoicetype;
	    @JSONField(name="reMarks")
	    private String remarks;
	    @JSONField(name="listFlag")
	    private String listflag;
	    @JSONField(name="serialNoReds")
	    private String serialnoreds;
	    private String status;
	    private String notice;
	    @JSONField(name="payStatus")
	    private String paystatus;
	    @JSONField(name="checkCode")
	    private String checkcode;
	    private String h5weburl;
	    
	    public void setSerialno(String serialno) {
	         this.serialno = serialno;
	    }
	    
	    public String getSerialno() {
	         return serialno;
	    }

	    public void setDetails(List<Details> details) {
	         this.details = details;
	    }
	    
	    public List<Details> getDetails() {
	         return details;
	    }

	    public void setProjectname(String projectname) {
	         this.projectname = projectname;
	    }
	    
	    public String getProjectname() {
	         return projectname;
	    }

	    public void setTaxtotal(String taxtotal) {
	         this.taxtotal = taxtotal;
	    }
	    
	    public String getTaxtotal() {
	         return taxtotal;
	    }

	    public void setNotaxtotal(String notaxtotal) {
	         this.notaxtotal = notaxtotal;
	    }
	    
	    public String getNotaxtotal() {
	         return notaxtotal;
	    }

	    public void setPricetotal(String pricetotal) {
	         this.pricetotal = pricetotal;
	    }
	    
	    public String getPricetotal() {
	         return pricetotal;
	    }

	    public void setInvoicecode(String invoicecode) {
	         this.invoicecode = invoicecode;
	    }
	    
	    public String getInvoicecode() {
	         return invoicecode;
	    }

	    public void setInvoicenum(String invoicenum) {
	         this.invoicenum = invoicenum;
	    }
	    
	    public String getInvoicenum() {
	         return invoicenum;
	    }

	    public void setInvoicedate(Long invoicedate) {
	         this.invoicedate = invoicedate;
	    }
	    
	    public Long getInvoicedate() {
	         return invoicedate;
	    }

	    public void setPdfurl(String pdfurl) {
	         this.pdfurl = pdfurl;
	    }
	    
	    public String getPdfurl() {
	         return pdfurl;
	    }

	    public void setInvoicetype(String invoicetype) {
	         this.invoicetype = invoicetype;
	    }
	    
	    public String getInvoicetype() {
	         return invoicetype;
	    }

	    public void setRemarks(String remarks) {
	         this.remarks = remarks;
	    }
	    
	    public String getRemarks() {
	         return remarks;
	    }

	    public void setListflag(String listflag) {
	         this.listflag = listflag;
	    }
	    
	    public String getListflag() {
	         return listflag;
	    }

	    public void setSerialnoreds(String serialnoreds) {
	         this.serialnoreds = serialnoreds;
	    }
	    
	    public String getSerialnoreds() {
	         return serialnoreds;
	    }

	    public void setStatus(String status) {
	         this.status = status;
	    }
	    
	    public String getStatus() {
	         return status;
	    }

	    public void setNotice(String notice) {
	         this.notice = notice;
	    }
	    
	    public String getNotice() {
	         return notice;
	    }

	    public void setPaystatus(String paystatus) {
	         this.paystatus = paystatus;
	    }
	    
	    public String getPaystatus() {
	         return paystatus;
	    }

	    public void setCheckcode(String checkcode) {
	         this.checkcode = checkcode;
	    }
	    
	    public String getCheckcode() {
	         return checkcode;
	    }

	    public void setH5weburl(String h5weburl) {
	         this.h5weburl = h5weburl;
	    }
	    
	    public String getH5weburl() {
	         return h5weburl;
	    }
	}  
     
    public class Details {
	    @JSONField(name="dataMark")
	    private int datamark;
	    @JSONField(name="rowNum")
	    private int rownum;
	    @JSONField(name="itemName")
	    private String itemname;
	    @JSONField(name="itemUnit")
	    private String itemunit;
	    @JSONField(name="itemNum")
	    private String itemnum;
	    @JSONField(name="specMode")
	    private String specmode;
	    @JSONField(name="itemPrice")
	    private String itemprice;
	    @JSONField(name="taxIncluded")
	    private String taxincluded;
	    @JSONField(name="invoiceNature")
	    private int invoicenature;
	    @JSONField(name="itemTaxCode")
	    private String itemtaxcode;
	    private String yhzcbs;
	    private String lslbs;
	    private String zzstsgl;
	    @JSONField(name="taxRate")
	    private String taxrate;
	    private String amount;
	    @JSONField(name="discountAmount")
	    private String discountamount;
	    private String tax;
	    @JSONField(name="discountLineObjId")
	    private String discountlineobjid;
	    
	    public void setDatamark(int datamark) {
	         this.datamark = datamark;
	    }
	    
	    public int getDatamark() {
	         return datamark;
	    }

	    public void setRownum(int rownum) {
	         this.rownum = rownum;
	    }
	    
	    public int getRownum() {
	         return rownum;
	    }

	    public void setItemname(String itemname) {
	         this.itemname = itemname;
	    }
	    
	    public String getItemname() {
	         return itemname;
	    }

	    public void setItemunit(String itemunit) {
	         this.itemunit = itemunit;
	    }
	    
	    public String getItemunit() {
	         return itemunit;
	    }

	    public void setItemnum(String itemnum) {
	         this.itemnum = itemnum;
	    }
	    
	    public String getItemnum() {
	         return itemnum;
	    }

	    public void setSpecmode(String specmode) {
	         this.specmode = specmode;
	    }
	    
	    public String getSpecmode() {
	         return specmode;
	    }

	    public void setItemprice(String itemprice) {
	         this.itemprice = itemprice;
	    }
	    
	    public String getItemprice() {
	         return itemprice;
	    }

	    public void setTaxincluded(String taxincluded) {
	         this.taxincluded = taxincluded;
	    }
	    
	    public String getTaxincluded() {
	         return taxincluded;
	    }

	    public void setInvoicenature(int invoicenature) {
	         this.invoicenature = invoicenature;
	    }
	    
	    public int getInvoicenature() {
	         return invoicenature;
	    }

	    public void setItemtaxcode(String itemtaxcode) {
	         this.itemtaxcode = itemtaxcode;
	    }
	    
	    public String getItemtaxcode() {
	         return itemtaxcode;
	    }

	    public void setYhzcbs(String yhzcbs) {
	         this.yhzcbs = yhzcbs;
	    }
	    
	    public String getYhzcbs() {
	         return yhzcbs;
	    }

	    public void setLslbs(String lslbs) {
	         this.lslbs = lslbs;
	    }
	    
	    public String getLslbs() {
	         return lslbs;
	    }

	    public void setZzstsgl(String zzstsgl) {
	         this.zzstsgl = zzstsgl;
	    }
	    
	    public String getZzstsgl() {
	         return zzstsgl;
	    }

	    public void setTaxrate(String taxrate) {
	         this.taxrate = taxrate;
	    }
	    public String getTaxrate() {
	         return taxrate;
	    }

	    public void setAmount(String amount) {
	         this.amount = amount;
	    }
	    
	    public String getAmount() {
	         return amount;
	    }

	    public void setDiscountamount(String discountamount) {
	         this.discountamount = discountamount;
	    }
	    
	    public String getDiscountamount() {
	         return discountamount;
	    }

	    public void setTax(String tax) {
	         this.tax = tax;
	    }
	    
	    public String getTax() {
	         return tax;
	    }

	    public void setDiscountlineobjid(String discountlineobjid) {
	         this.discountlineobjid = discountlineobjid;
	    }
	    
	    public String getDiscountlineobjid() {
	         return discountlineobjid;
	    }
	}
}


