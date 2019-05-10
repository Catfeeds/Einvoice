package com.invoice.task.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baiwang.bop.respose.entity.FormatfileBuildResponse;
import com.baiwang.bop.respose.entity.InvoiceOpenResponse;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.apiservice.service.impl.InvoiceAnddetailService;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.InvqueList;
import com.invoice.bean.db.InvqueListDetail;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.port.PortService;
import com.invoice.port.PortServiceFactory;
import com.invoice.port.bwdz.invoice.bean.BwdzRtOpenInvoiceBean;
import com.invoice.port.bwdz.invoice.service.BwdzService;
import com.invoice.port.bwgf.invoice.service.BwgfServiceImpl;
import com.invoice.port.bwjf.invoice.bean.BwjfDzRtInvoiceBean;
import com.invoice.port.bwjf.invoice.bean.BwjfRtInvoiceBean;
import com.invoice.port.bwjf.invoice.service.BwjfServiceImpl;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.dao.TaxinfoDao;
import com.invoice.util.NewHashMap;
import com.invoice.util.SpringContextUtil;
import com.invoice.yuande.invoice.bean.YuanDeDzRntDataBean;
import com.invoice.yuande.invoice.service.YuanDeService;

import baiwang.invoice.bean.BaiWangRtInvoiceBean;
import baiwang.invoice.bean.RtInvoiceHeadList;
import baiwang.invoice.service.BaiWangServiceImpl;
import hangxin.invoice.bean.HangXinQzRtBean;
import hangxin.invoice.bean.HangXinRtInvoiceBean;
import hangxin.invoice.service.HangXinServiceImpl;

/**
 * @author Baij
 * 申请开票
 */

@Service("AskInvoiceTask")
public class AskInvoiceTask {
	private final Log log = LogFactory.getLog(AskInvoiceTask.class);
	
	@Autowired
	TaxinfoDao taxinfoDao;
	
	@Autowired
	InvqueDao inqueDao;
	
	@Autowired
	InvoiceSaleDao billDao;
	
	@Autowired
	InvoiceService invoiceService;

	@Autowired
	YuanDeService ydService;
	
	@Autowired
	BwdzService  bwdzService;
	
	@Autowired
	EntPrivatepara entPrivatepara;
	
	public void insert(Invque que) {
		inqueDao.insertInvque(que);
		if(que.getInvqueList()!=null) {
			for (InvqueList quelist : que.getInvqueList()) {
				inqueDao.insertInvqueList(quelist);
				if(!quelist.getListDetail().isEmpty()) {
					inqueDao.insertInvqueListDetail(quelist.getListDetail());
				}
			}
		}
	}
	
	public RequestBillInfo lockSheetFlag(Invque que, InvqueList invqueList){
		RequestBillInfo bill = new RequestBillInfo();
		bill.setEntid(que.getIqentid());
		bill.setSheettype(invqueList.getSheettype());
		bill.setSheetid(invqueList.getSheetid());
		
		//查询单据信息
		ResponseBillInfo head = billDao.getInvoiceSaleHead(bill);
		if(head.getFlag()==1){
			throw new RuntimeException("单据已开票或正在开票："+invqueList.getSheetid());
		}
		
		//如果队列有单据明细信息，则按明细信息锁定单据
		List<InvqueListDetail> detialList = invqueList.getListDetail();
		InvoiceSaleDetail detail = new InvoiceSaleDetail();
		detail.setIqseqno(que.getIqseqno());
		detail.setEntid(head.getEntid());
		detail.setSerialid(invqueList.getSerialid());
		detail.setSheetid(invqueList.getSheetid());
		detail.setSheettype(invqueList.getSheettype());
		//只有费用开票时判断明细是否开票
		if("4".equals(que.getIqsource())){
			if(!detialList.isEmpty()) {
				for (InvqueListDetail invqueListDetail : detialList) {
					detail.setRowno(invqueListDetail.getRowno());
					int row = billDao.lockedInvoiceSaleDetailFlag(detail);
					if(row !=1){
						throw new RuntimeException(head.getSheetid()+"正在被操作，请稍后再试");
					}
				}
			}else {
				//没有队列明细，直接将明细表flag全部设置为1
				int row = billDao.lockedInvoiceSaleDetailFlag4All(detail);
				if(row == 0){
					throw new RuntimeException(head.getSheetid()+"正在被操作，请稍后再试");
				}
			}
		}
		
		//如果明细flag全部为1，则将表头的flag设置为1
		//锁定对应小票信息，是否开票状态设置为1
		InvoiceSaleHead saleHead = new InvoiceSaleHead();
		saleHead.setFlag(1);
		saleHead.setIqseqno(que.getIqseqno());
		saleHead.setEntid(head.getEntid());
		saleHead.setSheettype(head.getSheettype());
		saleHead.setSheetid(head.getSheetid());
		saleHead.setBackflag(0);
		saleHead.setRecvemail(que.getIqemail());
		saleHead.setRecvphone(que.getIqtel());
		
		if("4".equals(que.getIqsource())) {
			int count = billDao.countInvqueList(saleHead);
			if(count==0){
				int row = billDao.lockedInvoiceSaleFlag(saleHead);
				if(row !=1){
					throw new RuntimeException(head.getSheetid()+"正在被操作，请稍后再试");
				}
			}
		}else{
			int row = billDao.lockedInvoiceSaleFlag(saleHead);
			if(row !=1){
				throw new RuntimeException(head.getSheetid()+"正在被操作，请稍后再试");
			}
		}
		return bill;
	}
	
