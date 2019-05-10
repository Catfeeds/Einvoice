package com.invoice.apiservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.SheetDetail;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.util.Convert;
import com.invoice.util.MathCal;
import com.invoice.util.Serial;

/**
 * 通用业务数据开票，遵循标准接口。
 * @author Baij
 */
@Service("CommonInvoiceSheetService")
public class CommonInvoiceSheetService extends InvoiceSheetServie {


	public CommonInvoiceSheetService() {
		super();
		super.SHEET_TYPE="7";
		super.SHEET_NAME="common";
	}
	
	public InvoiceSaleHead calculateSheet(SheetHead sell, String serialid) {
		try {
			InvoiceSaleHead invoiceSaleHead = new InvoiceSaleHead();
			// 基本信息
			String entid = sell.getEntid();

			if(serialid==null) serialid = Serial.getSheetSerial();
			String shopid = sell.getShopid();
			String syjid = sell.getSyjid();
			String billno = sell.getBillno();
			String sheetid = sell.getSheetid();
			invoiceSaleHead.setSerialid(serialid);
			invoiceSaleHead.setEntid(entid);
			invoiceSaleHead.setSheetid(sheetid);
			invoiceSaleHead.setSheettype(SHEET_TYPE);
			invoiceSaleHead.setShopid(shopid);
			invoiceSaleHead.setSyjid(syjid);
			invoiceSaleHead.setBillno(billno);
			invoiceSaleHead.setTotalamount(sell.getAmt());
			invoiceSaleHead.setTradedate(Convert.dateFormat(sell.getTradedate(), "yyyyMMdd"));
			Date tradeDate = sell.getTradedate();
			
			invoiceSaleHead.setGmfadd(sell.getGmfadd());
			invoiceSaleHead.setGmfbank(sell.getGmfbank());
			invoiceSaleHead.setGmfname(sell.getGmfname());
			invoiceSaleHead.setGmftax(sell.getGmftax());
			invoiceSaleHead.setGmfno(sell.getGmfno());
			invoiceSaleHead.setRecvemail(sell.getRecvemail());
			
			if(sell.getIsauto()==null) {
				sell.setIsauto(0);
			}
			invoiceSaleHead.setIsauto(sell.getIsauto());

			// 明细信息
			List<SheetDetail> detail = sell.getSheetdetail();

			if (detail == null || detail.isEmpty()) {
				throw new RuntimeException("开票明细缺失");
			}

			ArrayList<InvoiceSaleDetail> invoiceSaleDetailList = new ArrayList<InvoiceSaleDetail>();

			double totalGooodsAmount = 0.00; // 商品合计金额
			double invoiceGoodsAmount = 0.00; // 可开票商品金额

			int rows = 1;
			for (SheetDetail sheetDetail : detail) {
				totalGooodsAmount = MathCal.add(totalGooodsAmount, sheetDetail.getAmt(), 2);

				String goodsid = sheetDetail.getGoodsid();

				InvoiceSaleDetail invoiceDetail = new InvoiceSaleDetail();
				invoiceDetail.setSerialid(serialid);
				invoiceDetail.setEntid(entid);
				invoiceDetail.setSheetid(invoiceSaleHead.getSheetid());
				invoiceDetail.setSheettype(SHEET_TYPE);
				invoiceDetail.setGoodsid(goodsid);
				invoiceDetail.setGoodsname(sheetDetail.getGoodsname());
				invoiceDetail.setRowno(rows++);
				invoiceDetail.setTradedate(tradeDate);
				
				// 销售数量
				invoiceDetail.setQty(sheetDetail.getQty());
				// 单位
				invoiceDetail.setUnit(sheetDetail.getUnit());

				invoiceDetail.setAmt(sheetDetail.getAmt());
				invoiceDetail.setOldamt(sheetDetail.getAmt());
				

				// 剔除商品，则标记为不开票
				//如果金额和数量为0则标记为不可开票
				if (isNoTaxGoods(entid, goodsid) || sheetDetail.getAmt()<=0) {
					invoiceDetail.setIsinvoice("N");
					invoiceDetail.setTaxrate(0.0);// 税率
				} else {
					invoiceDetail.setIsinvoice("Y");

					// 检查商品名称和数量
					if (StringUtils.isEmpty(sheetDetail.getGoodsname())) {
						throw new RuntimeException("没有开票名称:" + goodsid);
					}

					if (sheetDetail.getQty() == null) {
						throw new RuntimeException("没有开票数量:" + sheetDetail.getGoodsname());
					}

					String taxitemid = sheetDetail.getTaxitemid();
					//如果前端指定了税目，则以前端指定的税目为准
					if(!StringUtils.isEmpty(taxitemid)) {
						invoiceDetail.setTaxpre("0");
						invoiceDetail.setTaxprecon("");
						invoiceDetail.setZerotax("");
					}else {
						// 根据商品编码查询对应税目
						Taxitem taxitem = getTaxitemid(entid, goodsid);

						if (taxitem == null) {
							throw new RuntimeException("[税目缺失]，项目:" + goodsid);
						} else {
							taxitemid = taxitem.getTaxitemid();
							//读取配置的税率
							double taxrate = Double.parseDouble(taxitem.getTaxrate());
							sheetDetail.setTaxrate(taxrate);
							invoiceDetail.setTaxpre(taxitem.getTaxpre()==null?"0":taxitem.getTaxpre());
							invoiceDetail.setTaxprecon(taxitem.getTaxprecon());
							invoiceDetail.setZerotax(taxitem.getZerotax());
						}
					}
					
					invoiceDetail.setTaxitemid(taxitemid);
					
					if (sheetDetail.getTaxrate() == null || sheetDetail.getTaxrate() < 0
							|| sheetDetail.getTaxrate() > 0.17) {
						throw new RuntimeException("费用税率异常:" + sheetDetail.getItemname() + ",税率:"
								+ sheetDetail.getTaxrate());
					}
					invoiceDetail.setTaxrate(sheetDetail.getTaxrate());
					
					if(sheetDetail.getQty()==0.0 || sheetDetail.getAmt()==0.0){
						invoiceDetail.setIsinvoice("N");
					}else{
						invoiceDetail.setPrice(sheetDetail.getAmt() / sheetDetail.getQty()); // 成交单价
						invoiceDetail.setAmount(sheetDetail.getAmt()); // 成交金额
						invoiceGoodsAmount = MathCal.add(invoiceGoodsAmount, sheetDetail.getAmt(), 2);
					}
				}
				invoiceSaleDetailList.add(invoiceDetail);
			}

			//如果没有指定 合计金额，则从明细汇总
			if(invoiceSaleHead.getTotalamount()==null){
				invoiceSaleHead.setTotalamount(totalGooodsAmount);
			}
			

			double totalTaxFee = 0.00; // 合计可开票税额
			invoiceSaleHead.setInvoiceamount(invoiceGoodsAmount);


			// 对小票明细中的金额和税额进行重新运算
			for (int i = 0; i < invoiceSaleDetailList.size(); i++) {
				InvoiceSaleDetail d = invoiceSaleDetailList.get(i);
				if ("N".equalsIgnoreCase(d.getIsinvoice())) {
					double dj = d.getQty()==0?0.00:d.getOldamt()/d.getQty();
					d.setPrice(dj); // 成交单价
					d.setAmount(0.00); // 成交金额
					d.setTaxfee(0.00);
					d.setAmt(0.00);
					d.setTaxpre("0");
					if(d.getTaxitemid()==null)
						d.setTaxitemid("");
					continue;
				}

				double amount = d.getAmount();
				// 计算税额
				double taxFee = MathCal.mul(amount / (1 + d.getTaxrate()), d.getTaxrate(), 2); // 税额
				totalTaxFee = MathCal.add(totalTaxFee, taxFee, 2);
				amount = MathCal.sub(amount, taxFee, 2); // 金额
				double price = amount / d.getQty(); // 单价
				d.setPrice(price);
				d.setTaxfee(taxFee);
				d.setAmount(amount);
				d.setAmt(MathCal.add(amount, taxFee, 2));
			}
			invoiceSaleHead.setTotaltaxfee(totalTaxFee);

			invoiceSaleHead.setInvoiceSaleDetail(invoiceSaleDetailList);
			return invoiceSaleHead;
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("[异常]:" + e.getLocalizedMessage());
		}
	}
	
	public Taxitem getTaxitemid(String entid, String goodsid) {
		Taxitem taxitem = getTaxitemidByGoods(entid, goodsid);
		return taxitem;
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
		bill.setShopid(Token.getToken().getShopid());
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
}
