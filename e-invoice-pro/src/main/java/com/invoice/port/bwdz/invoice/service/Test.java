package com.invoice.port.bwdz.invoice.service;

import java.util.ArrayList;
import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;


public class Test {
	
	BwdzServiceImpl service = new BwdzServiceImpl();
	
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		 
		RtnData rtn = new RtnData();
		//测试开票
		//test.openinvoice(rtn);
		//System.out.println(rtn.getMessage());
		test.findInvoice(rtn);
	}
	
	/**
	 * 发票查询
	 * **/
	public void findInvoice(RtnData rtn){
		Invque invque = new Invque();
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setTaxno("110109500321655");
		invque.setIqseqno("Q01803281412201000");
		service.findInvoice(invque, taxinfo, rtn);
	}
	/**
	 * 开具发票
	 * **/
	public void openinvoice(RtnData rtn){
		

		//HashMap map = new HashMap();
		Invque invque = new Invque();
		
		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		InvoiceSaleDetail detail1 = new InvoiceSaleDetail();
		InvoiceSaleDetail detail2 = new InvoiceSaleDetail();
		InvoiceSaleDetail detail3 = new InvoiceSaleDetail();
		InvoiceSaleDetail detail4 = new InvoiceSaleDetail();
		InvoiceSaleDetail detail5 = new InvoiceSaleDetail();
		InvoiceSaleDetail detail6 = new InvoiceSaleDetail();
		
		
		
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setTaxno("110109500321655");
		taxinfo.setTaxname("苏州工业园区邻里中心商业发展有限公司");
		taxinfo.setTaxadd("苏州工业园区启月街198号6楼");
		taxinfo.setTaxbank("中国工商银行股份有限公司苏州工业园区支行1102020309000383250");
		
		detail1.setGoodsid("1030201030000000000");
		detail1.setGoodsname("卡夫太平苏打香葱味100g");
		detail1.setTaxitemid("1030201030000000000");
		detail1.setAmount(430.77);
		detail1.setTaxfee(73.23);
		detail1.setTaxrate(0.17);
		detail1.setIsinvoice("Y");
		detail1.setPrice(2.991458);
		detail1.setQty(144.0);
		detail1.setTaxpre("0");
		detail1.setTaxprecon("");
		detail1.setZerotax("");
		detail1.setUnit("包");
		
		detail2.setGoodsid("1030201030000000000");
		detail2.setGoodsname("卡夫太平苏打奶盐味100g");
		detail2.setTaxitemid("1030201030000000000");
		detail2.setAmount(430.77);
		detail2.setTaxfee(73.23);
		detail2.setTaxrate(0.17);
		detail2.setIsinvoice("Y");
		detail2.setPrice(2.991458);
		detail2.setQty(144.0);
		detail2.setTaxpre("0");
		detail2.setTaxprecon("");
		detail2.setZerotax("");
		detail2.setUnit("包");
		
		detail3.setGoodsid("1030201030000000000");
		detail3.setGoodsname("卡夫太平苏打芝麻味100g");
		detail3.setTaxitemid("1030201030000000000");
		detail3.setAmount(574.36);
		detail3.setTaxfee(97.64);
		detail3.setTaxrate(0.17);
		detail3.setIsinvoice("Y");
		detail3.setPrice(2.991458);
		detail3.setQty(192.0);
		detail3.setTaxpre("0");
		detail3.setTaxprecon("");
		detail3.setZerotax("");
		detail3.setUnit("包");
		
		detail4.setGoodsid("1030203030000000000");
		detail4.setGoodsname("康师傅经典桶红烧牛肉87.5g");
		detail4.setTaxitemid("1030203030000000000");
		detail4.setAmount(430.77);
		detail4.setTaxfee(73.23);
		detail4.setTaxrate(0.17);
		detail4.setIsinvoice("Y");
		detail4.setPrice(3.589750);
		detail4.setQty(120.0);
		detail4.setTaxpre("0");
		detail4.setTaxprecon("");
		detail4.setZerotax("");
		detail4.setUnit("桶");
		
		detail5.setGoodsid("1030203030000000000");
		detail5.setGoodsname("康师傅经典桶香辣牛肉87.5g");
		detail5.setTaxitemid("1030203030000000000");
		detail5.setAmount(861.54);
		detail5.setTaxfee(146.46);
		detail5.setTaxrate(0.17);
		detail5.setIsinvoice("Y");
		detail5.setPrice(3.589750);
		detail5.setQty(240.0);
		detail5.setTaxpre("0");
		detail5.setTaxprecon("");
		detail5.setZerotax("");
		detail5.setUnit("桶");

		detail6.setGoodsid("1030203030000000000");
		detail6.setGoodsname("康师傅老坛酸菜牛肉桶面125g");
		detail6.setTaxitemid("1030203030000000000");
		detail6.setAmount(430.77);
		detail6.setTaxfee(73.23);
		detail6.setTaxrate(0.17);
		detail6.setIsinvoice("Y");
		detail6.setPrice(3.589750);
		detail6.setQty(120.0);
		detail6.setTaxpre("0");
		detail6.setTaxprecon("");
		detail6.setZerotax("");
		detail6.setUnit("桶");
		
		detailList.add(detail1);
		detailList.add(detail2);
		detailList.add(detail3);
		detailList.add(detail4);
		detailList.add(detail5);
		detailList.add(detail6);
		
		invque.setIqseqno("Q01712071003581500");
		invque.setIqtotje(3158.98);
		invque.setIqtotse(537.02);
		invque.setIqgmfname("苏州第壹制药有限公司");
		invque.setIqgmftax("91320594783360722P");
		//invque.setIqgmfadd("北京市海淀区中关村南大街8号甲8号乙8号威地科技大厦805室51650988");
		//invque.setIqgmfbank("招商银行北京分行东方广场支行862580489510001");
		invque.setIqadmin("system");
		invque.setIqyfpdm("032001600511");
		invque.setIqyfphm("86440577");
		invque.setIqfplxdm("026");
		invque.setIqemail("15371528@qq.com");
		invque.setIqtype(1);
		invque.setZsfs("0");
		
		
		//map.put("invque", invque);
		//map.put("taxinfo", taxinfo);
		//map.put("detailList", detailList);
	 
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.openinvoice(invque,taxinfo,detailList, rtn);
	}
	
}
