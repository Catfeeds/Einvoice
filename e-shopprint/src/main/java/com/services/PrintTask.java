package com.services;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import com.efuture.open.api.sdk.service.PrintService;
import com.sun.jersey.spi.resource.Singleton;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Produces("text/plain")
@Path("/print")
// 不能够跟别的文件重复...
@Singleton
// 获取报表的服务
public class PrintTask {
	private static String LogisticsTaskIP = "";
	private static String PrintTaskIP = "";
	private static String OfflinePrintIP = "";
	private static String TemplateUrl = "";

	private static String PrintJLFIP = "";
	private static String UserName = "";
	private static String Password = "";

	private static String ImagePath = "printimg";
	private static String TemplatePath = "printtemplate";

	static {
		// 物流服务地址
		LogisticsTaskIP = PropertiesUtils.getProperty("PrinterConfig.properties", "LogisticsTaskIP");
		// 运单服务地址
		PrintTaskIP = PropertiesUtils.getProperty("PrinterConfig.properties", "PrintTaskIP");
		// 线下打印服务地址
		OfflinePrintIP = PropertiesUtils.getProperty("PrinterConfig.properties", "OfflinePrintIP");
		// 服务器模板地地址
		TemplateUrl = PropertiesUtils.getProperty("PrinterConfig.properties", "TemplateUrl");
		// 家乐福验证IP
		PrintJLFIP = PropertiesUtils.getProperty("PrinterConfig.properties", "JLFIP");
		UserName = PropertiesUtils.getProperty("PrinterConfig.properties", "UserName");
		Password = PropertiesUtils.getProperty("PrinterConfig.properties", "Password");
	}

	// ********************************************************************************************
	// 获取订单内容,调用打印订单....
	@POST
	// @GET
	@Path("/askprint")
	public String askprint(@QueryParam("shopid") final String shopid, @QueryParam("taskid") String taskid) {
		System.out.println("*********开始打印 "+taskid+"**********");
		String taskarray[] = taskid.split(",");
		JSONArray rearray = new JSONArray();

		for (int i = 0; i < taskarray.length; i++) {
			final String id = taskarray[i];
			// 异步调用
			Runnable t = new Runnable() {
				public void run() {
					doTaskPrint(shopid, id);
				}
			};
			t.run();
		}

		JSONObject rejson = new JSONObject();
		rejson.put("code", 1);
		rejson.put("msg", "已调用打印!");
		rejson.put("data", rearray.toString());

		return rejson.toString();
	}

