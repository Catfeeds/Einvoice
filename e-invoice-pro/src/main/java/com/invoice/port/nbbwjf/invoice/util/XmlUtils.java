package com.invoice.port.nbbwjf.invoice.util;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Encoder;

public class XmlUtils {
	/**
	 * 拼通用报文(xml)
	 * 
	 * @return
	 * @throws Exception 
	 */

	public static String getSendToTaxXML(String interfaceCode,String content,String appid,String requestcode,String requestsecret)
			throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<interface version=\"1.0\"> ");
		sb.append("<globalInfo>");
		sb.append("<appId>").append(appid).append("</appId>");
		sb.append("<interfaceId></interfaceId>");
		sb.append("<interfaceCode>").append(interfaceCode).append("</interfaceCode>");
		sb.append("<requestCode>").append(requestcode).append("</requestCode>");
		sb.append("<requestSecret>").append(requestsecret).append("</requestSecret>");
		sb.append("<requestTime>").append(Utils.formatToDateTime()).append("</requestTime>");
		sb.append("<responseCode>").append(requestcode).append("</responseCode>");
		sb.append("<dataExchangeId>").append(requestcode).append(interfaceCode).append(Utils.formatToDay()).append(Utils.randNineData()).append("</dataExchangeId>");
		sb.append("</globalInfo>");
		sb.append("<Data>");
		sb.append("<dataDescription>");
		sb.append("<zipCode>0</zipCode>");
		sb.append("</dataDescription>");
		sb.append("<content>");
		content =  getUploadContent(content);
		content = content.replaceAll("\r\n", "").replaceAll("\n", "");//去掉空格和换行
		sb.append(content);
		sb.append("</content>");
		sb.append("</Data>");
		sb.append("</interface>");
		return sb.toString();
	}
	
	/**
	 * 根据加密上传发票内容报文（发票开具）
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getUploadContent(String content) throws UnsupportedEncodingException {
		return  new BASE64Encoder().encodeBuffer(content.toString().getBytes("UTF-8"));
	}

	

}
