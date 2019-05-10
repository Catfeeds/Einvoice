package com.invoice.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Paymode;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.PaymodeService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class PaymodeRest {

	@Autowired
	PaymodeService paymodeService;
	
	
	@RequestMapping(value = "/queryPaymode", method = RequestMethod.POST)
	@ResponseBody
	public String queryPaymode(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			Page.cookPageInfo(returnJson);
			int count = paymodeService.getPaymodeCount(returnJson);
			List<HashMap<String,String>> paymode  = paymodeService.queryPaymode(returnJson);
			return new RtnData(paymode,count).toString();	
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();	
		}
	}

	
	@RequestMapping(value = "/insertPaymode", method = RequestMethod.POST)
	@ResponseBody
	public String insertPaymode(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Paymode paymode=JSONObject.parseObject(data, Paymode.class);
		try {	
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			paymode.setEntid(token.getEntid());
			paymode.setCrud("I");
			paymode.setLoginid(token.getLoginid());
			paymode.setProcessTime(new Date());
			paymode.setUsername(token.getUsername());
			paymodeService.insertPaymode(paymode);
			List<HashMap<String,String>> pm = paymodeService.queryPaymode(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", pm);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
}
	
	@RequestMapping(value = "/getPaymodeById", method = RequestMethod.POST)
	@ResponseBody
	public String getPaymodeById(@RequestBody String data) {
		Paymode paymode=JSONObject.parseObject(data, Paymode.class);
		JSONObject returnJson = new JSONObject();
		try {
			Token token = Token.getToken();
			paymode.setEntid(token.getEntid());
			Paymode pm = paymodeService.getPaymodeById(paymode);
			returnJson.put("code", "0");
			returnJson.put("data", pm);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

    	return returnJson.toJSONString();
	}
	
	
	@RequestMapping(value = "/updatePaymode", method = RequestMethod.POST)
	@ResponseBody
	public String updatePaymode(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Paymode paymode=JSONObject.parseObject(data, Paymode.class);
		try {	
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			paymode.setEntid(token.getEntid());
			paymode.setCrud("U");
			paymode.setLoginid(token.getLoginid());
			paymode.setUsername(token.getUsername());
			paymode.setProcessTime(new Date());
			paymodeService.updatePaymode(paymode);
			List<HashMap<String,String>> pm = paymodeService.queryPaymode(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", pm);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
}	
	
	@RequestMapping(value = "/getPaymodeCount")
	@ResponseBody
	public String getGoodsdisCount(@RequestBody String data){
		    JSONObject jo = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			int count = paymodeService.getPaymodeCount(jo);
			jo.put("count", count);
			return new RtnData(jo).toString();
		} catch (Exception e) {
			return new RtnData(-1,e.getMessage()).toString();
		}

	}
	
	
	@RequestMapping(value = "/deletePaymode", method = RequestMethod.POST)
	@ResponseBody
	public String deletePaymode(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Paymode paymode=JSONObject.parseObject(data, Paymode.class);
		try {
			Token token = Token.getToken();
			paymode.setEntid(token.getEntid());
			paymode.setCrud("D");
			paymode.setLoginid(token.getLoginid());
			paymode.setProcessTime(new Date());
			paymode.setUsername(token.getUsername());
			paymodeService.deletePaymode(paymode);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}
	
	@RequestMapping(value = "/queryPaymodelog", method = RequestMethod.POST)
	@ResponseBody
	public String queryPaymodelog(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			Page.cookPageInfo(returnJson);
			int count = paymodeService.getPaymodeCountlog(returnJson);
			List<HashMap<String,String>> paymode  = paymodeService.queryPaymodelog(returnJson);
			return new RtnData(paymode,count).toString();	
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();	
		}
	}
}
