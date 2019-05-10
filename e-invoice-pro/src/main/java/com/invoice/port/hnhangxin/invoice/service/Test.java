package com.invoice.port.hnhangxin.invoice.service;
 

import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinRtInvoiceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.MyAES;

/**
 * Hello world!
 */
//使用时地址要切换为可访问地址
public class Test {
    public static void main(String[] args) throws Exception {
    	RtnData rtn= new RtnData();
    	Test test = new Test();
    	//test.rtOpenToBean(rtn);
    	
    	//test.hangXinBeanToXml(rtn);
    	//推送
    	//test.tuisongTest(rtn);
    	//test.findInvoice(rtn);
    	System.out.println(rtn.getMessage());
    }
    
   
    
    
    private void tuisongTest( RtnData rtn){
    	String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"+
    				"<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"DZFP1.0\" >"+
					"<globalInfo>"+
						"<terminalCode>0</terminalCode>"+
						"<appId>ZZS_PT_DZFP</appId>"+
						"<version>1.8</version>"+
						"<interfaceCode>ECXML.FPKJJG.TS.E_INV_PLATFORM</interfaceCode>"+
						"<userName>111BAWTP</userName>"+
						"<passWord>3721590846NzU3ZDIxYzg4MmM1N2Y4ZTQ3ZjhjODcwZDgzNTFlY2YzOWFhNDcwZg==</passWord>"+
						"<taxpayerId>500102666666222</taxpayerId>"+
						"<authorizationCode>5066666222</authorizationCode>"+
						"<requestCode>111BAWTP</requestCode>"+
						"<requestTime>2018-07-31 10:51:27 329 </requestTime>"+
						"<responseCode/>"+
						"<dataExchangeId>Q0180731105119a9fa8</dataExchangeId>"+
					"</globalInfo>"+
					"<returnStateInfo>"+
						"<returnCode/>"+
						"<returnMessage/>"+
					"</returnStateInfo>"+
					"<Data>"+
						"<dataDescription>"+
							"<zipCode>0</zipCode>"+
							"<encryptCode>0</encryptCode>"+
							"<codeType>BASE64</codeType>"+
						"</dataDescription>	<content>PFJFUVVFU1RfQ09NTU9OX0ZQVFMgY2xhc3M9IlJFUVVFU1RfQ09NTU9OX0ZQVFMiPjxUU0ZTPjA8L1RTRlM+PFNKPjwvU0o+PEVNQUlMPjE1NzQzMjM4MUBxcS5jb208L0VNQUlMPjxGUFFRTFNIPlEwMTgwNzMxMTA1MTE5YTlmYTg8L0ZQUVFMU0g+PE5TUlNCSD41MDAxMDI2NjY2NjYyMjI8L05TUlNCSD48RlBfRE0+MTUwMDAzNTI4ODg4PC9GUF9ETT48RlBfSE0+NTk4MDQ5OTE8L0ZQX0hNPjxLUF9KRT4xNi4xPC9LUF9KRT48S1BfU0U+Mi4wODwvS1BfU0U+PFBERl9QQVRIPmh0dHBzOi8vdGVzdGh0dHBzLjUxZmFwaWFvLmNuOjgxODEvRlBGWC9hY3Rpb25zLzlhNGFhYzA5OTAwZmMyY2U1MDlmNTY0ZDExMzFlODU1ODI1MTcxPC9QREZfUEFUSD48S1BSUT4yMDE4LTA3LTMxIDEwOjUyOjMyPC9LUFJRPjxYRk1DPueUqOaItzI8L1hGTUM+PEdGTUM+5Liq5Lq6PC9HRk1DPjwvUkVRVUVTVF9DT01NT05fRlBUUz4=</content>"+
					"</Data>"+
				"</interface>";
    	String resultData="";
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	
		try {
			paramMap.put("requestXml", new String(Base64.encodeBase64(xml.getBytes("utf-8")),"utf-8"));
			 resultData = HttpClientCommon.doPost(paramMap, null, "http://dzfp.hnhtxx.cn/msgPush", 6000, 6000, "utf-8");
			 System.out.println(resultData);
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
			System.out.println("访问百望九赋异常：");
		}	
    }
    
