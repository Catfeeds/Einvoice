package com.invoice.apiservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.dao.PreInvoiceDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.InvqueList;
import com.invoice.bean.db.InvqueListDetail;
import com.invoice.bean.db.PreInvoiceAsk;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.ResponseInvoice;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.util.HttpClientCommon;
import com.invoice.util.LocalRuntimeException;
import com.invoice.util.Serial;

@Service
public class InvqueService {
	@Autowired
	EntPrivatepara entPrivatepara;
	
	@Autowired
	InvqueDao dao;
	
	@Autowired
	InvoiceSaleDao invoiceSaleDao;
	
	@Autowired
	PreInvoiceDao preInvoiceDao;
	
	public void insert(Invque que) {
		dao.insertInvque(que);
		for (InvqueList quelist : que.getInvqueList()) {
			dao.insertInvqueList(quelist);
		}
	}

	public List<Invque> getInvque(Map<String, Object> p){
		return dao.getInvque(p);
	}
	
	public int getInvqueCount(Map<String, Object> p){
		return dao.getInvqueCount(p);
	}
	
	public int getInvquelistForSheetidcount(Map<String, Object> p){
		return dao.getInvquelistForSheetidcount(p);
	}
	
	public List<Invque> getInvquelistForSheetid(Map<String, Object> p){
		return dao.getInvquelistForSheetid(p);
	}
	
	public List<InvqueList> getInvqueList(Map<String, Object> p){
		return dao.getInvqueList(p);
	}
	
	public List<Invque> getInvqueForsheetid(Map<String, Object> p){
		return dao.getInvqueForsheetid(p);
	}
	
	/**
	 * 根据预览结果生成开票队列
	 * @param responseInvoiceList
	 * @return
	 */
	public List<Invque> saveInvoiceQue(List<ResponseInvoice> responseInvoiceList){
		List<Invque> invqueList = new ArrayList<Invque>();
		//循环写入
		for (ResponseInvoice responseInvoice : responseInvoiceList) {
			Invque que = cookInvoiceQue(responseInvoice);
			
			//开票服务rest
			String url = FGlobal.openinvoiceurl;
			
			//如果不是微信渠道，立即执行
			if(que.getIqchannel().equals("wx1")){
				//异步执行
				url += "asyncAskInvoice";
			}else{
				//立即执行
				url += "immediAskInvoice";
			}
			
			String res = HttpClientCommon.doPostStream(JSONObject.toJSONString(que), null, url, 0, 0, "utf-8");
			if(StringUtils.isEmpty(res)){
				throw new RuntimeException("请求电子发票平台超时");
			}
			
			JSONObject js = JSONObject.parseObject(res);
			if(js.getIntValue("code")==0){
				que = JSONObject.parseObject(js.getString("data"), Invque.class);
				if(que.getIqstatus()==30){
					throw new RuntimeException(que.getIqmsg());
				}
			}else{
				throw new RuntimeException(js.getString("message"));
			}
			
			invqueList.add(que);
		}
		
		return invqueList;
	}
	
