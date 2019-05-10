package com.invoice.uiservice.service.impl;

import com.invoice.bean.db.InvoiceSaleHeadInvque;
import com.invoice.uiservice.dao.InvoiceSaleHeadInvqueDao;
import com.invoice.uiservice.service.InvoiceSaleHeadInvqueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("InvoiceSaleHeadInvqueService")
public class InvoiceSaleHeadInvqueServiceImpl implements InvoiceSaleHeadInvqueService{
    @Resource(name = "InvoiceSaleHeadInvqueDao")
    InvoiceSaleHeadInvqueDao invoiceSaleHeadInvqueDao;

    @Override
    public List<InvoiceSaleHeadInvque> getAll(Map<String, Object> p) throws Exception {
        return invoiceSaleHeadInvqueDao.getAll(p);
    }

    @Override
    public int getCount(Map<String, Object> p) {
        return invoiceSaleHeadInvqueDao.getCount(p);
    }

}
