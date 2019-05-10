package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Taxitem;
import com.invoice.uiservice.dao.TaxitemDao;
import com.invoice.uiservice.service.TaxitemService;

@Service("TaxitemService")
public class TaxitemServiceImpl implements TaxitemService{

	@Resource(name = "TaxitemDao")
	TaxitemDao taxitemDao;
	
	@Override
	public void insertTaxitem(Taxitem taxitem) throws Exception {
		taxitemDao.insertTaxitem(taxitem);
		
	}

	@Override
	public void addTaxitemByExcel(Taxitem taxitem) throws Exception {
		taxitemDao.addTaxitemByExcel(taxitem);
	}

	@Override
	public void updateTaxitem(Taxitem taxitem) throws Exception {
		taxitemDao.updateTaxitem(taxitem);
		
	}

	@Override
	public void deleteTaxitem(Taxitem taxitem) throws Exception {
		taxitemDao.deleteTaxitem(taxitem);
		
	}

	@Override
	public List<Taxitem> queryTaxitem(Map<String, Object> p) throws Exception {
		return taxitemDao.queryTaxitem(p);
	}

	@Override
	public  List<HashMap<String,String>> getTaxitemById(Taxitem taxitem) throws Exception {
		
		return taxitemDao.getTaxitemById(taxitem);
	}

	@Override
	public int getTaxitemCount(Map<String, Object> p) {
		return taxitemDao.getTaxitemCount(p);
	}

	 public void addTaxitemList(List<Taxitem> list) throws Exception {
		 taxitemDao.addTaxitemList(list);
	 }

	@Override
	public Taxitem getTaxitemByTaxitemId(Taxitem taxitem) throws Exception {
		return taxitemDao.getTaxitemByTaxitemId(taxitem);
	}

}
