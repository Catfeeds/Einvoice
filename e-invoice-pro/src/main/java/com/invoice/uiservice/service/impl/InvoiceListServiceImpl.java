package com.invoice.uiservice.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.ui.InvoiceListView;
import com.invoice.uiservice.dao.InvoiceListDao;
import com.invoice.uiservice.service.InvoiceListService;

@Service("InvoiceListService")
public class InvoiceListServiceImpl implements InvoiceListService{

	@Resource(name = "InvoiceListDao")
	InvoiceListDao invoiceListDao;
	

	@Override
	public List<InvoiceListView> queryInvoiceList(Map<String, Object> p) throws Exception {
		return invoiceListDao.queryInvoiceList(p);
	}

	@Override
	public InvoiceHead getInvoiceHeadById(InvoiceHead invoiceHead) throws Exception {
		
		return invoiceListDao.getInvoiceHeadById(invoiceHead);
	}

	@Override
	public int getInvoiceListCount(Map<String, Object> p) {
		return invoiceListDao.getInvoiceListCount(p);
	}
}
