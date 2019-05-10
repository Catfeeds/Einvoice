package com.einvoice.sell.dao;

import java.util.Map;



/**
 * @author 
 * 天一广场回传发票
 */
public interface NBBillDao extends BaseDao{

	int getInvoiceCount(Map<String, Object> p);
	int NBIcallbackSheet(Map<String, Object> p);
	int NBUcallbackSheet(Map<String, Object> p);
}
