package com.invoice.port.bwdz.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="REQUEST_COMMON_FPKJ")
public class BwdzInvoiceHeadBean {
	private String fpqqlsh;//发票请求流水号
	private int kplx;//开票类型
	private String zsfs;//征税方式
	private String bmb_bbh;//编码表版本号
	private String xsf_nsrsbh;//销售方纳税人识别号
	private String xsf_mc;//销售方名称
	private String xsf_dzdh;//销售方地址电话
	private String xsf_yhzh;//销售方银行账户
	private String gmf_nsrsbh;//购买方纳税人识别号
	private String gmf_mc;//购买方名称
	private String gmf_dzdh;//购买方地址电话
	private String gmf_yhzh;//购买方银行账户
	private String gmf_sjh;//购买方手机号
	private String gmf_dzyx;//购买方电子邮箱
	private String fpt_zh;//发票通账户
	private String wx_openid;//微信openid
	private String kpr;//开票人
	private String skr;//收款人
	private String fhr;//复核人
	private String yfp_dm;//原发票代码
	private String yfp_hm;//原发票号码
	private double jshj;//价税合计
	private double hjje;//合计金额
	private double hjse;//合计税额
	private double kce;//扣除额
	private String bz;//备注
	private String hylx;//行业类型
	private String by1;//备用字段1
	private String by2;//备用字段2
	private String by3;//备用字段3
	private String by4;//备用字段4
	private String by5;//备用字段5
	private String by6;//备用字段6
	private String by7;//备用字段7
	private String by8;//备用字段8
	private String by9;//备用字段9
	private String by10;//备用字段10
	private String wx_order_id;//微信用于预制卡券的唯一识别ID
	private String wx_app_id;//商户所属微信公众号APPID
	private String zfb_uid;//支付宝UID
	private String tspz;//特殊票种标识
	private String qj_order_id;//全局唯一订单ID
	private String aa;
	private BwdzInvoiceDetaiListBean detaillist;

	@XmlElement(name = "COMMON_FPKJ_XMXXS")  
	public BwdzInvoiceDetaiListBean getDetaillist() {
		return detaillist;
	}

	public void setDetaillist(BwdzInvoiceDetaiListBean detaillist) {
		this.detaillist = detaillist;
	}

