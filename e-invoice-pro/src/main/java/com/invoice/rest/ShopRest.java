package com.invoice.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Shop;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class ShopRest {

	@Autowired
	ShopService shopService; 
	
	@RequestMapping(value = "/queryShop", method = RequestMethod.POST)
	@ResponseBody
	public String queryShop(@RequestBody String data){
		JSONObject jo = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);
			int count = shopService.getShopCount(jo);
			List<HashMap<String,String>> tfo = shopService.queryShop(jo);
			return new RtnData(tfo,count).toString();			
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();		
		}
	}
	
	@RequestMapping(value = "/insertShop", method = RequestMethod.POST)
	@ResponseBody
	public String insertShop(@RequestBody String data) {
		JSONObject jo = new JSONObject();
		Shop shop=JSONObject.parseObject(data, Shop.class);

		try {		
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			shop.setEntid(token.getEntid());
			PostMethod postMethod = new PostMethod(FGlobal.portalurl
					+ "rest/ui/module/findEnterpriseRegister.action?entid=" + token.getEntid()+"&token="+token.getTokenid());
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
			postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
			HttpClient httpClient = new HttpClient();
			int statusCode = 0;
			statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == 200) {
				String tokeninfo = postMethod.getResponseBodyAsString();
				JSONObject tokenjson = new JSONObject();
				tokenjson = JSONObject.parseObject(tokeninfo);
				int count = shopService.getShopCount(jo);
				JSONObject jsonObject = tokenjson.getJSONObject("data");
				if(count<jsonObject.getInteger("shopnum")){
					statusCode = 0;
					shopService.insertShop(shop);
/*					PostMethod postMethod2 = new PostMethod(FGlobal.portalurl
							+ "rest/ui/module/registerShop.action?entid=" + token.getEntid()+"&token="+token.getTokenid()+"&flag=I&data="+JSONObject.toJSONString(shop));
				
					postMethod2.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
					postMethod2.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
					postMethod2.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
					HttpClient httpClient1 = new HttpClient();
					statusCode = httpClient1.executeMethod(postMethod2);
					*/
					try {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					Map<String, String> headMap = new HashMap<String, String>();
					paramMap.put("entid", token.getEntid());
					paramMap.put("token", token.getTokenid());
					paramMap.put("flag", "I");
					paramMap.put("data", JSONObject.toJSONString(shop));
					HttpClientCommon.doPost(paramMap, headMap, FGlobal.portalurl+ "rest/ui/module/registerShop.action", 3000, 3000, "utf-8");
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("访问PORTAL门店API异常");
					}	
					jo.put("code", "0");
				}else{
					jo.put("code", "-1");
					jo.put("msg","许可机构数量已满");
				}
			}

		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
}
	
	@RequestMapping(value = "/getShopById", method = RequestMethod.POST)
	@ResponseBody
	public String getShopById(@RequestBody String data) {
		Shop shop=JSONObject.parseObject(data, Shop.class);
		JSONObject returnJson = new JSONObject();
		try {
			Token token = Token.getToken();
			shop.setEntid(token.getEntid());
			Shop sp = shopService.getShopById(shop);
			returnJson.put("code", "0");
			returnJson.put("data", sp);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

    	return returnJson.toJSONString();
	}

	@RequestMapping(value = "/updateShop", method = RequestMethod.POST)
	@ResponseBody
	public String updateShop(@RequestBody String data) {
		JSONObject jo = new JSONObject();
		Shop shop=JSONObject.parseObject(data, Shop.class);
		    Token token = Token.getToken();
		try {
			shop.setEntid(token.getEntid());
			shopService.updateShop(shop);
			jo.put("code", "0");
		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
}	
	
	
	@RequestMapping(value = "/deleteShop", method = RequestMethod.POST)
	@ResponseBody
	public String deleteShop(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Shop shop=JSONObject.parseObject(data, Shop.class);
		int statusCode = 0;
		try {
			Token token = Token.getToken();
			shop.setEntid(token.getEntid());
			shopService.deleteShop(shop);
			PostMethod postMethod2 = new PostMethod(FGlobal.portalurl
					+ "rest/ui/module/registerShop.action?entid=" + token.getEntid()+"&token="+token.getTokenid()+"&flag=D&data="+JSONObject.toJSONString(shop));
			postMethod2.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod2.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
			postMethod2.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
			HttpClient httpClient1 = new HttpClient();
			statusCode = httpClient1.executeMethod(postMethod2);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}	
}
