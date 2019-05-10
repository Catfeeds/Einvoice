package com.invoice.port.bwjs.invoice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="group")
public class BwjsdjzpRtGroupbean {
	private String fpdm;//	发票代码
	private String fphm;//	发票号码
	private String fpzt;//	发票状态
	private String scbz;//	上传标志
	private String kprq;//	是
	private String jqbh;//	税控服务器编号
	private String skm;//	税控码
	private String jym;//校验码
	private String xhdwsbh;//	销货单位识别号
	private String xhdwmc;//	销货单位名称
	private String xhdwdzdh;//	销货单位地址电话
	private String xhdwyhzh;//	销货单位银行账号
	private String ghdwsbh;//	购货单位识别号
	private String ghdwmc;//	购货单位名称
	private String ghdwdzdh;//	购货单位地址电话
	private String ghdwyhzh;//	购货单位银行账号
	private String bmbbbh;//	编码表版本号
	private String zsfs;//	征税方式
	private String fppy;//	发票票样	
	private double hjje;//	合计金额
	private double hjse;//	合计税额
	private double jshj;//	加税合计
	private String bz;//	备注
	private String skr;//	收款人
	private String kpr;//	开票人
	private String yfpdm;//	原发票代码
	private String yfphm;//	原发票号码
	private String zfrq;//	作废日期
	private String zfr;//	作废人
	private String qmcs;//	签名参数
	private String qmz;//	签名值
	private String ykfsje;//	已开负数金额
	private String yqjg;//	验签结果
	private String ewm;//	二维码
	private BwjsdjzpRtFyxmBean fyxm;
	
	private int xh;
	
	
	@XmlAttribute(name = "xh")
	public int getXh() {
		return xh;
	}
	public void setXh(int xh) {
		this.xh = xh;
	}
	
	
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
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
	public String getFpzt() {
		return fpzt;
	}
	public void setFpzt(String fpzt) {
		this.fpzt = fpzt;
	}
	public String getScbz() {
		return scbz;
	}
	public void setScbz(String scbz) {
		this.scbz = scbz;
	}
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	public String getJqbh() {
		return jqbh;
	}
	public void setJqbh(String jqbh) {
		this.jqbh = jqbh;
	}
	public String getSkm() {
		return skm;
	}
	public void setSkm(String skm) {
		this.skm = skm;
	}
	public String getXhdwsbh() {
		return xhdwsbh;
	}
	public void setXhdwsbh(String xhdwsbh) {
		this.xhdwsbh = xhdwsbh;
	}
	public String getXhdwmc() {
		return xhdwmc;
	}
	public void setXhdwmc(String xhdwmc) {
		this.xhdwmc = xhdwmc;
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
	public String getGhdwsbh() {
		return ghdwsbh;
	}
	public void setGhdwsbh(String ghdwsbh) {
		this.ghdwsbh = ghdwsbh;
	}
	public String getGhdwmc() {
		return ghdwmc;
	}
	public void setGhdwmc(String ghdwmc) {
		this.ghdwmc = ghdwmc;
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
	public String getBmbbbh() {
		return bmbbbh;
	}
	public void setBmbbbh(String bmbbbh) {
		this.bmbbbh = bmbbbh;
	}
	public String getZsfs() {
		return zsfs;
	}
	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
	}
	public String getFppy() {
		return fppy;
	}
	public void setFppy(String fppy) {
		this.fppy = fppy;
	}
	public double getHjje() {
		return hjje;
	}
	public void setHjje(double hjje) {
		this.hjje = hjje;
	}
	public double getHjse() {
		return hjse;
	}
	public void setHjse(double hjse) {
		this.hjse = hjse;
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
	public String getKpr() {
		return kpr;
	}
	public void setKpr(String kpr) {
		this.kpr = kpr;
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
	public String getZfrq() {
		return zfrq;
	}
	public void setZfrq(String zfrq) {
		this.zfrq = zfrq;
	}
	public String getZfr() {
		return zfr;
	}
	public void setZfr(String zfr) {
		this.zfr = zfr;
	}
	public String getQmcs() {
		return qmcs;
	}
	public void setQmcs(String qmcs) {
		this.qmcs = qmcs;
	}
	public String getQmz() {
		return qmz;
	}
	public void setQmz(String qmz) {
		this.qmz = qmz;
	}
	public String getYkfsje() {
		return ykfsje;
	}
	public void setYkfsje(String ykfsje) {
		this.ykfsje = ykfsje;
	}
	public String getYqjg() {
		return yqjg;
	}
	public void setYqjg(String yqjg) {
		this.yqjg = yqjg;
	}
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	@XmlElement(name = "fyxm")  
	public BwjsdjzpRtFyxmBean getFyxm() {
		return fyxm;
	}
	public void setFyxm(BwjsdjzpRtFyxmBean fyxm) {
		this.fyxm = fyxm;
	}
	
	
}
