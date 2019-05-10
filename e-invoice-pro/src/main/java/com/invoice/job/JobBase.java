package com.invoice.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.invoice.job.service.impl.JobServiceImpl;
import com.invoice.util.NewHashMap;
import com.invoice.util.SpringContextUtil;

@Component
@DisallowConcurrentExecution
public abstract class JobBase implements Job{
	public static Logger log = Logger.getLogger(JobBase.class);
	
	abstract public void jobdo(JobDataMap dataMap) throws Exception;
	
	public void execute(){
		try {
			jobdo(null);
		} catch (Exception e) {
			log.error("invoke jobdo error", e);
		}
	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String message = "";
		JobDetail jobDetail = context.getJobDetail();
		try {
			jobDetail.getJobDataMap().put("error", "正在运行");
			heart(context);
		} catch (Exception e) {
			log.error("heart error", e);
		}
		
		try {
			long s = System.currentTimeMillis();
			jobdo(jobDetail.getJobDataMap());
			message = "运行完成，耗时："+(System.currentTimeMillis()-s)/1000+"秒";
		} catch (Exception e) {
			log.error("invoke jobdo error", e);
			message = e.getMessage();
		}
		
		try {
			jobDetail.getJobDataMap().put("error", message);
			heart(context);
		} catch (Exception e) {
			log.error("heart error", e);
		}
	}
	
	public void heart(JobExecutionContext context){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Date nextTime = context.getNextFireTime();
		JobDetail job = context.getJobDetail();
		String jobError = job.getJobDataMap().getString("error");
		if(jobError!=null && jobError.length()>80){
			jobError = jobError.substring(0,80);
		}
		log.info(Thread.currentThread().getId() + "-"+job.getDescription()+"-nextTime:"+sf.format(nextTime));
		
		Map<String, Object> paramMap = new NewHashMap<String, Object>();
		paramMap.put("jobid", job.getKey().getName());
		paramMap.put("nextTime", nextTime);
		paramMap.put("heartTime",new Date());
		paramMap.put("jobError", jobError);
		paramMap.put("runCount", 1);
		SpringContextUtil.getBean(JobServiceImpl.class).update(paramMap);
	}
}
