package com.einvoice.sell.service;

import java.util.List;
import java.util.Map;

import com.einvoice.sell.bean.ShopConnect;

public interface SheetService {
	/**
	 * 获取指定单据数据
	 * @param shop
	 * @param sheetid
	 * @return
	 */
	public Map<String,Object> getSheet(ShopConnect shop,String sheetid);
	
	/**
	 * 回写单据状态
	 * @param shop
	 * @param data
	 * @return
	 */
	public int callBackSheet(ShopConnect shop,String data);
	
	public int NBcallBackSheet(ShopConnect shop,String data);
	
	public Map<String,String> cookParams(String sheetid);
	
	/**
	 * 获取指定日期单据统计信息
	 * @param shop
	 * @param sdate
	 * @return
	 */
	public List<Map<String, Object>> getStat(ShopConnect shop,String sdate);

	/**
	 * 获取指定日期单据支付统计信息
	 * @param shop
	 * @param sdate
	 * @return
	 */
	public List<Map<String, Object>> getStatPay(ShopConnect shop, String sdate);
	
	/**
	 * 获取待开票单据清单
	 * @param shop
	 * @return
	 */
	public List<Map<String, Object>> getList(ShopConnect shop);

	public List<Map<String, Object>> getZH(ShopConnect shop,Integer page);
	public List<Map<String, Object>> getHT(ShopConnect shop,String gysid,Integer page);
	
}
