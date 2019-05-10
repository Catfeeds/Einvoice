package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.ZMSaleServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author Baij
 * 提取扣项数据
 */
@Component("ZMSaleJob")
public class ZMSaleJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		
		String entid = dataMap.getString("entid");
		if(StringUtils.isEmpty(entid)) {
			throw new Exception("企业ID必须指定："+entid);
		}
		
		ZMSaleServiceImpl service = SpringContextUtil.getBean(ZMSaleServiceImpl.class);
		service.execute(entid);
	}

}
