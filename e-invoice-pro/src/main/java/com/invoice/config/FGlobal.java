package com.invoice.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.invoice.job.impl.JobHeart;
import com.invoice.util.PropertiesUtils;
import com.invoice.util.SpringContextUtil;

@Component
public class FGlobal implements InitializingBean {
	@Autowired
	SpringContextUtil springContextUtil;
	private static final String CONFIG_FILE = "config.properties";
	
	private static boolean jobstart = false;

	public static final String invoiceSystemSqlSessionFactory = "invoiceSystemSqlSessionFactory";

	public static String portalurl;
	
	public static String openinvoiceurl;
	
	public static String searchblankInvoice;
	
	public static String sid;
	
	public static String jobGroup;
	
	public static String sellurl;
	
	/**
	 * 金额校验误差
	 */
	final public static double amtRange= 0.1;

	@Override
	public void afterPropertiesSet() throws Exception {
		sid = getProperties(CONFIG_FILE, "sid", "0");
		portalurl = getProperties(CONFIG_FILE, "portalurl", portalurl);
		openinvoiceurl = getProperties(CONFIG_FILE, "openinvoiceurl", openinvoiceurl);
		String tmp = getProperties(CONFIG_FILE, "jobstart", "false");
		searchblankInvoice = getProperties(CONFIG_FILE, "searchblankInvoice", searchblankInvoice);
		sellurl = getProperties(CONFIG_FILE, "sellurl", sellurl);
		jobGroup = getProperties(CONFIG_FILE, "jobGroup", null);
		
		jobstart = Boolean.parseBoolean(tmp);
		if(jobstart){
			springContextUtil.getBean("jobHeart",JobHeart.class).init();
		}
	}

	public static String getProperties(String configFile, String key, String defaultValue) {
		String temp = PropertiesUtils.getProperty(configFile, key);
		if (StringUtils.isEmpty(temp)) {
			return defaultValue;
		} else {
			return temp.trim();
		}
	}
	
	//修正多企业时相互覆盖问题 by ZHAO on 2019.05.15
	final public static Map<String, HashMap<String,String>> WeixTokenMap = new HashMap<String, HashMap<String,String>>();

	final public static String WeixinAppID = "weixinAppid";
	final public static String WeixinSecret = "weixinSecret";
	final public static String WeixinNote = "weixinNote";
	final public static String weixinIndex = "weixinIndex";
	
	final public static String WeixinKPR = "weixinKPR";
	final public static String WeixinFHR = "WeixinFHR";
	final public static String weixinSKR = "weixinSKR";
	
	final public static String WeixinCard = "weixinCard";
	final public static String WeixinHelp = "weixinHelp";
	final public static String WeixinRedirect = "weixinRedirect";
	
	public static final String AmtType = "amtType";
	public static final String usePhone = "usePhone";
	public static final String useEmail = "useEmail";
	public static final String useQR = "useQR";
	
	//文件下载前缀
	public static final String pdfPre = "pdfPre";
	
	//电子票是否立即锁定
	public static final String LockSheet = "LockSheet";
}
