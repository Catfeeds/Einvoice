package com.invoice.port.nbbanji.invoice.bean;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="business")
public class ErrorResponseBean {
	private String RETURNCOD;
	private String RETURNMSG;
	private String NSRSBH;
	private String FPQQLSH;
	
	public String getFPQQLSH() {
		return FPQQLSH;
	}

	public void setFPQQLSH(String fPQQLSH) {
		FPQQLSH = fPQQLSH;
	}

	public String getNSRSBH() {
		return NSRSBH;
	}

	public void setNSRSBH(String nSRSBH) {
		NSRSBH = nSRSBH;
	}

	public String getRETURNCOD() {
		return RETURNCOD;
	}
	
	public void setRETURNCOD(String rETURNCOD) {
		RETURNCOD = rETURNCOD;
	}
	
	public String getRETURNMSG() {
		return RETURNMSG;
	}
	
	public void setRETURNMSG(String rETURNMSG) {
		RETURNMSG = rETURNMSG;
	}
	
	public String toString(String flag){
		String strCX = "";
		String strMG = "";
		
		if (flag.equalsIgnoreCase("DDCX"))
		{
			strCX = "DDCX";
			strMG = "订单查询";
		}
		
		if (flag.equalsIgnoreCase("DDHX"))
		{
			strCX = "DDHX";
			strMG = "订单开具结果回写";
		}
		
		StringBuilder strResult = new StringBuilder();
		
		strResult.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				 .append("<business id=\""+strCX+"\" comment=\""+strMG+"\">")
				 .append("<NSRSBH>"+getNSRSBH()+"</NSRSBH>")
				 .append("<FPQQLSH>"+getFPQQLSH()+"</FPQQLSH>")
				 .append("<RETURNCODE>"+getRETURNCOD()+"</RETURNCODE>")
				 .append("<RETURNMSG>"+getRETURNMSG()+"</RETURNMSG>")
				 .append("</business>");
		
		return strResult.toString();
	}	
}
