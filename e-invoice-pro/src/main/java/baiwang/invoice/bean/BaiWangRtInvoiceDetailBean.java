package baiwang.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

/*查询发票后返回的发票明细数据
 * */
@XmlRootElement(name="fpmx")
public class BaiWangRtInvoiceDetailBean {
	private String mxid;//	String(40)
	private String fpdm;//	String(12)
	private String fphm;//	String(8)
	private String fpmxxh;//	String(4)
	private String fphxz;//	String(1)
	private double je;//	double
	private double sl;//	double
	private double se;//	double
	private String spmc;//	String(200)
	private String spsm;//	String(20)
	private String ggxh;//	String(50)
	private String dw;//	String(32)
	private double spsl;//	double
	private double spdj;//	double
	private String hsbz;//	String(1)
	private String spbm;//	String(19)
	private String bb;//	String(10)
	private String yhzcbs;//	String(1)
	private String lslbs;//	String(1)
	private String zzstsgl;//	String(200)
	private double jshj;//	double
	private String zhdyhh;//	String(10)
	
	public String getMxid() {
		return mxid;
	}
	public void setMxid(String mxid) {
		this.mxid = mxid;
	}
	public String getFpdm() {
		return fpdm;
	}
	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
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
	public double getJe() {
		return je;
	}
	public void setJe(double je) {
		this.je = je;
	}
	public double getSl() {
		return sl;
	}
	public void setSl(double sl) {
		this.sl = sl;
	}
	public double getSe() {
		return se;
	}
	public void setSe(double se) {
		this.se = se;
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
	public double getSpsl() {
		return spsl;
	}
	public void setSpsl(double spsl) {
		this.spsl = spsl;
	}
	public double getSpdj() {
		return spdj;
	}
	public void setSpdj(double spdj) {
		this.spdj = spdj;
	}
	public String getHsbz() {
		return hsbz;
	}
	public void setHsbz(String hsbz) {
		this.hsbz = hsbz;
	}
	public String getSpbm() {
		return spbm;
	}
	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}
	public String getBb() {
		return bb;
	}
	public void setBb(String bb) {
		this.bb = bb;
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
	public String getZzstsgl() {
		return zzstsgl;
	}
	public void setZzstsgl(String zzstsgl) {
		this.zzstsgl = zzstsgl;
	}
	public double getJshj() {
		return jshj;
	}
	public void setJshj(double jshj) {
		this.jshj = jshj;
	}
	public String getZhdyhh() {
		return zhdyhh;
	}
	public void setZhdyhh(String zhdyhh) {
		this.zhdyhh = zhdyhh;
	}
	@Override
	public String toString() {
		return "BaiWangRtInvoiceDetailBean [mxid=" + mxid + ", fpdm=" + fpdm + ", fphm=" + fphm + ", fpmxxh=" + fpmxxh
				+ ", fphxz=" + fphxz + ", je=" + je + ", sl=" + sl + ", se=" + se + ", spmc=" + spmc + ", spsm=" + spsm
				+ ", ggxh=" + ggxh + ", dw=" + dw + ", spsl=" + spsl + ", spdj=" + spdj + ", hsbz=" + hsbz + ", spbm="
				+ spbm + ", bb=" + bb + ", yhzcbs=" + yhzcbs + ", lslbs=" + lslbs + ", zzstsgl=" + zzstsgl + ", jshj="
				+ jshj + ", zhdyhh=" + zhdyhh + "]";
	}
	
	

}
