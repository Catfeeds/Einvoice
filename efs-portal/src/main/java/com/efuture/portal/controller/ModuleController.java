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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.minidev.json.JSONValue;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.beans.User;
import com.efuture.portal.beans.UserToken;
import com.efuture.portal.service.ModuleService;
import com.efuture.portal.utils.BaseController;
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
@RequestMapping("/ui/module")
public class ModuleController extends BaseController {

	/**
	 * 
	 * 功能描述: 登录验证<br>
	 * 输入地址: http://localhost:8080/efs-ui/rest/module/loginCheck.action <br>
	 * 
	 * @param operator
	 *            ,password
	 * @return 失败: {"code":"0","msg":"失败原因"} 成功: {"code":"1","msg":"success"}
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 * 
	 */

	@Resource(name = "ModuleService")
	ModuleService ms;

	// /////////////////////////////////////100005
	// 门店管理//////////////////////////////////////////////////////////
	@RequestMapping(value = "/getShopInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getShopInfo(@RequestParam("data") String data) {
		List<Map<String, Object>> resultlist = null;
//		resultlist = ms.checkLoginModule("700002", getUser().getCompanyid(),
//				getUser().getLoginid());
//		// 验证是否有模块权限.....
//		if (resultlist.size() == 0) {
//			JSONObject rejson = new JSONObject();
//			rejson.put("code", "-1");
//			rejson.put("msg", "对不起,您没有此数据权限"); // 这里是执行异常输出
//			return rejson.toString();
//		}

		return ms.getShopInfo(getUser().getCompanyid(), getUser().getLoginid(),data);
	}

	// /////////////////////////////////////700001
	// 角色管理//////////////////////////////////////////////////////////
	@RequestMapping(value = "/getRoleInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getRoleInfo() {
		List<Map<String, Object>> resultlist = null;
//		resultlist = ms.checkLoginModule("700001", getUser().getCompanyid(),
//				getUser().getLoginid());
//
//		// 验证是否有模块权限.....
//		if (resultlist.size() == 0) {
//			JSONObject rejson = new JSONObject();
//			rejson.put("code", "-1");
//			rejson.put("msg", "对不起,您没有此数据权限"); // 这里是执行异常输出
//			return rejson.toString();
//		}

		return ms.getRoleInfo(getUser().getCompanyid(), getUser().getLoginid());
	}

	@RequestMapping(value = "/getRoleModule.action", method = RequestMethod.POST)
	@ResponseBody
	public String getRoleModule(@RequestParam("roleid") String roleid) {
		return ms.getRoleModule(getUser().getCompanyid(), getUser()
				.getLoginid(), roleid);
	}

	@RequestMapping(value = "/queryCompany.action", method = RequestMethod.POST)
	@ResponseBody
	public String queryCompany(@RequestParam("data") String data) {
		return ms.queryCompany(data);
	}

	@RequestMapping(value = "/loginPartner.action", method = RequestMethod.POST)
	@ResponseBody
	public String loginPartner(@RequestParam("data") String data) {

		JSONObject json = null;
		Map<String, Object> dataMap = null;
		json = JSON.parseObject(ms.loginPartner(data));

		try {
			dataMap = (Map<String, Object>) JSONValue.parseWithException(data);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("code", "0");
			ret.put("msg", e.toString());
			return ret.toString();
		}

		if ("1".equals(json.getString("code"))) {
			String uniqueid = String.valueOf(UniqueID.getUniqueID());
			User user = new User();
			user.setCompanyid(dataMap.get("companyid").toString());
			user.setLoginid(dataMap.get("account").toString());
			user.setUsername(json.get("name").toString());
			user.setPartnerid(json.get("partnerid").toString());
			user.setCompanyname(json.get("companyname").toString());
			user.setToken(uniqueid);

			json.put("token", uniqueid);
			json.put("loginname", json.get("name").toString());

			// 保存进session
			setUser(user);

			// 保存进单例
			UserToken usertoken = UserToken.getInstance();
			usertoken.setUser(user);
		}

		return json.toJSONString();
	}

	@RequestMapping(value = "/getModuleInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getModuleInfo() {
		return ms.getModuleInfo(getUser().getCompanyid(), getUser()
				.getLoginid());
	}

