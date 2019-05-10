package com.invoice.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.apiservice.service.impl.CustomerService;
import com.invoice.apiservice.service.impl.InvqueService;
import com.invoice.apiservice.service.impl.LockedSheetInvoiceService;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.apiservice.service.impl.WxService;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.Customer;
import com.invoice.bean.db.InvoiceFlagLog;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.InvqueList;
import com.invoice.bean.db.Privatepara;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.RequestInvoice;
import com.invoice.bean.ui.RequestInvoiceDetail;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.ResponseInvoice;
import com.invoice.bean.ui.Token;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.task.queue.AskInvoiceTask;
import com.invoice.uiservice.dao.InvoiceFlagLogDao;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.uiservice.service.GoodstaxService;
import com.invoice.util.Base64;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.LocalRuntimeException;
import com.invoice.util.NewHashMap;
import com.invoice.util.Page;
import com.invoice.util.Serial;
import com.invoice.util.SpringContextUtil;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * @author Baij 公开API调用
 */
@Controller
@RequestMapping(value = "/api")
public class ApiRest {
	private final Log log = LogFactory.getLog(ApiRest.class);

	@Autowired
	PrivateparaService privateparaService;
	
	@Autowired
	EntPrivatepara entPrivatepara;

	@Autowired
	CustomerService customerService;

	@Autowired
	WxService wxService;

	@Autowired
	InvqueService invqueService;
	
	@Autowired
	LockedSheetInvoiceService lockedSheetInvoiceService;
	
	@Autowired
	TaxinfoDao taxinfoDao;
	
	@Autowired
	AskInvoiceTask askInvoiceTask;
	
	@Autowired
	InvoiceFlagLogDao invoiceflaglogdao;
	/**
	 * 扫描微信发票抬头
	 * @param callback
	 * @param weburl
	 * @return
	 */
	@RequestMapping(value = "/scanTitle")
	@ResponseBody
	public String scanTitle(
			@RequestParam(value = "scanText", required = true) String scanText) {
		if(!scanText.contains("w.url.cn")) {
			return new RtnData(-1,"仅支持扫微信").toString();
		}else {
			Token token = Token.getToken();
			String entid = token.getEntid();
			return new RtnData(wxService.scanTitle(entid,scanText)).toString();
		}
	}
	

	/**
	 * 获取企业私有参数
	 * 
	 * @param entid
	 * @return
	 */
	@RequestMapping(value = "/getPrivatepara" )
	@ResponseBody
	public String getPrivatepara(@RequestParam(value = "entid", required = false) String entid) {
		log.debug(entid);
		Privatepara p = new Privatepara();
		p.setEntid(entid);
		List<Privatepara> l = privateparaService.getPrivatepara(p);
		return new RtnData(l).toString();
	}

	/**
	 * 根据Session中的userid属性获取用户的信息
	 * @return
	 */
	@RequestMapping(value = "/getCustomerInfo")
	@ResponseBody
	public String getCustomerInfo(HttpSession session) {
		try {

			// 强制用户权限范围
			Token token = Token.getToken();
			String loginid = token.getLoginid();
			if(StringUtils.isEmpty(loginid) || loginid.equals(token.getEntid())){
				return new RtnData(-1, "no userinfo set").toString();
			}
			
			Customer c = new Customer();
			c.setLoginid(loginid);
			Customer res = customerService.getCustomer(c);
			return new RtnData(res).toString();
		} catch (Exception e) {
			return new RtnData(-1, e.toString()).toString();
		}
	}
	
	@RequestMapping(value = "/getWebCustomerInfo")
	@ResponseBody
	public String getWebCustomerInfo(@RequestBody String data) {
		log.debug(data);
		try {

			// 强制用户权限范围
			Token token = Token.getToken();
			String loginid = token.getLoginid();
			if(StringUtils.isEmpty(loginid) || loginid.equals(token.getEntid())){
				return new RtnData(-1, "no userinfo set").toString();
			}
			
			Customer c = JSONObject.parseObject(data, Customer.class);
			c.setLoginid(loginid);
			if(c.getBz() != null && !"".equals(c.getBz())){
				c.setEntid(token.getEntid());
				c.setShopid(token.getShopid());
				List<Customer> res = customerService.getBzCustomer(c);
				return new RtnData(res).toString();
			}else{
				//银座
				List<Customer> res = customerService.getWebCustomer(c);
				return new RtnData(res).toString();
			}
			
		} catch (Exception e) {
			return new RtnData(-1, e.toString()).toString();
		}
	}
	
	
	@RequestMapping(value = "/updateCustomerInfo")
	@ResponseBody
	public String updateCustomerInfo(@RequestBody String data) {
		log.debug(data);
		try {

			// 强制用户权限范围
			Token token = Token.getToken();
			String loginid = token.getLoginid();
			if(StringUtils.isEmpty(loginid) || loginid.equals(token.getEntid())){
				return new RtnData(-1, "no userinfo set").toString();
			}
			
			Customer c = JSONObject.parseObject(data, Customer.class);
			c.setLoginid(loginid);
			if(c.getBz() != null && !"".equals(c.getBz())){
				c.setEntid(token.getEntid());
				c.setShopid(token.getShopid());
				Customer res = customerService.addorBzupdate(c);
				return new RtnData(res).toString();
			}else{
				//银座
				Customer res = customerService.addorupdate(c);
				return new RtnData(res).toString();
			}
			
		} catch (Exception e) {
			return new RtnData(-1, e.toString()).toString();
		}
	}