    private void rtOpenToBean( RtnData rtn){
    	String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
					"<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"WLFP1.0\">"+
					 "<globalInfo>"+
					  "<terminalCode>0</terminalCode>"+
					  "<appId>DZFP</appId>"+
					  "<version>1.0</version>"+
					  "<interfaceCode>ECXML.FPKJJG.TS.E_INV</interfaceCode>"+
					  "<userName>111BAWTP</userName>"+
					  "<passWord>1018027655vrNP9pA6P7i0C5/jropqrw==</passWord>"+
					  "<taxpayerId>500102666666222</taxpayerId>"+
					  "<dataExchangeId />"+
					  "<authorizationCode />"+
					  "<requestCode>111BAWTP</requestCode>"+
					  "<requestTime>2018-07-25 09:22:43:216</requestTime>"+
					  "<responseCode />"+
					 "</globalInfo>"+
					 "<returnStateInfo>"+
					 "<returnCode>0000</returnCode>"+
					 "<returnMessage>5Lia5Yqh5aSE55CG5oiQ5Yqf</returnMessage>"+
					 "</returnStateInfo>"+
					 "<Data>"+
					 "<dataDescription>"+
					 "<zipCode>1</zipCode>"+
					   "<encryptCode>0</encryptCode>"+
					   "<codeType>BASE64</codeType>"+
					  "</dataDescription>"+
					  "<content>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</content>"+
					 "</Data>"+
					"</interface>";
    	
    	HnHangXinGenerateBean hx = new HnHangXinGenerateBean("A00001002");
    			hx.rtOpenToBean(xml,rtn);
    }
    
    private void hangXinBeanToXml( RtnData rtn){
    	String 	encryptTxt="	<REQUEST_FPKJXX class=\"REQUEST_FPKJXX\">"+
				"<FPKJXX_FPTXX class=\"FPKJXX_FPTXX\">"+
				"<FPQQLSH>123456789012345678901234567111</FPQQLSH>"+
				"<DSPTBM>111G1GQF</DSPTBM>"+
				"<NSRSBH>150001205510289384</NSRSBH>"+
				"<NSRMC>150001205510289384</NSRMC>"+
				"<NSRDZDAH></NSRDZDAH>"+
				"<SWJG_DM></SWJG_DM>"+
				"<DKBZ>0</DKBZ>"+
				"<SGBZ></SGBZ>"+
				"<PYDM></PYDM>"+
				"<KPXM>纸制品</KPXM>"+
				"<BMB_BBH>13.0</BMB_BBH>"+
				"<XHF_NSRSBH>150001205510289384</XHF_NSRSBH>"+
				"<XHFMC>150001205510289384</XHFMC>"+
				"<XHF_DZ>1</XHF_DZ>"+
				"<XHF_DH>1</XHF_DH>"+
				"<XHF_YHZH>1</XHF_YHZH>"+
				"<GHFMC>个人</GHFMC>"+
				"<GHF_NSRSBH></GHF_NSRSBH>"+
				"<GHF_SF></GHF_SF>"+
				"<GHF_DZ>1</GHF_DZ>"+
				"<GHF_GDDH>1</GHF_GDDH>"+
				"<GHF_SJ>15200941833</GHF_SJ>"+
				"<GHF_EMAIL>1271472692@qq.com</GHF_EMAIL>"+
				"<GHFQYLX>01</GHFQYLX>"+
				"<GHF_YHZH>1</GHF_YHZH>"+
				"<HY_DM></HY_DM>"+
				"<HY_MC></HY_MC>"+
				"<KPY>1</KPY>"+
				"<SKY>1</SKY>"+
				"<FHR>1</FHR>"+
				"<KPRQ>2017-11-01 14:10:00</KPRQ>"+
				"<KPLX>1</KPLX>"+
				"<YFP_DM></YFP_DM>"+
				"<YFP_HM></YFP_HM>"+
				"<CZDM>10</CZDM>"+
				"<QD_BZ>0</QD_BZ>"+
				"<QDXMMC></QDXMMC>"+
				"<CHYY></CHYY>"+
				"<TSCHBZ>0</TSCHBZ>"+
				"<KPHJJE>1.17</KPHJJE>"+
				"<HJBHSJE>1</HJBHSJE>"+
				"<HJSE>0.17</HJSE>"+
				"<BZ></BZ>"+
				"<BYZD1></BYZD1>"+
				"<BYZD2></BYZD2>"+
				"<BYZD3></BYZD3>"+
				"<BYZD4></BYZD4>"+
				"<BYZD5></BYZD5>"+
				"</FPKJXX_FPTXX>"+
				"<FPKJXX_XMXXS class=\"FPKJXX_XMXX;\" size=\"1\">"+
				"<FPKJXX_XMXX>"+
				"<XMMC>纸制品</XMMC>"+
				"<XMDW></XMDW>"+
				"<GGXH></GGXH>"+
				"<XMSL></XMSL>"+
				"<HSBZ>1</HSBZ>"+
				"<XMDJ>0</XMDJ>"+
				"<FPHXZ>0</FPHXZ>"+
				"<SPBM>1060105020000000000</SPBM>"+
				"<ZXBM></ZXBM>"+
				"<YHZCBS>0</YHZCBS>"+
				"<LSLBS></LSLBS>"+
				"<ZZSTSGL></ZZSTSGL>"+
				"<KCE></KCE>"+
				"<XMJE>1.17</XMJE>"+
				"<SL>0.17</SL>"+
				"<SE>0.17</SE>"+
				"<BYZD1></BYZD1>"+
				"<BYZD2></BYZD2>"+
				"<BYZD3></BYZD3>"+
				"<BYZD4></BYZD4>"+
				"<BYZD5></BYZD5>"+
				"</FPKJXX_XMXX>"+
				"</FPKJXX_XMXXS>"+
				"<FPKJXX_DDXX class=\"FPKJXX_DDXX\" />"+
				"<FPKJXX_DDMXXXS class=\"FPKJXX_DDMXXX;\" size=\"0\" />"+
				"<FPKJXX_ZFXX class=\"FPKJXX_ZFXX\" />"+
				"<FPKJXX_WLXX class=\"FPKJXX_WLXX\" />"+
				"</REQUEST_FPKJXX>";
    	Taxinfo taxinfo = new Taxinfo();
    	taxinfo.setEntid("A00001002");
    	taxinfo.setTaxno("500102666666222");
    	taxinfo.setItfbbh("13.0");
    	taxinfo.setItfserviceUrl("http://hnspt.hnhtxx.cn:8080/t9088/spt_zzssync/webservice/eInvWS/?Wsdl");
    	taxinfo.setItfskpbh("111BAWTP");
    	taxinfo.setItfskpkl("43988592");
    	taxinfo.setItfkeypwd("5066666222");
    	HnHangXinGenerateBean hx = new HnHangXinGenerateBean("A00001002");
    	hx.hangXinBeanToXml(encryptTxt, taxinfo, "ECXML.FPKJ.BC.E_INV","0", rtn);
    }
    
