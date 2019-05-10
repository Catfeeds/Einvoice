package baiwang.invoice.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;
import com.invoice.util.XMLConverter;

import baiwang.invoice.bean.BaiWangInvoiceInValidBean;
import baiwang.invoice.bean.BaiWangInvoicePrintBean;
import baiwang.invoice.bean.BaiWangInvoiceSaleDetailBean;
import baiwang.invoice.bean.BaiWangInvoiceSaleHeadBean;
import baiwang.invoice.bean.BaiWangRtBlankBean;
import baiwang.invoice.bean.BaiWangRtInvoiceBean;
import baiwang.invoice.bean.RtInvoiceHeadList;

public class BaiWangGenerateBean {
	static public final Log log = LogFactory.getLog(BaiWangGenerateBean.class);
	//纳税人识别号
	String nsrsbh;
	//接入代码
	String jrdm;
	
	public BaiWangGenerateBean(String nsrsbh,String jrdm ){
		this.nsrsbh = nsrsbh;
		this.jrdm = jrdm;
	}
	
	/**
	 * 开具发票
	 * 装换为百旺接口对象 
	 * **/
	public void generateBaiWangBean(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,String fwdm,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对百旺数据对象定义
		 * head 开票头
		 * bwDetailList 开票明细集合
		 * **/
		BaiWangInvoiceSaleHeadBean head = new BaiWangInvoiceSaleHeadBean();
		List<BaiWangInvoiceSaleDetailBean> bwDetailList = new ArrayList<BaiWangInvoiceSaleDetailBean>();
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
			rtn.setMessage("小票数据集合 detailList 没有初始化");
			return;
		}
		//开票头转换
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		}else{
			head.setFpqqlsh(invque.getIqseqno());//发票流水号
		}
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票点编码不能为空");
			return;
		}else{
			head.setKpddm(invque.getIqtaxzdh());//开票点编码
		}
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return;
		}else{
			head.setEqpttype(taxinfo.getItfeqpttype());//设备类型
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return;
		}else{
			head.setFplxdm(invque.getIqfplxdm());//发票种类编码
		}
		
		head.setTspz("00");//特殊票种标识
		
		if(invque.getIqgmfname()==null||"".equals(invque.getIqgmfname())){
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return;
		}else{
			head.setGhdwmc(invque.getIqgmfname());//购方客户名称
		}
		
		
		head.setGhdwdm(invque.getIqgmftax());//购方单位代码
		head.setGhdwdzdh(invque.getIqgmfadd());//购方地址及电话
		head.setGhdwyhzh(invque.getIqgmfbank());//购方开户行及账号
		head.setSkr(invque.getIqpayee()==null?"":invque.getIqpayee());//收款人
		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return;
		}else{
			head.setKpr(invque.getIqadmin());//开票人
		}
		
		head.setFhr(invque.getIqchecker()==null?"":invque.getIqchecker());//审核人
		
		if(invque.getIqtype()==0||invque.getIqtype()==1){
			head.setKplx(String.valueOf(invque.getIqtype()));//开票类型
		}else{
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return;
		}
		
		if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
			if(detailList.size()>=8)
				head.setQdbz("1");//清单标志
			else
				head.setQdbz("0");
		}else{
			head.setQdbz("0");
		}
