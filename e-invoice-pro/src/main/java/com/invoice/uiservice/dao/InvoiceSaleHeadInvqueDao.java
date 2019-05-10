package com.invoice.uiservice.dao;

import com.invoice.bean.db.InvoiceSaleHeadInvque;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("InvoiceSaleHeadInvqueDao")
public interface InvoiceSaleHeadInvqueDao {
    //界面
    List<InvoiceSaleHeadInvque> getAll(Map<String, Object> p);

    int getCount(Map<String, Object> p);

}
