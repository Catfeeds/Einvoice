package com.einvoice.sell.dao;

import java.util.List;
import java.util.Map;



/**
 * @author Baij
 * 收银小票
 */
public interface BillDao extends BaseDao{

	List<Map<String, Object>> getHeadRet(Map<String, String> p);

	List<Map<String, Object>> getStat101(Map<String, String> p);
	List<Map<String, Object>> getStat102(Map<String, String> p);
	List<Map<String, Object>> getStat103(Map<String, String> p);

	List<Map<String, Object>> getStatPay(Map<String, String> p);
}
