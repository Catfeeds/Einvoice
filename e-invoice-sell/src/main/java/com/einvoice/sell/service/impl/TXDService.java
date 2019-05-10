package com.einvoice.sell.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.dao.TXDDao;
import com.einvoice.sell.util.NewHashMap;

@Service("txd")
public class TXDService extends BaseService {
	private final Log log = LogFactory.getLog(TXDService.class);

	@Autowired
	TXDDao dao;

	public Map<String, Object> getSheet(ShopConnect shop, String sheetid) {
		Map<String, String> p = cookParams(sheetid);
		p.put("shopid", shop.getShopid());

		List<Map<String, Object>> headList = dao.getHead(p);

		if (headList == null || headList.isEmpty()) {
			throw new RuntimeException("没有找到数据");
		}
		
		Map<String, Object> head = headList.get(0);
		
		String billno = head.get("sheetid").toString();
		p.put("sheetid", billno);
		List<Map<String, Object>> selldetail = dao.getDetail(p);

		if (selldetail == null)
			throw new RuntimeException("明细信息缺失");
		
		List<Map<String, Object>> sellpayment = dao.getPayment(p);

		if (sellpayment == null)
			throw new RuntimeException("支付信息缺失");
		
		//商品名称字符集处理
		String charcode = shop.getDbcharcode();
		if(!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)){
			for (Map<String, Object> map : selldetail) {
				 Object obj = map.get("itemname");
				 if(obj!=null){
					 try {
						map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				 }
				 
				 obj = map.get("unit");
				 if(obj!=null){
					 try {
						map.put("unit", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				 }
			}
		}
		
		head.put("sheetdetail", selldetail);
		head.put("sheetpayment", sellpayment);

		return head;
	}

	public Map<String, String> cookParams(String sheetid) {
		if (sheetid == null)
			throw new RuntimeException("无法解析小票信息");
		NewHashMap<String, String> map = new NewHashMap<String, String>();
		String type = sheetid.substring(0, 1);
		sheetid = sheetid.substring(1);
		if(type.equals("A")) {
			map.put("abillno", sheetid);
		}else {
			map.put("billno", sheetid);
		}
		
		return map;
	}


}
