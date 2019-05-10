package com.invoice.uiservice.service;

import java.util.List;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.Privatepara;
import com.invoice.bean.db.PurchaserInfo;

public interface CompanyNameService {
    
	//获取企业名称
	Enterprise getEnterpriseByIdTitle(String data);
	
	//获取开票人列表
    public List<PurchaserInfo> getPurchaserListByName(String name);
    
    //保存开票人信息
    public int insertPurchaser(PurchaserInfo p);
    
    //获取开票人业备入口
    public List<PurchaserInfo> getPurchaserListByClass(PurchaserInfo c,List<Privatepara> p);
}
