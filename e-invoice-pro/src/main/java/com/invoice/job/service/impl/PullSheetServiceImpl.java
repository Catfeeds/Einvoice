package com.invoice.job.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.apiservice.dao.InvoiceDao;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.PreInvoiceDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.apiservice.service.impl.InvqueService;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.bean.db.CClient;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.PreInvoiceAsk;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.Shop;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.job.dao.JobDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.LocalRuntimeException;
import com.invoice.util.NewHashMap;
import com.invoice.util.Serial;
import com.invoice.util.SpringContextUtil;

/**
 * 主动拉取业务数据
 * @author Baij
 *
 */
@Service
public class PullSheetServiceImpl {
	public final Log log = LogFactory.getLog(this.getClass());
	
	
	@Autowired
	JobDao jobDao;

	@Autowired
	CalculateDao calculateDao;

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
	InvoiceDao invoiceDao;
	
	@Autowired
	PreInvoiceDao  preInvoiceDao;
	
	@Autowired
	EntPrivatepara entPrivatepara;
	
	public void execute(String entid,String sheettype, String shopid) throws Exception {
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
					 
					CClient client = privateparaService.getClientUrlByShopid(entid, shopid);
					if (client == null) {
						log.error("门店未配置：" + shopid);
					}
					client.initHeadMap();
		
					executeSheetByYz(client, sheettype, shop);
				}
			}
			
		}else{
		
			//如果没有指定门店，则遍历所有门店
			// 取门店
			List<Shop> listShop = null;
			if(shopid == null) {
				listShop = shopService.getShopsByEntId(entid);
				
			}else {
				Shop shop = new Shop();
				shop.setShopid(shopid);
				shop.setEntid(entid);
				shop = shopService.getShopById(shop);
				if(shop==null) {
					log.error("门店不存在:"+shopid);
					return;
				}
				listShop = new ArrayList<>();
				listShop.add(shop);
			}
			
			for (Shop shop : listShop) {
				String sid = shop.getShopid();
				CClient client = privateparaService.getClientUrlByShopid(entid, sid);
				if (client == null) {
					log.error("门店未配置：" + sid);
					continue;
				}
				client.initHeadMap();
	
				executeSheet(client, sheettype, shop);
			}
		}
		
	}
	
	public void test() {
		Shop shop = new Shop();
		shop.setShopid("02");
		shop.setEntid("HN001");
		
		CClient client = privateparaService.getClientUrlByShopid("HN001", "02");
		if (client == null) {
			log.error("门店未配置：" + "02");
		}
		client.initHeadMap();
		
//		executeSheet(client, "7", shop);
		
		String sheetid = "1811230100000000012";
		log.info("处理单据："+sheetid);
		RequestBillInfo bill = new RequestBillInfo();
		bill.setSheetid(sheetid);
		bill.setEntid(shop.getEntid());
		bill.setShopid(shop.getShopid());
		bill.setSheettype("7");
		JSONObject json = new JSONObject();
		json.put("sheetid", sheetid);
		json.put("sheettype","7");
		SheetService sheetservice = SheetServiceFactory.getInstance("7");
//		try {
//			getSheet(sheetservice,bill);
//			json.put("flag", 10);
//			json.put("oldflag", 0);
//			json.put("flagmsg", "提取成功");
//		} catch (LocalRuntimeException e) {
//			log.error(e,e);
//			json.put("flag", 9);
//			json.put("oldflag", 0);
//			json.put("flagmsg", e.getMessage());
//		} catch (Exception e) {
//			log.error(e,e);
//			json.put("flag", 9);
//			json.put("oldflag", 0);
//			json.put("flagmsg", e.getMessage());
//		}
		
		json.put("flag", 0);
		json.put("oldflag", 10);
		json.put("flagmsg", "");
		
		callBackSheet(sheetservice,client, shop, json.toJSONString());
	}


	private void executeSheet(CClient client,String sheettype, Shop shop) {
		SheetService sheetservice = SheetServiceFactory.getInstance(sheettype);
		List<Map<String, Object>> sheetIdList = getList(sheetservice,client, shop);
		if (sheetIdList != null) {
			log.info("获取待处理数据"+sheetIdList.size()+"条");
			for (Map<String, Object> map : sheetIdList) {
				String sheetid = map.get("sheetid").toString();
				log.info("处理单据："+sheetid);
				RequestBillInfo bill = new RequestBillInfo();
				bill.setSheetid(sheetid);
				bill.setEntid(shop.getEntid());
				bill.setShopid(shop.getShopid());
				bill.setSyjid(map.get("syjid")==null?"":map.get("syjid").toString());
				bill.setBillno(map.get("billno")==null?"":map.get("billno").toString());
				
				bill.setSheettype(sheettype);
				JSONObject json = new JSONObject();
				json.put("sheetid", sheetid);
				json.put("sheettype",sheettype);
				try {
					getSheet(sheetservice,bill,map);
					json.put("flag", 10);
					json.put("oldflag", 0);
					json.put("flagmsg", "提取成功");
				} catch (LocalRuntimeException e) {
					log.error("提取数据异常："+e);
					json.put("flag", 9);
					json.put("oldflag", 0);
					json.put("flagmsg", e.getMessage());
				} catch (Exception e) {
					log.error("提取数据异常："+e);
					json.put("flag", 9);
					json.put("oldflag", 0);
					json.put("flagmsg", e.getMessage());
				}
				callBackSheet(sheetservice,client, shop, json.toJSONString());
			}
		}
	}
	
	private void executeSheetByYz(CClient client,String sheettype, Shop shop) {
		SheetService sheetservice = SheetServiceFactory.getInstance(sheettype);
		List<Map<String, Object>> sheetIdList = getList(sheetservice,client, shop);
		if (sheetIdList != null) {
			log.info("获取待处理数据"+sheetIdList.size()+"条");
			for (Map<String, Object> map : sheetIdList) {
				if(!StringUtils.isEmpty(map.get("createtime").toString())){
					java.util.Calendar Cal=java.util.Calendar.getInstance();    
					try{
					java.text.SimpleDateFormat formatter=new java.text.SimpleDateFormat("yyyy-MM-dd HH:dd:ss");    
					Cal.setTime(formatter.parse(map.get("createtime").toString()));    
					Cal.add(java.util.Calendar.HOUR_OF_DAY,shop.getOpentime());
					}catch(Exception e){
						log.error("提取数据异常："+e);
						
					}
					//System.out.println(Cal.getTime().before(new java.util.Date()));  
					if(Cal.getTime().before(new java.util.Date())){
						String sheetid = map.get("sheetid").toString();
						log.info("处理单据："+sheetid);
						RequestBillInfo bill = new RequestBillInfo();
						bill.setSheetid(sheetid);
						bill.setEntid(shop.getEntid());
						bill.setShopid(shop.getShopid());
						bill.setSyjid(map.get("syjid")==null?"":map.get("syjid").toString());
						bill.setBillno(map.get("billno")==null?"":map.get("billno").toString());
						
						bill.setSheettype(sheettype);
						JSONObject json = new JSONObject();
						json.put("sheetid", sheetid);
						json.put("sheettype",sheettype);
						try {
							getSheet(sheetservice,bill,map);
							json.put("flag", 10);
							json.put("oldflag", 0);
							json.put("flagmsg", "提取成功");
						} catch (LocalRuntimeException e) {
							log.error("提取数据异常："+e);
							json.put("flag", 9);
							json.put("oldflag", 0);
							json.put("flagmsg", e.getMessage());
						} catch (Exception e) {
							log.error("提取数据异常："+e);
							json.put("flag", 9);
							json.put("oldflag", 0);
							json.put("flagmsg", e.getMessage());
						}
						callBackSheet(sheetservice,client, shop, json.toJSONString());
					}
					
				}
				
			}
		}
	}
	
	private void hongchong(String sheetid,String iqSeqno,String sheettype) throws Exception {
		//已经开票，自动红冲
		log.info("已开票，自动红冲："+sheetid);
		Map<String, Object> jo = new NewHashMap<>();
		jo.put("iqseqno", iqSeqno);
		// TODO 自动红冲
		List<Invque> que = invqueService.getInvque(jo);
		if(que.isEmpty()) {
			throw new Exception("红冲发票失败，原发票未找到");
		}
		Invque invque = que.get(0);
		//查询该单据是否有蓝屏存在//wmj
		int invoicecount	= invoiceDao.getInvoiceCount(jo);
		if(invoicecount>0){
			//如果发票不是电票，则不自动红冲
			if(invque.getIqfplxdm().equals("026")) {
	
				String iqseqno = Serial.getInvqueSerial();
				invque.setIqseqno(iqseqno);
				invque.setIqyfpdm(invque.getRtfpdm());
				invque.setIqyfphm(invque.getRtfphm());
				invque.setIqtype(1);
				//baij 红冲票发票信息设置为空，开具成功后填入
				invque.setIqstatus(0);
				invque.setRtfpdm("");
				invque.setRtfphm("");
				invque.setRtewm("");
				invque.setRtskm("");
				invque.setRtjym("");
				//开票服务rest
				String url = FGlobal.openinvoiceurl;
				//立即执行
				url += "immediAskInvoice";
				String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(invque), null, url, 0, 0, "utf-8");
				if(StringUtils.isEmpty(res)){
					throw new RuntimeException("红冲发票失败，请求电子发票平台超时");
				}
				JSONObject js = JSONObject.parseObject(res);
				if(js.getIntValue("code")==0){
					invque = JSONObject.parseObject(js.getString("data"), Invque.class);
					if(invque.getIqstatus()==30){
						throw new RuntimeException("红冲发票失败，"+invque.getIqmsg());
					}
					log.info("红冲发票成功："+sheetid);
				}else{
					log.info("红冲发票失败："+sheetid);
					throw new RuntimeException("红冲发票失败，"+js.getString("message"));
				}
				log.info("自动红冲结束："+sheetid);
				//将原队列中顾客发票抬头信息记录
				invque.setSheetid(sheetid);
				savePreInvoice_ask(invque,sheettype);
			}
			
		}

		
	}
	//wmj
	private void savePreInvoice_ask(Invque que,String sheettype){
		PreInvoiceAsk invoiceask = new PreInvoiceAsk();
		invoiceask.setCreatetime(new Date());
		invoiceask.setEntid(que.getIqentid());
		invoiceask.setSheetid(que.getSheetid());
		invoiceask.setSheettype(sheettype);
		invoiceask.setRecvemail(que.getIqemail());
		invoiceask.setRecvphone(que.getIqtel());
		invoiceask.setFlag(0);
		invoiceask.setGmftax(que.getIqgmftax());
		invoiceask.setGmfname(que.getIqgmfname());
		invoiceask.setGmfadd(que.getIqgmfadd());
		invoiceask.setGmfbank(que.getIqgmfbank());
		invoiceask.setGmfno("");
		invoiceask.setInvoicelx(que.getIqfplxdm());
		invoiceask.setOpenid(que.getIqperson());
		preInvoiceDao.insertPreInvoiceAsk(invoiceask);
		
	}
	
	private void getSheet(SheetService sheetservice,RequestBillInfo bill,Map<String, Object> map) throws Exception {
		//读取本地数据
		ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
		if(head!=null && head.getFlag()==1){
			hongchong(head.getSheetid(), head.getIqseqno(),bill.getSheettype());
		}
		
		boolean ishongchong = false; //标记是否是红冲
		String  serialid = null;
		//需重算标志，Serialid不要变
		if(head!=null && head.getFlag()==-1){
			serialid = head.getSerialid();
			InvoiceSaleHead saleHead = new InvoiceSaleHead();
			saleHead.setSheettype(head.getSheettype());
			saleHead.setSheetid(head.getSheetid());
			saleHead.setEntid(head.getEntid());
			saleHead.setSerialid(head.getSerialid());
			SpringContextUtil.getBean("BillInvoiceService", SheetService.class).txInvoiceSale2Delete(saleHead );
			head = null;
			ishongchong = true;
		}
		
		if (head == null) {
			// 本地没有则向小票服务查询
			String rearchSheetid=bill.getSheetid();
			SheetHead sell =sheetservice.getRemoteBill(bill.getEntid(), bill.getShopid(), rearchSheetid);
			log.info("读到远端数据："+JSONObject.toJSONString(sell));
			if(sell==null){
				throw new LocalRuntimeException("数据未找到，请检查是否正确或稍后再试 "+ bill.getSheetid(),"提取码错误[数据准备中]",LocalRuntimeException.DATA_NOT_FOUND);
			}
			
			//强制单据的单号
			sell.setSheetid(bill.getSheetid());
			
			//如果没有读到isauto，则以任务表的为准
			String isauto = map.get("isauto")==null?"1":map.get("isauto").toString();
			if(sell.getIsauto()==null) {
				sell.setIsauto(Integer.parseInt(isauto));
			}
			
			//河南银基特殊处理 当数据中没有邮箱时，不自动开票
			if(StringUtils.isEmpty(sell.getRecvemail())) {
				if(bill.getEntid().equals("HN001")) {
					sell.setIsauto(0);
				}
			}
			
			//远端门店必须和请求的门店信息一致
			if(!sell.getShopid().equals(bill.getShopid())){
				throw new LocalRuntimeException("数据门店信息异常 ，门店R:L="+ sell.getShopid()+":"+bill.getShopid(),"提取码错误[门店]",LocalRuntimeException.DATA_BASE_ERROR);
			}

			// 查到小票后，对小票进行运算
			log.info("开始计算小票"+sell.getSheetid());
			InvoiceSaleHead saleHead = sheetservice.calculateSheet(sell,serialid);
			saleHead.setCreatetime(new Date());
			saleHead.setFlag(0);
			
			log.info("写入数据库"+saleHead.getSheetid());
			//mj
			if(StringUtils.isEmpty(saleHead.getGmfname())&&ishongchong){//当获取的小票信息中不存在顾客发票抬头信息执行
				Map<String, Object> headMap =  new NewHashMap<String, Object>();
				headMap.put("entid", bill.getEntid());
				headMap.put("sheetid", bill.getSheetid());
				headMap.put("sheettype", bill.getSheettype());
				headMap.put("flag", "0");
				PreInvoiceAsk invoiceAsk= preInvoiceDao.getPreInvoiceAsk(headMap);
				//获取原此小票对应的发票抬头 --如果有以前开具过发票
				if(invoiceAsk!=null){
					saleHead.setGmftax(invoiceAsk.getGmftax());
					saleHead.setGmfname(invoiceAsk.getGmfname());
					saleHead.setGmfadd(invoiceAsk.getGmfadd());
					saleHead.setGmfbank(invoiceAsk.getGmfbank());
					saleHead.setGmfno(invoiceAsk.getGmfno());
					saleHead.setRecvemail(invoiceAsk.getRecvemail());
					saleHead.setRecvphone(invoiceAsk.getRecvphone());
				}
			}else{
				saleHead.setGmfname(map.get("gmfname")==null?saleHead.getGmfname():map.get("gmfname").toString());
				saleHead.setGmfadd(map.get("gmfadd")==null?saleHead.getGmfadd():map.get("gmfadd").toString());
				saleHead.setGmfbank(map.get("gmfbank")==null?saleHead.getGmfbank():map.get("gmfbank").toString());
				saleHead.setGmfno(map.get("gmfno")==null?saleHead.getGmfno():map.get("gmfno").toString());
				saleHead.setGmftax(map.get("gmftax")==null?saleHead.getGmftax():map.get("gmftax").toString());
			}
			// 写入数据库 独立事物nx开头
			SpringContextUtil.getBean("BillInvoiceService", SheetService.class).nxInvoiceSale2DB(saleHead);

		}
	}
 
	
	private List<Map<String, Object>> getList(SheetService sheetservice,CClient client, Shop shop) {
		String shopid = shop.getShopid();

		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetname", sheetservice.getSHEET_NAME());
		
		client.setHeadMap(headMap);
		
		String res = client.getMessage("getList");

		if (StringUtils.isEmpty(res)) return null;

		JSONObject jo = JSONObject.parseObject(res);
		if (jo.getIntValue("code") == 0) {
			JSONArray sheetIdList = jo.getJSONArray("data");
			return sheetIdList.toJavaObject(List.class);
		}else {
			log.info("获取待处理数据异常"+res);
		}

		return null;
	}
	
	private String callBackSheet(SheetService sheetservice,CClient client, Shop shop, String data) {
		String shopid = shop.getShopid();
		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetname", sheetservice.getSHEET_NAME());

		try {
			data = URLEncoder.encode(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		headMap.put("data", data);
		
		client.setHeadMap(headMap);

		String res = client.getMessage("callBackSheet");
		log.info("callBackSheet" + res);
		return res;
	}
}
