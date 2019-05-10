package com.invoice.job.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.PreInvoiceDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.apiservice.service.impl.InvqueService;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.PreInvoiceAsk;
import com.invoice.bean.db.Shop;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.RequestInvoice;
import com.invoice.bean.ui.RequestInvoiceItem;
import com.invoice.bean.ui.ResponseInvoice;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.NewHashMap;

/**
 * 自动开票
 * @author Baij
 * 
 */
@Service
public class AutoOpenInvoiceServiceImpl {
	public final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	PrivateparaService privateparaService;

	@Autowired
	EnterpriseService enterpriseService;

	@Autowired
	ShopService shopService;

	@Autowired
	InvoiceSaleDao invoiceSaleDao;
	
	@Autowired
	InvqueService invqueService;
	
	@Autowired
	EntPrivatepara entPrivatepara;
	
	@Autowired
	TaxinfoDao taxinfoDao;
	
	@Autowired
	PreInvoiceDao preInvoiceDao;
	
	public void execute(String entid, String shopid) throws Exception {
		Enterprise enterprise = new Enterprise();
		enterprise.setEntid(entid);
		Enterprise ent = enterpriseService.getEnterpriseById(enterprise);

		if (ent == null) {
			throw new RuntimeException("[企业不存在]：" + entid);
		}
		String autoOpen = entPrivatepara.get(entid, "isautoopeninvoicebyshop");
		
		if(!StringUtils.isEmpty(autoOpen)&&"1".equals(autoOpen)){
			if(StringUtils.isEmpty(shopid)) {
				throw new RuntimeException("[门店ID不存在]：" + entid);
			}else{
				 Shop shop = new Shop();
				 shop.setShopid(shopid);
				 shop.setEntid(entid);
				 shop = shopService.getShopById(shop);
				if(StringUtils.isEmpty(shop)){
					throw new RuntimeException("[门店不存在]：" + shopid);
				}else{
					log.info(shopid+"是否全量开票标记为"+shop.getIsopenall());
					if(shop.getIsopenall()==1){
						Map<String,String> p = new NewHashMap<>();
						p.put("entid", entid);
						p.put("shopid", shopid);
						log.info("已验证为按门店开票");
						// 取待开票数据
						List<Map<String,Object>> list = invoiceSaleDao.getAutoList(p);
						for (Map<String,Object> map : list) {
							log.info("开始循环小票");
							if(!StringUtils.isEmpty(map.get("createtime").toString())){
								log.info("小票创建日期不为空");
								java.text.SimpleDateFormat formatter=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
								java.util.Calendar Cal=java.util.Calendar.getInstance();    
								Cal.setTime(formatter.parse(map.get("createtime").toString()));    
								
								Cal.add(java.util.Calendar.HOUR_OF_DAY,shop.getOpentime());      
								//System.out.println(Cal.getTime().before(new java.util.Date()));  
								if(Cal.getTime().before(new java.util.Date())){
									log.info("可以执行开票");
									open(map);
								}
								
							}
						}
					}
				}
			}
		}else{
			Map<String,String> p = new NewHashMap<>();
			p.put("entid", entid);
			p.put("shopid", shopid);
	
			// 取待开票数据
			List<Map<String,Object>> list = invoiceSaleDao.getAutoList(p);
			for (Map<String,Object> map : list) {
				open(map);
			}
		}
	}

	private void open(Map<String,Object> map) {
		log.info("已经调用开票方法");
		if(!StringUtils.isEmpty(map.get("iqseqno")) && (Integer)map.get("backflag")==0){
			log.info("该任务未回写Erp，跳过");
			return;
		}
		
		try {
			RequestInvoiceItem item = new RequestInvoiceItem();
			item.setSheetid( map.get("sheetid").toString());
			item.setJe(Double.parseDouble(map.get("totalAmount").toString()));
			List<RequestInvoiceItem> requestInvoicePreviewItem = new ArrayList<>();
			requestInvoicePreviewItem .add(item);
			
			RequestInvoice request = new RequestInvoice();
			request.setSheettype(map.get("sheettype").toString());
			request.setGmfMc(map.get("gmfname")==null?"":map.get("gmfname").toString());
			request.setGmfNsrsbh(map.get("gmftax")==null?"":map.get("gmftax").toString());
			request.setRecvEmail(map.get("recvemail")==null?"":map.get("recvemail").toString());
			request.setRecvPhone(map.get("recvphone")==null?"":map.get("recvphone").toString());
			request.setRequestInvoicePreviewItem(requestInvoicePreviewItem);
			
			request.setEntid(map.get("entid").toString());
			request.setUserid(map.get("entid").toString());
			if("SDYZ".equals(map.get("entid").toString())){
				request.setChannel("wx");
			}else{
				request.setChannel("app");
			}
			
			SheetService sheetservice = SheetServiceFactory.getInstance(request.getSheettype());
			List<ResponseInvoice> p = sheetservice.getInvoicePreview(request);
			for (ResponseInvoice responseInvoice : p) {
				
				//开票人
				String kpr = entPrivatepara.get(responseInvoice.getEntid(), FGlobal.WeixinKPR);
				if(StringUtils.isEmpty(kpr)) kpr = "system";
				responseInvoice.setAdmin(kpr);
				
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
				
				Shop shop = new Shop();
				shop.setEntid(request.getEntid());
				shop.setShopid(map.get("shopid").toString());
				shop = shopService.getShopById(shop);
				Taxinfo taxinfo = new Taxinfo();
				taxinfo.setEntid(request.getEntid());
				taxinfo.setTaxno(shop.getTaxno());
				taxinfo = taxinfoDao.getTaxinfoByNo(taxinfo);
				responseInvoice.setKpd(taxinfo.getItfkpd());
				
				//中免根据预申请表读取记录openid
				Map<String, Object> par = new NewHashMap<>();
				par.put("entid", responseInvoice.getEntid());
				par.put("sheetid", responseInvoice.getBillInfoList().get(0).getSheetid());
				par.put("sheettype", map.get("sheettype"));
				par.put("flag", 1);
				PreInvoiceAsk ask = preInvoiceDao.getPreInvoiceAsk(par);
				if(ask!=null) {
					request.setUserid(ask.getOpenid());
					request.setChannel("wx");
					responseInvoice.setUserid(ask.getOpenid());
					responseInvoice.setChannel("wx");
				}
			}
			log.info("申请开票："+map.get("sheetid").toString());
			invqueService.saveInvoiceQue(p);
			log.info("开票完成："+map.get("sheetid").toString());
		} catch (Exception e) {
			log.error("开票失败："+map.get("sheetid").toString()+" - "+e.getMessage());
		}
		
	}
}