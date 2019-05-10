/**
 * 版权所有 2013 efuture Company, Inc. 保留所有权
 */
package com.efuture.portal.utils.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * junit测试获得bean
 *
 * @author wanghuajun
 * @data 2014-01-18
 *
 */
public class SpringBeanFactory {

	public static <T> T getBean(String name, Class<T> requiredType) {
		return ApplicationContextHolder.context.getBean(name, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType) {
		System.out.println("ApplicationContextHolder.context:"+ApplicationContextHolder.context);
		return ApplicationContextHolder.context.getBean(requiredType);
	}

	private static class ApplicationContextHolder {
       
		private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"classpath*:spring/applicationContext.xml"});

	}
}
