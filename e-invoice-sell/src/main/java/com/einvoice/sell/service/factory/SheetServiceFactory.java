package com.einvoice.sell.service.factory;

import com.einvoice.sell.service.SheetService;
import com.einvoice.sell.util.SpringContextUtil;

public class SheetServiceFactory {
	static public SheetService getInstance(String name){
		return SpringContextUtil.getBean(name,SheetService.class);
	}
}
