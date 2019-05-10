package com.invoice.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.config.FGlobal;

public class Sheet {
	private static final Log log = LogFactory.getLog(Sheet.class);
	
	public static String enBillSheetid(String shopid,String syj,String billno){
		return shopid+"-"+syj+"-"+billno;
	}
	
	public static InvoiceSaleHead deBillSheetid(String qr){
		if(qr==null) throw new RuntimeException("无法解析小票信息");
		String[] ss = qr.split("-");
		try {
			InvoiceSaleHead res = new InvoiceSaleHead();
			if(ss.length==4){
				res.setShopid(ss[0]);
				res.setSyjid(ss[1]);
				res.setBillno(ss[2]);
				res.setTotalamount(Double.valueOf(ss[3]));
			}else if(ss.length==3){
				res.setShopid(ss[0]);
				res.setSyjid(ss[1]);
				res.setBillno(ss[2]);
			}else{
				throw new RuntimeException("无法解析小票信息:"+qr);
			}
			res.setSheetid(enBillSheetid(res.getShopid(), res.getSyjid(), res.getBillno()));
			return res;
			
		} catch (Exception e) {
			log.error(e,e);
			throw new RuntimeException("无法解析小票信息");
		}
	}
	
	public static InvoiceSaleHead deWholeSheetid(String qr){
		if(StringUtils.isEmpty(qr)) throw new RuntimeException("单据信息为空");
		String[] ss = qr.split("-");
		try {
			InvoiceSaleHead res = new InvoiceSaleHead();
			res.setSheetid(ss[0]);
			if(ss.length==1){
			}else if(ss.length==2){
				res.setTotalamount(Double.valueOf(ss[1]));
			}else{
				throw new RuntimeException("无法解析单据信息:"+qr);
			}
			res.setSheetid(ss[0]);
			return res;
		} catch (Exception e) {
			log.error(e,e);
			throw new RuntimeException("无法解析单据信息");
		}
	}
}
