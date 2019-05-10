package com.invoice.uiservice.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.Catedis;
import com.invoice.bean.db.Catetax;

@Component("CateDisDao")
public interface CateDisDao {
	
    public List<Catedis> queryCatedis(Map<String, Object> p) throws Exception;
    
    int getCatedisCount(Map<String, Object> p);
    
    public void insertCateDis(Catedis catetax) throws Exception;
    
    public List<Catedis> getCatedisById(Map<String, Object> p);
    
    public void deleteCateDis(Catedis catetax) throws Exception;
}
