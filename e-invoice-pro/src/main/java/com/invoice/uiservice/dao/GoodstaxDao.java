package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.Goodstax;
import com.invoice.bean.db.Taxitem;

@Component("GoodstaxDao")
public interface GoodstaxDao {
    public int insertGoodstax(Goodstax goodstax) throws Exception;
    
    public void updateGoodstax(Goodstax goodstax) throws Exception;
    
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
    
    public int insertGoodstaxlog(Goodstax goodstax) throws Exception;
    
    public Taxitem getTaxitemById(String taxitemid);
}
