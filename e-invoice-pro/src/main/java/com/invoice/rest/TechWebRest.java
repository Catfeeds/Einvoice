package com.invoice.rest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCallBackAcceptBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCallBackReturnBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCardAuthurlRequestBaean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCardCancelRequestBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCardInsertRequestBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebCardModelRequestBean;
import com.invoice.port.sztechweb.invoice.service.SzTechWebGenerateImpl;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.util.NewHashMap;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/")
public class TechWebRest {
	private final Log log = LogFactory.getLog(TechWebRest.class);
	
	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	InvqueDao inqueDao;
	
	@Autowired
	EntPrivatepara entPrivatepara;
	
	@Autowired
	EnterpriseService enterpriseService;
	
	@Autowired
	TaxinfoDao taxinfoDao;
	
	@RequestMapping(value = "/techweb/billcallback")
	@ResponseBody
	public String TechWebBackWriteInvoice(@RequestBody String data)
	{
		log.info(data);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SzTechWebCallBackReturnBean callBack = new SzTechWebCallBackReturnBean();
		
		try {
			if (data == null || data.length() == 0)
			{
				callBack.setCode(-1);
				callBack.setMsg("发票数据回写内容为空!");
				callBack.setData(null);
				log.info("发票数据回写内容为空!");
				return callBack.toString();
			}
		
			SzTechWebCallBackAcceptBean queryReponse = (SzTechWebCallBackAcceptBean)JSONObject.toBean(JSONObject.fromObject(data), SzTechWebCallBackAcceptBean.class);
			if (queryReponse.getTicket_status() == 1)
			{
				callBack.setCode(-1);
				callBack.setMsg("开票中的数据请不要乱调!");
				callBack.setData(null);
				log.error("开票中的数据请不要乱调!");
				return callBack.toString();
			}
			
			if (queryReponse.getTicket_status() == 2 && 
			   (queryReponse.getTicket_sn()==null || queryReponse.getTicket_sn().trim().length()==0 ||
			    queryReponse.getTicket_code()==null || queryReponse.getTicket_code().trim().length()==0 ||
			    queryReponse.getPdf_url()==null || queryReponse.getTicket_code().trim().length()==0))
			{
				callBack.setCode(-1);
				callBack.setMsg("发票信息不完整!");
				callBack.setData(null);
				log.error("发票信息不完整!");
				return callBack.toString();
			}
			
			//查出平台原开票信息
			Map<String, Object> p = new NewHashMap<>();
			p.put("iqseqno", queryReponse.getOrder_id());
			List<Invque> queList = inqueDao.getInvque(p);
			
			for (Invque myInvque : queList)
			{
				if (myInvque != null && myInvque.getIqstatus() <= 40)
				{
					//开票失败－必须在此进行处理，否则就不正确，请后面修改注意
					if (queryReponse.getTicket_status() == -2)
					{
						myInvque.setIqstatus(30);
						myInvque.setIqmsg(queryReponse.getMessage()==null?"高灯返回开票失败":queryReponse.getMessage());
						inqueDao.updateForCallGD(myInvque);
					}
					else
					{
						myInvque.setRtskm(queryReponse.getG_unique_id());
						myInvque.setRtkprq(sdf.format(queryReponse.getTicket_date()).replace("-","").replace(":","").replace(" ",""));
						myInvque.setRtfphm(queryReponse.getTicket_sn());
						myInvque.setRtfpdm(queryReponse.getTicket_code());
						myInvque.setIqpdf(queryReponse.getPdf_url());
						myInvque.setRtjym(queryReponse.getCheck_code());
					
						myInvque.setIqstatus(50);
						inqueDao.updateForCallGD(myInvque);

						//如果是红冲票，则移除卡包数据
						if (myInvque.getIqtype() != 0)
						{
							Map<String,String> myMap = new HashMap<String,String>();
							myMap.put("iqentid", myInvque.getIqentid());
							myMap.put("rtfpdm",myInvque.getIqyfpdm());
							myMap.put("rtfphm", myInvque.getIqyfphm());
							
							List<Invque> myList = inqueDao.getInvqueCancelCard(myMap);

							for (Invque myCard : myList)
							{
								TechWebCancelCard(myCard.getRtewm(),myCard.getIqentid(),myCard.getIqtaxno());
							}
						}
					}
				}
			}
			
			//开票失败则视同处理成功
			if  (queryReponse.getTicket_status() == -2)
			{
				callBack.setCode(0);
				callBack.setMsg("处理成功!");
				callBack.setData(null);
				log.info(queryReponse.getMessage()==null?"高灯返回开票失败":queryReponse.getMessage());
				return callBack.toString();
			}
			
			List<InvoiceHead> headList = invoiceService.queryInvoiceHeadByCallGD(p);
			for (InvoiceHead myHead : headList)
			{
				myHead.setFpskm(queryReponse.getG_unique_id());
				myHead.setFprq(sdf.format(queryReponse.getTicket_date()).replace("-","").replace(":","").replace(" ",""));
				myHead.setFphm(queryReponse.getTicket_sn());
				myHead.setFpdm(queryReponse.getTicket_code());
				myHead.setPdf(queryReponse.getPdf_url());
				myHead.setFpjym(queryReponse.getCheck_code());

				myHead.setStatus(100);
				invoiceService.updateInvoiceHeadForCallGD(myHead);
			}

			callBack.setCode(0);
			callBack.setMsg("发票信息回写成功!");
			callBack.setData(null);
			
			return callBack.toString();
		} 
		catch (Exception ex) 
		{
			callBack.setCode(-1);
			callBack.setMsg(ex.getMessage());
			callBack.setData(null);
			log.error(ex.getMessage());
			return callBack.toString();
		}
	}
	
