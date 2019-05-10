package com.invoice.job.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.bean.db.Invque;
import com.invoice.rtn.data.RtnData;
import com.invoice.task.queue.AskInvoiceTask;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.util.NewHashMap;
/**
 * 获取未返回的PDF
 * @author wangmj
 * 
 */
@Service
public class GainInvoicePdfServiceImpl {
	public final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	InvqueDao invqueDao;
	
	@Autowired
	AskInvoiceTask askInvoiceTask;
	
	@Autowired
	TaxinfoDao taxinfoDao;
	
	public void execute(String entid) throws Exception {
		
		Map<String,Object> map = new NewHashMap<String,Object>();
		map.put("entid", entid);
		// 未返回PDF队列数据
		log.info("JOB开始执行获取PDF");
		List<Invque> list = invqueDao.getInvques(map);
		for (Invque invque : list) {
			getpdf(invque);
		}
		log.info("JOB获取PDF完毕！");
	}
	
	private void getpdf(Invque invque) {
		askInvoiceTask.getPdf(invque,new RtnData());
	}
	
	
	
}
