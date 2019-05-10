package com.einvoice.sell.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.einvoice.dynamicdb.DBMultiDataSource;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.config.FConfig;
import com.einvoice.sell.service.SheetService;
import com.einvoice.sell.service.factory.SheetServiceFactory;
import com.einvoice.sell.service.impl.CommonSheetService;
import com.einvoice.sell.util.Convert;
import com.einvoice.sell.util.RtnData;
import com.einvoice.sell.util.SHA1;
import com.einvoice.sell.util.SpringContextUtil;

/**
 * @author Baij 公开API调用
 */
@Controller
@RequestMapping(value = "/api")
public class ApiRest {
	private final Log log = LogFactory.getLog(ApiRest.class);
	/**
	 * 获取小票明细
	 * 
	 * @param entid
	 * @return
	 */
	@RequestMapping(value = "/getSheet")
	@ResponseBody
	public String getSheet(@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetid") String sheetid,
			@RequestHeader(value="sheetname") String sheetname){
		log.debug("entid:"+entid+",clientid:"+clientid+",password:"+password+",time:"+time+",qr:"+sheetid);
		SpringContextUtil.getBean(FConfig.class);
		ShopConnect shop = null;
		try {
			//校验
			String localPassword = SHA1.sha1(entid+clientid+FConfig.password+time);
			if(!FConfig.entid.equals(entid)
				||!FConfig.clientid.equals(clientid)
				||!localPassword.equals(password)
				){
				return new RtnData(-99,"鉴权不通过").toString();
			}
			
			DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
			shop = FConfig.shopConnectMap.get(shopid);
			
			if(shop==null){
				throw new RuntimeException("没有这个门店定义："+shopid);
			}else{
				datasource.switchDataSource(shop);
				shop.setLastActiveDate(Convert.getNowString());
				shop.setQueryCount(shop.getQueryCount()+1);
				shop.setLastMsg("sheetname="+sheetname+"; sheetid="+sheetid);
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			Map<String, Object> sheet = service.getSheet(shop,sheetid);
			sheet.put("entid", entid);
			
			return new RtnData(sheet).toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; sheetid="+sheetid+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname+" "+sheetid).toString();
		}
		
	}
	
	/**
	 * 查询待开票单据清单
	 * @param entid
	 * @param clientid
	 * @param password
	 * @param time
	 * @param shopid
	 * @param sheetname
	 * @return
	 */
	@RequestMapping(value = "/getList")
	@ResponseBody
	public String getList(@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetname") String sheetname){
		log.debug("entid:"+entid+",clientid:"+clientid+",password:"+password+",time:"+time);
		SpringContextUtil.getBean(FConfig.class);
		ShopConnect shop = null;
		try {
			//校验
			String localPassword = SHA1.sha1(entid+clientid+FConfig.password+time);
			if(!FConfig.entid.equals(entid)
				||!FConfig.clientid.equals(clientid)
				||!localPassword.equals(password)
				){
				return new RtnData(-99,"鉴权不通过").toString();
			}
			
			DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
			shop = FConfig.shopConnectMap.get(shopid);
			
			if(shop==null){
				throw new RuntimeException("没有这个门店定义："+shopid);
			}else{
				datasource.switchDataSource(shop);
				shop.setLastActiveDate(Convert.getNowString());
				shop.setQueryCount(shop.getQueryCount()+1);
				shop.setLastMsg("sheetname="+sheetname);
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			List<Map<String, Object>> list = service.getList(shop);
			
			return new RtnData(list).toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname).toString();
		}
	}
	
	/**
	 * 回写单据状态
	 * @param entid
	 * @param clientid
	 * @param password
	 * @param time
	 * @param shopid
	 * @param sheetname
	 * @return
	 */
	@RequestMapping(value = "/callBackSheet")
	@ResponseBody
	public String callBackSheet(@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="data") String data,
			@RequestHeader(value="sheetname") String sheetname){
		log.debug("entid:"+entid+",clientid:"+clientid+",password:"+password+",time:"+time+",data:"+data);
		SpringContextUtil.getBean(FConfig.class);
		ShopConnect shop = null;
		try {
			//校验
			String localPassword = SHA1.sha1(entid+clientid+FConfig.password+time);
			if(!FConfig.entid.equals(entid)
				||!FConfig.clientid.equals(clientid)
				||!localPassword.equals(password)
				){
				return new RtnData(-99,"鉴权不通过").toString();
			}
			
			DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
			shop = FConfig.shopConnectMap.get(shopid);
			
			if(shop==null){
				throw new RuntimeException("没有这个门店定义："+shopid);
			}else{
				datasource.switchDataSource(shop);
				shop.setLastActiveDate(Convert.getNowString());
				shop.setQueryCount(shop.getQueryCount()+1);
				shop.setLastMsg("sheetname="+sheetname+"; data="+data);
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			service.callBackSheet(shop,data);
			
			return new RtnData().toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; data="+data+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname+" "+data).toString();
		}
		
	}
	
	
	@RequestMapping(value = "/getStat/{sheetname}/{shopid}/{sdate}")
	@ResponseBody
	public String getStat(@PathVariable(value = "shopid") String shopid,
			@PathVariable(value = "sheetname") String sheetname,
			@PathVariable(value = "sdate") String sdate){
		SpringContextUtil.getBean(FConfig.class);
		ShopConnect shop = null;
		try {
			
			DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
			shop = FConfig.shopConnectMap.get(shopid);
			
			if(shop==null){
				throw new RuntimeException("没有这个门店定义："+shopid);
			}else{
				datasource.switchDataSource(shop);
				shop.setLastActiveDate(Convert.getNowString());
				shop.setQueryCount(shop.getQueryCount()+1);
				shop.setLastMsg("getStat sheetname="+sheetname);
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			List<Map<String, Object>> sale = service.getStat(shop,sdate);
			HashMap<String, List<Map<String, Object>>> res = new HashMap<String, List<Map<String, Object>>>();
			res.put("sale", sale);
			List<Map<String, Object>> pay = service.getStatPay(shop,sdate);
			res.put("pay", pay);
			return new RtnData(res).toString();
		} catch (Exception e) {
			if(shop!=null){
				shop.setLastMsg("getStat sheetname="+sheetname+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,"异常："+e.getMessage()).toString();
		}
		
	}

	@RequestMapping(value = "/status")
	public void status(HttpServletResponse response) {
		SpringContextUtil.getBean(FConfig.class);
		
		response.setContentType("text/html;charset=utf-8");
		try {

			String res = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></head><body><table border=\"1\"><tr><th>门店</th><th>最近消息</th><th>最近检查时间</th><th>总计查询次数</th></tr>";
			Set<Entry<String, ShopConnect>> entrySet = FConfig.shopConnectMap.entrySet();
			for (Entry<String, ShopConnect> entry : entrySet) {
				ShopConnect shop = entry.getValue();
				res += "<tr><td>" + shop.getShopid() + "</td><td>" + shop.getLastMsg() + "</td><td>"
						+ shop.getLastActiveDate() + "</td><td>" + shop.getQueryCount() + "</td></tr>";
			}
			res += "</table></body></html>";
			response.getWriter().print(res);
		} catch (IOException e) {
		}
	}
	
	@RequestMapping(value = "/reload")
	@ResponseBody
	public String reload(HttpServletResponse response) {
		FConfig config = SpringContextUtil.getBean(FConfig.class);
		config.initShopConnectMap();
		return new RtnData(FConfig.shopConnectMap).toString();
	}
	
	@RequestMapping(value = "/status/shop/{shopid}")
	@ResponseBody
	public String statusByShopid(@PathVariable String shopid) {
		if(StringUtils.isEmpty(shopid)) return new RtnData(-1,"异常：需要门店ID").toString();
		SpringContextUtil.getBean(FConfig.class);
		Set<Entry<String, ShopConnect>> entrySet = FConfig.shopConnectMap.entrySet();
		for (Entry<String, ShopConnect> entry : entrySet) {
			ShopConnect shop = entry.getValue();
			if(shopid.equals(shop.getShopid())) {
				return new RtnData(shop).toString();
			}
		}
		return new RtnData(-1,"异常：门店找不到："+shopid).toString();
	}
	
	@RequestMapping(value = "/status/connect/{connectid}")
	@ResponseBody
	public String statusByConnectid(@PathVariable String connectid) {
		if(StringUtils.isEmpty(connectid)) return new RtnData(-1,"异常：需要连接ID").toString();
		
		SpringContextUtil.getBean(FConfig.class);
		Set<Entry<String, ShopConnect>> entrySet = FConfig.shopConnectMap.entrySet();
		for (Entry<String, ShopConnect> entry : entrySet) {
			ShopConnect shop = entry.getValue();
			if(connectid.equals(shop.getConnectid())) {
				return new RtnData(shop).toString();
			}
		}
		return new RtnData(-1,"异常：连接找不到："+connectid).toString();
	}
	
	@RequestMapping(value = "/getZH/{shopid}")
	@ResponseBody
	public String getZH(@PathVariable String shopid,@RequestParam(required=false) Integer page) {
		DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
		ShopConnect shop = FConfig.shopConnectMap.get(shopid);
		if(shop==null){
			throw new RuntimeException("没有这个门店定义："+shopid);
		}else{
			datasource.switchDataSource(shop);
		}
		
		SheetService s = SpringContextUtil.getBean("common",SheetService.class);
		return new RtnData(s.getZH(shop,page)).toString();
	}
	@RequestMapping(value = "/getHT/{shopid}/{gysid}")
	@ResponseBody
	public String getHT(@PathVariable String shopid,@PathVariable String gysid,@RequestParam(required=false) Integer page) {
		DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
		ShopConnect shop = FConfig.shopConnectMap.get(shopid);
		if(shop==null){
			throw new RuntimeException("没有这个门店定义："+shopid);
		}else{
			datasource.switchDataSource(shop);
		}
		SheetService s = SpringContextUtil.getBean("common",SheetService.class);
		return new RtnData(s.getHT(shop,gysid,page)).toString();
	}
}
