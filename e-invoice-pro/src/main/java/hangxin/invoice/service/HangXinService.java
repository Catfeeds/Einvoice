package hangxin.invoice.service;

import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;

import hangxin.invoice.bean.HangXinRtInvoiceBean;

public interface HangXinService {
	
	public HangXinRtInvoiceBean openinvoice(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn);
	
}
