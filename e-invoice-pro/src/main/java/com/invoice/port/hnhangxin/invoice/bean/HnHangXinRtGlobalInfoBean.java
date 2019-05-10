package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="globalInfo")
public class HnHangXinRtGlobalInfoBean {
	
	  private String terminalCode;//<terminalCode>0</terminalCode>
	  private String appId;//<appId>DZFP</appId>
	  private String version;//<version>1.0</version>
	  private String interfaceCode;//<interfaceCode>ECXML.FPKJJG.TS.E_INV</interfaceCode>
	  private String userName;//<userName>111BAWTP</userName>
	  private String passWord;//<passWord>1018027655vrNP9pA6P7i0C5/jropqrw==</passWord>
	  private String taxpayerId;//<taxpayerId>500102666666222</taxpayerId>
	  private String dataExchangeId;//<dataExchangeId />
	  private String authorizationCode;//<authorizationCode />
	  private String requestCode;//<requestCode>111BAWTP</requestCode>
	  private String requestTime;//	  <requestTime>2018-07-25 09:22:43:216</requestTime>
	  private String responseCode;//<responseCode />
	public String getTerminalCode() {
		return terminalCode;
	}
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getInterfaceCode() {
		return interfaceCode;
	}
	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getTaxpayerId() {
		return taxpayerId;
	}
	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
	}
	public String getDataExchangeId() {
		return dataExchangeId;
	}
	public void setDataExchangeId(String dataExchangeId) {
		this.dataExchangeId = dataExchangeId;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	  
	  
 
}
