package com.invoice.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.ui.Option;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.dao.TaxdataDao;
import com.invoice.uiservice.service.impl.LookupServiceImpl;
/**
 * @author Baij
 * 界面调用
 * 路径 http://localhost/e-invoice/ui/*
 */
@Controller
@RequestMapping(value = "/ui")
public class UIZbRest {
	private final Log log = LogFactory.getLog(UIZbRest.class);
	
	
	@Autowired
	TaxdataDao taxdataDao;
	
	
	@RequestMapping(value = "/Zbgetiseinvoice", method = RequestMethod.POST)
	@ResponseBody
	public String Zbgetiseinvoice(){
			
		try {
			List<Option> res = new ArrayList<Option>();
			Option o =new Option();
			o.setName("开通");
			o.setValue("1");
			res.add(o);
			Option o1 =new Option();
			o1.setName("不开通");
			o1.setValue("0");
			res.add(o1);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e,e);
			return new RtnData(-1,e.getMessage()).toString();
		}
	
	}
	
	@RequestMapping(value = "/Zbgetshopstatus", method = RequestMethod.POST)
	@ResponseBody
	public String Zbgetshopstatus(){
			
		try {
			List<Option> res = new ArrayList<Option>();
			Option o =new Option();
			o.setName("启用");
			o.setValue("1");
			res.add(o);
			Option o1 =new Option();
			o1.setName("禁用");
			o1.setValue("0");
			res.add(o1);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e,e);
			return new RtnData(-1,e.getMessage()).toString();
		}
	
	}
	
	@RequestMapping(value = "/Zbgetselectshoptype", method = RequestMethod.POST)
	@ResponseBody
	public String Zbgetselectshoptype(){
			
		try {
			List<Option> res = new ArrayList<Option>();
			Option o =new Option();
			o.setName("门店");
			o.setValue("0");
			res.add(o);
			Option o1 =new Option();
			o1.setName("仓库");
			o1.setValue("1");
			res.add(o1);
			Option o2 =new Option();
			o2.setName("总部");
			o2.setValue("2");
			res.add(o2);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e,e);
			return new RtnData(-1,e.getMessage()).toString();
		}
	
	}
	
//	@RequestMapping(value = "/getcuruser", method = RequestMethod.POST)
//	@ResponseBody
//	public String getcuruser(){
//		try {
//			Token token = Token.getToken();
//			return new RtnData(token).toString();
//		} catch (Exception e) {
//			log.error(e,e);
//			return new RtnData(-1,e.getMessage()).toString();
//		}
//	
//	}
//	
//	@RequestMapping(value = "/getShopList", method = RequestMethod.POST)
//	@ResponseBody
//	public String getShopList(){
//		try {
//			Token token = Token.getToken();
//			List<Option> res = new ArrayList<Option>();
//			for(int i=0;i<token.getShoplist().size();i++){
//				Option op = new Option();
//				op.value=token.getShoplist().get(i).get("shopid").toString();
//				op.name=token.getShoplist().get(i).get("shopname").toString();
//				res.add(op);
//			}
//			return new RtnData(res).toString();
//		} catch (Exception e) {
//			log.error(e,e);
//			return new RtnData(-1,e.getMessage()).toString();
//		}
//	
//	}
//	
//	@RequestMapping(value = "/gettaxdata", method = RequestMethod.POST)
//	@ResponseBody
//	public String gettaxdata(@RequestBody String data) {
//		JSONObject jo = JSONObject.parseObject(data);
//		try {
//			int count = taxdataDao.getTaxdataCount(jo);
//			List<HashMap<String,String>> ep  = taxdataDao.gettaxdata(jo);
//			return new RtnData(ep,count).toString();
//		} catch (Exception e) {
//			return new RtnData(-1, e.getMessage()).toString();
//		}
//
//	}
	
}
