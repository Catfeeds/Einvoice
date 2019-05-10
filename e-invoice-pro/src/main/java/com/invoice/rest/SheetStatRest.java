package com.invoice.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.service.impl.SheetStatService;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.Page;

/**
 * 单据开票统计
 * 
 * @author Baij
 */
@Controller
@RequestMapping(value = "/api")
public class SheetStatRest {
	private final Log log = LogFactory.getLog(SheetStatRest.class);

	@Autowired
	SheetStatService service;

	/**
	 * 待开商品统计
	 * @param data 查询参数 {shopid:门店编码,sdate:开始日期,edate:结束日期,goodsname:商品名称}
	 * @return
	 */
	@RequestMapping(value = "/querySheetStat")
	@ResponseBody
	public String querySheetStat(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject params = JSONObject.parseObject(data);
			Page.cookPageInfo(params);
			int count = service.querySheetStatCount(params);
			List<Map<String,Object>> res = null;
			if(count>0) {
				res = service.querySheetStat(params);
			}
			return new RtnData(res,count).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	
	/**
	 * 待开税目统计
	 * @param data 查询参数 {shopid:门店编码,sdate:开始日期,edate:结束日期,goodsname:商品名称}
	 * @return
	 */
	@RequestMapping(value = "/querySheetTaxitem")
	@ResponseBody
	public String querySheetTaxitem(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject params = JSONObject.parseObject(data);
			Page.cookPageInfo(params);
			int count = service.querySheetTaxitemCount(params);
			List<Map<String,Object>> res = null;
			if(count>0) {
				res = service.querySheetTaxitem(params);
			}
			return new RtnData(res,count).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/queryBillTaxGoodsName")
	@ResponseBody
	public String queryBillTaxGoodsName(@RequestBody String data) {
		try {
			JSONObject params = JSONObject.parseObject(data);
			List<Map<String, Object>> res = service.queryBillTaxGoodsName(params.getString("taxitemid"),params.getFloatValue("taxrate"));
			return new RtnData(res,res.size()).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/querySheetLog", method = RequestMethod.POST)
	@ResponseBody
	public String querySheetLog(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);			
			List<HashMap<String, String>> res = service.querySheetLog(jo);
			int count = service.querySheetStatLogCount(jo);
			return new RtnData(res,count).toString();
			
		} catch (Exception e) {
			e.printStackTrace();	
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
}
