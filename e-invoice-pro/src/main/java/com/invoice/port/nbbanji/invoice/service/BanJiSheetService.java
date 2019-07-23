package com.invoice.port.nbbanji.invoice.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.dao.SheetInvqueBJDao;
import com.invoice.apiservice.service.impl.PrivateparaService;
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
import com.invoice.bean.db.Paymode;
import com.invoice.bean.db.SheetDetail;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.SheetPayment;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.port.nbbanji.invoice.bean.InvoiceInvqueReturn;
import com.invoice.port.nbbanji.invoice.bean.OrderQueryReceiveBean;
import com.invoice.port.nbbanji.invoice.bean.ResponseBillInfoBJ;
import com.invoice.uiservice.service.EnterpriseService;
import com.invoice.util.Convert;
import com.invoice.util.MathCal;
import com.invoice.util.Serial;

//阪急定制化小票提取方法 on 2019.06.10

@Service("BJSheetService")
public class BanJiSheetService {
	static public final Log log = LogFactory.getLog(BanJiSheetService.class);
	String sheetName = "NBBJ";
	
	@Autowired
	InvoiceSaleDao invoiceSaleDao;
	
	@Autowired
	PrivateparaService privateparaService;
	
	@Autowired
	EnterpriseService enterpriseService;
	
	@Autowired
	CalculateDao calculateDao;
	
	@Autowired
	InvqueDao inqueDao;
	
	@Autowired
	EntPrivatepara entPrivatepara;
	
	@Autowired
	SheetInvqueBJDao sheetInvqueBJDao;
	
