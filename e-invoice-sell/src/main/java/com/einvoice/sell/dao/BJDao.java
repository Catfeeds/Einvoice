package com.einvoice.sell.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Zhao on 2019.06.20
 *  收银小票 For 宁波阪急
 */
public interface BJDao{
	
	public void test();
	
	public List<Map<String, Object>> getProvHead(Map<String, String> p);
	
	public List<Map<String, Object>> getProvDetail(Map<String, String> p);
	
	public List<Map<String, Object>> getProvRet(Map<String, String> p);
	
	public List<Map<String, Object>> getHead(Map<String, String> p);
	
	public List<Map<String, Object>> getSheet(Map<String, String> p);
	
	public List<Map<String, Object>> getDetail(Map<String, String> p);
	
	public List<Map<String, Object>> getPayment(Map<String, String> p);
	
	public List<Map<String, Object>> getHeadRet(Map<String, String> p);
	
	public int callProvSheetBJ(Map<String, Object> p);
	
	public int callBackSheetBJ(Map<String, Object> p);
}
