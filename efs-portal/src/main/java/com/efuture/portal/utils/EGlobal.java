package com.efuture.portal.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@SuppressWarnings({ "all" })
@Component
public class EGlobal implements InitializingBean {

	private static final String CONFIG_FILE = "config.properties";
	public static String efs_srm_url = "http://localhost:8080/efs-srm/ui/srm/srmmain.html";
	public static String efs_store_url = "http://localhost:8080/efs-ui/ui/storemain.html";
	public static String efs_myshop_url = "http://localhost:8080/myshop/ui/menu/main.html";
	public static String portalurl = "http://127.0.0.1:8081/efs-portal/";
	

	@Override
	public void afterPropertiesSet() throws Exception {
		efs_srm_url = getProperties(CONFIG_FILE, "efs_srm_url", efs_srm_url);
		efs_store_url = getProperties(CONFIG_FILE, "efs_store_url", efs_store_url);
		efs_myshop_url = getProperties(CONFIG_FILE, "efs_myshop_url", efs_myshop_url);
		portalurl = getProperties(CONFIG_FILE, "portalurl", portalurl);
	}

	public static String getProperties(String configFile, String key,
			Object defaultValue) {
		String temp = PropertiesUtils.getProperty(configFile, key);
		if (StringUtils.isEmpty(temp)) {
			return defaultValue + "";
		} else {
			return temp.trim();
		}
	}

}
