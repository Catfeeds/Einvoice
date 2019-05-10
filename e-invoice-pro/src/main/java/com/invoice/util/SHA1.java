package com.invoice.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class SHA1 {

	public static String sha1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

		//微信公众号开发的获取时间戳
		public static String getTimeStamp() {
			return String.valueOf(System.currentTimeMillis() / 1000);
		}
			
	   //创建签名SHA1
		public static String createSHA1Sign(SortedMap<String, String> signParams,String password) throws Exception {
			StringBuffer sb = new StringBuffer();
			Set es = signParams.entrySet();
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				String v = (String) entry.getValue();
				sb.append(k + v);
				//System.out.println(k+"  "+ v);
				//要采用URLENCODER的原始值！
			}
			String params = password+sb.toString()+password;
			//System.out.println("params "+ params);
			return getSha1(params);
		}
		//Sha1签名
		public static String getSha1(String str) {
			if (str == null || str.length() == 0) {
				return null;
			}
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };

			try {
				MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
				mdTemp.update(str.getBytes("UTF-8"));

				byte[] md = mdTemp.digest();
				int j = md.length;
				char buf[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
					buf[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(buf);
			} catch (Exception e) {
				return null;
			}
		}

	
	public static void main(String[] args) {
		//System.out.println(SHA1.sha1("abcd1234"));
		try{
		System.out.println(SignUtil.MD5("wang1234wang123456", "utf-8"));
		System.out.println(String.valueOf(System.currentTimeMillis() / 1000));
		}catch(Exception e){
			
		}
	}
}