	/**
	 * 异步开票，仅记录开票队列
	 * @param que
	 */
	public void asyncAskInvoice(Invque que) {
		SpringContextUtil.getBean(AskInvoiceTask.class).nxAsyncOpenInvoice(que);
	}
	public void nxAsyncOpenInvoice(Invque que){
		List<InvqueList> list = que.getInvqueList();
		if(list==null || list.isEmpty()){
			throw new RuntimeException("开票明细数据空");
		}
		insert(que);
		for (InvqueList invqueList : list) {
			//锁定单据状态为已开票
			lockSheetFlag(que, invqueList);
		}
	}
	
	
	/**
	 * 立即开票
	 * @param que
	 */
	public void immediInvoiceQue(Invque que){
		try {
			List<InvoiceSaleDetail> detailList = cookOpenInvoiceDetail(que);
			
			//写入队列
			insert(que);
			
			//电子票 调整锁定数据flag=1动作不在事物中，即使开票事物出错，数据仍然标记为1.
			String tmp = entPrivatepara.get(que.getIqentid(), FGlobal.LockSheet);
			boolean lockSheet = (tmp==null || tmp.equals("1"))?true:false;
			
			if(lockSheet && que.getIqfplxdm().equals("026") && que.getIqsource().equals("1")) {
				if(que.getIqtype()==0){
					List<InvqueList> list = que.getInvqueList();
						for (InvqueList invqueList : list) {
							lockSheetFlag(que, invqueList);
						}
				}
			}
			
			SpringContextUtil.getBean(AskInvoiceTask.class).nxImmediOpenInvoice(que,detailList);
			
		} catch (Exception e) {
			log.error(e,e);
			que.setIqstatus(30);
			que.setIqmsg(e.getMessage());
			inqueDao.updateTo30(que);
		}
	}
	
