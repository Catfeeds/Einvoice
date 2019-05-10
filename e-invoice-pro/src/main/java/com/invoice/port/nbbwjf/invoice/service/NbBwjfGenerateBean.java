package com.invoice.port.nbbwjf.invoice.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.port.nbbwjf.invoice.bean.NbBwjfInvoiceDetaiBean;
import com.invoice.port.nbbwjf.invoice.bean.NbBwjfInvoiceDetaiListBean;
import com.invoice.port.nbbwjf.invoice.bean.NbBwjfInvoiceHeadBean;
import com.invoice.port.nbbwjf.invoice.bean.NbBwjfRtOpenInvoiceBean;
import com.invoice.port.nbbwjf.invoice.bean.NbBwjfInterfaceBean;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;
import com.invoice.util.XMLConverter;
import sun.misc.BASE64Decoder;

public class NbBwjfGenerateBean {

	public NbBwjfGenerateBean() {

	}

	/**
	 * 开具发票 装换为百旺接口对象
	 **/
	public void generateBaiWangBean(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList, RtnData rtn) {
		// 对象定义和初始化
		/**
		 * 对百旺数据对象定义 head 开票头 bwDetailList 开票明细集合
		 **/
		NbBwjfInvoiceHeadBean head = new NbBwjfInvoiceHeadBean();
		List<NbBwjfInvoiceDetaiBean> bwDetailList = new ArrayList<NbBwjfInvoiceDetaiBean>();
		NbBwjfInvoiceDetaiListBean detailListBean = new NbBwjfInvoiceDetaiListBean();
		/**
		 * 计算后小票数据初始化 invque 队列数据 taxinfo 企业纳税号信息 detailList 小票数据集合
		 **/
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
		if (detailList == null||detailList.size()<=0) {
			rtn.setCode(-1);
			rtn.setMessage("小票数据集合 detailList 没有初始化");
			return;
		}
		// 开票头转换
		if (invque.getIqseqno() == null || "".equals(invque.getIqseqno())) {
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		} else {
			head.setFpqqlsh(invque.getIqseqno());// 发票流水号
		}

		if (invque.getIqtype() == 0 || invque.getIqtype() == 1) {
			head.setKplx(invque.getIqtype());// 开票类型
		} else {
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}

		head.setBmb_bbh(taxinfo.getItfbbh());// 商品编码版本号

		if (invque.getZsfs() == null || "".equals(invque.getZsfs())) {
			rtn.setCode(-1);
			rtn.setMessage("征税方式不能为空");
			return;
		} else {
			head.setZsfs(invque.getZsfs());// 征税方式
		}

		if (taxinfo.getTaxno() == null || "".equals(taxinfo.getTaxno())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方纳税人识别号不能为空");
			return;
		} else {
			head.setXsf_nsrsbh(taxinfo.getTaxno());// 销售方纳税人识别号
		}

		if (taxinfo.getTaxname() == null || "".equals(taxinfo.getTaxname())) {
			rtn.setCode(-1);
			rtn.setMessage("销售方名称不能为空");
			return;
		} else {
			head.setXsf_mc(taxinfo.getTaxname());// 销售方名称
		}

		if (taxinfo.getTaxadd() == null || "".equals(taxinfo.getTaxadd())) {
			rtn.setCode(-1);
			rtn.setMessage("销货方地址电话不能为空");
			return;
		} else {
			head.setXsf_dzdh(taxinfo.getTaxadd());// 销货方地址电话
		}

		head.setXsf_yhzh(taxinfo.getTaxbank());// 销售方银行账号

		if (invque.getIqgmfname() == null || "".equals(invque.getIqgmfname())) {
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		} else {
			head.setGmf_mc(invque.getIqgmfname());// 购方客户名称
		}
		head.setFplxdm("026");
		head.setGmf_nsrsbh(invque.getIqgmftax() == null ? "" : invque.getIqgmftax());// 购方单位代码
		head.setGmf_yhzh(invque.getIqgmfbank() == null ? "" : invque.getIqgmfbank());// 购方开户行及账号
		head.setGmf_dzdh(invque.getIqgmfadd() == null ? "" : invque.getIqgmfadd());// 购方地址及电话
		head.setGmf_sjh(invque.getIqtel() == null ? "" : invque.getIqtel());// 购买方手机号
		head.setGmf_dzyx(invque.getIqemail() == null ? "" : invque.getIqemail());// 购买方电子邮箱

		head.setSkr(invque.getIqpayee() == null ? "" : invque.getIqpayee());// 收款人
		head.setFhr(invque.getIqchecker() == null ? "" : invque.getIqchecker());// 审核人
		if (invque.getIqadmin() == null || "".equals(invque.getIqadmin())) {
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		} else {
			head.setKpr(invque.getIqadmin());// 开票人
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

			head.setHjje((Double) invque.getIqtotje() * -1);// 合计金额
			head.setHjse((Double) invque.getIqtotse() * -1);// 合计税额
			head.setJshj((MathCal.add(invque.getIqtotje(), invque.getIqtotse(), 2)) * -1);// 价税合计

		} else {
			head.setYfp_dm(invque.getIqyfpdm());// 原发票代码
			head.setYfp_hm(invque.getIqyfphm());// 原发票号码
			head.setHjje((Double) invque.getIqtotje());// 合计金额
			head.setHjse((Double) invque.getIqtotse());// 合计税额
			head.setJshj(MathCal.add(invque.getIqtotje(), invque.getIqtotse(), 2));// 价税合计
		}
		if (invque.getInvqueList().size()<=0) {
			head.setBz("  "+detailList.get(0).getSheetid());// 备注
		}else {
			head.setBz("  "+invque.getInvqueList().get(0).getSheetid());// 备注
		}
		// 开票明细转换

		// 标记明细行是否为开票行是产生错误
		String flag = "N";
		for (InvoiceSaleDetail detail : detailList) {
			if (detail.getIsinvoice() == null || "".equals(detail.getIsinvoice())) {
				flag = "Y";
				break;
			} else {
				if ("Y".equals(detail.getIsinvoice())) {
					NbBwjfInvoiceDetaiBean bwdetail = new NbBwjfInvoiceDetaiBean();
					bwdetail.setFphxz("0");// 发票行性质
					bwdetail.setSpbm(detail.getTaxitemid());// 商品编码
					bwdetail.setZxbm("");
					bwdetail.setZzstsgl(detail.getTaxprecon());// 增值税特殊管理
					bwdetail.setYhzcbs(detail.getTaxpre());// 是否使用优惠政策
					bwdetail.setLslbs(detail.getZerotax());// 免税类型
					bwdetail.setXmmc(detail.getGoodsname());// 商品名称
					// bwdetail.setGgxh("");//规格型号
					bwdetail.setGgxh(detail.getSpec() == null ? "" : detail.getSpec());// 规格型号
					bwdetail.setDw(detail.getUnit());// 计量单位
					bwdetail.setXmdj(String.valueOf(detail.getPrice()));// detail.getPrice();单价
					if (invque.getIqtype() == 1) {
						bwdetail.setXmsl(String.valueOf(detail.getQty() * -1));// 商品数量
						bwdetail.setXmje(String.valueOf(detail.getAmount() * -1));// 金额
						bwdetail.setSe(String.valueOf(detail.getTaxfee() * -1));// 税额
					} else {
						bwdetail.setXmsl(String.valueOf(detail.getQty()));// 商品数量
						bwdetail.setXmje(String.valueOf(detail.getAmount()));// 金额
						bwdetail.setSe(String.valueOf(detail.getTaxfee()));// 税额
					}
					if (detail.getTaxrate() == 0) {
						bwdetail.setSl("0");// 税额
					} else {
						bwdetail.setSl(String.valueOf(detail.getTaxrate()));
					}

					bwDetailList.add(bwdetail);
				}
			}
		}

		if ("Y".equals(flag)) {
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return;
		}
		detailListBean.setDetail(bwDetailList);
		head.setDetaillist(detailListBean);

		String dataRequest = XMLConverter.objectToXml(head, "utf-8");

		if (dataRequest == null || "".equals(dataRequest)) {
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		} else {
			rtn.setMessage(dataRequest);
		}

	}

