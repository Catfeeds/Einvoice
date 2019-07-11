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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.beans.User;
import com.efuture.portal.beans.UserToken;
import com.efuture.portal.service.ZbModuleService;
import com.efuture.portal.utils.BaseController;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Controller
@RequestMapping("/ui/Zbmodule")
public class ZbModuleController extends BaseController {

	@Resource(name = "ZbModuleService")
	ZbModuleService ms;

	// 初始化门店
	@RequestMapping(value = "/ZbregisterShop.action")
	@ResponseBody
	public String registerShop(@RequestParam("data") String data, @RequestParam("token") String token,
			@RequestParam("flag") String flag) {
		JSONObject userjson = new JSONObject();
		try {

			UserToken usertoken = UserToken.getInstance();
			User user = usertoken.getUserByToken_new(token);

			if (user == null) {
				userjson.put("code", "0");
				userjson.put("msg", "token不存在或已失效，请重新登录!");
				return userjson.toString();
			}

			if ("I".equals(flag)) {
				ms.insertShop(data.toString());
			} else if ("D".equals(flag)) {
				ms.deleteShop(data);
			} else if ("U".equals(flag)) {
				ms.updateShop(data);
			}

			userjson.put("code", "1");
			userjson.put("msg", "Success");
		} catch (Exception e) {
			userjson.put("code", "0");
			e.printStackTrace();
			userjson.put("msg", "门店初始化发生异常!");
		}

		return userjson.toString();
	}
	@RequestMapping(value = "/adminmodifyPassword.action", method = RequestMethod.POST)
	@ResponseBody
	public String adminmodifyPassword(@RequestParam("loginid") String loginid,
			@RequestParam("newpwd1") String newpwd) {
			User user = new User();
			user.setLoginid(loginid);
			user.setCompanyid(super.getUser().getCompanyid());
		return ms.adminmodifyPassword(user, newpwd);
	}

}
