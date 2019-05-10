package com.invoice.apiservice.service.impl;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.config.FGlobal;
import com.invoice.util.MathCal;

/**
 * 小票类数据开票
 * 
 * @author Baij
 */
@Service("BillInvoiceService")
public class BillInvoiceService extends InvoiceSheetServie {

	public BillInvoiceService() {
		super();
		super.SHEET_TYPE = "1";
		super.SHEET_NAME = "bill";
	}


	@Autowired
	CalculateDao calculateDao;


	@Override
	public void cookBillInfo(RequestBillInfo bill) {
		// 如果是小票码形式则拆分为店、收银、流水
		if (!StringUtils.isEmpty(bill.getTicketQC())) {
			InvoiceSaleHead head = deSheetid(bill);
			bill.setSheetid(head.getSheetid());
			bill.setShopid(head.getShopid());
			bill.setSyjid(head.getSyjid());
			bill.setBillno(head.getBillno());
			bill.setJe(head.getTotalamount());
		} else if (StringUtils.isEmpty(bill.getSheetid())) {
			throw new RuntimeException("小票信息获取失败");
		}

		bill.setSheettype(SHEET_TYPE);
	}


	@Override
	public InvoiceSaleHead deSheetid(RequestBillInfo bill) {
		String  qr = bill.getTicketQC();
		if(qr==null) throw new RuntimeException("无法解析小票信息");
		
		InvoiceSaleHead res = new InvoiceSaleHead();
		if(qr.contains("-")){//如果串码中含有 - 号，则尝试用-分隔处理
			try {
				String[] ss = qr.split("-");
				if(ss.length==4){
					res.setShopid(ss[0]);
					res.setSyjid(ss[1]);
					String BillnoType = entPrivatepara.get(bill.getEntid(), "BillnoType");
					if(BillnoType==null||"".equals(BillnoType)){
						res.setBillno(ss[2]);
					}else{
						if(ss[2].length()<6&&"1".equals(BillnoType)){
						SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
						String currentDate = format.format(new java.util.Date());
						res.setBillno(currentDate+ss[2]);
						}else{
							res.setBillno(ss[2]);
						}
						
					}

					double amt = Double.valueOf(ss[3]);
					String amtType = entPrivatepara.get(bill.getEntid(), FGlobal.AmtType);
					if("100".equals(amtType)){
						amt = MathCal.div(amt, 100, 2);
					}
					res.setTotalamount(amt);
				}else if(ss.length==3){
					res.setShopid(ss[0]);
					res.setSyjid(ss[1]);
					String BillnoType = entPrivatepara.get(bill.getEntid(), "BillnoType");
					if(BillnoType==null||"".equals(BillnoType)){
						res.setBillno(ss[2]);
					}else{
						if(ss[2].length()<6&&"1".equals(BillnoType)){
						SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
						String currentDate = format.format(new java.util.Date());
						res.setBillno(currentDate+ss[2]);
						}else{
							res.setBillno(ss[2]);
						}
					}
					
					res.setTotalamount(bill.getJe());
				}else{
					throw new RuntimeException("无法解析小票信息:"+qr);
				}
				
				//流水号中的0补位去掉
				res.setBillno(Long.parseLong(res.getBillno())+"");
				
				res.setSheetid(res.getShopid()+"-"+res.getSyjid()+"-"+res.getBillno());
			 
			} catch (Exception e) {
				log.error(e,e);
				throw new RuntimeException("无法解析小票信息"+qr);
			}
		}else{
			throw new RuntimeException("无法解析小票信息:"+qr);
		}
		
		return res;
	}
}
