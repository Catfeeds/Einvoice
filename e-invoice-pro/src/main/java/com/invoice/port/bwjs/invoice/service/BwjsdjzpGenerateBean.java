package com.invoice.port.bwjs.invoice.service;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.invoice.bean.db.InvoicePrintDetail;
import com.invoice.bean.db.InvoicePrintHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.port.RtInvoiceBean;
import com.invoice.port.bwjs.invoice.bean.BwjsdjzpInvoiceDetailBean;
import com.invoice.port.bwjs.invoice.bean.BwjsdjzpInvoiceHeadBean;
import com.invoice.port.bwjs.invoice.bean.BwjsdjzpRtInvoiceBean;
import com.invoice.port.bwjs.invoice.bean.BwjsdjzpZpInvoiceDetailList;
import com.invoice.port.bwjs.invoice.bean.GlobalZpinterfaceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;
import com.invoice.util.NumberToCN;

public class BwjsdjzpGenerateBean {
	
	/**
	 * 开具发票
	 * 装换为百旺接口对象 
	 * **/
	public void generateBaiWangzpBean(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailListOld,RtnData rtn){
		//对象定义和初始化
				/**
				 * 对百旺数据对象定义
				 * head 开票头
				 * bwDetailList 开票明细集合
				 * **/
				BwjsdjzpInvoiceHeadBean head = new BwjsdjzpInvoiceHeadBean();
				List<BwjsdjzpInvoiceDetailBean> bwDetailList = new ArrayList<BwjsdjzpInvoiceDetailBean>();
				BwjsdjzpZpInvoiceDetailList zpInvliceDetaillist = new BwjsdjzpZpInvoiceDetailList();
				List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
				GlobalZpinterfaceBean globalzp = new GlobalZpinterfaceBean();
				globalzp.setId("10008");
				globalzp.setComment("发票开具");
				head.setYylxdm("1");
				StringBuffer xml = new StringBuffer();
				xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?>"+
							"<business id=\"10008\" comment=\"发票开具\">"+
							"<body yylxdm=\"1\">");

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
				if(detailListOld ==null){
					rtn.setCode(-1);
					rtn.setMessage("小票数据集合 detailList 没有初始化");
					return;
				}
				
				for(InvoiceSaleDetail detail:detailListOld){
					if(detail.getIsinvoice()==null||"".equals(detail.getIsinvoice())){
						detail.setIsinvoice("Y");
					}
					if("Y".equals(detail.getIsinvoice())){
						detailList.add(detail);
					}
				}
				
