package com.invoice.yuande.invoice.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.springframework.util.StringUtils;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.ResponseInvoice;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;
import com.invoice.util.XMLConverter;
import com.invoice.yuande.invoice.bean.YuanDeDzRntDataBean;
import com.invoice.yuande.invoice.bean.YuanDeInvoiceSaleDetailBean;
import com.invoice.yuande.invoice.bean.YuanDeInvoiceSaleDetailList;
import com.invoice.yuande.invoice.bean.YuanDeInvoiceSaleDzDetailBean;
import com.invoice.yuande.invoice.bean.YuanDeInvoiceSaleDzHeadBean;
import com.invoice.yuande.invoice.bean.YuanDeInvoiceSaleHeadBean;
import com.invoice.yuande.invoice.bean.YuanDeResultBean;
import com.invoice.yuande.invoice.bean.YuanDeRntDataBean;
import com.invoice.yuande.invoice.bean.YuanDeRtFindInvoiceHeadBean;
import com.invoice.yuande.invoice.bean.YuanDeRtInvoiceBean;

import baiwang.invoice.service.XmlUtil;

public class YuanDeGenerateBean {
	
	
	//纳税人识别号
	String nsrsbh;
	//数据交换流水号
	String dataExchangeId;
	
	public YuanDeGenerateBean(String nsrsbh ){
		this.nsrsbh = nsrsbh;
		
	}
	
