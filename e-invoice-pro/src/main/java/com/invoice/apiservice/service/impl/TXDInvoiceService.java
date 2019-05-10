package com.invoice.apiservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.CalculateDao;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.util.MD5;

/**
 * 淘鲜达小票开票
 * 
 * @author Baij
 */
@Service("TXDInvoiceService")
public class TXDInvoiceService extends InvoiceSheetServie {

	public TXDInvoiceService() {
		super();
		super.SHEET_TYPE = "5";
		super.SHEET_NAME = "txd";
	}

	@Autowired
	CalculateDao calculateDao;

	@Override
	public void cookBillInfo(RequestBillInfo bill) {
		if (!StringUtils.isEmpty(bill.getTicketQC())) {
			InvoiceSaleHead head = deSheetid(bill);
			bill.setSheetid(head.getSheetid());
			bill.setShopid(head.getShopid());
		} else if (StringUtils.isEmpty(bill.getSheetid())) {
			throw new RuntimeException("小票信息获取失败");
		}

		bill.setSheettype(SHEET_TYPE);
	}


	@Override
	public InvoiceSaleHead deSheetid(RequestBillInfo bill) {
		String  qr = bill.getTicketQC();
		if(qr==null) throw new RuntimeException("无法解析小票信息");
		
		
		log.info(qr);
		
		if(!qr.startsWith("A") && !qr.startsWith("B")) {
			throw new RuntimeException("小票信息不符合规范[1]："+qr);
		}
		
		String type = qr.substring(0,1);
		
		int startIdx = qr.indexOf("ZB");
		//淘鲜达订单是A、B开头字母
		if(startIdx<6) {
			throw new RuntimeException("小票信息不符合规范[2]："+qr);
		}
		
		if(qr.length()<23) {
			throw new RuntimeException("小票信息不符合规范[3]："+qr);
		}
		
		InvoiceSaleHead res = new InvoiceSaleHead();
		res.setSheettype(SHEET_TYPE);
		//从关键字ZB开始查询数据
		qr = qr.substring(startIdx);
		
		//7位门店号
		res.setShopid(qr.substring(2, 9));
		
		qr = qr.substring(9);
		//剩下的是订单号和校验位
		String sheetid = qr.substring(0,qr.length()-2);
		res.setSheetid(type+sheetid);
		String sign = qr.substring(sheetid.length());
		String localSign = MD5.md5("zb!@#"+sheetid.substring(sheetid.length()-6)).substring(0, 2);
		
		log.info("sign"+sign+" localSign"+localSign);
		log.info(JSONObject.toJSONString(res));
		
		if(!localSign.equalsIgnoreCase(sign)) {
			throw new RuntimeException("小票信息校验异常："+qr);
		}
		
		return res;
	}
}
