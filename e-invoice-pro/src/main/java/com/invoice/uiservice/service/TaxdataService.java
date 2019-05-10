package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaxdataService {
	
	List<HashMap<String,String>> gettaxdata(Map<String, Object> p);
	
	int getTaxdataCount(Map<String, Object> p);
}
