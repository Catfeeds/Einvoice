package com.invoice.apiservice.dao;

import java.util.List;
import com.invoice.bean.db.ZBGoodsinfo;

/**
 * @author Baij
 */
public interface ZBGoodInfoDao {

	
	public void insertGoodsInfo(List<ZBGoodsinfo> detail);
	
}

