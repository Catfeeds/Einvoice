package com.invoice.port.bwgf.invoice.service;

import java.util.ArrayList;
import java.util.List;

import com.baiwang.bop.request.impl.invoice.common.InvoiceDetails;
import com.baiwang.bop.request.impl.invoice.impl.BlankInvoiceQueryRequest;
import com.baiwang.bop.request.impl.invoice.impl.FormatfileBuildRequest;
import com.baiwang.bop.request.impl.invoice.impl.FormatfileQueryRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoiceInvalidRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoiceOpenRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoicePrintRequest;
import com.baiwang.bop.request.impl.invoice.impl.InvoiceQueryRequest;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;
import com.invoice.util.MathCal;

public class BwgfGenerateBean {
	/**
	 * 开具发票
	 * 装换为百旺接口对象 
	 * **/
	public InvoiceOpenRequest generateBaiWangBean(Invque invque,Taxinfo taxinfo,List<InvoiceSaleDetail> detailList,RtnData rtn){
		//对象定义和初始化
		/**
		 * 对百旺数据对象定义
		 * head 开票头
		 * bwDetailList 开票明细集合
		 * **/
		InvoiceOpenRequest head = new InvoiceOpenRequest();
		List<InvoiceDetails> bwDetailList = new ArrayList<InvoiceDetails>();
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
			return null;
		}
		//Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return null;
		}
		//List<InvoiceSaleDetail>  detailList= (ArrayList)map.get("detailList");
		if(detailList ==null){
			rtn.setCode(-1);
			rtn.setMessage("小票数据集合 detailList 没有初始化");
			return null;
		}
		//开票头转换
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			rtn.setCode(-1);
			rtn.setMessage("发票流水号不能为空");
			return null;
		}else{
			head.setSerialNo(invque.getIqseqno());//发票流水号
		}
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票点编码不能为空");
			return null;
		}else{
			head.setInvoiceTerminalCode(invque.getIqtaxzdh());//开票点编码
		}
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return null;
		}else{
			head.setDeviceType(taxinfo.getItfeqpttype());//设备类型
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return  null;
		}else{
			head.setInvoiceTypeCode(invque.getIqfplxdm());//发票种类编码
		}
		
		head.setInvoiceSpecialMark("00");//特殊票种标识
		
		if(invque.getIqgmfname()==null||"".equals(invque.getIqgmfname())){
			rtn.setCode(-1);
			rtn.setMessage("购方客户名称不能为空");
			return null;
		}else{
			head.setBuyerName(invque.getIqgmfname());//购方客户名称
		}
		
		if(invque.getIqgmftax()==null||"".equals(invque.getIqgmftax())){
			head.setBuyerTaxNo("");//购方单位代码
		}else{
			head.setBuyerTaxNo(invque.getIqgmftax());//购方单位代码
		}
		if(invque.getIqgmfadd()==null||"".equals(invque.getIqgmfadd())){
			head.setBuyerAddressPhone("");//购方地址及电话
		}else{
			head.setBuyerAddressPhone(invque.getIqgmfadd());//购方地址及电话
		}
		if(invque.getIqgmfbank()==null||"".equals(invque.getIqgmfbank())){
			head.setBuyerBankAccount("");//购方开户行及账号
		}else{
			head.setBuyerBankAccount(invque.getIqgmfbank());//购方开户行及账号
		}
		
		if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			rtn.setCode(-1);
			rtn.setMessage("销方纳税号不能为空");
			return null;
		}else{
		head.setSellerTaxNo(taxinfo.getTaxno());
		}
		
		head.setPayee(invque.getIqpayee()==null?"":invque.getIqpayee());//收款人
		head.setChecker(invque.getIqchecker()==null?"":invque.getIqchecker());//审核人
		
		if(invque.getIqadmin()==null||"".equals(invque.getIqadmin())){
			rtn.setCode(-1);
			rtn.setMessage("开票人不能为空");
			return null;
		}else{
			head.setDrawer(invque.getIqadmin());//开票人
		}
		
		if(invque.getIqtype()==0||invque.getIqtype()==1){
			head.setInvoiceType(String.valueOf(invque.getIqtype()));//开票类型
		}else{
			rtn.setCode(-1);
			rtn.setMessage("开票类型不正确");
			return null;
		}
		
		if("004".equals(invque.getIqfplxdm())||"007".equals(invque.getIqfplxdm())){
			if(detailList.size()>=8)
				head.setInvoiceListMark("1");//清单标志
			else
				head.setInvoiceListMark("0");
		}else{
			head.setInvoiceListMark("0");
		}
