package com.invoice.apiservice.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invoice.apiservice.service.SheetService;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.SheetDetail;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.SheetLog;
import com.invoice.bean.db.SheetPayment;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.Token;
import com.invoice.config.FGlobal;
import com.invoice.util.Convert;
import com.invoice.util.LocalRuntimeException;
import com.invoice.util.MathCal;
import com.invoice.util.NewHashMap;
import com.invoice.util.Serial;
import com.invoice.util.SpringContextUtil;

/**
 * 测试用数据开票，遵循标准接口。
 * @author Baij
 */
@Service("TestInvoiceSheetService")
public class TestInvoiceSheetService extends InvoiceSheetServie {


	public TestInvoiceSheetService() {
		super();
		super.SHEET_TYPE="99";
		super.SHEET_NAME="test";
	}
	
	public String shopid = "0104";
	
	public ResponseBillInfo getInvoiceSheetInfo(RequestBillInfo bill) {
		cookBillInfo(bill);
		bill.setShopid(shopid);
		try {
			// 先查询本地有没有对应发票小票信息
			ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
			String  serialid = null;
			
			if(head!=null) {
				bill.setShopid(head.getShopid());
			}

			if (head == null) {
				// 本地没有则造一个
				
				SheetHead sell = new SheetHead();
				sell.setAmt(0.1);
				sell.setBillno(bill.getBillno());
				sell.setShopid(shopid);
				sell.setEntid("A00001002");
				sell.setSdate(new Date());
				sell.setTradedate(sell.getSdate());
				
				List<SheetDetail> sheetdetail = new ArrayList<>();
				SheetDetail detail = new SheetDetail();
				detail.setAmt(0.1);
				detail.setTaxitemid("3070401000000000000");
				detail.setGoodsname("服务体验");
				detail.setGoodsid("10");
				detail.setQty(1D);
				detail.setTaxrate(0.06);
				sheetdetail.add(detail);
				sell.setSheetdetail(sheetdetail);
				List<SheetPayment> sheetpayment = new ArrayList<>();
				SheetPayment pay = new SheetPayment();
				pay.setAmt(0.1);
				pay.setPayname("支付宝支付");
				pay.setPayid("999");
				pay.setIsinvoice("Y");
				pay.setRowno(1);
				sheetpayment.add(pay);
				sell.setSheetpayment(sheetpayment);
				
				//强制单据的单号
				sell.setSheetid(bill.getSheetid());
				
				// 查到小票后，对小票进行运算
				InvoiceSaleHead saleHead = calculateSheet(sell,serialid);
				saleHead.setCreatetime(new Date());
				saleHead.setFlag(0);
				
				// 写入数据库 独立事物nx开头
				SpringContextUtil.getBean("BillInvoiceService", SheetService.class).nxInvoiceSale2DB(saleHead);

				head = invoiceSaleDao.getInvoiceSaleHead(bill);
				
			}

			if (head == null) {
				throw new LocalRuntimeException("数据未找到"+ bill.getSheetid(),"提取码错误[数据获取]",LocalRuntimeException.DATA_NOT_FOUND);
			}
			
			if(head.getTaxno()==null){
				throw new LocalRuntimeException("配置异常，门店纳税信息不存在:"+ head.getShopid(),"提取码错误[纳税]",LocalRuntimeException.DATA_BASE_ERROR);
			}
			
			//如果已开票，将pdf文件下载地址返回
			if(head.getFlag()==1) {
				Map<String, Object> p = new NewHashMap<>();
				p.put("iqseqno", head.getIqseqno());
				List<Invque> list = queDao.getInvque(p );
				if(list!=null && !list.isEmpty()) {
					String pdf = list.get(0).getIqpdf();
					head.setPdf(pdf);
					int iqstatus = list.get(0).getIqstatus();
					head.setIqstatus(iqstatus);
				}
			}

				
			return head;
		} catch (LocalRuntimeException e) {
			SheetLog log = new SheetLog();
			log.setEntid(bill.getEntid());
			log.setLogtime(new Date());
			log.setMsg(e.getMessage()+"");
			log.setProcessFlag(0);
			log.setSheetid(bill.getSheetid()+"-"+bill.getJe());
			log.setSheettype(bill.getSheettype());
			log.setShopid(bill.getShopid());
			sheetlogDao.insert(log);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			SheetLog log = new SheetLog();
			log.setEntid(bill.getEntid());
			log.setLogtime(new Date());
			log.setMsg(e.getMessage()+"");
			log.setProcessFlag(0);
			log.setSheetid(bill.getSheetid()+"-"+bill.getJe());
			log.setSheettype(bill.getSheettype());
			log.setShopid(bill.getShopid());
			sheetlogDao.insert(log);
			
			String msg = "您输入的提取码错误";
			
			if(e.getMessage()!=null && e.getMessage().startsWith("[")){
				msg = e.getMessage();
			}
			
			throw new RuntimeException(msg);
		}
		
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
				res.setSheetid(res.getShopid()+"-"+res.getSyjid()+"-"+res.getBillno());
				res.setTotalamount(bill.getJe());
			}else if(ss.length==4){
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
				res.setSheetid(res.getShopid()+"-"+res.getSyjid()+"-"+res.getBillno());

				double amt = Double.valueOf(ss[3]);
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
