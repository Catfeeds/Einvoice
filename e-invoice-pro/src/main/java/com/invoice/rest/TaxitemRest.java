package com.invoice.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Taxitem;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.TaxitemService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class TaxitemRest {

	@Autowired
	TaxitemService taxitemService;
	
	@RequestMapping(value = "/queryTaxitem", method = RequestMethod.POST)
	@ResponseBody
	public String queryTaxitem(@RequestBody String data){
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Page.cookPageInfo(jo);			
			int count = taxitemService.getTaxitemCount(jo);
		    List<Taxitem> taxitemlist = taxitemService.queryTaxitem(jo);
			return new RtnData(taxitemlist,count).toString();
		} catch (Exception e) {
			e.printStackTrace();	
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/insertTaxitem", method = RequestMethod.POST)
	@ResponseBody
	public String insertTaxitem(@RequestBody String data) {
		JSONObject jo = new JSONObject();
		try {
			Taxitem taxitem=JSONObject.parseObject(data, Taxitem.class);
			Taxitem tm = taxitemService.getTaxitemByTaxitemId(taxitem);
			if(tm==null){
				taxitemService.insertTaxitem(taxitem);
				jo.put("code", "0");
				jo.put("data", tm);
			}else{
				jo.put("code", "-2");
				jo.put("msg", "此税目代码已经添加过，如需更改请在编辑模块进行编辑！");
			}

		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
}
	
	@RequestMapping(value = "/getTaxitemById", method = RequestMethod.POST)
	@ResponseBody
	public String getTaxitemById(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Taxitem taxitem=JSONObject.parseObject(data, Taxitem.class);
			List<HashMap<String,String>> tm = taxitemService.getTaxitemById(taxitem);
			returnJson.put("code", "0");
			returnJson.put("data", tm);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

    	return returnJson.toJSONString();
	}

	 
	@RequestMapping(value = "/updateTaxitem", method = RequestMethod.POST)
	@ResponseBody
	public String updateTaxitem(@RequestBody String data) {
		JSONObject jo = new JSONObject();
		try {		
			Taxitem taxitem=JSONObject.parseObject(data, Taxitem.class);
			Page.cookPageInfo(jo);
			taxitemService.updateTaxitem(taxitem);
			List<Taxitem> tm = taxitemService.queryTaxitem(jo);
			jo.put("code", "0");
			jo.put("data", tm);
		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
}
	
	
	@RequestMapping(value = "/getTaxitemCount")
	@ResponseBody
	public String getTaxitemCount(@RequestBody String data){
		try {
			JSONObject jo = JSONObject.parseObject(data);
			int count = taxitemService.getTaxitemCount(jo);
			jo.put("count", count);
			return new RtnData(jo).toString();
		} catch (Exception e) {
			return new RtnData(-1,e.getMessage()).toString();
		}

	}

	@RequestMapping(value = "/deleteTaxitem")
	@ResponseBody
	public String deleteTaxitem(@RequestBody String data){
		JSONObject jo = new JSONObject();
		try {
			Taxitem taxitem=JSONObject.parseObject(data, Taxitem.class);
			taxitemService.deleteTaxitem(taxitem);
			jo.put("code", "0");
		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();

	}

	@RequestMapping(value = "/getTaxitemByTaxitemId", method = RequestMethod.POST)
	@ResponseBody
	public String getTaxitemByTaxitemId(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Taxitem taxitem=JSONObject.parseObject(data, Taxitem.class);
			Taxitem tm = taxitemService.getTaxitemByTaxitemId(taxitem);
			returnJson.put("code", "0");
			returnJson.put("data", tm);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

		return returnJson.toJSONString();
	}
}
