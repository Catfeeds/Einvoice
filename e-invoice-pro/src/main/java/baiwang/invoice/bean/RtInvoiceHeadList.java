package baiwang.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="fplist")

public class RtInvoiceHeadList {
	
	List<BaiWangRtInvoiceHeadBean> heads;
	  @XmlElement(name = "fp")  
	public List<BaiWangRtInvoiceHeadBean> getHeads() {
		return heads;
	}

	public void setHeads(List<BaiWangRtInvoiceHeadBean> heads) {
		this.heads = heads;
	}
	
	
	
}