	@RequestMapping(value = "/setRoleModule.action", method = RequestMethod.POST)
	@ResponseBody
	public String setRoleModule(@RequestParam("roleid") String roleid,
			                    @RequestParam("data") String data) {

		return ms.setRoleModule(getUser().getCompanyid(), getUser()
				.getLoginid(), roleid, data);
	}
	
	@RequestMapping(value = "/setRoleControl.action", method = RequestMethod.POST)
	@ResponseBody
	public String setRoleControl(@RequestParam("roleid") String roleid,
			                    @RequestParam("data") String data) {

		System.out.print(roleid);
		System.out.print(data);
		return ms.setRoleControl(getUser().getCompanyid(), getUser()
				.getLoginid(), roleid, data);
	}

	// ///////////////////////////////////// 700002
	// 企业账号管理///////////////////////////////////////////////////////
	@RequestMapping(value = "/getCompanyLoginInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getCompanyLoginInfo() {
		return ms.getCompanyLoginInfo(getUser().getCompanyid(), getUser()
				.getLoginid());
	}

	@RequestMapping(value = "/checkLoginModule.action", method = RequestMethod.POST)
	@ResponseBody
	public String checkLoginModule(@RequestParam("moduleid") String moduleid,@RequestParam("token") String token) {

		JSONObject rejson = new JSONObject();
		List<Map<String, Object>> resultlist = null;
		resultlist = ms.checkLoginModule(moduleid, getUser().getCompanyid(),
				getUser().getLoginid());

		// 验证是否有模块权限.....
		if (resultlist.size() == 0) {
			rejson.put("code", "-1");
			rejson.put("msg", "对不起,您没有此数据权限"); // 这里是执行异常输出
		} else {
			UserToken usertoken = UserToken.getInstance();
			User user = usertoken.getUserByToken_new(token);
			
			if(user==null) {
				rejson.put("code", "-1");
				rejson.put("msg", "登录超时，请重新登录");
				return rejson.toString();
			}

			rejson.put("code", "1");
			rejson.put("moduleid", resultlist.get(0).get("moduleid").toString());
			rejson.put("modulename", resultlist.get(0).get("modulename")
					.toString());
			rejson.put("url", resultlist.get(0).get("url").toString()+"?changeshopid="+user.getShopid());
			rejson.put("msg", "success");
		}

		return rejson.toString();
	}

	// ///////////////////////////////////// 700003
	// 帐号管理//////////////////////////////////////////////////////////
	@RequestMapping(value = "/getLoginInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getLoginInfo(@RequestParam("username") String username,@RequestParam("userid") String userid) {
		// 验证是否有模块权限.....
		List<Map<String, Object>> resultlist = null;
//		resultlist = ms.checkLoginModule("700003", getUser().getCompanyid(),
//				getUser().getLoginid());
//		if (resultlist.size() == 0) {
//			JSONObject rejson = new JSONObject();
//			rejson.put("code", "-1");
//			rejson.put("msg", "对不起,您没有此数据权限"); // 这里是执行异常输出
//			return rejson.toString();
//		}

		return ms
				.getLoginInfo(getUser().getCompanyid(), getUser().getLoginid(),username,userid);
	}
	// ///////////////////////////////////// 700003
		// 帐号管理//////////////////////////////////////////////////////////
		@RequestMapping(value = "/getShopInfoByReqest.action", method = RequestMethod.POST)
		@ResponseBody
		public String getShopInfoByReqest(@RequestParam("reqshop") String reqshop) {
			// 验证是否有模块权限.....
			List<Map<String, Object>> resultlist = null;
			resultlist = ms.checkLoginModule("700003", getUser().getCompanyid(),
					getUser().getLoginid());
			if (resultlist.size() == 0) {
				JSONObject rejson = new JSONObject();
				rejson.put("code", "-1");
				rejson.put("msg", "对不起,您没有此数据权限"); // 这里是执行异常输出
				return rejson.toString();
			}
			return ms
					.getShopInfoByReqest(getUser().getCompanyid(), getUser().getLoginid(),reqshop);
		}

	@RequestMapping(value = "/getLoginPower.action", method = RequestMethod.POST)
	@ResponseBody
	public String getLoginPower(@RequestParam("loginid") String loginid) {
		return ms.getLoginPower(getUser().getCompanyid(), getUser()
				.getLoginid(), loginid);
	}

