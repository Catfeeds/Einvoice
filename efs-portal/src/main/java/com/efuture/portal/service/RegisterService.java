package com.efuture.portal.service;

import com.alibaba.fastjson.JSONObject;
import com.efuture.portal.beans.RegisterBean;

public interface RegisterService {
	
	public String findEnterpriseRegister(String entid,String userid);
	
	public RegisterBean findEnterpriseRegister(String entid);
		
	public void insertEnterpriseRegister(RegisterBean bean,String userid,JSONObject rejson);
	
	
}
