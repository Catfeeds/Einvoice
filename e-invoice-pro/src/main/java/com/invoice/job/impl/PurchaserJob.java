package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;
import com.invoice.job.JobBase;
import com.invoice.job.service.impl.PurchaserServiceImpl;
import com.invoice.util.SpringContextUtil;

@Component("PurchaserJob")
public class PurchaserJob extends JobBase{
	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		PurchaserServiceImpl service = SpringContextUtil.getBean(PurchaserServiceImpl.class);
		service.execute();
	}
}
