package com.invoice.port.hnhangxin.invoice.util;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * Created by TT on 2018-03-16.
 */
public class Base64Helper {
    private static final Logger LOGGER = Logger.getLogger(Base64Helper.class);
    private static final String CHARSET = "UTF-8";

    public static String encode(String res) {
        try {
            Base64 base = new Base64();
            if ((res != null) && (!"".equals(res))) {
                return new String(base.encode(res.getBytes("UTF-8")));
            }
            LOGGER.warn("即将解密串为null或者为“” ");
            return "";
        } catch (Exception e) {
            LOGGER.error("未知：" + e);
        }
        return "";
    }

    public static String encode(String res, String charset) {
        try {
            Base64 base = new Base64();
            if ((res != null) && (!"".equals(res))) {
                return new String(base.encode(res.getBytes(charset)));
            }
            LOGGER.warn("即将解密串为null或者为“” ");
            return "";
        } catch (Exception e) {
            LOGGER.error("未知：" + e);
        }
        return "";
    }

    public static byte[] encode(byte[] res) {
        try {
            Base64 base = new Base64();
            return base.encode(res);
        } catch (Exception e) {
            LOGGER.error("未知：" + e);
        }
        return null;
    }

    public static String decode(String str) {
        try {
            return new String(new Base64().decode(str.getBytes()), "UTF-8");
        } catch (Exception e) {
            LOGGER.error("未知：" + e);
        }
        return null;
    }

    public static String decode(String str, String encode) {
        try {
            return new String(new Base64().decode(str.getBytes()), encode);
        } catch (Exception e) {
            LOGGER.error("未知：" + e);
        }
        return null;
    }

    public static byte[] decodeByte(byte[] str) {
        return new Base64().decode(str);
    }

    public static byte[] decode(byte[] str)
            throws UnsupportedEncodingException {
        return new Base64().decode(str);
    }
}
