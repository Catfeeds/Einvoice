package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.Enterprise;

@Component("EnterpriseDao")
public interface EnterpriseDao {
	
    public void insertEnterprise(Enterprise enterprise) throws Exception;

    public void updateEnterprise(Enterprise enterprise) throws Exception;

    public void deleteEnterprise(Enterprise enterprise) throws Exception;

    List<HashMap<String,String>> getEnterprise(Map<String, Object> p);
	
	public Enterprise getEnterpriseById(Enterprise enterprise);
	
	int getEnterpriseCount(Map<String, Object> p);
}
