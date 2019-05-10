package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.ChargeSaleServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author Baij
 * 提取扣项数据
 */
@Component("ChargeSaleJob")
public class ChargeSaleJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		
		String entid = dataMap.getString("entid");
		
		ChargeSaleServiceImpl service = SpringContextUtil.getBean(ChargeSaleServiceImpl.class);
		service.execute(entid);
	}

}
