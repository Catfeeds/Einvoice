package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.GainInvoiceServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author wangmj
 * 自动获取未返回发票数据
 */
@Component("GainInvoiceJob")
public class GainInvoiceJob extends JobBase {

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		// TODO Auto-generated method stub
		String entid = dataMap.getString("entid");
		GainInvoiceServiceImpl service = SpringContextUtil.getBean(GainInvoiceServiceImpl.class);
		service.execute(entid);
	}

}
