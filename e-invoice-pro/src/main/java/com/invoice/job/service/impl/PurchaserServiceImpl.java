package com.invoice.job.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.invoice.job.dao.JobDao;
import com.invoice.uiservice.dao.CompanyNameDao;

@Service
public class PurchaserServiceImpl {
	static public final Log log = LogFactory.getLog(PurchaserServiceImpl.class);
	
	@Autowired
	JobDao jobDao;
	
	@Autowired
	CompanyNameDao purchaserDao;
	
	public void execute(){
		log.info("执行提取手工开票抬头信息开始");
		purchaserDao.insertInvoiceInfo();
		log.info("执行提取手工开票抬头信息结束");
	}
}
