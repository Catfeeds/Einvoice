package com.efuture.portal.dao.db.one;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("ZbUsermoduleDao")
public interface ZbUsermoduleDao {
	
	
	public List<Map<String, Object>> findShop(Map<String, Object> paramsmap);
	
	public int insertShop(Map<String, Object> paramsmap);
	
	public int deleteShop(Map<String, Object> paramsmap);
	
	public int updateShop(Map<String, Object> paramsmap);
	
	public int getcountShop(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getloginrole(Map<String, Object> paramsmap);
	
	public int insertrole(Map<String, Object> paramsmap);
	
	public int deleterole(Map<String, Object> paramsmap);
	
	public List<Map<String,Object>> adminmodifyPassword(Map<String, Object> param);
}
