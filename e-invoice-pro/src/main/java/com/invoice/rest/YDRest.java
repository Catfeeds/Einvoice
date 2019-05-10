package com.invoice.rest;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Customer;
import com.invoice.bean.ui.TokenForPort;
import com.invoice.rtn.data.RtnData;
import com.invoice.yuande.invoice.service.YuanDeService;

@Controller
@RequestMapping(value = "/que")
public class YDRest {
	private final Log log = LogFactory.getLog(YDRest.class);

	@Autowired
	YuanDeService ydserver;

	/**
	 * 获取小票开票信息 标版
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getYztInvoice_sale.action")
	@ResponseBody
	public void getYztInvoice_sale(@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "data", required = true) String data, HttpServletResponse response) {
		log.debug(data);
		RtnData rtn = new RtnData();
		String serialid = "";
		String msg = "";
		String rtdata = "";
		String rtcode = "";
		String sign = "";
		// if("111".equals(password)){
		System.out.println(password);
		if ("".equals(password) || password == null) {
			rtn.setCode(-1);
			rtn.setMessage("password 值不能为空！");

		}
		TokenForPort tokenPort = JSONObject.parseObject(password, TokenForPort.class);
		try {
			sign = tokenPort.checkToken(rtn);
			System.out.println(sign);
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
		}
		if (rtn.getCode() != -1) {
			if (sign.equals(tokenPort.getSign())) {
				try {

					serialid = ydserver.rtYzInvoiceSaleData(data, tokenPort.getEntid(), rtn);

				} catch (Exception e) {
					rtn.setCode(-1);
					rtn.setMessage(e.getMessage());
					log.error(e, e);
					// return new RtnData(-1, e.getMessage()).toString();
				}
			} else {
				rtcode = "9999";
				msg = "sign 值传入错误！";
			}
		}

		if (rtn.getCode() == -1) {
			rtcode = "9999";
			msg = rtn.getMessage();
		} else {
			rtdata = rtn.getMessage();
			msg = "返回数据成功！";
			rtcode = "0000";
		}
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write("<RESULT><REQUESTID>" + (serialid) + "</REQUESTID><RTNCODE>" + (rtcode)
					+ "</RTNCODE><RTNMSG>" + (msg) + "</RTNMSG><RTNDATA>" + (rtdata) + "</RTNDATA></RESULT>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开票成功发票返回
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getYztInvoice_sale_pdf.action")
	@ResponseBody
	public void getYztInvoice_sale_pdf(@RequestParam(value = "password", required = true) String password,
			@RequestBody String data, HttpServletResponse response) {
		log.debug(data);
		System.out.println(data);
		RtnData rtn = new RtnData();
		System.out.println(password);
		String msg = "";
		String rtcode = "";
		String sign = "";
		if ("".equals(password) || password == null) {
			rtn.setCode(-1);
			rtn.setMessage("password 值不能为空！");

		}
		TokenForPort tokenPort = JSONObject.parseObject(password, TokenForPort.class);
		try {
			sign = tokenPort.checkToken(rtn);
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
		}
		if (rtn.getCode() != -1) {
			if (sign.equals(tokenPort.getSign())) {
				try {
					ydserver.txRtOpenToBean(data, tokenPort.getEntid(), rtn);

				} catch (Exception e) {
					rtn.setCode(-1);
					rtn.setMessage(e.getMessage());
					log.error(e, e);

				}
			} else {
				rtcode = "9999";
				rtn.setMessage("sign 值传入错误！");
			}
		}

		if (rtn.getCode() == -1) {
			rtcode = "9999";
			msg = rtn.getMessage();
		} else {
			msg = "返回数据成功！";
			rtcode = "0000";
		}

		String ret = "<RESPONSE >" + "<RETURNCODE>" + (rtcode) + "</RETURNCODE>" + "<RETURNMESSAGE>" + (msg)
				+ "</RETURNMESSAGE>" + "</RESPONSE>";

		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------------------------
	// 银座接口程序

	/**
	 * 获取小票开票信息
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoice_sale.action")
	@ResponseBody
	public void getInvoice_sale(@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "data", required = true) String data, HttpServletResponse response) {
		log.debug(data);
		RtnData rtn = new RtnData();
		String serialid = "";
		// if("111".equals(password)){
		System.out.println(password);
		try {

			serialid = ydserver.rtInvoiceSaleData(data, "SDYZ", rtn);

		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			log.error(e, e);
			// return new RtnData(-1, e.getMessage()).toString();
		}
		/*
		 * }else{
		 * 
		 * rtn.setMessage(
		 * "<RESULT><REQUESTID></REQUESTID><RTNCODE>-1</RTNCODE><RTNMSG>密码校验不正确！</RTNMSG><RTNDATA><RTNDATA><RESULT>"
		 * ); }
		 */
		String msg = "";
		String rtdata = "";
		String rtcode = "";
		if (rtn.getCode() == -1) {
			rtcode = "9999";
			msg = rtn.getMessage();
		} else {
			rtdata = rtn.getMessage();
			msg = "返回数据成功！";
			rtcode = "0000";
		}
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write("<RESULT><REQUESTID>" + (serialid) + "</REQUESTID><RTNCODE>" + (rtcode)
					+ "</RTNCODE><RTNMSG>" + (msg) + "</RTNMSG><RTNDATA>" + (rtdata) + "</RTNDATA></RESULT>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开票成功发票返回
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/getInvoice_sale_pdf.action")
	@ResponseBody
	public void getInvoice_sale_pdf(@RequestParam(value = "password", required = false) String password,
			@RequestBody String data, HttpServletResponse response) {
		// log.debug(data);
		System.out.println("getInvoice_sale_pdf.action:"+data);
		RtnData rtn = new RtnData();
		// if("111".equals(password)){
		try {
			ydserver.txRtOpenToBean(data, "SDYZ", rtn);
		} catch (Exception e) {
			rtn.setCode(-1);
			rtn.setMessage(e.getMessage());
			log.error(e, e);
			// return new RtnData(-1, e.getMessage()).toString();
		}
		/*
		 * }else{ rtn.setCode(-1); rtn.setMessage("密码校验不正确！"); }
		 */
		String msg = "";
		String rtcode = "";
		if (rtn.getCode() == -1) {
			rtcode = "9999";
			msg = rtn.getMessage();
		} else {
			msg = "返回数据成功！";
			rtcode = "0000";
		}

		String ret = "<RESPONSE >" + "<RETURNCODE>" + (rtcode) + "</RETURNCODE>" + "<RETURNMESSAGE>" + (msg)
				+ "</RETURNMESSAGE>" + "</RESPONSE>";

		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(ret);
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
