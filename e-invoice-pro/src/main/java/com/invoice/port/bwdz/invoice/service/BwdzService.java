package com.invoice.port.bwdz.invoice.service;

import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.bwdz.invoice.bean.BwdzRtOpenInvoiceBean;
import com.invoice.rtn.data.RtnData;

public interface BwdzService {
	
	public BwdzRtOpenInvoiceBean openinvoice(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn);
	
	public BwdzRtOpenInvoiceBean findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn);
}
