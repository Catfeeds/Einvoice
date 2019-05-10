package com.invoice.apiservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.apiservice.dao.InvoiceDao;
import com.invoice.bean.db.InvoiceDetail;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.util.NewHashMap;
import com.invoice.util.Serial;

@Service
public class InvoiceService {
	@Autowired
	InvoiceDao dao;

	public void insertInvoiceHead(InvoiceHead head) {
		dao.insertInvoiceHead(head);
	}

	public void updateInvoiceHead(InvoiceHead head) {
		dao.updateInvoiceHead(head);
	}

	public void insertInvoiceDetail(List<InvoiceDetail> detailList) {
		dao.insertInvoiceDetail(detailList);
	}
	
	public List<InvoiceHead> queryInvoiceHeadByCallGD(Map<String, Object> p)
	{
		return dao.queryInvoiceHeadByCallGD(p);
	}
	
	public void updateInvoiceHeadForCallGD(InvoiceHead head)
	{
		dao.updateInvoiceHeadForCallGD(head);
	}
	
	/**
	 * 保存到发票表
	 */
	public void saveInvoice(InvoiceHead head){
		insertInvoiceHead(head);
		insertInvoiceDetail(head.getDetailList());
	}
	
	/**
	 * 红冲作废后修改发票表状态
	 */
	public void updateInvoice(Invque invque){
		Map<String,Object> map = new NewHashMap<String,Object>();
		map.put("fpDM", invque.getRtfpdm());
		map.put("fpHM", invque.getRtfphm());
		map.put("iqseqno", invque.getIqseqno());
		if(invque.getIqtype()==1){
			map.put("status", 90);
		}else
		if(invque.getIqtype()==2)
		{
			map.put("status", 99);	
		}else
		{
			throw new RuntimeException("开票类型错误："+invque.getIqtype());
	
		}	
		dao.updateInvoiceStatus(map);
	}
	
	/**
	 * 发票签章后回写返回的PDF连接
	 */
	public void updateInvoicePdf(Invque invque){
		Map<String,Object> map = new NewHashMap<String,Object>();
		map.put("fpDM", invque.getRtfpdm());
		map.put("fpHM", invque.getRtfphm());
		map.put("iqseqno", invque.getIqseqno());
		map.put("pdf", invque.getIqpdf());
		dao.updateInvoicePdf(map);
	}

	/**
	 * 根据原发票号获取发票信息。
	 */
	public List<InvoiceHead> getInvoiceHeadByYfp(Map<String,Object> map){
		 return dao.getInvoiceHeadByYfp(map);
	}
	
	/**
	 * 根据队列号查找是否存在蓝票。
	 */
	public int getInvoiceCount(Invque invque){
		Map<String,Object> map = new NewHashMap<String,Object>();
		map.put("iqseqno", invque.getIqseqno());
		 return dao.getInvoiceCount(map);
	}
	
	public InvoiceHead cookInvoiceHead(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		String invoiceid = Serial.getInvoiceSerial();
		InvoiceHead head = new InvoiceHead();
		head.setInvoiceid(invoiceid);
		head.setIqseqno(que.getIqseqno());
		head.setStatus(100);
		head.setLxdm(que.getIqfplxdm());
		head.setInvoicetype(que.getIqtype());
		
		head.setXsftax(taxinfo.getTaxno());
		head.setXsfname(taxinfo.getTaxname());
		head.setXsfaddr(taxinfo.getTaxadd());
		head.setXsfbank(taxinfo.getTaxbank());
		
		head.setGmftax(que.getIqgmftax());
		head.setGmfname(que.getIqgmfname());
		head.setGmfaddr(que.getIqgmfadd());
		head.setGmfbank(que.getIqgmfbank());
		
		head.setOperator(que.getIqadmin());
		
		head.setTotalamt(que.getIqtotje() + que.getIqtotse());
		head.setTotaltaxamt(que.getIqtotje());
		head.setTotaltaxfee(que.getIqtotse());
		
		head.setYfpdm(que.getIqyfpdm());
		head.setYfphm(que.getIqyfphm());
		head.setMemo(que.getIqmemo());
		head.setZsfs(que.getZsfs());
		
		head.setIslist(que.getIsList());
		
		head.setXfzemail(que.getIqemail());
		head.setXfzphone(que.getIqtel());
		
		List<InvoiceDetail> list = new ArrayList<InvoiceDetail>();
		
		int i=1;
		for (InvoiceSaleDetail invoiceSaleDetail : detailList) {
			InvoiceDetail detail = new InvoiceDetail();
			detail.setInvoiceid(invoiceid);
			
			if(invoiceSaleDetail.getSeqno()==null){
				detail.setSeqno(i);
			}else{
				detail.setSeqno(Integer.parseInt(invoiceSaleDetail.getSeqno()));
			}
			i++;
			detail.setRowno(invoiceSaleDetail.getRowno());
			detail.setUnit(invoiceSaleDetail.getUnit());
			detail.setQty(invoiceSaleDetail.getQty());
			detail.setPrice(invoiceSaleDetail.getPrice());
			detail.setAmt(invoiceSaleDetail.getAmt());
			detail.setAmount(invoiceSaleDetail.getAmount());
			detail.setTaxfee(invoiceSaleDetail.getTaxfee());
			detail.setTaxrate(invoiceSaleDetail.getTaxrate());
			detail.setGoodsid(invoiceSaleDetail.getGoodsid());
			detail.setGoodsname(invoiceSaleDetail.getGoodsname());
			detail.setTaxitemid(invoiceSaleDetail.getTaxitemid());
			detail.setLslbs(invoiceSaleDetail.getZerotax());
			detail.setZzstsgl(invoiceSaleDetail.getTaxprecon());
			detail.setYhzcbs(invoiceSaleDetail.getTaxpre());
			detail.setFphxz(invoiceSaleDetail.getFphxz());
			detail.setZhdyhh(invoiceSaleDetail.getZhdyhh());
			list.add(detail);
		}
		head.setDetailList(list);
		
		return head;
	}
	
}
