package hangxin.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPQZ_XMXXS")
public class HangXinQzDetaiListBean {
	
	private List<HangXinQzDetaiBean> detail;
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPQZ_XMXX;";
	}
	
	private int size;
	
	
	@XmlAttribute(name = "size")
	public int getSize() {
		return detail.size();
	}

	@XmlElement(name = "FPQZ_XMXX")  
	public List<HangXinQzDetaiBean> getDetail() {
		return detail;
	}


	public void setDetail(List<HangXinQzDetaiBean> detail) {
		this.detail = detail;
	}
	
	

 
	 
	 
}
