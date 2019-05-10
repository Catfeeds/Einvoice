package com.invoice.apiservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.invoice.apiservice.dao.PreInvoiceDao;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.PreInvoiceAsk;
import com.invoice.bean.db.SheetDetail;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.SheetLog;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.config.FGlobal;
import com.invoice.util.Convert;
import com.invoice.util.LocalRuntimeException;
import com.invoice.util.MathCal;
import com.invoice.util.NewHashMap;
import com.invoice.util.Serial;

/**
 * 中免开票数据处理
 * 
 * @author Baij
 */
@Service("ZMInvoiceService")
public class ZMInvoiceService extends InvoiceSheetServie {
	
	@Autowired
	PreInvoiceDao preInvoiceDao;


	public ZMInvoiceService() {
		super();
		super.SHEET_TYPE="6";
		super.SHEET_NAME="zm";
	}
	
	public ResponseBillInfo getInvoiceSheetInfo(RequestBillInfo bill) {
		cookBillInfo(bill);
		
		try {
			// 先查询本地有没有对应发票小票信息
			ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
			
			//尝试根据billno列查询数据
			if(head==null) {
				head = invoiceSaleDao.getInvoiceSaleHeadByBillno(bill);
			}
			
			//避免写sheetlog日志缺少shopid列
			bill.setShopid("");
			if (head == null) {
				throw new LocalRuntimeException("数据未找到"+ bill.getSheetid(),"提取码错误[数据准备中请稍后再试]",LocalRuntimeException.DATA_NOT_FOUND);
			}
			
			if(head.getTaxno()==null){
				throw new LocalRuntimeException("配置异常，门店纳税信息不存在:"+ head.getShopid(),"提取码错误[纳税]",LocalRuntimeException.DATA_BASE_ERROR);
			}
			
			//微信渠道验证指定的发票类型
//			if ("wx".equals(bill.getChannel())) {
//				if(!StringUtils.isEmpty(head.getInvoicelx()) && !head.getInvoicelx().equals("026")) {
//					throw new LocalRuntimeException("当前不能开具电子票:"+ head.getInvoicelx(),"当前不能开具电子票:"+ head.getInvoicelx(),LocalRuntimeException.DATA_NOT_FOUND);
//				}
//			}
			
			//检查用户是否提交了预开票申请
			Map<String, Object> p = new NewHashMap<>();
			p.put("entid", bill.getEntid());
			p.put("sheetid", bill.getSheetid());
			p.put("sheettype", SHEET_TYPE);
			p.put("flag", 0);
			PreInvoiceAsk ask = preInvoiceDao.getPreInvoiceAsk(p);
			if(ask!=null) {
				throw new LocalRuntimeException("您的申请已成功受理，<br/>我们在您办理提货/退担保金手续后，<br/>将发票发送至您的邮箱、手机","您的申请已成功受理，<br/>我们在您办理提货/退担保金手续后，<br/>将发票发送至您的邮箱、手机",LocalRuntimeException.DATA_MESSAGE);
			}
			

			//修正值
			bill.setShopid(head.getShopid());
			bill.setSheetid(head.getSheetid());
			
			// 微信渠道需要校验金额
			checkAmt(bill, head);

			//如果已开票，将pdf文件下载地址返回
			if(head.getFlag()==1) {
				p.clear();
				p.put("iqseqno", head.getIqseqno());
				p.put("pagestart", 0);
				p.put("pagesize", 1);
				List<Invque> list = queDao.getInvque(p );
				if(list!=null && !list.isEmpty()) {
					String pdf = list.get(0).getIqpdf();
					head.setPdf(pdf);
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
			
			String msg = "提取码错误";
			
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

			Enterprise enterprise = new Enterprise();
			enterprise.setEntid(entid);
			Enterprise ent = enterpriseService.getEnterpriseById(enterprise);

			if (ent == null) {
				throw new RuntimeException("[企业不存在]：" + entid);
			}
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
			
			invoiceSaleHead.setRecvemail(sell.getRecvemail());
			invoiceSaleHead.setRecvphone(sell.getRecvphone());
			
			invoiceSaleHead.setIsauto(sell.getIsauto());
			Date tradeDate = sell.getSdate();
			if(sell.getSdate()!=null) {
			}
			if(sell.getTradedate()!=null) {
				tradeDate = sell.getTradedate();
			}
			invoiceSaleHead.setTradedate(Convert.dateFormat(tradeDate, "yyyyMMdd"));
			
			invoiceSaleHead.setGmfadd(sell.getGmfadd());
			invoiceSaleHead.setGmfbank(sell.getGmfbank());
			invoiceSaleHead.setGmfname(sell.getGmfname());
			invoiceSaleHead.setGmftax(sell.getGmftax());
			invoiceSaleHead.setGmfno(sell.getGmfno());
			invoiceSaleHead.setInvoicelx(sell.getInvoicelx());
			
			
			invoiceSaleHead.setTotalamount(sell.getTotalAmount());
			invoiceSaleHead.setInvoiceamount(0.00);
			invoiceSaleHead.setTotaltaxfee(0.00);
			
			// 明细信息
			List<SheetDetail> detail = sell.getSheetdetail();

			//允许明细为空，表示不可开票
			if (detail == null || detail.isEmpty()) {
				return invoiceSaleHead;
				//throw new RuntimeException("明细缺失");
			}

			ArrayList<InvoiceSaleDetail> invoiceSaleDetailList = new ArrayList<InvoiceSaleDetail>();

			int rows = 1;
			double totalGooodsAmount = 0.00;
			double totalTaxFee = 0.00;
			for (SheetDetail sheetDetail : detail) {

				InvoiceSaleDetail invoiceDetail = new InvoiceSaleDetail();
				invoiceDetail.setSerialid(serialid);
				invoiceDetail.setEntid(entid);
				invoiceDetail.setSheetid(invoiceSaleHead.getSheetid());
				invoiceDetail.setSheettype(SHEET_TYPE);
				invoiceDetail.setGoodsid(sheetDetail.getGoodsid());
				invoiceDetail.setGoodsname(sheetDetail.getGoodsname());
				invoiceDetail.setTaxitemid(sheetDetail.getTaxitemid());
				invoiceDetail.setRowno(sheetDetail.getRowno() == null ? rows++ : sheetDetail.getRowno());
				invoiceDetail.setTradedate(tradeDate);
				invoiceDetail.setFphxz(sheetDetail.getFphxz());
				invoiceDetail.setZhdyhh(sheetDetail.getZhdyhh());
				invoiceDetail.setTradedate(tradeDate);
				invoiceDetail.setTaxpre("0");
				
				
				// 销售数量
				Double qty = sheetDetail.getQty();
				if(qty==null) qty=1.0;
				invoiceDetail.setQty(qty);
				// 单位
				invoiceDetail.setUnit(sheetDetail.getUnit());
				invoiceDetail.setSpec(sheetDetail.getSpec());

				invoiceDetail.setTaxrate(sheetDetail.getTaxrate());
				Double amt = sheetDetail.getAmt();
				//金额为0的商品强制为1分钱，20180705
				if(amt==0) {
					amt = 0.01;
				}
				invoiceDetail.setAmt(amt);
				invoiceDetail.setOldamt(amt);
				
				invoiceDetail.setIsinvoice("Y");
				invoiceSaleDetailList.add(invoiceDetail);
				totalGooodsAmount = MathCal.add(totalGooodsAmount, amt, 2);
			}

			invoiceSaleHead.setTotalamount(sell.getTotalAmount());
			invoiceSaleHead.setInvoiceamount(totalGooodsAmount);
			
			for (int i = 0; i < invoiceSaleDetailList.size(); i++) {
				InvoiceSaleDetail d = invoiceSaleDetailList.get(i);

				double amount = d.getAmt();
				// 计算税额
				double taxFee = MathCal.mul(amount / (1 + d.getTaxrate()), d.getTaxrate(), 2); // 税额
				totalTaxFee = MathCal.add(totalTaxFee, taxFee, 2);
				amount = MathCal.sub(amount, taxFee, 2); // 金额
				double price = amount / d.getQty(); // 单价
				d.setPrice(price);
				d.setTaxfee(taxFee);
				d.setAmount(amount);
			}
			invoiceSaleHead.setTotaltaxfee(totalTaxFee);

			invoiceSaleHead.setInvoiceSaleDetail(invoiceSaleDetailList);
			return invoiceSaleHead;
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("[异常]:" + e.getLocalizedMessage());
		}
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
				res.setTotalamount(bill.getJe());
				res.setSheetid(qr);
			}else if(ss.length==4){
				double amt = Double.parseDouble(ss[3]);
				String amtType = entPrivatepara.get(bill.getEntid(), FGlobal.AmtType);
				if("100".equals(amtType)){
					amt = MathCal.div(amt, 100, 2);
				}
				res.setTotalamount(amt);
				res.setSheetid(ss[0]+"-"+ss[1]+"-"+ss[2]);
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
