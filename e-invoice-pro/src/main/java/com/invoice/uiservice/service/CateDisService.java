package com.invoice.uiservice.service;

import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Catedis;

public interface CateDisService {
	  	public List<Catedis> queryCatedis(Map<String, Object> p) throws Exception;
	    
	  	public int getCatedisCount(Map<String, Object> p);
	    
	    public void insertCateDis(Catedis catetax) throws Exception;

	    public List<Catedis> getCatedisById(Map<String, Object> p);
	    
	    public void deleteCateDis(Catedis catetax) throws Exception;
}
