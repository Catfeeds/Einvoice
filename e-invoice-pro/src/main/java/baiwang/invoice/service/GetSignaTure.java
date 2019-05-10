package baiwang.invoice.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import baiwang.invoice.bean.FpttBean;

public class GetSignaTure {
	//获取签名参数
	private static String apiName = "baiwang.bizinfo.companySearch";                      //API 
	private static String appKey;                        					 //appKey
	private static String appSecret;         //appSecret
	private static String token;             //token
	private static String timestamp;
	
	public GetSignaTure(FpttBean fptt){
		
		appKey = fptt.getClient_id();
		appSecret = fptt.getClient_secret();
		token = fptt.getToken();
		timestamp = fptt.getTimestamp();
	}
	
	public String getST() throws Exception
	{
	    Map<String, String> textParams = new HashMap<String, String>();

	    textParams.put("method", apiName);
	    textParams.put("version", "2.0");
	    textParams.put("appKey", appKey);
	    textParams.put("format", "json");
	    textParams.put("timestamp",timestamp);
	    textParams.put("token", token);
	    textParams.put("type", "sync");

	    String signString = signTopRequest(textParams, appSecret);
	    System.out.println(signString);
	    return signString;
	}


	public static String signTopRequest(Map<String, String> params, String secret) throws Exception 
	{
	    ArrayList<String> keys = new ArrayList<String>(params.keySet());
	    Collections.sort(keys);
	    StringBuilder query = new StringBuilder();
	    query.append(secret);
	    for (String key : keys) 
	    {
	        String value = params.get(key);
	        if (!isNull(key) && !isNull(value)) 
	        {
	            query.append(key).append(value);
	        }
	    }
	    query.append(secret);
	    byte[] bytes;
	    MessageDigest md5 = null;
	    try 
	    {
	        md5 = MessageDigest.getInstance("MD5");
	    }
	    catch (NoSuchAlgorithmException ignored) 
	    {
	        throw new Exception(ignored);
	    }

	    bytes = md5.digest(query.toString().getBytes("UTF-8"));
	    StringBuilder sign = new StringBuilder();
	    for (byte b : bytes) {
	        String hex = Integer.toHexString(b & 0xFF);
	        if (hex.length() == 1) {
	            sign.append("0");
	        }
	        sign.append(hex.toUpperCase());
	    }
	    return sign.toString();
	}

	public static boolean isNull(String str)
	{
	    return (str==null || "".equals(str)?true:false);
	}
}
