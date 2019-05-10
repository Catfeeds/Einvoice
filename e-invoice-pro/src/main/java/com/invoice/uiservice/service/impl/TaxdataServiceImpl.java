package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.uiservice.dao.TaxdataDao;
import com.invoice.uiservice.service.TaxdataService;

@Service("TaxdataService")
public class TaxdataServiceImpl implements TaxdataService{

	@Resource(name = "TaxdataDao")
	TaxdataDao taxdataDao;
	
	@Override
	public List<HashMap<String, String>> gettaxdata(
			Map<String, Object> p) {

		return taxdataDao.gettaxdata(p);
	}

	@Override
	public int getTaxdataCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return taxdataDao.getTaxdataCount(p);
	}

}
