package com.einvoice.sell.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author Baij
 * 批发单
 */
public interface CommonSheetDao extends BaseDao{
	public List<Map<String, Object>> getZH(@Param("page") int page, @Param("size") int size);

	public List<Map<String, Object>> getHT(@Param("page") int page,@Param("gysid") String gysid,@Param("size") int size);
}
