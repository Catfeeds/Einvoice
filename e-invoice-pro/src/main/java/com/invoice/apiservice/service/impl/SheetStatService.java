package com.invoice.apiservice.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.SheetStatDao;
import com.invoice.util.Convert;
import com.invoice.util.MathCal;
import com.invoice.util.NewHashMap;

/**
 * 开票统计服务
 * 
 * @author Baij
 * 
 */
@Service
public class SheetStatService {

	@Autowired
	SheetStatDao dao;

	public List<Map<String, Object>> querySheetStat(JSONObject params) {
		if (!params.containsKey("sdate")) {
			throw new RuntimeException("开始日期必须指定");
		}
		if (!params.containsKey("edate")) {
			throw new RuntimeException("结束日期必须指定");
		}
		
		NewHashMap<String, Object> map = new NewHashMap<String, Object>(params);
		Calendar calendar = Calendar.getInstance();
		 SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMdd");
		//已开票金额开始日期要放到1号
		String sdate = params.getString("sdate");
		Date sd = Convert.stringToDate(sdate);
		calendar.setTime(sd);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date esdate = calendar.getTime();
		map.put("esdate", format0.format(esdate));
		
		//TODO 【 待确认】已开票结束日期设置为月末最后一天
		String edate = params.getString("edate");
		Date ed = Convert.stringToDate(edate);
//		calendar.setTime(ed);
//		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
//		Date eedate = calendar.getTime();
		map.put("eedate", format0.format(ed));
		
		//TODO 【 待确认】销售统计结束日期向前10天
//		calendar.setTime(ed);
//		calendar.add(Calendar.DAY_OF_MONTH, -10);
//		map.put("edate", calendar.getTime());

		List<Map<String, Object>> res = dao.querySheetStat(map);
		for (Map<String, Object> map2 : res) {
			//计算可开票金额
			Double amt = Double.valueOf(map2.get("amt").toString());//销售开票金额
			Double useAmt = Double.valueOf(map2.get("useamt").toString());//已开金额
			Double allowAmt = MathCal.mul(amt-useAmt, 0.9, 2);//可开票金额 * 0.9【 系数待确认】
			map2.put("allowAmt", allowAmt);
			
			//数量
			Double qty = Double.valueOf(map2.get("qty").toString());//总销售数量
			Double useQty = Double.valueOf(map2.get("useqty").toString());//已开票数量
			map2.put("allowQty", qty-useQty);
		}

		return res;
	}

	public int querySheetTaxitemCount(JSONObject params) {
		return dao.querySheetTaxitemCount(params);
	}
	
	public List<Map<String, Object>> queryBillTaxGoodsName(String taxitemid,float taxrate){
		return dao.queryBillTaxGoodsName(taxitemid,taxrate);
	}
	
	public int querySheetStatCount(JSONObject params) {
		return dao.querySheetCount(params);
	}
	
	public List<Map<String, Object>> querySheetTaxitem(JSONObject params) {
		if (!params.containsKey("sdate")) {
			throw new RuntimeException("开始日期必须指定");
		}
		if (!params.containsKey("edate")) {
			throw new RuntimeException("结束日期必须指定");
		}
		
		Calendar calendar = Calendar.getInstance();
		 SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMdd");
		//已开票金额开始日期最近三个月要放到1号
		String sdate = params.getString("sdate");
		Date sd = Convert.stringToDate(sdate);
		calendar.setTime(sd);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date esdate = calendar.getTime();
		
		//calendar.setTime(new Date());
		//calendar.add(Calendar.DAY_OF_MONTH, -90);
		//calendar.set(Calendar.DAY_OF_MONTH, 1);
		//Date esdate = calendar.getTime();
		NewHashMap<String, Object> map = new NewHashMap<String, Object>(params);
		map.put("esdate", format0.format(esdate));
		//map.put("esdate", esdate);
		//已开票结束金额为10天前
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -10);
		Date eedate = calendar.getTime();
		//map.put("eedate", eedate);
		map.put("eedate", format0.format(eedate));
		
		List<Map<String, Object>> res = dao.querySheetTaxitem(map);
		for (Map<String, Object> map2 : res) {
			//计算可开票金额
			Double amt = Double.valueOf(map2.get("amt").toString());//销售开票金额
			Double useAmt = Double.valueOf(map2.get("useamt").toString());//已开金额
			Double allowAmt = MathCal.mul(amt-useAmt, 0.9, 2);//可开票金额 * 0.9【 系数待确认】
			map2.put("allowAmt", allowAmt);
			
			//数量
			Double qty = Double.valueOf(map2.get("qty").toString());//总销售数量
			Double useQty = Double.valueOf(map2.get("useqty").toString());//已开票数量
			map2.put("allowQty", qty-useQty);
		}

		return res;
	}
	

	public List<HashMap<String,String>> querySheetLog(Map<String, Object> p) throws Exception{
		return dao.querySheetLog(p);
	}
	public int querySheetStatLogCount(JSONObject params) {
		return dao.querySheetLogCount(params);
	}
}
