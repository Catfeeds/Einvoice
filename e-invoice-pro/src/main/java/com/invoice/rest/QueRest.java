package com.invoice.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.service.impl.ZpaperService;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;
import com.invoice.task.queue.AskInvoiceTask;

import baiwang.invoice.bean.BaiWangRtBlankBean;
import baiwang.invoice.bean.BaiWangRtInvoiceBean;

/**
 * @author Baij 队列开票调用
 */
@Controller
@RequestMapping(value = "/que")
public class QueRest {
	private final Log log = LogFactory.getLog(QueRest.class);

	@Autowired
	AskInvoiceTask invoiceTaskService;
	
	@Autowired
	ZpaperService zpaperService;

	/**
	 * 立即开票
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/immediAskInvoice")
	@ResponseBody
	public String immediAskInvoice(@RequestBody String data) {
		log.debug(data);
		try {
			Invque p = JSONObject.parseObject(data, Invque.class);
			invoiceTaskService.immediInvoiceQue(p);
			return new RtnData(p).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 异步开票记录
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/asyncAskInvoice")
	@ResponseBody
	public String asyncAskInvoice(@RequestBody String data) {
		log.debug(data);
		try {
			Invque p = JSONObject.parseObject(data, Invque.class);
			invoiceTaskService.asyncAskInvoice(p);
			return new RtnData(p).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 查询空白票
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/blankInvoice")
	@ResponseBody
	public String blankInvoice(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject p = JSONObject.parseObject(data);
			Taxinfo taxinfo = JSONObject.parseObject(p.getString("taxinfo"),Taxinfo.class);
			Invque invque = new Invque();
			invque.setIqtaxzdh(p.getString("kpd"));
			invque.setIqfplxdm(p.getString("iqfplxdm"));
			RtnData rtn = new RtnData();
			BaiWangRtBlankBean bean = zpaperService.blankInvoice(invque, taxinfo, rtn);
			if(bean == null){
				return new RtnData(-1, rtn.getMessage()).toString();
			}
			return new RtnData(bean).toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 打印纸票
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/fp_print")
	@ResponseBody
	public String fp_print(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject p = JSONObject.parseObject(data);
			Taxinfo taxinfo = JSONObject.parseObject(p.getString("taxinfo"),Taxinfo.class);
			Invque invque = JSONObject.parseObject(p.getString("invque"),Invque.class);
			RtnData rtn = new RtnData();
			invque.setIqtaxzdh(p.getString("kpd"));
			zpaperService.fp_print(invque, taxinfo, rtn);
			rtn.setData(invque);
			return rtn.toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 发票作废
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/fp_zuofei")
	@ResponseBody
	public String fp_zuofei(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject p = JSONObject.parseObject(data);
			Taxinfo taxinfo = JSONObject.parseObject(p.getString("taxinfo"),Taxinfo.class);
			Invque invque = JSONObject.parseObject(p.getString("invque"),Invque.class);
			RtnData rtn = new RtnData();
			invque.setIqtaxzdh(p.getString("kpd"));
			BaiWangRtInvoiceBean bean = zpaperService.fp_zuofei(invque, taxinfo, rtn,p.getString("zflx"));
			log.info("fp_zuofei  return "+rtn.getCode());
			p.put("rtinvoiceBean", bean);
			rtn.setData(p);
			
			if(rtn.getCode() == 0){
				if(!"6".equals(taxinfo.getTaxmode())){//如果是九赋接口需要作废完在重新调用
					if(invque.getIqtype()==2){
						if(invque.getZflx()==null||"".equals(invque.getZflx())){
							throw new RuntimeException("发票作废类型不能为空："+invque.getRtfphm());
						}
						if("1".equals(invque.getZflx()))
							invoiceTaskService.restSaleInvoice(invque);
						 
					}
				}
				
			}
			return rtn.toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	/**
	 * 重新推送发票到邮箱
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/sendPdf")
	@ResponseBody
	public String sendPdf(@RequestBody String data) {
		log.debug(data);
		try {
			JSONObject p = JSONObject.parseObject(data);
			Invque invque = JSONObject.parseObject(data, Invque.class);
			log.info("sendPdf:"+invque.getIqemail());
			RtnData rtn = new RtnData();
			invoiceTaskService.getPdf(invque,rtn);
			log.info("generatePdf  return "+rtn.getCode());
			return rtn.toString();
		} catch (Exception e) {
			log.error(e, e);
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
}
