package com.invoice.port.bwjf.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="COMMON_FPKJ_FPT")
public class BwjfDzInvoiceHeadBean {
	private String fpqqlsh;//         发票请求流水号      
	private String kplx;//            开票类型            
	private String bmb_bbh;//         编码表版本号        
	private String zsfs;//            征税方式            
	private String tspz;//            特殊票种标识        
	private String xsf_nsrsbh;//      销售方纳税人识别号  
	private String xsf_mc;//          销售方名称          
	private String xsf_dzdh;//        销售方地址、电话    
	private String xsf_yhzh;//        销售方银行账号      
	private String xsf_lxfs;//        销售方移动电话或邮箱
	private String gmf_nsrsbh;//      购买方纳税人识别号  
	private String gmf_mc;//          购买方名称          
	private String gmf_dzdh;//        购买方地址、电话    
	private String gmf_yhzh;//        购买方银行账号      
	private String gmf_lxfs;//        购买方移动电话或邮箱
	private String fp_dm;//           发票代码            
	private String fp_hm;//           发票号码            
	private String kprq;//            开具日期            
	private String fp_mw;//           密文                
	private String jym;//             校验码              
	private String kpr;//             开票人              
	private String skr;//             收款人              
	private String fhr;//             复核人              
	private String yfp_dm;//          原发票代码          
	private String yfp_hm;//          原发票号码          
	private String jshj;//            价税合计            
	private String hjje;//            合计金额            
	private String hjse;//            合计税额            
	private String kce;//             扣除额              
	private String bz;//              备注    
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "COMMON_FPKJ_FPT";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	public String getKplx() {
		return kplx;
	}
	public void setKplx(String kplx) {
		this.kplx = kplx;
	}
	public String getBmb_bbh() {
		return bmb_bbh;
	}
	public void setBmb_bbh(String bmb_bbh) {
		this.bmb_bbh = bmb_bbh;
	}
	public String getZsfs() {
		return zsfs;
	}
	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
	}
	public String getTspz() {
		return tspz;
	}
	public void setTspz(String tspz) {
		this.tspz = tspz;
	}
	public String getXsf_nsrsbh() {
		return xsf_nsrsbh;
	}
	public void setXsf_nsrsbh(String xsf_nsrsbh) {
		this.xsf_nsrsbh = xsf_nsrsbh;
	}
	public String getXsf_mc() {
		return xsf_mc;
	}
	public void setXsf_mc(String xsf_mc) {
		this.xsf_mc = xsf_mc;
	}
	public String getXsf_dzdh() {
		return xsf_dzdh;
	}
	public void setXsf_dzdh(String xsf_dzdh) {
		this.xsf_dzdh = xsf_dzdh;
	}
	public String getXsf_yhzh() {
		return xsf_yhzh;
	}
	public void setXsf_yhzh(String xsf_yhzh) {
		this.xsf_yhzh = xsf_yhzh;
	}
	public String getXsf_lxfs() {
		return xsf_lxfs;
	}
	public void setXsf_lxfs(String xsf_lxfs) {
		this.xsf_lxfs = xsf_lxfs;
	}
	public String getGmf_nsrsbh() {
		return gmf_nsrsbh;
	}
	public void setGmf_nsrsbh(String gmf_nsrsbh) {
		this.gmf_nsrsbh = gmf_nsrsbh;
	}
	public String getGmf_mc() {
		return gmf_mc;
	}
	public void setGmf_mc(String gmf_mc) {
		this.gmf_mc = gmf_mc;
	}
	public String getGmf_dzdh() {
		return gmf_dzdh;
	}
	public void setGmf_dzdh(String gmf_dzdh) {
		this.gmf_dzdh = gmf_dzdh;
	}
	public String getGmf_yhzh() {
		return gmf_yhzh;
	}
	public void setGmf_yhzh(String gmf_yhzh) {
		this.gmf_yhzh = gmf_yhzh;
	}
	public String getGmf_lxfs() {
		return gmf_lxfs;
	}
	public void setGmf_lxfs(String gmf_lxfs) {
		this.gmf_lxfs = gmf_lxfs;
	}
	public String getFp_dm() {
		return fp_dm;
	}
	public void setFp_dm(String fp_dm) {
		this.fp_dm = fp_dm;
	}
	public String getFp_hm() {
		return fp_hm;
	}
	public void setFp_hm(String fp_hm) {
		this.fp_hm = fp_hm;
	}
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	public String getFp_mw() {
		return fp_mw;
	}
	public void setFp_mw(String fp_mw) {
		this.fp_mw = fp_mw;
	}
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	public String getKpr() {
		return kpr;
	}
	public void setKpr(String kpr) {
		this.kpr = kpr;
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
	public String getYfp_dm() {
		return yfp_dm;
	}
	public void setYfp_dm(String yfp_dm) {
		this.yfp_dm = yfp_dm;
	}
	public String getYfp_hm() {
		return yfp_hm;
	}
	public void setYfp_hm(String yfp_hm) {
		this.yfp_hm = yfp_hm;
	}
	public String getJshj() {
		return jshj;
	}
	public void setJshj(String jshj) {
		this.jshj = jshj;
	}
	public String getHjje() {
		return hjje;
	}
	public void setHjje(String hjje) {
		this.hjje = hjje;
	}
	public String getHjse() {
		return hjse;
	}
	public void setHjse(String hjse) {
		this.hjse = hjse;
	}
	public String getKce() {
		return kce;
	}
	public void setKce(String kce) {
		this.kce = kce;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
}
