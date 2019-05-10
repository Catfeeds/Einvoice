package com.invoice.uiservice.service;

import com.invoice.bean.db.InvoiceSaleHeadInvque;

import java.util.List;
import java.util.Map;

public interface InvoiceSaleHeadInvqueService {
    List<InvoiceSaleHeadInvque> getAll(Map<String, Object> p)throws Exception;

    int getCount(Map<String, Object> p);

}
