package com.invoice.port.hnhangxin.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="FPKJXX_FPTXX")
public class HnHangXinInvoiceSaleHeadBean {
	
	private String fpqqlsh;// <FPQQLSH>发票请求唯一流水号</FPQQLSH> 

	private String dsptbm;//<DSPTBM>平台编码</DSPTBM> 

	private String nsrsbh;// <NSRSBH>开票方识别号</NSRSBH> 

	private String nsrmc;//<NSRMC>开票方名称</NSRMC> 

	private String nsrdzdah;//<NSRDZDAH>开票方电子档案号</NSRDZDAH> 

	private String swjg_dm;//<SWJG_DM>税务机构代码</SWJG_DM> 

	private String dkbz;//<DKBZ>代开标志</DKBZ> 

	private String sgbz;//<SGBZ>收购标志</SGBZ> 

	private String pydm;//<PYDM>票样代码</PYDM> 

	private String kpxm;//<KPXM>主要开票项目</KPXM> 

	private String bmb_bbh;// <BMB_BBH>编码表版本号</BMB_BBH> 

	private String xhf_nsrsbh;// <XHF_NSRSBH>销货方识别号</XHF_NSRSBH>
	private String xhfmc;//<XHFMC>销货方名称</XHFMC> 

	private String xhf_dz;//<XHF_DZ>销货方地址</XHF_DZ> 

	private String xhf_dh;//<XHF_DH>销货方电话</XHF_DH> 

	private String xhf_yhzh;//<XHF_YHZH>销货方银行账号</XHF_YHZH> 

	private String ghfmc;//<GHFMC>购货方名称</GHFMC> 

	private String ghf_nsrsbh;//<GHF_NSRSBH>购货方识别号</GHF_NSRSBH> 

	private String ghf_sf;//<GHF_SF>购货方省份</GHF_SF> 

	private String ghf_dz;//<GHF_DZ>购货方地址</GHF_DZ> 

	private String ghf_gddh;// <GHF_GDDH>购货方固定电话</GHF_GDDH> 

	private String ghf_sj;//<GHF_SJ>购货方手机</GHF_SJ> 

	private String ghf_email;//<GHF_EMAIL>购货方邮箱</GHF_EMAIL> 

	private String ghfqylx;//<GHFQYLX>购货方企业类型</GHFQYLX> 

	private String ghf_yhzh;//<GHF_YHZH>购货方银行账号</GHF_YHZH> 

	private String hy_dm;//<HY_DM>行业代码</HY_DM> 

	private String hy_mc;//<HY_MC>行业名称</HY_MC> 

	private String kpy ;//<KPY>开票员</KPY> 

	private String sky;//<SKY>收款员</SKY> 

	private String fhr;//<FHR>复核人</FHR> 

	private String kprq;//<KPRQ>开票日期</KPRQ> 

	private String kplx;//<KPLX>开票类型</KPLX> 

	private String yfp_dm;// <YFP_DM>原发票代码</YFP_DM> 

	private String yfp_hm;//<YFP_HM>原发票号码</YFP_HM> 

	private String czdm;//<CZDM>操作代码</CZDM> 

	private String qd_bz;//<QD_BZ>清单标志</QD_BZ> 

	private String qdxmmc;//<QDXMMC>清单发票项目名称</QDXMMC> 

	private String chyy;//<CHYY>冲红原因</CHYY> 

	private String tschbz;//<TSCHBZ>特殊冲红标志</TSCHBZ> 

	private String kphjje;//<KPHJJE>价税合计金额</KPHJJE> 

	private String hjbhsje;// <HJBHSJE>合计不含税金额</HJBHSJE> 

	private String hjse;//<HJSE>合计税额</HJSE> 

	private String bz;//<BZ>备注</BZ> 

	private String byzd1;//<BYZD1>备用字段</BYZD1> 

	private String byzd2;//<BYZD2>备用字段</BYZD2> 

	private String byzd3;// <BYZD3>备用字段</BYZD3> 

	private String byzd4;//<BYZD4>备用字段</BYZD4> 

	private String byzd5;//<BYZD5>备用字段</BYZD5> 
	
