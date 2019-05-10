package com.einvoice.dynamicdb;


public class DataSourceDynamicBean extends DynamicBean{
	private String url;
	private String username;
	private String password;
	private String driver;
	private String dbcharcode;
	private Integer maxactive;
	private Integer initsize;
	private Integer maxwait;
	private Integer minidle;
	private Integer maxidle;

	public DataSourceDynamicBean(String beanName) {
		super(beanName);
	}
	@Override
	protected String getBeanXml() {
		
		initsize = initsize==null?3:initsize;
		maxactive = maxactive==null?50:maxactive;
		
		return "<bean id=\""+this.beanName+"\" class=\"com.alibaba.druid.pool.DruidDataSource\" init-method=\"init\" destroy-method=\"close\">" +
				"<property name=\"driverClassName\" value=\""+this.driver+"\" />"+
				"<property name=\"url\" value=\""+this.url+"\" />" +
				"<property name=\"username\" value=\""+this.username+"\" />" +
				"<property name=\"password\" value=\""+this.password+"\" />" +
				"<property name=\"maxActive\" value=\""+this.maxactive+"\" />" +
				"<property name=\"maxWait\" value=\"60000\" />" +
				"<property name=\"minIdle\" value=\"3\" />" +
				"<property name=\"timeBetweenEvictionRunsMillis\" value=\"60000\" />" +
				"<property name=\"minEvictableIdleTimeMillis\" value=\"300000\" />" +
				"<property name=\"testWhileIdle\" value=\"true\" />" +
				"<property name=\"testOnBorrow\" value=\"false\" />" +
				"<property name=\"testOnReturn\" value=\"false\" />" +
				"</bean>";
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getDbcharcode() {
		return dbcharcode;
	}
	public void setDbcharcode(String dbcharcode) {
		this.dbcharcode = dbcharcode;
	}
	public Integer getMaxactive() {
		return maxactive;
	}
	public void setMaxactive(Integer maxactive) {
		this.maxactive = maxactive;
	}
	public Integer getInitsize() {
		return initsize;
	}
	public void setInitsize(Integer initsize) {
		this.initsize = initsize;
	}
	public Integer getMaxwait() {
		return maxwait;
	}
	public void setMaxwait(Integer maxwait) {
		this.maxwait = maxwait;
	}
	public Integer getMinidle() {
		return minidle;
	}
	public void setMinidle(Integer minidle) {
		this.minidle = minidle;
	}
	public Integer getMaxidle() {
		return maxidle;
	}
	public void setMaxidle(Integer maxidle) {
		this.maxidle = maxidle;
	}

}