				/*if(taxinfo.getItfjrdm()==null||"".equals(taxinfo.getItfjrdm())){
					rtn.setCode(-1);
					rtn.setMessage("访问的开票端IP不能为空");
					return;
				}else{
					//head.setXhdwsbh(taxinfo.getTaxno());//销售方纳税人识别号
					xml.append("<fwqdz>"+(taxinfo.getItfjrdm())+"</fwqdz>");
				}
				
				if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
					rtn.setCode(-1);
					rtn.setMessage("访问的开票端端口号不能为空");
					return;
				}else{
					//head.setXhdwsbh(taxinfo.getTaxno());//销售方纳税人识别号
					xml.append("<fwqdkh>"+(taxinfo.getItfskpkl())+"</fwqdkh>");
				}
				
				if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
					rtn.setCode(-1);
					rtn.setMessage("访问的开票端机器码不能为空");
					return;
				}else{
					//head.setXhdwsbh(taxinfo.getTaxno());//销售方纳税人识别号
					xml.append("<jqbh>"+(taxinfo.getItfskpbh())+"</jqbh>");
				}*/
				
				
				if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
					rtn.setCode(-1);
					rtn.setMessage("开票点编码不能为空");
					return;
				}else{
					//head.setKpzdbs(invque.getIqtaxzdh());//开票点编码
					xml.append("<kpzdbs>"+(invque.getIqtaxzdh())+"</kpzdbs>");
				}
				
				if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
					rtn.setCode(-1);
					rtn.setMessage("发票种类编码不能为空");
					return;
				}else{
					//head.setFplxdm(invque.getIqfplxdm());//发票种类编码
					xml.append("<fplxdm>"+(invque.getIqfplxdm())+"</fplxdm>");
				}
				
				//开票头转换
				if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
					rtn.setCode(-1);
					rtn.setMessage("发票流水号不能为空");
					return;
				}else{
					//head.setFpqqlsh(invque.getIqseqno());//发票流水号
					xml.append("<fpqqlsh>"+(invque.getIqseqno())+"</fpqqlsh>");
				}
				
				if(invque.getIqtype()==0||invque.getIqtype()==1){
					
					//head.setKplx(String.valueOf(invque.getIqtype()));//开票类型
					xml.append("<kplx>"+(String.valueOf(invque.getIqtype()))+"</kplx>");
				}else{
					rtn.setCode(-1);
					rtn.setMessage("开票类型不正确");
					return;
				}
				
				if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
					//head.setTspz("00");//特殊票种标识
					xml.append("<tspz>00</tspz>"); 
				}
				
				if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
					rtn.setCode(-1);
					rtn.setMessage("销售方纳税人识别号不能为空");
					return;
				}else{
					//head.setXhdwsbh(taxinfo.getTaxno());//销售方纳税人识别号
					xml.append("<xhdwsbh>"+(taxinfo.getTaxno())+"</xhdwsbh>");
				}
				
				if(taxinfo.getTaxname()==null||"".equals(taxinfo.getTaxname())){
					rtn.setCode(-1);
					rtn.setMessage("销售方名称不能为空");
					return;
				}else{
					//head.setXhdwmc(taxinfo.getTaxname());//销售方名称
					xml.append("<xhdwmc>"+(taxinfo.getTaxname())+"</xhdwmc>");
				}
				if("007".equals(invque.getIqfplxdm())||"004".equals(invque.getIqfplxdm())){
					if (taxinfo.getTaxadd() == null || "".equals(taxinfo.getTaxadd())) {
						rtn.setCode(-1);
						rtn.setMessage("销货方地址电话不能为空");
						return;
					} else {
						//head.setXhdwdzdh(taxinfo.getTaxadd());// 销货方地址电话
						xml.append("<xhdwdzdh>"+(taxinfo.getTaxadd())+"</xhdwdzdh>");
					}
					
					if (taxinfo.getTaxbank() == null || "".equals(taxinfo.getTaxbank())) {
						rtn.setCode(-1);
						rtn.setMessage("销售方银行账号不能为空");
						return;
					} else {
						//head.setXhdwyhzh(taxinfo.getTaxbank());// 销售方银行账号
						xml.append("<xhdwyhzh>"+(taxinfo.getTaxbank())+"</xhdwyhzh>");
					}

				}
				
				if("004".equals(invque.getIqfplxdm())){
					if(invque.getIqgmftax()==null||"".equals(invque.getIqgmftax())){
						rtn.setCode(-1);
						rtn.setMessage("购方单位代码不能为空");
						return;
					}else{
						//head.setGhdwsbh(invque.getIqgmftax());//购方单位代码
						xml.append("<ghdwsbh>"+(invque.getIqgmftax())+"</ghdwsbh>");
					}
					
				}else{
					
					//head.setGhdwsbh(invque.getIqgmftax());//购方单位代码
					xml.append("<ghdwsbh>"+(invque.getIqgmftax())+"</ghdwsbh>");
				}
				
				if(invque.getIqgmfname()==null||"".equals(invque.getIqgmfname())){
					rtn.setCode(-1);
					rtn.setMessage("购方客户名称不能为空");
					return;
				}else{
					//head.setGhdwmc(invque.getIqgmfname());//购方客户名称
					xml.append("<ghdwmc>"+(invque.getIqgmfname())+"</ghdwmc>");
				}
				
				if("004".equals(invque.getIqfplxdm())){

					if(invque.getIqgmfadd()==null||"".equals(invque.getIqgmfadd())){
						rtn.setCode(-1);
						rtn.setMessage("购方地址及电话不能为空");
						return;
					}else{
						//head.setGhdwdzdh(invque.getIqgmfadd());//购方地址及电话
						xml.append("<ghdwdzdh>"+(invque.getIqgmfadd())+"</ghdwdzdh>");
					}
					
					if(invque.getIqgmfbank()==null||"".equals(invque.getIqgmfbank())){
						rtn.setCode(-1);
						rtn.setMessage("购方开户行及账号不能为空");
						return;
					}else{
						//head.setGhdwyhzh(invque.getIqgmfbank());//购方开户行及账号
						xml.append("<ghdwyhzh>"+(invque.getIqgmfbank())+"</ghdwyhzh>");
					}
				}else{
					
					if("007".equals(invque.getIqfplxdm())){
						//head.setGhdwdzdh(invque.getIqgmfadd());//购方地址及电话
						//head.setGhdwyhzh(invque.getIqgmfbank());//购方开户行及账号
						xml.append("<ghdwdzdh>"+(invque.getIqgmfadd())+"</ghdwdzdh>");
						xml.append("<ghdwyhzh>"+(invque.getIqgmfbank())+"</ghdwyhzh>");
					}

				}
				
				if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
					if(detailList.size()>=8)
						//head.setQdbz("1");//清单标志
					xml.append("<qdbz>1</qdbz>");
					else
						//head.setQdbz("0");
					xml.append("<qdbz>0</qdbz>");
				}
			
				if(invque.getZsfs()==null||"".equals(invque.getZsfs())){
					rtn.setCode(-1);
					rtn.setMessage("征税方式不能为空");
					return;
				}else{
					//head.setZsfs(invque.getZsfs());//征税方式
					xml.append("<zsfs>"+(invque.getZsfs())+"</zsfs>");
				}
			
				
				if("025".equals(invque.getIqfplxdm())){
					//head.setFppy("06");//发票样张
					
					if(invque.getJpzz()==null||"".equals(invque.getJpzz())){
						xml.append("<fppy>"+("06")+"</fppy>");
						invque.setJpzz("06");
					}else{
						xml.append("<fppy>"+(invque.getJpzz())+"</fppy>");
					}
					
				}
				
				xml.append("<fyxm count=\""+(detailList.size())+"\">");

				//开票明细转换
				int rownum = 1;
				//标记明细行是否为开票行是产生错误
				String flag = "N";
				for(InvoiceSaleDetail detail:detailList){
					if(detail.getIsinvoice()==null||"".equals(detail.getIsinvoice())){
						flag ="Y";
						break;
					}else{
						if("Y".equals(detail.getIsinvoice())){
							BwjsdjzpInvoiceDetailBean bwdetail = new BwjsdjzpInvoiceDetailBean();
							if(detail.getFphxz()==null||"".equals(detail.getFphxz())){
								detail.setFphxz("0");
							}
							rownum =openGoodsDetail(detail, bwdetail,detailList, invque, rownum ,bwDetailList,xml);
							 
						}
					}
				}
				xml.append("</fyxm>");
				
				if(invque.getIqtype()==1&&!invque.getIqfplxdm().equals("004")){

					//head.setHjje(String.valueOf(MathCal.add(invque.getIqtotje()*-1,0,2)));//合计金额
					xml.append("<hjje>"+(String.valueOf(MathCal.add(invque.getIqtotje()*-1,0,2)))+"</hjje>");
					//head.setHjse(String.valueOf(MathCal.add(invque.getIqtotse()*-1,0,2)));//合计税额
					xml.append("<hjse>"+(String.valueOf(MathCal.add(invque.getIqtotse()*-1,0,2)))+"</hjse>");
					//head.setJshj(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1));//价税合计
					xml.append("<jshj>"+(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1))+"</jshj>");
				}else{
					
					//head.setHjje(String.valueOf(MathCal.add(invque.getIqtotje(),0,2)));//合计金额
					xml.append("<hjje>"+(String.valueOf(MathCal.add(invque.getIqtotje(),0,2)))+"</hjje>");
					//head.setHjse(String.valueOf(MathCal.add(invque.getIqtotse(),0,2)));//合计税额
					xml.append("<hjse>"+(String.valueOf(MathCal.add(invque.getIqtotse(),0,2)))+"</hjse>");
					//head.setJshj(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))));//价税合计
					xml.append("<jshj>"+(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))))+"</jshj>");
				}
				//head.setKce("");//扣除额
				xml.append("<kce></kce>");
				//head.setBz(invque.getIqmemo());//备注
				xml.append("<bz>"+(invque.getPaynote()==null?"":invque.getPaynote())+"</bz>");
				//xml.append("<bz>"+(invque.getIqmemo())+"</bz>");
				//head.setSkr(invque.getIqpayee());//收款人
				if("025".equals(invque.getIqfplxdm())){ //如果卷票收款人为空,用开票人
				if(invque.getIqpayee()==null) invque.setIqpayee("");
				if("".equals(invque.getIqpayee())){
					xml.append("<skr>"+(invque.getIqadmin())+"</skr>");
				}else{
					xml.append("<skr>"+(invque.getIqpayee())+"</skr>");
				}
				}else{
					xml.append("<skr>"+(invque.getIqpayee())+"</skr>");
				}
				if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
				//	head.setFhr(invque.getIqchecker());//审核人
					xml.append("<fhr>"+(invque.getIqchecker())+"</fhr>");
				}

				if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
					rtn.setCode(-1);
					rtn.setMessage("开票人不能为空");
					return;
				}else{
					//head.setKpr(invque.getIqadmin());//开票人
					xml.append("<kpr>"+(invque.getIqadmin())+"</kpr>");
				}
				
				if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
					//	head.setFhr(invque.getIqchecker());//审核人
						xml.append("<tzdbh></tzdbh>");
				}
				
			if(invque.getIqtype()==1&&!invque.getIqfplxdm().equals("004")){
					
					if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
						rtn.setCode(-1);
						rtn.setMessage("原发票代码不能为空");
						return;
					}else{
						//head.setYfpdm(invque.getIqyfpdm());//原发票代码
						xml.append("<yfpdm>"+(invque.getIqyfpdm())+"</yfpdm>");
					}
					
					if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
						rtn.setCode(-1);
						rtn.setMessage("原发票号码不能为空");
						return;
					}else{
						//head.setYfphm(invque.getIqyfphm());//原发票号码
						xml.append("<yfphm>"+(invque.getIqyfphm())+"</yfphm>");
					}
				
				}else{
					xml.append("<yfpdm></yfpdm>");
					xml.append("<yfphm></yfphm>");
					//head.setYfpdm(invque.getIqyfpdm());//原发票代码
					//head.setYfphm(invque.getIqyfphm());//原发票号码
				
				}
				xml.append("<qmcs></qmcs>");
				xml.append("</body></business>");

				if("Y".equals(flag)){
					rtn.setCode(-1);
					rtn.setMessage("开票明细是否可开票标记错误数据有问题");
					return;
				}

			System.out.println(xml.toString());	
			 rtn.setCode(0);
			 rtn.setMessage(xml.toString());
	}
	
	
	public int openGoodsDetail(InvoiceSaleDetail detail,BwjsdjzpInvoiceDetailBean bwdetail,List<InvoiceSaleDetail> detailList,Invque invque,int rownum,List<BwjsdjzpInvoiceDetailBean> bwDetailList, StringBuffer xml){
		xml.append("<group xh=\""+(String.valueOf(rownum))+"\">");
		DecimalFormat df1 = new DecimalFormat("#########.00");
		if(invque.getIqtype()==0){
			//bwdetail.setFphxz(detail.getFphxz());//发票行性质
			xml.append("<fphxz>"+(detail.getFphxz())+"</fphxz>");
			//bwdetail.setXh(String.valueOf(rownum));//明细行号
			if("025".equals(invque.getIqfplxdm())){
				
				//bwdetail.setXm(detail.getGoodsname());//商品名称
				xml.append("<xm>"+(detail.getGoodsname())+"</xm>");
				if(!"1".equals(detail.getFphxz())){
					//bwdetail.setSl(String.valueOf(detail.getQty()));//商品数量
					xml.append("<sl>"+(String.valueOf(MathCal.add(detail.getQty(),0,3)))+"</sl>");
					
					if(detail.getPrice()==null){
						//bwdetail.setHsdj("");//detail.getPrice();单价
						xml.append("<hsdj></hsdj>");
					}else{
						//bwdetail.setHsdj(String.valueOf(MathCal.add(detail.getPrice()*(1+detail.getTaxrate()),0,6)));//detail.getPrice();单价
						xml.append("<hsdj>"+(String.valueOf(MathCal.add(detail.getPrice()*(1+detail.getTaxrate()),0,3)))+"</hsdj>");
					}
					//bwdetail.setHsje(String.valueOf(MathCal.add(detail.getAmount()*(1+detail.getTaxrate()),0,2)));//含税金额
					xml.append("<hsje>"+(String.valueOf(MathCal.add(detail.getAmount(),detail.getTaxfee(),2)))+"</hsje>");
					
					if(detail.getPrice()==null){
						//bwdetail.setBhsdj("");//detail.getPrice();单价
						xml.append("<bhsdj></bhsdj>");
					}else{
						//bwdetail.setBhsdj(String.valueOf(MathCal.add(detail.getPrice(),0,6)));//detail.getPrice();单价
						xml.append("<bhsdj>"+(String.valueOf(MathCal.add(detail.getPrice(),0,6)))+"</bhsdj>");
					}
					
					//bwdetail.setBhsje(String.valueOf(detail.getAmount()));//不含税金额
					xml.append("<bhsje>"+(String.valueOf(detail.getAmount()))+"</bhsje>");
					
					if(detail.getTaxrate()==0){
						xml.append("<zsl>0</zsl>");
					}else{
						xml.append("<zsl>"+(String.valueOf(detail.getTaxrate()))+"</zsl>");
					}
					
					//bwdetail.setZsl(String.valueOf(detail.getTaxrate()));//税率
					bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
					xml.append("<se>"+(String.valueOf(detail.getTaxfee()))+"</se>");
				}
			}else{
				//bwdetail.setSpmc(detail.getGoodsname());//商品名称
				xml.append("<spmc>"+(detail.getGoodsname())+"</spmc>");
				//bwdetail.setSpsm("");//商品税目  //004  007有
				xml.append("<spsm></spsm>");
				if(!"1".equals(detail.getFphxz())){
					//bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
					xml.append("<ggxh>"+(detail.getSpec()==null?"":detail.getSpec())+"</ggxh>");
					//bwdetail.setDw(detail.getUnit());//计量单位
					xml.append("<dw>"+(detail.getUnit()==null?"":detail.getUnit())+"</dw>");
					//bwdetail.setSpsl(String.valueOf(detail.getQty()));//商品数量
					xml.append("<spsl>"+(String.valueOf(detail.getQty()))+"</spsl>");
					if(detail.getPrice()==null){
						//bwdetail.setDj("");//detail.getPrice();单价
						xml.append("<dj></dj>");
					}else{
						//bwdetail.setDj(String.valueOf(MathCal.add(detail.getPrice(),0,6)));//detail.getPrice();单价
						xml.append("<dj>"+(String.valueOf(MathCal.add(detail.getPrice(),0,6)))+"</dj>");
					}
				}else{
					xml.append("<ggxh></ggxh>");
					xml.append("<dw></dw>");
					xml.append("<spsl></spsl>");
					xml.append("<dj></dj>");
			
				}
				
				//bwdetail.setJe(String.valueOf(detail.getAmount()));//金额
				xml.append("<je>"+(String.valueOf(detail.getAmount()))+"</je>");
				//bwdetail.setSl(String.valueOf(detail.getTaxrate()));//税率
				if(detail.getTaxrate()==0){
					xml.append("<sl>0</sl>");
				}else{
					xml.append("<sl>"+(String.valueOf(detail.getTaxrate()))+"</sl>");
				}
				if(detail.getTaxfee()==0){
					xml.append("<se>0.00</se>");
				}else{
					xml.append("<se>"+(String.valueOf(detail.getTaxfee()))+"</se>");
				}
				//bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
				
				//bwdetail.setHsbz("0");//含税标志
				xml.append("<hsbz>0</hsbz>");
			}
			
			//bwdetail.setSpbm(detail.getTaxitemid());//商品编码
			xml.append("<spbm>"+(detail.getTaxitemid())+"</spbm>");
			xml.append("<zxbm></zxbm>");
			//bwdetail.setYhzcbs(detail.getTaxpre());//是否使用优惠政策
			xml.append("<yhzcbs>"+(detail.getTaxpre())+"</yhzcbs>");
			//bwdetail.setLslbs(detail.getZerotax());//免税类型
			xml.append("<lslbs>"+(detail.getZerotax()==null?"":detail.getZerotax())+"</lslbs>");
			//bwdetail.setZzstsgl(detail.getTaxprecon());//增值税特殊管理
			xml.append("<zzstsgl>"+(detail.getTaxprecon()==null?"":detail.getTaxprecon())+"</zzstsgl>");
			//bwDetailList.add(bwdetail);
			detail.setSeqno(String.valueOf(rownum));
			rownum++;

		}else
		if(invque.getIqtype()==1){
			if(!"1".equals(detail.getFphxz())){
				//bwdetail.setFphxz("0");//发票行性质
				xml.append("<fphxz>0</fphxz>");
				//bwdetail.setXh(String.valueOf(rownum));//明细行号
				double bhsje =0; double hsje =0; double se =0;
				if("025".equals(invque.getIqfplxdm())){
					//bwdetail.setXm(detail.getGoodsname());//商品名称
					xml.append("<xm>"+(detail.getGoodsname())+"</xm>");
					xml.append("<sl></sl>");
					
					
					if("2".equals(detail.getFphxz())){
						for(InvoiceSaleDetail detail1:detailList){
							if("1".equals(detail1.getFphxz())){
								if(detail.getSeqno().equals(String.valueOf(Integer.parseInt(detail1.getSeqno())-1))&& detail.getGoodsid().equals(detail1.getGoodsid())&&detail.getRowno()==detail1.getZhdyhh()){
									//bwdetail.setJe(String.valueOf(MathCal.add(detail.getAmount(),detail1.getAmount(),2)*-1));//金额
									bhsje =MathCal.add(detail.getAmount(),detail1.getAmount(),2)*-1;
									//hsje = MathCal.add(detail.getAmount()*(1+detail.getTaxrate()),detail1.getAmount()*(1+detail.getTaxrate()),2)*-1;
									se =MathCal.add(detail.getTaxfee(),detail1.getTaxfee(),2)*-1;//税额
									hsje = bhsje+se;
									break;
								}
							}
						} 
					}else{
						//bwdetail.setJe(String.valueOf(detail.getAmount()*-1));//金额
						bhsje = detail.getAmount()*-1;
						//hsje = MathCal.add(detail.getAmount()*(1+detail.getTaxrate()),0,2)*-1;
						//bwdetail.setSe(String.valueOf(detail.getTaxfee()*-1));//税额
						se = detail.getTaxfee()*-1;
						hsje = bhsje+se;
					}
					xml.append("<hsdj></hsdj>");
					xml.append("<hsje>"+(String.valueOf(hsje))+"</hsje>");
					xml.append("<bhsdj></bhsdj>");
					xml.append("<bhsje>"+(bhsje)+"</bhsje>");
					xml.append("<zsl>"+(String.valueOf(detail.getTaxrate()))+"</zsl>");
					xml.append("<se>"+(String.valueOf(se))+"</se>");
				}else{
					//bwdetail.setSpmc(detail.getGoodsname());//商品名称
					xml.append("<spmc>"+(detail.getGoodsname())+"</spmc>");
					xml.append("<spsm></spsm>");
					//bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
					xml.append("<ggxh>"+(detail.getSpec()==null?"":detail.getSpec())+"</ggxh>");
					//bwdetail.setDw(detail.getUnit());//计量单位
					xml.append("<dw>"+(detail.getUnit()==null?"":detail.getUnit())+"</dw>");
					
					if("2".equals(detail.getFphxz())){
						for(InvoiceSaleDetail detail1:detailList){
							if("1".equals(detail1.getFphxz())){
								if(detail.getSeqno().equals(String.valueOf(Integer.parseInt(detail1.getSeqno())-1))&& detail.getGoodsid().equals(detail1.getGoodsid())&&detail.getRowno()==detail1.getZhdyhh()){
									//bwdetail.setJe(String.valueOf(MathCal.add(detail.getAmount(),detail1.getAmount(),2)*-1));//金额
									xml.append("<je>"+(String.valueOf(MathCal.add(detail.getAmount(),detail1.getAmount(),2)*-1))+"</je>");
									//bwdetail.setSe(String.valueOf(MathCal.add(detail.getTaxfee(),detail1.getTaxfee(),2)*-1));//税额
									 se = MathCal.add(detail.getTaxfee(),detail1.getTaxfee(),2)*-1;
									break;
								}
							}
						}
					}else{
						//bwdetail.setJe(String.valueOf(detail.getAmount()*-1));//金额
						xml.append("<je>"+(String.valueOf(detail.getAmount()*-1))+"</je>");
						//bwdetail.setSe(String.valueOf(detail.getTaxfee()*-1));//税额
						se = detail.getTaxfee()*-1;
					}
					xml.append("<sl>"+(String.valueOf(detail.getTaxrate()))+"</sl>");
					//bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
					xml.append("<se>"+(String.valueOf(se))+"</se>");
					//bwdetail.setHsbz("0");//含税标志
					xml.append("<hsbz>0</hsbz>");
				}
				

				//bwdetail.setSpbm(detail.getTaxitemid());//商品编码
				xml.append("<spbm>"+(detail.getTaxitemid())+"</spbm>");
				//bwdetail.setSpsm(detail.getTaxitemid());//商品税目
				xml.append("<zxbm></zxbm>");
				xml.append("<yhzcbs>"+(detail.getTaxpre())+"</yhzcbs>");
				//bwdetail.setLslbs(detail.getZerotax());//免税类型
				xml.append("<lslbs>"+(detail.getZerotax()==null?"":detail.getZerotax())+"</lslbs>");
				//bwdetail.setZzstsgl(detail.getTaxprecon());//增值税特殊管理
				xml.append("<zzstsgl>"+(detail.getTaxprecon()==null?"":detail.getTaxprecon())+"</zzstsgl>");
				//bwDetailList.add(bwdetail);
				//detail.setSeqno(String.valueOf(rownum));
				rownum++;

			}
		}
		xml.append("</group>");
		return rownum;
}
	
	 
	
 
	
	 
	
	
	/**
	 * 开具发票返回后的发票数据解析
	 * **/

	public BwjsdjzpRtInvoiceBean rtZpOpenToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("发票正在开具中,请耐心登陆或稍后再试");
			return null;
		}
		GlobalZpinterfaceBean rtinvoicebean =new GlobalZpinterfaceBean();
		BwjsdjzpRtInvoiceBean rtOpenInvoiceBean = new BwjsdjzpRtInvoiceBean();
		 
		try{
		       JAXBContext jc = JAXBContext.newInstance( GlobalZpinterfaceBean.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer(xml);
		       rtinvoicebean = (GlobalZpinterfaceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
		       
		       if(rtinvoicebean.getBody().getReturncode()==null||!"0".equals(rtinvoicebean.getBody().getReturncode())){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage(rtinvoicebean.getBody().getReturnmsg());
		    	   return rtOpenInvoiceBean;
		       }
		       
		       if(rtinvoicebean.getId()==null) rtinvoicebean.setId("");
		       /*if("10008".equals(rtinvoicebean.getId())){
		    	   rtOpenInvoiceBean.setEwm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getEwm());
		    	   rtOpenInvoiceBean.setFpdm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getFpdm());
		    	   rtOpenInvoiceBean.setFphm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getFphm());
		    	   //rtOpenInvoiceBean.setJym(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().get);
		    	   rtOpenInvoiceBean.setKprq(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getKprq());
		    	   rtOpenInvoiceBean.setSkm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getSkm());
		       }else{*/
		       rtOpenInvoiceBean = rtinvoicebean.getBody().getRtBean();
		       //}
		      
		       if(rtOpenInvoiceBean==null){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage("解析发票返回的数据有问题！");
		    	   return null;
		       }

		       System.out.println(rtOpenInvoiceBean.getFphm() );
	
		    	
			   
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	
		return rtOpenInvoiceBean;
	}
	
	/**
	 * 发票查询返回后的发票数据解析
	 * **/

	public GlobalZpinterfaceBean rtZpFindToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("发票正在开具中,请耐心登陆或稍后再试");
			return null;
		}
		GlobalZpinterfaceBean rtinvoicebean =new GlobalZpinterfaceBean();

		try{
		       JAXBContext jc = JAXBContext.newInstance( GlobalZpinterfaceBean.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer(xml);
		       rtinvoicebean = (GlobalZpinterfaceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );

			   
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	
		return rtinvoicebean;
	}
	
	/**
	 * 发票作废返回后的发票数据解析
	 * **/

	public RtInvoiceBean rtZpZfToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("发票作废中,请耐心登陆或稍后再试");
			return null;
		}
		GlobalZpinterfaceBean rtinvoicebean =new GlobalZpinterfaceBean();
		RtInvoiceBean rtInvoiceBean = new RtInvoiceBean();
		 
		try{
		       JAXBContext jc = JAXBContext.newInstance( GlobalZpinterfaceBean.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer(xml);
		       rtinvoicebean = (GlobalZpinterfaceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
		       if(rtinvoicebean==null){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage("解析发票返回的数据有问题！");
		    	   return null;
		       }
		       if(rtinvoicebean.getBody().getReturncode()==null||!"0".equals(rtinvoicebean.getBody().getReturncode())){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage(rtinvoicebean.getBody().getReturnmsg());
		    	   return rtInvoiceBean;
		       }
		       	   rtInvoiceBean.setFp_dm(rtinvoicebean.getBody().getRtBean().getFpdm());
		       	   rtInvoiceBean.setFp_hm(rtinvoicebean.getBody().getRtBean().getFphm());
		       	   rtInvoiceBean.setKprq(rtinvoicebean.getBody().getRtBean().getZfrq());
  
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	
		return rtInvoiceBean;
	}
	
	/**
	 * 查询空白票返回后的发票数据解析
	 * **/

	public RtInvoiceBean rtblankInvoiceToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("发票作废中,请耐心登陆或稍后再试");
			return null;
		}
		GlobalZpinterfaceBean rtinvoicebean =new GlobalZpinterfaceBean();
		RtInvoiceBean rtInvoiceBean = new RtInvoiceBean();
		 
		try{
		       JAXBContext jc = JAXBContext.newInstance( GlobalZpinterfaceBean.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer(xml);
		       rtinvoicebean = (GlobalZpinterfaceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
		       if(rtinvoicebean==null){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage("解析发票返回的数据有问题！");
		    	   return null;
		       }
		       if(rtinvoicebean.getBody().getReturncode()==null||!"0".equals(rtinvoicebean.getBody().getReturncode())){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage(rtinvoicebean.getBody().getReturnmsg());
		    	   return rtInvoiceBean;
		       }
		       	   rtInvoiceBean.setFp_dm(rtinvoicebean.getBody().getRtBean().getDqfpdm());
		       	   rtInvoiceBean.setFp_hm(rtinvoicebean.getBody().getRtBean().getDqfphm());
		       	    
  
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	
		return rtInvoiceBean;
	}
	
	 
	
	
	/**
	 * 空白发票查询
	 * **/
	public void blankInvoice(Invque invque,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
		StringBuffer xml = new StringBuffer();
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}

		xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"10004\" comment=\"查询当前未开票号\"><body yylxdm=\"1\">");
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票终端标识不能为空");
			return;
		}else{
			xml.append("<kpzdbs>"+ invque.getIqtaxzdh() +"</kpzdbs>");//开票终端标识
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票类型代码不能为空");
			return;
		}else{
			xml.append("<fplxdm>"+ invque.getIqfplxdm() +"</fplxdm>");//发票类型代码
		}
		
		xml.append("</body></business>");
		
		rtn.setMessage(xml.toString());
	}
	
	
	/**
	 * 发票打印
	 * **/
	public InvoicePrintHead zp_print(Invque invque, Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
		StringBuffer xml = new StringBuffer();
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return null;
		}
		
		if(taxinfo == null){
			rtn.setCode(-1);
			rtn.setMessage("纳税号信息没有初始化");
			return null;
		}
		
		if(detailList == null){
			rtn.setCode(-1);
			rtn.setMessage("打印明细没有初始化");
			return null;
		}
		InvoicePrintHead printhead = new InvoicePrintHead();
		DecimalFormat df1 = new DecimalFormat("########0.00");
 
		printhead.setSeqno(invque.getIqseqno());
		printhead.setEwm(invque.getRtewm());
		//printhead.setEwm("01,04,"+invque.getRtfpdm()+","+invque.getRtfphm()+","+String.valueOf(df1.format(invque.getIqtotje()))+","+invque.getRtkprq().substring(0, 8)+","+invque.getRtjym()+",");
		printhead.setItfskpbh(taxinfo.getItfskpbh());
		printhead.setFpdm(invque.getRtfpdm());
		printhead.setFphm(invque.getRtfphm());
		printhead.setFprq(invque.getRtkprq().substring(0, 4)+"年"+invque.getRtkprq().substring(4, 6)+"月"+invque.getRtkprq().substring(6, 8)+"日");
		printhead.setFpjym(invque.getRtjym().substring(0, 5)+" "+invque.getRtjym().substring(5, 10)+" "+invque.getRtjym().substring(10, 15)+" "+invque.getRtjym().substring(15));
		printhead.setGmfname(invque.getIqgmfname());
		printhead.setGmftax(invque.getIqgmftax());
		printhead.setGmfaddr(invque.getIqgmfadd());
		printhead.setGmfbank(invque.getIqgmfbank());
		printhead.setFpskm(invque.getRtskm());
		printhead.setSumamount(String.valueOf(df1.format(invque.getIqtotje())));
		printhead.setSumtaxfee(String.valueOf(df1.format(invque.getIqtotse())));
		printhead.setAmt(String.valueOf(df1.format(MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))));
		printhead.setAmtdx(NumberToCN.number2CNMontrayUnit(new BigDecimal(printhead.getAmt())));
		printhead.setXsfname(taxinfo.getTaxname());
		printhead.setXsftax(taxinfo.getTaxno());
		printhead.setXsfaddr(taxinfo.getTaxadd());
		printhead.setXsfbank(taxinfo.getTaxbank());
		printhead.setNote(invque.getIqmemo());
		printhead.setWeixinkpr(invque.getIqadmin());
		printhead.setWeixinfhr(invque.getIqchecker());
		printhead.setWeixinskr(invque.getIqpayee());
		
		List<InvoicePrintDetail> details = new ArrayList<InvoicePrintDetail>();
		int lslist =  invque.getIsList();
		if(lslist!=1&detailList.size()>7){
			InvoicePrintDetail printdetail = new InvoicePrintDetail();
			printdetail.setGoodsname("(详见销货清单)");
			printdetail.setAmount(String.valueOf(df1.format(invque.getIqtotje())));
			printdetail.setTaxfee(String.valueOf(df1.format(invque.getIqtotse())));
			details.add(printdetail);
		}else{
			if(lslist==1&detailList.size()>7){
				int  rownum = detailList.size();
				int yu = rownum%25;
				int cha = 25-yu;
				for(InvoiceSaleDetail saledetail :detailList){
					InvoicePrintDetail printdetail = new InvoicePrintDetail();
					printdetail.setGoodsname(saledetail.getGoodsname());
					printdetail.setSpec(saledetail.getSpec());
					printdetail.setUnit(saledetail.getUnit());
					printdetail.setQty(String.valueOf(BigDecimal.valueOf(saledetail.getQty()).stripTrailingZeros().toPlainString()));
					printdetail.setPrice(String.valueOf(BigDecimal.valueOf(saledetail.getPrice()).stripTrailingZeros().toPlainString()));
					printdetail.setAmount(String.valueOf(df1.format(saledetail.getAmount())));
					if(saledetail.getTaxrate()==0){
						printdetail.setTaxrate("0%");
					}else{
						printdetail.setTaxrate((BigDecimal.valueOf(saledetail.getTaxrate()*100).stripTrailingZeros().toPlainString())+"%");	
					}
					printdetail.setTaxfee(String.valueOf(df1.format(saledetail.getTaxfee())));
					printdetail.setSeqno(saledetail.getSeqno()==null?"":saledetail.getSeqno());
					details.add(printdetail);
				}
				for(int i=0;i<cha;i++){
					InvoicePrintDetail printdetail = new InvoicePrintDetail();
					details.add(printdetail);
				}
			}else{
				for(InvoiceSaleDetail saledetail :detailList){
					InvoicePrintDetail printdetail = new InvoicePrintDetail();
					printdetail.setGoodsname(saledetail.getGoodsname());
					printdetail.setSpec(saledetail.getSpec());
					printdetail.setUnit(saledetail.getUnit());
					printdetail.setQty(String.valueOf(BigDecimal.valueOf(saledetail.getQty()).stripTrailingZeros().toPlainString()));
					printdetail.setPrice(String.valueOf(BigDecimal.valueOf(saledetail.getPrice()).stripTrailingZeros().toPlainString()));
					printdetail.setAmount(String.valueOf(df1.format(saledetail.getAmount())));
					if(saledetail.getTaxrate()==0){
						printdetail.setTaxrate("0%");
					}else{
						printdetail.setTaxrate((BigDecimal.valueOf(saledetail.getTaxrate()*100).stripTrailingZeros().toPlainString())+"%");	
					}
					printdetail.setTaxfee(String.valueOf(df1.format(saledetail.getTaxfee())));
					printdetail.setSeqno(saledetail.getSeqno()==null?"":saledetail.getSeqno());
					details.add(printdetail);
				}
			}
		}
		printhead.setDetails(details);
		return printhead;
	}
	
	
	/**
	 * 空白发票作废
	 * 转换为百旺对象
	 * **/
	public void generateInvoiceblankInValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		generateInvoiceInValid(invque,taxinfo,rtn,"0");
	}
	
	/**
	 * 已开发票作废
	 * 转换为百旺对象
	 * **/
	public void generateInvoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		generateInvoiceInValid(invque,taxinfo,rtn,"1");
	}
	
	/**
	 * 发票作废
	 * 转换为百旺对象
	 * **/
	public void generateInvoiceInValid(Invque invque,Taxinfo taxinfo,RtnData rtn,String Zflx){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
		StringBuffer xml = new StringBuffer();
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}

		xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"10009\" comment=\"发票作废\"><body yylxdm=\"1\">");
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票终端标识不能为空");
			return;
		}else{
			xml.append("<kpzdbs>"+ invque.getIqtaxzdh() +"</kpzdbs>");//开票终端标识
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票类型代码不能为空");
			return;
		}else{
			xml.append("<fplxdm>"+ invque.getIqfplxdm() +"</fplxdm>");//发票类型代码
		}
		
		xml.append("<zflx>"+ (Zflx) +"</zflx>");
		
		if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票代码不能为空");
			return;
		}else{
			xml.append("<fpdm>"+ invque.getRtfpdm() +"</fpdm>");//发票类型代码
		}
		
		if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
			rtn.setCode(-1);
			rtn.setMessage("发票号码不能为空");
			return;
		}else{
			xml.append("<fphm>"+ invque.getRtfphm() +"</fphm>");//发票类型代码
		}
		
		if("1".equals(Zflx)){
			xml.append("<hjje>"+(MathCal.add(invque.getIqtotje(),0,2)  )+"</hjje>");
		}else{
			xml.append("<hjje></hjje>");
		}
		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("作废人不能为空");
			return;
		}else{
			xml.append("<zfr>"+ invque.getIqadmin() +"</zfr>");//发票类型代码
		}
		
		xml.append("</body></business>");
		//invque.setIqmsg(xml.toString());
		rtn.setMessage(xml.toString());
		
	}
	
	/**
	 * 发票查询
	 * **/
	public void zp_findInvoice(Invque invque,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
		StringBuffer xml = new StringBuffer();
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}

		xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"10010\" comment=\"发票查询\"><body yylxdm=\"1\">");
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票终端标识不能为空");
			return;
		}else{
			xml.append("<kpzdbs>"+ invque.getIqtaxzdh() +"</kpzdbs>");//开票终端标识
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票类型代码不能为空");
			return;
		}else{
			xml.append("<fplxdm>"+ invque.getIqfplxdm() +"</fplxdm>");//发票类型代码
		}
		
		
		xml.append("<cxfs>2</cxfs>");//发票类型代码
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("流水号不能为空");
			return;
		}else{
			xml.append("<cxtj>"+ invque.getIqseqno() +"</cxtj>");//发票类型代码
		}
		xml.append("</body></business>");
		//invque.setIqmsg(xml.toString());
		rtn.setMessage(xml.toString());
		
	}
	
}
