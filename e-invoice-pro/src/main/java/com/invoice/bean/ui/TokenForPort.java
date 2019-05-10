package com.invoice.bean.ui;

import java.io.Serializable;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aisino.CaConstant;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.SHA1;
import com.invoice.util.SignUtil;

public class TokenForPort implements Serializable {

 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6722933854003855841L;
	
	private String appkey;
	private String entid;
	private String sessionid;
	private String method;
	private String v;
	private String format;
	private String locale;
	private String timestamp;
	private String client;
	private String sign;
	
	
	
	public String getEntid() {
		return entid;
	}
	public void setEntid(String entid) {
		this.entid = entid;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String checkToken(RtnData rtn) throws Exception {
		
		SortedMap<String, String> sortMap  = new TreeMap<String,String>();
		
		if(entid!=null&&!entid.equals("")){
			sortMap.put("entid", entid);
		}else{
			rtn.setCode(-1);
			rtn.setMessage("entid 值不能为空");
			return null;
		}  
		Properties ca =  CaConstant.get(entid);
		
		if(appkey!=null&&!appkey.equals("")){
			sortMap.put("appkey", appkey);
		}else{
			rtn.setCode(-1);
			rtn.setMessage("appkey 值不能为空");
			return null;
		}  
		
		if(client!=null&&!client.equals("")){
			sortMap.put("client", client);
		}
		if(format!=null&&!format.equals("")){
			sortMap.put("format", format);
		}
		if(locale!=null&&!locale.equals("")){
			sortMap.put("locale", locale);
		}
		if(method!=null&&!method.equals("")){
			sortMap.put("method", method);
		}
		if(sessionid!=null&&!sessionid.equals("")){
			sortMap.put("sessionid", sessionid);
		}else{
			rtn.setCode(-1);
			rtn.setMessage("sessionid 值不能为空");
			return null;
		}  
		
		if(timestamp!=null&&!timestamp.equals("")){
			sortMap.put("timestamp", timestamp);
		}else{
			rtn.setCode(-1);
			rtn.setMessage("timestamp 值不能为空");
			return null;
		}  
		
		if(v!=null&&!v.equals("")){
			sortMap.put("v", v);
		}else{
			rtn.setCode(-1);
			rtn.setMessage("v 值不能为空");
			return null;
		}  
		
		if(sign==null||"".equals(sign)){
			rtn.setCode(-1);
			rtn.setMessage("sign 值不能为空");
			return null;
		}  
		//System.out.println(ca.getProperty("userid")+"   "+ca.getProperty("password"));
		if(!SignUtil.MD5(ca.getProperty("userid")+ca.getProperty("password"), "utf-8").equals(sessionid)){
			rtn.setCode(-1);
			rtn.setMessage("用户名密码不正确!");
			return null;
		}

		return SHA1.createSHA1Sign(sortMap,ca.getProperty("password"));
	}
	@Override
	public String toString() {
		return "Token [appkey=" + appkey + ", sessionid=" + sessionid
				+ ", method=" + method + ", v=" + v + ", format=" + format
				+ ", locale=" + locale + ", timestamp=" + timestamp
				+ ", client=" + client + ", sign=" + sign + "]";
	}
	
	
}
