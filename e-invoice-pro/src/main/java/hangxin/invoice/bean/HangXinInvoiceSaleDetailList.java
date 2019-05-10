package hangxin.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FP_KJMXS")
public class HangXinInvoiceSaleDetailList {
	
	private List<HangXinInvoiceSaleDetailBean> details;
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FP_KJMX;";
	}
	
	private int size;
	
	
	@XmlAttribute(name = "size")
	public int getSize() {
		return details.size();
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setAa(String aa) {
		this.aa = aa;
	}

	@XmlElement(name = "FP_KJMX")  
	public List<HangXinInvoiceSaleDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<HangXinInvoiceSaleDetailBean> details) {
		this.details = details;
	}
	
	
}
