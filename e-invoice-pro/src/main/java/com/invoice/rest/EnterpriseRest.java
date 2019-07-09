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
import com.invoice.bean.db.Enterprise;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class EnterpriseRest {
	
	@Autowired
	EnterpriseService enterpriseService;

	@RequestMapping(value = "/queryEnterprise", method = RequestMethod.POST)
	@ResponseBody
	public String queryEnterprise(@RequestBody String data) {
		JSONObject jo = JSONObject.parseObject(data);
		try {
			Page.cookPageInfo(jo);
			int count = enterpriseService.getEnterpriseCount(jo);
			List<HashMap<String,String>> ep  = enterpriseService.getEnterprise(jo);
			return new RtnData(ep,count).toString();
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/addEnterprise", method = RequestMethod.POST)
	@ResponseBody
	public String addEnterprise(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Enterprise enterprise=JSONObject.parseObject(data, Enterprise.class);
		try {
			enterpriseService.addEnterprise(enterprise);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
	@RequestMapping(value = "/getEnterpriseById", method = RequestMethod.POST)
	@ResponseBody
	public String getEnterpriseById(@RequestBody String data) {
		Enterprise en=JSONObject.parseObject(data, Enterprise.class);
		JSONObject returnJson = new JSONObject();
		try {
			Enterprise enterprise  = enterpriseService.getEnterpriseById(en);
			returnJson.put("code", "0");
			returnJson.put("data", enterprise);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

    	return returnJson.toJSONString();
	}
	
	@RequestMapping(value = "/updateEnterprise", method = RequestMethod.POST)
	@ResponseBody
	public String updateEnterprise(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Enterprise en=JSONObject.parseObject(data, Enterprise.class);

		try {		
			enterpriseService.updateEnterprise(en);
			List<HashMap<String,String>> ep  = enterpriseService.getEnterprise(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", ep);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
	@RequestMapping(value = "/getEnterpriseCount")
	@ResponseBody
	public String getEnterpriseCount(@RequestBody String data){
		
		try {
			JSONObject jo = JSONObject.parseObject(data);
			int count = enterpriseService.getEnterpriseCount(jo);
			jo.put("count", count);
			return new RtnData(jo).toString();
		} catch (Exception e) {
			return new RtnData(-1,e.getMessage()).toString();
		}

	}
	
	@RequestMapping(value = "/deleteEnterprise", method = RequestMethod.POST)
	@ResponseBody
	public String deleteEnterprise(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Enterprise enterprise=JSONObject.parseObject(data, Enterprise.class);
		try {
			enterpriseService.deleteEnterprise(enterprise);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
}
