package com.invoice.job.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.bean.db.CClient;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.Shop;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.job.dao.JobDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.NewHashMap;

@Service
public class ChargeSaleServiceImpl {
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
	@Qualifier("ChargeInvoiceService")
	SheetService chargeInvoiceService;

	@Autowired
	InvoiceSaleDao invoiceSaleDao;

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
				log.error("门店未配置："+shopid);
				continue;
			}
			client.initHeadMap();

			executeComplete(client, shop);
			
			executeSheet(client,shop);
			
		}

	}
	
	private void executeSheet(CClient client, Shop shop) {
		// 获取待处理的清单
		List<Map<String, Object>> sheetIdList = getList(client,shop);
		if (sheetIdList != null) {
			for (Map<String, Object> map : sheetIdList) {
				try {
					String sheetid = map.get("sheetid").toString();
					ResponseBillInfo o = getSheet(shop, sheetid);
					if (o == null) {
						o = getRometSheet(shop, sheetid);
						if (o != null) {
							JSONObject json = new JSONObject();
							json.put("flag", 1);
							json.put("oldFlag", 0);
							json.put("sheetid", sheetid);
							json.put("invoiceCode", "");
							json.put("invoiceNo", "");
							json.put("invoiceDate", "");
							json.put("invoiceType", "");
							callBackSheet(client, shop, json.toJSONString());
						}
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}
	
	
	public void executeComplete(CClient client,Shop shop) {
		Map<String,Object> para = new NewHashMap<String,Object>();
		para.put("entid", client.getEntid());
		para.put("shopid", shop.getShopid());
		List<Map<String, Object>> list = invoiceSaleDao.getDetailCallbackList(para);
		for (Map<String, Object> map : list) {
			try {
				JSONObject json = new JSONObject();
				json.put("flag", 100);
				json.put("oldFlag", 1);
				json.putAll(map);
				String res = callBackSheet(client, shop, json.toJSONString());
				JSONObject rj = JSONObject.parseObject(res);
				if(rj.getInteger("code") == 0) {
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
		headMap.put("sheetname", "charge");

		String res = client.getMessage("getList");
		
		if(StringUtils.isEmpty(res)) return null;
		
		JSONObject jo = JSONObject.parseObject(res);
		if (jo.getIntValue("code") == 0) {
			JSONArray sheetIdList = jo.getJSONArray("data");
			return sheetIdList.toJavaObject(List.class);
		}

		return null;
	}

	private String callBackSheet(CClient client,Shop shop, String data) {
		String shopid = shop.getShopid();
		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetname", "charge");
		headMap.put("data", data);

		String res = client.getMessage("callBackSheet");
		log.info("callBackSheet"+res);
		return res;
	}

	private ResponseBillInfo getSheet(Shop shop, String sheetid) {
		String entid = shop.getEntid();
		String shopid = shop.getShopid();
		String sheettype = "4";
		RequestBillInfo bill = new RequestBillInfo();
		bill.setSheetid(sheetid);
		bill.setEntid(entid);
		bill.setShopid(shopid);
		bill.setSheettype(sheettype);
		// 先查询本地有没有对应发票小票信息
		ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
		return head;
	}
	
	private ResponseBillInfo getRometSheet(Shop shop, String sheetid) {
		String entid = shop.getEntid();
		String shopid = shop.getShopid();
		String sheettype = "4";
		
		RequestBillInfo bill = new RequestBillInfo();
		bill.setSheetid(sheetid);
		bill.setEntid(entid);
		bill.setShopid(shopid);
		bill.setSheettype(sheettype);
		
		SheetHead sell = chargeInvoiceService.getRemoteBill(entid, shopid, sheetid);

		if (sell == null) {
			throw new RuntimeException("数据未找到，请检查是否正确或稍后再试 " + bill.getSheetid());
		}

		sell.setSheetid(bill.getSheetid());

		// 查到小票后，对小票进行运算
		InvoiceSaleHead saleHead = chargeInvoiceService.calculateSheet(sell, null);
		saleHead.setCreatetime(new Date());
		saleHead.setFlag(0);

		// 写入数据库 独立事物nx开头
		chargeInvoiceService.nxInvoiceSale2DB(saleHead);

		return invoiceSaleDao.getInvoiceSaleHead(bill);
	}
}