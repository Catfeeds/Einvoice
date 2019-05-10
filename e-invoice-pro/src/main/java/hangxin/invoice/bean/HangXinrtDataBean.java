package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
public class HangXinrtDataBean {
	

	private HangXinRtDataDescriptionBean dataDescription;
	
	private String content;
	
	@XmlElement(name = "dataDescription") 
	public HangXinRtDataDescriptionBean getDataDescription() {
		return dataDescription;
	}

	public void setDataDescription(HangXinRtDataDescriptionBean dataDescription) {
		this.dataDescription = dataDescription;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
