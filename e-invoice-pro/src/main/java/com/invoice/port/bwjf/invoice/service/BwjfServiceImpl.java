package com.invoice.port.bwjf.invoice.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.bwjf.invoice.bean.BwjfDzRtInvoiceBean;
import com.invoice.port.bwjf.invoice.bean.BwjfRtInvoiceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.MyAES;

public class BwjfServiceImpl implements BwjfService {
	
	BwjfGenerateBean bw;
	public BwjfServiceImpl( ){

		bw = new BwjfGenerateBean();
	}

	@Override
	public void openinvoicezp(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {
		 
		bw.generateBaiWangzpBean(invque,taxinfo,detailList, rtn);
		 
		System.out.println(rtn.getMessage());
		invque.setIqmsg(rtn.getMessage());
	}

	@Override
	public BwjfRtInvoiceBean rtZpOpenToBean(String xml,RtnData rtn) {
	//	BwjfRtInvoiceBean  rtinvoicebean = new BwjfRtInvoiceBean();
		return bw.rtZpOpenToBean(xml,rtn);
		 
		
	}
	
	@Override
	public String  zp_print(Invque invque, Taxinfo taxinfo,
			RtnData rtn) {
		 
		bw.zp_print(invque,taxinfo, rtn);
		 
		System.out.println(rtn.getMessage());

		return rtn.getMessage();
	}
	
	
	@Override
	public String  invoiceInblankValid(Invque invque, Taxinfo taxinfo,RtnData rtn) {
		 
		bw.generateInvoiceblankInValid(invque,taxinfo, rtn);
		 
		System.out.println(invque.getIqmsg());
		
		return invque.getIqmsg();
	}
	
	@Override
	public String  invoiceYkInValid(Invque invque, Taxinfo taxinfo,RtnData rtn) {
		 
		bw.generateInvoiceYkInValid(invque,taxinfo, rtn);
		 
		System.out.println(invque.getIqmsg());
		
		return invque.getIqmsg();
	}
	
	@Override
	public BwjfDzRtInvoiceBean openinvoice(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {
		
		bw.generateBaiWangBean(invque,taxinfo,detailList, rtn);
		try{
		bw.globalDzinterfaceXml(invque, taxinfo, rtn.getMessage(), rtn);
		}catch(Exception e){
			rtn.setCode(-1);
			e.printStackTrace();
		}
		System.out.println(rtn.getMessage());
		//登陆百旺获取数据
		if(rtn.getCode()==0){
			String aa =getHttpConnectResult(rtn.getMessage(),taxinfo.getItfserviceUrl(),rtn);
		//	System.out.println("百望九赋电子发票返回"+aa);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		
		BwjfDzRtInvoiceBean rtinvoicebean = new BwjfDzRtInvoiceBean();
		//返回后的数据做装换
		/*String rtxml = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
				"<business id=\"FPKJ\" comment=\"发票开具\">"+
				"<RESPONSE_COMMON_FPKJ class=\"RESPONSE_COMMON_FPKJ\">"+
				"<FPQQLSH>发票请求流水号</FPQQLSH>"+
				"<JQBH>税控设备编号</JQBH>"+
				"<FP_DM>发票代码</FP_DM>"+
				"<FP_HM>发票号码</FP_HM>"+
				"<KPRQ>开票日期</KPRQ>"+
				"<FP_MW>发票密文</FP_MW>"+
				"<JYM>校验码</JYM>"+
				"<EWM>二维码</EWM>"+
				"<BZ>备注</BZ>"+
				"<RETURNCODE>0000</RETURNCODE>"+
				"<RETURNMSG>返回信息</RETURNMSG>"+
				"</RESPONSE_COMMON_FPKJ>"+
				"</business>";*/
		
		if(rtn.getCode()==0){
			rtinvoicebean=  bw.rtDzOpenToBean(rtn.getMessage(),rtn);
			
			System.out.println(rtn.getMessage());
			
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		
		return rtinvoicebean;
	}

	/**
	 * 获取发票PDF连接(含推送)
	 * **/
	public String getPdf(Invque invque,Taxinfo taxinfo,RtnData rtn){
		try{
			
		bw.generatePdf(invque,taxinfo, rtn);
		
		}catch(Exception e){
			rtn.setCode(-1);
			e.printStackTrace();
		}
		if(rtn.getCode()==0){
			getHttpConnectResult(rtn.getMessage(),taxinfo.getItfserviceUrl(),rtn);

		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
	
		String rt = "";
		
		if(rtn.getCode()==0){
			rt = bw.rtPdfToString(rtn.getMessage(), rtn);
			System.out.println(rt);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		return rt;
	}
	
	/**
	 * 空白发票查询
	 * **/
	
	public String blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		bw.blankInvoice(invque,rtn);

		if(rtn.getCode()==0){
			return rtn.getMessage();
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("查询空白发票返回的数据有问题");
			return null;
		}

	}
	
	/**
	 * 获取连接方式
	 * **/
	
	public String urlconnnet(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		bw.urlconnnet(invque,taxinfo,rtn);

		if(rtn.getCode()==0){
			return rtn.getMessage();
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("查询空白发票返回的数据有问题");
			return null;
		}

	}
	
	/**
	 * http post请求方式
	 * 
	 * @param xml
	 * @param address
	 * @return
	 */
	public String getHttpConnectResult(String xml, String address,RtnData rtn) {
		String resultData = "";
		System.out.println("http请求开始，请求地址：" + address);
		try {
			 resultData = HttpClientCommon.doPostStream(xml, null, address, 6000, 6000, "utf-8");
			 if(StringUtils.isEmpty(resultData)){
				 	rtn.setCode(-1);
					rtn.setMessage("请求电子发票平台超时");
					throw new RuntimeException("请求电子发票平台超时");
			 }
			 rtn.setMessage(new String (MyAES.decryptBASE64(resultData),"utf-8") );
			 //System.out.println(rtn.getMessage());
			 System.out.println(resultData);
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
			System.out.println("访问百望九赋异常：" + address);
		}	
			
		return resultData;
	}
	
	/**
	 * http post请求方式
	 * 
	 * @param xml
	 * @param address
	 * @return
	 */
	public String getHttpConnectResult(String xml, String address,RtnData rtn,String a) {
		String resultData = "";
		System.out.println("http请求开始，请求地址：" + address);
		OutputStream wr = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(address);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(60000);// 设置连接主机的超时时间
			conn.setReadTimeout(60000);// 设置从主机读取数据的超时时间

			wr = conn.getOutputStream();
			wr.write(xml.getBytes("utf-8"));
			wr.flush();
			resultData = IOUtils.toString(conn.getInputStream(), "utf-8");
			rtn.setMessage(new String (MyAES.decryptBASE64(resultData),"utf-8") );
			System.out.println("Message  "+rtn.getMessage());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			System.out.println("http请求失败！请求地址不正确！请求地址：" + address);
		} catch (IOException e) {
			e.printStackTrace();
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			System.out.println("http请求失败！发生i/o错误，请求地址：" + address);
		} catch (Exception e) {
			e.printStackTrace();
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			System.out.println("访问百望九赋异常：" + address);
		} finally {
			try {
				if (wr != null) {
					wr.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultData;
	}
	
}