	@XmlAttribute(name = "class")
	public String getAa() {
		return "REQUEST_COMMON_FPKJ";
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

	@XmlElement(name = "BMB_BBH")  
	public String getBmb_bbh() {
		return bmb_bbh;
	}
	public void setBmb_bbh(String bmb_bbh) {
		this.bmb_bbh = bmb_bbh;
	}
	@XmlElement(name = "XSF_NSRSBH")  
	public String getXsf_nsrsbh() {
		return xsf_nsrsbh;
	}
	public void setXsf_nsrsbh(String xsf_nsrsbh) {
		this.xsf_nsrsbh = xsf_nsrsbh;
	}
	@XmlElement(name = "XSF_MC")  
	public String getXsf_mc() {
		return xsf_mc;
	}
	public void setXsf_mc(String xsf_mc) {
		this.xsf_mc = xsf_mc;
	}
	@XmlElement(name = "XSF_DZDH")  
	public String getXsf_dzdh() {
		return xsf_dzdh;
	}
	public void setXsf_dzdh(String xsf_dzdh) {
		this.xsf_dzdh = xsf_dzdh;
	}
	@XmlElement(name = "XSF_YHZH")  
	public String getXsf_yhzh() {
		return xsf_yhzh;
	}
	public void setXsf_yhzh(String xsf_yhzh) {
		this.xsf_yhzh = xsf_yhzh;
	}
	@XmlElement(name = "GMF_NSRSBH")  
	public String getGmf_nsrsbh() {
		return gmf_nsrsbh;
	}
	public void setGmf_nsrsbh(String gmf_nsrsbh) {
		this.gmf_nsrsbh = gmf_nsrsbh;
	}
	@XmlElement(name = "GMF_MC")  
	public String getGmf_mc() {
		return gmf_mc;
	}
	public void setGmf_mc(String gmf_mc) {
		this.gmf_mc = gmf_mc;
	}
	@XmlElement(name = "GMF_DZDH")  
	public String getGmf_dzdh() {
		return gmf_dzdh;
	}
	public void setGmf_dzdh(String gmf_dzdh) {
		this.gmf_dzdh = gmf_dzdh;
	}
	@XmlElement(name = "GMF_YHZH")  
	public String getGmf_yhzh() {
		return gmf_yhzh;
	}
	public void setGmf_yhzh(String gmf_yhzh) {
		this.gmf_yhzh = gmf_yhzh;
	}
	@XmlElement(name = "KPR")  
	public String getKpr() {
		return kpr;
	}
	public void setKpr(String kpr) {
		this.kpr = kpr;
	}
	@XmlElement(name = "SKR")  
	public String getSkr() {
		return skr;
	}
	public void setSkr(String skr) {
		this.skr = skr;
	}
	@XmlElement(name = "FHR")  
	public String getFhr() {
		return fhr;
	}
	public void setFhr(String fhr) {
		this.fhr = fhr;
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
	@XmlElement(name = "BZ")  
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	

	@XmlElement(name = "ZSFS")  
	public String getZsfs() {
		return zsfs;
	}

	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
	}
	@XmlElement(name = "GMF_SJH")  
	public String getGmf_sjh() {
		return gmf_sjh;
	}

	public void setGmf_sjh(String gmf_sjh) {
		this.gmf_sjh = gmf_sjh;
	}
	@XmlElement(name = "GMF_DZYX")  
	public String getGmf_dzyx() {
		return gmf_dzyx;
	}

	public void setGmf_dzyx(String gmf_dzyx) {
		this.gmf_dzyx = gmf_dzyx;
	}
	@XmlElement(name = "FPT_ZH")  
	public String getFpt_zh() {
		return fpt_zh;
	}

	public void setFpt_zh(String fpt_zh) {
		this.fpt_zh = fpt_zh;
	}
	@XmlElement(name = "WX_OPENID")  
	public String getWx_openid() {
		return wx_openid;
	}

	public void setWx_openid(String wx_openid) {
		this.wx_openid = wx_openid;
	}
	@XmlElement(name = "KCE")  
	public double getKce() {
		return kce;
	}

	public void setKce(double kce) {
		this.kce = kce;
	}
	@XmlElement(name = "HYLX")  
	public String getHylx() {
		return hylx;
	}

	public void setHylx(String hylx) {
		this.hylx = hylx;
	}
	@XmlElement(name = "BY1")  
	public String getBy1() {
		return by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}
	@XmlElement(name = "BY2")  
	public String getBy2() {
		return by2;
	}

	public void setBy2(String by2) {
		this.by2 = by2;
	}
	@XmlElement(name = "BY3")  
	public String getBy3() {
		return by3;
	}

	public void setBy3(String by3) {
		this.by3 = by3;
	}
	@XmlElement(name = "BY4")  
	public String getBy4() {
		return by4;
	}

	public void setBy4(String by4) {
		this.by4 = by4;
	}
	@XmlElement(name = "BY5")  
	public String getBy5() {
		return by5;
	}

	public void setBy5(String by5) {
		this.by5 = by5;
	}@XmlElement(name = "BY6")  

	public String getBy6() {
		return by6;
	}

	public void setBy6(String by6) {
		this.by6 = by6;
	}
	@XmlElement(name = "BY7")  
	public String getBy7() {
		return by7;
	}

	public void setBy7(String by7) {
		this.by7 = by7;
	}
	@XmlElement(name = "BY8")  
	public String getBy8() {
		return by8;
	}

	public void setBy8(String by8) {
		this.by8 = by8;
	}
	@XmlElement(name = "BY9")  
	public String getBy9() {
		return by9;
	}

	public void setBy9(String by9) {
		this.by9 = by9;
	}
	@XmlElement(name = "BY10")  
	public String getBy10() {
		return by10;
	}

	public void setBy10(String by10) {
		this.by10 = by10;
	}
	@XmlElement(name = "WX_ORDER_ID")  
	public String getWx_order_id() {
		return wx_order_id;
	}

	public void setWx_order_id(String wx_order_id) {
		this.wx_order_id = wx_order_id;
	}
	@XmlElement(name = "WX_APP_ID")  
	public String getWx_app_id() {
		return wx_app_id;
	}

	public void setWx_app_id(String wx_app_id) {
		this.wx_app_id = wx_app_id;
	}
	@XmlElement(name = "ZFB_UID")  
	public String getZfb_uid() {
		return zfb_uid;
	}

	public void setZfb_uid(String zfb_uid) {
		this.zfb_uid = zfb_uid;
	}
	@XmlElement(name = "TSPZ")  
	public String getTspz() {
		return tspz;
	}

	public void setTspz(String tspz) {
		this.tspz = tspz;
	}
	@XmlElement(name = "QJ_ORDER_ID")  
	public String getQj_order_id() {
		return qj_order_id;
	}

	public void setQj_order_id(String qj_order_id) {
		this.qj_order_id = qj_order_id;
	}
	
	
}
