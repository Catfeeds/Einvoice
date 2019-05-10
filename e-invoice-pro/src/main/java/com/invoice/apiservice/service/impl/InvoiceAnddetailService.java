package com.invoice.apiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.apiservice.dao.InvoiceDao;
import com.invoice.bean.db.InvoiceDetail;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;

@Service
public class InvoiceAnddetailService {
	@Autowired
	InvoiceDao dao;
	
	
	public List<InvoiceHead> queryInvoiceHead(Map<String, Object> p){
		return dao.queryInvoiceHead(p);
	}
	
	public int queryInvoiceHeadCount(Map<String, Object> p){
		return dao.queryInvoiceHeadCount(p);
	}

	public List<HashMap<String,Object>> getInvoiceHead(Map<String, Object> p){
		return dao.getInvoiceHead(p);
	}
	
	public int getInvoiceHeadCount(Map<String, Object> p){
		return dao.getInvoiceHeadCount(p);
	}
	
	public List<InvoiceDetail> getInvoiceDetail(Map<String, Object> p){
		return dao.getInvoiceDetail(p);
	}
	public List<InvoiceSaleDetail> getHongInvoiceDetail(Map<String, Object> p){
		return dao.getHongInvoiceDetail(p);
	}
	public int getInvoiceDetailCount(Map<String, Object> p){
		return dao.getInvoiceDetailCount(p);
	}
	public List<HashMap<String,String>> getInvoiceDetailForSum(Map<String, Object> p) throws Exception{
		return dao.getInvoiceDetailForSum(p);
	}
	public int getInvoiceDetailForSumCount(Map<String, Object> p){
		return dao.getInvoiceDetailForSumCount(p);
	}
	public HashMap<String,String> getDetailForSumAmt(Map<String, Object> p) throws Exception{
		return dao.getDetailForSumAmt(p);
	}
	
	public int getbillreportCount(Map<String, Object> p){
		return dao.getbillreportCount(p);
	}
	public List<HashMap<String,String>> getbillreport(Map<String, Object> p) throws Exception{
		return dao.getbillreport(p);
	}
	public HashMap<String,String> getbillreportForsum(Map<String, Object> p) throws Exception{
		return dao.getbillreportForsum(p);
	}
	
	public List<HashMap<String,Object>> getHnyjHead(Map<String, Object> p){
		return dao.getHnyjHead(p);
	}
	
	public int getHnyjHeadCount(Map<String, Object> p){
		return dao.getHnyjHeadCount(p);
	}
}