	@RequestMapping(value = "/setLoginPower.action", method = RequestMethod.POST)
	@ResponseBody
	public String setLoginPower(@RequestParam("loginid") String loginid,
			@RequestParam("data") String data) {
		return ms.setLoginPower(getUser().getCompanyid(), getUser()
				.getLoginid(), loginid, data);
	}

	// 获取机构列表...
	@RequestMapping(value = "/getShopTreeList.action", method = RequestMethod.POST)
	@ResponseBody
	public String getShopTreeList() {
		return ms.getShopTreeList(getUser().getCompanyid(), getUser()
				.getLoginid());
	}

	// 获取注册企业信息...
	@RequestMapping(value = "/getCompanyInfo.action", method = RequestMethod.POST)
	@ResponseBody
	public String getCompanyInfo() {
		return ms.getCompanyInfo(getUser().getCompanyid(), getUser()
				.getLoginid());
	}

	// //////////////////////////////////////////////////888888//////////////////////////////////////////////////////////////////
	// 注册入驻企业
	@RequestMapping(value = "/registerCompany.action", method = RequestMethod.POST)
	@ResponseBody
	public String registerCompany(@RequestParam("data") String data) {
		JSONObject rejson = new JSONObject();
		try {
			String result = ms.addCompany(getUser().getCompanyid(), getUser()
					.getLoginid(), data);

			if (result.equals("")) {
				rejson.put("code", "1");
				rejson.put("msg", "success"); // 这里是调用处理成功
			} else {
				rejson.put("code", "0");
				rejson.put("msg", result); // 这里用作显式错误提示
			}
		} catch (Exception e) {
			rejson.put("code", "0");
			rejson.put("msg", "注册企业发生异常!"); // 这里是执行异常输出
		}

		return rejson.toString();
	}

	// /////////////////////////////////////////////- 777777
	// -//////////////////////////////////////
	// 注册企业帐号
	@RequestMapping(value = "/registerCompanyLogin.action", method = RequestMethod.POST)
	@ResponseBody
	public String registerCompanyLogin(@RequestParam("data") String data) {
		JSONObject rejson = new JSONObject();
		try {
			String result = ms.addCompanyLogin(getUser().getCompanyid(),
					getUser().getLoginid(), data);

			if (result.equals("")) {
				rejson.put("code", "1");
				rejson.put("msg", "success"); // 这里是调用处理成功
			} else {
				rejson.put("code", "0");
				rejson.put("msg", result); // 这里用作显式错误提示
			}
		} catch (Exception e) {
			rejson.put("code", "0");
			rejson.put("msg", "注册企业帐号异常!"); // 这里是执行异常输出
		}

		return rejson.toString();
	}

	// /////////////////////////////////////999999获取数据表的列信息//////////////////////////////////////////////////////////
	@RequestMapping(value = "/getTableConfig.action", method = RequestMethod.POST)
	@ResponseBody
	public String getTableConfig() {
		return ms.getTableConfig();
	}

	@RequestMapping(value = "/getTableColumnConfig.action", method = RequestMethod.POST)
	@ResponseBody
	public String getTableColumnConfig(
			@RequestParam("tablename") String tablename) {
		return ms.getTableColumnConfig(tablename);
	}

	@RequestMapping(value = "/insertTableColumnConfig.action", method = RequestMethod.POST)
	@ResponseBody
	public String insertTableColumnConfig(
			@RequestParam("tablename") String tablename) {
		return ms.insertTableColumnConfig(tablename);
	}

	/**
	 * 
	 * 功能描述: 标准插入数据功能<br>
	 * 输入地址: http://localhost:8080/efs-ui/rest/module/insertStandardTable.action <br>
	 * 
	 * @param tablename
	 *            表名 data 要插入的表的内容（JSON数组,每一个元素下必须包含所有的KEY和不能为空并且没有默认值的列）
	 * @return 失败: {"code":"0","msg":"失败原因"} 成功: {"code":"1","msg":"success"}
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 * 
	 */
	@RequestMapping(value = "/insertStandardTable.action", method = RequestMethod.POST)
	@ResponseBody
	public String insertStandardTable(
			@RequestParam("tablename") String tablename,
			@RequestParam("data") String data) {

		return ms.insertStandardTable(getUser().getCompanyid(), getUser()
				.getLoginid(), tablename, data);
	}

