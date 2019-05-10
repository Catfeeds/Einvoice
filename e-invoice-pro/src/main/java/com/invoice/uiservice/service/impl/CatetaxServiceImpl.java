package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Category;
import com.invoice.bean.db.Catetax;
import com.invoice.uiservice.dao.CatetaxDao;
import com.invoice.uiservice.service.CatetaxService;

@Service("CatetaxService")
public class CatetaxServiceImpl implements CatetaxService{

	@Resource(name = "CatetaxDao")
	CatetaxDao catetaxDao;
	
	@Override
	public void insertCatetax(Catetax catetax) throws Exception {
		catetaxDao.insertCatetax(catetax);
	}

	@Override
	public void updateCatetax(Catetax catetax) throws Exception {
		catetaxDao.updateCatetax(catetax);
	}

	@Override
	public void deleteCatetax(Catetax catetax) throws Exception {
		catetaxDao.deleteCatetax(catetax);
	}

	@Override
	public List<Category> queryCategory(Category ca) throws Exception {
		return catetaxDao.queryCategory(ca);
	}
	
	@Override
	public int getCatetaxCount(Map<String, Object> p) {
		return catetaxDao.getCatetaxCount(p);
	}
	
	@Override
	public List<Catetax> getCatetaxById(Map<String, Object> p) throws Exception {
		return catetaxDao.getCatetaxById(p);
	}

	public void addCatetaxList(List<Catetax> list) throws Exception{
		 catetaxDao.addCatetaxList(list);
	}
	 
	public void addCategoryList(List<Category> list) throws Exception{
		 catetaxDao.addCategoryList(list);
	}

}
