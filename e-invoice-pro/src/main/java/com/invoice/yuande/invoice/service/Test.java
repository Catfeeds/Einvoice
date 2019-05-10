package com.invoice.yuande.invoice.service;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.htxx.service.scserver.WbServiceI;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;
import com.invoice.yuande.invoice.bean.YuanDeDzRntDataBean;
import com.invoice.yuande.invoice.bean.YuanDeRtInvoiceBean;


public class Test {
	String nsrsbh = "15000119780514458045";
	//String jrdm  ="aabbccddeeff";
	//String serviceUrl = "http://221.234.42.183:8082/zzs_kpfw_ARM9/webservice/eInvWS/15000119780514458045?wsdl";  //发票开具
    String serviceUrl  ="http://60.208.106.171:8080/FPGLXT/wb/sendData.action"; //发票开具
	// String serviceUrl  ="http://ei-test.51fapiao.cn:28888/fpqz/webservice/eInvWS?wsdl";//发票签章
	// String serviceUrl  ="http://221.234.42.183:8082/zzs_kpfw_ARM9/webservice/eInvWS/15000119780514458045?wsdl";//发票查询
	 

	 //      webservice方法名：fpcyInterface 
    
    YuanDeServiceImpl service = new YuanDeServiceImpl();
    
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Test test = new Test();
		RtnData rtn = new RtnData();
		//test.rtInvoice(rtn);
		//test.openinvoice(rtn); //发票开具
		//test.findInvoiceBean(rtn);//发票查询
		//test.test();
		//test.rtOpenSubmitToBean(rtn);
		//	test.wbservice();
		
		//double sum = 6; double tax =2;
		//System.out.println(MathCal.div(sum, tax, 2));

		//test.findInvoiceDzBbBean(rtn);
/*		String time = "2";
		String day = "2018-04-11 14:03:58";
		java.text.SimpleDateFormat formatter=new java.text.SimpleDateFormat("yyyy-MM-dd HH:dd:ss");    
		java.util.Calendar Cal=java.util.Calendar.getInstance();    
		
		Cal.setTime(formatter.parse(day));    
		
		Cal.add(java.util.Calendar.HOUR_OF_DAY,Integer.parseInt(time));    
		
		System.out.println("date:"+formatter.format(Cal.getTime()));  
		System.out.println(Cal.getTime().before(new java.util.Date()));  */
		
