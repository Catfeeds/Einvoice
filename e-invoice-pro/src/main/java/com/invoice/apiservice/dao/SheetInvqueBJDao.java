package com.invoice.apiservice.dao;

import java.util.Map;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.port.nbbanji.invoice.bean.InvoiceInvqueReturn;
import com.invoice.port.nbbanji.invoice.bean.ResponseBillInfoBJ;

public interface SheetInvqueBJDao {
	//阪急
	public void updateForInvqueBanJI(Map<String, String> p);
	
	public void updateForReturnBanJI(Map<String, String> p);
	
	public void updateForSaleHeadBanJI(Map<String, String> p);
	
	public Invque getInvqueBJ(Map<String, Object> p);
	
	public ResponseBillInfoBJ getSheetSaleHeadBJ(RequestBillInfo bill);
	
	public ResponseBillInfoBJ getSheetHeadOriginal(Map<String, String> p);
	
	public void insertInvoiceInvqueReturn(InvoiceInvqueReturn p);
	
	public InvoiceInvqueReturn getSheetHeadReturn(Map<String, String> p);
	
	public void updateInvoiceHeadIqseqno(InvoiceSaleHead p);
	
	public void updateInvoiceDetailIqseqno(InvoiceSaleHead p);
}