//		if(detailList.size()>=8)
//			head.setQdbz("1");//清单标志
//		else
		//百望电子发票写死为0
		
		
		
		head.setHzxxId("");//红字信息表编号
		
		if(invque.getIqtype()==1&&!invque.getIqfplxdm().equals("004")){
			
			if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return;
			}else{
				head.setYfpdm(invque.getIqyfpdm());//原发票代码
			}
			
			if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return;
			}else{
				head.setYfphm(invque.getIqyfphm());//原发票号码
			}
			
			head.setHjje(MathCal.add(invque.getIqtotje()*-1,0,2));//合计金额
			head.setSe(MathCal.add(invque.getIqtotse()*-1,0,2));//合计税额
			head.setJshj((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1);//价税合计
		
		}else{
			head.setYfpdm(invque.getIqyfpdm());//原发票代码
			head.setYfphm(invque.getIqyfphm());//原发票号码
			
			head.setHjje(MathCal.add(invque.getIqtotje(),0,2));//合计金额
			head.setSe(MathCal.add(invque.getIqtotse(),0,2));//合计税额
			head.setJshj(MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2));//价税合计
		}
		
		if(invque.getZsfs()==null||"".equals(invque.getZsfs())){
			rtn.setCode(-1);
			rtn.setMessage("征税方式不能为空");
			return;
		}else{
			head.setZsfs(invque.getZsfs());//征税方式
		}
		
		head.setKce("");//扣除额
		

		
		head.setBz(invque.getIqmemo());//备注
		head.setQmcs("");//签名值参数
		if("1".equals(taxinfo.getItfeqpttype())){
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return;
			}else{
				head.setSkpbh(taxinfo.getItfskpbh());//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return;
			}else{
				head.setSkpkl(taxinfo.getItfskpkl());//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return;
			}else{
				head.setKeypwd(taxinfo.getItfkeypwd());//税务数字证书密码
			}
		}
		head.setBbh(taxinfo.getItfbbh());//商品编码版本号
		
		head.setTzdbh("");//通知单编号
		head.setZhsl("");//综合税率
		//head.setKppy("01");//定义卷票开票打印的纸张大小
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
					BaiWangInvoiceSaleDetailBean bwdetail = new BaiWangInvoiceSaleDetailBean();
					if(detail.getFphxz()==null||"".equals(detail.getFphxz())){
						detail.setFphxz("0");
					}
					rownum =openGoodsDetail(detail, bwdetail,detailList, invque.getIqtype(), rownum ,bwDetailList);
					//if(invque.getIqtype()==1&&!"1".equals(detail.getFphxz())||invque.getIqtype()==0){
						
