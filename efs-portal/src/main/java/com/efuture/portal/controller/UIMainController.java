/*
 * Copyright (C), 1996-2014
 * FileName: SampleController.java
 * Author:   王华君
 * Date:     Dec 18, 2014 4:07:19 PM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.beans.Option;
import com.efuture.portal.beans.RegisterBean;
import com.efuture.portal.beans.RtnData;
import com.efuture.portal.beans.User;
import com.efuture.portal.beans.UserToken;
import com.efuture.portal.service.RegisterService;
import com.efuture.portal.service.UIMainService;
import com.efuture.portal.utils.BaseController;
import com.efuture.portal.utils.Constant;
import com.efuture.portal.utils.EGlobal;
import com.efuture.portal.utils.HttpClientCommon;
import com.efuture.portal.utils.SHA1;
import com.efuture.portal.utils.UniqueID;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Controller
@RequestMapping("/ui/main")
public class UIMainController extends BaseController {

	/**
	 * 
	 * 功能描述: 登录验证<br>
	 * 输入地址: http://localhost:8080/efs-ui/rest/main/loginCheck.action <br>
	 * 
	 * @param loginid
	 *            ,password
	 * @return 失败: {"code":"0","msg":"失败原因"} 成功: {"code":"1","msg":"success"}
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 * 
	 */

	@Resource(name = "RegisterService")
	RegisterService register;
	
	
	
	@RequestMapping(value = "/{entid}")
	public String findById(@PathVariable("entid") String entid, HttpSession session, ModelMap model,
			HttpServletResponse response, HttpServletRequest request, RedirectAttributes attr) throws Exception {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String ret = "";
		List<Map<String, Object>> company = null;
		if (entid != null && !"".equals(uims)) {
			company = uims.findCompany(entid);
		}
		ModelAndView mv = null;
		if (company != null && company.size() > 0) {
			session.setAttribute("companyid", company.get(0).get("companyid"));
			attr.addAttribute("companyid", company.get(0).get("companyid"));
			attr.addAttribute("companyname", company.get(0).get("companyname"));
			// response.sendRedirect("efs-portal/ui/login.html");

			ret = "redirect:" + EGlobal.portalurl + "/ui/login.html";

			ret = "redirect:" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ "/efs-portal/ui/login.html";
			// System.out.println(ret);
			// mv = new ModelAndView(new
			// RedirectView("efs-portal/ui/login.html"));//默认为forward模式
			// ret =
			// "redirect:/ui/login.html?companyid="+company.get(0).get("companyid")+"&companyname="+company.get(0).get("companyname");
		} else {
			PrintWriter writer = response.getWriter();
			writer.write(" 没有查询到对应企业信息 ");
			writer.flush();
		}

		return ret;

	}
	

	
	@RequestMapping(value = "/loginCheck.action", method = RequestMethod.POST)
	@ResponseBody
	public String loginCheck(@RequestParam("companyid") String companyid,
			@RequestParam("loginid") String loginid,
			@RequestParam("password") String password,
			@RequestParam("verifycode") String verifycode,
			HttpSession session) {
		JSONObject json = null;
    
//		if ("".equals(verifycode)
//				|| !verifycode.equalsIgnoreCase(super.getVerifyCode())) {
//			json = new JSONObject();
//			json.put("code", "0");
//			json.put("msg", "验证码不正确，请重新输入!");
//			return json.toJSONString();
//		}
		
		//companyid =String.valueOf((Integer)session.getAttribute("companyid"));
		
		json = JSON.parseObject(uims.checkLogin(companyid, loginid, password));

		if ("1".equals(json.getString("code"))) {

			JSONArray shoplist = new JSONArray();
			shoplist = json.getJSONArray("shoplist");

			String uniqueid = String.valueOf(UniqueID.getUniqueID());
			User user = new User();
			user.setCompanyid(companyid);
			user.setLoginid(loginid);
			user.setUsername(json.get("username").toString());
			user.setCompanyname(json.get("companyname").toString());
			user.setEntid(json.get("entid")+"");
			user.setToken(uniqueid);
			user.setArea(json.get("area").toString());
			user.setKpd(json.get("kpd").toString());
			user.setJpzz(json.get("jpzz").toString());
			// 只有一个机构的权限，不需要选择，直接登录....
			if (shoplist.size() == 1) {
				JSONObject shop = JSON.parseObject(shoplist.get(0).toString());
				user.setShopid(shop.get("shopid").toString());
				json.put("shopname", shop.get("shopname").toString());
			}

			// 保存进session
			setUser(user);

			// 保存进单例
			UserToken usertoken = UserToken.getInstance();
//			usertoken.setUser(user);
			usertoken.setUser_new(user);

			json.put("token", uniqueid);
			
			try{
				RegisterBean bean = register.findEnterpriseRegister(json.get("entid")+"");
				SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
				long day = 0;
				if(bean!=null){
					if(bean.getEnddate()!=null&&!"".equals(bean.getEnddate())){
						Date date = sdf.parse(bean.getEnddate());
						
						day = (date.getTime() - sdf.parse(sdf.format(new Date())).getTime()) / (24 * 60 * 60 * 1000);
			
						if(day<0){
							json.put("code", "0");
							json.put("msg", "您登录的账号已过期!");
						}
					}else{
						json.put("code", "0");
						json.put("msg", "对不起,您登录的企业账号还注册异常!");
					}
					
				}else{
					json.put("code", "0");
					json.put("msg", "对不起,您登录的企业账号还没有注册!");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		return json.toJSONString();
	}

	@RequestMapping(value = "/modifyPassword.action", method = RequestMethod.POST)
	@ResponseBody
	public String modifyPassword(@RequestParam("oldpwd") String oldpwd,
			@RequestParam("newpwd1") String newpwd) {
		
		return this.uims.modifyPassword(super.getUser(), oldpwd, newpwd);
	}

	@RequestMapping(value = "/adminmodifyPassword.action", method = RequestMethod.POST)
	@ResponseBody
	public String adminmodifyPassword(@RequestParam("loginid") String loginid,
			@RequestParam("newpwd1") String newpwd) {
			User user = new User();
			user.setLoginid(loginid);
			user.setCompanyid(super.getUser().getCompanyid());
		return this.uims.adminmodifyPassword(user, newpwd);
	}
	
	@Resource(name = "UIMainService")
	UIMainService uims;

	@RequestMapping(value = "/getMySheet.action", method = RequestMethod.POST)
	@ResponseBody
	public String getMySheet(@RequestParam("pageno") String pageno) {
		return uims.getMySheet(getUser().getCompanyid(),
				getUser().getLoginid(), Integer.parseInt(pageno));
	}

	@RequestMapping(value = "/getMonthSaleInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getMonthSaleInfo(
			@RequestParam(value = "startmonth", required = false, defaultValue = "0") int startmonth,
			@RequestParam(value = "endmonth", required = false, defaultValue = "0") int endmonth) {
		return uims.getMonthSaleInfo(getUser().getCompanyid(), getUser()
				.getLoginid(), startmonth, endmonth);
	}

	@RequestMapping(value = "/getDaySaleInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getDaySaleInfo(
			@RequestParam(value = "startday", required = false, defaultValue = "") String startday,
			@RequestParam(value = "endday", required = false, defaultValue = "") String endday) {
		return uims.getDaySaleInfo(getUser().getCompanyid(), getUser()
				.getLoginid(), startday, endday);
	}

	@RequestMapping(value = "/getServerDate.action", method = RequestMethod.POST)
	@ResponseBody
	public String getDaySaleInfo(
			@RequestParam(value = "offsetday", required = false, defaultValue = "0") int offsetday) {
		JSONObject returnjson = new JSONObject();

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		returnjson.put("nowday", sd.format(cal.getTime()));

		if (offsetday != 0) {
			cal.add(Calendar.DATE, offsetday);
			returnjson.put("offsetday", sd.format(cal.getTime()));
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnjson.toString();
	}

	/* 获取账户应有的菜单权限 */
	@RequestMapping(value = "/getMenu.action", method = RequestMethod.POST)
	@ResponseBody
	public String getMenu() {
		return uims.getMenu(getUser().getCompanyid(), getUser().getLoginid());
	}

	/* 获取账户应有的机构列表 */
	@RequestMapping(value = "/getMyShopList.action", method = RequestMethod.POST)
	@ResponseBody
	public String getMyShopList() {
		return uims.getMyShopList(getUser().getCompanyid(), getUser()
				.getLoginid());
	}
	/* 获取账户应有的机构列表 */
	@RequestMapping(value = "/getMyShopListByName.action", method = RequestMethod.POST)
	@ResponseBody
	public String getMyShopListByName(String data) {
		JSONObject jo = JSONObject.parseObject(data);
		return uims.getMyShopList(getUser().getCompanyid(), getUser()
				.getLoginid(),jo.getString("searchShop"));
	}

	/* 账户登陆某机构 */
	@RequestMapping(value = "/loginshop.action", method = RequestMethod.POST)
	@ResponseBody
	public String loginshop(
			@RequestParam(value = "shopid", required = true) String shopid) {

		JSONObject resultjson = new JSONObject();
		
		try {
			User user = getUser();

			user.setShopid(shopid);
			setUser(user);

			// 保存进单例
			UserToken usertoken = UserToken.getInstance();
			usertoken.setUser_new(user);

			resultjson.put("code", "1");
			resultjson.put("msg", "登陆机构成功");

			return resultjson.toString();
			
		} catch (Exception e) {
			e.printStackTrace();

			resultjson.put("code", "0");
			resultjson.put("msg", "记录登陆机构时发生异常...");

			return resultjson.toString();
		}
	}

	/* 账户登陆某机构 */
	@RequestMapping(value = "/getUserInfoByToken.action", method = RequestMethod.POST)
	@ResponseBody
	public String getUserInfoByToken(@RequestParam("token") String token) {
		JSONObject userjson = new JSONObject();

		// User user=getUser();
		UserToken usertoken = UserToken.getInstance();
		User user = usertoken.getUserByToken_new(token);

		if (user == null) {
			userjson.put("code", "0");
			userjson.put("msg", "token不存在或已失效，请重新登录!");
			return userjson.toString();
		}

		if (token.equals(user.getToken())) {
			userjson.put("code", "1");
			userjson.put("msg", "success");
			userjson.put("companyid", user.getCompanyid());
			userjson.put("companyname", user.getCompanyname());
			userjson.put("entid", user.getEntid());
			userjson.put("loginid", user.getLoginid());
			userjson.put("username", user.getUsername());
			userjson.put("shopid", user.getShopid());
			userjson.put("partnerid", user.getPartnerid());
			userjson.put("area", user.getArea());
			userjson.put("kpd", user.getKpd());
			userjson.put("jpzz", user.getJpzz());
			userjson.put("tokenid", token);
			userjson.put("shoplist", user.getShoplist());
		} else {
			userjson.put("tokenid", "0");
			userjson.put("code", "0");
			userjson.put("msg", "token验证失败");
			return userjson.toString();
		}

		return userjson.toString();
	}

	/* 获取账户应有的菜单权限 */
	@RequestMapping(value = "/getShopList.action", method = RequestMethod.POST)
	@ResponseBody
	public String getShopList(@RequestParam("companyid") String companyid) {
		return uims.getShopList(companyid);
	}	
	
	
	/* 获取权限 */
	@RequestMapping(value = "/getUserControl.action", method = RequestMethod.POST)
	@ResponseBody
	public String getUserControl(@RequestParam("companyid") String companyid) 
	{
		return uims.getShopList(companyid);
	}
	
	@RequestMapping(value = "/getWeixTicket.action")
	@ResponseBody
	public String getTicket(@RequestParam(value="callback",required=false) String callback,
											@RequestParam(value="weburl",required=false) String weburl)
	{
		//未处多线程理异步问题
		String secret = Constant.WeixTokenMap.get("access_token");
		String expires = Constant.WeixTokenMap.get("expires");
		if(secret == null || Long.parseLong(expires)<System.currentTimeMillis()){
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+Constant.WeixAppID+"&secret="+Constant.WeixSecret;
			String res = HttpClientCommon.doGet(null, null, url, 2000, 2000, "utf-8");
			JSONObject json = JSONObject.parseObject(res);
			String access_token = json.getString("access_token");
			long expires_in = json.getIntValue("expires_in");
			Constant.WeixTokenMap.put("access_token", access_token);
			expires_in = System.currentTimeMillis() + expires_in*1000;
			Constant.WeixTokenMap.put("expires", String.valueOf(expires_in));
			url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
			res = HttpClientCommon.doGet(null, null, url, 2000, 2000, "utf-8");
			json = JSONObject.parseObject(res);
			String jsapi_ticket = json.getString("ticket");
			Constant.WeixTokenMap.put("jsapi_ticket", jsapi_ticket);
		}
		
		String jsapi_ticket = Constant.WeixTokenMap.get("jsapi_ticket");
		System.out.println(jsapi_ticket);
		String noncestr = "abcd1234";
		int timestamp = (int)(System.currentTimeMillis()/1000);
		String mark = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+weburl;
		String signature = SHA1.sha1(mark);
		
		JSONObject json = new JSONObject();
		json.put("appId", Constant.WeixAppID);
		json.put("signature", signature);
		json.put("jsapi_ticket", jsapi_ticket);
		json.put("url", weburl);
		json.put("noncestr", noncestr);
		json.put("timestamp", timestamp);
		
		String res = json.toJSONString();
		if(callback!=null && callback.length()>0){
			res = callback+"("+res+")";
		}
		return res;
	}	
	
	/* 获取权限 */
	@RequestMapping(value = "/modifyShopId_session.action", method = RequestMethod.POST)
	@ResponseBody
	public String modifyShopId_session(@RequestParam("shopid") String shopid,@RequestParam("token") String token) 
	{
		 
		UserToken usertoken = UserToken.getInstance();
		User user = usertoken.getUserByToken_new(token);
		user.setShopid(shopid);
		return uims.modifyShopId_session(user);
	}
	
	@RequestMapping(value = "/getLookupSelect", method = RequestMethod.POST)
	@ResponseBody
	public String test(@RequestParam(value = "lookupid", required = false) int lookupid){
			
		try {
			List<Option> res = uims.getLookupSelect(lookupid);
			return  new RtnData(res).toString();
		} catch (Exception e) {
			 e.printStackTrace();
			return new RtnData(-1,e.getMessage()).toString();
		}
	
	}
	
	
	/* 创建供应商 
	 * companyid企业编码 
	 * loginid登录帐号
	 * roleid对应的角色id
	 暂且先注释，Portal太多，还是先从工程里直接访问数据库吧。
	@RequestMapping(value = "/createUserControl.action", method = RequestMethod.POST)
	@ResponseBody
	public String createUserControl
	 (@RequestParam("companyid") String companyid,
			 @RequestParam("loginid") String loginid,
			 @RequestParam("loginname") String loginname,
			 @RequestParam("role") String role
			 ) 
	{
		return uims.getShopList(companyid,loginid,loginname,role);
	}	
*/
	
}
