package com.invoice.job.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.invoice.config.FGlobal;
import com.invoice.job.dao.JobDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.NewHashMap;
import com.invoice.util.Serial;
import com.invoice.util.SpringContextUtil;

@Service
public class ZMSaleServiceImpl {
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
	PreInvoiceDao preInvoiceDao;
	
	/**
	 * @param entid
	 */
	public void test(String entid) {
		CClient client = privateparaService.getClientUrlByShopid(entid, "0106");
		client.initHeadMap();
		JSONObject json = new JSONObject();
		json.put("flag", 200);
		json.put("oldflag", 0);
		json.put("hzinvoicecode", "456");
		json.put("hzinvoiceno", "123");
		json.put("hzinvoicedate", "20180301120101");
		json.put("sheetid", "1800001931");
		Shop shop =  new Shop();
		shop.setShopid("0106");
		String res = callBackSheet(client, shop ,json.toJSONString());
		System.out.println(res);
	}

	public void execute(String entid) throws Exception {
		Enterprise enterprise = new Enterprise();
		enterprise.setEntid(entid);
		Enterprise ent = enterpriseService.getEnterpriseById(enterprise);

		if (ent == null) {
			throw new RuntimeException("[企业不存在]：" + entid);
		}

		// 取门店
		List<Shop> listShop = shopService.getShopsByEntId(entid);

		for (Shop shop : listShop) {
			String shopid = shop.getShopid();
			CClient client = privateparaService.getClientUrlByShopid(entid, shopid);
			if (client == null) {
				log.error("门店未配置：" + shopid);
				continue;
			}
			client.initHeadMap();

			executeSheet(client, shop);

		}

	}

