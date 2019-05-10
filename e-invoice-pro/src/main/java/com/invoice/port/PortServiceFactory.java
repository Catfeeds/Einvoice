package com.invoice.port;

import com.invoice.util.SpringContextUtil;

public class PortServiceFactory {
	static public PortService getInstance(String portmode) {
		//湖南航信接口
		if ("8".equals(portmode)) {
			return SpringContextUtil.getBean("HnHangXinServiceImpl", PortService.class);
		} else
		//百望金税直连税控接口
		if ("9".equals(portmode)) {
			return SpringContextUtil.getBean("BwjsdjzpServiceImpl", PortService.class);
		} else
		//浙江爱信诺航信接口
		if ("10".equals(portmode)) {
			return SpringContextUtil.getBean("ZjaxnServiceImpl", PortService.class);
		} else
		//深圳易票云航信接口
		if ("A".equals(portmode)) {
			return SpringContextUtil.getBean("SzYiPiaoYunServiceImpl", PortService.class);
		} else
		//深圳腾迅高灯接口
		if ("B".equals(portmode)) {
			return SpringContextUtil.getBean("SzTechWebServiceImpl", PortService.class);
		} else
		if ("C".equals(portmode)) {
			return SpringContextUtil.getBean("NbBwjfServiceImpl", PortService.class);
		} 
		//未在定义范围内则报错退出
		else {
			throw new RuntimeException("portmode is not define:" + portmode);
		}
	}
}
