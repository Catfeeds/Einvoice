package com.invoice.job.impl;

import org.quartz.JobDataMap;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.invoice.job.JobBase;
import com.invoice.job.service.impl.ZBGetgoodsInfoServiceImpl;
import com.invoice.util.SpringContextUtil;
/**
 * @author Baij
 * 开票数据回传ERP
 */
@Component("ZBGetgoodsInfoJob")
public class ZBGetgoodsInfoJob  extends JobBase{

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		String entid = dataMap.getString("entid");
		if(StringUtils.isEmpty(entid)) {
			throw new Exception("企业ID必须指定："+entid);
		}
		String shopid=null;
		if(dataMap.containsKey("shopid")) {
			shopid = dataMap.getString("shopid");
		}
		ZBGetgoodsInfoServiceImpl service = SpringContextUtil.getBean(ZBGetgoodsInfoServiceImpl.class);
		service.execute(entid,shopid);
	}

	
}
