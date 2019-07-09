package com.einvoice.sell.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.dao.NBBillDao;

@Service("NBbill")
public class NBBillService extends BaseService {
	private final Log log = LogFactory.getLog(NBBillService.class);

	@Autowired
	NBBillDao dao;
	@Override
	public int NBcallBackSheet(ShopConnect shop, String data) {
		try {
			data = URLDecoder.decode(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int rows = 0;
		Map<String, Object> map = callBackMap(data);

		log.info(JSONObject.toJSON(map));

		int row = dao.getInvoiceCount(map);
		if (row == 0) {
			rows = dao.NBIcallbackSheet(map);
			if (rows == 0) {
				throw new RuntimeException("新增记录失败，影响行为零：" + JSONObject.toJSON(map));
			}
		}
		else {
			rows = dao.NBUcallbackSheet(map);
			if (rows == 0) {
				throw new RuntimeException("更新记录失败，影响行为零：" + JSONObject.toJSON(map));
			}
		}
		return rows;
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
