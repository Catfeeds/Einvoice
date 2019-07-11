package com.invoice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.BillTax;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.BillTaxService;
import com.invoice.util.Page;

@Controller
@RequestMapping(value = "/ui")
public class BillTaxRest {
	@Autowired
	BillTaxService billTaxService;
	
 
	@RequestMapping(value = "/queryBillTax", method = RequestMethod.POST)
	@ResponseBody
	public String queryBillTax(@RequestBody String data) {
		try {
			JSONObject jo=JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			Page.cookPageInfo(jo);
			List<BillTax> list = billTaxService.queryBillTax(jo);
			int count = billTaxService.getBillTaxCount(jo);
			return new RtnData(list,count).toString();
		} catch (Exception e) {
			e.printStackTrace();	
			return new RtnData(-1, e.getMessage()).toString();
		}

	}
	
	 
	@RequestMapping(value = "/insertBillTax", method = RequestMethod.POST)
	@ResponseBody
	public String insertBillTax(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		BillTax catetax=JSONObject.parseObject(data, BillTax.class);
		Token token = Token.getToken();
		try {
			catetax.setEntid(token.getEntid());
			billTaxService.insertBillTax(catetax);
			returnJson.put("entid",token.getEntid());
			BillTax ct  = billTaxService.getBillTaxById(catetax);
			returnJson.put("code", "0");
			returnJson.put("data", ct);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
 
	@RequestMapping(value = "/deleteBillTaxByid", method = RequestMethod.POST)
	@ResponseBody
	public String deleteBillTaxByid(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		BillTax catetax=JSONObject.parseObject(data, BillTax.class);
		try {
			Token token = Token.getToken();
			catetax.setEntid(token.getEntid());
			billTaxService.deleteBillTax(catetax);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}
	
	@RequestMapping(value = "/updateBillTax", method = RequestMethod.POST)
	@ResponseBody
	public String updateBillTax(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		BillTax catetax=JSONObject.parseObject(data, BillTax.class);
		try {
			Token token = Token.getToken();
			catetax.setEntid(token.getEntid());
			billTaxService.updateBillTax(catetax);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}
	
	@RequestMapping(value = "/getBillTaxById", method = RequestMethod.POST)
	@ResponseBody
	public String getBillTaxById(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		BillTax catetax=JSONObject.parseObject(data, BillTax.class);
		Token token = Token.getToken();
		try {
			returnJson.put("entid",token.getEntid());
			catetax.setEntid(token.getEntid());
			BillTax ct = billTaxService.getBillTaxById(catetax);
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

