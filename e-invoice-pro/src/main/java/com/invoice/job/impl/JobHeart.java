package com.invoice.job.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.invoice.config.FGlobal;
import com.invoice.job.JobAgent;
import com.invoice.job.JobBase;
import com.invoice.job.service.impl.JobServiceImpl;
import com.invoice.util.NewHashMap;
import com.invoice.util.SpringContextUtil;

/**
 * @author Baij
 * 
 */
@Component
public class JobHeart extends JobBase {
	
	public static Logger log = Logger.getLogger(JobHeart.class);
	private String cronExpression = "0 */10 * * * ?";

	@Override
	public void jobdo(JobDataMap dataMap) throws Exception {
		work();
	}
	
	public void init() throws SchedulerException, ParseException{
		SpringContextUtil.getBean(JobAgent.class).addJob("jobHeart", this, cronExpression,"定时任务心跳",null);
		work();
	}

	public void work() {
		List<Map<String, Object>> jobList = SpringContextUtil.getBean(JobServiceImpl.class).getJobs(null);
		for (Map<String, Object> map : jobList) {
			Object jobGroup = map.get("jobGroup");
			if(FGlobal.jobGroup==null || !FGlobal.jobGroup.equals(jobGroup)){
				continue;
			}
			
			String beanId = (String) map.get("jobClass");
			String jobId = (String) map.get("jobId");
			String jobName = (String) map.get("jobName");
			String cronexp = (String) map.get("cronExpression");
			boolean enable = ((Boolean) map.get("enable")).booleanValue();
			String jobJsonData = (String) map.get("jobJsonData");
			updateJob(beanId, jobId, jobName, cronexp, enable, jobJsonData);
		}
	}

	public void updateJob(String beanId, String jobId, String jobName, String cronexp, boolean enable, String jobJsonData) {
		
		JobAgent jobAgent = SpringContextUtil.getBean(JobAgent.class);
		
		boolean exists = jobAgent.isJobExists(jobId, null);

		if (enable && !exists && !StringUtils.isEmpty(cronexp)) {
			try {
				
				//将配置的json数据转为map并存入job
				Map<String, Object> dataMap = new NewHashMap<String, Object>();
				if(jobJsonData!=null && jobJsonData.length()>0){
					JSONObject json = JSONObject.parseObject(jobJsonData);
					Set<String> keySet = json.keySet();
					for (String key : keySet) {
						dataMap.put(key, json.get(key));
					}
				}
				
				jobAgent.addJob(jobId, SpringContextUtil.getBean(beanId, Job.class), cronexp, jobName, dataMap);
				
			} catch (Exception e) {
				log.error("fail create job " + jobId + " exception:" + e.getMessage());
			}
		} else if (StringUtils.isEmpty(cronexp)) {
			if (exists) {
				log.info("job " + jobId + " cronexp not set, the job will be stop ");
				try {
					jobAgent.removeJob(jobId, jobId);
				} catch (SchedulerException e) {
					log.error("job " + jobId + " remove fail", e);
				}
			} else {
				log.info("job " + jobId + " cronexp not set, the job is skipped ");
			}
		} else if (!enable) {
			if (jobAgent.isJobExists(jobId, null)) {
				log.info("job " + jobId + " enable set false, the job will be stop ");
				try {
					jobAgent.removeJob(jobId, jobId);
				} catch (SchedulerException e) {
					log.error("job " + jobId + " remove fail", e);
				}
			}
		}
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
}
