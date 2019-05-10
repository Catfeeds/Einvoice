package com.invoice.apiservice.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.baiwang.baiwangcloud.utils.StringUtil;
import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.dao.SheetStatDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.bean.db.CClient;
import com.invoice.bean.db.Catedis;
import com.invoice.bean.db.Category;
import com.invoice.bean.db.Catetax;
import com.invoice.bean.db.Enterprise;
import com.invoice.bean.db.Goodsdis;
import com.invoice.bean.db.Goodstax;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.InvoiceSalePay;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.InvqueList;
import com.invoice.bean.db.InvqueListDetail;
import com.invoice.bean.db.Paymode;
import com.invoice.bean.db.SheetDetail;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.SheetLog;
import com.invoice.bean.db.SheetPayment;
import com.invoice.bean.db.Shop;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.RequestInvoice;
import com.invoice.bean.ui.RequestInvoiceDetail;
import com.invoice.bean.ui.RequestInvoiceItem;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.ResponseInvoice;
import com.invoice.bean.ui.ResponseInvoiceItem;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.Convert;
import com.invoice.util.LocalRuntimeException;
import com.invoice.util.MathCal;
import com.invoice.util.NewHashMap;
import com.invoice.util.Serial;
import com.invoice.util.SpringContextUtil;

public abstract class InvoiceSheetServie implements SheetService{
	static public final Log log = LogFactory.getLog(InvoiceSheetServie.class);
	@Autowired
	CalculateDao calculateDao;
	
	@Autowired
	InvoiceSaleDao invoiceSaleDao;
	
	@Autowired
	SheetStatDao sheetlogDao;
	
	@Autowired
	InvqueDao queDao;
	
	@Autowired
	PrivateparaService privateparaService;
	
	@Autowired
	EnterpriseService enterpriseService;
	
	@Autowired
	ShopService shopService;
	
	@Autowired
	EntPrivatepara entPrivatepara;
	
	protected String SHEET_TYPE;
	protected String SHEET_NAME;
	
	
	@Transactional
	public void nxInvoiceSale2DB(InvoiceSaleHead saleHead) {
		invoiceSaleDao.insertSaleHead(saleHead);
		List<InvoiceSaleDetail> detail = saleHead.getInvoiceSaleDetail();
		if(detail!=null && !detail.isEmpty())
			invoiceSaleDao.insertSaleDetail(detail);
		List<InvoiceSalePay> pay = saleHead.getInvoiceSalePay();
		if(pay!=null && !pay.isEmpty())
			invoiceSaleDao.insertSalePay(pay);
	}
	
	@Transactional
	public void txInvoiceSale2Delete(InvoiceSaleHead saleHead){
		invoiceSaleDao.deleteSaleDetail(saleHead);
		invoiceSaleDao.deleteSalePay(saleHead);
		invoiceSaleDao.deleteSaleHead(saleHead);
	}
	
	@Transactional
	public void nxInvoiceSale2DBReplace(InvoiceSaleHead saleHead) {
		invoiceSaleDao.deleteSaleDetail(saleHead);
		invoiceSaleDao.deleteSalePay(saleHead);
		invoiceSaleDao.deleteSaleHead(saleHead);
		
		invoiceSaleDao.insertSaleHead(saleHead);
		List<InvoiceSaleDetail> detail = saleHead.getInvoiceSaleDetail();
		invoiceSaleDao.insertSaleDetail(detail);
		List<InvoiceSalePay> pay = saleHead.getInvoiceSalePay();
		if(pay!=null && !pay.isEmpty())
			invoiceSaleDao.insertSalePay(pay);
	}
	
