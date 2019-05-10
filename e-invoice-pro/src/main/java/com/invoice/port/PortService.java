package com.invoice.port;

import java.util.List;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.rtn.data.RtnData;

public interface PortService {
	
	/**
	 * 开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	
	public void openInvoice(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList);

	/**
	 * 空白票查询
	 * @param que
	 * @param taxinfo
	 */
	
	public RtInvoiceBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	/**
	 * 空白发票作废
	 * **/
	public RtInvoiceBean invoiceInblankValid(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	/**
	 * 已开发票作废
	 * **/
	public RtInvoiceBean invoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	
	/**
	 * 发票打印
	 * **/
	
	public void invoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	
	/**
	 * 获取发票PDF连接 
	 * **/
	public String getPdf(Invque invque,RtnData rtn);
	
	/**
	 * 发票查询
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	
	public RtInvoiceBean findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
}
