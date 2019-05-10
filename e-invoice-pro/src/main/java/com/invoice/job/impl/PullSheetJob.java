package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.PullSheetServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author Baij
 * 自动拉取业务数据
 */
@Component("PullSheetJob")
public class PullSheetJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		
		String entid = dataMap.getString("entid");
		if(StringUtils.isEmpty(entid)) {
			throw new Exception("企业ID必须指定");
		}
		String sheettype = dataMap.getString("sheettype");
		if(StringUtils.isEmpty(sheettype)) {
			throw new Exception("业务类型必须指定");
		}
		
		String shopid = dataMap.getString("shopid");
		
		PullSheetServiceImpl service = SpringContextUtil.getBean(PullSheetServiceImpl.class);
		service.execute(entid,sheettype,shopid);
	}

}