	private String aa;
	
	
	@XmlAttribute(name = "class")
	public String getAa() {
		return "FPKJXX_FPTXX";
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	@XmlElement(name = "FPQQLSH")
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	@XmlElement(name = "DSPTBM")  
	public String getDsptbm() {
		return dsptbm;
	}
	public void setDsptbm(String dsptbm) {
		this.dsptbm = dsptbm;
	}
	@XmlElement(name = "NSRSBH")  
	public String getNsrsbh() {
		return nsrsbh;
	}
	public void setNsrsbh(String nsrsbh) {
		this.nsrsbh = nsrsbh;
	}
	@XmlElement(name = "NSRMC")  
	public String getNsrmc() {
		return nsrmc;
	}
	public void setNsrmc(String nsrmc) {
		this.nsrmc = nsrmc;
	}
	@XmlElement(name = "NSRDZDAH")  
	public String getNsrdzdah() {
		return nsrdzdah;
	}
	public void setNsrdzdah(String nsrdzdah) {
		this.nsrdzdah = nsrdzdah;
	}
	@XmlElement(name = "SWJG_DM")  
	public String getSwjg_dm() {
		return swjg_dm;
	}
	public void setSwjg_dm(String swjg_dm) {
		this.swjg_dm = swjg_dm;
	}
	@XmlElement(name = "DKBZ")  
	public String getDkbz() {
		return dkbz;
	}
	public void setDkbz(String dkbz) {
		this.dkbz = dkbz;
	}
	@XmlElement(name = "SGBZ")  
	public String getSgbz() {
		return sgbz;
	}
	public void setSgbz(String sgbz) {
		this.sgbz = sgbz;
	}
	@XmlElement(name = "PYDM")  
	public String getPydm() {
		return pydm;
	}
	public void setPydm(String pydm) {
		this.pydm = pydm;
	}
	@XmlElement(name = "KPXM")  
	public String getKpxm() {
		return kpxm;
	}
	public void setKpxm(String kpxm) {
		this.kpxm = kpxm;
	}
	@XmlElement(name = "BMB_BBH")  
	public String getBmb_bbh() {
		return bmb_bbh;
	}
	public void setBmb_bbh(String bmb_bbh) {
		this.bmb_bbh = bmb_bbh;
	}
	@XmlElement(name = "XHF_NSRSBH")  
	public String getXhf_nsrsbh() {
		return xhf_nsrsbh;
	}
	public void setXhf_nsrsbh(String xhf_nsrsbh) {
		this.xhf_nsrsbh = xhf_nsrsbh;
	}
	@XmlElement(name = "XHFMC")  
	public String getXhfmc() {
		return xhfmc;
	}
	public void setXhfmc(String xhfmc) {
		this.xhfmc = xhfmc;
	}
	@XmlElement(name = "XHF_DZ")  
	public String getXhf_dz() {
		return xhf_dz;
	}
	public void setXhf_dz(String xhf_dz) {
		this.xhf_dz = xhf_dz;
	}
	@XmlElement(name = "XHF_DH")  
	public String getXhf_dh() {
		return xhf_dh;
	}
	public void setXhf_dh(String xhf_dh) {
		this.xhf_dh = xhf_dh;
	}
	@XmlElement(name = "XHF_YHZH")  
	public String getXhf_yhzh() {
		return xhf_yhzh;
	}
	public void setXhf_yhzh(String xhf_yhzh) {
		this.xhf_yhzh = xhf_yhzh;
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
	@XmlElement(name = "GHF_SF")  
	public String getGhf_sf() {
		return ghf_sf;
	}
	public void setGhf_sf(String ghf_sf) {
		this.ghf_sf = ghf_sf;
	}
	@XmlElement(name = "GHF_DZ")  
	public String getGhf_dz() {
		return ghf_dz;
	}
	public void setGhf_dz(String ghf_dz) {
		this.ghf_dz = ghf_dz;
	}
	@XmlElement(name = "GHF_GDDH")  
	public String getGhf_gddh() {
		return ghf_gddh;
	}
	public void setGhf_gddh(String ghf_gddh) {
		this.ghf_gddh = ghf_gddh;
	}
	@XmlElement(name = "GHF_SJ")  
	public String getGhf_sj() {
		return ghf_sj;
	}
	public void setGhf_sj(String ghf_sj) {
		this.ghf_sj = ghf_sj;
	}
	@XmlElement(name = "GHF_EMAIL")  
	public String getGhf_email() {
		return ghf_email;
	}
	public void setGhf_email(String ghf_email) {
		this.ghf_email = ghf_email;
	}
	@XmlElement(name = "GHFQYLX")  
	public String getGhfqylx() {
		return ghfqylx;
	}
	public void setGhfqylx(String ghfqylx) {
		this.ghfqylx = ghfqylx;
	}
	@XmlElement(name = "GHF_YHZH")  
	public String getGhf_yhzh() {
		return ghf_yhzh;
	}
	public void setGhf_yhzh(String ghf_yhzh) {
		this.ghf_yhzh = ghf_yhzh;
	}
	@XmlElement(name = "HY_DM")  
	public String getHy_dm() {
		return hy_dm;
	}
	public void setHy_dm(String hy_dm) {
		this.hy_dm = hy_dm;
	}
	@XmlElement(name = "HY_MC")  
	public String getHy_mc() {
		return hy_mc;
	}
	public void setHy_mc(String hy_mc) {
		this.hy_mc = hy_mc;
	}
	@XmlElement(name = "KPY")  
	public String getKpy() {
		return kpy;
	}
	public void setKpy(String kpy) {
		this.kpy = kpy;
	}
	@XmlElement(name = "SKY")  
	public String getSky() {
		return sky;
	}
	public void setSky(String sky) {
		this.sky = sky;
	}
	@XmlElement(name = "FHR")  
	public String getFhr() {
		return fhr;
	}
	public void setFhr(String fhr) {
		this.fhr = fhr;
	}
	@XmlElement(name = "KPRQ")  
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	@XmlElement(name = "KPLX")  
	public String getKplx() {
		return kplx;
	}
	public void setKplx(String kplx) {
		this.kplx = kplx;
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
	@XmlElement(name = "CZDM")  
	public String getCzdm() {
		return czdm;
	}
	public void setCzdm(String czdm) {
		this.czdm = czdm;
	}
	@XmlElement(name = "QD_BZ")  
	public String getQd_bz() {
		return qd_bz;
	}
	public void setQd_bz(String qd_bz) {
		this.qd_bz = qd_bz;
	}
	@XmlElement(name = "QDXMMC")  
	public String getQdxmmc() {
		return qdxmmc;
	}
	public void setQdxmmc(String qdxmmc) {
		this.qdxmmc = qdxmmc;
	}
	@XmlElement(name = "CHYY")  
	public String getChyy() {
		return chyy;
	}
	public void setChyy(String chyy) {
		this.chyy = chyy;
	}
	@XmlElement(name = "TSCHBZ")  
	public String getTschbz() {
		return tschbz;
	}
	public void setTschbz(String tschbz) {
		this.tschbz = tschbz;
	}
	@XmlElement(name = "KPHJJE")  
	public String getKphjje() {
		return kphjje;
	}
	public void setKphjje(String kphjje) {
		this.kphjje = kphjje;
	}
	@XmlElement(name = "HJBHSJE")  
	public String getHjbhsje() {
		return hjbhsje;
	}
	public void setHjbhsje(String hjbhsje) {
		this.hjbhsje = hjbhsje;
	}
	@XmlElement(name = "HJSE")  
	public String getHjse() {
		return hjse;
	}
	public void setHjse(String hjse) {
		this.hjse = hjse;
	}
	@XmlElement(name = "BZ")  
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	@XmlElement(name = "BYZD1")  
	public String getByzd1() {
		return byzd1;
	}
	public void setByzd1(String byzd1) {
		this.byzd1 = byzd1;
	}
	@XmlElement(name = "BYZD2")  
	public String getByzd2() {
		return byzd2;
	}
	public void setByzd2(String byzd2) {
		this.byzd2 = byzd2;
	}
	@XmlElement(name = "BYZD3")  
	public String getByzd3() {
		return byzd3;
	}
	public void setByzd3(String byzd3) {
		this.byzd3 = byzd3;
	}
	@XmlElement(name = "BYZD4")  
	public String getByzd4() {
		return byzd4;
	}
	public void setByzd4(String byzd4) {
		this.byzd4 = byzd4;
	}
	@XmlElement(name = "BYZD5")  
	public String getByzd5() {
		return byzd5;
	}
	public void setByzd5(String byzd5) {
		this.byzd5 = byzd5;
	}
	
	
}
