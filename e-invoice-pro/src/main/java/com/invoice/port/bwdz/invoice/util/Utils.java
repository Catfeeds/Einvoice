package com.invoice.port.bwdz.invoice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	public static final String dfxj1001 = "DFXJ1001"; // 订单上传接口
	public static final String dfxj1003 = "DFXJ1003"; // 发票库存信息
	public static final String dfxj1004 = "DFXJ1004"; // 发票查询
	public static final String dfxj1005 = "DFXJ1005"; // 发票下载地址查询
	
	public static final String interfaceLau_json = "json";//语言-json
	public static final String interfaceLau_xml = "xml";//语言-xml
	
	public static final String webservice_axis = "webservice_axis";//请求方式使用axis的webservice
	public static final String webservice_xfire = "webservice_xfire";//请求方式使用xfire的webservice		
	public static final String post_https = "post_https";//使用post请求方式
	
	public static final String requestMethod_xml = "doService";//xml的webservice请求方法名
	public static final String requestMethod_json = "doJsonService";//json的webservice请求方法名
	
	public static final String requestUrlMethod_xml = "/services/DZFPService";//xml的webservice请求方法名
	public static final String requestUrlMethod_json = "/services/DZFPJsonService";//json的webservice请求方法名
	
	/**
	 * 获取指定格式时间(yyyy-MM-dd)
	 * @return
	 */
	public static String formatToDay(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format((new Date()));
	}
	
	/**
	 * 获取指定格式时间(yyyyMMddHHmmss)
	 * @return
	 */
	public static String formatToTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format((new Date()));
	}
	
	/************************************************************************
	 * 获取9位随机数
	 */
	public static String randNineData(){
		return randData()+randFiveData();
	}
	
	/************************************************************************
	 * 获取5位随机数
	 */
	public static String randFiveData(){
		return String.valueOf((int)(Math.random()*90000+10000));
	}
	
	/************************************************************************
	 * 获取4位随机数
	 */
	public static String randData(){
		return String.valueOf((int)(Math.random()*9000+1000));
	}
}
