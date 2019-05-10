package com.invoice.bean.db;

public class Taxprintinfo {
	
	 private String entid;// varchar(20) NOT NULL COMMENT '所属企业',
	 private String  taxno;// varchar(20) NOT NULL COMMENT '纳税人识别号',
	 private String kpd;// varchar(50) NOT NULL COMMENT '开票点代码',
	 private String fpprintUrl;// varchar(128) DEFAULT NULL COMMENT '发票请求路径',
	 private String fptempletName;// varchar(128) DEFAULT NULL COMMENT '发票模板名称',
	 private String fpprinterName;// varchar(128) DEFAULT NULL COMMENT '发票打印机名称',
	 private String qdprintUrl;// varchar(128) DEFAULT NULL COMMENT '清单请求路径',
	 private String qdtempletName;// varchar(128) DEFAULT NULL COMMENT '清单模板名称',
	 private String qdprinterName;// varchar(128) DEFAULT NULL COMMENT '清单打印机名称',
	 private int    flag;
	 
	 	
	 	
		public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
		public String getEntid() {
			return entid;
		}
		public void setEntid(String entid) {
			this.entid = entid;
		}
		public String getTaxno() {
			return taxno;
		}
		public void setTaxno(String taxno) {
			this.taxno = taxno;
		}
		public String getKpd() {
			return kpd;
		}
		public void setKpd(String kpd) {
			this.kpd = kpd;
		}
		public String getFpprintUrl() {
			return fpprintUrl;
		}
		public void setFpprintUrl(String fpprintUrl) {
			this.fpprintUrl = fpprintUrl;
		}
		public String getFptempletName() {
			return fptempletName;
		}
		public void setFptempletName(String fptempletName) {
			this.fptempletName = fptempletName;
		}
		public String getFpprinterName() {
			return fpprinterName;
		}
		public void setFpprinterName(String fpprinterName) {
			this.fpprinterName = fpprinterName;
		}
		public String getQdprintUrl() {
			return qdprintUrl;
		}
		public void setQdprintUrl(String qdprintUrl) {
			this.qdprintUrl = qdprintUrl;
		}
		public String getQdtempletName() {
			return qdtempletName;
		}
		public void setQdtempletName(String qdtempletName) {
			this.qdtempletName = qdtempletName;
		}
		public String getQdprinterName() {
			return qdprinterName;
		}
		public void setQdprinterName(String qdprinterName) {
			this.qdprinterName = qdprinterName;
		}
	 
	 
}
