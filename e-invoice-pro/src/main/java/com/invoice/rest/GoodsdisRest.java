package com.invoice.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Goodsdis;
import com.invoice.bean.db.Goodstax;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.GoodsdisService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class GoodsdisRest {

	@Autowired
	GoodsdisService goodsdisService;
	
	
	@RequestMapping(value = "/queryGoodsdis", method = RequestMethod.POST)
	@ResponseBody
	public String queryGoodsdis(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			Page.cookPageInfo(returnJson);
			int count = goodsdisService.getGoodsdisCount(returnJson);
			List<HashMap<String,String>> goodsdis  = goodsdisService.queryGoodsdis(returnJson);
			return new RtnData(goodsdis,count).toString();	
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();	
		}
	}
	
	@RequestMapping(value = "/insertGoodsdis", method = RequestMethod.POST)
	@ResponseBody
	public String insertGoodsdis(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Goodsdis goodsdis=JSONObject.parseObject(data, Goodsdis.class);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			goodsdis.setEntid(token.getEntid());
			goodsdisService.insertGoodsdis(goodsdis);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
}
	
	@RequestMapping(value = "/getGoodsdisById", method = RequestMethod.POST)
	@ResponseBody
	public String getGoodsdisById(@RequestBody String data) {
		Goodsdis goodsdis=JSONObject.parseObject(data, Goodsdis.class);
		JSONObject returnJson = new JSONObject();
		try {
			Token token = Token.getToken();
			goodsdis.setEntid(token.getEntid());
			Goodsdis gd  = goodsdisService.getGoodsdisById(goodsdis);
			returnJson.put("code", "0");
			returnJson.put("data", gd);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

    	return returnJson.toJSONString();
	}
	
	
	@RequestMapping(value = "/updateGoodsdis", method = RequestMethod.POST)
	@ResponseBody
	public String updateGoodsdis(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Goodsdis goodsdis=JSONObject.parseObject(data, Goodsdis.class);

		try {	
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			goodsdis.setEntid(token.getEntid());
			goodsdisService.updateGoodsdis(goodsdis);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
}
	
	
	@RequestMapping(value = "/deleteGoodsdis", method = RequestMethod.POST)
	@ResponseBody
	public String deleteGoodsdis(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Goodsdis goodsdis=JSONObject.parseObject(data, Goodsdis.class);
		try {
			Token token = Token.getToken();
			goodsdis.setEntid(token.getEntid());
			goodsdisService.deleteGoodsdis(goodsdis);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}
}
