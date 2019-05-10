package com.invoice.port.zjaxn.invoice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.zjaxn.invoice.bean.InvoiceDetailBean;
import com.invoice.port.zjaxn.invoice.bean.InvoiceHeadBean;
import com.invoice.port.zjaxn.invoice.bean.RtInvoiceHeadBean;
import com.invoice.port.zjaxn.invoice.bean.RtInvoiceInterfaceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;

public class ZjaxnGenerateBean {
	
	/**
	 * 开具发票 装换为航信接口对象
	 * **/
	public void generateBean(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList ,
			RtnData rtn) {
		// 对象定义和初始化
		/**
		 * 对航信数据对象定义 head 开票头 bwDetailList 开票明细集合
		 * **/
		InvoiceHeadBean salehead = new InvoiceHeadBean();
		List<InvoiceDetailBean> ordddetaillist =  new ArrayList<InvoiceDetailBean>();  
 

		/**
		 * 计算后小票数据初始化 invque 队列数据 taxinfo 企业纳税号信息 detailList 小票数据集合
		 * **/
		// Invque invque = (Invque) map.get("invque");
		if (invque == null) {
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}
		// Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if (taxinfo == null) {
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return;
		}
		// List<InvoiceSaleDetail> detailList= (ArrayList)map.get("detailList");
		if (detailList == null) {
			rtn.setCode(-1);
			rtn.setMessage("小票数据集合 detailList 没有初始化");
			return;
		}

		if (invque.getIqgmfname() == null || "".equals(invque.getIqgmfname())) {
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		} else {
			salehead.setBuyername(invque.getIqgmfname());// 购方客户名称
		}
		
		salehead.setTaxnum(invque.getIqgmftax() == null ? "" : invque.getIqgmftax());// 购方单位代码
		if(invque.getIqtel()==null||"".equals(invque.getIqtel())){
			salehead.setPhone("17700000000");
		}else{
			salehead.setPhone(invque.getIqtel());
		}

/*		if (invque.getIqtel() == null || "".equals(invque.getIqtel())) {
			rtn.setCode(-1);
			rtn.setMessage("购方手机号不能为空");
			return;
		} else {
			salehead.setGhf_sj(invque.getIqtel());
		}*/
		salehead.setAddress(invque.getIqgmfadd() == null ? "" : invque.getIqgmfadd());// 购方地址及电话
		salehead.setAccount(invque.getIqgmfbank()== null ? "" : invque.getIqgmfbank());// 购方开户行及账号
		salehead.setTelephone("");
		
		if (invque.getIqseqno() == null || "".equals(invque.getIqseqno())) {
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		} else {
			salehead.setOrderno(invque.getIqseqno());// 发票流水号
			 
		}
		java.text.SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		salehead.setInvoicedate(df1.format(new Date()));
		
		if (invque.getIqadmin() == null || "".equals(invque.getIqadmin())) {
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		} else {
			salehead.setClerk(invque.getIqadmin());// 开票人
		}
		
		salehead.setSaleaccount(taxinfo.getTaxbank() == null ? "" : taxinfo.getTaxbank());
		salehead.setSalephone(taxinfo.getItfskpbh());
		if (taxinfo.getTaxadd() == null || "".equals(taxinfo.getTaxadd())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方地址不能为空");
			return;
		} else {
			salehead.setSaleaddress(taxinfo.getTaxadd());//销售方地址
		}
 
		if (taxinfo.getTaxno() == null || "".equals(taxinfo.getTaxno())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方纳税人识别号不能为空");
			return;
		} else {
			salehead.setSaletaxnum(taxinfo.getTaxno());//销售方纳税人识别号
		}
		
 
/*		if (taxinfo.getTaxname() == null || "".equals(taxinfo.getTaxname())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方名称不能为空");
			return;
		} else {
			salehead.setXhfmc(taxinfo.getTaxname());//销售方名称
		}
		*/
		
		if (invque.getIqtype() == 0 || invque.getIqtype() == 1) {
			if(invque.getIqtype() == 0){
				salehead.setKptype("1");// 开票类型
			}if(invque.getIqtype() == 1){
				salehead.setKptype("2");// 开票类型
			}
			
		} else {
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}
		
		if(invque.getIqtype() == 1){
			 
			salehead.setMessage("对应正数发票代码:"+invque.getIqyfpdm()+"号码:"+invque.getIqyfphm()+(invque.getIqmemo() == null ? "" : invque.getIqmemo()));// 备注
		}else{
			 
		salehead.setMessage(invque.getIqmemo() == null ? "" : invque.getIqmemo());// 备注
		}
		
		salehead.setPayee(invque.getIqpayee()==null?"":invque.getIqpayee());// 收款人
		salehead.setChecker(invque.getIqchecker()==null?"":invque.getIqchecker());// 审核人
		
