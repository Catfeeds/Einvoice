package hangxin.invoice.service;

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
import com.invoice.rtn.data.RtnData;
import com.invoice.util.FileUtil;
import com.invoice.util.XMLConverter;

import baiwang.invoice.service.XmlUtil;
import hangxin.invoice.bean.HangXinInterfaceBean;
import hangxin.invoice.bean.HangXinInvoiceSaleDetailBean;
import hangxin.invoice.bean.HangXinInvoiceSaleDetailList;
import hangxin.invoice.bean.HangXinInvoiceSaleHeadBean;
import hangxin.invoice.bean.HangXinQzCommonnodesBean;
import hangxin.invoice.bean.HangXinQzComonnodeBean;
import hangxin.invoice.bean.HangXinQzDetaiBean;
import hangxin.invoice.bean.HangXinQzDetaiListBean;
import hangxin.invoice.bean.HangXinQzFptBean;
import hangxin.invoice.bean.HangXinQzInvoiceHeadBean;
import hangxin.invoice.bean.HangXinQzRtBean;
import hangxin.invoice.bean.HangXinQzTsfsBean;
import hangxin.invoice.bean.HangXinQzTsfsValueBean;
import hangxin.invoice.bean.HangXinRtInvoiceBean;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class HangXinGenerateBean {
	private final Log log = LogFactory.getLog(HangXinGenerateBean.class);
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

	public HangXinGenerateBean(String nsrsbh) {
		this.nsrsbh = nsrsbh;
		ca = CaConstant.get(nsrsbh);
	}

	/**
	 * 开具发票 装换为航信接口对象
	 * **/
	public void generateHangXinBean(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList, String fwdm,
			RtnData rtn) {
		// 对象定义和初始化
		/**
		 * 对航信数据对象定义 head 开票头 bwDetailList 开票明细集合
		 * **/
		HangXinInvoiceSaleHeadBean head = new HangXinInvoiceSaleHeadBean();
		HangXinInvoiceSaleDetailList list = new HangXinInvoiceSaleDetailList();
		List<HangXinInvoiceSaleDetailBean> bwDetailList = new ArrayList<HangXinInvoiceSaleDetailBean>();
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
			head.setFpqqlsh(invque.getIqseqno());// 发票流水号
			dataExchangeId = invque.getIqseqno();
		}

		if (invque.getIqgmfname() == null || "".equals(invque.getIqgmfname())) {
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		} else {
			head.setGhfmc(invque.getIqgmfname());// 购方客户名称
		}

		head.setGhf_nsrsbh(invque.getIqgmftax() == null ? "" : invque.getIqgmftax());// 购方单位代码
		head.setFkfkhyh_fkfyhzh(invque.getIqgmfbank() == null ? "" : invque.getIqgmfbank());// 购方开户行及账号
		head.setFkfdz_fkfdh(invque.getIqgmfadd() == null ? "" : invque.getIqgmfadd());// 购方地址及电话

		head.setXhfkhyh_skfyhzh(taxinfo.getTaxbank() == null ? "" : taxinfo.getTaxbank());// 销货方银行及账号

		if (taxinfo.getTaxadd() == null || "".equals(taxinfo.getTaxadd())) {
			rtn.setCode(-1);
			rtn.setMessage("销货方地址电话不能为空");
			return;
		} else {
			head.setXhfdz_xhfdh(taxinfo.getTaxadd());// 销货方地址电话
		}

		if (invque.getIqfplxdm() == null || "".equals(invque.getIqfplxdm())) {
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		} else {
			if ("026".equals(invque.getIqfplxdm())) {
				head.setFpzl_dm("51");// 发票种类编码
			} else {
				rtn.setCode(-1);
				rtn.setMessage("发票种类编码不正确");
				return;
			}
		}

		if (invque.getIqtype() == 1) {

			if (invque.getIqyfpdm() == null || "".equals(invque.getIqyfpdm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			} else {
				head.setYfp_dm(invque.getIqyfpdm());// 原发票代码
			}

			if (invque.getIqyfphm() == null || "".equals(invque.getIqyfphm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			} else {
				head.setYfp_hm(invque.getIqyfphm());// 原发票号码
			}
			
			head.setJshj(((Double) invque.getIqtotje() + (Double) invque.getIqtotse())*-1);// 价税合计
			head.setHjje((Double) invque.getIqtotje()*-1);// 合计金额
			head.setHjse((Double) invque.getIqtotse()*-1);// 合计税额
			
		} else {
			head.setYfp_dm("");// 原发票代码
			head.setYfp_hm("");// 原发票号码
			head.setJshj((Double) invque.getIqtotje() + (Double) invque.getIqtotse());// 价税合计
			head.setHjje((Double) invque.getIqtotje());// 合计金额
			head.setHjse((Double) invque.getIqtotse());// 合计税额

		}

		head.setBz(invque.getIqmemo() == null ? "" : invque.getIqmemo());// 备注

		if (invque.getIqadmin() == null || "".equals(invque.getIqadmin())) {
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		} else {
			head.setKpy(invque.getIqadmin());// 开票人
		}
		head.setSky("");// 收款人
		head.setFhr("");// 审核人
		head.setXhqd("");

		if (invque.getIqtype() == 0 || invque.getIqtype() == 1) {
			head.setKplx(invque.getIqtype());// 开票类型
		} else {
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}


		head.setBmb_bbh(taxinfo.getItfbbh() == null ? "" : taxinfo.getItfbbh());// 商品编码版本号

		// 开票明细转换
		// 标记明细行是否为开票行是产生错误
		String flag = "N";
		for (InvoiceSaleDetail detail : detailList) {
			if (detail.getIsinvoice() == null || "".equals(detail.getIsinvoice())) {
				flag = "Y";
				break;
			} else {
				if ("Y".equals(detail.getIsinvoice())) {
					HangXinInvoiceSaleDetailBean bwdetail = new HangXinInvoiceSaleDetailBean();
					bwdetail.setSpmc(detail.getGoodsname() == null ? "" : detail.getGoodsname());// 商品名称
					bwdetail.setSm("");// 商品所属类别
					bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate())) ? "" : String.valueOf(detail
							.getTaxrate()));// 税率
					//bwdetail.setGgxh("");// 规格型号
					bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
					bwdetail.setJldw(detail.getUnit() == null ? "" : detail.getUnit());// 计量单位
					if(invque.getIqtype()==1){
						bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee()*-1)) ? "" : String.valueOf(detail
								.getTaxfee()*-1));// 税额
						bwdetail.setSpje("null".equals(String.valueOf(detail.getAmount()*-1)) ? "" : String.valueOf(detail
								.getAmount()*-1));// 金额
						bwdetail.setSpsl("null".equals(String.valueOf(detail.getQty()*-1)) ? "" : String.valueOf(detail
								.getQty()*-1));// 商品数量
						
					}else{
						bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(detail
								.getTaxfee()));// 税额
						bwdetail.setSpje("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(detail
								.getAmount()));// 金额
						bwdetail.setSpsl("null".equals(String.valueOf(detail.getQty())) ? "" : String.valueOf(detail
								.getQty()));// 商品数量
					}
					
					bwdetail.setSpdj("null".equals(String.valueOf(detail.getPrice())) ? "" : String.valueOf(detail
							.getPrice()));// detail.getPrice();单价

					

					String fphxz = "0";
					bwdetail.setFphxz(fphxz);// 发票行性质
					bwdetail.setHsjbz("0");// 含税标志
					bwdetail.setSpbm(detail.getTaxitemid() == null ? "" : detail.getTaxitemid());// 商品税目
					bwdetail.setZxbm(detail.getGoodsid() == null ? "" : detail.getGoodsid());// 商品编码
					bwdetail.setYhzcbs("0");// 是否使用优惠政策
					bwdetail.setLslbs("");
					bwdetail.setZzstsgl("");// 增值税特殊管理

					bwDetailList.add(bwdetail);
				}
			}
		}

		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return;
		}

		list.setDetails(bwDetailList);
		head.setFp_kjmxs(list);

		String dataRequest = XMLConverter.objectToXml(head, "utf-8");

		if (dataRequest == null || "".equals(dataRequest)) {
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}
		hangXinBeanToXml(dataRequest, fwdm, rtn);

	}

	public HangXinRtInvoiceBean rtOpenToBean(String xml, RtnData rtn) {
		if (xml == null || "".equals(xml)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		HangXinRtInvoiceBean rtinvoicebean = new HangXinRtInvoiceBean();
		XmlUtil util = new XmlUtil();
		Map map = util.xmlToMap(xml);
		if (map == null || map.size() == 0) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回数据转换对象时错误");
			return null;
		}

		try {
			JAXBContext jc = JAXBContext.newInstance(HangXinInterfaceBean.class);
			Unmarshaller u = jc.createUnmarshaller();
			StringBuffer xmlStr = new StringBuffer(xml);
			HangXinInterfaceBean bean = (HangXinInterfaceBean) u.unmarshal(new StreamSource(new StringReader(xmlStr
					.toString())));

			String encryptTxt = "";
			BASE64Decoder decoder = new BASE64Decoder();
			encryptTxt = new String(decoder.decodeBuffer(bean.getData().getContent()), "utf-8");
			// System.out.println(encryptTxt);

			jc = JAXBContext.newInstance(HangXinRtInvoiceBean.class);
			u = jc.createUnmarshaller();
			xmlStr = new StringBuffer(encryptTxt);
			rtinvoicebean = (HangXinRtInvoiceBean) u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
			System.out.println(rtinvoicebean.getFwmw());
		} catch (Exception e) {
			log.error(e, e);
		}

		/*
		 * if("0000".equals(map.get("rtncode"))){ rtinvoicebean =
		 * (HangXinRtInvoiceBean)
		 * util.xmlStrToBean((String)map.get("rtndata"),BaiWangRtInvoiceBean
		 * .class);
		 * //System.out.println(rtinvoicebean.getFpdm()+" "+rtinvoicebean
		 * .getFphm()+" "+rtinvoicebean.getSkm()+" "+rtinvoicebean.getEym()+" "+
		 * rtinvoicebean.getJym()+" "+rtinvoicebean.getKprq()); }else{
		 * rtn.setCode(-1); rtn.setMessage((String)map.get("rtnmsg")); return
		 * null; }
		 */
		return rtinvoicebean;
	}

	public void generateHangXinQzBean(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList, RtnData rtn) {
		/**
		 * 对航信数据对象定义 headbean 开票头 fptbean 开票头
		 * 
		 * bwDetailList 开票明细集合
		 * **/
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
			head.setFpqqlsh(invque.getIqseqno());// 发票流水号
			dataExchangeId = invque.getIqseqno();
		}

		if (invque.getIqtype() == 0 || invque.getIqtype() == 1) {
			head.setKplx(invque.getIqtype());// 开票类型
		} else {
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}

		// head.setDkbz("0");
		head.setBmb_bbh(taxinfo.getItfbbh() == null ? "" : taxinfo.getItfbbh());// 商品编码版本号
		head.setQd_bz("0");
		head.setQdxmmc("");
		head.setXsf_nsrsbh(nsrsbh);

		if (taxinfo.getTaxname() == null || "".equals(taxinfo.getTaxname())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方名称不能为空");
			return;
		} else {
			head.setXsf_mc(taxinfo.getTaxname());// 销售方名称
		}

		head.setXsf_yhzh(taxinfo.getTaxbank() == null ? "" : taxinfo.getTaxbank());// 销售方银行及账号

		if (taxinfo.getTaxadd() == null || "".equals(taxinfo.getTaxadd())) {
			rtn.setCode(-1);
			rtn.setMessage("销货方地址电话不能为空");
			return;
		} else {
			head.setXsf_dzdh(taxinfo.getTaxadd());// 销售方地址电话
		}

		if (invque.getIqgmfname() == null || "".equals(invque.getIqgmfname())) {
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		} else {
			head.setGmf_mc(invque.getIqgmfname());// 购方客户名称
		}

		head.setGmf_nsrsbh(invque.getIqgmftax() == null ? "" : invque.getIqgmftax());// 购方单位代码
		head.setGmf_yhzh(invque.getIqgmfbank() == null ? "" : invque.getIqgmfbank());// 购方开户行及账号
		head.setGmf_dzdh(invque.getIqgmfadd() == null ? "" : invque.getIqgmfadd());// 购方地址及电话

		if (invque.getIqadmin() == null || "".equals(invque.getIqadmin())) {
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		} else {
			head.setKpr(invque.getIqadmin());// 开票人
		}
		head.setSkr("");// 收款人
		head.setFhr("");// 审核人

		if (invque.getIqtype() == 1) {

			if (invque.getIqyfpdm() == null || "".equals(invque.getIqyfpdm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			} else {
				head.setYfp_dm(invque.getIqyfpdm());// 原发票代码
			}

			if (invque.getIqyfphm() == null || "".equals(invque.getIqyfphm())) {
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			} else {
				head.setYfp_hm(invque.getIqyfphm());// 原发票号码
			}

		} else {
			head.setYfp_dm("");// 原发票代码
			head.setYfp_hm("");// 原发票号码
		}

		head.setJshj((Double) invque.getIqtotje() + (Double) invque.getIqtotse());// 价税合计
		head.setHjje((Double) invque.getIqtotje());// 合计金额
		head.setHjse((Double) invque.getIqtotse());// 合计税额

		head.setBz(invque.getIqmemo() == null ? "" : invque.getIqmemo());// 备注

		if (invque.getIqtype() == 1 || invque.getIqtype() == 0) {
			head.setFpzt(0);
		} else if (invque.getIqtype() == 2) {
			head.setFpzt(1);
		} else {
			rtn.setCode(-1);
			rtn.setMessage("发票状态类型错误");
			return;
		}

		head.setJqbh(taxinfo.getItfskpbh());
		head.setFp_dm(invque.getRtfpdm());
		head.setFp_hm(invque.getRtfphm());
		head.setKprq(invque.getRtkprq());
		head.setFp_mw(invque.getRtskm());
		head.setJym(invque.getRtjym());
		head.setEwm(invque.getRtewm());
		head.setPdf_xzfs("2");

		// 开票明细转换
		// 标记明细行是否为开票行是产生错误
		String flag = "N";
		for (InvoiceSaleDetail detail : detailList) {
			if (detail.getIsinvoice() == null || "".equals(detail.getIsinvoice())) {
				flag = "Y";
				break;
			} else {
				if ("Y".equals(detail.getIsinvoice())) {
					HangXinQzDetaiBean bwdetail = new HangXinQzDetaiBean();
					bwdetail.setXmmc(detail.getGoodsname() == null ? "" : detail.getGoodsname());// 商品名称
					bwdetail.setDw(detail.getUnit() == null ? "" : detail.getUnit());// 计量单位
					//bwdetail.setGgxh("");// 规格型号
					bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
					bwdetail.setXmsl("null".equals(String.valueOf(detail.getQty())) ? "" : String.valueOf(detail
							.getQty()));// 商品数量
					bwdetail.setXmdj("null".equals(String.valueOf(detail.getPrice())) ? "" : String.valueOf(detail
							.getPrice()));// detail.getPrice();单价
					bwdetail.setXmje("null".equals(String.valueOf(detail.getAmount())) ? "" : String.valueOf(detail
							.getAmount()));// 金额
					bwdetail.setSl("null".equals(String.valueOf(detail.getTaxrate())) ? "" : String.valueOf(detail
							.getTaxrate()));// 税率
					bwdetail.setSe("null".equals(String.valueOf(detail.getTaxfee())) ? "" : String.valueOf(detail
							.getTaxfee()));// 税额
					bwdetail.setHsbz("0");// 含税标志
					bwdetail.setFphxz("0");
					// bwdetail.setFphxz("null".equals(String.valueOf(detail.getRowno()))?"":String.valueOf(detail.getRowno()));//发票行性质
					bwdetail.setSpbm(detail.getTaxitemid() == null ? "" : detail.getTaxitemid());// 商品税目
					// bwdetail.setZxbm(detail.getGoodsid()==null?"":detail.getGoodsid());//商品编码
					bwdetail.setYhzcbs("0");// 是否使用优惠政策
					bwdetail.setKce("");
					// bwdetail.setLslbs("");
					// bwdetail.setZzstsgl("");//增值税特殊管理
					// bwdetail.setSm("");//商品所属类别

					bwDetailList.add(bwdetail);
				}
			}
		}

		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return;
		}

		String splx = "";
		if (invque.getIqfplxdm() == null || "".equals(invque.getIqfplxdm())) {
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		} else {
			if ("026".equals(invque.getIqfplxdm())) {
				splx = "1";
			} else if ("007".equals(invque.getIqfplxdm())) {
				splx = "3";
			} else if ("004".equals(invque.getIqfplxdm())) {
				splx = "2";
			} else {
				rtn.setCode(-1);
				rtn.setMessage("发票种类编码不正确");
				return;
			}
		}

		detailListBean.setDetail(bwDetailList);

		headbean.setFpt(head);
		headbean.setDetailList(detailListBean);
		String info = "SJLY" + "" + "06" + "" + "FPLX" + "" + splx;
		// System.out.println(info);
		try {
			info = new BASE64Encoder().encode(info.getBytes("utf-8"));
		} catch (Exception e) {
			log.error(e, e);
		}
		headbean.setFpqz_info(info);

		HangXinQzComonnodeBean comnode = new HangXinQzComonnodeBean();
		HangXinQzCommonnodesBean comnodes = new HangXinQzCommonnodesBean();
		// List<HangXinQzComonnodeBean> DetailList = new
		// ArrayList<HangXinQzComonnodeBean>();
		HangXinQzTsfsBean tsfs = new HangXinQzTsfsBean();
		HangXinQzTsfsValueBean tsfsvalue = new HangXinQzTsfsValueBean();
		HangXinQzTsfsValueBean sj = new HangXinQzTsfsValueBean();
		HangXinQzTsfsValueBean email = new HangXinQzTsfsValueBean();

		if (invque.getIqtel() == null || "".equals(invque.getIqtel())) {

			if (invque.getIqemail() == null || "".equals(invque.getIqemail())) {

			} else {

				tsfsvalue.setName("TSFS");
				tsfsvalue.setValue("0");
				email.setName("EMAIL");
				email.setValue(invque.getIqemail());
				comnodes.setEmail(email);
				// comnodes.setSj(sj);
				comnodes.setTsfs(tsfsvalue);
				tsfs.setSs(comnodes);
				headbean.setTsfs(tsfs);
			}
		} else {
			if (invque.getIqemail() == null || "".equals(invque.getIqemail())) {
				tsfsvalue.setName("TSFS");
				tsfsvalue.setValue("1");
				sj.setName("SJ");
				sj.setValue(invque.getIqtel());
				comnodes.setSj(sj);
				comnodes.setTsfs(tsfsvalue);
				tsfs.setSs(comnodes);
				headbean.setTsfs(tsfs);
			} else {
				tsfsvalue.setValue("2");
				sj.setName("SJ");
				sj.setValue(invque.getIqtel());
				email.setName("EMAIL");
				email.setValue(invque.getIqemail());
				tsfsvalue.setName("TSFS");
				comnodes.setEmail(email);
				comnodes.setSj(sj);
				comnodes.setTsfs(tsfsvalue);
				tsfs.setSs(comnodes);
				headbean.setTsfs(tsfs);
			}
		}

		String dataRequest = XMLConverter.objectToXml(headbean, "utf-8");

		if (dataRequest == null || "".equals(dataRequest)) {
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}

		hangXinQzBeanToXml(dataRequest, rtn);

	}

	public HangXinQzRtBean rtQzOpenToBean(String xml, RtnData rtn) {
		if (xml == null || "".equals(xml)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		HangXinQzRtBean rtinvoicebean = new HangXinQzRtBean();
		XmlUtil util = new XmlUtil();
		Map map = util.xmlToMap(xml);
		if (map == null || map.size() == 0) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回数据转换对象时错误");
			return null;
		}

		try {

			JAXBContext jc = JAXBContext.newInstance(HangXinInterfaceBean.class);
			Unmarshaller u = jc.createUnmarshaller();
			StringBuffer xmlStr = new StringBuffer(xml);
			HangXinInterfaceBean bean = (HangXinInterfaceBean) u.unmarshal(new StreamSource(new StringReader(xmlStr
					.toString())));
			if (bean.getData().getContent() != null && !"".equals(bean.getData().getContent())) {
				String encryptTxt = "";

				encryptTxt = decode(bean.getData().getContent());

				jc = JAXBContext.newInstance(HangXinQzRtBean.class);
				u = jc.createUnmarshaller();
				xmlStr = new StringBuffer(encryptTxt);
				rtinvoicebean = (HangXinQzRtBean) u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
			}
		} catch (Exception e) {
			log.error(e, e);
		}

		return rtinvoicebean;
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
	public void findInvoiceBean(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList, String fwdm,
			RtnData rtn) {
		// 对象定义和初始化
		/**
		 * 对航信数据对象定义 head 开票头 bwDetailList 开票明细集合
		 * **/
		HangXinInvoiceSaleHeadBean head = new HangXinInvoiceSaleHeadBean();
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
		String dataRequest = "";
		if (invque.getIqseqno() == null || "".equals(invque.getIqseqno())) {
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		} else {
			dataRequest = "<REQUEST_FPKJXX_FPJGXX_CX class=\"REQUEST_FPKJXX_FPJGXX_CX\"> " + "<FPQQLSH>"
					+ (invque.getIqseqno()) + "</FPQQLSH> " + "</REQUEST_FPKJXX_FPJGXX_CX> ";
			dataExchangeId = invque.getIqseqno();
		}

		if (dataRequest == null || "".equals(dataRequest)) {
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}

		hangXinBeanToXml(dataRequest, fwdm, rtn);

	}

	/**
	 * 装换为XML格式
	 * **/

	public void hangXinBeanToXml(String encryptTxt, String fwdm, RtnData rtn) {
		String xmlData = "";

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
				encryptTxt = new BASE64Encoder().encode(encryptTxt.getBytes("utf-8"));
			} catch (Exception e) {
				log.error(e, e);
			}
		}

		xmlData = "<?xml version=\"1.0\" encoding=\"gbk\"?>"
				+ "<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"DZFP1.0\">"
				+ "	<globalInfo>" + "		<appId>ZZSDZFP</appId>" 
				+ "		<interfaceCode>"
				+ fwdm
				+ "</interfaceCode>"
				+ "		<userName>"
				+ nsrsbh
				+ "</userName>"
				+ "		<passWord></passWord>"
				+ "		<requestCode></requestCode>"
				+ "		<requestTime></requestTime>"
				+ "		<responseCode></responseCode>"
				+ "		<dataExchangeId>"
				+ dataExchangeId
				+ "</dataExchangeId>"
				+ "		<fjh></fjh>"
				+ "		<jqbh></jqbh>"
				+ "	</globalInfo>"
				+ "	<returnStateInfo>"
				+ "		<returnCode>0000</returnCode>"
				+ "		<returnMessage></returnMessage>"
				+ "	</returnStateInfo>"
				+ "	<Data>"
				+ "		<dataDescription>"
				+ "			<zipCode>0</zipCode>"
				+ "			<encryptCode>0</encryptCode>"
				+ "			<codeType>0</codeType>"
				+ "		</dataDescription>" + "		<content>" + encryptTxt + "</content>" + "	</Data>" + "</interface>";
		if (xmlData == null || "".equals(xmlData)) {
			rtn.setCode(-1);
			rtn.setMessage("生成的XML为空");
			return;
		} else {

			rtn.setMessage(xmlData);
		}
	}

	public void hangXinQzBeanToXml(String encryptTxt, RtnData rtn) {
		String xmlData = "";

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

			System.out.println("加密前" + encryptTxt);

			try {
				encryptTxt = encode(encryptTxt);
//				System.out.println("加密后" + encryptTxt);
			} catch (Exception e) {
				log.error(e, e);
				throw new RuntimeException("电子签章配置异常：" + e.getMessage());
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS ");

		xmlData = "<?xml version=\"1.0\" encoding=\"utf-8\"?> "
				+ " <interface xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"DZFP1.0\"> "
				+ "	<globalInfo> " + "		<terminalCode>1</terminalCode> " + "		<appId>A16D8DBD18EDDD80</appId> "
				+ "		<version>2.0</version>" + "		<interfaceCode>ECXML.FPQZ.BC.E.INV</interfaceCode>" + "		<userName>"
				+ nsrsbh
				+ "</userName>"
				+ "		<passWord/>"
				+ "		<taxpayerId>"
				+ nsrsbh
				+ "</taxpayerId>"
				+ "		<authorizationCode/>"
				+ "		<requestCode/>"
				+ "		<requestTime>"
				+ sdf.format(new java.util.Date())
				+ "</requestTime>"
				+ "		<responseCode/>"
				+ "		<dataExchangeId>"
				+ dataExchangeId
				+ "</dataExchangeId>"
				+ "	</globalInfo>"
				+ "	<returnStateInfo>"
				+ "		<returnCode>0000</returnCode>"
				+ "		<returnMessage/>"
				+ "	</returnStateInfo>"
				+ "	<Data>"
				+ "		<dataDescription>"
				+ "			<zipCode>0</zipCode>"
				+ "			<encryptCode>2</encryptCode>"
				+ "			<codeType>CA</codeType>"
				+ "		</dataDescription>"
				+ "		<content>" + encryptTxt + "</content>" + "	</Data>" + " </interface>";

		// System.out.println(xmlData);
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
