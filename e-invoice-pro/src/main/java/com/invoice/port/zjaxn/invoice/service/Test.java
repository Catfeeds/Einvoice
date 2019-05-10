package com.invoice.port.zjaxn.invoice.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.zjaxn.invoice.bean.RtInvoiceHeadBean;
import com.invoice.rtn.data.RtnData;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test test = new Test();
		//test.getHttpConnectResult();
		RtnData rtn = new RtnData();
		//test.rtOpenToBean(rtn);
		
		test.findInvoice(rtn);
	}
	
	public RtInvoiceBean findInvoice(RtnData rtn) {
		Invque  invque= new Invque();
		Taxinfo taxinfo = new Taxinfo();
		//invque.setRtskm("18092716152801143763");
		invque.setIqseqno("Q0180927162149c67f5");
		taxinfo.setItfjrdm("93363DCC6064869708F1F3C72A0CE72A713A9D425CD50CDE");
		RtInvoiceBean invoiceBean = new RtInvoiceBean();
		ZjaxnGenerateBean hx = new ZjaxnGenerateBean();
		RtInvoiceHeadBean headBean = new RtInvoiceHeadBean();
		Map<String, Object> map=new HashMap<String, Object>();
		String url = "https://nnfpdev.jss.com.cn/shop/buyer/allow/ecOd/queryElectricKp.action";
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
	
	public RtInvoiceBean findInvoice1( RtnData rtn) {
		Invque  invque= new Invque();
		Taxinfo taxinfo = new Taxinfo();
		invque.setRtskm("18092716152801143763");
		taxinfo.setItfjrdm("93363DCC6064869708F1F3C72A0CE72A713A9D425CD50CDE");
		RtInvoiceBean invoiceBean = new RtInvoiceBean();
		ZjaxnGenerateBean hx = new ZjaxnGenerateBean();
		if (rtn.getCode() == 0) {
			//System.out.println("发送数据：" + rtn.getMessage());
			String url = "https://nnfpdev.jss.com.cn/shop/buyer/allow/ecOd/queryElectricKp.action";
			
			Map<String, Object> map=new HashMap<String, Object>();
			String[] fpqqlsh = {invque.getRtskm()};
			map.put("identity", taxinfo.getItfjrdm());
			map.put("fpqqlsh", fpqqlsh);
			rtn.setMessage(JSONObject.toJSONString(map));
	        try {
	        	getHttpConnectResult(url,rtn.getMessage(),rtn);
	        	
			} catch (Exception e) {
				 
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				return null;
			}
	        
	        if(rtn.getCode()==0){
	        	RtInvoiceHeadBean headBean = hx.rtOpenToBean(rtn.getMessage(),rtn);
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
	        
		} else {
			return null;
		}
		return invoiceBean;
	}
	
	public void rtOpenToBean(RtnData rtn){
		ZjaxnGenerateBean pBean = new ZjaxnGenerateBean();
		pBean.rtOpenToBean("", rtn);
		
	} 
	
	public void getHttpConnectResult(String url,String order,RtnData rtn){
		HttpClient httpclient=null;
		PostMethod post=null;
/*		
		String order="{\"identity\":\"93363DCC6064869708F1F3C72A0CE72A713A9D425CD50CDE\","
				+ "\"order\":{\"orderno\":\"No.12016101300002\",\"saletaxnum\":\"339901999999142\","
				+ "\"saleaddress\":\"&*杭州市中河中路222号平海国际商务大厦5楼\",\"salephone\":\"0571-87022168\","
				+ "\"saleaccount\":\"东亚银行杭州分行131001088303400\",\"clerk\":\"袁牧庆\",\"payee\":\"络克\","
				+ "\"checker\":\"朱燕\",\"invoicedate\":\"2016-06-15 01:51:41\","
				+ "\"kptype\":\"1\",\"address\":\"\","
				+ "\"phone\":\"13185029520\",\"taxnum\":\"110101TRDX8RQU1\",\"buyername\":\"个人\",\"account\":\"\","
				+ "\"fpdm\":\"\",\"fphm\":\"\",\"message\":\"开票机号为02 请前往诺诺网(www.jss.com.cn)查询发票详情\","
				+ "\"qdbz\":\"1\",\"qdxmmc\":\"1111\","
				+ "\"detail\":[{\"goodsname\":\"1\",\"spec\":\"1\",\"unit\":\"1\",\"hsbz\":\"1\",\"num\":\"1\","
				+ "\"price\":\"19.99\","
				+ "\"spbm\":\"1090511030000000000\",\"fphxz\":\"0\",\"yhzcbs\":\"0\",\"zzstsgl\":\"222222\",\"lslbs\":\"\","
				+ "\"taxrate\":\"0.16\"},"
				+ "{\"goodsname\":\"2\",\"spec\":\"2\","
				+ "\"unit\":\"2\",\"hsbz\":\"1\",\"num\":\"1\",\"price\":\"20\","
				+ "\"spbm\":\"1090511030000000000\",\"fphxz\":\"0\",\"yhzcbs\":\"0\",\"zzstsgl\":\"222222\",\"lslbs\":\"\","
				+ "\"taxrate\":\"0.16\"}]}}";*/
		
		try{
			httpclient = new HttpClient();
			post = new PostMethod(url);
			//设置编码方式
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
			//添加参数
			String xml = DESDZFP.encrypt(order);
			System.out.println(xml);
			post.addParameter("order",xml);
			//执行
			httpclient.executeMethod(post);
			//接口返回信息
			String info = new String(post.getResponseBody(),"UTF-8");
			rtn.setMessage(info);
			System.out.println(info);
		}catch (Exception e) {
			e.printStackTrace();
		}finally { 
			//关闭连接，释放资源 
			post.releaseConnection();
			((SimpleHttpConnectionManager)httpclient.getHttpConnectionManager()).closeIdleConnections(0); 
		}
	 
	}
	
}
