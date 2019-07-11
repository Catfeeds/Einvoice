package com.invoice.uiservice.service.impl;

import com.invoice.bean.db.Zbgoodstax;
import com.invoice.uiservice.dao.ZbgoodstaxDao;
import com.invoice.uiservice.service.ZbgoodtaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ZbgoodtaxService")
public class ZbgoodsidServiceImpl implements ZbgoodtaxService{
    @Autowired
    private ZbgoodstaxDao zbgoodstaxDao;

    @Override
    public List<Zbgoodstax> getZbGoodsBarcodes(Zbgoodstax zbgoodstax) {
        return zbgoodstaxDao.getZbGoodsBarcodes(zbgoodstax);
    }

    @Override
    public List<Zbgoodstax> getZbGoodsNames(Zbgoodstax zbgoodstax) {
        return zbgoodstaxDao.getZbGoodsNames(zbgoodstax);
    }

    @Override
    public Zbgoodstax getGoodsInfoByBarcode(Zbgoodstax zbgoodstax) {
        return zbgoodstaxDao.getGoodsInfoByBarcode(zbgoodstax);
    }

    @Override
    public Zbgoodstax getGoodsInfoByName(Zbgoodstax zbgoodstax) {
        return zbgoodstaxDao.getGoodsInfoByName(zbgoodstax);
    }

	@Override
	public Zbgoodstax gettaxitemid(Zbgoodstax zbgoodstax) {
		return zbgoodstaxDao.gettaxitemid(zbgoodstax);
	}

	
}
