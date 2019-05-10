package baiwang.invoice.service;

import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;

import baiwang.invoice.bean.BaiWangRtBlankBean;
import baiwang.invoice.bean.BaiWangRtInvoiceBean;
import baiwang.invoice.bean.FpttBean;
import baiwang.invoice.bean.RtInvoiceHeadList;

public interface BaiWangService {
	
	public BaiWangRtInvoiceBean openinvoice(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn);
	
	public BaiWangRtInvoiceBean invoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	public String getPdf(Invque invque,RtnData rtn);
	
	public void login(String xml,RtnData rtn );
	
	public String access_fptt(FpttBean fptt,RtnData rtn);
	
	public BaiWangRtBlankBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	public BaiWangRtInvoiceBean invoiceInblankValid(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	public RtInvoiceHeadList findInvoice(Invque invque,RtnData rtn);
}
