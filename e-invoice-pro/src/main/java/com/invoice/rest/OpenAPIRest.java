package com.invoice.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.invoice.apiservice.service.impl.OpenapiService;
import com.invoice.rtn.data.RtnData;
import com.invoice.rtn.data.RtnData2;
import com.invoice.util.Base64;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.MD5;

@Controller
@RequestMapping(value = "/")
public class OpenAPIRest {
	public final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	OpenapiService service;

	/**
	 * 小票数据接收
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/recv/sell")
	@ResponseBody
	public String sheetRecv(@RequestParam(value = "entid", required = true) String entid,
			@RequestParam(value = "data", required = true) String data,
			@RequestParam(value = "sign", required = true) String sign) {

		if (!service.checkSign(entid, data, sign)) {
			return new RtnData(-99, "鉴权不通过").toString();
		}
		return new RtnData("success").toString();
	}

	/**
	 * 销售数据接收接口
	 * 
	 * @param entid
	 * @param data
	 * @param sign
	 * @return json string
	 */
	@RequestMapping(value = "/recv/push")
	@ResponseBody
	public String push(@RequestParam(value = "entid", required = true) String entid,
			@RequestParam(value = "data", required = true) String data,
			@RequestParam(value = "sign", required = true) String sign) {
		try {
			if (!service.checkSign(entid, data, sign)) {
				return new RtnData2("-99", "鉴权不通过").toString();
			}
		} catch (Exception e) {
			log.info("sign:" + sign);
			log.info("data:" + data);
			return new RtnData2("-99", "鉴权不通过，注意特殊符合需要URL转义data:" + data + "; sign:" + sign).toString();
		}

		return service.push(entid, data);
	}
	
	/**
	 * 已开票数据查询接口
	 * 
	 * @param entid
	 * @param data
	 * @param sign
	 * @return json string
	 */
	@RequestMapping(value = "/invoice/search")
	@ResponseBody
	public String search(@RequestParam(value = "entid", required = true) String entid,
			@RequestParam(value = "data", required = true) String data,
			@RequestParam(value = "sign", required = true) String sign) {
		try {
			if (!service.checkSign(entid, data, sign)) {
				return new RtnData2("-99", "鉴权不通过").toString();
			}
			return service.search(entid, data);
		} catch (Exception e) {
			log.info("sign:" + sign);
			log.info("data:" + data);
			return new RtnData2("-99", "鉴权不通过，注意特殊符号需要URL转义data:" + data + "; sign:" + sign).toString();
		}

	}

	static public void main(String[] args) {
		// 企业编码
		String entid = "A00001002";
		// 密匙
		String checkword = "efuture";
		// 发送的业务数据
		String data = "{\"data\":[{\"sheetid\":\"14add0d8879844fb5b5d07c07b294c27d00000001\",\"sheettype\":\"2\",\"sdate\":\"1528439190567\",\"billno\":\"1028\",\"amt\":\"334.00\",\"zlamt\":\"0\",\"shopid\":\"10001\",\"editor\":\"90001\",\"syjid\":\"1\",\"isauto\":\"0\",\"taxtitle\":\"华润控股深圳分公司\",\"taxno\":\"420348431027623112G\",\"taxaddr\":\"深圳南山区洪头街2009号\",\"taxbank\":\"华润银行深圳支行 6524378410323627\",\"sheetdetail\":[{\"taxrate\":\"0.17\",\"unit\":\"个\",\"amt\":\"85.00\",\"categoryid\":\"4-Bean Coffee\",\"itemname\":\"新低因祥龙\",\"rowno\":\"1\",\"qty\":\"1\",\"itemid\":\"6101135\"},{\"taxrate\":\"0.17\",\"unit\":\"个\",\"amt\":\"85.00\",\"categoryid\":\"4-Bean Coffee\",\"itemname\":\"低因祥龙咖啡\",\"rowno\":\"2\",\"qty\":\"1\",\"itemid\":\"6101210\"},{\"taxrate\":\"0.17\",\"unit\":\"个\",\"amt\":\"95.00\",\"categoryid\":\"4-Bean Coffee\",\"itemname\":\"新肯亚\",\"rowno\":\"3\",\"qty\":\"1\",\"itemid\":\"6101134\"},{\"taxrate\":\"0.17\",\"unit\":\"个\",\"amt\":\"69.00\",\"categoryid\":\"111-VIA\",\"itemname\":\"派克滴滤6包\",\"rowno\":\"4\",\"qty\":\"1\",\"itemid\":\"6110301\"}],\"sheetpayment\":[{\"rowno\":\"1\",\"amt\":\"334.00\",\"payid\":\"1\",\"payname\":\"Cash\"}]}]}";
		System.out.println(data);
		String str = data + checkword;
		String sign = MD5.md5(str).toUpperCase();
		sign = Base64.encode(sign.getBytes());// 计算后的sign

		// 将data以base64编码，作为发送的内容
		data = Base64.encode(data.getBytes());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entid", entid);
		params.put("data", data);
		params.put("sign", sign);
		String res = HttpClientCommon.post(params, null,
				"http://fptest.cloud360.com.cn/e-invoice-pro/openapi/recv/push", 0, 0, "utf-8");
		System.out.println(res);
	}
}
