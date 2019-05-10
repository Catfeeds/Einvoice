package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="kpxx")
public class BwjfRtKpxxBean {
	private int count;
	private BwjfRtGroupbean group;
	
	@XmlAttribute(name = "count")
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@XmlElement(name = "group")  
	public BwjfRtGroupbean getGroup() {
		return group;
	}
	public void setGroup(BwjfRtGroupbean group) {
		this.group = group;
	}
	
	
	
}
