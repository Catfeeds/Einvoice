package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.CClient;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.CShopconnect;
import com.invoice.bean.db.Shop;
import com.invoice.uiservice.dao.ZbShopDao;
import com.invoice.uiservice.service.ZbShopService;

@Service("ZbShopService")
public class ZbShopServiceImpl implements ZbShopService{

	@Resource(name = "ZbShopDao")
	ZbShopDao zbshopDao;
	
	@Override
	public void insertShop(Shop shop) throws Exception {
		zbshopDao.insertShop(shop);
	}

	@Override
	public void updateShop(Shop shop) throws Exception {
		zbshopDao.updateShop(shop);
	}

	@Override
	public void deleteShop(Shop shop) throws Exception {
		zbshopDao.deleteShop(shop);
	}

	@Override
	public List<HashMap<String,String>> queryShop(Map<String, Object> p) throws Exception {
		return zbshopDao.queryShop(p);
	}

	@Override
	public Shop getShopById(Shop shop) throws Exception {
		return zbshopDao.getShopById(shop);
	}


	@Override
	public int getShopCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return zbshopDao.getShopCount(p);
	}

	@Override
	public CClient getClientById(CClient client) throws Exception {
		// TODO Auto-generated method stub
		return zbshopDao.getClientById(client);
	}

	@Override
	public CConnect getConnentById(CConnect connect) throws Exception {
		// TODO Auto-generated method stub
		return zbshopDao.getConnentById(connect);
	}

	@Override
	public CShopconnect getCShopById(CShopconnect cshop) throws Exception {
		// TODO Auto-generated method stub
		return zbshopDao.getCShopById(cshop);
	}

	@Override
	public void insertClient(CClient client) throws Exception {
		// TODO Auto-generated method stub
		zbshopDao.insertClient(client);
	}

	@Override
	public void insertConnent(CConnect connect) throws Exception {
		// TODO Auto-generated method stub
		zbshopDao.insertConnent(connect);
	}

	@Override
	public void insertCshop(CShopconnect cshop) throws Exception {
		// TODO Auto-generated method stub
		zbshopDao.insertCshop(cshop);
	}

	@Override
	public void updateClient(CClient client) throws Exception {
		// TODO Auto-generated method stub
		zbshopDao.updateClient(client);
	}

	@Override
	public void updateConnent(CConnect connect) throws Exception {
		// TODO Auto-generated method stub
		zbshopDao.updateConnent(connect);
	}

	@Override
	public void updateCshop(CShopconnect cshop) throws Exception {
		// TODO Auto-generated method stub
		zbshopDao.updateCshop(cshop);
	}

	@Override
	public List<Shop> getShops(String entid) {
		Shop shop = new Shop();
		shop.setEntid(entid);
		return zbshopDao.getShops(shop);
	}
}
