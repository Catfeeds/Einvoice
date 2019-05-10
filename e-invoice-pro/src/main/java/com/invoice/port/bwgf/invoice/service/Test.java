package com.invoice.port.bwgf.invoice.service;

import java.util.ArrayList;
import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;

import baiwang.invoice.bean.FpttBean;
import baiwang.invoice.service.Md5AndSHA1Factory;

public class Test {
	
	BwgfServiceImpl service = new BwgfServiceImpl();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RtnData rtn = new RtnData();
		Test test = new Test();
		//开票测试
		test.openinvoice(rtn);
		//查询测试
		//test.findInvoice(rtn);
		//版式生成
		//test.testPdf(rtn);
		//测试空白票查询
		//test.testblankInvoice(rtn);
	}
	
	
	public void getFptt(RtnData rtn){
		FpttBean fptt = new FpttBean();
		fptt.setClient_id("10000005");
		fptt.setClient_secret("b65025d0-19d2-4841-88f4-ff4439b8da58");
		fptt.setGrant_type("password");
		fptt.setMethod("baiwang.oauth.token");
		fptt.setPassword(Md5AndSHA1Factory.md5AndSha("a123456"+"94db610c5c3049df8da3e9ac91390015"));
		fptt.setUrl("http://60.205.83.27/router/rest");
		fptt.setUsername("admin_1800000021168");
		fptt.setVersion("1.0");
		fptt.setTimestamp(System.currentTimeMillis()+"");
		service.access_fptt(fptt,rtn);
	}
	
	/**
	 * 开具发票
	 * **/
	public void openinvoice(RtnData rtn){
		

		//HashMap map = new HashMap();
		Invque invque = new Invque();
		
		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		InvoiceSaleDetail detail = new InvoiceSaleDetail();
		detailList.add(detail);
		
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setItfeqpttype("0");
		//taxinfo.setItfskpbh("499000150629");
		//taxinfo.setItfskpkl("88888888");
	///	taxinfo.setItfkeypwd("12345678");
	//	taxinfo.setItfbbh("");
		taxinfo.setTaxno("91500000747150426A");
		taxinfo.setItfserviceUrl("http://60.205.83.27/router/rest");
		//taxinfo.setItfkpd("kpyuan002");
		
		detail.setGoodsid("1060301020100000000");
		detail.setGoodsname("测试中类税目");
		detail.setTaxitemid("1060301020100000000");
		detail.setAmount(100.00);
		detail.setTaxfee(17.00);
		detail.setTaxrate(0.17);
		detail.setIsinvoice("Y");
		detail.setTaxpre("0");
		detail.setRowno(1);
		 
		
		invque.setIqseqno("12345671234345666653");
		invque.setIqtaxzdh("kpyuan002"); 
		invque.setIqtotje(100.00);
		invque.setIqtotse(17.00);
		invque.setIqgmfname("北京富基融通科技有限公司");
		invque.setIqgmftax("91110108718774697G");
		invque.setIqgmfadd("北京市海淀区中关村南大街8号甲8号乙8号威地科技大厦805室51650988");
		invque.setIqgmfbank("招商银行北京分行东方广场支行862580489510001");
		invque.setIqadmin("张三");
		//invque.setIqyfpdm("");
		//invque.setIqyfphm("");
		invque.setIqtype(0);
		invque.setZsfs("0");
		//invque.setIqshop("");
		invque.setIqfplxdm("026");
		invque.setIqemail("157432381@qq.com");
		//invque.setIqmemo("");
		
		//map.put("invque", invque);
		//map.put("taxinfo", taxinfo);
		//map.put("detailList", detailList);
		
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.openinvoice(invque,taxinfo,detailList, rtn);
	}
	
	/**
	 * 版式推送
	 * **/
	public void testPdf(RtnData rtn){
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setItfeqpttype("0");
		taxinfo.setTaxno("91500000747150532A");
		Invque invque = new Invque();
		invque.setIqseqno("12345671234345666647");
		invque.setRtfpdm("150000000054");
		invque.setRtfphm("40184513");
		invque.setIqemail("157432381@qq.com");
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.getPdf(invque,taxinfo, rtn);
		
	}
	
	/**
	 * 发票查询
	 * **/
	public void findInvoice(RtnData rtn){
		Invque invque = new Invque();
		//invque.setRtfpdm("150000000054");
		//invque.setRtfphm("40184513");
		invque.setIqseqno("12345671234345666647");
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setItfeqpttype("0");
		taxinfo.setTaxno("91500000747150532A");

		service.findInvoice(invque,taxinfo, rtn);
	}
	
	
	/**
	 * 空白发票查询
	 * **/
	public void testblankInvoice(RtnData rtn){
		
		//HashMap map = new HashMap();
		Invque invque = new Invque();
		Taxinfo taxinfo = new Taxinfo();
		
		taxinfo.setItfeqpttype("0");
		taxinfo.setTaxno("91500000747150532A");
		invque.setIqtaxzdh("532dzp");
		invque.setIqfplxdm("007");
 
		
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.blankInvoice(invque,taxinfo,rtn);
		
	}
}
