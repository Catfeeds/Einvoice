package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FP_KJMX" )
public class HangXinInvoiceSaleDetailBean {

	/*商品名称*/
	private String spmc;
	/*税目*/
	private String sm;
	/*税率*/
	private String sl;
	/*规格型号*/
	private String ggxh;
	/*计量单位*/
	private String jldw;
	/*商品数量*/
	private String spsl;
	/*商品单价*/
	private String spdj;
	/*商品金额*/
	private String spje;
	/*发票行性质*/
	private String fphxz;
	/*含税价标识*/
	private String hsjbz;
	/*税额*/
	private String se;
	/*商品编码*/
	private String spbm;
	/*自行编码*/
	private String zxbm;
	/*优惠政策标识*/
	private String yhzcbs;
	/*零税率标识*/
	private String lslbs;
	/*增值税特殊管理*/
	private String zzstsgl;
	/*扣除额*/
	private String kce;
	
	@XmlElement(name = "SPMC")  
	public String getSpmc() {
		return spmc;
	}
	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}
	@XmlElement(name = "SM")  
	public String getSm() {
		return sm;
	}
	public void setSm(String sm) {
		this.sm = sm;
	}
	@XmlElement(name = "SL")  
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	@XmlElement(name = "GGXH")  
	public String getGgxh() {
		return ggxh;
	}
	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}
	@XmlElement(name = "JLDW")  
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	@XmlElement(name = "FPHXZ")  
	public String getFphxz() {
		return fphxz;
	}
	public void setFphxz(String fphxz) {
		this.fphxz = fphxz;
	}
	@XmlElement(name = "HSJBZ")  
	public String getHsjbz() {
		return hsjbz;
	}
	public void setHsjbz(String hsjbz) {
		this.hsjbz = hsjbz;
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
	@XmlElement(name = "SPSL")  
	public String getSpsl() {
		return spsl;
	}
	public void setSpsl(String spsl) {
		this.spsl = spsl;
	}
	@XmlElement(name = "SPDJ")  
	public String getSpdj() {
		return spdj;
	}
	public void setSpdj(String spdj) {
		this.spdj = spdj;
	}
	@XmlElement(name = "SPJE")  
	public String getSpje() {
		return spje;
	}
	public void setSpje(String spje) {
		this.spje = spje;
	}
	@XmlElement(name = "SE")  
	public String getSe() {
		return se;
	}
	public void setSe(String se) {
		this.se = se;
	}
	@XmlElement(name = "KCE")  
	public String getKce() {
		return kce;
	}
	public void setKce(String kce) {
		this.kce = kce;
	}

	
	
	
}
