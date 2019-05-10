package com.invoice.port.szyipiaoyun.invoice.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class YiPiaoYunOpenResponseBean {
    @JSONField(name="operateCode")
    private String operatecode;
    private String message;
    private Datas datas;
    @JSONField(name="pageAndSort")
    private String pageandsort;
    
    public void setOperatecode(String operatecode) {
         this.operatecode = operatecode;
    }
    
    public String getOperatecode() {
         return operatecode;
    }

    public void setMessage(String message) {
         this.message = message;
    }
    
    public String getMessage() {
         return message;
    }

    public void setDatas(Datas datas) {
         this.datas = datas;
    }
    
    public Datas getDatas() {
         return datas;
    }

    public void setPageandsort(String pageandsort) {
         this.pageandsort = pageandsort;
    }
    
    public String getPageandsort() {
         return pageandsort;
    }
     
	public class Datas {
		@JSONField(name="orderObjId")
		private String orderobjid;
		@JSONField(name="taxNo")
		private String taxno;
		@JSONField(name="orderNo")
		private String orderno;
		@JSONField(name="serialNo")
		private String serialno;
		@JSONField(name="pdfUrl")
		private String pdfurl;
		@JSONField(name="invoiceDate")
		private Long invoicedate;
		@JSONField(name="invoiceCode")
		private String invoicecode;
		@JSONField(name="invoiceNum")
		private String invoicenum;
		
		public void setOrderobjid(String orderobjid) {
			 this.orderobjid = orderobjid;
		}
		
		public String getOrderobjid() {
			 return orderobjid;
		}

		public void setTaxno(String taxno) {
			 this.taxno = taxno;
		}
		
		public String getTaxno() {
			 return taxno;
		}

		public void setOrderno(String orderno) {
			 this.orderno = orderno;
		}
		
		public String getOrderno() {
			 return orderno;
		}

		public void setSerialno(String serialno) {
			 this.serialno = serialno;
		}
		
		public String getSerialno() {
			 return serialno;
		}

		public void setPdfurl(String pdfurl) {
			 this.pdfurl = pdfurl;
		}
		
		public String getPdfurl() {
			 return pdfurl;
		}

		public void setInvoicedate(Long invoicedate) {
			 this.invoicedate = invoicedate;
		}
		
		public Long getInvoicedate() {
			 return invoicedate;
		}

		public void setInvoicecode(String invoicecode) {
			 this.invoicecode = invoicecode;
		}
		
		public String getInvoicecode() {
			 return invoicecode;
		}

		public void setInvoicenum(String invoicenum) {
			 this.invoicenum = invoicenum;
		}
		
		public String getInvoicenum() {
			 return invoicenum;
		}
	}
}