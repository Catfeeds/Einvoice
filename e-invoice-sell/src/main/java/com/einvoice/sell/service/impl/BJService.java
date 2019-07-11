package com.einvoice.sell.service.impl;

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
import com.einvoice.sell.dao.BJDao;
import com.einvoice.sell.util.MathCal;
import com.einvoice.sell.util.NewHashMap;

@Service("NBBJ")
public class BJService extends BaseService {
	private final Log log = LogFactory.getLog(BJService.class);
	String loginfo = "";
	
	@Autowired
	BJDao dao;
	
	/**
	 * 获取费用单据列表
	 */
	@Override
	public List<Map<String, Object>> getBillListBJ(ShopConnect shop,String entid,String sheetid,String begdate,String enddate) {
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("entid", entid);
		p.put("shopid", shop.getShopid());
		p.put("sheetid", sheetid);
		p.put("begdate", begdate);
		p.put("enddate", enddate);
		
		List<Map<String, Object>> headList = dao.getProvHead(p);
		
		// 字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : headList) {
					obj = map.get("gmfname");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("gmfadd");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfadd", new String(obj.toString().getBytes(charcode), "GBK"));
	
					}
	
					obj = map.get("gmfbank");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfbank", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("remark");
					if (!StringUtils.isEmpty(obj)) {
						map.put("remark", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}

		return headList;
	}
	
