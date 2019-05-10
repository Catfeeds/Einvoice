package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.InvoiceCallBackServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author Baij
 * 开票数据回传ERP
 */
@Component("InvoiceCallBackJob")
public class InvoiceCallBackJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		String entid = dataMap.getString("entid");
		if(StringUtils.isEmpty(entid)) {
			throw new Exception("企业ID必须指定："+entid);
		}
		String sheettype = dataMap.getString("sheettype");
		if(StringUtils.isEmpty(sheettype)) {
			throw new Exception("业务类型必须指定：sheettype");
		}
		
		String shopid=null;
		if(dataMap.containsKey("shopid")) {
			shopid = dataMap.getString("sheettype");
		}
		
		InvoiceCallBackServiceImpl service = SpringContextUtil.getBean(InvoiceCallBackServiceImpl.class);
		service.execute(entid,sheettype,shopid);
	}

}
