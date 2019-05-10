package com.einvoice.sell.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Zhao on 2018.11.13
 *  收银小票 For 东港瑞宏
 */
public interface TKDao{
	public void test();
	
	public List<Map<String, Object>> getHead(Map<String, String> p);

	public List<Map<String, Object>> getDetail(Map<String, String> p);
}
