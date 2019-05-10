package com.invoice.uiservice.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.ui.InvoiceListView;

@Component("InvoiceListDao")
public interface InvoiceListDao {
    
	  public List<InvoiceListView> queryInvoiceList(Map<String, Object> p) throws Exception;
	    
	  public InvoiceHead getInvoiceHeadById(InvoiceHead invoiceHead) throws Exception;
	    
	  int getInvoiceListCount(Map<String, Object> p);
}
