package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.uiservice.dao.InvoiceFlagLogDao;
import com.invoice.uiservice.service.ZhuanpiaoService;
@Service("ZhuanpiaoService")
public class ZhuanpiaoServiceImpl implements ZhuanpiaoService {
	
	@Autowired
	InvoiceFlagLogDao invoiceFlagLogDao;
	
	@Override
	public List<HashMap<String, String>> queryinvoiceFlaglog(Map<String, Object> p) throws Exception {
		 
		return invoiceFlagLogDao.queryInvoiceFlagLog(p);
	}

	@Override
	public int getinvoiceFlaglogCountlog(Map<String, Object> p)  {
		 
		return invoiceFlagLogDao.getInvoiceFlagLogcount(p);
	}

}
