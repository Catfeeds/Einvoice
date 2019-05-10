package com.invoice.apiservice.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.OpenapiDao;
import com.invoice.apiservice.service.SheetService;
import com.invoice.apiservice.service.factory.SheetServiceFactory;
import com.invoice.bean.db.InvoiceSaleHead;
import com.invoice.bean.db.OpenapiSaleDetail;
import com.invoice.bean.db.OpenapiSaleHead;
import com.invoice.bean.db.OpenapiSalePay;
import com.invoice.bean.db.SheetDetail;
import com.invoice.bean.db.SheetHead;
import com.invoice.bean.db.SheetPayment;
import com.invoice.bean.ui.RequestOpenApiSearch;
import com.invoice.bean.ui.ResponseOpenApiSearch;
import com.invoice.rtn.data.RtnData2;
import com.invoice.util.Base64;
import com.invoice.util.MD5;
import com.invoice.util.SpringContextUtil;

@Service
public class OpenapiService {
	public final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	OpenapiDao dao;
	
	public String push(String entid, String data) {
		try {
			byte[] decode = Base64.decode(data);
			data = new String(decode, "utf-8");
			log.info(data);
			JSONObject root = JSONObject.parseObject(data);
			JSONArray dataArray = root.getJSONArray("data");
			JSONArray retArray = new JSONArray();
			
			for (int i = 0; i < dataArray.size(); i++) {
				JSONObject retData = new JSONObject();
				JSONObject sheet = dataArray.getJSONObject(i);
				retData.put("sheetid", sheet.getString("sheetid"));
				try {
					checkData(sheet);
					
					final OpenapiSaleHead head = JSONObject.parseObject(sheet.toJSONString(), OpenapiSaleHead.class);
					head.setEntid(entid);
					head.setCreatetime(new Date());
					
					OpenapiSaleHead o = dao.getSaleHead(head);
					if(o!=null) {
						throw new Exception("数据重复");
					}
					
					SpringContextUtil.getBean(this.getClass()).insertSheet(head);
					
					retData.put("code", "0");
					retData.put("message", "success");
				} catch (Exception e) {
					retData.put("code", "-1");
					retData.put("message", e.getMessage());
				}
				retArray.add(retData);
			}
			
			return new RtnData2("0","success",retArray).toString();
		} catch (UnsupportedEncodingException e) {
			return new RtnData2("-1","data error "+e.getMessage()).toString();
		}
	}
	
	private void checkData(JSONObject sheet) {
		String ss[] = {"sheetid","sheettype","shopid","sdate","amt","isauto","sheetdetail"};
		for (String key : ss) {
			if(!sheet.containsKey(key)) {
				throw new RuntimeException("data error "+key+" is null");
			}
		}
		
		JSONArray detail = sheet.getJSONArray("sheetdetail");
		String detailss[] = {"rowno","itemid","itemname","categoryid","qty","taxrate","amt"};
		for (int i = 0; i < detail.size(); i++) {
			JSONObject jo = detail.getJSONObject(i);
			for (String key : detailss) {
				if(!jo.containsKey(key)) {
					throw new RuntimeException("detail data error "+key+" is null");
				}
			}
		}
	}
	
