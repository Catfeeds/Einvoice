package com.invoice.port.hnhangxin.invoice.service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aisino.CaConstant;
import com.aisino.PKCS7;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInterfaceBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceOrdDetailBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceOrdDetaillist;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceOrdHeadBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceOrdPayBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceSaleDetailBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceSaleDetailList;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceSaleHeadBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinInvoiceWlBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinRtInterfaceBean;
import com.invoice.port.hnhangxin.invoice.bean.HnHangXinRtInvoiceBean;
import com.invoice.port.hnhangxin.invoice.util.GZipUtils;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.FileUtil;
import com.invoice.util.MathCal;
import com.invoice.util.SHA1;
import com.invoice.util.XMLConverter;

import hangxin.invoice.bean.HangXinInvoiceSaleHeadBean;
import hangxin.invoice.bean.HangXinQzDetaiBean;
import hangxin.invoice.bean.HangXinQzDetaiListBean;
import hangxin.invoice.bean.HangXinQzFptBean;
import hangxin.invoice.bean.HangXinQzInvoiceHeadBean;

public class HnHangXinGenerateBean {
	private final Log log = LogFactory.getLog(HnHangXinGenerateBean.class);
	static private Map<String,PKCS7> pkcs7Map = new HashMap<String,PKCS7>();
	
	synchronized private PKCS7 getPkcs7Map(String nsrsbh) throws Exception {
		PKCS7 pkcs7Client = pkcs7Map.get(nsrsbh);
		if(pkcs7Client==null){
			String fix = FileUtil.getRunPath() + File.separator + "CA" + File.separator + nsrsbh + File.separator;
			String trusts = fix + ca.getProperty("PUBLIC_TRUSTS");
			String decryptPFXKey = ca.getProperty("CLIENT_DECRYPTPFX_KEY"); // "0000000000";
			String decryptPFX = fix + ca.getProperty("CLIENT_DECRYPTPFX");// prefixDir+
			String dll = fix + ca.getProperty("DLLADDRESS");
			
			File file = new File(trusts);
			if(!file.exists()){
				throw new Exception("PUBLIC_TRUSTS File not exists "+trusts);
			}
			file = new File(decryptPFX);
			if(!file.exists()){
				throw new Exception("CLIENT_DECRYPTPFX File not exists "+decryptPFX);
			}
			file = new File(dll);
			if(!file.exists()){
				throw new Exception("DLLADDRESS File not exists "+dll);
			}
			
			pkcs7Client = new PKCS7(FileUtils.readFileToByteArray(new File(trusts)),FileUtils.readFileToByteArray(new File(decryptPFX)), decryptPFXKey, dll);
		}

		return pkcs7Client;
	}
	

	// 纳税人识别号
	String nsrsbh;
	// 数据交换流水号
	String dataExchangeId;

	Properties ca;

	public HnHangXinGenerateBean(String nsrsbh) {
		this.nsrsbh = nsrsbh;
		ca = CaConstant.get(nsrsbh);
	}

