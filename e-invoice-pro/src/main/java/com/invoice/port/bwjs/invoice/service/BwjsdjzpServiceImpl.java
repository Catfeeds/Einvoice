package com.invoice.port.bwjs.invoice.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.apiservice.service.impl.InvoiceAnddetailService;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoicePrintHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.db.Taxprintinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.PortService;
import com.invoice.port.bwjs.invoice.bean.BwjsdjzpRtInvoiceBean;
import com.invoice.port.bwjs.invoice.bean.GlobalZpinterfaceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.uiservice.dao.TaxitemDao;
import com.invoice.util.SpringContextUtil;
@Service("BwjsdjzpServiceImpl")
public class BwjsdjzpServiceImpl implements PortService {
	private final Log log = LogFactory.getLog(BwjsdjzpServiceImpl.class);
	
 
	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	TaxitemDao taxitemDao;
	
	@Autowired
	TaxinfoDao taxinfoDao;
	
	public void openInvoice(Invque que, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList ) {
		RtnData rtn = new RtnData();

			BwjsdjzpRtInvoiceBean resInv = openInvoicezp(que,taxinfo,detailList, rtn);
			
			if(rtn.getCode()!=0){
				throw new RuntimeException("回写发票失败："+rtn.getMessage());
			}
			
			if(resInv==null){
				throw new RuntimeException("发票开具失败："+rtn.getMessage());
			}else
			if(resInv.getFphm()==null||"".equals(resInv.getFphm())){
				throw new RuntimeException("发票开具失败："+rtn.getMessage());
			}
			
			List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
			for(InvoiceSaleDetail detail:detailList){
				if("Y".equals(detail.getIsinvoice())){
					detailList1.add(detail);
				}
			}
			
			InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
			invoiceHead.setFphm(resInv.getFphm());
			invoiceHead.setFpdm(resInv.getFpdm());
			invoiceHead.setFpskm(resInv.getSkm());
			invoiceHead.setFpewm(resInv.getEwm());
			invoiceHead.setFpjym(resInv.getJym());
			invoiceHead.setFprq(resInv.getKprq());
			//invoiceHead.setPdf(resInv.getPdf_url());
			invoiceService.saveInvoice(invoiceHead);
			
			que.setRtewm(resInv.getEwm());
			que.setRtfphm(resInv.getFphm());
			que.setRtfpdm(resInv.getFpdm());
			que.setRtskm(resInv.getSkm());
			que.setRtjym(resInv.getJym());
			//que.setIqpdf(resInv.getPdf_url());
			que.setRtkprq(resInv.getKprq());
			que.setIqstatus(50);
			
			
		}

