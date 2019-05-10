package com.efuture.portal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.beans.Option;
import com.efuture.portal.beans.User;
import com.efuture.portal.constant.Constant;
import com.efuture.portal.dao.db.one.LookupDao;
import com.efuture.portal.dao.db.one.MainDao;
import com.efuture.portal.dao.db.one.UsermoduleDao;
import com.efuture.portal.service.UIMainService;

import net.minidev.json.JSONValue;

@Service("UIMainService")
public class UIMainServiceImpl implements UIMainService {

	// @Autowired
	@Resource(name = "MainDao")
	MainDao mainDao;
	
	@Resource
	UsermoduleDao usermoduleDao;
	
	@Autowired
	LookupDao dao;
	
	/**
	 * 登陆验证
	 */
	@Override
	public String checkLogin(String companyid, String loginid, String password) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		JSONObject resultjson = new JSONObject();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);

		List<Map<String, Object>> resultList = null;
		try {
			resultList = mainDao.checkLogin(valueObj);

			if (resultList.size() == 0) {
				resultjson.put("code", "0");
				resultjson.put("msg", "用户不存在");
				return resultjson.toString();
			}

			System.out.println("##################################################");
			System.out.println(password);
			System.out.println(resultList.get(0).get("password").toString());
			System.out.println("##################################################");
			
			if (!password.equals(resultList.get(0).get("password").toString())) {
				resultjson.put("code", "0");
				resultjson.put("msg", "用户的账号或密码不正确");
				return resultjson.toString();
			}
			resultjson.put("companyid", companyid);
			resultjson.put("username", resultList.get(0).get("username").toString());
			resultjson.put("companyname", resultList.get(0).get("companyname").toString());
			resultjson.put("entid", resultList.get(0).get("entid").toString());
			resultjson.put("kpd", resultList.get(0).get("kpd")==null?"":resultList.get(0).get("kpd").toString());
			resultjson.put("jpzz", resultList.get(0).get("jpzz")==null?"":resultList.get(0).get("jpzz").toString());
			
			// 获取用户可以登录的机构列表
			resultList = mainDao.getLoginShopList(valueObj);

			JSONArray shoplist = new JSONArray();

			if (resultList.size() > 0) {
				 
				for (int i = 0; i < resultList.size(); i++) {
					JSONObject shop = new JSONObject();
					shop.put("shopid", resultList.get(i).get("shopid")
							.toString());
					shop.put("shopname", resultList.get(i).get("shopname")
							.toString());
					shoplist.add(shop);
				}
			} else {
				
				boolean boo = true;
				List<Map<String, Object>> roleFlag = mainDao.getRoleFlag(valueObj);
				
				if(roleFlag!=null&&roleFlag.size()>0){
					for(Map map:roleFlag){
			
						if(map!=null){
							
							if(map.get("flag")!=null&&!"".equals(map.get("flag"))){
								if("1".equals(map.get("flag"))){
									boo = false;
									break;
								}
							}
						}
					}
				}
				if(boo){
					resultjson.put("code", "0");
					resultjson.put("msg", "此帐号没有可登录门店！");
					return resultjson.toString();
				}
			}

			resultjson.put("shoplist", shoplist);
			
			// 查询用户对应区域(家乐福需求)
			List<Map<String,Object>> arealist = mainDao.queryUserarea(valueObj);
			if (arealist.size()!=0 && arealist.get(0).get("area")!=null){
				resultjson.put("area", arealist.get(0).get("area")+"");
			} else{
				resultjson.put("area", "");
			}

			// 因为需要适配杭州商户系统关于不同角色分配到不同主页的需求，先根据用户角色取主页url
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put(Constant.LOGINID, loginid);
			List<Map<String, Object>> tlist1 = this.mainDao
					.queryHomeUrlWithRole(param2);
			if (tlist1.size() != 0) {
				resultjson.put(Constant.HOME_URL, tlist1.get(0).get("homeurl")
						+ "");
			} else {
				// 如果登录成功，则查询主页页面url
				Map<String, Object> param1 = new HashMap<String, Object>();
				param1.put(Constant.COMPANYID, companyid);
				resultjson.put(Constant.HOME_URL,
						this.mainDao.queryHomeUrl(param1).get(0).get("url")+ "");

			}
			
			resultjson.put("code", "1");
			resultjson.put("msg", "success");

			return resultjson.toString();
		} catch (Exception e) {
			e.printStackTrace();

			resultjson.put("code", "0");
			resultjson.put("msg", "验证密码时发生异常...");
			return resultjson.toString();
		}
	}

	/**
	 * 获取登录门店列表
	 */
	@Override
	public String getMyShopList(String companyid, String loginid,String shopname) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		JSONObject resultjson = new JSONObject();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		valueObj.put("searchShop", shopname);
		List<Map<String, Object>> resultList = null;
		try {
			// 获取用户可以登录的机构列表
			resultList = mainDao.getLoginShopList(valueObj);

			JSONArray shoplist = new JSONArray();

			for (int i = 0; i < resultList.size(); i++) {
				JSONObject shop = new JSONObject();
				shop.put("shopid", resultList.get(i).get("shopid").toString());
				shop.put("shopname", resultList.get(i).get("shopname")
						.toString());
				shoplist.add(shop);
			}

			resultjson.put("data", shoplist);

			resultjson.put("code", "1");
			resultjson.put("msg", "success");

			return resultjson.toString();
		} catch (Exception e) {
			e.printStackTrace();

			resultjson.put("code", "0");
			resultjson.put("msg", "验证密码时发生异常...");
			return resultjson.toString();
		}
	}
	/**
	 * 获取登录门店列表
	 */
	@Override
	public String getMyShopList(String companyid, String loginid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		JSONObject resultjson = new JSONObject();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		
		List<Map<String, Object>> resultList = null;
		try {
			// 获取用户可以登录的机构列表
			resultList = mainDao.getLoginShopList(valueObj);

			JSONArray shoplist = new JSONArray();

			for (int i = 0; i < resultList.size(); i++) {
				JSONObject shop = new JSONObject();
				shop.put("shopid", resultList.get(i).get("shopid").toString());
				shop.put("shopname", resultList.get(i).get("shopname")
						.toString());
				shoplist.add(shop);
			}

			resultjson.put("data", shoplist);

			resultjson.put("code", "1");
			resultjson.put("msg", "success");

			return resultjson.toString();
		} catch (Exception e) {
			e.printStackTrace();

			resultjson.put("code", "0");
			resultjson.put("msg", "验证密码时发生异常...");
			return resultjson.toString();
		}
	}
	/**
	 * 获取待审单据
	 */
	@Override
	public String getMySheet(String companyid, String loginid, int pageno) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		valueObj.put("pageindex", (pageno - 1) * 5);
		valueObj.put("pagesize", pageno * 5);

		List<Map<String, Object>> resultList = null;
		try {
			resultList = mainDao.getMySheet(valueObj);

			JSONObject returnjson = new JSONObject();
			JSONArray jsonarray = new JSONArray();

			for (Map<String, Object> result : resultList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("sheettype", result.get("sheettype").toString());
				jsonObj.put("amount", result.get("amount").toString());
				jsonarray.add(jsonObj);
			}

			returnjson.put("data", jsonarray);

			resultList = mainDao.getMySheet_Total(valueObj);

			returnjson.put("total", resultList.get(0).get("total").toString());

			System.out.println(returnjson.toString());

			return returnjson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取月销售汇总数据
	 */
	@Override
	public String getMonthSaleInfo(String companyid, String loginid,
			int startmonth, int endmonth) {

		SimpleDateFormat sd = new SimpleDateFormat("yyyyMM");

		// 没有指定startmonth和endmonth时认为是倒推1年....
		if (startmonth == 0 && endmonth == 0) {
			Date date = new Date();
			endmonth = Integer.parseInt(sd.format(date));

			// 获取一年前的月份
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, -1);
			startmonth = Integer.parseInt(sd.format(cal.getTime()));
		}

		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		valueObj.put("startmonth", startmonth);
		valueObj.put("endmonth", endmonth);

		List<Map<String, Object>> resultList = null;

		try {
			resultList = mainDao.getMonthSaleInfo(valueObj);

			JSONObject returnjson = new JSONObject();
			JSONArray jsonarray = new JSONArray();

			for (Map<String, Object> result : resultList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("monthid", result.get("monthid").toString());
				jsonObj.put("salevalue", result.get("SaleValue").toString());
				jsonObj.put("profitvalue", result.get("ProfitValue").toString());
				jsonarray.add(jsonObj);
			}

			returnjson.put("data", jsonarray);

			return returnjson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取日销售汇总数据
	 */

	@Override
	public String getDaySaleInfo(String companyid, String loginid,
			String startday, String endday) {

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

		// 没有指定startmonth和endmonth时认为是倒推1年....
		if (startday.equals("") && endday.equals("")) {
			Date date = new Date();

			endday = sd.format(date);

			// 获取一年前的月份
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, -1);
			startday = sd.format(cal.getTime());
		} else if (endday.equals("")) {
			Date date = new Date();
			endday = sd.format(date);
		} else if (startday.equals("")) {
			startday = endday;
		}

		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		valueObj.put("startday", startday);
		valueObj.put("endday", endday);

		List<Map<String, Object>> resultList = null;
		try {
			resultList = mainDao.getDaySaleInfo(valueObj);

			JSONObject returnjson = new JSONObject();
			JSONArray jsonarray = new JSONArray();

			for (Map<String, Object> result : resultList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("saleday", result.get("saleday").toString());
				jsonObj.put("salevalue", result.get("salevalue").toString());
				jsonObj.put("profitvalue", result.get("profitvalue").toString());
				jsonarray.add(jsonObj);
			}

			returnjson.put("data", jsonarray);

			System.out.println(returnjson.toString());
			return returnjson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取日销售汇总数据
	 */

	@Override
	public String getMenu(String companyid, String loginid) {

		Map<String, Object> valueObj = new HashMap<String, Object>();

		valueObj.put("companyid", companyid);
		valueObj.put("loginid", loginid);

		List<Map<String, Object>> resultList = null;
		JSONObject returnjson = new JSONObject();
		try {
			resultList = mainDao.getMenuGroup(valueObj);
			JSONArray menujsonarray = new JSONArray();

			for (Map<String, Object> result : resultList) {
				JSONObject menujson = new JSONObject();
				menujson.put("menuid", result.get("menuid").toString());
				menujson.put("menuname", result.get("menuname").toString());
				menujson.put("note", result.get("note").toString());
				menujsonarray.add(menujson);
			}

			resultList = mainDao.getMenuModule(valueObj);
			JSONArray modulejsonarray = new JSONArray();

			for (Map<String, Object> result : resultList) {
				JSONObject modulejson = new JSONObject();
				modulejson.put("menuid", result.get("menuid").toString());
				modulejson.put("moduleid", result.get("moduleid").toString());
				modulejson.put("modulename", result.get("modulename")
						.toString());
				modulejson.put("note", result.get("note").toString());
				modulejson.put("url", result.get("url").toString());
				modulejsonarray.add(modulejson);
			}

			JSONObject obj = new JSONObject();
			obj.put("menu", menujsonarray);
			obj.put("module", modulejsonarray);
			returnjson.put("data", obj);


			// 因为需要适配杭州商户系统关于不同角色分配到不同主页的需求，先根据用户角色取主页url
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put(Constant.LOGINID, loginid);
			List<Map<String, Object>> tlist1 = this.mainDao
					.queryHomeUrlWithRole(param2);
			if (tlist1.size() != 0) {
				returnjson.put(Constant.HOME_URL, tlist1.get(0).get("homeurl")
						+ "");
			} else {
				// 如果登录成功，则查询主页页面url
				Map<String, Object> param1 = new HashMap<String, Object>();
				param1.put(Constant.COMPANYID, companyid);
				returnjson.put(Constant.HOME_URL,
						this.mainDao.queryHomeUrl(param1).get(0).get("url")
								+ "");

			}

			return returnjson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取门店列表
	 */
	@Override
	public String getShopList(String companyid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		JSONObject resultjson = new JSONObject();

		List<Map<String, Object>> resultList = null;
		try {

			valueObj.put("companyid", companyid);

			resultList = mainDao.getShopList(valueObj);

			resultjson.put("code", "1");
			resultjson.put("msg", "success");
			resultjson.put("data", JSONObject.toJSON(resultList));

			return resultjson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			resultjson.put("code", "0");
			resultjson.put("msg", "获取机构列表时发生异常...");
			return resultjson.toString();
		}
	}

	@Override
	public String modifyPassword(User user, String oldpwd, String newpwd) {
		Map<String, Object> retjson = new HashMap<String, Object>();
		retjson.put("code", 1);
		retjson.put("msg", "success");

		// 密码合法性验证...
		if (user.getCompanyname().equals("家乐福")) {
//			ValidatePassword vp = new ValidatePassword();
//
//			if (vp.checkPassword(newpwd) != 1) {
//				retjson.put("code", 0);
//				retjson.put("msg", "密码必须包含大小写字母,必须包含数字,必须包含特殊字符!");
//				return JSONValue.toJSONString(retjson);
//			}
		}

		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("companyid", user.getCompanyid());
			param.put("loginid", user.getLoginid());
			param.put("oldpwd", oldpwd);
			param.put("newpwd", newpwd);
			if (mainDao.existsUser(param).size() == 0) {
				retjson.put("code", "0");
				retjson.put("msg", "原始密码错误");
				return JSONValue.toJSONString(retjson);
			}
			this.mainDao.modifyPassword(param);
			return JSONValue.toJSONString(retjson);
		} catch (Exception e) {
			e.printStackTrace();
			retjson.put("code", "0");
			retjson.put("msg", e.toString());
			return JSONValue.toJSONString(retjson);
		}

	}
	
	@Override
	public String adminmodifyPassword(User user, String newpwd) {
		Map<String, Object> retjson = new HashMap<String, Object>();
		retjson.put("code", 1);
		retjson.put("msg", "success");

		// 密码合法性验证...


		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("companyid", user.getCompanyid());
			param.put("loginid", user.getLoginid());
			param.put("newpwd", newpwd);
			this.mainDao.adminmodifyPassword(param);
			return JSONValue.toJSONString(retjson);
		} catch (Exception e) {
			e.printStackTrace();
			retjson.put("code", "0");
			retjson.put("msg", e.toString());
			return JSONValue.toJSONString(retjson);
		}

	}
	
//////////////////////////////////////////////华丽的分割线//////////////////////////////////////////
	
	public String modifyShopId_session(User user){
		Map<String, Object> retjson = new HashMap<String, Object>();
		retjson.put("code", 0);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("shopid", user.getShopid());
		param.put("token", user.getToken());
		mainDao.updateShopId_Usertoken(param);
		
		retjson.put("code", 1);
		retjson.put("msg", "success");
		return JSONValue.toJSONString(retjson);
	}
	
	public List<Map<String, Object>> findCompany(String entid){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, Object>> company = null; 
		if(entid!=null&&!"".equals(entid)){
			dataMap.put("entid", entid);
			company =  usermoduleDao.findCompany(dataMap);
			
		}
		return company;
	}
	
	public List<Option> getLookupSelect(int lookupid) {
		return dao.getLookupSelect(lookupid);
	}
}
