package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dataDescription")
public class HnHangXinRtDataDescriptionBean {
	  private String  zipCode;//<zipCode>1</zipCode>
	  private String  encryptCode;// <encryptCode>0</encryptCode>
	  private String  codeType;// <codeType>BASE64</codeType>
	  
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getEncryptCode() {
		return encryptCode;
	}
	public void setEncryptCode(String encryptCode) {
		this.encryptCode = encryptCode;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	  
	  
}
