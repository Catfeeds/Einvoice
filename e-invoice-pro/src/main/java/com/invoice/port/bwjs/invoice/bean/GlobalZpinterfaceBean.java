package com.invoice.port.bwjs.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.invoice.port.bwjf.invoice.bean.BwjfInvoiceHeadBean;
@XmlRootElement(name="business")
public class GlobalZpinterfaceBean {
	private String id;
	private String comment;
	private BwjsdjzpInvoiceHeadBean body;
	
	
	@XmlAttribute(name = "id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlAttribute(name = "comment")
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	@XmlElement(name = "body")  
	public BwjsdjzpInvoiceHeadBean getBody() {
		return body;
	}
	public void setBody(BwjsdjzpInvoiceHeadBean body) {
		this.body = body;
	}
	
}
