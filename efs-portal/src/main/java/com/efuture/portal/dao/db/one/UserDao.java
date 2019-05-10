/*
 * Copyright (C), 1996-2015
 * FileName: UserDao.java
 * Author:   王华君
 * Date:     Feb 9, 2015 4:23:31 PM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.dao.db.one;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.efuture.portal.beans.User;

/**
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉
 *
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface UserDao {

	public User getUser(String token);

	public List<Map<String,Object>> queryCompany(Map<String, Object> dataMap);

	public List<Map<String, Object>> queryPartner(Map<String, Object> dataMap);
	public void intoCompany(Map<String, Object> valueObj);

	public List<Map<String, Object>> getNewCompany(Map<String, Object> valueObj);

	public void intoCompanyRole(Map<String, Object> valueObj);

	public void newShop(Map<String, Object> valueObj);

	public void newRoleModule(Map<String, Object> valueObj);

	public JSONArray checkEShopDB(Map<String, Object> valueObj);

	public void newCompanyRebate(Map<String, Object> valueObj);

	public void newCompanyPrepay(Map<String, Object> valueObj);

	public void newCompanyPointExConfig(Map<String, Object> valueObj);
	
}