	/**
	 * 
	 * 功能描述: 标准删除数据功能<br>
	 * 输入地址: http://localhost:8080/efs-ui/rest/module/deleteStandardTable.action <br>
	 * 
	 * @param tablename
	 *            表名 data 要插入的表的内容（JSON数组,每一个元素下必须包含所有的KEY和不能为空并且没有默认值的列）
	 * @return 失败: {"code":"0","msg":"失败原因"} 成功: {"code":"1","msg":"success"}
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 * 
	 */
	@RequestMapping(value = "/deleteStandardTable.action", method = RequestMethod.POST)
	@ResponseBody
	public String deleteStandardTable(
			@RequestParam("tablename") String tablename,
			@RequestParam("data") String data) {

		return ms.deleteStandardTable(getUser().getCompanyid(), getUser()
				.getLoginid(), tablename, data);
	}

	/**
	 * 
	 * 功能描述: 标准更新数据功能<br>
	 * 输入地址: http://localhost:8080/efs-ui/rest/module/updateStandardTable.action <br>
	 * 
	 * @param tablename
	 *            表名 data
	 *            要更新的内容(JSON数组,每一个元素下必须包含所有的KEY和不能为空并且没有默认值的列),必须有old_key作为更新条件
	 * @return 失败: {"code":"0","msg":"失败原因"} 成功: {"code":"1","msg":"success"}
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 * 
	 */
	@RequestMapping(value = "/updateStandardTable.action", method = RequestMethod.POST)
	@ResponseBody
	public String updateStandardTable(
			@RequestParam("tablename") String tablename,
			@RequestParam("data") String data) {
			
		return ms.updateStandardTable(getUser().getCompanyid(), getUser()
				.getLoginid(), tablename, data);
	}

	@RequestMapping(value = "/heartbeat_detection.action", method = RequestMethod.POST)
	@ResponseBody
	public String heartbeat_detection() {
		return "ksk";
	}

	// 注册入驻企业
	@RequestMapping(value = "/registerCompanySelf.action", method = RequestMethod.POST)
	@ResponseBody
	public String registerCompanySelf(@RequestParam("data") String data) {
		JSONObject rejson = new JSONObject();
		try {
			String result = ms.TX4addCompanySelf(data);

			if (result.equals("")) {
				rejson.put("code", "1");
				rejson.put("msg", "success"); // 这里是调用处理成功
			} else {
				rejson.put("code", "0");
				rejson.put("msg", result); // 这里用作显式错误提示
			}
		} catch (Exception e) {
			rejson.put("code", "0");
			e.printStackTrace();
			rejson.put("msg", "注册企业发生异常!"); // 这里是执行异常输出
		}

		return rejson.toString();
	}
	

	// 初始化企业管理员
	@RequestMapping(value = "/registerUserModule.action")
	@ResponseBody
	public String registerUserModule(@RequestParam("data") String data,@RequestParam("token") String token) {
		JSONObject userjson = new JSONObject();
		try {
			 
			UserToken usertoken = UserToken.getInstance();
			User user = usertoken.getUserByToken_new(token);

			if (user == null) {
				userjson.put("code", "0");
				userjson.put("msg", "token不存在或已失效，请重新登录!");
				return userjson.toString();
			}
			
			ms.insertUserModule(data.toString());
			userjson.put("code", "1");
			userjson.put("msg", "Success");
		} catch (Exception e) {
			userjson.put("code", "0");
			e.printStackTrace();
			userjson.put("msg", "企业初始化发生异常!");
		}
		
		return userjson.toString();
	}	
	
	// 初始化门店
		@RequestMapping(value = "/registerShop.action")
		@ResponseBody
		public String registerShop(@RequestParam("data") String data,@RequestParam("token") String token,@RequestParam("flag") String flag) {
			JSONObject userjson = new JSONObject();
			try {
				 
				UserToken usertoken = UserToken.getInstance();
				User user = usertoken.getUserByToken_new(token);

				if (user == null) {
					userjson.put("code", "0");
					userjson.put("msg", "token不存在或已失效，请重新登录!");
					return userjson.toString();
				}
				if("I".equals(flag)){
					ms.insertShop(data.toString());
				}
				else
				if("D".equals(flag))
				{		
				
				ms.deleteShop(data);
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
	
}


