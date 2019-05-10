package rest;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
import com.services.EFTJRPrintServiceExporter;
import com.services.PropertiesUtils;
import com.sun.jersey.spi.resource.Singleton;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Produces("application/json")
@Path("/invoice")
@Singleton
public class Print {
	private static String TemplatePath = "printtemplate";
	@POST
	@Path("/print")
	public String print(@FormParam("templetName") String templet, @FormParam("printerName") String PrinterName,@FormParam("data") String data) {
		JSONObject rejson = new JSONObject();
		try {
			doPrint(PrinterName, templet, data);
			rejson.put("code", 1);
			rejson.put("msg", "已调用打印!");
			rejson.put("data", "");
		} catch (Exception e) {
			e.printStackTrace();
			rejson.put("code", -1);
			rejson.put("msg", "调用打印失败! 原因："+e.getMessage());
		}
		return rejson.toString();
	}

	private void doPrint(String PrinterName, String templet, String data) throws FileNotFoundException, JRException {
		//检查模板文件是否存在
		File pf = new File(Print.class.getResource("/").getFile());
		String dirPath = pf.getParentFile().getParentFile().getAbsolutePath() + File.separator + TemplatePath;
		File dirFile = new File(dirPath);

		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		// 检查文件文件
		String filePath = dirPath + File.separator + templet;
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("打印模板文件不存在："+filePath);
		}
		
		JSONObject datajson = JSONObject.fromObject(data);
		HashMap<String,Object> parameterMap = new HashMap<String,Object>();
		Set<String> keys = datajson.keySet();
		for (String key : keys) {
			if(key.equals("details")) {
				continue;
			}
			parameterMap.put(key, datajson.get(key));
		}
		System.out.println("打印内容表头："+parameterMap);
		
		JRMapCollectionDataSource dataSourceMapCollection = null;
		
		if(datajson.containsKey("details")) {
			Collection<Map<String, ?>> reportRows = new ArrayList<Map<String, ?>>();
			JSONArray detailJson = datajson.getJSONArray("details");
			for (int i = 0; i < detailJson.size(); i++) {
				JSONObject itemjson = detailJson.getJSONObject(i);
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				keys = itemjson.keySet();
				for (String key : keys) {
					rowMap.put(key, itemjson.get(key));
				}

				reportRows.add(rowMap);
			}
			
			dataSourceMapCollection = new JRMapCollectionDataSource(reportRows);
		}
		System.out.println("打印内容明细："+JSONArray.fromObject(dataSourceMapCollection.getData()).toString());
		
		// 构造打印报表
		JasperReport report = JasperCompileManager.compileReport(file.getAbsolutePath());
		
		  ;
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameterMap,dataSourceMapCollection);
		javax.print.PrintService[] printers = PrinterJob.lookupPrintServices();
		javax.print.PrintService getMatchPrintService = null;
		if(StringUtils.isEmpty(PrinterName)) {
			getMatchPrintService = PrintServiceLookup.lookupDefaultPrintService();
			if (getMatchPrintService == null) {
				throw new JRException("未找到默认打印机！");
			}
		}else {
			for (int index = 0; index < printers.length; index++) {
				if (printers[index].getName().equalsIgnoreCase(PrinterName)) {
					getMatchPrintService = printers[index];
					break;
				}
			}
			if(getMatchPrintService==null) {
				throw new FileNotFoundException("打印机不存在："+PrinterName);
			}
		}
		
		
		System.out.println("正准备调用【" + PrinterName + "】打印．．．．．．");
		
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(PrinterName, null));
		printRequestAttributeSet.add(new Copies(1)); // 打印次数
		
		EFTJRPrintServiceExporter exporter = new EFTJRPrintServiceExporter();
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
		
	}

}
