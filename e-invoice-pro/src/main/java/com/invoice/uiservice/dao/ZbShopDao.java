package com.invoice.uiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.invoice.bean.db.CClient;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.CShopconnect;
import com.invoice.bean.db.Shop;

@Component("ZbShopDao")
public interface ZbShopDao {
    public void insertShop(Shop shop) throws Exception;
    
    public void updateShop(Shop shop) throws Exception;
  
    public void deleteShop(Shop shop) throws Exception;
    
    List<HashMap<String,String>> queryShop(Map<String, Object> p) throws Exception;
    
    public Shop getShopById(Shop shop) throws Exception;
    public CClient getClientById(CClient client) throws Exception;
    public CConnect getConnentById(CConnect connect) throws Exception;
    public CShopconnect getCShopById(CShopconnect cshop) throws Exception;
    
    public void insertClient(CClient client) throws Exception;
    public void insertConnent(CConnect connect) throws Exception;
    public void insertCshop(CShopconnect cshop) throws Exception;
   
    public void updateClient(CClient client) throws Exception;
    public void updateConnent(CConnect connect) throws Exception;
    public void updateCshop(CShopconnect cshop) throws Exception;
    public List<Shop> getShops(Shop shop);
 
    int getShopCount(Map<String, Object> p);
    
    
}
