package com.invoice.apiservice.dao;

import java.util.Map;

import com.invoice.bean.db.PreInvoiceAsk;

public interface PreInvoiceDao {
	public PreInvoiceAsk getPreInvoiceAsk(Map<String,Object> p);
	
	public int insertPreInvoiceAsk(PreInvoiceAsk p);
	
	public int updatePreInvoiceAsk(PreInvoiceAsk p);
}
