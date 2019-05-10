package com.invoice.port.bwjs.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="kpxx")
public class BwjsdjzpRtKpxxBean {
	private int count;
	private BwjsdjzpRtGroupbean group;
	
	@XmlAttribute(name = "count")
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@XmlElement(name = "group")  
	public BwjsdjzpRtGroupbean getGroup() {
		return group;
	}
	public void setGroup(BwjsdjzpRtGroupbean group) {
		this.group = group;
	}
	
	
	
}
