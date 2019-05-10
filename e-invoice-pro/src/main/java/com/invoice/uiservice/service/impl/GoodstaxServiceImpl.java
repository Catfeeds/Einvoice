package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Goodstax;
import com.invoice.bean.db.Taxitem;
import com.invoice.uiservice.dao.GoodstaxDao;
import com.invoice.uiservice.service.GoodstaxService;

@Service("GoodstaxService")
public class GoodstaxServiceImpl implements GoodstaxService{

	@Resource(name = "GoodstaxDao")
	GoodstaxDao goodstaxDao;
	
	@Override
	public int insertGoodstax(Goodstax goodstax) throws Exception {
		try {
			goodstaxDao.insertGoodstaxlog(goodstax);
			goodstaxDao.insertGoodstax(goodstax);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int updateGoodstax(Goodstax goodstax) throws Exception {
		try {
		goodstaxDao.insertGoodstaxlog(goodstax);//插入日志表
		goodstaxDao.updateGoodstax(goodstax);
		return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void deleteGoodstax(Goodstax goodstax) throws Exception {
		Goodstax goodtax = new Goodstax();
		goodtax= goodstaxDao.getGoodstaxById(goodstax);
		goodtax.setCrud(goodstax.getCrud());
		goodtax.setEdittype(goodstax.getEdittype());
		goodtax.setLoginid(goodstax.getLoginid());
		goodtax.setProcessTime(goodstax.getProcessTime());
		goodtax.setUsername(goodstax.getUsername());
		goodstaxDao.insertGoodstaxlog(goodtax);
		goodstaxDao.deleteGoodstax(goodstax);
	}

	@Override
	public List<HashMap<String,String>> queryGoodstax(Map<String, Object> p) throws Exception {
		return goodstaxDao.queryGoodstax(p);
	}

	@Override
	public Goodstax getGoodstaxById(Goodstax goodstax) throws Exception {
		return goodstaxDao.getGoodstaxById(goodstax);
	}

	@Override
	public int getGoodstaxCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return goodstaxDao.getGoodstaxCount(p);
	}

	@Override
	public void addGoodstaxList(List<Goodstax> list) throws Exception {
		// TODO Auto-generated method stub
		goodstaxDao.addGoodstaxList(list);
	}
	@Override
	public List<HashMap<String,String>> getBillsalereport(Map<String, Object> p) throws Exception{
		return goodstaxDao.getBillsalereport(p);
	}
	
	@Override
	public int getBillsalereportCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return goodstaxDao.getBillsalereportCount(p);
	}
	@Override
	public List<HashMap<String,String>> getGoodstaxByIds(Map<String, Object> p) throws Exception{
		return goodstaxDao.getGoodstaxByIds(p);
	}
	
	@Override
	public List<HashMap<String,String>> queryGoodstaxlog(Map<String, Object> p) throws Exception {
		return goodstaxDao.queryGoodstaxlog(p);
	}
	
	@Override
	public int getGoodstaxCountlog(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return goodstaxDao.getGoodstaxCountlog(p);
	}
	
	@Override
	public Taxitem getTaxitemById(String taxitemid){
		return goodstaxDao.getTaxitemById(taxitemid);
	}
}
