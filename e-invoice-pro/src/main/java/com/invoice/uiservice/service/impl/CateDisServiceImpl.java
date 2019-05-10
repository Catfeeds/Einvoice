package com.invoice.uiservice.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Catedis;
import com.invoice.uiservice.dao.CateDisDao;
import com.invoice.uiservice.service.CateDisService;

@Service("CateDisService")
public class CateDisServiceImpl implements CateDisService{

	@Resource(name = "CateDisDao")
	CateDisDao cateDisDao;
	
	@Override
	public List<Catedis> queryCatedis(Map<String, Object> p) throws Exception{
		return cateDisDao.queryCatedis(p);
	}
    
	@Override
	public int getCatedisCount(Map<String, Object> p){
		return cateDisDao.getCatedisCount(p);
	}
    
	@Override
    public void insertCateDis(Catedis catetax) throws Exception{
		cateDisDao.insertCateDis(catetax);
	}

	@Override
    public void deleteCateDis(Catedis catetax) throws Exception{
		cateDisDao.deleteCateDis(catetax);
	}
	
	@Override
	public List<Catedis> getCatedisById(Map<String, Object> p){
		return cateDisDao.getCatedisById(p);
	}

}
