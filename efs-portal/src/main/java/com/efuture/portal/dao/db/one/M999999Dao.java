/*
 * Copyright (C), 1996-2014
 * FileName: SampleDao.java
 * Author:   王华君
 * Date:     Dec 18, 2014 11:15:28 AM
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.efuture.portal.dao.db.one;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * sample dao 操作类<br>
 * 下面都是一些通用的操作数据库例子,方便不懂的人快速入门<br>
 * @author 王华君
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component("M999999Dao")
public interface M999999Dao {
	
	public List<Map<String, Object>> getTableData(Map<String, Object> paramsmap);	
		
	public List<Map<String, Object>> getTableConfig(Map<String, Object> paramsmap);
	
	public List<Map<String, Object>> getTableColumnConfig(Map<String, Object> paramsmap);
	
	public void deleteTableConfig(Map<String, Object> paramsmap);
	
	public void insertTableConfig(Map<String, Object> paramsmap);
	
	public void deleteTableColumnConfig(Map<String, Object> paramsmap);
	
	public void insertTableColumnConfig(Map<String, Object> paramsmap);
	
	public void insertStandardTable(Map<String, Object> paramsmap);
	
	public void deleteStandardTable(Map<String, Object> paramsmap);
	
	public void updateStandardTable(Map<String, Object> paramsmap);
	
	
}
