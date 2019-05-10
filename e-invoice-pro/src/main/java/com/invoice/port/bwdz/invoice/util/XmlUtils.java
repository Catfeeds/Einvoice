package com.invoice.port.bwdz.invoice.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.invoice.util.MyAES;

import sun.misc.BASE64Encoder;

public class XmlUtils {
	/**
	 * 拼通用报文(xml)
	 * 
	 * @return
	 * @throws Exception 
	 */

	public static String getSendToTaxXML(String interfaceCode,String content,String appid,String contentPassword)
			throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version='1.0' encoding='UTF-8' ?>");
		sb.append("<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"DZFPQZ0.2\"> ");
		sb.append("<globalInfo>");
		sb.append("<appId>").append(appid).append("</appId>");
		sb.append("<interfaceId></interfaceId>");
		sb.append("<interfaceCode>").append(interfaceCode).append("</interfaceCode>");
		sb.append("<requestCode>DZFPQZ</requestCode>");
		sb.append("<requestTime>").append(new Date()).append("</requestTime>");
		sb.append("<responseCode>Ds</responseCode>");
		sb.append("<dataExchangeId>").append("DZFPQZ").append(interfaceCode).append(Utils.formatToDay()).append(Utils.randNineData()).append("</dataExchangeId>");
		sb.append("</globalInfo>");
		sb.append("<returnStateInfo>");
		sb.append("<returnCode></returnCode>");
		sb.append("<returnMessage></returnMessage>");
		sb.append("</returnStateInfo>");
		sb.append("<Data>");
		sb.append("<dataDescription>");
		sb.append("<zipCode>0</zipCode>");
		sb.append("</dataDescription>");
		sb.append("<content>");
		//if (interfaceCode.equals(Utils.dfxj1001)) {
			content =  getUploadContent(content);
		//} 
		/*else if (interfaceCode.equals(Utils.dfxj1003)) {
			content = getRemainContent(nsrsbh);
		}
		else if (interfaceCode.equals(Utils.dfxj1004)) {
			content = getSearchContent(nsrsbh,fpqqlsh);
		}
		else if (interfaceCode.equals(Utils.dfxj1005)) {
			content = getDownloadAddrContent(nsrsbh, fpqqlsh, fpdm, fphm);
		}*/
		content = content.replaceAll("\r\n", "").replaceAll("\n", "");//去掉空格和换行
		sb.append(content);
		sb.append("</content>");
		sb.append("<contentKey>");
		String contentMD5 = MyAES.MD5(content.getBytes("UTF-8"));//对content数据进行MD5加密
		String contentKey = MyAES.encryptBASE64(MyAES.encrypt(contentMD5.getBytes("UTF-8"), contentPassword)).replaceAll("\r\n", "").replaceAll("\n", "");//对md5后的数据进行AES加密
		sb.append(contentKey);
		sb.append("</contentKey>");
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
		/*StringBuffer content = new StringBuffer("");
		content.append("<REQUEST_COMMON_FPKJ class='REQUEST_COMMON_FPKJ'> ");
		content.append("<FPQQLSH>TEST" + Utils.formatToTime() + "01"+ "</FPQQLSH>");
		content.append("<KPLX>0</KPLX>");
		content.append("<ZSFS>0</ZSFS>");
		content.append("<XSF_NSRSBH>110109500321655</XSF_NSRSBH>");
		content.append("<XSF_MC>百旺电子测试2</XSF_MC>");
		content.append("<XSF_DZDH>南山区蛇口、83484949</XSF_DZDH>");
		content.append("<XSF_YHZH>xx银行、88888888888</XSF_YHZH>");
		content.append("<GMF_NSRSBH/>");
		content.append("<GMF_MC>张三</GMF_MC>");
		content.append("<GMF_DZDH/>");
		content.append("<GMF_YHZH/>");
		content.append("<KPR>开票人</KPR>");
		content.append("<SKR>收款人</SKR>");
		content.append("<FHR>复核人</FHR>");
		content.append("<YFP_DM/>");
		content.append("<YFP_HM/>");
		content.append("<JSHJ>117</JSHJ>");
		content.append("<HJJE>100</HJJE>");
		content.append("<HJSE>17</HJSE>");
		content.append("<BZ>备注</BZ>");
		content.append("<GMF_SJH></GMF_SJH>");
		content.append("<GMF_DZYX></GMF_DZYX>");
		content.append("<FPT_ZH></FPT_ZH>");
		content.append("<HYLX>0</HYLX>");
		content.append("<BY1></BY1>");
		content.append("<BY2></BY2>");
		content.append("<BY3></BY3>");
		content.append("<BY4></BY4>");
		content.append("<BY5></BY5>");
		content.append("<BY6></BY6>");
		content.append("<BY7></BY7>");
		content.append("<BY8></BY8>");
		content.append("<BY9></BY9>");
		content.append("<BY10></BY10>");
		content.append("<COMMON_FPKJ_XMXXS class='COMMON_FPKJ_XMXX' size='2'>");
		content.append("<COMMON_FPKJ_XMXX>");
		content.append("<FPHXZ>0</FPHXZ>");
		content.append("<SPBM>1010101050000000000</SPBM>");
		content.append("<XMMC>红高粱</XMMC>");
		content.append("<GGXH>500克</GGXH>");
		content.append("<DW>袋</DW>");
		content.append("<XMSL>1</XMSL>");
		content.append("<XMDJ>50</XMDJ>");
		content.append("<XMJE>50</XMJE>");
		content.append("<SL>0.17</SL>");
		content.append("<SE>8.5</SE>");
		content.append("<BY1></BY1>");
		content.append("<BY2></BY2>");
		content.append("<BY3></BY3>");
		content.append("<BY4></BY4>");
		content.append("<BY5></BY5>");
		content.append("<BY6></BY6>");
		content.append("</COMMON_FPKJ_XMXX>");
		content.append("<COMMON_FPKJ_XMXX>");
		content.append("<FPHXZ>0</FPHXZ>");
		content.append("<SPBM>1010101010000000000</SPBM>");
		content.append("<XMMC>东北大米</XMMC>");
		content.append("<GGXH>500克</GGXH>");
		content.append("<DW>袋</DW>");
		content.append("<XMSL>1</XMSL>");
		content.append("<XMDJ>50</XMDJ>");
		content.append("<XMJE>50</XMJE>");
		content.append("<SL>0.17</SL>");
		content.append("<SE>8.5</SE>");
		content.append("<BY1></BY1>");
		content.append("<BY2></BY2>");
		content.append("<BY3></BY3>");
		content.append("<BY4></BY4>");
		content.append("<BY5></BY5>");
		content.append("<BY6></BY6>");
		content.append("</COMMON_FPKJ_XMXX>");
		content.append("</COMMON_FPKJ_XMXXS>");
		content.append("</REQUEST_COMMON_FPKJ>");*/
		return  new BASE64Encoder().encodeBuffer(content.toString().getBytes("UTF-8"));
	}

	/**
	 * 获取加密查询报文内容(发票查询报文)
	 * 
	 * @param fpqqlsh
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSearchContent(String nsrsbh ,String fpqqlsh)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer("");
		sb.append("<REQUEST_COMMON_FPCX class='REQUEST_COMMON_FPCX'>");
		sb.append("<FPQQLSH>" + fpqqlsh + "</FPQQLSH>");
		sb.append("<XSF_NSRSBH>" + nsrsbh + "</XSF_NSRSBH>");
		sb.append("</REQUEST_COMMON_FPCX>");
		return new BASE64Encoder().encodeBuffer(sb.toString().getBytes("UTF-8"));
	}

	/**
	 * 获取加密查询报文内容(发票结余报文)
	 * 
	 * @param nsrsbh
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getRemainContent(String nsrsbh)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer("");
		sb.append("<REQUEST_COMMON_FPKCCX class=\"REQUEST_COMMON_FPKCCX\">");
		sb.append("<NSRSBH>" + nsrsbh + "</NSRSBH>");
		sb.append("</REQUEST_COMMON_FPKCCX>");
		return new BASE64Encoder().encodeBuffer(sb.toString().getBytes("UTF-8"));
	}

	/**
	 * 获取加密查询报文内容(发票下载地址查询)
	 * @param nsrsbh
	 * @param fpqqlsh
	 * @param fpdm
	 * @param fphm
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getDownloadAddrContent(String nsrsbh,String fpqqlsh,String fpdm,String fphm)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer("");
		sb.append("<REQUEST_COMMON_FPXZDZCX class=\"REQUEST_COMMON_FPXZDZCX\">");
		sb.append("<NSRSBH>"+nsrsbh+"</NSRSBH>");
		sb.append("<FPQQLSH>"+fpqqlsh+"</FPQQLSH>");
		sb.append("<FP_DM>"+fpdm+"</FP_DM>");
		sb.append("<FP_HM>"+fphm+"</FP_HM>");
		sb.append("</REQUEST_COMMON_FPXZDZCX>");
		return new BASE64Encoder().encodeBuffer(sb.toString().getBytes("UTF-8"));
	}

}