	/**
	 * 开具发票
	 * 装换为航信接口对象 
	 * 银座
	 * **/
	public void generateHangXinBean(Invque invque,Taxinfo taxinfo,ResponseBillInfo saleshead,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对航信数据对象定义
		 * head 开票头
		 * bwDetailList 开票明细集合
		 * **/
		YuanDeInvoiceSaleHeadBean head = new YuanDeInvoiceSaleHeadBean();
		YuanDeInvoiceSaleDetailList list = new YuanDeInvoiceSaleDetailList();
		List<YuanDeInvoiceSaleDetailBean> bwDetailList = new ArrayList<YuanDeInvoiceSaleDetailBean>();
		YuanDeResultBean result = new YuanDeResultBean();
		YuanDeRntDataBean rntData = new YuanDeRntDataBean();
		result.setRntcode("0001");
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * detailList 小票数据集合
		 * **/
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}
		//Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return;
		}
		//List<InvoiceSaleDetail>  detailList= (ArrayList)map.get("detailList");
		if(saleshead ==null){
			rtn.setCode(-1);
			rtn.setMessage("小票头数据 saleshead 没有初始化");
			return;
		}
		 List<InvoiceSaleDetail> detailList = saleshead.getInvoiceSaleDetail();
		
		 if(detailList ==null){
				rtn.setCode(-1);
				rtn.setMessage("小票数据 detailList 没有初始化");
				return;
			}
		 
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		}else{
			head.setSerialid(invque.getIqseqno());//发票流水号
			//dataExchangeId = invque.getIqseqno();
		}
		
		if(saleshead.getSheettype()==null||"".equals(saleshead.getSheettype())){
			rtn.setCode(-1);
			rtn.setMessage("单据类型不能为空");
			return;
		}else{
			head.setSheettype(saleshead.getSheettype());//发票流水号
			//dataExchangeId = invque.getIqseqno();
		}
		if("0".equals(saleshead.getSheettype())||"3".equals(saleshead.getSheettype())){
			
			head.setFpqqlsh(saleshead.getSerialid());//交易流水号
			
		}else{
			if(saleshead.getShopid()==null||"".equals(saleshead.getShopid())){
				rtn.setCode(-1);
				rtn.setMessage("门店不能为空");
				return;
			}else
			if(saleshead.getTradedate()==null||"".equals(saleshead.getTradedate())){
				rtn.setCode(-1);
				rtn.setMessage("交易日期不能为空");
				return;
			}else
			if(saleshead.getSyjid()==null||"".equals(saleshead.getSyjid())){
				rtn.setCode(-1);
				rtn.setMessage("收银机号不能为空");
				return;
			}else
			if(saleshead.getBillno()==null||"".equals(saleshead.getBillno())){
				rtn.setCode(-1);
				rtn.setMessage("小票号不能为空");
				return;
			}else{
				head.setFpqqlsh(saleshead.getShopid()+"-"+saleshead.getSyjid()+"-"+saleshead.getBillno());//交易流水号
			}
		}
		
		
		if(invque.getIqgmfname()==null||"".equals(invque.getIqgmfname())){
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		}else{
			head.setGhfmc(invque.getIqgmfname());//购方客户名称
		}
		
		
		head.setGhf_nsrsbh(invque.getIqgmftax()==null?"":invque.getIqgmftax());//购方单位代码
		head.setFkfkhyh_fkfyhzh(invque.getIqgmfbank()==null?"":invque.getIqgmfbank());//购方开户行及账号
		head.setFkfdz_fkfdh(invque.getIqgmfadd()==null?"":invque.getIqgmfadd());//购方地址及电话
		
		head.setXhfkhyh_skfyhzh(taxinfo.getTaxbank()==null?"":taxinfo.getTaxbank());//销货方银行及账号
		
		if(taxinfo.getTaxadd()==null||"".equals(taxinfo.getTaxadd())){
			rtn.setCode(-1);
			rtn.setMessage("销货方地址电话不能为空");
			return;
		}else{
			head.setXhfdz_xhfdh(taxinfo.getTaxadd());//销货方地址电话
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		}else{
			if("026".equals(invque.getIqfplxdm())){
				head.setFpzl_dm("51");//发票种类编码
			}else
			if("025".equals(invque.getIqfplxdm())){
				head.setFpzl_dm("41");//发票种类编码
			}else
			if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
				head.setFpzl_dm(invque.getIqfplxdm());//发票种类编码
			}else{
				rtn.setCode(-1);
				rtn.setMessage("发票种类编码不正确");
				return;
			}
		}
		
		
		if(invque.getIqtype()==1){
			
			if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			}else{
				head.setYfp_dm(invque.getIqyfpdm());//原发票代码
			}
			
			if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			}else{
				head.setYfp_hm(invque.getIqyfphm());//原发票号码
			}

			//head.setJshj((Double)invque.getIqtotje()+(Double)invque.getIqtotse());//价税合计  
			head.setJshj((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1);//价税合计
			head.setHjje((Double)invque.getIqtotje()*-1);//合计金额
			head.setHjse((Double)invque.getIqtotse()*-1);//合计税额
			
		}else{
			head.setYfp_dm("");//原发票代码
			head.setYfp_hm("");//原发票号码
			
			//head.setJshj((Double)invque.getIqtotje()+(Double)invque.getIqtotse());//价税合计  
			head.setJshj(MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2));//价税合计
			head.setHjje((Double)invque.getIqtotje());//合计金额
			head.setHjse((Double)invque.getIqtotse());//合计税额
		}
		
		head.setBz(invque.getIqmemo()==null?"":invque.getIqmemo());//备注
		

		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		}else{
			head.setKpy(invque.getIqadmin());//开票人
		}
		head.setSky("");//收款人
		head.setFhr("");//审核人
		head.setXhqd("");

		
		if(invque.getIqtype()==0||invque.getIqtype()==1||invque.getIqtype()==2){
			head.setKplx(invque.getIqtype());//开票类型
		}else{
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}
	
		
		head.setBmb_bbh(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh());//商品编码版本号
		
		if(invque.getIqchannel()!=null&&!"".equals(invque.getIqchannel())){
			
			if("app".equals(invque.getIqchannel())){
				head.setYwlx("3");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else
			if("wx".equals(invque.getIqchannel())){
				head.setYwlx("2");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else
			if("mp".equals(invque.getIqchannel())){
				head.setYwlx("3");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else{
				rtn.setCode(-1);
				rtn.setMessage("业务类型不正确");
				return;
			}

		}else{  
			rtn.setCode(-1);
			rtn.setMessage("业务类型不正确");
			return;
		}
		
		head.setShopid(saleshead.getShopid());//门店ID
		head.setXpjysj(saleshead.getTradedate());//小票交易时间
		head.setEmail(invque.getIqemail()==null?"":invque.getIqemail());//购买方邮箱
		head.setMemo(invque.getPaynote());//备注(支付方式)
		head.setUserid(invque.getIqperson());//登陆人员名称
		
		
		
		//开票明细转换
		/**
		 * 从商品明细中计算小票交易金额
		 * */
		double oldamtsum = 0.0;
		//对储值卡开票的商品做合并商品处理
		if("0".equals(saleshead.getSheettype())){
			
			//按商品编码合并数据
			List<InvoiceSaleDetail> cookList = new ArrayList<InvoiceSaleDetail>();
			for (InvoiceSaleDetail detail : detailList) {
				boolean hasJoin = false;
				for (InvoiceSaleDetail cookItem : cookList) {
					//对商品编码一致，则进行合计
					if(detail.getGoodsid().equals(cookItem.getGoodsid())){
						hasJoin = true;
						//累加金额和数量，重算单价
						double qty = MathCal.add(cookItem.getQty() , detail.getQty(),8);
						double amount = MathCal.add(cookItem.getAmount(), detail.getAmount(), 2);
						double taxFee = MathCal.add(cookItem.getTaxfee(), detail.getTaxfee(), 2);
						double oldAmt = MathCal.add(cookItem.getOldamt(), detail.getOldamt(), 2);
						
						cookItem.setQty(qty);
						cookItem.setAmount(amount);
						cookItem.setTaxfee(taxFee);
						cookItem.setOldamt(oldAmt);
					}
				}
				
				if(!hasJoin) {
					cookList.add(detail);
				}
				
			}
			
			for(InvoiceSaleDetail detail:cookList){
				
				if(detail.getTaxitemid()==null) detail.setTaxitemid("");
				//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
				if (!"".equals(detail.getTaxitemid())) {	
				//退货商品原始金额为0的过滤掉
					if(detail.getOldamt()>0){
						
							YuanDeInvoiceSaleDetailBean bwdetail = new YuanDeInvoiceSaleDetailBean();
								
							if(invque.getIqtype()==1){
								bwdetail.setSpsl(String.valueOf(detail.getQty()*-1));//商品数量
								bwdetail.setSpje(String.valueOf(detail.getAmount()*-1));//金额
								bwdetail.setSe(String.valueOf(detail.getTaxfee()*-1));//税额
							}else{
								bwdetail.setSpsl(String.valueOf(detail.getQty()));//商品数量
								bwdetail.setSpje(String.valueOf(detail.getAmount()));//金额
								bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
							}
									
								
								bwdetail.setSpdj(String.valueOf(MathCal.add((detail.getAmount()/detail.getQty()),0,8)));//detail.getPrice();单价

								
								bwdetail.setOldamt(String.valueOf(detail.getOldamt()));//原始金额
								
								bwdetail.setSpmc(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
								bwdetail.setSm("");//商品所属类别
								bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate()))?"":String.valueOf(detail.getTaxrate()));//税率
								//bwdetail.setGgxh("");//规格型号
								bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
								bwdetail.setJldw(detail.getUnit()==null?"":detail.getUnit());//计量单位
								
								
								
								
								bwdetail.setFphxz("0");//发票行性质
								//bwdetail.setFphxz("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//发票行性质
								bwdetail.setHsjbz("0");//含税标志
								
								bwdetail.setSpbm(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
								bwdetail.setZxbm("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//商品行号
								bwdetail.setYhzcbs(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
								bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
								bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
								
								bwdetail.setIsinvoice(detail.getIsinvoice()==null?"":detail.getIsinvoice());//是否开票
								
								bwdetail.setGoodsid(detail.getGoodsid()==null?"":detail.getGoodsid());
								oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
								bwDetailList.add(bwdetail);
						
					}
				}else{
					oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
					}	
		
			}
		}else{
			for(InvoiceSaleDetail detail:detailList){
				if(detail.getTaxitemid()==null) detail.setTaxitemid("");
				//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
				if (!"".equals(detail.getTaxitemid())) {	
				//退货商品原始金额为0的过滤掉
					if(detail.getOldamt()>0){
						YuanDeInvoiceSaleDetailBean bwdetail = new YuanDeInvoiceSaleDetailBean();
						
						if(invque.getIqtype()==1){
							bwdetail.setSpsl("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()*-1));//商品数量
							bwdetail.setSpje("null".equals(String.valueOf(detail.getAmount()))?"":String.valueOf(detail.getAmount()*-1));//金额
							bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee()))?"":String.valueOf(detail.getTaxfee()*-1));//税额
						}else{
							bwdetail.setSpsl("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()));//商品数量
							bwdetail.setSpje("null".equals(String.valueOf(detail.getAmount()))?"":String.valueOf(detail.getAmount()));//金额
							bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee()))?"":String.valueOf(detail.getTaxfee()));//税额
						}
						
						bwdetail.setSpmc(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
						bwdetail.setSm("");//商品所属类别
						bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate()))?"":String.valueOf(detail.getTaxrate()));//税率
						//bwdetail.setGgxh("");//规格型号
						bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
						bwdetail.setJldw(detail.getUnit()==null?"":detail.getUnit());//计量单位
						
						
						bwdetail.setSpdj("null".equals(String.valueOf(detail.getPrice()))?"":String.valueOf(detail.getPrice()));//detail.getPrice();单价
						
						
						bwdetail.setFphxz("0");//发票行性质
						//bwdetail.setFphxz("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//发票行性质
						bwdetail.setHsjbz("0");//含税标志
						
						bwdetail.setSpbm(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
						bwdetail.setZxbm("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//商品行号
						bwdetail.setYhzcbs(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
						bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
						bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
						
						bwdetail.setIsinvoice(detail.getIsinvoice()==null?"":detail.getIsinvoice());//是否开票
						bwdetail.setOldamt("null".equals(String.valueOf(detail.getOldamt()))?"":String.valueOf(detail.getOldamt()));//原始金额
						bwdetail.setGoodsid(detail.getGoodsid()==null?"":detail.getGoodsid());
						oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
						bwDetailList.add(bwdetail);
					}
				}else{
					oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
					}	
		
			}
		}
		
/*		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return;
		}*/
			
		head.setXpjyje(oldamtsum);//小票交易金额
		
		list.setDetails(bwDetailList);
		head.setFp_kjmxs(list);
		rntData.setSalehead(head);
		
		result.setRequestid(invque.getIqseqno());
		result.setRntcode("0000");
		result.setRntmsg("");
		result.setRntdata(rntData);
		
		String dataRequest = XMLConverter.objectToXml(result, "utf-8") ;	
		System.out.println(dataRequest);
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}else{
			rtn.setMessage(dataRequest);
		}
		//hangXinBeanToXml(dataRequest,fwdm,rtn);
		
	}
	
	/**
	 * 小票计算后装换XML
	 * 装换为航信接口对象 
	 * 银座
	 * **/
	public String generateInvoiceSaleBean(ResponseBillInfo res,Taxinfo taxinfo,String fpqqlsh,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对航信数据对象定义
		 * head 开票头
		 * bwDetailList 开票明细集合
		 * **/
		YuanDeInvoiceSaleHeadBean head = new YuanDeInvoiceSaleHeadBean();
		YuanDeInvoiceSaleDetailList list = new YuanDeInvoiceSaleDetailList();
		List<YuanDeInvoiceSaleDetailBean> bwDetailList = new ArrayList<YuanDeInvoiceSaleDetailBean>();
/*		YuanDeResultBean result = new YuanDeResultBean();
		YuanDeRntDataBean rntData = new YuanDeRntDataBean();
		result.setRntcode("0001");*/
		/**
		 * 计算后小票数据初始化
		 * billinfo 小票头
		 * detailList 小票数据集合
		 * **/
		if(res ==null){
			rtn.setCode(-1);
			rtn.setMessage("小票头数据 没有初始化");
			return null;
		}
		List<InvoiceSaleDetail>  detailList = res.getInvoiceSaleDetail();
		
		 if(detailList ==null){
				rtn.setCode(-1);
				rtn.setMessage("小票明细数据 没有初始化");
				return null;
			}
		 
		if(res.getSerialid()==null||"".equals(res.getSerialid())){
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return null;
		}else{
			head.setSerialid(res.getSerialid());//发票流水号
			 
		}

		head.setSheettype("1");
		
		head.setFpqqlsh(fpqqlsh);
		
		head.setXhfkhyh_skfyhzh(res.getTaxbank()==null?"":res.getTaxbank());//销货方银行及账号
		
		if(res.getTaxadd()==null||"".equals(res.getTaxadd())){
			rtn.setCode(-1);
			rtn.setMessage("销货方地址电话不能为空");
			return null;
		}else{
			head.setXhfdz_xhfdh(res.getTaxadd());//销货方地址电话
		}
		
/*		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		}else{
			if("026".equals(invque.getIqfplxdm())){
				head.setFpzl_dm("51");//发票种类编码
			}else
			if("025".equals(invque.getIqfplxdm())){
				head.setFpzl_dm("41");//发票种类编码
			}else
			if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
				head.setFpzl_dm(invque.getIqfplxdm());//发票种类编码
			}else{
				rtn.setCode(-1);
				rtn.setMessage("发票种类编码不正确");
				return;
			}
		}*/
		
		
/*		if(invque.getIqtype()==1){
			
			if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			}else{
				head.setYfp_dm(invque.getIqyfpdm());//原发票代码
			}
			
			if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			}else{
				head.setYfp_hm(invque.getIqyfphm());//原发票号码
			}

		
		}else{
			head.setYfp_dm("");//原发票代码
			head.setYfp_hm("");//原发票号码
		}*/
		//如果是家电小票，将商品编码放入备注
		if("3".equals(res.getShopid().substring(0, 1))){
			String bz = "";
			for(InvoiceSaleDetail detail:detailList){
				if(detail.getTaxitemid()==null) detail.setTaxitemid("");
				//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
				if (!"".equals(detail.getTaxitemid())) {	
				//退货商品原始金额为0的过滤掉
					if(detail.getOldamt()>0){
						bz += detail.getGoodsname()+";";
					}
				}
			}
			head.setBz(res.getRemark()==null?bz:bz+res.getRemark());//备注
		}else{
			head.setBz(res.getRemark()==null?"":res.getRemark());//备注
		}
		
		
		

/*		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		}else{
			head.setKpy(invque.getIqadmin());//开票人
		}
		head.setSky("");//收款人
		head.setFhr("");//审核人
		head.setXhqd("");

		
		if(invque.getIqtype()==0||invque.getIqtype()==1||invque.getIqtype()==2){
			head.setKplx(invque.getIqtype());//开票类型
		}else{
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}*/
		
		head.setJshj(res.getInvoiceamount());//价税合计
		
		head.setHjje(MathCal.sub(res.getInvoiceamount(),res.getTotaltaxfee(),2));//合计金额
		head.setHjse(res.getTotaltaxfee());//合计税额
		
		head.setBmb_bbh(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh());//商品编码版本号
		
/*		if(invque.getIqchannel()!=null&&!"".equals(invque.getIqchannel())){
			if("desk".equals(invque.getIqchannel())){
				head.setYwlx("1");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else
			if("ws".equals(invque.getIqchannel())){
				head.setYwlx("2");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else
			if("mp".equals(invque.getIqchannel())){
				head.setYwlx("3");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else{
				rtn.setCode(-1);
				rtn.setMessage("业务类型不正确");
				return;
			}
			
		}else{
			rtn.setCode(-1);
			rtn.setMessage("业务类型不正确");
			return;
		}
		*/
		head.setShopid(res.getShopid());//门店ID
		head.setXpjysj(res.getTradedate());//小票交易时间
		//head.setEmail(invque.getIqemail()==null?"":invque.getIqemail());//购买方邮箱
		head.setMemo(res.getPaynote());//备注(支付方式)
		//head.setUserid(invque.getIqperson());//登陆人员名称
		
		
		
		//开票明细转换
		/**
		 * 从商品明细中计算小票交易金额
		 * */
		double oldamtsum = 0.0;

		//对储值卡开票的商品做合并商品处理
		if("0".equals(res.getSheettype())){
			
			//按商品编码合并数据
			List<InvoiceSaleDetail> cookList = new ArrayList<InvoiceSaleDetail>();
			if(res.getSyjid()==null) res.setSyjid("");
			//if("A".equals(res.getSyjid())){//如果收银机号为A 则为位未开商品开票，需要对明细做合并
				for (InvoiceSaleDetail detail : detailList) {
					boolean hasJoin = false;
					for (InvoiceSaleDetail cookItem : cookList) {
						//对商品编码一致，则进行合计
						if(detail.getGoodsid().equals(cookItem.getGoodsid())){
							hasJoin = true;
							//累加金额和数量，重算单价
							double qty = MathCal.add(cookItem.getQty() , detail.getQty(),8);
							double amount = MathCal.add(cookItem.getAmount(), detail.getAmount(), 2);
							double taxFee = MathCal.add(cookItem.getTaxfee(), detail.getTaxfee(), 2);
							double oldAmt = MathCal.add(cookItem.getOldamt(), detail.getOldamt(), 2);
							
							cookItem.setQty(qty);
							cookItem.setAmount(amount);
							cookItem.setTaxfee(taxFee);
							cookItem.setOldamt(oldAmt);
						}
					}
					
					if(!hasJoin) {
						cookList.add(detail);
					}
					
				}
			//}else{
			//	cookList = detailList;
			//}
			
			for(InvoiceSaleDetail detail:cookList){
				
				if(detail.getTaxitemid()==null) detail.setTaxitemid("");
				//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
				if (!"".equals(detail.getTaxitemid())) {	
				//退货商品原始金额为0的过滤掉
					if(detail.getOldamt()>0){
						
							YuanDeInvoiceSaleDetailBean bwdetail = new YuanDeInvoiceSaleDetailBean();

									
								bwdetail.setSpsl(String.valueOf(detail.getQty()));//商品数量
								bwdetail.setSpdj(String.valueOf(MathCal.add((detail.getAmount()/detail.getQty()),0,8)));//detail.getPrice();单价

								bwdetail.setSpje(String.valueOf(detail.getAmount()));//金额
								bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
								bwdetail.setOldamt(String.valueOf(detail.getOldamt()));//原始金额
								
								bwdetail.setSpmc(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
								bwdetail.setSm("");//商品所属类别
								bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate()))?"":String.valueOf(detail.getTaxrate()));//税率
								//bwdetail.setGgxh("");//规格型号
								bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
								bwdetail.setJldw(detail.getUnit()==null?"":detail.getUnit());//计量单位
								
		
								
								
								bwdetail.setFphxz("0");//发票行性质
								//bwdetail.setFphxz("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//发票行性质
								bwdetail.setHsjbz("0");//含税标志
								
								bwdetail.setSpbm(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
								bwdetail.setZxbm("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//商品行号
								bwdetail.setYhzcbs(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
								bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
								bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
								
								bwdetail.setIsinvoice(detail.getIsinvoice()==null?"":detail.getIsinvoice());//是否开票
								
								bwdetail.setGoodsid(detail.getGoodsid()==null?"":detail.getGoodsid());
								oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
								bwDetailList.add(bwdetail);
						
					}
				}else{
					oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
					}	
		
			}
		}else{
				/**
				 * 标记是否为开票商品，不是则过滤掉
				 * */
			//String flag = "N";
			for(InvoiceSaleDetail detail:detailList){
				if(detail.getTaxitemid()==null) detail.setTaxitemid("");
				//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
				if (!"".equals(detail.getTaxitemid())) {	
				//退货商品原始金额为0的过滤掉
					if(detail.getOldamt()>0){
						YuanDeInvoiceSaleDetailBean bwdetail = new YuanDeInvoiceSaleDetailBean();
						bwdetail.setSpmc(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
						bwdetail.setSm("");//商品所属类别
						bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate()))?"":String.valueOf(detail.getTaxrate()));//税率
						//bwdetail.setGgxh("");//规格型号
						bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
						bwdetail.setJldw(detail.getUnit()==null?"":detail.getUnit());//计量单位
						
						bwdetail.setSpsl("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()));//商品数量
						bwdetail.setSpdj("null".equals(String.valueOf(detail.getPrice()))?"":String.valueOf(detail.getPrice()));//detail.getPrice();单价
						
						bwdetail.setSpje("null".equals(String.valueOf(detail.getAmount()))?"":String.valueOf(detail.getAmount()));//金额
						bwdetail.setFphxz("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//发票行性质
						bwdetail.setHsjbz("0");//含税标志
						bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee()))?"":String.valueOf(detail.getTaxfee()));//税额
						bwdetail.setSpbm(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
						bwdetail.setZxbm("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//商品行号
						
						bwdetail.setYhzcbs(detail.getPrice()==null?"":detail.getTaxpre());//是否使用优惠政策
						bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
						bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
						
						bwdetail.setIsinvoice(detail.getIsinvoice()==null?"":detail.getIsinvoice());//是否开票
						bwdetail.setOldamt("null".equals(String.valueOf(detail.getOldamt()))?"":String.valueOf(detail.getOldamt()));//原始金额
						bwdetail.setGoodsid(detail.getGoodsid()==null?"":detail.getGoodsid());
						oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
						bwDetailList.add(bwdetail);
					}
				}else{
					oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
				}	
				
				
			}
		}
		
/*		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return null;
		}*/
		
		head.setXpjyje(oldamtsum);//小票交易金额
		
		list.setDetails(bwDetailList);
		head.setFp_kjmxs(list);
		head.setSheettype("1");//单据类型
/*		rntData.setSalehead(head);
		
		result.setRequestid(res.getIqseqno());
		result.setRntcode("0000");
		result.setRntmsg("");
		result.setRntdata(rntData);*/
		
		String dataRequest = XMLConverter.objectToXml(head, "utf-8") ;	
		 
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return null;
		}else{
			rtn.setMessage(dataRequest);
		}
		//hangXinBeanToXml(dataRequest,fwdm,rtn);
		return res.getSerialid();
	}
	
	/**
	 * 开具发票
	 * 对接山东航信电子发票接口
	 * 标版
	 * **/
	public void generateHangXinDzBean(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,String organ,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对航信数据对象定义
		 * head 开票头
		 * bwDetailList 开票明细集合
		 * **/
		YuanDeInvoiceSaleDzHeadBean head = new YuanDeInvoiceSaleDzHeadBean();
		List<YuanDeInvoiceSaleDzDetailBean> bwDetailList = new ArrayList<YuanDeInvoiceSaleDzDetailBean>();

		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * detailList 小票数据集合
		 * **/
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}
		//Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return;
		}
		//List<InvoiceSaleDetail>  detailList= (ArrayList)map.get("detailList");
		if(detailList ==null){
			rtn.setCode(-1);
			rtn.setMessage("小票明细数据 detailList 没有初始化");
			return;
		}
		// List<InvoiceSaleDetail> detailList = saleshead.getInvoiceSaleDetail();
		
 
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		}else{
			head.setNumber(invque.getIqseqno());//发票流水号
			head.setBusno(invque.getIqseqno());//订单编号
		}
		
		if(organ==null) organ="";
		if("1".equals(organ)){
			head.setOrgan(invque.getIqshop()==null?"":invque.getIqshop());
		}else{
			head.setOrgan("");
		}
	//	head.setOrgan("A005");
		
		
		if(invque.getIqgmfname()==null||"".equals(invque.getIqgmfname())){
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		}else{
			head.setClientname(invque.getIqgmfname());//购方客户名称
		}
		
		
		head.setClienttaxcode(invque.getIqgmftax()==null?"":invque.getIqgmftax());//购方单位代码
		head.setClientbankaccount(invque.getIqgmfbank()==null?"":invque.getIqgmfbank());//购方开户行及账号
		head.setClientaddressphone(invque.getIqgmfadd()==null?"":invque.getIqgmfadd());//购方地址及电话
		
		head.setClientphone(invque.getIqtel()==null?"":invque.getIqtel());
		head.setClientmail(invque.getIqemail()==null?"":invque.getIqemail());
		
		if(invque.getIqtype()==0||invque.getIqtype()==1){
			if(invque.getIqtype()==0)
				head.setBilltype("1");//开票类型
			else
			if(invque.getIqtype()==1)
				head.setBilltype("0");//开票类型
			
		}else{
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		}else{
			if("026".equals(invque.getIqfplxdm())){
				head.setInfokind("51");//发票种类编码
			}else
			if("025".equals(invque.getIqfplxdm())){
				head.setInfokind("41");//发票种类编码
			}else
			if("004".equals(invque.getIqfplxdm())){
				head.setInfokind("0");//发票种类编码
			}else
			if("007".equals(invque.getIqfplxdm())){
				head.setInfokind("2");//发票种类编码
			}else{
				rtn.setCode(-1);
				rtn.setMessage("发票种类编码不正确");
				return;
			}
		}
		
		head.setNotes(invque.getIqmemo()==null?"":invque.getIqmemo());//备注
		
		if(invque.getIqtype()==1){
			
			if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			}else{
				head.setInvoicecode(invque.getIqyfpdm());//原发票代码
			}
			
			if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			}else{
				head.setInvoiceno(invque.getIqyfphm());//原发票号码
			}

		
		}else{
			head.setInvoicecode("");//原发票代码
			head.setInvoiceno("");//原发票号码
		}
 
		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		}else{
			head.setInvoicer(invque.getIqadmin());//开票人
		}
		head.setCashier(invque.getIqpayee()==null?"":invque.getIqpayee());//收款人
		head.setChecker(invque.getIqchecker()==null?"":invque.getIqchecker());//审核人
		
		head.setAllmoney("1");
		
		if(invque.getIqtype()!=1){
			head.setSummoney(String.valueOf(MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2)));//价税合计
		}else{
			head.setSummoney(String.valueOf(MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2)*-1));//价税合计
		}
		
 
		//开票明细转换
		/**
		 * 从商品明细中计算小票交易金额
		 * */

		//对储值卡开票的商品做合并商品处理
		if("0".equals(invque.getIqsource())){
			
			//按商品编码合并数据
			List<InvoiceSaleDetail> cookList = new ArrayList<InvoiceSaleDetail>();
			for (InvoiceSaleDetail detail : detailList) {
				boolean hasJoin = false;
				for (InvoiceSaleDetail cookItem : cookList) {
					//对商品编码一致，则进行合计
					if(detail.getGoodsid().equals(cookItem.getGoodsid())){
						hasJoin = true;
						//累加金额和数量，重算单价
						double qty = MathCal.add(cookItem.getQty() , detail.getQty(),8);
						double amount = MathCal.add(cookItem.getAmount(), detail.getAmount(), 2);
						double taxFee = MathCal.add(cookItem.getTaxfee(), detail.getTaxfee(), 2);
						double oldAmt = MathCal.add(cookItem.getOldamt(), detail.getOldamt(), 2);
						
						cookItem.setQty(qty);
						cookItem.setAmount(amount);
						cookItem.setTaxfee(taxFee);
						cookItem.setOldamt(oldAmt);
					}
				}
				
				if(!hasJoin) {
					cookList.add(detail);
				}
				
			}
			
			for(InvoiceSaleDetail detail:cookList){
				if(invque.getIqtype()!=1){
					if(detail.getTaxitemid()==null) detail.setTaxitemid("");
					//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
					if (!"".equals(detail.getTaxitemid())) {	
					//退货商品原始金额为0的过滤掉
						if(detail.getOldamt()>0){
							
							YuanDeInvoiceSaleDzDetailBean bwdetail = new YuanDeInvoiceSaleDzDetailBean();
							
							bwdetail.setGoodsname(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
							bwdetail.setStandard(detail.getSpec()==null?"":detail.getSpec());//规格型号
							bwdetail.setUnit(detail.getUnit()==null?"":detail.getUnit());//计量单位
							if(detail.getQty()>0){
								bwdetail.setNum("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()));//商品数量
								bwdetail.setPrice(String.valueOf(MathCal.div(detail.getAmount()+detail.getTaxfee(), detail.getQty(), 6)));//单价	
							}
							bwdetail.setAmount(String.valueOf(MathCal.add(detail.getAmount(), detail.getTaxfee(), 2)));//含税金额
							bwdetail.setTaxrate(String.valueOf(detail.getTaxrate()));//税率
							bwdetail.setTaxamount("null".equals(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)))?"":String.valueOf(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)) ));
							bwdetail.setAigo("");
							bwdetail.setAigotax("");
							bwdetail.setGoodsgroup("");//商品所属类别
							bwdetail.setGoodsnover(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh()); 
							bwdetail.setGoodstaxno(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
							
							bwdetail.setTaxpre(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
							bwdetail.setZerotax(detail.getZerotax()==null?"":detail.getZerotax());
							bwdetail.setTaxprecon(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
							bwdetail.setGoodstaxname(detail.getTaxitemname());
							bwDetailList.add(bwdetail);
							
						}
					} 
				}else{
					YuanDeInvoiceSaleDzDetailBean bwdetail = new YuanDeInvoiceSaleDzDetailBean();
					
					bwdetail.setGoodsname(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
					bwdetail.setStandard(detail.getSpec()==null?"":detail.getSpec());//规格型号
					bwdetail.setUnit(detail.getUnit()==null?"":detail.getUnit());//计量单位
					if(detail.getQty()>0){
						bwdetail.setNum("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()*-1));//商品数量
						bwdetail.setPrice(String.valueOf(MathCal.div(detail.getAmount()+detail.getTaxfee(), detail.getQty(), 6)));//单价	
					}
					bwdetail.setAmount(String.valueOf(MathCal.add(detail.getAmount(), detail.getTaxfee(), 2)*-1));//含税金额
					bwdetail.setTaxrate(String.valueOf(detail.getTaxrate()));//税率
					bwdetail.setTaxamount("null".equals(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)))?"":String.valueOf(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)*-1) ));
					bwdetail.setAigo("");
					bwdetail.setAigotax("");
					bwdetail.setGoodsgroup("");//商品所属类别
					bwdetail.setGoodsnover(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh()); 
					bwdetail.setGoodstaxno(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
					
					bwdetail.setTaxpre(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
					bwdetail.setZerotax(detail.getZerotax()==null?"":detail.getZerotax());
					bwdetail.setTaxprecon(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
					bwdetail.setGoodstaxname(detail.getTaxitemname());
					bwDetailList.add(bwdetail);
				}
			}
		}else{
			for(InvoiceSaleDetail detail:detailList){
				if(invque.getIqtype()!=1){
					if(detail.getTaxitemid()==null) detail.setTaxitemid("");
					//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
					if (!"".equals(detail.getTaxitemid())) {	
					//退货商品原始金额为0的过滤掉
						if(detail.getOldamt()>0){
							YuanDeInvoiceSaleDzDetailBean bwdetail = new YuanDeInvoiceSaleDzDetailBean();
							
							bwdetail.setGoodsname(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
							bwdetail.setStandard(detail.getSpec()==null?"":detail.getSpec());//规格型号
							bwdetail.setUnit(detail.getUnit()==null?"":detail.getUnit());//计量单位
							if(detail.getQty()>0){
								bwdetail.setNum("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()));//商品数量
								bwdetail.setPrice(String.valueOf(MathCal.div(detail.getAmount()+detail.getTaxfee(), detail.getQty(), 6)));//单价	
							}
							bwdetail.setAmount(String.valueOf(MathCal.add(detail.getAmount(), detail.getTaxfee(), 2)));//含税金额
							bwdetail.setTaxrate(String.valueOf(detail.getTaxrate()));//税率
							bwdetail.setTaxamount("null".equals(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)))?"":String.valueOf(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)) ));
							bwdetail.setAigo("");
							bwdetail.setAigotax("");
							bwdetail.setGoodsgroup("");//商品所属类别
							bwdetail.setGoodsnover(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh()); 
							bwdetail.setGoodstaxno(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
							
							bwdetail.setTaxpre(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
							bwdetail.setZerotax(detail.getZerotax()==null?"":detail.getZerotax());
							bwdetail.setTaxprecon(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
							bwdetail.setGoodstaxname(detail.getTaxitemname());
							bwDetailList.add(bwdetail);
						}
					} 	
			
				}else{
					if(detail.getTaxitemid()==null) detail.setTaxitemid("");
					//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
					
						YuanDeInvoiceSaleDzDetailBean bwdetail = new YuanDeInvoiceSaleDzDetailBean();
						
						bwdetail.setGoodsname(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
						bwdetail.setStandard(detail.getSpec()==null?"":detail.getSpec());//规格型号
						bwdetail.setUnit(detail.getUnit()==null?"":detail.getUnit());//计量单位
						if(detail.getQty()>0){
							bwdetail.setNum("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()*-1));//商品数量
							bwdetail.setPrice(String.valueOf(MathCal.div(detail.getAmount()+detail.getTaxfee(), detail.getQty(), 6)));//单价	
						}
						bwdetail.setAmount(String.valueOf(MathCal.add(detail.getAmount(), detail.getTaxfee(), 2)*-1));//含税金额
						bwdetail.setTaxrate(String.valueOf(detail.getTaxrate()));//税率
						bwdetail.setTaxamount("null".equals(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)))?"":String.valueOf(String.valueOf(MathCal.add(0, detail.getTaxfee(), 2)*-1) ));
						bwdetail.setAigo("");
						bwdetail.setAigotax("");
						bwdetail.setGoodsgroup("");//商品所属类别
						bwdetail.setGoodsnover(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh()); 
						bwdetail.setGoodstaxno(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
						
						bwdetail.setTaxpre(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
						bwdetail.setZerotax(detail.getZerotax()==null?"":detail.getZerotax());
						bwdetail.setTaxprecon(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
						bwdetail.setGoodstaxname(detail.getTaxitemname());
						bwDetailList.add(bwdetail);
		
				}
			}
		}
		
		head.setDetails(bwDetailList);

		String dataRequest = XMLConverter.objectToXml(head, "utf-8") ;	
		
		System.out.println(dataRequest);
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}else{
			rtn.setMessage(dataRequest);
		}
		//hangXinBeanToXml(dataRequest,fwdm,rtn);
		
	}
	
	
	/**
	 * 小票计算后装换XML
	 * 装换为航信接口对象 
	 * 标版
	 * **/
	public String generateInvoiceSaleBbBean(ResponseBillInfo res,Taxinfo taxinfo,String fpqqlsh,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对航信数据对象定义
		 * head 开票头
		 * bwDetailList 开票明细集合
		 * **/
		YuanDeInvoiceSaleHeadBean head = new YuanDeInvoiceSaleHeadBean();
		YuanDeInvoiceSaleDetailList list = new YuanDeInvoiceSaleDetailList();
		List<YuanDeInvoiceSaleDetailBean> bwDetailList = new ArrayList<YuanDeInvoiceSaleDetailBean>();
/*		YuanDeResultBean result = new YuanDeResultBean();
		YuanDeRntDataBean rntData = new YuanDeRntDataBean();
		result.setRntcode("0001");*/
		/**
		 * 计算后小票数据初始化
		 * billinfo 小票头
		 * detailList 小票数据集合
		 * **/
		if(res ==null){
			rtn.setCode(-1);
			rtn.setMessage("小票头数据 没有初始化");
			return null;
		}
		List<InvoiceSaleDetail>  detailList = res.getInvoiceSaleDetail();
		
		 if(detailList ==null){
				rtn.setCode(-1);
				rtn.setMessage("小票明细数据 没有初始化");
				return null;
			}
		 
		if(res.getSerialid()==null||"".equals(res.getSerialid())){
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return null;
		}else{
			head.setSerialid(res.getSerialid());//发票流水号
			 
		}

		head.setSheettype("1");
		
		head.setFpqqlsh(fpqqlsh);
		
		head.setXhfkhyh_skfyhzh(res.getTaxbank()==null?"":res.getTaxbank());//销货方银行及账号
		
		if(res.getTaxadd()==null||"".equals(res.getTaxadd())){
			rtn.setCode(-1);
			rtn.setMessage("销货方地址电话不能为空");
			return null;
		}else{
			head.setXhfdz_xhfdh(res.getTaxadd());//销货方地址电话
		}
		
/*		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		}else{
			if("026".equals(invque.getIqfplxdm())){
				head.setFpzl_dm("51");//发票种类编码
			}else
			if("025".equals(invque.getIqfplxdm())){
				head.setFpzl_dm("41");//发票种类编码
			}else
			if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
				head.setFpzl_dm(invque.getIqfplxdm());//发票种类编码
			}else{
				rtn.setCode(-1);
				rtn.setMessage("发票种类编码不正确");
				return;
			}
		}*/
		
		
/*		if(invque.getIqtype()==1){
			
			if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			}else{
				head.setYfp_dm(invque.getIqyfpdm());//原发票代码
			}
			
			if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			}else{
				head.setYfp_hm(invque.getIqyfphm());//原发票号码
			}

		
		}else{
			head.setYfp_dm("");//原发票代码
			head.setYfp_hm("");//原发票号码
		}*/
		
		head.setBz(res.getRemark()==null?"":res.getRemark());//备注
		

/*		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		}else{
			head.setKpy(invque.getIqadmin());//开票人
		}
		head.setSky("");//收款人
		head.setFhr("");//审核人
		head.setXhqd("");

		
		if(invque.getIqtype()==0||invque.getIqtype()==1||invque.getIqtype()==2){
			head.setKplx(invque.getIqtype());//开票类型
		}else{
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}*/
		
		head.setJshj(res.getInvoiceamount());//价税合计
		
		head.setHjje(MathCal.sub(res.getInvoiceamount(),res.getTotaltaxfee(),2));//合计金额
		head.setHjse(res.getTotaltaxfee());//合计税额
		
		head.setBmb_bbh(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh());//商品编码版本号
		
/*		if(invque.getIqchannel()!=null&&!"".equals(invque.getIqchannel())){
			if("desk".equals(invque.getIqchannel())){
				head.setYwlx("1");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else
			if("ws".equals(invque.getIqchannel())){
				head.setYwlx("2");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else
			if("mp".equals(invque.getIqchannel())){
				head.setYwlx("3");//业务类型 渠道，desk：服务台，ws：微信，mp：网站
			}else{
				rtn.setCode(-1);
				rtn.setMessage("业务类型不正确");
				return;
			}
			
		}else{
			rtn.setCode(-1);
			rtn.setMessage("业务类型不正确");
			return;
		}
		*/
		head.setShopid(res.getShopid());//门店ID
		head.setXpjysj(res.getTradedate());//小票交易时间
		//head.setEmail(invque.getIqemail()==null?"":invque.getIqemail());//购买方邮箱
		head.setMemo(res.getPaynote());//备注(支付方式)
		//head.setUserid(invque.getIqperson());//登陆人员名称
		
		
		
		//开票明细转换
		/**
		 * 从商品明细中计算小票交易金额
		 * */
		double oldamtsum = 0.0;

		//对储值卡开票的商品做合并商品处理
		if("0".equals(res.getSheettype())){
			
			//按商品编码合并数据
			List<InvoiceSaleDetail> cookList = new ArrayList<InvoiceSaleDetail>();
			if(res.getSyjid()==null) res.setSyjid("");
			//if("A".equals(res.getSyjid())){//如果收银机号为A 则为位未开商品开票，需要对明细做合并
				for (InvoiceSaleDetail detail : detailList) {
					boolean hasJoin = false;
					for (InvoiceSaleDetail cookItem : cookList) {
						//对商品编码一致，则进行合计
						if(detail.getGoodsid().equals(cookItem.getGoodsid())){
							hasJoin = true;
							//累加金额和数量，重算单价
							double qty = MathCal.add(cookItem.getQty() , detail.getQty(),8);
							double amount = MathCal.add(cookItem.getAmount(), detail.getAmount(), 2);
							double taxFee = MathCal.add(cookItem.getTaxfee(), detail.getTaxfee(), 2);
							double oldAmt = MathCal.add(cookItem.getOldamt(), detail.getOldamt(), 2);
							
							cookItem.setQty(qty);
							cookItem.setAmount(amount);
							cookItem.setTaxfee(taxFee);
							cookItem.setOldamt(oldAmt);
						}
					}
					
					if(!hasJoin) {
						cookList.add(detail);
					}
					
				}
			//}else{
			//	cookList = detailList;
			//}
			
			for(InvoiceSaleDetail detail:cookList){
				
				if(detail.getTaxitemid()==null) detail.setTaxitemid("");
				//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
				if (!"".equals(detail.getTaxitemid())) {	
				//退货商品原始金额为0的过滤掉
					if(detail.getOldamt()>0){
						
							YuanDeInvoiceSaleDetailBean bwdetail = new YuanDeInvoiceSaleDetailBean();

									
								bwdetail.setSpsl(String.valueOf(detail.getQty()));//商品数量
								bwdetail.setSpdj(String.valueOf(MathCal.add((detail.getAmount()/detail.getQty()),0,6)));//detail.getPrice();单价

								bwdetail.setSpje(String.valueOf(detail.getAmount()));//金额
								bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
								bwdetail.setOldamt(String.valueOf(detail.getOldamt()));//原始金额
								
								bwdetail.setSpmc(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
								bwdetail.setSm("");//商品所属类别
								bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate()))?"":String.valueOf(detail.getTaxrate()));//税率
								//bwdetail.setGgxh("");//规格型号
								bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
								bwdetail.setJldw(detail.getUnit()==null?"":detail.getUnit());//计量单位
								
		
								
								
								bwdetail.setFphxz("0");//发票行性质
								//bwdetail.setFphxz("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//发票行性质
								bwdetail.setHsjbz("0");//含税标志
								
								bwdetail.setSpbm(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
								bwdetail.setZxbm("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//商品行号
								bwdetail.setYhzcbs(detail.getTaxpre()==null?"":detail.getTaxpre());//是否使用优惠政策
								bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
								bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
								
								bwdetail.setIsinvoice(detail.getIsinvoice()==null?"":detail.getIsinvoice());//是否开票
								
								bwdetail.setGoodsid(detail.getGoodsid()==null?"":detail.getGoodsid());
								oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
								bwDetailList.add(bwdetail);
						
					}
				}else{
					oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
					}	
		
			}
		}else{
				/**
				 * 标记是否为开票商品，不是则过滤掉
				 * */
			//String flag = "N";
			for(InvoiceSaleDetail detail:detailList){
				if(detail.getTaxitemid()==null) detail.setTaxitemid("");
				//判断税目是否为空,代开票商品没有维护税目编码，过滤掉
				if (!"".equals(detail.getTaxitemid())) {	
				//退货商品原始金额为0的过滤掉
					if(detail.getOldamt()>0){
						YuanDeInvoiceSaleDetailBean bwdetail = new YuanDeInvoiceSaleDetailBean();
						bwdetail.setSpmc(detail.getGoodsname()==null?"":detail.getGoodsname());//商品名称
						bwdetail.setSm("");//商品所属类别
						bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate()))?"":String.valueOf(detail.getTaxrate()));//税率
						//bwdetail.setGgxh("");//规格型号
						bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
						bwdetail.setJldw(detail.getUnit()==null?"":detail.getUnit());//计量单位
						
						bwdetail.setSpsl("null".equals(String.valueOf(detail.getQty()))?"":String.valueOf(detail.getQty()));//商品数量
						bwdetail.setSpdj("null".equals(String.valueOf(detail.getPrice()))?"":String.valueOf(detail.getPrice()));//detail.getPrice();单价
						
						bwdetail.setSpje("null".equals(String.valueOf(detail.getAmount()))?"":String.valueOf(detail.getAmount()));//金额
						bwdetail.setFphxz("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//发票行性质
						bwdetail.setHsjbz("0");//含税标志
						bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee()))?"":String.valueOf(detail.getTaxfee()));//税额
						bwdetail.setSpbm(detail.getTaxitemid()==null?"":detail.getTaxitemid());//商品税目
						bwdetail.setZxbm("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//商品行号
						
						bwdetail.setYhzcbs(detail.getPrice()==null?"":detail.getTaxpre());//是否使用优惠政策
						bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
						bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());//增值税特殊管理
						
						bwdetail.setIsinvoice(detail.getIsinvoice()==null?"":detail.getIsinvoice());//是否开票
						bwdetail.setOldamt("null".equals(String.valueOf(detail.getOldamt()))?"":String.valueOf(detail.getOldamt()));//原始金额
						bwdetail.setGoodsid(detail.getGoodsid()==null?"":detail.getGoodsid());
						oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
						bwDetailList.add(bwdetail);
					}
				}else{
					oldamtsum  = MathCal.add(oldamtsum, detail.getOldamt(), 2);
				}	
				
				
			}
		}
		