	/**
	 * 获取费用单据明细（不合并退货或红冲）
	 */
	@Override
	public Map<String, Object> getProvSheetBJ(ShopConnect shop,String entid,String sheetid,String sheettype) {
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("entid", entid);
		p.put("shopid", shop.getShopid());
		p.put("sheetid", sheetid);
		p.put("sheettype", sheettype);
		
		List<Map<String, Object>> headList = dao.getProvHead(p);

		if (headList == null || headList.isEmpty()) {
			loginfo = "阪急－没有找到费用单据:" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		// 字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : headList) {
					obj = map.get("gmfname");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("gmfadd");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfadd", new String(obj.toString().getBytes(charcode), "GBK"));
	
					}
	
					obj = map.get("gmfbank");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfbank", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("remark");
					if (!StringUtils.isEmpty(obj)) {
						map.put("remark", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}
		
		Map<String, Object> head = null;
		
		head = headList.get(0);

		if (head == null) {
			loginfo = "阪急－没有找到费用数据:" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		List<Map<String, Object>> selldetail = dao.getProvDetail(p);
		
		if (selldetail == null){
			loginfo = "阪急－费用明细信息缺失:" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		// 商品名称字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : selldetail) {
					obj = map.get("itemname");
					if (obj != null) {
						map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
					obj = map.get("unit");
					if (obj != null) {
						map.put("unit", new String(obj.toString().getBytes(charcode), "GBK"));
					}
					obj = map.get("spec");
					if (obj != null) {
						map.put("spec", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}
		
		head.put("sheetdetail", selldetail);
		
		return head;
	}
	
	/**
	 * 获取费用单据明细（合并退货或红冲）
	 */
	@Override
	public Map<String, Object> getProvSheetSum(ShopConnect shop,String entid,String sheetid,String sheettype) {
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("entid", entid);
		p.put("shopid", shop.getShopid());
		p.put("sheetid", sheetid);
		p.put("sheettype", sheettype);
		
		List<Map<String, Object>> headList = dao.getProvHead(p);

		if (headList == null || headList.isEmpty()) {
			loginfo = "阪急－没有找到费用单据:" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		// 字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : headList) {
					obj = map.get("gmfname");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("gmfadd");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfadd", new String(obj.toString().getBytes(charcode), "GBK"));
	
					}
	
					obj = map.get("gmfbank");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfbank", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("remark");
					if (!StringUtils.isEmpty(obj)) {
						map.put("remark", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}
		
		Map<String, Object> head = null;
		
		head = headList.get(0);

		if (head == null) {
			loginfo = "阪急－没有找到费用数据:" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		List<Map<String, Object>> selldetail = dao.getProvDetail(p);
		
		if (selldetail == null){
			loginfo = "阪急－费用明细信息缺失:" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		// 商品名称字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : selldetail) {
					obj = map.get("itemname");
					if (obj != null) {
						map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
					obj = map.get("unit");
					if (obj != null) {
						map.put("unit", new String(obj.toString().getBytes(charcode), "GBK"));
					}
					obj = map.get("spec");
					if (obj != null) {
						map.put("spec", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}
		
		// 检查有没有退货商品
		p.put("refsheetid", sheetid);
		
		List<Map<String, Object>> retselldetail = new ArrayList<Map<String, Object>>();
		
		List<Map<String, Object>> retHeadList = dao.getProvRet(p);
		
		if (retHeadList != null && !retHeadList.isEmpty()) {
			for (Map<String, Object> map : retHeadList) {
				String retbillno = map.get("sheetid").toString();
				p.put("sheetid", retbillno);
				retselldetail.addAll(dao.getDetail(p));
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
			if (retqty == 0) retqty = 1;
			double retamt = Double.parseDouble(retmap.get("amt").toString());
			double retprice = MathCal.div(retamt, retqty, 2);

			// 先找单价一致的商品
			for (Map<String, Object> map : selldetail) {
				String itemid = map.get("goodsid").toString();
				if (itemid.equals(retitemid) && retamt > 0) {
					double qty = Double.parseDouble(map.get("qty").toString());
					if (qty == 0) qty = 1;
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
					String itemid = map.get("goodsid").toString();
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
		
		head.put("sheetdetail", selldetail);
		
		return head;
	}
	
	/**
	 * 获取费用单退货列表
	 */
	@Override
	public List<Map<String, Object>> getProvRetList(ShopConnect shop,String entid,String begdate,String enddate)
	{
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("entid", entid);
		p.put("shopid", shop.getShopid());
		p.put("begdate", begdate);
		p.put("enddate", enddate);
		
		List<Map<String, Object>> headList = dao.getProvRet(p);
		
		// 字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : headList) {
					obj = map.get("gmfname");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("gmfadd");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfadd", new String(obj.toString().getBytes(charcode), "GBK"));
	
					}
	
					obj = map.get("gmfbank");
					if (!StringUtils.isEmpty(obj)) {
						map.put("gmfbank", new String(obj.toString().getBytes(charcode), "GBK"));
					}
	
					obj = map.get("remark");
					if (!StringUtils.isEmpty(obj)) {
						map.put("remark", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}

		return headList;
	}
	
	/**
	 * 获取小票单据退货列表
	 */
	@Override
	public List<Map<String, Object>> getHeadRetList(String shopid,String begdate,String enddate)
	{
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("shopid", shopid);
		p.put("begdate", begdate);
		p.put("enddate", enddate);
		
		List<Map<String, Object>> list = dao.getHeadRet(p);

		return list;
	}
	
	/**
	 * 获取小票明细，合并退货数据
	 */
	@Override
	public Map<String, Object> getSheetSum(ShopConnect shop, String syjid,String billno) {
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("shopid", shop.getShopid());
		p.put("syjid", syjid);
		p.put("billno", billno);
		
		List<Map<String, Object>> headList = dao.getHead(p);

		if (headList == null || headList.size() == 0) {
			loginfo = "阪急－没有找到小票数据:" + shop.getShopid() + "-" + syjid + "-" + billno;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}

		Map<String, Object> head = null;
		
		head = headList.get(0);

		if (head == null) {
			loginfo = "阪急－小票头数据为空:" + shop.getShopid() + "-" + syjid + "-" + billno;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		p.put("sheetid", head.get("sheetid").toString());
		
		List<Map<String, Object>> selldetail = dao.getDetail(p);
		
		if (selldetail == null){
			loginfo = "阪急－小票明细数据缺失:" + shop.getShopid() + "-" + syjid + "-" + billno;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}

		List<Map<String, Object>> sellpayment = dao.getPayment(p);
		
		if (sellpayment == null){
			loginfo = "阪急－小票支付数据缺失:" + shop.getShopid() + "-" + syjid + "-" + billno;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}

		// 商品名称字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : selldetail) {
					obj = map.get("itemname");
					if (obj != null) {
						map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
					obj = map.get("unit");
					if (obj != null) {
						map.put("unit", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}

		// 检查有没有退货商品
		p.put("refsheetid", head.get("sheetid").toString());
		
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
			if (retqty == 0) retqty = 1;
			double retamt = Double.parseDouble(retmap.get("amt").toString());
			double retprice = MathCal.div(retamt, retqty, 2);

			// 先找单价一致的商品
			for (Map<String, Object> map : selldetail) {
				String itemid = map.get("itemid").toString();
				if (itemid.equals(retitemid) && retamt > 0) {
					double qty = Double.parseDouble(map.get("qty").toString());
					if (qty == 0) qty = 1;
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

	/**
	 * 获取小票明细数据，不合并退货数据
	 */
	@Override
	public Map<String,Object> getSheetBJ(ShopConnect shop,String sheetid)
	{
		NewHashMap<String, String> p = new NewHashMap<String, String>();
		p.put("sheetid", sheetid);
		
		List<Map<String, Object>> headList = dao.getSheet(p);

		if (headList == null || headList.isEmpty()) {
			loginfo = "阪急－没有找到小票数据:" + shop.getShopid() + "-" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}

		Map<String, Object> head = null;
		
		head = headList.get(0);

		if (head == null) {
			loginfo = "阪急－小票头数据为空:" + shop.getShopid() + "-" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}
		
		List<Map<String, Object>> selldetail = dao.getDetail(p);
		
		if (selldetail == null){
			loginfo = "阪急－小票明细数据缺失:" + shop.getShopid() + "-" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}

		List<Map<String, Object>> sellpayment = dao.getPayment(p);
		
		if (sellpayment == null){
			loginfo = "阪急－小票支付数据缺失:" + shop.getShopid() + "-" + sheetid;
			log.info(loginfo);
			throw new RuntimeException(loginfo);
		}

		// 商品名称字符集处理
		try {
			String charcode = shop.getDbcharcode();Object obj = null;
			if (!StringUtils.isEmpty(charcode) && !"GBK".equalsIgnoreCase(charcode) && !"UTF-8".equalsIgnoreCase(charcode)) {
				for (Map<String, Object> map : selldetail) {
					obj = map.get("itemname");
					if (obj != null) {
						map.put("itemname", new String(obj.toString().getBytes(charcode), "GBK"));
					}
					obj = map.get("unit");
					if (obj != null) {
						map.put("unit", new String(obj.toString().getBytes(charcode), "GBK"));
					}
				}
			}
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
		}

		head.put("sheetdetail", selldetail);
		head.put("sheetpayment", sellpayment);

		return head;
	}
	
	// 小票状态回传
	@Override
	public int callBackSheetBJ(ShopConnect shop,String sheetid,String status,String invoicecode,String invoiceno,String invoicedate) {
		try {
			Map<String, Object> map = new NewHashMap<String, Object>();

			map.put("shopid", shop.getShopid());
			map.put("sheetid", sheetid);
			map.put("invoicecode", invoicecode);
			map.put("invoiceno", invoiceno);
			map.put("invoicedate", invoicedate);
			map.put("status", status);

			int rows = dao.callBackSheetBJ(map);
			
			if (rows == 0) throw new RuntimeException("阪急－更新传输状态出错：" + JSONObject.toJSON(map));

			return rows;
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
			
			return 0;
		}
	}
	
	// 费用状态回传
	@Override
	public int callProvSheetBJ(ShopConnect shop,String entid,String sheetid,String sheettype,String flag,String flagmsg,String invoicecode,String invoiceno,String invoicedate) {
		try {
			Map<String, Object> map = new NewHashMap<String, Object>();

			map.put("entid", entid);
			map.put("shopid", shop.getShopid());
			map.put("sheetid", sheetid);
			map.put("sheettype", sheettype);
			map.put("invoicecode", invoicecode);
			map.put("invoiceno", invoiceno);
			map.put("invoicedate", invoicedate);
			map.put("flag", flag);
			map.put("flagmsg", flagmsg);
			
			int rows = dao.callProvSheetBJ(map);
			
			if (rows == 0) throw new RuntimeException("阪急－更新传输状态出错：" + JSONObject.toJSON(map));

			return rows;
		}
		catch(Exception ex)
		{
			log.error(ex.toString());
			
			return 0;
		}
	}

	@Override
	public Map<String, String> cookParams(String sheetid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getSheet(ShopConnect shop, String sheetid) {
		// TODO Auto-generated method stub
		return null;
	}
}
