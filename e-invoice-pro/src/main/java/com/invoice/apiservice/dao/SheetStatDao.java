package com.invoice.apiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.SheetLog;
import com.invoice.util.NewHashMap;

public interface SheetStatDao {
	void insert(SheetLog log);

	List<Map<String, Object>> querySheetStat(NewHashMap<String, Object> map);

	int querySheetCount(JSONObject params);
	
	List<HashMap<String,String>> querySheetLog(Map<String, Object> p);
	int querySheetLogCount(JSONObject params);

	int querySheetTaxitemCount(JSONObject params);

	List<Map<String, Object>> querySheetTaxitem(NewHashMap<String, Object> map);
	
	List<Map<String, Object>> queryBillTaxGoodsName(@Param("taxitemid")String taxitemid,@Param("taxrate")float taxrate);
}
