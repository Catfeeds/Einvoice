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
import com.einvoice.sell.dao.CommonSheetDao;
import com.einvoice.sell.util.Convert;
import com.einvoice.sell.util.NewHashMap;

@Service("common")
public class CommonSheetService extends BaseService {
	private final Log log = LogFactory.getLog(CommonSheetService.class);
	
	final public String sheetType="7";

	@Autowired
	CommonSheetDao dao;
	
	@Override
	public List<Map<String, Object>> getList(ShopConnect shop) {
		Map<String, String> p = new NewHashMap<>();
		p.put("flag", "0");
		p.put("sheettype", sheetType);
		p.put("shopid", shop.getShopid());
		List<Map<String, Object>> list = dao.getList(p);
		return list;
	}

	public Map<String, Object> getSheet(ShopConnect shop, String sheetid) {
		Map<String, String> p = cookParams(sheetid);

		List<Map<String, Object>> headList = dao.getHead(p);

		if (headList == null || headList.isEmpty()) {
			throw new RuntimeException("没有找到数据");
		}

		Map<String, Object> head = headList.get(0);
		
		String charcode = shop.getDbcharcode();
		Convert.dbChar(charcode, head);
		

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
		
		for (Map<String, Object> map : selldetail) {
			Convert.dbChar(charcode, map);
		}

		head.put("sheetdetail", selldetail);

		return head;
	}

	public Map<String, String> cookParams(String sheetid) {
		if (sheetid == null)
			throw new RuntimeException("无法解析单号信息");
		try {
			Map<String, String> map = new NewHashMap<>();
			map.put("sheetid", sheetid);
			map.put("sheettype",sheetType);
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
	
	public List<Map<String, Object>> getZH(ShopConnect shop,Integer page){
		int size = 1000;
		if(page==null || page<1) page=1;
		page=size*(page-1);
		
		List<Map<String, Object>> list = dao.getZH(page,size);
		String charcode = shop.getDbcharcode();
			for (Map<String, Object> map : list) {
				Convert.dbChar(charcode, map);
			}
		return list;
	}
	
	public List<Map<String, Object>> getHT(ShopConnect shop,String gysid, Integer page){
		int size = 1000;
		if(page==null || page<1) page=1;
		page=size*(page-1);
		
		List<Map<String, Object>> list = dao.getHT(page,gysid,size);
		
		String charcode = shop.getDbcharcode();
		for (Map<String, Object> map : list) {
			Convert.dbChar(charcode, map);
		}
		return list;
	}
}
