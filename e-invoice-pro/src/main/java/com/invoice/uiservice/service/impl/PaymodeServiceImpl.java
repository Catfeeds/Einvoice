package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Paymode;
import com.invoice.uiservice.dao.PaymodeDao;
import com.invoice.uiservice.service.PaymodeService;

@Service("PaymodeService")
public class PaymodeServiceImpl implements PaymodeService{

	@Resource(name = "PaymodeDao")
	PaymodeDao paymodeDao;
	
	@Override
	public void insertPaymode(Paymode paymode) throws Exception {
		paymodeDao.insertPaymodelog(paymode);
		paymodeDao.insertPaymode(paymode);
		
	}

	@Override
	public void updatePaymode(Paymode paymode) throws Exception {
		paymodeDao.insertPaymodelog(paymode);
		paymodeDao.updatePaymode(paymode);
	}

	@Override
	public void deletePaymode(Paymode paymode) throws Exception {
		paymodeDao.insertPaymodelog(paymode);
		paymodeDao.deletePaymode(paymode);
	}

	@Override
	public List<HashMap<String,String>> queryPaymode(Map<String, Object> p) throws Exception {
		
		return paymodeDao.queryPaymode(p);
	}

	@Override
	public Paymode getPaymodeById(Paymode paymode) throws Exception {

		return paymodeDao.getPaymodeById(paymode);
	}

	@Override
	public int getPaymodeCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return paymodeDao.getPaymodeCount(p);
	}
	
	@Override
	public List<HashMap<String,String>> queryPaymodelog(Map<String, Object> p) throws Exception {
		
		return paymodeDao.queryPaymodelog(p);
	}
	
	@Override
	public int getPaymodeCountlog(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return paymodeDao.getPaymodeCountlog(p);
	}
	

}
