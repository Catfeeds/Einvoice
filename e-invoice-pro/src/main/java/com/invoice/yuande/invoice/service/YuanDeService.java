package com.invoice.yuande.invoice.service;

import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.rtn.data.RtnData;
import com.invoice.yuande.invoice.bean.YuanDeDzRntDataBean;
import com.invoice.yuande.invoice.bean.YuanDeRtFindInvoiceHeadBean;
import com.invoice.yuande.invoice.bean.YuanDeRtInvoiceBean;

public interface YuanDeService {
	
	/**
	 *开具发票*/
	public YuanDeRtInvoiceBean openinvoice(Invque invque,Taxinfo taxinfo,ResponseBillInfo saleshead,RtnData rtn);
	
	//推送--//银座
	public void tuiSonginvoice(Invque invque,RtnData rtn);
	
	/**
	 *开具发票*/
	public YuanDeDzRntDataBean openinvoiceDzBb(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn);

	/**
	 * 开票后返回的发票信息*/
	public void txRtOpenToBean(String xml,String entid,RtnData rtn);
	/**
	 * 发票查询
	 * 装换为航信接口对象 
	 * **/
	public YuanDeRtFindInvoiceHeadBean findInvoiceBean(Invque invque,Taxinfo taxinfo,InvoiceSaleHead saleshead,RtnData rtn);
	/**
	 * 发票查询
	 * 装换为航信接口对象 
	 * **/
	public YuanDeDzRntDataBean findInvoiceDzBbBean(Invque invque,Taxinfo taxinfo,RtnData rtn);
	
	/**
	 * 小票结算后转换XML返回
	 * 
	 * */
	public String rtInvoiceSaleData(String fpqqlsh,String entid,RtnData rtn);
	
	/**
	 * 小票结算后转换XML返回
	 * */
	public String rtYzInvoiceSaleData(String fpqqlsh,String entid,RtnData rtn);
}
