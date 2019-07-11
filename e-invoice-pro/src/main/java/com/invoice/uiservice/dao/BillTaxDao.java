package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.BillTax;;

@Component("BillTaxDao")
public interface BillTaxDao {
	
    public void insertBillTax(BillTax billtax) throws Exception;
    
    public void insertBillTaxGoodsName(BillTax billtax) throws Exception;
    
    public void updateBillTax(BillTax billtax) throws Exception;
    
    public void updateBillTaxGoodsName(BillTax billtax) throws Exception;
    
    public void deleteBillTax(BillTax billtax) throws Exception;
    
    public void deleteBillTaxGoodsName(BillTax billtax) throws Exception;
    
    List<BillTax> queryBillTax(Map<String, Object> p) throws Exception;
    
    public BillTax getBillTaxById(BillTax billtax) throws Exception;
    
    int getBillTaxCount(Map<String, Object> p);
	
}