	/**
	 * 开具发票 装换为航信接口对象
	 * **/
	public void generateHangXinBean(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,String fwdm,String encryptCode ,
			RtnData rtn) {
		// 对象定义和初始化
		/**
		 * 对航信数据对象定义 head 开票头 bwDetailList 开票明细集合
		 * **/
		HnHangXinInterfaceBean inter = new HnHangXinInterfaceBean();
		HnHangXinInvoiceSaleHeadBean salehead = new HnHangXinInvoiceSaleHeadBean();
		HnHangXinInvoiceSaleDetailList saleDetaillist =new HnHangXinInvoiceSaleDetailList();
		HnHangXinInvoiceOrdHeadBean ordhead = new HnHangXinInvoiceOrdHeadBean();
		HnHangXinInvoiceOrdDetailBean orddetail = new HnHangXinInvoiceOrdDetailBean();
		HnHangXinInvoiceOrdDetaillist orddetaillist = new HnHangXinInvoiceOrdDetaillist();
		List<HnHangXinInvoiceOrdDetailBean> ordddetaillist =  new ArrayList<HnHangXinInvoiceOrdDetailBean>();  
		HnHangXinInvoiceOrdPayBean ordpay = new HnHangXinInvoiceOrdPayBean();
		HnHangXinInvoiceWlBean wl = new HnHangXinInvoiceWlBean();

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

		if (invque.getIqseqno() == null || "".equals(invque.getIqseqno())) {
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		} else {
			salehead.setFpqqlsh(invque.getIqseqno());// 发票流水号
			dataExchangeId = invque.getIqseqno();
		}
		
		if (taxinfo.getItfskpbh() == null || "".equals(taxinfo.getItfskpbh())) {
			rtn.setCode(-1);
			rtn.setMessage("平台编码不能为空");
			return;
		} else {
			salehead.setDsptbm(taxinfo.getItfskpbh()); //必填
		}
	
		
		if (taxinfo.getTaxno() == null || "".equals(taxinfo.getTaxno())) {
			rtn.setCode(-1);
			rtn.setMessage("开票方识别号不能为空");
			return;
		} else {
			salehead.setNsrsbh(taxinfo.getTaxno());// 开票方识别号
		}
		
		if (taxinfo.getTaxname() == null || "".equals(taxinfo.getTaxname())) {
			rtn.setCode(-1);
			rtn.setMessage("开票方名称不能为空");
			return;
		} else {
			salehead.setNsrmc(taxinfo.getTaxname());// 开票方名称
		}
		
		salehead.setNsrdzdah("");//开票方电子档案号
		salehead.setSwjg_dm("");//税务机构代码
		salehead.setDkbz("0");//代开标志  默认0  税号为代开税号必须填1
		salehead.setSgbz("");//收购标志
		salehead.setPydm("");//票样代码
		
		for(InvoiceSaleDetail detail: detailList){
			salehead.setKpxm(detail.getGoodsname());
			break;
		}
		
		salehead.setBmb_bbh(taxinfo.getItfbbh() == null ? "" : taxinfo.getItfbbh());// 商品编码版本号
		
		if (taxinfo.getTaxno() == null || "".equals(taxinfo.getTaxno())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方纳税人识别号不能为空");
			return;
		} else {
			salehead.setXhf_nsrsbh(taxinfo.getTaxno());//销售方纳税人识别号
		}
		
		if (taxinfo.getTaxname() == null || "".equals(taxinfo.getTaxname())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方名称不能为空");
			return;
		} else {
			salehead.setXhfmc(taxinfo.getTaxname());//销售方名称
		}
		
		if (taxinfo.getTaxadd() == null || "".equals(taxinfo.getTaxadd())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方地址不能为空");
			return;
		} else {
			salehead.setXhf_dz(taxinfo.getTaxadd());//销售方地址
		}
		salehead.setXhf_dh("");
		
		salehead.setXhf_yhzh(taxinfo.getTaxbank() == null ? "" : taxinfo.getTaxbank());
		
		if (invque.getIqgmfname() == null || "".equals(invque.getIqgmfname())) {
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		} else {
			salehead.setGhfmc(invque.getIqgmfname());// 购方客户名称
		}
		
		

		salehead.setGhf_nsrsbh(invque.getIqgmftax() == null ? "" : invque.getIqgmftax());// 购方单位代码
		salehead.setGhf_dz(invque.getIqgmfadd() == null ? "" : invque.getIqgmfadd());// 购方开户行及账号
		salehead.setGhf_gddh("");// 购方地址及电话
		salehead.setGhf_sf("");
		
/*		if (invque.getIqtel() == null || "".equals(invque.getIqtel())) {
			rtn.setCode(-1);
			rtn.setMessage("购方手机号不能为空");
			return;
		} else {
			salehead.setGhf_sj(invque.getIqtel());
		}*/
		salehead.setGhf_sj(invque.getIqtel() == null?"":invque.getIqtel());
		salehead.setGhf_email(invque.getIqemail()==null?"":invque.getIqemail());
		
		if(invque.getIqgmftax()==null||"".equals(invque.getIqgmftax())){
			salehead.setGhfqylx("03");
		}else{
			salehead.setGhfqylx("01");
		}
		
		salehead.setGhf_yhzh(invque.getIqgmfbank() == null ? "" : invque.getIqgmfbank());// 购方开户行及账号
		
		salehead.setHy_dm("");
		salehead.setHy_mc("");
		
		if (invque.getIqadmin() == null || "".equals(invque.getIqadmin())) {
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		} else {
			salehead.setKpy(invque.getIqadmin());// 开票人
		}
		salehead.setSky(invque.getIqpayee()==null?"":invque.getIqpayee());// 收款人
		salehead.setFhr(invque.getIqchecker()==null?"":invque.getIqchecker());// 审核人
		
		salehead.setKprq("");
		
		if (invque.getIqtype() == 0 || invque.getIqtype() == 1) {
			if(invque.getIqtype() == 0){
				salehead.setKplx("1");// 开票类型
			}if(invque.getIqtype() == 1){
				salehead.setKplx("2");// 开票类型
			}
			
		} else {
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}
 

		if (invque.getIqtype() == 1) {

			if (invque.getIqyfpdm() == null || "".equals(invque.getIqyfpdm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			} else {
				salehead.setYfp_dm(invque.getIqyfpdm());// 原发票代码
			}

			if (invque.getIqyfphm() == null || "".equals(invque.getIqyfphm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			} else {
				salehead.setYfp_hm(invque.getIqyfphm());// 原发票号码
			}
			
			salehead.setKphjje(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1));// 价税合计
			salehead.setHjbhsje(String.valueOf((MathCal.add(invque.getIqtotje(),0,2)*-1)));// 合计金额
			salehead.setHjse( String.valueOf(MathCal.add(invque.getIqtotse(),0,2)*-1));// 合计税额
			
		} else {
			salehead.setYfp_dm("");// 原发票代码
			salehead.setYfp_hm("");// 原发票号码
			salehead.setKphjje(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))));// 价税合计
			salehead.setHjbhsje(String.valueOf((MathCal.add(invque.getIqtotje(),0,2))));// 合计金额
			salehead.setHjse( String.valueOf(MathCal.add(invque.getIqtotse(),0,2)));// 合计税额

		}
		salehead.setTschbz("0");
		salehead.setCzdm("10");
		salehead.setQd_bz("0");
		salehead.setQdxmmc("");
		
		if(invque.getIqtype() == 1){
			salehead.setChyy("退货");
			salehead.setBz("对应正数发票代码:"+salehead.getYfp_dm()+"号码:"+salehead.getYfp_hm()+(invque.getIqmemo() == null ? "" : invque.getIqmemo()));// 备注
		}else{
			salehead.setChyy("");
		salehead.setBz(invque.getIqmemo() == null ? "" : invque.getIqmemo());// 备注
		}
		salehead.setByzd1("");
		salehead.setByzd2("");
		salehead.setByzd3("");
		salehead.setByzd4("");
		salehead.setByzd5("");

		// 开票明细转换
		// 标记明细行是否为开票行是产生错误
		String flag = "N";
		List<HnHangXinInvoiceSaleDetailBean> saledetailList = new ArrayList<HnHangXinInvoiceSaleDetailBean>();
		if(invque.getIqtype()==0){
			for (InvoiceSaleDetail detail : detailList) {
				if (detail.getIsinvoice() == null || "".equals(detail.getIsinvoice())) {
					flag = "Y";
					break;
				} else {
					if ("Y".equals(detail.getIsinvoice())) {
						HnHangXinInvoiceSaleDetailBean bwdetail = new HnHangXinInvoiceSaleDetailBean();
						bwdetail.setXmmc(detail.getGoodsname() == null ? "" : detail.getGoodsname());// 商品名称
						bwdetail.setXmdw(detail.getUnit() == null ? "" : detail.getUnit());// 计量单位
						bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
						
						bwdetail.setXmsl("null".equals(String.valueOf(detail.getQty())) ? "" : String.valueOf(MathCal.add(detail
								.getQty(),0,8)));// 商品数量
						
						bwdetail.setHsbz("1");// 含税标志
						
						 if(detail.getFphxz()==null) detail.setFphxz("");
						bwdetail.setFphxz("".equals(detail.getFphxz())?"0":detail.getFphxz());// 发票行性质
						
						bwdetail.setXmdj("null".equals(String.valueOf(detail.getPrice())) ? "" : String.valueOf(MathCal.add(detail
								.getAmt()/detail.getQty(),0,8))); 
						
						bwdetail.setSpbm(detail.getTaxitemid() == null ? "" : detail.getTaxitemid());// 商品税目
						bwdetail.setZxbm(detail.getGoodsid() == null ? "" : detail.getGoodsid());// 商品编码
						
						bwdetail.setYhzcbs(detail.getTaxpre()==null?"0":detail.getTaxpre());// 是否使用优惠政策
						bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
						bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());// 增值税特殊管理
						bwdetail.setKce("");
						
						bwdetail.setXmje("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(MathCal.add(detail
								.getAmt(),0,2)));// 金额
						
						bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate())) ? "" : String.valueOf(detail
								.getTaxrate()));// 税率

						bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(MathCal.add(detail
								.getTaxfee(),0,2)));// 税额
						bwdetail.setByzd1("");
						bwdetail.setByzd2("");
						bwdetail.setByzd3("");
						bwdetail.setByzd4("");
						bwdetail.setByzd5("");

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
						
						HnHangXinInvoiceSaleDetailBean bwdetail = new HnHangXinInvoiceSaleDetailBean();
						if(detail.getFphxz()==null) detail.setFphxz("");
						bwdetail.setFphxz("".equals(detail.getFphxz())?"0":detail.getFphxz());// 发票行性质
						
						if(!"1".equals(detail.getFphxz())){
							
							bwdetail.setXmmc(detail.getGoodsname() == null ? "" : detail.getGoodsname());// 商品名称
							bwdetail.setXmdw(detail.getUnit() == null ? "" : detail.getUnit());// 计量单位
							bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
							bwdetail.setHsbz("1");// 含税标志
							bwdetail.setSpbm(detail.getTaxitemid() == null ? "" : detail.getTaxitemid());// 商品税目
							bwdetail.setZxbm(detail.getGoodsid() == null ? "" : detail.getGoodsid());// 商品编码
							bwdetail.setYhzcbs(detail.getTaxpre()==null?"0":detail.getTaxpre());// 是否使用优惠政策
							bwdetail.setLslbs(detail.getZerotax()==null?"":detail.getZerotax());
							bwdetail.setZzstsgl(detail.getTaxprecon()==null?"":detail.getTaxprecon());// 增值税特殊管理
							
							
							if("2".equals(detail.getFphxz())){
								for(InvoiceSaleDetail detail1:detailList){
									if("1".equals(detail1.getFphxz())){
										if(detail.getGoodsid().equals(detail1.getGoodsid())&&detail.getRowno()==detail1.getZhdyhh()){
											bwdetail.setXmje("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(MathCal.add(detail
													.getAmt(),detail1.getAmt(),2)*-1));// 金额
											
											bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(MathCal.add(detail
													.getTaxfee(),detail1.getTaxfee(),2)*-1));// 税额 
											break;
										}
									}
								}
							 
								bwdetail.setXmsl("");// 商品数量
								bwdetail.setXmdj(""); 
							}else{
								bwdetail.setXmje("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(MathCal.add(detail
										.getAmt(),0,2)*-1));// 金额
								
								bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(MathCal.add(detail
										.getTaxfee(),0,2)*-1));// 税额
								if(detail.getQty()>0){
								double qty = MathCal.add(detail.getQty(),0,8)*-1;
								
								bwdetail.setXmsl("null".equals(String.valueOf(detail.getQty())) ? "" : String.valueOf(qty));// 商品数量
		
								bwdetail.setXmdj("null".equals(String.valueOf(detail.getQty())) ? "" : String.valueOf(MathCal.add(detail
										.getAmt()*-1/qty,0,8))); 
								}
							}
							
							bwdetail.setXmje("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(MathCal.add(detail
									.getAmt(),0,2)*-1));// 金额
							
							bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(MathCal.add(detail
									.getTaxfee(),0,2)*-1));// 税额
							
							bwdetail.setKce("");
							
					
							
							bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate())) ? "" : String.valueOf(detail
									.getTaxrate()));// 税率

							bwdetail.setByzd1("");
							bwdetail.setByzd2("");
							bwdetail.setByzd3("");
							bwdetail.setByzd4("");
							bwdetail.setByzd5("");
	
							saledetailList.add(bwdetail);
						}
					}
				}
			}
			
		}
		
		saleDetaillist.setSaleDetail(saledetailList);
		
		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return;
		}
		inter.setSalehead(salehead); 
		inter.setSaleDetaillist(saleDetaillist);
		inter.setOrdpay(ordpay);
		inter.setOrdhead(ordhead);
		ordddetaillist.add(orddetail);
		orddetaillist.setOrdDetaillist(ordddetaillist); 
		inter.setOrddetaillist(orddetaillist);
		inter.setWl(wl);

		String dataRequest = XMLConverter.objectToXml(inter, "utf-8");

		if (dataRequest == null || "".equals(dataRequest)) {
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}
		//System.out.println(dataRequest);
		hangXinBeanToXml(dataRequest,taxinfo ,fwdm, encryptCode,rtn);

	}

	public HnHangXinRtInvoiceBean rtOpenToBean(String xml, RtnData rtn) {
		if (xml == null || "".equals(xml)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		HnHangXinRtInvoiceBean rtinvoicebean = new HnHangXinRtInvoiceBean();

		try {
			JAXBContext jc = JAXBContext.newInstance(HnHangXinRtInterfaceBean.class);
			Unmarshaller u = jc.createUnmarshaller();
			StringBuffer xmlStr = new StringBuffer(xml);
			HnHangXinRtInterfaceBean bean = (HnHangXinRtInterfaceBean) u.unmarshal(new StreamSource(new StringReader(xmlStr
					.toString())));
			if("0000".equals(bean.getReturnStateInfo().getReturnCode())){
				
				 //有内层报文进行CA解密
	            String secondXml=bean.getData().getContent();
	            //判断需不需要解压缩
	            byte[] decodeData1 = null;
	            byte[] secondXmlByte = null;
	            //判断测试服务器返回内层报文是否进行了压缩：
	            if("1".equals(bean.getData().getDataDescription().getZipCode())){
	                //解压前需先对内层报文进行解Base64操作
	                secondXmlByte = GZipUtils.decompress(Base64.decodeBase64(secondXml));
	            }else {
	                //解密前需先对内层报文进行解Base64操作
	                secondXmlByte = Base64.decodeBase64(secondXml);
	            }
	            //判断测试服务器返回是否进行了加密：
	            if("2".equals(bean.getData().getDataDescription().getEncryptCode())){
	             //   PKCS7 pkcs72 = PKCS7.getPkcs7Client("ca/fapiao/client/cert/914300007170539007.pfx", "7170539007");
	            	PKCS7 pkcs7Client = getPkcs7Map(nsrsbh);
	                decodeData1 = pkcs7Client.pkcs7Decrypt(secondXmlByte) ;// pkcs72.pkcs7Decrypt(secondXmlByte);
	            }else {
	                decodeData1=secondXmlByte;
	            }
	            
	            bean.getData().setContent(new String(decodeData1,"UTF-8"));
	            
				jc = JAXBContext.newInstance(HnHangXinRtInvoiceBean.class);
				u = jc.createUnmarshaller();
				xmlStr = new StringBuffer(bean.getData().getContent());
				rtinvoicebean = (HnHangXinRtInvoiceBean) u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
				System.out.println(rtinvoicebean.getFp_hm());
			}else{
				rtn.setCode(-1);
				rtn.setMessage(new String(Base64.decodeBase64(bean.getReturnStateInfo().getReturnMessage().getBytes("utf-8")), "utf-8"));
			}
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
			log.error(e, e);
		}

		return rtinvoicebean;
	}
	
	public void tuisongDzBean(Invque invque, Taxinfo taxinfo, String fwdm,String encryptCode,RtnData rtn) {
		/**
		 * 对航信数据对象定义 headbean 开票头 fptbean 开票头
		 * 
		 * bwDetailList 开票明细集合
		 * **/
		
		StringBuffer  xml = new StringBuffer();
		
		HangXinQzInvoiceHeadBean headbean = new HangXinQzInvoiceHeadBean();
		HangXinQzFptBean head = new HangXinQzFptBean();
		HangXinQzDetaiListBean detailListBean = new HangXinQzDetaiListBean();
		List<HangXinQzDetaiBean> bwDetailList = new ArrayList<HangXinQzDetaiBean>();

		// bwDetailList.add(detailbean);

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
		xml.append("<REQUEST_COMMON_FPTS class=\"REQUEST_COMMON_FPTS\">");
		
		if (invque.getIqtel() == null || "".equals(invque.getIqtel())) {

			if (invque.getIqemail() == null || "".equals(invque.getIqemail())) {
				
				rtn.setCode(-1);
				rtn.setMessage("手机和邮箱不能同时为空");
				return;
				
			} else {
				xml.append("<TSFS>0</TSFS>");
				xml.append("<SJ></SJ>");
				xml.append("<EMAIL>"+(invque.getIqemail()==null?"":invque.getIqemail())+"</EMAIL>");
			}
		} else {
			if (invque.getIqemail() == null || "".equals(invque.getIqemail())) {
				xml.append("<TSFS>1</TSFS>");
				xml.append("<SJ>"+(invque.getIqtel())+"</SJ>");
				xml.append("<EMAIL></EMAIL>");
			} else {
				xml.append("<TSFS>1</TSFS>");
				xml.append("<SJ>"+(invque.getIqtel())+"</SJ>");
				xml.append("<EMAIL>"+(invque.getIqemail())+"</EMAIL>");
			}
		}
		
		if (invque.getIqseqno() == null || "".equals(invque.getIqseqno())) {
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		} else {
			 // 发票流水号
			xml.append("<FPQQLSH>"+(invque.getIqseqno())+"</FPQQLSH>");
			dataExchangeId =invque.getIqseqno();
		}
		
		if (taxinfo.getTaxno() == null || "".equals(taxinfo.getTaxno() )) {
			rtn.setCode(-1);
			rtn.setMessage("纳税人识别号不能为空");
			return;
		} else {
		 
			xml.append("<NSRSBH>"+(taxinfo.getTaxno())+"</NSRSBH>");
		}
		
		if (invque.getRtfpdm() == null || "".equals(invque.getRtfpdm())) {
			rtn.setCode(-1);
			rtn.setMessage("发票代码不能为空");
			return;
		} else {
			 // 发票流水号
			xml.append("<FP_DM>"+(invque.getRtfpdm())+"</FP_DM>");
		}
		
		if (invque.getRtfphm() == null || "".equals(invque.getRtfphm())) {
			rtn.setCode(-1);
			rtn.setMessage("发票号码不能为空");
			return;
		} else {
			 
			xml.append("<FP_HM>"+(invque.getRtfphm())+"</FP_HM>");
		}
		
		xml.append("<KP_JE>"+(invque.getIqtotje()+invque.getIqtotse())+"</KP_JE>");
		
		xml.append("<KP_SE>"+(invque.getIqtotse())+"</KP_SE>");
		xml.append("<PDF_PATH>"+(invque.getIqpdf())+"</PDF_PATH>");
		xml.append("<KPRQ>"+(invque.getRtkprq())+"</KPRQ>");
		xml.append("<XFMC>"+(taxinfo.getTaxname())+"</XFMC>");
		xml.append("<GFMC>"+(invque.getIqgmfname())+"</GFMC>");
		xml.append("</REQUEST_COMMON_FPTS>");
		hangXinBeanToXml(xml.toString(),taxinfo, fwdm,encryptCode,rtn);
		System.out.println(rtn.getMessage());
		try{
		rtn.setMessage(new String(Base64.encodeBase64((rtn.getMessage()).getBytes("utf-8")),"utf-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	/**
	 * 解密
	 * @param enstr
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public String decode(String enstr) throws IOException, Exception {
		PKCS7 pkcs7Client = getPkcs7Map(nsrsbh);
		return new String(pkcs7Client.pkcs7Decrypt(Base64.decodeBase64(enstr.getBytes("utf-8"))), "utf-8");
	}

	/**
	 * 加密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String encode(String str) throws Exception {
		PKCS7 pkcs7Client = getPkcs7Map(nsrsbh);
		String fix = FileUtil.getRunPath() + File.separator + "CA" + File.separator + nsrsbh + File.separator;
		String cer = fix + ca.getProperty("CLIENT_DECRYPTCER");
		final byte[] encodeData = pkcs7Client.pkcs7Encrypt(str, FileUtils.readFileToByteArray(new File(cer)));
		return new String(Base64.encodeBase64(encodeData), "utf-8");
	}

	/**
	 * 发票查询 装换为航信接口对象
	 * **/
	public void findInvoiceBean(Invque invque, Taxinfo taxinfo, String fwdm,String encryptCode,
			RtnData rtn) {
		// 对象定义和初始化
		/**
		 * 对航信数据对象定义 head 开票头 bwDetailList 开票明细集合
		 * **/
		HangXinInvoiceSaleHeadBean head = new HangXinInvoiceSaleHeadBean();
		/**
		 * 计算后小票数据初始化 invque 队列数据 taxinfo 企业纳税号信息 detailList 小票数据集合
		 * **/
	
		if (invque == null) {
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return;
		}

		if (taxinfo == null) {
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return;
		}
	 
		StringBuffer  xml = new StringBuffer();
		xml.append("<REQUEST_FPXXXZ_NEW class=\"REQUEST_FPXXXZ_NEW\">");
		
		if (invque.getIqseqno() == null || "".equals(invque.getIqseqno())) {
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		} else {
			xml.append("<FPQQLSH>"+(invque.getIqseqno())+"</FPQQLSH>");
			dataExchangeId = invque.getIqseqno();
		}
		
		if (taxinfo.getItfskpbh() == null || "".equals(taxinfo.getItfskpbh())) {
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		} else {
			xml.append("<DSPTBM>"+(taxinfo.getItfskpbh())+"</DSPTBM>");
			 
		}
		
		if (taxinfo.getTaxno() == null || "".equals(taxinfo.getTaxno())) {
			rtn.setCode(-1);
			rtn.setMessage("开票方识别号不能为空");
			return;
		} else {
			xml.append("<NSRSBH>"+(taxinfo.getTaxno())+"</NSRSBH>");
			 
		}
		xml.append("<DDH></DDH>");
		xml.append("<PDF_XZFS>2</PDF_XZFS>");
		xml.append("</REQUEST_FPXXXZ_NEW> ");
 
		hangXinBeanToXml(xml.toString(),taxinfo, fwdm,encryptCode,rtn);

	}

	/**
	 * 装换为XML格式
	 * **/

	public void hangXinBeanToXml(String encryptTxt,Taxinfo taxinfo, String fwdm,String encryptCode, RtnData rtn) {
		String xmlData = "";
		String passWord="";
		String codeType ="";
		if (fwdm == null || "".equals(fwdm)) {
			rtn.setCode(-1);
			rtn.setMessage("服务代码为空");
			return;
		}
		if (nsrsbh == null || "".equals(nsrsbh)) {
			rtn.setCode(-1);
			rtn.setMessage("纳税人识别号为空");
			return;
		}
		 
		if (dataExchangeId == null || "".equals(dataExchangeId)) {
			rtn.setCode(-1);
			rtn.setMessage("数据交换流水号为空");
			return;
		}

		if (encryptTxt == null || "".equals(encryptTxt)) {
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		} else {
			try {
				System.out.println("hunanhongxin xml encrypt befoer "+encryptTxt);
				if("0".equals(encryptCode)){
					codeType ="BASE64";
					encryptTxt = new String(Base64.encodeBase64(encryptTxt.getBytes("utf-8")),"utf-8");
				}else{
					codeType="CA";
					encryptTxt = encode(encryptTxt);// new String(Base64.encodeBase64(encryptTxt.getBytes("utf-8")),"utf-8");
				}
				
			
				String uuid="";
				for (int i = 0; i < 10; i++) {
				int random=(int)(Math.random()*10);
				if(uuid.indexOf(random+"")!=-1){
				i=i-1;
				}else{
					uuid+=random;
				}	
				}
		 
				 
				passWord =new String(Base64.encodeBase64((SHA1.sha1(uuid+taxinfo.getItfskpkl())).getBytes("utf-8")),"utf-8");
				 passWord =  uuid+passWord;
				 
			} catch (Exception e) {
				rtn.setCode(-1);
				rtn.setMessage(e.getMessage());
				log.error(e, e);
			}
		}
	
		//SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS ");
		   xmlData = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
	                "<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
	                "xsi:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"DZFP1.0\" >\n" +
	                "\t<globalInfo>\n" +
	                "\t\t<terminalCode>0</terminalCode>\n" +
	                "\t\t<appId>ZZS_PT_DZFP</appId>\n" +
	                "\t\t<version>1.8</version>\n" +
	                "\t\t<interfaceCode>"+fwdm+"</interfaceCode>\n" +
	                "\t\t<userName>"+(taxinfo.getItfskpbh())+"</userName>\n" +
	                "\t\t<passWord>"+(passWord)+"</passWord>\n" +
	                "\t\t<taxpayerId>"+(taxinfo.getTaxno())+"</taxpayerId>\n" +
	                "\t\t<authorizationCode>"+(taxinfo.getItfkeypwd())+"</authorizationCode>\n" +
	                "\t\t<requestCode>"+(taxinfo.getItfskpbh())+"</requestCode>\n" +
	                "\t\t<requestTime>"+ sdf.format(new java.util.Date())+"</requestTime>\n" +
	                "\t\t<responseCode/>\n" +
	                "\t\t<dataExchangeId>"+dataExchangeId+"</dataExchangeId>\n" +
	                "\t</globalInfo>\n" +
	                "\t<returnStateInfo>\n" +
	                "\t\t<returnCode/>\n" +
	                "\t\t<returnMessage/>\n" +
	                "\t</returnStateInfo>\n" +
	                "\t<Data>\n" +
	                "\t\t<dataDescription>\n" +
	                "\t\t\t<zipCode>0</zipCode>\n" +
	                "\t\t\t<encryptCode>"+(encryptCode)+"</encryptCode>\n" +
	                "\t\t\t<codeType>"+(codeType)+"</codeType>\n" +
	                "\t\t</dataDescription>\t<content>"+(encryptTxt)+"</content>\n" +
	                "\t</Data>\n" +
	                "</interface>";
		   System.out.println(xmlData);
		if (xmlData == null || "".equals(xmlData)) {
			rtn.setCode(-1);
			rtn.setMessage("生成的XML为空");
			return;
		} else {

			rtn.setMessage(xmlData);
		}
	}
	


	public String getCaProperty(String key) {
		return ca.getProperty(key);
	}
}