//		if(detailList.size()>=8)
//			head.setQdbz("1");//清单标志
//		else
		//百望电子发票写死为0
		
		
		
		head.setRedInfoNo("");//红字信息表编号
		
		if(invque.getIqtype()==1&&!invque.getIqfplxdm().equals("004")){
			
			if(invque.getIqyfpdm()==null||"".equals(invque.getIqyfpdm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票代码不能为空");
				return null;
			}else{
				head.setOriginalInvoiceCode(invque.getIqyfpdm());//原发票代码
			}
			
			if(invque.getIqyfphm()==null||"".equals(invque.getIqyfphm())){
				rtn.setCode(-1);
				rtn.setMessage("原发票号码不能为空");
				return null;
			}else{
				head.setOriginalInvoiceNo(invque.getIqyfphm());//原发票号码
			}
			
			head.setInvoiceTotalPrice(String.valueOf(MathCal.add(invque.getIqtotje()*-1,0,2)));//合计金额
			head.setInvoiceTotalTax(String.valueOf(MathCal.add(invque.getIqtotse()*-1,0,2)));//合计税额
			head.setInvoiceTotalPriceTax(String.valueOf((MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2))*-1));//价税合计
		
		}else{
			head.setOriginalInvoiceCode("");//原发票代码
			head.setOriginalInvoiceNo("");//原发票号码
			
			head.setInvoiceTotalPrice(String.valueOf(MathCal.add(invque.getIqtotje(),0,2)));//合计金额
			head.setInvoiceTotalTax(String.valueOf(MathCal.add(invque.getIqtotse(),0,2)));//合计税额
			head.setInvoiceTotalPriceTax(String.valueOf(MathCal.add(invque.getIqtotje(),invque.getIqtotse(),2)));//价税合计
		}
		
		if(invque.getZsfs()==null||"".equals(invque.getZsfs())){
			rtn.setCode(-1);
			rtn.setMessage("征税方式不能为空");
			return null;
		}else{
			head.setTaxationMode(invque.getZsfs());//征税方式
		}
		
		head.setDeductibleAmount("");//扣除额
		

		if(invque.getIqmemo()==null||"".equals(invque.getIqmemo())){
			head.setRemarks("");//备注
		}else{
			head.setRemarks(invque.getIqmemo());//备注
		}
		
		head.setSignatureParameter("0000004282000000");//签名值参数
		if("1".equals(taxinfo.getItfeqpttype())){
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return null;
			}else{
				head.setTaxDiskNo(taxinfo.getItfskpbh());//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return null;
			}else{
				head.setTaxDiskKey(taxinfo.getItfskpkl());//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return null;
			}else{
				head.setTaxDiskPassword(taxinfo.getItfkeypwd());//税务数字证书密码
			}
		}else{
			head.setTaxDiskNo("");//税控盘编号
			head.setTaxDiskKey("");//税控盘口令
			head.setTaxDiskPassword("");//税务数字证书密码
		}
		if(taxinfo.getItfbbh()==null||"".equals(taxinfo.getItfbbh())){
			head.setGoodsCodeVersion("");//商品编码版本号
		}else{
			head.setGoodsCodeVersion(taxinfo.getItfbbh());//商品编码版本号
		}
		
		
		head.setNotificationNo("");//通知单编号
		head.setConsolidatedTaxRate("");//综合税率
		//if(invque.getIqshop()==null||"".equals(invque.getIqshop())){
			
		//}else{
			//head.setOrganizationCode(invque.getIqshop());
		//}
		
		head.setOrganizationCode("");
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
					InvoiceDetails bwdetail = new InvoiceDetails();
