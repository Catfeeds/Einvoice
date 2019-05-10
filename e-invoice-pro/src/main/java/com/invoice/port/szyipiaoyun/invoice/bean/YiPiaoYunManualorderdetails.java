package com.invoice.port.szyipiaoyun.invoice.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class YiPiaoYunManualorderdetails {
	private String amount;
    @JSONField(name="dataMark")
    private int datamark;
    @JSONField(name="invoiceNature")
    private int invoicenature;
    @JSONField(name="itemName")
    private String itemname;
    @JSONField(name="itemNum")
    private String itemnum;
    @JSONField(name="itemPrice")
    private String itemprice;
    @JSONField(name="itemTaxCode")
    private String itemtaxcode;
    @JSONField(name="itemUnit")
    private String itemunit;
    private String lslbs;
    @JSONField(name="rowNum")
    private int rownum;
    @JSONField(name="specMode")
    private String specmode;
    private String tax;
    @JSONField(name="taxIncluded")
    private String taxincluded;
    @JSONField(name="taxRate")
    private String taxrate;
    private String yhzcbs;
    private String zzstsgl;
    
    public void setAmount(String amount) {
    	this.amount = amount;
    }
    
    public String getAmount() {
         return amount;
    }

    public void setDatamark(int datamark) {
         this.datamark = datamark;
    }
    
    public int getDatamark() {
         return datamark;
    }

    public void setInvoicenature(int invoicenature) {
         this.invoicenature = invoicenature;
    }
    
    public int getInvoicenature() {
         return invoicenature;
    }

    public void setItemname(String itemname) {
         this.itemname = itemname;
    }
    
    public String getItemname() {
         return itemname;
    }

    public void setItemnum(String itemnum) {
         this.itemnum = itemnum;
    }
    
    public String getItemnum() {
         return itemnum;
    }

    public void setItemprice(String itemprice) {
         this.itemprice = itemprice;
    }
    
    public String getItemprice() {
         return itemprice;
    }

    public void setItemtaxcode(String itemtaxcode) {
         this.itemtaxcode = itemtaxcode;
    }
    
    public String getItemtaxcode() {
         return itemtaxcode;
    }

    public void setItemunit(String itemunit) {
         this.itemunit = itemunit;
    }
    
    public String getItemunit() {
         return itemunit;
    }

    public void setLslbs(String lslbs) {
         this.lslbs = lslbs;
    }
    
    public String getLslbs() {
         return lslbs;
    }

    public void setRownum(int rownum) {
         this.rownum = rownum;
    }
    
    public int getRownum() {
         return rownum;
    }

    public void setSpecmode(String specmode) {
         this.specmode = specmode;
    }
    
    public String getSpecmode() {
         return specmode;
    }

    public void setTax(String tax) {
         this.tax = tax;
    }
    
    public String getTax() {
         return tax;
    }

    public void setTaxincluded(String taxincluded) {
         this.taxincluded = taxincluded;
    }
    
    public String getTaxincluded() {
         return taxincluded;
    }

    public void setTaxrate(String taxrate) {
         this.taxrate = taxrate;
    }

    public String getTaxrate() {
         return taxrate;
    }

    public void setYhzcbs(String yhzcbs) {
         this.yhzcbs = yhzcbs;
    }
    
    public String getYhzcbs() {
         return yhzcbs;
    }

    public void setZzstsgl(String zzstsgl) {
         this.zzstsgl = zzstsgl;
    }
    
    public String getZzstsgl() {
         return zzstsgl;
    }
}
