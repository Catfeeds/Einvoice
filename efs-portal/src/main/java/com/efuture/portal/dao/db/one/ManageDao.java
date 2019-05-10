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
@Component("ManageDao")
public interface ManageDao {
				
	
	public List<Map<String, Object>> getLoginInfo(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getCompanyLoginInfo(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getCompanyInfo(Map<String, Object> paramsmap);	
	
	public List<Map<String, Object>> getLoginRolePower(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getLoginShopPower(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getShopInfo(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getRoleInfo(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getAreaInfo(Map<String, Object> paramsmap);	
	
	public List<Map<String, Object>> getRoleMenuGroup(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getRoleMenuModule(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getRoleModule(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getNewCompany(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getNewMenu(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> checkLoginModule(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> checkEShopDB(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getUserControl(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getControl(Map<String, Object> paramsmap);
	
	public void deleteUserControl(Map<String, Object> paramsmap);
	
						
	public void deleteRoleModule(Map<String, Object> paramsmap);
	
	public void deleteLoginRole(Map<String, Object> paramsmap);
	
	public void deleteLoginShop(Map<String, Object> paramsmap);		
	
	public void intoRoleModule(Map<String, Object> paramsmap);
	
	public void intoLoginRole(Map<String, Object> paramsmap);
	
	public void intoLoginShop(Map<String, Object> paramsmap);
	
	public void intoQuyuShop(Map<String, Object> valueObj);
	
	public void intoCompany(Map<String, Object> paramsmap);
	
	public void intoCompanyRole(Map<String, Object> paramsmap);
	
	public void intoRoleControl(Map<String, Object> paramsmap);
	
	public void newLoginRole(Map<String, Object> paramsmap);
	
	public void newRoleModule(Map<String, Object> paramsmap);
	
	public void newShop(Map<String, Object> paramsmap);
	
	public void newMenu(Map<String, Object> paramsmap);
	
	public void newModule(Map<String, Object> paramsmap);
	
	public void newLoginShop(Map<String, Object> paramsmap);
	
	public void newLogin(Map<String, Object> paramsmap);
	
	public void newCompanyRebate(Map<String, Object> paramsmap);
	
	public void newCompanyPrepay(Map<String, Object> paramsmap);
	
	public void newCompanyPointExConfig(Map<String, Object> paramsmap);

	public void deleteQuyuShop(Map<String, Object> valueObj);
	
	public List<Map<String, Object>> getShopInfoByReqest(Map<String, Object> paramsmap);
}

