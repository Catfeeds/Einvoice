package com.invoice.apiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.InvoiceDetail;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;

public interface InvoiceDao {
	public int insertInvoiceHead(InvoiceHead head);
	
	public int updateInvoiceHead(InvoiceHead head);

	public int insertInvoiceDetail(List<InvoiceDetail> detail);
	
	public int getInvoiceCount(Map<String, Object> p);
	
	public int getIsInvoiceCount(Map<String, Object> p);
	
	public int updateInvoiceStatus(Map<String, Object> p);
	
	public int updateInvoicePdf(Map<String, Object> p);
	
	public List<InvoiceHead> getInvoiceHeadByYfp(Map<String, Object> p);
	
	public List<HashMap<String,Object>> getInvoiceHead(Map<String, Object> p);
	
	public int getInvoiceHeadCount(Map<String, Object> p);
	
	public List<InvoiceDetail> getInvoiceDetail(Map<String, Object> p);
	
	public List<InvoiceSaleDetail> getHongInvoiceDetail(Map<String, Object> p);
	
	public int getInvoiceDetailCount(Map<String, Object> p);
	
	public List<HashMap<String,String>> getInvoiceDetailForSum(Map<String, Object> p) throws Exception;
	
	public int getInvoiceDetailForSumCount(Map<String, Object> p);
	
	public HashMap<String,String> getDetailForSumAmt(Map<String, Object> p) throws Exception;

	public List<InvoiceHead> queryInvoiceHead(Map<String, Object> p);

	public int queryInvoiceHeadCount(Map<String, Object> p);
	
	public int getbillreportCount(Map<String, Object> p);
	
	public List<HashMap<String,String>> getbillreport(Map<String, Object> p) throws Exception;
	
	public HashMap<String,String> getbillreportForsum(Map<String, Object> p) throws Exception;
	
	public List<HashMap<String,Object>> getHnyjHead(Map<String, Object> p);
	
	public int getHnyjHeadCount(Map<String, Object> p);
	
	public List<InvoiceHead> queryInvoiceHeadByCallGD(Map<String, Object> p);
	
	public int updateInvoiceHeadForCallGD(InvoiceHead head);
}
