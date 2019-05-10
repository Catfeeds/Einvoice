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
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.ZhuanpiaoService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class ZhuanpiaoRest {

	@Autowired
	ZhuanpiaoService zhuanpiaoService;
	
	@RequestMapping(value = "/queryInvoiceFlaglog", method = RequestMethod.POST)
	@ResponseBody
	public String queryInvoiceFlaglog(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			Page.cookPageInfo(returnJson);
			int count = zhuanpiaoService.getinvoiceFlaglogCountlog(returnJson);
			List<HashMap<String,String>> paymode  = zhuanpiaoService.queryinvoiceFlaglog(returnJson);
			return new RtnData(paymode,count).toString();	
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();	
		}
	}
}
