package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="interface")
public class HangXinInterfaceBean {
	
	
	private HangXinRtGlobalInfoBean globalInfo;
	
	private HangXinRtStateInfoBean returnStateInfo;
	
	private HangXinrtDataBean data;

	 @XmlElement(name = "globalInfo")  
	public HangXinRtGlobalInfoBean getGlobalInfo() {
		return globalInfo;
	}

	public void setGlobalInfo(HangXinRtGlobalInfoBean globalInfo) {
		this.globalInfo = globalInfo;
	}
	 @XmlElement(name = "returnStateInfo")  
	public HangXinRtStateInfoBean getReturnStateInfo() {
		return returnStateInfo;
	}

	public void setReturnStateInfo(HangXinRtStateInfoBean returnStateInfo) {
		this.returnStateInfo = returnStateInfo;
	}
	 @XmlElement(name = "Data")  
	public HangXinrtDataBean getData() {
		return data;
	}

	public void setData(HangXinrtDataBean data) {
		this.data = data;
	}
	
	
}
