package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.BillTax;
import com.invoice.uiservice.dao.BillTaxDao;
import com.invoice.uiservice.service.BillTaxService;
@Service("BillTaxService")
public class BillTaxServiceImpl implements BillTaxService {
	@Resource(name = "BillTaxDao")
	BillTaxDao billTaxDao;
	
	@Override
	public void insertBillTax(BillTax billtax) throws Exception{
		billTaxDao.insertBillTax(billtax);
		billTaxDao.insertBillTaxGoodsName(billtax);
	}
	@Override
    public void insertBillTaxGoodsName(BillTax billtax) throws Exception{
    	
    }
	@Override
    public void updateBillTax(BillTax billtax) throws Exception{
		billTaxDao.updateBillTax(billtax);
		billTaxDao.updateBillTaxGoodsName(billtax);
	}
	@Override
    public void updateBillTaxGoodsName(BillTax billtax) throws Exception{
		
	}
	@Override
    public void deleteBillTax(BillTax billtax) throws Exception{
		billTaxDao.deleteBillTax(billtax);
		billTaxDao.deleteBillTaxGoodsName(billtax);
	}
	@Override
    public void deleteBillTaxGoodsName(BillTax billtax) throws Exception{
		
	}
	@Override
	public List<BillTax> queryBillTax(Map<String, Object> p) throws Exception{
		return billTaxDao.queryBillTax(p);
	}
	@Override
    public BillTax getBillTaxById(BillTax billtax) throws Exception{
		return billTaxDao.getBillTaxById(billtax);
	}
	@Override
	public  int getBillTaxCount(Map<String, Object> p){
		return billTaxDao.getBillTaxCount(p);
	}
}
