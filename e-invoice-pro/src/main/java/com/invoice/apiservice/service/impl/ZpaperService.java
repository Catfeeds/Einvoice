package com.invoice.apiservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.PortService;
import com.invoice.port.PortServiceFactory;
import com.invoice.port.bwjf.invoice.service.BwjfService;
import com.invoice.port.bwjf.invoice.service.BwjfServiceImpl;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.EnterpriseService;

import baiwang.invoice.bean.BaiWangRtBlankBean;
import baiwang.invoice.bean.BaiWangRtInvoiceBean;
import baiwang.invoice.service.BaiWangServiceImpl;

/**
 * 纸质发票接口
 * @author Baij
 */
@Service("ZpaperService")
public class ZpaperService {
	
	final public static String SHEET_TYPE = "1";
	
	@Autowired
	PrivateparaService privateparaService;
	
	@Autowired
	EnterpriseService enterpriseService;
	
	/**
	 * 查询空白票
	 * @param invque
	 * @param taxinfo
	 * @param rtn
	 * @return
	 */
	public BaiWangRtBlankBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		BaiWangRtBlankBean bean = new BaiWangRtBlankBean();
		//百望九赋空白票查询
		if("6".equals(taxinfo.getTaxmode())){
			BwjfService service = new BwjfServiceImpl();
			bean.setDqfphm(service.urlconnnet(invque, taxinfo, rtn));
			bean.setDqfpdm(service.blankInvoice(invque,taxinfo,rtn));
		}else
		if("1".equals(taxinfo.getTaxmode())){
			//百望金税空白票查询
		BaiWangServiceImpl service = new BaiWangServiceImpl(taxinfo.getItfserviceUrl(),taxinfo.getTaxno(),taxinfo.getItfjrdm());
		bean = service.blankInvoice(invque, taxinfo, rtn);
		}else
		if(!"".equals(taxinfo.getTaxmode())){
			PortService portservice = PortServiceFactory.getInstance(taxinfo.getTaxmode());
			RtInvoiceBean rtbean = portservice.blankInvoice(invque, taxinfo, rtn);
			bean.setDqfpdm(rtbean.getFp_dm());
			bean.setDqfphm(rtbean.getFp_hm());
		} else{
			throw new RuntimeException("发票平台类型未实现："+taxinfo.getTaxmode());
		} 
		return bean;
	}
	
	/**
	 * 打印纸票
	 * @param invque
	 * @param taxinfo
	 * @param rtn
	 * @return
	 */
	public void fp_print(Invque invque,Taxinfo taxinfo,RtnData rtn){
		//百望九赋打印
		if("6".equals(taxinfo.getTaxmode())){
			BwjfService service = new BwjfServiceImpl();
			service.urlconnnet(invque, taxinfo, rtn);
			service.zp_print(invque,taxinfo,rtn);
		}else
			if("1".equals(taxinfo.getTaxmode())){
				//百望金税打印
				BaiWangServiceImpl service = new BaiWangServiceImpl(taxinfo.getItfserviceUrl(),taxinfo.getTaxno(),taxinfo.getItfjrdm());
				service.invoicePrint(invque, taxinfo, rtn);
			}else
			if(!"".equals(taxinfo.getTaxmode())){
				PortService portservice = PortServiceFactory.getInstance(taxinfo.getTaxmode());
				portservice.invoicePrint(invque, taxinfo, rtn);

			} else{
				throw new RuntimeException("发票平台类型未实现："+taxinfo.getTaxmode());
			} 

	}
	/**
	 * 发票作废
	 * @param invque
	 * @param taxinfo
	 * @param rtn
	 * @return
	 */
	public BaiWangRtInvoiceBean fp_zuofei(Invque invque,Taxinfo taxinfo,RtnData rtn,String zflx){
		//百望九赋空白票查询
		BaiWangRtInvoiceBean bean = new BaiWangRtInvoiceBean();
		if("6".equals(taxinfo.getTaxmode())){
			BwjfService service = new BwjfServiceImpl();
			 bean.setFphm(service.urlconnnet(invque, taxinfo, rtn));
			if(zflx != null && "0".equals(zflx)){
				 bean.setFpdm(service.invoiceInblankValid(invque, taxinfo, rtn));
			}else{
				bean.setFpdm(service.invoiceYkInValid(invque, taxinfo, rtn));
			}
		}else
		if("1".equals(taxinfo.getTaxmode())){
			BaiWangServiceImpl service = new BaiWangServiceImpl(taxinfo.getItfserviceUrl(),taxinfo.getTaxno(),taxinfo.getItfjrdm());
			if(zflx != null && "0".equals(zflx)){
				bean= service.invoiceInblankValid(invque, taxinfo, rtn);
			}else{
				bean= service.invoiceYkInValid(invque, taxinfo, rtn);
			}
		}else
			if(!"".equals(taxinfo.getTaxmode())){
				RtInvoiceBean rtbean = new RtInvoiceBean();
				PortService portservice = PortServiceFactory.getInstance(taxinfo.getTaxmode());
				if(zflx != null && "0".equals(zflx)){
					rtbean= portservice.invoiceInblankValid(invque, taxinfo, rtn);
				
				}else{
					rtbean= portservice.invoiceYkInValid(invque, taxinfo, rtn);
				}
				bean.setFpdm(rtbean.getFp_dm());
				bean.setFphm(rtbean.getFp_hm());
				bean.setKprq(rtbean.getKprq());

			} else{
				throw new RuntimeException("发票平台类型未实现："+taxinfo.getTaxmode());
			} 
		
		return bean;
	}
}
