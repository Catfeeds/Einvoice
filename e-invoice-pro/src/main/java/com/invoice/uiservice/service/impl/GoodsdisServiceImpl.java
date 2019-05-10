package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Goodsdis;
import com.invoice.uiservice.dao.GoodsdisDao;
import com.invoice.uiservice.service.GoodsdisService;

@Service("GoodsdisService")
public class GoodsdisServiceImpl implements GoodsdisService{

	@Resource(name = "GoodsdisDao")
	GoodsdisDao goodsdisDao;
	
	@Override
	public void insertGoodsdis(Goodsdis goodsdis) throws Exception {
		goodsdisDao.insertGoodsdis(goodsdis);
	}

	@Override
	public void updateGoodsdis(Goodsdis goodsdis) throws Exception {
		goodsdisDao.updateGoodsdis(goodsdis);
	}

	@Override
	public void deleteGoodsdis(Goodsdis goodsdis) throws Exception {
		goodsdisDao.deleteGoodsdis(goodsdis);
	}

	@Override
	public List<HashMap<String,String>> queryGoodsdis(Map<String, Object> p) throws Exception {
		return goodsdisDao.queryGoodsdis(p);
	}

	@Override
	public Goodsdis getGoodsdisById(Goodsdis goodsdis) throws Exception {
		return goodsdisDao.getGoodsdisById(goodsdis);
	}

	@Override
	public int getGoodsdisCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return goodsdisDao.getGoodsdisCount(p);
	}

	@Override
	public void addGoodsdisList(List<Goodsdis> list) throws Exception {
		// TODO Auto-generated method stub
		goodsdisDao.addGoodsdisList(list);
	}
       
}
