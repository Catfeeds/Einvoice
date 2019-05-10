package com.invoice.apiservice.dao;

import java.util.List;
import java.util.Map;

import com.invoice.bean.db.OpenapiSaleDetail;
import com.invoice.bean.db.OpenapiSaleHead;
import com.invoice.bean.db.OpenapiSalePay;
import com.invoice.bean.ui.RequestOpenApiSearch;
import com.invoice.bean.ui.ResponseOpenApiSearch;

public interface OpenapiDao {
	public void insertSaleHead(OpenapiSaleHead head);
	public void insertSaleDetail(OpenapiSaleDetail detail);
	public void insertSalePay(OpenapiSalePay pay);
	public OpenapiSaleHead getSaleHead(OpenapiSaleHead head);
	public int updateSaleHead(OpenapiSaleHead head);
	public List<ResponseOpenApiSearch> searchInvoice(RequestOpenApiSearch request);
	public List<Map<String, Object>> searchInvoiceItem(String invoiceid);
}
