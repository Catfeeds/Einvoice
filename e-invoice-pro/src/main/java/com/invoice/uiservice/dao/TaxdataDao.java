package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component("TaxdataDao")
public interface TaxdataDao {

	List<HashMap<String,String>> gettaxdata(Map<String, Object> p);
	
	int getTaxdataCount(Map<String, Object> p);
}
