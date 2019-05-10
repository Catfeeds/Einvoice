package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Paymode;

public interface PaymodeService {
    public void insertPaymode(Paymode paymode) throws Exception;
    
    public void updatePaymode(Paymode paymode) throws Exception;
    
    public void deletePaymode(Paymode paymode) throws Exception;
    
    public List<HashMap<String,String>> queryPaymode(Map<String, Object> p) throws Exception;
    
    public Paymode getPaymodeById(Paymode paymode) throws Exception;
    
    int getPaymodeCount(Map<String, Object> p);
    
    public List<HashMap<String,String>> queryPaymodelog(Map<String, Object> p) throws Exception;
    
    int getPaymodeCountlog(Map<String, Object> p);
    
    
}
