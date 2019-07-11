package com.invoice.uiservice.dao;

import com.invoice.bean.db.CClient;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.Shop;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("DBoptionDao")
public interface DBoptionDao {
    List<Map<String,Object>> getAllByshopid(Shop shop);
    List<CClient> getClientinfoByshopid(CClient client);
    List<CClient> getClientAllinfoByshopid(CClient client);
    List<CConnect> getCconectinfoByshopid(CConnect cConnect);
    List<CConnect> getCconectAllinfoByshopid(CConnect cConnect);
}
