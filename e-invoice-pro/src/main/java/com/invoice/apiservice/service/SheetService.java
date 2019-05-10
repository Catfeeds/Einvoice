package com.invoice.apiservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.ui.RequestBillDetail;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.RequestInvoice;
import com.invoice.bean.ui.RequestInvoiceDetail;
import com.invoice.bean.ui.ResponseBillDetail;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.ResponseInvoice;

/**
 * 单据服务
 * @author Baij
 * 
 */
public interface SheetService {
	public String getSHEET_TYPE();
	public String getSHEET_NAME();
	/**
	 * 获取单据数据头信息
	 * @param bill
	 * @return
	 */
	public ResponseBillInfo getInvoiceSheetInfo(RequestBillInfo bill);
	
	/**
	 * 解析前台传过来的单据信息，拆分串，设置sheetid等值
	 * @param info
	 */
	public void cookBillInfo(RequestBillInfo bill);
	
	/**
	 * 获取单据信息，附带明细
	 * @param bill
	 * @return
	 */
	public ResponseBillInfo getInvoiceSheetInfoWithDetail(RequestBillInfo bill);
	
	/**
	 * 单据开票预览信息
	 * @param requestInvoice
	 * @return
	 */
	public List<ResponseInvoice> getInvoicePreview(RequestInvoice requestInvoice);
	
	
	/**
	 * 获取远端单据信息
	 * @param entid
	 * @param shopid
	 * @param sheetid
	 * @return
	 */
	public SheetHead getRemoteBill(String entid,String shopid,String sheetid);

	/**
	 * 保存到数据库
	 * @param saleHead
	 */
	public void nxInvoiceSale2DB(InvoiceSaleHead saleHead);
	
	/**
	 * 从数据库删除
	 * @param saleHead
	 */
	public void txInvoiceSale2Delete(InvoiceSaleHead saleHead);
	
	
	/**
	 * 保存数据，先删除后插入
	 * @param saleHead
	 */
	public void nxInvoiceSale2DBReplace(InvoiceSaleHead saleHead);
	
	/**
	 * 计算单据数据
	 * @param sell
	 * @return
	 */
	public InvoiceSaleHead calculateSheet(SheetHead sell, String serialid);
	
	/**
	 * 将提取码转为单据信息
	 * @param qr
	 * @return
	 */
	public InvoiceSaleHead deSheetid(RequestBillInfo bill);

	/**
	 * 获取可开票的明细清单
	 * @param bill
	 * @return
	 */
	public List<HashMap<String, String>> getInvoiceBillDetailList(Map<String, Object> p);
	/**
	 * 获取可开票的明细清单数量
	 * @param bill
	 * @return
	 */
	public int getInvoiceBillDetailListCount(Map<String, Object> p);

	/**
	 * 按明细开票发票信息预览
	 * @param listRequest
	 * @return
	 */
	public List<ResponseInvoice> getInvoicePreview4Detail(List<RequestInvoiceDetail> listRequest);

	/**
	 * 根据队列信息产生开票明细信息
	 * @param que
	 * @return
	 */
	public List<InvoiceSaleDetail> cookOpenInvoiceDetail(Invque que);
	
	
	/**
	 * 根据用户唯一号码查询
	 * @param gmfNo
	 * @return
	 */
	public List<ResponseBillInfo> getInvoiceBillInfoByGmfNo(RequestBillInfo bill);
	public List<ResponseBillInfo> getInvoiceSaleHeadList(RequestBillInfo requestParams);
	
}