/*						bwdetail.setFpmxxh(String.valueOf(rownum++));//明细行号
						bwdetail.setSpbm(detail.getTaxitemid());//商品编码
						bwdetail.setSpmc(detail.getGoodsname());//商品名称
						bwdetail.setSpsm(detail.getTaxitemid());//商品税目
						bwdetail.setGgxh("");//规格型号
						bwdetail.setDw(detail.getUnit());//计量单位
						
						if(invque.getIqtype()==1){
							if(detail.getQty()!=null)
							bwdetail.setSpsl(String.valueOf(detail.getQty()*-1));//商品数量
							bwdetail.setFphxz("0");//发票行性质
							bwdetail.setZhdyhh("");
							if("0".equals(detail.getFphxz())){
									bwdetail.setJe(String.valueOf(detail.getAmount()*-1));//金额
									bwdetail.setSe(String.valueOf(detail.getTaxfee()*-1));//税额
							}else{
								if("2".equals(detail.getFphxz())){
									for(InvoiceSaleDetail detail1:detailList){
										if("1".equals(detail1.getFphxz())){
											if(detail.getSeqno().equals(Integer.parseInt(detail1.getSeqno())-1)&& detail.getGoodsid().equals(detail1.getGoodsid())&&String.valueOf(detail.getRowno()).equals(detail1.getZhdyhh())){
												bwdetail.setJe(String.valueOf((detail.getAmount()+detail1.getAmount())*-1));//金额
												bwdetail.setSe(String.valueOf((detail.getTaxfee()+detail1.getTaxfee())*-1));//税额
											}
										}
									}
								}
							}
						
						}else{
							bwdetail.setFphxz(detail.getFphxz());//发票行性质
							bwdetail.setZhdyhh(detail.getZhdyhh());
							bwdetail.setSpsl(String.valueOf(detail.getQty()));//商品数量
							bwdetail.setJe(String.valueOf(detail.getAmount()));//金额
							bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额
						}
						
						if(detail.getPrice()==null){
							bwdetail.setSpdj(String.valueOf(detail.getPrice()));//detail.getPrice();单价
						}else{
							bwdetail.setSpdj(String.valueOf(MathCal.add(detail.getPrice(),0,6)));//detail.getPrice();单价
						}
						bwdetail.setSl(String.valueOf(detail.getTaxrate()));//税率
						bwdetail.setHsbz("0");//含税标志
						bwdetail.setZzstsgl(detail.getTaxprecon());//增值税特殊管理
						bwdetail.setYhzcbs(detail.getTaxpre());//是否使用优惠政策
						bwdetail.setLslbs(detail.getZerotax());//免税类型
*/						
						//bwDetailList.add(bwdetail);
					//}
				}
			}
		}
		
		if("Y".equals(flag)){
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return;
		}
		
		head.setDetailList(bwDetailList);
		
		String dataRequest = XMLConverter.objectToXml(head, "utf-8") ;	
		
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}
		baiWangBeanToXml(dataRequest,fwdm,rtn);
		
	}
	
	public int openGoodsDetail(InvoiceSaleDetail detail,BaiWangInvoiceSaleDetailBean bwdetail,List<InvoiceSaleDetail> detailList,int iqtype,int rownum,List<BaiWangInvoiceSaleDetailBean> bwDetailList ){
			if(iqtype==0){
				bwdetail.setFphxz(detail.getFphxz());//发票行性质
				
				if("2".equals(detail.getFphxz())){
					bwdetail.setZhdyhh(String.valueOf(rownum+1));
					detail.setZhdyhh(rownum+1);
				}else
				if("1".equals(detail.getFphxz())){
					bwdetail.setZhdyhh(String.valueOf(rownum-1));
					detail.setZhdyhh(rownum-1);
				}else{
					bwdetail.setZhdyhh("");
				}
				
				bwdetail.setFpmxxh(String.valueOf(rownum));//明细行号
				bwdetail.setSpbm(detail.getTaxitemid());//商品编码
				bwdetail.setSpmc(detail.getGoodsname());//商品名称
				bwdetail.setSpsm(detail.getTaxitemid());//商品税目
				if(!"1".equals(detail.getFphxz())){
					bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
					bwdetail.setDw(detail.getUnit());//计量单位
					
					bwdetail.setSpsl(String.valueOf(detail.getQty()));//商品数量
					if(detail.getPrice()==null){
						bwdetail.setSpdj("");//detail.getPrice();单价
					}else{
						bwdetail.setSpdj(String.valueOf(MathCal.add(detail.getPrice(),0,6)));//detail.getPrice();单价
					}
				}
				bwdetail.setJe(String.valueOf(detail.getAmount()));//金额
				bwdetail.setSe(String.valueOf(detail.getTaxfee()));//税额

				bwdetail.setSl(String.valueOf(detail.getTaxrate()));//税率
				bwdetail.setHsbz("0");//含税标志
				bwdetail.setZzstsgl(detail.getTaxprecon());//增值税特殊管理
				bwdetail.setYhzcbs(detail.getTaxpre());//是否使用优惠政策
				bwdetail.setLslbs(detail.getZerotax());//免税类型
				bwDetailList.add(bwdetail);
				detail.setSeqno(String.valueOf(rownum));
				rownum++;

			}else
			if(iqtype==1){
				
				if(!"1".equals(detail.getFphxz())){
					bwdetail.setFpmxxh(String.valueOf(rownum));//明细行号
					bwdetail.setSpbm(detail.getTaxitemid());//商品编码
					bwdetail.setSpmc(detail.getGoodsname());//商品名称
					bwdetail.setSpsm(detail.getTaxitemid());//商品税目
					bwdetail.setGgxh(detail.getSpec()==null?"":detail.getSpec());//规格型号
					bwdetail.setDw(detail.getUnit());//计量单位
					
					//if(detail.getQty()!=null)
					//bwdetail.setSpsl(String.valueOf(detail.getQty()*-1));//商品数量
					bwdetail.setFphxz("0");//发票行性质
					bwdetail.setZhdyhh("");
		
					if("2".equals(detail.getFphxz())){
						for(InvoiceSaleDetail detail1:detailList){
							if("1".equals(detail1.getFphxz())){
								if(detail.getSeqno().equals(String.valueOf(Integer.parseInt(detail1.getSeqno())-1))&& detail.getGoodsid().equals(detail1.getGoodsid())&&detail.getRowno()==detail1.getZhdyhh()){
									bwdetail.setJe(String.valueOf(MathCal.add(detail.getAmount(),detail1.getAmount(),2)*-1));//金额
									bwdetail.setSe(String.valueOf(MathCal.add(detail.getTaxfee(),detail1.getTaxfee(),2)*-1));//税额
									 
									break;
								}
							}
						}
					}else{
						bwdetail.setJe(String.valueOf(detail.getAmount()*-1));//金额
						bwdetail.setSe(String.valueOf(detail.getTaxfee()*-1));//税额
					}

					/*if(detail.getPrice()==null){
						bwdetail.setSpdj("");//detail.getPrice();单价
					}else{
						bwdetail.setSpdj(String.valueOf(MathCal.add(detail.getPrice(),0,6)));//detail.getPrice();单价
					}*/
					bwdetail.setSl(String.valueOf(detail.getTaxrate()));//税率
					bwdetail.setHsbz("0");//含税标志
					bwdetail.setZzstsgl(detail.getTaxprecon());//增值税特殊管理
					bwdetail.setYhzcbs(detail.getTaxpre());//是否使用优惠政策
					bwdetail.setLslbs(detail.getZerotax());//免税类型
					bwDetailList.add(bwdetail);
					//detail.setSeqno(String.valueOf(rownum));
					rownum++;

				}
			}
			return rownum;
	}
	
	/**
	 * 开具发票返回后的发票数据解析
	 * **/

	public BaiWangRtInvoiceBean rtOpenToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		BaiWangRtInvoiceBean rtinvoicebean =new BaiWangRtInvoiceBean();
		XmlUtil util= new XmlUtil();  
		Map map= util.xmlToMap(xml);
		if(map==null||map.size()==0){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回数据转换对象时错误");
			return null;
		}

		if("0000".equals(map.get("rtncode"))){
			rtinvoicebean =  (BaiWangRtInvoiceBean) util.xmlStrToBean((String)map.get("rtndata"),BaiWangRtInvoiceBean.class);
			//System.out.println(rtinvoicebean.getFpdm()+" "+rtinvoicebean.getFphm()+" "+rtinvoicebean.getSkm()+" "+rtinvoicebean.getEym()+" "+rtinvoicebean.getJym()+" "+rtinvoicebean.getKprq());	
		}else{
			rtn.setCode(-1);
			rtn.setMessage((String)map.get("rtnmsg"));
			return null;
		}
		return rtinvoicebean;
	}
	
	/**
	 * 开具发票查询(不分页)
	 * 根据发票代码和发票号码查询
	 * **/	
	
	public void findInvoice(Invque invque,String fwdm,RtnData rtn){
		
		StringBuffer xml = new StringBuffer();
		
		/*
		 * 查询类型
		 * 1 (流水号不为空，其他忽略)
		 *2发票号码和发票代码查询
		*  (发票代码号码不为空，其他忽略)
		* 3纳税人识别号【销方】
		* (纳税人识别号不为空，其他忽略)
		* 4开票终端标识
		* (开票终端不为空，其他忽略)
		* 5开票日期起止
		* (开票日期起止不为空，其他忽略)
		* 6.购方名称
		* (购方名称或购方税号，其他忽略)
		 * */
		if(invque.getIqseqno()!=null&&!"".equals(invque.getIqseqno())){
			xml.append("<cxlx>1</cxlx>");//查询类型
			xml.append("<fpqqlsh>"+ invque.getIqseqno() +"</fpqqlsh>");//发票代码
		}else{
			xml.append("<cxlx>2</cxlx>");//查询类型
			if(invque.getRtfpdm() ==null||"".equals(invque.getRtfpdm() )){
				rtn.setCode(-1);
				rtn.setMessage("发票代码不能为空");
				return;
			}else{  
				xml.append("<fpdm>"+ invque.getRtfpdm() +"</fpdm>");//发票代码
			}
			
			if(invque.getRtfphm() ==null||"".equals(invque.getRtfphm() )){
				rtn.setCode(-1);
				rtn.setMessage("发票号码不能为空");
				return;
			}else{
				xml.append("<fphm>"+ invque.getRtfphm() +"</fphm>");//发票号码
			}
		}
		
		
		
		baiWangBeanToXml(xml.toString(),fwdm,rtn);
		
		
	}
	
	/**
	 * 发票查询返回后的发票数据解析
	 * **/

	public RtInvoiceHeadList rtFindToBean(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("查询发票返回为空");
			return null;
		}
		RtInvoiceHeadList list = new RtInvoiceHeadList();
		