	// 主函数，用于外部调用
	public List<ResponseBillInfoBJ> getInvoiceSheetInfo(OrderQueryReceiveBean bill) {
		try {
			String serialId = null,logInfo = "",entid = "", shopid = "";
			
			entid = bill.getEntId(); shopid = bill.getShopId();
			
			// 初始化远程连接
			CClient client =  privateparaService.getClientUrlByShopid(entid, shopid);
			
			if(client == null){
				if (shopid == null || shopid.isEmpty())
					logInfo = "阪急－数据不存在entid:"+entid;
				else
					logInfo = "阪急－不存在此门店shopid:"+shopid;
				
				log.info(logInfo);
				
				return null;
			}
		
			client.initHeadMap();
			
			//获取开具小票
			if (bill.getDpType().equals("电票")&&bill.getRetFlag().equals("0"))
			{
				RequestBillInfo myReqDP = new RequestBillInfo();
				
				myReqDP.setEntid(bill.getEntId());
				myReqDP.setSyjid(bill.getClientId());
				myReqDP.setSheettype("1");
				myReqDP.setShopid(bill.getShopId());
				myReqDP.setBillno(bill.getSheetId());
				
				ResponseBillInfoBJ head = sheetInvqueBJDao.getSheetSaleHeadBJ(myReqDP);

				if(head!=null && head.getFlag()==-1){
					serialId = head.getSerialid();
					InvoiceSaleHead saleHead = new InvoiceSaleHead();
					saleHead.setSheettype(head.getSheettype());
					saleHead.setSheetid(head.getSheetid());
					saleHead.setEntid(head.getEntid());
					saleHead.setSerialid(head.getSerialid());
					saleInvoice2Delete(saleHead);
					head = null;
				}
				
				// 本地没有则向小票服务查询
				if (head == null) {
					Map<String, String> dpMap = client.getHeadMap();
					dpMap.put("shopid", myReqDP.getShopid());
					dpMap.put("syjid", myReqDP.getSyjid());
					dpMap.put("billno", myReqDP.getBillno());
					dpMap.put("sheetname", "NBBJ");
					
					client.setHeadMap(dpMap);
					
					String res = client.getMessage("getSheetSum");
					
					JSONObject sheetJO = JSONObject.parseObject(res);
					
					if(sheetJO.getIntValue("code") !=0 ){

						logInfo = "获取数据异常:"+client.getClientid() + "," + sheetJO.getString("message");
						
						log.info(logInfo);
						
						return null;
					}
					
					SheetHead sheetSell = JSONObject.parseObject(sheetJO.getString("data"), SheetHead.class);
					
					if(sheetSell== null){
						logInfo = "阪急－远程数据未找到:" + myReqDP.getSheetid();
						log.info(logInfo);
						return null;
					}
					
					// 远端门店必须和请求的门店信息一致
					if(!sheetSell.getShopid().equals(myReqDP.getShopid())){
						logInfo = "阪急－数据门店信息异常 ，要求门店" + sheetSell.getShopid()+"与返回门店" + myReqDP.getShopid() + "不一样";
						log.info(logInfo);
						return null;
					}
					
					sheetSell.setEntid(myReqDP.getEntid());
					
					// 查到小票后，对小票进行运算
					InvoiceSaleHead saleHead = calculateSheet(sheetSell, serialId, myReqDP.getSheettype());
					saleHead.setCreatetime(new Date());
					saleHead.setFlag(0);
					saleHead.setIsauto(0);
					saleHead.setInvoicelx("10");
					saleHead.setIqseqno(Serial.getInvqueSerial());
					
					// 写入小票表
					saleInvoice2Insert(saleHead);
					// 写入发票队列表
					insertInvque(saleHead,bill.getTaxNo());

					head = sheetInvqueBJDao.getSheetSaleHeadBJ(myReqDP);
				}

				if (head == null) {
					logInfo = "阪急－数据未找到" + myReqDP.getSheetid() + ",提取码错误";
					log.info(logInfo);
					return null;
				}
				
				if(head.getTaxno() == null||head.getTaxno().isEmpty()){
					logInfo = "阪急－门店纳税信息不存在:"+ head.getShopid();
					log.info(logInfo);
					return null;
				}
				
				myReqDP.setSheetid(head.getSheetid());
				
				
				List<InvoiceSaleDetail> detail = invoiceSaleDao.getInvoiceSaleDetail(myReqDP);
				
				head.setInvoiceSaleDetail(detail);
				
				head.setKpr(entPrivatepara.get(head.getEntid(), FGlobal.WeixinKPR));
				head.setSkr(entPrivatepara.get(head.getEntid(), FGlobal.weixinSKR));
				head.setFhr(entPrivatepara.get(head.getEntid(), FGlobal.WeixinFHR));

				//数据转换
				List<ResponseBillInfoBJ> sheetBJ = new ArrayList<ResponseBillInfoBJ>();

				sheetBJ.add(head);
				
				return sheetBJ;
			}
			
			//获取开具费用数据
			if (bill.getDpType().equals("纸票")&&bill.getRetFlag().equals("0")) 
			{
				//先根据条件从ERP获取退货清单
				Map<String, String> zpMap = client.getHeadMap();
				zpMap.put("shopid", bill.getShopId());
				zpMap.put("sheetid", bill.getSheetId());
				zpMap.put("begdate", bill.getDdRQq());
				zpMap.put("enddate", bill.getDdRQz());
				zpMap.put("sheetname", "NBBJ");
				
				client.setHeadMap(zpMap);
				
				String billList =  client.getMessage("getBillList");
				
				JSONObject zpJO = JSONObject.parseObject(billList);
				
				if(zpJO.getIntValue("code") !=0 ){

					logInfo = "阪急－获取数据异常:"+client.getClientid() + "," + zpJO.getString("message");
					
					log.info(logInfo);
					
					return null;
				}
				
				List<ResponseBillInfoBJ> provBill = JSONObject.parseArray(zpJO.getString("data"), ResponseBillInfoBJ.class);
				
				List<ResponseBillInfoBJ> provBJ = new ArrayList<ResponseBillInfoBJ>();
				
				for(ResponseBillInfoBJ myres : provBill)
				{
					RequestBillInfo myReqZP = new RequestBillInfo();
					
					myReqZP.setSyjid(myres.getSyjid());
					myReqZP.setEntid(myres.getEntid());
					myReqZP.setSheetid(myres.getSheetid());
					myReqZP.setSheettype(myres.getSheettype());
					myReqZP.setShopid(myres.getShopid());
					myReqZP.setBillno(myres.getSheetid());
					
					ResponseBillInfoBJ head = sheetInvqueBJDao.getSheetSaleHeadBJ(myReqZP);

					if(head!=null && head.getFlag()==-1){
						serialId = head.getSerialid();
						InvoiceSaleHead saleHead = new InvoiceSaleHead();
						saleHead.setSheettype(head.getSheettype());
						saleHead.setSheetid(head.getSheetid());
						saleHead.setEntid(head.getEntid());
						saleHead.setSerialid(head.getSerialid());
						saleInvoice2Delete(saleHead);
						head = null;
					}
					
					// 本地没有则向小票服务查询
					if (head == null) {
						Map<String, String> provMap = client.getHeadMap();
						provMap.put("shopid", myReqZP.getShopid());
						provMap.put("sheetid", myReqZP.getSheetid());
						provMap.put("sheettype", myReqZP.getSheettype());
						provMap.put("sheetname", "NBBJ");
						
						client.setHeadMap(provMap);
						
						String prov = client.getMessage("getProvSheetSum");
						
						JSONObject provJO = JSONObject.parseObject(prov);
						
						if(provJO.getIntValue("code") !=0 ){

							logInfo = "阪急－获取数据异常:"+client.getClientid() + "," + provJO.getString("message");
							
							log.info(logInfo);
							
							return null;
						}
						
						SheetHead provSell = JSONObject.parseObject(provJO.getString("data"), SheetHead.class);
						
						if(provSell== null){
							logInfo = "阪急－远程数据未找到:" + myReqZP.getSheetid();
							log.info(logInfo);
							return null;
						}
						
						// 远端门店必须和请求的门店信息一致
						if(!provSell.getShopid().equals(myReqZP.getShopid())){
							logInfo = "阪急－数据门店信息异常 ，要求门店" + provSell.getShopid() + "与返回门店" + myReqZP.getShopid() + "不一样";
							log.info(logInfo);
							return null;
						}
						
						provSell.setEntid(myReqZP.getEntid());
						
						// 查到小票后，对小票进行运算
						InvoiceSaleHead provHead = calculateSheet(provSell, serialId, myReqZP.getSheettype());
						
						provHead.setCreatetime(new Date());
						provHead.setFlag(0);
						provHead.setSyjid("1");
						provHead.setIsauto(0);
						provHead.setBillno(provSell.getSheetid());
						provHead.setInvoicelx(myres.getInvoicelx());
						provHead.setIqseqno(Serial.getInvqueSerial());
						SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
						provHead.setTradedate(f.format(provSell.getTradedate()));

						// 写入小票表
						saleInvoice2Insert(provHead);
						// 写入发票队列表
						insertInvque(provHead,bill.getTaxNo());

						head = sheetInvqueBJDao.getSheetSaleHeadBJ(myReqZP);
					}

					if (head == null) {
						logInfo = "阪急－数据未找到" + myReqZP.getSheetid() + ",提取码错误";
						log.info(logInfo);
						return null;
					}
					
					if(head.getTaxno()== null||head.getTaxno().isEmpty()){
						logInfo = "阪急－门店纳税信息不存在:"+ head.getShopid();
						log.info(logInfo);
						return null;
					}
					
					List<InvoiceSaleDetail> detail = invoiceSaleDao.getInvoiceSaleDetail(myReqZP);
					
					head.setInvoiceSaleDetail(detail);
					
					head.setKpr(entPrivatepara.get(head.getEntid(), FGlobal.WeixinKPR));
					head.setSkr(entPrivatepara.get(head.getEntid(), FGlobal.weixinSKR));
					head.setFhr(entPrivatepara.get(head.getEntid(), FGlobal.WeixinFHR));
					
					provBJ.add(head);
				}
				
				return provBJ;
			}

			// 电票+纸票的退货统一处理
			if (bill.getRetFlag().equals("1"))
			{
				String res = "";
				
				//先根据条件从ERP获取退货清单
				Map<String, String> retMap = client.getHeadMap();
				retMap.put("shopid", bill.getShopId());
				retMap.put("begdate", bill.getDdRQq());
				retMap.put("enddate", bill.getDdRQz());
				retMap.put("sheetname", "NBBJ");
				
				client.setHeadMap(retMap);

				if (bill.getDpType().equals("电票"))
					res = client.getMessage("getHeadRetList"); // 小票退货清单
				else
					res = client.getMessage("getProvRetList"); // 费用退货清单
				
				JSONObject returnJO = JSONObject.parseObject(res);
				
				if(returnJO.getIntValue("code") != 0 ){

					logInfo = "阪急－获取数据异常:"+client.getClientid() + "," + returnJO.getString("message");
					
					log.info(logInfo);
					
					return null;
				}

				List<ResponseBillInfoBJ> retSell = JSONObject.parseArray(returnJO.getString("data"), ResponseBillInfoBJ.class);
				
				if (retSell == null || retSell.size() == 0 )
				{
					logInfo = "阪急－没有获取到数据:" + bill.getDdRQq() + "," + bill.getDdRQz();
					
					log.info(logInfo);
					
					return null;
				}
				
				List<ResponseBillInfoBJ> sellRes = new ArrayList<ResponseBillInfoBJ>();
				
				// 根据退货清单，如果已提取，则会生成退货数据给到航信
				for(ResponseBillInfoBJ myres : retSell)
				{
					Map<String,String> returnMap = new HashMap<String,String>();
					returnMap.put("entid", bill.getEntId());
					returnMap.put("sheetid",myres.getRefsheetid());
					
					if (bill.getDpType().equals("纸票"))
						returnMap.put("sheettype", "7");
					else
						returnMap.put("sheettype", "1");
					
					// 先找一下原小票是否已经同步过来
					ResponseBillInfoBJ myOrig = sheetInvqueBJDao.getSheetHeadOriginal(returnMap);
					
					// 同步原小票数据
					if (myOrig == null) {
						//先根据条件从ERP获取退货清单
						Map<String, String> syMap = client.getHeadMap();
						syMap.put("shopid", myres.getShopid());
						syMap.put("sheetid", myres.getRefsheetid());
						syMap.put("sheetname", "NBBJ");

						if (bill.getDpType().equals("电票"))
							res = client.getMessage("getSheetBJ");
						else
							res = client.getMessage("getProvSheetBJ");			

						JSONObject syncJO = JSONObject.parseObject(res);
						
						if(syncJO.getIntValue("code") != 0 ){

							logInfo = "阪急－获取原小票数据异常:"+client.getClientid() + "," + syncJO.getString("message");
							
							log.info(logInfo);
							
							return null;
						}
						
						SheetHead syncSell = JSONObject.parseObject(syncJO.getString("data"), SheetHead.class);
						
						if(syncSell == null){
							logInfo = "阪急－远程原小票数据未找到:" + myres.getRefsheetid();
							log.info(logInfo);
							return null;
						}
						
						// 远端门店必须和请求的门店信息一致
						if(!syncSell.getShopid().equals(myres.getShopid())){
							logInfo = "阪急－数据门店信息异常 ，要求门店" + syncSell.getShopid()+"与返回门店" + myres.getShopid() + "不一样";
							log.info(logInfo);
							return null;
						}
						
						syncSell.setEntid(bill.getEntId());
						
						// 查到小票后，对小票进行运算
						InvoiceSaleHead syncHead = calculateSheet(syncSell, serialId, returnMap.get("sheettype"));
						syncHead.setCreatetime(new Date());
						syncHead.setFlag(0);
						syncHead.setIsauto(0);
						syncHead.setInvoicelx("10");
						syncHead.setIqseqno("");
						
						// 写入小票表
						saleInvoice2Insert(syncHead);

						myOrig = sheetInvqueBJDao.getSheetHeadOriginal(returnMap);
					}
					
					if (myOrig == null) continue;

					//先查退货表中有没有数据，如没有则进行保存
					returnMap.put("sheetid",myres.getSheetid());
					
					InvoiceInvqueReturn myReturn = sheetInvqueBJDao.getSheetHeadReturn(returnMap);

					if (myReturn == null)
					{
						myReturn = new InvoiceInvqueReturn();
						
						myReturn.setBillno(myres.getBillno());
						myReturn.setEntid(myOrig.getEntid());
						myReturn.setSheetid(myres.getSheetid());
						myReturn.setSheettype(myOrig.getSheettype());
						myReturn.setHzfpxxbbh(myres.getGmfno());
						myReturn.setInvoicelx(myOrig.getInvoicelx());
						myReturn.setIqfplxdm(myOrig.getInvoicelx());
						myReturn.setIqgmfadd(myOrig.getGmfadd() );
						myReturn.setIqgmfbank(myOrig.getGmfbank());
						myReturn.setIqgmfname(myOrig.getGmfname());
						myReturn.setIqgmftax(myOrig.getGmftax());
						myReturn.setIqstatus("10");
						myReturn.setIqtype("1");
						myReturn.setRefbillno(myOrig.getBillno());
						myReturn.setRefinvoicecode(myOrig.getRtfpdm());
						myReturn.setRefinvoicedate(myOrig.getRtkprq());
						myReturn.setRefinvoiceno(myOrig.getRtfphm());
						myReturn.setRefsheetid(myOrig.getSheetid());
						myReturn.setRefshopid(myOrig.getShopid());
						myReturn.setRefsyjid(myOrig.getSyjid());
						myReturn.setSdate(myres.getTradedate()== null?stampToDate(myOrig.getTradedate()):stampToDate(myres.getTradedate()));
						myReturn.setShopid(myres.getShopid());
						myReturn.setStatus("0");
						myReturn.setSyjid(myres.getSyjid());
						myReturn.setZsfs("0");
					
						sheetInvqueBJDao.insertInvoiceInvqueReturn(myReturn);	
					}
					else
					{
						if (myReturn.getStatus().equals("1")) continue;
					}

					myres.setTaxno(myOrig.getTaxno());
					myres.setTaxadd(myOrig.getTaxadd());
					myres.setTaxbank(myOrig.getTaxbank());
					myres.setTaxname(myOrig.getTaxname());
					myres.setGmfadd(myOrig.getGmfadd());
					myres.setGmfbank(myOrig.getGmfbank());
					myres.setGmfname(myOrig.getGmfname());
					myres.setGmftax(myOrig.getGmftax());
					myres.setKpr(myOrig.getKpr());
					myres.setFhr(myOrig.getFhr());
					myres.setSkr(myOrig.getSkr());
					myres.setHzfpxxbbh(myres.getGmfno());
					myres.setYskt(myOrig.getSyjid());
					myres.setYmdbh(myOrig.getShopid());
					myres.setYfpdm(myOrig.getRtfpdm());
					myres.setYfphm(myOrig.getRtfphm());
					myres.setYxsdjbh(myOrig.getBillno());
					myres.setRefsheetid(myOrig.getSheetid());
					myres.setTotalamount(-1*myOrig.getTotalamount());
					myres.setInvoiceamount(-1*myOrig.getInvoiceamount());
					myres.setTotaltaxfee(-1*myOrig.getTotaltaxfee());
					myres.setTradedate(stampToDate(myres.getTradedate()));
					
					RequestBillInfo myReqFP = new RequestBillInfo();
					myReqFP.setSyjid(myOrig.getSyjid());
					myReqFP.setEntid(myOrig.getEntid());
					myReqFP.setSheetid(myOrig.getSheetid());
					myReqFP.setSheettype(myOrig.getSheettype());
					myReqFP.setShopid(myOrig.getShopid());
					myReqFP.setBillno(myOrig.getSheetid());
					
					List<InvoiceSaleDetail> detail = invoiceSaleDao.getInvoiceSaleDetail(myReqFP);
					
					for (InvoiceSaleDetail myDetl : detail)
					{
						myDetl.setQty(-1*(myDetl.getQty()==null?0.00:myDetl.getQty()));
						myDetl.setOldamt(-1*(myDetl.getOldamt()==null?0.00:myDetl.getOldamt()));
						myDetl.setAmount(-1*(myDetl.getAmount()==null?0.00:myDetl.getAmount()));
						myDetl.setAmt(-1*(myDetl.getAmt()==null?0.00:myDetl.getAmt()));
						myDetl.setTaxfee(-1*(myDetl.getTaxfee()==null?0.00:myDetl.getTaxfee()));
					}
					
					myres.setInvoiceSaleDetail(detail);
					
					sellRes.add(myres);
				}

				return sellRes;
			}
			
			return null;
		} 
		catch (Exception ex) {
			log.error("阪急－调取小票信息出错：" + ex.toString());
			return null;
		}
	}