		if (invque.getIqtype() == 1) {

			if (invque.getIqyfpdm() == null || "".equals(invque.getIqyfpdm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			} else {
				salehead.setFpdm(invque.getIqyfpdm());// 原发票代码
			}

			if (invque.getIqyfphm() == null || "".equals(invque.getIqyfphm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			} else {
				salehead.setFphm(invque.getIqyfphm());// 原发票号码
			}
			
/*			salehead.setKphjje(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1));// 价税合计
			salehead.setHjbhsje(String.valueOf((MathCal.add(invque.getIqtotje(),0,2)*-1)));// 合计金额
			salehead.setHjse( String.valueOf(MathCal.add(invque.getIqtotse(),0,2)*-1));// 合计税额
*/			
		} else {
			salehead.setFpdm("");// 原发票代码
			salehead.setFphm("");// 原发票号码
/*			salehead.setKphjje(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))));// 价税合计
			salehead.setHjbhsje(String.valueOf((MathCal.add(invque.getIqtotje(),0,2))));// 合计金额
			salehead.setHjse( String.valueOf(MathCal.add(invque.getIqtotse(),0,2)));// 合计税额
*/
		}
		
		salehead.setTsfs("0");
		salehead.setEmail(invque.getIqemail());
		salehead.setQdbz("0");
		salehead.setQdxmmc("");
		salehead.setDkbz("0");
		salehead.setDeptid("");
		salehead.setClerkid("");
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		}else{
			if("026".equals(invque.getIqfplxdm())){
				salehead.setInvoiceLine("p");//发票种类编码
			}else
			if("007".equals(invque.getIqfplxdm())){
				salehead.setInvoiceLine("c");//发票种类编码
			}else
			if("004".equals(invque.getIqfplxdm())){
				salehead.setInvoiceLine("s");//发票种类编码
			}
			
		}
		salehead.setCpybz("0");
	 

		// 开票明细转换
		// 标记明细行是否为开票行是产生错误
		String flag = "N";
		List<InvoiceDetailBean> saledetailList = new ArrayList<InvoiceDetailBean>();
		if(invque.getIqtype()==0){
			for (InvoiceSaleDetail detail : detailList) {
				if (detail.getIsinvoice() == null || "".equals(detail.getIsinvoice())) {
					flag = "Y";
					break;
				} else {
					if ("Y".equals(detail.getIsinvoice())) {
						InvoiceDetailBean bwdetail = new InvoiceDetailBean();
						
						bwdetail.setGoodsname(detail.getGoodsname() == null ? "" : detail.getGoodsname());// 商品名称
						bwdetail.setNum("null".equals(String.valueOf(detail.getQty())) ? "" : String.valueOf(MathCal.add(detail
								.getQty(),0,8)));// 商品数量
						
						bwdetail.setPrice("null".equals(String.valueOf(detail.getPrice())) ? "" : String.valueOf(MathCal.add(detail
								.getAmount()/detail.getQty(),0,8))); 
						bwdetail.setHsbz("0");// 含税标志
						
						bwdetail.setTaxrate("null".equals(String.valueOf(detail.getTaxrate())) ? "" : String.valueOf(detail
								.getTaxrate()));// 税率
						bwdetail.setSpec(detail.getSpec()==null?"":detail.getSpec());//规格型号
						
						bwdetail.setUnit(detail.getUnit() == null ? "" : detail.getUnit());// 计量单位

						bwdetail.setSpbm(detail.getTaxitemid() == null ? "" : detail.getTaxitemid());// 商品税目
						bwdetail.setZsbm(detail.getGoodsid() == null ? "" : detail.getGoodsid());// 商品编码
						
						 if(detail.getFphxz()==null) detail.setFphxz("");
						bwdetail.setFphxz("".equals(detail.getFphxz())?"0":detail.getFphxz());// 发票行性质
						

						bwdetail.setYhzcbs(detail.getTaxpre()==null?"0":detail.getTaxpre());// 是否使用优惠政策
						bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
						bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());// 增值税特殊管理
						bwdetail.setKce("");
						
						bwdetail.setTaxfreeamt("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(MathCal.add(detail
								.getAmount(),0,2)));// 金额
						
						

						bwdetail.setTax("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(MathCal.add(detail
								.getTaxfee(),0,2)));// 税额
						bwdetail.setTaxamt(String.valueOf(MathCal.add(detail.getTaxfee(),detail.getAmount(),2)));

