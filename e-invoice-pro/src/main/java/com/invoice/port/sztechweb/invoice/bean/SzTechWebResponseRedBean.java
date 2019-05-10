package com.invoice.port.sztechweb.invoice.bean;

import java.util.List;

public class SzTechWebResponseRedBean {
	private int code;
	private List<Data> data;
	private String msg;
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}
	
	public List<Data> getData() {
		return data;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

	public class Data {
	    private String taxpayer_num;
	    private String b_trade_no;
	    private String g_trade_no;
	    private int state;
	    private String red_g_trade_no;
	    
	    public void setTaxpayer_num(String taxpayer_num) {
	         this.taxpayer_num = taxpayer_num;
	    }
	    
	    public String getTaxpayer_num() {
	         return taxpayer_num;
	    }

	    public void setB_trade_no(String b_trade_no) {
	         this.b_trade_no = b_trade_no;
	    }
	    
	    public String getB_trade_no() {
	         return b_trade_no;
	    }

	    public void setG_trade_no(String g_trade_no) {
	         this.g_trade_no = g_trade_no;
	    }
	    
	    public String getG_trade_no() {
	         return g_trade_no;
	    }

	    public void setState(int state) {
	         this.state = state;
	    }
	    
	    public int getState() {
	         return state;
	    }

	    public void setRed_g_trade_no(String red_g_trade_no) {
	         this.red_g_trade_no = red_g_trade_no;
	    }
	    
	    public String getRed_g_trade_no() {
	         return red_g_trade_no;
	    }
	}
}
