package com.invoice.port.sztechweb.invoice.bean;

public class SzTechWebQueryRequestBean {
	private String taxpayer_num;
    private String order_id;
    private String g_unique_id;
    private int is_red;
    
    public int getIs_red() {
		return is_red;
	}

	public void setIs_red(int is_red) {
		this.is_red = is_red;
	}

	public void setTaxpayer_num(String taxpayer_num) {
         this.taxpayer_num = taxpayer_num;
    }
    
    public String getTaxpayer_num() {
         return taxpayer_num;
    }

    public void setOrder_id(String order_id) {
         this.order_id = order_id;
    }
    
    public String getOrder_id() {
         return order_id;
    }

    public void setG_unique_id(String g_unique_id) {
         this.g_unique_id = g_unique_id;
    }
    
    public String getG_unique_id() {
         return g_unique_id;
    }
}
