package com.invoice.job.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.job.dao.JobDao;

@Service
public class JobServiceImpl
{

  @Autowired
  JobDao jobDao;

  public List<Map<String,Object>> getJobs(Map<String, Object> paramMap){
	  return jobDao.getJobs(paramMap);
  }
  
  public int update(Map<String, Object> paramMap){
	  return jobDao.update(paramMap);
  }
}