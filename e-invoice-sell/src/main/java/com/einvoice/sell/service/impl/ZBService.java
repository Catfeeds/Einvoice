package com.einvoice.sell.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.dao.ZBDao;

@Service("ZB")
public class ZBService extends BaseService {

	@Autowired
	ZBDao dao;
	
	public List<Map<String, Object>> getgoodsinfo(int startrow,int endrow) {
		List<Map<String, Object>> list = dao.getgoodsinfo(startrow,endrow);
		return list;
	}

	@Override
	public Map<String, Object> getSheet(ShopConnect shop, String sheetid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> cookParams(String sheetid) {
		// TODO Auto-generated method stub
		return null;
	}
}
