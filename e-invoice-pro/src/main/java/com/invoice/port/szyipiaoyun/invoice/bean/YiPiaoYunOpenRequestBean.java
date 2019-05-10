package com.invoice.port.szyipiaoyun.invoice.bean;

import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;

public class YiPiaoYunOpenRequestBean {
	@JSONField(name="buyerAddr")
    private String buyeraddr;
	@JSONField(name="buyerBankName")
    private String buyerbankname;
	@JSONField(name="buyerBankNum")
    private String buyerbanknum;
    @JSONField(name="buyerEmail")
    private String buyeremail;
    @JSONField(name="buyerMobile")
    private String buyermobile;
    @JSONField(name="buyerName")
    private String buyername;
    @JSONField(name="buyerProvince")
    private String buyerprovince;
    @JSONField(name="buyerTaxNo")
    private String buyertaxno;
    @JSONField(name="buyerTele")
    private String buyertele;
    @JSONField(name="buyerType")
    private String buyertype;
    private String drawer;
    @JSONField(name="machineNo")
    private String machineno;
    @JSONField(name="majorItems")
    private String majoritems;
    @JSONField(name="manualOrderDetails")
    private List<YiPiaoYunManualorderdetails> manualorderdetails;
    @JSONField(name="noTaxAmount")
    private String notaxamount;
    @JSONField(name="orderNo")
    private String orderno;
    private String payee;
    private String remarks;
    private String reviewer;
    @JSONField(name="sellerBankAcc")
    private String sellerbankacc;
    @JSONField(name="taxTotal")
    private String taxtotal;
    @JSONField(name="totalAmountTax")
    private String totalamounttax;
    
    public void setBuyeraddr(String buyeraddr) {
         this.buyeraddr = buyeraddr;
    }
    
    public String getBuyeraddr() {
         return buyeraddr;
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

    public void setBuyeremail(String buyeremail) {
         this.buyeremail = buyeremail;
    }
    
    public String getBuyeremail() {
         return buyeremail;
    }

    public void setBuyermobile(String buyermobile) {
         this.buyermobile = buyermobile;
    }
    
    public String getBuyermobile() {
         return buyermobile;
    }

    public void setBuyername(String buyername) {
         this.buyername = buyername;
    }
    
    public String getBuyername() {
         return buyername;
    }

    public void setBuyerprovince(String buyerprovince) {
         this.buyerprovince = buyerprovince;
    }
    
    public String getBuyerprovince() {
         return buyerprovince;
    }

    public void setBuyertaxno(String buyertaxno) {
         this.buyertaxno = buyertaxno;
    }
    
    public String getBuyertaxno() {
         return buyertaxno;
    }

    public void setBuyertele(String buyertele) {
         this.buyertele = buyertele;
    }
    
    public String getBuyertele() {
         return buyertele;
    }

    public void setBuyertype(String buyertype) {
         this.buyertype = buyertype;
    }
    
    public String getBuyertype() {
         return buyertype;
    }

    public void setDrawer(String drawer) {
         this.drawer = drawer;
    }
    
    public String getDrawer() {
         return drawer;
    }

    public void setMachineno(String machineno) {
         this.machineno = machineno;
    }
    
    public String getMachineno() {
         return machineno;
    }

    public void setMajoritems(String majoritems) {
         this.majoritems = majoritems;
    }
    
    public String getMajoritems() {
         return majoritems;
    }

    public void setManualorderdetails(List<YiPiaoYunManualorderdetails> manualorderdetails) {
         this.manualorderdetails = manualorderdetails;
    }
    
    public List<YiPiaoYunManualorderdetails> getManualorderdetails() {
         return manualorderdetails;
    }

    public void setNotaxamount(String notaxamount) {
         this.notaxamount = notaxamount;
    }
    
    public String getNotaxamount() {
         return notaxamount;
    }

    public void setOrderno(String orderno) {
         this.orderno = orderno;
    }
    
    public String getOrderno() {
         return orderno;
    }

    public void setPayee(String payee) {
         this.payee = payee;
    }
    
    public String getPayee() {
         return payee;
    }

    public void setRemarks(String remarks) {
         this.remarks = remarks;
    }
    
    public String getRemarks() {
         return remarks;
    }

    public void setReviewer(String reviewer) {
         this.reviewer = reviewer;
    }
    
    public String getReviewer() {
         return reviewer;
    }

    public void setSellerbankacc(String sellerbankacc) {
         this.sellerbankacc = sellerbankacc;
    }
    
    public String getSellerbankacc() {
         return sellerbankacc;
    }

    public void setTaxtotal(String taxtotal) {
         this.taxtotal = taxtotal;
    }
    
    public String getTaxtotal() {
         return taxtotal;
    }

    public void setTotalamounttax(String totalamounttax) {
         this.totalamounttax = totalamounttax;
    }
    
    public String getTotalamounttax() {
         return totalamounttax;
    }
}
