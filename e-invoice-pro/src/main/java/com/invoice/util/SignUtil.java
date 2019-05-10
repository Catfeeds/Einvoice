package com.invoice.util;

import java.security.MessageDigest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SignUtil {
	/**
	 * 对传入的字符串进行MD5加密
	 * 
	 * @param plainText
	 * @return
	 */
	public static String MD5(String plainText, String charset) throws Exception {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes(charset));
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
	
	public boolean checkSign(JSONObject json,String key) throws Exception{
		String sign = json.getString("sign");
		
		if(sign==null) return false;
		
		json = cookSign(json, key);
		String localSign = json.getString("sign");
		
		return sign.equals(localSign);	
	}
	
	public JSONObject cookSign(JSONObject json,String key) throws Exception{
		json.remove("sign");
		String plainText = json.toJSONString()+key;
		String sign = SignUtil.MD5(plainText, "utf-8");
		json.put("sign", sign);
		return json;
	}
	
	
}
