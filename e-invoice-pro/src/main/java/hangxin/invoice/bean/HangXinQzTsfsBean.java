package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="FPQZ_TSFSXX")
public class HangXinQzTsfsBean {
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPQZ_TSFSXX";
	}
	
	private HangXinQzCommonnodesBean ss;
	
	
	@XmlElement(name = "COMMON_NODES") 
	public HangXinQzCommonnodesBean getSs() {
		return ss;
	}

	public void setSs(HangXinQzCommonnodesBean ss) {
		this.ss = ss;
	}
	
	
	
}
