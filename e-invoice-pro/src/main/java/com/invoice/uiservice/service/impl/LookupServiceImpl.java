package com.invoice.uiservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.invoice.bean.ui.Option;
import com.invoice.uiservice.dao.LookupDao;

@Service
public class LookupServiceImpl {
	@Autowired
	LookupDao dao;

	public List<Option> getLookupSelect(String lookupid) {
		return dao.getLookupSelect(lookupid);
	}
}
