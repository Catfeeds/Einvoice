package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RESPONSE_FPKJ")
public class HangXinRtInvoiceBean {
	
	/*返回代码*/
	private String returncode;
	/*返回描述*/
	private String returnmessage;
	/*合计不含税金额*/
	private double hjbhsje;
	/*合计税额*/
	private double hjse;
	/*开票日期*/
	private String kprq;
	/*所属月份*/
	private String ssyf;
	/*发票代码*/
	private String fp_dm;
	/*发票号码*/
	private String fp_hm;
	/*销售清单标识*/
	private String xhqdbz;
	/*成功失败code*/
	private String retcode;
	/*防伪密文*/
	private String fwmw;
	/*校验码*/
	private String jym;
	/*签名值*/
	private String szqm;
	/*二维码*/
	private String ewm;
	
	 @XmlElement(name = "RETURNCODE")  
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	 @XmlElement(name = "RETURNMESSAGE")  
	public String getReturnmessage() {
		return returnmessage;
	}
	public void setReturnmessage(String returnmessage) {
		this.returnmessage = returnmessage;
	}
	 @XmlElement(name = "HJBHSJE")  
	public double getHjbhsje() {
		return hjbhsje;
	}
	public void setHjbhsje(double hjbhsje) {
		this.hjbhsje = hjbhsje;
	}
	 @XmlElement(name = "HJSE")  
	public double getHjse() {
		return hjse;
	}
	public void setHjse(double hjse) {
		this.hjse = hjse;
	}
	 @XmlElement(name = "KPRQ")  
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	 @XmlElement(name = "SSYF")  
	public String getSsyf() {
		return ssyf;
	}
	public void setSsyf(String ssyf) {
		this.ssyf = ssyf;
	}
	 @XmlElement(name = "FP_DM")  
	public String getFp_dm() {
		return fp_dm;
	}
	public void setFp_dm(String fp_dm) {
		this.fp_dm = fp_dm;
	}
	 @XmlElement(name = "FP_HM")  
	public String getFp_hm() {
		return fp_hm;
	}
	public void setFp_hm(String fp_hm) {
		this.fp_hm = fp_hm;
	}
	 @XmlElement(name = "XHQDBZ")  
	public String getXhqdbz() {
		return xhqdbz;
	}
	public void setXhqdbz(String xhqdbz) {
		this.xhqdbz = xhqdbz;
	}
	 @XmlElement(name = "RETCODE")  
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	 @XmlElement(name = "FWMW")  
	public String getFwmw() {
		return fwmw;
	}
	public void setFwmw(String fwmw) {
		this.fwmw = fwmw;
	}
	 @XmlElement(name = "JYM")  
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	 @XmlElement(name = "SZQM")  
	public String getSzqm() {
		return szqm;
	}
	public void setSzqm(String szqm) {
		this.szqm = szqm;
	}
	 @XmlElement(name = "EWM")  
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	
	
}
