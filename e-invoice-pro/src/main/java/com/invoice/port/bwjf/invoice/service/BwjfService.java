package com.invoice.port.bwjf.invoice.service;

import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.bwjf.invoice.bean.BwjfDzRtInvoiceBean;
import com.invoice.port.bwjf.invoice.bean.BwjfRtInvoiceBean;
import com.invoice.rtn.data.RtnData;

public interface BwjfService {
	
	public void openinvoicezp(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn);
	
	public BwjfRtInvoiceBean rtZpOpenToBean(String xml,RtnData rtn) ;
	
	public BwjfDzRtInvoiceBean openinvoice(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn);
	
	public String getPdf(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	public String getHttpConnectResult(String xml, String address,RtnData rtn);
	
	public String blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	public String urlconnnet(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	public String zp_print(Invque invque, Taxinfo taxinfo,RtnData rtn);
	
	public String  invoiceInblankValid(Invque invque, Taxinfo taxinfo,RtnData rtn);
	
	public String  invoiceYkInValid(Invque invque, Taxinfo taxinfo,RtnData rtn);
}
