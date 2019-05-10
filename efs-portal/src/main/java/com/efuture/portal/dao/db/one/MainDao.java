/*
 * Copyright (C), 1996-2014
 * FileName: SampleDao.java
 * Author:   王华君
 * Date:     Dec 18, 2014 11:15:28 AM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.dao.db.one;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * sample dao 操作类<br>
 * 下面都是一些通用的操作数据库例子,方便不懂的人快速入门<br>
 * 
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component("MainDao")
public interface MainDao {

	public List<Map<String, Object>> checkLogin(Map<String, Object> paramsmap);

	public List<Map<String, Object>> getMySheet(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getRoleFlag(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getMySheet_Total(
			Map<String, Object> paramsmap);

	public List<Map<String, Object>> getMonthSaleInfo(
			Map<String, Object> paramsmap);

	public List<Map<String, Object>> getDaySaleInfo(
			Map<String, Object> paramsmap);

	public List<Map<String, Object>> getMenuGroup(Map<String, Object> paramsmap);

	public List<Map<String, Object>> getMenuModule(Map<String, Object> paramsmap);

	public List<Map<String, Object>> getLoginShopList(
			Map<String, Object> paramsmap);

	public List<Map<String, Object>> getShopList(Map<String, Object> paramsmap);

	public List<Map<String, Object>> queryHomeUrl(Map<String, Object> param1);

	public List<Map<String,Object>> existsUser(Map<String, Object> param);

	public List<Map<String,Object>> modifyPassword(Map<String, Object> param);
	
	public List<Map<String,Object>> adminmodifyPassword(Map<String, Object> param);

	public void test_feng(Map<String, Object> valueObj);

	public List<Map<String,Object>> queryHomeUrlWithRole(Map<String, Object> param2);

	public void replaceUserToken(Map<String, Object> map);

	public void update_Lasttime_4Usertoken(Map<String, Object> map);
	
	public void updateShopId_Usertoken(Map<String, Object> map);

	public List<Map<String,Object>> queryUserTokenByToken(Map<String, Object> map);

	public List<Map<String, Object>> queryUserarea(Map<String, Object> valueObj);

}
