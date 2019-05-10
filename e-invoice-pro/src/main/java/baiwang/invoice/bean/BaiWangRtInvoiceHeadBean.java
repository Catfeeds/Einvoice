package baiwang.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*查询发票后返回的发票头数据
 * */
@XmlRootElement(name="fp")
public class BaiWangRtInvoiceHeadBean {
	private String fpqqlsh;//		String(50)
	private String eqptType;//	String(1)
	private String fplxdm;//	String(3)
	private String fpdm;//	String(12)
	private String fphm;//	String(8)
	private String kprq;//	String(14)
	private String skm;//	String(112)
	private String ghdwmc;//	String(100)
	private String ghdwdm;//	String(20)
	private String ghdwdzdh;//	String(100)
	private String ghdwyhzh;//	String(100)
	private String xhdwmc;//	String(100)
	private String xhdwdm;//	String(20)
	private String xhdwdzdh;//	String(140)
	private String xhdwyhzh;//	String(160)
	private double hjje;//	double
	private double se;//	double
	private double jshj;//	double
	private String bz;//	String(200)
	private String skr;//	String(32)
	private String fhr;//	String(32)
	private String kpr;//	String(32)
	private String wspzhm;//	String(100)
	private String tzdh;//	String(20)
	private String kpjh;//	String(20)
	private String qdbz;//	String(1)
	private String zfrq;//	String(14)
	private String yfpdm;//	String(12)
	private String yfphm;//	String(8)
	private String scbz;//	String(1)
	private String bbh;//	String(40)
	private String jym;//	String(150)
	private String kpddm;//	String(30)
	private String by1;//	String(20)
	private String by2;//	String(20)
	private String tspz;//	String(2)
	private String jqbh;//	String(12)
	private String fpzt;//	String(2)
	private String zkbz;//	Boolean(1)
	private String zfyy;//	String(80)
	private String qmbz;//	String(1)
	private String hzxxb;//	String(20)
	private String qmz;//	String(512)
	private String yqbz;//	String(1)
	private String qmcs;//	String(40)
	private long zzBm;//	Long(19)
	private long zhBm;//	Long(19)
	private String zsfs;//	String(1)
	private String kplx;//	String(1)
	private String ewm;//	String(1200)
	private String czbz;//	String(20)	
	private String businessId;//	String(255)
	private double kce;//	double
	private String gfkhyx;//	String(40)
	private String gfkhdh;//	String(40)
	
	 
	private RtInvoiceDetailList fpmxList;
	
	
	@XmlElement(name = "fpmxList")  
	public RtInvoiceDetailList getFpmxList() {
		return fpmxList;
	}
	public void setFpmxList(RtInvoiceDetailList fpmxList) {
		this.fpmxList = fpmxList;
	}
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	public String getEqptType() {
		return eqptType;
	}
	public void setEqptType(String eqptType) {
		this.eqptType = eqptType;
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
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	public String getSkm() {
		return skm;
	}
	public void setSkm(String skm) {
		this.skm = skm;
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
	public String getXhdwmc() {
		return xhdwmc;
	}
	public void setXhdwmc(String xhdwmc) {
		this.xhdwmc = xhdwmc;
	}
	public String getXhdwdm() {
		return xhdwdm;
	}
	public void setXhdwdm(String xhdwdm) {
		this.xhdwdm = xhdwdm;
	}
	public String getXhdwdzdh() {
		return xhdwdzdh;
	}
	public void setXhdwdzdh(String xhdwdzdh) {
		this.xhdwdzdh = xhdwdzdh;
	}
	public String getXhdwyhzh() {
		return xhdwyhzh;
	}
	public void setXhdwyhzh(String xhdwyhzh) {
		this.xhdwyhzh = xhdwyhzh;
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
	public String getSkr() {
		return skr;
	}
	public void setSkr(String skr) {
		this.skr = skr;
	}
	public String getFhr() {
		return fhr;
	}
	public void setFhr(String fhr) {
		this.fhr = fhr;
	}
	public String getKpr() {
		return kpr;
	}
	public void setKpr(String kpr) {
		this.kpr = kpr;
	}
	public String getWspzhm() {
		return wspzhm;
	}
	public void setWspzhm(String wspzhm) {
		this.wspzhm = wspzhm;
	}
	public String getTzdh() {
		return tzdh;
	}
	public void setTzdh(String tzdh) {
		this.tzdh = tzdh;
	}
	public String getKpjh() {
		return kpjh;
	}
	public void setKpjh(String kpjh) {
		this.kpjh = kpjh;
	}
	public String getQdbz() {
		return qdbz;
	}
	public void setQdbz(String qdbz) {
		this.qdbz = qdbz;
	}
	public String getZfrq() {
		return zfrq;
	}
	public void setZfrq(String zfrq) {
		this.zfrq = zfrq;
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
	public String getScbz() {
		return scbz;
	}
	public void setScbz(String scbz) {
		this.scbz = scbz;
	}
	public String getBbh() {
		return bbh;
	}
	public void setBbh(String bbh) {
		this.bbh = bbh;
	}
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	public String getKpddm() {
		return kpddm;
	}
	public void setKpddm(String kpddm) {
		this.kpddm = kpddm;
	}
	public String getBy1() {
		return by1;
	}
	public void setBy1(String by1) {
		this.by1 = by1;
	}
	public String getBy2() {
		return by2;
	}
	public void setBy2(String by2) {
		this.by2 = by2;
	}
	public String getTspz() {
		return tspz;
	}
	public void setTspz(String tspz) {
		this.tspz = tspz;
	}
	public String getJqbh() {
		return jqbh;
	}
	public void setJqbh(String jqbh) {
		this.jqbh = jqbh;
	}
	public String getFpzt() {
		return fpzt;
	}
	public void setFpzt(String fpzt) {
		this.fpzt = fpzt;
	}
	public String getZkbz() {
		return zkbz;
	}
	public void setZkbz(String zkbz) {
		this.zkbz = zkbz;
	}
	public String getZfyy() {
		return zfyy;
	}
	public void setZfyy(String zfyy) {
		this.zfyy = zfyy;
	}
	public String getQmbz() {
		return qmbz;
	}
	public void setQmbz(String qmbz) {
		this.qmbz = qmbz;
	}
	public String getHzxxb() {
		return hzxxb;
	}
	public void setHzxxb(String hzxxb) {
		this.hzxxb = hzxxb;
	}
	public String getQmz() {
		return qmz;
	}
	public void setQmz(String qmz) {
		this.qmz = qmz;
	}
	public String getYqbz() {
		return yqbz;
	}
	public void setYqbz(String yqbz) {
		this.yqbz = yqbz;
	}
	public String getQmcs() {
		return qmcs;
	}
	public void setQmcs(String qmcs) {
		this.qmcs = qmcs;
	}
	public long getZzBm() {
		return zzBm;
	}
	public void setZzBm(long zzBm) {
		this.zzBm = zzBm;
	}
	public long getZhBm() {
		return zhBm;
	}
	public void setZhBm(long zhBm) {
		this.zhBm = zhBm;
	}
	public String getZsfs() {
		return zsfs;
	}
	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
	}
	public String getKplx() {
		return kplx;
	}
	public void setKplx(String kplx) {
		this.kplx = kplx;
	}
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	public String getCzbz() {
		return czbz;
	}
	public void setCzbz(String czbz) {
		this.czbz = czbz;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public double getKce() {
		return kce;
	}
	public void setKce(double kce) {
		this.kce = kce;
	}
	public String getGfkhyx() {
		return gfkhyx;
	}
	public void setGfkhyx(String gfkhyx) {
		this.gfkhyx = gfkhyx;
	}
	public String getGfkhdh() {
		return gfkhdh;
	}
	public void setGfkhdh(String gfkhdh) {
		this.gfkhdh = gfkhdh;
	}
	@Override
	public String toString() {
		return "BaiWangRtInvoiceHeadBean [fpqqlsh=" + fpqqlsh + ", eqptType=" + eqptType + ", fplxdm=" + fplxdm
				+ ", fpdm=" + fpdm + ", fphm=" + fphm + ", kprq=" + kprq + ", skm=" + skm + ", ghdwmc=" + ghdwmc
				+ ", ghdwdm=" + ghdwdm + ", ghdwdzdh=" + ghdwdzdh + ", ghdwyhzh=" + ghdwyhzh + ", xhdwmc=" + xhdwmc
				+ ", xhdwdm=" + xhdwdm + ", xhdwdzdh=" + xhdwdzdh + ", xhdwyhzh=" + xhdwyhzh + ", hjje=" + hjje
				+ ", se=" + se + ", jshj=" + jshj + ", bz=" + bz + ", skr=" + skr + ", fhr=" + fhr + ", kpr=" + kpr
				+ ", wspzhm=" + wspzhm + ", tzdh=" + tzdh + ", kpjh=" + kpjh + ", qdbz=" + qdbz + ", zfrq=" + zfrq
				+ ", yfpdm=" + yfpdm + ", yfphm=" + yfphm + ", scbz=" + scbz + ", bbh=" + bbh + ", jym=" + jym
				+ ", kpddm=" + kpddm + ", by1=" + by1 + ", by2=" + by2 + ", tspz=" + tspz + ", jqbh=" + jqbh + ", fpzt="
				+ fpzt + ", zkbz=" + zkbz + ", zfyy=" + zfyy + ", qmbz=" + qmbz + ", hzxxb=" + hzxxb + ", qmz=" + qmz
				+ ", yqbz=" + yqbz + ", qmcs=" + qmcs + ", zzBm=" + zzBm + ", zhBm=" + zhBm + ", zsfs=" + zsfs
				+ ", kplx=" + kplx + ", ewm=" + ewm + ", czbz=" + czbz + ", businessId=" + businessId + ", kce=" + kce
				+ ", gfkhyx=" + gfkhyx + ", gfkhdh=" + gfkhdh + "]";
	}
	
	

}
