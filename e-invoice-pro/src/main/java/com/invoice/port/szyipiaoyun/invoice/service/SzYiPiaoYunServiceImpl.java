package com.invoice.port.szyipiaoyun.invoice.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.PortService;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunFindRequestBean;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunFindResponseBean;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunManualorderdetails;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunOpenRequestBean;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunOpenResponseBean;
import com.invoice.port.szyipiaoyun.invoice.bean.YiPiaoYunRedResponseBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.NewHashMap;

@Service("SzYiPiaoYunServiceImpl")
public class SzYiPiaoYunServiceImpl implements PortService {
	private final Log log = LogFactory.getLog(SzYiPiaoYunServiceImpl.class);
	//token url
	private final String tokenURL = "/oauth/token";
	//查询 url
	private final String findURL = "/api/invoice/invoiceinfo/find";
	//开票 url
	private final String openURL = "/api/invoice/invoiceinfo/billing";
	//红冲 url
	private final String redURL = "/api/invoice/invoiceinfo/doRed";

	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	InvqueDao inqueDao;
	
	/**
	 * 开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	@Override
	public void openInvoice(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList)
	{
		RtnData rtn = new RtnData();
		
		if(que.getRetflag() == null) que.setRetflag("");
		
		RtInvoiceBean resInv = null;
		
		if("1".equals(que.getRetflag())){
			resInv = findInvoice(que,taxinfo,rtn);
		}
		else{
			resInv = openInvoiceDP(que,taxinfo,detailList,rtn);
		}
		
		if(rtn.getCode() != 0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		else if(resInv == null){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		else if(resInv.getFp_hm() == null || "".equals(resInv.getFp_hm())){
			throw new RuntimeException("发票平台无返回信息");
		}
		
		List<InvoiceSaleDetail> dataList = new ArrayList<InvoiceSaleDetail>();
		
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				dataList.add(detail);
			}
		}
 
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, dataList);
		
		invoiceHead.setFpewm(resInv.getEwm());
		invoiceHead.setFphm(resInv.getFp_hm());
		invoiceHead.setFpdm(resInv.getFp_dm());
		invoiceHead.setFpskm(resInv.getSkm());
		invoiceHead.setFpjym(resInv.getFwm());
		invoiceHead.setFprq(resInv.getKprq().replace("-","").replace(":","").replace(" ",""));
		invoiceHead.setPdf(resInv.getPdf_url());
		invoiceService.saveInvoice(invoiceHead);
		
		que.setRtewm(resInv.getEwm());
		que.setRtfphm(resInv.getFp_hm());
		que.setRtfpdm(resInv.getFp_dm());
		que.setRtskm(resInv.getSkm());
		que.setRtjym(resInv.getFwm());
		que.setRtkprq(resInv.getKprq().replace("-","").replace(":","").replace(" ",""));
		que.setIqpdf(resInv.getPdf_url());
		
		//针对深圳易票云航信特别作法：因为开具成功后，结果中已有PDF的URL
		que.setIqstatus(40);
		inqueDao.updateTo40(que);
		que.setIqstatus(50);
		inqueDao.updateTo50(que);
	}

	/**
	 * 空白票查询
	 * @param que
	 * @param taxinfo
	 */
	@Override
	public RtInvoiceBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		return null;
	}
	
	/**
	 * 空白发票作废
	 * **/
	@Override
	public RtInvoiceBean invoiceInblankValid(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		return null;
	}
	
	/**
	 * 已开发票作废
	 * **/
	@Override
	public RtInvoiceBean invoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		log.info("佳兆业易票云接口：请使用红冲功能进行作废!");
		rtn.setCode(-1);
		rtn.setMessage("请使用红冲功能进行作废!");
		return null;
	}
	
	/**
	 * 发票打印
	 * **/
	@Override
	public void invoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		return;
	}
	
	/**
	 * 获取发票PDF连接 
	 * **/
	@Override
	public String getPdf(Invque invque,RtnData rtn)
	{
		return null;
	}
	
	/**
	 * 发票查询
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	@Override
	public RtInvoiceBean findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		try {
			RtInvoiceBean invoiceBean = new RtInvoiceBean();
			
			//获取Token
			SzYiPiaoYunGenerateBean ypyHX = new SzYiPiaoYunGenerateBean();
			
			String strToken = ypyHX.getToken(taxinfo.getItfserviceUrl() + tokenURL, taxinfo.getItfskpbh(), taxinfo.getItfskpkl());
			if (strToken == null || strToken.length() == 0)
			{
				log.info("佳兆业易票云接口：获取token值失败[发票查询]!");
				rtn.setCode(-1);
				rtn.setMessage("获取token值失败!");
				return null;
			}
			
			//请求报文类
			YiPiaoYunFindRequestBean findBean = new YiPiaoYunFindRequestBean();
			
			//小票号
			findBean.setOrderNo(invque.getIqseqno());	
			//发票流水号（第三方）
			findBean.setSerialNo(invque.getRtskm());	
			//发票代码
			findBean.setInvoiceCode(invque.getRtfpdm());
			//发票号码
			findBean.setInvoiceNum(invque.getIqyfphm());	
			//发票号码
			findBean.setInvoiceNumEnd(invque.getIqyfphm());	
			//1:蓝票,2:红票
			findBean.setInvoiceType(invque.getIqtype()==0?"1":"2");	
			//N:未开,F:失败,A:已开,R:已红冲
			findBean.setStatus(invque.getIqtype()==0?"A":"R");
			//开票日期
			findBean.setInvoiceDate(invque.getRtkprq());	
			//开票日期
			findBean.setInvoiceDateEnd(invque.getRtkprq());	
			//显示行数
			findBean.setRp("10");
			//第几页开始
			findBean.setPage("1");									
			
			//设置查询接口报文Header值
			Map<String, String> map = new HashMap<String, String>();
			map.put("Accept", "application/json;charset=UTF-8");
			map.put("Authorization", strToken);
			map.put("taxNo", taxinfo.getTaxno());
			map.put("machineNo", "0");
			
			String strResult = ypyHX.sendHttpRequest(taxinfo.getItfserviceUrl() + findURL,findBean.getRequestText(),map);
			
			if (strResult == null || strResult.length() == 0) 
			{
				log.info("佳兆业易票云接口：获取发票查询结果失败!");
				rtn.setCode(-1);
				rtn.setMessage("获取发票查询结果失败!");
				return null;
			}
			
			if (strResult.startsWith("[ERROR]") || strResult.startsWith("[FATAL]"))
			{
				log.info("佳兆业易票云接口：" + strResult.substring(7));
				rtn.setCode(-1);
				rtn.setMessage(strResult.substring(7));
				return null;
			}

			YiPiaoYunFindResponseBean findReponse = JSON.parseObject(strResult, YiPiaoYunFindResponseBean.class);
			
			if (findReponse != null)
			{
				if (findReponse.getOperatecode().equalsIgnoreCase("S"))
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					invoiceBean.setKprq(sdf.format(new Date(Long.valueOf(findReponse.getDatas().get(0).getInvoice().getInvoicedate()))));
					invoiceBean.setPdf_url(findReponse.getDatas().get(0).getInvoice().getPdfurl());
					invoiceBean.setFp_dm(findReponse.getDatas().get(0).getInvoice().getInvoicecode());
					invoiceBean.setFp_hm(findReponse.getDatas().get(0).getInvoice().getInvoicenum());
					invoiceBean.setJym(findReponse.getDatas().get(0).getInvoice().getCheckcode());
					invoiceBean.setSkm(findReponse.getDatas().get(0).getInvoice().getSerialno());
				}
				else
				{
					log.info("佳兆业易票云接口：" + findReponse.getMessage());
					rtn.setCode(-1);
					rtn.setMessage(findReponse.getMessage());
					
					return null;
				}
			}
			else
			{
				log.info("佳兆业易票云接口：序列化发票查询结果失败!");
				rtn.setCode(-1);
				rtn.setMessage("序列化发票查询结果失败!");
				
				return null;
			}
			
			return invoiceBean;
		}
		catch (Exception ex)
		{
			log.info("佳兆业易票云接口：" + ex.toString());
			rtn.setCode(-1);
			rtn.setMessage(ex.toString());
			
			return null;
		}
	}
	
	//开具电票
	public RtInvoiceBean openInvoiceDP(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList, RtnData rtn) 
	{
		try {
			String openJson = "";
			double totalJE = 0,totalTax = 0;
			String url = invque.getIqtype() == 0 ? openURL : redURL;

			RtInvoiceBean invoiceBean = new RtInvoiceBean();
			
			//获取Token
			SzYiPiaoYunGenerateBean ypyHX = new SzYiPiaoYunGenerateBean();
					
			String strToken = ypyHX.getToken(taxinfo.getItfserviceUrl() + tokenURL, taxinfo.getItfskpbh(), taxinfo.getItfskpkl());
			if (strToken == null || strToken.length() == 0)
			{
				log.info("佳兆业易票云接口：获取token值失败[发票开具]!");
				rtn.setCode(-1);
				rtn.setMessage("获取token值失败!");
				return null;
			}
			
			//直接开票
			if (invque.getIqtype() == 0)
			{
				YiPiaoYunOpenRequestBean openBean = new YiPiaoYunOpenRequestBean();
				
				//购方地址
				openBean.setBuyeraddr(invque.getIqgmfadd());		
				//购方银行名称
				openBean.setBuyerbankname(invque.getIqgmfbank());	
				//购方银行账号
				openBean.setBuyerbanknum("");			
				//购方邮件地址
				openBean.setBuyeremail(invque.getIqemail());	
				//购方手机号码
				openBean.setBuyermobile("");		
				//购方名称
				openBean.setBuyername(invque.getIqgmfname());	
				//购方省份
				openBean.setBuyerprovince("");				
				//购方税号
				openBean.setBuyertaxno(invque.getIqgmftax());	
				//购方电话
				openBean.setBuyertele(invque.getIqtel());		
				//01:单位；03：个人
				openBean.setBuyertype(invque.getIqgmftax()==null||invque.getIqgmftax().length()==0?"03":"01"); 
				//开票人
				openBean.setDrawer(invque.getIqadmin());	
				//机器号
				openBean.setMachineno("");		
				//主开商品名
				openBean.setMajoritems("");							

				List<YiPiaoYunManualorderdetails> myDetailList = new ArrayList<YiPiaoYunManualorderdetails>();
				
				for (InvoiceSaleDetail itemData :detailList)
				{
					totalJE = totalJE + itemData.getAmt();
					totalTax = totalTax + itemData.getTaxfee();
					
					YiPiaoYunManualorderdetails myDetail = new YiPiaoYunManualorderdetails();
					
					//金额
					myDetail.setAmount(itemData.getAmount().toString()); 		
					//默认为1
					myDetail.setDatamark(1);
					//0:正常  1:折扣行  2:被折扣行
					myDetail.setInvoicenature(0);
					//商品名称
					myDetail.setItemname(itemData.getGoodsname());
					//商品数量
					myDetail.setItemnum(itemData.getQty().toString());
					//商品单价
					myDetail.setItemprice(itemData.getPrice().toString());
					//商品税目
					myDetail.setItemtaxcode(itemData.getTaxitemid());	
					//商品单位
					myDetail.setItemunit(itemData.getUnit());
					//零税率标识    空：非零税率， 1： 免税，2：不征税， 3 普通零税率 
					myDetail.setLslbs(itemData.getZerotax());	
					//行号
					myDetail.setRownum(itemData.getRowno());	
					//规格
					myDetail.setSpecmode(itemData.getSpec());	
					//税额
					myDetail.setTax(itemData.getTaxfee().toString());	
					//1:含税  0:不含税
					myDetail.setTaxincluded("0");							
					//税率
					myDetail.setTaxrate(itemData.getTaxrate()>0?itemData.getTaxrate().toString():"0");	
					//是否享受优惠政策 0：否，1：是
					myDetail.setYhzcbs(itemData.getTaxpre());	
					//享受优惠政策内容
					myDetail.setZzstsgl(itemData.getTaxprecon());								
					
					myDetailList.add(myDetail);
				}
				
				//商品明细
				openBean.setManualorderdetails(myDetailList);
				//总不含税金额
				openBean.setNotaxamount(String.valueOf(totalJE - totalTax));
				//订单号
				openBean.setOrderno(invque.getIqseqno());
				//收款人
				openBean.setPayee(invque.getIqpayee());	
				//备注
				openBean.setRemarks("");	
				//复核人
				openBean.setReviewer(invque.getIqchecker());
				//销方开户行地址及账号
				openBean.setSellerbankacc(taxinfo.getTaxbank());
				//总税额
				openBean.setTaxtotal(String.valueOf(totalTax));	
				//总价税合计
				openBean.setTotalamounttax(String.valueOf(totalJE));		
				
				//开票内容转JSON
				openJson = JSONObject.toJSONString(openBean);
			}
			else //1:红字、2:作废
			{
				Map<String,Object> map = new NewHashMap<String,Object>();
				
				map.put("fpDM", invque.getIqyfpdm());
				map.put("fpHM", invque.getIqyfphm());
				
				List<InvoiceHead> headList = invoiceService.getInvoiceHeadByYfp(map);
				
				if (headList == null || headList.isEmpty())
				{
					log.info("佳兆业易票云接口：红冲或作废时没有查到原发票号!");
					rtn.setCode(-1);
					rtn.setMessage("红冲或作废时没有查到原发票号!");
					return null;
				}
				
				openJson = "serialNo=" + headList.get(0).getFpskm();
			}
			
			//判一下报文内容不可为空
			if (openJson == null || openJson.length() ==0)
			{
				log.info("佳兆业易票云接口：请求报文内容为空!");
				rtn.setCode(-1);
				rtn.setMessage("请求报文内容为空!");
				return null;				
			}
			
			//设置开票接口报文Header值
			Map<String, String> map = new HashMap<String, String>();
			map.put("Accept", "application/json;charset=UTF-8");
			map.put("Authorization", strToken);
			map.put("taxNo", taxinfo.getTaxno());
			
			//只有是开具发票时增加此项
			if (invque.getIqtype() == 0) 
				map.put("Content-Type","application/json");
			else
				map.put("Content-Type","application/x-www-form-urlencoded");

			//调用直接开票接口
			String strResult = ypyHX.sendHttpRequest(taxinfo.getItfserviceUrl() + url, openJson, map);
			
			if (strResult == null || strResult.length() == 0) 
			{
				log.info("佳兆业易票云接口：获取开票结果失败!");
				rtn.setCode(-1);
				rtn.setMessage("获取开票结果失败!");
				return null;
			}
			
			if (strResult.startsWith("[ERROR]") || strResult.startsWith("[FATAL]"))
			{
				log.info("佳兆业易票云接口：" + strResult.substring(7));
				rtn.setCode(-1);
				rtn.setMessage(strResult.substring(7));
				return null;
			}
			
			if (invque.getIqtype() == 0)
			{
				YiPiaoYunOpenResponseBean openResponse = JSON.parseObject(strResult, YiPiaoYunOpenResponseBean.class);
				
				if (openResponse != null)
				{
					if (openResponse.getOperatecode().equalsIgnoreCase("S"))
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						invoiceBean.setKprq(sdf.format(new Date(Long.valueOf(openResponse.getDatas().getInvoicedate()))));
						invoiceBean.setPdf_url(openResponse.getDatas().getPdfurl());
						invoiceBean.setFp_dm(openResponse.getDatas().getInvoicecode());
						invoiceBean.setFp_hm(openResponse.getDatas().getInvoicenum());
						invoiceBean.setJym(openResponse.getDatas().getOrderobjid());
						invoiceBean.setSkm(openResponse.getDatas().getSerialno());
					}
					else
					{
						log.info("佳兆业易票云接口：" + openResponse.getMessage());
						rtn.setCode(-1);
						rtn.setMessage(openResponse.getMessage());
						
						return null;
					}
				}
				else
				{
					log.info("佳兆业易票云接口：调用直接开票接口结果失败!");
					rtn.setCode(-1);
					rtn.setMessage("调用直接开票接口结果失败!");
					
					return null;
				}
			}
			else
			{
				YiPiaoYunRedResponseBean redResponse = JSON.parseObject(strResult,YiPiaoYunRedResponseBean.class);
				
				if (redResponse != null)
				{
					if (redResponse.getOperateCode().equalsIgnoreCase("S"))
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
						invoiceBean.setKprq(sdf.format(new Date(Long.valueOf(redResponse.getDatas().getInvoiceDate()))));
						invoiceBean.setPdf_url(redResponse.getDatas().getPdfUrl());
						invoiceBean.setFp_dm(redResponse.getDatas().getInvoiceCode());
						invoiceBean.setFp_hm(redResponse.getDatas().getInvoiceNum());
						invoiceBean.setJym(redResponse.getDatas().getCheckCode());
						invoiceBean.setSkm(String.valueOf(redResponse.getDatas().getSerialNo()));
					}
					else
					{
						log.info("佳兆业易票云接口：" + redResponse.getMessage());
						rtn.setCode(-1);
						rtn.setMessage(redResponse.getMessage());
						
						return null;
					}
				}
				else
				{
					log.info("佳兆业易票云接口：序列化发票作废结果失败!");
					rtn.setCode(-1);
					rtn.setMessage("序列化发票作废结果失败!");
					
					return null;
				}
			}
		
			return invoiceBean;
		}
		catch (Exception ex)
		{
			log.info("佳兆业易票云接口：" + ex.toString());
			rtn.setCode(-1);
			rtn.setMessage(ex.toString());
			
			return null;
		}
	}
}
