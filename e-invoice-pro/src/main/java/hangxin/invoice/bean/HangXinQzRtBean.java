package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="RESPONSE_FPQZ")
public class HangXinQzRtBean {
	
	private String fpqqlsh;
	private String fp_dm;
	private String fp_hm;
	private String pdf_file;
	private String pdf_url;
	private String returncode;
	private String returnmessage;
	
	@XmlElement(name = "FPQQLSH")  
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
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
	@XmlElement(name = "PDF_FILE")  
	public String getPdf_file() {
		return pdf_file;
	}
	public void setPdf_file(String pdf_file) {
		this.pdf_file = pdf_file;
	}
	@XmlElement(name = "PDF_URL")  
	public String getPdf_url() {
		return pdf_url;
	}
	public void setPdf_url(String pdf_url) {
		this.pdf_url = pdf_url;
	}
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
	
	
}