	// 计算小票信息
	public InvoiceSaleHead calculateSheet(SheetHead sell, String serialid, String sheettype) {
		try {
			String logInfo = "";
			
			InvoiceSaleHead invoiceSaleHead = new InvoiceSaleHead();

			String entid = sell.getEntid();

			Enterprise enterprise = new Enterprise();
			enterprise.setEntid(entid);
			Enterprise ent = enterpriseService.getEnterpriseById(enterprise);

			if (ent == null) {
				logInfo = "阪急－企业不存在：" + entid;
				log.info(logInfo);
				return null;
			}
			
			if(serialid== null) serialid = Serial.getSheetSerial();
			
			String shopid = sell.getShopid();
			String syjid = sell.getSyjid();
			String billno = sell.getBillno();
			String sheetid = sell.getSheetid();
			
			invoiceSaleHead.setSerialid(serialid);
			invoiceSaleHead.setEntid(entid);
			invoiceSaleHead.setSheetid(sheetid);
			invoiceSaleHead.setSheettype(sheettype);
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

			// 小票明细信息
			List<SheetDetail> detail = sell.getSheetdetail();

			if (detail == null || detail.isEmpty()) {
				logInfo = "阪急－小票明细缺失:" + sheetid;
				log.info(logInfo);
				return null;
			}

			ArrayList<InvoiceSaleDetail> invoiceSaleDetailList = new ArrayList<InvoiceSaleDetail>();

			double totalGooodsAmount = 0.00; 	// 商品合计金额
			double invoiceGoodsAmount = 0.00; 	// 可开票商品金额
			double unInvoiceGoodsAmount = 0.00; // 不可开票商品金额

			int rows = 1;
			
			for (SheetDetail sheetDetail : detail) {
				logInfo = "阪急－sheetDetail:"+JSONObject.toJSONString(sheetDetail);
				
				log.info(logInfo);
				
				totalGooodsAmount = MathCal.add(totalGooodsAmount, sheetDetail.getAmt(), 2);

				String goodsid = sheetDetail.getItemid();

				InvoiceSaleDetail invoiceDetail = new InvoiceSaleDetail();
				invoiceDetail.setSerialid(serialid);
				invoiceDetail.setEntid(entid);
				invoiceDetail.setSheetid(invoiceSaleHead.getSheetid());
				invoiceDetail.setSheettype(sheettype);
				invoiceDetail.setGoodsid(goodsid);
				
				// 商品名称特殊字符处理
				String goodsName = sheetDetail.getItemname();
				goodsName = cookString(goodsName);
				invoiceDetail.setGoodsname(goodsName);
				invoiceDetail.setRowno(sheetDetail.getRowno()== null||sheetDetail.getRowno()==0?rows++ : sheetDetail.getRowno());
				invoiceDetail.setTradedate(sell.getSdate());
				
				// 销售数量
				invoiceDetail.setQty(sheetDetail.getQty());
				invoiceDetail.setUnit(sheetDetail.getUnit());
				invoiceDetail.setSpec(sheetDetail.getSpec());
				invoiceDetail.setAmt(sheetDetail.getAmt());
				invoiceDetail.setOldamt(sheetDetail.getAmt());
				invoiceDetail.setTaxrate(sheetDetail.getTaxrate());

				// 剔除商品，则标记为不开票 and 如果金额和数量为0则标记为不可开票
				String categoryid = sheetDetail.getCategoryid();
				
				invoiceDetail.setCategoryid(categoryid);
				
				if (isNoTaxCate(entid, categoryid) || isNoTaxGoods(entid, goodsid)) {
					invoiceDetail.setIsinvoice("N");
					unInvoiceGoodsAmount = MathCal.add(unInvoiceGoodsAmount, sheetDetail.getAmt(), 2);
				} 
				else {
					invoiceDetail.setIsinvoice("Y");

					// 检查商品名称和数量
					if (sheetDetail.getItemname() == null || sheetDetail.getItemname().isEmpty()) {
						logInfo = "阪急－小票：" + invoiceSaleHead.getSheetid() + "没有商品名称：" + goodsid;
						log.info(logInfo);
						return null;
					}

					if (sheetDetail.getQty() == null) {
						logInfo = "阪急－小票：" + invoiceSaleHead.getSheetid() + "没有销售数量：" + goodsid;
						log.info(logInfo);
						return null;
					}

					if (sheetDetail.getTaxrate() == null || sheetDetail.getTaxrate() < 0 || sheetDetail.getTaxrate() > 0.17) {
						logInfo = "阪急－小票：" + invoiceSaleHead.getSheetid() + "商品税率异常：" + goodsid;
						log.info(logInfo);
						return null;
					}

					String taxitemid = sheetDetail.getTaxitemid();
					
					// 如果前端指定了税目，则以前端指定的税目为准
					if(taxitemid !=null && !taxitemid.isEmpty() && taxitemid.trim().length() != 0) {
						invoiceDetail.setTaxpre("0");
						invoiceDetail.setTaxprecon("");
						invoiceDetail.setZerotax("");
					}else {
						// 根据商品编码查询对应税目
						Taxitem taxitem = getTaxitemid(entid, goodsid, categoryid,sheetDetail.getTaxrate());

						if (taxitem == null) {
							logInfo = "阪急－小票：" + invoiceSaleHead.getSheetid() + "商品：" + goodsid + "税目缺失类别：" + categoryid;
							log.info(logInfo);
							return null;
						} else {
							taxitemid = taxitem.getTaxitemid();
						}
						
						invoiceDetail.setTaxpre(taxitem.getTaxpre()== null?"0":taxitem.getTaxpre());
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
				
				if (invoiceDetail.getTradedate()== null) invoiceDetail.setTradedate(sell.getTradedate());
				
				invoiceSaleDetailList.add(invoiceDetail);
			}
			
			// 支付信息
			List<SheetPayment> payList = sell.getSheetpayment();

			ArrayList<InvoiceSalePay> invoiceSalePayment = new ArrayList<InvoiceSalePay>();
			
			double totalPayAmount = 0.00; 		// 支付合计金额
			double invoicePayAmount = 0.00; 	// 可开票支付金额
			double unInvoicePayAmount = 0.00;	//不可开票支付金额
			
			if (payList == null) 
			{
				payList = new ArrayList<SheetPayment>();
				totalPayAmount = totalGooodsAmount;  	// 支付合计金额
				invoicePayAmount = invoiceGoodsAmount; 	// 可开票支付金额
			}
			
			rows = 1;
			
			for (SheetPayment sheetPayment : payList) {
				String payid = sheetPayment.getPayid();
				
				double amt = sheetPayment.getAmt();

				InvoiceSalePay pay = new InvoiceSalePay();

				pay.setSerialid(serialid);
				pay.setEntid(entid);
				pay.setSheetid(sheetid);
				pay.setRowno(sheetPayment.getRowno());
				pay.setPayid(payid);
				pay.setSheettype(sheettype);
				pay.setAmt(amt);
				pay.setRowno(sheetPayment.getRowno()== null||sheetPayment.getRowno()==0? rows++ : sheetPayment.getRowno());

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
				}
				else {
					invoicePayAmount = MathCal.add(invoicePayAmount, amt, 2);
					pay.setIsinvoice("Y");
					String tmp = sheetPayment.getPayname();
					
					if(tmp== null||tmp.isEmpty()) tmp=payid;
					pay.setPayname(tmp);
				}
				
				invoiceSalePayment.add(pay);
			}

			//如果没有指定合计金额，则从明细汇总
			if(invoiceSaleHead.getTotalamount()== null){
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
			} 
			else {
				totalInvoiceAmount = invoiceGoodsAmount;
			}

			// 计算分摊或剔除商品
			if ("B".equalsIgnoreCase(ent.getArithmetic())) {
				log.info("末位剔除算法 "+fentanAmt);
				calculateBillB(invoiceSaleDetailList, fentanAmt);
			} else if ("A".equalsIgnoreCase(ent.getArithmetic()) && invoiceGoodsAmount > 0) {
				log.info("销售占比分摊算法 "+fentanAmt);
				calculateBillA(invoiceSaleDetailList, fentanAmt, invoiceGoodsAmount, totalInvoiceAmount);
			}

			// 对小票明细中的金额和税额进行运算
			totalInvoiceAmount = 0.0;
			for (int i = 0; i < invoiceSaleDetailList.size(); i++) {
				InvoiceSaleDetail d = invoiceSaleDetailList.get(i);
				
				if ("N".equalsIgnoreCase(d.getIsinvoice())) {
					double dj = d.getQty()==0?0.00:d.getOldamt()/d.getQty();
					d.setPrice(dj); 	// 成交单价
					d.setAmount(0.00); 	// 成交金额
					d.setTaxfee(0.00);
					d.setAmt(0.00);
					d.setTaxpre("0");
					if(d.getTaxitemid()== null) d.setTaxitemid("");
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
		} 
		catch (Exception ex) {
			log.error("阪急－小票信息异常：" + ex.toString());
			return null;
		}
	}
	
	// 均摊算法
	protected void calculateBillA(ArrayList<InvoiceSaleDetail> invoiceSaleDetailList, double fentanAmt, double invoiceGoodsAmount, double totalInvoiceAmount) {
		log.info("待核减金额:"+fentanAmt);
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

	// 末位商品剔除法
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
	
	// 查询支付方式
	public Paymode getPaymodeById(String entid, String payid) {
		Paymode p = new Paymode();
		p.setEntid(entid);
		p.setPayid(payid);
		return calculateDao.getPaymodeById(p);
	}
	
	// 去除特殊字符或半角汉字
	public String cookString(String s){
		if(s== null) return "";
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
	
	// 是否是不开票商品
	public boolean isNoTaxGoods(String entid, String goodsid) {
		Goodsdis p = new Goodsdis();
		p.setEntid(entid);
		p.setGoodsid(goodsid);
		Goodsdis obj = calculateDao.getGoodsDis(p);
		return obj == null ? false : true;
	}
	
	// 是否是不开票分类
	public boolean isNoTaxCate(String entid, String cateid) {
		cateid = cateid.length()>6?cateid.substring(0, 6):cateid;
		Catedis p = new Catedis();
		p.setEntid(entid);
		p.setCateid(cateid);
		Catedis obj = calculateDao.getCateDis(p);
		return obj == null ? false : true;
	}
	
	// 根据类别获取商品税目
	public Taxitem getTaxitemid(String entid, String goodsid, String categoryid,double taxRate) {
		Taxitem taxitem = getTaxitemidByGoods(entid, goodsid);

		if (taxitem == null) {
			taxitem = getTaxitemidByCategory(entid, categoryid,taxRate);
		}
		
		return taxitem;
	}
	
	// 根据商品获取商品税目
	public Taxitem getTaxitemidByGoods(String entid, String goodsid) {
		Goodstax p = new Goodstax();
		p.setEntid(entid);
		p.setGoodsid(goodsid);
		Goodstax goods = calculateDao.getGoodstaxById(p);
		
		if (goods == null) return null;
		
		Taxitem item = new Taxitem();
		item.setTaxitemid(goods.getTaxitemid());
		item.setTaxrate(goods.getTaxrate()+"");
		item.setTaxpre(goods.getTaxpre());
		item.setTaxprecon(goods.getTaxprecon());
		item.setZerotax(goods.getZerotax());
		
		return item;
	}

	//  根据类别获取类目信息
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
				log.info("阪急－类别信息需维护：" + categoryid);
				return null;
			}

			if (r.getDeptlevelid() <= 1) {
				log.info("阪急－类别等级维护不正确：" + categoryid);
				return null;
			}

			return getTaxitemidByCategory(entid, r.getHeadcatid(), taxRate);
		}
		
		//可以配置多条类别与税目记录。如果有多条，取商品税率与配置的税率一致的记录。如果按税率都匹配不到，则取第一条记录
		Catetax catetax = null;
		if(cateList.size()==1) {
			catetax = cateList.get(0);
		}else 
		{
			for (Catetax cate : cateList) {
				if(cate.getTaxrate()!=null  && cate.getTaxrate()==taxRate) {
					catetax = cate;
					break;
				}
			}
		}
		
		//如果按税率无法匹配到则取第一条
		if(catetax== null) {
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
	
	public String stampToDate(String s){
		if (s == null || s.trim().length() == 0) return "";
		
		if (!s.startsWith("15")) return s;
		
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

	@Transactional
	public void insertInvque(InvoiceSaleHead p,String taxNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("entid", p.getEntid());
		map.put("sheetid", p.getSheetid());
		map.put("sheettype", p.getSheettype());
		map.put("iqseqno", p.getIqseqno());
		
		Invque que = sheetInvqueBJDao.getInvqueBJ(map);

		if (que == null) 
		{
			Invque myQue = new Invque();
			InvqueList e = new InvqueList();
			List<InvqueList> invqueListArray = new ArrayList<InvqueList>();
		
			String iqseqno = p.getIqseqno();
			String shopid = p.getShopid();

			e.setSerialid(p.getSerialid());
			e.setIqseqno(iqseqno);
			e.setSheetid(p.getSheetid());
			e.setSheettype(p.getSheettype());
			e.setShopid(p.getShopid());
			e.setSyjid(p.getSyjid());
			e.setBillno(p.getBillno());
			e.setTotalamount(p.getTotalamount());
			e.setInvoiceamount(p.getInvoiceamount());
			e.setTotaltaxfee(p.getTotaltaxfee());
			invqueListArray.add(e);
			
			myQue.setInvqueList(invqueListArray);
			myQue.setIqseqno(iqseqno);
			myQue.setIqentid(p.getEntid());
			myQue.setIqstatus(10);
			myQue.setIqshop(shopid);
			myQue.setIqsource(p.getSheettype());
			myQue.setIqtype(0);
			myQue.setIqmode(1);
			myQue.setIqchannel("BJ");
			myQue.setIqperson("");
			myQue.setIqtaxno(taxNo);
			myQue.setIqdate(new Date());
			myQue.setIqgmftax(p.getGmftax());
			myQue.setIqgmfname(p.getGmfname() == null||p.getGmfname().trim().length()==0?"个人":p.getGmfname());
			myQue.setIqgmfadd(p.getGmfadd());
			myQue.setIqgmfbank(p.getGmfbank());
			myQue.setIqtaxzdh("");
			myQue.setJpzz("");
			myQue.setIqadmin(entPrivatepara.get(p.getEntid(), FGlobal.WeixinKPR));
			myQue.setIqpayee(entPrivatepara.get(p.getEntid(), FGlobal.weixinSKR));
			myQue.setIqchecker(entPrivatepara.get(p.getEntid(), FGlobal.WeixinFHR));
			myQue.setIqmemo(p.getRemark());
			myQue.setIqtel("");
			myQue.setIqemail("");
			myQue.setIqyfpdm("");
			myQue.setIqyfphm("");
			myQue.setZsfs("0");
			myQue.setIsList(0);
			myQue.setIqtotje(p.getInvoiceamount() - p.getTotaltaxfee());
			myQue.setIqtotse(p.getTotaltaxfee());
			myQue.setIqfplxdm(p.getInvoicelx());
			
			// 写入开票队列
			inqueDao.insertInvque(myQue);
			if(myQue.getInvqueList()!=null) {
				for (InvqueList quelist : myQue.getInvqueList()) {
					inqueDao.insertInvqueList(quelist);
					if(!quelist.getListDetail().isEmpty()) {
						inqueDao.insertInvqueListDetail(quelist.getListDetail());
					}
				}
			}
		}
	}
	
	@Transactional
	public void saleInvoice2Delete(InvoiceSaleHead saleHead){
		invoiceSaleDao.deleteSaleDetail(saleHead);
		invoiceSaleDao.deleteSalePay(saleHead);
		invoiceSaleDao.deleteSaleHead(saleHead);
	}
	
	@Transactional
	public void saleInvoice2Insert(InvoiceSaleHead saleHead) {
		invoiceSaleDao.insertSaleHead(saleHead);
		sheetInvqueBJDao.updateInvoiceHeadIqseqno(saleHead);
		List<InvoiceSaleDetail> detail = saleHead.getInvoiceSaleDetail();
		if(detail!=null && !detail.isEmpty()){
			invoiceSaleDao.insertSaleDetail(detail);
			sheetInvqueBJDao.updateInvoiceDetailIqseqno(saleHead);
		}
		List<InvoiceSalePay> pay = saleHead.getInvoiceSalePay();
		if(pay!=null && !pay.isEmpty()) invoiceSaleDao.insertSalePay(pay);
	}
}
