package com.efuture.portal.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.beans.RegisterBean;
import com.efuture.portal.beans.User;
import com.efuture.portal.beans.UserToken;
import com.efuture.portal.service.ModuleService;
import com.efuture.portal.service.RegisterService;
import com.efuture.portal.utils.BaseController;

@Controller
@RequestMapping("/ui/module")
public class RegisterController  extends BaseController {
	
	@Resource(name = "RegisterService")
	RegisterService rs;
	
	@Resource(name = "ModuleService")
	ModuleService ms;

	@RequestMapping(value = "/findEnterpriseRegister.action", method = RequestMethod.POST)
	@ResponseBody
	public String findEnterpriseRegister(@RequestParam("entid") String entid,
										 @RequestParam("token") String token) { 
		JSONObject userjson = new JSONObject();
		if(token==null||"".equals(token)){
			userjson.put("code", "0");
			userjson.put("msg", "企业ID不能为空");
			return userjson.toString();
		}
	
		
		UserToken usertoken = UserToken.getInstance();
		User user = usertoken.getUserByToken_new(token);
		
		if (user == null) {
			userjson.put("code", "0");
			userjson.put("msg", "token不存在或已失效，请重新登录!");
			return userjson.toString();
		}
		
		if(entid==null||"".equals(entid)){
			userjson.put("code", "0");
			userjson.put("msg", "企业ID不能为空");
			return userjson.toString();
		}
		
		//userjson.put("data", rs.findEnterpriseRegister(entid,getUser().getLoginid()));
		
		return rs.findEnterpriseRegister(entid,user.getLoginid());
	}
	
	@RequestMapping(value = "/findEnterprise.action", method = RequestMethod.POST)
	@ResponseBody
	public String findEnterprise(@RequestParam("entid") String entid ) { 
		
		JSONObject userjson = new JSONObject();
		User user = getUser();
		if (user == null) {
			userjson.put("code", "0");
			userjson.put("msg", "请重新登录!");
			return userjson.toString();
		}
/*		JSONObject rejson = new JSONObject();
		rejson.put("address","测试");
		rejson.put("entid","A00001006");
		rejson.put("entname","华润");
		rejson.put("phone","123456");
		try{
		System.out.println(ms.insertUserModule(rejson.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}*/
/*		
		JSONObject rejson = new JSONObject();
		rejson.put("entid","A00001001");
		rejson.put("shopid","0103");
		rejson.put("shopname","华润深圳分公司2号门店");
		rejson.put("shoptype","0");
		try{
		System.out.println(ms.insertShop(rejson.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
		
		return rs.findEnterpriseRegister(entid,getUser().getLoginid());
	}
	
	@RequestMapping(value = "/getEnterprise.action", method = RequestMethod.POST)
	@ResponseBody
	public String getEnterprise() { 
		
		JSONObject userjson = new JSONObject();
		User user = getUser();
		if (user == null) {
			userjson.put("code", "0");
			userjson.put("msg", "请重新登录!");
			return userjson.toString();
		}
 
		try{
			RegisterBean bean = rs.findEnterpriseRegister(user.getEntid());
			SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
			Calendar cal = Calendar.getInstance();
			long time1 = 0;
			long time2 = 0;
			if(bean!=null){
				
				if(bean.getEnddate()!=null&&!"".equals(bean.getEnddate())){
					cal.setTime(sdf.parse(bean.getEnddate()));
					time1 = cal.getTimeInMillis();
					cal.setTime(sdf.parse(sdf.format(new Date())));
					time2 = cal.getTimeInMillis();
					long between_days=(time1-time2)/(1000*3600*24); 
					 
				    //Integer.parseInt(String.valueOf(between_days));
					if(between_days<=30&&between_days>=0){
						 
						userjson.put("code", "1");
						userjson.put("msg", "您还有"+between_days+"天到期!请您提前续交费用以免影响您的使用,是否要续交费用？");
					}
				}
				
			}
		 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return userjson.toString();
	}
	
	@RequestMapping(value = "/insertEnterpriseRegister.action", method = RequestMethod.POST)
	@ResponseBody
	public String insertEnterpriseRegister(@RequestParam("register") String register) {

		JSONObject userjson = new JSONObject();
		User user = getUser();
		if (user == null) {
			userjson.put("code", "0");
			userjson.put("msg", "请重新登录!");
			return userjson.toString();
		}

		try{
			if(register!=null&&!"".equals(register)){
			
	        RegisterBean bean = JSON.parseObject(register, RegisterBean.class); 
	       // System.out.println(bean.getEnddate());
			 rs.insertEnterpriseRegister(bean,getUser().getLoginid(),userjson);
			
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return userjson.toString();
	}
	
}
