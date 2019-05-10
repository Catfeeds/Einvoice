package com.invoice.port.sztechweb.invoice.bean;

import java.util.Date;

public class SzTechWebCallBackAcceptBean {
	private String appkey;
    private String order_id;
    private String check_code;
    private String g_unique_id;
    private String pdf_url;
    private String qrcode;
    private Date notify_time;
    private String notify_type;
    private String message;
    private String ticket_code;
    private Date ticket_date;
    private String ticket_sn;
    private int ticket_status;
    private String ticket_tax_amount;
    private String ticket_total_amount_has_tax;
    private String ticket_total_amount_no_tax;
    
    public void setAppkey(String appkey) {
         this.appkey = appkey;
    }
    
    public String getAppkey() {
         return appkey;
    }

    public void setOrder_id(String order_id) {
         this.order_id = order_id;
    }
    
    public String getOrder_id() {
         return order_id;
    }

    public void setCheck_code(String check_code) {
         this.check_code = check_code;
    }
    
    public String getCheck_code() {
         return check_code;
    }

    public void setG_unique_id(String g_unique_id) {
         this.g_unique_id = g_unique_id;
    }
    
    public String getG_unique_id() {
         return g_unique_id;
    }

    public void setPdf_url(String pdf_url) {
         this.pdf_url = pdf_url;
    }
    
    public String getPdf_url() {
         return pdf_url;
    }

    public void setQrcode(String qrcode) {
         this.qrcode = qrcode;
    }
    
    public String getQrcode() {
         return qrcode;
    }

    public void setNotify_time(Date notify_time) {
         this.notify_time = notify_time;
    }
    
    public Date getNotify_time() {
         return notify_time;
    }

    public void setNotify_type(String notify_type) {
         this.notify_type = notify_type;
    }
    
    public String getNotify_type() {
         return notify_type;
    }

    public void setMessage(String message) {
         this.message = message;
    }
    
    public String getMessage() {
         return message;
    }

    public void setTicket_code(String ticket_code) {
         this.ticket_code = ticket_code;
    }
    
    public String getTicket_code() {
         return ticket_code;
    }

    public void setTicket_date(Date ticket_date) {
         this.ticket_date = ticket_date;
    }
    
    public Date getTicket_date() {
         return ticket_date;
    }

    public void setTicket_sn(String ticket_sn) {
         this.ticket_sn = ticket_sn;
    }
    
    public String getTicket_sn() {
         return ticket_sn;
    }

    public void setTicket_status(int ticket_status) {
         this.ticket_status = ticket_status;
    }
    
    public int getTicket_status() {
         return ticket_status;
    }

    public void setTicket_tax_amount(String ticket_tax_amount) {
         this.ticket_tax_amount = ticket_tax_amount;
    }
    
    public String getTicket_tax_amount() {
         return ticket_tax_amount;
    }

    public void setTicket_total_amount_has_tax(String ticket_total_amount_has_tax) {
         this.ticket_total_amount_has_tax = ticket_total_amount_has_tax;
    }
    
    public String getTicket_total_amount_has_tax() {
         return ticket_total_amount_has_tax;
    }

    public void setTicket_total_amount_no_tax(String ticket_total_amount_no_tax) {
         this.ticket_total_amount_no_tax = ticket_total_amount_no_tax;
    }
    
    public String getTicket_total_amount_no_tax() {
         return ticket_total_amount_no_tax;
    }
}