	private void executeSheet(CClient client, Shop shop) {
		SheetService service = SpringContextUtil.getBean("BillInvoiceService", SheetService.class);
		// 获取待处理的清单
		List<Map<String, Object>> sheetIdList = getList(client, shop);
		if (sheetIdList != null) {
			for (Map<String, Object> map : sheetIdList) {
				String sheetid = map.get("sheetid").toString();
				log.info("处理单据："+sheetid);
				boolean hc = false;
				try {
					ResponseBillInfo o = getLocalSheet(shop, sheetid);

					if (o == null) {
						log.info("新单据，读取："+sheetid);
						o = getRometSheet(shop, sheetid);
						if (o != null) {
							JSONObject json = new JSONObject();
							json.put("flag", 10);
							json.put("oldflag", 0);
							json.put("sheetid", sheetid);
							callBackSheet(client, shop, json.toJSONString());
						}
					} else {
						// 如果本地已开票，则自动红冲
						if (o.getFlag() == 1) {
							log.info("已开票，自动红冲："+sheetid);
							String iqSeqno = o.getIqseqno();
							Map<String, Object> jo = new NewHashMap<>();
							jo.put("iqseqno", iqSeqno);
							// TODO 自动红冲
							List<Invque> que = invqueService.getInvque(jo);
							if(que.isEmpty()) {
								throw new Exception("红冲发票失败，原发票未找到");
							}
							Invque invque = que.get(0);
							
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
									Invque p = JSONObject.parseObject(js.getJSONObject("data").toJSONString(), Invque.class);
									log.info("红冲发票成功："+sheetid);
									hc = true;
									
									JSONObject json = new JSONObject();
									json.put("flag", 200);
									json.put("oldflag", 0);
									json.put("hzinvoicecode", p.getRtfpdm());
									json.put("hzinvoiceno", p.getRtfphm());
									json.put("hzinvoicedate", p.getIqdate());
									json.put("hzpdfurl", p.getIqpdf());
									json.put("sheetid", sheetid);
									res = callBackSheet(client, shop,json.toJSONString());
									JSONObject rj = JSONObject.parseObject(res);
									if(rj.getInteger("code") == 0) {
										
									}else {
										log.error("回写eop状态失败"+rj.getString("message"));
									}
								}else{
									log.info("红冲发票失败："+sheetid);
									throw new RuntimeException("红冲发票失败，"+js.getString("message"));
								}
								log.info("自动红冲结束："+sheetid);
							}
						}else if (o.getFlag() == -1 && o.getBackflag()==0){
							//如果本地已红冲，但是没有回写，则跳过，等待回写erp后再处理
							log.info("本地erp状态尚未回写，跳过："+sheetid);
							continue;
						}
						
						log.info("重新读取数据："+sheetid);
						//重新写入数据
						InvoiceSaleHead saleHead = new InvoiceSaleHead();
						saleHead.setSheettype(o.getSheettype());
						saleHead.setSheetid(o.getSheetid());
						saleHead.setEntid(o.getEntid());
						saleHead.setSerialid(o.getSerialid());
						service.txInvoiceSale2Delete(saleHead);
						
						o = getRometSheet(shop, sheetid);
						
						if (o != null && !hc) {
							log.info("回写状态10："+sheetid);
							JSONObject json = new JSONObject();
							json.put("flag", 10);
							json.put("oldflag", 0);
							json.put("sheetid", sheetid);
							callBackSheet(client, shop, json.toJSONString());
						}
					}
				} catch (Exception e) {
					JSONObject json = new JSONObject();
					json.put("flag", 9);
					json.put("oldflag", 0);
					json.put("sheetid", sheetid);
					json.put("flagmsg", e.getMessage());
					callBackSheet(client, shop, json.toJSONString());
				}
			}
		}
	}

	public void executeComplete(CClient client, Shop shop) {
		Map<String, Object> para = new NewHashMap<String, Object>();
		para.put("entid", client.getEntid());
		para.put("shopid", shop.getShopid());
		List<Map<String, Object>> list = invoiceSaleDao.getDetailCallbackList(para);
		for (Map<String, Object> map : list) {
			try {
				JSONObject json = new JSONObject();
				json.put("flag", 100);
				json.put("oldflag", 10);
				json.putAll(map);
				String res = callBackSheet(client, shop, json.toJSONString());
				JSONObject rj = JSONObject.parseObject(res);
				if (rj.getInteger("code") == 0) {
					map.put("backflag", 1);
					invoiceSaleDao.updateDetailCallbackFlag(map);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	private List<Map<String, Object>> getList(CClient client, Shop shop) {
		String shopid = shop.getShopid();

		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetname", "zm");

		String res = client.getMessage("getList");

		if (StringUtils.isEmpty(res))
			return null;

		JSONObject jo = JSONObject.parseObject(res);
		if (jo.getIntValue("code") == 0) {
			JSONArray sheetIdList = jo.getJSONArray("data");
			return sheetIdList.toJavaObject(List.class);
		}

		return null;
	}

	private String callBackSheet(CClient client, Shop shop, String data) {
		String shopid = shop.getShopid();
		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetname", "zm");

		try {
			data = URLEncoder.encode(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		headMap.put("data", data);

		String res = client.getMessage("callBackSheet");
		log.info("callBackSheet" + res);
		return res;
	}

	private ResponseBillInfo getLocalSheet(Shop shop, String sheetid) {
		String entid = shop.getEntid();
		String sheettype = "6";
		RequestBillInfo bill = new RequestBillInfo();
		bill.setSheetid(sheetid);
		bill.setEntid(entid);
		bill.setShopid(null);
		bill.setSheettype(sheettype);
		// 先查询本地有没有对应发票小票信息
		ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
		return head;
	}
	
	private ResponseBillInfo getRometSheet(Shop shop, String sheetid) {
		String entid = shop.getEntid();
		String shopid = shop.getShopid();
		String sheettype = "6";

		RequestBillInfo bill = new RequestBillInfo();
		bill.setSheetid(sheetid);
		bill.setEntid(entid);
		bill.setShopid(null);
		bill.setSheettype(sheettype);

		SheetService service = SheetServiceFactory.getInstance(sheettype);

		SheetHead sell = service.getRemoteBill(entid, shopid, sheetid);

		if (sell == null) {
			throw new RuntimeException("数据未找到 " + bill.getSheetid());
		}
		
		sell.setBillno(sell.getExtsheetid());

		// 查到小票后，对小票进行运算
		InvoiceSaleHead saleHead = service.calculateSheet(sell, null);
		saleHead.setCreatetime(new Date());
		
		
		PreInvoiceAsk ask = null;
		if(StringUtils.isEmpty(saleHead.getInvoicelx()) || "026".equals(saleHead.getInvoicelx())) {
			log.info("订单当前提货状态："+bill.getSheetid());
			//如果指定的发票类型是空或026，且存在预申请中，则调整为自动开票
			Map<String, Object> p = new NewHashMap<>();
			p.put("entid", bill.getEntid());
			p.put("sheetid", bill.getSheetid());
			p.put("sheettype", 6);
			p.put("flag", 0);
			ask = preInvoiceDao.getPreInvoiceAsk(p);
			if(ask!=null) {
				saleHead.setRecvemail(ask.getRecvemail());
				saleHead.setRecvphone(ask.getRecvphone());
				//saleHead.setGmfname(ask.getGmfname());
				saleHead.setGmftax(ask.getGmftax());
				saleHead.setGmfadd(ask.getGmfadd());
				saleHead.setGmfbank(ask.getGmfbank());
				saleHead.setIsauto(1);
			}
			// 写入数据库 独立事物nx开头
			service.nxInvoiceSale2DB(saleHead);
			if(ask!=null) {
				ask.setFlag(1);
				preInvoiceDao.updatePreInvoiceAsk(ask);
			}
		}else {
			// 写入数据库 独立事物nx开头
			service.nxInvoiceSale2DB(saleHead);
		}

		
		return invoiceSaleDao.getInvoiceSaleHead(bill);
	}
}