package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.Option;

public interface TaxinfoService {
	
    public void insertTaxinfo(Taxinfo taxinfo) throws Exception;
    
    public void updateTaxinfo(Taxinfo taxinfo) throws Exception;
    
    public void deleteTaxinfo(Taxinfo taxinfo) throws Exception;
    
    List<HashMap<String,String>> queryTaxinfo(Map<String, Object> p) throws Exception;
    
    Taxinfo getTaxinfoByNo(Taxinfo taxinfo) throws Exception;
   
    Taxinfo getTaxinfoItemByNo(Taxinfo taxinfo) throws Exception;
    
    int getTaxinfoCount(Map<String, Object> p);
    
    List<Option> getTaxnoSelect(String entid);
}
