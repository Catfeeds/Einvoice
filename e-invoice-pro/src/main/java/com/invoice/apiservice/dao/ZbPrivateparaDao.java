package com.invoice.apiservice.dao;

import java.util.List;
import java.util.Map;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.Privatepara;
import com.invoice.bean.db.ZbCClient;

public interface ZbPrivateparaDao {
	public List<Privatepara> getPrivatepara(Privatepara p);

	public List<Map<String, String>> getConnectList(String entid);

	public List<Map<String, String>> getShopConnectList(Map<String,String> p);

	public ZbCClient getClientUrlByShopid(Map<String, String> p);
	
	public List<Map<String, String>> getCConnectList(Map<String, Object> p);
	
	public void updateCConnect(CConnect connect) throws Exception;
	
	public int getCConnectCount(Map<String, Object> p);

	public void updatePrivatepara(Privatepara p);
	
	public void savePrivatepara(Privatepara p);
}