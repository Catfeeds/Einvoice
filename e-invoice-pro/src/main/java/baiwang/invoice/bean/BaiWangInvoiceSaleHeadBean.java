package baiwang.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

//推送发票头数据
@XmlRootElement(name="fp")
public class BaiWangInvoiceSaleHeadBean {
	
	protected String fpqqlsh;
	protected String kpddm;
	protected String eqpttype;
	protected String fplxdm;
	protected String tspz;
	protected String ghdwmc;
	protected String ghdwdm;
	protected String ghdwdzdh;
	protected String ghdwyhzh;
	protected String skr;
	protected String kpr;
	protected String fhr;
	protected String kplx;
	protected String qdbz;
	protected String hzxxId;
	protected String yfpdm;
	protected String yfphm;
	protected String zsfs;
	protected String kce;
	protected double hjje;
	protected double se;
	protected double jshj;
	protected String bz;
	protected String qmcs;
	protected String skpbh;
	protected String skpkl;
	protected String keypwd;
	protected String bbh;
	protected String tzdbh;
	protected String zhsl;
	protected String kppy;
	
	
	//不能转化为baiwang的报文，转换之前，设置为null
	//仅仅作为系统与数据库交互字段。
	protected String invoiceid;
	

	public String getInvoiceid() {
		return invoiceid;
	}
	public void setInvoiceid(String invoiceid) {
		this.invoiceid = invoiceid;
	}
	protected List<BaiWangInvoiceSaleDetailBean> detailList;
	public String getKppy() {
		return kppy;
	}
	public void setKppy(String kppy) {
		this.kppy = kppy;
	}
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	public String getKpddm() {
		return kpddm;
	}
	public void setKpddm(String kpddm) {
		this.kpddm = kpddm;
	}
	public String getEqpttype() {
		return eqpttype;
	}
	public void setEqpttype(String eqpttype) {
		this.eqpttype = eqpttype;
	}
	public String getFplxdm() {
		return fplxdm;
	}
	public void setFplxdm(String fplxdm) {
		this.fplxdm = fplxdm;
	}
	public String getTspz() {
		return tspz;
	}
	public void setTspz(String tspz) {
		this.tspz = tspz;
	}
	public String getGhdwmc() {
		return ghdwmc;
	}
	public void setGhdwmc(String ghdwmc) {
		this.ghdwmc = ghdwmc;
	}
	public String getGhdwdm() {
		return ghdwdm;
	}
	public void setGhdwdm(String ghdwdm) {
		this.ghdwdm = ghdwdm;
	}
	public String getGhdwdzdh() {
		return ghdwdzdh;
	}
	public void setGhdwdzdh(String ghdwdzdh) {
		this.ghdwdzdh = ghdwdzdh;
	}
	public String getGhdwyhzh() {
		return ghdwyhzh;
	}
	public void setGhdwyhzh(String ghdwyhzh) {
		this.ghdwyhzh = ghdwyhzh;
	}
	public String getSkr() {
		return skr;
	}
	public void setSkr(String skr) {
		this.skr = skr;
	}
	public String getKpr() {
		return kpr;
	}
	public void setKpr(String kpr) {
		this.kpr = kpr;
	}
	public String getKplx() {
		return kplx;
	}
	public void setKplx(String kplx) {
		this.kplx = kplx;
	}
	public String getFhr() {
		return fhr;
	}
	public void setFhr(String fhr) {
		this.fhr = fhr;
	}
	public String getQdbz() {
		return qdbz;
	}
	public void setQdbz(String qdbz) {
		this.qdbz = qdbz;
	}
	public String getHzxxId() {
		return hzxxId;
	}
	public void setHzxxId(String hzxxId) {
		this.hzxxId = hzxxId;
	}
	public String getYfpdm() {
		return yfpdm;
	}
	public void setYfpdm(String yfpdm) {
		this.yfpdm = yfpdm;
	}
	public String getYfphm() {
		return yfphm;
	}
	public void setYfphm(String yfphm) {
		this.yfphm = yfphm;
	}


	public double getHjje() {
		return hjje;
	}
	public void setHjje(double hjje) {
		this.hjje = hjje;
	}
	public double getSe() {
		return se;
	}
	public void setSe(double se) {
		this.se = se;
	}
	public double getJshj() {
		return jshj;
	}
	public void setJshj(double jshj) {
		this.jshj = jshj;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getQmcs() {
		return qmcs;
	}
	public void setQmcs(String qmcs) {
		this.qmcs = qmcs;
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
	public String getBbh() {
		return bbh;
	}
	public void setBbh(String bbh) {
		this.bbh = bbh;
	}

	public String getZsfs() {
		return zsfs;
	}
	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
	}
	public String getKce() {
		return kce;
	}
	public void setKce(String kce) {
		this.kce = kce;
	}
	public String getTzdbh() {
		return tzdbh;
	}
	public void setTzdbh(String tzdbh) {
		this.tzdbh = tzdbh;
	}
	public String getZhsl() {
		return zhsl;
	}
	public void setZhsl(String zhsl) {
		this.zhsl = zhsl;
	}
	@XmlElementWrapper(name="fpmxList") 
	@XmlElement(name = "fpmx")
	public List<BaiWangInvoiceSaleDetailBean> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<BaiWangInvoiceSaleDetailBean> detailList) {
		this.detailList = detailList;
	}

}
