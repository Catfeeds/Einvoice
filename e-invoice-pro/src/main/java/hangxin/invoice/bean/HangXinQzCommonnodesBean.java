package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="COMMON_NODES")
public class HangXinQzCommonnodesBean {
	
	private String aa;
	
	private HangXinQzTsfsValueBean tsfs;
	private HangXinQzTsfsValueBean sj;
	private HangXinQzTsfsValueBean email;
	
	@XmlElement(name = "COMMON_NODE") 
	public HangXinQzTsfsValueBean getTsfs() {
		return tsfs;
	}


	public void setTsfs(HangXinQzTsfsValueBean tsfs) {
		this.tsfs = tsfs;
	}

	@XmlElement(name = "COMMON_NODE") 
	public HangXinQzTsfsValueBean getSj() {
		return sj;
	}


	public void setSj(HangXinQzTsfsValueBean sj) {
		this.sj = sj;
	}

	@XmlElement(name = "COMMON_NODE") 
	public HangXinQzTsfsValueBean getEmail() {
		return email;
	}


	public void setEmail(HangXinQzTsfsValueBean email) {
		this.email = email;
	}


	@XmlAttribute(name = "class")
	public String getAa() {
		return "COMMON_NODE;";
	}
	
	private int size;
	
	
	@XmlAttribute(name = "size")
	public int getSize() {
		return 1;
	}



	
	
	
}