	//微信卡包授权
	@RequestMapping(value = "/techweb/wechatcard")
	@ResponseBody
	public String WechatCard(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.fromObject(data);
			String iqSeqNo = jo.getString("iqseqno");
			
			//获取开票队列
			Invque myInvque = new Invque();
			myInvque.setIqseqno(iqSeqNo);
			myInvque = inqueDao.getInvqueHead(myInvque);	
			if (myInvque == null)
			{
				return new RtnData(-1,"开票信息队列:" + iqSeqNo + "缺失").toString();
			}
			
			//已插卡包标志
			if (myInvque.getIqinvoicetimes() == -1) 
			{
				return new RtnData(-1,"此发票信息已插入微信卡包").toString();
			}
			
			//红冲票不可以插入卡包
			if (myInvque.getIqtype() != 0) 
			{
				return new RtnData(-1,"红冲票不可以插入微信卡包").toString();
			}
			
			String iqEntId = myInvque.getIqentid();
			String iqTaxNo = myInvque.getIqtaxno();
			
			//从参数表中取参数
			String wxAppid = entPrivatepara.get(iqEntId, FGlobal.WeixinAppID);
			String wxCardId = entPrivatepara.get(iqEntId, FGlobal.WeixinCard);
			
			if (wxAppid == null || wxAppid.length() == 0) wxAppid = "";
			
			//获取企业名称
			Enterprise enterprise = new Enterprise();
			enterprise.setEntid(iqEntId);
			Enterprise ent = enterpriseService.getEnterpriseById(enterprise);
			if (ent == null) 
			{
				return new RtnData(-1,"企业:" + iqEntId + "不存在").toString();
			}
			
			//获取税号信息
			Taxinfo myTaxInfo = new Taxinfo();
			myTaxInfo.setEntid(iqEntId);
			myTaxInfo.setTaxno(iqTaxNo);
			myTaxInfo = taxinfoDao.getTaxinfoByNo(myTaxInfo);
			if (myTaxInfo == null || myTaxInfo.getItfserviceUrl() == null || myTaxInfo.getItfserviceUrl().length() == 0) 
			{
				return new RtnData(-1,"税号:" + iqTaxNo + "不存在或高灯URL没有配置").toString();
			}
			
			//调用HTTP/HTTPS
			SzTechWebGenerateImpl mySend = new SzTechWebGenerateImpl();
			
			//生成卡包模板ID
			if (wxCardId == null || wxCardId.length() == 0)
			{
				SzTechWebCardModelRequestBean mycardRequest = new SzTechWebCardModelRequestBean();
				
				mycardRequest.setAppid(wxAppid);
				mycardRequest.setFull_name(ent.getEntname());
				mycardRequest.setShort_name(ent.getAddress());
				mycardRequest.setLogo_url("http://wechat.fapiaoer.cn/upload/fp_logo/wx_logo.jpg");
				
				String request = JSONObject.fromObject(mycardRequest).toString();
				
				Long timestamp = System.currentTimeMillis() / 1000;
						
				String sign = mySend.genereateSign(JSONObject.fromObject(request), timestamp, myTaxInfo.getItfskpbh(), myTaxInfo.getItfskpkl());
				
				String strUrl = myTaxInfo.getItfserviceUrl().trim() + "/card/create-template";
				
				strUrl = strUrl + "?signature=" + sign + "&appkey=" + myTaxInfo.getItfskpbh() + "&timestamp=" + timestamp;

				String strResult = mySend.sendHttpRequest(strUrl, request);
				if (strResult == null || strResult.length() == 0) 
				{
					return new RtnData(-1,"生成卡包模板返回结果为空").toString();
				}

				JSONObject js = JSONObject.fromObject(strResult);
				
				if (js.getInt("code") != 0)
				{
					return new RtnData(-1,js.getString("msg")).toString();
				}
				
				wxCardId = js.getJSONObject("data").getString("card_id");
				
				entPrivatepara.set(iqEntId, FGlobal.WeixinCard, wxCardId);
			}
			
			//获取授权链接
			SzTechWebCardAuthurlRequestBaean myAuthurlRequest = new SzTechWebCardAuthurlRequestBaean();
			
			Long timestamp  = System.currentTimeMillis() / 1000;
			String totalamt = String.valueOf((double)Math.round((myInvque.getIqtotje()+myInvque.getIqtotse())*100)/100);
			String callback = myTaxInfo.getItfjrdm() + "/techweb/cardcallback";
					
			myAuthurlRequest.setAppid(wxAppid);
			myAuthurlRequest.setOrder_id(iqSeqNo);
			myAuthurlRequest.setTimestamp(String.valueOf(timestamp));
			myAuthurlRequest.setSource("web");
			myAuthurlRequest.setRedirect_url("");
			myAuthurlRequest.setType("2");
			myAuthurlRequest.setCallback_url(callback);
			myAuthurlRequest.setMoney(totalamt);
			
			String request = JSONObject.fromObject(myAuthurlRequest).toString();
			
			String sign = mySend.genereateSign(JSONObject.fromObject(request), timestamp, myTaxInfo.getItfskpbh(), myTaxInfo.getItfskpkl());
			
			String strUrl = myTaxInfo.getItfserviceUrl().trim() + "/authorize/authurl";
			
			strUrl = strUrl + "?signature=" + sign + "&appkey=" + myTaxInfo.getItfskpbh() + "&timestamp=" + timestamp;

			String strResult = mySend.sendHttpRequest(strUrl, request);
			if (strResult == null || strResult.length() == 0) 
			{
				return new RtnData(-1,"获取授权链接返回结果为空").toString();
			}
			
			JSONObject js = JSONObject.fromObject(strResult);
			
			if (js.getInt("code") != 0)
			{
				return new RtnData(-1,js.getString("msg")).toString();
			}
			
			String res = js.getJSONObject("data").getString("auth_url");
			
			return new RtnData(0,res).toString();
		} 
		catch (Exception ex) {
			log.error(ex.getMessage());
			return new RtnData(-1, ex.getMessage()).toString();
		}
	}
	
	//微信卡包回调并插卡
	@RequestMapping(value = "/techweb/cardcallback")
	@ResponseBody
	public String CardCallback(@RequestBody String data) {
		
		String succMsg = "{\"code\":0,\"msg\":\"ok\"}";
		String errorMsg = "{\"code\":-1,\"msg\":\"获取到的数据为空\"}";
		
		if (data == null || data.length() == 0)
		{
			log.info("调灯返回的授权结果为空！");
			return errorMsg;
		}
		
		log.info("调灯授权返回结果:"+data);
		
		try {
			JSONObject jo = JSONObject.fromObject(data);
			String order_id = jo.getString("order_id");
			String open_id = jo.getString("open_id");
			
			if (order_id == null || order_id.length() == 0 || open_id == null || open_id.length() == 0)
			{
				log.info("调灯授权返回的order_id为空或open_id为空");
				return errorMsg;
			}
			
			//查找已开票标志
			Invque myInvque = new Invque();
			myInvque.setIqseqno(order_id);
			myInvque = inqueDao.getInvqueHead(myInvque);	
			if (myInvque == null)
			{
				log.info("高灯插卡包："+order_id+"没有发现数据");
				return errorMsg;
			}
			
			//已插卡包标志
			if (myInvque.getIqinvoicetimes() == -1) 
			{
				log.info("高灯插卡包："+order_id+"的发票已经插入卡包");
				return succMsg;
			}
			
			//只有蓝票才需要插卡
			if (myInvque.getIqtype() != 0) 
			{
				log.info("高灯插卡包："+order_id+"的发票不是蓝票");
				return succMsg;
			}
			
			//判断微信open_id是否一致
			/*
			if (!myInvque.getIqperson().equalsIgnoreCase(open_id))
			{
				log.info("高灯插卡包open_id："+open_id+"与发票："+myInvque.getIqperson()+"不匹配");
				return errorMsg;
			}*/
			
			//判断PDF的URL是否存在
			if (myInvque.getIqpdf() == null || myInvque.getIqpdf().trim().length() == 0)
			{
				log.info("高灯插卡包："+order_id+"发票PDF的URL没有发现");
				return errorMsg;
			}
			
			//从参数表中取参数
			String wxAppid = entPrivatepara.get(myInvque.getIqentid(), FGlobal.WeixinAppID);
			String wxCardId = entPrivatepara.get(myInvque.getIqentid(), FGlobal.WeixinCard);
			
			if (wxAppid == null || wxAppid.length() == 0) wxAppid = "";
			
			//获取税号信息
			Taxinfo myTaxInfo = new Taxinfo();
			myTaxInfo.setEntid(myInvque.getIqentid());
			myTaxInfo.setTaxno(myInvque.getIqtaxno());
			myTaxInfo = taxinfoDao.getTaxinfoByNo(myTaxInfo);
			
			//调用HTTP/HTTPS
			SzTechWebGenerateImpl mySend = new SzTechWebGenerateImpl();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			//调用插卡动作
			SzTechWebCardInsertRequestBean myCardInsert = new SzTechWebCardInsertRequestBean();
			myCardInsert.setAppid(wxAppid);
			myCardInsert.setOrder_id(order_id);
			myCardInsert.setProvider_id(wxAppid);
			myCardInsert.setTax_no(myTaxInfo.getTaxno());
			myCardInsert.setTax_name(myTaxInfo.getTaxname());
			myCardInsert.setStore_name("");
			myCardInsert.setIndustry_code("");
			myCardInsert.setBuyer_title_type(myInvque.getIqgmftax()!=null&&myInvque.getIqgmftax().length()>0?"2":"1");
			myCardInsert.setBuyer_title(myInvque.getIqgmfname());
			myCardInsert.setBuyer_address(myInvque.getIqgmfadd());
			myCardInsert.setBuyer_address_phone("");
			myCardInsert.setBuyer_bank_name(myInvque.getIqgmfbank());
			myCardInsert.setBuyer_bank_account("");
			myCardInsert.setBuyer_phone(myInvque.getIqtel());
			myCardInsert.setBuyer_email(myInvque.getIqemail());
			myCardInsert.setTicket_sn(myInvque.getRtfphm());
			myCardInsert.setTicket_code(myInvque.getRtfpdm());
			myCardInsert.setTicket_date(String.valueOf((sdf.parse(myInvque.getRtkprq()).getTime()/1000)));
			myCardInsert.setPdf_url(myInvque.getIqpdf());
			myCardInsert.setFee(String.valueOf((double)Math.round((myInvque.getIqtotje()+myInvque.getIqtotse())*100)/100));
			myCardInsert.setFee_without_tax(String.valueOf(myInvque.getIqtotje()));
			myCardInsert.setTax(String.valueOf(myInvque.getIqtotse()));
			myCardInsert.setCheck_code(myInvque.getRtjym());
			myCardInsert.setCard_id(wxCardId);
			myCardInsert.setGoods_name("明细请查看发票");
			
			String request = JSONObject.fromObject(myCardInsert).toString();
			
			Long timestamp = System.currentTimeMillis() / 1000;
			
			String sign = mySend.genereateSign(JSONObject.fromObject(request), timestamp, myTaxInfo.getItfskpbh(), myTaxInfo.getItfskpkl());
			
			String strUrl = myTaxInfo.getItfserviceUrl().trim() + "/card/create";
			
			strUrl = strUrl + "?signature=" + sign + "&appkey=" + myTaxInfo.getItfskpbh() + "&timestamp=" + timestamp;

			String strResult = mySend.sendHttpRequest(strUrl, request);
			if (strResult == null || strResult.length() == 0) 
			{
				log.info("高灯插卡包:" + order_id + "返回结果为空");
				return errorMsg;
			}
			
			JSONObject js = JSONObject.fromObject(strResult);
			
			if (js.getInt("code") != 0)
			{
				log.info("高灯插卡包:" + js.getString("msg"));
				return errorMsg;
			}
			
			String card_code = js.getJSONObject("data").getString("card_code");
			
			myInvque.setRtewm(card_code);
			myInvque.setIqinvoicetimes(-1);

			inqueDao.updateForCardGD(myInvque);			
		}
		catch(Exception ex)
		{
			errorMsg = "{\"code\":-1,\"msg\":\"" + ex.getMessage() + "\"}";
			log.error(errorMsg);
			return errorMsg;
		}
		
		return succMsg;
	}
	
	//微信卡包红冲－从微信卡包中移除电票
	public String TechWebCancelCard(String card_code, String iqEntId, String iqTaxNo)
	{
		String errorMsg = "";
		try
		{
			//取卡包模板ID
			String wxCardId = entPrivatepara.get(iqEntId, FGlobal.WeixinCard);
			
			//设置请求参数
			SzTechWebCardCancelRequestBean myRequest = new SzTechWebCardCancelRequestBean();
			myRequest.setCard_id(wxCardId);
			myRequest.setCard_code(card_code);
			myRequest.setStatus("cancel");
			
			//调用HTTP/HTTPS
			SzTechWebGenerateImpl mySend = new SzTechWebGenerateImpl();
			
			String request = JSONObject.fromObject(myRequest).toString();
			
			Long timestamp = System.currentTimeMillis() / 1000;
			
			Taxinfo myTaxInfo = new Taxinfo();
			myTaxInfo.setEntid(iqEntId);
			myTaxInfo.setTaxno(iqTaxNo);
			myTaxInfo = taxinfoDao.getTaxinfoByNo(myTaxInfo);
			
			String sign = mySend.genereateSign(JSONObject.fromObject(request), timestamp, myTaxInfo.getItfskpbh(), myTaxInfo.getItfskpkl());
			
			String strUrl = myTaxInfo.getItfserviceUrl().trim() + "/card/update-card-status";
			
			strUrl = strUrl + "?signature=" + sign + "&appkey=" + myTaxInfo.getItfskpbh() + "&timestamp=" + timestamp;
	
			String strResult = mySend.sendHttpRequest(strUrl, request);
			if (strResult == null || strResult.length() == 0) 
			{
				errorMsg = "高灯卡包红冲返回结果为空";
				log.info(errorMsg);
				return errorMsg;
			}
			
			JSONObject js = JSONObject.fromObject(strResult);
			
			if (js.getInt("code") != 0)
			{
				errorMsg = js.getString("msg");
				log.info("高灯卡包红冲:" + errorMsg);
				return errorMsg;
			}
			
			return "OK";
		}
		catch(Exception ex)
		{
			errorMsg = ex.getMessage();
			log.error(errorMsg);
			return errorMsg;
		}
	}
}
