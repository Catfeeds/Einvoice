package com.aisino;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * 西部CA配置文件路径和参数
 */
public final class CaConstant {

	private final static Logger LOGGER = LoggerFactory.getLogger(CaConstant.class);

	public final static String DEFAULT_CHARSET = "UTF-8";
	
	private static Map<String,Properties> caMap = new HashMap<String,Properties>();
	
	synchronized public static Properties get(String pathurl){
		Properties p = caMap.get(pathurl);
		if(p==null){
			String fileName = "/CA/"+pathurl+"/pkcs7.properties";
			p=new Properties();
			try {
				p.load(CaConstant.class.getResourceAsStream(fileName));
			} catch (IOException e) {
				LOGGER.error("pkcs7接口初始化系统参数失败! - "+pathurl, e.fillInStackTrace());
			}
		}
		return p;
	}
}
