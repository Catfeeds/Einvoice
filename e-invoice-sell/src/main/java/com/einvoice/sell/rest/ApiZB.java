package com.einvoice.sell.rest;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.einvoice.dynamicdb.DBMultiDataSource;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.config.ZBConfig;
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
public class ApiZB {
	private final Log log = LogFactory.getLog(ApiZB.class);
	
	
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
	@RequestMapping(value = "/ZBGetgoodsInfo")
	@ResponseBody
	public String ZBGetgoodsInfo(@RequestHeader(value="entid") String entid,
			@RequestHeader(value="clientid") String clientid,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="time") String time,
			@RequestHeader(value="shopid") String shopid,
			@RequestHeader(value="startrow") String startrow,
			@RequestHeader(value="endrow") String endrow,
			@RequestHeader(value="sheetname") String sheetname){
		log.debug("entid:"+entid+",clientid:"+clientid+",password:"+password+",time:"+time+",row:"+startrow+"-"+endrow+"");
		SpringContextUtil.getBean(ZBConfig.class);
		ShopConnect shop = null;
		try {
			//校验
			String localPassword = SHA1.sha1(entid+clientid+ZBConfig.password+time);
			if(!ZBConfig.entid.equals(entid)
				||!ZBConfig.clientid.equals(clientid)
				||!localPassword.equals(password)
				){
				return new RtnData(-99,"鉴权不通过").toString();
			}
			
			DBMultiDataSource datasource = SpringContextUtil.getBean("dynamicdb");
			shop = ZBConfig.shopConnectMap.get(shopid);
			
			if(shop==null){
				throw new RuntimeException("没有这个门店定义："+shopid);
			}else{
				datasource.switchDataSource(shop);
				shop.setLastActiveDate(Convert.getNowString());
				shop.setQueryCount(shop.getQueryCount()+1);
				shop.setLastMsg("sheetname="+sheetname+"");
			}
			
			SheetService service = SheetServiceFactory.getInstance(sheetname);
			List<Map<String, Object>> list=service.getgoodsinfo(Integer.valueOf(startrow),Integer.valueOf(endrow));
			String charcode = shop.getDbcharcode();
		    if ((!StringUtils.isEmpty(charcode)) && (!"GBK".equalsIgnoreCase(charcode)) && (!"UTF-8".equalsIgnoreCase(charcode))) {
		      for (Map<String, Object> map : list)
		      {
		        Object obj = map.get("VCNAME");
		        if (obj != null) {
		          try
		          {
		            map.put("VCNAME", new String(obj.toString().getBytes(charcode), "GBK"));
		          }
		          catch (UnsupportedEncodingException e)
		          {
		            this.log.error(e);
		          }
		        }
		        obj = map.get("VUNIT");
		        if (obj != null) {
		          try
		          {
		            map.put("VUNIT", new String(obj.toString().getBytes(charcode), "GBK"));
		          }
		          catch (UnsupportedEncodingException e)
		          {
		            this.log.error(e);
		          }
		        }
		      }
		    }
			
			return new RtnData(list).toStringDES(clientid);
		} catch (Exception e) {
			log.error(e);
			if(shop!=null){
				shop.setLastMsg("sheetname="+sheetname+"; 异常："+e.getMessage());
			}
			return new RtnData(-1,e.getMessage()+"; 请求数据："+sheetname+" ").toString();
		}
		
	}
	
}
