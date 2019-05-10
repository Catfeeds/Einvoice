package com.invoice.port.sztechweb.invoice.bean;

import java.util.Date;

public class SzTechWebQueryResponseBean {
	private int code;
    private String msg;
    private Data data;
    
    public void setCode(int code) {
         this.code = code;
    }
    
    public int getCode() {
         return code;
    }

    public void setMsg(String msg) {
         this.msg = msg;
    }
    
    public String getMsg() {
         return msg;
    }

    public void setData(Data data) {
         this.data = data;
    }
    
    public Data getData() {
         return data;
    }

	public class Data {
		private String g_unique_id;
		private String fail_msg;
		private String audit_pay_state;
		private String audit_pay_msg;
		private Date ticket_date;
		private String ticket_sn;
		private String ticket_code;
		private String ticket_total_amount_has_tax;
		private String ticket_total_amount_no_tax;
		private String ticket_tax_amount;
		private String is_red;
		private String has_red;
		private String gp_pdf_name;
		private String check_code;
		private String order_id;
		private int status;
		
		public void setG_unique_id(String g_unique_id) {
			this.g_unique_id = g_unique_id;
		}
		
		public String getG_unique_id() {
			return g_unique_id;
		}

		public void setFail_msg(String fail_msg) {
			this.fail_msg = fail_msg;
		}
		
		public String getFail_msg() {
			return fail_msg;
		}

		public void setAudit_pay_state(String audit_pay_state) {
			this.audit_pay_state = audit_pay_state;
		}
		
		public String getAudit_pay_state() {
			return audit_pay_state;
		}

		public void setAudit_pay_msg(String audit_pay_msg) {
			this.audit_pay_msg = audit_pay_msg;
		}
		
		public String getAudit_pay_msg() {
			return audit_pay_msg;
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

		public void setTicket_code(String ticket_code) {
			this.ticket_code = ticket_code;
		}
		
		public String getTicket_code() {
			return ticket_code;
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

		public void setTicket_tax_amount(String ticket_tax_amount) {
			this.ticket_tax_amount = ticket_tax_amount;
		}
		
		public String getTicket_tax_amount() {
			return ticket_tax_amount;
		}

		public void setIs_red(String is_red) {
			this.is_red = is_red;
		}
		
		public String getIs_red() {
			return is_red;
		}

		public void setHas_red(String has_red) {
			this.has_red = has_red;
		}
		
		public String getHas_red() {
			return has_red;
		}

		public void setGp_pdf_name(String gp_pdf_name) {
			this.gp_pdf_name = gp_pdf_name;
		}
		
		public String getGp_pdf_name() {
			return gp_pdf_name;
		}

		public void setCheck_code(String check_code) {
			this.check_code = check_code;
		}
		
		public String getCheck_code() {
			return check_code;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}
		
		public String getOrder_id() {
			return order_id;
		}

		public void setStatus(int status) {
			this.status = status;
		}
		
		public int getStatus() {
			return status;
		}
	}
}