	/**
	 * 前端请求数据转换为队列信息
	 * @param responseInvoice
	 * @return
	 */
	public Invque cookInvoiceQue(ResponseInvoice responseInvoice){
		checkInvqueData(responseInvoice);
		
		String sheettype = responseInvoice.getSheettype();
		List<InvqueList> invqueListArray = new ArrayList<InvqueList>();
		//检查开票单据信息
		List<ResponseBillInfo> billList = responseInvoice.getBillInfoList();
		
		SheetService sheetservice = SheetServiceFactory.getInstance(sheettype);
		
		String iqseqno = Serial.getInvqueSerial();
		String shopid = "";
		
		for (ResponseBillInfo item : billList) {
			RequestBillInfo bill = new RequestBillInfo();
			bill.setEntid(responseInvoice.getEntid());
			bill.setJe(item.getTotalamount());
			bill.setShopid(item.getShopid());
			bill.setSheettype(sheettype);
			bill.setSheetid(item.getSheetid());
			ResponseBillInfo head = sheetservice.getInvoiceSheetInfo(bill);
			
			if(head==null) throw new RuntimeException(""+item.getSheetid()+"不存在，请重新选择");
			
			//微信端金额不一致则报错
			if ("wx".equals(responseInvoice.getChannel())) {
				Double totalAmount = head.getTotalamount();
				double je = bill.getJe();
				if(je != totalAmount){
					throw new RuntimeException(""+item.getBillno()+"金额异常");
				}
			}
			
			//如果已开票则直接跳过
			if(head.getFlag()==1 && !"other".equalsIgnoreCase(responseInvoice.getChannel())){
				throw new RuntimeException(""+item.getSheetid()+"已开票或正在开票，请重新选择");
			}
			
			//请求队列明细
			InvqueList e = new InvqueList();
			e.setSerialid(head.getSerialid());
			e.setIqseqno(iqseqno);
			e.setSheetid(head.getSheetid());
			e.setSheettype(sheettype);
			e.setShopid(head.getShopid());
			e.setSyjid(head.getSyjid());
			e.setBillno(head.getBillno());
			e.setTotalamount(head.getTotalamount());
			e.setInvoiceamount(head.getInvoiceamount());
			e.setTotaltaxfee(head.getTotaltaxfee());
			
			shopid = e.getShopid();
			
			//添加行记录
			List<Integer> rowList = item.getRownoList();
			if(rowList==null || rowList.isEmpty()) {
				//TODO 把单据所有=Y明细行插入
			}else {
				for (Integer rowno : rowList) {
					InvqueListDetail listDetail = new InvqueListDetail();
					listDetail.setIqseqno(iqseqno);
					listDetail.setSheetid(head.getSheetid());
					listDetail.setSheettype(sheettype);
					listDetail.setSerialid(head.getSerialid());
					listDetail.setRowno(rowno);
					e.getListDetail().add(listDetail);
				}
			}
			
			invqueListArray.add(e);
		}
		
		//如果是微信端，则使用纳税表的法人列作为开票人
		//调整到rest层设置
//		if ("wx".equals(responseInvoice.getChannel())) {
//			String kpr = entPrivatepara.get(responseInvoice.getEntid(), FGlobal.WeixinKPR);
//			if(StringUtils.isEmpty(kpr)) kpr = "system";
//			
//			responseInvoice.setAdmin(kpr);
//		}else if ("app".equals(responseInvoice.getChannel())) {
//			Token token = Token.getToken();
//			responseInvoice.setAdmin(token.getUsername());
//		}
				
		//请求队列
		Invque que = new Invque();
		que.setIqseqno(iqseqno);
		que.setIqentid(responseInvoice.getEntid());
		
		que.setIqstatus(0);
		//如果不是是微信端，则状态直接设置为10，避免后台job进行处理
		if (!"wx".equals(responseInvoice.getChannel())) {
			que.setIqstatus(10);
		}
		
		que.setIqshop(shopid);
		que.setIqsource(sheettype);
		//写死蓝字
		que.setIqtype(0);
		//写死明细方式
		que.setIqmode(1);
		que.setIqchannel(responseInvoice.getChannel());
		que.setIqperson(responseInvoice.getUserid());
		que.setIqtaxno(responseInvoice.getXsfNsrsbh());
		que.setIqdate(new Date());

		//合计信息放在后面开票时统计
//		que.setIqtotje(iqtotje);
//		que.setIqtotse(iqtotse);
		
		que.setIqgmftax(responseInvoice.getGmfNsrsbh());
		que.setIqgmfname(responseInvoice.getGmfMc());
		que.setIqgmfadd(responseInvoice.getGmfDzdh());
		que.setIqgmfbank(responseInvoice.getGmfYhzh());
		
		//开票点
		que.setIqtaxzdh(responseInvoice.getKpd());
		que.setJpzz(responseInvoice.getJpzz());
		
		que.setIqadmin(responseInvoice.getAdmin());
		que.setIqpayee(responseInvoice.getIqpayee());
		que.setIqchecker(responseInvoice.getIqchecker());
		que.setIqmemo(responseInvoice.getIqmemo());
		
		que.setIqtel(responseInvoice.getRecvPhone());
		que.setIqemail(responseInvoice.getRecvEmail());
		que.setIqyfpdm(responseInvoice.getYfpdm());
		que.setIqyfphm(responseInvoice.getYfphm());
		
		que.setIsList(0);
		//是否为电子票
		if(responseInvoice.getIqfplxdm() != null && !"".equals(responseInvoice.getIqfplxdm())){
			que.setIqfplxdm(responseInvoice.getIqfplxdm());
			if(que.getIqfplxdm().equals("007") || que.getIqfplxdm().equals("004")){
				if(invqueListArray.size()>8){
					que.setIsList(1);
				}
			}
		}else{
			que.setIqfplxdm("026");
		}
		
		//征税方式默认为0
		que.setZsfs("0");
		
		que.setInvqueList(invqueListArray);
		
		return que;
	}
	
