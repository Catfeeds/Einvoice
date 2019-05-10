package com.invoice.port.zjaxn.invoice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.PortService;
import com.invoice.port.zjaxn.invoice.bean.RtInvoiceHeadBean;
import com.invoice.port.zjaxn.invoice.bean.RtInvoiceStatusBean;
import com.invoice.rtn.data.RtnData;
@Service("ZjaxnServiceImpl")
public class ZjaxnServiceImpl implements PortService {
	private final Log log = LogFactory.getLog(ZjaxnServiceImpl.class);
	
	@Autowired
	InvoiceService invoiceService;
	
	@Override
	public void openInvoice(Invque que, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList) {
		RtnData rtn = new RtnData();
		if(que.getRetflag()==null) que.setRetflag("");
		RtInvoiceBean resInv = null;
		if("1".equals(que.getRetflag())){
			resInv =findInvoice(que,taxinfo,rtn);
		}else{
			resInv = openinvoicedp(que,taxinfo,detailList, rtn);
		}
		if(rtn.getCode()!=0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}else
		if(resInv==null){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}else
		if(resInv.getFp_hm()==null||"".equals(resInv.getFp_hm())){
			throw new RuntimeException("发票平台无返回信息");
		}
		List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				detailList1.add(detail);
			}
		}
 
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
		invoiceHead.setFpewm(resInv.getEwm());
		invoiceHead.setFphm(resInv.getFp_hm());
		invoiceHead.setFpdm(resInv.getFp_dm());
		invoiceHead.setFpskm(resInv.getSkm());
		invoiceHead.setFpjym(resInv.getFwm());
		invoiceHead.setFprq(resInv.getKprq());
		invoiceHead.setPdf(resInv.getPdf_url());
		invoiceService.saveInvoice(invoiceHead);
		
		que.setRtewm(resInv.getEwm());
		que.setRtfphm(resInv.getFp_hm());
		que.setRtfpdm(resInv.getFp_dm());
		que.setRtskm(resInv.getSkm());
		que.setRtjym(resInv.getFwm());
		que.setRtkprq(resInv.getKprq());
		que.setIqpdf(resInv.getPdf_url());
		que.setIqstatus(50);
		 

	}
	
	public RtInvoiceBean openinvoicedp(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {
		ZjaxnGenerateBean hx = new ZjaxnGenerateBean();
		
		hx.generateBean(invque, taxinfo, detailList, rtn);
		// 向航信发送数据
	if (rtn.getCode() == 0) {
			//System.out.println("发送数据：" + rtn.getMessage());
			String url = taxinfo.getItfserviceUrl();
			
	        try {
	        	getHttpConnectResult(url,rtn.getMessage(),rtn);

			} catch (Exception e) {
				log.error(e, e);
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				return null;
			}
	            
		} else {
			return null;
		}
		// 返回后的数据做装换
	RtInvoiceBean invoiceHeadBean = new RtInvoiceBean();
		if (rtn.getCode() == 0) {
			RtInvoiceStatusBean p = JSONObject.parseObject(rtn.getMessage(), RtInvoiceStatusBean.class);
			if("0000".equals(p.getStatus())){
				invque.setRtskm(p.getFpqqlsh());//把开票接口返回的流水号记录不字段,此接口没有返回SKM值，用流水号替换
				try{
				Thread.sleep(10000);
				}catch(Exception e){
					rtn.setCode(-1);
					rtn.setMessage("等待查询发票出错");
					e.printStackTrace();
				}
				invoiceHeadBean  =findInvoice(invque,taxinfo,rtn);
			}else{
				rtn.setCode(-1);
				rtn.setMessage(p.getMessage());
			}
			return invoiceHeadBean;
		} else {
			return null;
		}
	}

	@Override
	public RtInvoiceBean blankInvoice(Invque invque, Taxinfo taxinfo, RtnData rtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RtInvoiceBean invoiceInblankValid(Invque invque, Taxinfo taxinfo, RtnData rtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RtInvoiceBean invoiceYkInValid(Invque invque, Taxinfo taxinfo, RtnData rtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invoicePrint(Invque invque, Taxinfo taxinfo, RtnData rtn) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getPdf(Invque invque, RtnData rtn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RtInvoiceBean findInvoice(Invque invque, Taxinfo taxinfo, RtnData rtn) {
		RtInvoiceBean invoiceBean = new RtInvoiceBean();
		ZjaxnGenerateBean hx = new ZjaxnGenerateBean();
		RtInvoiceHeadBean headBean = new RtInvoiceHeadBean();
		Map<String, Object> map=new HashMap<String, Object>();
		String url = taxinfo.getItfkeypwd();
		if (rtn.getCode() == 0) {
			//System.out.println("发送数据：" + rtn.getMessage());
			if(invque.getRtskm()==null||"".equals(invque.getRtskm())){
				map=new HashMap<String, Object>();
    			String[] orderno = {invque.getIqseqno()};
    			map.put("identity", taxinfo.getItfjrdm());
    			map.put("orderno", orderno);
    			rtn.setMessage(JSONObject.toJSONString(map));
    	        try {
    	        	getHttpConnectResult(url,rtn.getMessage(),rtn);
    	        	
    			} catch (Exception e) {
    				log.error(e, e);
    				rtn.setCode(-1);
    				rtn.setMessage(e.getMessage());
    				return null;
    			}
        	
	        	headBean = hx.rtOpenToBean(rtn.getMessage(),rtn);
	        	if(rtn.getCode()==0){
	        		invoiceBean.setPdf_url(headBean.getC_url());
	        		invoiceBean.setKprq(headBean.getC_kprq());
	        		invoiceBean.setFp_dm(headBean.getC_fpdm());
	        		invoiceBean.setFp_hm(headBean.getC_fphm());
	        		invoiceBean.setJym(headBean.getC_jym());
	        	}else{
		        	return null;
		        }
			
			}else{
				
				String[] fpqqlsh = {invque.getRtskm()};
				map.put("identity", taxinfo.getItfjrdm());
				map.put("fpqqlsh", fpqqlsh);
				rtn.setMessage(JSONObject.toJSONString(map));
		        try {
		        	getHttpConnectResult(url,rtn.getMessage(),rtn);
		        	
				} catch (Exception e) {
					log.error(e, e);
					rtn.setCode(-1);
					rtn.setMessage(e.getMessage());
					return null;
				}
		        
		        if(rtn.getCode()==0){
		        	 headBean = hx.rtOpenToBean(rtn.getMessage(),rtn);
		        	if(!"2".equals(headBean.getC_status())){//如果没有返回发票信息用订单号再查询一次
		        		map=new HashMap<String, Object>();
		    			String[] orderno = {invque.getIqseqno()};
		    			map.put("identity", taxinfo.getItfjrdm());
		    			map.put("orderno", orderno);
		    			rtn.setMessage(JSONObject.toJSONString(map));
		    	        try {
		    	        	getHttpConnectResult(url,rtn.getMessage(),rtn);
		    	        	
		    			} catch (Exception e) {
		    				log.error(e, e);
		    				rtn.setCode(-1);
		    				rtn.setMessage(e.getMessage());
		    				return null;
		    			}
		        	}
		        	headBean = hx.rtOpenToBean(rtn.getMessage(),rtn);
		        	if(rtn.getCode()==0){
		        		invoiceBean.setPdf_url(headBean.getC_url());
		        		invoiceBean.setKprq(headBean.getC_kprq());
		        		invoiceBean.setFp_dm(headBean.getC_fpdm());
		        		invoiceBean.setFp_hm(headBean.getC_fphm());
		        		invoiceBean.setJym(headBean.getC_jym());
		        	}else{
			        	return null;
			        }
		        	
		        }else{
		        	return null;
		        }
	        
			}
			
		} else {
			return null;
		}
		return invoiceBean;
	}
	
	/**
	 * http post请求方式
	 * 
	 * @param xml
	 * @param address
	 * @return
	 */
	public void getHttpConnectResult(String address, String order,RtnData rtn) {
		HttpClient httpclient=null;
		PostMethod post=null;
		
		 
		
		try{
			httpclient = new HttpClient();
			post = new PostMethod(address);
			//设置编码方式
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
			//添加参数
			 
			post.addParameter("order",DESDZFP.encrypt(order));
			//执行
			httpclient.executeMethod(post);
			//接口返回信息
			String info = new String(post.getResponseBody(),"UTF-8");
			System.out.println(info);
			rtn.setMessage(info);
		}catch (Exception e) {
			e.printStackTrace();
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
		}finally { 
			//关闭连接，释放资源 
			post.releaseConnection();
			((SimpleHttpConnectionManager)httpclient.getHttpConnectionManager()).closeIdleConnections(0); 
		}
	 
	}
	
}
