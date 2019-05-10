package com.efuture.portal.dao.db.one;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("UsermoduleDao")
public interface UsermoduleDao {
	
	public int insertCompany(Map<String, Object> paramsmap);
	
	public int insertLoginDao(Map<String, Object> paramsmap);
	
	public int insertLoginRole(Map<String, Object> paramsmap);
	
	public int insertRole(Map<String, Object> paramsmap);
	
	public int insertRoleModule(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> findLogin(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> findCompany(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> findShop(Map<String, Object> paramsmap);
	
	public int insertShop(Map<String, Object> paramsmap);
	
	public int deleteShop(Map<String, Object> paramsmap);
}
