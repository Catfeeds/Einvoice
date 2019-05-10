package com.einvoice.dynamicdb;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.einvoice.sell.bean.ShopConnect;

@Component
public class DBMultiDataSource implements DataSource, ApplicationContextAware {

	private ThreadLocal<String> local = new ThreadLocal<String>();

	@Autowired
	private DynamicLoadBean dynamicLoadBean;

	private ApplicationContext applicationContext = null;
	private DataSource dataSource = null;

	public DBMultiDataSource(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public DBMultiDataSource() {
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return getDataSource().getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		getDataSource().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		getDataSource().setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return getDataSource().getLoginTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

	public DataSource getDataSource() {
		// 获取本线程中存入的数据源标识
		String dataSourceName = this.local.get();
		return getDataSource(dataSourceName);
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource(String dataSourceName) {

		try {
			if (StringUtils.isEmpty(dataSourceName)) {
				return null;// this.dataSource;
			}
			return (DataSource) this.applicationContext.getBean(dataSourceName);
		} catch (NoSuchBeanDefinitionException ex) {
			// throw new Exception("There is not the dataSource ");
			ex.printStackTrace();
		}
		return dataSource;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @Desc 识别请求需要连接的数据源，动态切换数据源
	 * @param reqPath
	 * @param projName
	 * @param user
	 * @return 0：不需要切换数据源，或已切换成功； -1：用户数据错误导致切换失败
	 */
	public int switchDataSource(ShopConnect shopConnect) {
		if (StringUtils.isEmpty(shopConnect))
			return -1;

		String dataSourceName = "ds" + "_" + shopConnect.getConnectid() + "_db";
		if (!dynamicLoadBean.hasBean(dataSourceName)) {
			/* ============== 动态装配DataSource begin ================== */
			DataSourceDynamicBean dataSourceDynamicBean = new DataSourceDynamicBean(dataSourceName);
			dataSourceDynamicBean.setDriver(shopConnect.getDriver());
			dataSourceDynamicBean.setUrl(shopConnect.getUrl());
			dataSourceDynamicBean.setUsername(shopConnect.getUsername());
			dataSourceDynamicBean.setPassword(shopConnect.getPassword());

			dynamicLoadBean.loadBean(dataSourceDynamicBean);//
			/* ============== 动态装配DataSource end ================== */
		}
		// 切换数据源
		this.local.set(dataSourceName);

		return 0;
	}
	
	public boolean removeDataSource(ShopConnect shopConnect) {
		if (StringUtils.isEmpty(shopConnect)) return false;
		
		String dataSourceName = "ds" + "_" + shopConnect.getConnectid() + "_db";
		if (dynamicLoadBean.hasBean(dataSourceName)) {
			dynamicLoadBean.removeBean(dataSourceName);
			System.out.println("remove bean:" + dataSourceName);
		}
		return true;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

}