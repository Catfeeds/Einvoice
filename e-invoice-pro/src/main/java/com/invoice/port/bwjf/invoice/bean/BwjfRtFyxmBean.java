package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fyxm")
public class BwjfRtFyxmBean {
	private int count;
	private BwjfRtmxGroupBean group;
	
	@XmlAttribute(name = "count")
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@XmlElement(name = "group")  
	public BwjfRtmxGroupBean getGroup() {
		return group;
	}
	public void setGroup(BwjfRtmxGroupBean group) {
		this.group = group;
	}
	
	
}
