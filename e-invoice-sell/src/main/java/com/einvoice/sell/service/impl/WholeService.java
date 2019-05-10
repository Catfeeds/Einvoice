package com.einvoice.sell.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.dao.WholeDao;
import com.einvoice.sell.util.NewHashMap;

@Service("whole")
public class WholeService extends BaseService {
	private final Log log = LogFactory.getLog(WholeService.class);

	@Autowired
	WholeDao dao;

	public Map<String, Object> getSheet(ShopConnect shop, String sheetid) {
		Map<String, String> p = cookParams(sheetid);

		List<Map<String, Object>> headList = dao.getHead(p);

		if (headList == null || headList.isEmpty()) {
			throw new RuntimeException("没有找到数据");
		}
		
		Map<String,Object> head = headList.get(0);

		List<Map<String, Object>> selldetail = dao.getDetail(p);
		if (selldetail == null)
			throw new RuntimeException("明细信息缺失");

		List<Map<String, Object>> sellpayment = dao.getPayment(p);

		head.put("sheetdetail", selldetail);
		head.put("sheetpayment", sellpayment);

		return head;
	}

	public Map<String, String> cookParams(String sheetid) {
		if (sheetid == null)
			throw new RuntimeException("无法解析单号信息");
		try {
			NewHashMap<String, String> map = new NewHashMap<String, String>();
			map.put("sheetid", sheetid);
			return map;
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("无法解析单号信息");
		}
	}
}
