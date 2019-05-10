package com.einvoice.sell.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.einvoice.dynamicdb.DBMultiDataSource;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.config.NConfig;
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
public class ApiNB {
	private final Log log = LogFactory.getLog(ApiNB.class);
	
	
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
	@RequestMapping(value = "/NBcallBackSheet")
	@ResponseBody
	public String NBcallBackSheet(@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="data") String data,
			@RequestHeader(value="sheetname") String sheetname){
		log.debug("entid:"+entid+",clientid:"+clientid+",password:"+password+",time:"+time+",data:"+data);
		SpringContextUtil.getBean(NConfig.class);
		ShopConnect shop = null;
		try {
			//校验
			String localPassword = SHA1.sha1(entid+clientid+NConfig.password+time);
			if(!NConfig.entid.equals(entid)
				||!NConfig.clientid.equals(clientid)
				||!localPassword.equals(password)
				){
				return new RtnData(-99,"鉴权不通过").toString();
			}
			
			DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
			shop = NConfig.shopConnectMap.get(shopid);
			
			if(shop==null){
				throw new RuntimeException("没有这个门店定义："+shopid);
			}else{
				datasource.switchDataSource(shop);
				shop.setLastActiveDate(Convert.getNowString());
				shop.setQueryCount(shop.getQueryCount()+1);
				shop.setLastMsg("sheetname="+sheetname+"; data="+data);
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			service.NBcallBackSheet(shop,data);
			
			return new RtnData().toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; data="+data+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname+" "+data).toString();
		}
		
	}
	
}
