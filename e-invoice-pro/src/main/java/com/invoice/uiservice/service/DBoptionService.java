package com.invoice.uiservice.service;

import com.invoice.bean.db.CClient;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.Shop;

import java.util.List;
import java.util.Map;

public interface DBoptionService {
    List<Map<String,Object>> getAllByshopid(Shop shop);
    List<CClient> getClientinfoByshopid(CClient cClient);
    List<CClient> getClientAllinfoByshopid(CClient client);
    List<CConnect> getCconectinfoByshopid(CConnect cConnect);
    List<CConnect> getCconectAllinfoByshopid(CConnect cConnect);
}
