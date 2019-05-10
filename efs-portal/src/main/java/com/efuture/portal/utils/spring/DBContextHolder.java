/*
 * Copyright (C), 1996-2014
 * FileName: Sample.java
 * Author:   王华君
 * Date:     Dec 18, 2014 11:13:54 AM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.utils.spring;

/**
 * 
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class DBContextHolder {
	public static final String DATA_SOURCE_ONE = "one"; // 对应dataSource_one数据源key
	public static final String DATA_SOURCE_TWO = "two"; // 对应远程dataSource_two数据源key
	public static final String DATA_SOURCE_THREE = "three"; // 对应远程dataSource_three数据源key

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	/**
	 * 设置数据源
	 * 
	 * @param dbType
	 *            本类中两个静态变量的值
	 */
	public static void setDBType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDBType() {
		return contextHolder.get();
	}

	public static void clearDBType() {
		contextHolder.remove();
	}
}