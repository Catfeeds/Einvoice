package com.efuture.portal.beans;

public class Option {
	public String value;
	public String name;
	public String getValue() {
		return value.trim();
	}
	public void setValue(String value) {
		this.value = value.trim();
	}
	public String getName() {
		return name.trim();
	}
	public void setName(String name) {
		this.name = name.trim();
	}
}
