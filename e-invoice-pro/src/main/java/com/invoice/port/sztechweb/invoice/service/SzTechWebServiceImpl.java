package com.invoice.port.sztechweb.invoice.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.PortService;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebResponseBlueBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebQueryRequestBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebQueryResponseBean;
import com.invoice.port.sztechweb.invoice.bean.SzTechWebResponseRedBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.NewHashMap;
import com.alibaba.fastjson.JSON;
import com.golden.Sdk;
import com.golden.request.InvoiceBlue;
import com.golden.request.InvoiceBlueGoodsInfo;
import com.golden.request.InvoiceRed;
import com.golden.request.InvoiceRedInvoice;
import net.sf.json.JSONObject;

@Service("SzTechWebServiceImpl")
public class SzTechWebServiceImpl implements PortService {
	private final Log log = LogFactory.getLog(SzTechWebServiceImpl.class);
	
	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	InvqueDao inqueDao;
	
	/**
	 * 开具发票
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	@Override
	public void openInvoice(Invque que,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList)
	{
		RtnData rtn = new RtnData();
		
		if(que.getRetflag() == null) que.setRetflag("");
		
		RtInvoiceBean resInv = null;
		
		if("1".equals(que.getRetflag())){
			resInv = findInvoice(que,taxinfo,rtn);
		}
		else{
			resInv = openInvoiceDP(que,taxinfo,detailList,rtn);
		}
		
		if(rtn.getCode() != 0){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		else if(resInv == null){
			throw new RuntimeException("发票开具失败："+rtn.getMessage());
		}
		else if(resInv.getReturncode() != null && resInv.getReturncode().equals("-2")){
			throw new RuntimeException(resInv.getReturnmessage()==null?"高灯返回开票失败!":resInv.getReturnmessage());
		}
		else if(resInv.getReturncode() != null && 
				resInv.getReturncode().equals("2") && 
				(resInv.getFp_hm() == null || "".equals(resInv.getFp_hm()))){
			throw new RuntimeException("高灯返回开票成功但没有发票号!");
		}
		
		//过滤出需要开票的商品
		List<InvoiceSaleDetail> dataList = new ArrayList<InvoiceSaleDetail>();
		
		for(InvoiceSaleDetail detail:detailList){
			if("Y".equals(detail.getIsinvoice())){
				dataList.add(detail);
			}
		}
		
		//生成Invoice_head和Invoice_detail数据
		InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, dataList);

		invoiceHead.setFpskm(resInv.getSkm()==null?"":resInv.getSkm());
		invoiceHead.setFprq(resInv.getKprq()==null?"19990101":resInv.getKprq().replace("-","").replace(":","").replace(" ",""));
		invoiceHead.setFphm(resInv.getFp_hm()==null?"":resInv.getFp_hm());
		invoiceHead.setFpdm(resInv.getFp_dm()==null?"":resInv.getFp_dm());
		invoiceHead.setPdf(resInv.getPdf_url()==null?"":resInv.getPdf_url());
		invoiceHead.setFpjym(resInv.getJym()==null?"":resInv.getJym());
		
		//保存到Invoice_head和Invoice_detail表
		invoiceService.saveInvoice(invoiceHead);
		
		//针对深圳高灯特别作法：因为开具成功后，增加回调URL让高灯调用
		que.setRtskm(resInv.getSkm()==null?"":resInv.getSkm());
		que.setRtkprq(resInv.getKprq()==null?"19990101":resInv.getKprq().replace("-","").replace(":","").replace(" ",""));
		que.setRtfphm(resInv.getFp_hm()==null?"":resInv.getFp_hm());
		que.setRtfpdm(resInv.getFp_dm()==null?"":resInv.getFp_dm());
		que.setIqpdf(resInv.getPdf_url()==null?"":resInv.getPdf_url());
		que.setRtjym(resInv.getJym()==null?"":resInv.getJym());
		
		//如果开票正确则更改状态
		if (resInv.getReturncode() != null && resInv.getReturncode().equals("2"))
		{
			que.setIqstatus(50);
			inqueDao.updateForCallGD(que);
		}
		else
		{
			que.setIqstatus(40);
			inqueDao.updateTo40(que);
		}
	}

	/**
	 * 空白票查询
	 * @param que
	 * @param taxinfo
	 */
	@Override
	public RtInvoiceBean blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		return null;
	}
	
	/**
	 * 空白发票作废
	 * **/
	@Override
	public RtInvoiceBean invoiceInblankValid(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		return null;
	}
	
	/**
	 * 已开发票作废
	 * **/
	@Override
	public RtInvoiceBean invoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		log.info("高灯作废接口：请使用红冲功能进行作废!");
		rtn.setCode(-1);
		rtn.setMessage("请使用红冲功能进行作废!");
		return null;
	}
	
	/**
	 * 发票打印
	 * **/
	@Override
	public void invoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		return;
	}
	
	/**
	 * 获取发票PDF连接 
	 * **/
	@Override
	public String getPdf(Invque invque,RtnData rtn)
	{
		return null;
	}
	
	/**
	 * 发票查询
	 * @param que
	 * @param taxinfo
	 * @param detailList
	 */
	@Override
	public RtInvoiceBean findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn)
	{
		try {
			RtInvoiceBean invoiceBean = new RtInvoiceBean();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			//调用HTTP/HTTPS
			SzTechWebGenerateImpl mySend = new SzTechWebGenerateImpl();
			
			//设置请求参数
			SzTechWebQueryRequestBean request = new SzTechWebQueryRequestBean();
			
			request.setIs_red(invque.getIqtype());       //0：蓝票， 1：红票
			request.setOrder_id(invque.getIqseqno());    //开票流水号
			request.setTaxpayer_num(taxinfo.getTaxno()); //销方税号
			
			String jsonText = JSONObject.fromObject(request).toString();

			String strUrl = taxinfo.getItfserviceUrl();
			if (strUrl == null || strUrl.length() == 0) 
			{
				log.info("高灯查询接口：获取开票远程URL出错!");
				rtn.setCode(-1);
				rtn.setMessage("获取开票远程URL出错!");
				return null;
			}
			
			Long timestamp = System.currentTimeMillis() / 1000;
			
			String sign = mySend.genereateSign(JSONObject.fromObject(jsonText), timestamp, taxinfo.getItfskpbh(), taxinfo.getItfskpkl());
			
			strUrl = strUrl.trim() + "/invoice/status";
			
			strUrl = strUrl + "?signature=" + sign + "&appkey=" + taxinfo.getItfskpbh() + "&timestamp=" + timestamp;

			String strResult = mySend.sendHttpRequest(strUrl, jsonText);
			
			if (strResult == null || strResult.length() == 0) 
			{
				log.info("高灯查询接口：获取发票查询结果失败!");
				rtn.setCode(-1);
				rtn.setMessage("获取发票查询结果失败!");
				return null;
			}
			
			if (strResult.startsWith("[ERROR]") || strResult.startsWith("[FATAL]"))
			{
				log.info("高灯查询接口：" + strResult.substring(7));
				rtn.setCode(-1);
				rtn.setMessage(strResult.substring(7));
				return null;
			}
			
			JSONObject jsonObj = JSONObject.fromObject(strResult);
			
			if (jsonObj.getInt("code") != 0)
			{
				log.info("高灯查询接口：" + jsonObj.getString("msg"));
				rtn.setCode(-1);
				rtn.setMessage(jsonObj.getString("msg"));
				return null;
			}
			
			//转成JAVA对象
			SzTechWebQueryResponseBean queryReponse = (SzTechWebQueryResponseBean)JSON.parseObject(strResult, SzTechWebQueryResponseBean.class);
			
			invoiceBean.setSkm(queryReponse.getData().getG_unique_id()); //开票平台订单id
			invoiceBean.setReturncode(String.valueOf(queryReponse.getData().getStatus())); //发票状态（0：待开票 1：开票中 2：开票成功 -2：开票失败）
			invoiceBean.setReturnmessage(queryReponse.getData().getFail_msg()); //开票错误信息
			invoiceBean.setKprq(sdf.format(queryReponse.getData().getTicket_date())); //开票日期
			invoiceBean.setFp_hm(queryReponse.getData().getTicket_sn()); //发票号码
			invoiceBean.setFp_dm(queryReponse.getData().getTicket_code()); //发票代码
			invoiceBean.setCzdm(queryReponse.getData().getIs_red()); //是否为红票
			invoiceBean.setPdf_url(queryReponse.getData().getGp_pdf_name()); //发票pdf文件
			invoiceBean.setJym(queryReponse.getData().getCheck_code()); //发票校验码

			return invoiceBean;
		}
		catch (Exception ex)
		{
			log.error("高灯查询接口：" + ex.toString());
			rtn.setCode(-1);
			rtn.setMessage(ex.toString());
			
			return null;
		}
	}
	
	//开具电票
	public RtInvoiceBean openInvoiceDP(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList, RtnData rtn)
	{
		try
		{
			RtInvoiceBean invoiceBean = new RtInvoiceBean();
		
			//调用高灯SDK
			Sdk sdk = new Sdk(taxinfo.getItfskpbh(), taxinfo.getItfskpkl(), taxinfo.getItfkeypwd());

			//蓝票
			if (invque.getIqtype() == 0)
			{
				InvoiceBlue invoice = new InvoiceBlue();
				List<InvoiceBlueGoodsInfo> goodsInfos = new ArrayList<>();
				
				for (InvoiceSaleDetail itemData :detailList)
				{
					InvoiceBlueGoodsInfo goodsInfo = new InvoiceBlueGoodsInfo();
					
					goodsInfo.setName(itemData.getGoodsname()) //商品名称
                		     .setPrice(itemData.getPrice().toString()) //不含税单价
                		     .setTaxAmount(itemData.getTaxfee().toString()) //税额
                		     .setTaxRate(itemData.getTaxrate()>0?itemData.getTaxrate().toString():"0") //税率
                		     .setTaxCode(itemData.getTaxitemid()) //税目
                		     .setTotal(itemData.getQty().toString()) //数量
                		     .setTotalPrice(itemData.getAmount().toString()) //不含税金额
                		     .setUnit(itemData.getUnit()) //单位
                		     .setModels(itemData.getSpec()) //规格
                		     .setZeroTaxFlag(itemData.getZerotax()); //空：非零税率， 0：出口零税，1：免税，2：不征税，3：普通零税率
					goodsInfos.add(goodsInfo);
				}

				invoice.setBuyerTitleType(invque.getIqgmftax()==null||invque.getIqgmftax().length()==0?1:2) //1：个人，2：企业
                       .setTaxpayerNum(taxinfo.getTaxno()) //销方税号
                       .setOrderId(invque.getIqseqno()) //订单号
                       .setBuyerEmail(invque.getIqemail()) //购方Email
                       .setBuyerTitle(invque.getIqgmfname()) //购方名称
                       .setBuyerAddress(invque.getIqgmfadd()) //购方地址
                       .setBuyerPhone(invque.getIqtel()) //购方电话
                       .setBuyerBankAccount("") //银行账号
                       .setBuyerBankName(invque.getIqgmfbank()) //开户银行
                       .setSellerBankName(taxinfo.getTaxbank())
                       .setSellerAddress(taxinfo.getTaxadd())
                       .setExtra(invque.getIqmemo()) //备注内容
                       .setCashier(invque.getIqpayee()) //收款人
                       .setChecker(invque.getIqchecker()) //复核人
                       .setInvoicer(invque.getIqadmin()) //开票人
                       .setTradeType(0) //1通信、2餐饮、3交通、4支付平台、5票务/旅游、0其他
                       .setCallbackUrl(taxinfo.getItfjrdm()) //回调URL
                       .setInvoiceTypeCode("032") //004:增值税专用发票，007:增值税普通发票，026：增值税电子发票，025：增值税卷式发票, 032:区块链发票 默认为026
                       .setGoodsInfos(goodsInfos);
				
				JSONObject result = sdk.invoiceBlue(invoice);
				
				if (result == null)
				{
					log.info("高灯开具接口：调用开具蓝票功能返回结果为空");
					rtn.setCode(-1);
					rtn.setMessage("调用开具蓝票功能返回结果为空");
					
					return null;
				}
				
				if (result.getInt("code") != 0)
				{
					log.info("高灯开具接口：" + result.getString("msg"));
					rtn.setCode(-1);
					rtn.setMessage(result.getString("msg"));
					
					return null;
				}
				
				//开具结果转对象
				SzTechWebResponseBlueBean blue = (SzTechWebResponseBlueBean)JSON.parseObject(result.toString(),SzTechWebResponseBlueBean.class);
				
				//先记录下高灯开票流水号
				invque.setRtskm(blue.getData().getG_unique_id());
			}
			else //红票
			{
				InvoiceRed invoiceRed = new InvoiceRed();
		        List<InvoiceRedInvoice> redInvoices = new ArrayList<>();
		        InvoiceRedInvoice redInvoice = new InvoiceRedInvoice();
		        
		        //根据发票号查原订单号
		        Map<String,Object> map = new NewHashMap<String,Object>();
				map.put("fpDM", invque.getIqyfpdm());
				map.put("fpHM", invque.getIqyfphm());
				
				List<InvoiceHead> headList = invoiceService.getInvoiceHeadByYfp(map);
				if (headList == null || headList.isEmpty())
				{
					log.info("高灯红票接口：红冲或作废时没有查到原发票号!");
					rtn.setCode(-1);
					rtn.setMessage("红冲或作废时没有查到原发票号!");
					
					return null;
				}
		        
		        redInvoice.setTaxpayerNum(taxinfo.getTaxno())
		                  .setgTradeNo(headList.get(0).getIqseqno())
		                  .setbTradeNo("");
		        
		        redInvoices.add(redInvoice);

		        invoiceRed.setCallbackUrl(taxinfo.getItfjrdm()).setInvoices(redInvoices);

		        JSONObject result = sdk.invoiceRed(invoiceRed);
		        
		        if (result == null)
				{
					log.info("高灯红票接口：调用开具红票功能返回结果为空");
					rtn.setCode(-1);
					rtn.setMessage("调用开具红票功能返回结果为空");
					
					return null;
				}
				
				if (result.getInt("code") != 0)
				{
					log.info("高灯红票接口：" + result.getString("msg"));
					rtn.setCode(-1);
					rtn.setMessage(result.getString("msg"));
					
					return null;
				}
				
				//红票结果转对象
				SzTechWebResponseRedBean red = (SzTechWebResponseRedBean)JSON.parseObject(result.toString(),SzTechWebResponseRedBean.class);
				
				//先记录下高灯开票流水号
				invque.setRtskm(red.getData().get(0).getG_trade_no());
			}
			
			//等待10秒后查询开具结果
			Thread.sleep(10000);
			invoiceBean = findInvoice(invque, taxinfo, rtn);
			
			return invoiceBean;
        }
		catch (Exception ex)
		{
			log.error("高开具灯接口：" + ex.toString());
			rtn.setCode(-1);
			rtn.setMessage(ex.toString());
			
			return null;
		}
	}
}
