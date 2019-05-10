package com.invoice.apiservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.util.MathCal;

/**
 * 单号类数据开票
 * 
 * @author Baij
 */
@Service("WholeInvoiceService")
public class WholeInvoiceService extends InvoiceSheetServie {


	public WholeInvoiceService() {
		super();
		super.SHEET_TYPE="2";
		super.SHEET_NAME="whole";
	}

	protected void checkIseinvoice(RequestBillInfo bill){
		//不校验门店
	}
	@Override
	public void cookBillInfo(RequestBillInfo bill) {
		// 如果是小票码形式则拆分为单号、金额
		if (!StringUtils.isEmpty(bill.getTicketQC())) {
			InvoiceSaleHead head = deSheetid(bill);
			bill.setSheetid(head.getSheetid());
			bill.setJe(head.getTotalamount());
		} else {
			// 没有qr，则以前端传过来的sheetid为准
		}
		if(!StringUtils.isEmpty(Token.getToken().getShopid())) {
			bill.setShopid(Token.getToken().getShopid());
		}
		bill.setSheettype(SHEET_TYPE);
	}

	@Override
	public InvoiceSaleHead deSheetid(RequestBillInfo bill) {
		String  qr = bill.getTicketQC();
		
		if(StringUtils.isEmpty(qr)) throw new RuntimeException("单据信息为空");
		String[] ss = qr.split("-");
		try {
			InvoiceSaleHead res = new InvoiceSaleHead();
			res.setSheetid(ss[0]);
			if(ss.length==1){
				res.setTotalamount(bill.getJe());
			}else if(ss.length==2){
				double amt = Double.parseDouble(ss[1]);
				String amtType = entPrivatepara.get(bill.getEntid(), FGlobal.AmtType);
				if("100".equals(amtType)){
					amt = MathCal.div(amt, 100, 2);
				}
				
				res.setTotalamount(amt);
			}else{
				throw new RuntimeException("无法解析单据信息:"+qr);
			}
			return res;
		} catch (Exception e) {
			log.error(e,e);
			throw new RuntimeException("无法解析单据信息:"+qr);
		}
	}
	
	public InvoiceSaleHead calculateSheet(SheetHead sell, String serialid) {
		InvoiceSaleHead salehead = super.calculateSheet(sell, serialid);
		
		List<InvoiceSaleDetail> cookList = new ArrayList<InvoiceSaleDetail>();
		InvoiceSaleDetail item = null; 
		List<InvoiceSaleDetail> detailList = salehead.getInvoiceSaleDetail();
		for (InvoiceSaleDetail invoiceSaleDetail : detailList) {
			//餐饮类税目合并，只针对SH001－巨石（上海）啤酒有限公司
			if (invoiceSaleDetail.getEntid().equalsIgnoreCase("SH001") &&
			    invoiceSaleDetail.getTaxitemid().startsWith("3070401")) {
				Double amount = invoiceSaleDetail.getAmount();
				Double amt = invoiceSaleDetail.getAmt();
				Double taxfee = invoiceSaleDetail.getTaxfee();
				if(item==null) {
					item = invoiceSaleDetail;
					item.setGoodsname("餐饮费");
					item.setQty(1.00);
					item.setUnit("批");
					cookList.add(item);
				}else {
					item.setTaxfee(MathCal.add(taxfee, item.getTaxfee(), 2));
					item.setAmt(MathCal.add(amt, item.getAmt(), 2));
					item.setOldamt(item.getAmt());
					item.setAmount(MathCal.add(amount, item.getAmount(), 2));
					item.setPrice(item.getAmount());
				}
			}else{
				cookList.add(invoiceSaleDetail);
			}
		}
		
		for (InvoiceSaleDetail cook : cookList) {
			if(cook.getAmt()>0) {
				cook.setIsinvoice("Y");
			}
		}
		
		salehead.setInvoiceSaleDetail(cookList);
		
		return salehead;
	}
}
