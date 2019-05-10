package com.invoice.apiservice.service.factory;

import com.invoice.apiservice.service.SheetService;
import com.invoice.util.SpringContextUtil;

public class SheetServiceFactory {
	static public SheetService getInstance(String sheettype) {
		if ("1".equals(sheettype)) {
			return SpringContextUtil.getBean("BillInvoiceService", SheetService.class);
		} else if ("2".equals(sheettype)) {
			return SpringContextUtil.getBean("WholeInvoiceService", SheetService.class);
		} else if ("0".equals(sheettype)) {
			return SpringContextUtil.getBean("HandInvoiceService", SheetService.class);
		} else if ("3".equals(sheettype)) {
			return SpringContextUtil.getBean("HandCardService", SheetService.class);
		} else if ("4".equals(sheettype)) {
			return SpringContextUtil.getBean("ChargeInvoiceService", SheetService.class);
		} else if ("5".equals(sheettype)) {
			return SpringContextUtil.getBean("TXDInvoiceService", SheetService.class);
		} else if ("6".equals(sheettype)) {
			return SpringContextUtil.getBean("ZMInvoiceService", SheetService.class);
		} else if ("7".equals(sheettype)) {
			//通用业务数据开票
			return SpringContextUtil.getBean("CommonInvoiceSheetService", SheetService.class);
		} else if ("99".equals(sheettype)) {
			return SpringContextUtil.getBean("TestInvoiceSheetService", SheetService.class);
		} else {
			throw new RuntimeException("sheettype is not define:" + sheettype);
		}
	}
}
