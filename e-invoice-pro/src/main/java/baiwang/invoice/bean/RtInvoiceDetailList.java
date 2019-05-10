package baiwang.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="fpmxList")

public class RtInvoiceDetailList {
	
	List<BaiWangRtInvoiceDetailBean> details;
	@XmlElement(name = "fpmx")  
	public List<BaiWangRtInvoiceDetailBean> getDetails() {
		return details;
	}

	public void setDetails(List<BaiWangRtInvoiceDetailBean> details) {
		this.details = details;
	}
	
	
}