	private List<InvoiceSaleDetail> cookOpenInvoiceDetail(Invque que){
		List<InvoiceSaleDetail> detailList;
		//获取开票明细信息
		if(que.getIqtype() == 1){
			//红冲
			detailList = hongInvoiceDetail(que);
		}else{
			//根据业务类型找到对应的处理服务
			SheetService sheetservice = SheetServiceFactory.getInstance(que.getIqsource());
			detailList = sheetservice.cookOpenInvoiceDetail(que);
		}
		
		return detailList;
	}
	
	
	/**
	 * 立即开票
	 * 独立事物
	 * @param que
	 * @return
	 */
	public void nxImmediOpenInvoice(Invque que,List<InvoiceSaleDetail> detailList){
		String nsrsbh = que.getIqtaxno();
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setEntid(que.getIqentid());
		taxinfo.setTaxno(nsrsbh);
		try {
			taxinfo = taxinfoDao.getTaxinfoByNo(taxinfo);
		} catch (Exception e) {
			throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
		}
		
		//电子票事物内锁定 当为蓝票时锁定单据状态为已开票
		String tmp = entPrivatepara.get(que.getIqentid(), FGlobal.LockSheet);
		boolean lockSheet = (tmp==null || tmp.equals("1"))?true:false;
		
		if(!lockSheet || !que.getIqfplxdm().equals("026") || !que.getIqsource().equals("1")) {
			if(que.getIqtype()==0){
				List<InvqueList> list = que.getInvqueList();
					for (InvqueList invqueList : list) {
						lockSheetFlag(que, invqueList);
					}
			}
		}
		
		//没有启用短信的企业强制清空电话
		String usephone = entPrivatepara.get(taxinfo.getEntid(), FGlobal.usePhone);
		if(!"1".equals(usephone)) {
			que.setIqtel(null);
		}
		
		//如果收款人|复核人是null，设置为空字符，避免发票上出现null字样
		if(que.getIqpayee()==null) que.setIqpayee("");
		if(que.getIqchecker()==null) que.setIqchecker("");
		
		//调用开票接口
		openInvoice(que,taxinfo,detailList);
		inqueDao.updateTo40(que);
		
		//执行下一步操作
		if("2".equals(taxinfo.getTaxmode())){
			//武汉航信，开票和签章必须一起完成
			openInvoiceNext(que, taxinfo, detailList);
			inqueDao.updateTo50(que);
		}else if("1".equals(taxinfo.getTaxmode())){
			//百望金税可以后面再获取pdf
			try {
				openInvoiceNext(que, taxinfo, detailList);
				inqueDao.updateTo50(que);
			} catch (Exception e) {
				log.error("百望金税获取pdf失败："+e.getMessage());
			}
		}else if("4".equals(taxinfo.getTaxmode())){
			//湖南航信
			try {
				inqueDao.updateTo50(que);
			} catch (Exception e) {
				log.error("百望电子获取pdf失败："+e.getMessage());
			}
		}else if("5".equals(taxinfo.getTaxmode())){
			//百望股份可以后面再获取pdf
			try {
				openInvoiceNext(que, taxinfo, detailList);
				inqueDao.updateTo50(que);
			} catch (Exception e) {
				log.error("百望股份获取pdf失败："+e.getMessage());
			}
		}else if("6".equals(taxinfo.getTaxmode())){
			//百望股份可以后面再获取pdf
			try {
				openInvoiceNext(que, taxinfo, detailList);
				inqueDao.updateTo50(que);
			} catch (Exception e) {
				log.error("百望股份获取pdf失败："+e.getMessage());
			}
		}else if("7".equals(taxinfo.getTaxmode())){
			//远的航信标版
			try {
				inqueDao.updateTo50(que);
			} catch (Exception e) {
				log.error("湖南航信获取pdf失败："+e.getMessage());
			}
		}  
		else if("8".equals(taxinfo.getTaxmode())||"10".equals(taxinfo.getTaxmode())){
			//湖南航信
			try {
				inqueDao.updateTo50(que);
			} catch (Exception e) {
				log.error("湖南航信获取pdf失败："+e.getMessage());
			}
		}
		else if("C".equals(taxinfo.getTaxmode())){
			//湖南航信
			try {
				inqueDao.updateTo50(que);
			} catch (Exception e) {
				log.error("宁波百旺获取pdf失败："+e.getMessage());
			}
		}
		
		//红冲或作废发票更改状态
		if(!"6".equals(que.getIqmode())){
			if(que.getIqtype()==1){
				restSaleInvoice(que);
			}else
			if(que.getIqtype()==2){
				if(que.getZflx()==null||"".equals(que.getZflx())){
					throw new RuntimeException("发票作废类型不能为空："+que.getRtfphm());
				}
				if("1".equals(que.getZflx()))
					restSaleInvoice(que);
			}
		}
	}
	
	
	/**
	 * 调用开票
	 * @param que
	 */
	public void openInvoice(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		//开票之前判断企业纳税信息是否从企业纳税明细表信息获取。按企业ID 纳税号ID 和开票点获取  
		if(que.getIqtaxzdh()==null) que.setIqtaxzdh("");
		
		if(!"".equals(que.getIqtaxzdh())&&!"026".equals(que.getIqfplxdm())){
			Taxinfo taxinfoitem = new Taxinfo();
			taxinfoitem.setEntid(que.getIqentid());
			taxinfoitem.setTaxno(que.getIqtaxno());
			taxinfoitem.setItfkpd(que.getIqtaxzdh());
			try {
				taxinfoitem = taxinfoDao.getTaxinfoItemByNo(taxinfoitem);
				if(taxinfoitem!=null){
					if(taxinfoitem.getTaxmode()==null) taxinfoitem.setTaxmode("");
					if(!"".equals(taxinfoitem.getTaxmode())){
						taxinfo = taxinfoitem;
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
			}
		}	
		//武汉航信
	    if("2".equals(taxinfo.getTaxmode())){
	    	openInvoiceHX(que, taxinfo, detailList);
	    //百望金税
		}else if("1".equals(taxinfo.getTaxmode())){
			openInvoiceBW(que, taxinfo, detailList);
		//远的银座	
		}else if("3".equals(taxinfo.getTaxmode())){
			openInvoiceYD(que, taxinfo, detailList);
		//百望电子
		}else if("4".equals(taxinfo.getTaxmode())){
			 
			openInvoiceBwdz(que, taxinfo, detailList);
		//百望股份
		}else if("5".equals(taxinfo.getTaxmode())){
			 
			openInvoiceBWGF(que, taxinfo, detailList);
		//百望九赋
		}else if("6".equals(taxinfo.getTaxmode())){
			 
		openInvoiceBWJF(que, taxinfo, detailList);
		//远的标版
		}else if("7".equals(taxinfo.getTaxmode())){
			openInvoiceYDBb(que, taxinfo, detailList);
		}else if(!"".equals(taxinfo.getTaxmode())){
			PortService portservice = PortServiceFactory.getInstance(taxinfo.getTaxmode());
			portservice.openInvoice(que, taxinfo, detailList);
		} else{
			throw new RuntimeException("发票平台类型未实现："+taxinfo.getTaxmode());
		}
	    log.info("openInvoice success");
	}
	
	public void openInvoiceNext(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		if("2".equals(taxinfo.getTaxmode())){//武汉航信
			qzInvoiceHX(que, taxinfo, detailList);
		}else if("1".equals(taxinfo.getTaxmode())){//百望金税
			getBWPdf(que, taxinfo);
		}else if("5".equals(taxinfo.getTaxmode())){//百望股份
			getBWGFPdf(que, taxinfo);
		}else if("6".equals(taxinfo.getTaxmode())){//百望九赋
			getBWJFPdf(que, taxinfo);
		}else{
			throw new RuntimeException("发票平台类型未实现："+taxinfo.getTaxmode());
		}
		//将PDF连接写入发票表
		invoiceService.updateInvoicePdf(que);
		
		log.info("openInvoiceNext success");
	}
	
	
	/**
	 * 航信-开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	private void openInvoiceHX(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		String nsrsbh = taxinfo.getTaxno();
		RtnData rtn = new RtnData();
		
		HangXinServiceImpl service = new HangXinServiceImpl(nsrsbh);
		HangXinRtInvoiceBean resInv = service.openinvoice(que,taxinfo,detailList, rtn);
		
		if(rtn.getCode()!=0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}else
		if(resInv==null){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}else
		if(resInv.getFp_hm()==null||"".equals(resInv.getFp_hm())){
			throw new RuntimeException("发票平台无返回信息");
		}
		List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				detailList1.add(detail);
			}
		}
		
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList);
		invoiceHead.setFpewm(resInv.getEwm());
		invoiceHead.setFphm(resInv.getFp_hm());
		invoiceHead.setFpdm(resInv.getFp_dm());
		invoiceHead.setFpskm(resInv.getFwmw());
		invoiceHead.setFpjym(resInv.getJym());
		invoiceHead.setFprq(resInv.getKprq());
		invoiceService.saveInvoice(invoiceHead);
		
		que.setRtewm(resInv.getEwm());
		que.setRtfphm(resInv.getFp_hm());
		que.setRtfpdm(resInv.getFp_dm());
		que.setRtskm(resInv.getFwmw());
		que.setRtjym(resInv.getJym());
		que.setRtkprq(resInv.getKprq());
	}
	
	/**
	 * 航信-开票后签章
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	private void qzInvoiceHX(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		RtnData rtn = new RtnData();
		String nsrsbh = taxinfo.getTaxno();
		HangXinServiceImpl service = new HangXinServiceImpl(nsrsbh);
		HangXinQzRtBean resQz = service.openQzinvoice(que,taxinfo,detailList, rtn);
		if(rtn.getCode()!=0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		que.setIqpdf(resQz.getPdf_url());
		que.setIqstatus(50);
		 
	}
	
	
	/**
	 * 百望-开具发票
	 */
	private void openInvoiceBW(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		String serviceUrl = taxinfo.getItfserviceUrl();
		String jrdm = taxinfo.getItfjrdm();
		String nsrsbh = taxinfo.getTaxno();
		RtnData rtn = new RtnData();
		BaiWangRtInvoiceBean res = new BaiWangRtInvoiceBean();
		BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh ,jrdm);

		if(que.getRetflag()==null) que.setRetflag("");
		System.out.println("Retflag : "+que.getRetflag());
		if("1".equals(que.getRetflag())){
			RtInvoiceHeadList list = service.findInvoice(que, rtn);
			if(rtn.getCode()!=0){
				throw new RuntimeException("发票查询失败："+rtn.getMessage());
			}else
			if(list==null){
				throw new RuntimeException("百望发票平台无返回信息");
			}
			res.setEym(list.getHeads().get(0).getEwm());
			res.setFpdm(list.getHeads().get(0).getFpdm());
			res.setFphm(list.getHeads().get(0).getFphm());
			res.setJym(list.getHeads().get(0).getJym());
			res.setKprq(list.getHeads().get(0).getKprq());
			res.setSkm(list.getHeads().get(0).getSkm());
			res.setEwm(list.getHeads().get(0).getEwm());
			 
		}else{
			res = service.openinvoice(que,taxinfo,detailList,rtn);
		}
		

		
		if(rtn.getCode()!=0){
			String msg = rtn.getMessage();
			if(msg!=null && msg.contains("：")) {
				msg = msg.substring(msg.lastIndexOf("：")+1,msg.length());
			}
			if(msg!=null && msg.contains(":")) {
				msg = msg.substring(msg.lastIndexOf(":")+1,msg.length());
			}
			throw new RuntimeException("发票开具失败baiwang："+msg);
		}else{
			if(res==null){
				throw new RuntimeException("百望发票平台无返回信息");
			}else
			if(res.getFphm()==null||"".equals(res.getFphm())){
				throw new RuntimeException("百望发票平台无返回信息");
			}
			
			List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
			for(InvoiceSaleDetail detail:detailList){
				if("Y".equals(detail.getIsinvoice())){
					detailList1.add(detail);
				}
			}
			
			InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
			invoiceHead.setFpewm(res.getEwm());
			invoiceHead.setFphm(res.getFphm());
			invoiceHead.setFpdm(res.getFpdm());
			invoiceHead.setFpskm(res.getSkm());
			invoiceHead.setFpjym(res.getJym());
			invoiceHead.setFprq(res.getKprq());
			invoiceService.saveInvoice(invoiceHead);
			
			que.setRtewm(res.getEwm());
			que.setRtfphm(res.getFphm());
			que.setRtfpdm(res.getFpdm());
			que.setRtjym(res.getJym());
			que.setRtkprq(res.getKprq());
			que.setRtskm(res.getSkm());
			que.setIqstatus(40);
		}
	}
	
	/**
	 * 百望金税-调用PDF获取及推送
	 * @param que
	 */
	private void getBWPdf(Invque que,Taxinfo taxinfo){
		//如果不是电子票则直接返回
		if(!"026".equals(que.getIqfplxdm())){
			return;
		}
		
		String nsrsbh = taxinfo.getTaxno();
		String jrdm = taxinfo.getItfjrdm();
		String serviceUrl = taxinfo.getItfserviceUrl();
		RtnData rtn = new RtnData();
		BaiWangServiceImpl service = new BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		String res = service.getPdf(que, rtn);
		if(rtn.getCode()!=0){
			throw new RuntimeException("获取PDF文件失败，系统将稍后重试："+rtn.getMessage());
		}
		que.setIqpdf(res);
		que.setIqstatus(50);
		
	}
	
	/**
	 * 百望股份-调用PDF获取及推送
	 * @param que
	 */
	private void getBWGFPdf(Invque que,Taxinfo taxinfo){
		//如果不是电子票则直接返回
		if(!"026".equals(que.getIqfplxdm())){
			return;
		}
		RtnData rtn = new RtnData();
		BwgfServiceImpl service = new BwgfServiceImpl();
		FormatfileBuildResponse res = service.getPdf(que,taxinfo, rtn);
		if(rtn.getCode()!=0){
			throw new RuntimeException("获取PDF文件失败，系统将稍后重试："+rtn.getMessage());
		}
		que.setIqpdf(res.getData());
		que.setIqstatus(50);
		
	}
	
	/**
	 * 百望九赋-调用PDF获取及推送
	 * @param que
	 */
	private void getBWJFPdf(Invque que,Taxinfo taxinfo){
		//如果不是电子票则直接返回
		if(!"026".equals(que.getIqfplxdm())){
			return;
		}
		RtnData rtn = new RtnData();
		BwjfServiceImpl service = new BwjfServiceImpl();
		String res = service.getPdf(que,taxinfo, rtn);
		if(rtn.getCode()!=0){
			throw new RuntimeException("获取PDF文件失败，系统将稍后重试："+rtn.getMessage());
		}
		que.setIqpdf(res);
		que.setIqstatus(50);
		
	}
	
	
	/**
	 * 远的银座-开具发票
	 */
	private void openInvoiceYD(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		 
		RtnData rtn = new RtnData();
		RequestBillInfo reqeustinfo = new RequestBillInfo();
		
		if(detailList.get(0).getSheetid()==null||"".equals(detailList.get(0).getSheetid())){
			throw new RuntimeException("队列明细表没有查到对应的小票表序列");
		}
		reqeustinfo.setEntid(detailList.get(0).getEntid());
		reqeustinfo.setSheettype(detailList.get(0).getSheettype());
		reqeustinfo.setSheetid(detailList.get(0).getSheetid());
		
		ResponseBillInfo info = billDao.getInvoiceSaleHead(reqeustinfo);
		info.setInvoiceSaleDetail(detailList);
		ydService.openinvoice(que,taxinfo,info,rtn);
		if(rtn.getCode()!=0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}/*else{
			if(res==null){
				throw new RuntimeException("远的发票平台无返回信息");
			}
			

			que.setRtewm(res.getEwm());
			que.setRtfphm(res.getFp_hm());
			que.setRtfpdm(res.getFp_dm());
			que.setRtjym(res.getJym());
			que.setRtkprq(res.getKprq());
			que.setRtskm(res.getFwmw());
			que.setIqstatus(40);
		}*/
	}
	/**
	 * 远的标版-开具发票
	 */
	private void openInvoiceYDBb(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		 
		RtnData rtn = new RtnData();
		RequestBillInfo reqeustinfo = new RequestBillInfo();
		
/*		if(detailList.get(0).getSheetid()==null||"".equals(detailList.get(0).getSheetid())){
			throw new RuntimeException("队列明细表没有查到对应的小票表序列");
		}
		reqeustinfo.setEntid(que.getIqentid());
		reqeustinfo.setSheettype(detailList.get(0).getSheettype());
		reqeustinfo.setSheetid(detailList.get(0).getSheetid());
		
		ResponseBillInfo info = billDao.getInvoiceSaleHead(reqeustinfo);
		info.setInvoiceSaleDetail(detailList);*/
		if(que.getRetflag()==null) que.setRetflag("");
		YuanDeDzRntDataBean resInv = new YuanDeDzRntDataBean();
		if("1".equals(que.getRetflag())){
			resInv=	ydService.findInvoiceDzBbBean(que, taxinfo, rtn);
		}else{
		resInv = ydService.openinvoiceDzBb(que,taxinfo,detailList,rtn);
		}
		if(rtn.getCode()!=0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		if(resInv==null){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}else
		if(resInv.getInvoice().get(0).getInfoNumber()==null||"".equals(resInv.getInvoice().get(0).getInfoNumber())){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				detailList1.add(detail);
			}
		}
		
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
		//invoiceHead.setFpewm(resInv.getEwm());
		invoiceHead.setFphm(resInv.getInvoice().get(0).getInfoNumber());
		invoiceHead.setFpdm(resInv.getInvoice().get(0).getInfoTypeCode());
		//invoiceHead.setFpskm(resInv.getFwmw());
		//invoiceHead.setFpjym(resInv.getInvoice().get(0));
		invoiceHead.setFprq(resInv.getInvoice().get(0).getTime());
		invoiceHead.setPdf(resInv.getInvoice().get(0).getPdfurl());
		invoiceService.saveInvoice(invoiceHead);
		
		//que.setRtewm(resInv.getEwm());
		que.setRtfphm(resInv.getInvoice().get(0).getInfoNumber());
		que.setRtfpdm(resInv.getInvoice().get(0).getInfoTypeCode());
		//que.setRtskm(resInv.getFwmw());)
		//que.setRtjym(resInv.getJym());
		que.setIqpdf(resInv.getInvoice().get(0).getPdfurl());
		que.setRtkprq(resInv.getInvoice().get(0).getTime());

		//que.setIqstatus(50); 
	}

	/**
	 * 远的标版-发票查询
	 */
	private void openInvoiceYDBbFind(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		 
		RtnData rtn = new RtnData();
		//RequestBillInfo reqeustinfo = new RequestBillInfo();
		
		YuanDeDzRntDataBean resInv = new YuanDeDzRntDataBean();
		
		resInv=	ydService.findInvoiceDzBbBean(que, taxinfo, rtn);
	
		if(rtn.getCode()==0){
			 if(resInv!=null){
				 
				 List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
					for(InvoiceSaleDetail detail:detailList){
						if("Y".equals(detail.getIsinvoice())){
							detailList1.add(detail);
						}
					}
					
					InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
					//invoiceHead.setFpewm(resInv.getEwm());
					invoiceHead.setFphm(resInv.getInvoice().get(0).getInfoNumber());
					invoiceHead.setFpdm(resInv.getInvoice().get(0).getInfoTypeCode());
					//invoiceHead.setFpskm(resInv.getFwmw());
					//invoiceHead.setFpjym(resInv.getInvoice().get(0));
					invoiceHead.setFprq(resInv.getInvoice().get(0).getTime());
					invoiceHead.setPdf(resInv.getInvoice().get(0).getPdfurl());
					invoiceService.saveInvoice(invoiceHead);
					
					//que.setRtewm(resInv.getEwm());
					que.setRtfphm(resInv.getInvoice().get(0).getInfoNumber());
					que.setRtfpdm(resInv.getInvoice().get(0).getInfoTypeCode());
					//que.setRtskm(resInv.getFwmw());)
					//que.setRtjym(resInv.getJym());
					que.setIqpdf(resInv.getInvoice().get(0).getPdfurl());
					que.setRtkprq(resInv.getInvoice().get(0).getTime());
					que.setIqstatus(50); 
			 }
		}

	}
	
	/**
	 * 百旺股份-开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	private void openInvoiceBWGF(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		RtnData rtn = new RtnData();
		
		BwgfServiceImpl service = new BwgfServiceImpl();
		InvoiceOpenResponse resInv = service.openinvoice(que,taxinfo,detailList, rtn);
		
		if(rtn.getCode()!=0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		if(resInv==null){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}else
		if(resInv.getInvoiceNo()==null||"".equals(resInv.getInvoiceNo())){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		
		List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				detailList1.add(detail);
			}
		}
		
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
		invoiceHead.setFphm(resInv.getInvoiceNo());
		invoiceHead.setFpdm(resInv.getInvoiceCode());
		invoiceHead.setFpskm(resInv.getTaxControlCode());
		invoiceHead.setFpewm(resInv.getInvoiceQrCode());
		invoiceHead.setFpjym(resInv.getInvoiceCheckCode());
		invoiceHead.setFprq(resInv.getInvoiceDate());
		//invoiceHead.setPdf(resInv.getPdf_url());
		invoiceService.saveInvoice(invoiceHead);
		
		que.setRtewm(resInv.getInvoiceQrCode());
		que.setRtfphm(resInv.getInvoiceNo());
		que.setRtfpdm(resInv.getInvoiceCode());
		que.setRtskm(resInv.getTaxControlCode());
		que.setRtjym(resInv.getInvoiceCheckCode());
		//que.setIqpdf(resInv.getPdf_url());
		que.setRtkprq(resInv.getInvoiceDate());
		que.setIqstatus(40);
	}
	
	
	/**
	 * 百旺九赋-开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	private void openInvoiceBWJF(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		RtnData rtn = new RtnData();
		
		BwjfServiceImpl service = new BwjfServiceImpl();
		if("026".equals(que.getIqfplxdm())){
			System.out.println("进入聊百电票");
			BwjfDzRtInvoiceBean resInv = service.openinvoice(que,taxinfo,detailList, rtn);
			
			if(rtn.getCode()!=0){
				throw new RuntimeException("发票开具失败："+rtn.getMessage());
			}
			if(resInv==null){
				throw new RuntimeException("发票开具失败："+rtn.getMessage());
			}else
			if(resInv.getFp_hm()==null||"".equals(resInv.getFp_hm())){
				throw new RuntimeException("发票开具失败："+rtn.getMessage());
			}
			
			List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
			for(InvoiceSaleDetail detail:detailList){
				if("Y".equals(detail.getIsinvoice())){
					detailList1.add(detail);
				}
			}
			
			InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
			invoiceHead.setFphm(resInv.getFp_hm());
			invoiceHead.setFpdm(resInv.getFp_dm());
			invoiceHead.setFpskm(resInv.getJym());
			invoiceHead.setFpewm(resInv.getEwm());
			invoiceHead.setFpjym(resInv.getFp_mw());
			invoiceHead.setFprq(resInv.getKprq());
			//invoiceHead.setPdf(resInv.getPdf_url());
			invoiceService.saveInvoice(invoiceHead);
			
			que.setRtewm(resInv.getEwm());
			que.setRtfphm(resInv.getFp_hm());
			que.setRtfpdm(resInv.getFp_dm());
			que.setRtskm(resInv.getJym());
			que.setRtjym(resInv.getFp_mw());
			//que.setIqpdf(resInv.getPdf_url());
			que.setRtkprq(resInv.getKprq());
			que.setIqstatus(40);
		}else{
			if("1".equals(que.getRetflag())){
				BwjfRtInvoiceBean resInv = service.rtZpOpenToBean(que.getProcessMsg(), rtn);
				
				if(rtn.getCode()!=0){
					throw new RuntimeException("回写发票失败："+rtn.getMessage());
				}
				
				List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
				for(InvoiceSaleDetail detail:detailList){
					if("Y".equals(detail.getIsinvoice())){
						detailList1.add(detail);
					}
				}
				
				InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
				invoiceHead.setFphm(resInv.getFphm());
				invoiceHead.setFpdm(resInv.getFpdm());
				invoiceHead.setFpskm(resInv.getSkm());
				invoiceHead.setFpewm(resInv.getEwm());
				invoiceHead.setFpjym(resInv.getJym());
				invoiceHead.setFprq(resInv.getKprq());
				//invoiceHead.setPdf(resInv.getPdf_url());
				invoiceService.saveInvoice(invoiceHead);
				
				que.setRtewm(resInv.getEwm());
				que.setRtfphm(resInv.getFphm());
				que.setRtfpdm(resInv.getFpdm());
				que.setRtskm(resInv.getSkm());
				que.setRtjym(resInv.getJym());
				//que.setIqpdf(resInv.getPdf_url());
				que.setRtkprq(resInv.getKprq());
				que.setIqstatus(40);
			}else{
				que.setProcessMsg(service.urlconnnet(que, taxinfo, rtn));
				service.openinvoicezp(que, taxinfo, detailList, rtn);
			}
			
		}
	}

	/**
	 * 百旺电子-开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	private void openInvoiceBwdz(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList){
		RtnData rtn = new RtnData();
		if(que.getRetflag()==null) que.setRetflag("");
		BwdzRtOpenInvoiceBean resInv = null;
		if("1".equals(que.getRetflag())){
			resInv =	bwdzService.findInvoice(que, taxinfo, rtn);
		}else{
			 resInv = bwdzService.openinvoice(que,taxinfo,detailList, rtn);
		}
		
		if(rtn.getCode()!=0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		if(resInv==null){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}else
		if(resInv.getFp_hm()==null||"".equals(resInv.getFp_hm())){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		List<InvoiceSaleDetail> detailList1 = new ArrayList<InvoiceSaleDetail>();
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				detailList1.add(detail);
			}
		}
		
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, detailList1);
		//invoiceHead.setFpewm(resInv.getEwm());
		invoiceHead.setFphm(resInv.getFp_hm());
		invoiceHead.setFpdm(resInv.getFp_dm());
		//invoiceHead.setFpskm(resInv.getFwmw());
		invoiceHead.setFpjym(resInv.getJym());
		invoiceHead.setFprq(resInv.getKprq());
		invoiceHead.setPdf(resInv.getPdf_url());
		invoiceService.saveInvoice(invoiceHead);
		
		//que.setRtewm(resInv.getEwm());
		que.setRtfphm(resInv.getFp_hm());
		que.setRtfpdm(resInv.getFp_dm());
		//que.setRtskm(resInv.getFwmw());)
		que.setRtjym(resInv.getJym());
		que.setIqpdf(resInv.getPdf_url());
		que.setRtkprq(resInv.getKprq());
		que.setIqstatus(40);
	}
	
	/**
	 * 开红票获取红冲明细
	 * @param que
	 */
	public List<InvoiceSaleDetail> hongInvoiceDetail(Invque que){
		Map<String, Object> p=new HashMap<String, Object>();
		p.put("yfpdm", que.getIqyfpdm());
		p.put("yfphm", que.getIqyfphm());
		List<InvoiceSaleDetail> tm = SpringContextUtil.getBean(InvoiceAnddetailService.class).getHongInvoiceDetail(p);
		for(InvoiceSaleDetail detail:tm){
			detail.setIsinvoice("Y");
		}
		return tm;
	}
	
	/**
	 * 发票红冲或作废后判断小票是否可以重新抓取
	 * @param que
	 * @param seqno 
	 */
	public void restSaleInvoice(Invque que){
		if(que==null) {
			throw new RuntimeException("队列对象为NULL");
		}
			//统一队列信息
			Invque invque = invqueClone(que);
			//修改发票状态
			invoiceService.updateInvoice(invque);
			//该队列是否存在蓝票
			int invoiceStatus = invoiceService.getInvoiceCount(invque);
			if(invoiceStatus==0){
				updateInvoiceSaleFlagByserialid(invque,que.getIqseqno());
			}
		
	}
	
	public void updateInvoiceSaleFlagByserialid(Invque que,String hzSeqno){
		Map<String,Object> map = new NewHashMap<String,Object>();
		List<InvqueList> invquelist = new ArrayList<InvqueList>();
		map.put("iqseqno", que.getIqseqno());
		if(que.getIqseqno()==null||"".equals(que.getIqseqno())){
			
			throw new RuntimeException("队列对象为NULL");
			
		}
		invquelist =inqueDao.gethcInvqueList(map);
		
		InvoiceSaleHead head = new InvoiceSaleHead();
		head.setFlag(-1);
		head.setIqseqno(hzSeqno);
		head.setEntid(que.getIqentid());
		head.setBackflag(0);
		String serialid  = "";
		for(InvqueList quelist : invquelist){
			serialid += "'"+quelist.getSerialid()+"',";
		}
		if(serialid.length()>0){
			serialid =serialid.substring(0, serialid.length()-1);
		}
		head.setSerialid(serialid);
		billDao.updateInvoiceSaleFlagByserialid(head);
	 
	}
	
	public Invque invqueClone(Invque que){
		Map<String,Object> map = new NewHashMap<String,Object>();
		List<InvoiceHead> invoiceList = new ArrayList<InvoiceHead>();
		Invque invque = new Invque();		
		if(que.getIqtype()==1){ 
			map.put("fpDM", que.getIqyfpdm());
			map.put("fpHM", que.getIqyfphm());
			invoiceList =invoiceService.getInvoiceHeadByYfp(map);
			invque.setIqseqno(invoiceList.get(0).getIqseqno());
			invque.setRtfpdm(que.getIqyfpdm());
			invque.setRtfphm(que.getIqyfphm());
		}else{
			invque.setIqseqno(que.getIqseqno());
			invque.setRtfpdm(que.getRtfpdm());
			invque.setRtfphm(que.getRtfphm());
		}
		invque.setIqtype(que.getIqtype());
		invque.setIqentid(que.getIqentid());
		return invque;
	}


	/**
	 * 获取pdf文件，同时推送到邮箱
	 * @param invque
	 * @param rtn
	 */
	public void getPdf(Invque invque,RtnData rtn) {
		if(!"026".equals(invque.getIqfplxdm())){
			return;
		}
		String pdf = "";
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setEntid(invque.getIqentid());
		taxinfo.setTaxno(invque.getIqtaxno());
		try {
			taxinfo = taxinfoDao.getTaxinfoByNo(taxinfo);
			
		} catch (Exception e) {
			throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
		}
		if(taxinfo!=null){
			if(taxinfo.getTaxmode()==null) taxinfo.setTaxmode("");
			//没有启用短信的企业强制清空电话
			String usephone = entPrivatepara.get(taxinfo.getEntid(), FGlobal.usePhone);
			if(usephone == null) usephone="";
			if(!"1".equals(usephone)) {
				invque.setIqtel(null);
			}
			
			if("1".equals(taxinfo.getTaxmode())){//百望金税
		
				BaiWangServiceImpl service = new BaiWangServiceImpl(taxinfo.getItfserviceUrl(),taxinfo.getTaxno(),taxinfo.getItfjrdm());
				 pdf =  service.getPdf(invque, rtn);
				
			}if("3".equals(taxinfo.getTaxmode())){//远的
		
				String sendPerson = entPrivatepara.get(invque.getIqentid(), "sendPerson");
				if(StringUtils.isEmpty(sendPerson)) sendPerson="";
				
				if("1".equals(sendPerson)){
					
					Map<String,Object> map = new NewHashMap<String,Object>();
					map.put("iqseqno", invque.getIqseqno());

					invque.setInvqueList(inqueDao.gethcInvqueList(map));
					
					if(invque.getInvqueList()==null||invque.getInvqueList().isEmpty()){
						throw new RuntimeException("没有获取到队列号信息");
					}
					
					List<InvoiceSaleDetail> detailList = cookOpenInvoiceDetail(invque);
					RequestBillInfo reqeustinfo = new RequestBillInfo();
					if(detailList.get(0).getSheetid()==null||"".equals(detailList.get(0).getSheetid())){
						throw new RuntimeException("队列明细表没有查到对应的小票表序列");
					}
					reqeustinfo.setEntid(detailList.get(0).getEntid());
					reqeustinfo.setSheettype(detailList.get(0).getSheettype());
					reqeustinfo.setSheetid(detailList.get(0).getSheetid());
					
					ResponseBillInfo info = billDao.getInvoiceSaleHead(reqeustinfo);
					if(!StringUtils.isEmpty(info)){
						if(StringUtils.isEmpty(info.getGmfname())){
							throw new RuntimeException("该小票已按税局要求自动生成抬头为个人的发票，如需换开发票抬头，需到店换开");
						}
						if(StringUtils.isEmpty(info.getIsauto())){
							throw new RuntimeException("该小票不是自动换开的发票，如需换开发票抬头，需到店换开");
						}
						if("个人".equals(info.getGmfname())&&"1".equals(info.getIsauto())){
							ydService.tuiSonginvoice(invque, rtn);
							//openInvoiceYD(invque, taxinfo, detailList);//远的电票_银座版
						}else{
							throw new RuntimeException("该小票已按税局要求自动生成抬头为个人的发票，如需换开发票抬头，需到店换开");
						}
					}else{
						throw new RuntimeException("没有查询到此小票");
					}
				}
				
				
			}else if("5".equals(taxinfo.getTaxmode())){//百望股份
	
				BwgfServiceImpl service = new BwgfServiceImpl();
				FormatfileBuildResponse res = service.getPdf(invque,taxinfo, rtn);
				pdf = res.getData();
				//getBWGFPdf(invque, taxinfo);
			}else if("6".equals(taxinfo.getTaxmode())){//百望九赋
				BwjfServiceImpl service = new BwjfServiceImpl();
				pdf = service.getPdf(invque,taxinfo, rtn);
				//getBWJFPdf(invque, taxinfo);
			}
			if(pdf==null) pdf ="";
			//将PDF连接写入发票表
			if(rtn.getCode()==0&&!"".equals(pdf)){
				invque.setIqpdf(pdf);
				invque.setIqstatus(50);
				inqueDao.updatepdf(invque);
				invoiceService.updateInvoicePdf(invque);
			}
		}
	
	}
	
	/**
	 * 获取未返回的发票信息
	 * @param invque
	 * @param rtn
	 */
	public void gainInvoiceJob(Invque invque,RtnData rtn) {
		if(!"026".equals(invque.getIqfplxdm())){
			return;
		}		
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			throw new RuntimeException("没有获取到队列号信息");
		}
		
		boolean jsxp =false; //是否解锁小票
		
		Map<String,Object> map = new NewHashMap<String,Object>();
		map.put("iqseqno", invque.getIqseqno());
		invque.setInvqueList(inqueDao.gethcInvqueList(map));
		
		if(invque.getInvqueList()==null||"".equals(invque.getInvqueList())){
			throw new RuntimeException("没有获取到队列号信息");
		}
		
		String value = entPrivatepara.get(invque.getIqentid(), "releaseSaleInvoice");
		
		if(!StringUtils.isEmpty(value)){
			String split[] = value.split(":");

			if("A".equals(split[0])){
				if(invque.getIqinvoicetimes()>Integer.parseInt(split[1])){
					jsxp = true;
					//return;
				}
			}else
			if("B".equals(split[0])){
				//java.text.SimpleDateFormat formatter=new java.text.SimpleDateFormat("yyyy-MM-dd HH:dd:ss");    
				java.util.Calendar Cal=java.util.Calendar.getInstance();    
				Cal.setTime(invque.getIqdate());    
				
				Cal.add(java.util.Calendar.HOUR_OF_DAY,Integer.parseInt(split[1]));      
				//System.out.println(Cal.getTime().before(new java.util.Date()));  
				if(Cal.getTime().before(new java.util.Date())){
					jsxp = true;
					//return;
				}
			}else
				if("C".equals(split[0])){ 
					 
				}
		}else{
			
			if(invque.getIqinvoicetimes()>3){
				inqueDao.updateTo99(invque);
				return;
			}
		}
		
		if(jsxp){//将符合条件的小票解锁
			InvoiceSaleHead salehead = new InvoiceSaleHead();
			salehead.setIqseqno(invque.getIqseqno());
			salehead.setEntid(invque.getIqentid());
			salehead.setSheettype(invque.getIqsource());
			for(InvqueList invquelist :invque.getInvqueList()){
				salehead.setSheetid(invquelist.getSheetid());
				billDao.unLockedInvoiceSaleFlag(salehead);
				inqueDao.updateTo99(invque);
			}
			return;
		}

		inqueDao.updateinvoicetimes(invque);
		
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setEntid(invque.getIqentid());
		taxinfo.setTaxno(invque.getIqtaxno());
		
		try {
			taxinfo = taxinfoDao.getTaxinfoByNo(taxinfo);
 
		} catch (Exception e) {
			throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
		}
		
		if(taxinfo!=null){
			if(taxinfo.getTaxmode()==null) taxinfo.setTaxmode("");
			if(invque.getIqseqno()!=null&&!"".equals(invque.getIqseqno())){

				boolean invoiceSaleHeadflag = true;
				for(InvqueList invquelist :invque.getInvqueList()){
					ResponseBillInfo billinfo = billDao.getInvoiceSaleHeadByiqseqno(invquelist.getSerialid());
					if(billinfo==null){
						invoiceSaleHeadflag = false;
						break;
					}else{
						if(billinfo.getFlag()!=1){
							invoiceSaleHeadflag = false;
							break;
						}
					}
					
				}
				if(invoiceSaleHeadflag){
					try{
					List<InvoiceSaleDetail> detailList = cookOpenInvoiceDetail(invque);
					
					if("1".equals(taxinfo.getTaxmode())){//百望金税
						invque.setRetflag("1");
						openInvoiceBW(invque,taxinfo,detailList);
					}else if("3".equals(taxinfo.getTaxmode())){
							openInvoiceYD(invque, taxinfo, detailList);//远的电票_银座版
					}else if("4".equals(taxinfo.getTaxmode())){//百望电子
						invque.setRetflag("1");
						openInvoiceBwdz(invque,taxinfo,detailList);
					}else if("5".equals(taxinfo.getTaxmode())){//百望股份
						openInvoiceBWGF(invque,taxinfo,detailList);
					}else if("6".equals(taxinfo.getTaxmode())){//百望九赋
						openInvoiceBWJF(invque,taxinfo,detailList);
					}else if("7".equals(taxinfo.getTaxmode())){//山东航信
						invque.setRetflag("1");
						//openInvoiceYDBb(invque,taxinfo,detailList);
						openInvoiceYDBbFind(invque,taxinfo,detailList);
					}else if(!"".equals(taxinfo.getTaxmode())){
						invque.setRetflag("1");
						PortService portservice = PortServiceFactory.getInstance(taxinfo.getTaxmode());
						portservice.openInvoice(invque, taxinfo, detailList);
					}  else{
						throw new RuntimeException("发票平台类型未实现："+taxinfo.getTaxmode());
					}
					inqueDao.updateToFp(invque);
					inqueDao.updatepdf(invque);
					if(invque.getIqtype()==1){
						restSaleInvoice(invque);
					}
					}catch(Exception e){
						if(rtn.getCode()!=0){
							rtn.setCode(0);
						}
					}
				}
			}
		}
		
	}
	
