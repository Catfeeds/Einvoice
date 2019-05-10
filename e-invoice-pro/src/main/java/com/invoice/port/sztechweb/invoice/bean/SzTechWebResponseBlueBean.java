package com.invoice.port.sztechweb.invoice.bean;

public class SzTechWebResponseBlueBean {
	private int code;
    private Data data;
    private String msg;
    
    public void setCode(int code) {
         this.code = code;
    }
    
    public int getCode() {
         return code;
    }

    public void setData(Data data) {
         this.data = data;
    }
    
    public Data getData() {
         return data;
    }

    public void setMsg(String msg) {
         this.msg = msg;
    }
    
    public String getMsg() {
         return msg;
    }

	public class Data {
	
	    private String g_unique_id;
	    private String order_id;
	    private int state;
	    
	    public void setG_unique_id(String g_unique_id) {
	         this.g_unique_id = g_unique_id;
	    }
	
	    public String getG_unique_id() {
	         return g_unique_id;
	    }
	
	    public void setOrder_id(String order_id) {
	         this.order_id = order_id;
	    }
	    
	    public String getOrder_id() {
	         return order_id;
	    }
	
	    public void setState(int state) {
	         this.state = state;
	    }
	    
	    public int getState() {
	         return state;
	    }
	}
}
