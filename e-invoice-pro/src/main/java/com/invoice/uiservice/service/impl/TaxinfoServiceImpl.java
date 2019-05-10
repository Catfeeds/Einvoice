package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.Option;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.uiservice.service.TaxinfoService;

@Service("TaxinfoService")
public class TaxinfoServiceImpl implements TaxinfoService{

	@Resource(name = "TaxinfoDao")
	TaxinfoDao taxinfoDao;

	@Override
	public void insertTaxinfo(Taxinfo taxinfo) throws Exception {
		taxinfoDao.insertTaxinfo(taxinfo);
	}

	@Override
	public void updateTaxinfo(Taxinfo taxinfo) throws Exception {
		taxinfoDao.updateTaxinfo(taxinfo);
	}

	@Override
	public void deleteTaxinfo(Taxinfo taxinfo) throws Exception {
		taxinfoDao.deleteTaxinfo(taxinfo);
	}

	@Override
	public List<HashMap<String,String>> queryTaxinfo(Map<String, Object> p) throws Exception {
		return taxinfoDao.queryTaxinfo(p);
	}

	@Override
	public Taxinfo getTaxinfoByNo(Taxinfo taxinfo) throws Exception {
		return taxinfoDao.getTaxinfoByNo(taxinfo);
	}
	
	@Override
	public Taxinfo getTaxinfoItemByNo(Taxinfo taxinfo) throws Exception {
		return taxinfoDao.getTaxinfoItemByNo(taxinfo);
	}
	
	@Override
	public int getTaxinfoCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return taxinfoDao.getTaxinfoCount(p);
	}

	@Override
	public List<Option> getTaxnoSelect(String entid) {
		// TODO Auto-generated method stub
		return taxinfoDao.getTaxnoSelect(entid);
	}

}
