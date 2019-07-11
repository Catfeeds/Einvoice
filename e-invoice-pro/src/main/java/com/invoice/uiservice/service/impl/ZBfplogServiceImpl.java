package com.invoice.uiservice.service.impl;

import com.invoice.uiservice.dao.ZBfplogDao;
import com.invoice.uiservice.service.ZBfplogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("ZBfplogService")
public class ZBfplogServiceImpl implements ZBfplogService {

    @Resource(name = "ZBfplogDao")
    private ZBfplogDao zBfplogDao;

    @Override
    public int getAllZBfplogCount(Map<String, Object> p) {
        return zBfplogDao.getAllZBfplogCount(p);
    }

    @Override
    public List<Map<String, Object>> getAllZBfplog(Map<String, Object> maps) {
        return zBfplogDao.getAllZBfplog(maps);
    }
}
