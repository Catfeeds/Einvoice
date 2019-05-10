package com.invoice.port.bwgf.invoice.service;

import java.util.List;
import java.util.Properties;

import com.aisino.CaConstant;
import com.alibaba.fastjson.JSONObject;
import com.baiwang.bop.client.IBopClient;
import com.baiwang.bop.client.ILoginClient;
import com.baiwang.bop.client.impl.BopRestClient;
import com.baiwang.bop.client.impl.PostLogin;
import com.baiwang.bop.request.impl.LoginRequest;
import com.baiwang.bop.request.impl.bizinfo.CompanySearchRequest;
import com.baiwang.bop.request.impl.invoice.impl.BlankInvoiceQueryRequest;
import com.baiwang.bop.request.impl.invoice.impl.FormatfileBuildRequest;
import com.baiwang.bop.request.impl.invoice.impl.FormatfileQueryRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoiceInvalidRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoiceOpenRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoicePrintRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoiceQueryRequest;
import com.baiwang.bop.respose.entity.BlankInvoiceQueryResponse;
import com.baiwang.bop.respose.entity.FormatfileBuildResponse;
import com.baiwang.bop.respose.entity.FormatfileQueryResponse;
import com.baiwang.bop.respose.entity.InvoiceInvalidResponse;
import com.baiwang.bop.respose.entity.InvoiceOpenResponse;
import com.baiwang.bop.respose.entity.InvoicePrintResponse;
import com.baiwang.bop.respose.entity.InvoiceQueryResponse;
import com.baiwang.bop.respose.entity.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;

import baiwang.invoice.bean.FpttBean;
import baiwang.invoice.service.GetSignaTure;
import baiwang.invoice.service.UrlFactory;

public class BwgfServiceImpl implements BwgfService {
	
	
	//private  String url = "http://60.205.83.27/router/rest";             //接口地址
/*	private  String appkey="10000005";                                   //AppKey
	private  String appSecret="b65025d0-19d2-4841-88f4-ff4439b8da58";    //AppSecrect
	private  String userName="admin_1800000021168";                      //用户名
	private  String password="a123456";                                   //密码
	private  String userSalt="94db610c5c3049df8da3e9ac91390015";         //盐值 
*/	
   
	BwgfGenerateBean bw;
	public BwgfServiceImpl( ){

		bw = new BwgfGenerateBean();
	}
	
