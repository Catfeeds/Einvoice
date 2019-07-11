package com.invoice.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.bean.db.Zbgoodstax;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.RequestBillItem;
import com.invoice.bean.ui.ResponseBillInfo;
import com.invoice.bean.ui.Token;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.ZbgoodtaxService;
import com.invoice.util.LocalRuntimeException;

/**
 * @author Baij 公开API调用
 */
@Controller
@RequestMapping(value = "/zbapi")
public class ZBApiRest {
	private final Log log = LogFactory.getLog(ApiRest.class);
	@Autowired
	private ZbgoodtaxService zbgoodtaxService;
	@Autowired
	EntPrivatepara entPrivatepara;
	
	@RequestMapping(value = "/ZBgetInvoiceBillInfo")
	@ResponseBody
	public String ZBgetInvoiceBillInfo(@RequestBody String data) {
		try {
			log.debug(data);
			RequestBillInfo bill = JSONObject.parseObject(data, RequestBillInfo.class);
			// 强制用户权限范围
			Token token = Token.getToken();
			
			JSONObject jsonreturn=new JSONObject();
			bill.getRequestBillItem().clear();
			JSONObject rj = JSONObject.parseObject(data);
			List<RequestBillItem> rbl = new ArrayList<RequestBillItem>();
			
			Zbgoodstax zbgoodstax = new Zbgoodstax();
			zbgoodstax.setEntid(bill.getEntid());
			JSONArray jsonArray = JSONArray.parseArray(rj.getString("requestBillItem"));
			for (int i = 0; i < jsonArray.size(); i++) {
				RequestBillItem rb = new RequestBillItem();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				zbgoodstax.setGoodsBarcode(jsonObject.getString("goodsBarcode"));
				Zbgoodstax goods = zbgoodtaxService.getGoodsInfoByBarcode(zbgoodstax);
				if (goods==null) {
					jsonreturn.put("code", "-2");
					jsonreturn.put("message", "未找到商品条码："+jsonObject.getString("goodsBarcode")+"的任何信息");
					return jsonreturn.toJSONString();
				}
				goods.setEntid(token.getEntid());
				rb.setGoodsid(goods.getGoodsId());
				rb.setGoodsname(jsonObject.getString("goodsname"));
				rb.setUnit(jsonObject.getString("unit"));
				rb.setQty(Double.valueOf(jsonObject.getString("num")));
				rb.setAmt(Double.valueOf(jsonObject.getString("amt")));
				rb.setTaxrate(Double.valueOf(jsonObject.getString("taxrate")));
				Zbgoodstax taxitem = zbgoodtaxService.gettaxitemid(goods);
				if (taxitem== null ) {
					jsonreturn.put("code", "-2");
					jsonreturn.put("message", "商品条码："+goods.getGoodsBarcode()+"，分类:"+goods.getCategateId()+"未找到对应的税目");
					return jsonreturn.toJSONString();
				} else {
					rb.setTaxitemid(taxitem.getTaxitemid());
				}
				rb.setCategoryid(goods.getCategateId());
				rbl.add(rb);
			}
			bill.setRequestBillItem(rbl);

			
			if ("wx".equals(token.getChannel())) {
				bill.setChannel(token.getChannel());
				bill.setUserID(token.getLoginid());
			}
			bill.setEntid(token.getEntid());
			
			
			SheetService sheetservice = SheetServiceFactory.getInstance(bill.getSheettype());

			ResponseBillInfo res = sheetservice.getInvoiceSheetInfo(bill);

			String pdfPre = entPrivatepara.get(token.getEntid(), FGlobal.pdfPre);
			String pdf = res.getPdf();
			if (!StringUtils.isEmpty(pdfPre) && !StringUtils.isEmpty(pdf) && !"wx".equals(token.getChannel())) {
				String pre = pdf.substring(0, pdf.indexOf("//"));
				pdf = pdf.substring(pdf.indexOf("://") + 3, pdf.length());
				pdf = pdf.substring(pdf.indexOf("/"), pdf.length());
				pdf = pre + "//" + pdfPre + pdf;
				res.setPdf(pdf);
			}

			return new RtnData(res).toString();
		} catch (LocalRuntimeException e) {
			StackTraceElement stackTraceElement= e.getStackTrace()[0];// 得到异常棧的首个元素
			
			log.error("File="+stackTraceElement.getFileName());// 打印文件名
			log.error("Line="+stackTraceElement.getLineNumber());// 打印出错行号
			log.error("Method="+stackTraceElement.getMethodName());// 打印出错方法
			e.printStackTrace();
			return new RtnData(e.getLocalCode(), e.getMessage()).toString();
		} catch (Exception e) {
			StackTraceElement stackTraceElement= e.getStackTrace()[0];// 得到异常棧的首个元素
			
			log.error("File="+stackTraceElement.getFileName());// 打印文件名
			log.error("Line="+stackTraceElement.getLineNumber());// 打印出错行号
			log.error("Method="+stackTraceElement.getMethodName());// 打印出错方法
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
		
	}
}
