package com.invoice.yuande.invoice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.jdom.output.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aisino.CaConstant;
import com.htxx.service.scserver.WbServiceI;
import com.invoice.apiservice.dao.InvoiceDao;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.dao.InvqueDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.apiservice.service.impl.InvoiceService;
import com.invoice.apiservice.service.impl.InvqueService;
import com.invoice.bean.db.InvoiceDetail;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.InvqueList;
import com.invoice.bean.db.Shop;
import com.invoice.bean.db.Taxinfo;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.ResponseInvoice;
import com.invoice.config.EntPrivatepara;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.dao.TaxitemDao;
import com.invoice.uiservice.service.ShopService;
import com.invoice.util.NewHashMap;
import com.invoice.yuande.invoice.bean.YuanDeDzRntDataBean;
import com.invoice.yuande.invoice.bean.YuanDeRtFindInvoiceHeadBean;
import com.invoice.yuande.invoice.bean.YuanDeRtInvoiceBean;
import com.invoice.yuande.invoice.bean.YuanDeRtSpmxBean;

@Service("YuanDeService")
public class YuanDeServiceImpl implements YuanDeService {
	private final Log log = LogFactory.getLog(YuanDeServiceImpl.class);
	private String serviceUrl;
	private String nsrsbh;

	@Resource(name = "ShopService")
	ShopService shopService;

	@Autowired
	InvoiceSaleDao saleDao;

	@Autowired
	InvqueService invqueService;

	@Autowired
	InvqueDao inqueDao;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	InvoiceDao invoiceDao;

	@Autowired
	EntPrivatepara entPrivatepara;

	@Autowired
	TaxitemDao taxitemDao;

	// 开具发票--//银座
	public YuanDeRtInvoiceBean openinvoice(Invque invque, Taxinfo taxinfo, ResponseBillInfo saleshead, RtnData rtn) {
		Properties ca = CaConstant.get(taxinfo.getEntid());
		serviceUrl = ca.getProperty("openinvoice");

		YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);

		hx.generateHangXinBean(invque, taxinfo, saleshead, rtn);
		System.out.println(rtn.getMessage());
		YuanDeRtInvoiceBean rtinvoicebean = new YuanDeRtInvoiceBean();

