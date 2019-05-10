package com.invoice.job;

import java.sql.Timestamp;

import com.invoice.job.service.impl.AutoOpenInvoiceServiceImpl;
import com.invoice.job.service.impl.InvoiceCallBackServiceImpl;
import com.invoice.job.service.impl.PullSheetServiceImpl;
import com.invoice.util.SpringContextUtil;

public class Test {
	
	public  static void main(String[] args) {
		java.sql.Timestamp v  = new Timestamp(System.currentTimeMillis());
		if(v instanceof java.sql.Timestamp) {
			String s = ((java.sql.Timestamp)v).toString();
			int idx = s.indexOf(".");
			if(idx>0) {
				System.out.println(s.substring(0, idx));
			}
		}
		System.out.println("2018-09-13 17:49:32.0".replace(".0", ""));
		SpringContextUtil.init();
		
		PullSheetServiceImpl service = SpringContextUtil.getBean(PullSheetServiceImpl.class);
		AutoOpenInvoiceServiceImpl service2 = SpringContextUtil.getBean(AutoOpenInvoiceServiceImpl.class);
		InvoiceCallBackServiceImpl service3 = SpringContextUtil.getBean(InvoiceCallBackServiceImpl.class);
		try {
//			service.execute("A00001002","7","01");
//			service2.execute("A00001002",null);
//			service3.execute("A00001002","7","01");
			service.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
