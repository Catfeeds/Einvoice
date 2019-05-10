package baiwang.invoice.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MyAES;

import baiwang.invoice.bean.BaiWangRtInvoiceDetailBean;
import baiwang.invoice.bean.BaiWangRtInvoiceHeadBean;
import baiwang.invoice.bean.FpttBean;
import baiwang.invoice.bean.RtInvoiceHeadList;

public class Test {

	String nsrsbh = "91500000747150426A";
	String jrdm  ="aabbccddeeff";
    String serviceUrl  ="http://36.110.112.203:8031/api/service/bwapi";
    
    
    BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
    
	public static void main(String[] args) {
		
		//没有分隔符号则读取配置解析 S=门店 Y=收银机 Q=流水 M=金额
/*		String  str = "S4Y4Q8M";
		String firstKey = str.substring(0, 1);
		String firstKeyLength = str.substring(1, 2);
		String secondKey = str.substring(0, 1);
		String secondKeyLength = str.substring(1, 2);
		String thirdKey = str.substring(0, 1);
		String thirdKeyLength = str.substring(1, 2);*/
		
		
		Test test = new Test();
		//BaiWangServiceImpl service = new BaiWangServiceImpl(test.serviceUrl,test.nsrsbh,test.jrdm);
		
		
		RtnData rtn = new RtnData();
		//测试开票
		//test.openinvoice(rtn);
		
		//测试开票返回
		//test.testrtToBean(rtn);
		
		//测试PDF
		//test.testPdf(rtn);
		
		//test.getsearchPdf(rtn);
		
		//测试空白票查询
		//test.testblankInvoice(rtn);
		
		//测试发票作废
		//test.testInvoiceInValid(rtn);
		
		//测试发票打印
		//test.invoicePrint(rtn);
		
		//发票查询
		//	test.findInvoice(rtn);
		
/*		 if(rtn.getCode()==0){
			 service.login(rtn.getMessage(),rtn);
		 }*/
		
		//测试PDF返回值
		//test.rtPdfToString(rtn);
		
		//测试获取发票抬头
		//test.getFptt(rtn);
		 
		//System.out.println(rtn.getCode()+" "+rtn.getMessage());
		//test.xmlToBean();
		//test.bb();
		test.connentServlet(rtn);
	}
	
