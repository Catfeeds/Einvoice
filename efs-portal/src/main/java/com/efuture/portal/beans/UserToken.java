package com.efuture.portal.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.efuture.portal.dao.db.one.MainDao;
import com.efuture.portal.utils.spring.SpringBeanFactoryAware;

public class UserToken {

	private static UserToken instance;

	public ArrayList<User> userlist;

	private static MainDao MainDao;

	private UserToken() {
		userlist = new ArrayList<User>();
	}

	public static synchronized UserToken getInstance() {
		if (instance == null) {
			instance = new UserToken();
			MainDao = SpringBeanFactoryAware.getBean(MainDao.class);
		}

		return instance;
	}

	public User getUserByToken(String token) {
		for (int i = userlist.size() - 1; i >= 0; i--) {
			if (token.equals(userlist.get(i).getToken())) {
				userlist.get(i).setLasttime(new Date());
				return userlist.get(i);
			}
		}

		return null;
	}

	public void setUser(User user) {
		for (int i = userlist.size() - 1; i >= 0; i--) {
			// 企业相同,账号相同
			if (user.getCompanyid().equals(userlist.get(i).getCompanyid())
					&& user.getLoginid().equals(userlist.get(i).getLoginid())
					&& user.getPartnerid().equals(
							userlist.get(i).getPartnerid())) {
				userlist.get(i).setToken(user.getToken());
				userlist.get(i).setShopid(user.getShopid());
				userlist.get(i).setLasttime(new Date());
				return;
			}
		}

		userlist.add(user);

		return;
	}

	public void setUser_new(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyid", user.getCompanyid());
		map.put("loginid", user.getLoginid());
		map.put("partnerid", user.getPartnerid());
		map.put("username", user.getUsername());
		map.put("companyname", user.getCompanyname());
		map.put("token", user.getToken());
		map.put("shopid", user.getShopid());
		map.put("lasttime", new Date());
		map.put("area", user.getArea());
		map.put("entid", user.getEntid());
		map.put("kpd", user.getKpd());
		map.put("jpzz", user.getJpzz());
		map.put("note", "");
		MainDao.replaceUserToken(map);
	}

	public User getUserByToken_new(String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", token);
		MainDao.update_Lasttime_4Usertoken(map);

		List<Map<String, Object>> list = MainDao.queryUserTokenByToken(map);
		if (list.size() == 0) {
			return null;
		}

		if (list.size() != 1) {
			throw new RuntimeException("token is not unique!");
		}
		
		User user = new User();
		user.setCompanyid(list.get(0).get("companyid") + "");
		user.setEntid(list.get(0).get("entid") + "");
		user.setLoginid(list.get(0).get("loginid") + "");
		user.setPartnerid(list.get(0).get("partnerid") + "");
		user.setUsername(list.get(0).get("username") + "");
		user.setCompanyname(list.get(0).get("companyname") + "");
		user.setArea(list.get(0).get("area") + "");
		user.setToken(list.get(0).get("token") + "");
		user.setShopid(list.get(0).get("shopid") + "");
		user.setLasttime((Date) list.get(0).get("lasttime"));
		user.setKpd(list.get(0).get("kpd") + "");
		user.setJpzz(list.get(0).get("jpzz") + "");
		
		Map<String, Object> valueObj = new HashMap<String, Object>();
		valueObj.put("loginid", list.get(0).get("loginid") + "");
		valueObj.put("companyid", list.get(0).get("companyid") + "");
		List<Map<String, Object>> shoplist = MainDao.getLoginShopList(valueObj);
		user.setShoplist(shoplist);
		if(list.get(0).get("shopid") == null && shoplist!=null && !shoplist.isEmpty()){
			user.setShopid(shoplist.get(0).get("shopid") + "");
		}
		return user;
	}
}
