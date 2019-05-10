package com.invoice.apiservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invoice.apiservice.service.SheetService;
import com.invoice.bean.db.Goodstax;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.RequestBillItem;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.Token;
import com.invoice.uiservice.service.GoodstaxService;
import com.invoice.util.Convert;
import com.invoice.util.MathCal;
import com.invoice.util.Serial;
import com.invoice.util.SpringContextUtil;

/**
 * 手工单据数据开票
 * 
 * @author Baij
 */
@Service("HandCardService")
public class HandCardService extends InvoiceSheetServie {

	public HandCardService() {
		super();
		super.SHEET_TYPE = "3";
	}

	@Autowired
	GoodstaxService goodstaxService;
	/* 手工开票根据前台传过来的数据直接存到数据库
	 */
	public ResponseBillInfo getInvoiceSheetInfo(RequestBillInfo bill) {
		String sheetid = bill.getSheetid();
		String action = bill.getAction();
		//前台没有提供单号，或更新动作
		if(StringUtils.isEmpty(sheetid) || "update".equalsIgnoreCase(action)){
			InvoiceSaleHead saleHead = deSheetid(bill);
			saleHead.setEntid(bill.getEntid());
			saleHead.setSheettype(SHEET_TYPE);
			saleHead.setShopid(Token.getToken().getShopid());
			Date now = new Date();
			saleHead.setTradedate(Convert.dateFormat(now, "yyyyMMdd"));
			saleHead.setRemark(bill.getRemark());
			saleHead.setFlag(0);
			saleHead.setCreatetime(now);
			
			//处理明细数据
			Token token = Token.getToken();
			List<RequestBillItem> items = bill.getRequestBillItem();
			if(items != null && items.size() > 0){
				for(RequestBillItem item : items){
					if(item.getGoodsid()!=null){
						Goodstax goodstax=new Goodstax();
						goodstax.setEntid(token.getEntid());
						goodstax.setGoodsid(item.getGoodsid());
						try {
							Goodstax newgoodstax = goodstaxService.getGoodstaxById(goodstax);
							if(newgoodstax != null){
								item.setTaxpre(newgoodstax.getTaxpre());
								item.setTaxprecon(newgoodstax.getTaxprecon());
								item.setZerotax(newgoodstax.getZerotax());
							}else{
									item.setTaxpre("0");
									}
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				}
			}
			
			List<InvoiceSaleDetail> list = new ArrayList<InvoiceSaleDetail>();
			double sumAmt = 0.0;
			double sumtaxFee = 0.0;
			int row=1;
			for (RequestBillItem item : items) {
				InvoiceSaleDetail detail = new InvoiceSaleDetail();
				detail.setSerialid(saleHead.getSerialid());
				detail.setEntid(saleHead.getEntid());
				detail.setSheetid(saleHead.getSheetid());
				detail.setSheettype(saleHead.getSheettype());
				detail.setRowno(item.getRowno()==null?row++:item.getRowno());
				detail.setGoodsid(item.getGoodsid()==null?item.getTaxitemid():item.getGoodsid());
				detail.setGoodsname(item.getGoodsname());
				detail.setTaxitemid(item.getTaxitemid());
				detail.setTaxpre(item.getTaxpre());
				detail.setTaxprecon(item.getTaxprecon());
				detail.setZerotax(item.getZerotax());
				if(StringUtils.isEmpty(item.getTradedate())){
					detail.setTradedate(now);
				}else{
					detail.setTradedate(item.getTradedate());
				}
				
				double amt = item.getAmt();
				double taxRate = item.getTaxrate();
				double qty = item.getQty();
				
				double taxFee = MathCal.mul(amt / (1 + taxRate), taxRate, 2); // 税额
				double amount = MathCal.sub(amt, taxFee, 2); // 不含税金额
				double price = amount / qty; // 单价
				sumAmt = MathCal.add(sumAmt, amt, 2);
				sumtaxFee = MathCal.add(sumtaxFee, taxFee, 2);
				
				detail.setTaxrate(taxRate);
				detail.setPrice(price);
				detail.setQty(qty);
				detail.setUnit(item.getUnit());
				detail.setAmount(amount);
				detail.setTaxfee(taxFee);
				detail.setTaxrate(taxRate);
				detail.setAmt(amt);
				detail.setOldamt(amt);
				detail.setIsinvoice("Y");
				list.add(detail);
			}
			saleHead.setTotalamount(sumAmt);
			saleHead.setInvoiceamount(sumAmt);
			saleHead.setTotaltaxfee(sumtaxFee);
			saleHead.setInvoiceSaleDetail(list);
			
			//直接存入数据库
			SpringContextUtil.getBean("HandInvoiceService", SheetService.class).nxInvoiceSale2DBReplace(saleHead);
			bill.setSheetid(saleHead.getSheetid());
		}
		
		ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
		return head;
	}
	
	@Override
	public void cookBillInfo(RequestBillInfo bill) {
		// 如果是小票码形式则拆分为单号、金额
		InvoiceSaleHead head = deSheetid(bill);
		bill.setSheetid(head.getSheetid());
		bill.setJe(head.getTotalamount());
		bill.setShopid(Token.getToken().getShopid());
		bill.setSheettype(SHEET_TYPE);
	}

	@Override
	public InvoiceSaleHead deSheetid(RequestBillInfo bill) {
		String sheetid = bill.getSheetid();
		if(StringUtils.isEmpty(sheetid )){
			sheetid = Serial.getSheetSerial();
		}
		InvoiceSaleHead res = new InvoiceSaleHead();
		res.setSheetid(sheetid);
		res.setSerialid(sheetid);
		return res;
	}
}
