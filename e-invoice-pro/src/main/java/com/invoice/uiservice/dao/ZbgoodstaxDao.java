package com.invoice.uiservice.dao;

import com.invoice.bean.db.Zbgoodstax;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ZbgoodstaxDao")
public interface ZbgoodstaxDao {
    List<Zbgoodstax> getZbGoodsBarcodes(Zbgoodstax zbgoodstax);
    List<Zbgoodstax> getZbGoodsNames(Zbgoodstax zbgoodstax);
    Zbgoodstax getGoodsInfoByBarcode(Zbgoodstax zbgoodstax);
    Zbgoodstax getGoodsInfoByName(Zbgoodstax zbgoodstax);
    Zbgoodstax gettaxitemid(Zbgoodstax zbgoodstax);
}

