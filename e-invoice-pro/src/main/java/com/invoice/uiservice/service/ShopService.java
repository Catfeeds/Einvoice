package com.invoice.uiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Shop;

public interface ShopService {
    public void insertShop(Shop shop) throws Exception;
    
    public void updateShop(Shop shop) throws Exception;
    
    public void deleteShop(Shop shop) throws Exception;
    
    List<HashMap<String,String>> queryShop(Map<String, Object> p) throws Exception;
    
    public Shop getShopById(Shop shop) throws Exception;
    
    public Shop getShopByShopId(Shop shop) throws Exception;
    
    int getShopCount(Map<String, Object> p);
    
    public List<Shop> getShopsByEntId(String entid);
    public List<Shop> getShops(String entid);
}
