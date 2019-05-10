package baiwang.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

/*发票作废
 * */
@XmlRootElement(name="fpzf")
public class BaiWangInvoiceInValidBean {
	
	private String sblx;
	private String zflx;
	private String nsrsbh;
	private String kpzdbs;
	private String fplxdm;
	private String fpdm;
	private String fphm;
	private String skpbh;
	private String skpkl;
	private String keypwd;
	private String qmcs;
	private String zfr;
	public String getSblx() {
		return sblx;
	}
	public void setSblx(String sblx) {
		this.sblx = sblx;
	}
	public String getZflx() {
		return zflx;
	}
	public void setZflx(String zflx) {
		this.zflx = zflx;
	}
	public String getNsrsbh() {
		return nsrsbh;
	}
	public void setNsrsbh(String nsrsbh) {
		this.nsrsbh = nsrsbh;
	}
	public String getKpzdbs() {
		return kpzdbs;
	}
	public void setKpzdbs(String kpzdbs) {
		this.kpzdbs = kpzdbs;
	}
	public String getFplxdm() {
		return fplxdm;
	}
	public void setFplxdm(String fplxdm) {
		this.fplxdm = fplxdm;
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
	public String getSkpbh() {
		return skpbh;
	}
	public void setSkpbh(String skpbh) {
		this.skpbh = skpbh;
	}
	public String getSkpkl() {
		return skpkl;
	}
	public void setSkpkl(String skpkl) {
		this.skpkl = skpkl;
	}
	public String getKeypwd() {
		return keypwd;
	}
	public void setKeypwd(String keypwd) {
		this.keypwd = keypwd;
	}
	public String getQmcs() {
		return qmcs;
	}
	public void setQmcs(String qmcs) {
		this.qmcs = qmcs;
	}
	public String getZfr() {
		return zfr;
	}
	public void setZfr(String zfr) {
		this.zfr = zfr;
	}
	
	
}