/*					if(detail.getRowno()==null||"".equals(detail.getRowno())){
						rtn.setCode(-1);
						rtn.setMessage("商品明细第"+rownum+"行,行号不能为空");
						break;
					}else{
					
					}*/
					bwdetail.setGoodsLineNo(String.valueOf(rownum));//明细行号
					bwdetail.setGoodsExtendCode("");
					bwdetail.setGoodsLineNature("0");//发票行性质
					if(detail.getTaxitemid()==null||"".equals(detail.getTaxitemid())){
						rtn.setCode(-1);
						rtn.setMessage("商品明细第"+rownum+"行,税目编码不能为空");
						break;
					}else{
						bwdetail.setGoodsCode(detail.getTaxitemid());//商品编码
					}
					if(detail.getGoodsname()==null||"".equals(detail.getGoodsname())){
						rtn.setCode(-1);
						rtn.setMessage("商品明细第"+rownum+"行,商品名称不能为空");
						break;
					}else{
						bwdetail.setGoodsName(detail.getGoodsname());//商品名称
					}
					
					if(detail.getTaxitemid()==null||"".equals(detail.getTaxitemid())){
						bwdetail.setGoodsTaxItem("");//商品税目
					}else{
						bwdetail.setGoodsTaxItem(detail.getTaxitemid());//商品税目
					}
					
					
					//bwdetail.setGoodsSpecification("");//规格型号
					bwdetail.setGoodsSpecification(detail.getSpec()==null?"":detail.getSpec());//规格型号
					if(detail.getUnit()==null||"".equals(detail.getUnit())){
						bwdetail.setGoodsUnit("");//计量单位
					}else{
						bwdetail.setGoodsUnit(detail.getUnit());//计量单位
					}
					
					if(invque.getIqtype()==1){
						if(detail.getQty()==null){
							bwdetail.setGoodsQuantity("");//商品数量
						}else{
							bwdetail.setGoodsQuantity(String.valueOf(detail.getQty()*-1));//商品数量
						}
						
							bwdetail.setGoodsTotalPrice(String.valueOf(detail.getAmount()*-1));//金额
							bwdetail.setGoodsTotalTax(String.valueOf(detail.getTaxfee()*-1));//税额
							
					}else{
						if(detail.getQty()==null){
							bwdetail.setGoodsQuantity("");//商品数量
						}else{
							bwdetail.setGoodsQuantity(String.valueOf(detail.getQty()));//商品数量
						}
						
						bwdetail.setGoodsTotalPrice(String.valueOf(detail.getAmount()));//金额
						bwdetail.setGoodsTotalTax(String.valueOf(detail.getTaxfee()));//税额
					}
					if(detail.getPrice()==null){
						bwdetail.setGoodsPrice("");//detail.getPrice();单价
					}else{
						bwdetail.setGoodsPrice(String.valueOf(MathCal.add(detail.getPrice(),0,6)));//detail.getPrice();单价
					}
					if(detail.getTaxrate()==null){
						rtn.setCode(-1);
						rtn.setMessage("商品明细第"+rownum+"行,税率不能为空");
						break;
					}else{
						bwdetail.setGoodsTaxRate(String.valueOf(detail.getTaxrate()));//税率
					}
					
					
					bwdetail.setGoodsDiscountLineNo("");//折行对应行号
					bwdetail.setPriceTaxMark("0");//含税标志  0不含税  1含税
					if(detail.getTaxprecon()==null||"".equals(detail.getTaxprecon())){
						bwdetail.setVatSpecialManagement("");//增值税特殊管理
					}else{
						bwdetail.setVatSpecialManagement(detail.getTaxprecon());//增值税特殊管理
					}
					
					if(detail.getTaxpre()==null||"".equals(detail.getTaxpre())){
						rtn.setCode(-1);
						rtn.setMessage("商品明细第"+rownum+"行,优惠政策标识不能为空");
						break;
					}else{
						bwdetail.setPreferentialMark(detail.getTaxpre());//是否使用优惠政策
					}
					
					if(detail.getZerotax()==null||"".equals(detail.getZerotax())){
						bwdetail.setFreeTaxMark("");//免税类型
					}else{
						bwdetail.setFreeTaxMark(detail.getZerotax());//免税类型
					}
					
					bwDetailList.add(bwdetail);
					rownum++;
				}
			}
		}
		
		if("Y".equals(flag)){
			rtn.setCode(-1);
			rtn.setMessage("开票明细是否可开票标记错误数据有问题");
			return null;
		}
		
		head.setInvoiceDetailsList(bwDetailList);
		
		
 
		
		
		//String dataRequest = XMLConverter.objectToXml(head, "utf-8") ;	
		return head;
		//baiWangBeanToXml(dataRequest,fwdm,rtn);
		
	}
	
	/**
	 * 开具发票查询(不分页)
	 * 根据发票代码和发票号码查询
	 * **/	
	
	public InvoiceQueryRequest findInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		 InvoiceQueryRequest request = new InvoiceQueryRequest();

		//Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return null;
		}
		
		 if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			 	rtn.setCode(-1);
				rtn.setMessage("销方纳税号不能为空！");
				return null;
		 }else{
			 request.setSellerTaxNo(taxinfo.getTaxno());
		 }
		
 
		 
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return null;
		}
		/*查询类型 1发票流水号查询  2发票号码和发票代码查询 3纳税人识别号【销方】  4开票终端标识 5开票日期起止  6.购方信息*/

    	if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
	    	if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
	    		rtn.setCode(-1);
				rtn.setMessage("发票查询时发票代码不能为空！");
				return null;
	    	}else{
	    		request.setInvoiceCode(invque.getRtfpdm());
	    	}
	    	if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
	    		rtn.setCode(-1);
				rtn.setMessage("发票查询时发票号码不能为空！");
				return null;
	    	}else{
	    		request.setInvoiceNo(invque.getRtfphm());
	    	}
	    	request.setInvoiceQueryType("2");
    	}else{
    		request.setSerialNo(invque.getIqseqno());
    		request.setInvoiceQueryType("1");
    	}
	   
	    
		return request;
	}
	
	
	/**
	 * 空白发票作废
	 * 转换为百旺对象
	 * **/
	public InvoiceInvalidRequest generateInvoiceblankInValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		 return generateInvoiceInValid(invque,taxinfo,rtn,"0");
	}
	
	/**
	 * 已开发票作废
	 * 转换为百旺对象
	 * **/
	public InvoiceInvalidRequest generateInvoiceYkInValid(Invque invque,Taxinfo taxinfo,RtnData rtn){
		 return generateInvoiceInValid(invque,taxinfo,rtn,"1");
	}
	
	/**
	 * 发票作废
	 * 转换为百旺对象
	 * **/
	public InvoiceInvalidRequest generateInvoiceInValid(Invque invque,Taxinfo taxinfo,RtnData rtn,String Zflx){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
		InvoiceInvalidRequest request = new InvoiceInvalidRequest();
		
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return null;
		}
		//Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return null;
		}
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return null;
		}else{
			request.setDeviceType(taxinfo.getItfeqpttype());//设备类型
		}
		
		request.setInvoiceInvalidType("Zflx");
		
		 if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			 	rtn.setCode(-1);
				rtn.setMessage("销方纳税号不能为空！");
				return null;
		 }else{
			 request.setSellerTaxNo(taxinfo.getTaxno());
		 }
		 
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return  null;
		}else{
			request.setInvoiceTypeCode(invque.getIqfplxdm());//发票种类编码
		}
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票点编码不能为空");
			return null;
		}else{
			request.setInvoiceTerminalCode(invque.getIqtaxzdh());//开票点编码
		}
		
    	if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
    		rtn.setCode(-1);
			rtn.setMessage("发票查询时发票代码不能为空！");
			return null;
    	}else{
    		request.setInvoiceCode(invque.getRtfpdm());
    	}
    	if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
    		rtn.setCode(-1);
			rtn.setMessage("发票查询时发票号码不能为空！");
			return null;
    	}else{
    		request.setInvoiceNo(invque.getRtfphm());
    	}
		
    	if("1".equals(taxinfo.getItfeqpttype())){
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return null;
			}else{
				request.setTaxDiskNo(taxinfo.getItfskpbh());//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return null;
			}else{
				request.setTaxDiskKey(taxinfo.getItfskpkl());//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return null;
			}else{
				request.setTaxDiskPassword(taxinfo.getItfkeypwd());//税务数字证书密码
			}
		}else{
			request.setTaxDiskNo("");//税控盘编号
			request.setTaxDiskKey("");//税控盘口令
			request.setTaxDiskPassword("");//税务数字证书密码
		}
    	if(invque.getIqperson()==null||"".equals(invque.getIqperson())){
    		rtn.setCode(-1);
			rtn.setMessage("作废人不能为空");
			return null;
    	}else{
    		request.setInvoiceInvalidOperator(invque.getIqperson());
    	}
		return request;
	}
	
	/**
	 * 发票打印
	 * 转换为百旺对象
	 * **/
	public InvoicePrintRequest generateInvoicePrint(Invque invque,Taxinfo taxinfo,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
		InvoicePrintRequest request = new InvoicePrintRequest();
		 
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return null;
		}
		 
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return null;
		}
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return null;
		}else{
			request.setDeviceType(taxinfo.getItfeqpttype());//设备类型
		}
		
		
		if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			 	rtn.setCode(-1);
				rtn.setMessage("销方纳税号不能为空！");
				return null;
		}else{
			 request.setSellerTaxNo(taxinfo.getTaxno());
		}
		
    	if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
    		request.setSerialNo("");
    	}else{
    		request.setSerialNo(invque.getIqseqno());
    	}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return  null;
		}else{
			request.setInvoiceTypeCode(invque.getIqfplxdm());//发票种类编码 004:增值税专用发票，007:增值税普通发票，026：增值税电子发票，025：增值税卷式发票（只支持税控服务器，不支持税控盘）
		}
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票点编码不能为空");
			return null;
		}else{
			request.setInvoiceTerminalCode(invque.getIqtaxzdh());//开票点编码
		}
		
    	if("1".equals(taxinfo.getItfeqpttype())){
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return null;
			}else{
				request.setTaxDiskNo(taxinfo.getItfskpbh());//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return null;
			}else{
				request.setTaxDiskKey(taxinfo.getItfskpkl());//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return null;
			}else{
				request.setTaxDiskPassword(taxinfo.getItfkeypwd());//税务数字证书密码
			}
		}else{
			request.setTaxDiskNo("");//税控盘编号
			request.setTaxDiskKey("");//税控盘口令
			request.setTaxDiskPassword("");//税务数字证书密码
		}
		
    	if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
    		rtn.setCode(-1);
			rtn.setMessage("发票查询时发票代码不能为空！");
			return null;
    	}else{
    		request.setInvoiceCode(invque.getRtfpdm());
    	}
    	if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
    		rtn.setCode(-1);
			rtn.setMessage("发票查询时发票号码不能为空！");
			return null;
    	}else{
    		request.setInvoiceNo(invque.getRtfphm());
    	}
    	
		if(invque.getIsList()==null){
			rtn.setCode(-1);
			rtn.setMessage("打印类型不能为空");
			return null;
		}else{
			request.setInvoicePrintType(invque.getIsList()+"");//打印类型
		}
    	
		request.setInvoicePrintMode("1");
		
		return request;
	}
	
	/**
	 * 空白发票查询
	 * **/
	public BlankInvoiceQueryRequest blankInvoice(Invque invque,Taxinfo taxinfo,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
		//Invque invque = (Invque) map.get("invque");
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return null;
		}
		//Taxinfo taxinfo = (Taxinfo)map.get("taxinfo");
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return null;
		}
		
		BlankInvoiceQueryRequest request = new BlankInvoiceQueryRequest();
		
		if(taxinfo.getItfeqpttype()==null||"".equals(taxinfo.getItfeqpttype())){
			rtn.setCode(-1);
			rtn.setMessage("设备类型不能为空");
			return null;
		}else{
			request.setDeviceType(taxinfo.getItfeqpttype());//设备类型
		}
		
		if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			rtn.setCode(-1);
			rtn.setMessage("销方纳税号不能为空");
			return null;
		}else{
			request.setSellerTaxNo(taxinfo.getTaxno());
		}
		
		if(invque.getIqfplxdm()==null||"".equals(invque.getIqfplxdm())){
			rtn.setCode(-1);
			rtn.setMessage("发票种类编码不能为空");
			return  null;
		}else{
			request.setInvoiceTypeCode(invque.getIqfplxdm());//发票种类编码
		}
		
		if(invque.getIqtaxzdh()==null||"".equals(invque.getIqtaxzdh())){
			rtn.setCode(-1);
			rtn.setMessage("开票点编码不能为空");
			return null;
		}else{
			request.setInvoiceTerminalCode(invque.getIqtaxzdh());//开票点编码
		}
		
		if("1".equals(taxinfo.getItfeqpttype())){
			if(taxinfo.getItfskpbh()==null||"".equals(taxinfo.getItfskpbh())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘编号不能为空");
				return null;
			}else{
				request.setTaxDiskNo(taxinfo.getItfskpbh());//税控盘编号
			}
			
			if(taxinfo.getItfskpkl()==null||"".equals(taxinfo.getItfskpkl())){
				rtn.setCode(-1);
				rtn.setMessage("税控盘口令不能为空");
				return null;
			}else{
				request.setTaxDiskKey(taxinfo.getItfskpkl());//税控盘口令
			}
			
			if(taxinfo.getItfkeypwd()==null||"".equals(taxinfo.getItfkeypwd())){
				rtn.setCode(-1);
				rtn.setMessage("税务数字证书密码不能为空");
				return null;
			}else{
				request.setTaxDiskPassword(taxinfo.getItfkeypwd());//税务数字证书密码
			}
		}else{
			request.setTaxDiskNo("");//税控盘编号
			request.setTaxDiskKey("");//税控盘口令
			request.setTaxDiskPassword("");//税务数字证书密码
		}
		
		
		return request;
	}
	
	/**
	 * 获取pdf连接参数转换
	 * **/
	
	public FormatfileBuildRequest generatePdf(Invque invque,Taxinfo taxinfo,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
	 
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return null;
		}
		
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return null;
		}
		
		//判断邮箱电话是否都填写
		int flag = 1;
		FormatfileBuildRequest request = new FormatfileBuildRequest();
		
		if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			rtn.setCode(-1);
			rtn.setMessage("销方纳税号不能为空");
			return null;
		}else{
			request.setSellerTaxNo(taxinfo.getTaxno());
		}
		
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			
			if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
	    		rtn.setCode(-1);
				rtn.setMessage("发票查询时发票代码不能为空！");
				return null;
	    	}else{
	    		request.setInvoiceCode(invque.getRtfpdm());
	    	}
	    	if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
	    		rtn.setCode(-1);
				rtn.setMessage("发票查询时发票号码不能为空！");
				return null;
	    	}else{
	    		request.setInvoiceNo(invque.getRtfphm());
	    	}
		}else{
			request.setSerialNo(invque.getIqseqno());//开票流水号；流水号和发票号码代码二选一必填
			
			if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
				request.setInvoiceCode("");
	    	}else{
	    		request.setInvoiceCode(invque.getRtfpdm());
	    	}
	    	if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
	    		request.setInvoiceNo("");
	    	}else{
	    		request.setInvoiceNo(invque.getRtfphm());
	    	}
		}
		
		if(invque.getIqtel()!=null&&!"".equals(invque.getIqtel())){
			request.setPushType("1");
			request.setBuyerPhone(invque.getIqtel());
			flag = 0;
		}
		else
		if(invque.getIqemail()!=null&&!"".equals(invque.getIqemail())){
			if(flag==1)
				request.setPushType("1");
				request.setBuyerEmail(invque.getIqemail());
		}else{
			request.setPushType("0");
		}

		return request;
	}
	
	/**
	 * 获取pdf查询
	 * **/
	
	public FormatfileQueryRequest findPdf(Invque invque,Taxinfo taxinfo,RtnData rtn){
		/**
		 * 计算后小票数据初始化
		 * invque 队列数据
		 * taxinfo 企业纳税号信息
		 * **/
	 
		if(invque == null){
			rtn.setCode(-1);
			rtn.setMessage("队列数据 invque没有初始化");
			return null;
		}
		
		if(taxinfo ==null){
			rtn.setCode(-1);
			rtn.setMessage("企业纳税号信息 taxinfo没有初始化");
			return null;
		}
		
 
		FormatfileQueryRequest request = new FormatfileQueryRequest();
		
		if(taxinfo.getTaxno()==null||"".equals(taxinfo.getTaxno())){
			rtn.setCode(-1);
			rtn.setMessage("销方纳税号不能为空");
			return null;
		}else{
			request.setSellerTaxNo(taxinfo.getTaxno());
		}
		
		if(invque.getIqseqno()==null||"".equals(invque.getIqseqno())){
			
			if(invque.getRtfpdm()==null||"".equals(invque.getRtfpdm())){
	    		rtn.setCode(-1);
				rtn.setMessage("发票查询时发票代码不能为空！");
				return null;
	    	}else{
	    		request.setInvoiceCode(invque.getRtfpdm());
	    	}
	    	if(invque.getRtfphm()==null||"".equals(invque.getRtfphm())){
	    		rtn.setCode(-1);
				rtn.setMessage("发票查询时发票号码不能为空！");
				return null;
	    	}else{
	    		request.setInvoiceNo(invque.getRtfphm());
	    	}
	    	request.setInvoiceQueryType("0");
		}else{
			request.setSerialNo(invque.getIqseqno());//开票流水号；流水号和发票号码代码二选一必填
			request.setInvoiceQueryType("1");
 
		}
		request.setReturnType("1");

		return request;
	}
	
}
