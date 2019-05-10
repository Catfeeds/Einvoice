package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Goodsdis;

public interface GoodsdisService {
    public void insertGoodsdis(Goodsdis goodsdis) throws Exception;
    
    public void updateGoodsdis(Goodsdis goodsdis) throws Exception;
    
    public void deleteGoodsdis(Goodsdis goodsdis) throws Exception;
    
    public List<HashMap<String,String>> queryGoodsdis(Map<String, Object> p) throws Exception;
    
    public Goodsdis getGoodsdisById(Goodsdis goodsdis) throws Exception;
    
    int getGoodsdisCount(Map<String, Object> p);
    
    public void addGoodsdisList(List<Goodsdis> list) throws Exception;
}