	/**
	 * 开具发票
	 * **/
	public InvoiceOpenResponse openinvoice(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn){
		InvoiceOpenResponse response = new InvoiceOpenResponse();
		InvoiceOpenRequest request= bw.generateBaiWangBean(invque,taxinfo,detailList, rtn);
		//登陆百旺获取数据
		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, InvoiceOpenResponse.class);
		}else{
			rtn.setCode(-1);
			 
			return null;
		}
	 
		return response;
	}
	
	
	/**
	 * 发票查询(不分页)
	 * **/
	public InvoiceQueryResponse findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		InvoiceQueryResponse response= new InvoiceQueryResponse();
		
		InvoiceQueryRequest request= bw.findInvoice(invque,taxinfo, rtn);
		
		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, InvoiceQueryResponse.class);
			response.getInvoiceList().get(0).getInvoiceDetailsList().get(0).getGoodsName();
		}else{
			rtn.setCode(-1);
			return null;
		}
		
		return response;
	}
	
	/**
	 * 空白发票作废
	 * **/
	public InvoiceInvalidResponse  invoiceInblankValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		InvoiceInvalidResponse response = new InvoiceInvalidResponse();

		InvoiceInvalidRequest request =  bw.generateInvoiceblankInValid(invque,taxinfo, rtn);

		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, InvoiceInvalidResponse.class);
		}else{
			rtn.setCode(-1);

			return null;
		}
	
		
		return response;
	}
	
	/**
	 * 已开发票作废
	 * **/
	public InvoiceInvalidResponse invoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		InvoiceInvalidResponse response = new InvoiceInvalidResponse();

		InvoiceInvalidRequest request =  bw.generateInvoiceYkInValid(invque,taxinfo, rtn);

		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, InvoiceInvalidResponse.class);
		}else{
			rtn.setCode(-1);

			return null;
		}
	
		
		return response;
	}
	
	/**
	 * 发票打印
	 * **/
	
	public InvoicePrintResponse invoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn){
		InvoicePrintResponse response =new InvoicePrintResponse();
	
		InvoicePrintRequest request =bw.generateInvoicePrint(invque,taxinfo,rtn);

		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, InvoicePrintResponse.class);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		return response;
	}
	
	/**
	 * 空白发票查询
	 * **/
	
	public BlankInvoiceQueryResponse blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		BlankInvoiceQueryResponse response = new BlankInvoiceQueryResponse();

		BlankInvoiceQueryRequest request=  bw.blankInvoice(invque,taxinfo,rtn);

		
		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, BlankInvoiceQueryResponse.class);
		}else{
			rtn.setCode(-1);
			return null;
		}
		
		return response;
	}
	
	/**
	 * 获取发票PDF连接(含推送)
	 * **/
	public FormatfileBuildResponse getPdf(Invque invque,Taxinfo taxinfo,RtnData rtn){
	
		FormatfileBuildResponse response = new FormatfileBuildResponse();
		FormatfileBuildRequest request = bw.generatePdf(invque,taxinfo, rtn);
		
		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, FormatfileBuildResponse.class);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		 
		return response;
	}

	/**
	 * 获取发票PDF查询
	 * **/
	public FormatfileQueryResponse  findPdf(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		FormatfileQueryResponse  response = new FormatfileQueryResponse ();
		FormatfileQueryRequest request = bw.findPdf(invque,taxinfo, rtn);
		
		if(rtn.getCode()==0){
			LoginRequest loginRequest= login(taxinfo,rtn);
			ILoginClient loginClient = new PostLogin(taxinfo.getItfserviceUrl());
			LoginResponse loginResponse = loginClient.login(loginRequest);
			String token=loginResponse.getAccess_token();//获取token
			IBopClient client = new BopRestClient(taxinfo.getItfserviceUrl(),loginRequest.getAppkey(), loginRequest.getAppSecret());
			response = 	client.execute(request, token, FormatfileQueryResponse .class);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		 
		return response;
	}
	
	
	

	public String access_fptt(FpttBean fptt,RtnData rtn){
		String ret = "";
		try{
			fptt.setToken(access_token(fptt));
			fptt.setSign(new GetSignaTure(fptt).getST() );
			
			ObjectMapper mapper = new ObjectMapper();
			
			CompanySearchRequest searchRequest = new CompanySearchRequest();
			CompanySearchRequest request = new CompanySearchRequest();
			request.setCompanyName("百望金税");
			request.setAccuracy("false");
			request.setSort("{\"frequency\": 0}");
			request.setTaxId("");
			request.setAppId("str");
			String jackSon=mapper.writeValueAsString(request);

			ret = UrlFactory.setUrl(fptt.getUrl()+"?method=baiwang.bizinfo.companySearch"
					+ "&version=2.0"
					+ "&appKey="+fptt.getClient_id()
					+ "&format=json"
					+ "&timestamp="+fptt.getTimestamp()
					+ "&token="+fptt.getToken()
					+ "&type=sync"
					+ "&sign="+fptt.getSign(),jackSon);
			System.out.println(ret);
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
			
		}
		return ret;
	}
	
	public String access_token(FpttBean fptt){
		String ret = "";
		String url = fptt.getUrl()+"?timestamp="+fptt.getTimestamp()+"&username="+fptt.getUsername()+"&client_secret="+fptt.getClient_secret()+
					 "&grant_type="+fptt.getGrant_type()+"&method="+fptt.getMethod()+"&client_id="+fptt.getClient_id()+"&password="+fptt.getPassword()+
					 "&version="+fptt.getVersion();
			 
		try{
					ret = JSONObject.parseObject(JSONObject.parseObject(UrlFactory.setUrl(url, fptt.getClient_id())).getString("response")).getString("access_token");
					 
		}catch(Exception e){
			e.printStackTrace();
		}
		 return ret;
	}
	
	public LoginRequest login(Taxinfo taxinfo,RtnData rtn ){
			Properties ca =  CaConstant.get(taxinfo.getTaxno());
			LoginRequest loginRequest = new LoginRequest();
/*			loginRequest.setAppkey(appkey);
			loginRequest.setAppSecret(appSecret);
			loginRequest.setUserName(userName);
			loginRequest.setPasswordMd5(password);
			loginRequest.setUserSalt(userSalt); */
			System.out.println("ddddddddddddddddddddd "+ca.getProperty("password")+" "+ca.getProperty("password").length());
			loginRequest.setAppkey(ca.getProperty("appkey"));
			loginRequest.setAppSecret(ca.getProperty("appSecret"));
			loginRequest.setUserName(ca.getProperty("userName"));
			loginRequest.setPasswordMd5(ca.getProperty("password"));
			loginRequest.setUserSalt(ca.getProperty("userSalt"));

		 return loginRequest;
	}
	
}
