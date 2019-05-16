package com.invoice.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.impl.WxService;
import com.invoice.bean.db.Invque;
import com.invoice.bean.ui.Token;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.NewHashMap;

/**
 * @author Baij 微信API调用
 */
@Controller
@RequestMapping(value = "/wx")
public class WxRest {
	private final Log log = LogFactory.getLog(WxRest.class);
	
	@Autowired
	EntPrivatepara entPrivatepara;

	@Autowired
	WxService wxService;
	
	@Autowired
	InvqueDao inqueDao;
	
	
	@RequestMapping(value = "/reload")
	@ResponseBody
	public String reload() {
		entPrivatepara.init();
		return new RtnData("OK").toString();
	}
	
	@RequestMapping(value = "/createCard/{entid}")
	@ResponseBody
	public String createCard(@PathVariable String entid) {
		String cardid = wxService.getCardId(entid);
		return new RtnData(cardid).toString();
	}
	
	@RequestMapping(value = "/getSpappid/{entid}")
	@ResponseBody
	public String getSpappid(@PathVariable String entid) {
		String url = wxService.getSpappid(entid);
		return new RtnData(url).toString();
	}
	
	
	@RequestMapping(value = "/insertCard/{iqseqno}")
	@ResponseBody
	public String insertCard(@PathVariable String iqseqno) {
		
		if(StringUtils.isEmpty(iqseqno)) {
			return new RtnData(-1,"不能是空:"+iqseqno).toString();
		}
		
		Map<String, Object> p = new NewHashMap<>();
		p.put("iqseqno", iqseqno);
		List<Invque> list = inqueDao.getInvque(p);
		if(list==null || list.isEmpty()) {
			return new RtnData(-1,"不存在:"+iqseqno).toString();
		}
		try {
			wxService.insertCard(list.get(0));
		} catch (IOException e) {
			return new RtnData(-1,e.toString()).toString();
		}
		return new RtnData("ok").toString();
	}
	
	/**
	 * 扫描微信发票抬头
	 * @param callback
	 * @param weburl
	 * @return
	 */
	@RequestMapping(value = "/scanTitle")
	@ResponseBody
	public String scanTitle(
			@RequestParam(value = "scanText", required = false) String scanText) {
		Token token = Token.getToken();
		String entid = token.getEntid();
		return new RtnData(wxService.scanTitle(entid,scanText)).toString();
	}

