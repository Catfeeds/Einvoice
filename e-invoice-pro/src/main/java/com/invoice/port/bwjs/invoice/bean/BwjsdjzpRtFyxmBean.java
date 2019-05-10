package com.invoice.port.bwjs.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fyxm")
public class BwjsdjzpRtFyxmBean {
	private int count;
	private BwjsdjzpRtmxGroupBean group;
	
	@XmlAttribute(name = "count")
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@XmlElement(name = "group")  
	public BwjsdjzpRtmxGroupBean getGroup() {
		return group;
	}
	public void setGroup(BwjsdjzpRtmxGroupBean group) {
		this.group = group;
	}
	
	
}
