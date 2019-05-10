package com.invoice.port.bwgf.invoice.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5AndSHA1Factory {
	/**
     * md5+sha-1
     * @param inputText 
     */
    public static String md5AndSha(String inputText) 
    {
        return sha(md5(inputText));
    }

    /**
     * md5
     */
    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }


    
    /**
     * sha
     */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }


    /**
     * md5
     * @param inputText  
     * @param algorithmName 
     */
    private static String encrypt(String inputText, String algorithmName) {

        if (inputText == null || "".equals(inputText.trim())) {

            throw new IllegalArgumentException("要加密的字符串不能为空 ");
        
        }

        if (algorithmName == null || "".equals(algorithmName.trim())) {

            algorithmName = "md5";
        
        }

        String encryptText = null;

        try {
            
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			            
			m.update(inputText.getBytes("UTF8"));

            byte[] s = m.digest();

            return hex(s);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {
            
        	e.printStackTrace();
            
        }
        return encryptText;
        
    }
    /**
     *
     */
    private static String hex(byte[] arr) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; ++i) {

            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
            
        	return sb.toString();
    }
}
