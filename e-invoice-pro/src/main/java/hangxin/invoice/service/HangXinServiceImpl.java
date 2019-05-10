package hangxin.invoice.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.aisino.ws.op.IEInvWebServicePortType;
import com.aisino.ws.qz.IEInvWebService;
import com.aisino.ws.qz.IEInvWebService_Service;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;

import hangxin.invoice.bean.HangXinQzRtBean;
import hangxin.invoice.bean.HangXinRtInvoiceBean;

public class HangXinServiceImpl implements HangXinService {
	private final Log log = LogFactory.getLog(HangXinServiceImpl.class);

	private String nsrsbh;

	HangXinGenerateBean hx;

	public HangXinServiceImpl(String nsrsbh) {
		this.nsrsbh = nsrsbh;
		hx = new HangXinGenerateBean(nsrsbh);
	}

	public HangXinRtInvoiceBean openinvoice(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {

		if (nsrsbh == null || "".equals(nsrsbh)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票时纳税人识别号为空");
			return null;
		}

		hx.generateHangXinBean(invque, taxinfo, detailList, "EI.FPKJ.U.EC.INTRA", rtn);
		// 向航信发送数据
		if (rtn.getCode() == 0) {
			System.out.println("发送数据：" + rtn.getMessage());
			String url = hx.getCaProperty("KAIPIAO_URL");
			
	        try {
	        	com.aisino.ws.op.IEInvWebService ss = new com.aisino.ws.op.IEInvWebService(new URL(url));
		        IEInvWebServicePortType port = ss.getIEInvWebServiceHttpPort(); 
				String res = port.eiInterface(rtn.getMessage());
				rtn.setMessage(res);
				log.info("kp result:"+res);
			} catch (MalformedURLException e) {
				log.error(e, e);
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				return null;
			}
	        
//			sendQzData(0, url, "eiInterface", rtn.getMessage(), rtn);
			
//			sendData(url,rtn.getMessage(),rtn);
			System.out.println("返回数据：" + rtn.getMessage());
		} else {
			return null;
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			HangXinRtInvoiceBean rtinvoicebean = hx.rtOpenToBean(rtn.getMessage(), rtn);
			if (!"0000".equals(rtinvoicebean.getReturncode())) {
				rtn.setCode(-1);
				rtn.setMessage(rtinvoicebean.getReturnmessage());
			}
			return rtinvoicebean;
		} else {
			return null;
		}
	}

	/**
	 * 发送数据信息
	 * **/
	public void sendData(String serviceUrl, String xml, RtnData rtn) {

		if (serviceUrl == null || "".equals(serviceUrl)) {
			rtn.setCode(-1);
			rtn.setMessage("URL为空");
			return;
		}

		if (nsrsbh == null || "".equals(nsrsbh)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票时纳税人识别号为空");
			return;
		}

		try {
			PostMethod postMethod = new PostMethod(serviceUrl);

			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
			postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
			postMethod.setRequestBody(xml);
			int statusCode = 0;
			HttpClient httpClient = new HttpClient();

			statusCode = httpClient.executeMethod(postMethod);

			if (statusCode == 200) {
				// System.out.println("200 success ");
				String tokeninfo = postMethod.getResponseBodyAsString();
				// System.out.println(tokeninfo);

				if (StringUtils.isEmpty(tokeninfo)) {
					rtn.setCode(-1);
					rtn.setMessage("返回的数据为空");
					return;
				} else {
					rtn.setMessage(tokeninfo);
				}

			} else {
				System.out.println("500 error ");
				rtn.setCode(-1);
				rtn.setMessage("返回数据错误 状态 " + statusCode);
			}

		} catch (Exception e) {
			log.error(e, e);
		}

	}

	public HangXinQzRtBean openQzinvoice(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList, RtnData rtn) {
		hx.generateHangXinQzBean(invque, taxinfo, detailList, rtn);
		// 登陆航信获取数据
		if (rtn.getCode() == 0) {
			String url = hx.getCaProperty("QIANZHANG_URL");
//			sendQzData(1, url, "eiInterface", rtn.getMessage(), rtn);
			try {
				IEInvWebService_Service ss = new IEInvWebService_Service(new URL(url));
				IEInvWebService port = ss.getIEInvWebServicePort();
				String res = port.eiInterface(rtn.getMessage());
				rtn.setMessage(res);
				log.info("qz result:"+res);
			} catch (MalformedURLException e) {
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
			HangXinQzRtBean rtinvoicebean = hx.rtQzOpenToBean(rtn.getMessage(), rtn);
			if (!"0000".equals(rtinvoicebean.getReturncode())) {
				rtn.setCode(-1);
				rtn.setMessage(rtinvoicebean.getReturnmessage());
			}
			return rtinvoicebean;
		} else {
			return null;
		}
	}

	/**
	 * 发票查询 装换为航信接口对象
	 * **/
	public HangXinRtInvoiceBean findInvoiceBean(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {
		if (nsrsbh == null || "".equals(nsrsbh)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票时纳税人识别号为空");
			return null;
		}
		hx.findInvoiceBean(invque, taxinfo, detailList, "EI.FPKJCX.U.EC.INTRA", rtn);

		// 登陆航信获取数据
		if (rtn.getCode() == 0) {
			String url = hx.getCaProperty("KAIPIAO_URL");
			//sendQzData(0, url, "fpcyInterface", rtn.getMessage(), rtn);
			 try {
		        	com.aisino.ws.op.IEInvWebService ss = new com.aisino.ws.op.IEInvWebService(new URL(url));
			        IEInvWebServicePortType port = ss.getIEInvWebServiceHttpPort(); 
					String res = port.fpcyInterface(rtn.getMessage());
					rtn.setMessage(res);
				} catch (MalformedURLException e) {
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
			HangXinRtInvoiceBean rtinvoicebean = hx.rtOpenToBean(rtn.getMessage(), rtn);

			if (!"0000".equals(rtinvoicebean.getReturncode())) {
				rtn.setCode(-1);
				rtn.setMessage(rtinvoicebean.getReturnmessage());
			}

			return rtinvoicebean;
		} else {
			return null;
		}
	}

}