		System.out.println(rtn.getMessage());
		System.out.println(MathCal.add((3535.26/1621.6789),0,4));
	}
	
	public void findInvoiceDzBbBean(RtnData rtn) {
		Invque invque = new Invque();
		Taxinfo taxinfo = new Taxinfo();
		invque.setIqseqno("913713007063096591");
		taxinfo.setItfserviceUrl("http://60.213.49.66:8091/FPGLXT_FW/CXF/WbService?wsdl");
		YuanDeServiceImpl service = new YuanDeServiceImpl();
		YuanDeDzRntDataBean bean =  service.findInvoiceDzBbBean(invque, taxinfo, rtn);
	}
	
	public static void wbservice() throws Exception
	{
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		
		factory.setServiceClass(WbServiceI.class);
		factory.setAddress("http://60.213.49.66:8081/FPGLXT_FW/CXF/WbService?wsdl");
				
		WbServiceI service = (WbServiceI) factory.create();
		
		SAXBuilder reader = new SAXBuilder(); 
		String xml ="<?xml version=\"1.0\" encoding=\"GBK\" ?>"+
					"<InfoMaster>"+
					"<Number>1111</Number>"+
					"<BusNo>1</BusNo>"+
					"<Organ>00</Organ>"+
					"<ClientName></ClientName>"+
					"<ClientTaxCode>370103999999030</ClientTaxCode>"+
					"<ClientBankAccount>中国银行123</ClientBankAccount>"+
					"<ClientAddressPhone>地址电话</ClientAddressPhone>"+
					"<ClientPhone></ClientPhone>"+
					"<ClientMail></ClientMail>"+
					"<BillType>1</BillType>"+
					"<InvoiceCode></InvoiceCode>"+
					"<InvoiceNo></InvoiceNo>"+
					"<Invoicer></Invoicer>"+
					"<Checker></Checker>"+
					"<Cashier></Cashier>"+
					"<Notes>备注</Notes>"+
					"<SumMoney>2341</SumMoney>"+
					"<AllMoney>1</AllMoney>"+
					"<InfoDetail>"+
					"<GoodsGroup>水果</GoodsGroup>"+
					"<GoodsName>测试商品1</GoodsName>"+
					"<Standard></Standard>"+
					"<Unit></Unit>"+
					"<Num>2</Num>"+
					"<Price>117</Price>"+
					"<Amount>234</Amount>"+
					"<TaxAmount></TaxAmount>"+
					"<TaxRate>0.17</TaxRate>"+
					"<Aigo>117</Aigo>"+
					"<AigoTax></AigoTax>"+
					"<GoodsNoVer>27.0</GoodsNoVer>"+
					"<GoodsTaxNo>1010102020000000000</GoodsTaxNo>"+
					"<GoodsTaxName>谷物</GoodsTaxName>"+
					"<TaxPre>0</TaxPre>"+
					"<TaxPreCon></TaxPreCon>"+
					"<ZeroTax></ZeroTax>"+
					"</InfoDetail>"+
					"</InfoMaster>";
		Reader returnQuote = new StringReader(xml);
		Document document=reader.build(returnQuote);
		Format format =Format.getPrettyFormat();      
		format.setEncoding("UTF-8");//设置编码格式   
       
		StringWriter out=null; //输出对象  
		String sReturn =""; //输出字符串  
		XMLOutputter outputter =new XMLOutputter();   
		out=new StringWriter();  
		outputter.output(document,out);
		sReturn=out.toString();
		System.out.println(service.sendData(xml));
	}

	
	public void test(){
		RtnData rtn = new RtnData();
		//String sa ="http://10.173.3.77:18089/e-invoice-pro/rest/que/getInvoice_sale.action?password=111&data=010620160901P619110";
		service.sendData("", rtn);
	}
	
	
	//发票查询
	public void findInvoiceBean(RtnData rtn){
		Invque invque = new Invque();
		InvoiceSaleHead saleshead = new InvoiceSaleHead();
		invque.setIqseqno("20170918001");
		invque.setRtfpdm("1234567");
		invque.setRtfphm("bbbbb");
		
		saleshead.setTradedate("20170914");
		saleshead.setShopid("0001");
		saleshead.setSyjid("S001");
		saleshead.setBillno("110");
		
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setTaxno("91500000747150426A");
		
		service.findInvoiceBean(invque,taxinfo, saleshead, rtn);
	}
	
	
	//发票返回
	public void rtInvoice(RtnData rtn){
		String aa = "<RESPONSE_FPFH class=\"RESPONSE_FPFH\"> "+
					" <RETURNCODE>0000</RETURNCODE>"+
					"<RETURNMESSAGE>返回描述</RETURNMESSAGE>"+
					"<FPQQLSH>hbhxhbhx1707281449072</FPQQLSH>"+
					"<HJBHSJE>18.03</HJBHSJE>"+
					"<HJSE>3.07</HJSE>"+
					"<KPRQ>20170904104315</KPRQ>"+
					"<FP_DM>150003533340</FP_DM>"+
					"<FP_HM>35681569</FP_HM>"+
					"<XHQDBZ>1</XHQDBZ>"+
					"<STATUS>1</STATUS>  "+
					"<PDF_URL>http://ei-test.51fapiao.cn:9080/FPFX/actions/da3898f4b6e920e0e2aa0524a14a441a7f9192</PDF_URL>"+
					"</RESPONSE_FPFH>";
		
		String bb = "<RESPONSE_FPFH class=\"RESPONSE_FPFH\">"+
					"<RETURNCODE>返回代码</RETURNCODE>"+
					"<RETURNMESSAGE>返回描述</RETURNMESSAGE>"+
					"<FPQQLSH>S123456789011</FPQQLSH>"+
					"<GHFMC>购货方名称</GHFMC>"+
					"<GHF_NSRSBH>购货方识别号</GHF_NSRSBH>"+
					"<FKFKHYH_FKFYHZH>付款方银行及账号</FKFKHYH_FKFYHZH>"+
					"<FKFDZ_FKFDH>付款方地址电话</FKFDZ_FKFDH>"+
					"<XHFKHYH_SKFYHZH>销货方银行及账号</XHFKHYH_SKFYHZH>"+
					"<XHFDZ_XHFDH>销货方地址电话</XHFDZ_XHFDH>"+
					"<FPZL_DM>发票种类代码</FPZL_DM>"+
					"<YFP_DM>原发票代码</YFP_DM>"+
					"<YFP_HM>原发票号码</YFP_HM>"+
					"<YWLX>业务类型</YWLX>"+
					"<SHOPID>门店ID </SHOPID>"+
					"<XPJYSJ>小票交易时间 </XPJYSJ>"+
					"<EMAIL>购买方邮箱</EMAIL>"+
					"<BZ>备注</BZ> "+
					"<USERID>登陆人员名称</USERID>"+
					"<KPY>开票人</KPY>"+
					"<FHR>复核人</FHR>"+
					"<SKY>收款人</SKY>"+
					"<KPLX>1</KPLX>"+
					"<HJJE>0</HJJE>"+
					"<HJSE>0</HJSE>"+
					"<BMB_BBH>编码表版本号</BMB_BBH>"+
					"<KPRQ>开票日期</KPRQ>"+
					"<FP_DM>发票号码</FP_DM>"+
					"<FP_HM>发票号码</FP_HM>"+
					"<XHQDBZ>销货清单标志</XHQDBZ>"+
					"<STATUS>3</STATUS>"+  
					"<PDF_URL>PDF下载路径</PDF_URL>"+
					"</RESPONSE_FPFH>";
		//service.rtOpenToBean(bb, rtn);
		service.sendData(bb,rtn);
	}
	
	/**
	 * 开具发票
	 * **/
	public void openinvoice(RtnData rtn){
		

		//HashMap map = new HashMap();
		Invque invque = new Invque();
		
		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		InvoiceSaleDetail detail = new InvoiceSaleDetail();
		InvoiceSaleDetail detail1 = new InvoiceSaleDetail();
		ResponseBillInfo saleshead = new ResponseBillInfo();
		
		saleshead.setTotalamount(-2.00);
		saleshead.setTradedate("20170914");
		saleshead.setShopid("0001");
		saleshead.setSyjid("S001");
		saleshead.setBillno("110");
		saleshead.setSheettype("1");
		
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setItfeqpttype("0");
		taxinfo.setItfskpbh("661565676559");
		//taxinfo.setItfskpkl("88888888");
		//taxinfo.setItfkeypwd("12345678");
		taxinfo.setTaxadd("石家庄");
		taxinfo.setTaxbank("建行  3334455666");
		taxinfo.setItfbbh("12.0");
		//taxinfo.setItfkpd("kpyuan002");
		taxinfo.setTaxno("91500000747150426A");

/*		detail.setGoodsid("1060301020100000000");
		detail.setGoodsname("测试中类税目");
		detail.setTaxitemid("1060301020100000000");
		detail.setAmount(19.74);
		detail.setTaxfee(3.36);
		detail.setTaxrate(0.17);
		detail.setIsinvoice("Y");
		detail.setTaxpre("0");
		detail.setOldamt(23.10);

		detail1.setGoodsid("1060301020100000000");
		detail1.setGoodsname("测试中类税目");
		detail1.setTaxitemid("1060301020100000000");
		detail1.setAmount(100.00);
		detail1.setTaxfee(17.00);
		detail1.setTaxrate(0.17);
		detail1.setIsinvoice("Y");
		detail1.setTaxpre("0");
		detail1.setOldamt(117.00);*/
		
		detail1.setGoodsid("1060301020100000000");
		detail1.setGoodsname("测试中类税目");
		detail1.setTaxitemid("1060301020100000000");
		
		detail1.setAmount(-1.71);
		detail1.setTaxfee(-0.29);
		detail1.setTaxrate(0.17);
		detail1.setIsinvoice("Y");
		detail1.setOldamt(-2.00);
		detail1.setTaxpre("0");
		
		invque.setIqseqno("hbhxhbhx1707281449072");
		//invque.setIqtaxzdh("kpyuan002");
		invque.setIqtotje(-1.71);
		invque.setIqtotse(-0.29);
		invque.setIqgmfname("北京富基融通科技有限公司");
		invque.setIqgmftax("91110108718774697G");
		invque.setIqgmfadd("北京市海淀区中关村南大街8号甲8号乙8号威地科技大厦805室51650988");
		invque.setIqgmfbank("招商银行北京分行东方广场支行862580489510001");
		invque.setIqadmin("张三");
		invque.setIqyfpdm("150003521055");
		invque.setIqyfphm("82520088");
		invque.setIqtype(1);
		invque.setZsfs("0");
		invque.setIqfplxdm("026");
		invque.setIqchannel("mp");
		invque.setIqshop("01");
		invque.setIqemail("11223344@qq.com");
		invque.setIqperson("112233");


		//detailList.add(detail);
		detailList.add(detail1);
		saleshead.setInvoiceSaleDetail(detailList);
		//map.put("invque", invque);
		//map.put("taxinfo", taxinfo);
		//map.put("detailList", detailList);
		
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.openinvoice(invque,taxinfo,saleshead, rtn);
	}
	
	//开票提交单据返回结果转换为BEAN
	public YuanDeRtInvoiceBean rtOpenSubmitToBean(RtnData rtn){
		YuanDeGenerateBean  gen= new YuanDeGenerateBean("11");
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><RESPONSE_FPFH class=\"RESPONSE_FPFH\"><RETURNCODE>0000</RETURNCODE><RETURNMESSAGE><![CDATA[保存成功]]></RETURNMESSAGE><FPQQLSH>Q01805292244381000</FPQQLSH><HJBHSJE></HJBHSJE><HJSE></HJSE><KPRQ></KPRQ><FP_DM></FP_DM><FP_HM></FP_HM><XHQDBZ></XHQDBZ><STATUS>1</STATUS><PDF_URL></PDF_URL></RESPONSE_FPFH>";
		
		return gen.rtOpenSubmitToBean(xml,rtn);
	}
	
}