	/**
	 * 获取开票信息
	 * 
	 * @param data
	 * @return
	 */
	public ResponseBillInfo getInvoiceSheetInfo(RequestBillInfo bill) {
		cookBillInfo(bill);
		checkIseinvoice(bill);
		try {
			// 先查询本地有没有对应发票小票信息
			ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
			String  serialid = null;
			//需重算标志，Serialid不要变
			if(head!=null && head.getFlag()==-1){
				serialid = head.getSerialid();
				InvoiceSaleHead saleHead = new InvoiceSaleHead();
				saleHead.setSheettype(head.getSheettype());
				saleHead.setSheetid(head.getSheetid());
				saleHead.setEntid(head.getEntid());
				saleHead.setSerialid(head.getSerialid());
				SpringContextUtil.getBean("BillInvoiceService", SheetService.class).txInvoiceSale2Delete(saleHead );
				head = null;
			}
			
			if(head!=null) {
				bill.setShopid(head.getShopid());
			}

			if (head == null) {
				// 本地没有则向小票服务查询
				String rearchSheetid=bill.getSheetid();
				 
				//小票类型，且是微信端单号后面带上金额
				if(bill.getSheettype().equals("1") && "wx".equalsIgnoreCase(bill.getChannel())){
					Double amt = bill.getJe();
					 
					if(amt==null) amt=0.0;
					amt = MathCal.mul(amt, 100, 2);
					rearchSheetid +="-"+amt.intValue();
				}
				
				SheetHead sell = getRemoteBill(bill.getEntid(), bill.getShopid(), rearchSheetid);
				
				if(sell==null){
					throw new LocalRuntimeException("数据未找到，请检查是否正确或稍后再试 "+ bill.getSheetid(),"提取码错误[数据准备中]",LocalRuntimeException.DATA_NOT_FOUND);
				}
				
				//强制单据的单号
				sell.setSheetid(bill.getSheetid());
				
				//远端门店必须和请求的门店信息一致
				if(!sell.getShopid().equals(bill.getShopid())){
					throw new LocalRuntimeException("数据门店信息异常 ，门店R:L="+ sell.getShopid()+":"+bill.getShopid(),"提取码错误[门店]",LocalRuntimeException.DATA_BASE_ERROR);
				}

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

			
			// 微信渠道需要校验金额
			checkAmt(bill, head);

			//如果已开票，将pdf文件下载地址返回
			if(head.getFlag()==1) {
				Map<String, Object> p = new NewHashMap<>();
				p.put("iqseqno", head.getIqseqno());
				p.put("pagestart", 0);
				p.put("pagesize", 1);
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
	
	/**
	 * 获取小票开票信息,附带商品明细
	 * @param bill
	 * @return
	 */
	public ResponseBillInfo getInvoiceSheetInfoWithDetail(RequestBillInfo bill) {
		ResponseBillInfo head = getInvoiceSheetInfo(bill);
		List<InvoiceSaleDetail> detail = invoiceSaleDao.getInvoiceSaleDetail(bill);
		head.setInvoiceSaleDetail(detail);
		double sumAmt = 0.0;
		double sumInvoiceAmt = 0.0;
		double sumUnPay = 0.0;
		double sumdkspvalue = 0.0;
		for (InvoiceSaleDetail invoiceSaleDetail : detail) {
			
				if(invoiceSaleDetail.getTaxitemid()==null) invoiceSaleDetail.setTaxitemid("");
				
				if("Y".equals(invoiceSaleDetail.getIsinvoice())){
					sumInvoiceAmt = MathCal.add(sumInvoiceAmt, invoiceSaleDetail.getAmount()+invoiceSaleDetail.getTaxfee(), 2);
					
				} 
				
				if("".equals(invoiceSaleDetail.getTaxitemid())){
					sumdkspvalue = MathCal.add(sumdkspvalue, invoiceSaleDetail.getOldamt(), 2);
				}
					sumAmt = MathCal.add(sumAmt, invoiceSaleDetail.getOldamt(), 2); 
				
				
		}
		//支付信息
		String notePay = "";
		List<InvoiceSalePay> paylist = invoiceSaleDao.getInvoiceSalePay(bill);
		head.setInvoiceSalePay(paylist);
		
		if(paylist!=null && !paylist.isEmpty()){
			for (InvoiceSalePay invoiceSalePay : paylist) {
				if(!"Y".equalsIgnoreCase(invoiceSalePay.getIsinvoice())){
					sumUnPay = MathCal.add(sumUnPay, invoiceSalePay.getAmt(), 2);
					notePay+=invoiceSalePay.getPayname()+": "+invoiceSalePay.getAmt()+";";
				}
			}
		}
		
		double unAmt = sumUnPay>sumdkspvalue?sumUnPay:sumdkspvalue;
		String payNote = "小票总金额："+sumAmt+"，发票金额："+sumInvoiceAmt+"，不可开票金额："+unAmt;
		if(sumUnPay>sumdkspvalue){
			payNote+="，其中不可开票支付方式："+notePay;
		}else{
			payNote+="，其中代开票商品金额："+unAmt+".";
		}
		//如果是聊百将交易日期加入明细
		if("SD003".equals(head.getEntid())){
			payNote +="交易日期:"+head.getTradedate();
		}
		head.setPaynote(payNote);
		return head;
	}
	
	
	/**
	 * 预览发票信息
	 * @param requestInvoice
	 * @return
	 */
	public List<ResponseInvoice> getInvoicePreview(RequestInvoice requestInvoice){
		List<ResponseInvoice> resList = new ArrayList<ResponseInvoice>();
		
		Map<String,Set<String>> taxnoMap = new HashMap<String, Set<String>>();
		
		//根据小票信息找到对应的小票及销售方发票信息
		List<RequestInvoiceItem> itemList = requestInvoice.getRequestInvoicePreviewItem();
		
		for (RequestInvoiceItem item : itemList) {
			RequestBillInfo bill = new RequestBillInfo();
			bill.setEntid(requestInvoice.getEntid());
			bill.setChannel(requestInvoice.getChannel());
			bill.setJe(item.getJe());
			bill.setShopid(item.getShopid());
			bill.setSheettype(requestInvoice.getSheettype());
			bill.setSheetid(item.getSheetid());
			
			ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
			
			if(head==null) throw new RuntimeException(""+item.getSheetid()+"不存在，请重新选择");
			
			//如果已开票则直接跳过
			if(head.getFlag()==1) throw new RuntimeException(""+item.getSheetid()+"已开票或正在开票，请重新选择");
			if(head.getInvoiceamount()<=0)  throw new RuntimeException(""+item.getSheetid()+"不可开票，请重新选择");
			
			//金额不一致则跳过
			checkAmt(bill, head);
			
			String taxno = head.getTaxno();
			
			if(taxnoMap.containsKey(taxno)){
				Set<String> sheetSet = taxnoMap.get(taxno);
				if(sheetSet.contains(item.getSheetid())){
					throw new RuntimeException(""+item.getSheetid()+"数据重复");
				}else {
					sheetSet.add(item.getSheetid());
				}
				
				//已经有这个纳税号的发票则取出后，直接累加明细
				for (ResponseInvoice res : resList) {
					if(res.getXsfNsrsbh().equals(taxno)){
						res.setAmount(MathCal.add(Double.valueOf(res.getAmount()), head.getInvoiceamount(), 2));
						res.setAmountDX(Convert.changeToBig(Double.valueOf(res.getAmount())));
						List<InvoiceSaleDetail> detailList = invoiceSaleDao.getInvoiceSaleDetailWithTaxName(bill);
						for (InvoiceSaleDetail invoiceSaleDetail : detailList) {
							if("Y".equalsIgnoreCase(invoiceSaleDetail.getIsinvoice())){
								String preName = invoiceSaleDetail.getTaxitemname()==null?"":"*"+invoiceSaleDetail.getTaxitemname()+"*";
								
								ResponseInvoiceItem previewitem = new ResponseInvoiceItem();
								previewitem.setTaxFee(invoiceSaleDetail.getTaxfee());
								previewitem.setTaxRate(invoiceSaleDetail.getTaxrate()*100);
								previewitem.setQty(invoiceSaleDetail.getQty());
								previewitem.setAmount(invoiceSaleDetail.getAmount());
								previewitem.setGoodsName(preName+invoiceSaleDetail.getGoodsname());
								previewitem.setPrice(invoiceSaleDetail.getPrice());
								previewitem.setUnit(invoiceSaleDetail.getUnit());
								res.getInvoicePreviewItem().add(previewitem);
							}
						}
						
						//添加发票信息
						ResponseBillInfo billInfo = new ResponseBillInfo();
						billInfo.setShopid(item.getShopid());
						billInfo.setSyjid(item.getSyjid());
						billInfo.setBillno(item.getBillno());
						billInfo.setSheetid(item.getSheetid());
						res.getBillInfoList().add(billInfo);
					}
				}
			}else{
				Set<String> sheetSet = new HashSet<String>();
				sheetSet.add(item.getSheetid());
				taxnoMap.put(taxno, sheetSet );
				
				//新增这个发票信息
				ResponseInvoice res  = new ResponseInvoice();
				res.setEntid(requestInvoice.getEntid());
				res.setChannel(requestInvoice.getChannel());
				res.setUserid(requestInvoice.getUserid());
				res.setSheettype(requestInvoice.getSheettype());
			
				res.setRecvEmail(requestInvoice.getRecvEmail());
				res.setRecvPhone(requestInvoice.getRecvPhone());
				
				res.setXsfDzdh(head.getTaxadd());
				res.setXsfMc(head.getTaxname());
				res.setXsfNsrsbh(head.getTaxno());
				res.setXsfYhzh(head.getTaxbank());
				
				res.setGmfMc(requestInvoice.getGmfMc());
				res.setGmfNsrsbh(requestInvoice.getGmfNsrsbh()==null?"":requestInvoice.getGmfNsrsbh());
				res.setGmfDzdh(requestInvoice.getGmfDzdh()==null?"":requestInvoice.getGmfDzdh());
				res.setGmfYhzh(requestInvoice.getGmfYhzh()==null?"":requestInvoice.getGmfYhzh());
				
				res.setIqmemo(requestInvoice.getIqmemo()==null?"":requestInvoice.getIqmemo());
				res.setIqpayee(requestInvoice.getIqpayee()==null?"":requestInvoice.getIqpayee());
				res.setIqchecker(requestInvoice.getIqchecker()==null?"":requestInvoice.getIqchecker());
				//开票点设置为纳税号默认
				res.setKpd(head.getKpd());
				
				res.setAmount(head.getInvoiceamount());
				res.setAmountDX(Convert.changeToBig(head.getInvoiceamount()));
				res.setTaxAmount(head.getTotaltaxfee());
				//设置发票类型
				res.setIqfplxdm(requestInvoice.getIqfplxdm());
				//增加发票明细
				List<InvoiceSaleDetail> detailList = invoiceSaleDao.getInvoiceSaleDetailWithTaxName(bill);
				List<ResponseInvoiceItem> invoicePreviewItem = new ArrayList<ResponseInvoiceItem>();
				for (InvoiceSaleDetail invoiceSaleDetail : detailList) {
					if("Y".equalsIgnoreCase(invoiceSaleDetail.getIsinvoice())){
						
						String preName = invoiceSaleDetail.getTaxitemname()==null?"":"*"+invoiceSaleDetail.getTaxitemname()+"*";
						
						ResponseInvoiceItem previewitem = new ResponseInvoiceItem();
						previewitem.setTaxFee(invoiceSaleDetail.getTaxfee());
						previewitem.setTaxRate(invoiceSaleDetail.getTaxrate()*100);
						previewitem.setQty(invoiceSaleDetail.getQty());
						previewitem.setAmount(invoiceSaleDetail.getAmount());
						previewitem.setGoodsName(preName + invoiceSaleDetail.getGoodsname());
						previewitem.setPrice(invoiceSaleDetail.getPrice());
						previewitem.setUnit(invoiceSaleDetail.getUnit());
						invoicePreviewItem.add(previewitem);
					}
				}
				res.setInvoicePreviewItem(invoicePreviewItem);
				
				//添加发票信息
				List<ResponseBillInfo> billInfoList = new ArrayList<ResponseBillInfo>();
				ResponseBillInfo billInfo = new ResponseBillInfo();
				billInfo.setShopid(item.getShopid());
				billInfo.setTotalamount(item.getJe());
				billInfo.setSheetid(item.getSheetid());
				billInfoList.add(billInfo);
				res.setBillInfoList(billInfoList);
				
				res.setIqchecker(requestInvoice.getIqchecker());
				res.setIqpayee(requestInvoice.getIqpayee());
				res.setAdmin(requestInvoice.getAdmin());
				resList.add(res);
			}
		}
		
		return resList;
	}
	
	@Override
	public List<ResponseInvoice> getInvoicePreview4Detail(List<RequestInvoiceDetail> listRequest){
		List<ResponseInvoice> resList = new ArrayList<ResponseInvoice>();
		Map<String,Set<String>> keyTaxnoMap = new HashMap<String, Set<String>>();
		
		//根据小票信息找到对应的小票及销售方发票信息
		Long fpdm = null;
		Long fphm = null;
		for (RequestInvoiceDetail item : listRequest) {
			fpdm = item.getFpdm();
			fphm = item.getFphm();
			String key = ""+item.getSheetid()+" - "+item.getRowno();
			RequestBillInfo bill = new RequestBillInfo();
			bill.setEntid(item.getEntid());
			bill.setSheettype(item.getSheettype());
			bill.setSheetid(item.getSheetid());
			bill.setRowno(item.getRowno());
			ResponseBillInfo head = invoiceSaleDao.getInvoiceSaleHead(bill);
			InvoiceSaleDetail detail =  invoiceSaleDao.getInvoiceSaleDetailRow(bill);
			if(detail==null) throw new RuntimeException(key+"不存在，请重新选择");
			
			if(!"Y".equalsIgnoreCase(detail.getIsinvoice())){
				continue;
			}
			
			//如果已开票则直接跳过
			if(detail.getFlag()==1) throw new RuntimeException(key+"已开票或正在开票，请重新选择");
			if(!detail.getIsinvoice().equals("Y"))  throw new RuntimeException(key+"不可开票，请重新选择");
			
			String gfTaxno = head.getGmftax();
			String xfTaxno = head.getTaxno();
			String keyTaxno = gfTaxno+"#"+xfTaxno;
			
			//合并相同购方#销售方纳税号的数据
			if(keyTaxnoMap.containsKey(keyTaxno)){
				Set<String> sheetSet = keyTaxnoMap.get(keyTaxno);
				if(sheetSet.contains(key)){
					throw new RuntimeException(key+"数据重复");
				}else {
					sheetSet.add(key);
				}
				
				//已经有这个纳税号的发票则取出后，按项目名称合并
				for (ResponseInvoice res : resList) {
					//纳税号对比一致
					if(res.getXsfNsrsbh().equals(xfTaxno) && res.getGmfNsrsbh().equals(gfTaxno)){
						List<ResponseInvoiceItem> preItem = res.getInvoicePreviewItem();
						boolean hasJoin = false;
						for (ResponseInvoiceItem responseInvoiceItem : preItem) {
							//对税目，名称，税率一致，则进行合计
							if(responseInvoiceItem.getGoodsName().equals(detail.getGoodsname())
								&& responseInvoiceItem.getTaxitemid().equals(detail.getTaxitemid())
								&& responseInvoiceItem.getTaxRate()==detail.getTaxrate()*100
									) {
								hasJoin = true;
								//累加金额和数量，重算单价
								double qty = MathCal.add(responseInvoiceItem.getQty() , detail.getQty(),5);
								double amount = MathCal.add(responseInvoiceItem.getAmount(), detail.getAmount(), 2);
								double taxFee = MathCal.add(responseInvoiceItem.getTaxFee(), detail.getTaxfee(), 2);
								responseInvoiceItem.setQty(qty);
								responseInvoiceItem.setAmount(amount);
								responseInvoiceItem.setTaxFee(taxFee);
							}
						}
						
						if(!hasJoin) {
							ResponseInvoiceItem previewitem = new ResponseInvoiceItem();
							previewitem.setTaxFee(detail.getTaxfee());
							previewitem.setTaxRate(detail.getTaxrate()*100);
							previewitem.setQty(detail.getQty());
							previewitem.setAmount(detail.getAmount());
							previewitem.setGoodsName(detail.getGoodsname());
							previewitem.setPrice(detail.getPrice());
							previewitem.setUnit(detail.getUnit());
							previewitem.setTaxitemid(detail.getTaxitemid());
							res.getInvoicePreviewItem().add(previewitem);
						}
						
						//添加开票单据信息
						//先判断是否已经存在
						List<ResponseBillInfo> billInfoList = res.getBillInfoList();
						boolean hasBill = false;
						for (ResponseBillInfo billInfo : billInfoList) {
							if(billInfo.getSerialid().equals(head.getSerialid())) {
								hasBill = true;
								billInfo.getRownoList().add(detail.getRowno());
							}
						}
						if(!hasBill) {
							ResponseBillInfo billInfo = new ResponseBillInfo();
							billInfo.setSerialid(head.getSerialid());
							billInfo.setShopid(head.getShopid());
							billInfo.setSheetid(head.getSheetid());
							billInfo.setSheettype(head.getSheettype());
							billInfo.getRownoList().add(detail.getRowno());
							res.getBillInfoList().add(billInfo);
						}
					}
				}
			}else{
				Set<String> sheetSet = new HashSet<String>();
				sheetSet.add(key);
				keyTaxnoMap.put(keyTaxno, sheetSet );
				
				//新增这个发票信息
				ResponseInvoice res  = new ResponseInvoice();
				res.setEntid(item.getEntid());
				res.setChannel(item.getChannel());
				res.setUserid(item.getUserid());
				res.setSheettype(item.getSheettype());
			
				res.setRecvEmail(item.getRecvEmail());
				res.setRecvPhone(item.getRecvPhone());
				
				res.setXsfDzdh(head.getTaxadd());
				res.setXsfMc(head.getTaxname());
				res.setXsfNsrsbh(head.getTaxno());
				res.setXsfYhzh(head.getTaxbank());
				
				res.setGmfMc(head.getGmfname());
				res.setGmfNsrsbh(head.getGmftax());
				res.setGmfDzdh(head.getGmfadd());
				res.setGmfYhzh(head.getGmfbank());
				
				//开票点设置为纳税号默认
				res.setKpd(head.getKpd());
				res.setFpdm(fpdm);
//				res.setAmount(head.getInvoiceamount());
//				res.setAmountDX(Convert.changeToBig(head.getInvoiceamount()));
//				res.setTaxAmount(head.getTotaltaxfee());
				//设置发票类型
				res.setIqfplxdm(item.getIqfplxdm());
				//增加发票明细
				List<ResponseInvoiceItem> invoicePreviewItem = new ArrayList<ResponseInvoiceItem>();
				List<ResponseBillInfo> billInfoList = new ArrayList<ResponseBillInfo>();
				
				ResponseInvoiceItem previewitem = new ResponseInvoiceItem();
				previewitem.setTaxFee(detail.getTaxfee());
				previewitem.setTaxRate(detail.getTaxrate()*100);
				previewitem.setQty(detail.getQty());
				previewitem.setAmount(detail.getAmount());
				previewitem.setGoodsName(detail.getGoodsname());
				previewitem.setPrice(detail.getPrice());
				previewitem.setUnit(detail.getUnit());
				previewitem.setTaxitemid(detail.getTaxitemid());
				invoicePreviewItem.add(previewitem);
				res.setInvoicePreviewItem(invoicePreviewItem);
				
				//添加开票单据信息
				ResponseBillInfo billInfo = new ResponseBillInfo();
				billInfo.setSerialid(head.getSerialid());
				billInfo.setShopid(head.getShopid());
				billInfo.setSheetid(head.getSheetid());
				billInfo.setSheettype(head.getSheettype());
				billInfo.getRownoList().add(detail.getRowno());
				billInfoList.add(billInfo);
				res.setBillInfoList(billInfoList);
				
				resList.add(res);
			}
		}
		
		//重算合计金额
		long i = 0;
		for (ResponseInvoice res : resList) {
			List<ResponseInvoiceItem> item = res.getInvoicePreviewItem();
			double sumAmount = 0.0;
			double sumTaxfee = 0.0;
			for (ResponseInvoiceItem responseInvoiceItem : item) {
				sumAmount = MathCal.add(responseInvoiceItem.getAmount(), sumAmount, 2);
				sumTaxfee = MathCal.add(responseInvoiceItem.getTaxFee(), sumTaxfee, 2);
				responseInvoiceItem.setPrice(responseInvoiceItem.getAmount()/responseInvoiceItem.getQty());
			}
			
			res.setAmount(sumAmount);
			try {
				res.setAmountDX(Convert.changeToBig(sumAmount));
			}catch (Exception e) {
				res.setAmountDX("");
			}
			res.setTaxAmount(sumTaxfee);
			if(fphm!=null) {
				res.setFphm(fphm+i);
				i++;
			}
		}
		
		return resList;
	}
	
	
	public SheetHead getRemoteBill(String entid,String shopid,String sheetid){
		
		CClient client =  privateparaService.getClientUrlByShopid(entid, shopid);
		
		if(client==null){
			if (shopid == null || shopid.isEmpty() || shopid.equalsIgnoreCase("null"))
				throw new LocalRuntimeException("数据不存在或门店未注册","提取码错误[门店]");
			else
				throw new LocalRuntimeException("不存在此门店编码:"+shopid,"提取码错误[门店]");
		}
		
		client.initHeadMap();
		
		Map<String, String> headMap = client.getHeadMap();
		headMap.put("shopid", shopid);
		headMap.put("sheetid", sheetid);
		headMap.put("sheetname", SHEET_NAME);
		
		String res = client.getMessage("getSheet");
		
		JSONObject r = JSONObject.parseObject(res);
		if(r.getIntValue("code")!=0){
			String msg = r.getString("message");
			if (msg == null && r.containsKey("msg")) {
				msg = r.getString("msg");
			}
			throw new LocalRuntimeException("获取数据异常:"+client.getClientid()+",MSG:"+msg,"提取码错误[数据异常]");
		}
		return JSONObject.parseObject(r.getString("data"), SheetHead.class);
	}

	/**
	 * 计算小票信息
	 * @param sell 原始小票信息
	 * @param serialid 
	 * @return 计算好的开票小票信息
	 */
	public InvoiceSaleHead calculateSheet(SheetHead sell, String serialid) {
		try {
			InvoiceSaleHead invoiceSaleHead = new InvoiceSaleHead();
			// 基本信息
			String entid = sell.getEntid();

			Enterprise enterprise = new Enterprise();
			enterprise.setEntid(entid);
			Enterprise ent = enterpriseService.getEnterpriseById(enterprise);

			if (ent == null) {
				throw new LocalRuntimeException("企业不存在：" + entid,"提取码错误[企业]");
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
			invoiceSaleHead.setTotalamount(sell.getAmt());
			invoiceSaleHead.setTradedate(Convert.dateFormat(sell.getSdate(), "yyyyMMdd"));
			
			invoiceSaleHead.setGmfadd(sell.getGmfadd());
			invoiceSaleHead.setGmfbank(sell.getGmfbank());
			invoiceSaleHead.setGmfname(sell.getGmfname());
			invoiceSaleHead.setGmftax(sell.getGmftax());
			invoiceSaleHead.setRecvemail(sell.getRecvemail());
			
			if(sell.getIsauto()==null) {
				sell.setIsauto(0);
			}
			invoiceSaleHead.setIsauto(sell.getIsauto());
			
			Date tradeDate = sell.getSdate();

			// 支付信息
			List<SheetPayment> payList = sell.getSheetpayment();
			ArrayList<InvoiceSalePay> invoiceSalePayment = new ArrayList<InvoiceSalePay>();
			double totalPayAmount = 0.00; // 支付合计金额
			double invoicePayAmount = 0.00; // 可开票支付金额
			double unInvoicePayAmount = 0.00;//不可开票支付金额
			int rows = 1;
			for (SheetPayment sheetPayment : payList) {
				String payid = sheetPayment.getPayid();
				
				//如果是银座4位支付方式，自动在后面补位#店号第一个字符
				if(payid != null && entid.equals("SDYZ")){
					payid = payid+"#"+shopid.substring(0,1);
				}
				
				double amt = sheetPayment.getAmt();

				InvoiceSalePay pay = new InvoiceSalePay();
				pay.setSerialid(serialid);
				pay.setEntid(entid);
				pay.setSheetid(sheetid);
				pay.setRowno(sheetPayment.getRowno());
				pay.setPayid(payid);
				pay.setSheettype(SHEET_TYPE);
				pay.setAmt(amt);
				pay.setRowno(sheetPayment.getRowno() == null ? rows++ : sheetPayment.getRowno());

				totalPayAmount = MathCal.add(totalPayAmount, amt, 2);
				
				// 判断付款方式是否需要剔除
				Paymode paymode = getPaymodeById(entid, payid);
				if(paymode != null){
					// 状态为不剔除的支付计入合计金额
					if (paymode.getPaystatus() == 1) {
						invoicePayAmount = MathCal.add(invoicePayAmount, amt, 2);
						pay.setIsinvoice("Y");
					} else {
						unInvoicePayAmount = MathCal.add(unInvoicePayAmount, amt, 2);
						pay.setIsinvoice("N");
					}
					pay.setPayname(paymode.getPayname());
				}else {
					invoicePayAmount = MathCal.add(invoicePayAmount, amt, 2);
					pay.setIsinvoice("Y");
					String tmp = sheetPayment.getPayname();
					if(StringUtil.isEmpty(tmp)) {
						tmp=payid;
					}
					pay.setPayname(tmp);
				}
				invoiceSalePayment.add(pay);
			}

			// 小票明细信息
			List<SheetDetail> detail = sell.getSheetdetail();

			if (detail == null || detail.isEmpty()) {
				throw new LocalRuntimeException("小票明细缺失","提取码错误[无明细]");
			}

			ArrayList<InvoiceSaleDetail> invoiceSaleDetailList = new ArrayList<InvoiceSaleDetail>();

			double totalGooodsAmount = 0.00; // 商品合计金额
			double invoiceGoodsAmount = 0.00; // 可开票商品金额
			double unInvoiceGoodsAmount = 0.00; // 不可开票商品金额
			rows = 1;
			for (SheetDetail sheetDetail : detail) {
				log.debug("info sheetDetail:"+JSONObject.toJSONString(sheetDetail));
				
				totalGooodsAmount = MathCal.add(totalGooodsAmount, sheetDetail.getAmt(), 2);

				String goodsid = sheetDetail.getItemid();

				InvoiceSaleDetail invoiceDetail = new InvoiceSaleDetail();
				invoiceDetail.setSerialid(serialid);
				invoiceDetail.setEntid(entid);
				invoiceDetail.setSheetid(invoiceSaleHead.getSheetid());
				invoiceDetail.setSheettype(SHEET_TYPE);
				invoiceDetail.setGoodsid(goodsid);
				
				//商品名称特殊字符处理
				String goodsName = sheetDetail.getItemname();
				goodsName = cookString(goodsName);
				invoiceDetail.setGoodsname(goodsName);
				invoiceDetail.setRowno(StringUtils.isEmpty(sheetDetail.getRowno()) ? rows++ : sheetDetail.getRowno());
				invoiceDetail.setTradedate(tradeDate);
				
				
				// 销售数量
				invoiceDetail.setQty(sheetDetail.getQty());
				// 单位
				invoiceDetail.setUnit(sheetDetail.getUnit());
				invoiceDetail.setSpec(sheetDetail.getSpec());

				invoiceDetail.setAmt(sheetDetail.getAmt());
				invoiceDetail.setOldamt(sheetDetail.getAmt());
				invoiceDetail.setTaxrate(sheetDetail.getTaxrate());// 税率

				// 剔除商品，则标记为不开票
				//如果金额和数量为0则标记为不可开票
				String categoryid = sheetDetail.getCategoryid();
				invoiceDetail.setCategoryid(categoryid);
				
				if (isNoTaxCate(entid, categoryid) || isNoTaxGoods(entid, goodsid)) {
					invoiceDetail.setIsinvoice("N");
					unInvoiceGoodsAmount = MathCal.add(unInvoiceGoodsAmount, sheetDetail.getAmt(), 2);
				} else {
					invoiceDetail.setIsinvoice("Y");

					// 检查商品名称和数量
					if (StringUtils.isEmpty(sheetDetail.getItemname())) {
						throw new LocalRuntimeException("没有商品名称:" + goodsid,"提取码错误[g:"+goodsid+"]");
					}

					if (sheetDetail.getQty() == null) {
						throw new LocalRuntimeException("没有销售数量:" + sheetDetail.getItemname(),"提取码错误[q:"+goodsid+"]");
					}

					if (sheetDetail.getTaxrate() == null || sheetDetail.getTaxrate() < 0
							|| sheetDetail.getTaxrate() > 0.17) {
						throw new LocalRuntimeException("商品税率异常:" + sheetDetail.getItemname() + ",税率:"
								+ sheetDetail.getTaxrate(),"提取码错误[r:"+goodsid+":"+sheetDetail.getTaxrate()+"]");
					}

					String taxitemid = sheetDetail.getTaxitemid();
					//如果前端指定了税目，则以前端指定的税目为准
					if(!StringUtils.isEmpty(taxitemid)) {
						invoiceDetail.setTaxpre("0");
						invoiceDetail.setTaxprecon("");
						invoiceDetail.setZerotax("");
					}else {
						// 根据商品编码查询对应税目
						Taxitem taxitem = getTaxitemid(entid, goodsid, categoryid,sheetDetail.getTaxrate());

						if (taxitem == null) {
							throw new LocalRuntimeException("商品税目缺失，商品:" + goodsid + ",类别:" + categoryid,"提取码错误[t:"+goodsid+":"+categoryid+"]");
						} else {
							taxitemid = taxitem.getTaxitemid();
						}
						invoiceDetail.setTaxpre(taxitem.getTaxpre()==null?"0":taxitem.getTaxpre());
						invoiceDetail.setTaxprecon(taxitem.getTaxprecon());
						invoiceDetail.setZerotax(taxitem.getZerotax());
					}
					invoiceDetail.setTaxitemid(taxitemid);
					
					if(sheetDetail.getQty()==0.0 || sheetDetail.getAmt()==0.0){
						invoiceDetail.setIsinvoice("N");
					}else{
						invoiceGoodsAmount = MathCal.add(invoiceGoodsAmount, sheetDetail.getAmt(), 2);
					}
				}
				log.debug("info invoiceDetail:"+JSONObject.toJSONString(invoiceDetail));
				invoiceSaleDetailList.add(invoiceDetail);
			}

			//如果没有指定 合计金额，则从明细汇总
			if(invoiceSaleHead.getTotalamount()==null){
				invoiceSaleHead.setTotalamount(totalGooodsAmount);
			}
			
			double totalTaxFee = 0.00; // 合计可开票税额
			double totalInvoiceAmount = 0.00; // 可开票总金额(含税)
			double fentanAmt = 0.00;
			
			//如果合计商品金额大于合计支付金额，说明存在舍分.可开票商品金额减去差额
			if(totalGooodsAmount > totalPayAmount) {
				double cha = MathCal.sub(totalGooodsAmount, totalPayAmount, 2);
				invoiceGoodsAmount = MathCal.sub(invoiceGoodsAmount, cha, 2);
				totalGooodsAmount = MathCal.sub(totalGooodsAmount, cha, 2);
				unInvoiceGoodsAmount = MathCal.add(unInvoiceGoodsAmount, cha, 2);
				fentanAmt = cha;
			}else if(totalGooodsAmount < totalPayAmount) {
				//如果合计商品金额小于合计支付金额，说明存在找零，在下面的算法计算
//				double cha = MathCal.sub(totalPayAmount, totalGooodsAmount, 2);
//				invoicePayAmount = MathCal.sub(invoicePayAmount, cha, 2);
//				unInvoicePayAmount = MathCal.add(unInvoicePayAmount, cha, 2);
//				fentanAmt = 0.00;
			}
			
			//算法：对比不可开票商品和不可开票支付，取较大值作为需剔除的金额
			if (unInvoiceGoodsAmount != 0 || unInvoicePayAmount !=0) {
				//如果不可开票商品金额大于不可开票支付金额，则可开票金额=可开票商品金额
				if (unInvoiceGoodsAmount > unInvoicePayAmount) {
					totalInvoiceAmount = invoiceGoodsAmount;
				} else {
					//存在找零
					//不可开票支付金额大时，还需要将不可开票支付金额 - 不可开票商品金额 的差额作为需要剔除数据
					// 差额作为需剔除的金额
					fentanAmt = MathCal.sub(unInvoicePayAmount, unInvoiceGoodsAmount, 2);
					//可开票金额=商品金额 - 不可开票支付金额
					totalInvoiceAmount = MathCal.sub(totalGooodsAmount, unInvoicePayAmount, 2);
				}
			} else {
				totalInvoiceAmount = invoiceGoodsAmount;
			}
			

			// 计算分摊或剔除商品
			if ("B".equalsIgnoreCase(ent.getArithmetic())) {
				log.info("末位剔除算法 "+fentanAmt);
				calculateBillB(invoiceSaleDetailList, fentanAmt);
			} else if ("A".equalsIgnoreCase(ent.getArithmetic()) && invoiceGoodsAmount>0) {
				log.info("销售占比分摊算法 "+fentanAmt);
				calculateBillA(invoiceSaleDetailList, fentanAmt, invoiceGoodsAmount, totalInvoiceAmount);
			}

			// 对小票明细中的金额和税额进行运算
			totalInvoiceAmount = 0.0;
			for (int i = 0; i < invoiceSaleDetailList.size(); i++) {
				InvoiceSaleDetail d = invoiceSaleDetailList.get(i);
				
				log.info("remark invoiceDetail:"+JSONObject.toJSONString(d));
				
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

				double amount = d.getAmt();
				// 计算税额
				double taxFee = MathCal.mul(amount / (1 + d.getTaxrate()), d.getTaxrate(), 2); // 税额
				totalTaxFee = MathCal.add(totalTaxFee, taxFee, 2);
				amount = MathCal.sub(amount, taxFee, 2); // 金额
				double price = amount / d.getQty(); // 单价
				d.setPrice(price);
				d.setTaxfee(taxFee);
				d.setAmount(amount);
				
				totalInvoiceAmount = MathCal.add(totalInvoiceAmount, d.getAmt(), 2);
			}
			invoiceSaleHead.setInvoiceamount(totalInvoiceAmount);
			invoiceSaleHead.setTotaltaxfee(totalTaxFee);

			invoiceSaleHead.setInvoiceSaleDetail(invoiceSaleDetailList);
			invoiceSaleHead.setInvoiceSalePay(invoiceSalePayment);
			return invoiceSaleHead;
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("小票异常:" + e.getMessage());
		}
	}
	
	/**
	 * 均摊算法
	 * 
	 * @param sell
	 * @return
	 */
	protected void calculateBillA(ArrayList<InvoiceSaleDetail> invoiceSaleDetailList, double fentanAmt,
			double invoiceGoodsAmount, double totalInvoiceAmount) {
		if (fentanAmt > 0) {
			double totalFentan = 0.00; // 分摊后金额合计
			for (int i = 0; i < invoiceSaleDetailList.size(); i++) {
				InvoiceSaleDetail d = invoiceSaleDetailList.get(i);

				if ("N".equalsIgnoreCase(d.getIsinvoice())) {// 跳过不开票商品
					continue;
				}

				double amt = d.getAmt();

				// 需要分摊，则按销售占比分摊
				double fentan = amt / invoiceGoodsAmount * fentanAmt;
				amt = MathCal.sub(amt, fentan, 2);
				totalFentan = MathCal.add(totalFentan, amt, 2);
				d.setAmt(amt);
			}
			
			//可能有尾差，算在第一个商品上
			// 最后一个商品，进行倒挤
			double daoji = MathCal.sub(totalInvoiceAmount, totalFentan, 2);
			if (daoji != 0) {
				for (int i = invoiceSaleDetailList.size()-1; i >= 0; i--) {
					InvoiceSaleDetail d = invoiceSaleDetailList.get(i);
					if ("N".equalsIgnoreCase(d.getIsinvoice())) {// 跳过不开票商品
						continue;
					}
					double amt = d.getAmt();
					amt = MathCal.add(amt, daoji, 2);
					d.setAmt(amt);
					break;
				}
			}
		}
	}

	/**
	 * 末位商品剔除法
	 * 
	 * @param sell
	 * @return
	 */
	protected void calculateBillB(ArrayList<InvoiceSaleDetail> invoiceSaleDetailList, double fentanAmt) {
		log.info("待核减金额:"+fentanAmt);
		if (fentanAmt > 0) {
			int i = invoiceSaleDetailList.size() - 1;
			while (fentanAmt > 0.00 || i == 0) {
				InvoiceSaleDetail d = invoiceSaleDetailList.get(i);
				i--;
				if ("N".equalsIgnoreCase(d.getIsinvoice())) {// 跳过不开票商品
					continue;
				}

				double amt = d.getAmt().doubleValue();
				if (amt > fentanAmt) {
					d.setAmt(MathCal.sub(amt, fentanAmt, 2));
					fentanAmt = 0.0;
				} else {
					d.setIsinvoice("N");
					fentanAmt = MathCal.sub(fentanAmt, amt, 2);
				}
				
			}
		}
	}
	
	/**
	 * 判断商品是否不开票商品
	 * 
	 * @return
	 */
	public boolean isNoTaxGoods(String entid, String goodsid) {
		Goodsdis p = new Goodsdis();
		p.setEntid(entid);
		p.setGoodsid(goodsid);
		Goodsdis obj = calculateDao.getGoodsDis(p);
		return obj == null ? false : true;
	}
	
	/**
	 * 是否剔除类别
	 * @param entid
	 * @param cateid
	 * @return
	 */
	public boolean isNoTaxCate(String entid, String cateid) {
		//强制到小类
		cateid = cateid.length()>6?cateid.substring(0, 6):cateid;
		
		Catedis p = new Catedis();
		p.setEntid(entid);
		p.setCateid(cateid);
		Catedis obj = calculateDao.getCateDis(p);
		return obj == null ? false : true;
	}

	public Taxitem getTaxitemid(String entid, String goodsid, String categoryid,double taxRate) {
		Taxitem taxitem = getTaxitemidByGoods(entid, goodsid);

		// 根据类别获取商品税目
		if (taxitem == null) {
			taxitem = getTaxitemidByCategory(entid, categoryid,taxRate);
		}
		return taxitem;
	}

	/**
	 * 根据商品获取税目
	 * 
	 * @param entid
	 * @param goodsid
	 * @return
	 */
	public Taxitem getTaxitemidByGoods(String entid, String goodsid) {
		Goodstax p = new Goodstax();
		p.setEntid(entid);
		p.setGoodsid(goodsid);
		Goodstax goods = calculateDao.getGoodstaxById(p);
		if (goods == null)
			return null;
		Taxitem item = new Taxitem();
		item.setTaxitemid(goods.getTaxitemid());
		item.setTaxrate(goods.getTaxrate()+"");
		item.setTaxpre(goods.getTaxpre());
		item.setTaxprecon(goods.getTaxprecon());
		item.setZerotax(goods.getZerotax());
		return item;
	}

	/**
	 * 根据类别获取类目信息
	 * 
	 * @param entid
	 * @param categoryid
	 * @return
	 */
	public Taxitem getTaxitemidByCategory(String entid, String categoryid,double taxRate) {
		Catetax p = new Catetax();
		p.setEntid(entid);
		p.setCateid(categoryid);
		List<Catetax> cateList = calculateDao.getCatetaxById(p);
		if (cateList.isEmpty()) {
			// 查询类别
			Category c = new Category();
			c.setEntid(entid);
			c.setCategoryid(categoryid);
			Category r = calculateDao.getCategoryById(c);
			if (r == null) {
				throw new RuntimeException("类别信息需维护：" + categoryid);
			}

			if (r.getDeptlevelid() <= 1) {
				return null;
			}

			return getTaxitemidByCategory(entid, r.getHeadcatid(), taxRate);
		}
		
		//可以配置多条类别与税目记录。如果有多条，取商品税率与配置的税率一致的记录。如果按税率都匹配不到，则取第一条记录
		Catetax catetax = null;
		if(cateList.size()==1) {
			catetax = cateList.get(0);
		}else {
			for (Catetax cate : cateList) {
				if(cate.getTaxrate()!=null  && cate.getTaxrate()==taxRate) {
					catetax = cate;
					break;
				}
			}
		}
		//如果按税率无法匹配到则取第一条
		if(catetax==null) {
			catetax = cateList.get(0);
		}
		
		Taxitem item = new Taxitem();
		item.setTaxitemid(catetax.getTaxitemid());
		item.setTaxrate(catetax.getTaxrate()+"");
		item.setTaxpre(catetax.getTaxpre());
		item.setTaxprecon(catetax.getTaxprecon());
		item.setZerotax(catetax.getZerotax());
		return item;
	}

	public Paymode getPaymodeById(String entid, String payid) {
		Paymode p = new Paymode();
		p.setEntid(entid);
		p.setPayid(payid);
		return calculateDao.getPaymodeById(p);
	}
	
	protected void checkAmt(RequestBillInfo bill,ResponseBillInfo head){
		if(bill.getSheettype().equals("5")) return;
		
		if ("wx".equals(bill.getChannel())) {
			//限定开票日期
			Date sdate;
			try {
				//小票交易日期
				sdate = (new SimpleDateFormat("yyyyMMdd")).parse(head.getTradedate());
				//配置的过期天数
				String limitBillDay = entPrivatepara.get(bill.getEntid(), "limitBillDay");
				int limit = 10;//默认10天
				if(limitBillDay!=null && !StringUtils.isEmpty(limitBillDay)){
					limit = Integer.parseInt(limitBillDay);
				}
				//截止时间
				long expd = 24L*60*60*1000 *limit + sdate.getTime();
				long current = System.currentTimeMillis();
				
				if(current > expd){
					String msg = "[提取码已过期]<br>交易日期:"+Convert.dateFormat(sdate, "yyyy-MM-dd")+"<br>截止日期:"+Convert.dateFormat(new Date(expd), "yyyy-MM-dd");
					throw new LocalRuntimeException(msg,msg,LocalRuntimeException.DATA_MESSAGE);
				}
			} catch (ParseException e) {
				log.error(e,e);
			}
			
			log.debug("bill:"+JSONObject.toJSONString(bill));
			log.debug("head:"+JSONObject.toJSONString(head));
			
			double je = bill.getJe();
			
			if(Math.abs(je - head.getTotalamount()) > FGlobal.amtRange){
				throw new LocalRuntimeException("金额校验未通过:"+bill.getSheetid(),"提取码错误",LocalRuntimeException.DAT_AMT_ERROR);
			}
		}
	}
	
	/**
	 * 验证门店是否开通电票
	 * @param bill
	 */
	protected void checkIseinvoice(RequestBillInfo bill){
		if ("wx".equals(bill.getChannel())) {
			//判断门店是否启用电票
			Shop shop = new Shop();
			shop.setShopid(bill.getShopid());
			shop.setEntid(bill.getEntid());
			try {
				shop = shopService.getShopById(shop);
			} catch (Exception e) {
			}
			if(shop==null || shop.getIsEinvoice()!=1){
				throw new RuntimeException("[尚未开通电子发票]<br>请至门店换取纸票");
			}
		}
	}
	
	@Override
	public List<HashMap<String, String>> getInvoiceBillDetailList(Map<String, Object> requestParams) {
		return invoiceSaleDao.getInvoiceBillDetailList(requestParams);
	}
	
	@Override
	public int getInvoiceBillDetailListCount(Map<String, Object> p) {
		return invoiceSaleDao.getInvoiceBillDetailListCount(p);
	}
	
	public List<InvoiceSaleDetail> cookOpenInvoiceDetail(Invque que){
		List<InvqueList> list = que.getInvqueList();
		if(list==null || list.isEmpty()){
			throw new RuntimeException("开票明细数据空");
		}
		
		double sumJe = 0.0;
		double sumSe = 0.0;
		
		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		for (InvqueList invqueList : list) {
			RequestBillInfo bill = new RequestBillInfo();
			bill.setEntid(que.getIqentid());
			bill.setSheettype(invqueList.getSheettype());
			bill.setSheetid(invqueList.getSheetid());
			
			List<InvoiceSaleDetail> detail = invoiceSaleDao.getInvoiceSaleDetail(bill);
			if(detail==null || detail.isEmpty()){
				throw new RuntimeException("开票明细数据获取失败："+invqueList.getSheetid());
			}
			
			//将税目编码指向商品编码
			int i =1;
			double sumAmt = 0.0;
			double sumInvoiceAmt = 0.0;
			double sumUnPay = 0.0;
			double sumdkspvalue = 0.0;
			for (InvoiceSaleDetail invoiceSaleDetail : detail) {
				if(que.getIqsource().equals("4")) {
					//4业务类型按行开票
					int rowNo = invoiceSaleDetail.getRowno();
					boolean match = false;
					for (InvqueListDetail invqueListDetail : invqueList.getListDetail()) {
						if(invqueListDetail.getRowno()==rowNo) {
							match=true;
							break;
						}
					}
					if(!match) {
						continue;
					}
				}
				
				invoiceSaleDetail.setRowno(i);
				invoiceSaleDetail.setGoodsid(invoiceSaleDetail.getGoodsid());
				
				//如果税率是0，又没有指定0税率标识，则默认为普通0税率，zerotax=3
				if(invoiceSaleDetail.getTaxrate()==0 && StringUtils.isEmpty(invoiceSaleDetail.getZerotax())) {
					invoiceSaleDetail.setZerotax("3");
				}
				
				detailList.add(invoiceSaleDetail);
				i++;
				if(invoiceSaleDetail.getTaxitemid()==null) invoiceSaleDetail.setTaxitemid("");
				
				if("Y".equals(invoiceSaleDetail.getIsinvoice())){
					sumSe = MathCal.add(sumSe, invoiceSaleDetail.getTaxfee(), 2);
					sumJe = MathCal.add(sumJe, invoiceSaleDetail.getAmount(), 2);
					sumInvoiceAmt = MathCal.add(sumInvoiceAmt, invoiceSaleDetail.getTaxfee()+invoiceSaleDetail.getAmount(), 2);
				} 
				
				if("".equals(invoiceSaleDetail.getTaxitemid())){
					sumdkspvalue = MathCal.add(sumdkspvalue, invoiceSaleDetail.getOldamt(), 2);
				}
				sumAmt = MathCal.add(sumAmt, invoiceSaleDetail.getOldamt(), 2);
			}
			
			//银座需要提供支付信息
			if(que.getIqentid().equals("SDYZ")) {

				//支付信息
				String notePay = "";
				List<InvoiceSalePay> paylist = invoiceSaleDao.getInvoiceSalePay(bill);
				if(paylist!=null && !paylist.isEmpty()){
					List<InvoiceSalePay> cookList = new ArrayList<InvoiceSalePay>();
					for (InvoiceSalePay invoiceSalePay : paylist) {
						boolean hasJoin = false;
						for (InvoiceSalePay cookItem : cookList) {
							if(invoiceSalePay.getEntid().equals(cookItem.getEntid())&&invoiceSalePay.getSheetid().equals(cookItem.getSheetid())&&invoiceSalePay.getPayid().equals(cookItem.getPayid())){
								hasJoin = true;
								cookItem.setAmt(invoiceSalePay.getAmt()+cookItem.getAmt());
							}
						}
						
						if(!hasJoin) {
							cookList.add(invoiceSalePay);
						}
					
					}
					for (InvoiceSalePay cookItem : cookList) {
						if(!"Y".equalsIgnoreCase(cookItem.getIsinvoice())){
							sumUnPay = MathCal.add(sumUnPay, cookItem.getAmt(), 2);
							notePay+=cookItem.getPayname()+": "+cookItem.getAmt()+";";
						}
					}
				}
				double unAmt = sumUnPay>sumdkspvalue?sumUnPay:sumdkspvalue;
				String payNote = "小票总金额："+sumAmt+"，发票金额："+sumInvoiceAmt+"，不可开票金额："+unAmt;
				if(sumUnPay>sumdkspvalue){
					payNote+="，其中不可开票支付方式："+notePay;
				}else{
					payNote+="，其中代开票商品金额："+unAmt;
				}
				
				que.setPaynote(payNote);
			}else{
				//备注中是否显示payNOTE
				String paynote = entPrivatepara.get(bill.getEntid(), "payNote");
				if(paynote==null) paynote="";
				if("1".equals(paynote)){
					if(list.size()==1){
						//支付信息
						String notePay = "";
						List<InvoiceSalePay> paylist = invoiceSaleDao.getInvoiceSalePay(bill);
						if(paylist!=null && !paylist.isEmpty()){
							for (InvoiceSalePay invoiceSalePay : paylist) {
								if(!"Y".equalsIgnoreCase(invoiceSalePay.getIsinvoice())){
									sumUnPay = MathCal.add(sumUnPay, invoiceSalePay.getAmt(), 2);
									notePay+=invoiceSalePay.getPayname()+": "+invoiceSalePay.getAmt()+";";
								}
							}
						}
						double unAmt = sumUnPay>sumdkspvalue?sumUnPay:sumdkspvalue;
						String payNote = "小票总金额："+sumAmt+"，发票金额："+sumInvoiceAmt+"，不可开票金额："+unAmt;
	/*					if(sumUnPay>sumdkspvalue){
							payNote+="，其中不可开票支付方式："+notePay;
						}else{
							payNote+="，其中代开票商品金额："+unAmt+".";
						}*/
						ResponseBillInfo head =  invoiceSaleDao.getInvoiceSaleHead(bill);
						if(head!=null){
							payNote +="交易日期:"+head.getTradedate();
						}
						payNote +="提取码"+head.getShopid()+head.getSyjid()+head.getBillno();
						//if("1".equals(paynote)){
							que.setPaynote(payNote);
							
				/*		}else
						if("2".equals(paynote)){
							que.setIqmemo(payNote);
							System.out.println(que.getIqmemo());
						}*/
					}
				}
			}
		}
		
		que.setIqtotje(sumJe);
		que.setIqtotse(sumSe);
		
		if(detailList==null || detailList.isEmpty()){
			throw new RuntimeException("开票明细数据空");
		}
		
		return detailList;
	}
	
	public String cookString(String s){
		if(s==null) return "";
		String res = "";
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] >= 19968 && chars[i] <= 40869) 
					|| (chars[i] >= 97 && chars[i] <= 122)
					|| (chars[i] >= 65 && chars[i] <= 90)
					|| (chars[i] >= 48 && chars[i] <= 57)) {
				res += chars[i];
			}
		}
		return res;
	}
	
	public List<ResponseBillInfo> getInvoiceBillInfoByGmfNo(RequestBillInfo bill){
		List<ResponseBillInfo> head = invoiceSaleDao.getInvoiceSaleHeadByGmfNo(bill);
		for (ResponseBillInfo responseBillInfo : head) {
			bill.setSheetid(responseBillInfo.getSheetid());
			List<InvoiceSaleDetail> detail = invoiceSaleDao.getInvoiceSaleDetail(bill);
			responseBillInfo.setInvoiceSaleDetail(detail);
		}
		return head;
	}
	
	public List<ResponseBillInfo> getInvoiceSaleHeadList(RequestBillInfo requestParams){
		return invoiceSaleDao.getInvoiceSaleHeadList(requestParams);
	}

	public String getSHEET_TYPE() {
		return SHEET_TYPE;
	}

	public String getSHEET_NAME() {
		return SHEET_NAME;
	}
	
}
