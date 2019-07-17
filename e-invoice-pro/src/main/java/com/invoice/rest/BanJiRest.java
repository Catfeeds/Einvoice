package com.invoice.rest;

import java.io.StringReader;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.invoice.port.nbbanji.invoice.bean.HeaderReceiveBean;
import com.invoice.port.nbbanji.invoice.bean.InvoiceResultReceiveBean;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.port.nbbanji.invoice.bean.ErrorResponseBean;
import com.invoice.port.nbbanji.invoice.bean.OrderQueryReceiveBean;
import com.invoice.port.nbbanji.invoice.bean.ResponseBillInfoBJ;
import com.invoice.port.nbbanji.invoice.service.BanJiInvoiceService;
import com.invoice.port.nbbanji.invoice.service.BanJiSheetService;

@Controller
@RequestMapping(value = "/")
public class BanJiRest {
	private final static Log log = LogFactory.getLog(BanJiRest.class);
	private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
	private static final String keyPassport = "SHIJIRETAIL12345";
	
	@Autowired
	BanJiSheetService bjSheetService;
	
	@Autowired
	BanJiInvoiceService bjInvoiceService;
	
	//开票数据获取接口－由航信调用
	@ResponseBody
	@RequestMapping(value = "/bj/getTicketDetail",method = RequestMethod.POST)
	public String getTicketDetail(@RequestBody String data)
	{
		String strFlag = "DDCX", logInfo = "";
		
		ErrorResponseBean errorResponse = new ErrorResponseBean();
		
		try {
			//记录日志
			log.info("获取请求报文：" + data);
			
			HeaderReceiveBean myRequestHead = new HeaderReceiveBean();
			
			myRequestHead = xmlToBean(data,myRequestHead);
			
			if (myRequestHead == null) {
				logInfo = "订单查询请求进行反序列化出错";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			if (myRequestHead.getTaxNo()==null || myRequestHead.getTaxNo().length()==0){
				logInfo = "订单查询传入税号为空";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}

			if (myRequestHead.getConTent()==null || myRequestHead.getConTent().length()==0){
				logInfo = "订单查询传入数据为空";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			String reqData = AES_Decrypt(keyPassport,myRequestHead.getConTent());
			
			log.info("解密后报文：" + reqData);
			
			OrderQueryReceiveBean myReqDetail = new OrderQueryReceiveBean();

			myReqDetail = xmlToBean(reqData,myReqDetail);
			
			if (myReqDetail == null){
				logInfo = "订单内容反序列化出错";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			if (myReqDetail.getEntId()==null || myReqDetail.getEntId().trim().length()==0 ||
				myReqDetail.getShopId()==null || myReqDetail.getShopId().trim().length()==0 ||
				myReqDetail.getTaxNo()==null || myReqDetail.getTaxNo().trim().length()==0)
			{
				logInfo = "企业、门店、税号不可为空";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);

				return errorResponse.toString(strFlag);
			}

			if (!myRequestHead.getTaxNo().equalsIgnoreCase(myReqDetail.getTaxNo())){
				logInfo = "订单税号校验不通过";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			if (!myReqDetail.getDpType().equalsIgnoreCase("纸票") && !myReqDetail.getDpType().equalsIgnoreCase("电票")){
				logInfo = "发票类型不是纸票也不是电票";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
					
			// 退货标志不允许为空
			if (myReqDetail.getRetFlag() == null || (!myReqDetail.getRetFlag().equals("0") && !myReqDetail.getRetFlag().equals("1"))) {
				logInfo = "小票退货标志只能为0或1";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
					
				return errorResponse.toString(strFlag);
			}

			// 只查询所有退货小票或纸票时，需要判断日期是否合法
			if (myReqDetail.getRetFlag().equals("1") || myReqDetail.getDpType().equalsIgnoreCase("纸票"))
			{
				if (myReqDetail.getDdRQq() == null || myReqDetail.getDdRQq().trim().length() == 0 ||
					myReqDetail.getDdRQz() == null || myReqDetail.getDdRQz().trim().length() == 0)
				{
					logInfo = "查询退货小票或纸票时，查询日期段不允许为空";
					log.info(logInfo);
					errorResponse.setRETURNCOD("9999");
					errorResponse.setRETURNMSG(logInfo);
						
					return errorResponse.toString(strFlag);
				}
				
				if (!isValidDate(myReqDetail.getDdRQq()) || !isValidDate(myReqDetail.getDdRQz()))
				{
					logInfo = "查询退货小票或纸票时，日期必须合法。DDRQQ:" + myReqDetail.getDdRQq() + ";DDRQZ:" + myReqDetail.getDdRQz();
					log.info(logInfo);
					errorResponse.setRETURNCOD("9999");
					errorResponse.setRETURNMSG(logInfo);
						
					return errorResponse.toString(strFlag);
				}
			}
			
			//电票
			if (myReqDetail.getDpType().equalsIgnoreCase("电票") && myReqDetail.getRetFlag().equals("0")){
				if (myReqDetail.getClientId()==null || myReqDetail.getClientId().trim().length()==0)
				{
					logInfo = "电票－终端号不可为空";
					log.info(logInfo);
					errorResponse.setRETURNCOD("9999");
					errorResponse.setRETURNMSG(logInfo);
						
					return errorResponse.toString(strFlag);
				}
				
				if (myReqDetail.getSheetId()==null || myReqDetail.getSheetId().trim().length()==0)
				{
					logInfo = "电票－小票流水号不可为空";
					log.info(logInfo);
					errorResponse.setRETURNCOD("9999");
					errorResponse.setRETURNMSG(logInfo);
						
					return errorResponse.toString(strFlag);
				}
			}
			
			//针对返回XML给航信
			List<ResponseBillInfoBJ> bills = bjSheetService.getInvoiceSheetInfo(myReqDetail);
			
			if (bills == null || bills.size() == 0)
			{
				logInfo = "获取订单数据为空或已开具发票";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
					
				return errorResponse.toString(strFlag);
			}
			
			StringBuilder myXML = new StringBuilder();
			
			myXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
			     .append("<business id=\"DDCX\">")
			     .append("<RETURNCODE>0000</RETURNCODE>")
			     .append("<RETURNMSG>查询成功</RETURNMSG>");
			
			for (ResponseBillInfoBJ myBill : bills) {
				myXML.append("<DDXXS>")
			         .append("<NSRSBH>"+(myBill.getTaxno()==null?"":myBill.getTaxno())+"</NSRSBH>") //税号
			         .append("<SKT>"+(myBill.getSyjid()==null?"":myBill.getSyjid())+"</SKT>") //收款台
			         .append("<MDBH>"+(myBill.getShopid()==null?"":myBill.getShopid())+"</MDBH>") //门店编号
			         .append("<FPQQLSH>"+(myBill.getSheetid()==null?"":myBill.getSheetid())+"</FPQQLSH>") //小票流水号
			         .append("<XSDJBH>"+(myBill.getBillno()==null?"":myBill.getBillno())+"</XSDJBH>") //销售单据编号
			         .append("<DDRQ>"+(myBill.getTradedate()==null?"":myBill.getTradedate())+"</DDRQ>") //订单日期
			         .append("<DDLX>"+(myReqDetail.getRetFlag()==null?"":myReqDetail.getRetFlag())+"</DDLX>") //订单类型
			         .append("<THBZ>"+(myReqDetail.getRetFlag()==null?"":myReqDetail.getRetFlag())+"</THBZ>") //退货标志
					 .append("<Y_FPQQLSH>"+(myBill.getRefsheetid()==null?"":myBill.getRefsheetid())+"</Y_FPQQLSH>") //原小票流水号
					 .append("<Y_XSDJBH>"+(myBill.getYxsdjbh()==null?"":myBill.getYxsdjbh())+"</Y_XSDJBH>") //原销售单号
					 .append("<Y_SKT>"+(myBill.getYskt()==null?"":myBill.getYskt())+"</Y_SKT>") //原收款台
					 .append("<Y_MDBH>"+(myBill.getYmdbh()==null?"":myBill.getYmdbh())+"</Y_MDBH>") //原门店编号
					 .append("<Y_FPDM>"+(myBill.getYfpdm()==null?"":myBill.getYfpdm())+"</Y_FPDM>") //原发票代码
					 .append("<Y_FPHM>"+(myBill.getYfphm()==null?"":myBill.getYfphm())+"</Y_FPHM>") //原发票号码
					 .append("<HZFPXXBBH>"+(myBill.getHzfpxxbbh()==null?"":myBill.getHzfpxxbbh())+"</HZFPXXBBH>") //红字信息表编号
					 .append("<KPLX>"+(myReqDetail.getRetFlag()==null?"":myReqDetail.getRetFlag())+"</KPLX>") //开票类型
					 .append("<KPJH></KPJH>") //开票机号
					 .append("<FPZL>"+(myBill.getInvoicelx()==null?"":myBill.getInvoicelx())+"</FPZL>") //发票种类
					 .append("<GFSH>"+(myBill.getGmftax()==null?"":myBill.getGmftax())+"</GFSH>") //购方税号
					 .append("<GFMC>"+(myBill.getGmfname()==null?"":myBill.getGmfname())+"</GFMC>") //购方名称
					 .append("<GFDZDH>"+(myBill.getGmfadd()==null?"":myBill.getGmfadd())+"</GFDZDH>") //购方地址、电话
					 .append("<GFYHZH>"+(myBill.getGmfbank()==null?"":myBill.getGmfbank())+"</GFYHZH>") //购方银行、账号
					 .append("<XFSH>"+(myBill.getTaxno()==null?"":myBill.getTaxno())+"</XFSH>") //销方税号
					 .append("<XFMC>"+(myBill.getTaxname()==null?"":myBill.getTaxname())+"</XFMC>") //销方名称
					 .append("<XFDZDH>"+(myBill.getTaxadd()==null?"":myBill.getTaxadd())+"</XFDZDH>") //销方地址、电话
					 .append("<XFYHZH>"+(myBill.getTaxbank()==null?"":myBill.getTaxbank())+"</XFYHZH>") //销方银行、账号
					 .append("<JSHJ>"+String.valueOf(myBill.getInvoiceamount()==null?0.00:myBill.getInvoiceamount())+"</JSHJ>") //价税合计
					 .append("<HJJE>"+String.valueOf((double)Math.round(((myBill.getInvoiceamount()==null?0.00:myBill.getInvoiceamount())-(myBill.getTotaltaxfee()==null?0.00:myBill.getTotaltaxfee()))*100)/100)+"</HJJE>") //合计金额
					 .append("<HJSE>"+String.valueOf(myBill.getTotaltaxfee()==null?0.00:myBill.getTotaltaxfee())+"</HJSE>") //合计税额
					 .append("<BZ>"+(myBill.getRemark()==null?"":myBill.getRemark())+"</BZ>") //备注
					 .append("<KPR>"+(myBill.getKpr()==null?"":myBill.getKpr())+"</KPR>") //开票人
					 .append("<SKR>"+(myBill.getSkr()==null?"":myBill.getSkr())+"</SKR>") //收款人
					 .append("<FHR>"+(myBill.getFhr()==null?"":myBill.getFhr())+"</FHR>") //复核人
					 .append("<XMXXS>");

				for (InvoiceSaleDetail mySale : myBill.getInvoiceSaleDetail())
				{
					if (mySale.getIsinvoice().equals("Y"))
					{
						myXML.append("<XMXX>")
						 	 .append("<FPMXXH>"+(mySale.getRowno()==null?"":mySale.getRowno())+"</FPMXXH>") //序号
						 	 .append("<XMMC>"+(mySale.getGoodsname()==null?"":mySale.getGoodsname())+"</XMMC>") //项目名称
						 	 .append("<SPBM>"+(mySale.getGoodsid()==null?"":mySale.getGoodsid())+"</SPBM>") //商品编码
						 	 .append("<GGXH>"+(mySale.getSpec()==null?"":mySale.getSpec())+"</GGXH>") //规格型号
						 	 .append("<JLDW>"+(mySale.getUnit()==null?"":mySale.getUnit())+"</JLDW>") //计量单位
						 	 .append("<SLV>"+(mySale.getTaxrate()==null?"":mySale.getTaxrate())+"</SLV>") //税率
						 	 .append("<SE>"+(mySale.getTaxfee()==null?"0":mySale.getTaxfee())+"</SE>") //项目税额
						 	 .append("<JE>"+(mySale.getAmount()==null?"0":mySale.getAmount())+"</JE>") //项目金额
						 	 .append("<DJ>"+(mySale.getPrice()==null?"0":mySale.getPrice())+"</DJ>") //项目单价
						 	 .append("<SL>"+(mySale.getQty()==null?"0":mySale.getQty())+"</SL>") //项目数量
						 	 .append("<FLBM>"+(mySale.getTaxitemid()==null?"":mySale.getTaxitemid())+"</FLBM>") //税收分类编码
						 	 .append("<FPHXZ>"+(mySale.getFphxz()==null?"0":mySale.getFphxz())+"</FPHXZ>") //发票行性质
						 	 .append("<XSYH>"+(mySale.getTaxpre()==null?"0":mySale.getTaxpre())+"</XSYH>") //是否享受优惠
						 	 .append("<YHSM>"+(mySale.getTaxprecon()==null?"":mySale.getTaxprecon())+"</YHSM>") //优惠说明
						 	 .append("<LSLVBS>"+(mySale.getZerotax()==null?"":mySale.getZerotax())+"</LSLVBS>") //零税率标识
						 	 .append("</XMXX>");
					}
				}
				
				myXML.append("</XMXXS>").append("</DDXXS>");
			}
			
			myXML.append("</business>"); 
			
			log.info(myXML.toString());
			
			return myXML.toString();
		}
		catch(Exception ex){
			log.error(ex.toString());			
			errorResponse.setRETURNCOD("9999");
			errorResponse.setRETURNMSG(ex.getMessage());
			
			return errorResponse.toString(strFlag);
		}
	}
	
	//发票信息回写－由航信调用
	@ResponseBody
	@RequestMapping(value = "/bj/setInvoiceInfo",method = RequestMethod.POST)
	public String setInvoiceInfo(@RequestBody String data)
	{
		String strFlag = "DDHX", logInfo = "";
		
		ErrorResponseBean errorResponse = new ErrorResponseBean();
		
		try {
			//记录日志
			log.info("获取请求报文：" + data);
			
			HeaderReceiveBean myRequestHead = new HeaderReceiveBean();
			
			myRequestHead = xmlToBean(data,myRequestHead);
			
			if (myRequestHead == null) {
				logInfo = "订单查询请求进行反序列化出错";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			if (myRequestHead.getTaxNo()==null || myRequestHead.getTaxNo().length()==0){
				logInfo = "订单查询传入税号为空";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			
			if (myRequestHead.getConTent()==null || myRequestHead.getConTent().length()==0){
				logInfo = "订单查询传入数据为空";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			String reqData = AES_Decrypt(keyPassport,myRequestHead.getConTent());
			
			if (reqData == null || reqData.trim().length() == 0)
			{
				logInfo = "解密数据出错，请核查！";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			log.info("解密后报文：" + reqData);
			
			InvoiceResultReceiveBean myReceive = new InvoiceResultReceiveBean();
			
			myReceive = xmlToBean(reqData,myReceive);
			
			if (myReceive == null){
				logInfo = "开票内容反序列化出错";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			if (myReceive.getZDH()==null || myReceive.getZDH().trim().length()==0 ||
				myReceive.getQYH()==null || myReceive.getQYH().trim().length()==0 ||
				myReceive.getMDBH()==null || myReceive.getMDBH().trim().length()==0 ||
				myReceive.getNSRSBH()==null || myReceive.getNSRSBH().trim().length()==0 ||
				myReceive.getFPQQLSH()==null || myReceive.getFPQQLSH().trim().length()==0)
			{
				logInfo = "企业、终端、门店、小票号不可为空";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
					
				return errorResponse.toString(strFlag);
			}
			
			if (!myRequestHead.getTaxNo().equalsIgnoreCase(myReceive.getNSRSBH())){
				logInfo = "查询订单税号校验不通过";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
			
			if (!myReceive.getDPZPBZ().equalsIgnoreCase("纸票") && !myReceive.getDPZPBZ().equalsIgnoreCase("电票")){
				logInfo = "发票类型不是纸票也不是电票";
				log.info(logInfo);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(logInfo);
				
				return errorResponse.toString(strFlag);
			}
		
			String strResult = bjInvoiceService.setInvoiceInvqueInfo(myReceive);
			
			if (!strResult.equalsIgnoreCase("OK"))
			{
				log.info(strResult);
				errorResponse.setRETURNCOD("9999");
				errorResponse.setRETURNMSG(strResult);
				
				return errorResponse.toString(strFlag);
			}
			
			//如果是成功的则返回
			errorResponse.setRETURNCOD("0000");
			errorResponse.setRETURNMSG("订单开具结果回写成功");
			errorResponse.setFPQQLSH(myReceive.getFPQQLSH());
			
			return errorResponse.toString(strFlag);
		}
		catch(Exception ex){
			log.error(ex.toString());			
			errorResponse.setRETURNCOD("9999");
			errorResponse.setRETURNMSG(ex.getMessage());
			
			return errorResponse.toString(strFlag);
		}
	}
	
	private static String AES_Decrypt(String keyStr, String encryptData) 
    {
        byte[] decrypt = null; 
        try
        { 
            Key key = generateKey(keyStr); 
            
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            
            cipher.init(Cipher.DECRYPT_MODE, key); 
            
            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData)); 
            
            return new String(decrypt,"utf-8").trim(); 
        }
        catch(Exception e)
        { 
            e.printStackTrace(); 
            
            return "";
        }
    } 
	
	private static Key generateKey(String key) throws Exception
    { 
        try
        {            
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES"); 
            
            return keySpec; 
        }
        catch(Exception e)
        { 
            e.printStackTrace(); 
            return null; 
        } 
    }

	@SuppressWarnings("unchecked")
	private static <T> T xmlToBean(String xml, T t) 
	{
	  try
	  {
		  JAXBContext context = JAXBContext.newInstance(t.getClass());  
	    
		  Unmarshaller um = context.createUnmarshaller();  
	      
		  StringReader sr = new StringReader(xml);  
	      
		  t = (T) um.unmarshal(sr);  
	      
		  return t;
	  }
	  catch (Exception ex)
	  {
		  log.error(ex.getMessage());
	   	
		  return null;
	  }
	} 
	
	public static boolean isValidDate(String strDate) {
		boolean convertSuccess = true;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		
		try {
			format.setLenient(false);
			format.parse(strDate);
		} catch (Exception e) {
			convertSuccess=false;
		} 
		
		return convertSuccess;
	}
}
