package com.invoice.uiservice.service;

import java.util.List;
import java.util.Map;

import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.ui.InvoiceListView;

public interface InvoiceListService {
    
    public List<InvoiceListView> queryInvoiceList(Map<String, Object> p) throws Exception;
    
    public InvoiceHead getInvoiceHeadById(InvoiceHead invoiceHead) throws Exception;
    
    int getInvoiceListCount(Map<String, Object> p);
}
