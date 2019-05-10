package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.BillSaleServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author Baij
 * 提取小票销售数据
 */
@Component("BillSaleJob")
public class BillSaleJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		BillSaleServiceImpl service = SpringContextUtil.getBean(BillSaleServiceImpl.class);
		service.initBillSale();
		service.execute();
	}

}
