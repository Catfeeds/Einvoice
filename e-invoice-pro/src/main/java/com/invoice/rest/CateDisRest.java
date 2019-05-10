package com.invoice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Catedis;
import com.invoice.bean.db.Catetax;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.CateDisService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class CateDisRest {
	@Autowired
	CateDisService catedisService;
	
 
	@RequestMapping(value = "/queryCatedis", method = RequestMethod.POST)
	@ResponseBody
	public String queryCatedis(@RequestBody String data) {
		try {
			JSONObject jo=JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			Page.cookPageInfo(jo);
			List<Catedis> list = catedisService.queryCatedis(jo);
			int count = catedisService.getCatedisCount(jo);
			return new RtnData(list,count).toString();
		} catch (Exception e) {
			e.printStackTrace();	
			return new RtnData(-1, e.getMessage()).toString();
		}

	}
	
	 
	@RequestMapping(value = "/insertCateDis", method = RequestMethod.POST)
	@ResponseBody
	public String insertCateDis(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		Catedis catetax=JSONObject.parseObject(data, Catedis.class);
		Token token = Token.getToken();
		try {
			catetax.setEntid(token.getEntid());
			catedisService.insertCateDis(catetax);
			returnJson.put("entid",token.getEntid());
			List<Catedis> ct  = catedisService.getCatedisById(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", ct);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
 
	@RequestMapping(value = "/deleteCateDisByid", method = RequestMethod.POST)
	@ResponseBody
	public String deleteCateDisByid(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Catedis catetax=JSONObject.parseObject(data, Catedis.class);
		try {
			Token token = Token.getToken();
			catetax.setEntid(token.getEntid());
			catedisService.deleteCateDis(catetax);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}
	
	@RequestMapping(value = "/getCatedisById", method = RequestMethod.POST)
	@ResponseBody
	public String getCatedisById(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		Token token = Token.getToken();
		try {
			returnJson.put("entid",token.getEntid());
			List<Catedis> ct = catedisService.getCatedisById(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", ct);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
    	return returnJson.toJSONString();
	}

}

