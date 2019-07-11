package com.invoice.uiservice.service.impl;

import com.invoice.bean.db.CClient;
import com.invoice.bean.db.CConnect;
import com.invoice.bean.db.Shop;
import com.invoice.uiservice.dao.DBoptionDao;
import com.invoice.uiservice.service.DBoptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("DBoptionService")
public class DBoptionServiceImpl implements DBoptionService{

    @Resource(name="DBoptionDao")
    private DBoptionDao dBoptionDao;


    @Override
    public List<Map<String, Object>> getAllByshopid(Shop shop) {
        return dBoptionDao.getAllByshopid(shop);
    }

    @Override
    public List<CClient> getClientinfoByshopid(CClient cClient) {
        return dBoptionDao.getClientinfoByshopid(cClient);
    }

    @Override
    public List<CClient> getClientAllinfoByshopid(CClient client) {
        return dBoptionDao.getClientAllinfoByshopid(client);
    }

    @Override
    public List<CConnect> getCconectinfoByshopid(CConnect cConnect) {
        return dBoptionDao.getCconectinfoByshopid(cConnect);
    }

    @Override
    public List<CConnect> getCconectAllinfoByshopid(CConnect cConnect) {
        return dBoptionDao.getCconectAllinfoByshopid(cConnect);
    }
}
