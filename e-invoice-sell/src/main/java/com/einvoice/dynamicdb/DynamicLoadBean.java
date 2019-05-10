package com.einvoice.dynamicdb;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 使用方法loadBean()向spring的beanFactory动态地装载bean，该方法的参数configLocationString等同于
 * spring配置中的contextConfigLocation，同样支持诸如"/WEB-INF/ApplicationContext-*.xml"的写法。
 * 
 * @author FanGang
 * 
 */
@Component
public class DynamicLoadBean implements ApplicationContextAware {

	private ConfigurableApplicationContext applicationContext = null;
	private XmlBeanDefinitionReader beanDefinitionReader;

	/* 初始化方法 */
	public void init() {
		beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) applicationContext.getBeanFactory());
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(applicationContext));
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public boolean hasBean(String beanName) {
		return applicationContext.containsBean(beanName);
	}

	/**
	 * 向spring的beanFactory动态地装载bean
	 * 
	 * @param configLocationString
	 *            要装载的bean所在的xml配置文件位置。
	 */
	public void loadBean(String configLocationString) {

		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
				(BeanDefinitionRegistry) getApplicationContext().getBeanFactory());
		beanDefinitionReader.setResourceLoader(getApplicationContext());
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));

		try {
			String[] configLocations = new String[] { configLocationString };
			for (int i = 0; i < configLocations.length; i++)
				beanDefinitionReader.loadBeanDefinitions(getApplicationContext().getResources(configLocations[i]));
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadBean(DynamicBean dynamicBean) {
		if(beanDefinitionReader==null){
			init();
		}
		
		String beanName = dynamicBean.getBeanName();
		if (applicationContext.containsBean(beanName)) {
			// LogUtil.clientLog("bean【" + beanName + "】已经加载！");
			return;
		}
		DynamicResource dy = new DynamicResource(dynamicBean);
		beanDefinitionReader.loadBeanDefinitions(dy);
		// LogUtil.clientLog("初始化bean【" + dynamicBean.getBeanName() + "】耗时" +
		// (System.currentTimeMillis() - startTime) + "毫秒。");
	}

	public void removeBean(String beanName) {
		if (applicationContext.containsBean(beanName)) {
			BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext
					.getBeanFactory();
			beanDefinitionRegistry.removeBeanDefinition(beanName);
			return;
		}
	}
}