	private Taxinfo checkInvqueData(ResponseInvoice responseInvoice){
		if(responseInvoice.getChannel()==null) throw new RuntimeException("渠道异常");
		if(responseInvoice.getUserid()==null) throw new RuntimeException("用户信息异常");
		
		//验证纳税号正确
		Taxinfo info = new Taxinfo();
		info.setEntid(responseInvoice.getEntid());
		info.setTaxno(responseInvoice.getXsfNsrsbh());
		Taxinfo taxinfo = invoiceSaleDao.getTaxinfo(info);
		
		if(taxinfo==null) throw new RuntimeException("纳税号异常："+info.getTaxno());
		
		//如果前端开票点为空则取纳税表中的开票点
		//微信会传空值。前台会传入token中的开票点
		if(StringUtils.isEmpty(responseInvoice.getKpd())){
			responseInvoice.setKpd(taxinfo.getItfkpd());
		}
		
		return taxinfo;
	}
	
	public List<InvoiceSaleDetail> getSaleDetailListByInvquelist(Map<String, Object> p){
		return invoiceSaleDao.getSaleDetailListByInvquelist(p);
	}

	public boolean preInvoice(ResponseInvoice responseInvoice) {
		String sheettype = responseInvoice.getSheettype();
		//检查开票单据信息
		List<ResponseBillInfo> billList = responseInvoice.getBillInfoList();
		ResponseBillInfo item = billList.get(0);
		SheetService sheetservice = SheetServiceFactory.getInstance(sheettype);
		RequestBillInfo bill = new RequestBillInfo();
		bill.setEntid(responseInvoice.getEntid());
		bill.setSheettype(sheettype);
		bill.setSheetid(item.getSheetid());
		bill.setJe(item.getTotalamount());
		ResponseBillInfo head = sheetservice.getInvoiceSheetInfo(bill);
		if(!StringUtils.isEmpty(head.getInvoicelx()) && !head.getInvoicelx().equals("026")) {
			PreInvoiceAsk p = new PreInvoiceAsk();
			p.setEntid(head.getEntid());
			p.setSheetid(head.getSheetid());
			p.setSheettype(head.getSheettype());
			p.setFlag(0);
			p.setRecvemail(responseInvoice.getRecvEmail());
			p.setRecvphone(responseInvoice.getRecvPhone());
			p.setGmfname(responseInvoice.getGmfMc());
			p.setGmftax(responseInvoice.getGmfNsrsbh());
			p.setGmfadd(responseInvoice.getGmfDzdh());
			p.setGmfbank(responseInvoice.getGmfYhzh());
			p.setGmfno("");
			p.setInvoicelx(head.getInvoicelx());
			p.setCreatetime(new Date());
			p.setOpenid(responseInvoice.getUserid());
			preInvoiceDao.insertPreInvoiceAsk(p);
			return true;
		}
		return false;
	}
}