	synchronized private void doTaskPrint(String shopid, String taskid) {
		String url = PrintTaskIP;
		System.out.println("*********" + url + "**********");
		// 创建POST方法实例
		PostMethod postMethod = new PostMethod(url);
		// 设置中文转码
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		// 设置响应时间
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000 * 10);
		// 设置超时时间
		postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000 * 10);

		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("shopid", shopid), new NameValuePair("taskid", taskid) };
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// 执行postMethod
		// System.out.println("order-center打印传过来的token值为："+token);

		int statusCode = 0;

		HttpClient httpClient = getHttpClient();
		try {
			// 执行PostMethod
			statusCode = httpClient.executeMethod(postMethod);
		} catch (Exception e) {
			System.out.println("错误：doTaskPrint访问服务失败："+e.toString());
		}

		if (statusCode == 200) {
			try {
				// 成功，读取内容
				String response = postMethod.getResponseBodyAsString();
				System.out.println("打印内容：" + response);

				JSONObject taskdata = new JSONObject();

				taskdata = JSONObject.fromObject(response).getJSONObject("data");

				String language = taskdata.get("language").toString();
				// 调用打印订单方法
				printTask(taskdata.toString(), language);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (statusCode == 500) {
			System.out.println(taskid + "=访问门店打印服务返回500错误!");
		} else {
			System.out.println(taskid + "=未知返回" + String.valueOf(statusCode));
		}
	}

	// ********************************************************************************************
	// 打印订单..
	@POST
	// @GET
	@Path("/printTask")
	public String printTask(@FormParam("taskdata") String taskdata, @FormParam("language") String language) {

		String sourceFileName = "";
		try {
			HashMap<String,Object> parameterMap = new HashMap<String,Object>();

			JSONObject mainjson = new JSONObject();
			JSONArray itemarray = new JSONArray();

			// 取到订单的物流信息
			mainjson = JSONObject.fromObject(taskdata).getJSONObject("main");
			// main里面的对象转换成Map(键值对)数组
			Iterator<String> keys = mainjson.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				parameterMap.put(key, mainjson.get(key));
			}

			// 取到订单商品信息数组
			itemarray = JSONObject.fromObject(taskdata).getJSONArray("item");
			// item里面的兑现转成Map(键值对)数组
			ArrayList<Map<String, Object>> reportRows = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < itemarray.size(); i++) {
				JSONObject itemjson = itemarray.getJSONObject(i);
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				Iterator<String> itemkeys = itemjson.keys();
				while (itemkeys.hasNext()) {
					String key = itemkeys.next();
					rowMap.put(key, itemjson.get(key));
				}

				reportRows.add(rowMap);
			}

			// 获取模板里的图片
			JSONObject imgjson = mainjson.getJSONObject("printtemplate").getJSONObject("images");
			Iterator<String> imgkeys = imgjson.keys();
			while (imgkeys.hasNext()) {
				String imgkey = imgkeys.next();
				String imgname = imgjson.getString(imgkey);// 图片名
				// 下载模板所用图片
				downloadPrintFile(imgname, ImagePath);
			}

			// 获取打印模板
			String printtemplate = "";
			if ("2".equals(language)) {
				printtemplate = mainjson.getJSONObject("printtemplate").get("task_eng").toString();
			} else {
				printtemplate = mainjson.getJSONObject("printtemplate").get("task").toString();
			}
			// 校验打印模板是否存在
			String templateFilename = downloadPrintFile(printtemplate, TemplatePath);

			JSONObject jsonObj = new JSONObject();

			if (StringUtils.isEmpty(templateFilename)) {
				jsonObj.put("code", "0");
				jsonObj.put("msg", "找不到对应打印格式文件!" + printtemplate);
				return jsonObj.toString();
			}

			sourceFileName = URLDecoder.decode(templateFilename, "UTF-8");

			// 税率表信息
			// Collection<Map<String, Object>> dataSourceMapvatcodeRows = vatcodeRows;

			Collection<Map<String, Object>> dataSourceMapCollection = reportRows;
			// 添加订单信息到打印报表
			PrintService printService = new PrintService(sourceFileName, parameterMap);

			// 税率表信息添加到打印报表
			// printService.reportByMapCollectionDataSource(dataSourceMapvatcodeRows, true);

			// 第二个参数为预览或者默认打印机直接打印....false=预览,true=打印//将商品信息添加到打印报表中
			String result = printService.reportByMapCollectionDataSource(dataSourceMapCollection, true);
			if (!StringUtils.isEmpty(result)) {
				System.out.println("打印机出错："+result);
				jsonObj.put("code", "0");
				if (result.equals("Error printing report.")) {
					jsonObj.put("msg", "调用打印机错误!请检查打印机是否在线。");
				} else {
					jsonObj.put("msg", result);
				}

				return jsonObj.toString();
			}

			// 获取打印模板
			String printtemplatecheck = "";
			if ("2".equals(language)) {
				printtemplatecheck = mainjson.getJSONObject("printtemplate").get("checktask_eng").toString();
			} else {
				printtemplatecheck = mainjson.getJSONObject("printtemplate").get("checktask").toString();
			}
			// 校验打印模板是否存在
			templateFilename = downloadPrintFile(printtemplatecheck, TemplatePath);

			if (StringUtils.isEmpty(templateFilename)) {
				System.out.println("找不到对应打印格式文件!" + printtemplate);
				jsonObj.put("code", "0");
				jsonObj.put("msg", "找不到对应打印格式文件!" + printtemplate);
				return jsonObj.toString();
			}

			sourceFileName = URLDecoder.decode(templateFilename, "UTF-8");

			Collection<Map<String, Object>> checkdataSourceMapCollection = reportRows;
			PrintService checkprintService = new PrintService(sourceFileName, parameterMap);

			// 第二个参数为预览或者默认打印机直接打印....false=预览,true=打印
			String checkresult = checkprintService.reportByMapCollectionDataSource(checkdataSourceMapCollection, true);

			// 格式化输出结果...
			if (!StringUtils.isEmpty(checkresult)) {
				System.out.println("打印机出错："+result);
				jsonObj.put("code", "0");
				if (checkresult.equals("Error printing report.")) {
					jsonObj.put("msg", "调用打印机错误!");
				} else {
					jsonObj.put("msg", checkresult);
				}

				return jsonObj.toString();
			}

			jsonObj.put("code", "1");
			jsonObj.put("msg", "success");

			return jsonObj.toString();

		} catch (Exception e) {
			System.out.println("打印机出错："+e.toString());
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("code", "0");
			jsonObj.put("msg", "门店打印执行异常");
			return jsonObj.toString();
		}
	}

	// ********************************************************************************************
	// 获取订单内容,调用打印订单....
	@POST
	// @GET
	@Path("/askLogisticsprint")
	public String askLogisticsprint(@QueryParam("shopid") final String shopid,
			@QueryParam("taskid") final String taskid, @QueryParam("expressno") final String expressno) {
		String taskarray[] = taskid.split(",");
		JSONArray rearray = new JSONArray();

		for (int i = 0; i < taskarray.length; i++) {
			final String id = taskarray[i];
			// 异步调用
			Runnable t = new Runnable() {
				public void run() {
					printLogistics(shopid, id, expressno);
				}
			};
			t.run();
		}

		JSONObject rejson = new JSONObject();
		rejson.put("code", 1);
		rejson.put("msg", "已调用打印!");
		rejson.put("data", rearray.toString());

		return rejson.toString();
	}

	synchronized private void printLogistics(String shopid, String taskid, String expressno) {
		String url = LogisticsTaskIP;
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000 * 10);
		postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000 * 10);

		System.out.println("**************开始打印:" + taskid + "|" + expressno + "******************");
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("shopid", shopid), new NameValuePair("taskid", taskid),
				new NameValuePair("expressno", expressno) };
		postMethod.setRequestBody(data);
		int statusCode = 0;

		HttpClient httpClient = getHttpClient();

		try {
			statusCode = httpClient.executeMethod(postMethod);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (statusCode == 200) {
			try {
				String response = postMethod.getResponseBodyAsString();

				JSONObject taskdata = new JSONObject();
				taskdata = JSONObject.fromObject(response).getJSONObject("data");

				// 打印物流面单
				printLogistics(taskdata.toString());

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (statusCode == 500) {
			System.err.println(taskid + "|" + expressno + "=访问门店打印服务返回500错误!");
		} else {
			System.err.println(taskid + "|" + expressno + "=未知返回" + String.valueOf(statusCode));
		}
	}

	private void printLogistics(String jsonData) throws IOException {
		System.out.println("取到的需要打印的数据" + jsonData);
		JSONObject mainjson = new JSONObject();
		// 打印模板jsondata
		JSONObject printjson = new JSONObject();

		int size = JSONObject.fromObject(jsonData).getJSONArray("main").size();
		String atat = "";
		for (int i = 0; i < size; i++) {
			atat = "(" + size + "-" + (1 + i) + ")";
			mainjson = JSONObject.fromObject(jsonData).getJSONArray("main").getJSONObject(i);
			Iterator<String> keys = mainjson.keys();
			System.setProperty("jna.encoding", "GBK");
			Map<String, Object> param = new HashMap<String, Object>();

			File pf = new File(PrintTask.class.getResource("/").getFile());
			String dirPath = pf.getParentFile().getParentFile().getAbsolutePath() + File.separator + ImagePath
					+ File.separator;
			param.put("imgpath", dirPath);
			while (keys.hasNext()) {
				String key = keys.next();
				param.put(key, mainjson.get(key));
			}
			param.put("atat", atat);
			// Map<String, Object> param = new HashMap<String, Object>();

			// 取物流打印机名称
			String printername = JSONObject.fromObject(jsonData).getJSONArray("main").getJSONObject(i)
					.get("printername").toString();
			// 获取物流打印模板
			printjson = (JSONObject) param.get("printtemplate");
			String printtemplate = printjson.getString("logistic");

			String templateFilePath = downloadPrintFile(printtemplate, TemplatePath);

			// 获取模板里的图片
			JSONObject imgjson = printjson.getJSONObject("images");
			Iterator<String> imgkeys = imgjson.keys();
			while (imgkeys.hasNext()) {
				String imgkey = imgkeys.next();
				String imgname = imgjson.getString(imgkey);// 图片名
				// 下载模板所用图片
				downloadPrintFile(imgname, ImagePath);
			}

			try {
				// String PathFile = "/printfile/demoreport.jrxml";
				if (StringUtils.isEmpty(templateFilePath)) {
					throw new JRException("找不到对应打印格式文件 :" + templateFilePath);
				}
				String SourceFileName = URLDecoder.decode(templateFilePath, "UTF-8");
				PrintPackageLabel(param, SourceFileName, printername);
			} catch (JRException e) {
				System.out.println("打印物流单出错："+e.toString());
			}
		}

	}

	// 校验客户端是否存在物流面单模板
	public void getPrintTemplate(String printtemplate) {
		URL checkurl = PrintTask.class.getResource("/printfile/" + printtemplate);
		String filePath = PrintTask.class.getResource("/").getFile() + "/printfile/" + printtemplate;

		HttpClient httpClient = getHttpClient();
		// 查询本地项目有无该模板
		if (checkurl == null) {
			File f = new File(filePath);
			// 没有则去服务器下载
			if (!f.exists()) {
				// String url = "http://img5.cache.netease.com/photo/0001/2016-08-04/BTK1FA8K3R710001.jpg";
				String url = TemplateUrl + printtemplate;
				GetMethod getMethod = new GetMethod(url);

				try {
					httpClient.executeMethod(getMethod);
					// 得到文件流
					InputStream is = getMethod.getResponseBodyAsStream();
					// 创建文件
					f.createNewFile();
					// 将得到文件流输出到创建的文件上
					FileOutputStream fileout = new FileOutputStream(f);

					byte[] buffer = new byte[10240];
					int ch = 0;
					while ((ch = is.read(buffer)) != -1) {
						fileout.write(buffer, 0, ch);
					}
					is.close();
					fileout.flush();
					fileout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 校验客户端是否存在物流面单模板所用的图片
	public void getPrintImg(String img) {
		URL checkurl = PrintTask.class.getResource("/printfile/" + img);
		String filePath = PrintTask.class.getResource("/").getFile() + "/printfile/" + img;

		HttpClient httpClient = getHttpClient();
		// 查询本地项目有无该图片
		if (checkurl == null) {
			File f = new File(filePath);
			// 没有则去服务器下载
			if (!f.exists()) {
				String url = TemplateUrl + img;
				GetMethod getMethod = new GetMethod(url);

				try {
					httpClient.executeMethod(getMethod);
					// 得到文件流
					InputStream is = getMethod.getResponseBodyAsStream();
					// 创建文件
					f.createNewFile();
					// 将得到文件流输出到创建的文件上
					FileOutputStream fileout = new FileOutputStream(f);

					byte[] buffer = new byte[10240];
					int ch = 0;
					while ((ch = is.read(buffer)) != -1) {
						fileout.write(buffer, 0, ch);
					}
					is.close();
					fileout.flush();
					fileout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String downloadPrintFile(String filename, String dir) throws IOException {
		// 获取文件存放路径
		File pf = new File(PrintTask.class.getResource("/").getFile());
		String dirPath = pf.getParentFile().getParentFile().getAbsolutePath() + File.separator + dir;
		File dirFile = new File(dirPath);

		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		// 检查文件文件
		String filePath = dirPath + File.separator + filename;
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("更新文件："+TemplateUrl + filename);
			// 文件不存在则下载文件
			String url = TemplateUrl + filename;
			HttpClient httpClient = getHttpClient();
			GetMethod getMethod = new GetMethod(url);
			try {
				int statusCode = httpClient.executeMethod(getMethod);

				if (statusCode == 200) {
					// 得到文件流
					InputStream is = getMethod.getResponseBodyAsStream();
					// 创建文件
					file.createNewFile();
					// 将得到文件流输出到创建的文件上
					FileOutputStream fileout = new FileOutputStream(file);

					byte[] buffer = new byte[10240];
					int ch = 0;
					while ((ch = is.read(buffer)) != -1) {
						fileout.write(buffer, 0, ch);
					}
					is.close();
					fileout.flush();
					fileout.close();
					System.out.println("文件更新成功：" + file.getName());
					return file.getAbsolutePath();
				} else {
					System.err.println("获取文件失败，statusCode=" + statusCode + ";file=" + url);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("获取文件失败。file=" + url);
			}
		} else {
			return file.getAbsolutePath();
		}

		return "";
	}

	static public void PrintPackageLabel(Map<String, Object> ReportParameter, String TemplateFile, String PrinterName)
			throws JRException {
		String FileSuffix = "";
		JasperReport report = null;
		JasperPrint jasperPrint;
		// String TemplateFile = "C:/Users/bandari/demoreport.jrxml";
		if (TemplateFile != null && !("".equals(TemplateFile)) && TemplateFile.contains(".")
				&& (CountSplit(TemplateFile, ".") == 1)) {
			String[] splitArray = TemplateFile.split("\\.");
			if (splitArray.length > 1) {
				FileSuffix = splitArray[splitArray.length - 1];
			}
		} else {
			throw new JRException("打印模板文件异常！");
		}

		try {
			if ("jasper".equals(FileSuffix)) {
				report = (JasperReport) JRLoader.loadObjectFromFile(TemplateFile);
			} else if ("jrxml".equals(FileSuffix)) {
				report = JasperCompileManager.compileReport(TemplateFile);
			} else {
				throw new JRException("非模板文件！");
			}
		} catch (Exception e) {
			System.err.println(e.toString());
			throw new JRException(e.toString());
		}

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.putAll(ReportParameter);
		jasperPrint = JasperFillManager.fillReport(report, parameter, new JREmptyDataSource());
		// JasperViewer.viewReport(jasperPrint,false);
		javax.print.PrintService getMatchPrintService = null;
		if (PrinterName == null || PrinterName.equalsIgnoreCase("")) {
			getMatchPrintService = PrintServiceLookup.lookupDefaultPrintService();
			if (getMatchPrintService == null) {
				throw new JRException("未找到默认打印服务！");
			}
			PrinterName = getMatchPrintService.getName();
		} else {
			javax.print.PrintService[] printers = PrinterJob.lookupPrintServices();
			for (int index = 0; index < printers.length; index++) {
				if (printers[index].getName().equalsIgnoreCase(PrinterName)) {
					getMatchPrintService = printers[index];
					break;
				}
			}
		}
		System.out.println("正准备调用【" + PrinterName + "】打印．．．．．．");
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(PrinterName, null)); // "Honeywell PC42t (203 dpi) - DP"
		printRequestAttributeSet.add(new Copies(1)); // 打印次数
		// printJobAttributeSet.add(new JobPriority(80));

		EFTJRPrintServiceExporter exporter = new EFTJRPrintServiceExporter();
		// 方法1

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
		configuration.setPrintService(getMatchPrintService);
		configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
		configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
		configuration.setDisplayPageDialog(false);
		configuration.setDisplayPrintDialog(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		System.out.println("打印完毕！");

		// 方法2
		/*
		 * exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		 * exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
		 * exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
		 * exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
		 * exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
		 * exporter.exportReport();
		 */

	}

	static private int CountSplit(String strSource, String strSplit) {
		int count = 0;
		if (strSource.indexOf(strSplit) == -1) {
			return 0;
		} else if (strSource.indexOf(strSplit) != -1) {
			count++;
			CountSplit(strSource.substring(strSource.indexOf(strSplit) + strSplit.length()), strSplit);
			return count;
		}
		return 0;
	}

	// ************************线下订单打印********************************************************************
	// 获取订单内容,调用打印订单....
	@POST
	@Path("/askOfflinePrint")
	public String askOffineOrderPrint(@QueryParam("shopid") String shopid, @QueryParam("taskid") String taskid) {
		String taskarray[] = taskid.split(",");
		System.out.println("*********进入线下订单A4打印程序**********");
		JSONArray rearray = new JSONArray();

		for (int i = 0; i < taskarray.length; i++) {
			taskid = taskarray[i];

			// 这里设定成家乐福正式环境的ip地址即可.....
			String url = OfflinePrintIP;// 正式环境
			// String url="http://10.132.254.251:8090/order-center/ui/Parcel/printTask.do";//本地调试
			// String url = "http://127.0.0.1:8080/order-center/ui/Parcel/offlinePrintOrder.do";//测试库
			System.out.println("*********" + url + "**********");
			PostMethod postMethod = new PostMethod(url);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000 * 10);
			postMethod.getParams().setParameter(HttpMethodParams.HEAD_BODY_CHECK_TIMEOUT, 3000 * 10);

			// 填入各个表单域的值
			NameValuePair[] data = { new NameValuePair("shopid", shopid), new NameValuePair("offlineOrder", taskid) };
			// 将表单的值放入postMethod中
			postMethod.setRequestBody(data);
			// 执行postMethod
			// System.out.println("order-center打印传过来的token值为："+token);

			int statusCode = 0;

			HttpClient httpClient = getHttpClient();
			try {
				statusCode = httpClient.executeMethod(postMethod);
			} catch (Exception e) {
				e.printStackTrace();
				JSONObject json = new JSONObject();
				json.put("taskid", taskid);
				json.put("code", "0");
				json.put("msg", taskid + "=请检查门店打印服务是否已启动!");
				rearray.add(json);
				continue;
			}

			if (statusCode == 200) {
				try {
					String response = postMethod.getResponseBodyAsString();
					JSONObject taskdata = new JSONObject();
					taskdata = JSONObject.fromObject(response).getJSONObject("data");
					offlinePrintTask(taskdata.toString());
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("taskid", taskid);
					jsonObj.put("code", "1");
					jsonObj.put("msg", taskid + "=success");
					rearray.add(jsonObj);
					continue;

				} catch (Exception e) {
					e.printStackTrace();

					JSONObject jsonObj = new JSONObject();
					jsonObj.put("taskid", taskid);
					jsonObj.put("code", "0");
					jsonObj.put("msg", taskid + "=打印失败!");
					rearray.add(jsonObj);
					continue;
				}

			} else if (statusCode == 500) {
				JSONObject json = new JSONObject();
				json.put("taskid", taskid);
				json.put("code", "0");
				json.put("msg", taskid + "=访问门店打印服务返回500错误!");
				rearray.add(json);
				continue;
			} else {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("taskid", taskid);
				jsonObj.put("code", "0");
				jsonObj.put("msg", taskid + "=未知返回" + String.valueOf(statusCode));
				rearray.add(jsonObj);
				continue;
			}
		}

		JSONObject rejson = new JSONObject();
		rejson.put("code", 1);
		rejson.put("msg", "已调用打印!");
		rejson.put("data", rearray.toString());

		return rejson.toString();
	}

	// 打印订单..
	@POST
	@Path("/offlinePrintTask")
	public String offlinePrintTask(@FormParam("taskdata") String printdata) {

		String sourceFileName = "";
		try {
			System.out.println("################################打印订单#################################");
			System.out.println(printdata);
			System.out.println("########################################################################");

			HashMap<String, Object> parameterMap = new HashMap<String, Object>();

			JSONObject mainjson = new JSONObject();
			JSONArray itemarray = new JSONArray();

			mainjson = JSONObject.fromObject(printdata).getJSONObject("main");

			itemarray = JSONObject.fromObject(printdata).getJSONArray("item");

			Iterator<String> keys = mainjson.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				parameterMap.put(key, mainjson.get(key));
			}

			ArrayList<Map<String, Object>> reportRows = new ArrayList<Map<String, Object>>();

			for (int i = 0; i < itemarray.size(); i++) {
				JSONObject itemjson = itemarray.getJSONObject(i);
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				Iterator<String> itemkeys = itemjson.keys();
				while (itemkeys.hasNext()) {
					String key = itemkeys.next();
					rowMap.put(key, itemjson.get(key));
				}

				reportRows.add(rowMap);
			}

			JSONObject jsonObj = new JSONObject();
			// 固定路径
			String path = "/printfile/offlineOrder.jrxml";
			System.out.println("path的路径为：" + path);
			URL url = this.getClass().getResource(path);
			if (url == null) {
				// 格式化输出结果...
				jsonObj.put("code", "0");
				jsonObj.put("msg", "找不到对应打印格式文件offlineOrder.jrxml!");
				return jsonObj.toString();
			}

			sourceFileName = URLDecoder.decode(url.getFile(), "UTF-8");

			Collection<Map<String, Object>> dataSourceMapCollection = reportRows;
			PrintService printService = new PrintService(sourceFileName, parameterMap);

			// 第二个参数为预览或者默认打印机直接打印....false=预览,true=打印
			String result = printService.reportByMapCollectionDataSource(dataSourceMapCollection, false);
			if (result == null) {
				jsonObj.put("code", "0");
				if (result.equals("Error printing report.")) {
					jsonObj.put("msg", "调用打印机错误!");
				} else {
					jsonObj.put("msg", result);
				}

				return jsonObj.toString();
			}

			// 固定路径
			path = "/printfile/offlineLogicCheckOrder.jrxml";
			URL logiccheckurl = this.getClass().getResource(path);
			if (logiccheckurl == null) {
				jsonObj.put("code", "0");
				jsonObj.put("msg", "找不到对应打印格式文件checktaskOfflineOrder.jrxml!");
				return jsonObj.toString();
			}
			sourceFileName = URLDecoder.decode(logiccheckurl.getFile(), "UTF-8");

			Collection<Map<String, Object>> logiccheckdataSourceMapCollection = reportRows;
			PrintService logiccheckprintService = new PrintService(sourceFileName, parameterMap);

			// 第二个参数为预览或者默认打印机直接打印....false=预览,true=打印
			String logiccheckresult = logiccheckprintService.reportByMapCollectionDataSource(
					logiccheckdataSourceMapCollection, false);

			// 格式化输出结果...
			if (logiccheckresult == null) {
				jsonObj.put("code", "0");
				if (result.equals("Error printing report.")) {
					jsonObj.put("msg", "调用打印机错误!");
				} else {
					jsonObj.put("msg", logiccheckresult);
				}

				return jsonObj.toString();
			}

			// 固定路径
			path = "/printfile/offlineCheckOrder.jrxml";
			URL checkurl = this.getClass().getResource(path);
			if (checkurl == null) {
				jsonObj.put("code", "0");
				jsonObj.put("msg", "找不到对应打印格式文件checktaskOfflineOrder.jrxml!");
				return jsonObj.toString();
			}
			sourceFileName = URLDecoder.decode(checkurl.getFile(), "UTF-8");

			Collection<Map<String, Object>> checkdataSourceMapCollection = reportRows;
			PrintService checkprintService = new PrintService(sourceFileName, parameterMap);

			// 第二个参数为预览或者默认打印机直接打印....false=预览,true=打印
			String checkresult = checkprintService.reportByMapCollectionDataSource(checkdataSourceMapCollection, false);

			// 格式化输出结果...
			if (checkresult == null) {
				jsonObj.put("code", "0");
				if (result.equals("Error printing report.")) {
					jsonObj.put("msg", "调用打印机错误!");
				} else {
					jsonObj.put("msg", checkresult);
				}

				return jsonObj.toString();
			}

			jsonObj.put("code", "1");
			jsonObj.put("msg", "success");

			return jsonObj.toString();

		} catch (Exception e) {
			e.printStackTrace();

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("code", "0");
			jsonObj.put("msg", "门店打印执行异常");
			return jsonObj.toString();
		}
	}

	public static void main(String[] args) {
		URL o = PrintTask.class.getResource("/");
		System.out.println(o.getFile());
		File f = new File(o.getFile());
		System.out.println(f.getParentFile().getParentFile());
	}
	
	private HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		if (!StringUtils.isEmpty(PrintJLFIP)) {
			System.out.println("*********使用代理" + PrintJLFIP + ":8080**********");
			httpClient.getHostConfiguration().setProxy(PrintJLFIP, 8080);
			if (!StringUtils.isEmpty(UserName)) {
				UsernamePasswordCredentials creds = new UsernamePasswordCredentials(UserName, Password);
				httpClient.getState().setProxyCredentials(AuthScope.ANY, creds);
			}
		}
		return httpClient;
	}

}
