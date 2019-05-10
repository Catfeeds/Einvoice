package com.invoice.apiservice.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.Invque;
import com.invoice.bean.ui.InvoiceTitle;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.LocalRuntimeException;
import com.invoice.util.SHA1;

/**
 * 处理一些微信相关的
 * 
 * @author Baij
 * 
 */
@Service
public class WxService {
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	EntPrivatepara entPrivatepara;
	@Autowired
	EnterpriseService enterpriseService;

	synchronized public String getTicket(String entid, String callback, String weburl) {
		//TODO 未处多线程理异步问题
		try {
			String weixinAppid = entPrivatepara.get(entid, FGlobal.WeixinAppID);
			String jsapi_ticket = getJsApiTicket(entid);
			String noncestr = "abcd1234";
			int timestamp = (int) (System.currentTimeMillis() / 1000);
			String mark = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp
					+ "&url=" + weburl;
			String signature = SHA1.sha1(mark);

			JSONObject json = new JSONObject();
			json.put("appId", weixinAppid);
			json.put("signature", signature);
			json.put("jsapi_ticket", jsapi_ticket);
			json.put("url", weburl);
			json.put("noncestr", noncestr);
			json.put("timestamp", timestamp);

			if (!StringUtils.isEmpty(callback)) {
				String res = json.toJSONString();
				res = callback + "(" + res + ")";
				return new RtnData(res).toString();
			}
			return new RtnData(json).toString();
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	synchronized public String getAccessToken(String entid) {
		String secret = FGlobal.WeixTokenMap.get("access_token");
		String expires = FGlobal.WeixTokenMap.get("expires");
		
		if (secret == null || Long.parseLong(expires) < System.currentTimeMillis()) {
			String weixinAppid = entPrivatepara.get(entid, FGlobal.WeixinAppID);
			String weixinSecret = entPrivatepara.get(entid, FGlobal.WeixinSecret);
			
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
					+ weixinAppid + "&secret=" + weixinSecret;
			String res = HttpClientCommon.doGet(null, null, url, 2000, 2000, "utf-8");
			log.info("getAccessToken:"+res);
			JSONObject json = JSONObject.parseObject(res);
			secret = json.getString("access_token");
			long expires_in = json.getIntValue("expires_in");
			FGlobal.WeixTokenMap.put("access_token", secret);
			expires_in = System.currentTimeMillis() + expires_in * 1000;
			FGlobal.WeixTokenMap.put("expires", String.valueOf(expires_in));
		}
		
		return secret;
	}
	
	synchronized public String getJsApiTicket(String entid) {
		String jsapi_ticket = FGlobal.WeixTokenMap.get("jsapi_ticket");
		String expires = FGlobal.WeixTokenMap.get("expires");
		
		if (jsapi_ticket == null || Long.parseLong(expires) < System.currentTimeMillis()) {
			String access_token = getAccessToken(entid);
			
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";
			String res = HttpClientCommon.doGet(null, null, url, 2000, 2000, "utf-8");
			log.info("getJsApiTicket:"+res);
			JSONObject json = JSONObject.parseObject(res);
			jsapi_ticket = json.getString("ticket");
			FGlobal.WeixTokenMap.put("jsapi_ticket", jsapi_ticket);
		}
		
		return jsapi_ticket;
	}
	
	
	
	
	public String getSpappid(String entid) {
		String token = getAccessToken(entid);
		String url = "https://api.weixin.qq.com/card/invoice/seturl?access_token="+token;
		String res = HttpClientCommon.doPostStream("{}", null, url, 0, 0, "utf-8");
		log.info("initSpappid:"+res);
		JSONObject json = JSONObject.parseObject(res);
		return json.getString("invoice_url");
	}
	
	synchronized public String getCardId(String entid){
		String id = entPrivatepara.get(entid, FGlobal.WeixinCard);
		
		if(StringUtils.isEmpty(id)){
			Enterprise enterprise = new Enterprise();
			enterprise.setEntid(entid);
			Enterprise ent = enterpriseService.getEnterpriseById(enterprise);
			
			
			String token = getAccessToken(entid);
			
			JSONObject js = new JSONObject();
			JSONObject invoice_info = new JSONObject();
			js.put("invoice_info", invoice_info);
			JSONObject base_info = new JSONObject();
			invoice_info.put("base_info", base_info);
			invoice_info.put("payee", ent.getEntname());
			invoice_info.put("type", "电子发票");
			
			String logo_url="https://mmbiz.qpic.cn/mmbiz_png/a2T8UdtDMqJsRTNplWz0ttTRwnAiaElTd2CI88aVUGuicGfvV5b9jYLKRPcUdd1Wd5OmlORbXa1JcicNN5H3EickTw/0";
			base_info.put("logo_url", logo_url);
			base_info.put("title", "富基融通");
			base_info.put("description", "电子发票卡包");
			
			
			String url = "https://api.weixin.qq.com/card/invoice/platform/createcard?access_token="+token;
			log.info("createCard request:"+url);
			String res = HttpClientCommon.doPostStream(js.toJSONString(), null, url, 0, 0, "utf-8");
			log.info("createCard response:"+res);
			JSONObject json = JSONObject.parseObject(res);
			entPrivatepara.set(entid, FGlobal.WeixinCard, json.getString("card_id"));
		}
		
		return id;
	}
	
	/**
	 * 获取授权页ticket
	 */
	public String getCardTicket(String entid) {
		String token = getAccessToken(entid);
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token+"&type=wx_card";
		log.info("getCardTicket request:"+url);
		String res = HttpClientCommon.doGet(null, null, url, 0, 0, "utf-8");
		log.info("getCardTicket response:"+res);
		JSONObject json = JSONObject.parseObject(res);
		return json.getString("ticket");
	}
	
	
	/**
	 * 获取授权页，用户方位该授权页
	 * @param que
	 * @return
	 */
	public String getCardAuthurl(Invque que) {
		String ticket = getCardTicket(que.getIqentid());
		String token = getAccessToken(que.getIqentid());
		String url = "https://api.weixin.qq.com/card/invoice/getauthurl?access_token="+token;
		
		JSONObject js = new JSONObject();
		js.put("s_pappid", "wxabcd");
		js.put("order_id", que.getIqseqno());
		js.put("money", Integer.parseInt(((que.getIqtotje()+que.getIqtotse())*100)+""));
		js.put("timestamp", new Date().getTime());
		js.put("source", "web");
		js.put("redirect_url", "");
		js.put("ticket", ticket);
		js.put("type", 2);
		
		log.info("getCardAuthurl request:"+url);
		String res = HttpClientCommon.doPostStream(js.toJSONString(), null, url, 0, 0, "utf-8");
		log.info("getCardAuthurl response:"+res);
		JSONObject json = JSONObject.parseObject(res);
		String authUrl = json.getString("auth_url");
		return new RtnData(authUrl).toString();
	}
	
	public void insertCard(Invque que) throws IOException {
		String token = getAccessToken(que.getIqentid());
		String url = "https://api.weixin.qq.com/card/invoice/insert?access_token="+token;
		String cardid = getCardId(que.getIqentid());
		String appid = entPrivatepara.get(que.getIqentid(), FGlobal.WeixinAppID);
		JSONObject js = new JSONObject();
		js.put("order_id",que.getIqseqno());
		js.put("card_id",cardid);
		js.put("appid",appid);
		JSONObject card_ext = new JSONObject();
		js.put("card_ext", card_ext);
		
		card_ext.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
		JSONObject user_card = new JSONObject();
		card_ext.put("user_card", user_card);
		user_card.put("fee", Integer.parseInt(((que.getIqtotje()+que.getIqtotse())*100)+""));
		user_card.put("title", que.getIqgmfname());
		String kprq = que.getRtkprq();
		Date d = new Date();
		if(!StringUtils.isEmpty(kprq)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
			try {
				d = sdf.parse(kprq);
			} catch (ParseException e) {
			}
		}
		
		user_card.put("billing_time", d.getTime());
		user_card.put("billing_no", que.getRtfpdm());
		user_card.put("billing_code", que.getRtfphm());
		user_card.put("fee_without_tax", Integer.parseInt(((que.getIqtotje())*100)+""));
		user_card.put("tax", Integer.parseInt(((que.getIqtotse())*100)+""));
		user_card.put("check_code", que.getRtskm());
		user_card.put("check_code", que.getRtskm());
		user_card.put("s_pdf_media_id", uploadPdf(que));//TODO 上传PDF
		
		log.info("insertCard request:"+url);
		String res = HttpClientCommon.doPostStream(js.toJSONString(), null, url, 0, 0, "utf-8");
		log.info("insertCard response:"+res);
		JSONObject json = JSONObject.parseObject(res);
		
	}
	
	public String uploadPdf(Invque que) throws IOException {
		File file = File.createTempFile("invoice", ".pdf");
		HttpClientCommon.getFile(file, que.getIqpdf(), 0, 0, "utf-8");
		log.info("uploadPdf getFile:"+file.getAbsolutePath());
		
		String token = getAccessToken(que.getIqentid());
		String url = "https://api.weixin.qq.com/card/invoice/platform/setpdf?access_token="+token;
		
		log.info("uploadPdf request:"+url);
		String res = HttpClientCommon.postFile("pdf", file, url, 0, 0, "utf-8");
		log.info("uploadPdf response:"+res);
		JSONObject json = JSONObject.parseObject(res);
		return json.getString("s_media_id");
	}

	public String getAppid(String entid) {
		return entPrivatepara.get(entid, FGlobal.WeixinAppID);
	}

	public String getSecret(String entid) {
		return entPrivatepara.get(entid, FGlobal.WeixinSecret);
	}
	
	public String getNote(String entid) {
		return entPrivatepara.get(entid, FGlobal.WeixinNote);
	}
	
	public String getIndex(String entid) {
		String tmp = entPrivatepara.get(entid, FGlobal.weixinIndex);
		if(StringUtils.isEmpty(tmp)) tmp="invoiceOpen.html";
		return tmp;
	}

	public InvoiceTitle scanTitle(String entid, String scanText) {
		String token = getAccessToken(entid);
		JSONObject  js = new  JSONObject();
		js.put("scan_text", scanText);
		String url = "https://api.weixin.qq.com/card/invoice/scantitle?access_token="+token;
		String res = HttpClientCommon.doPostStream(js.toJSONString(), null, url, 0, 0, "utf-8");
		log.info(res);
		JSONObject jo = JSONObject.parseObject(res);
		if(jo.getIntValue("errcode")==0) {
			InvoiceTitle it = new InvoiceTitle();
			it.setTitle(jo.getString("title"));
			it.setAddr(jo.getString("addr"));
			it.setBank(jo.getString("bank_type"));
			it.setBankNo(jo.getString("bank_no"));
			it.setPhone(jo.getString("phone"));
			it.setTaxNo(jo.getString("tax_no"));
			return it;
		}else {
			throw new LocalRuntimeException("获取失败:"+jo.getString("errmsg"));
		}
	}
}