						saledetailList.add(bwdetail);
					}
				}
			}

		}else
		if(invque.getIqtype()==1){
			
			for (InvoiceSaleDetail detail : detailList) {
				if (detail.getIsinvoice() == null || "".equals(detail.getIsinvoice())) {
					flag = "Y";
					break;
				} else {
					if ("Y".equals(detail.getIsinvoice())) {
						
						InvoiceDetailBean bwdetail = new InvoiceDetailBean();
						if(detail.getFphxz()==null) detail.setFphxz("");
						bwdetail.setFphxz("".equals(detail.getFphxz())?"0":detail.getFphxz());// 发票行性质
						
						if(!"1".equals(detail.getFphxz())){
							
							
							
							bwdetail.setGoodsname(detail.getGoodsname() == null ? "" : detail.getGoodsname());// 商品名称
							
							bwdetail.setHsbz("0");// 含税标志
							
							bwdetail.setTaxrate("null".equals(String.valueOf(detail.getTaxrate())) ? "" : String.valueOf(detail
									.getTaxrate()));// 税率
							bwdetail.setSpec(detail.getSpec()==null?"":detail.getSpec());//规格型号
							
							bwdetail.setUnit(detail.getUnit() == null ? "" : detail.getUnit());// 计量单位

							bwdetail.setSpbm(detail.getTaxitemid() == null ? "" : detail.getTaxitemid());// 商品税目
							bwdetail.setZsbm(detail.getGoodsid() == null ? "" : detail.getGoodsid());// 商品编码
							
							 if(detail.getFphxz()==null) detail.setFphxz("");
							bwdetail.setFphxz("".equals(detail.getFphxz())?"0":detail.getFphxz());// 发票行性质
							

							bwdetail.setYhzcbs(detail.getTaxpre()==null?"0":detail.getTaxpre());// 是否使用优惠政策
							bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
							bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());// 增值税特殊管理
							bwdetail.setKce("");

							if("2".equals(detail.getFphxz())){
								for(InvoiceSaleDetail detail1:detailList){
									if("1".equals(detail1.getFphxz())){
										if(detail.getGoodsid().equals(detail1.getGoodsid())&&detail.getRowno()==detail1.getZhdyhh()){
											double amount =  MathCal.add(detail.getAmount(),detail1.getAmount(),2);
											double taxfee = MathCal.add(detail.getTaxfee(),detail1.getTaxfee(),2);
											bwdetail.setTaxfreeamt("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(amount*-1));// 金额									

											bwdetail.setTax("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(taxfee*-1));// 税额
											bwdetail.setTaxamt(String.valueOf(MathCal.add(amount,taxfee,2)*-1));
											break;
										}
									}
								}
							 
								bwdetail.setNum("");// 商品数量
								bwdetail.setPrice(""); 
							}else{
								bwdetail.setTaxfreeamt("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(MathCal.add(detail
										.getAmount(),0,2)*-1));// 金额
								
								bwdetail.setTax("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(MathCal.add(detail
										.getTaxfee(),0,2)*-1));// 税额
								
								bwdetail.setTaxamt(String.valueOf(MathCal.add(detail.getTaxfee(),detail.getAmount(),2)*-1));
								
								if(detail.getQty()>0){
								double qty = MathCal.add(detail.getQty(),0,8)*-1;
								
								bwdetail.setNum("null".equals(String.valueOf(detail.getQty())) ? "" : String.valueOf(MathCal.add(qty,0,8)));// 商品数量
								
								bwdetail.setPrice("null".equals(String.valueOf(detail.getPrice())) ? "" : String.valueOf(MathCal.add(detail
										.getAmount()*-1/qty,0,8))); 
								}
							}

							saledetailList.add(bwdetail);
						}
					}
				}
			}
			
		}
		
		salehead.setDetail(saledetailList);
		
		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return;
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("identity", taxinfo.getItfjrdm());
		map.put("order", salehead);
		rtn.setMessage(JSONObject.toJSONString(map));
		System.out.println(JSONObject.toJSONString(map));
 
	}
	
	public RtInvoiceHeadBean rtOpenToBean(String jsonBean,RtnData rtn){
		RtInvoiceHeadBean invoiceHeadBean  = new RtInvoiceHeadBean();
			//String json = "{\"result\":\"success\",\"list\":[{\"c_status\":\"2\",\"c_fpdm\":\"140110930111\",\"c_kprq\":1501507173000,\"c_bhsje\":0.09,\"c_orderno\":\"20160108165823395151\",\"c_invoiceid\":\"17073120233601285880\",\"c_msg\":\"已开票\",\"c_fpqqlsh\":\"17073120233601285880\",\"c_fphm\":\"10030133\",\"c_resultmsg\":\"\",\"c_url\":\"https://inv.jss.com.cn/group1/M00/A6/4C/wKgHPll_IU-AVELfAACNq5bmzFM769.pdf\",\"c_jym\":\"70598593833665063630\",\"c_jpg_url\":\"nnfp.jss.com.cn/h6Oj0Dn?p=Kh4Nj\",\"c_hjse\":0.01, \"c_buyername\": \"人个\",\"c_taxnum\": \"\"}]}";
			RtInvoiceInterfaceBean p = JSONObject.parseObject(jsonBean, RtInvoiceInterfaceBean.class);
			if("success".equals(p.getResult())){
				if(p.getList().size()>0){
					invoiceHeadBean = p.getList().get(0);
				}
			}else{
				rtn.setCode(-1);
				rtn.setMessage(p.getErrorMsg());
				return null;
			}
			return invoiceHeadBean;
	}
	
}
