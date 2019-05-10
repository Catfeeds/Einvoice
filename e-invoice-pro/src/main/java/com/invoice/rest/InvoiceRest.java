package com.invoice.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.invoice.apiservice.dao.InvoiceSaleDao;
import com.invoice.apiservice.service.impl.InvoiceAnddetailService;
import com.invoice.bean.db.InvoiceDetail;
import com.invoice.bean.db.InvoiceHead;
import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.ui.InvoiceListView;
import com.invoice.bean.ui.RequestBillInfo;
import com.invoice.bean.ui.Token;
import com.invoice.config.EntPrivatepara;
import com.invoice.config.FGlobal;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.InvoiceListService;
import com.invoice.util.NewHashMap;
import com.invoice.util.Page;
import com.invoice.util.SpringContextUtil;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
@RequestMapping(value = "/ui")
public class InvoiceRest {

	@Autowired
	EntPrivatepara entPrivatepara;
	@Autowired
	InvoiceListService invoiceListService;
	@Autowired
	InvoiceAnddetailService invoiceAnddetailService;

	@RequestMapping(value = "/queryInvoiceList", method = RequestMethod.POST)
	@ResponseBody
	public String queryInvoiceList(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Page.cookPageInfo(jo);
			int count = invoiceListService.getInvoiceListCount(jo);
			List<InvoiceListView> taxitemlist = invoiceListService.queryInvoiceList(jo);
			return new RtnData(taxitemlist, count).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/getInvoiceHeadById", method = RequestMethod.POST)
	@ResponseBody
	public String getInvoiceHeadById(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		InvoiceHead inv = JSONObject.parseObject(data, InvoiceHead.class);
		try {
			InvoiceHead tm = invoiceListService.getInvoiceHeadById(inv);
			returnJson.put("code", "0");
			returnJson.put("data", tm);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

		return returnJson.toJSONString();
	}

	@RequestMapping(value = "/getInvoiceHead", method = RequestMethod.POST)
	@ResponseBody
	public String getInvoiceHead(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			jo.put("shopid", token.getShopid());
			jo.put("loginid", token.getLoginid());
			Page.cookPageInfo(jo);
			int count = invoiceAnddetailService.getInvoiceHeadCount(jo);
			List<HashMap<String, Object>> tm = invoiceAnddetailService.getInvoiceHead(jo);
			// if(tm != null && tm.size() > 0){
			// for(InvoiceHead invoiceHead:tm){
			// invoiceHead.setLxdm(getLxString(invoiceHead.getLxdm()));
			// }
			// }

			return new RtnData(tm, count).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/getInvoiceHeadByAdmin", method = RequestMethod.POST)
	@ResponseBody
	public String getInvoiceHeadByAdmin(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);
			int count = invoiceAnddetailService.getInvoiceHeadCount(jo);
			List<HashMap<String, Object>> tm = invoiceAnddetailService.getInvoiceHead(jo);
			// if(tm != null && tm.size() > 0){
			// for(InvoiceHead invoiceHead:tm){
			// invoiceHead.setLxdm(getLxString(invoiceHead.getLxdm()));
			// }
			// }

			return new RtnData(tm, count).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/getHnyjHeadByAdmin", method = RequestMethod.POST)
	@ResponseBody
	public String getHnyjHeadByAdmin(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);
			int count = invoiceAnddetailService.getHnyjHeadCount(jo);
			List<HashMap<String, Object>> tm = invoiceAnddetailService.getHnyjHead(jo);

			return new RtnData(tm, count).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/exportInvoiceHeadByAdmin", method = RequestMethod.POST)
	public void exportInvoiceHeadByAdmin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			List<HashMap<String, Object>> tm = invoiceAnddetailService.getInvoiceHead(jo);
			
			 if(tm != null && tm.size() > 0){
				 for(HashMap<String, Object> map:tm){
					 String tmp = map.get("status").toString();
					 if(!StringUtils.isEmpty(tmp)) {
						 if(tmp.equals("99")){
							 map.put("statusmsg", "已作废");
						 }else if(tmp.equals("90")){
							 map.put("statusmsg", "已红冲");
						 }else {
							 map.put("statusmsg", "正常");
						 }
					 }
					 
					 tmp = map.get("lxdm").toString();
					 if(!StringUtils.isEmpty(tmp)) {
						 if(tmp.equals("025")){
							 map.put("lxdm", "卷票");
						 }else if(tmp.equals("026")){
							 map.put("lxdm", "电子票");
						 }else if(tmp.equals("004")){
							 map.put("lxdm", "专票");
						 }else if(tmp.equals("007")){
							 map.put("lxdm", "普票");
						 }
					 }
					 
					 tmp = map.get("invoicetype").toString();
					 if(!StringUtils.isEmpty(tmp)) {
						 if(tmp.equals("0")){
							 map.put("invoicetype", "蓝票");
						 }else if(tmp.equals("1")){
							 map.put("invoicetype", "红冲票");
						 }
					 }
				 }
			 }
			
			String excelName = "超级发票管理导出.xls";
			String[] heads = { "门店号", "门店名称", "提取码", "状态", "发票代码", "发票号码", "发票种类", "总金额", "可开票金额", "总税额", "开票日期",
					"申请时间", "开票人", "开票终端", "开票类型" };
			String[] headsStr = { "iqshop", "shopname", "sheetid", "statusmsg", "fpdm", "fphm", "lxdm", "totalamt",
					"totaltaxamt", "totaltaxfee", "fprq", "iqdate", "iqadmin", "iqtaxzdh", "invoicetype" };

			download(request, response, tm, excelName, data, heads, headsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询发票
	 * 
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/queryInvoiceHead", method = RequestMethod.POST)
	@ResponseBody
	public String queryInvoiceHead(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			jo.put("shopid", token.getShopid());
			Page.cookPageInfo(jo);
			NewHashMap<String, Object> map = new NewHashMap<String, Object>(jo);
			int count = invoiceAnddetailService.queryInvoiceHeadCount(map);
			List<InvoiceHead> tm = invoiceAnddetailService.queryInvoiceHead(jo);
			if (tm != null && tm.size() > 0) {
				for (InvoiceHead invoiceHead : tm) {
					invoiceHead.setLxdm(getLxString(invoiceHead.getLxdm()));
					invoiceHead.setStatusmsg(getStatusMsg(invoiceHead.getStatus()));

					// 发票下载地址转换
					// TODO
					String pdfPre = entPrivatepara.get(token.getEntid(), FGlobal.pdfPre);
					String pdf = invoiceHead.getPdf();
					if (!StringUtils.isEmpty(pdfPre) && !StringUtils.isEmpty(pdf) && !"wx".equals(token.getChannel())) {
						String pre = pdf.substring(0, pdf.indexOf("//"));
						pdf = pdf.substring(pdf.indexOf("://") + 3, pdf.length());
						pdf = pdf.substring(pdf.indexOf("/"), pdf.length());
						pdf = pre + "//" + pdfPre + pdf;
						invoiceHead.setPdf(pdf);
					}
				}
			}

			return new RtnData(tm, count).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/getInvoiceDetail", method = RequestMethod.POST)
	@ResponseBody
	public String getInvoiceDetail(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			jo.put("loginid", token.getLoginid());
			Page.cookPageInfo(jo);
			int count = invoiceAnddetailService.getInvoiceDetailCount(jo);
			List<InvoiceDetail> tm = invoiceAnddetailService.getInvoiceDetail(jo);

			return new RtnData(tm, count).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	public String getLxString(String fplx) {
		if ("004".equals(fplx)) {
			return "专票";
		} else if ("007".equals(fplx)) {
			return "普票";
		} else if ("025".equals(fplx)) {
			return "卷票";
		} else if ("026".equals(fplx)) {
			return "电子票";
		}
		return fplx;
	}

	public String getStatusMsg(int status) {
		switch (status) {
		case 100:
			return "正常";
		case 99:
			return "已作废";
		case 90:
			return "已红冲";
		default:
			return "未知状态";
		}
	}

	@RequestMapping(value = "/getInvoiceDetailForSum", method = RequestMethod.POST)
	@ResponseBody
	public String getInvoiceDetailForSum(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			jo.put("loginid", token.getLoginid());
			if (token.getEntid().equals("SDYZ")) {
				jo.put("iqsource", "3");
			}

			Page.cookPageInfo(jo);
			int count = invoiceAnddetailService.getInvoiceDetailForSumCount(jo);
			List<HashMap<String, String>> tm = invoiceAnddetailService.getInvoiceDetailForSum(jo);
			HashMap<String, String> list = invoiceAnddetailService.getDetailForSumAmt(jo);
			JSONObject returnJson = new JSONObject();
			if (list != null && list.size() > 0) {
				returnJson.put("zamt", list.get("zamt"));
				returnJson.put("zqty", list.get("zqty"));
			} else {
				returnJson.put("zamt", 0);
				returnJson.put("zqty", 0);
			}
			returnJson.put("code", "0");
			returnJson.put("data", tm);
			returnJson.put("count", count);

			return returnJson.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/getInvoiceHeadReport", method = RequestMethod.POST)
	@ResponseBody
	public String getInvoiceHeadReport(@RequestBody String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);
			int count = invoiceAnddetailService.getbillreportCount(jo);
			List<HashMap<String, String>> tm = invoiceAnddetailService.getbillreport(jo);
			HashMap<String, String> billreport = invoiceAnddetailService.getbillreportForsum(jo);
			JSONObject returnJson = new JSONObject();
			if (tm != null && tm.size() > 0) {
				returnJson.put("zamt", billreport.get("zamt"));
				for (HashMap<String, String> has : tm) {
					has.put("iqfplxdm", getLxString(has.get("iqfplxdm")));
				}
			}
			returnJson.put("code", "0");
			returnJson.put("data", tm);
			returnJson.put("count", count);
			return returnJson.toJSONString();

		} catch (Exception e) {
			e.printStackTrace();
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	protected void download(HttpServletRequest request, HttpServletResponse response, List detailList, String excelName,
			String data, String[] heads, String[] headsStr) throws ServletException, IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			Token token1 = Token.getToken();

			RequestBillInfo bill = new RequestBillInfo();
			bill.setEntid(token1.getEntid());
			bill.setSheettype("0");
			// List<InvoiceSaleDetail> detailList =
			// SpringContextUtil.getBean("invoiceSaleDao",
			// InvoiceSaleDao.class).getInvoiceSaleDetail(bill);
			// String excelName = "需开专票的商品.xls";

			File excelFile = new File(excelName);
			// 如果文件存在就删除它
			if (excelFile.exists())
				excelFile.delete();
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(excelFile);
			// // 文字样式
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);

			// 根据数据大小具体分n个sheet页，默认一页存储1000条数据
			int sizeLoop = detailList.size();// 数据大小
			int size = detailList.size();
			if (sizeLoop < 1000) {
				sizeLoop = 1000;
			}
			int sheetSize = 1000;
			int loopSize = sizeLoop / sheetSize;
			if (sizeLoop % sheetSize != 0) {
				loopSize += 1;
			}
			// String[] heads = {"商品编码","商品名称","数量","单价","金额","税目代码","税率"};
			// String[] headsStr=
			// {"goodsid","goodsname","qty","price","amt","taxitemid","taxrate"};
			// 分别往每个sheet页写数据
			for (int l = 0; l < loopSize; l++) {
				WritableSheet sheet = book.createSheet("第" + (l + 1) + "页", l);

				for (int i = 0; i < heads.length; i++) {
					Label cell = new Label(i, 0, heads[i], wcfFC);
					sheet.addCell(cell);
				}

				// 循环读取数据列表
				int n = 1;
				for (int i = l * sheetSize; i < (l + 1) * sheetSize && i <= size - 1; i++) {
					Object vrd = detailList.get(i);
					for (int j = 0; j < headsStr.length; j++) {
						Object value = PropertyUtils.getProperty(vrd, headsStr[j]);
						if (value == null)
							value = "";
						sheet.setColumnView(j, value.toString().length() + 10);
						sheet.addCell(new Label(j, n, value.toString(), wcfFC));
					}
					n++;
				}
			}

			// 写入数据并关闭文件
			book.write();
			book.close();

			byte[] bytes = new byte[1024];
			int len = 0;
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(excelName.getBytes(), "iso-8859-1"));
			response.setContentType("application/octet-stream;charset=utf-8");
			// 读取文件
			in = new FileInputStream(excelName);
			// 写入浏览器的输出流
			out = response.getOutputStream();
			while ((len = in.read(bytes)) > 0) {
				out.write(bytes, 0, len);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
			out.flush();
			out.close();
		}
	}

	@RequestMapping(value = "/exportInvoiceDetailForSum", method = RequestMethod.POST)
	@ResponseBody
	public void exportInvoiceDetailForSum(HttpServletRequest request, HttpServletResponse response, String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			jo.put("loginid", token.getLoginid());
			if (token.getEntid().equals("SDYZ")) {
				jo.put("iqsource", "3");
			}

			List<HashMap<String, String>> tm = invoiceAnddetailService.getInvoiceDetailForSum(jo);
			String excelName = "已开商品.xls";
			String[] heads = { "发票号码", "商品编码", "商品名称", "单价", "数量", "总金额", "开票日期", "开票人" };
			String[] headsStr = { "fphm", "goodsid", "goodsname", "price", "qty", "amt", "fprq", "iqperson" };

			download(request, response, tm, excelName, data, heads, headsStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/exportInvoiceHeadReport", method = RequestMethod.POST)
	@ResponseBody
	public void exportInvoiceHeadReport(HttpServletRequest request, HttpServletResponse response, String data) {
		try {
			JSONObject jo = JSONObject.parseObject(data);
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			List<HashMap<String, String>> tm = invoiceAnddetailService.getbillreport(jo);
			String excelName = "已开发票小票报表.xls";
			String[] heads = { "小票提取码", "发票号码", "发票种类", "开票金额", "开票日期", "开票类型" };
			String[] headsStr = { "sheetid", "fphm", "iqfplxdm", "totalamt", "fprq", "invoicetype" };

			download(request, response, tm, excelName, data, heads, headsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