	private void toSaleHead(OpenapiSaleHead head) {
			SheetHead sell = new SheetHead();
			sell.setSheetid(head.getSheetid());
			sell.setAmt(head.getAmt());
			sell.setBillno(head.getBillno());
			sell.setEditor(head.getEditor());
			sell.setEntid(head.getEntid());
			sell.setIsauto(head.getIsauto());
			sell.setRecvemail(head.getRecvemail());
			sell.setRecvphone(head.getRecvphone());
			sell.setSdate(head.getSdate());
			sell.setGmfadd(head.getTaxaddr());
			sell.setGmfbank(head.getTaxbank());
			sell.setGmfname(head.getTaxtitle());
			sell.setGmfno(head.getTaxno());
			sell.setShopid(head.getShopid());
			sell.setSyjid(head.getSyjid());
			sell.setTradedate(head.getSdate());
			List<SheetDetail> sheetdetail = new ArrayList<>();
			for (OpenapiSaleDetail od : head.getSheetdetail()) {
				SheetDetail sd = new SheetDetail();
				sd.setItemid(od.getItemid());
				sd.setAmt(od.getAmt());
				sd.setCategoryid(od.getCategoryid());
				sd.setItemname(od.getItemname());
				sd.setQty(od.getQty());
				sd.setSpec(od.getSpec());
				sd.setUnit(od.getUnit());
				sd.setRowno(od.getRowno());
				sd.setTaxrate(od.getTaxrate());
				sd.setTaxitemid(od.getTaxitemid());
				sheetdetail.add(sd);
			}
			sell.setSheetdetail(sheetdetail);
			List<SheetPayment> sheetpayment = new ArrayList<>();
			for (OpenapiSalePay op : head.getSheetpayment()) {
				SheetPayment sp = new SheetPayment();
				sp.setAmt(op.getAmt());
				sp.setPayid(op.getPayid());
				sp.setPayname(op.getPayname());
				sp.setRowno(op.getRowno());
				sheetpayment.add(sp);
			}
			sell.setSheetpayment(sheetpayment);

			SheetService service = SheetServiceFactory.getInstance(head.getSheettype());
			InvoiceSaleHead saleHead = service.calculateSheet(sell,null);
			saleHead.setCreatetime(new Date());
			saleHead.setFlag(0);
			service.nxInvoiceSale2DB(saleHead);
	}
	
	@Transactional
	public void insertSheet(OpenapiSaleHead head) {
		dao.insertSaleHead(head);
		List<OpenapiSaleDetail> detail = head.getSheetdetail();
		if(detail==null || detail.isEmpty()) {
			throw new RuntimeException("明细sheetdetail不能为空");
		}else {
			for (OpenapiSaleDetail openapiSaleDetail : detail) {
				openapiSaleDetail.setEntid(head.getEntid());
				openapiSaleDetail.setSheetid(head.getSheetid());
				openapiSaleDetail.setSheettype(head.getSheettype());
				dao.insertSaleDetail(openapiSaleDetail);
			}
		}
		List<OpenapiSalePay> pay = head.getSheetpayment();
		if(pay!=null && !pay.isEmpty()) {
			for (OpenapiSalePay payDetail : pay) {
				payDetail.setEntid(head.getEntid());
				payDetail.setSheetid(head.getSheetid());
				payDetail.setSheettype(head.getSheettype());
				dao.insertSalePay(payDetail);
			}
		}
		
		toSaleHead(head);
	}

	public boolean checkSign(String entid,String data,String sign) {
		try
		{
			// 以base64解码
			String checkword = "efuture";
			
			log.info("encode:"+data);
			byte[] decode = Base64.decode(data);
			data = new String(decode, "utf-8");
			log.info("decode:"+data);
			
			String str = data + checkword;
			
			// 本地校验码
			String localSign = MD5.md5(str).toUpperCase();
			log.info("localMD5:"+localSign);
			
			localSign = Base64.encode(localSign.getBytes());
			log.info("localSign:"+localSign);
			
			return localSign.equals(sign);
		}
		catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 查询指定单据开票数据
	 * @param entid
	 * @param data
	 * @return
	 */
	public String search(String entid, String data) {
		try {
			
			byte[] decode = Base64.decode(data);
			data = new String(decode, "utf-8");
			log.info(data);
			
			RequestOpenApiSearch request = JSONObject.parseObject(data, RequestOpenApiSearch.class);
			request.setEntid(entid);
			
			if(StringUtils.isEmpty(request.getStartdate())) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_MONTH, 1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
				request.setEnddate(sdf.format(cal.getTime()));
				cal.add(Calendar.MONTH, -1);
				request.setStartdate(sdf.format(cal.getTime()));
			}
			
			List<ResponseOpenApiSearch> resultList = dao.searchInvoice(request);
			for (ResponseOpenApiSearch responseOpenApiSearch : resultList) {
				String invoiceid = responseOpenApiSearch.getInvoiceid();
				List<Map<String,Object>> items = dao.searchInvoiceItem(invoiceid);
				responseOpenApiSearch.setSheetdetail(items);
			}
			
			return new RtnData2("0","success",resultList).toString();
		} catch (Exception e) {
			log.error(e,e);
			return new RtnData2("-1","data error "+e.getMessage()).toString();
		}
	}
}
