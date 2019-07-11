package com.invoice.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.CClient;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.CShopconnect;
import com.invoice.bean.db.Shop;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.ZbShopService;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class ZbShopRest {

	@Autowired
	ZbShopService shopService;

	@RequestMapping(value = "/ZbqueryShop", method = RequestMethod.POST)
	@ResponseBody
	public String queryShop(@RequestBody String data) {
		JSONObject jo = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);
			int count = shopService.getShopCount(jo);
			List<HashMap<String, String>> tfo = shopService.queryShop(jo);
			return new RtnData(tfo, count).toString();
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/ZbinsertShop", method = RequestMethod.POST)
	@ResponseBody
	public String insertShop(@RequestBody String data) {
		JSONObject jo = new JSONObject();
		Shop shop = JSONObject.parseObject(data, Shop.class);
		Shop shopid = null;

		try {

			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			shop.setEntid(token.getEntid());
			shopid = shopService.getShopById(shop);
			if (shopid != null) {
				jo.put("code", "-2");
				jo.put("msg", "机构编码重复，请重新输入");
				return jo.toJSONString();
			}
			PostMethod postMethod = new PostMethod(
					FGlobal.portalurl + "rest/ui/module/findEnterpriseRegister.action?entid=" + token.getEntid()
							+ "&token=" + token.getTokenid());
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
			postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
			HttpClient httpClient = new HttpClient();
			int statusCode = 0;
			statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == 200) {
				statusCode = 0;
				shopService.insertShop(shop);
				try {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					Map<String, String> headMap = new HashMap<String, String>();
					paramMap.put("entid", token.getEntid());
					paramMap.put("token", token.getTokenid());
					paramMap.put("flag", "I");
					paramMap.put("data", JSONObject.toJSONString(shop));
					HttpClientCommon.doPost(paramMap, headMap,
							FGlobal.portalurl + "rest/ui/Zbmodule/ZbregisterShop.action", 3000, 3000, "utf-8");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("访问PORTAL门店API异常");
				}
				jo.put("code", "0");
			} else {
				jo.put("code", "-1");
				jo.put("msg", "许可机构数量已满");
			}

		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
	}

	@RequestMapping(value = "/ZbgetShopById", method = RequestMethod.POST)
	@ResponseBody
	public String getShopById(@RequestBody String data) {
		Shop shop = JSONObject.parseObject(data, Shop.class);
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

	@RequestMapping(value = "/ZbupdateShop", method = RequestMethod.POST)
	@ResponseBody
	public String updateShop(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Shop shop = JSONObject.parseObject(data, Shop.class);
		try {
			Token token = Token.getToken();
			shop.setEntid(token.getEntid());

			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, String> headMap = new HashMap<String, String>();
			paramMap.put("entid", token.getEntid());
			paramMap.put("token", token.getTokenid());
			paramMap.put("flag", "U");
			paramMap.put("data", JSONObject.toJSONString(shop));
			String obj = HttpClientCommon.doPost(paramMap, headMap,
					FGlobal.portalurl + "rest/ui/Zbmodule/ZbregisterShop.action", 3000, 3000, "utf-8");
			JSONObject retobj = JSONObject.parseObject(obj);
			if (retobj.getString("code").equals("1")) {
				shopService.updateShop(shop);
				returnJson.put("code", "0");
			} else {
				returnJson.put("code", "-1");
				returnJson.put("msg", "访问PORTAL门店API异常");
			}

		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}

	@RequestMapping(value = "/ZbdeleteShop", method = RequestMethod.POST)
	@ResponseBody
	public String deleteShop(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Shop shop = JSONObject.parseObject(data, Shop.class);
		try {
			Token token = Token.getToken();
			shop.setEntid(token.getEntid());
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, String> headMap = new HashMap<String, String>();
			paramMap.put("entid", token.getEntid());
			paramMap.put("token", token.getTokenid());
			paramMap.put("flag", "D");
			paramMap.put("data", JSONObject.toJSONString(shop));
			String obj = HttpClientCommon.doPost(paramMap, headMap,
					FGlobal.portalurl + "rest/ui/Zbmodule/ZbregisterShop.action", 3000, 3000, "utf-8");
			JSONObject retobj = JSONObject.parseObject(obj);
			if (retobj.getString("code").equals("1")) {
				shopService.deleteShop(shop);
				
				returnJson.put("code", "0");
			} else {
				returnJson.put("code", "-1");
				returnJson.put("msg", "访问PORTAL门店API异常");
			}
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}

	@RequestMapping(value = "/ZbupdateOption", method = RequestMethod.POST)
	@ResponseBody
	public String ZbupdateOption(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		CConnect cc = JSONObject.parseObject(data, CConnect.class);
		CClient ccl = JSONObject.parseObject(data, CClient.class);
		CShopconnect cshop = JSONObject.parseObject(data, CShopconnect.class);
		JSONObject client = JSONObject.parseObject(data);
		ccl.setPassword(client.getString("clientpassword"));
		ccl.setUrl(client.getString("clienturl"));
		try {
			Token token = Token.getToken();
			cc.setEntid(token.getEntid());
			ccl.setEntid(token.getEntid());
			cshop.setEntid(token.getEntid());

			CConnect(cc, ccl, cshop);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}

	public void CConnect(CConnect cc, CClient ccl, CShopconnect cshop) throws Exception {

		CConnect rtcc = shopService.getConnentById(cc);
		CClient rtccl = shopService.getClientById(ccl);
		CShopconnect rtcshop = shopService.getCShopById(cshop);
		if (rtcc == null) {
			shopService.insertConnent(cc);
		} else {
			shopService.updateConnent(cc);
		}
		if (rtccl == null) {
			shopService.insertClient(ccl);
		} else {
			shopService.updateClient(ccl);
		}
		if (rtcshop == null) {
			shopService.insertCshop(cshop);
		} else {
			shopService.updateCshop(cshop);
		}

	}
	
}
