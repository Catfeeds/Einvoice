package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPQZ_XMXX")
public class HangXinQzDetaiBean {
	
	private String xmmc;
	private String dw;
	private String ggxh;
	private String xmsl;
	private String xmdj;
	private String xmje;
	private String sl;
	private String se;
	private String hsbz;
	private String fphxz;
	private String spbm;
	private String zxbm;
	private String yhzcbs;
	private String lslbs;
	private String zzstsgl;
	private String kce;

	
	 @XmlElement(name = "XMMC")  
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	 @XmlElement(name = "DW")  
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	 @XmlElement(name = "GGXH")  
	public String getGgxh() {
		return ggxh;
	}
	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}
	 @XmlElement(name = "XMSL")  
	public String getXmsl() {
		return xmsl;
	}
	public void setXmsl(String xmsl) {
		this.xmsl = xmsl;
	}
	 @XmlElement(name = "XMDJ")  
	public String getXmdj() {
		return xmdj;
	}
	public void setXmdj(String xmdj) {
		this.xmdj = xmdj;
	}
	 @XmlElement(name = "XMJE")  
	public String getXmje() {
		return xmje;
	}
	public void setXmje(String xmje) {
		this.xmje = xmje;
	}
	 @XmlElement(name = "SL")  
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	 @XmlElement(name = "SE")  
	public String getSe() {
		return se;
	}
	public void setSe(String se) {
		this.se = se;
	}
	 @XmlElement(name = "HSBZ")  
	public String getHsbz() {
		return hsbz;
	}
	public void setHsbz(String hsbz) {
		this.hsbz = hsbz;
	}
	 @XmlElement(name = "FPHXZ")  
	public String getFphxz() {
		return fphxz;
	}
	public void setFphxz(String fphxz) {
		this.fphxz = fphxz;
	}
	 @XmlElement(name = "SPBM")  
	public String getSpbm() {
		return spbm;
	}
	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}
	 @XmlElement(name = "ZXBM")  
	public String getZxbm() {
		return zxbm;
	}
	public void setZxbm(String zxbm) {
		this.zxbm = zxbm;
	}
	 @XmlElement(name = "YHZCBS")  
	public String getYhzcbs() {
		return yhzcbs;
	}
	public void setYhzcbs(String yhzcbs) {
		this.yhzcbs = yhzcbs;
	}
	 @XmlElement(name = "LSLBS")  
	public String getLslbs() {
		return lslbs;
	}
	public void setLslbs(String lslbs) {
		this.lslbs = lslbs;
	}
	 @XmlElement(name = "ZZSTSGL")  
	public String getZzstsgl() {
		return zzstsgl;
	}
	public void setZzstsgl(String zzstsgl) {
		this.zzstsgl = zzstsgl;
	}
	 @XmlElement(name = "KCE")  
	public String getKce() {
		return kce;
	}
	public void setKce(String kce) {
		this.kce = kce;
	}
	
}
