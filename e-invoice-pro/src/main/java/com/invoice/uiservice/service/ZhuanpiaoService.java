package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Paymode;

public interface ZhuanpiaoService {

    
    public List<HashMap<String,String>> queryinvoiceFlaglog(Map<String, Object> p) throws Exception;
    
    int getinvoiceFlaglogCountlog(Map<String, Object> p);
    
    
}
