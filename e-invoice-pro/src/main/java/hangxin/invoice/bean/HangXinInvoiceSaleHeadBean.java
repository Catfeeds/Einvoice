package hangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="REQUEST_FPKJ")
public class HangXinInvoiceSaleHeadBean {
	/* 购货方名称*/
	private String ghfmc;
	/*购货方税号*/
	private String ghf_nsrsbh;
	/*付款方银行及账号*/
	private String fkfkhyh_fkfyhzh;
	/*付款方地址电话*/
	private String fkfdz_fkfdh;
	/*销货方银行及账号*/
	private String xhfkhyh_skfyhzh;
	/*销货方地址电话*/
	private String xhfdz_xhfdh;
	/*发票种类代码*/
	private String fpzl_dm;
	/*原发票代码*/
	private String yfp_dm;
	/*原发票号码*/
	private String yfp_hm;
	/*备注*/
	private String bz;
	/*开票人*/
	private String kpy;
	/*复核人*/
	private String fhr;
	/*收款人*/
	private String sky;
	/*清单标识*/
	private String xhqd;
	/*请求流水号*/
	private String fpqqlsh;
	/*开票类型*/
	private int kplx;
	/*价税合计金额*/
	private double jshj;
	/*合计金额*/
	private double hjje;
	/*合计税额*/
	private double hjse;
	/*编码表版本号*/
	private String bmb_bbh;
	
	private HangXinInvoiceSaleDetailList fp_kjmxs;
	
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "REQUEST_FPKJ";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	@XmlElement(name = "FP_KJMXS")  
	public HangXinInvoiceSaleDetailList getFp_kjmxs() {
		return fp_kjmxs;
	}
	public void setFp_kjmxs(HangXinInvoiceSaleDetailList fp_kjmxs) {
		this.fp_kjmxs = fp_kjmxs;
	}
	@XmlElement(name = "GHFMC")  
	public String getGhfmc() {
		return ghfmc;
	}
	public void setGhfmc(String ghfmc) {
		this.ghfmc = ghfmc;
	}
	@XmlElement(name = "GHF_NSRSBH")  
	public String getGhf_nsrsbh() {
		return ghf_nsrsbh;
	}
	public void setGhf_nsrsbh(String ghf_nsrsbh) {
		this.ghf_nsrsbh = ghf_nsrsbh;
	}
	@XmlElement(name = "FKFKHYH_FKFYHZH")  
	public String getFkfkhyh_fkfyhzh() {
		return fkfkhyh_fkfyhzh;
	}
	public void setFkfkhyh_fkfyhzh(String fkfkhyh_fkfyhzh) {
		this.fkfkhyh_fkfyhzh = fkfkhyh_fkfyhzh;
	}
	@XmlElement(name = "FKFDZ_FKFDH")  
	public String getFkfdz_fkfdh() {
		return fkfdz_fkfdh;
	}
	public void setFkfdz_fkfdh(String fkfdz_fkfdh) {
		this.fkfdz_fkfdh = fkfdz_fkfdh;
	}
	@XmlElement(name = "XHFKHYH_SKFYHZH")  
	public String getXhfkhyh_skfyhzh() {
		return xhfkhyh_skfyhzh;
	}
	public void setXhfkhyh_skfyhzh(String xhfkhyh_skfyhzh) {
		this.xhfkhyh_skfyhzh = xhfkhyh_skfyhzh;
	}
	@XmlElement(name = "XHFDZ_XHFDH")  
	public String getXhfdz_xhfdh() {
		return xhfdz_xhfdh;
	}
	public void setXhfdz_xhfdh(String xhfdz_xhfdh) {
		this.xhfdz_xhfdh = xhfdz_xhfdh;
	}
	@XmlElement(name = "FPZL_DM")  
	public String getFpzl_dm() {
		return fpzl_dm;
	}
	public void setFpzl_dm(String fpzl_dm) {
		this.fpzl_dm = fpzl_dm;
	}
	@XmlElement(name = "YFP_DM")  
	public String getYfp_dm() {
		return yfp_dm;
	}
	public void setYfp_dm(String yfp_dm) {
		this.yfp_dm = yfp_dm;
	}
	@XmlElement(name = "YFP_HM")  
	public String getYfp_hm() {
		return yfp_hm;
	}
	public void setYfp_hm(String yfp_hm) {
		this.yfp_hm = yfp_hm;
	}
	@XmlElement(name = "BZ")  
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	@XmlElement(name = "KPY")  
	public String getKpy() {
		return kpy;
	}
	public void setKpy(String kpy) {
		this.kpy = kpy;
	}
	@XmlElement(name = "FHR")  
	public String getFhr() {
		return fhr;
	}
	public void setFhr(String fhr) {
		this.fhr = fhr;
	}
	@XmlElement(name = "SKY")  
	public String getSky() {
		return sky;
	}
	public void setSky(String sky) {
		this.sky = sky;
	}
	@XmlElement(name = "XHQD")  
	public String getXhqd() {
		return xhqd;
	}
	public void setXhqd(String xhqd) {
		this.xhqd = xhqd;
	}
	@XmlElement(name = "FPQQLSH")  
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	@XmlElement(name = "KPLX")  
	public int getKplx() {
		return kplx;
	}
	public void setKplx(int kplx) {
		this.kplx = kplx;
	}
	@XmlElement(name = "JSHJ")  
	public double getJshj() {
		return jshj;
	}
	public void setJshj(double jshj) {
		this.jshj = jshj;
	}
	@XmlElement(name = "HJJE")  
	public double getHjje() {
		return hjje;
	}
	public void setHjje(double hjje) {
		this.hjje = hjje;
	}
	@XmlElement(name = "HJSE")  
	public double getHjse() {
		return hjse;
	}
	public void setHjse(double hjse) {
		this.hjse = hjse;
	}
	@XmlElement(name = "BMB_BBH")  
	public String getBmb_bbh() {
		return bmb_bbh;
	}
	public void setBmb_bbh(String bmb_bbh) {
		this.bmb_bbh = bmb_bbh;
	}
	
	@Override
	public String toString() {
		return "HangXinInvoiceSaleHeadBean [ghfmc=" + ghfmc + ", ghf_nsrsbh=" + ghf_nsrsbh + ", fkfkhyh_fkfyhzh="
				+ fkfkhyh_fkfyhzh + ", fkfdz_fkfdh=" + fkfdz_fkfdh + ", xhfkhyh_skfyhzh=" + xhfkhyh_skfyhzh
				+ ", xhfdz_xhfdh=" + xhfdz_xhfdh + ", fpzl_dm=" + fpzl_dm + ", yfp_dm=" + yfp_dm + ", yfp_hm=" + yfp_hm
				+ ", bz=" + bz + ", kpy=" + kpy + ", fhr=" + fhr + ", sky=" + sky + ", xhqd=" + xhqd + ", fpqqlsh="
				+ fpqqlsh + ", kplx=" + kplx + ", jshj=" + jshj + ", hjje=" + hjje + ", hjse=" + hjse + ", bmb_bbh="
				+ bmb_bbh + "]";
	}

	
	
}