	/**
	 * 微信ticket
	 * @param callback
	 * @param weburl
	 * @return
	 */
	@RequestMapping(value = "/getWeixTicket")
	@ResponseBody
	public String getTicket(
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "weburl", required = false) String weburl) {
		Token token = Token.getToken();
		String entid = token.getEntid();
		return wxService.getTicket(entid, callback, weburl);
	}
	
	@RequestMapping(value = "/getWeixConfig")
	@ResponseBody
	public String getWeixConfig(HttpSession session) {
		
		Token token = Token.getToken();
		if(token==null){
			return new RtnData(-1, "请从菜单进入").toString();
		}
		
		String entid = token.getEntid();
		Map<String, String> map = new HashMap<String, String>();
		//微信配置
		map.put(FGlobal.useEmail, entPrivatepara.get(entid, FGlobal.useEmail));
		map.put(FGlobal.usePhone, entPrivatepara.get(entid, FGlobal.usePhone));
		map.put(FGlobal.useQR, entPrivatepara.get(entid, FGlobal.useQR));
		map.put(FGlobal.WeixinNote,wxService.getNote(entid));
		map.put(FGlobal.WeixinHelp,entPrivatepara.get(entid, FGlobal.WeixinHelp));
		map.put("sheettype", session.getAttribute("sheettype")==null?"1":session.getAttribute("sheettype").toString());
		map.put("shopid", token.getShopid());
		map.put("entid", entid);
		map.put("qr", session.getAttribute("qr")==null?"1":session.getAttribute("qr").toString());
		
		return new RtnData(map).toString();
	}

	/**
	 * 微信接入入口
	 * 页面直接指定用户的openid以及企业entid 如果没有指定openid 则尝试通过微信api获取
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wxopen")
	@ResponseBody
	public String wxopen(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "entid", required = false) String entid,
			@RequestParam(value = "shopid", required = false) String shopid,
			@RequestParam(value = "sheettype", required = false) String sheettype,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "qr", required = false) String qr,
			@RequestParam(value = "data", required = false) String data,
			@RequestParam(value = "sign", required = false) String sign) {
		
		if (StringUtils.isEmpty(entid)) {
			return new RtnData(-1, "必须指定企业信息").toString();
		}
		
		if (StringUtils.isEmpty(sheettype)) {
			return new RtnData(-1, "必须指定业务类型").toString();
		}
		
		//TODO 微信公众号过期，暂时开启此处
		//openid=entid;
		//TODO 微信公众号过期，暂时开启此处
		
		/*处理二维码中含有完整小票信息*/
		if (data != null && !data.toLowerCase().equals("null") && data.length() > 0 && 
		    sign != null && !sign.toLowerCase().equals("null") && sign.length() > 0)
		{
			try {
				String myUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/openapi/recv/push";
				Map<String, Object> paramMap = new HashMap<String, Object>();
				Map<String, String> headMap = new HashMap<String, String>();
				paramMap.put("entid", entid);
				paramMap.put("data", data);
				paramMap.put("sign", sign);
				
				String res = HttpClientCommon.doPost(paramMap, headMap, myUrl, 30000, 30000, "utf-8");
				if(StringUtils.isEmpty(res)){
					return new RtnData(-1, "保存小票数据失败!").toString();
				}
				
				JSONObject js = JSONObject.parseObject(res);
				if(js.getIntValue("code") != 0 || js.getIntValue("status") != 0){
					String resText = js.getString("message");
					if (resText == null || resText.length() == 0) resText = "未知错误";
					return new RtnData(-1, "保存小票数据失败：" + resText + "!").toString();
				}
			}
			catch(Exception e){
				e.printStackTrace();
				return new RtnData(-1, "保存小票数据失败：" + e.getMessage()).toString();
			}
		}
		
		if (!StringUtils.isEmpty(qr)) {
			qr = qr.trim();
		}
		
		if (!StringUtils.isEmpty(entid)) {
			entid = entid.trim();
		}

		String url = wxService.getIndex(entid)+"?qr="+qr;
				
		String jsessionid = session.getId();
		
		if (StringUtils.isEmpty(openid) || openid.equalsIgnoreCase("null")) {
			String appid = wxService.getAppid(entid);
			
			String localUrl =entPrivatepara.get(entid, FGlobal.WeixinRedirect);
			if(StringUtils.isEmpty(localUrl)) {
				localUrl = request.getScheme() +"://" + request.getServerName() +request.getContextPath();
			}
			String redirectUrl = localUrl + "/rest/wx/wxoauth/" + entid + "/" + shopid + "/" + qr + "/" + sheettype;
			//没有指定openid，转到微信api
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri="
					+ redirectUrl + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		
			log.info("weixin callback url:"+url);
		} 
		
		//如果entid和openid一致，则不设定用户userid的信息
		session.setAttribute("sheettype", sheettype);
		session.setAttribute("qr", qr);
		
		Token token = Token.getToken();
		if (token==null) {
			token = new Token();
			session.setAttribute("token", token);
		}
		
		token.setEntid(entid);
		token.setLoginid(openid);
		token.setChannel("wx");
		
		if(shopid!=null && !shopid.equalsIgnoreCase("null")){
			token.setShopid(shopid);
		}else {
			token.setShopid("");
		}
		
		token.setTokenid(jsessionid);
		
		JSONObject res = new JSONObject();
		res.put("url", url);
		res.put("jsessionid", jsessionid);
		
		return new RtnData(res).toString();
	}

	/**
	 * 微信权限申请
	 * @param session
	 * @param code
	 * @param shopid
	 * @param sheettype
	 * @param entid
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/wxoauth/{entid}/{shopid}/{qr}/{sheettype}")
	public String wxoauth(HttpSession session,HttpServletRequest request,
			@RequestParam(value = "code") String code,
			@PathVariable(value = "shopid") String shopid,
			@PathVariable(value = "sheettype") String sheettype,
			@PathVariable(value = "entid") String entid,
			@PathVariable(value = "qr") String qr) throws IOException {
		// 通过code获取用户的openid
		String appid = wxService.getAppid(entid);
		String secret = wxService.getSecret(entid);
		
		String authUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		log.info("weixin callback url:"+authUrl);
		String res = HttpClientCommon.doGet(null, null, authUrl, 10000, 10000, "utf-8");
		log.info("weixin callback msg:"+res);
		String openid = "";
		try {
			JSONObject json = JSONObject.parseObject(res);
			openid = json.getString("openid");
			if(openid==null) {
				log.error("get weixin openid error");
				openid = entid;
			}
		} catch (Exception e) {
			log.error("get weixin openid error"+e,e);
			openid = entid;
		}
		
		//如果entid和openid一致，则不设定用户userid的信息
		session.setAttribute("sheettype", sheettype);
		session.setAttribute("qr", qr);
		Token token = Token.getToken();
		if (token==null) {
			token = new Token();
			session.setAttribute("token", token);
		}
		log.info("openid:"+openid);
		token.setEntid(entid);
		token.setLoginid(openid);
		token.setChannel("wx");
		if(shopid!=null && !shopid.equalsIgnoreCase("null")){
			token.setShopid(shopid);
		}else {
			token.setShopid("");
		}
		String jsessionid = session.getId();
		token.setTokenid(jsessionid);
		
		String index = wxService.getIndex(entid);
		String ret = "redirect:" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		           + "/e-invoice-pro/ui/wx/"+index+"?qr="+qr;
		return ret;
	}
	
	/**
	 * 网页端开票入口
	 * @param session
	 * @param shopid
	 * @param sheettype
	 * @param entid
	 * @param qr
	 * @return
	 */
	@RequestMapping(value = "/appopen")
	@ResponseBody
	public String appopen(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "entid", required = false) String entid,
			@RequestParam(value = "shopid", required = false) String shopid,
			@RequestParam(value = "sheettype", required = false) String sheettype,
			@RequestParam(value = "openid", required = false) String openid,
			@RequestParam(value = "qr", required = false) String qr) {
		if (StringUtils.isEmpty(entid)) {
			return new RtnData(-1, "必须指定企业信息").toString();
		}
		if (StringUtils.isEmpty(sheettype)) {
			return new RtnData(-1, "必须指定业务类型").toString();
		}
		
		String url = "invoiceApp.html";
		session.setAttribute("sheettype", sheettype);
		session.setAttribute("qr", qr);
		Token token = Token.getToken();
		if (token==null) {
			token = new Token();
			session.setAttribute("token", token);
		}
		token.setEntid(entid);
		token.setLoginid(openid);
		token.setChannel("wx");
		if(shopid!=null && !shopid.equalsIgnoreCase("null")){
			token.setShopid(shopid);
		}
		return new RtnData(url).toString();
	}
	
	@RequestMapping(value = "/getEntIdForZM")
	@ResponseBody
	public String getEntIdForZM() {
		Token token = Token.getToken();
		log.info(token.toString());
		String entid = token.getEntid();
		return new RtnData(entid).toString();
	}
}
