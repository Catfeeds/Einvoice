package com.invoice.rest;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.Option;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.TaxinfoService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class TaxinfoRest {
	
	@Autowired
	TaxinfoService taxinfoService;
	
	@RequestMapping(value = "/queryTaxinfo", method = RequestMethod.POST)
	@ResponseBody
	public String queryTaxinfo(@RequestBody String data){
		JSONObject jo = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);
			int count = taxinfoService.getTaxinfoCount(jo);
			List<HashMap<String,String>> tfo = taxinfoService.queryTaxinfo(jo);
			return new RtnData(tfo,count).toString();			
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();		
		}
	}
	
	@RequestMapping(value = "/insertTaxinfo", method = RequestMethod.POST)
	@ResponseBody
	public String insertTaxinfo(@RequestBody String data) {
		JSONObject jo = new JSONObject();
		Taxinfo taxinfo=JSONObject.parseObject(data, Taxinfo.class);
		try {
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			taxinfo.setEntid(token.getEntid());
			PostMethod postMethod = new PostMethod(FGlobal.portalurl
					+ "rest/ui/module/findEnterpriseRegister.action?entid=" + token.getEntid()+"&token="+token.getTokenid());
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
			postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
			HttpClient httpClient = new HttpClient();
			int statusCode = 0;
			statusCode = httpClient.executeMethod(postMethod);
			System.out.println("statusCode "+statusCode);
			if (statusCode == 200) {
				String tokeninfo = postMethod.getResponseBodyAsString();
				JSONObject tokenjson = new JSONObject();
				tokenjson = JSONObject.parseObject(tokeninfo);
				int count = taxinfoService.getTaxinfoCount(jo);
				JSONObject jsonObject = tokenjson.getJSONObject("data");
				if(count<jsonObject.getInteger("taxnonum")){
					taxinfoService.insertTaxinfo(taxinfo);
					jo.put("code", "0");
				}else{
					jo.put("code", "-1");
					jo.put("msg","许可纳税号数量已满");
				}
				
			}
		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
}
	
	@RequestMapping(value = "/getTaxinfoByNo", method = RequestMethod.POST)
	@ResponseBody
	public String getTaxinfoByNo(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Taxinfo taxinfo=JSONObject.parseObject(data, Taxinfo.class);	
		try {
			Token token = Token.getToken();
			taxinfo.setEntid(token.getEntid());
			
			Taxinfo tf = taxinfoService.getTaxinfoByNo(taxinfo);
			returnJson.put("code", "0");
			returnJson.put("data", tf);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

    	return returnJson.toJSONString();
	}
	
	
	@RequestMapping(value = "/updateTaxinfo", method = RequestMethod.POST)
	@ResponseBody
	public String updateTaxinfo(@RequestBody String data) {
		JSONObject jo = new JSONObject();
		Taxinfo taxinfo=JSONObject.parseObject(data, Taxinfo.class);
		try {	
			Token token = Token.getToken();
			taxinfo.setEntid(token.getEntid());
			jo.put("entid", token.getEntid());
			taxinfoService.updateTaxinfo(taxinfo);
			jo.put("code", "0");
		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
}	
	
	
	@RequestMapping(value = "/getTaxnoSelect", method = RequestMethod.POST)
	@ResponseBody
	public String test(@RequestParam(value = "entid", required = false) String entid){
			
		try {
			Token token = Token.getToken();
			List<Option> res = taxinfoService.getTaxnoSelect(token.getEntid());
			return new RtnData(res).toString();
		} catch (Exception e) {
			return new RtnData(-1,e.getMessage()).toString();
		}
	
	}
	
	
	@RequestMapping(value = "/deleteTaxinfo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteTaxinfo(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Taxinfo taxinfo=JSONObject.parseObject(data, Taxinfo.class);
		try {
			Token token = Token.getToken();
			taxinfo.setEntid(token.getEntid());
			taxinfoService.deleteTaxinfo(taxinfo);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}	
}
