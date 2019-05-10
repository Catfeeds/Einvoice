package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.Shop;

@Component("ShopDao")
public interface ShopDao {
    public void insertShop(Shop shop) throws Exception;
    
    public void updateShop(Shop shop) throws Exception;
    
    public void deleteShop(Shop shop) throws Exception;
    
    List<HashMap<String,String>> queryShop(Map<String, Object> p) throws Exception;
    
    public Shop getShopById(Shop shop) throws Exception;
    
    public Shop getShopByShopId(Shop shop) throws Exception;
    
    int getShopCount(Map<String, Object> p);
    
    public List<Shop> getShopsByEntId(Shop shop);
    public List<Shop> getShops(Shop shop);
}
