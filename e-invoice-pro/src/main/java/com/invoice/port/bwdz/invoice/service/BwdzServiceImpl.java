package com.invoice.port.bwdz.invoice.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.aisino.CaConstant;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.bwdz.invoice.bean.BwdzRtOpenInvoiceBean;
import com.invoice.port.bwdz.invoice.util.RequestUtils;
import com.invoice.port.bwdz.invoice.util.Utils;
import com.invoice.port.bwdz.invoice.util.XmlUtils;
import com.invoice.rtn.data.RtnData;

@Service("BwdzService")
public class BwdzServiceImpl implements BwdzService {

	BwdzGenerateBean bw;
	public BwdzServiceImpl( ){

		bw = new BwdzGenerateBean();
	}
	
	/**
	 * 开具发票
	 * **/
	public BwdzRtOpenInvoiceBean openinvoice(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn){
		
		bw.generateBaiWangBean(invque,taxinfo,detailList, rtn);
		System.out.println(rtn.getMessage());
		//登陆百旺获取数据
		if(rtn.getCode()==0){
			 login(rtn.getMessage(),invque.getIqentid()+File.separator+taxinfo.getTaxno(),Utils.dfxj1001,rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		BwdzRtOpenInvoiceBean rtinvoicebean = new BwdzRtOpenInvoiceBean();
		//返回后的数据做装换
		if(rtn.getCode()==0){
			rtinvoicebean=  bw.rtOpenToBean(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		
		return rtinvoicebean;
	}

	/**
	 * 发票查询(不分页)
	 * **/
	public BwdzRtOpenInvoiceBean findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		//BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		bw.findInvoice(invque,taxinfo, rtn);
		System.out.println(rtn.getMessage());
		if(rtn.getCode()==0){
		 login(rtn.getMessage(),invque.getIqentid()+File.separator+taxinfo.getTaxno(),Utils.dfxj1004,rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		BwdzRtOpenInvoiceBean rtinvoicebean = new BwdzRtOpenInvoiceBean();
		//返回后的数据做装换
		if(rtn.getCode()==0){
			rtinvoicebean=  bw.rtOpenToBean(rtn.getMessage(),rtn);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		return rtinvoicebean;
	}
	
	/**
	 * 登陆百旺系统，发送数据信息
	 * **/
	public void login(String xml,String nsrsbh,String interfaceCode,RtnData rtn ){
		
		Properties ca =  CaConstant.get(nsrsbh);
		String requestUrl = ca.getProperty("requestUrl");
		String appid = ca.getProperty("appid");
		String contentPassword = ca.getProperty("contentPassword");
		
		Date startDate = new Date();//初始化开始时间
		// 初始化参数
		String requestData = null;//初始化请求报文
		String rsData = null;//初始化结果报文
	/*	String requestMethod = null;//初始化请求方法
		String requestUrlMethod = null;//初始化连接方法
*/		
		/*String requestUrl = "https://dev.fapiao.com:18943/fpt-dsqz/";//初始化地址
		String appid = "6d29f136114544bcc73edcce960c430231183cc192c433e2b9ebcad56e8ceb08";//appid
		String contentPassword = "5EE6C2C11DD421F2";//AES加密密钥
		String fpqqlsh = "TEST2017022415272501";// 需要查询发票的流水号
		//String nsrsbh = "110109500321655";
		String fpdm = "050003521333";
		String fphm = "85004524";*/

		// 通过注释选择语言
		String interfaceLau = Utils.interfaceLau_xml;
//		 String interfaceLau = Utils.interfaceLau_json;
		

		// 通过注释选择接口
		// String interfaceCode = Utils.dfxj1001;//开具
//		 String interfaceCode = Utils.dfxj1004;//查询
//		 String interfaceCode = Utils.dfxj1003;//库存查询
//		String interfaceCode = Utils.dfxj1005;// 获取下载地址
		
		//通过注释选择调用方式
//		String requestInterface = Utils.webservice_axis;//请求方式使用axis的webservice
//		String requestInterface = Utils.webservice_xfire;//请求方式使用xfire的webservice		
		String requestInterface = Utils.post_https;//使用post请求方式
		try{
			// 组装请求报文
			if (Utils.interfaceLau_xml.equals(interfaceLau)) {
				requestData = XmlUtils.getSendToTaxXML(interfaceCode, xml,appid,contentPassword);
			/*	requestMethod = Utils.requestMethod_xml;//xml的请求方法
				requestUrlMethod = Utils.requestUrlMethod_xml;//xml的连接后缀
*/			} /*else if (Utils.interfaceLau_json.equals(interfaceLau)) {
				requestData = JsonUtils.getSendToTaxJson(interfaceCode, fpqqlsh, nsrsbh, fpdm, fphm,appid,contentPassword);
				requestMethod = Utils.requestMethod_json;//json的请求方法
				requestUrlMethod = Utils.requestUrlMethod_json;//json的连接后缀
			}*/else{
				System.out.println("请选择语言！");
			}
			
			System.out.println("组装报文完毕,请求使用的语言是:"+interfaceLau+",请求的方式是："+requestInterface+",请求报文为:"+requestData+",开始请求。");
			//Date requestStartDate = new Date();//初始化请求开始时间
			// 调用接口
			/*if(Utils.webservice_axis.equals(requestInterface)){
				rsData = RequestUtils.webServiceAxis(requestData,requestMethod,requestUrl+requestUrlMethod);
			}else if(Utils.webservice_xfire.equals(requestInterface)){
				rsData = RequestUtils.webServiceXfile(requestData, requestMethod, requestUrl+requestUrlMethod+"?wsdl");
			}else*/ if(Utils.post_https.equals(requestInterface)){
				rsData = RequestUtils.getHttpConnectResult(requestData,requestUrl+"invoice",nsrsbh);
			}
			//Date requestEndDate = new Date();//初始化请求结束时间
			//System.out.println("请求完毕，耗时【"+(requestEndDate.getTime()-requestStartDate.getTime())+"ms】");
			Date endDate = new Date();//初始化结束时间
			System.out.println("请求接口结束，获得结果:" + rsData);
			//System.out.println("总耗时【"+(endDate.getTime()-startDate.getTime())+"ms】");
			rtn.setMessage(rsData);
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	}
 
	
	 
	 
	 
	 
	
}
