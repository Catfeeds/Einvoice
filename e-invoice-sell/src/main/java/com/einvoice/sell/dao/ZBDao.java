package com.einvoice.sell.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;



/**
 * @author 
 * 天一广场回传发票
 */
public interface ZBDao extends BaseDao{

	public List<Map<String, Object>> getgoodsinfo(@Param("startrow") int startrow ,@Param("endrow") int endrow);
}
