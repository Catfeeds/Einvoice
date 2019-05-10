package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.AutoOpenInvoiceServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author Baij
 * 自动开票
 */
@Component("AutoOpenJob")
public class AutoOpenJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		
		String entid = dataMap.getString("entid");
		if(StringUtils.isEmpty(entid)) {
			throw new Exception("企业ID必须指定："+entid);
		}
		
		String shopid = dataMap.getString("shopid");
		
		AutoOpenInvoiceServiceImpl service = SpringContextUtil.getBean(AutoOpenInvoiceServiceImpl.class);
		service.execute(entid,shopid);
	}

}