/*		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return null;
		}*/
		
		head.setXpjyje(oldamtsum);//小票交易金额
		
		list.setDetails(bwDetailList);
		head.setFp_kjmxs(list);
		head.setSheettype("1");//单据类型
/*		rntData.setSalehead(head);
		
		result.setRequestid(res.getIqseqno());
		result.setRntcode("0000");
		result.setRntmsg("");
		result.setRntdata(rntData);*/
		
		String dataRequest = XMLConverter.objectToXml(head, "utf-8") ;	
		
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return null;
		}else{
			rtn.setMessage(dataRequest);
		}
		//hangXinBeanToXml(dataRequest,fwdm,rtn);
		return res.getSerialid();
	}
	
	//开票返回结果转换为BEAN
	public YuanDeRtInvoiceBean rtOpenToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		YuanDeRtInvoiceBean rtinvoicebean =new YuanDeRtInvoiceBean();

		XmlUtil util= new XmlUtil();  
		Map map= util.xmlToMap(xml);
		if(map==null||map.size()==0){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回数据转换对象时错误");
			return rtinvoicebean;
		}
		
		if("0000".equals(map.get("RETURNCODE"))){
			
			try{
			       JAXBContext jc = JAXBContext.newInstance( YuanDeRtInvoiceBean.class);
			       Unmarshaller u = jc.createUnmarshaller();
			       StringBuffer xmlStr = new StringBuffer(xml);
			       rtinvoicebean = (YuanDeRtInvoiceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
			       
					if(rtinvoicebean == null){
						rtn.setCode(-1);
						rtn.setMessage("输入的XML转换对象错误！");
						return rtinvoicebean;
					}
			       
			       if(rtinvoicebean.getFpqqlsh()==null||"".equals(rtinvoicebean.getFpqqlsh())){
						rtn.setCode(-1);
						rtn.setMessage("小票提取码为空！");
						return rtinvoicebean;
					}else{
						if(rtinvoicebean.getFpqqlsh().indexOf("-")!=-1){
							
						String fpqqlsh[] = rtinvoicebean.getFpqqlsh().split("-");
						fpqqlsh[2] = String.valueOf(Integer.parseInt(fpqqlsh[2]));
						rtinvoicebean.setFpqqlsh(fpqqlsh[0]+"-"+fpqqlsh[1]+"-"+fpqqlsh[2]);
						}
					}
					
			       if(rtinvoicebean.getSerialid()==null||"".equals(rtinvoicebean.getSerialid())){
						rtn.setCode(-1);
						rtn.setMessage("发票流水号为空！");
						return rtinvoicebean;
			       }else
			       if(!"Q".equals(rtinvoicebean.getSerialid().substring(0, 1))&&!"S".equals(rtinvoicebean.getSerialid().substring(0, 1))){
			    	   rtn.setCode(-1);
						rtn.setMessage("发票请求唯一流水号格式不正确！");
						return rtinvoicebean;
			       }
			       
					if(rtinvoicebean.getShopid()==null||"".equals(rtinvoicebean.getShopid())){
						rtn.setCode(-1);
						rtn.setMessage("组织机构不能为空！");
						return rtinvoicebean;
					}
					
			       if(rtinvoicebean.getSheettype()==null||"".equals(rtinvoicebean.getSheettype())){
								rtn.setCode(-1);
								rtn.setMessage("单据类型为空！");
								return rtinvoicebean;
							}
			       
			       if("3".equals(rtinvoicebean.getStatus())||"4".equals(rtinvoicebean.getStatus())){
			       
		
				       
						if(rtinvoicebean.getKplx()!=0&&rtinvoicebean.getKplx()!=1&&rtinvoicebean.getKplx()!=2){
							rtn.setCode(-1);
							rtn.setMessage("开票类型数据错误！");
							return rtinvoicebean;
						}
						
						if(rtinvoicebean.getFpzl_dm()==null||"".equals(rtinvoicebean.getFpzl_dm())){
							rtn.setCode(-1);
							rtn.setMessage("发票种类代码为空！");
							return rtinvoicebean;
						}
						
						if("51".equals(rtinvoicebean.getFpzl_dm())){
							rtinvoicebean.setFpzl_dm("026");
						}else
						if("41".equals(rtinvoicebean.getFpzl_dm())){
							rtinvoicebean.setFpzl_dm("025");
						}
						
			
						
						if(rtinvoicebean.getStatus()==null||"".equals(rtinvoicebean.getStatus())){
							rtn.setCode(-1);
							rtn.setMessage("返回状态为空！");
							return rtinvoicebean;
						}else
						if(!"1".equals(rtinvoicebean.getStatus())&&!"2".equals(rtinvoicebean.getStatus())&&!"3".equals(rtinvoicebean.getStatus())&&!"4".equals(rtinvoicebean.getStatus())){
							rtn.setCode(-1);
							rtn.setMessage("返回状态值有问题！");
							return rtinvoicebean;
						}else{
							if("3".equals(rtinvoicebean.getStatus())||"4".equals(rtinvoicebean.getStatus())){
								if(rtinvoicebean.getFp_dm()==null||"".equals(rtinvoicebean.getFp_dm())){
									rtn.setCode(-1);
									rtn.setMessage("发票代码为空！");
									return rtinvoicebean;
								}
								
								if(rtinvoicebean.getFp_hm()==null||"".equals(rtinvoicebean.getFp_hm())){
									rtn.setCode(-1);
									rtn.setMessage("发票号码为空！");
									return rtinvoicebean;
								}
							}
						}
						
						if(rtinvoicebean.getKplx()==1||rtinvoicebean.getKplx()==2){
							
							if(rtinvoicebean.getYfp_dm()==null||"".equals(rtinvoicebean.getYfp_dm())){
								rtn.setCode(-1);
								rtn.setMessage("红冲或作废原发票代码不能为空！");
								return rtinvoicebean;
							}
							
							if(rtinvoicebean.getYfp_hm()==null||"".equals(rtinvoicebean.getYfp_hm())){
								rtn.setCode(-1);
								rtn.setMessage("红冲或作废原发票号码不能为空！");
								return rtinvoicebean;
							}
							
							if(rtinvoicebean.getKplx()==1){
								if(rtinvoicebean.getHjje()>=0){
									rtn.setCode(-1);
									rtn.setMessage("红冲合计金额不能大于等于零！");
									return rtinvoicebean;
								}
								if(rtinvoicebean.getHjse()>0){
									rtn.setCode(-1);
									rtn.setMessage("红冲合计税额不能大于等于零！");
									return rtinvoicebean;
								}
				
							}else
							if(rtinvoicebean.getKplx()==2){
								if(rtinvoicebean.getHjje()<=0){
									rtn.setCode(-1);
									rtn.setMessage("作废合计金额不能小于等于零！");
									return  rtinvoicebean;
								}
								if(rtinvoicebean.getHjse()<0){
									rtn.setCode(-1);
									rtn.setMessage("作废合计税额不能小于零！");
									return rtinvoicebean;
								}
			
							}
	
						}else 
						if(rtinvoicebean.getKplx()==0){
							if(rtinvoicebean.getHjje()<=0){
								rtn.setCode(-1);
								rtn.setMessage("开票合计金额不能小于等于零！");
								return  rtinvoicebean;
							}
							if(rtinvoicebean.getHjse()<0){
								rtn.setCode(-1);
								rtn.setMessage("开票合计税额不能小于零！");
								return rtinvoicebean;
							}
						}
			       }
			}catch(Exception e){
				e.printStackTrace();
			}
			
		 
			
			/*rtinvoicebean.setReturnmessage((String)map.get("RETURNMESSAGE"));
			rtinvoicebean.setFpqqlsh((String)map.get("FPQQLSH"));
			rtinvoicebean.setHjbhsje(Double.valueOf((String)map.get("HJBHSJE")));
			rtinvoicebean.setHjse(Double.valueOf((String)map.get("HJSE")));
			rtinvoicebean.setKprq((String)map.get("KPRQ"));
			rtinvoicebean.setFp_dm((String)map.get("FP_DM"));
			rtinvoicebean.setFp_hm((String)map.get("FP_HM"));
			rtinvoicebean.setXhqdbz((String)map.get("XHQDBZ"));
			rtinvoicebean.setStatus((String)map.get("STATUS"));
			rtinvoicebean.setPdf_url((String)map.get("PDF_URL"));
			System.out.println(rtinvoicebean.getFpqqlsh());
			
			rtinvoicebean.setGhfmc((String)map.get("GHFMC"));
			rtinvoicebean.setGhf_nsrsbh((String)map.get("GHF_NSRSBH"));
			rtinvoicebean.setFkfkhyh_fkfyhzh((String)map.get("FKFKHYH_FKFYHZH"));
			rtinvoicebean.setFkfdz_fkfdh((String)map.get("FKFDZ_FKFDH"));
			rtinvoicebean.setXhfkhyh_skfyhzh((String)map.get("XHFKHYH_SKFYHZH"));
			rtinvoicebean.setXhfdz_xhfdh((String)map.get("XHFDZ_XHFDH>"));
			rtinvoicebean.setFpzl_dm((String)map.get("FPZL_DM"));
			rtinvoicebean.setYfp_dm((String)map.get("YFP_DM"));
			rtinvoicebean.setYfp_hm((String)map.get("YFP_HM"));
			rtinvoicebean.setBz((String)map.get("BZ"));
			rtinvoicebean.setKpy((String)map.get("KPY"));
			rtinvoicebean.setFhr((String)map.get("FHR"));
			rtinvoicebean.setSky((String)map.get("SKY"));
			rtinvoicebean.setKplx((int)map.get("KPLX"));
			rtinvoicebean.setHjje((double)map.get("HJJE"));
			rtinvoicebean.setHjse((double)map.get("HJSE"));
			rtinvoicebean.setJshj(rtinvoicebean.getHjje()+rtinvoicebean.getHjse());
			rtinvoicebean.setBmb_bbh((String)map.get("BMB_BBH"));
			rtinvoicebean.setUserid((String)map.get("USERID"));
			rtinvoicebean.setYwlx((String)map.get("YWLX"));
			rtinvoicebean.setShopid((String)map.get("SHOPID"));
			rtinvoicebean.setXpjysj((String)map.get("XPJYSJ"));
			rtinvoicebean.setEmail((String)map.get("EMAIL"));*/
			
		}else{
			rtn.setCode(-1);
			rtn.setMessage(map.get("RETURNMESSAGE").toString());
			return null;
		}
		return rtinvoicebean;
	}
	
	
	//开票提交单据返回结果转换为BEAN
		public YuanDeRtInvoiceBean rtOpenSubmitToBean(String xml,RtnData rtn){
			if(xml==null||"".equals(xml)){
				rtn.setCode(-1);
				rtn.setMessage("开具发票返回为空");
				return null;
			}
			YuanDeRtInvoiceBean rtinvoicebean =new YuanDeRtInvoiceBean();

			XmlUtil util= new XmlUtil();  
			Map map= util.xmlToMap(xml);
			if(map==null||map.size()==0){
				rtn.setCode(-1);
				rtn.setMessage("开具发票返回数据转换对象时错误");
				return rtinvoicebean;
			}
			 
			if("0000".equals(map.get("RETURNCODE"))){
				 
				rtn.setMessage(map.get("RETURNMESSAGE").toString());
/*				try{
				       JAXBContext jc = JAXBContext.newInstance( YuanDeRtInvoiceBean.class);
				       Unmarshaller u = jc.createUnmarshaller();
				       StringBuffer xmlStr = new StringBuffer(xml);
				       rtinvoicebean = (YuanDeRtInvoiceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
				       if("1".equals(rtinvoicebean.getStatus()){
				    	   rtn.setMessage(map.get("RETURNMESSAGE").toString());
				       }
				       
				       
				}catch(Exception e){
					e.printStackTrace();
				}
				*/
			
				
			}else{
				rtn.setCode(-1);
				rtn.setMessage(map.get("RETURNMESSAGE").toString());
				return rtinvoicebean;
			}
			return rtinvoicebean;
		}
	
	
	//将开票返回结果BEAN转换为可以向数据库写入的bean
	public ResponseInvoice rtBeanToBean(YuanDeRtInvoiceBean rtinvoicebean,RtnData rtn){
		if(rtinvoicebean==null||"".equals(rtinvoicebean)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		ResponseInvoice res = new ResponseInvoice();

				 //开票人
				 if(rtinvoicebean.getKpy()==null||"".equals(rtinvoicebean.getKpy())){
					 rtn.setCode(-1);
						rtn.setMessage("开票人不能为空！");
						return null;
				 }else{
					 res.setAdmin(rtinvoicebean.getKpy());
				 }
				 res.setUserid(rtinvoicebean.getUserid());
				 
				 res.setXsfDzdh(rtinvoicebean.getXhfdz_xhfdh());
				 res.setXsfYhzh(rtinvoicebean.getXhfkhyh_skfyhzh());
				 
				 res.setGmfDzdh(rtinvoicebean.getFkfdz_fkfdh());
				 res.setGmfMc(rtinvoicebean.getGhfmc());
				 res.setGmfNsrsbh(rtinvoicebean.getGhf_nsrsbh());
				 res.setGmfYhzh(rtinvoicebean.getFkfkhyh_fkfyhzh());
				 
				 res.setRecvEmail(rtinvoicebean.getEmail());
				 
				 res.setYfpdm(rtinvoicebean.getFp_dm());
				 res.setYfphm(rtinvoicebean.getFp_hm());
				 
				 return res;		 
   } 

	
	/**
	 * 发票查询
	 * 装换为航信接口对象 
	 * **/
	public void findInvoiceBean(Invque invque,InvoiceSaleHead saleshead,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对航信数据对象定义
		 * head 开票头
		 * **/
		YuanDeInvoiceSaleHeadBean head = new YuanDeInvoiceSaleHeadBean();
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * **/
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}
		
		if(invque.getIqseqno()==null) invque.setIqseqno("");
		if(invque.getRtfpdm()==null) invque.setRtfpdm("");
		if(invque.getRtfphm()==null) invque.setRtfphm("");
		
		if(saleshead.getShopid()==null||"".equals(saleshead.getShopid())){
			rtn.setCode(-1);
			rtn.setMessage("门店不能为空");
			return;
		}else
		if(saleshead.getTradedate()==null||"".equals(saleshead.getTradedate())){
			rtn.setCode(-1);
			rtn.setMessage("交易日期不能为空");
			return;
		}else
		if(saleshead.getSyjid()==null||"".equals(saleshead.getSyjid())){
			rtn.setCode(-1);
			rtn.setMessage("收银机号不能为空");
			return;
		}else
		if(saleshead.getBillno()==null||"".equals(saleshead.getBillno())){
			rtn.setCode(-1);
			rtn.setMessage("小票号不能为空");
			return;
		}else{
			invque.setIqseqno(saleshead.getShopid()+"-"+saleshead.getSyjid()+"-"+saleshead.getBillno());//交易流水号
		}
		
		if("".equals(invque.getIqseqno())){
			if("".equals(invque.getRtfpdm())||"".equals(invque.getRtfphm())){
				rtn.setCode(-1);
				rtn.setMessage("发票流水号和发票代码不能同时为空");
				return;
			}
		}
		


		String dataRequest ="<REQUEST >"+
							"<FPQQLSH>"+(invque.getIqseqno())+"</FPQQLSH>"+
							"<FP_DM>"+(invque.getRtfpdm())+"</FP_DM>"+
							"<FP_HM>"+(invque.getRtfphm())+"</FP_HM>"+
							"</REQUEST >";

		rtn.setMessage(dataRequest);
		
	}
 
	/**
	 * 推送接口
	 * 全量开票后，针对开票抬头是个人的重新推送到邮箱
	 * **/
	public void tuiSongInvoiceBean(Invque invque,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对航信数据对象定义
		 * head 开票头
		 * **/
		YuanDeInvoiceSaleHeadBean head = new YuanDeInvoiceSaleHeadBean();
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * **/
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}
 
		if(StringUtils.isEmpty(invque.getIqemail())){
			rtn.setCode(-1);
			rtn.setMessage("需要推送的邮箱不能为空");
			return;
		}
		if(StringUtils.isEmpty(invque.getRtfpdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票代码不能为空");
			return;
		}
		if(StringUtils.isEmpty(invque.getRtfphm())){
			rtn.setCode(-1);
			rtn.setMessage("发票号码不能为空");
			return;
		}


		String dataRequest ="<REQUEST_FPTS class=\"REQUEST_FPTS\">"+
							"<FP_DM>"+(invque.getRtfpdm())+"</FP_DM>"+
							"<FP_HM>"+(invque.getRtfphm())+"</FP_HM>"+
							"<EMAIL>"+(invque.getIqemail())+"</EMAIL>"+
							"</REQUEST_FPTS>";

		rtn.setMessage(dataRequest);
		
	}
	
	
	//发票查询后将XML装换为BEAN
		public YuanDeRtFindInvoiceHeadBean rtXmlToBean(String xml,RtnData rtn){
			if(xml==null||"".equals(xml)){
				rtn.setCode(-1);
				rtn.setMessage("开具发票返回为空");
				return null;
			}
			YuanDeRtFindInvoiceHeadBean rtinvoicebean =new YuanDeRtFindInvoiceHeadBean();
	 
			
			XmlUtil util= new XmlUtil();  
			Map map= util.xmlToMap(xml);
			if(map==null||map.size()==0){
				rtn.setCode(-1);
				rtn.setMessage("开具发票返回数据转换对象时错误");
				return null;
			}
			
			if("0000".equals(map.get("RETURNCODE"))){
				try{
				       JAXBContext jc = JAXBContext.newInstance( YuanDeRtFindInvoiceHeadBean.class);
				       Unmarshaller u = jc.createUnmarshaller();
				       StringBuffer xmlStr = new StringBuffer(xml);
				       rtinvoicebean = (YuanDeRtFindInvoiceHeadBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
	/*			       System.out.println(rtinvoicebean.getFpqqlsh()+"  "+ rtinvoicebean.getDetails().size());
				       for(YuanDeRtFindInvoiceDetailBean detail: rtinvoicebean.getDetails()){
				    	   System.out.println("ddddddddddddddddddddddddddddddddddddddd");
				    	   System.out.println( detail.getFp_dm());
				       }*/
				       
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				rtn.setCode(-1);
				rtn.setMessage((String)map.get("RETURNMESSAGE"));
				return null;
			}
			return rtinvoicebean;
		}

		//发票查询后将XML装换为BEAN
		public YuanDeDzRntDataBean rtDzBbXmlToBean(String xml,RtnData rtn){
			if(xml==null||"".equals(xml)){
				rtn.setCode(-1);
				rtn.setMessage("开具发票返回为空");
				return null;
			}
			YuanDeDzRntDataBean rtinvoicebean =new YuanDeDzRntDataBean();
	 
			try{
			       JAXBContext jc = JAXBContext.newInstance( YuanDeDzRntDataBean.class);
			       Unmarshaller u = jc.createUnmarshaller();
			       StringBuffer xmlStr = new StringBuffer(xml);
			       rtinvoicebean = (YuanDeDzRntDataBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
			       
			}catch(Exception e){
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				e.printStackTrace();
			}
				
 
			return rtinvoicebean;
		}
	
}
