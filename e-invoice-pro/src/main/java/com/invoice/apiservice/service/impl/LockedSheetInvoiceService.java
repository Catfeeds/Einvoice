package com.invoice.apiservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.InvqueList;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.Token;
import com.invoice.task.queue.AskInvoiceTask;
import com.invoice.util.Serial;

/**
 * 手工锁定或解锁单据
 * @author Baij
 */
@Service("LockedSheetInvoiceService")
public class LockedSheetInvoiceService {
	@Autowired
	InvoiceSaleDao billDao;
	
	@Autowired
	InvqueDao queDao;
	
	@Autowired
	AskInvoiceTask askInvoiceTask;
	
	public void lockedSheetInvoice(String sheetid,String sheetType,int isLock,String invoiceType,String fpdm,String fphm){
		if(isLock==1){
			nxLockedSheet(sheetid,sheetType,invoiceType,fpdm,fphm);
		}else if(isLock==0){
			nxUnLockedSheet(sheetid,sheetType,invoiceType);
		}else{
			throw new RuntimeException("未知的 isLock："+isLock);
		}
	}

	/**
	 * 解锁
	 * @param sheetid
	 * @param sheetType
	 * @param invoiceType
	 */
	@Transactional
	private void nxUnLockedSheet(String sheetid, String sheetType, String invoiceType) {
		//查询对应小票信息
		SheetService sheetservice = SheetServiceFactory.getInstance(sheetType);
		RequestBillInfo bill = new RequestBillInfo();
		bill.setTicketQC(sheetid);
		Token token = Token.getToken();
		bill.setEntid(token.getEntid());
		
		ResponseBillInfo sheet = sheetservice.getInvoiceSheetInfo(bill);
		if(sheet.getFlag()!=1){
			throw new RuntimeException("单据尚未锁定或开票，不能解锁："+sheetid);
		}
		
		//虚拟产生队列，锁定单据
		String iqseqno = Serial.getInvqueSerial();
		List<InvqueList> invqueListArray = new ArrayList<InvqueList>();
		InvqueList invqueList = new InvqueList();
		invqueList.setSerialid(sheet.getSerialid());
		invqueList.setIqseqno(iqseqno);
		invqueList.setSheetid(sheet.getSheetid());
		invqueList.setSheettype(sheetType);
		invqueList.setShopid(sheet.getShopid());
		invqueList.setSyjid(sheet.getSyjid());
		invqueList.setBillno(sheet.getBillno());
		invqueList.setTotalamount(sheet.getTotalamount());
		invqueList.setInvoiceamount(sheet.getInvoiceamount());
		invqueList.setTotaltaxfee(sheet.getTotaltaxfee());
		invqueListArray.add(invqueList);
		
		String memo = "手工解锁小票开票";
		Invque que = new Invque();
		que.setIqseqno(iqseqno);
		que.setIqentid(bill.getEntid());
		que.setIqstatus(0);
		//直接设置为开票成功
		que.setIqstatus(50);
		que.setIqsource(sheetType);
		//红字
		que.setIqtype(1);
		//汇总
		que.setIqmode(2);
		que.setIqchannel("app");
		que.setIqperson(token.getUsername());
		que.setIqtaxno(memo);
		que.setIqdate(new Date());
		que.setIqtotje(sheet.getInvoiceamount());
		que.setIqtotse(sheet.getTotaltaxfee());
		que.setIqgmftax(memo);
		que.setIqgmfname(memo);
		que.setIqgmfadd("");
		que.setIqgmfbank("");
		que.setIqtaxzdh(memo);
		que.setIqadmin(token.getUsername());
		que.setIqtel("");
		que.setIqemail("");
		que.setIqyfpdm("");
		que.setIqyfphm("");
		que.setIsList(0);
		que.setIqfplxdm(invoiceType);
		que.setZsfs("0");
		que.setInvqueList(invqueListArray);
		
		//保存队列
		askInvoiceTask.insert(que);
		
		InvoiceSaleHead saleHead = new InvoiceSaleHead();
		saleHead.setFlag(1);
		saleHead.setIqseqno(que.getIqseqno());
		saleHead.setEntid(bill.getEntid());
		saleHead.setSheettype(sheetType);
		saleHead.setSheetid(sheetid);
		int row = billDao.unLockedInvoiceSaleFlag(saleHead);
		
		if(row !=1){
			throw new RuntimeException(sheetid+"正在被操作，请稍后再试");
		}
	}

