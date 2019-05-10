package com.invoice.uiservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invoice.bean.db.Shop;
import com.invoice.uiservice.dao.ShopDao;
import com.invoice.uiservice.service.ShopService;

@Service("ShopService")
public class ShopServiceImpl implements ShopService{

	@Resource(name = "ShopDao")
	ShopDao shopDao;
	
	@Override
	public void insertShop(Shop shop) throws Exception {
		shopDao.insertShop(shop);
	}

	@Override
	public void updateShop(Shop shop) throws Exception {
		shopDao.updateShop(shop);
	}

	@Override
	public void deleteShop(Shop shop) throws Exception {
		shopDao.deleteShop(shop);
	}

	@Override
	public List<HashMap<String,String>> queryShop(Map<String, Object> p) throws Exception {
		return shopDao.queryShop(p);
	}

	@Override
	public Shop getShopById(Shop shop) throws Exception {
		return shopDao.getShopById(shop);
	}
	
	@Override
	public Shop getShopByShopId(Shop shop) throws Exception {
		return shopDao.getShopByShopId(shop);
	}

	@Override
	public int getShopCount(Map<String, Object> p) {
		// TODO Auto-generated method stub
		return shopDao.getShopCount(p);
	}

	@Override
	public List<Shop> getShopsByEntId(String entid) {
		Shop shop = new Shop();
		shop.setEntid(entid);
		return shopDao.getShopsByEntId(shop);
	}
	@Override
	public List<Shop> getShops(String entid) {
		Shop shop = new Shop();
		shop.setEntid(entid);
		return shopDao.getShops(shop);
	}

}
