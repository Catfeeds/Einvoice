package com.einvoice.sell.rest;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.einvoice.dynamicdb.DBMultiDataSource;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.config.FConfig;
import com.einvoice.sell.service.SheetService;
import com.einvoice.sell.service.factory.SheetServiceFactory;
import com.einvoice.sell.util.Convert;
import com.einvoice.sell.util.RtnData;
import com.einvoice.sell.util.SHA1;
import com.einvoice.sell.util.SpringContextUtil;

/**
 * @author Baij 公开API调用
 */
@Controller
@RequestMapping(value = "/api")
public class ApiBJ {
	private final Log log = LogFactory.getLog(ApiBJ.class);
	
	/**
	 * 获取费用单号列表
	 */
	@RequestMapping(value = "/getBillList")
	@ResponseBody
	public String getSheetBJ(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetid") String sheetid,
			@RequestHeader(value="begdate") String begdate,
			@RequestHeader(value="enddate") String enddate,
			@RequestHeader(value="sheetname") String sheetname){
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
			List<Map<String, Object>> sheet = service.getBillListBJ(shop,entid,sheetid,begdate,enddate);
			
			return new RtnData(sheet).toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+";异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+";请求数据："+sheetname).toString();
		}
	}
	
	/**
	 * 获取费用单号明细（只取正向开具数据）
	 */
	@RequestMapping(value = "/getProvSheetBJ")
	@ResponseBody
	public String getProvSheetBJ(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetid") String sheetid,
			@RequestHeader(value="sheettype") String sheettype,
			@RequestHeader(value="sheetname") String sheetname){
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
			Map<String, Object> sheet = service.getProvSheetBJ(shop,entid,sheetid,sheettype);
			
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
	 * 获取费用单号明细（合并退货或红冲）
	 */
	@RequestMapping(value = "/getProvSheetSum")
	@ResponseBody
	public String getProvSheetSum(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetid") String sheetid,
			@RequestHeader(value="sheettype") String sheettype,
			@RequestHeader(value="sheetname") String sheetname){
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
			Map<String, Object> sheet = service.getProvSheetSum(shop,entid,sheetid,sheettype);
			
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
	 * 获取费用单号退货列表
	 */
	@RequestMapping(value = "/getProvRetList")
	@ResponseBody
	public String getProvRetList(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="begdate") String begdate,
			@RequestHeader(value="enddate") String enddate,
			@RequestHeader(value="sheetname") String sheetname){
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
			List<Map<String, Object>> sheet = service.getProvRetList(shop,entid,begdate,enddate);
			
			return new RtnData(sheet).toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname).toString();
		}
	}
	
	/**
	 * 获取小票数据（只查询正向开具数据）
	 * 
	 */
	@RequestMapping(value = "/getSheetBJ")
	@ResponseBody
	public String getSheetBJ(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetid") String sheetid,
			@RequestHeader(value="sheetname") String sheetname){
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
				shop.setLastMsg("sheetname="+sheetname+";shopid="+shopid+";sheetid="+sheetid);
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			Map<String, Object> list = service.getSheetBJ(shop,sheetid);
			
			return new RtnData(list).toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+";shopid="+shopid+";sheetid="+sheetid+";异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname+";sheetid="+sheetid).toString();
		}
	}
	
	/**
	 * 获取小票数据（合并退货或红冲数据）
	 * 
	 */
	@RequestMapping(value = "/getSheetSum")
	@ResponseBody
	public String getSheetSum(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="syjid") String syjid,
			@RequestHeader(value="billno") String billno,
			@RequestHeader(value="sheetname") String sheetname){
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
				shop.setLastMsg("sheetname="+sheetname+";shop="+shopid+";syjid="+syjid+";billno="+billno);
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			Map<String, Object> list = service.getSheetSum(shop,syjid,billno);
			
			return new RtnData(list).toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+";shop="+shopid+";syjid="+syjid+";billno="+billno+";异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+";请求数据："+sheetname+";syjid="+syjid+";billno="+billno).toString();
		}
	}
	
	/**
	 * 获取小票退货列表
	 */
	@RequestMapping(value = "/getHeadRetList")
	@ResponseBody
	public String getHeadRetList(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="begdate") String begdate,
			@RequestHeader(value="enddate") String enddate,
			@RequestHeader(value="sheetname") String sheetname){
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
			List<Map<String, Object>> sheet = service.getHeadRetList(shopid,begdate,enddate);
			
			return new RtnData(sheet).toStringDES(clientid);
		} 
		catch (ParseException ex) {
			log.error(ex.toString());
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+";异常：" + ex.getMessage());
			}
			return new RtnData(-1,ex.getMessage()+";请求数据："+sheetname).toString();
		}
		catch (Exception ex) {
			log.error(ex.toString());
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; 异常：" + ex.getMessage());
			}
			return new RtnData(-1,ex.getMessage()+"; 请求数据："+sheetname).toString();
		}
	}
	
	/**
	 * 回写费用单据状态
	 * 
	 */
	@RequestMapping(value = "/callProvSheetBJ")
	@ResponseBody
	public String callProvSheetBJ(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetid") String sheetid,
			@RequestHeader(value="sheettype") String sheettype,
			@RequestHeader(value="flag") String flag,
			@RequestHeader(value="flagmsg") String flagmsg,
			@RequestHeader(value="invoicecode") String invoiceCode,
			@RequestHeader(value="invoiceno") String invoiceNo,
			@RequestHeader(value="invoicedate") String invoiceDate,
			@RequestHeader(value="sheetname") String sheetname){
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
			
			int retFlag = service.callProvSheetBJ(shop,entid,sheetid,sheettype,flag,flagmsg,invoiceCode,invoiceNo,invoiceDate);
			
			if (retFlag > 0)
				return new RtnData().toStringDES(clientid);
			else
				return new RtnData(-1,"更新ERP已开票状态出错").toStringDES(clientid);
		} 
		catch (ParseException ex) {
			log.error(ex.toString());
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; sheetid="+sheetid+"; 异常："+ ex.getMessage());
			}
			return new RtnData(-1,ex.getMessage()+"; 请求数据："+sheetname+" "+sheetid).toString();
		}
		catch (Exception ex) {
			log.error(ex.toString());
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; sheetid="+sheetid+"; 异常："+ ex.getMessage());
			}
			return new RtnData(-1,ex.getMessage()+"; 请求数据："+sheetname+" "+sheetid).toString();
		}
		
	}
	
	/**
	 * 回写小票单据状态
	 * 
	 */
	@RequestMapping(value = "/callBackSheetBJ")
	@ResponseBody
	public String callBackSheetBJ(
			@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="sheetid") String sheetid,
			@RequestHeader(value="status") String status,
			@RequestHeader(value="invoicecode") String invoiceCode,
			@RequestHeader(value="invoiceno") String invoiceNo,
			@RequestHeader(value="invoicedate") String invoiceDate,
			@RequestHeader(value="sheetname") String sheetname){
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
			
			int retFlag = service.callBackSheetBJ(shop,sheetid,status,invoiceCode,invoiceNo,invoiceDate);

			if (retFlag > 0)
				return new RtnData().toStringDES(clientid);
			else
				return new RtnData(-1,"更新ERP已开票状态出错").toStringDES(clientid);
			
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; sheetid="+sheetid+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname+" "+sheetid).toString();
		}
	}
}
