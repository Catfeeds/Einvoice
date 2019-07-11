package com.invoice.uiservice.service;

import java.util.List;
import java.util.Map;

public interface ZBfplogService {
    int getAllZBfplogCount(Map<String, Object> p);
    List<Map<String,Object>> getAllZBfplog(Map<String,Object> maps);
}
