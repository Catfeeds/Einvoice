package com.invoice.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.invoice.uiservice.service.TaxitemService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Goodstax;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.GoodstaxService;
import com.invoice.util.Page;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


@Controller
@RequestMapping(value = "/ui")
public class GoodstaxRest {
	
	@Autowired
	GoodstaxService goodstaxService;

	@Autowired
	TaxitemService taxitemService;
	
	@RequestMapping(value = "/queryGoodstax", method = RequestMethod.POST)
	@ResponseBody
	public String queryGoodstax(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			Page.cookPageInfo(returnJson);
			int count = goodstaxService.getGoodstaxCount(returnJson); 
			List<HashMap<String,String>> goodstax  = goodstaxService.queryGoodstax(returnJson);
			return new RtnData(goodstax,count).toString();	
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();	
		}
	}
	
	
	@RequestMapping(value = "/insertGoodstax", method = RequestMethod.POST)
	@ResponseBody
	public String insertGoodstax(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Goodstax goodstax=JSONObject.parseObject(data, Goodstax.class);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			goodstax.setEntid(token.getEntid());
			goodstax.setCrud("I");
			goodstax.setEdittype("0");//0录入  1批量导入
			goodstax.setLoginid(token.getLoginid());
			goodstax.setProcessTime(new Date());
			goodstax.setUsername(token.getUsername());
			Taxitem taxitem = goodstaxService.getTaxitemById(goodstax.getTaxitemid());
			if(taxitem==null){
				 return  new RtnData(-1,"税目编码没有找到").toString();
			}
			goodstaxService.insertGoodstax(goodstax);
			//List<HashMap<String,String>> gt  = goodstaxService.queryGoodstax(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", "");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
	@RequestMapping(value = "/getGoodstaxById", method = RequestMethod.POST)
	@ResponseBody
	public String getGoodstaxByNo(@RequestBody String data) {
		Goodstax goodstax=JSONObject.parseObject(data, Goodstax.class);
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			goodstax.setEntid(token.getEntid());
			Goodstax gt  = goodstaxService.getGoodstaxById(goodstax);
			returnJson.put("code", "0");
			returnJson.put("data", gt);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

    	return returnJson.toJSONString();
	}

	@RequestMapping(value = "/updateGoodstax", method = RequestMethod.POST)
	@ResponseBody
	public String updateGoodstax(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Goodstax goodstax=JSONObject.parseObject(data, Goodstax.class);
		try {	
			Token token = Token.getToken();
			goodstax.setEntid(token.getEntid());
			returnJson.put("entid", token.getEntid());
			goodstax.setCrud("U");
			goodstax.setEdittype("0");//0录入  1批量导入
			goodstax.setLoginid(token.getLoginid());
			goodstax.setProcessTime(new Date());
			goodstax.setUsername(token.getUsername());
			Taxitem taxitem = goodstaxService.getTaxitemById(goodstax.getTaxitemid());
			if(taxitem==null){
				 return  new RtnData(-1,"税目编码没有找到").toString();
			}
			goodstaxService.updateGoodstax(goodstax);

			returnJson.put("code", "0");
			returnJson.put("data", "");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
	@RequestMapping(value = "/getGoodstaxCount")
	@ResponseBody
	public String getGoodstaxCount(@RequestBody String data){
		    JSONObject jo = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			int count = goodstaxService.getGoodstaxCount(jo);
			jo.put("count", count);
			return new RtnData(jo).toString();
		} catch (Exception e) {
			return new RtnData(-1,e.getMessage()).toString();
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/uploadGoodstax", method = RequestMethod.POST)
	public  String uploadGoodstax(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") CommonsMultipartFile file) throws Exception{
		List<Goodstax> suclist=new ArrayList<Goodstax>();
		List<Map<String,String>> sucMsg=new ArrayList<Map<String,String>>();
		List<Map<String,String>> errlist=new ArrayList<Map<String,String>>();
		JSONObject returnJson = new JSONObject();

		String realPath="";

		if (file != null) {
			String os = System.getProperty("os.name");
			if(os.toLowerCase().startsWith("win")){
				realPath = "c:\\uploadfile\\";
			}else if(os.toLowerCase().startsWith("Lin")){
				realPath = "/tmp/";
			}

			File newfile = new File(realPath+file.getFileItem().getName());

			if(!newfile.getParentFile().exists()) {
				//如果目标文件所在的目录不存在，则创建父目录
				if(!newfile.getParentFile().mkdirs()) {
					throw new Exception("创建目录：" + newfile.getParentFile() + "失败");
				}
			}

			FileInputStream fis = null;
			org.apache.poi.ss.usermodel.Workbook wb = null;

			file.transferTo(newfile);

			try {
				org.apache.poi.ss.usermodel.Sheet sheet = null;

				String[] split = newfile.getName().split("\\."); // .是特殊字符，需要转义！！！！！

				// 先判一下后缀名是不是Excel文件
				if (!"xls".equals(split[1].toLowerCase()) && !"xlsx".equals(split[1].toLowerCase()))
				{
					throw new Exception("Excel文件格式不正确!");
				}

				// 根据文件后缀（xls/xlsx）进行判断
				if ("xls".equals(split[1].toLowerCase())) {
					fis = new FileInputStream(newfile); // 文件流对象
					wb = new HSSFWorkbook(fis);
				}

				if ("xlsx".equals(split[1].toLowerCase())) {
					fis = new FileInputStream(newfile); // 文件流对象
					wb = new XSSFWorkbook(fis);
				}

				sheet= wb.getSheetAt(0);

				int firstRowIndex = sheet.getFirstRowNum() + 1; // 第一行是列名，所以不读
				int lastRowIndex = sheet.getLastRowNum();

				for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) { // 遍历行
					String entid="";
					String goodsid="";
					String goodsname="";
					String goodstype="";
					String taxitemid="";
					String taxitename="";
					Double taxrate=0.00;
					String taxpre="";
					String taxprecon="";
					String zerotax="";
					entid=Token.getToken().getEntid();//企业ID
					Map<String,String> map=new HashMap<String,String>();

					Row row = sheet.getRow(rIndex);

					if (row == null) break;

					int firstCellIndex = row.getFirstCellNum();
					int lastCellIndex = row.getLastCellNum();

					if(firstCellIndex==-1) break;

					for (int cIndex = firstCellIndex; cIndex <= lastCellIndex; cIndex++) { // 遍历列
						Cell cell = row.getCell(cIndex);
						if (cell != null) {
							if(cIndex==1){
								goodsid=cell.getStringCellValue();//商品编码
							}else if(cIndex==2){
								goodsname=cell.getStringCellValue();//商品名称
							}else if(cIndex==3){
								goodstype=cell.getStringCellValue();//商品类别
							}else if(cIndex==4){
								taxitemid=cell.getStringCellValue();//税目代码
							}else if(cIndex==5){
								taxitename=cell.getStringCellValue();//税目名称
							}else if(cIndex==6){
								taxrate=cell.getNumericCellValue();//税率
							}else if(cIndex==7){
								taxpre = String.valueOf(cell.getNumericCellValue());//是否享受税收优惠政策
								if(taxpre.equals("0.0")){
									taxpre="0";
								}else if(taxpre.equals("1.0")){
									taxpre="1";
								}
							}else if(cIndex==8){
								taxprecon=cell.getStringCellValue();;//优惠政策类型
							}else if(cIndex==9){
								zerotax=String.valueOf(cell.getNumericCellValue());//零税率标识
								if(zerotax.equals("0.0")){
									zerotax="0";
								}else if(zerotax.equals("1.0")){
									zerotax="1";
								}else if(zerotax.equals("2.0")){
									zerotax="2";
								}else if(zerotax.equals("3.0")){
									zerotax="3";
								}
							}
						}
					}

					Goodstax goodstax = new Goodstax(entid,goodsid,goodsname,taxitemid,taxrate,taxpre,taxprecon,zerotax);

					if (goodstax.getTaxitemid() == null) goodstax.setTaxitemid("");

					if(goodstax.getTaxitemid().trim().length()!=19){
						map.put("status","税目长度必须19位！");
						map.put("goodsid",goodstax.getGoodsid());
						map.put("goodsname",goodstax.getGoodsname());
						map.put("taxitemid",goodstax.getTaxitemid());
						errlist.add(map);
						continue;
					}

					if(!(goodstax.getTaxrate()>=0 && goodstax.getTaxrate()<=0.13)){
						map.put("status","税率必须介于0到0.13之间！");
						map.put("goodsid",goodsid);
						map.put("goodsname",goodsname);
						map.put("taxitemid",taxitemid);
						errlist.add(map);
						continue;
					}

					if (goodstax.getTaxpre()==null) goodstax.setTaxpre("0");

					if(!(goodstax.getTaxpre().trim()=="1"||goodstax.getTaxpre().trim()=="0")){
						map.put("status","享受税收优惠政策必须是0或者1！");
						map.put("goodsid",goodstax.getGoodsid());
						map.put("goodsname",goodstax.getGoodsname());
						map.put("taxitemid",goodstax.getTaxitemid());
						errlist.add(map);
						continue;
					}

					if(goodstax.getTaxpre().trim()=="1"&& (goodstax.getTaxprecon()== null || goodstax.getTaxprecon()=="")){
						map.put("status","优惠政策类型不能为空！");
						map.put("goodsid",goodstax.getGoodsid());
						map.put("goodsname",goodstax.getGoodsname());
						map.put("taxitemid",goodstax.getTaxitemid());
						errlist.add(map);
						continue;
					}

					if(!(goodstax.getZerotax().trim()==""||goodstax.getZerotax()=="0"||goodstax.getZerotax()=="1"|| goodstax.getZerotax()=="2"|| goodstax.getZerotax()=="3")){
						map.put("status","零税率标识错误！");
						map.put("goodsid",goodstax.getGoodsid());
						map.put("goodsname",goodstax.getGoodsname());
						map.put("taxitemid",goodstax.getTaxitemid());
						errlist.add(map);
						continue;
					}

					goodstax.setEdittype("1");
					goodstax.setProcessTime(new Date());
					goodstax.setLoginid(Token.getToken().getLoginid());

					Taxitem taxitem = goodstaxService.getTaxitemById(goodstax.getTaxitemid());

					if(taxitem==null){
						Taxitem tempItem=new Taxitem();
						tempItem.setTaxversion("30.0");
						tempItem.setTaxitemid(taxitemid);
						tempItem.setTaxitemname(taxitename);
						tempItem.setTaxrate(taxrate.toString());
						tempItem.setTaxpre(taxpre);
						tempItem.setTaxprecon(taxprecon);
						tempItem.setZerotax(zerotax);
						tempItem.setTaxitemmode("N");
						taxitemService.addTaxitemByExcel(tempItem);
					}

					map.put("status", "正常");
					map.put("goodsid", goodstax.getGoodsid());
					map.put("goodsname", goodstax.getGoodsname());
					map.put("taxitemid", goodstax.getTaxitemid());
					suclist.add(goodstax);
					sucMsg.add(map);
				}

				if(errlist.size()==0){
					try {
						for(int i=0;i<suclist.size();i++){
							Goodstax goodstaxs=suclist.get(i);
							Goodstax goodtax = goodstaxService.getGoodstaxById(goodstaxs);
							if(goodtax == null){
								goodstaxs.setCrud("I");
								goodstaxService.insertGoodstax(goodstaxs);
							}else{
								goodstaxs.setCrud("U");
								goodstaxService.updateGoodstax(goodstaxs);
							}
						}
					}catch (Exception e){
						throw new Exception(e.getMessage());
					}

					returnJson.put("code", "1");
					returnJson.put("sucMsg", sucMsg);
				}else{
					returnJson.put("code", "0");
					returnJson.put("errlist", errlist);
				}
			}
			catch (Exception e){
				throw new Exception(e.getMessage());
			}
			finally
			{
				if (wb !=null) wb.close();
				if (fis !=null) fis.close();
				deleteExcel(newfile);
			}
		}

		return returnJson.toJSONString();
	}

    public boolean deleteExcel(File file){
    	boolean flag = false;
    	// 推断文件夹或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 推断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                file.delete();
                flag = true;
            }
        }
        return flag;
    }
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
		}
	public static boolean IsNumber(String str) {
		String regex = "[0-9]+";
		return match(regex, str);
		}
	
	@RequestMapping(value = "/deleteGoodstax", method = RequestMethod.POST)
	@ResponseBody
	public String deleteGoodstax(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		Goodstax goodstax=JSONObject.parseObject(data, Goodstax.class);
		try {
			Token token = Token.getToken();
			goodstax.setEntid(token.getEntid());
			goodstax.setCrud("D");
			goodstax.setEdittype("0");//0录入  1批量导入
			goodstax.setLoginid(token.getLoginid());
			goodstax.setProcessTime(new Date());
			goodstax.setUsername(token.getUsername());
			goodstaxService.deleteGoodstax(goodstax);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}
	public String creatErrExcel(List<HashMap<String,String>> errlist){
		
		String excelName = "错误或重复数据.xls";
		 try {  
		  
		   File excelFile = new File(excelName);  
		   // 如果文件存在就删除它  
		   if (excelFile.exists())  
		    excelFile.delete();  
		   // 打开文件  
		   WritableWorkbook book = Workbook.createWorkbook(excelFile);  
		   // 文字样式  
		   jxl.write.WritableFont wfc = new jxl.write.WritableFont(  
		     WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,  
		     UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		  
		   	jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(wfc);  
		  
		   	//根据数据大小具体分n个sheet页，默认一页存储1000条数据
	        int sizeLoop = errlist.size();//数据大小
	        int size = errlist.size();
	        if(sizeLoop < 1000){
	            sizeLoop = 1000;
	        }
	        int sheetSize = 1000;
	        int loopSize = sizeLoop/sheetSize;
	        if(sizeLoop%sheetSize!=0){
	            loopSize+=1;
	        }
	        String[] heads = {"商品编码","商品名称","税目代码"};
	        String[] headsStr= {"goodsid","goodsname","taxitemid"};
	        //分别往每个sheet页写数据
	        for(int l = 0;l<loopSize;l++){
	            WritableSheet sheet = book.createSheet("第"+(l+1)+"页", l);
	             
	            for(int i=0;i<heads.length;i++){
	                Label cell = new Label(i,0, heads[i],wcfFC);
	                sheet.addCell(cell );
	            }
	             
	            //循环读取数据列表
	            int n = 1;
	            for(int i=l*sheetSize;i<(l+1)*sheetSize && i<=size-1;i++){
	                Object vrd = errlist.get(i);
	                for(int j = 0;j<headsStr.length;j++){
	                    Object value = PropertyUtils.getProperty(vrd, headsStr[j]);
                        sheet.setColumnView(j, value.toString().length()+10);
                        sheet.addCell(new Label(j,n,value.toString(),wcfFC));
	                }
	                n++;
	            }
	        }
		  
		   // 写入数据并关闭文件  
		   book.write();  
		   book.close();  
		   return excelName;
		  } catch (Exception e) {  
			  e.printStackTrace();
		  }
		 return excelName;
	}
	
	@RequestMapping(value="/downloaderrdata")
	 @ResponseBody
	public void downloaderrdata(HttpServletRequest request, HttpServletResponse response,String excelName)
            throws ServletException, IOException {
		 InputStream in = null;
		 OutputStream out = null; 
		 try {  
		   
		   byte[] bytes = new byte[1024];
	        int len = 0;
	        response.addHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes(),"iso-8859-1"));
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
		  }finally {  
		        in.close(); 
		        out.flush();
		        out.close();
		     }  
		}
	
	@RequestMapping(value = "/queryGoodstaxlog", method = RequestMethod.POST)
	@ResponseBody
	public String queryGoodstaxlog(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			Page.cookPageInfo(returnJson);
			int count = goodstaxService.getGoodstaxCountlog(returnJson); 
			List<HashMap<String,String>> goodstax  = goodstaxService.queryGoodstaxlog(returnJson);
			return new RtnData(goodstax,count).toString();	
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();	
		}
	}
}