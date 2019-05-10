package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import com.invoice.job.JobBase;
@Component
public class AskInvoiceJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		
	}

}
