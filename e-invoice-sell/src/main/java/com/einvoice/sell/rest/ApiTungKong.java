package com.einvoice.sell.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.einvoice.dynamicdb.DBMultiDataSource;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.config.TKConfig;
import com.einvoice.sell.dao.TKDao;
import com.einvoice.sell.tungkong.InvoicePrivate;
import com.einvoice.sell.tungkong.InvoiceReqDetail;
import com.einvoice.sell.tungkong.InvoiceRequest;
import com.einvoice.sell.util.Convert;
import com.einvoice.sell.util.MathCal;
import com.einvoice.sell.util.NewHashMap;
import com.einvoice.sell.util.SpringContextUtil;

@Controller
@RequestMapping(value = "/api")
public class ApiTungKong {
	private final Log log = LogFactory.getLog(ApiTungKong.class);
	
	/**
	 * 获取小票明细
	 * 针对东港瑞宏 on 2018.11.08 by zhaomain
	 * @param entid
	 * @return
	 * @throws ServletException,IOException 
	 */
	@Autowired
	TKDao dao;
	
	@RequestMapping(value = "/TicketDetail",method = RequestMethod.POST)
	public void getTicketDetail(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException 
	{
		String keyPassport = com.einvoice.sell.config.TKConfig.getProperties("config.properties", "tkpasswd", "UITN25DZFPC222IM");;
		
		//用于记录请求内容
		String strTaxNo = "",strSheetId = "";
		InvoiceRequest myRequest = new InvoiceRequest();
		InvoiceReqDetail myReqDetial = new InvoiceReqDetail();
	
		try {
			//获取XML报文
			String tmpStr = "", reqStr = "";
			
			BufferedReader br = request.getReader();
			while((tmpStr = br.readLine()) != null)
			{
				reqStr += tmpStr;
			}
			
			if (reqStr == null || reqStr.isEmpty()) 
			{
				throw new RuntimeException("获取请求报文为空！");
			}
			
			//记录日志
			log.info("获取请求报文：" + reqStr);
			
			//反序列化
			myRequest = InvoicePrivate.xmlToBean(reqStr,myRequest);
			if (myRequest == null) 
			{
				throw new RuntimeException("请求报文反序列化为空！");
			}
			
			//记录税号
			strTaxNo = myRequest.getTaxNo();
			if (strTaxNo == null | strTaxNo.isEmpty()) 
			{
				strTaxNo = "";
				throw new RuntimeException("税号不可为空！");
			}

			//判断明细是否为空
			if (myRequest.getConTent() == null || myRequest.getConTent().isEmpty())
			{
				throw new RuntimeException("报文明细数据为空！");
			}
			
			//解密明细数据
			String strDetail = InvoicePrivate.AES_Decrypt(keyPassport, myRequest.getConTent());
			if (strDetail == null)
			{
				throw new RuntimeException("解密后明细数据为空！");
			}
			
			//记录日志
			log.info("明细报文：" + strDetail);

			//反序列化
			myReqDetial = InvoicePrivate.xmlToBean(strDetail,myReqDetial);
			if (myReqDetial == null) 
			{
				throw new RuntimeException("明细数据反序列化为空！");
			}
			
			//记录小票号
			strSheetId = myReqDetial.getSheetId();
			if (strSheetId == null || strSheetId.isEmpty())
			{
				strSheetId = "";
				throw new RuntimeException("小票号不可为空！");
			}
			
			//企业号不可为空
			if(myReqDetial.getEntId()==null || myReqDetial.getEntId().isEmpty())
			{
				throw new RuntimeException("企业号不可为空！");
			}
			
			//终端号不可为空
			if(myReqDetial.getClentId()==null || myReqDetial.getClentId().isEmpty())
			{
				throw new RuntimeException("终端号不可为空！");
			}
			
			//门店不可为空
			if(myReqDetial.getShopId()==null || myReqDetial.getShopId().isEmpty())
			{
				throw new RuntimeException("门店号不可为空！");
			}
			
			//数据连接配置信息
			SpringContextUtil.getBean(TKConfig.class);
			
			//校验
			if(!TKConfig.entid.equals(myReqDetial.getEntId())
		     ||!TKConfig.clientid.equals(myReqDetial.getClentId()))
			{
				throw new RuntimeException("企业号或终端号不正确！");
			}
			
			//动态门店数据连接源
			ShopConnect shop = null;
			DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
			
			shop = TKConfig.shopConnectMap.get(myReqDetial.getShopId());
			
			//门店没有定义
			if(shop==null)
			{
				throw new RuntimeException("门店：[" + myReqDetial.getShopId() + "]没有定义！");
			}
			else
			{
				datasource.switchDataSource(shop);
				shop.setLastActiveDate(Convert.getNowString());
				shop.setQueryCount(shop.getQueryCount()+1);
				shop.setLastMsg("sheetname=tk;sheetid="+myReqDetial.getSheetId());
			}
			
			//获取小票数据
			Map<String, String> p = new NewHashMap<String, String>();
			
			p.put("entid",myReqDetial.getEntId());
			p.put("sheetid",myReqDetial.getSheetId());
			p.put("shopid",myReqDetial.getShopId());
			
			//小票头
			List<Map<String, Object>> headList = dao.getHead(p);
			if (headList == null) 
			{
				throw new RuntimeException("没有发现数据！");
			}
			
			if(headList.size() > 1) 
			{
				throw new RuntimeException("小票数据重复！" );
			}
			
			//校验税号是否一致
			if (!myRequest.getTaxNo().equals(myReqDetial.getTaxNo()) 
			 || !myRequest.getTaxNo().equals(headList.get(0).get("taxno").toString()))
			{
				throw new RuntimeException("税号不匹配！" );
			}
			
			//小票明细
			List<Map<String, Object>> selldetail = dao.getDetail(p);
			if (selldetail == null)
			{
				throw new RuntimeException("小票明细信息缺失！");
			}
			
			//商品名称字符集处理
			String charcode = shop.getDbcharcode();
			if(!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode))
			{
				for (Map<String, Object> map : selldetail) 
				{
					Object obj = map.get("itemname");
					
					if(obj!=null)
					{
						 try
						 {
							map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
						 } 
						 catch (UnsupportedEncodingException e) 
						 {
							log.error(e);
							throw new RuntimeException(e.getMessage());
						 }
					}

					obj = map.get("unit");
					if(obj!=null)
					{
						try 
						{
							map.put("unit", new String(obj.toString().getBytes(charcode), "GBK"));
						} 
						catch (UnsupportedEncodingException e) 
						{
							log.error(e);
							throw new RuntimeException(e.getMessage());
						}
					 }
				}
			}
			
			//返回正确结果
			StringBuilder strResult = new StringBuilder();
			strResult.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
			         .append("<business id=\"DDCX\">")
			         .append("<RETURNCODE>0000</RETURNCODE>")
			         .append("<RETURNMSG>查询成功</RETURNMSG>")
			         .append("<DDXX>")
			         .append("<NSRSBH>" + headList.get(0).get("taxno").toString() + "</NSRSBH>")
			         .append("<FPQQLSH>" + headList.get(0).get("sheetid").toString() + "</FPQQLSH>")
			         .append("<MDBH>" + headList.get(0).get("shopid").toString() + "</MDBH>")
			         .append("<DDRQ>" + (new SimpleDateFormat("yyyyMMdd")).format(headList.get(0).get("sdate")) + "</DDRQ>")
			         .append("</DDXX>")
			         .append("<XMXXS>");
			
			//按小票的商品明细进行循环
			for (Map<String, Object> line : selldetail) 
			{
				double taxrate = Double.parseDouble(line.get("taxRate").toString());
				double retamt = Double.parseDouble(line.get("amt").toString());
				double retqty = Double.parseDouble(line.get("qty").toString());
				if(retqty==0) retqty=1;
			
				strResult.append("<XMXX>")
					     .append("<XH>" + line.get("rowno").toString() + "</XH>")
					     .append("<XMMC>" + line.get("itemname").toString() + "</XMMC>")
					     .append("<SPBM>" + line.get("itemid").toString() + "</SPBM>")
					     .append("<SL>" + String.valueOf(MathCal.div(taxrate, 1, 2)) + "</SL>")
					     .append("<XMJE>" + String.valueOf(MathCal.div(retamt, 1, 2)) + "</XMJE>")
					     .append("<XMDJ>" + String.valueOf(MathCal.div(retamt, retqty, 2)) + "</XMDJ>")
					     .append("<XMSL>" + String.valueOf((int)MathCal.div(retqty, 1, 0)) + "</XMSL>")
					     .append("<FPHXZ>0</FPHXZ>")
						 .append("</XMXX>");
			}
			
			strResult.append("</XMXXS>").append("</business>");
			
			//返回结果
			response.setContentType("application/xml;charset=utf-8"); 
			response.setCharacterEncoding("UTF-8"); 
			response.getWriter().print(strResult.toString()); 
			
			return;
		} 
		catch (Exception ex) 
		{
			String tmpError = ex.getMessage();
			
			if (tmpError == null || tmpError.isEmpty() || tmpError.equals("null"))
			{
				tmpError = "解密出错或发生未知错误！";
			}
			
			//记录错误日志
			log.error(tmpError);
			
			//返回错误信息
			try 
			{
				response.setContentType("application/xml;charset=utf-8"); 
				response.setCharacterEncoding("UTF-8"); 
				response.getWriter().print(InvoicePrivate.ResponseXML(strTaxNo,strSheetId,tmpError)); 
			}
			catch (Exception nx)
			{
				log.error(nx);
			}
			
			return;
		}
	}
}
