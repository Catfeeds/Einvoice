package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.InvoiceFlagLog;

public interface InvoiceFlagLogDao {
	
	List<HashMap<String,String>> queryInvoiceFlagLog(Map<String, Object> p) throws Exception;
	
	int getInvoiceFlagLogcount(Map<String, Object> p);
	
	public int insertInvoiceFlagLog(InvoiceFlagLog log);
}
