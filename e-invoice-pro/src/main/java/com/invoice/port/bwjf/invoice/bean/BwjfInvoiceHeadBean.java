package com.invoice.port.bwjf.invoice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="body")
//开具发票需要的发票头信息
public class BwjfInvoiceHeadBean {
	private String kpzdbs;//	开票终端标识  券票
	private String fplxdm;//	发票类型代码  券票
	private String kplx;//	开票类型  券票
	private String fpqqlsh;//	发票请求流水号  券票
	private String tspz;//	特殊票种标识
	private String xhdwsbh;//	销货单位识别号  券票
	private String xhdwmc;//	销货单位名称   券票
	private String xhdwdzdh;//	销货单位地址电话
	private String xhdwyhzh;//	销货单位银行账号
	private String ghdwsbh;//	购货单位识别号  券票
	private String ghdwmc;//	购货单位名称  券票
	private String ghdwdzdh;//	购货单位地址电话
	private String ghdwyhzh;//	购货单位银行账号
	private String qdbz;//	清单标志
	private String zsfs;//	征税方式  券票
	private String hjje;//	合计金额
	private String hjse;//	合计税额
	private String jshj;//	价税合计
	private String kce;//	差额征税扣除额
	private String bz;//	备注
	private String skr;//	收款人
	private String fhr;//	复核人
	private String kpr;//	开票人
	private String tzdbh;//	信息表编号
	private String yfpdm;//	原发票代码
	private String yfphm;// 	原发票号码
	private String fppy;//	发票票样 券票独有字段;
	
	private  BwjfZpInvoiceDetailList detailList;
	
	private String yylxdm;
	
	
	@XmlAttribute(name = "yylxdm")
	public String getYylxdm() {
		return yylxdm;
	}
	public void setYylxdm(String yylxdm) {
		this.yylxdm = yylxdm;
	}
	@XmlElement(name = "fyxm")  
	public BwjfZpInvoiceDetailList getDetailList() {
		return detailList;
	}
	public void setDetailList(BwjfZpInvoiceDetailList detailList) {
		this.detailList = detailList;
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
	public String getKplx() {
		return kplx;
	}
	public void setKplx(String kplx) {
		this.kplx = kplx;
	}
	public String getFpqqlsh() {
		return fpqqlsh;
	}
	public void setFpqqlsh(String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}
	public String getTspz() {
		return tspz;
	}
	public void setTspz(String tspz) {
		this.tspz = tspz;
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
	public String getQdbz() {
		return qdbz;
	}
	public void setQdbz(String qdbz) {
		this.qdbz = qdbz;
	}
	public String getZsfs() {
		return zsfs;
	}
	public void setZsfs(String zsfs) {
		this.zsfs = zsfs;
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
	public String getJshj() {
		return jshj;
	}
	public void setJshj(String jshj) {
		this.jshj = jshj;
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
	public String getTzdbh() {
		return tzdbh;
	}
	public void setTzdbh(String tzdbh) {
		this.tzdbh = tzdbh;
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
	public String getFppy() {
		return fppy;
	}
	public void setFppy(String fppy) {
		this.fppy = fppy;
	}
	
	/**
	 * 开票返回参数
	 * */
	private String returncode;
	private String returnmsg;
	private BwjfRtInvoiceBean rtBean;


	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	public String getReturnmsg() {
		return returnmsg;
	}
	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}
	
	@XmlElement(name = "returndata")  
	public BwjfRtInvoiceBean getRtBean() {
		return rtBean;
	}
	public void setRtBean(BwjfRtInvoiceBean rtBean) {
		this.rtBean = rtBean;
	}
	
	

}
