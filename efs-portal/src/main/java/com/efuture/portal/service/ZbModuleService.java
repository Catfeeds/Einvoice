package com.efuture.portal.service;

import com.efuture.portal.beans.User;

public interface ZbModuleService {

	public String insertShop(String data)  throws Exception;
	public String deleteShop(String data)  throws Exception;
	public String updateShop(String data)  throws Exception;
	public String adminmodifyPassword(User user, String newpwd);

	
}
