package com.einvoice.sell.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao {
	public void test();

	public List<Map<String, Object>> getHead(Map<String, String> p);

	public List<Map<String, Object>> getDetail(Map<String, String> p);

	public List<Map<String, Object>> getPayment(Map<String, String> p);
	
	public List<Map<String, Object>> getList(Map<String, String> p);

	int callbackSheet(Map<String, Object> p);
	
}
