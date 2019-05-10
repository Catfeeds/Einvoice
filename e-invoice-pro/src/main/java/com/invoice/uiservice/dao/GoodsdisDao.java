package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.Goodsdis;

@Component("GoodsdisDao")
public interface GoodsdisDao {
    public void insertGoodsdis(Goodsdis goodsdis) throws Exception;
    
    public void updateGoodsdis(Goodsdis goodsdis) throws Exception;
    
    public void deleteGoodsdis(Goodsdis goodsdis) throws Exception;
    
    List<HashMap<String,String>> queryGoodsdis(Map<String, Object> p) throws Exception;
    
    public Goodsdis getGoodsdisById(Goodsdis goodsdis) throws Exception;
    
    int getGoodsdisCount(Map<String, Object> p);
    
    public void addGoodsdisList(List<Goodsdis> list) throws Exception;
}