	/**
	 * 加锁
	 * @param sheetid
	 * @param sheetType
	 * @param invoiceType
	 */
	@Transactional
	private void nxLockedSheet(String sheetid, String sheetType, String invoiceType,String fpdm,String fphm) {
		//查询对应小票信息
		SheetService sheetservice = SheetServiceFactory.getInstance(sheetType);
		RequestBillInfo bill = new RequestBillInfo();
		bill.setTicketQC(sheetid);
		Token token = Token.getToken();
		bill.setEntid(token.getEntid());
		if("0".equals(sheetType)){
			bill.setSheetid(sheetid);
			bill.setSheettype("0");
		}
		ResponseBillInfo sheet = sheetservice.getInvoiceSheetInfo(bill);
		if(sheet.getFlag()==1){
			throw new RuntimeException("单据已开票，不能锁定："+sheetid);
		}
		
		//虚拟产生队列，锁定单据
		String iqseqno = Serial.getInvqueSerial();
		List<InvqueList> invqueListArray = new ArrayList<InvqueList>();
		InvqueList invqueList = new InvqueList();
		invqueList.setSerialid(sheet.getSerialid());
		invqueList.setIqseqno(iqseqno);
		invqueList.setSheetid(sheet.getSheetid());
		invqueList.setSheettype(sheetType);
		invqueList.setShopid(sheet.getShopid());
		invqueList.setSyjid(sheet.getSyjid());
		invqueList.setBillno(sheet.getBillno());
		invqueList.setTotalamount(sheet.getTotalamount());
		invqueList.setInvoiceamount(sheet.getInvoiceamount());
		invqueList.setTotaltaxfee(sheet.getTotaltaxfee());
		invqueListArray.add(invqueList);
		
		String memo = "手工锁定小票开票";
		Invque que = new Invque();
		que.setIqseqno(iqseqno);
		que.setIqentid(bill.getEntid());
		que.setIqstatus(0);
		//直接设置为开票成功
		que.setIqstatus(50);
		que.setIqsource(sheetType);
		//蓝字
		que.setIqtype(0);
		//汇总
		que.setIqmode(2);
		que.setIqchannel("app");
		que.setIqperson(token.getUsername());
		que.setIqtaxno(memo);
		que.setIqdate(new Date());
		que.setIqtotje(sheet.getInvoiceamount());
		que.setIqtotse(sheet.getTotaltaxfee());
		que.setIqgmftax(memo);
		que.setIqgmfname(memo);
		que.setIqgmfadd("");
		que.setIqgmfbank("");
		que.setIqtaxzdh(memo);
		que.setIqadmin(token.getUsername());
		que.setIqtel("");
		que.setIqemail("");
		que.setRtfpdm(fpdm);
		que.setRtfphm(fphm);
		que.setIqyfpdm("");
		que.setIqyfphm("");
		que.setIsList(0);
		que.setIqfplxdm(invoiceType);
		que.setZsfs("0");
		que.setInvqueList(invqueListArray);
		
		//保存队列
		//锁定对应小票信息，是否开票状态设置为1
		askInvoiceTask.lockSheetFlag(que, invqueList);
		askInvoiceTask.insert(que);
	}
	
	/**
	 * 重置小票
	 */
	@Transactional
	public int reset_bill(Map<String, Object> p) {
		String iqseqno = (String)p.get("iqseqno");
		if (!StringUtils.isEmpty(iqseqno)) {
			Invque que =  new Invque();
			que.setIqseqno(iqseqno);
			queDao.updateTo99(que);
		}
		return billDao.resetBill(p);
	}
}
