package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.BillTax;

public interface BillTaxService {
	
	public void insertBillTax(BillTax billtax) throws Exception;
    
    public void insertBillTaxGoodsName(BillTax billtax) throws Exception;
    
    public void updateBillTax(BillTax billtax) throws Exception;
    
    public void updateBillTaxGoodsName(BillTax billtax) throws Exception;
    
    public void deleteBillTax(BillTax billtax) throws Exception;
    
    public void deleteBillTaxGoodsName(BillTax billtax) throws Exception;
    
    public List<BillTax> queryBillTax(Map<String, Object> p) throws Exception;
    
    public BillTax getBillTaxById(BillTax billtax) throws Exception;
    
    public int getBillTaxCount(Map<String, Object> p);
}
