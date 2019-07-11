package com.efuture.portal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.efuture.portal.beans.User;
import com.efuture.portal.dao.db.one.ZbUsermoduleDao;
import com.efuture.portal.service.ZbModuleService;


import net.minidev.json.JSONValue;

@Service("ZbModuleService")
public class ZbModuleServiceImpl implements ZbModuleService {

	@Resource
	ZbUsermoduleDao ZbusermoduleDao;

	// 初始化门店
	public String insertShop(String data) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();

		Map<String, Object> dataMap = (Map<String, Object>) JSONValue.parseWithException(data);
		Map<String, Object> insertrole=new HashMap<String, Object>();
		if (data != null && !"".equals(data)) {
			List<Map<String, Object>> shop = ZbusermoduleDao.findShop(dataMap);
			if (shop == null || shop.size() == 0) {
				int count = ZbusermoduleDao.getcountShop(dataMap);
				List<Map<String, Object>> loginrole = ZbusermoduleDao.getloginrole(dataMap);
				for (Map<String, Object> map2 : loginrole) {
					if (String.valueOf(count).equalsIgnoreCase(map2.get("count").toString())) {
						insertrole.put("loginid", map2.get("loginid").toString());
						insertrole.put("shopid", dataMap.get("shopid").toString());
						insertrole.put("entid", dataMap.get("entid").toString());
						ZbusermoduleDao.insertrole(insertrole);
					}
				}
				ZbusermoduleDao.insertShop(dataMap);

			} else {
				ret.put("code", "0");
				ret.put("msg", "传入的门店已经存在");
			}

		} else {
			ret.put("code", "0");
			ret.put("msg", "传入的数据为空");

		}

		return JSONValue.toJSONString(ret);
	}

	// 删除门店
	public String deleteShop(String data) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();

		Map<String, Object> dataMap = (Map<String, Object>) JSONValue.parseWithException(data);
		if (data != null && !"".equals(data)) {
			List<Map<String, Object>> shop = ZbusermoduleDao.findShop(dataMap);
			if (shop == null || shop.size() == 0) {
				ret.put("code", "0");
				ret.put("msg", "传入的门店不存在");

			} else {
				ZbusermoduleDao.deleteShop(dataMap);
				ZbusermoduleDao.deleterole(dataMap);
				
			}

		} else {
			ret.put("code", "0");
			ret.put("msg", "传入的数据为空");

		}

		return JSONValue.toJSONString(ret);
	}

	@Override
	public String updateShop(String data) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();

		Map<String, Object> dataMap = (Map<String, Object>) JSONValue.parseWithException(data);
		if (data != null && !"".equals(data)) {
			List<Map<String, Object>> shop = ZbusermoduleDao.findShop(dataMap);
			if (shop == null || shop.size() == 0) {
				ret.put("code", "0");
				ret.put("msg", "传入的门店不存在");

			} else {
				ZbusermoduleDao.updateShop(dataMap);
			}

		} else {
			ret.put("code", "0");
			ret.put("msg", "传入的数据为空");

		}

		return JSONValue.toJSONString(ret);
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
			this.ZbusermoduleDao.adminmodifyPassword(param);
			return JSONValue.toJSONString(retjson);
		} catch (Exception e) {
			e.printStackTrace();
			retjson.put("code", "0");
			retjson.put("msg", e.toString());
			return JSONValue.toJSONString(retjson);
		}

	}

}
