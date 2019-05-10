package com.invoice.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.Base64;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.MD5;
import com.invoice.util.Serial;

/**
 * @author baij demo用
 */
@Controller
@RequestMapping(value = "/demo")
public class DemoRest {
	@RequestMapping(value = "/generateCoupons")
	@ResponseBody
	public String generateCoupons(String pCount, String pAmt) {
		String url = "http://test.baixingliangfan.cn/baixing/fujiCoupons/generateCoupons";
		int count = 1;
		if(!StringUtils.isEmpty(pCount)) {
			count = Integer.parseInt(pCount);
		}
		float amt = 100;
		if(!StringUtils.isEmpty(pAmt)) {
			amt = Float.parseFloat(pAmt);
		}
		
		JSONArray jData = new JSONArray();
		
		for (int i = 0; i < count; i++) {
			JSONObject jo = new JSONObject();
			jo.put("voucher_code", Serial.getInvoiceSerial().substring(2));
			jo.put("voucher_desc", "测试");
			jo.put("voucher_start_date", "20180301");
			jo.put("voucher_end_date", "20180601");
			jo.put("voucher_active_date", "20180329");
			jo.put("voucher_price", amt);
			jo.put("voucher_state", 1);
			jo.put("voucher_t_limit_stores", "0");
			jo.put("voucher_cat", "生鲜");
			jo.put("voucher_pre_price", amt*2);
			jo.put("voucher_1", "");
			jo.put("voucher_2", "");
			jo.put("voucher_3", "");
			jData.add(jo);
		}
		
		
		String data = jData.toJSONString();
		Map<String, Object> params = cookParams(data);

		String res = HttpClientCommon.post(params, null, url, 0, 0, "utf-8");
		
		String r  = "发送的报文：entid="+params.get("entid")+"&data="+params.get("data")+"&sign="+params.get("sign")+"; 收到的报文："+res;
		
		return new RtnData(r).toString();
	}

	@RequestMapping(value = "/changeCouponState")
	@ResponseBody
	public String changeCouponState(String code) {
		if(StringUtils.isEmpty(code)) {
			return new RtnData(-1,"需要提供逗号分隔的券编号").toString();
		}
		String url = "http://test.baixingliangfan.cn/baixing/fujiCoupons/changeCouponState";
		String[] codes = code.split(",");
		JSONArray jData = new JSONArray();
		for (String voucher_code : codes) {
			JSONObject jo = new JSONObject();
			jo.put("voucher_code", voucher_code);
			jo.put("voucher_state", 2);
			jo.put("voucher_store", "0");
			jo.put("voucher_1", "");
			jo.put("voucher_2", "");
			jo.put("voucher_3", "");
			jData.add(jo);
		}

		String data = jData.toJSONString();
		Map<String, Object> params = cookParams(data);

		String res = HttpClientCommon.post(params, null, url, 0, 0, "utf-8");
		
		String r  = "发送的报文："+data+"; 收到的报文："+res;
		
		return new RtnData(r).toString();
	}

	private Map<String, Object> cookParams(String data) {
		String entid = "001";
		String checkword = "efuture";
		String str = data + checkword;
		String sign = MD5.md5(str).toUpperCase();
		
		sign = Base64.byteArrayToBase64(sign.getBytes());
		data = Base64.byteArrayToBase64(data.getBytes());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entid", entid);
		params.put("data", data);
		params.put("sign", sign);
		return params;
	}
}
