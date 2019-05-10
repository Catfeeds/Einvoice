package com.invoice.uiservice.service.impl;

import com.baiwang.bop.client.IBopClient;
import com.baiwang.bop.client.ILoginClient;
import com.baiwang.bop.client.impl.BopRestClient;
import com.baiwang.bop.client.impl.PostLogin;
import com.baiwang.bop.request.impl.LoginRequest;
import com.baiwang.bop.request.impl.bizinfo.CompanySearchRequest;
import com.baiwang.bop.respose.entity.LoginResponse;
import com.baiwang.bop.respose.entity.bizinfo.CompanySearchResponse;
import com.baiwang.bop.respose.entity.bizinfo.CompanySearchResult;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.Privatepara;
import com.invoice.bean.db.PurchaserInfo;
import com.invoice.uiservice.dao.CompanyNameDao;
import com.invoice.uiservice.service.CompanyNameService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service("CompanyNameService")
public class CompanyNameServiceImpl implements CompanyNameService{
	private final Log log = LogFactory.getLog(CompanyNameServiceImpl.class);

	@Resource(name = "CompanyNameDao")
	CompanyNameDao companyNameDao;

	@Override
	public Enterprise getEnterpriseByIdTitle(String entid) {
		return companyNameDao.getEnterpriseByIdTile(entid);
	}
	
	@Override
	public List<PurchaserInfo> getPurchaserListByName(String name){
		return companyNameDao.getPurchaserListByName(name);
	}
	
	@Override
	public int insertPurchaser(PurchaserInfo p){
		return companyNameDao.insertPurchaser(p);
	}
	
	public List<PurchaserInfo> getPurchaserListByClass(PurchaserInfo c,List<Privatepara> p)
	{
		try
		{
			String url = "", appKey = "", appSecret = "", token = "", userSalt = "", userName = "", password = "";
			
			//先查询本地，如果本地没有则调用百望接口进行查询
			List<PurchaserInfo> myPurchaserList = getPurchaserListByName(c.getName());
			
			if (myPurchaserList == null || myPurchaserList.isEmpty() || myPurchaserList.size() == 0)
			{
				int rightCount = 0;
				//获取百望查询开票人信息权限
				for(Privatepara myPara :p)
				{
					if (myPara.getPparaid().equalsIgnoreCase("gmfURL"))    
					{
						rightCount = rightCount + 1;
						url = myPara.getPparavalue().trim();
					}
						
					if (myPara.getPparaid().equalsIgnoreCase("gmfAppKey"))
					{
						rightCount = rightCount + 1;
						appKey = myPara.getPparavalue().trim();
					}
					
					if (myPara.getPparaid().equalsIgnoreCase("gmfAppSecret")) 
					{
						rightCount = rightCount + 1;
						appSecret = myPara.getPparavalue().trim();
					}
					
					if (myPara.getPparaid().equalsIgnoreCase("gmfUserSalt")) 
					{
						rightCount = rightCount + 1;
						userSalt = myPara.getPparavalue().trim();
					}
					
					if (myPara.getPparaid().equalsIgnoreCase("gmfUserName")) 
					{
						rightCount = rightCount + 1;
						userName = myPara.getPparavalue().trim();
					}
					
					if (myPara.getPparaid().equalsIgnoreCase("gmfPassword")) 
					{
						rightCount = rightCount + 1;
						password = myPara.getPparavalue().trim();
					}
				}

				//只有所需参数都配置了才会调用接口，否则直接返回
				if (rightCount != 6) return null;

				//调用百望云平台接口-获取token
				LoginRequest loginRequest = new LoginRequest(); 
				loginRequest.setAppkey(appKey); 
				loginRequest.setAppSecret(appSecret); 
				loginRequest.setUserName(userName); 
				loginRequest.setPasswordMd5(password);
				loginRequest.setUserSalt(userSalt); 
				ILoginClient loginClient = new PostLogin(url); 
				LoginResponse loginResponse = loginClient.login(loginRequest); 
				token = loginResponse.getAccess_token();
				
				//调用百望云平台接口-获取开票抬头列表
				CompanySearchRequest request = new CompanySearchRequest(); 
				request.setCompanyName(c.getName()); //沙箱环境只能获取"百望股份有限公司" 
				request.setTaxId("");
				request.setAccuracy("false"); 
				request.setSort("{\"frequency\": 0}"); 
					
				IBopClient client = new BopRestClient(url, appKey, appSecret); 
				CompanySearchResponse response = client.execute(request, token, CompanySearchResponse.class);
					
				for (CompanySearchResult myResult : response.getResult())
				{
					PurchaserInfo myPurchaser = new PurchaserInfo();
					
					myPurchaser.setName(myResult.getName()!=null?myResult.getName().trim():"");
					myPurchaser.setTaxId(myResult.getTaxId()!=null?myResult.getTaxId().trim():"");
					myPurchaser.setBankAndAccount(myResult.getBankAndAccount()!=null?myResult.getBankAndAccount().trim():"");
					myPurchaser.setAddressAndPhone(myResult.getAddressAndPhone()!=null?myResult.getAddressAndPhone().trim():"");
					myPurchaser.setBank(myResult.getBank()!=null?myResult.getBank().trim():"");
					myPurchaser.setBankAccount(myResult.getBankAccount()!=null?myResult.getBankAccount().trim():"");
					myPurchaser.setLocation(myResult.getLocation()!=null?myResult.getLocation().trim():"");
					myPurchaser.setMobilePhone(myResult.getFixedPhone()!=null?myResult.getFixedPhone().trim():"");
					myPurchaser.setCity(myResult.getCity()!=null?myResult.getCity().trim():"");
					myPurchaser.setCountry(myResult.getCounty()!=null?myResult.getCounty().trim():"");
					myPurchaser.setProvince(myResult.getProvince()!=null?myResult.getProvince().trim():"");
					myPurchaser.setCompanyIndex(myResult.getCompanyIndex()!=null?myResult.getCompanyIndex().trim():"");
					myPurchaser.setFrequency(myResult.getFrequency()!=null?myResult.getFrequency().toString():"");
					myPurchaser.setScore(myResult.getScore()!=null?myResult.getScore().trim():"");
					myPurchaser.setTaxAuthorityCode("");
					myPurchaser.setTaxAuthorityName("");
					
					//处理BankAndAccount
					if (myPurchaser.getBankAndAccount().equals(""))
					{
						myPurchaser.setBankAndAccount(myPurchaser.getBank() + " " + myPurchaser.getBankAccount());
					}
					
					//处理AddressAndPhone
					if (myPurchaser.getAddressAndPhone().equals(""))
					{
						myPurchaser.setAddressAndPhone(myPurchaser.getLocation() + " " + myPurchaser.getMobilePhone());
					}
					
					myPurchaserList.add(myPurchaser);

					int resultCount = insertPurchaser(myPurchaser);
					
					if (resultCount <= 0) log.info(myResult);
				}
			}
			
			return myPurchaserList;
		}
		catch (Exception ex)
		{
			log.error(ex);
		}

		return null;
	}
}
