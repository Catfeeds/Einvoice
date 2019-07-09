package com.invoice.job.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.bean.db.CClient;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.Shop;
import com.invoice.job.dao.JobDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.NewHashMap;

@Service
public class NBInvoiceCallBackServiceImpl {
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

	public void execute(String entid, String sheettype, String shopid) throws Exception {
		Enterprise enterprise = new Enterprise();
		enterprise.setEntid(entid);
		Enterprise ent = enterpriseService.getEnterpriseById(enterprise);

		if (ent == null) {
			throw new RuntimeException("[企业不存在]：" + entid);
		}

		// 如果没有指定门店，则遍历所有门店
		// 取门店
		List<Shop> listShop = null;
		if (shopid == null) {
			listShop = shopService.getShops(entid);

		} else {
			Shop shop = new Shop();
			shop.setShopid(shopid);
			shop.setEntid(entid);
			shop = shopService.getShopById(shop);
			if (shop == null) {
				log.error("门店不存在:" + shopid);
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

			executeCallBack(client, shop);
		}

	}

	public void executeCallBack(CClient client, Shop shop) {
		Map<String, Object> para = new NewHashMap<String, Object>();
		para.put("entid", client.getEntid());
		para.put("shopid", shop.getShopid());
		List<Map<String, Object>> list = invoiceSaleDao.getSaleCallBackList(para);
		for (Map<String, Object> map : list) {
			try {
				JSONObject json = new JSONObject();
				json.putAll(map);
				String res = NBcallBackSheet(client, shop, json.toJSONString());
				JSONObject rj = JSONObject.parseObject(res);
				if (rj.getInteger("code") == 0) {
					map.put("backflag", 1);
					invoiceSaleDao.NBupdateCallbackFlag(map);
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	private String NBcallBackSheet(CClient client, Shop shop, String data) {
		String shopid = shop.getShopid();
		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetname", "NBbill");
		headMap.put("data", data);
		client.setHeadMap(headMap);
		String res = client.getMessage("NBcallBackSheet");
		log.info("NBcallBackSheet" + res);
		return res;
	}
}