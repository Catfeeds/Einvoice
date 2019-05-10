package com.einvoice.sell.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.dao.ZMDao;
import com.einvoice.sell.util.NewHashMap;

@Service("zm")
public class ZMService extends BaseService {
	private final Log log = LogFactory.getLog(ZMService.class);

	@Autowired
	ZMDao dao;
	
	@Override
	public List<Map<String, Object>> getList(ShopConnect shop) {
		Map<String, String> mapShop = new NewHashMap<String, String>();
		mapShop.put("shopid", shop.getShopid());
		List<Map<String, Object>> list = dao.getList(mapShop);
		return list;
	}

	public Map<String, Object> getSheet(ShopConnect shop, String sheetid) {
		Map<String, String> p = cookParams(sheetid);

		List<Map<String, Object>> headList = dao.getHead(p);

		if (headList == null || headList.isEmpty()) {
			throw new RuntimeException("没有找到数据");
		}

		Map<String, Object> head = headList.get(0);

		List<Map<String, Object>> selldetail = dao.getDetail(p);
		if (selldetail == null) {
			// 标记失败
			Map<String, Object> p2 = new NewHashMap<>();
			p2.put("sheetid", p.get("sheetid"));
			p2.put("flag", 9);
			p2.put("flagmsg", "明细信息缺失");
			dao.callbackSheet(p2);
			throw new RuntimeException("明细信息缺失");
		}

		head.put("sheetdetail", selldetail);

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
	
	@Override
	public int callBackSheet(ShopConnect shop, String data) {
		try {
			data = URLDecoder.decode(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> map = callBackMap(data);
		
		
		log.info(JSONObject.toJSON(map));

		int rows = dao.callbackSheet(map);
		if(rows==0) {
			throw new RuntimeException("更新记录失败，影响行为零："+JSONObject.toJSON(map));
		}
		return rows;
	}
}
