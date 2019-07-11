package com.invoice.uiservice.service;

import com.invoice.bean.db.Zbgoodstax;

import java.util.List;

public interface ZbgoodtaxService {
    List<Zbgoodstax> getZbGoodsBarcodes(Zbgoodstax zbgoodstax) ;
    List<Zbgoodstax> getZbGoodsNames(Zbgoodstax zbgoodstax);
    Zbgoodstax getGoodsInfoByBarcode(Zbgoodstax zbgoodstax) ;
    Zbgoodstax getGoodsInfoByName(Zbgoodstax zbgoodstax);
    Zbgoodstax gettaxitemid(Zbgoodstax zbgoodstax);
}

