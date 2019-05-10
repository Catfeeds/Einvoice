package com.einvoice.sell.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.einvoice.dynamicdb.DBMultiDataSource;
import com.einvoice.sell.bean.ShopConnect;
import com.einvoice.sell.dao.TKDao;
import com.einvoice.sell.util.Convert;
import com.einvoice.sell.util.DESedeAPIUtil;
import com.einvoice.sell.util.HttpClientCommon;
import com.einvoice.sell.util.NewHashMap;
import com.einvoice.sell.util.PropertiesUtils;
import com.einvoice.sell.util.SHA1;
import com.einvoice.sell.util.SpringContextUtil;

@Lazy
@Component
public class TKConfig implements InitializingBean {
	@Autowired
	TKDao dao;
	
	@Autowired
	SpringContextUtil springContextUtil;
	
	private final Log log = LogFactory.getLog(TKConfig.class);

	private static final String CONFIG_FILE = "config.properties";

	public static String clientid;
	public static String entid;
	public static String password;
	public static String service;

	/**
	 * key shopid value connect info
	 */
	public final static NewHashMap<String, ShopConnect> shopConnectMap = new NewHashMap<String, ShopConnect>();;

	@Override
	public void afterPropertiesSet() throws Exception {
		entid = getProperties(CONFIG_FILE, "entid", "");
		clientid = getProperties(CONFIG_FILE, "clientid", "");
		password = getProperties(CONFIG_FILE, "password", "");
		service = getProperties(CONFIG_FILE, "service", "");

		if (StringUtils.isEmpty(service)) {
			log.error("必须配置参数：service");
		}
		if (StringUtils.isEmpty(password)) {
			log.error("必须配置参数：password");
		}
		if (StringUtils.isEmpty(clientid)) {
			log.error("必须配置参数：clientid");
		}
		if (StringUtils.isEmpty(entid)) {
			log.error("必须配置参数：entid");
		}
		initShopConnectMap();
	}
	
	public void initShopConnectMap(){
		log.info("向服务器读取配置信息");
		JSONArray jshop = getShopConnectList();
		
		if(jshop==null){
			log.info("读取配置信息失败");
			return;
		}
		
		for (int i = 0; i < jshop.size(); i++) {
			JSONObject jo = jshop.getJSONObject(i);
			String client = jo.getString("clientid");
			String ent = jo.getString("entid");
			if(!clientid.equals(client) || !entid.equals(ent)){
				continue;
			}
			
			ShopConnect shop = JSONObject.toJavaObject(jo, ShopConnect.class);
			log.info("获取到门店："+shop.getShopid());
			shopConnectMap.put(shop.getShopid(), shop);
			
			try {
				log.info("验证门店数据库："+shop.getShopid());
				@SuppressWarnings("static-access")
				DBMultiDataSource datasource = springContextUtil.getBean("dynamicdb");
				datasource.removeDataSource(shop);
				datasource.switchDataSource(shop);
				dao.test();
				log.info("验证门店数据库通过："+shop.getShopid());
				shop.setLastActiveDate(Convert.getNowString());
				shop.setLastMsg("验证门店数据库通过");
				shop.setActive(true);
			} catch (Exception e) {
				log.error(e,e);
				log.info("验证门店数据库失败："+shop.getShopid()+"原因："+e.getMessage());
				shop.setLastActiveDate(Convert.getNowString());
				shop.setLastMsg("验证门店数据库失败，原因："+e.getMessage());
				shop.setActive(false);
			}
			
			shopConnectMap.put(shop.getShopid(), shop);
		}
	}

	public JSONArray getShopConnectList() {
		// 向服务器获取链接信息
		String url = service + "/rest/client/getShopConnectList";
		Map<String, String> headMap = new HashMap<String, String>();
		String time = System.currentTimeMillis()+"";
		String localPassword = SHA1.sha1(entid+clientid+time);
		headMap.put("time", time);
		headMap.put("entid", entid);
		headMap.put("clientid", clientid);
		headMap.put("password", localPassword);

		String res = HttpClientCommon.doPostStream("abcd1234", headMap, url, 20000, 20000, "utf-8");
		if (StringUtils.isEmpty(res)) {
			log.error("服务器返回空" + res);
			return null;
		}
		log.info("加密数据："+res);
		try {
			res = DESedeAPIUtil.decodeCBC(clientid, res);
		} catch (Exception e) {
			log.error("解密失败："+res);
			return null;
		}
		log.info("解密数据："+res);
		JSONObject jo = JSONObject.parseObject(res);
		int code = jo.getIntValue("code");
		if (code != 0) {
			log.error("服务器返回错误：" + jo.getString("message"));
			return null;
		}
		return jo.getJSONArray("data");
	}

	public static String getProperties(String configFile, String key, String defaultValue) {
		String temp = PropertiesUtils.getProperty(configFile, key);
		if (StringUtils.isEmpty(temp)) {
			return defaultValue;
		} else {
			return temp.trim();
		}
	}
}
