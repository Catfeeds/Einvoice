package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Goodstax;
import com.invoice.bean.db.Taxitem;

public interface GoodstaxService {
    public int insertGoodstax(Goodstax goodstax) throws Exception;
    
    public int updateGoodstax(Goodstax goodstax) throws Exception;
    
    public void deleteGoodstax(Goodstax goodstax) throws Exception;
    
    public List<HashMap<String,String>> queryGoodstax(Map<String, Object> p) throws Exception;
    
    public Goodstax getGoodstaxById(Goodstax goodstax) throws Exception;
    
    int getGoodstaxCount(Map<String, Object> p);
    
    public void addGoodstaxList(List<Goodstax> list) throws Exception;
    
    public List<HashMap<String,String>> getBillsalereport(Map<String, Object> p) throws Exception;
    
    int getBillsalereportCount(Map<String, Object> p);
    
    public List<HashMap<String,String>> getGoodstaxByIds(Map<String, Object> p) throws Exception;
    
    public List<HashMap<String,String>> queryGoodstaxlog(Map<String, Object> p) throws Exception;
    
    int getGoodstaxCountlog(Map<String, Object> p);
    
    public Taxitem getTaxitemById(String taxitemid);
}