  /*  private void connect()  throws Exception{
    	
//      String innerXml = "内部xml";
//      innerXml = Base64Helper.encode(innerXml);
//      //内层xml放入外层content中
//      String outXml = "<content>" + innerXml + "</content>";

      String outXml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
              "<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
              "xsi:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"DZFP1.0\" >\n" +
              "\t<globalInfo>\n" +
              "\t\t<terminalCode>0</terminalCode>\n" +
              "\t\t<appId>ZZS_PT_DZFP</appId>\n" +
              "\t\t<version>1.8</version>\n" +
              "\t\t<interfaceCode>ECXML.FPKJ.BC.E_INV</interfaceCode>\n" +
              "\t\t<userName>111G1GQF</userName>\n" +
              "\t\t<passWord>1234567890YTdjNTIxZTZjYTdiYzFhZmJhY2Y0MWM1NDdhYmFjNTU=</passWord>\n" +
              "\t\t<taxpayerId>150001205510289384</taxpayerId>\n" +
              "\t\t<authorizationCode>1510289384</authorizationCode>\n" +
              "\t\t<requestCode>111G1GQF</requestCode>\n" +
              "\t\t<requestTime>2018-03-20 14:26:30</requestTime>\n" +
              "\t\t<responseCode/>\n" +
              "\t\t<dataExchangeId>111G1GQF20180321123456710</dataExchangeId>\n" +
              "\t</globalInfo>\n" +
              "\t<returnStateInfo>\n" +
              "\t\t<returnCode/>\n" +
              "\t\t<returnMessage/>\n" +
              "\t</returnStateInfo>\n" +
              "\t<Data>\n" +
              "\t\t<dataDescription>\n" +
              "\t\t\t<zipCode>0</zipCode>\n" +
              "\t\t\t<encryptCode>0</encryptCode>\n" +
              "\t\t\t<codeType>BASE64</codeType>\n" +
              "\t\t</dataDescription>\t<content>PFJFUVVFU1RfRlBLSlhYIGNsYXNzPSJSRVFVRVNUX0ZQS0pYWCI+CjxGUEtKWFhfRlBUWFggY2xhc3M9IkZQS0pYWF9GUFRYWCI+CjxGUFFRTFNIPjEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2NzExMTwvRlBRUUxTSD4KPERTUFRCTT4xMTFHMUdRRjwvRFNQVEJNPgo8TlNSU0JIPjE1MDAwMTIwNTUxMDI4OTM4NDwvTlNSU0JIPgo8TlNSTUM+MTUwMDAxMjA1NTEwMjg5Mzg0PC9OU1JNQz4KPE5TUkRaREFIPjwvTlNSRFpEQUg+CjxTV0pHX0RNPjwvU1dKR19ETT4KPERLQlo+MDwvREtCWj4KPFNHQlo+PC9TR0JaPgo8UFlETT48L1BZRE0+CjxLUFhNPue6uOWItuWTgTwvS1BYTT4KPEJNQl9CQkg+MTMuMDwvQk1CX0JCSD4KPFhIRl9OU1JTQkg+MTUwMDAxMjA1NTEwMjg5Mzg0PC9YSEZfTlNSU0JIPgo8WEhGTUM+MTUwMDAxMjA1NTEwMjg5Mzg0PC9YSEZNQz4KPFhIRl9EWj4xPC9YSEZfRFo+CjxYSEZfREg+MTwvWEhGX0RIPgo8WEhGX1lIWkg+MTwvWEhGX1lIWkg+CjxHSEZNQz7kuKrkuro8L0dIRk1DPgo8R0hGX05TUlNCSD48L0dIRl9OU1JTQkg+CjxHSEZfU0Y+PC9HSEZfU0Y+CjxHSEZfRFo+MTwvR0hGX0RaPgo8R0hGX0dEREg+MTwvR0hGX0dEREg+CjxHSEZfU0o+MTUyMDA5NDE4MzM8L0dIRl9TSj4KPEdIRl9FTUFJTD4xMjcxNDcyNjkyQHFxLmNvbTwvR0hGX0VNQUlMPgo8R0hGUVlMWD4wMTwvR0hGUVlMWD4KPEdIRl9ZSFpIPjE8L0dIRl9ZSFpIPgo8SFlfRE0+PC9IWV9ETT4KPEhZX01DPjwvSFlfTUM+CjxLUFk+MTwvS1BZPgo8U0tZPjE8L1NLWT4KPEZIUj4xPC9GSFI+CjxLUFJRPjIwMTctMTEtMDEgMTQ6MTA6MDA8L0tQUlE+CjxLUExYPjE8L0tQTFg+CjxZRlBfRE0+PC9ZRlBfRE0+CjxZRlBfSE0+PC9ZRlBfSE0+CjxDWkRNPjEwPC9DWkRNPgo8UURfQlo+MDwvUURfQlo+CjxRRFhNTUM+PC9RRFhNTUM+CjxDSFlZPjwvQ0hZWT4KPFRTQ0hCWj4wPC9UU0NIQlo+CjxLUEhKSkU+MS4xNzwvS1BISkpFPgo8SEpCSFNKRT4xPC9ISkJIU0pFPgo8SEpTRT4wLjE3PC9ISlNFPgo8Qlo+PC9CWj4KPEJZWkQxPjwvQllaRDE+CjxCWVpEMj48L0JZWkQyPgo8QllaRDM+PC9CWVpEMz4KPEJZWkQ0PjwvQllaRDQ+CjxCWVpENT48L0JZWkQ1Pgo8L0ZQS0pYWF9GUFRYWD4KPEZQS0pYWF9YTVhYUyBjbGFzcz0iRlBLSlhYX1hNWFg7IiBzaXplPSIxIj4KPEZQS0pYWF9YTVhYPgo8WE1NQz7nurjliLblk4E8L1hNTUM+CjxYTURXPjwvWE1EVz4KPEdHWEg+PC9HR1hIPgo8WE1TTD48L1hNU0w+CjxIU0JaPjE8L0hTQlo+CjxYTURKPjA8L1hNREo+CjxGUEhYWj4wPC9GUEhYWj4KPFNQQk0+MTA2MDEwNTAyMDAwMDAwMDAwMDwvU1BCTT4KPFpYQk0+PC9aWEJNPgo8WUhaQ0JTPjA8L1lIWkNCUz4KPExTTEJTPjwvTFNMQlM+CjxaWlNUU0dMPjwvWlpTVFNHTD4KPEtDRT48L0tDRT4KPFhNSkU+MS4xNzwvWE1KRT4KPFNMPjAuMTc8L1NMPgo8U0U+MC4xNzwvU0U+CjxCWVpEMT48L0JZWkQxPgo8QllaRDI+PC9CWVpEMj4KPEJZWkQzPjwvQllaRDM+CjxCWVpEND48L0JZWkQ0Pgo8QllaRDU+PC9CWVpENT4KPC9GUEtKWFhfWE1YWD4KPC9GUEtKWFhfWE1YWFM+CjxGUEtKWFhfRERYWCBjbGFzcz0iRlBLSlhYX0REWFgiIC8+CjxGUEtKWFhfRERNWFhYUyBjbGFzcz0iRlBLSlhYX0RETVhYWDsiIHNpemU9IjAiIC8+CjxGUEtKWFhfWkZYWCBjbGFzcz0iRlBLSlhYX1pGWFgiIC8+CjxGUEtKWFhfV0xYWCBjbGFzcz0iRlBLSlhYX1dMWFgiIC8+CjwvUkVRVUVTVF9GUEtKWFg+</content>\n" +
              "\t</Data>\n" +
              "</interface>";

      System.out.println(outXml);

//      boolean b = true;
//      if(b) {
//          return;
//      }

      //除开票可使用同步异步外，其他均使用异步地址进行请求访问
      //同步开票
//      String url = "http://192.168.2.22:9088/spt_zzssync/webservice/eInvWS/?Wsdl";
      String url = "http://hnspt.hnhtxx.cn:8080/t9088/spt_zzssync/webservice/eInvWS/?Wsdl";

      //异步开票
//      String url = "http://192.168.2.22:9083/spt_zzssl/webservice/eInvWS?wsdl";

      String method = "eiInterface";

      ServiceClient sc = new ServiceClient();
      Options opts = new Options();
      opts.setTimeOutInMilliSeconds(1000 * 60);
      EndpointReference end = new EndpointReference(url);
      opts.setTo(end);
      opts.setAction(method);
      sc.setOptions(opts);
      OMFactory fac = OMAbstractFactory.getOMFactory();
      OMNamespace omNs = fac.createOMNamespace("http://impl.ws.aisino.com/", "");
      OMElement method1 = fac.createOMElement("eiInterface", omNs);
      OMElement value = fac.createOMElement("requestMessage", omNs);
      value.setText(outXml);
      method1.addChild(value);
      OMElement res = sc.sendReceive(method1);
      String resXml = res.getFirstElement().getText();
      if(sc != null) {
          try {
              sc.cleanup();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      System.out.println("=====================================");
      System.out.println("resXml--" + resXml);

      String content = null;
      if(resXml.contains("<content>")){
          //有内层报文进行CA解密
          String secondXml=resXml.substring(resXml.indexOf("<content>")+"<content>".length(), resXml.indexOf("</content>"));
          //判断需不需要解压缩
          byte[] decodeData1 = null;
          byte[] secondXmlByte = null;
          //判断测试服务器返回内层报文是否进行了压缩：
          if("1".equals(resXml.substring(resXml.indexOf("<zipCode>") + "<zipCode>".length(), resXml.indexOf("</zipCode>")))){
              //解压前需先对内层报文进行解Base64操作
              secondXmlByte = GZipUtils.decompress(Base64.decodeBase64(secondXml));
          }else {
              //解密前需先对内层报文进行解Base64操作
              secondXmlByte = Base64.decodeBase64(secondXml);
          }
          //判断测试服务器返回是否进行了加密：
          if("2".equals(resXml.substring(resXml.indexOf("<encryptCode>")+"<encryptCode>".length(), resXml.indexOf("</encryptCode>")))){
              PKCS7 pkcs72 = PKCS7.getPkcs7Client("ca/fapiao/client/cert/914300007170539007.pfx", "7170539007");
              decodeData1 = pkcs72.pkcs7Decrypt(secondXmlByte);
          }else {
              decodeData1=secondXmlByte;
          }
          content=new String(decodeData1,"UTF-8");
      }else{
          content="测试服务器返回内层报文为空";
      }
      System.out.println("content:"+content);
    }*/
    	
}
