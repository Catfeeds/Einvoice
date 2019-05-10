package com.invoice.port.bwjf.invoice.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.bwjf.invoice.bean.BwjfDzRtInvoiceBean;
import com.invoice.port.bwjf.invoice.bean.BwjfDzRtpdf_bodyBean;
import com.invoice.port.bwjf.invoice.bean.BwjfInvoiceDetailBean;
import com.invoice.port.bwjf.invoice.bean.BwjfInvoiceHeadBean;
import com.invoice.port.bwjf.invoice.bean.BwjfRtInvoiceBean;
import com.invoice.port.bwjf.invoice.bean.BwjfZpInvoiceDetailList;
import com.invoice.port.bwjf.invoice.bean.GlobalDzinterfaceBean;
import com.invoice.port.bwjf.invoice.bean.GlobalZpinterfaceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;
import com.invoice.util.MyAES;

public class BwjfGenerateBean {
	
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
				BwjfInvoiceHeadBean head = new BwjfInvoiceHeadBean();
				List<BwjfInvoiceDetailBean> bwDetailList = new ArrayList<BwjfInvoiceDetailBean>();
				BwjfZpInvoiceDetailList zpInvliceDetaillist = new BwjfZpInvoiceDetailList();
				List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
				GlobalZpinterfaceBean globalzp = new GlobalZpinterfaceBean();
				globalzp.setId("10008");
				globalzp.setComment("发票开具");
				head.setYylxdm("1");
				StringBuffer xml = new StringBuffer();
				xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?>"+
							"<business comment=\"发票开具\" id=\"10008\">"+
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
							BwjfInvoiceDetailBean bwdetail = new BwjfInvoiceDetailBean();
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
				xml.append("<kce>0</kce>");
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

				xml.append("</body></business>");

				if("Y".equals(flag)){
					rtn.setCode(-1);
					rtn.setMessage("开票明细是否可开票标记错误数据有问题");
					return;
				}
				
				//zpInvliceDetaillist.setCount(String.valueOf(bwDetailList.size()));
				//zpInvliceDetaillist.setDetailList(bwDetailList);
			
				
				
				//head.setDetailList(zpInvliceDetaillist);
				//globalzp.setBody(head);
				//String dataRequest = XMLConverter.objectToXml(globalzp, "utf-8") ;	
				
/*				if(dataRequest ==null||"".equals(dataRequest)){
					rtn.setCode(-1);
					rtn.setMessage("开票数据装换XML失败");
					return;
				}*/
				