	//返回发票数据记录到数据库 --九赋纸票
	public void retpapiao(Invque que) {
	try {
		que.setRetflag("1");//标记为1时时页面返回发票数据记录数据库
		if(que.getInvqueList()==null){
			Map<String,Object> map = new NewHashMap<String,Object>();
			List<InvqueList> invquelist = new ArrayList<InvqueList>();
			map.put("iqseqno", que.getIqseqno());
			if(que.getIqseqno()==null||"".equals(que.getIqseqno())){
				
				throw new RuntimeException("队列对象为NULL");
				
			}
			invquelist =inqueDao.gethcInvqueList(map);
			que.setInvqueList(invquelist);
		}
		List<InvoiceSaleDetail> detailList = cookOpenInvoiceDetail(que);
		
		String nsrsbh = que.getIqtaxno();
		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setEntid(que.getIqentid());
		taxinfo.setTaxno(nsrsbh);
		try {
			taxinfo = taxinfoDao.getTaxinfoByNo(taxinfo);
			
			
			if(!"".equals(que.getIqtaxzdh())&&!"026".equals(que.getIqfplxdm())){
				Taxinfo taxinfoitem = new Taxinfo();
				taxinfoitem.setEntid(que.getIqentid());
				taxinfoitem.setTaxno(que.getIqtaxno());
				taxinfoitem.setItfkpd(que.getIqtaxzdh());
				try {
					taxinfoitem = taxinfoDao.getTaxinfoItemByNo(taxinfoitem);
					if(taxinfoitem!=null){
						if(taxinfoitem.getTaxmode()==null) taxinfoitem.setTaxmode("");
						if(!"".equals(taxinfoitem.getTaxmode())){
							taxinfo = taxinfoitem;
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
				}
			}	
		} catch (Exception e) {
			throw new RuntimeException("获取开票方信息失败："+e.getLocalizedMessage());
		}
		
		openInvoiceBWJF(que,taxinfo,detailList);
		inqueDao.updateToFp(que);
		inqueDao.updateTo50(que);
		if(que.getIqtype()==1){
			restSaleInvoice(que);
		}
	//	SpringContextUtil.getBean(AskInvoiceTask.class).nxImmediOpenInvoice(que,detailList);
	} catch (Exception e) {
		log.error(e,e);
		que.setIqstatus(30);
		que.setIqmsg(e.getMessage());
		inqueDao.updateTo30(que);
	}
}
}
