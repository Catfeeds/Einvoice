package com.efuture.portal.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.dao.db.one.M999999Dao;
import com.efuture.portal.dao.db.one.ManageDao;
import com.efuture.portal.dao.db.one.UserDao;
import com.efuture.portal.dao.db.one.UsermoduleDao;
import com.efuture.portal.service.ModuleService;
import com.efuture.portal.utils.EGlobal;
import com.efuture.portal.utils.FormatUtil;
import com.efuture.portal.utils.SHA1;

import net.minidev.json.JSONValue;

@Service("ModuleService")
public class ModuleServiceImpl implements ModuleService {

	@Resource(name = "ManageDao")
	ManageDao manageDao;

	@Resource
	UserDao userDao;

	@Resource
	UsermoduleDao usermoduleDao;
	
	// 格式化日期
	public void formatDate(List<Map<String, Object>> resultlist) {
		// \\{(\\\"\\w+\\\":[+-]*\\d+,)+(\\\"\\w+\\\":\\d+)\\}
		for (int i = 0; i < resultlist.size(); i++) {
			Map<String, Object> map = resultlist.get(i);
			for (Entry<String, Object> en : map.entrySet()) {
				if (en.getValue() != null
						&& en.getValue() instanceof java.sql.Timestamp) {
					map.put(en.getKey(), FormatUtil.formatDate(new Date(
							((java.sql.Timestamp) en.getValue()).getTime()),
							FormatUtil.COMMON_FORMAT));
				} else {
					map.put(en.getKey(),
							StringUtils.isEmpty(en.getValue()) ? "" : en
									.getValue());
				}
			}
			resultlist.set(i, map);
		}
	}

	/**
	 * 门店管理-获取门店数据...
	 */
	@Override
	public String getShopInfo(String companyid, String loginid,String data) {

		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();

		try {
			Map<String, Object> valueObj = new HashMap<String, Object>();
			valueObj.put("companyid", companyid);
			valueObj.put("shopid", data);
			resultlist = manageDao.getShopInfo(valueObj);
			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total", resultlist.size());
			jsonObj.put("data", JSONObject.toJSON(resultlist));

			rejson.put("code", "1");
			rejson.put("msg", "sucess");
			rejson.put("data", jsonObj.toString());
			// return jsonObj.toString();
			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();

			rejson.put("code", "0");
			rejson.put("msg", "表信息不存在!");
			return rejson.toString();
		}
	}

	/**
	 * 权限管理-获取企业帐号数据...
	 */
	@Override
	public String getCompanyLoginInfo(String companyid, String loginid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);

		List<Map<String, Object>> resultlist = null;

