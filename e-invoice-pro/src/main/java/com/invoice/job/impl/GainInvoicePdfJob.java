package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.GainInvoicePdfServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author wangmj
 * 自动开票
 */
@Component("GainInvoicePdfJob")
public class GainInvoicePdfJob extends JobBase {

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		// TODO Auto-generated method stub
		String entid = dataMap.getString("entid");
		GainInvoicePdfServiceImpl service = SpringContextUtil.getBean(GainInvoicePdfServiceImpl.class);
		service.execute(entid);
	}

}
