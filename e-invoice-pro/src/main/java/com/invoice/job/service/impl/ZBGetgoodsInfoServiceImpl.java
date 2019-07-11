package com.invoice.job.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.ZBGoodInfoDao;
import com.invoice.apiservice.service.impl.PrivateparaService;
import com.invoice.apiservice.service.impl.ZbPrivateparaService;
import com.invoice.bean.db.ZbCClient;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.Shop;
import com.invoice.bean.db.ZBGoodsinfo;
import com.invoice.job.dao.JobDao;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.uiservice.service.ZbShopService;

@Service
public class ZBGetgoodsInfoServiceImpl {
	public final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	JobDao jobDao;

	@Autowired
	ZbPrivateparaService privateparaService;

	@Autowired
	EnterpriseService enterpriseService;

	@Autowired
	ZbShopService shopService;

	@Autowired
	ZBGoodInfoDao zbgoodinfodao;

	public void execute(String entid, String shopid) throws Exception {
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
			ZbCClient client = privateparaService.getClientUrlByShopid(entid, sid);
			if (client == null) {
				log.error("门店未配置：" + sid);
				continue;
			}
			client.initHeadMap();

			int success = executeGetgoodsinfo(client, shop);
			if (success == 0) {
				break;
			} else {
				continue;
			}
		}

	}

	public int executeGetgoodsinfo(ZbCClient client, Shop shop) {
		int startrow = 0, endrow = 5000, success = 0;
		try {
			log.info("startrow:" + startrow + "------endrow:" + endrow + "");
			while (true) {
				String res = GetgoodsInfoSheet(client, shop, startrow, endrow);
				JSONObject rj = JSONObject.parseObject(res);
				if (rj.getInteger("code") == 0) {
					List<ZBGoodsinfo> list1 = new ArrayList<ZBGoodsinfo>();
					JSONArray jsonArray1 = JSONArray.parseArray(rj.getString("data"));
					for (int i = 0; i < jsonArray1.size(); i++) {
						String str = jsonArray1.getJSONObject(i).toString();
						ZBGoodsinfo cust = (ZBGoodsinfo) JSONObject.parseObject(str, ZBGoodsinfo.class);
						list1.add(cust);
					}
					if (list1 != null && !list1.isEmpty()) {
						zbgoodinfodao.insertGoodsInfo(list1);
						startrow = endrow + 1;
						endrow = endrow + 5000;
						log.info("startrow:" + startrow + "------endrow:" + endrow + "");
					} else {
						log.info("GetgoodsEnd");
						success = 0;
						break;
					}

				} else {
					success = 1;
					break;
				}
			}

		} catch (Exception e) {
			log.error(e);
		}
		return success;

	}

	private String GetgoodsInfoSheet(ZbCClient client, Shop shop, int startrow, int endrow) {
		String shopid = shop.getShopid();
		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetname", "ZB");
		headMap.put("startrow", String.valueOf(startrow));
		headMap.put("endrow", String.valueOf(endrow));
		client.setHeadMap(headMap);
		String res = client.getMessage("ZBGetgoodsInfo");
		return res;
	}

	
}