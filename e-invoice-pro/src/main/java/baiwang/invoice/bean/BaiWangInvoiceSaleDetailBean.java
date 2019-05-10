package baiwang.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

//推送发票明细数据
@XmlRootElement(name="fpmx")
public class BaiWangInvoiceSaleDetailBean {
	protected String fpmxxh;
	protected String fphxz;
	protected String spbm;
	protected String spmc;
	protected String spsm;
	protected String ggxh;
	protected String dw;
	protected String spsl;
	protected String spdj;
	protected String je;
	protected String se;
	protected String sl;
	protected String zhdyhh;
	protected String hsbz;
	protected String zzstsgl;
	protected String yhzcbs;
	protected String lslbs;
	
	
	//不能转化为baiwang的报文，转换之前，设置为null
		//仅仅作为系统与数据库交互字段。
	protected String invoiceid;
	

	public String getInvoiceid() {
		return invoiceid;
	}
	public void setInvoiceid(String invoiceid) {
		this.invoiceid = invoiceid;
	}
	public String getFpmxxh() {
		return fpmxxh;
	}
	public void setFpmxxh(String fpmxxh) {
		this.fpmxxh = fpmxxh;
	}
	public String getFphxz() {
		return fphxz;
	}
	public void setFphxz(String fphxz) {
		this.fphxz = fphxz;
	}
	public String getSpbm() {
		return spbm;
	}
	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}
	public String getSpmc() {
		return spmc;
	}
	public void setSpmc(String spmc) {
		this.spmc = spmc;
	}
	public String getSpsm() {
		return spsm;
	}
	public void setSpsm(String spsm) {
		this.spsm = spsm;
	}
	public String getGgxh() {
		return ggxh;
	}
	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getSpsl() {
		return spsl;
	}
	public void setSpsl(String spsl) {
		this.spsl = spsl;
	}
	public String getSpdj() {
		return spdj;
	}
	public void setSpdj(String spdj) {
		this.spdj = spdj;
	}
	public String getJe() {
		return je;
	}
	public void setJe(String je) {
		this.je = je;
	}
	public String getSe() {
		return se;
	}
	public void setSe(String se) {
		this.se = se;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getZhdyhh() {
		return zhdyhh;
	}
	public void setZhdyhh(String zhdyhh) {
		this.zhdyhh = zhdyhh;
	}
	public String getHsbz() {
		return hsbz;
	}
	public void setHsbz(String hsbz) {
		this.hsbz = hsbz;
	}
	public String getZzstsgl() {
		return zzstsgl;
	}
	public void setZzstsgl(String zzstsgl) {
		this.zzstsgl = zzstsgl;
	}
	public String getYhzcbs() {
		return yhzcbs;
	}
	public void setYhzcbs(String yhzcbs) {
		this.yhzcbs = yhzcbs;
	}
	public String getLslbs() {
		return lslbs;
	}
	public void setLslbs(String lslbs) {
		this.lslbs = lslbs;
	}
	
	

}
