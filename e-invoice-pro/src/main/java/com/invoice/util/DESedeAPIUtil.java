package com.invoice.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.digest.DigestUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class DESedeAPIUtil {
	 /**
     * 固定密钥 DESEDEAPIUTIL
     */
    public static final String DESEDEAPI_INITKEY = "www.e-future.com.cn";
    /**
     * 加密方式 3DES
     */
    public static final String DESEDE = "desede";
    /**
     * 加密方式/工作方式/填充方式 desede/CBC/PKCS5Padding
     */
    public static final String DESEDE_PADDING = "desede/CBC/PKCS5Padding";

    /**
     * 3des CBC PKCS5Padding 加密
     * @param key 密钥
     * @param data 明文
     * @return
     * @throws Exception
     */
    public static String encodeCBC(String key,String data) throws Exception{
        Key deskey = null;
        byte[] newkey = getMD5Key(key);
        byte[] key24 = getKey24(newkey);
        byte[] key8 = getIV8(newkey);
        DESedeKeySpec spec = new DESedeKeySpec(key24);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(DESEDE);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(DESEDE_PADDING);
        IvParameterSpec ips = new IvParameterSpec(key8);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data.getBytes("utf8"));
        return new BASE64Encoder().encode(bOut);
    }

    /**
     * 3des CBC PKCS5Padding 解密
     * @param key 密钥
     * @param data 密文
     * @return
     * @throws Exception
     */
    public static String decodeCBC(String key,String data)throws Exception{
        Key deskey = null;
        byte[] newkey = getMD5Key(key);
        byte[] key24 = getKey24(newkey);
        byte[] key8 = getIV8(newkey);
        DESedeKeySpec spec = new DESedeKeySpec(key24);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(DESEDE);
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(DESEDE_PADDING);
        IvParameterSpec ips = new IvParameterSpec(key8);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] datas = new BASE64Decoder().decodeBuffer(data);
        byte[] bOut = cipher.doFinal(datas);
        return new String(bOut,"utf8");
    }

    private static byte[] getMD5Key(String key){
        String newKey = DESEDEAPI_INITKEY+"#"+key;
        byte[] newKeys = DigestUtils.md5(newKey);
        return newKeys;
    }

    /**
     * 获取24位密钥
     * @param key
     * @return
     */
    private static byte[] getKey24(byte[] key){
        byte[] key24 = new byte[24];
        if(key.length>=24){
            System.arraycopy(key,0,key24,0,24);
        }else if(key.length<8){
            System.arraycopy(key,0,key24,0,key.length);
        }else {
            System.arraycopy(key,0,key24,0,key.length);
            System.arraycopy(key,0,key24,key.length,8);
        }
        return key24;
    }

    /**
     * 获取8位向量
     * @param key
     * @return
     */
    private static byte[] getIV8(byte[] key){
        byte[] iv8 = new byte[8];
        if(key.length>=8){
            System.arraycopy(key,0,iv8,0,8);
        }else {
            System.arraycopy(key,0,iv8,0,key.length);
        }
        return iv8;
    }

    public static void main(String[] args) {
        String key = "25000001";
        String data = "31011019811118519X";
        try {
            System.out.println("约定密钥："+key);
            System.out.println("测试明文："+data);
            String encode = encodeCBC(key,data);
            System.out.println("加密密文："+encode);
            String decode = decodeCBC(key,encode);
            System.out.println("解密明文："+decode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
