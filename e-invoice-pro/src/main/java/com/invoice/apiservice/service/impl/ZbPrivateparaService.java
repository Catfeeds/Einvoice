package com.invoice.apiservice.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.apiservice.dao.ZbPrivateparaDao;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.Privatepara;
import com.invoice.bean.db.ZbCClient;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.NewHashMap;
import com.invoice.util.SHA1;

@Service
public class ZbPrivateparaService {
	@Autowired
	ZbPrivateparaDao dao;

	/**
	 * 获取企业私有参数
	 * 
	 * @param p
	 * @return
	 */
	public List<Privatepara> getPrivatepara(Privatepara p) {
		return dao.getPrivatepara(p);
	}
	
	public void updatePrivatepara(Privatepara p) {
		dao.updatePrivatepara(p);
	}

	public void savePrivatepara(Privatepara p) {
		dao.savePrivatepara(p);
	}
	/**
	 * 返回链接信息。附带数据验证码，已企业号作为key。
	 * @param entid
	 * @param clientid
	 * @param password
	 * @param time
	 * @return
	*/
	public String getConnectList(String entid, String clientid, String password) {
		// TODO 验证客户端和密码
		List<Map<String, String>> lits = dao.getConnectList(entid);
		return new RtnData(lits).toString();
	}

	public String getShopConnectList(String entid, String clientid, String password, String time) {
		//校验
		String localPassword = SHA1.sha1(entid + clientid + time);
		if (!localPassword.equals(password)) {
			return new RtnData(-99, "鉴权不通过").toString();
		}

		Map<String, String> p = new NewHashMap<String, String>();
		p.put("entid", entid);
		p.put("clientid", clientid);
		List<Map<String, String>> lits = dao.getShopConnectList(p);
		try {
			return new RtnData(lits).toStringDES(clientid);
		} catch (Exception e) {
			return new RtnData(-101, "消息加密异常").toString();
		}
	}

	public ZbCClient getClientUrlByShopid(String entid, String shopid) {
		Map<String, String> p = new NewHashMap<String, String>();
		p.put("entid", entid);
		p.put("shopid", shopid);
		return dao.getClientUrlByShopid(p);
	}
	
	public List<Map<String, String>> getCConnectList(Map<String, Object> p) {
		return dao.getCConnectList(p);
	}
	
	public void updateCConnect(CConnect connect) throws Exception{
		dao.updateCConnect(connect);
	}
	
	public int getCConnectCount(Map<String, Object> p) {
		return dao.getCConnectCount(p);
	}
	
}
