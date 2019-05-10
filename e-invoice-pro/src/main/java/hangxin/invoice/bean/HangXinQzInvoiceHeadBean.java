package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="REQUEST_FPQZ")
public class HangXinQzInvoiceHeadBean {
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "REQUEST_FPQZ";
	}
	private String fpqz_info;
	
	private HangXinQzFptBean fpt;
	
	private HangXinQzDetaiListBean detailList;
	
	private HangXinQzTsfsBean tsfs;
	
	
	@XmlElement(name = "FPQZ_TSFSXX") 
	public HangXinQzTsfsBean getTsfs() {
		return tsfs;
	}

	public void setTsfs(HangXinQzTsfsBean tsfs) {
		this.tsfs = tsfs;
	}

	@XmlElement(name = "FPQZ_INFO") 
	public String getFpqz_info() {
		return fpqz_info;
	}

	public void setFpqz_info(String fpqz_info) {
		this.fpqz_info = fpqz_info;
	}

	@XmlElement(name = "FPQZ_FPT") 
	public HangXinQzFptBean getFpt() {
		return fpt;
	}

	public void setFpt(HangXinQzFptBean fpt) {
		this.fpt = fpt;
	}
	
	@XmlElement(name = "FPQZ_XMXXS") 
	public HangXinQzDetaiListBean getDetailList() {
		return detailList;
	}

	public void setDetailList(HangXinQzDetaiListBean detailList) {
		this.detailList = detailList;
	}
	
	
	
}
