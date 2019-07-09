package com.einvoice.sell.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.dao.BillDao;
import com.einvoice.sell.util.MathCal;
import com.einvoice.sell.util.NewHashMap;

@Service("bill")
public class BillService extends BaseService {
	private final Log log = LogFactory.getLog(BillService.class);

	@Autowired
	BillDao dao;

	public Map<String, Object> getSheet(ShopConnect shop, String sheetid) {
		Map<String, String> p = cookParams(sheetid);

		List<Map<String, Object>> headList = dao.getHead(p);

		if (headList == null || headList.isEmpty()) {
			throw new RuntimeException("没有找到数据");
		}

		Map<String, Object> head = null;
		if (headList.size() == 1 || p.get("amt") == null) {
			head = headList.get(0);
		} else {
			double amt = MathCal.div(Double.parseDouble(p.get("amt").toString()), 100, 2);
			for (Map<String, Object> map : headList) {
				if (Double.parseDouble(map.get("amt").toString()) == amt) {
					head = map;
				}
			}
		}

		if (head == null) {
			throw new RuntimeException("没有匹配的数据");
		}

		String billno = head.get("sheetid").toString();
		p.put("sheetid", billno);
		List<Map<String, Object>> selldetail = dao.getDetail(p);

		if (selldetail == null)
			throw new RuntimeException("明细信息缺失");

		List<Map<String, Object>> sellpayment = dao.getPayment(p);

		if (sellpayment == null)
			throw new RuntimeException("支付信息缺失");

		// 商品名称字符集处理
		String charcode = shop.getDbcharcode();
		if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode)
				&& !"UTF-8".equalsIgnoreCase(charcode)) {
			for (Map<String, Object> map : selldetail) {
				Object obj = map.get("itemname");
				if (obj != null) {
					try {
						map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}

				obj = map.get("unit");
				if (obj != null) {
					try {
						map.put("unit", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}
			}
		}

		// 检查有没有退货商品
		p.put("refsheetid", billno);
		List<Map<String, Object>> retselldetail = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> retsellPay = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> retHeadList = dao.getHeadRet(p);
		if (retHeadList != null && !retHeadList.isEmpty()) {
			for (Map<String, Object> map : retHeadList) {
				String retbillno = map.get("sheetid").toString();
				p.put("sheetid", retbillno);
				retselldetail.addAll(dao.getDetail(p));
				retsellPay.addAll(dao.getPayment(p));
			}

			// 增加记录原小票退货余额列
			for (Map<String, Object> map : selldetail) {
				double qty = Double.parseDouble(map.get("qty").toString());
				double amt = Double.parseDouble(map.get("amt").toString());
				map.put("qty_u", qty);
				map.put("amt_u", amt);
			}
		}

		// 退货商品从原小票中剔除
		for (Map<String, Object> retmap : retselldetail) {
			String retitemid = retmap.get("itemid").toString();
			double retqty = Double.parseDouble(retmap.get("qty").toString());
			if (retqty == 0)
				retqty = 1;
			double retamt = Double.parseDouble(retmap.get("amt").toString());
			double retprice = MathCal.div(retamt, retqty, 2);

			// 先找单价一致的商品
			for (Map<String, Object> map : selldetail) {
				String itemid = map.get("itemid").toString();
				if (itemid.equals(retitemid) && retamt > 0) {
					double qty = Double.parseDouble(map.get("qty").toString());
					if (qty == 0)
						qty = 1;
					double amt = Double.parseDouble(map.get("amt").toString());
					double price = MathCal.div(amt, qty, 2);
					if (retprice == price) {
						if (retamt > amt) {
							map.put("qty", 0);
							map.put("amt", 0);
							retamt = MathCal.sub(retamt, amt, 2);
						} else {
							map.put("qty", MathCal.sub(qty, retqty, 5));
							map.put("amt", MathCal.sub(amt, retamt, 2));
							retamt = 0;
						}
					}

				}
			}

			// 如果退货金额还有则再次循环
			if (retamt > 0) {
				for (Map<String, Object> map : selldetail) {
					String itemid = map.get("itemid").toString();
					double amt = Double.parseDouble(map.get("amt").toString());
					if (itemid.equals(retitemid) && retamt > 0 && amt > 0) {
						double qty = Double.parseDouble(map.get("qty").toString());
						if (retamt > amt) {
							map.put("qty", 0);
							map.put("amt", 0);
							retamt = MathCal.sub(retamt, amt, 2);
						} else {
							map.put("qty", MathCal.sub(qty, retqty, 5));
							map.put("amt", MathCal.sub(amt, retamt, 2));
							retamt = 0;
						}
					}
				}
			}

		}

		// 从退货商品中剔除支付方式
		for (Map<String, Object> retmap : retsellPay) {
			String retpayid = retmap.get("payid").toString();
			// 退货金额
			double retamt = Double.parseDouble(retmap.get("amt").toString());
			for (Map<String, Object> map : sellpayment) {
				String payid = map.get("payid").toString();
				if (payid.equals(retpayid)) {
					double amt = Double.parseDouble(map.get("amt").toString());

					// 如果退货支付金额大于原支付金额，则原支付金额修改为0，同时记录下退货支付余额
					if (retamt > amt) {
						map.put("amt", 0);
						retamt = MathCal.sub(retamt, amt, 2);
					} else {
						map.put("amt", MathCal.sub(amt, retamt, 2));
						retamt = 0;
					}
				}
			}
		}

		head.put("sheetdetail", selldetail);
		head.put("sheetpayment", sellpayment);

		return head;
	}

	public Map<String, String> cookParams(String sheetid) {
		if (sheetid == null)
			throw new RuntimeException("无法解析小票信息");
		NewHashMap<String, String> map = new NewHashMap<String, String>();
		if (sheetid.contains("-")) {
			String[] ss = sheetid.split("-");
			if (ss.length < 3)
				throw new RuntimeException("无法解析小票信息");

			try {
				map.put("shopid", ss[0]);
				map.put("syjid", ss[1]);
				map.put("billno", ss[2]);
				if (ss.length > 3) {
					map.put("amt", ss[3]);
				}
				return map;
			} catch (Exception e) {
				log.error(e, e);
				throw new RuntimeException("无法解析小票信息");
			}
		} else {
			if (sheetid.length() <= 8) {
				throw new RuntimeException("无法解析小票信息:" + sheetid);
			}
			// 尝试根据位数截取
			String shopid = sheetid.substring(0, 4);
			String syjid = sheetid.substring(4, 8);
			String billno = sheetid.substring(8);
			map.put("shopid", shopid);
			map.put("syjid", syjid);
			map.put("billno", billno);
			return map;
		}
	}

	@Override
	public List<Map<String, Object>> getStat(ShopConnect shop, String sdate) {
		Map<String, String> p = new NewHashMap<String, String>();
		p.put("shopid", shop.getShopid());
		p.put("sdate", sdate);
		List<Map<String, Object>> list = null;

		if ("101".equals(shop.getShoptype())) {
			list = dao.getStat101(p);
		} else if ("102".equals(shop.getShoptype())) {
			list = dao.getStat102(p);
		} else if ("103".equals(shop.getShoptype())) {
			list = dao.getStat103(p);
		} else {
			throw new RuntimeException("门店类型未设置");
		}
		// 商品名称字符集处理
		String charcode = shop.getDbcharcode();
		if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode)
				&& !"UTF-8".equalsIgnoreCase(charcode)) {
			for (Map<String, Object> map : list) {
				Object obj = map.get("itemname");
				if (obj != null) {
					try {
						map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getStatPay(ShopConnect shop, String sdate) {
		Map<String, String> p = new NewHashMap<String, String>();
		p.put("shopid", shop.getShopid());
		p.put("sdate", sdate);
		return dao.getStatPay(p);
	}

	@Override
	public List<Map<String, Object>> getList(ShopConnect shop) {
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("shopid", shop.getShopid());
		List<Map<String, Object>> list = dao.getList(p);

		// 商品名称字符集处理
		String charcode = shop.getDbcharcode();
		if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode)
				&& !"UTF-8".equalsIgnoreCase(charcode)) {
			for (Map<String, Object> map : list) {
				Object obj = map.get("gmfname");
				if (!StringUtils.isEmpty(obj)) {
					try {
						map.put("gmfname", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}

				obj = map.get("gmftax");
				if (!StringUtils.isEmpty(obj)) {
					try {
						map.put("gmftax", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}

				obj = map.get("gmfadd");
				if (!StringUtils.isEmpty(obj)) {
					try {
						map.put("gmfadd", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}

				obj = map.get("gmfbank");
				if (!StringUtils.isEmpty(obj)) {
					try {
						map.put("gmfbank", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}

				obj = map.get("remark");
				if (!StringUtils.isEmpty(obj)) {
					try {
						map.put("remark", new String(obj.toString().getBytes(charcode), "GBK"));
					} catch (UnsupportedEncodingException e) {
						log.error(e);
					}
				}
			}
		}

		return list;
	}

	@Override
	public int callBackSheet(ShopConnect shop, String data) {
		try {
			data = URLDecoder.decode(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Map<String, Object> map = callBackMap(data);

		log.info(JSONObject.toJSON(map));

		int rows = dao.callbackSheet(map);
		if (rows == 0) {
			throw new RuntimeException("更新记录失败，影响行为零：" + JSONObject.toJSON(map));
		}
		return rows;
	}

}
