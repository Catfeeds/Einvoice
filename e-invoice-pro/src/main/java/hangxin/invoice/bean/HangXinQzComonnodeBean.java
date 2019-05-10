package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="COMMON_NODE")
public class HangXinQzComonnodeBean {
	
	private String tsfs;
	private String sj;
	private String email;
	public String getTsfs() {
		return  "<NAME>TSFS</NAME>"+
                "<VALUE>"+tsfs+"</VALUE>";
	}
	public void setTsfs(String tsfs) {
		this.tsfs = tsfs;
	}

	public String getSj() {
		return  "<NAME>SJ</NAME>"+
                "<VALUE>"+sj+"</VALUE>";
	}
	public void setSj(String sj) {
		this.sj = sj;
	}

	public String getEmail() {
		return "<NAME>EMAIL</NAME>"+
                "<VALUE>"+email+"</VALUE>";
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
