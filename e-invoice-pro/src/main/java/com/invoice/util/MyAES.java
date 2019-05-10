package com.invoice.util;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MyAES
{
  public static byte[] encrypt(byte[] content, String password)
  {
    try
    {
      byte[] enCodeFormat = password.getBytes();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(1, key);
      byte[] result = cipher.doFinal(content);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] decrypt(byte[] content, String password)
  {
    try
    {
      byte[] enCodeFormat = password.getBytes();
      SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(2, key);
      byte[] result = cipher.doFinal(content);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String MD5(byte[] data) throws Exception {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(data);
    byte[] b = md5.digest();

    StringBuffer buf = new StringBuffer("");
    for (int offset = 0; offset < b.length; ++offset) {
      int i = b[offset];
      if (i < 0) i += 256;
      if (i < 16)
        buf.append("0");
      buf.append(Integer.toHexString(i));
    }
    return buf.toString();
  }

  public static byte[] decryptBASE64(String key) throws Exception {
    return new BASE64Decoder().decodeBuffer(key);
  }

  public static String encryptBASE64(byte[] key) throws Exception {
    return new BASE64Encoder().encodeBuffer(key);
  }
}