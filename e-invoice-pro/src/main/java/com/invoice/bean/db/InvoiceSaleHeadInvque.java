package com.invoice.bean.db;

import java.math.BigDecimal;

public class InvoiceSaleHeadInvque {
    private String billno;
    private String iqdate;
    private String totalAmount;
    private String invoiceAmount;
    private String totalTaxFee;
    private String shopName;
    private String iqfplxdm;

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getIqdate() {
        return iqdate;
    }

    public void setIqdate(String iqdate) {
        this.iqdate = iqdate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getTotalTaxFee() {
        return totalTaxFee;
    }

    public void setTotalTaxFee(String totalTaxFee) {
        this.totalTaxFee = totalTaxFee;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getIqfplxdm() {
        return iqfplxdm;
    }

    public void setIqfplxdm(String iqfplxdm) {
        this.iqfplxdm = iqfplxdm;
    }

}
