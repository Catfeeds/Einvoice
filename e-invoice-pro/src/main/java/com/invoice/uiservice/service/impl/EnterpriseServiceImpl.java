package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.uiservice.dao.EnterpriseDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.util.HttpClientCommon;



@Service("EnterpriseService")
public class EnterpriseServiceImpl implements EnterpriseService{

	@Resource(name = "EnterpriseDao")
	EnterpriseDao enterpriseDao;


	@Override
	public void addEnterprise(Enterprise enterprise) throws Exception {
		enterpriseDao.insertEnterprise(enterprise);
		Token token = Token.getToken();
		
		HashMap<String, Object> parmas = new HashMap<String, Object>();
		parmas.put("token", token.getTokenid());
		parmas.put("data", JSONObject.toJSONString(enterprise));
		String url = FGlobal.portalurl + "rest/ui/module/registerUserModule.action";
		String res = HttpClientCommon.doPost(parmas, null, url, 3000, 3000, "utf-8");
				
		if(StringUtils.isEmpty(res)){
			throw new RuntimeException("添加企业账号异常，服务返回空");
		}else{
			JSONObject rjson = new JSONObject();
			rjson.parseObject(res);
			if("1".equals(rjson.getIntValue("code"))){
				throw new RuntimeException("添加企业账号异常："+rjson.getString("message"));
			}
		}
	}

	@Override
	public void updateEnterprise(Enterprise enterprise) throws Exception {
		enterpriseDao.updateEnterprise(enterprise);
	}

	@Override
	public void deleteEnterprise(Enterprise enterprise) throws Exception {
		enterpriseDao.deleteEnterprise(enterprise);
	}

	@Override
	public  List<HashMap<String,String>> getEnterprise(Map<String, Object> p)
			throws Exception {
		return enterpriseDao.getEnterprise(p);
	}
	

	@Override
	public Enterprise getEnterpriseById(Enterprise enterprise) {
		return enterpriseDao.getEnterpriseById(enterprise);
	}

	@Override
	public int getEnterpriseCount(Map<String, Object> p) {
		return enterpriseDao.getEnterpriseCount(p);
	}

}
