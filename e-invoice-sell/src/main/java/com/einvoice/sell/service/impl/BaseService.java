package com.einvoice.sell.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.service.SheetService;
import com.einvoice.sell.util.NewHashMap;

public abstract class BaseService implements SheetService {
	@Override
	public List<Map<String, Object>> getList(ShopConnect shop){return null;}
	
	@Override
	public int callBackSheet(ShopConnect shop, String data){return 0;}
	
	@Override
	public List<Map<String, Object>> getgoodsinfo(int startrow,int endrow){return null;}
	
	@Override
	public int NBcallBackSheet(ShopConnect shop, String data){return 0;}
	
	@Override
	public List<Map<String, Object>> getStat(ShopConnect shop, String sdate){return null;}
	
	@Override
	public List<Map<String, Object>> getStatPay(ShopConnect shop, String sdate){return null;}
	
	@Override
	public List<Map<String, Object>> getZH(ShopConnect shop,Integer page){return null;}
	
	@Override
	public List<Map<String, Object>> getHT(ShopConnect shop,String gysid,Integer page){return null;}
	
	protected Map<String, Object> callBackMap(String data){
		if(StringUtils.isEmpty(data)) {
			throw new RuntimeException("回调信息空");
		}
	
		JSONObject p = JSONObject.parseObject(data);
		p.put("updatetime", new Date());
		
		Map<String, Object> map = new NewHashMap<String, Object>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		map.putAll(p);
		
		if(map.get("invoiceDate")!=null) {
			try {
				map.put("invoiceDate", sf.parse(map.get("invoiceDate").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(map.get("hzinvoiceDate")!=null) {
			try {
				map.put("hzinvoiceDate", sf.parse(map.get("hzinvoiceDate").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return map;
	}
	
	/**
	 * 以下是阪急定制
	 */
	@Override
	public Map<String,Object> getSheetBJ(ShopConnect shop,String sheetid){return null;}
	
	@Override
	public Map<String,Object> getSheetSum(ShopConnect shop,String syjid,String billno){return null;}
	
	@Override
	public List<Map<String, Object>> getBillListBJ(ShopConnect shop,String entid,String sheetid,String begdate,String enddate){return null;}
	
	@Override
	public Map<String,Object> getProvSheetBJ(ShopConnect shop,String entid,String sheetid,String sheettype){return null;}
	
	@Override
	public Map<String,Object> getProvSheetSum(ShopConnect shop,String entid,String sheetid,String sheettype){return null;}
	
	@Override
	public List<Map<String, Object>> getProvRetList(ShopConnect shop,String entid,String begdate,String enddate){return null;}
	
	@Override
	public List<Map<String, Object>> getHeadRetList(String shopid,String begdate,String enddate){return null;}
	
	@Override
	public int callProvSheetBJ(ShopConnect shop,String entid,String sheetid,String sheettype,String flag,String flagmsg,String invoicecode,String invoiceno,String invoicedate){return 0;}
	
	@Override
	public int callBackSheetBJ(ShopConnect shop,String sheetid,String status,String invoicecode,String invoiceno,String invoicedate){return 0;}
}
