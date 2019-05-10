package com.invoice.port.nbbwjf.invoice.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.PortService;
import com.invoice.port.nbbwjf.invoice.bean.NbBwjfRtOpenInvoiceBean;
import com.invoice.port.nbbwjf.invoice.util.RequestUtils;
import com.invoice.port.nbbwjf.invoice.util.XmlUtils;
import com.invoice.rtn.data.RtnData;

@Service("NbBwjfServiceImpl")
public class NbBwjfServiceImpl implements PortService {
	private final Log _log = LogFactory.getLog(NbBwjfServiceImpl.class);
	
	String _interfacecode="FPKJ";
	String _interfacecodecx="FPCX";
	String _appid="DZFPQZ";
	String _requestcode="";
	String _unicode="utf-8";
	String _requestsecret="";
	String _url="";

	
	@Autowired
	InvoiceService invoiceService;

	@Autowired
	InvqueDao inqueDao;

	NbBwjfGenerateBean bw;

	public NbBwjfServiceImpl() {
		bw = new NbBwjfGenerateBean();
	}

	@Override
	public void openInvoice(Invque que, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList) {
		RtnData rtn = new RtnData();
		RtInvoiceBean resInv = null;
		_requestsecret=taxinfo.getItfjrdm();
		_requestcode=taxinfo.getItfskpkl();
		_url=taxinfo.getItfserviceUrl();
		if(_requestsecret.equalsIgnoreCase("")||_url.equalsIgnoreCase("")) {
			throw new RuntimeException("发票开具失败：开票地址或开票密钥为空！");
		}
		if (que.getRetflag() == null) {
			que.setRetflag("");
		}
		if ("1".equals(que.getRetflag())) {
			resInv = findInvoice(que,taxinfo,rtn);
		} else {
			bw.generateBaiWangBean(que, taxinfo, detailList, rtn);
			_log.info("XML转换："+rtn.getMessage());
			System.out.println(rtn.getMessage());
			if (rtn.getCode() == 0) {
				resInv=sendXml(rtn.getMessage(), rtn,_interfacecode);
			} else {
				throw new RuntimeException("发票开具失败：" + rtn.getMessage());
			}
		}
		
		if (resInv == null) {
			throw new RuntimeException("发票开具失败：" + rtn.getMessage());
		} else if (resInv.getFp_hm() == null || "".equals(resInv.getFp_hm())) {
			throw new RuntimeException("发票开具失败：" + rtn.getMessage());
		}
		List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
		for (InvoiceSaleDetail detail : detailList) {
			if ("Y".equals(detail.getIsinvoice())) {
				detailList1.add(detail);
			}
		}

		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo,detailList1);
		 invoiceHead.setFphm(resInv.getFp_hm());
		 invoiceHead.setFpdm(resInv.getFp_dm());
		 invoiceHead.setFpjym(resInv.getJym());
		 invoiceHead.setFprq(resInv.getKprq());
		 invoiceHead.setPdf(resInv.getPdf_url());
		 invoiceService.saveInvoice(invoiceHead);
		
		 que.setRtfphm(resInv.getFp_hm());
		 que.setRtfpdm(resInv.getFp_dm());
		 que.setRtjym(resInv.getJym());
		 que.setIqpdf(resInv.getPdf_url());
		 que.setRtkprq(resInv.getKprq());
		 que.setIqstatus(40);

	}

	public RtInvoiceBean sendXml(String xml, RtnData rtn,String interfacecode) {
		String requestData = null;
		String rsData = null;

		RtInvoiceBean resInv = new RtInvoiceBean();
		try {
			requestData = XmlUtils.getSendToTaxXML(interfacecode, xml,_appid, _requestcode,_requestsecret);
			_log.info("请求报文为:" + requestData);
			System.out.println("请求报文为:" + requestData + ",开始请求。");
			rsData = RequestUtils.httpPostForXMLUTF8(_url, requestData, _unicode);
			_log.info("请求接口结束，获得结果:" + rsData);
			System.out.println("请求接口结束，获得结果:" + rsData);
			rtn.setMessage(rsData);
			NbBwjfRtOpenInvoiceBean rtinvoicebean = new NbBwjfRtOpenInvoiceBean();
			rtinvoicebean = bw.rtOpenToBean(rtn.getMessage(),rtn);
			if (rtn.getCode() == 0) {
				resInv.setFpqqlsh(rtinvoicebean.getFpqqlsh());
				resInv.setFp_hm(rtinvoicebean.getFp_hm());
				resInv.setFp_dm(rtinvoicebean.getFp_dm());
				resInv.setJym(rtinvoicebean.getJym());
				resInv.setKprq(rtinvoicebean.getKprq());
				resInv.setPdf_url(rtinvoicebean.getPdf_url());
			} else {
				return null;
			}
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return resInv;
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
		RtInvoiceBean resInv=null;
		bw.findInvoice(invque,taxinfo, rtn);
		System.out.println(rtn.getMessage());
		if(rtn.getCode()==0){
			resInv=sendXml(rtn.getMessage(), rtn,_interfacecodecx);
		}else{
			rtn.setCode(-1);
			//rtn.setMessage("数据装换为XML出错");
			return null;
		}
		return resInv;
	}
}