	/**
	 * 开具发票返回后的发票数据解析
	 **/

	public NbBwjfRtOpenInvoiceBean rtOpenToBean(String xml, RtnData rtn) {
		if (xml == null || "".equals(xml)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		NbBwjfInterfaceBean rtinvoicebean = new NbBwjfInterfaceBean();
		NbBwjfRtOpenInvoiceBean rtOpenInvoiceBean = new NbBwjfRtOpenInvoiceBean();

		try {
			JAXBContext jc = JAXBContext.newInstance(NbBwjfInterfaceBean.class);
			Unmarshaller u = jc.createUnmarshaller();
			StringBuffer xmlStr = new StringBuffer(xml);
			rtinvoicebean = (NbBwjfInterfaceBean) u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
			// System.out.println(new String(new
			// BASE64Decoder().decodeBuffer(rtinvoicebean.getData().getContent())) );
			jc = JAXBContext.newInstance(NbBwjfRtOpenInvoiceBean.class);
			u = jc.createUnmarshaller();
			if (rtinvoicebean.getReturnStateInfo().getReturnCode().equalsIgnoreCase("0000")) {
				xmlStr = new StringBuffer(
						new String(new BASE64Decoder().decodeBuffer(rtinvoicebean.getData().getContent()), "utf-8"));
				rtOpenInvoiceBean = (NbBwjfRtOpenInvoiceBean) u
						.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
				rtn.setCode(0);
				rtn.setMessage("");
			} else {
				rtn.setCode(-1);
				rtn.setMessage(rtinvoicebean.getReturnStateInfo().getReturnMessage());
			}
			System.out.println(xmlStr);
		
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return rtOpenInvoiceBean;
	}

	/**
	 * 开具发票查询(不分页) 根据发票代码和发票号码查询
	 **/

	public void findInvoice(Invque invque, Taxinfo taxinfo, RtnData rtn) {
		try {
			StringBuffer sb = new StringBuffer("");
			sb.append("<REQUEST_COMMON_FPCX class='REQUEST_COMMON_FPCX'>");
			sb.append("<FP_DM>" + invque.getRtfpdm() + "</FP_DM>");
			sb.append("<FP_HM>" + invque.getRtfphm() + "</FP_HM>");
			sb.append("</REQUEST_COMMON_FPCX>");
			rtn.setMessage(sb.toString());
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
	}

}
