package com.invoice.uiservice.dao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("ZBfplogDao")
public interface ZBfplogDao {
    int getAllZBfplogCount(Map<String, Object> p);
    List<Map<String,Object>> getAllZBfplog(Map<String,Object> maps);
}
