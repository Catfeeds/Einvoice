package com.efuture.portal.service;

import java.util.List;
import java.util.Map;

import com.efuture.portal.beans.Option;
import com.efuture.portal.beans.User;



public interface UIMainService {

	public String checkLogin(String companyid,String loginid,String password);
	
	public String getMySheet(String companyid,String loginid,int pageno);
	
	public String getMonthSaleInfo(String companyid,String loginid,int startmonth,int endmonth);
	
	public String getDaySaleInfo(String companyid,String loginid,String startday,String endday);
	
	public String getMenu(String companyid,String loginid);
	
	public String getMyShopList(String companyid,String loginid);
	
	public String getMyShopList(String companyid,String loginid,String shopname);
	
	public String getShopList(String companyid);

	public String modifyPassword(User user, String oldpwd, String newpwd);
	
	public String adminmodifyPassword(User user, String newpwd);
	
	public String modifyShopId_session(User user);
	
	public List<Map<String, Object>> findCompany(String entid);
	
	public List<Option> getLookupSelect(int lookupid);
	

	
}