		try {
			resultlist = manageDao.getCompanyLoginInfo(valueObj);

			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total", resultlist.size());

			jsonObj.put("data", JSONObject.toJSON(resultlist));

			return jsonObj.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 权限管理-获取帐号数据...
	 */
	@Override
	public String getLoginInfo(String companyid, String loginid,String username,String userid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		if(!"null".equals(userid)){
			valueObj.put("userid", userid);
		}
		if(!"null".equals(username)){
			valueObj.put("username", username);
		}
		List<Map<String, Object>> resultlist = null;
		List<Map<String, Object>> rolelist = null;
		List<Map<String, Object>> arealist = null;
		List<Map<String, Object>> shoplist = null;

		try {
			resultlist = manageDao.getLoginInfo(valueObj);
			rolelist = manageDao.getRoleInfo(valueObj);
			arealist = manageDao.getAreaInfo(valueObj);
			shoplist = manageDao.getShopInfo(valueObj);

			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("code", "1");

			jsonObj.put("total", resultlist.size());

			jsonObj.put("data", JSONObject.toJSON(resultlist));
			jsonObj.put("rolelist", JSONObject.toJSON(rolelist));
			jsonObj.put("arealist", JSONObject.toJSON(arealist));
			jsonObj.put("shoplist", JSONObject.toJSON(shoplist));

			return jsonObj.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 权限管理-获取帐号权限数据...
	 */
	@Override
	public String getLoginPower(String companyid, String setloginid,
			String loginid) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		JSONObject jsonObj = new JSONObject();
		List<Map<String, Object>> rolelist = null;
		List<Map<String, Object>> shoplist = null;

		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		valueObj.put("setloginid", setloginid);

		try {
			rolelist = manageDao.getLoginRolePower(valueObj);
			shoplist = manageDao.getLoginShopPower(valueObj);

			jsonObj.put("rolelist", JSONObject.toJSON(rolelist));
			jsonObj.put("shoplist", JSONObject.toJSON(shoplist));

			return jsonObj.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 平台管理-机构入驻 1. 增加company数据... 2. 默认生成企业管理员角色...
	 */
	public String addCompany(String companyid, String loginid, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();

		JSONObject companydata = JSONArray.parseArray(data).getJSONObject(0);

		valueObj.put("companytype", companydata.get("companytype").toString());
		valueObj.put("companyname", companydata.get("companyname").toString());
		valueObj.put("shortname", companydata.get("shortname").toString());
		valueObj.put("address", companydata.get("address").toString());
		valueObj.put("companytel", companydata.get("companytel").toString());
		valueObj.put("contacttel", companydata.get("contacttel").toString());
		valueObj.put("email", companydata.get("email").toString());
		valueObj.put("fax", companydata.get("fax").toString());
		valueObj.put("taxno", companydata.get("taxno").toString());
		valueObj.put("industries", companydata.get("industries").toString());
		valueObj.put("legalperson", companydata.get("legalperson").toString());
		valueObj.put("depositbank", companydata.get("depositbank").toString());
		valueObj.put("bankno", companydata.get("bankno").toString());

		// 生成企业信息
		manageDao.intoCompany(valueObj);

		List<Map<String, Object>> resultlist = null;
		resultlist = manageDao.getNewCompany(valueObj);
		// 获取企业编码...
		String newcompanyid = resultlist.get(0).get("companyid").toString();
		valueObj.put("companyid", newcompanyid);

		// 生成企业权限管理菜单
		// manageDao.newMenu(valueObj);

		// 获取企业权限管理菜单
		// resultlist=manageDao.getNewMenu(valueObj);
		// String newmenuid=resultlist.get(0).get("menuid").toString();
		// valueObj.put("menuid", newmenuid);

		// 生成企业权限管理模块
		// manageDao.newModule(valueObj);

		// 生成企业管理员角色
		manageDao.intoCompanyRole(valueObj);

		// 生成企业总部机构
		manageDao.newShop(valueObj);

		// 生成角色模块权限
		manageDao.newRoleModule(valueObj);

		// 平台/零售/经销
		String companytype = companydata.get("companytype").toString();

		// 如果是经销商,初始化CompanyRebate (跨库写入)
		if (manageDao.checkEShopDB(valueObj).size() > 0) {
			if (companytype.equals("经销")) {
				manageDao.newCompanyRebate(valueObj);
			}

			// 如果是零售商,初始化CompanyPrepay和CompanyPointExConfig (跨库写入)
			if (companytype.equals("零售")) {
				manageDao.newCompanyPrepay(valueObj);
				manageDao.newCompanyPointExConfig(valueObj);
			}
		}

		return "";
	}

	/**
	 * 平台管理-注册企业用户 1. 增加login数据...
	 * 
	 */
	public String addCompanyLogin(String companyid, String loginid, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();

		JSONObject logindata = JSONArray.parseArray(data).getJSONObject(0);

		valueObj.put("companyid", logindata.get("companyid").toString());
		valueObj.put("loginid", logindata.get("loginid").toString());
		valueObj.put("password", logindata.get("password").toString());
		valueObj.put("username", logindata.get("username").toString());
		valueObj.put("idcard", logindata.get("idcard").toString());
		valueObj.put("contacttel", logindata.get("contacttel").toString());

		// 生成帐号信息
		manageDao.newLogin(valueObj);

		// 绑定企业管理员角色
		manageDao.newLoginRole(valueObj);

		// 绑定企业机构
		manageDao.newLoginShop(valueObj);

		return "";
	}

	/**
	 * 权限管理-获取企业数据...
	 */
	@Override
	public String getCompanyInfo(String companyid, String loginid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();

		List<Map<String, Object>> resultlist = null;

		try {
			resultlist = manageDao.getCompanyInfo(valueObj);

			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total", resultlist.size());
			jsonObj.put("data", JSONObject.toJSON(resultlist));

			jsonObj.put("code", "1");

			return jsonObj.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 角色管理-获取帐号数据...
	 */
	@Override
	public String getRoleInfo(String companyid, String loginid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);

		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();
		try {
			resultlist = manageDao.getRoleInfo(valueObj);
			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total", resultlist.size());
			jsonObj.put("data", JSONObject.toJSON(resultlist));

			rejson.put("code", "1");
			rejson.put("msg", "sucess");
			rejson.put("data", jsonObj.toString());
			// return jsonObj.toString();
			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();

			rejson.put("code", "0");
			rejson.put("msg", "表信息不存在!");
			return rejson.toString();
		}
	}

	/**
	 * 角色管理-获取角色已分配的模块...
	 */
	@Override
	public String getRoleModule(String companyid, String loginid, String roleid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		valueObj.put("roleid", roleid);

		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();
		try {
			resultlist = manageDao.getRoleModule(valueObj);
			
			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);
			rejson.put("code", "1");
			rejson.put("msg", "sucess");
			rejson.put("data", JSONObject.toJSON(resultlist));
			
			resultlist = manageDao.getUserControl(valueObj);
			rejson.put("usercontroldata", JSONObject.toJSON(resultlist));
			
			return rejson.toString();
		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "获取角色模块数据时发生异常!");
			return rejson.toString();
		}
	}

	/**
	 * 权限管理-获取角色模块数据...
	 */
	@Override
	public String getModuleInfo(String companyid, String loginid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);

		List<Map<String, Object>> resultList = null;
		JSONObject returnjson = new JSONObject();
		try {

			resultList = manageDao.getRoleMenuGroup(valueObj);
			JSONArray menujsonarray = new JSONArray();

			for (Map<String, Object> result : resultList) {
				JSONObject menujson = new JSONObject();
				menujson.put("menuid", result.get("menuid").toString());
				menujson.put("menuname", result.get("menuname").toString());
				menujson.put("note", result.get("note").toString());
				menujsonarray.add(menujson);
			}

			resultList = manageDao.getRoleMenuModule(valueObj);
			JSONArray modulejsonarray = new JSONArray();

			for (Map<String, Object> result : resultList) {
				JSONObject modulejson = new JSONObject();
				modulejson.put("menuid", result.get("menuid").toString());
				modulejson.put("moduleid", result.get("moduleid").toString());
				modulejson.put("modulename", result.get("modulename")
						.toString());
				modulejson.put("note", result.get("note").toString());
				modulejsonarray.add(modulejson);
			}
			
			resultList = manageDao.getControl(valueObj);		
			JSONObject obj = new JSONObject();
			obj.put("menu", menujsonarray);
			obj.put("module", modulejsonarray);
			obj.put("modulecontrol", resultList);
			returnjson.put("data", obj);
			return returnjson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 设置角色的模块权限
	 */
	@Override
	public String setRoleModule(String companyid, String loginid,
			String roleid, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();

		try {
			valueObj.put("roleid", roleid);

			manageDao.deleteRoleModule(valueObj);

			JSONArray modulearray = JSONArray.parseArray(data);

			for (int i = 0; i < modulearray.size(); i++) {

				JSONObject module = modulearray.getJSONObject(i);
				valueObj.remove("moduleid");
				valueObj.put("moduleid", module.get("moduleid").toString());
				manageDao.intoRoleModule(valueObj);
			}
			
			rejson.put("code", "1");
			rejson.put("msg", "success");

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "处理时发生异常!");
			return rejson.toString();
		}
	}
	
	
	/**
	 * 设置角色的模块权限
	 */
	@Override
	public String setRoleControl(String companyid, String loginid,
			String roleid, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();

		try {
			valueObj.put("roleid", roleid);

			
			//删除决色对于功能权限
			manageDao.deleteUserControl(valueObj);
			
			JSONArray controlarray = JSONArray.parseArray(data);
			for (int j = 0; j < controlarray.size(); j++) {

				JSONObject controlobj = controlarray.getJSONObject(j);
				valueObj.put("controlid", controlobj.get("controlid").toString());
				manageDao.intoRoleControl(valueObj);
			}

			rejson.put("code", "1");
			rejson.put("msg", "success");

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "处理时发生异常!");
			return rejson.toString();
		}
	}


	/**
	 * 设置帐号的[角色-门店-管理类别]权限
	 */
	@Override
	public String setLoginPower(String companyid, String setloginid,
			String loginid, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();

		try {
			valueObj.put("companyid", companyid);
			valueObj.put("loginid", loginid);
			
			valueObj.put("userid", loginid);

			manageDao.deleteLoginRole(valueObj);
			manageDao.deleteLoginShop(valueObj);
			//manageDao.deleteQuyuShop(valueObj);

			JSONObject datajson = JSONObject.parseObject(data);
			// 帐号角色权限...
			JSONArray rolelist = JSONArray.parseArray(datajson
					.getString("rolelist"));
			JSONArray shoplist = JSONArray.parseArray(datajson
					.getString("shoplist"));

			for (int i = 0; i < rolelist.size(); i++) {
				JSONObject role = rolelist.getJSONObject(i);
				valueObj.remove("roleid");
				valueObj.put("roleid", role.get("roleid").toString());
				manageDao.intoLoginRole(valueObj);
			}

			for (int i = 0; i < shoplist.size(); i++) {
				JSONObject shop = shoplist.getJSONObject(i);
				valueObj.remove("shopid");
				valueObj.put("shopid", shop.get("shopid").toString());
				manageDao.intoLoginShop(valueObj);
//				manageDao.intoQuyuShop(valueObj);
			}

			rejson.put("code", "1");
			rejson.put("msg", "success");

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "处理时发生异常!");
			return rejson.toString();
		}
	}

	/**
	 * 通用-获取机构树状信息...
	 */
	@Override
	public String getShopTreeList(String companyid, String loginid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);

		List<Map<String, Object>> arealist = null;
		List<Map<String, Object>> shoplist = null;
		JSONObject rejson = new JSONObject();
		try {
			arealist = manageDao.getAreaInfo(valueObj);
			shoplist = manageDao.getShopInfo(valueObj);

			rejson.put("code", "1");
			rejson.put("msg", "sucess");
			rejson.put("arealist", JSONObject.toJSON(arealist));
			rejson.put("shoplist", JSONObject.toJSON(shoplist));

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "获取机构数据时发生异常!");
			return rejson.toString();
		}
	}

	@Override
	public List<Map<String, Object>> checkLoginModule(String moduleid,
			String companyid, String loginid) {
		Map<String, Object> valueObj = new HashMap<String, Object>();

		valueObj.put("moduleid", moduleid);
		valueObj.put("companyid", companyid);
		valueObj.put("loginid", loginid);

		List<Map<String, Object>> resultlist = null;

		resultlist = manageDao.checkLoginModule(valueObj);

		return resultlist;

		// if (resultlist.size()==0)
		// {
		// //没有权限
		// return 0;
		// }
		//
		// return 1;

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Resource(name = "M999999Dao")
	M999999Dao m999999dao;

	/**
	 * 获取物理表...
	 */
	@Override
	public String getTableConfig() {
		Map<String, Object> valueObj = new HashMap<String, Object>();

		List<Map<String, Object>> resultlist = null;
		try {
			resultlist = m999999dao.getTableConfig(valueObj);
			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONArray jsonarray = new JSONArray();

			for (int i = 0; i < resultlist.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("name", resultlist.get(i).get("tablename"));
				jsonObj.put("id", resultlist.get(i).get("tablename"));
				jsonObj.put("expanded", true);
				jsonarray.add(jsonObj);
			}

			return jsonarray.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取物理表列信息...
	 */
	@Override
	public String getTableColumnConfig(String tablename) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("tablename", tablename);

		List<Map<String, Object>> resultlist = null;
		try {
			resultlist = m999999dao.getTableColumnConfig(valueObj);
			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total", resultlist.size());

			jsonObj.put("data", JSONObject.toJSON(resultlist));

			return jsonObj.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 自动捉取表的结构
	 */
	@Override
	public String insertTableColumnConfig(String tablename) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("tablename", tablename);

		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();
		try {

			// 先检查一下表是否存在...
			resultlist = m999999dao.getTableData(valueObj);

			if (resultlist.size() == 0) {
				rejson.put("code", "0");
				rejson.put("msg", "表信息不存在!");
				return rejson.toString();
			}

			m999999dao.deleteTableConfig(valueObj);
			m999999dao.insertTableConfig(valueObj);
			m999999dao.deleteTableColumnConfig(valueObj);
			m999999dao.insertTableColumnConfig(valueObj);

			rejson.put("code", "1");
			rejson.put("msg", "success");

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "处理时发生异常!");
			return rejson.toString();
		}
	}

	/**
	 * 标准化插入配置数据表
	 */
	@Override
	public String insertStandardTable(String companyid, String loginid,
			String tablename, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();
		String columns = "";

		try {
			valueObj.put("tablename", tablename);

			resultlist = m999999dao.getTableColumnConfig(valueObj);

			if (resultlist.size() == 0) {
				rejson.put("code", "0");
				rejson.put("msg", "没有表配置信息!");
				return rejson.toString();
			}

			JSONArray dataarray = JSONArray.parseArray(data);
			boolean companylogin = false; // 是否企业账号
			// 增加companyid项
			if (!tablename.equals("company")
					&& !(tablename.equals("login") && companyid.equals("1"))) {
				for (int i = 0; i < dataarray.size(); i++) {
					JSONObject dataJson = dataarray.getJSONObject(i);
					dataJson.remove("companyid");
					dataJson.put("companyid", companyid);
				}
			} else if (tablename.equals("login") && companyid.equals("1")) {
				companylogin = true;
			}

			for (int i = 0; i < resultlist.size(); i++) {
				String columnname = resultlist.get(i).get("columnname")
						.toString();

				// 是否允许为空...
				boolean nullable = true;
				if (resultlist.get(i).get("nullable").toString().equals("0")) {
					nullable = false;
				}

				// 是否主键...
				boolean iskey = true;
				if (resultlist.get(i).get("iskey").toString().equals("0")) {
					iskey = false;
				}

				boolean isauto = true;
				if (resultlist.get(i).get("isauto").toString().equals("0")) {
					isauto = false;
				}

				// 是否有默认值...
				boolean havedefault = true;
				String defaultvalue = "";
				if (resultlist.get(i).get("columndefault") == null) {
					havedefault = false;
				} else {
					defaultvalue = resultlist.get(i).get("columndefault")
							.toString();
				}

				// 循环data
				for (int j = 0; j < dataarray.size(); j++) {
					JSONObject dataJson = dataarray.getJSONObject(j);
					if (iskey && (!isauto)) {
						if ((dataJson.get(columnname) == null)
								|| (dataJson.get(columnname).equals(""))) {
							rejson.put("code", "0");
							rejson.put("msg", "第[" + (j + 1) + "]行数据不存在主键列["
									+ columnname + "]的信息或主键列为空字符!");
							return rejson.toString();
						}
					}

					// 检查不能为空并且没有默认值且不是自动填充数据的列
					if (!nullable && !havedefault && !isauto) {
						if (dataJson.get(columnname) == null) {
							rejson.put("code", "0");
							rejson.put("msg", "第[" + (j + 1) + "]行数据不存在不为空列["
									+ columnname + "]的信息!");
							return rejson.toString();
						}
					}

					// 处理可以为空的默认值的情况...
					if (!dataJson.containsKey(columnname) && havedefault) {
						dataJson.put(columnname, defaultvalue);
					}

					// 非字符类型数据,存在默认值,而value为空字符,则不让通过
					if (!resultlist.get(i).get("datatype").toString()
							.equals("char")
							&& !resultlist.get(i).get("datatype").toString()
									.equals("varchar")
							&& !resultlist.get(i).get("datatype").toString()
									.equals("datetime")
							&& !resultlist.get(i).get("datatype").toString()
									.equals("date") && !isauto) {

						if (dataJson.get(columnname).toString().equals("")) {
							if (havedefault) {
								// 这样就会使用默认值，避免将出现insert语句存在,,,,的情况
								dataJson.remove(columnname);
								dataJson.put(columnname, "default");
							} else {
								rejson.put("code", "0");
								rejson.put("msg", "[" + columnname
										+ "]列为数字类型且无默认值,第[" + (j + 1)
										+ "]行数据此列为空字符,不合规则!");
								return rejson.toString();
							}
						}

					}
				}

				// 拼接列名
				if (i == 0) {
					columns = columnname;
				} else {
					columns = columns + ',' + columnname;
				}
			}

			// 循环拼接列值并插入数据...
			for (int k = 0; k < dataarray.size(); k++) {
				JSONObject dataJson = dataarray.getJSONObject(k);
				String columnvalues = "";
				for (int x = 0; x < resultlist.size(); x++) {
					String columnname = resultlist.get(x).get("columnname")
							.toString();
					String columnvalue = "";
					if (dataJson.containsKey(columnname)) {
						if (resultlist.get(x).get("datatype").toString()
								.equals("char")
								|| resultlist.get(x).get("datatype").toString()
										.equals("varchar")
								|| resultlist.get(x).get("datatype").toString()
										.equals("datetime")
								|| resultlist.get(x).get("datatype").toString()
										.equals("date")) {
							// 有些类型需要双引号....
							columnvalue = "'"
									+ dataJson.get(columnname).toString() + "'";

						} else {

							columnvalue = dataJson.get(columnname).toString();
						}

					} else {
						columnvalue = "null";
					}

					if (x == 0) {
						columnvalues = columnvalue;
					} else {
						columnvalues = columnvalues + ',' + columnvalue;
					}
				}

				Map<String, Object> insertvalueObj = new HashMap<String, Object>();
				insertvalueObj.put("tablename", tablename);
				insertvalueObj.put("columns", columns);
				insertvalueObj.put("columnvalues", columnvalues);
				m999999dao.insertStandardTable(insertvalueObj);

				// 给企业账号绑定角色....
				if (companylogin) {
					Map<String, Object> newloginObj = new HashMap<String, Object>();
					newloginObj.put("companyid", dataJson.get("companyid")
							.toString());
					newloginObj.put("loginid", dataJson.get("loginid")
							.toString());

					// 生成企业管理员角色
					manageDao.newLoginRole(newloginObj);

					manageDao.newLoginShop(newloginObj);
				}
			}

			rejson.put("code", "1");
			rejson.put("msg", "success");

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "处理时发生异常!");
			return rejson.toString();
		}
	}

	/**
	 * 标准化删除配置数据表
	 */
	@Override
	public String deleteStandardTable(String companyid, String loginid,
			String tablename, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();
		String wherecolumns = "";

		try {
			valueObj.put("tablename", tablename);

			resultlist = m999999dao.getTableColumnConfig(valueObj);

			if (resultlist.size() == 0) {
				rejson.put("code", "0");
				rejson.put("msg", "没有表配置信息!");
				return rejson.toString();
			}

			JSONArray dataarray = JSONArray.parseArray(data);
			// 增加companyid项
			if (!tablename.equals("company")
					&& !(tablename.equals("login") && companyid.equals("1"))) {
				for (int i = 0; i < dataarray.size(); i++) {
					JSONObject dataJson = dataarray.getJSONObject(i);
					dataJson.remove("companyid");
					dataJson.put("companyid", companyid);
				}
			}

			for (int i = 0; i < resultlist.size(); i++) {
				String columnname = resultlist.get(i).get("columnname")
						.toString();

				// 是否主键...
				boolean iskey = true;
				if (resultlist.get(i).get("iskey").toString().equals("0")) {
					iskey = false;
				}

				// 循环data
				for (int j = 0; j < dataarray.size(); j++) {
					JSONObject dataJson = dataarray.getJSONObject(j);
					if (iskey) {
						if (dataJson.get(columnname) == null) {
							rejson.put("code", "0");
							rejson.put("msg", "第[" + (j + 1) + "]行数据不存在主键列["
									+ columnname + "]的信息!");
							return rejson.toString();
						}
					}
				}
			}

			for (int k = 0; k < dataarray.size(); k++) {
				JSONObject dataJson = dataarray.getJSONObject(k);
				String columnvalue = "";
				wherecolumns = "";

				for (int x = 0; x < resultlist.size(); x++) {
					if (resultlist.get(x).get("iskey").toString().equals("1")) {
						String columnname = resultlist.get(x).get("columnname")
								.toString();

						if (resultlist.get(x).get("datatype").toString()
								.equals("char")
								|| resultlist.get(x).get("datatype").toString()
										.equals("varchar")
								|| resultlist.get(x).get("datatype").toString()
										.equals("datetime")
								|| resultlist.get(x).get("datatype").toString()
										.equals("date")) {
							// 有些类型需要双引号....
							columnvalue = columnname + "='"
									+ dataJson.get(columnname).toString() + "'";
						} else {
							columnvalue = columnname + "="
									+ dataJson.get(columnname).toString();
						}

						if (wherecolumns.equals("")) {
							wherecolumns = columnvalue;
						} else {
							wherecolumns = wherecolumns + " and " + columnvalue;
						}
					}

				}

				Map<String, Object> deletevalueObj = new HashMap<String, Object>();
				deletevalueObj.put("tablename", tablename);
				deletevalueObj.put("wherecolumns", wherecolumns);
				m999999dao.deleteStandardTable(deletevalueObj);
			}

			rejson.put("code", "1");
			rejson.put("msg", "success");

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "处理时发生异常!");
			return rejson.toString();
		}
	}

	/**
	 * 标准化更新配置数据表
	 */
	@Override
	public String updateStandardTable(String companyid, String loginid,
			String tablename, String data) {

		Map<String, Object> valueObj = new HashMap<String, Object>();
		List<Map<String, Object>> resultlist = null;
		JSONObject rejson = new JSONObject();
		String wherecolumns = "";
		String setcolumns = "";

		try {
			valueObj.put("tablename", tablename);

			resultlist = m999999dao.getTableColumnConfig(valueObj);

			if (resultlist.size() == 0) {
				rejson.put("code", "0");
				rejson.put("msg", "没有表配置信息!");
				return rejson.toString();
			}

			JSONArray dataarray = JSONArray.parseArray(data);
			// 增加companyid项
			if (!tablename.equals("company")
					&& !(tablename.equals("login") && companyid.equals("1"))) {
				for (int i = 0; i < dataarray.size(); i++) {
					JSONObject dataJson = dataarray.getJSONObject(i);
					dataJson.remove("old_companyid");
					dataJson.put("old_companyid", companyid);
				}
			}

			for (int i = 0; i < resultlist.size(); i++) {
				String columnname = resultlist.get(i).get("columnname")
						.toString();

				// 是否主键...
				boolean iskey = true;
				if (resultlist.get(i).get("iskey").toString().equals("0")) {
					iskey = false;
				}

				// 循环data
				for (int j = 0; j < dataarray.size(); j++) {
					JSONObject dataJson = dataarray.getJSONObject(j);
					if (iskey) {
						if (dataJson.get("old_" + columnname) == null) {
							rejson.put("code", "0");
							rejson.put("msg", "第[" + (j + 1)
									+ "]行数据不存在主键列[old_" + columnname + "]的信息!");
							return rejson.toString();
						}
					}
				}
			}

			for (int k = 0; k < dataarray.size(); k++) {
				JSONObject dataJson = dataarray.getJSONObject(k);
				String newcolumnvalue = "";
				String oldcolumnvalue = "";
				wherecolumns = "";

				for (int x = 0; x < resultlist.size(); x++) {
					String columnname = resultlist.get(x).get("columnname")
							.toString();

					if (resultlist.get(x).get("iskey").toString().equals("1")) {
						if (resultlist.get(x).get("datatype").toString()
								.equals("char")
								|| resultlist.get(x).get("datatype").toString()
										.equals("varchar")
								|| resultlist.get(x).get("datatype").toString()
										.equals("datetime")
								|| resultlist.get(x).get("datatype").toString()
										.equals("date")) {
							// 有些类型需要双引号....
							oldcolumnvalue = columnname
									+ "='"
									+ dataJson.get("old_" + columnname)
											.toString() + "'";
						} else {
							oldcolumnvalue = columnname
									+ "="
									+ dataJson.get("old_" + columnname)
											.toString();
						}

						if (wherecolumns.equals("")) {
							wherecolumns = oldcolumnvalue;
						} else {
							wherecolumns = wherecolumns + " and "
									+ oldcolumnvalue;
						}
					}

					if (dataJson.get(columnname) != null) {
						if (resultlist.get(x).get("datatype").toString()
								.equals("char")
								|| resultlist.get(x).get("datatype").toString()
										.equals("varchar")
								|| resultlist.get(x).get("datatype").toString()
										.equals("datetime")
								|| resultlist.get(x).get("datatype").toString()
										.equals("date")) {
							// 有些类型需要双引号....
							newcolumnvalue = columnname + "='"
									+ dataJson.get(columnname).toString() + "'";
						} else {
							newcolumnvalue = columnname + "="
									+ dataJson.get(columnname).toString();
						}

						if (setcolumns.equals("")) {
							setcolumns = newcolumnvalue;
						} else {
							setcolumns = setcolumns + "," + newcolumnvalue;
						}
					}
				}

				if (!setcolumns.equals("")) {
					Map<String, Object> updatevalueObj = new HashMap<String, Object>();
					updatevalueObj.put("tablename", tablename);
					updatevalueObj.put("setcolumns", setcolumns);
					updatevalueObj.put("wherecolumns", wherecolumns);
					m999999dao.updateStandardTable(updatevalueObj);
				}
			}

			rejson.put("code", "1");
			rejson.put("msg", "success");

			return rejson.toString();

		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", "0");
			rejson.put("msg", "处理时发生异常!");
			return rejson.toString();
		}
	}

	@Override
	public String queryCompany(String data) {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", "1");
		ret.put("msg", "success");
		try {
			Map<String, Object> dataMap = (Map<String, Object>) JSONValue
					.parseWithException(data);

			ret.put("data", this.userDao.queryCompany(dataMap).get(0));

		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("msg", e.toString());
		}
		return JSONValue.toJSONString(ret);
	}

	@Override
	public String loginPartner(String data) {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", "1");
		ret.put("msg", "success");
		try {
			Map<String, Object> dataMap = (Map<String, Object>) JSONValue
					.parseWithException(data);
		
			List<Map<String, Object>> tlist1 = this.userDao
					.queryPartner(dataMap);

			if (tlist1.size() == 0) {
				ret.put("code", "0");
				ret.put("msg", "亲，用户不存在呃！");
				return JSONValue.toJSONString(ret);
			}

			if (!(dataMap.get("pwd") + "").equals(tlist1.get(0).get(
					"partnerpassword")
					+ "")) {
				ret.put("code", "0");
				ret.put("msg", "亲，密码错误呃！");
				return JSONValue.toJSONString(ret);
			}

			ret.put("name", tlist1.get(0).get("name").toString());
			ret.put("partnerid", tlist1.get(0).get("partnerid").toString());
			ret.put("companyname", tlist1.get(0).get("companyname").toString());
			if ("srm".equals(dataMap.get("type") + "")) {
				ret.put("main_url", EGlobal.efs_srm_url);
			} else if ("myshop".equals(dataMap.get("type") + "")) {
				ret.put("main_url", EGlobal.efs_myshop_url);
			} else {
				ret.put("main_url", EGlobal.efs_store_url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("code", "0");
			ret.put("msg", e.toString());
		}
		return JSONValue.toJSONString(ret);
	}
	/**
	 * 平台管理-机构入驻 1. 增加company数据... 2. 默认生成企业管理员角色...
	 * @throws Exception 
	 */
	public String TX4addCompanySelf(String data) throws Exception {

		Map<String, Object> 
			valueObj = (Map<String, Object>) JSONValue.parseWithException(data);

		valueObj.put("companytype", "零售");

		// 生成企业信息
		userDao.intoCompany(valueObj);

		List<Map<String, Object>> resultlist = null;
		resultlist = userDao.getNewCompany(valueObj);
		// 获取企业编码...
		String newcompanyid = resultlist.get(0).get("companyid").toString();
		valueObj.put("companyid", newcompanyid);

		// 生成企业管理员角色
		userDao.intoCompanyRole(valueObj);

		// 生成企业总部机构
		userDao.newShop(valueObj);

		// 生成角色模块权限
		userDao.newRoleModule(valueObj);

		// 平台/零售/经销
		String companytype = valueObj.get("companytype").toString();

		// 如果是经销商,初始化CompanyRebate (跨库写入)
		if (companytype.equals("经销")) {
			userDao.newCompanyRebate(valueObj);
		}

		// 如果是零售商,初始化CompanyPrepay和CompanyPointExConfig (跨库写入)
		if (companytype.equals("零售")) {
			userDao.newCompanyPrepay(valueObj);
			userDao.newCompanyPointExConfig(valueObj);
		}

		return "";
	}

//////////////////////////////////////////////华丽的分割线--初始化企业管理员//////////////////////////////////////////
	//初始化企业管理员
	public String insertUserModule(String data)  throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		
			
		
			Map<String, Object> dataMap = (Map<String, Object>) JSONValue
					.parseWithException(data);
			if(data!=null&&!"".equals(data)){
			List<Map<String, Object>> company =  usermoduleDao.findCompany(dataMap);
			if(company==null||company.size()==0){
				usermoduleDao.insertCompany(dataMap);
				company =  usermoduleDao.findCompany(dataMap);
				dataMap.put("companyid", company.get(0).get("companyid"));
				dataMap.put("password", SHA1.sha1("abcd1234"));
				
				List<Map<String, Object>> user =  usermoduleDao.findLogin(dataMap);
			//	System.out.println("vvvvvvvvvvvvvv "+dataMap.get("companyid")+" "+dataMap.get("entid"));
				if(user==null||user.size()==0){
					usermoduleDao.insertLoginDao(dataMap);
					usermoduleDao.insertRole(dataMap);
					usermoduleDao.insertLoginRole(dataMap);
					usermoduleDao.insertRoleModule(dataMap);
				}else{
					ret.put("code", "0");
					ret.put("msg","生成的管理员账号已经存在");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}else{
				ret.put("code", "0");
				ret.put("msg","传入的企业号已经存在");
			}
				
			}else{
				ret.put("code", "0");
				ret.put("msg","传入的数据为空");
				
			}

		return JSONValue.toJSONString(ret);
	}
	//初始化门店
	public String insertShop(String data)  throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		
			
		
			Map<String, Object> dataMap = (Map<String, Object>) JSONValue
					.parseWithException(data);
			if(data!=null&&!"".equals(data)){
			List<Map<String, Object>> shop =  usermoduleDao.findShop(dataMap);
			if(shop==null||shop.size()==0){
				usermoduleDao.insertShop(dataMap);
				 
			}else{
				ret.put("code", "0");
				ret.put("msg","传入的门店已经存在");
			}
				
			}else{
				ret.put("code", "0");
				ret.put("msg","传入的数据为空");
				
			}

		return JSONValue.toJSONString(ret);
	}
	
	//删除门店
	public String deleteShop(String data)  throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		

			Map<String, Object> dataMap = (Map<String, Object>) JSONValue
					.parseWithException(data);
			if(data!=null&&!"".equals(data)){
			List<Map<String, Object>> shop =  usermoduleDao.findShop(dataMap);
			if(shop==null||shop.size()==0){
				ret.put("code", "0");
				ret.put("msg","传入的门店不存在");
				
				 
			}else{
				usermoduleDao.deleteShop(dataMap);
			}
				
			}else{
				ret.put("code", "0");
				ret.put("msg","传入的数据为空");
				
			}

		return JSONValue.toJSONString(ret);
	}
	
	/**
	 * 门店管理-获取门店数据...
	 */
	@Override
	public String getShopInfoByReqest(String companyid, String loginid,String reqshop) {
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", loginid);
		valueObj.put("companyid", companyid);
		valueObj.put("reqshop", reqshop);
		List<Map<String, Object>> resultlist = null;
		List<Map<String, Object>> rolelist = null;
		List<Map<String, Object>> arealist = null;
		List<Map<String, Object>> shoplist = null;

		try {
			resultlist = manageDao.getLoginInfo(valueObj);
			rolelist = manageDao.getRoleInfo(valueObj);
			arealist = manageDao.getAreaInfo(valueObj);
			shoplist = manageDao.getShopInfoByReqest(valueObj);

			// 格式化日期，如果存在日期类型则需要格式化日期...
			formatDate(resultlist);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("code", "1");

			jsonObj.put("total", resultlist.size());

			jsonObj.put("data", JSONObject.toJSON(resultlist));
			jsonObj.put("rolelist", JSONObject.toJSON(rolelist));
			jsonObj.put("arealist", JSONObject.toJSON(arealist));
			jsonObj.put("shoplist", JSONObject.toJSON(shoplist));

			return jsonObj.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
