package com.invoice.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JobAgent {
	
	@Autowired
	SchedulerFactoryBean jobscheduler;
	
	public static Logger log = Logger.getLogger(JobAgent.class);

	private String JOB_GROUP_NAME = "efuture";
	private String TRIGGER_GROUP_NAME = "efutureTrigger";

	/** 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 
	 * @return */
	public JobDetail addJob(String jobName, Job job, String cronExpression,String jobDescription,Map<String,Object> dataMap) throws SchedulerException, ParseException {
		return addJob(jobName, null, jobName, null, job, cronExpression,jobDescription,dataMap);
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName
	 *        任务名
	 * @param jobGroupName
	 *        任务组名
	 * @param triggerName
	 *        触发器名
	 * @param triggerGroupName
	 *        触发器组名
	 * @param job
	 *        任务
	 * @param cronExpression
	 *        时间设置，参考quartz说明文档
	 */
	public JobDetail addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Job job, String cronExpression,String jobDescription,Map<String,Object> dataMap) throws SchedulerException, ParseException {
		if (StringUtils.isEmpty(jobGroupName)) {
			jobGroupName = JOB_GROUP_NAME;
		}
		if (StringUtils.isEmpty(triggerGroupName)) {
			triggerGroupName = TRIGGER_GROUP_NAME;
		}
		;
		
		JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(jobName, jobGroupName).withDescription(jobDescription).build();// 任务执行类，任务名，任务组
		if(dataMap!=null){
			jobDetail.getJobDataMap().putAll(dataMap);
		}
		
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, triggerGroupName).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
		//CronTriggerImpl trigger = new CronTriggerImpl(jobName, triggerGroupName, cronExpression);// 触发器名,触发器组,cron表达式
		jobscheduler.getScheduler().scheduleJob(jobDetail, trigger);

		log.info("Add a new job : " + jobName);

		// 启动
		if (!jobscheduler.getScheduler().isShutdown()) {
			jobscheduler.start();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			log.info("Job " + jobName + " will be run on " + sf.format(trigger.getNextFireTime()));
		}
		
		return jobDetail;
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 */
	public void modifyJobTime(String jobName, String cronExpression) throws SchedulerException, ParseException {
		modifyJobTime(jobName, null, cronExpression);
	}

	/**
	 * 修改一个任务的触发时间
	 */
	public void modifyJobTime(String triggerName, String triggerGroupName, String cronExpression)
			throws SchedulerException, ParseException {
		if (StringUtils.isEmpty(triggerGroupName)) {
			triggerGroupName = TRIGGER_GROUP_NAME;
		}
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
		Trigger trigger = jobscheduler.getScheduler().getTrigger(triggerKey );
		if (trigger != null) {
			CronTriggerImpl ct = (CronTriggerImpl) trigger;
			// 修改时间
			ct.setCronExpression(cronExpression);
			// 重启触发器
			jobscheduler.getScheduler().resumeTrigger(triggerKey);

			log.info("Modify job." + triggerName + "cron expression to " + cronExpression);
		}
	}

	/** 移除一个任务和触发器(使用默认的任务组名，触发器名，触发器组名) */
	public void removeJob(String jobName, String triggerName) throws SchedulerException {
		removeJob(jobName, null, triggerName, null);
	}

	/** 移除一个任务和触发器 */
	public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
			throws SchedulerException {
		if (StringUtils.isEmpty(jobGroupName)) {
			jobGroupName = JOB_GROUP_NAME;
		}
		if (StringUtils.isEmpty(triggerGroupName)) {
			triggerGroupName = TRIGGER_GROUP_NAME;
		}
		log.info("Remove a job." + jobName + " ing...");

		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
		JobKey jobKey = new JobKey(jobName, jobGroupName);

		jobscheduler.getScheduler().pauseTrigger(triggerKey);// 停止触发器
		jobscheduler.getScheduler().unscheduleJob(triggerKey);// 移除触发器
		jobscheduler.getScheduler().deleteJob(jobKey);// 删除任务
		log.info("Remove a job." + jobName + " success");
	}

	public boolean isJobExists(String jobName, String groupName) {
		if(groupName == null ) groupName =JOB_GROUP_NAME;
		JobKey jobKey = new JobKey(jobName, groupName);
		try {
			return jobscheduler.getScheduler().checkExists(jobKey );
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
	}
}
