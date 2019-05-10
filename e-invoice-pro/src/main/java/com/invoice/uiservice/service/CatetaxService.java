package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Category;
import com.invoice.bean.db.Catetax;
import com.invoice.bean.db.Taxitem;

public interface CatetaxService {
    public void insertCatetax(Catetax catetax) throws Exception;
    
    public void updateCatetax(Catetax catetax) throws Exception;
    
    public void deleteCatetax(Catetax catetax) throws Exception;
    
    public  List<Catetax> getCatetaxById(Map<String, Object> p) throws Exception;
    
    public List<Category> queryCategory(Category ca) throws Exception;
    
    int getCatetaxCount(Map<String, Object> p);
    
    public void addCatetaxList(List<Catetax> list) throws Exception;
    
    public void addCategoryList(List<Category> list) throws Exception;
}