	public void connentServlet(RtnData rtn){
	 //http://baiwangjinshui.tpddns.cn:8443/SKIServlet/SKDo  ?
	//帐号kp 密码1-6
		String xml = "";//"<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n<business id=\"20001\" comment=\"参数设置\">\r\n<body yylxdm=\"1\">\r\n<servletip>1.202.49.146</servletip>\r\n<servletport>8443</servletport>\r\n<keypwd>88888888</keypwd>\r\n</body>\r\n</business>";
		        xml ="<?xml version=\"1.0\" encoding=\"gbk\"?>\r\n<business id=\"20001\" comment=\"参数设置\">\r\n<body yylxdm=\"1\">\r\n<servletip>124.127.152.197</servletip>\r\n<servletport>8443</servletport>\r\n<keypwd>88888888</keypwd>\r\n</body>\r\n</business>";
		       // xml ="<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"20002\" comment=\"税控钥匙信息查询\"><body yylxdm=\"1\"><keypwd>88888888</keypwd></body></business>";
		       xml="<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"10004\" comment=\"查询当前未开票号\"><body yylxdm=\"1\"><kpzdbs>kp</kpzdbs><fplxdm>007</fplxdm></body></business>";
		        String resultData = "";
		
			OutputStream wr = null;
			HttpURLConnection conn = null;
			try {
				URL url = new URL("http://124.127.152.197:8443/SKServer/SKDo");
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(6000);// 设置连接主机的超时时间
				conn.setReadTimeout(6000);// 设置从主机读取数据的超时时间

				wr = conn.getOutputStream();
				wr.write(xml.getBytes("GBK"));
				wr.flush();
				resultData = IOUtils.toString(conn.getInputStream(), "GBK");
				//rtn.setMessage(new String (MyAES.decryptBASE64(resultData),"GBK") );
				rtn.setMessage(resultData);
				System.out.println("Message  "+rtn.getMessage());
			} catch (MalformedURLException e) {
				e.printStackTrace();
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				System.out.println("http请求失败！请求地址不正确！请求地址：");
			} catch (IOException e) {
				e.printStackTrace();
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				System.out.println("http请求失败！发生i/o错误，请求地址：" );
			} catch (Exception e) {
				e.printStackTrace();
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				System.out.println("访问百望九赋异常：" );
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
			 
/*			try{
			URL url = new URL("http://baiwangjinshui.tpddns.cn:8443/SKServer/SKDo");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//POST请求
			conn.setRequestMethod("POST");
			OutputStream wr = conn.getOutputStream();
			//读字节流
			
			byte[] content = xml.getBytes("GBK");//  IOUtils.toByteArray(new FileInputStream("D:\\ski\\007.xml"));
			wr.write(content);
			wr.flush();
			System.out.println("result = "+ IOUtils.toString(conn.getInputStream(), "GBK"));
			} catch (Exception e) {
			e.printStackTrace();
			}*/
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
	
	public void xmlToBean(){
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><fplist><fp><fpqqlsh>123456712343456666</fpqqlsh><eqptType>0</eqptType><fplxdm>026</fplxdm><fpdm>150003521055</fpdm><fphm>82520088</fphm><kprq>2017-07-18 16:53:36</kprq><skm>0335/62&gt;3204-*-6*34+06-*161*7312-9&gt;&lt;11&gt;370&gt;6&gt;28*&gt;7-343&gt;12-81/72/-/*/7+895-&lt;6&lt;6662+7&gt;46*0//+277010+0-192+*7+7&lt;+&lt;3</skm><ghdwmc>北京富基融通科技有限公司</ghdwmc><ghdwdm>91110108718774697G</ghdwdm><ghdwdzdh>北京市海淀区中关村南大街8号甲8号乙8号威地科技大厦805室51650988</ghdwdzdh><ghdwyhzh>招商银行北京分行东方广场支行862580489510001</ghdwyhzh><xhdwmc>百望云平台6</xhdwmc><xhdwdm>91500000747150426A</xhdwdm><xhdwdzdh>北京18893796531</xhdwdzdh><xhdwyhzh>北京银行333333123456783339789456123</xhdwyhzh><hjje>100.00</hjje><se>17.00</se><jshj>117.00</jshj><bz></bz><skr></skr><fhr></fhr><kpr>张三</kpr><wspzhm></wspzhm><tzdh></tzdh><kpjh></kpjh><qdbz>0</qdbz><kplx>0</kplx><zfrq></zfrq><zfr></zfr><yfpdm></yfpdm><yfphm></yfphm><scbz>0</scbz><qmbz>N</qmbz><yqbz>0</yqbz><bbh></bbh><jym>15758476878284903093</jym><zfrdm></zfrdm><kpddm>kpyuan002</kpddm><tspz>00</tspz><jqbh>499111006380</jqbh><fpzt>00</fpzt><zfyy></zfyy><hzxxb></hzxxb><qmz></qmz><qmcs></qmcs><zzBm>1700000001023</zzBm><zhBm>1700000001016</zhBm><zsfs>0</zsfs><kce></kce><ewm></ewm><czbz></czbz><businessId></businessId><gfkhyx></gfkhyx><gfkhdh></gfkhdh><ssyf></ssyf><ssnf></ssnf><pdfurl></pdfurl><by1></by1><by2></by2><fpmxList><fpmx><fpmxxh>1</fpmxxh><fphxz>0</fphxz><spbm>1060301020100000000</spbm><zxbm></zxbm><spmc>测试中类税目</spmc><spsm>1060301020100000000</spsm><ggxh></ggxh><dw></dw><spsl></spsl><spdj></spdj><je>100.000000</je><sl>0.170000</sl><se>17.000000</se><hsbz>0</hsbz><yhzcbs>0</yhzcbs><lslbs></lslbs><zzstsgl></zzstsgl><zhdyhh> </zhdyhh></fpmx></fpmxList></fp></fplist>";
		try{
       JAXBContext jc = JAXBContext.newInstance( RtInvoiceHeadList.class);
       Unmarshaller u = jc.createUnmarshaller();
       StringBuffer xmlStr = new StringBuffer(xml);
       RtInvoiceHeadList o = (RtInvoiceHeadList) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
       
       for(BaiWangRtInvoiceHeadBean head:o.getHeads()){
    	   System.out.println(head.getFpqqlsh());
    	   System.out.println(head.getFpdm());
    	   System.out.println(head.getFphm());
    	   for(BaiWangRtInvoiceDetailBean detail : head.getFpmxList().getDetails()){
    		   System.out.println(detail.getSe());
    		   System.out.println(detail.getSl());
    		   System.out.println(detail.getSpmc());
    		   System.out.println(detail.getSpsm());
    	   }
       }
       
		}catch (Exception e){
			
		}
	}
	
	/**
	 * 发票查询
	 * **/
	public void findInvoice(RtnData rtn){
		Invque invque = new Invque(); 
		invque.setIqseqno("Q0180712153535e657c");
		invque.setRtfpdm("150003521055");
		invque.setRtfphm("82530906");
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		//RtInvoiceHeadList list = new RtInvoiceHeadList();
		RtInvoiceHeadList list =  service.findInvoice(invque, rtn);
		System.out.println(list.getHeads().get(0).getFphm());
	}
	
	
	/**
	 * 空白发票查询
	 * **/
	public void testblankInvoice(RtnData rtn){
		
		//HashMap map = new HashMap();
		Invque invque = new Invque();
		Taxinfo taxinfo = new Taxinfo();
		
		taxinfo.setItfeqpttype("0");
		taxinfo.setTaxno("91321000711552055T");
		//taxinfo.setItfkpd("kpyuan03");
		invque.setIqtaxzdh("TJ0025");
		invque.setIqfplxdm("007");
		
		//taxinfo.setItfskpbh("499000150629");
		//taxinfo.setItfskpkl("88888888");
		//taxinfo.setItfkeypwd("12345678");
		
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.blankInvoice(invque,taxinfo,rtn);
		
	}
	
	
	/**
	 * 发票作废
	 * **/
	public void testInvoiceInValid(RtnData rtn){
		
		//HashMap map = new HashMap();
		Invque invque = new Invque();
		Taxinfo taxinfo = new Taxinfo();
		
		taxinfo.setItfeqpttype("0");
		taxinfo.setTaxno("91321000711552055T1");
		//taxinfo.setItfkpd("kpyuan03");
		
		invque.setIqtaxzdh("TJ0020");
		invque.setIqfplxdm("007");
		invque.setRtfpdm("3200174320");
		invque.setRtfphm("16044094");
		invque.setIqadmin("tj0020");
		
	//	taxinfo.setItfskpbh("499111006427");
		//taxinfo.setItfskpkl("88888888");
		//taxinfo.setItfkeypwd("12345678");
		
		//map.put("invque", invque);
		//map.put("taxinfo", taxinfo);
		
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.invoiceYkInValid(invque,taxinfo,rtn);
		
	}
	
	/**
	 * 发票打印
	 * **/
	public void invoicePrint(RtnData rtn){
		
		//HashMap map = new HashMap();
		Invque invque = new Invque();
		Taxinfo taxinfo = new Taxinfo();
		
		taxinfo.setItfeqpttype("0");
		invque.setIqseqno("12345671234345666641");
		invque.setIqtaxzdh("kpyuan03");
		taxinfo.setItfskpbh("499111006427");
		taxinfo.setItfskpkl("88888888");
		taxinfo.setItfkeypwd("12345678");
		invque.setIqfplxdm("007");
		invque.setRtfpdm("1110000478");
		invque.setRtfphm("82460616");
		invque.setIsList(0);

		service.invoicePrint(invque,taxinfo,rtn);
		
	}
	
	/**
	 * 开具发票返回数据转换为Bean
	 * **/
	public void testrtToBean(RtnData rtn){
		
		String xml1 = "<result>"+
			    "<requestId>3f45715656a64903b0eb9ab4bc905928</requestId>"+
			    "<rtncode>0000</rtncode>"+
			    "<rtndata><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><returndata><fpdm>150003521055</fpdm><fphm>82520284</fphm><kprq>20170719101037</kprq><skm>035-0+34////+*13-1**5-*++7*&lt;/2&lt;3+/85070/*-&lt;64&lt;362&gt;-88056669+*560605393*62*&lt;-03506-+&lt;6-79/61&lt;&lt;3011+0-1968&gt;*&gt;521-/</skm><jym>12564314311114028121</jym><ewm>null</ewm></returndata>]]></rtndata>"+
			    "<rtnmsg>调用[发票开具接口]成功,</rtnmsg>"+
			    "</result>";
		BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		bw.rtOpenToBean(xml1,rtn);
		
	}
	/**
	 * PDF返回URL转换为字符串
	 * **/
	public void rtPdfToString(RtnData rtn){
		
		String xml1 = "<result>"+
				"<requestId>58108377e0da43d889586c0fa7a725e0</requestId>"+
				"<rtncode>0</rtncode>"+
				"<rtndata><![CDATA[http://36.110.112.203:8095/fp/d?d=596f2452fb5aae7375f378d4]]></rtndata>"+
				"<rtnmsg>当前发票已生成版式文件。 短信稍后发送,请注意查收!</rtnmsg>"+
				"</result>";
		BaiWangGenerateBean bw = new BaiWangGenerateBean(nsrsbh,jrdm);
		
		System.out.println(bw.rtPdfToString(xml1,rtn));
		
	}
	
	/**
	 * 发票PDF格式查询
	 * **/
	public void testPdf(RtnData rtn){
		Invque invque = new Invque();
		invque.setIqseqno("Q01712141600461000");
		invque.setRtfpdm("150003521055");
		invque.setRtfphm("82581379");
		invque.setIqtel("13722890816");
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		String er =  service.getPdf(invque, rtn);
		System.out.println(er);
	}
	
	public void getsearchPdf(RtnData rtn){
		Invque invque = new Invque();
		invque.setIqseqno("12345671234345666657");
		invque.setRtfpdm("150003521055");
		invque.setRtfphm("82530179");
	//	invque.setIqtel("13722890816");
		
		System.out.println(service.getsearchPdf(invque,"0","2",rtn));
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
		InvoiceSaleDetail detail2 = new InvoiceSaleDetail();
		InvoiceSaleDetail detail3 = new InvoiceSaleDetail();
		detailList.add(detail);
		detailList.add(detail1);
		detailList.add(detail2);
		detailList.add(detail3);
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setItfeqpttype("1");
		taxinfo.setItfskpbh("539909051731");
		taxinfo.setItfskpkl("88888888");
		taxinfo.setItfkeypwd("12345678");
		taxinfo.setItfbbh("13.0");
		taxinfo.setItfkpd("QWATP");
		
		detail.setGoodsid("1060301020100000000");
		detail.setGoodsname("测试中类税目");
		detail.setTaxitemid("1060301020100000000");
		detail.setAmount(328.21);
		detail.setTaxfee(55.79);
		detail.setTaxrate(0.17);
		detail.setIsinvoice("Y");
		detail.setTaxpre("0");
		detail.setRowno(3);
		detail.setFphxz("2");
		detail.setZhdyhh(4);
		detail.setQty(1.0);
		detail.setPrice(328.21);
		detail.setSeqno("1");

		detail1.setGoodsid("1060301020100000000");
		detail1.setGoodsname("测试中类税目");
		detail1.setTaxitemid("1060301020100000000");
		detail1.setAmount(-251.31);
		detail1.setTaxfee(-42.72);
		detail1.setTaxrate(0.17);
		detail1.setIsinvoice("Y");
		detail1.setTaxpre("0");
		detail1.setRowno(4);
		detail1.setFphxz("1");
		detail1.setZhdyhh(3);
		detail1.setSeqno("2");
		
		detail2.setGoodsid("1060301020100000000");
		detail2.setGoodsname("测试中类税目1");
		detail2.setTaxitemid("1060301020100000000");
		detail2.setAmount(201.71);
		detail2.setTaxfee(34.29);
		detail2.setTaxrate(0.17);
		detail2.setIsinvoice("Y");
		detail2.setTaxpre("0");
		detail2.setRowno(3);
		detail2.setFphxz("2");
		detail2.setZhdyhh(4);	
		detail2.setQty(1.0);
		detail2.setPrice(201.71);
		detail2.setSeqno("3");

		detail3.setGoodsid("1060301020100000000");
		detail3.setGoodsname("测试中类税目1");
		detail3.setTaxitemid("1060301020100000000");
		detail3.setAmount(-158.97);
		detail3.setTaxfee(-27.03);
		detail3.setTaxrate(0.17);
		detail3.setIsinvoice("Y");
		detail3.setTaxpre("0");
		detail3.setRowno(4);
		detail3.setFphxz("1");
		detail3.setZhdyhh(3);		
		detail3.setSeqno("4");
		
		invque.setIqseqno("12345671234345666657");
		invque.setIqtaxzdh("QWATP");//纸质 kpyuan03  电子 kpyuan002  卷票dzp
		invque.setIqtotje(119.64);
		invque.setIqtotse(20.33);
		invque.setIqgmfname("北京富基融通科技有限公司");
		invque.setIqgmftax("91110108718774697G");
		invque.setIqgmfadd("北京市海淀区中关村南大街8号甲8号乙8号威地科技大厦805室51650988");
		invque.setIqgmfbank("招商银行北京分行东方广场支行862580489510001");
		invque.setIqadmin("张三");
		invque.setIqyfpdm("");
		invque.setIqyfphm("");
		invque.setIqtype(0);
		invque.setZsfs("0");
		invque.setIqfplxdm("026");
		invque.setIqemail("157432381@qq.com");
		
		//map.put("invque", invque);
		//map.put("taxinfo", taxinfo);
		//map.put("detailList", detailList);
		
		//BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.openinvoice(invque,taxinfo,detailList, rtn);
	}
	
	
}
