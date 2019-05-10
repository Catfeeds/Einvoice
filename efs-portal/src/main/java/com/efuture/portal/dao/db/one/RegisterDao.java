package com.efuture.portal.dao.db.one;

import org.springframework.stereotype.Component;

import com.efuture.portal.beans.RegisterBean;
import com.efuture.portal.beans.RegisterLogBean;

@Component("RegisterDao")
public interface RegisterDao {
	
	public RegisterBean findEnterpriseRegister(String entid);
	
	public int deleteEnterpriseRegister(String entid);
	
	public int insertEnterpriseRegister(RegisterBean bean);
	
	public int insertRegisterLog(RegisterLogBean bean);
	
	public int findcompanyByentid(String entid);
	
}
