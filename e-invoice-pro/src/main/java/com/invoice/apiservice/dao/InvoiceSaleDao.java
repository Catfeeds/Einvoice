package com.invoice.apiservice.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.InvoiceSalePay;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.RequestBillDetail;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillDetail;
import com.invoice.bean.ui.ResponseBillInfo;

/**
 * @author Baij
 */
public interface InvoiceSaleDao {

	public void insertSaleHead(InvoiceSaleHead saleHead);

	public void insertSaleDetail(List<InvoiceSaleDetail> detail);

	public int updateInvoiceSaleFlag(InvoiceSaleHead head);
	
	public int updateInvoiceSaleFlagByserialid(InvoiceSaleHead head);

	public ResponseBillInfo getInvoiceSaleHead(RequestBillInfo bill);
	
	public ResponseBillInfo getInvoiceSaleHeadByiqseqno(String iqseqno);

	public List<InvoiceSaleDetail> getInvoiceSaleDetail(RequestBillInfo bill);
	
	public List<InvoiceSalePay> getInvoiceSalePay(RequestBillInfo bill);
	
	public List<InvoiceSaleDetail> getInvoiceSaleDetailList(InvoiceSaleDetail bill);

	public Taxinfo getTaxinfo(Taxinfo info);

	public void insertSalePay(List<InvoiceSalePay> pay);

	public void deleteSaleHead(InvoiceSaleHead saleHead);

	public void deleteSaleDetail(InvoiceSaleHead saleHead);

	public void deleteSalePay(InvoiceSaleHead saleHead);

	/**
	 * 锁定单据开票状态从0到1
	 * @param saleHead
	 * @return
	 */
	public int lockedInvoiceSaleFlag(InvoiceSaleHead saleHead);

	/**
	 * 解锁发票开票状态
	 * @param saleHead
	 */
	public int unLockedInvoiceSaleFlag(InvoiceSaleHead saleHead);
	
	List<InvoiceSaleDetail> getSaleDetailListByInvquelist(Map<String, Object> p);

	public List<HashMap<String, String>> getInvoiceBillDetailList(Map<String, Object> p);

	public InvoiceSaleDetail getInvoiceSaleDetailRow(RequestBillInfo bill);

	public int getInvoiceBillDetailListCount(Map<String, Object> p);

	public int lockedInvoiceSaleDetailFlag(InvoiceSaleDetail detail);

	public int lockedInvoiceSaleDetailFlag4All(InvoiceSaleDetail detail);

	public int countInvqueList(InvoiceSaleHead saleHead);
	public int resetBill(Map<String, Object> p);

	public List<InvoiceSaleDetail> getInvoiceSaleDetailWithTaxName(RequestBillInfo bill);

	public List<Map<String,Object>> getDetailCallbackList(Map<String, Object> para);
	
	public int updateDetailCallbackFlag(Map<String, Object> para);
	
	public int NBupdateCallbackFlag(Map<String, Object> para);
	
	public List<Map<String,Object>> getCallbackList(Map<String, Object> para);
	
	public List<Map<String,Object>> getSaleCallBackList(Map<String, Object> para);
	
	public int updateCallbackFlag(Map<String, Object> para);

	public List<ResponseBillInfo> getInvoiceSaleHeadByGmfNo(RequestBillInfo bill);

	public List<Map<String, Object>> getAutoList(Map<String, String> p);

	public ResponseBillInfo getInvoiceSaleHeadByBillno(RequestBillInfo bill);

	public List<ResponseBillInfo> getInvoiceSaleHeadList(RequestBillInfo requestParams);
	
}