			 rtn.setCode(0);
			 rtn.setMessage(xml.toString());
	}
	
	
	public int openGoodsDetail(InvoiceSaleDetail detail,BwjfInvoiceDetailBean bwdetail,List<InvoiceSaleDetail> detailList,Invque invque,int rownum,List<BwjfInvoiceDetailBean> bwDetailList, StringBuffer xml){
		xml.append("<group xh=\""+(String.valueOf(rownum))+"\">");
		
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
					
					xml.append("<zsl>"+(String.valueOf(detail.getTaxrate()))+"</zsl>");
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
				}
				
				//bwdetail.setJe(String.valueOf(detail.getAmount()));//金额
				xml.append("<je>"+(String.valueOf(detail.getAmount()))+"</je>");
				//bwdetail.setSl(String.valueOf(detail.getTaxrate()));//税率
				xml.append("<sl>"+(String.valueOf(detail.getTaxrate()))+"</sl>");
				//bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
				xml.append("<se>"+(String.valueOf(detail.getTaxfee()))+"</se>");
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
	 * 开具发票
	 * 装换为百旺接口对象 
	 * **/
	public void generateBaiWangBean(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailListOld,RtnData rtn){
		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		//对象定义和初始化
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?>"+
				"<business id=\"FPKJ\" comment=\"发票开具\">"+
				"<REQUEST_COMMON_FPKJ class=\"REQUEST_COMMON_FPKJ\">"+
				"<COMMON_FPKJ_FPT class=\"COMMON_FPKJ_FPT\">");
	
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
				
				//开票头转换
				if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
					rtn.setCode(-1);
					rtn.setMessage("发票流水号不能为空");
					return;
				}else{
					xml.append("<FPQQLSH>"+invque.getIqseqno()+"</FPQQLSH>");
					//head.setFpqqlsh(invque.getIqseqno());//发票流水号
				}
				
				if(invque.getIqtype()==0||invque.getIqtype()==1){
					xml.append("<KPLX>"+invque.getIqtype()+"</KPLX>");
					//head.setKplx(String.valueOf(invque.getIqtype()));//开票类型
				}else{
					rtn.setCode(-1);
					rtn.setMessage("开票类型不正确");
					return;
				}
				
				xml.append("<BMB_BBH>"+(taxinfo.getItfbbh()==null?"":taxinfo.getItfbbh())+"</BMB_BBH>");
				//head.setBbh(taxinfo.getItfbbh());//商品编码版本号
				
				if(invque.getZsfs()==null||"".equals(invque.getZsfs())){
					rtn.setCode(-1);
					rtn.setMessage("征税方式不能为空");
					return;
				}else{
					xml.append("<ZSFS>"+invque.getZsfs()+"</ZSFS>");
					//head.setZsfs(invque.getZsfs());//征税方式
				}
				
				xml.append("<TSPZ>00</TSPZ>");
				//head.setTspz("00");//特殊票种标识
				
				if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
					rtn.setCode(-1);
					rtn.setMessage("销方纳税号不能为空");
				}else{
					xml.append("<XSF_NSRSBH>"+taxinfo.getTaxno()+"</XSF_NSRSBH>");
				//head.setSellerTaxNo(taxinfo.getTaxno());
				}
				
				if(taxinfo.getTaxname()==null||"".equals(taxinfo.getTaxname())){
					rtn.setCode(-1);
					rtn.setMessage("销售方名称不能为空");
					return;
				}else{
					xml.append("<XSF_MC>"+taxinfo.getTaxname()+"</XSF_MC>");
					//head.setXsf_mc(taxinfo.getTaxname());//销售方名称
				}
				
				if (taxinfo.getTaxadd() == null || "".equals(taxinfo.getTaxadd())) {
					rtn.setCode(-1);
					rtn.setMessage("销货方地址电话不能为空");
					return;
				} else {
					xml.append("<XSF_DZDH>"+taxinfo.getTaxadd()+"</XSF_DZDH>");
					//head.setXsf_dzdh(taxinfo.getTaxadd());// 销货方地址电话
				}
				xml.append("<XSF_YHZH>"+(taxinfo.getTaxbank()==null?"":taxinfo.getTaxbank())+"</XSF_YHZH>");
				//head.setXsf_yhzh(taxinfo.getTaxbank());//销售方银行账号
				
				xml.append("<XSF_LXFS></XSF_LXFS>");  //销售方移动电话或邮箱
				
				xml.append("<GMF_NSRSBH>"+(invque.getIqgmftax()==null?"":invque.getIqgmftax())+"</GMF_NSRSBH>");   
				//head.setGmf_nsrsbh(invque.getIqgmftax() == null ? "" : invque.getIqgmftax());// 购方单位代码
				
				if(invque.getIqgmfname()==null||"".equals(invque.getIqgmfname())){
					rtn.setCode(-1);
					rtn.setMessage("购方客户名称不能为空");
					return;
				}else{
					xml.append("<GMF_MC>"+invque.getIqgmfname()+"</GMF_MC>"); 
					//head.setGmf_mc(invque.getIqgmfname());//购方客户名称
				}
				
				xml.append("<GMF_DZDH>"+(invque.getIqgmfadd()==null?"":invque.getIqgmfadd())+"</GMF_DZDH>"); 
				//head.setGmf_dzdh(invque.getIqgmfadd() == null ? "" : invque.getIqgmfadd());// 购方地址及电话
				xml.append("<GMF_YHZH>"+(invque.getIqgmfbank()==null?"":invque.getIqgmfbank())+"</GMF_YHZH>"); 
				//head.setGmf_yhzh(invque.getIqgmfbank() == null ? "" : invque.getIqgmfbank());// 购方开户行及账号
				
				xml.append("<GMF_LXFS>"+(invque.getIqemail()==null?"":invque.getIqemail())+"</GMF_LXFS>"); 
				//head.setGmf_sjh(invque.getIqtel() == null ? "" : invque.getIqtel());//购买方手机号
				//head.setGmf_dzyx(invque.getIqemail() == null ? "" : invque.getIqemail());//购买方电子邮箱
				
				if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
					rtn.setCode(-1);
					rtn.setMessage("开票人不能为空");
					return;
				}else{
					xml.append("<KPR>"+invque.getIqadmin()+"</KPR>"); 
					//head.setKpr(invque.getIqadmin());//开票人
				}
				xml.append("<SKR>"+(invque.getIqpayee()==null?"":invque.getIqpayee())+"</SKR>"); 
				//head.setSkr(invque.getIqpayee());//收款人
				xml.append("<FHR>"+(invque.getIqchecker()==null?"":invque.getIqchecker())+"</FHR>"); 
				//head.setFhr(invque.getIqchecker());//审核人
				
				if(invque.getIqtype()==1&&!invque.getIqfplxdm().equals("004")){
					
					if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
						rtn.setCode(-1);
						rtn.setMessage("原发票代码不能为空");
						return;
					}else{
						xml.append("<YFP_DM>"+invque.getIqyfpdm()+"</YFP_DM>"); 
						//head.setYfpdm(invque.getIqyfpdm());//原发票代码
					}
					
					if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
						rtn.setCode(-1);
						rtn.setMessage("原发票号码不能为空");
						return;
					}else{
						xml.append("<YFP_HM>"+invque.getIqyfphm()+"</YFP_HM>"); 
						//head.setYfphm(invque.getIqyfphm());//原发票号码
					}
					xml.append("<JSHJ>"+((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1)+"</JSHJ>"); 
					xml.append("<HJJE>"+(MathCal.add(invque.getIqtotje()*-1,0,2))+"</HJJE>"); 
					xml.append("<HJSE>"+(MathCal.add(invque.getIqtotse()*-1,0,2))+"</HJSE>"); 
/*					head.setHjje(MathCal.add(invque.getIqtotje()*-1,0,2));//合计金额
					head.setSe(MathCal.add(invque.getIqtotse()*-1,0,2));//合计税额
					head.setJshj((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1);//价税合计
*/				
				}else{
					xml.append("<YFP_DM>"+(invque.getIqyfpdm()==null?"":invque.getIqyfpdm())+"</YFP_DM>"); 
					xml.append("<YFP_HM>"+(invque.getIqyfphm()==null?"":invque.getIqyfphm())+"</YFP_HM>"); 
					
					xml.append("<JSHJ>"+((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2)))+"</JSHJ>"); 
					xml.append("<HJJE>"+(MathCal.add(invque.getIqtotje(),0,2))+"</HJJE>"); 
					xml.append("<HJSE>"+(MathCal.add(invque.getIqtotse(),0,2))+"</HJSE>"); 