		// 登陆航信获取数据
		if (rtn.getCode() == 0) {
			sendData(rtn.getMessage(), rtn);
			System.out.println(rtn.getMessage());
		} else {
			rtn.setCode(-1);
			// rtn.setMessage("数据装换为XML出错");
			return null;
		}

		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			rtinvoicebean = hx.rtOpenSubmitToBean(rtn.getMessage(), rtn);
		} else {
			rtn.setCode(-1);
			rtn.setMessage("从航信获取的数据有问题");
			return null;
		}

		return rtinvoicebean;
	}

	// 推送--//银座
	public void tuiSonginvoice(Invque invque, RtnData rtn) {
		Properties ca = CaConstant.get(invque.getIqentid());
		serviceUrl = ca.getProperty("tuisongInvoice");

		YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);

		hx.tuiSongInvoiceBean(invque, rtn);
		System.out.println(rtn.getMessage());

		// 登陆航信获取数据
		if (rtn.getCode() == 0) {
			sendData(rtn.getMessage(), rtn);
			System.out.println(rtn.getMessage());
		} else {
			rtn.setCode(-1);
			// rtn.setMessage("数据装换为XML出错");
			return;
		}
	}

	// 开具发票
	public YuanDeDzRntDataBean openinvoiceDzBb(Invque invque, Taxinfo taxinfo, List<InvoiceSaleDetail> detailList,
			RtnData rtn) {

		YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);
		YuanDeDzRntDataBean rtinvoicebean = new YuanDeDzRntDataBean();
		try {
			boolean flag = false;
			for (InvoiceSaleDetail detail : detailList) {
				if ("Y".equals(detail.getIsinvoice())) {
					Taxitem taxitem = new Taxitem();
					taxitem.setTaxitemid(detail.getTaxitemid());
					List<HashMap<String, String>> list = taxitemDao.getTaxitemById(taxitem);
					if (list.size() > 0) {
						detail.setTaxitemname(list.get(0).get("taxitemname"));
					} else {
						rtn.setCode(-1);
						rtn.setMessage("商品税目编码是汇总项");
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				return null;
			}
			String organ = entPrivatepara.get(taxinfo.getEntid(), "Organ");
			hx.generateHangXinDzBean(invque, taxinfo, detailList, organ, rtn);
			System.out.println(rtn.getMessage());

			// 登陆航信获取数据
			if (rtn.getCode() == 0) {
				wbservice(rtn.getMessage(), invque, taxinfo, "0", rtn);
				System.out.println(rtn.getMessage());
			} else {
				rtn.setCode(-1);
				// rtn.setMessage("数据装换为XML出错");
				return null;
			}
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			rtinvoicebean = hx.rtDzBbXmlToBean(rtn.getMessage(), rtn);
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				rtn.setCode(-1);
				rtn.setMessage("等待查询发票出错");
				e.printStackTrace();
			}
			if ("0".equals(rtinvoicebean.getRetCode())) {
				rtinvoicebean = findInvoiceDzBbBean(invque, taxinfo, rtn);
			} else {
				rtn.setCode(-1);
				rtn.setMessage(rtinvoicebean.getRetMsg());
			}
		} else {
			rtn.setCode(-1);
			// rtn.setMessage("从航信获取的数据有问题");
			return null;
		}

		return rtinvoicebean;
	}

	// 发票返回结果
	public void txRtOpenToBean(String xml, String entid, RtnData rtn) {
//		System.out.println("回调返回的XML" + xml);
		if (xml == null || "".equals(xml)) {
			rtn.setCode(-1);
			rtn.setMessage("输入的XML参数为空");
			return;
		}
		long start = System.currentTimeMillis();

		log.info("txRtOpenToBean start" + start);

		try {
			YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);
			// 将XML转换为BEAN
			YuanDeRtInvoiceBean rtinvoicebean = hx.rtOpenToBean(xml, rtn);

			if (rtn.getCode() != -1) {

				InvoiceSaleHead head = new InvoiceSaleHead();
				Map<String, Object> map = new NewHashMap<String, Object>();

				// 获取门店和企业纳税号信息
				Shop shop = new Shop();
				Taxinfo taxinfo = new Taxinfo();
				shop.setShopid(rtinvoicebean.getShopid());
				shop.setEntid(entid);
				shop = shopService.getShopByShopId(shop);
				if (shop != null) {
					taxinfo.setEntid(shop.getEntid());
					taxinfo.setTaxno(shop.getTaxno());
					taxinfo = saleDao.getTaxinfo(taxinfo);
					if (taxinfo == null) {
						rtn.setCode(-1);
						rtn.setMessage("没有查找到相应的企业纳税信息");
						return;
					}
				} else {
					rtn.setCode(-1);
					rtn.setMessage("没有查找到相应的门店");
					return;
				}
				head.setEntid(shop.getEntid());
				ResponseBillInfo billinfo = null;
				List<InvqueList> list = null;
				InvqueList invqueList = null;
				// 获取小票对应队列信息 如果是空说明还没有插入队列
				map.clear();
				map.put("sheetid", rtinvoicebean.getFpqqlsh());
				map.put("sheettype", rtinvoicebean.getSheettype());
				if ("Q".equals(rtinvoicebean.getSerialid().substring(0, 1))) {
					map.put("iqseqno", rtinvoicebean.getSerialid());
				} else if ("S".equals(rtinvoicebean.getSerialid().substring(0, 1))) {
					map.put("serialid", rtinvoicebean.getSerialid());
				}

				list = inqueDao.getInvqueList(map);

				// 查找对应小票数据
				String serialid = "";
				if ("Q".equals(rtinvoicebean.getSerialid().substring(0, 1))) {
					// 获取小票头的流水号
					if (list == null || list.size() == 0) {
						rtn.setCode(-1);
						rtn.setMessage("没有找到相应的小票信息");
						return;
					}
					serialid = list.get(0).getSerialid();
				} else if ("S".equals(rtinvoicebean.getSerialid().substring(0, 1))) {
					serialid = rtinvoicebean.getSerialid();
				}
				billinfo = saleDao.getInvoiceSaleHeadByiqseqno(serialid);
				if (billinfo == null) {
					rtn.setCode(-1);
					rtn.setMessage("没有找到相应的小票信息！");
					return;
				} else {
					if (list != null && list.size() > 0) {
						for (InvqueList quelist : list) {
							if (quelist.getIqseqno().equals(billinfo.getIqseqno())) {
								invqueList = quelist;
							}
						}
					}

				}

				log.info("txRtOpenToBean getQuelist ok used " + (System.currentTimeMillis() - start));

				// 返回状态为1和2为保存或删除单据时操作,只有航信主动发票开票才会返回
				// 根据流水号查找对应的小票数据，然后修改 保存或删除状态
				if ("1".equals(rtinvoicebean.getStatus()) || "2".equals(rtinvoicebean.getStatus())) {

					// 判断小票是否已经为不能开票状态，此段代码暂停
					/*
					 * if("1".equals(rtinvoicebean.getStatus())&&billinfo.
					 * getFlag()!=0){ rtn.setCode(-1);
					 * rtn.setMessage("小票状态已经是不可开票状态！"); return; }
					 */

					// 当对单子做删除操作是判断单子是否已经进入队列,如果是责不能删除
					if (invqueList != null) {

						map.clear();
						map.put("iqseqno", invqueList.getIqseqno());// 疑问

						int invoiceStatus = invoiceDao.getInvoiceCount(map);

						if (invoiceStatus > 0) {
							rtn.setCode(-1);
							rtn.setMessage("小票状态已经是不可开票状态！");
							return;
						}

						head.setIqseqno(invqueList.getIqseqno());
					}

					// 更改小票invoice_sale_head表flag 状态,流水号Q开头的小票不会执行
					if ("Q".equals(rtinvoicebean.getSerialid().substring(0, 1))) {
						log.info("txRtOpenToBean updateInvoiceSaleFlag start status " + rtinvoicebean.getStatus()
								+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
						if (invqueList == null) {
							rtn.setCode(-1);
							rtn.setMessage("没有找到相应的小票信息");
							return;
						}

						// InvqueList invquelist = list.get(0);
						if ("1".equals(rtinvoicebean.getStatus())) {
							head.setFlag(1);

						} else {
							head.setFlag(0);

						}
						head.setSerialid("'" + invqueList.getSerialid() + "'");
						saleDao.updateInvoiceSaleFlagByserialid(head);
						log.info("txRtOpenToBean updateInvoiceSaleFlag end status " + rtinvoicebean.getStatus()
								+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
					} else if ("S".equals(rtinvoicebean.getSerialid().substring(0, 1))) {
						log.info("txRtOpenToBean updateInvoiceSaleFlag start status " + rtinvoicebean.getStatus()
								+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
						head.setSerialid("'" + rtinvoicebean.getSerialid() + "'");
						if ("1".equals(rtinvoicebean.getStatus())) {
							head.setFlag(1);
						} else {
							head.setFlag(0);
							head.setIqseqno("");
						}

						saleDao.updateInvoiceSaleFlagByserialid(head);
						log.info("txRtOpenToBean updateInvoiceSaleFlag end status " + rtinvoicebean.getStatus()
								+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
					} else {
						rtn.setCode(-1);
						rtn.setMessage("发票请求唯一流水号格式不正确！");
						return;
					}

				} else if ("3".equals(rtinvoicebean.getStatus()) || "4".equals(rtinvoicebean.getStatus())) {
					// 返回状态为3和4是保存发票或作废时操作
					log.info("txRtOpenToBean savezuofei start status " + rtinvoicebean.getStatus()
							+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
					// 查询开票类型不是作废时判断发票代码和号码是否存在
					if (rtinvoicebean.getKplx() != 2) {
						map.clear();
						map.put("rtfpdm", rtinvoicebean.getFp_dm());
						map.put("rtfphm", rtinvoicebean.getFp_hm());
						int i = invoiceDao.getIsInvoiceCount(map);

						if (i > 0) {
							rtn.setCode(-1);
							rtn.setMessage("返回的发票代码和发票号码已经存在！");
							return;
						}
					}

					// 将XML转换的BEAN转换为可以写入数据库的BEAN
					ResponseInvoice res = hx.rtBeanToBean(rtinvoicebean, rtn);

					// 开票数据来源 0手工 1小票、2批发、3储值卡售卡、4供应商费用 ',
					res.setSheettype(rtinvoicebean.getSheettype());

					// 业务类型 航信主动开票的业务类型为other, 只有航信主体开票业务会在这里向invque队列表插入数据
					res.setChannel("other");

					res.setEntid(shop.getEntid());
					res.setXsfNsrsbh(shop.getTaxno());

					List<ResponseBillInfo> billinfoList = new ArrayList<ResponseBillInfo>();
					billinfoList.add(billinfo);
					res.setBillInfoList(billinfoList);

					// 校验数据 转换为队列BEAN
					Invque que = invqueService.cookInvoiceQue(res);

					// 发票种类转换
					que.setIqfplxdm(rtinvoicebean.getFpzl_dm()); // 发票种类编码

					que.setIqstatus(50);
					que.setIqpdf(rtinvoicebean.getPdf_url());
					que.setIqtotje(rtinvoicebean.getHjje());
					que.setIqtotse(rtinvoicebean.getHjse());
					que.setRtfpdm(rtinvoicebean.getFp_dm());
					que.setRtfphm(rtinvoicebean.getFp_hm());
					que.setIqyfpdm(rtinvoicebean.getYfp_dm());
					que.setIqyfphm(rtinvoicebean.getYfp_hm());
					que.setIqtype(rtinvoicebean.getKplx());

					Invque invque = null;

					// 如果队列没有数据向队列插入数据
					// if(invqueList==null){
					if (StringUtils.isEmpty(invqueList) || StringUtils.isEmpty(invqueList.getIqseqno())) {

						// 向数据库队列插入数据
						invqueService.insert(que);

						for (InvqueList invquelist : que.getInvqueList()) {

							InvoiceSaleHead salehead = new InvoiceSaleHead();
							salehead.setEntid(que.getIqentid());
							salehead.setSheettype(invquelist.getSheettype());
							salehead.setSheetid(invquelist.getSheetid());
							salehead.setFlag(1);
							salehead.setIqseqno(que.getIqseqno());
							saleDao.updateInvoiceSaleFlag(salehead);
						}
						log.info("txRtOpenToBean savezuofei newque status " + rtinvoicebean.getStatus()
								+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
					} else {
						if (StringUtils.isEmpty(invqueList.getIqseqno())) {
							throw new RuntimeException("没有找到小票对应的队列");
						}
						que.setIqseqno(invqueList.getIqseqno());

						map.clear();
						map.put("iqseqno", invqueList.getIqseqno());
						map.put("pagestart", 0);
						map.put("pagesize", 1);
						List<Invque> quelist = inqueDao.getInvque(map);

						if (quelist == null || quelist.size() == 0) {
							rtn.setCode(-1);
							rtn.setMessage("没有查询到队列数据!");
							return;
						} else {
							invque = quelist.get(0);
							// if(invque.getIqstatus()!=50){

							invque.setRtfpdm(rtinvoicebean.getFp_dm());
							invque.setRtfphm(rtinvoicebean.getFp_hm());
							invque.setIqpdf(rtinvoicebean.getPdf_url());

							// 将发票信息写入数据库
							// inqueDao.updateTo40(invque);
							if (StringUtils.isEmpty(invque.getIqseqno())) {
								throw new RuntimeException("没有找到小票对应的队列");
							}
							inqueDao.updateTo50ByFp(invque);
							// 将签章PDF写入数据库
							// inqueDao.updateTo50(invque);

							// }

							/*
							 * if(rtinvoicebean.getKplx()==0){ InvoiceSaleHead
							 * salehead = new InvoiceSaleHead();
							 * salehead.setEntid(que.getIqentid());
							 * salehead.setSheettype(rtinvoicebean.getSheettype(
							 * ));
							 * salehead.setSheetid(rtinvoicebean.getFpqqlsh());
							 * salehead.setFlag(1);
							 * salehead.setIqseqno(que.getIqseqno());
							 * saleDao.updateInvoiceSaleFlag(salehead); }
							 */
						}
						log.info("txRtOpenToBean savezuofei upque status " + rtinvoicebean.getStatus()
								+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
					}
					// 根据返回的行号信息 查询小票
					if (rtinvoicebean.getKplx() != 2) {

						log.info("txRtOpenToBean savezuofei zuofei status " + rtinvoicebean.getStatus()
								+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));

						String zxbm = "";
						for (YuanDeRtSpmxBean spmx : rtinvoicebean.getDetaillist().getDetails()) {
							zxbm = zxbm + spmx.getZxbm() + ",";
						}
						if (zxbm.length() > 0) {
							zxbm = zxbm.substring(0, zxbm.length() - 1);
						}

						InvoiceSaleDetail detail = new InvoiceSaleDetail();
						detail.setEntid(shop.getEntid());
						detail.setSheettype(rtinvoicebean.getSheettype());
						detail.setSheetid(rtinvoicebean.getFpqqlsh());
						detail.setGoodsid(zxbm);
						List<InvoiceSaleDetail> invoicedetailList = saleDao.getInvoiceSaleDetailList(detail);

						// 向发票表插入发票数据
						InvoiceHead invoiceHead = invoiceService.cookInvoiceHead(que, taxinfo, invoicedetailList);
						invoiceHead.setFpewm(rtinvoicebean.getEwm());
						invoiceHead.setFphm(rtinvoicebean.getFp_hm());
						invoiceHead.setFpdm(rtinvoicebean.getFp_dm());
						invoiceHead.setFpskm(rtinvoicebean.getFwmw());
						invoiceHead.setFpjym(rtinvoicebean.getJym());
						invoiceHead.setFprq(rtinvoicebean.getKprq());
						invoiceHead.setPdf(rtinvoicebean.getPdf_url());

						if (rtinvoicebean.getKplx() == 1) {
							for (InvoiceDetail invoicedetail : invoiceHead.getDetailList()) {
								invoicedetail.setAmount(invoicedetail.getAmount() * -1);
								invoicedetail.setPrice(invoicedetail.getPrice() * -1);
								invoicedetail.setAmt(invoicedetail.getAmt() * -1);
								invoicedetail.setTaxfee(invoicedetail.getTaxfee() * -1);
							}
						}

						invoiceService.saveInvoice(invoiceHead);
					}

					log.info("txRtOpenToBean savezuofei searchbill status " + rtinvoicebean.getStatus()
							+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
					// 红冲和作废时判断小票是否可以重新开具发票
					if (rtinvoicebean.getKplx() == 1 || rtinvoicebean.getKplx() == 2) {

						if (invqueList == null) {
							rtn.setCode(-1);
							rtn.setMessage("没有找到相应的小票信息");
							return;
						}

						map = new NewHashMap<String, Object>();
						if (invque == null) {
							rtn.setCode(-1);
							rtn.setMessage("没有查询到队列数据！");
						}
						if (rtinvoicebean.getKplx() == 1)
							map.put("status", 90);
						else
							map.put("status", 99);
						map.put("iqseqno", invque.getIqseqno());
						map.put("fpDM", rtinvoicebean.getYfp_dm());
						map.put("fpHM", rtinvoicebean.getYfp_hm());
						invoiceDao.updateInvoiceStatus(map);

						// 查找对应的小票是否还有蓝票，如果没有小票恢复可开票状态
						int invoiceStatus = invoiceDao.getInvoiceCount(map);
						if (invoiceStatus == 0) {

							if ("Q".equals(rtinvoicebean.getSerialid().substring(0, 1))) {

								// InvqueList invquelist = list.get(0);
								// 修改小票头状态
								if ("1".equals(rtinvoicebean.getStatus()))
									head.setFlag(1);
								else
									head.setFlag(-1);
								head.setSerialid("'" + invqueList.getSerialid() + "'");
								head.setIqseqno(invqueList.getSerialid());
								saleDao.updateInvoiceSaleFlagByserialid(head);

							} else if ("S".equals(rtinvoicebean.getSerialid().substring(0, 1))) {

								head.setSerialid("'" + rtinvoicebean.getSerialid() + "'");
								head.setIqseqno(invqueList.getIqseqno());
								if ("1".equals(rtinvoicebean.getStatus()))
									head.setFlag(1);
								else
									head.setFlag(-1);

								saleDao.updateInvoiceSaleFlagByserialid(head);

							}
						}
					}
					log.info("txRtOpenToBean savezuofei start reopenend " + rtinvoicebean.getStatus()
							+ rtinvoicebean.getSerialid() + " used " + (System.currentTimeMillis() - start));
				} else {
					rtn.setCode(-1);
					rtn.setMessage("返回状态数据错误！");
					return;
				}

			} else {
				rtn.setCode(-1);
				return;
			}

		} catch (Exception e) {
			rtn.setCode(-1);
			e.printStackTrace();
			// rtn.setMessage(e.getMessage());
			throw new RuntimeException(rtn.getMessage() + e.getMessage());

		}

	}

	/**
	 * 发票查询 装换为航信接口对象 --//银座
	 **/
	public YuanDeRtFindInvoiceHeadBean findInvoiceBean(Invque invque, Taxinfo taxinfo, InvoiceSaleHead saleshead,
			RtnData rtn) {
		Properties ca = CaConstant.get(taxinfo.getEntid());
		serviceUrl = ca.getProperty("findInvoice");

		/*
		 * if(nsrsbh==null||"".equals(nsrsbh)){ rtn.setCode(-1);
		 * rtn.setMessage("开具发票时纳税人识别号为空"); return null; }
		 */

		YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);
		YuanDeRtFindInvoiceHeadBean rtinvoicebean = new YuanDeRtFindInvoiceHeadBean();
		hx.findInvoiceBean(invque, saleshead, rtn);
		// System.out.println(rtn.getMessage());

		// 登陆航信获取数据
		if (rtn.getCode() == 0) {
			sendData(rtn.getMessage(), rtn);
		} else {
			rtn.setCode(-1);
			// rtn.setMessage("数据装换为XML出错");
			return null;
		}
		// System.out.println(rtn.getMessage());
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			rtinvoicebean = hx.rtXmlToBean(rtn.getMessage(), rtn);
		} else {
			rtn.setCode(-1);
			// rtn.setMessage("从百旺获取的数据有问题");
			return null;
		}
		// System.out.println(rtinvoicebean.getEwm());
		return rtinvoicebean;
	}

	/**
	 * 发票查询 装换为航信接口对象
	 **/
	public YuanDeDzRntDataBean findInvoiceDzBbBean(Invque invque, Taxinfo taxinfo, RtnData rtn) {

		YuanDeDzRntDataBean rtinvoicebean = new YuanDeDzRntDataBean();
		YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);
		if (invque.getIqseqno() == null)
			invque.setIqseqno("");
		if (invque.getRtfpdm() == null)
			invque.setRtfpdm("");
		if (invque.getRtfphm() == null)
			invque.setRtfphm("");

		if ("".equals(invque.getIqseqno())) {
			if ("".equals(invque.getRtfpdm()) || "".equals(invque.getRtfphm())) {
				rtn.setCode(-1);
				rtn.setMessage("发票流水号和发票代码不能同时为空");
				return null;
			}
		}
		try {
			wbservice(rtn.getMessage(), invque, taxinfo, "1", rtn);
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			e.printStackTrace();
		}
		// 返回后的数据做装换
		if (rtn.getCode() == 0) {
			rtinvoicebean = hx.rtDzBbXmlToBean(rtn.getMessage(), rtn);
			if (!"0".equals(rtinvoicebean.getRetCode())) {
				rtn.setCode(-1);
				rtn.setMessage(rtinvoicebean.getRetMsg());
			}
		} else {
			rtn.setCode(-1);
			return null;
		}

		return rtinvoicebean;
	}

	/**
	 * 返回计算后的小票结果
	 */

	public String rtInvoiceSaleData(String fpqqlsh, String entid, RtnData rtn) {

		if (fpqqlsh == null || "".equals(fpqqlsh)) {
			rtn.setCode(-1);
			rtn.setMessage("传入的小票提取码为空");
			return null;
		} else {
			if (fpqqlsh.indexOf("-") != -1) {

				String fpqqlsh1[] = fpqqlsh.split("-");
				fpqqlsh1[2] = String.valueOf(Long.parseLong(fpqqlsh1[2]));
				fpqqlsh = fpqqlsh1[0] + "-" + fpqqlsh1[1] + "-" + fpqqlsh1[2];
			} else {
				rtn.setCode(-1);
				rtn.setMessage("输入的小票提取码格式有问题");
				return null;
			}
		}

		String serialid = "";
		try {
			// 解析小票提取码
			// String data = fpqqlsh.substring(0, 4)+"-"+fpqqlsh.substring(12,
			// 16)+"-"+fpqqlsh.substring(16);

			YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);
			RequestBillInfo bill = new RequestBillInfo();

			bill.setSheettype("1");
			bill.setTicketQC(fpqqlsh);
			bill.setSheetid(fpqqlsh);
			// 根据门店获取企业ID
			Shop shop = new Shop();
			Taxinfo taxinfo = new Taxinfo();
			shop.setShopid(fpqqlsh.substring(0, 4));
			shop.setEntid(entid);
			shop = shopService.getShopByShopId(shop);
			if (shop == null) {
				rtn.setCode(-1);
				rtn.setMessage("没有查找到相应的门店");
				return null;
			} else {
				taxinfo.setEntid(shop.getEntid());
				taxinfo.setTaxno(shop.getTaxno());
				taxinfo = saleDao.getTaxinfo(taxinfo);
				if (taxinfo == null) {
					rtn.setCode(-1);
					rtn.setMessage("没有查找到相应的企业纳税信息");
					return null;
				}
			}

			bill.setEntid(shop.getEntid());

			ResponseBillInfo sale_head = saleDao.getInvoiceSaleHead(bill);

			if (sale_head != null) {

				if (sale_head.getFlag() == 1) {
					if ("".equals(sale_head.getIqseqno()) || sale_head.getIqseqno() == null) {
						rtn.setCode(-1);
						rtn.setMessage("该小票已经保存!");
						return null;
					} else {
						rtn.setCode(-1);
						rtn.setMessage("该小票已经开具发票!");
						return null;
					}
				}

			}

			// 获取结算后的小票结果
			SheetService sheetservice = SheetServiceFactory.getInstance(bill.getSheettype());
			ResponseBillInfo res = sheetservice.getInvoiceSheetInfoWithDetail(bill);

			// 将结算后的小票装换为XML
			serialid = hx.generateInvoiceSaleBean(res, taxinfo, fpqqlsh, rtn);

			if (serialid == null || "".equals(serialid)) {
				rtn.setCode(-1);
				rtn.setMessage("流水号返回错误!");
				return null;
			}
			System.out.println("远的调用返回的XML：" + rtn.getMessage());
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			log.error(e, e);
			e.printStackTrace();
		}

		return serialid;
	}

	/**
	 * 返回计算后的小票结果 针对远的除银座其他客户
	 */

	public String rtYzInvoiceSaleData(String fpqqlsh, String entid, RtnData rtn) {
		if (fpqqlsh == null || "".equals(fpqqlsh)) {
			rtn.setCode(-1);
			rtn.setMessage("传入的小票提取码为空");
			return null;
		} else {
			if (fpqqlsh.indexOf("-") != -1) {

				String fpqqlsh1[] = fpqqlsh.split("-");
				fpqqlsh1[2] = String.valueOf(Long.parseLong(fpqqlsh1[2]));
				fpqqlsh = fpqqlsh1[0] + "-" + fpqqlsh1[1] + "-" + fpqqlsh1[2];
			} else {
				rtn.setCode(-1);
				rtn.setMessage("输入的小票提取码格式有问题");
				return null;
			}
		}

		String serialid = "";
		try {
			// 解析小票提取码
			// String data = fpqqlsh.substring(0, 4)+"-"+fpqqlsh.substring(12,
			// 16)+"-"+fpqqlsh.substring(16);

			YuanDeGenerateBean hx = new YuanDeGenerateBean(nsrsbh);
			RequestBillInfo bill = new RequestBillInfo();

			bill.setSheettype("1");
			bill.setTicketQC(fpqqlsh);
			bill.setSheetid(fpqqlsh);
			// 根据门店获取企业ID
			Shop shop = new Shop();
			Taxinfo taxinfo = new Taxinfo();
			shop.setShopid(fpqqlsh.substring(0, 4));
			shop.setEntid(entid);
			shop = shopService.getShopByShopId(shop);
			if (shop == null) {
				rtn.setCode(-1);
				rtn.setMessage("没有查找到相应的门店");
				return null;
			} else {
				taxinfo.setEntid(shop.getEntid());
				taxinfo.setTaxno(shop.getTaxno());
				taxinfo = saleDao.getTaxinfo(taxinfo);
				if (taxinfo == null) {
					rtn.setCode(-1);
					rtn.setMessage("没有查找到相应的企业纳税信息");
					return null;
				}
			}

			bill.setEntid(shop.getEntid());

			ResponseBillInfo sale_head = saleDao.getInvoiceSaleHead(bill);

			if (sale_head != null) {

				if (sale_head.getFlag() == 1) {
					if ("".equals(sale_head.getIqseqno()) || sale_head.getIqseqno() == null) {
						rtn.setCode(-1);
						rtn.setMessage("该小票已经保存!");
						return null;
					} else {
						rtn.setCode(-1);
						rtn.setMessage("该小票已经开具发票!");
						return null;
					}
				}

			}

			// 获取结算后的小票结果
			SheetService sheetservice = SheetServiceFactory.getInstance(bill.getSheettype());
			ResponseBillInfo res = sheetservice.getInvoiceSheetInfoWithDetail(bill);

			// 将结算后的小票装换为XML
			serialid = hx.generateInvoiceSaleBbBean(res, taxinfo, fpqqlsh, rtn);

			if (serialid == null || "".equals(serialid)) {
				rtn.setCode(-1);
				rtn.setMessage("流水号返回错误!");
				return null;
			}
			System.out.println("远的调用返回的XML：" + rtn.getMessage());
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			log.error(e, e);
			e.printStackTrace();
		}

		return serialid;
	}

	/**
	 * 发送数据信息
	 **/
	public void sendData(String xml, RtnData rtn) {

		if (serviceUrl == null || "".equals(serviceUrl)) {
			rtn.setCode(-1);
			rtn.setMessage("URL为空");
			return;
		}

		/*
		 * if(nsrsbh==null||"".equals(nsrsbh)){ rtn.setCode(-1);
		 * rtn.setMessage("开具发票时纳税人识别号为空"); return; }
		 */

		try {

			PostMethod postMethod = new PostMethod(serviceUrl);

			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
			postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000);
			postMethod.setRequestBody(xml);
			int statusCode = 0;
			HttpClient httpClient = new HttpClient();

			statusCode = httpClient.executeMethod(postMethod);

			if (statusCode == 200) {
				// System.out.println("200 success ");
				String tokeninfo = postMethod.getResponseBodyAsString();
				// System.out.println(tokeninfo);

				if (StringUtils.isEmpty(tokeninfo)) {
					rtn.setCode(-1);
					rtn.setMessage("返回的数据为空");

					return;
				} else {
					rtn.setMessage(tokeninfo);
				}

			} else {
				System.out.println("500 error ");
				rtn.setCode(-1);
				rtn.setMessage("返回数据错误 状态 " + statusCode);
			}
			/*
			 * } else { System.out.println("other error "); rtn.setCode(-1);
			 * rtn.setMessage("返回数据错误 状态 "+statusCode); return; }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void wbservice(String xml, Invque invque, Taxinfo taxinfo, String jklx, RtnData rtn)
			throws Exception {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

		factory.setServiceClass(WbServiceI.class);
		factory.setAddress(taxinfo.getItfserviceUrl());
		// factory.setAddress("http://222.174.170.5:10005/FPGLXT_FW/CXF/WbService?wsdl");

		WbServiceI service = (WbServiceI) factory.create();

		// SAXBuilder reader = new SAXBuilder();

		// Reader returnQuote = new StringReader(xml);
		// Document document=reader.build(returnQuote);
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");// 设置编码格式
		if ("0".equals(jklx)) {
			rtn.setMessage(service.sendData(xml));
		} else if ("1".equals(jklx)) {
			rtn.setMessage(service.getData(invque.getIqseqno(), invque.getRtfpdm(), invque.getRtfphm()));
		}

		/*
		 * StringWriter out=null; //输出对象 String sReturn =""; //输出字符串
		 * XMLOutputter outputter =new XMLOutputter(); out=new StringWriter();
		 * outputter.output(document,out); sReturn=out.toString();
		 */

	}

	/**
	 * 发送数据信息
	 **/
	public void sendQzData(String methodName, String xml, RtnData rtn) {

		if (serviceUrl == null || "".equals(serviceUrl)) {
			rtn.setCode(-1);
			rtn.setMessage("URL为空");
			return;
		}

		if (nsrsbh == null || "".equals(nsrsbh)) {
			rtn.setCode(-1);
			rtn.setMessage("开具发票时纳税人识别号为空");
			return;
		}

		if (methodName == null || "".equals(methodName)) {
			rtn.setCode(-1);
			rtn.setMessage("访问的方法名称为空");
			return;
		}

		try {

			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient(serviceUrl);
			// getUser 为接口中定义的方法名称 张三为传递的参数 返回一个Object数组
			Object[] objects = client.invoke(methodName, xml);
			// 输出调用结果
			rtn.setMessage(objects[0].toString());
			// System.out.println("*****"+objects[0].toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
