package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Enterprise;

public interface EnterpriseService {
	
	void addEnterprise(Enterprise enterprise) throws Exception;
	
	void updateEnterprise(Enterprise enterprise) throws Exception;
	
	void deleteEnterprise(Enterprise enterprise) throws Exception;
	
	 List<HashMap<String,String>> getEnterprise(Map<String, Object> p)throws Exception;
		
    Enterprise getEnterpriseById(Enterprise enterprise);
    
    int getEnterpriseCount(Map<String, Object> p);
}