/*		BaiWangRtInvoiceHeadBean rtinvoicebean =new BaiWangRtInvoiceHeadBean();
		List<BaiWangRtInvoiceDetailBean> bwDetailList = new ArrayList<BaiWangRtInvoiceDetailBean>();
		BaiWangRtInvoiceDetailBean rtinvoiceDetailBean = new BaiWangRtInvoiceDetailBean();
		*/
		XmlUtil util= new XmlUtil();  
		Map map= util.xmlToMap(xml);
		if(map==null||map.size()==0){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回数据转换对象时错误");
			return null;
		}
		Document document;
		try{
			
		if("0000".equals(map.get("rtncode"))){
		       JAXBContext jc = JAXBContext.newInstance( RtInvoiceHeadList.class);
		       Unmarshaller u = jc.createUnmarshaller();
		       StringBuffer xmlStr = new StringBuffer((String)map.get("rtndata"));
		       list = (RtInvoiceHeadList) u.unmarshal( new StreamSource( new StringReader( xmlStr.toString() ) ) );
		       
/*		       for(BaiWangRtInvoiceHeadBean head:list.getHeads()){
		    	   System.out.println(head.getFpqqlsh());
		    	   System.out.println(head.getFpdm());
		    	   System.out.println(head.getFphm());
		    	   for(BaiWangRtInvoiceDetailBean detail : head.getFpmxList().getDetails()){
		    		   System.out.println(detail.getSe());
		    		   System.out.println(detail.getSl());
		    		   System.out.println(detail.getSpmc());
		    		   System.out.println(detail.getSpsm());
		    	   }
		       }*/
		       
		}else{
			rtn.setCode(-1);
			rtn.setMessage((String)map.get("rtnmsg"));
			return null;
		}
		}catch(Exception e){
			
		}
		return list;
	}
	
	
	/**
	 * 获取pdf连接参数转换
	 * **/
	
	public void generatePdf(Invque invque,String fwdm,RtnData rtn){
		//判断邮箱电话是否都填写
		int flag = 0;
		StringBuffer str = new StringBuffer();
		
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("开票申请流水号不能为空");
			return;
		}else{
			str.append("<fpqqlsh>" + invque.getIqseqno() + "</fpqqlsh>");
		}
		
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
		
		
		if(invque.getIqtel()!=null&&!"".equals(invque.getIqtel())){
			flag = 1;
			str.append("<gfkhdh>" + invque.getIqtel() + "</gfkhdh>");
			
		}
		
		if(invque.getIqemail()!=null&&!"".equals(invque.getIqemail())){
			flag = 1;
			str.append("<gfkhyx>" + invque.getIqemail() + "</gfkhyx>");
		}

		str.append("<tsbz>" + flag + "</tsbz>");
		
		System.out.println(str.toString());
		if(str!=null&&!"".equals(str.toString())){
			baiWangBeanToXml(str.toString(), fwdm, rtn);
		}else{
			rtn.setCode(-1);
			rtn.setMessage("获取的数据有问题");
		}
	}
	
	/**
	 * 获取pdf连接参数转换
	 * **/
	
	public void searchPdf(Invque invque,String fwdm,String cxlx ,String fhbz,RtnData rtn){
		//判断邮箱电话是否都填写
		int flag = 1;
		StringBuffer str = new StringBuffer();
		
		if(cxlx==null||"".equals(cxlx)){
			str.append("<cxlx>0</cxlx>");
		}
		str.append("<cxlx>" + cxlx + "</cxlx>");
		
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("开票申请流水号不能为空");
			return;
		}else{
			str.append("<fpqqlsh>" + invque.getIqseqno() + "</fpqqlsh>");
		}
		
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
		
		if(fhbz==null||"".equals(fhbz)){
			str.append("<fhbz>1</fhbz>");
		}
		str.append("<fhbz>" + fhbz + "</fhbz>");
		
		if(str!=null&&!"".equals(str.toString())){
			baiWangBeanToXml(str.toString(), fwdm, rtn);
		}else{
			rtn.setCode(-1);
			rtn.setMessage("获取的数据有问题");
		}
	}
	
	public String rtPdfToString(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回为空");
			return null;
		}
		String rtPdf ="";
		XmlUtil util= new XmlUtil();  
		Map map= util.xmlToMap(xml);
		if(map==null||map.size()==0){
			rtn.setCode(-1);
			rtn.setMessage("开具发票返回数据转换对象时错误");
			return null;
		}

		if("0".equals(map.get("rtncode"))){
			rtPdf = (String)map.get("rtndata");
		}else{
			rtn.setCode(-1);
			rtn.setMessage((String)map.get("rtnmsg"));
			return null;
		}
		return rtPdf;
		
	}
	
	/**
	 * 空白发票作废
	 * 转换为百旺对象
	 * **/
	public void generateInvoiceblankInValid(Invque invque,Taxinfo taxinfo,String fwdm,RtnData rtn){
		generateInvoiceInValid(invque,taxinfo,fwdm,rtn,"0");
	}
	
	/**
	 * 已开发票作废
	 * 转换为百旺对象
	 * **/
	public void generateInvoiceYkInValid(Invque invque,Taxinfo taxinfo,String fwdm,RtnData rtn){
		generateInvoiceInValid(invque,taxinfo,fwdm,rtn,"1");
	}
	
	/**
	 * 发票作废
	 * 转换为百旺对象
	 * **/
	public void generateInvoiceInValid(Invque invque,Taxinfo taxinfo,String fwdm,RtnData rtn,String Zflx){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
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
		BaiWangInvoiceInValidBean invoiceInValid = new BaiWangInvoiceInValidBean();
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return;
		}else{
			invoiceInValid.setSblx(taxinfo.getItfeqpttype());//设备类型
		}
		
		invoiceInValid.setZflx(Zflx); //作废类型 0空白票作废 1已开票作废
		
		if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			rtn.setCode(-1);
			rtn.setMessage("纳税人识别号不能为空");
			return;
		}else{
			invoiceInValid.setNsrsbh(taxinfo.getTaxno());//纳税人识别号
		}

		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票终端标识不能为空");
			return;
		}else{
			invoiceInValid.setKpzdbs(invque.getIqtaxzdh());//开票终端标识 开票终端唯一性标识 Sblx为0时必填
		}
		

		invoiceInValid.setKpzdbs(invque.getIqtaxzdh());//开票终端标识 开票终端唯一性标识 Sblx为0时必填


		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票类型代码不能为空");
			return;
		}else{
			invoiceInValid.setFplxdm(invque.getIqfplxdm());//发票类型代码
		}
		
		if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票代码不能为空");
			return;
		}else{
			invoiceInValid.setFpdm(invque.getRtfpdm());//发票代码
		}
		
		if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
			rtn.setCode(-1);
			rtn.setMessage("发票号码不能为空");
			return;
		}else{
			invoiceInValid.setFphm(invque.getRtfphm());//发票号码
		}
		
		if("1".equals(taxinfo.getItfeqpttype())){
			
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return;
			}else{
				invoiceInValid.setSkpbh(taxinfo.getItfskpbh());//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return;
			}else{
				invoiceInValid.setSkpkl(taxinfo.getItfskpkl());//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return;
			}else{
				invoiceInValid.setKeypwd(taxinfo.getItfkeypwd());//税务数字证书密码
			}
			
		}else{
			invoiceInValid.setSkpbh(taxinfo.getItfskpbh());//税控盘编号
			invoiceInValid.setSkpkl(taxinfo.getItfskpkl());//税控盘口令
			invoiceInValid.setKeypwd(taxinfo.getItfkeypwd());//税务数字证书密码
		}
		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("作废人不能为空");
			return;
		}else{
			invoiceInValid.setZfr(invque.getIqadmin());//作废人
		}
		
		invoiceInValid.setQmcs("0000004282000000");
		
		String dataRequest = XMLConverter.objectToXml(invoiceInValid, "utf-8") ;	
		
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}
		
		if(dataRequest.contains("<fpzf>")){
			dataRequest =dataRequest.replace("<fpzf>", "");
			dataRequest =dataRequest.replace("</fpzf>", "");
		}

		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}
		//System.out.println(dataRequest);
		baiWangBeanToXml(dataRequest,fwdm,rtn);
		
	}
	
	public BaiWangRtInvoiceBean rtInvoiceInValid(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("发票作废返回为空");
			return null;
		}

		XmlUtil util= new XmlUtil();  
		Map map= util.xmlToMap(xml);
		if(map==null||map.size()==0){
			rtn.setCode(-1);
			rtn.setMessage("发票作废返回数据转换对象时错误");
			return null;
		}
		BaiWangRtInvoiceBean rtInvoice = new BaiWangRtInvoiceBean();
		
		if("0000".equals(map.get("rtncode"))){
			
			rtInvoice.setFpdm((String)map.get("fpdm"));
			rtInvoice.setFphm((String)map.get("fphm"));
			rtInvoice.setKprq((String)map.get("zfrq"));
		}else{
			 
			rtn.setCode(-1);
			rtn.setMessage((String)map.get("rtnmsg"));
			return null;
		}
		log.info("rtInvoiceInValid end !"+rtn.getCode());
		return rtInvoice;
		
	}
	
	/**
	 * 空白发票查询
	 * **/
	public void blankInvoice(Invque invque,Taxinfo taxinfo,String fwdm,RtnData rtn){
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
		//Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return;
		}
		
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
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return;
		}else{
			xml.append("<sblx>"+ taxinfo.getItfeqpttype() +"</sblx>");//设备类型 0税控服务 1税控盘
		}
		
		if("1".equals(taxinfo.getItfeqpttype())){
			
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return;
			}else{
				xml.append("<skpbh>"+ taxinfo.getItfskpbh() +"</skpbh>");//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return;
			}else{
				xml.append("<skpkl>"+ taxinfo.getItfskpkl() +"</skpkl>");//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return;
			}else{
				xml.append("<keypwd>"+ taxinfo.getItfkeypwd() +"</keypwd>");//税务数字证书密码
			}
			
		}else{
			xml.append("<skpbh>"+ taxinfo.getItfskpbh() +"</skpbh>");//税控盘编号
			xml.append("<skpkl>"+ taxinfo.getItfskpkl() +"</skpkl>");//税控盘口令
			xml.append("<keypwd>"+ taxinfo.getItfkeypwd() +"</keypwd>");//税务数字证书密码
		}
		
		xml.append("<qmcs>0000004282000000</qmcs>");//税务数字证书密码
		
		baiWangBeanToXml(xml.toString(),fwdm,rtn);
		
		
	}
	
	
	public BaiWangRtBlankBean rtBlankInvoice(String xml,RtnData rtn){
		if(xml==null||"".equals(xml)){
			rtn.setCode(-1);
			rtn.setMessage("查询空白发票返回为空");
			return null;
		}

		XmlUtil util= new XmlUtil();  
		Map map= util.xmlToMap(xml);
		if(map==null||map.size()==0){
			rtn.setCode(-1);
			rtn.setMessage("查询空白发票返回数据转换对象时错误");
			return null;
		}
		BaiWangRtBlankBean rtInvoice = new BaiWangRtBlankBean();
		
		if("0000".equals(map.get("rtncode"))){
			rtInvoice =  (BaiWangRtBlankBean) util.xmlStrToBean((String)map.get("rtndata"),BaiWangRtBlankBean.class);
			System.out.println(rtInvoice.getDqfpdm());	
		}else{
			rtn.setCode(-1);
			rtn.setMessage((String)map.get("rtnmsg"));
			return null;
		}
		
		return rtInvoice;
		
	}
	
	
	/**
	 * 发票打印
	 * 转换为百旺对象
	 * **/
	public void generateInvoicePrint(Invque invque,Taxinfo taxinfo,String fwdm,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
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
		BaiWangInvoicePrintBean invoicePrint = new BaiWangInvoicePrintBean();
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return;
		}else{
			invoicePrint.setEqpttype(taxinfo.getItfeqpttype());//设备类型
		}

		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return;
		}else{
			invoicePrint.setFpqqlsh(invque.getIqseqno());//发票流水号
		}
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票终端标识不能为空");
			return;
		}else{
			invoicePrint.setKpddm(invque.getIqtaxzdh());//开票终端标识 开票终端唯一性标识 Sblx为0时必填
		}
		
		if("1".equals(taxinfo.getItfeqpttype())){
			
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return;
			}else{
				invoicePrint.setSkpbh(taxinfo.getItfskpbh());//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return;
			}else{
				invoicePrint.setSkpkl(taxinfo.getItfskpkl());//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return;
			}else{
				invoicePrint.setKeypwd(taxinfo.getItfkeypwd());//税务数字证书密码
			}
			
			if(invque.getIsList()==null){
				rtn.setCode(-1);
				rtn.setMessage("打印类型不能为空");
				return;
			}else{
				invoicePrint.setDylx(invque.getIsList()+"");//打印类型
			}
			
			invoicePrint.setDyfs("1");//打印方式
			
		}else{
			invoicePrint.setDylx(invque.getIsList()+"");//打印类型
			invoicePrint.setSkpbh(taxinfo.getItfskpbh());//税控盘编号
			invoicePrint.setSkpkl(taxinfo.getItfskpkl());//税控盘口令
			invoicePrint.setKeypwd(taxinfo.getItfkeypwd());//税务数字证书密码
		}
		invoicePrint.setDyfs("1");//打印方式
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票类型代码不能为空");
			return;
		}else{
			invoicePrint.setFplxdm(invque.getIqfplxdm());//发票类型代码
		}
		
		
		if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票代码不能为空");
			return;
		}else{
			invoicePrint.setFpdm(invque.getRtfpdm());//发票代码
		}
		
		if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
			rtn.setCode(-1);
			rtn.setMessage("发票号码不能为空");
			return;
		}else{
			invoicePrint.setFphm(invque.getRtfphm());//发票号码
		}
		

		String dataRequest = XMLConverter.objectToXml(invoicePrint, "utf-8") ;	
		
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}
		//System.out.println(dataRequest);
		baiWangBeanToXml(dataRequest,fwdm,rtn);
		
	}
	
	/**
	 * 装换为XML格式
	 * **/
	
	public void  baiWangBeanToXml(String dataRequest,String fwdm,RtnData rtn ){
		String  xmlData =""; 
		
		if(fwdm ==null||"".equals(fwdm)){
			rtn.setCode(-1);
			rtn.setMessage("服务代码为空");
			return;
		}
		if(nsrsbh ==null||"".equals(nsrsbh)){
			rtn.setCode(-1);
			rtn.setMessage("纳税人识别号为空");
			return;
		}
		
		if(jrdm ==null||"".equals(jrdm)){
			rtn.setCode(-1);
			rtn.setMessage("接入代码为空");
			return;
		}
		
		
		if(dataRequest ==null||"".equals(dataRequest)){
			rtn.setCode(-1);
			rtn.setMessage("开票数据装换XML失败");
			return;
		}
		
		xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<fwpackage>"+
				"<jsnr>"+
					"<fwdm>"+fwdm+"</fwdm>"+
					"<nsrsbh>"+nsrsbh+"</nsrsbh>"+
					"<jrdm>"+jrdm+"</jrdm>"+
				"</jsnr>" +
				"<ywnr><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
				"<request>" +
				dataRequest +
				 "</request>]]></ywnr>"+
					"</fwpackage>";
		//System.out.println(xmlData);
		if(xmlData ==null||"".equals(xmlData)){
			rtn.setCode(-1);
			rtn.setMessage("生成的XML为空");
			return;
		}else{
			
		rtn.setMessage(xmlData);
		}
		log.debug(xmlData);
		//return xmlData;
		
	}
	
}
