package com.invoice.port.hnhangxin.invoice.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.jdom.output.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aisino.ws.impl.IEInvWebService;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.PortService;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinRtInvoiceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.MyAES;

@Service("HnHangXinServiceImpl")
public class HnHangXinServiceImpl implements PortService {
	private final Log log = LogFactory.getLog(HnHangXinServiceImpl.class);
	
	@Autowired
	InvoiceService invoiceService;
	
 
	/**
	 * 航信-开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	public void openInvoice(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		
		RtnData rtn = new RtnData();
		
		HnHangXinRtInvoiceBean resInv = openinvoicedp(que,taxinfo,detailList, rtn);
		
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
		try{
		if(resInv.getKprq()==null) resInv.setKprq("");
		if(!"".equals(resInv.getKprq())){
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date= format.parse(resInv.getKprq());

	        SimpleDateFormat format0 = new SimpleDateFormat("yyyyMMddHHmmss");
	        String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
	        resInv.setKprq(time);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
		invoiceHead.setFpewm(resInv.getEwm());
		invoiceHead.setFphm(resInv.getFp_hm());
		invoiceHead.setFpdm(resInv.getFp_dm());
		//invoiceHead.setFpskm(resInv.getFwmw());
		invoiceHead.setFpjym(resInv.getFwm());
		invoiceHead.setFprq(resInv.getKprq());
		invoiceHead.setPdf(resInv.getPdf_url());
		invoiceService.saveInvoice(invoiceHead);
		
		que.setRtewm(resInv.getEwm());
		que.setRtfphm(resInv.getFp_hm());
		que.setRtfpdm(resInv.getFp_dm());
		//que.setRtskm(resInv.getFwmw());
		que.setRtjym(resInv.getFwm());
		que.setRtkprq(resInv.getKprq());
		que.setIqpdf(resInv.getPdf_url());
		que.setIqstatus(50);
		tuisongDzBean(que, taxinfo, rtn);
	}
	
	public HnHangXinRtInvoiceBean openinvoicedp(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {
		
		HnHangXinGenerateBean hx = new HnHangXinGenerateBean(invque.getIqentid());
		
		hx.generateHangXinBean(invque, taxinfo, detailList, "ECXML.FPKJ.BC.E_INV","0", rtn);
		// 向航信发送数据
	if (rtn.getCode() == 0) {
			//System.out.println("发送数据：" + rtn.getMessage());
			String url =hx.getCaProperty("KAIPIAO_URL");// taxinfo.getItfserviceUrl();
			
	        try {
	        	sendDataAxis2(url,rtn.getMessage(),rtn);

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
		if (rtn.getCode() == 0) {
			HnHangXinRtInvoiceBean rtinvoicebean = hx.rtOpenToBean(rtn.getMessage(), rtn);
			if(rtn.getCode() == 0){
				if (!"0000".equals(rtinvoicebean.getReturncode())) {
					rtn.setCode(-1);
					rtn.setMessage(rtinvoicebean.getReturnmessage());
				}
			} 
			return rtinvoicebean;
		} else {
			return null;
		}
	 
	}
	
	public void tuisongDzBean(Invque invque, Taxinfo taxinfo,RtnData rtn) {
		HnHangXinGenerateBean hx = new HnHangXinGenerateBean(invque.getIqentid());
		
		hx.tuisongDzBean(invque, taxinfo, "ECXML.FPKJJG.TS.E_INV_PLATFORM","0", rtn);
		// 向航信发送数据
	if (rtn.getCode() == 0) {
			//System.out.println("发送数据：" + rtn.getMessage());
			String url = hx.getCaProperty("TUISONG_URL");//taxinfo.getItfserviceUrl();
			
	        try {
	        	Map<String, Object> paramMap = new HashMap<String, Object>();
	        	paramMap.put("requestXml", rtn.getMessage());
	        	getHttpConnectResult(paramMap, url, rtn);

			} catch (Exception e) {
				log.error(e, e);
			}
	            
		} else {
			return;
		}
	}
	
	public RtInvoiceBean findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		HnHangXinGenerateBean hx = new HnHangXinGenerateBean(invque.getIqentid());
		
		hx.findInvoiceBean(invque, taxinfo, "ECXML.FPXZ.CX.E_INV","0", rtn);	
		// 向航信发送数据
		if (rtn.getCode() == 0) {
				//System.out.println("发送数据：" + rtn.getMessage());
				String url =hx.getCaProperty("KAIPIAO_URL");// taxinfo.getItfserviceUrl();
				
		        try {
		        	sendDataAxis2(url,rtn.getMessage(),rtn);

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
			if (rtn.getCode() == 0) {
				HnHangXinRtInvoiceBean rtinvoicebean = hx.rtOpenToBean(rtn.getMessage(), rtn);
				if(rtn.getCode() == 0){
					if (!"0000".equals(rtinvoicebean.getReturncode())) {
						rtn.setCode(-1);
						rtn.setMessage(rtinvoicebean.getReturnmessage());
						return null;
					}
				} 
				RtInvoiceBean rtbean = new RtInvoiceBean();
				rtbean.setCzdm(rtinvoicebean.getCzdm());
				rtbean.setDdh(rtinvoicebean.getDdh());
				rtbean.setEwm(rtinvoicebean.getEwm());
				rtbean.setFp_dm(rtinvoicebean.getFp_dm());
				rtbean.setFp_hm(rtinvoicebean.getFp_hm());
				rtbean.setFpqqlsh(rtinvoicebean.getFpqqlsh());
				rtbean.setFpzl_dm(rtinvoicebean.getFpzl_dm());
				rtbean.setFwm(rtinvoicebean.getFwm());
				rtbean.setHjbhsje(rtinvoicebean.getHjbhsje());
				rtbean.setKphjse(rtinvoicebean.getKphjse());
				rtbean.setKplsh(rtinvoicebean.getKplsh());
				rtbean.setKplx(rtinvoicebean.getKplx());
				rtbean.setKprq(rtinvoicebean.getKprq());
				rtbean.setPdf_file(rtinvoicebean.getPdf_file());
				rtbean.setPdf_url(rtinvoicebean.getPdf_url());
				rtbean.setReturncode(rtinvoicebean.getReturncode());
				rtbean.setReturnmessage(rtinvoicebean.getReturnmessage());
				return rtbean;
			} else {
				return null;
			}
		 
 
	}
	
	public RtInvoiceBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		return null;
	}
	
	/**
	 * 空白发票作废
	 * **/
	public RtInvoiceBean invoiceInblankValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		return null;
	}
	
	/**
	 * 已开发票作废
	 * **/
	public RtInvoiceBean invoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		return null;
	}
	
	
	/**
	 * 发票打印
	 * **/
	
	public void invoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn){
 
	}
	
	
	/**
	 * 获取发票PDF连接 
	 * **/
	public String getPdf(Invque invque,RtnData rtn){
		return null;
	}
	
	/**
	 * 发送数据信息
	 * **/
	public void sendDataCxf(String serviceUrl, String outXml, RtnData rtn) throws Exception {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		
		factory.setServiceClass(IEInvWebService.class);
		factory.setAddress(serviceUrl);
		//factory.setAddress("http://222.174.170.5:10005/FPGLXT_FW/CXF/WbService?wsdl");
				
		IEInvWebService service = (IEInvWebService) factory.create();
		
		//SAXBuilder reader = new SAXBuilder(); 
		 
	//	Reader returnQuote = new StringReader(xml);
	//	Document document=reader.build(returnQuote);
		Format format =Format.getPrettyFormat();      
		format.setEncoding("UTF-8");//设置编码格式   
		 
		rtn.setMessage(service.eiInterface(outXml));
		 
	}
	
	/**
	 * 发送数据信息
	 * **/
	public void sendDataAxis2(String serviceUrl, String outXml, RtnData rtn) throws Exception {

	    //String url = "http://hnspt.hnhtxx.cn:8080/t9088/spt_zzssync/webservice/eInvWS/?Wsdl";

        //异步开票
//        String url = "http://192.168.2.22:9083/spt_zzssl/webservice/eInvWS?wsdl";

        String method = "eiInterface";

        ServiceClient sc = new ServiceClient();
        Options opts = new Options();
        opts.setTimeOutInMilliSeconds(1000 * 60);
        EndpointReference end = new EndpointReference(serviceUrl);
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
        rtn.setMessage(resXml);
        System.out.println("=====================================");
        System.out.println("resXml--" + resXml);
/*
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
        System.out.println("content:"+content);*/

	}
	
	/**
	 * http post请求方式
	 * 
	 * @param xml
	 * @param address
	 * @return
	 */
	public String getHttpConnectResult(Map<String, Object> paramMap, String address,RtnData rtn) {
		String resultData = "";
		System.out.println("http请求开始，请求地址：" + address);
		try {
			 resultData = HttpClientCommon.doPost(paramMap, null, address, 6000, 6000, "utf-8");
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
	
}
