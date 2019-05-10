package baiwang.invoice.bean;
/*开具发票后返回的数据
 * */
public class BaiWangRtInvoiceBean  {
	//发票代码
	private String fpdm;
	//发票号码
	private String fphm;
	//开票日期
	private String kprq;
	//税控码
	private String skm;
	//校验码
	private String jym;
	//二维码
	private String eym;
	//二维码
	private String ewm;
	
	
	
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
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
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	public String getEym() {
		return eym;
	}
	public void setEym(String eym) {
		this.eym = eym;
	}
	
	
}
