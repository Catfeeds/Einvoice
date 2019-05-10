package com.invoice.uiservice.dao;

import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.PurchaserInfo;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("CompanyNameDao")
public interface CompanyNameDao {

    //获取企业名称
    public Enterprise getEnterpriseByIdTile(String entid);
    
    //获取开票人列表
    public List<PurchaserInfo> getPurchaserListByName(String name);
    
    //保存开票人信息
    public int insertPurchaser(PurchaserInfo p);
    
    //保存自行录入的开票人信息
    public int insertInvoiceInfo();
}
