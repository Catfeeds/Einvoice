package com.efuture.portal.service;

import java.util.List;
import java.util.Map;

import com.efuture.portal.beans.Enterprise;



public interface ModuleService {

	public String getShopInfo(String companyid,String loginid,String data);
	
	public String getLoginInfo(String companyid,String loginid,String username,String userid);
	
	public String getCompanyLoginInfo(String companyid,String loginid);
	
	public String getCompanyInfo(String companyid,String loginid);
	
	public String getLoginPower(String companyid,String setloginid,String loginid);
	
	public String getModuleInfo(String companyid,String loginid);
	
	public String getRoleInfo(String companyid,String loginid);	
	
	public String getRoleModule(String companyid,String loginid,String roleid);
	
	public String setRoleModule(String companyid,String loginid,String roleid,String data);
	
	public String setRoleControl(String companyid,String loginid,String roleid,String data);
	
	public String setLoginPower(String companyid,String setloginid,String loginid,String data);
	
	public String getShopTreeList(String companyid,String loginid);
	
	public String addCompany(String companyid,String loginid,String data);
	
	public String addCompanyLogin(String companyid,String loginid,String data);

			
	//////////////////////////////////////////////华丽的分割线//////////////////////////////////////////
	public String getTableColumnConfig(String tablename);
	
	public String getTableConfig();		
	
	public String insertTableColumnConfig(String tablename);
	
	public String insertStandardTable(String companyid,String loginid,String tablename,String data);
	
	public String deleteStandardTable(String companyid,String loginid,String tablename,String data);
	
	public String updateStandardTable(String companyid,String loginid,String tablename,String data);
	
	
	public List<Map<String, Object>> checkLoginModule(String moduleid,String companyid,String loginid);

	public String queryCompany(String data);

	public String loginPartner(String data);

	public String TX4addCompanySelf(String data) throws Exception;
	//////////////////////////////////////////////华丽的分割线--初始化企业管理员//////////////////////////////////////////
	
	public String insertUserModule(String data)  throws Exception;
	public String insertShop(String data)  throws Exception;
	public String deleteShop(String data)  throws Exception;
	public String getShopInfoByReqest(String companyid, String loginid,String reqshop);
}