/*					head.setHjje(MathCal.add(invque.getIqtotje(),0,2));//合计金额
					head.setSe(MathCal.add(invque.getIqtotse(),0,2));//合计税额
					head.setJshj(MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2));//价税合计
*/				}
				
				xml.append("<KCE></KCE>"); //扣除额
				//if("SD003".equals(invque.getIqentid())){
					xml.append("<BZ>"+(invque.getPaynote()==null?"":invque.getPaynote())+"</BZ>");
		
				//}else{
				//	xml.append("<BZ>"+(invque.getIqmemo()==null?"":invque.getIqmemo())+"</BZ>");
				//}
				
				//head.setBz(invque.getIqmemo());//备注
				
				xml.append("</COMMON_FPKJ_FPT>"+
							"<COMMON_FPKJ_XMXXS class=\"COMMON_FPKJ_XMXX\" size=\""+(detailList.size())+"\">"); 
				
				//开票明细转换
				int rownum = 1;
				//标记明细行是否为开票行是产生错误
				String flag = "N";
				for(InvoiceSaleDetail detail:detailList){
					xml.append("<COMMON_FPKJ_XMXX>"); 
					if(detail.getIsinvoice()==null||"".equals(detail.getIsinvoice())){
						flag ="Y";
						break;
					}else{
						if("Y".equals(detail.getIsinvoice())){
							xml.append("<FPHXZ>0</FPHXZ>");
							xml.append("<SPBM>"+(detail.getTaxitemid())+"</SPBM>");	
							xml.append("<ZXBM></ZXBM>");	
							xml.append("<YHZCBS>"+(detail.getTaxpre())+"</YHZCBS>");
							xml.append("<LSLBS>"+(detail.getZerotax()==null?"":detail.getZerotax())+"</LSLBS>");
							xml.append("<ZZSTSGL>"+(detail.getTaxprecon()==null?"":detail.getTaxprecon())+"</ZZSTSGL>");
							xml.append("<XMMC>"+(detail.getGoodsname())+"</XMMC>");
							xml.append("<GGXH>"+(detail.getSpec()==null?"":detail.getSpec())+"</GGXH>");
							xml.append("<DW>"+(detail.getUnit()==null?"":detail.getUnit())+"</DW>");
							if(invque.getIqtype()==1){
							xml.append("<XMSL>"+(detail.getQty()*-1)+"</XMSL>");
							}else{
							xml.append("<XMSL>"+(detail.getQty())+"</XMSL>");
							}
							xml.append("<XMDJ>"+(detail.getPrice())+"</XMDJ>");
							if(invque.getIqtype()==1){
								xml.append("<XMJE>"+(detail.getAmount()*-1)+"</XMJE>");
							}else{
								xml.append("<XMJE>"+(detail.getAmount())+"</XMJE>");	
							}
							xml.append("<SL>"+(detail.getTaxrate())+"</SL>");
							if(invque.getIqtype()==1){
								xml.append("<SE>"+(detail.getTaxfee()*-1)+"</SE>");
							}else{
								xml.append("<SE>"+(detail.getTaxfee())+"</SE>");
							}
						}
					}
					xml.append("</COMMON_FPKJ_XMXX>"); 
				}
				xml.append("</COMMON_FPKJ_XMXXS>"+
							"</REQUEST_COMMON_FPKJ>"+
							"</business>"); 
				
				if("Y".equals(flag)){
					rtn.setCode(-1);
					rtn.setMessage("开票明细是否可开票标记错误数据有问题");
					return;
				}
		 
				rtn.setMessage(xml.toString());
		
	}
	
	public void globalDzinterfaceXml(Invque invque,Taxinfo taxinfo,String xml,RtnData rtn)   throws Exception {
		
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
		
		if(xml ==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("开票报文为空！");
			return;
		}
		
		if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
			rtn.setCode(-1);
			rtn.setMessage("设备编号不能为空,taxinfo.Itfskpbh");
			return;
		} 
		
		if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			rtn.setCode(-1);
			rtn.setMessage("税号不能为空,taxinfo.getTaxno");
			return;
		} 
		
		if(taxinfo.getItfjrdm()==null||"".equals(taxinfo.getItfjrdm())){
			rtn.setCode(-1);
			rtn.setMessage("Access_Token不能为空,taxinfo.getItfjrdm");
			return;
		} 
		
		String globalxml = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
						"<business id=\"DZFPKJ\" comment=\"电子发票开具\">"+
						"<body yylxdm=\"1\">"+
						"<input>"+
						"<JQBH>"+(taxinfo.getItfskpbh())+"</JQBH>"+   //设备编号
						"<DIRECT>4</DIRECT>"+  //接口参数
						"<AppID>"+(taxinfo.getTaxno())+"</AppID>"+  //税号
						"<Access_Token>"+(taxinfo.getItfjrdm())+"</Access_Token>"+  //电子发票平台上的访问AppKey
						"<XSF_LXFS>0635-8216640</XSF_LXFS>"+   //销售方联系方式
						"<GMF_LXFS>"+(invque.getIqemail())+"</GMF_LXFS>"+   //购买方联系方式
						"<FPXML>"+(MyAES.encryptBASE64(xml.getBytes("UTF-8")))+"</FPXML>"+   //开票报文
						"<SKType>0</SKType>"+  //0表示税控服务器、1表示税控盘
						"<BussinessID>FPKJ</BussinessID>"+  //FPKJ表示发票开具；FPCX表示从税控服务器上获取已开信息，上传到电子发票平台；YKFP表示上传已开具的发票，用于非本税控服务器开具的电子发票。
						"</input>"+
						"</body>"+
						"</business>";
	//	System.out.println(globalxml);
		//System.out.println(xml);
		rtn.setMessage(MyAES.encryptBASE64(globalxml.getBytes("UTF-8")));
	}
	
	
	/**
	 * 开具发票返回后的发票数据解析
	 * **/

	public BwjfDzRtInvoiceBean rtDzOpenToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		GlobalDzinterfaceBean rtinvoicebean =new GlobalDzinterfaceBean();
		BwjfDzRtInvoiceBean rtOpenInvoiceBean = new BwjfDzRtInvoiceBean();
		 
		try{
		       JAXBContext jc = JAXBContext.newInstance( GlobalDzinterfaceBean.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer(xml);
		       rtinvoicebean = (GlobalDzinterfaceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
		       rtOpenInvoiceBean = rtinvoicebean.getRtbean();
		      
		       if(rtOpenInvoiceBean==null){
		    	   rtOpenInvoiceBean = rtinvoicebean.getRterrorbean();
		    	   rtn.setCode(-1);
		    	   rtn.setMessage(rtOpenInvoiceBean.getReturnmsg());
		    	   return null;
		       }
		       //System.out.println(rtinvoicebean.getRtbean().getFp_hm() );
		       if(rtOpenInvoiceBean.getReturncode()==null||!"0000".equals(rtOpenInvoiceBean.getReturncode())){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage(rtOpenInvoiceBean.getReturnmsg());
		    	   return rtOpenInvoiceBean;
		       }
		    	
			   
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	
		return rtOpenInvoiceBean;
	}
	
	
	/**
	 * 开具发票返回后的发票数据解析
	 * **/

	public BwjfRtInvoiceBean rtZpOpenToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("发票正在开具中,请耐心登陆或稍后再试");
			return null;
		}
		GlobalZpinterfaceBean rtinvoicebean =new GlobalZpinterfaceBean();
		BwjfRtInvoiceBean rtOpenInvoiceBean = new BwjfRtInvoiceBean();
		 
		try{
		       JAXBContext jc = JAXBContext.newInstance( GlobalZpinterfaceBean.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer(xml);
		       rtinvoicebean = (GlobalZpinterfaceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
		       if(rtinvoicebean.getId()==null) rtinvoicebean.setId("");
		       if("10010".equals(rtinvoicebean.getId())){
		    	   rtOpenInvoiceBean.setEwm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getEwm());
		    	   rtOpenInvoiceBean.setFpdm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getFpdm());
		    	   rtOpenInvoiceBean.setFphm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getFphm());
		    	   //rtOpenInvoiceBean.setJym(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().get);
		    	   rtOpenInvoiceBean.setKprq(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getKprq());
		    	   rtOpenInvoiceBean.setSkm(rtinvoicebean.getBody().getRtBean().getKpxx().getGroup().getSkm());
		       }else{
		       rtOpenInvoiceBean = rtinvoicebean.getBody().getRtBean();
		       }
		      
		       if(rtOpenInvoiceBean==null){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage("解析发票返回的数据有问题！");
		    	   return null;
		       }
		       System.out.println(rtOpenInvoiceBean.getFphm() );
		       if(rtinvoicebean.getBody().getReturncode()==null||!"0".equals(rtinvoicebean.getBody().getReturncode())){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage(rtinvoicebean.getBody().getReturnmsg());
		    	   return rtOpenInvoiceBean;
		       }
		    	
			   
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	
		return rtOpenInvoiceBean;
	}
	
	/**
	 * 获取pdf连接参数转换
	 * **/
	
	public void generatePdf(Invque invque,Taxinfo taxinfo,RtnData rtn)  throws Exception{
		//判断邮箱电话是否都填写

		StringBuffer str = new StringBuffer();
		
		str.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
					"<business id=\"FPCFDZDZPT\">"+
					"<body>"+
					"<input>"+
					"<fpxx count=\"1\">"+
					"<group xh=\"1\">");
		
		if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票代码不能为空");
			return;
		}else{
			str.append("<fpdm>" + invque.getRtfpdm() + "</fpdm>");
		}
		
		if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
			rtn.setCode(-1);
			rtn.setMessage("发票号码不能为空");
			return;
		}else{
			str.append("<fphm>" + invque.getRtfphm() + "</fphm>");
		}
		if("1".equals(invque.getIqtype())){
			str.append("<jshj>" + (MathCal.add(invque.getIqtotje(),invque.getIqtotse()*-1,2)) + "</jshj>");
		}else{
			str.append("<jshj>" + (MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2)) + "</jshj>");
		}
		
		if(invque.getRtkprq()==null||"".equals(invque.getRtkprq())){
			rtn.setCode(-1);
			rtn.setMessage("发票日期不能为空");
			return;
		}else{
			str.append("<kprq>" + invque.getRtkprq() + "</kprq>");
		}
		
		str.append("</group></fpxx>"+
					"</input>"+
					"</body>"+
					"</business>");
		 
		
		String globalxml = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+
							"<business id=\"DZFPXZ\" comment=\"电子发票下载\">"+
							"<body yylxdm=\"1\">"+
							"<input>"+
							"<NSRSBH>"+(taxinfo.getTaxno())+"</NSRSBH>"+
							"<XSF_MC>"+(taxinfo.getTaxname())+"</XSF_MC>"+
							"<JQBH>"+(taxinfo.getItfskpbh())+"</JQBH>"+
							"<DIRECT>3</DIRECT>"+
							"<FPXML>"+(MyAES.encryptBASE64(str.toString().getBytes("UTF-8")))+"</FPXML>"+
							"</input>"+
							"</body>"+
							"</business>";

		rtn.setMessage(MyAES.encryptBASE64(globalxml.getBytes("UTF-8")));
		System.out.println(globalxml);
	}
	
	public String rtPdfToString(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		GlobalDzinterfaceBean rtinvoicebean =new GlobalDzinterfaceBean();
		BwjfDzRtpdf_bodyBean rtpdf = new BwjfDzRtpdf_bodyBean();
		try{
		       JAXBContext jc = JAXBContext.newInstance( GlobalDzinterfaceBean.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer(xml);
		       rtinvoicebean = (GlobalDzinterfaceBean) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
		       rtpdf = rtinvoicebean.getBodybean() ;
		      
		       if(rtpdf==null){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage("解析发票返回的数据有问题！");
		    	   return null;
		       }
		      // System.out.println(rtinvoicebean.getBodybean().getFpxxbean().getGroupbean().getUrl() );
		       if(rtpdf.getReturncode()==null||!"0000".equals(rtpdf.getReturncode())){
		    	   rtn.setCode(-1);
		    	   rtn.setMessage(rtpdf.getReturnmsg());
		    	 //  System.out.println(rtpdf.getReturncode()+" "+rtpdf.getReturnmsg());
		    	   return rtpdf.getFpxxbean().getGroupbean().getUrl();
		       }
		    	
			   
		}catch(Exception e){
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
		
		/*String rtPdf ="";
		XmlUtil util= new XmlUtil();  
		Map map= util.xmlToMap(xml);
		if(map==null||map.size()==0){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回数据转换对象时错误");
			return null;
		}
		System.out.println(map.get("returncode"));
		
		if("0".equals(map.get("returncode"))){
			rtPdf = (String)map.get("url");
		}else{
			rtn.setCode(-1);
			rtn.setMessage((String)map.get("returnmsg"));
			return null;
		}*/
		return rtinvoicebean.getBodybean().getFpxxbean().getGroupbean().getUrl();
		
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
	public void zp_print(Invque invque, Taxinfo taxinfo,RtnData rtn){
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

		xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"20004\" comment=\"发票打印\"><body yylxdm=\"1\">");
		
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
		
		if(invque.getIsList()==null){
			rtn.setCode(-1);
			rtn.setMessage("打印类型不能为空");
			return;
		}else{
			xml.append("<dylx>"+ invque.getIsList() +"</dylx>");//发票类型代码
			 
		}
		
		xml.append("<dyfs>1</dyfs>");//发票类型代码
		xml.append("<printername></printername>");//发票类型代码
		xml.append("</body></business>");
		invque.setIqmsg(xml.toString());
		//rtn.setMessage(xml.toString());
		
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
		invque.setIqmsg(xml.toString());
		//rtn.setMessage(xml.toString());
		
	}
	
	/**
	 *  
	 * **/
	public void urlconnnet(Invque invque,Taxinfo taxinfo,RtnData rtn){
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
		
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return;
		}
		
		xml.append("<?xml version=\"1.0\" encoding=\"gbk\"?><business id=\"20001\" comment=\"参数设置\"><body yylxdm=\"1\">");
		
		if(taxinfo.getItfkpd()==null||"".equals(taxinfo.getItfkpd())){
			rtn.setCode(-1);
			rtn.setMessage("连接服务器的IP不能为空");
			return;
		}else{
			xml.append("<servletip>"+ taxinfo.getItfkpd() +"</servletip>");//开票终端标识
		}
		
		if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
			rtn.setCode(-1);
			rtn.setMessage("连接服务器的端口号不能为空");
			return;
		}else{
			xml.append("<servletport>"+ taxinfo.getItfskpkl() +"</servletport>");//发票类型代码
		}
		
		if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
			rtn.setCode(-1);
			rtn.setMessage("连接服务器的税控钥匙口令不能为空");
			return;
		}else{
			xml.append("<keypwd>"+ taxinfo.getItfkeypwd() +"</keypwd>");//发票类型代码
		}
		
		xml.append("</body></business>");
		
		rtn.setMessage(xml.toString());
	}
	

	
}