	/**
	 * 获取小票开票信息
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoiceBillInfo")
	@ResponseBody
	public String getInvoiceBillInfo(@RequestBody String data) {
		log.debug(data);
		try {
			RequestBillInfo bill = JSONObject.parseObject(data, RequestBillInfo.class);

			// 强制用户权限范围
			Token token = Token.getToken();
			if ("wx".equals(token.getChannel())){
				bill.setChannel(token.getChannel());
				bill.setUserID(token.getLoginid());
			}
			bill.setEntid(token.getEntid());
			
			SheetService sheetservice = SheetServiceFactory.getInstance(bill.getSheettype());

			ResponseBillInfo res = sheetservice.getInvoiceSheetInfo(bill);
			
			String pdfPre = entPrivatepara.get(token.getEntid(), FGlobal.pdfPre);
			String pdf = res.getPdf();
			if(!StringUtils.isEmpty(pdfPre) && !StringUtils.isEmpty(pdf) && !"wx".equals(token.getChannel())) {
				String pre = pdf.substring(0, pdf.indexOf("//"));
				pdf = pdf.substring(pdf.indexOf("://")+3, pdf.length());
				pdf = pdf.substring(pdf.indexOf("/"), pdf.length());
				pdf = pre+"//"+pdfPre+pdf;
				res.setPdf(pdf);
			}
			
			return new RtnData(res).toString();
		} catch (LocalRuntimeException e) {
			log.error(e);
			return new RtnData(e.getLocalCode(), e.getMessage()).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 获取小票开票信息，带商品明细
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoiceBillInfoWithDetail")
	@ResponseBody
	public String getInvoiceBillInfoWithDetail(@RequestBody String data) {
		log.debug(data);
		try {
			RequestBillInfo requestParams = JSONObject.parseObject(data, RequestBillInfo.class);

			// 强制用户权限范围
			Token token = Token.getToken();
			if ("wx".equals(token.getChannel()))
				requestParams.setChannel(token.getChannel());
			requestParams.setEntid(token.getEntid());
			requestParams.setUserID(token.getLoginid());
			
			SheetService sheetservice = SheetServiceFactory.getInstance(requestParams.getSheettype());

			ResponseBillInfo res = sheetservice.getInvoiceSheetInfoWithDetail(requestParams);
			return new RtnData(res).toString();
		} catch (LocalRuntimeException e) {
			log.error(e);
			return new RtnData(e.getLocalCode(), e.getMessage()).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/getInvoiceBillInfoByGmfNo")
	@ResponseBody
	public String getInvoiceBillInfoByGmfNo(@RequestBody String data) {
		log.debug(data);
		try {
			RequestBillInfo requestParams = JSONObject.parseObject(data, RequestBillInfo.class);

			// 强制用户权限范围
			Token token = Token.getToken();
			if ("wx".equals(token.getChannel()))
				requestParams.setChannel(token.getChannel());
			requestParams.setEntid(token.getEntid());
			requestParams.setUserID(token.getLoginid());
			
			String gmfNo = requestParams.getGmfno();
			if(StringUtils.isEmpty(gmfNo)) {
				throw new Exception("必须提供号码");
			}
			
			SheetService sheetservice = SheetServiceFactory.getInstance(requestParams.getSheettype());

			List<ResponseBillInfo> res = sheetservice.getInvoiceBillInfoByGmfNo(requestParams);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 获取所有可开票数据
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoiceSaleHeadList")
	@ResponseBody
	public String getInvoiceSaleHeadList(@RequestBody String data) {
		log.debug(data);
		try {
			RequestBillInfo requestParams = JSONObject.parseObject(data, RequestBillInfo.class);

			// 强制用户权限范围
			Token token = Token.getToken();
			if ("wx".equals(token.getChannel()))
				requestParams.setChannel(token.getChannel());
			requestParams.setEntid(token.getEntid());
			requestParams.setUserID(token.getLoginid());
			
			
			SheetService sheetservice = SheetServiceFactory.getInstance(requestParams.getSheettype());

			List<ResponseBillInfo> res = sheetservice.getInvoiceSaleHeadList(requestParams);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e,e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	
	/**
	 * 获取可开票的明细清单
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoiceBillDetailList")
	@ResponseBody
	public String getInvoiceBillDetailList(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject bill = JSONObject.parseObject(data);

			// 强制用户权限范围
			Token token = Token.getToken();
			if ("wx".equals(token.getChannel()))
				bill.put("channel", token.getChannel());
			bill.put("entid", token.getEntid());
			bill.put("loginid", token.getLoginid());
			
			SheetService sheetservice = SheetServiceFactory.getInstance(bill.getString("sheettype"));
			Page.cookPageInfo(bill);
			List<HashMap<String, String>> res = sheetservice.getInvoiceBillDetailList(bill);
			int count=sheetservice.getInvoiceBillDetailListCount(bill);
			return new RtnData(res,count).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}


	/**
	 * 发票信息预览
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoicePreview")
	@ResponseBody
	public String getInvoicePreview(@RequestBody String data) {
		log.debug(data);
		try {
			RequestInvoice p = JSONObject.parseObject(data, RequestInvoice.class);

			//纳税人识别号必须15或18位
			String nsrsbh = p.getGmfNsrsbh();
			if(!StringUtils.isEmpty(nsrsbh)) {
				if(nsrsbh.length() < 15 || nsrsbh.length()>20) {
					throw new RuntimeException("纳税识别号必长度不正确，请检查");
				}
				
				if(nsrsbh.indexOf(" ")!=-1) {
					throw new RuntimeException("纳税识别号不能有空格，请检查");
				}
				if(nsrsbh.indexOf(",")!=-1) {
					throw new RuntimeException("纳税识别号不能有逗号，请检查");
				}
			}
			if(!StringUtils.isEmpty(p.getGmfMc())) {
				if(p.getGmfMc().indexOf(" ")!=-1) {
					throw new RuntimeException("购买方名称不能有空格，请检查");
				}
				if(p.getGmfMc().indexOf(",")!=-1) {
					throw new RuntimeException("购买方名称不能有逗号，请检查");
				}
			}
			
			if(!StringUtils.isEmpty(p.getRecvPhone())) {
				if(!isInteger((p.getRecvPhone()))){
					throw new RuntimeException("手机号必须为数字，请检查");
				}
			}
			
			
			// 强制用户权限范围
			Token token = Token.getToken();
			p.setEntid(token.getEntid());
			p.setUserid(token.getLoginid());
			
			if ("wx".equals(token.getChannel())) {
				p.setChannel(token.getChannel());
				String kpr = entPrivatepara.get(p.getEntid(), FGlobal.WeixinKPR);
				if(StringUtils.isEmpty(kpr)) kpr = "system";
				p.setAdmin(kpr);
			}else {
				//开票人
				p.setAdmin(token.getUsername());
			}
			
			
			//默认复核人
			if(StringUtils.isEmpty(p.getIqchecker())) {
				String fhr = entPrivatepara.get(p.getEntid(), FGlobal.WeixinFHR);
				if(StringUtils.isEmpty(fhr)) fhr="";
				p.setIqchecker(fhr);
			}
			
			//默认收款人
			if(StringUtils.isEmpty(p.getIqpayee())) {
				String skr = entPrivatepara.get(p.getEntid(), FGlobal.weixinSKR);
				if(StringUtils.isEmpty(skr)) skr="";
				p.setIqpayee(skr);
			}
			
			SheetService sheetservice = SheetServiceFactory.getInstance(p.getSheettype());
			List<ResponseInvoice> res = sheetservice.getInvoicePreview(p);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 按明细开票发票信息预览
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoicePreview4Detail")
	@ResponseBody
	public String getInvoicePreview4Detail(@RequestBody String data) {
		log.debug(data);
		try {
			List<RequestInvoiceDetail> p = JSONObject.parseArray(data, RequestInvoiceDetail.class);
			
			if(p.isEmpty()) {
				return new RtnData(-1, "没有数据").toString();
			}

			// 强制用户权限范围
			Token token = Token.getToken();
			for (RequestInvoiceDetail requestInvoiceDetail : p) {
				if ("wx".equals(token.getChannel()))
					requestInvoiceDetail.setChannel(token.getChannel());
				requestInvoiceDetail.setEntid(token.getEntid());
				requestInvoiceDetail.setUserid(token.getLoginid());
			}
			
			
			SheetService sheetservice = SheetServiceFactory.getInstance(p.get(0).getSheettype());
			List<ResponseInvoice> res = sheetservice.getInvoicePreview4Detail(p);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	/**
	 * 申请开票
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/askInvoice")
	@ResponseBody
	public String askInvoice(@RequestBody String data) {
		log.debug(data);
		try {
			List<ResponseInvoice> p = JSONObject.parseArray(data, ResponseInvoice.class);

			// 强制用户权限范围
			Token token = Token.getToken();
			String sheetid = "";
			String gmfmc = "";
			for (ResponseInvoice responseInvoice : p) {

				//纳税人识别号必须15或18位
				String nsrsbh = responseInvoice.getGmfNsrsbh();
				if(!StringUtils.isEmpty(nsrsbh)) {
					if(nsrsbh.length() < 15 || nsrsbh.length()>20) {
						throw new RuntimeException("纳税识别号必长度不正确，请检查");
					}
				}
				
				responseInvoice.setEntid(token.getEntid());
				responseInvoice.setUserid(token.getLoginid());
				responseInvoice.setKpd(token.getKpd());
				responseInvoice.setJpzz(token.getJpzz());
				
				if ("wx".equals(token.getChannel())) {
					responseInvoice.setChannel(token.getChannel());
					String kpr = entPrivatepara.get(responseInvoice.getEntid(), FGlobal.WeixinKPR);
					if(StringUtils.isEmpty(kpr)) kpr = "system";
					responseInvoice.setAdmin(kpr);
				}else if ("app".equals(token.getChannel())) {
					responseInvoice.setAdmin(token.getUsername());
				}
				
				//默认复核人
				if(StringUtils.isEmpty(responseInvoice.getIqchecker())) {
					String fhr = entPrivatepara.get(responseInvoice.getEntid(), FGlobal.WeixinFHR);
					if(StringUtils.isEmpty(fhr)) fhr="";
					responseInvoice.setIqchecker(fhr);
				}
				
				//默认收款人
				if(StringUtils.isEmpty(responseInvoice.getIqpayee())) {
					String skr = entPrivatepara.get(responseInvoice.getEntid(), FGlobal.weixinSKR);
					if(StringUtils.isEmpty(skr)) skr="";
					responseInvoice.setIqpayee(skr);
				}
				
				for(ResponseBillInfo info:responseInvoice.getBillInfoList()){
					sheetid = info.getSheetid();
				}
				gmfmc = responseInvoice.getGmfMc();
			}
			String sendPerson = entPrivatepara.get(token.getEntid(), "sendPerson");
			if(StringUtils.isEmpty(sendPerson)) sendPerson="";
			List<Invque> res = null;
			
			if("1".equals(sendPerson)){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("entid", token.getEntid());
			map.put("sheetid", sheetid);
			if(sheetid==null )sheetid="";
			  res = invqueService.getInvqueForsheetid(map);
			if(res==null||res.size()==0){
				 res = invqueService.saveInvoiceQue(p);
			}else{
				if("个人".equals(gmfmc)){//当发票抬头为个人时重新推送发票
					askInvoiceTask.gainInvoiceJob(res.get(0),new RtnData() );
				}else{
					throw new RuntimeException("该小票已按税局要求自动生成抬头为个人的发票，如需换开发票抬头，需到店换开");
				}
			}
			}
			else{
			 res = invqueService.saveInvoiceQue(p);
			}
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 请求开票，中免专用
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/askInvoiceZM")
	@ResponseBody
	public String askInvoiceZM(@RequestBody String data) {
		log.debug(data);
		try {
			List<ResponseInvoice> p = JSONObject.parseArray(data, ResponseInvoice.class);

			// 强制用户权限范围
			Token token = Token.getToken();
			for (ResponseInvoice responseInvoice : p) {
				responseInvoice.setEntid(token.getEntid());
				responseInvoice.setUserid(token.getLoginid());
				responseInvoice.setKpd(token.getKpd());
				
				if ("wx".equals(token.getChannel())) {
					//如果当前小票指定了非电票，则记录到预开票申请中，并返回到前端
					if(invqueService.preInvoice(responseInvoice)) {
						return new RtnData(LocalRuntimeException.DATA_MESSAGE, "您的申请已成功受理，<br/>我们在您办理提货/退担保金手续后，<br/>将发票发送至您的邮箱、手机").toString();
					}
					
					responseInvoice.setChannel(token.getChannel());
				}else if ("app".equals(token.getChannel())) {
					responseInvoice.setAdmin(token.getUsername());
				}
				
				//默认复核人
				if(StringUtils.isEmpty(responseInvoice.getIqchecker())) {
					String fhr = entPrivatepara.get(responseInvoice.getEntid(), FGlobal.WeixinFHR);
					if(StringUtils.isEmpty(fhr)) fhr="";
					responseInvoice.setIqchecker(fhr);
				}
				
				//默认收款人
				if(StringUtils.isEmpty(responseInvoice.getIqpayee())) {
					String skr = entPrivatepara.get(responseInvoice.getEntid(), FGlobal.weixinSKR);
					if(StringUtils.isEmpty(skr)) skr="";
					responseInvoice.setIqpayee(skr);
				}
			}
			List<Invque> res = invqueService.saveInvoiceQue(p);
			return new RtnData(res).toString();
		} catch (LocalRuntimeException e) {
			log.error(e);
			return new RtnData(e.getLocalCode(), e.getMessage()).toString();
		} catch (Exception e) {
			log.error(e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 锁定小票为已开票或解锁
	 * @param data :{sheetid:小票提取码,isLock:1=锁 0=解锁,invoiceType:开票类型（默认为专票）}
	 * @return
	 */
	@RequestMapping(value = "/lockedSheetInvoice")
	@ResponseBody
	public String lockedSheetInvoice(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jsonParams = JSONObject.parseObject(data);
			String sheetid = jsonParams.getString("sheetid");
			int isLock = jsonParams.getIntValue("isLock");
			String sheetType = jsonParams.containsKey("sheettype")?jsonParams.getString("sheettype"):"1";
			String invoiceType = jsonParams.containsKey("invoiceType")?jsonParams.getString("invoiceType"):"004";
			String fpdm = jsonParams.containsKey("requestfpdm")?jsonParams.getString("requestfpdm"):"";
			String fphm = jsonParams.containsKey("requestfphm")?jsonParams.getString("requestfphm"):"";
			lockedSheetInvoiceService.lockedSheetInvoice(sheetid, sheetType, isLock, invoiceType,fpdm,fphm);
			Token token = Token.getToken();
			if("SDYZ".equals(token.getEntid())&&"004".equals(invoiceType)){
				InvoiceFlagLog log = new InvoiceFlagLog();
				log.setEntid(token.getEntid());
				log.setIslock(isLock+"");
				log.setFlag(isLock+"");
				log.setSheetid(sheetid);
				log.setInvoicetype(invoiceType);
				log.setProcessTime(new Date());
				log.setUserid(token.getLoginid());
				log.setUsername(token.getUsername());
				invoiceflaglogdao.insertInvoiceFlagLog(log);
			}
			return new RtnData().toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	

	/**
	 * 获取单条开票队列
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvque")
	@ResponseBody
	public String getInvque(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			// WX端强制用户权限范围
			if ("wx".equals(token.getChannel())) {
				//如果没有openid，也没有提供seqno，则认为是非法数据
				if(token.getEntid().equalsIgnoreCase(token.getLoginid()) && !jo.containsKey("iqseqno")) {
					return new RtnData(-1, "无权访问").toString();
				}
				
				//如果没有提供seqno，则强制增加userid
				if(!jo.containsKey("iqseqno")) {
					jo.put("userid", token.getLoginid());
				}
				
				jo.put("status", 50);
			}
			jo.put("entid",token.getEntid());
			
			Page.cookPageInfo(jo);
			
			//验证申请流水号
			if(jo.containsKey("iqseqno") && jo.getString("iqseqno")==null) {
				return new RtnData(-1, "查询参数异常").toString();
			}
			
			List<Invque> res = invqueService.getInvque(jo);
			
			String pdfPre = entPrivatepara.get(token.getEntid(), FGlobal.pdfPre);
			for (Invque invque : res) {
				String pdf = invque.getIqpdf();
				if (!StringUtils.isEmpty(pdfPre) && !"wx".equals(token.getChannel())) {
					if (!StringUtils.isEmpty(pdf)) {
						String pre = pdf.substring(0, pdf.indexOf("//"));
						pdf = pdf.substring(pdf.indexOf("://") + 3, pdf.length());
						pdf = pdf.substring(pdf.indexOf("/"), pdf.length());
						pdf = pre + "//" + pdfPre + pdf;
						invque.setIqpdf(pdf);
					}
				}
				if(!StringUtils.isEmpty(pdf) && pdf.contains("/fp/d?d=")) {
					//百望微信卡包地址插卡
					String  p = "fpdm="+invque.getRtfpdm()+"&fphm="+invque.getRtfphm();
					p = Base64.encode(p.getBytes());
					invque.setExtUrl("http://pis.baiwang.com/invoice?param="+p);
				}else {
					invque.setExtUrl("");
				}
			}
			
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 获取单条开票队列
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getOneInvque")
	@ResponseBody
	public String getOneInvque(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			
			if(jo.getString("report") == null ) {
				//此方法必须提供iqseqno
				if(!jo.containsKey("iqseqno") || jo.getString("iqseqno")==null) {
					return new RtnData(-1, "查询参数异常").toString();
				}
			}
			
			// 强制用户权限范围
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			
			Page.cookPageInfo(jo);
			int count = invqueService.getInvqueCount(jo);
			List<Invque> res = invqueService.getInvque(jo);
			return new RtnData(res,count).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 获取单条开票队列
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getOneInvque1")
	@ResponseBody
	public Invque getOneInvque1(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			
			//此方法必须提供iqseqno
			if(!jo.containsKey("iqseqno") || jo.getString("iqseqno")==null) {
				return null;
			}
			
			// 强制用户权限范围
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			
			List<Invque> res = invqueService.getInvque(jo);
			if(res != null && res.size()>0){
				return res.get(0);
			}else{
				return null;
			}
			
		} catch (Exception e) {
			log.error(e, e);
			return null;
		}
	}

	/**
	 * 查询开票队列数量
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvqueCount")
	@ResponseBody
	public String getInvqueCount(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);

			// 微信端强制用户权限范围，设置用户名
			Token token = Token.getToken();
			if ("wx".equals(token.getChannel())) {
				if(token.getEntid().equalsIgnoreCase(token.getLoginid()) && !jo.containsKey("iqseqno")) {
					return new RtnData(-1, "无权访问").toString();
				}
				
				jo.put("channel", token.getChannel());
				jo.put("userid", token.getLoginid());
				jo.put("status", 50);
			}
			jo.put("entid",token.getEntid());
			
			//验证申请流水号
			if(jo.containsKey("iqseqno") && jo.getString("iqseqno")==null) {
				return new RtnData(-1, "查询参数异常").toString();
			}

			int count = invqueService.getInvqueCount(jo);
			jo.put("count", count);
			return new RtnData(jo).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	/**
	 * 查询开票队列清单
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvqueList")
	@ResponseBody
	public String getInvqueList(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			
			//此方法必须提供iqseqno
			if(!jo.containsKey("iqseqno") || jo.getString("iqseqno")==null) {
				return new RtnData(-1, "查询参数异常").toString();
			}
			
			//微信端强制用户权限范围
			Token token = Token.getToken();
			if ("wx".equals(token.getChannel())) {
				jo.put("channel", token.getChannel());
			}
			jo.put("endid",token.getEntid());
			
			List<InvqueList> res = invqueService.getInvqueList(jo);
			return new RtnData(res).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}


	/**
	 * 空白票查询
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getBlankInvoice")
	@ResponseBody
	public String blankInvoice(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("userid",token.getLoginid());
			jo.put("entid",token.getEntid());
			jo.put("taxinfo",token.getTaxinfo());
			jo.put("kpd",token.getKpd());
			String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(jo), null, FGlobal.searchblankInvoice, 0, 0, "utf-8");
			
			if(StringUtils.isEmpty(res)){
				throw new RuntimeException("处理电子发票平台超时");
			}
			
			return res;
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 打印纸票
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/fp_print")
	@ResponseBody
	public String fp_print(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			RtnData rtn = new RtnData();
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			jo.put("taxinfo",token.getTaxinfo());
			jo.put("invque",getOneInvque1(data));
			jo.put("kpd",token.getKpd());
			String url = FGlobal.openinvoiceurl;
			String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(jo), null, url+"fp_print", 0, 0, "utf-8");
			
			if(StringUtils.isEmpty(res)){
				throw new RuntimeException("处理电子发票平台超时");
			}else{
				JSONObject js = JSONObject.parseObject(res);
				rtn.setMessage(js.getString("message"));
				Invque que = JSONObject.parseObject(js.getString("data"),Invque.class);
				jo.put("invque", que);
				rtn.setData(jo);
			}
			
			List<InvoiceSaleDetail> list = invqueService.getSaleDetailListByInvquelist(jo);
			int isqingdan = 0;
			if (list != null) {
				if (list.size() > 7) {
					isqingdan = 1;
				}
			}
			jo.put("isqingdan", isqingdan);
			return rtn.toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 打印纸票清单
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/fp_printforQd")
	@ResponseBody
	public String fp_printforQd(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			RtnData rtn = new RtnData();
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			jo.put("taxinfo",token.getTaxinfo());
			Invque inv = getOneInvque1(data);
			inv.setIsList(1);
			jo.put("invque",inv);
			jo.put("kpd",token.getKpd());
			List<InvoiceSaleDetail> list = invqueService.getSaleDetailListByInvquelist(jo);
			String url = FGlobal.openinvoiceurl;
			if(list != null){
				if(list.size() > 7){
					String res="";
					res = HttpClientCommon.doPostStream(JSONObject.toJSONString(jo), null, url+"fp_print", 0, 0, "utf-8");
					
					if(StringUtils.isEmpty(res)){
						throw new RuntimeException("处理电子发票平台超时");
					}else{
						JSONObject js = JSONObject.parseObject(res);
						rtn.setMessage(js.getString("message"));
						Invque que = JSONObject.parseObject(js.getString("data"),Invque.class);
						jo.put("invque", que);
						rtn.setData(jo);
					}
					return rtn.toString();
				}
			}
			return new RtnData(1,"此发票没有清单").toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 查询未开票商品
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getBillsalereport")
	@ResponseBody
	public String getBillsalereport(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("shopid",token.getShopid());
			jo.put("entid",token.getEntid());
			Page.cookPageInfo(jo);
			int count = SpringContextUtil.getBean(GoodstaxService.class).getBillsalereportCount(jo);
			List<HashMap<String, String>> list = SpringContextUtil.getBean(GoodstaxService.class).getBillsalereport(jo);
			return new RtnData(list,count).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	
	 @RequestMapping(value="/download")
	 @ResponseBody
	 protected void download(HttpServletRequest request, HttpServletResponse response, String sheetid,String token)
            throws ServletException, IOException {
		 InputStream in = null;
		 OutputStream out = null;  
		 try {  
			lockedSheetInvoiceService.lockedSheetInvoice(sheetid, "0", 1, "004","","");
			Token token1 = Token.getToken();
			
			RequestBillInfo bill = new RequestBillInfo();
			bill.setEntid(token1.getEntid());
			bill.setSheetid(sheetid);
			bill.setSheettype("0");
			List<InvoiceSaleDetail> detailList = SpringContextUtil.getBean("invoiceSaleDao", InvoiceSaleDao.class).getInvoiceSaleDetail(bill);
			String excelName = "需开专票的商品.xls";
		  
		   File excelFile = new File(excelName);  
		   // 如果文件存在就删除它  
		   if (excelFile.exists())  
		    excelFile.delete();  
		   // 打开文件  
		   WritableWorkbook book = Workbook.createWorkbook(excelFile);  
//		   // 文字样式  
		   jxl.write.WritableFont wfc = new jxl.write.WritableFont(  
		     WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,  
		     UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		  
		   jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(  
		     wfc);  
		  
		 //根据数据大小具体分n个sheet页，默认一页存储1000条数据
	        int sizeLoop = detailList.size();//数据大小
	        int size = detailList.size();
	        if(sizeLoop < 1000){
	            sizeLoop = 1000;
	        }
	        int sheetSize = 1000;
	        int loopSize = sizeLoop/sheetSize;
	        if(sizeLoop%sheetSize!=0){
	            loopSize+=1;
	        }
	        String[] heads = {"商品编码","商品名称","数量","单价","金额","税目代码","税率"};
	        String[] headsStr= {"goodsid","goodsname","qty","price","amt","taxitemid","taxrate"};
	        //分别往每个sheet页写数据
	        for(int l = 0;l<loopSize;l++){
	            WritableSheet sheet = book.createSheet("第"+(l+1)+"页", l);
	             
	            for(int i=0;i<heads.length;i++){
	                Label cell = new Label(i,0, heads[i],wcfFC);
	                sheet.addCell(cell );
	            }
	             
	            //循环读取数据列表
	            int n = 1;
	            for(int i=l*sheetSize;i<(l+1)*sheetSize && i<=size-1;i++){
	                Object vrd = detailList.get(i);
	                for(int j = 0;j<headsStr.length;j++){
	                    Object value = PropertyUtils.getProperty(vrd, headsStr[j]);
                        sheet.setColumnView(j, value.toString().length()+10);
                        sheet.addCell(new Label(j,n,value.toString(),wcfFC));
	                }
	                n++;
	            }
	        }
		  
		   // 写入数据并关闭文件  
		   book.write();  
		   book.close();  
		   
		   
		   byte[] bytes = new byte[1024];
	        int len = 0;
	        response.addHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes(),"iso-8859-1"));
	        response.setContentType("application/octet-stream;charset=utf-8");
	        // 读取文件
	         in = new FileInputStream(excelName);
	         // 写入浏览器的输出流
	         out = response.getOutputStream();
	         while ((len = in.read(bytes)) > 0) {
	             out.write(bytes, 0, len);
	         }
	        
		  } catch (Exception e) {  
			  e.printStackTrace();
		  }finally {  
		        in.close(); 
		        out.flush();
		        out.close();
		     }  
		}
	 
	 	/**
		 * 红冲
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/hongchongInvoice")
		@ResponseBody
		public String hongchongInvoice(@RequestBody String data) {
			log.debug(data);
			try {
				JSONObject jo = JSONObject.parseObject(data);
				Token token = Token.getToken();
				jo.put("entid",token.getEntid());
				Invque invque = getOneInvque1(data);
				if(invque != null){
					//小票信息带入
					Map<String, Object> p = new NewHashMap<>();
					p.put("iqseqno", invque.getIqseqno());
					List<InvqueList> list = invqueService.getInvqueList(p);
					
					String iqseqno = Serial.getInvqueSerial();
					
					for (InvqueList invqueList : list) {
						invqueList.setIqseqno(iqseqno);
					}
					invque.setInvqueList(list);
					
					invque.setIqseqno(iqseqno);
					invque.setIqyfpdm(jo.getString("yfpdm"));
					invque.setIqyfphm(jo.getString("yfphm"));
					invque.setIqtype(1);
					//baij 红冲票发票信息设置为空，开具成功后填入
					invque.setIqstatus(0);
					invque.setRtfpdm("");
					invque.setRtfphm("");
					invque.setRtewm("");
					invque.setRtskm("");
					invque.setRtjym("");
				}else{
					throw new RuntimeException("无法找到此发票队列信息");
				}
				//开票服务rest
				String url = FGlobal.openinvoiceurl;
				//立即执行
				url += "immediAskInvoice";
				String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(invque), null, url, 0, 0, "utf-8");
				if(StringUtils.isEmpty(res)){
					throw new RuntimeException("请求电子发票平台超时");
				}
				JSONObject js = JSONObject.parseObject(res);
				if(js.getIntValue("code")==0){
					invque = JSONObject.parseObject(js.getString("data"), Invque.class);
					if(invque.getIqstatus()==30){
						throw new RuntimeException(invque.getIqmsg());
					}
				}else{
					throw new RuntimeException(js.getString("message"));
				}
				
				return res;
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 重新推送pdf到邮箱
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/sendPdf")
		@ResponseBody
		public String sendPdf(@RequestBody String data) {
			log.debug(data);
			try {
				Invque invque = getOneInvque1(data);
				if(invque != null){
					//设置邮箱
					JSONObject jo = JSONObject.parseObject(data);
					if(jo.containsKey("email") && !StringUtils.isEmpty(jo.getString("email"))) {
						invque.setIqemail(jo.getString("email"));
					}
				}else{
					throw new RuntimeException("无法找到此发票队列信息");
				}
				//开票服务rest
				String url = FGlobal.openinvoiceurl;
				url += "sendPdf";
				String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(invque), null, url, 0, 0, "utf-8");
				if(StringUtils.isEmpty(res)){
					throw new RuntimeException("请求电子发票平台超时");
				}
				JSONObject js = JSONObject.parseObject(res);
				if(js.getIntValue("code")==0){
				}else{
					throw new RuntimeException(js.getString("message"));
				}
				
				return res;
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 发票作废
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/fp_zuofei")
		@ResponseBody
		public String fp_zuofei(@RequestBody String data) {
			log.debug(data);
			try {
				JSONObject jo = JSONObject.parseObject(data);
				Token token = Token.getToken();
				jo.put("entid",token.getEntid());
				if(jo.getString("zflx") != null && "0".equals(jo.getString("zflx"))){
					//空白票作废
					Invque invque = new Invque();
					invque.setIqfplxdm(jo.get("iqfplxdm").toString());
					invque.setZflx("0");
					invque.setIqseqno("0");
					jo.put("invque",invque);
					jo.put("kpd",token.getKpd());
					jo.put("taxinfo",token.getTaxinfo());
					if("6".equals(token.getTaxinfo().getTaxmode())){
						invque.setRtfpdm(jo.getString("yfpdm"));
						invque.setRtfphm(jo.getString("yfphm"));
					}else{
						String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(jo), null, FGlobal.searchblankInvoice, 0, 0, "utf-8");
						if(StringUtils.isEmpty(res)){
							throw new RuntimeException("处理电子发票平台超时");
						}
						JSONObject jsores = JSONObject.parseObject(res);
						if(jsores != null && !"".equals(jsores)){
							invque.setRtfpdm(JSONObject.parseObject(jsores.get("data").toString()).get("dqfpdm").toString()); 
							invque.setRtfphm(JSONObject.parseObject(jsores.get("data").toString()).get("dqfphm").toString());
						}
					}
					invque.setIqtype(2);
					invque.setIqadmin(token.getLoginid());
					jo.put("invque",invque);
				}else{
					Invque invque = getOneInvque1(data);
					if(invque != null){
						invque.setIqtype(2);
						invque.setZflx("1");
						jo.put("invque",invque);
						jo.put("kpd",invque.getIqtaxzdh());
						String nsrsbh = invque.getIqtaxno();
						Taxinfo taxinfo = new Taxinfo();
						taxinfo.setEntid(invque.getIqentid());
						taxinfo.setTaxno(nsrsbh);
						//taxinfo更改为从token直接获取, token信息已优先获取taxinfoitem表信息
		/*				try {
							taxinfo = taxinfoDao.getTaxinfoByNo(taxinfo);
						} catch (Exception e) {
							throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
						}*/
						jo.put("taxinfo",token.getTaxinfo());
					}else{
						throw new RuntimeException("无法找到此发票队列信息");
					}
				}
				String url = FGlobal.openinvoiceurl;
				String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(jo), null, url+"fp_zuofei", 0, 0, "utf-8");
				if(StringUtils.isEmpty(res)){
					throw new RuntimeException("处理电子发票平台超时");
				}
				return res;
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 数据库连接配置
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/c_connect")
		@ResponseBody
		public String c_connect(@RequestBody String data) {
			log.debug(data);
			try {
				JSONObject jo = JSONObject.parseObject(data);
				Token token = Token.getToken();
				jo.put("entid",token.getEntid());
				Page.cookPageInfo(jo);
				int count = privateparaService.getCConnectCount(jo);
				List<Map<String, String>> list = privateparaService.getCConnectList(jo);
				return new RtnData(list,count).toString();
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 更新数据库连接配置
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/updateConnect")
		@ResponseBody
		public String updateConnect(@RequestBody String data) {
			log.debug(data);
			try {
				CConnect jo = JSONObject.parseObject(data,CConnect.class);
				Token token = Token.getToken();
				jo.setEntid(token.getEntid());
				privateparaService.updateCConnect(jo);
				return new RtnData(jo).toString();
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 数据库连接配置测试
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/test_connect")
		@ResponseBody
		public String test_connect(@RequestBody String data) {
			log.debug(data);
			try {
				JSONObject jo = JSONObject.parseObject(data);
				Token token = Token.getToken();
				jo.put("entid",token.getEntid());
				String res0 = HttpClientCommon.doPostStream(JSONObject.toJSONString(jo), null, jo.getString("sellurl")+"/rest/api/reload", 0, 0, "utf-8");
				if(!StringUtils.isEmpty(res0)){
					String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(jo), null, jo.getString("sellurl")+"/rest/api/status/connect/"+jo.getString("connectid"), 0, 0, "utf-8");
					if(StringUtils.isEmpty(res)){
						throw new RuntimeException("处理电子发票平台超时");
					}
					return res;
				}
				throw new RuntimeException("处理电子发票平台超时");
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 重置小票信息
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/reset_bill")
		@ResponseBody
		public String reset_bill(@RequestBody String data) {
			log.debug(data);
			try {
				JSONObject jo = JSONObject.parseObject(data);
				int a = lockedSheetInvoiceService.reset_bill(jo);
				return new RtnData().toString();
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 返回发票数据记录到数据库 --九赋纸票
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "retpapiao")
		@ResponseBody
		public String retpapiao(@RequestBody String data) {
			log.debug(data);
			try {
				System.out.println("九赋接口纸票开票返回结果："+data);
				//JSONObject jo = JSONObject.parseObject(data);
				Invque p = JSONObject.parseObject(data, Invque.class);
				askInvoiceTask.retpapiao(p);
				return new RtnData().toString();
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 返回发票作废记录到数据库 --九赋纸票
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "retzuofei")
		@ResponseBody
		public String retzuofei(@RequestBody String data) {
			log.debug(data);
			try {
				System.out.println("九赋接口纸票作废返回结果："+data);
				//JSONObject jo = JSONObject.parseObject(data);
				Invque p = JSONObject.parseObject(data, Invque.class);
				if(p.getIqtype()==2){
					if(p.getZflx()==null||"".equals(p.getZflx())){
						throw new RuntimeException("发票作废类型不能为空："+p.getRtfphm());
					}
					if("1".equals(p.getZflx()))
						askInvoiceTask.restSaleInvoice(p);
					 
				}
				
				return new RtnData().toString();
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}
		
		/**
		 * 开票异常日志查询
		 * @param data
		 * @return
		 */
		@RequestMapping(value = "/getInvquelistForSheetid")
		@ResponseBody
		public String getInvquelistForSheetid(@RequestBody String data) {
			log.debug(data);
			try {
				JSONObject jo = JSONObject.parseObject(data);
				
				
				// 强制用户权限范围
				Token token = Token.getToken();
				jo.put("entid",token.getEntid());
				
				Page.cookPageInfo(jo);
				int count = invqueService.getInvquelistForSheetidcount(jo);
				List<Invque> res = invqueService.getInvquelistForSheetid(jo);
				return new RtnData(res,count).toString();
			} catch (Exception e) {
				log.error(e, e);
				return new RtnData(-1, e.getMessage()).toString();
			}
		}	
		
		 public static boolean isInteger(String str) {  
		        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
		        return pattern.matcher(str).matches();  
		  }
}
