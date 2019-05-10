package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.Taxitem;

@Component("TaxitemDao")
public interface TaxitemDao {
    public void insertTaxitem(Taxitem taxitem) throws Exception;

    public void addTaxitemByExcel(Taxitem taxitem) throws Exception;

    public void updateTaxitem(Taxitem taxitem) throws Exception;
    
    public void deleteTaxitem(Taxitem taxitem) throws Exception;
    
    public List<Taxitem> queryTaxitem(Map<String, Object> p) throws Exception;
    
    public  List<HashMap<String,String>> getTaxitemById(Taxitem taxitem) throws Exception;
    
    int getTaxitemCount(Map<String, Object> p);
    
    public void addTaxitemList(List<Taxitem> list) throws Exception;

    Taxitem getTaxitemByTaxitemId(Taxitem taxitem) throws Exception;

}