	private BwjsdjzpRtInvoiceBean openInvoicezp(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {
		BwjsdjzpGenerateBean	bw = new BwjsdjzpGenerateBean();
		boolean flag = false;
		try{
		
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				Taxitem taxitem = new Taxitem();
				taxitem.setTaxitemid(detail.getTaxitemid());
				List<HashMap<String,String>> list= taxitemDao.getTaxitemById(taxitem);
				if(list.size()>0){
					
					detail.setGoodsname("*"+list.get(0).get("taxitemname")+"*"+detail.getGoodsname());
					//detail.setTaxitemname(list.get(0).get("taxitemname"));
				}else{
					rtn.setCode(-1);
					rtn.setMessage("商品税目编码是汇总项");
					flag = true;
					break;
				}
			}
		}
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
		if(flag){
			return null;
		}
		
		
		bw.generateBaiWangzpBean(invque,taxinfo,detailList, rtn);
		 
		if (rtn.getCode() == 0) {
			getHttpConnectResult(rtn.getMessage(),taxinfo.getItfserviceUrl(),rtn);      
		} else {
			return null;
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			BwjsdjzpRtInvoiceBean rtinvoicebean = bw.rtZpOpenToBean(rtn.getMessage(), rtn);

			return rtinvoicebean;
		} else {
			return null;
		}
		
	}

  
	public void invoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn){
		BwjsdjzpGenerateBean	bw = new BwjsdjzpGenerateBean();
		
		Map<String, Object> p=new HashMap<String, Object>();
		p.put("yfpdm", invque.getRtfpdm());
		p.put("yfphm", invque.getRtfphm());
		List<InvoiceSaleDetail> detailList = SpringContextUtil.getBean(InvoiceAnddetailService.class).getHongInvoiceDetail(p);	
		InvoicePrintHead printhead = bw.zp_print(invque, taxinfo, detailList, rtn);
		if(printhead==null){
			rtn.setCode(-1);
			rtn.setMessage("没有获取到打印的发票信息");
			return;
		}
		Taxprintinfo taxprint = new Taxprintinfo();
		taxprint.setEntid(invque.getIqentid());
		taxprint.setTaxno(taxinfo.getTaxno());
		taxprint.setKpd(invque.getIqtaxzdh());
		try{
			taxprint=taxinfoDao.getTaxprintinfoByNo(taxprint);
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
		if(taxprint==null){
			rtn.setCode(-1);
			rtn.setMessage("没有获取到打印配置信息");
			return;
		}
		printhead.setTaxprintinfo(taxprint);
		invque.setData(printhead);
		System.out.println(rtn.getMessage());

	}

	public RtInvoiceBean  invoiceInblankValid(Invque invque, Taxinfo taxinfo,RtnData rtn) {
		BwjsdjzpGenerateBean	bw = new BwjsdjzpGenerateBean();
		bw.generateInvoiceblankInValid(invque,taxinfo, rtn);
		
		if (rtn.getCode() == 0) {
			getHttpConnectResult(rtn.getMessage(),taxinfo.getItfserviceUrl(),rtn);      
		} else {
			return null;
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			RtInvoiceBean rtinvoicebean = bw.rtZpZfToBean(rtn.getMessage(), rtn);
			return rtinvoicebean;
		} else {
			return null;
		}
		
	}
	
 
	public RtInvoiceBean  invoiceYkInValid(Invque invque, Taxinfo taxinfo,RtnData rtn) {
		BwjsdjzpGenerateBean	bw = new BwjsdjzpGenerateBean();
		bw.generateInvoiceYkInValid(invque,taxinfo, rtn);
		 
		if (rtn.getCode() == 0) {
			getHttpConnectResult(rtn.getMessage(),taxinfo.getItfserviceUrl(),rtn);      
		} else {
			return null;
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			RtInvoiceBean rtinvoicebean = bw.rtZpZfToBean(rtn.getMessage(), rtn);
			return rtinvoicebean;
		} else {
			return null;
		}
	}

 
	/**
	 * 空白发票查询
	 * **/
	
	public RtInvoiceBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		BwjsdjzpGenerateBean	bw = new BwjsdjzpGenerateBean();
		bw.blankInvoice(invque,rtn);
		RtInvoiceBean  invoicebean = new RtInvoiceBean();
		if (rtn.getCode() == 0) {
			getHttpConnectResult(rtn.getMessage(),taxinfo.getItfserviceUrl(),rtn);      
		} else {
			return null;
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			invoicebean = bw.rtblankInvoiceToBean(rtn.getMessage(), rtn);
			if(invoicebean==null){
				rtn.setCode(-1);
				return null;
			}else
			if(invoicebean.getFp_hm()==null||"".equals(invoicebean.getFp_hm())){
				rtn.setCode(-1);
				return null;
			}
			
 

			return invoicebean;
		} else {
			return null;
		}
		
	}
	
	
	/**
	 * 获取发票PDF连接 
	 * **/
	public String getPdf(Invque invque,RtnData rtn){
		return null;
	}
	
	
	public RtInvoiceBean findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		
		BwjsdjzpGenerateBean bw = new BwjsdjzpGenerateBean();
		bw.zp_findInvoice(invque,rtn);
		GlobalZpinterfaceBean  rtinvoicebean = new GlobalZpinterfaceBean();
		RtInvoiceBean rtbean = new RtInvoiceBean();
		if (rtn.getCode() == 0) {
			getHttpConnectResult(rtn.getMessage(),taxinfo.getItfserviceUrl(),rtn);      
		} else {
			return null;
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			rtinvoicebean = bw.rtZpFindToBean(rtn.getMessage(), rtn);
			if(rtinvoicebean==null){
				rtn.setCode(-1);
				return null;
			}else
			{
			       if(rtinvoicebean.getBody().getReturncode()==null||!"0".equals(rtinvoicebean.getBody().getReturncode())){
			    	   rtn.setCode(-1);
			    	   rtn.setMessage(rtinvoicebean.getBody().getReturnmsg());
			    	   return null;
			       }

			       	   rtbean.setEwm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getEwm());
			       	   rtbean.setFp_dm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getFpdm());
			       	   rtbean.setFp_hm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getFphm());
			       	   rtbean.setKprq(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getKprq());
			       	   rtbean.setJym(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getJym());
			       	   rtbean.setSkm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getSkm());			       	   
		
			}
			return rtbean;
		} else {
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
	public void getHttpConnectResult(String xml, String address,RtnData rtn) {
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
			//conn.setConnectTimeout(6000);// 设置连接主机的超时时间
			//conn.setReadTimeout(6000);// 设置从主机读取数据的超时时间

			wr = conn.getOutputStream();
			wr.write(xml.getBytes("GBK"));
			wr.flush();
			resultData = IOUtils.toString(conn.getInputStream(), "GBK");
			rtn.setMessage(resultData);
			//rtn.setMessage(new String (MyAES.decryptBASE64(resultData),"utf-8") );
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
	 
	}
	
}
