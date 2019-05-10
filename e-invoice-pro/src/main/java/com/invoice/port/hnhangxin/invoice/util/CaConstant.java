package com.invoice.port.hnhangxin.invoice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by TT on 2018-03-16.
 */
public class CaConstant {
    private final static Logger LOGGER = LoggerFactory.getLogger(CaConstant.class);

    public final static String DEFAULT_CHARSET = "UTF-8";
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(CaConstant.class.getClassLoader().getResourceAsStream("ca/fapiao/platform/pkcs7.properties"));
        } catch (Exception e) {
            LOGGER.error("pkcs7接口初始化系统参数失败!", e.fillInStackTrace());
        }
    }

    /**
     * 读取配置文件里key的值
     *
     * @param key
     *            配置文件里的key
     * @return
     * @throws java.io.IOException
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
