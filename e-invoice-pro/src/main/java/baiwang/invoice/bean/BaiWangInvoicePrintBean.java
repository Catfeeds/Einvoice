package baiwang.invoice.bean;

import javax.xml.bind.annotation.XmlRootElement;

/*发票打印
 * */
@XmlRootElement(name="fpdy")

public class BaiWangInvoicePrintBean {
	private String eqpttype;//	设备类型
	private String fpqqlsh;//	发票请求流水号
	private String kpddm;//	开票点代码
	private String skpbh;//	税控盘编号
	private String skpkl;//	税控盘口令
	private String keypwd;//	税务数字证书密码
	private String fplxdm;//	发票类型代码
	private String fpdm;//	发票代码
	private String fphm;//	发票号码
	private String dylx;//	打印类型
	private String dyfs;//	打印方式
	public String getEqpttype() {
		return eqpttype;
	}
	public void setEqpttype(String eqpttype) {
		this.eqpttype = eqpttype;
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
	public String getDylx() {
		return dylx;
	}
	public void setDylx(String dylx) {
		this.dylx = dylx;
	}
	public String getDyfs() {
		return dyfs;
	}
	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
	}
	@Override
	public String toString() {
		return "BaiWangInvoicePrintBean [eqpttype=" + eqpttype + ", fpqqlsh=" + fpqqlsh + ", kpddm=" + kpddm
				+ ", skpbh=" + skpbh + ", skpkl=" + skpkl + ", keypwd=" + keypwd + ", fplxdm=" + fplxdm + ", fpdm="
				+ fpdm + ", fphm=" + fphm + ", dylx=" + dylx + ", dyfs=" + dyfs + "]";
	}
	
	
	
}
