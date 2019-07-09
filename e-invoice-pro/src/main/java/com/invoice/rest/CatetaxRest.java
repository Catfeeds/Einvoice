package com.invoice.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import jxl.Sheet;
import jxl.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.Category;
import com.invoice.bean.db.Catetax;
import com.invoice.bean.db.Taxitem;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.CatetaxService;
import com.invoice.uiservice.service.GoodstaxService;

@Controller
@RequestMapping(value = "/ui")
public class CatetaxRest {
	@Autowired
	CatetaxService catetaxService;
	
	@Autowired
	GoodstaxService goodstaxService;
	
	@RequestMapping(value = "/queryCategory", method = RequestMethod.POST)
	@ResponseBody
	public String queryCategory(@RequestBody String data) {
		try {
			Category jo=JSONObject.parseObject(data, Category.class);
			Token token = Token.getToken();
			jo.setEntid(token.getEntid());
			
			List<Category> list = catetaxService.queryCategory(jo);
			return new RtnData(list).toString();
		} catch (Exception e) {
			e.printStackTrace();	
			return new RtnData(-1, e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/getCatetaxById", method = RequestMethod.POST)
	@ResponseBody
	public String getCatetaxById(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		try {
			returnJson = JSONObject.parseObject(data);
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			List<Catetax> ct = catetaxService.getCatetaxById(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", ct);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
	@RequestMapping(value = "/insertCatetax", method = RequestMethod.POST)
	@ResponseBody
	public String insertCatetax(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			Catetax catetax=JSONObject.parseObject(data, Catetax.class);
			catetax.setEntid(token.getEntid());
			Taxitem taxitem = goodstaxService.getTaxitemById(catetax.getTaxitemid());
			if(taxitem==null){
				 return  new RtnData(-1,"税目编码没有找到").toString();
			}
			catetaxService.insertCatetax(catetax);
			returnJson.put("entid",token.getEntid());
			List<Catetax> ct  = catetaxService.getCatetaxById(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", ct);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}
	
	@RequestMapping(value = "/updateCatetax", method = RequestMethod.POST)
	@ResponseBody
	public String updateCatetax(@RequestBody String data) {
		JSONObject returnJson = JSONObject.parseObject(data);
		try {
			Catetax catetax=JSONObject.parseObject(data, Catetax.class);
			Token token = Token.getToken();
			returnJson.put("entid",token.getEntid());
			catetax.setEntid(token.getEntid());
			Taxitem taxitem = goodstaxService.getTaxitemById(catetax.getTaxitemid());
			if(taxitem==null){
				 return  new RtnData(-1,"税目编码没有找到").toString();
			}
			catetaxService.updateCatetax(catetax);
			
			List<Catetax> ct  = catetaxService.getCatetaxById(returnJson);
			returnJson.put("code", "0");
			returnJson.put("data", ct);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}

	@RequestMapping(value = "/deleteCatetaxByid", method = RequestMethod.POST)
	@ResponseBody
	public String deleteCatetaxByid(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		try {
			Catetax catetax=JSONObject.parseObject(data, Catetax.class);
			Token token = Token.getToken();
			catetax.setEntid(token.getEntid());
			catetaxService.deleteCatetax(catetax);
			returnJson.put("code", "0");
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	
	}

	@RequestMapping(value = "/getCatetaxCount")
	@ResponseBody
	public String getCatetaxCount(@RequestBody String data){
		JSONObject jo = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			jo.put("entid",token.getEntid());
			int count = catetaxService.getCatetaxCount(jo);
			jo.put("count", count);
			return new RtnData(jo).toString();
		} catch (Exception e) {
			return new RtnData(-1,e.getMessage()).toString();
		}

	}

	/**
	 * 导入excel（企业类目与税目关系表）
	 * @param request
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/uploadCatetax", method = RequestMethod.POST)
	public  String uploadCatetax(HttpServletRequest request,@RequestParam("file") CommonsMultipartFile file) throws Exception{
		List<Catetax> list=new ArrayList<Catetax>();
		if(file != null){
			String realPath = "d://uploadfile/";
			File newfile=new File(realPath+file.getFileItem().getName());
			 if(!newfile.getParentFile().exists()) {
		            //如果目标文件所在的目录不存在，则创建父目录
		            if(!newfile.getParentFile().mkdirs()) {}
		        }
			 file.transferTo(newfile);
			 JSONObject returnJson = new JSONObject();
			 try {
		            Workbook rwb=Workbook.getWorkbook(newfile);
		            Sheet rs=rwb.getSheet(0);
		            int clos=rs.getColumns();
		            int rows=rs.getRows();
		            for (int i = 1; i < rows; i++) {
		                for (int j = 0; j < clos; j++) {
		                    
		                    String entid=rs.getCell(j++, i).getContents();
		                    String cateid=rs.getCell(j++, i).getContents();
		                    String catename=rs.getCell(j++, i).getContents();
		                    String taxrate=rs.getCell(j++, i).getContents();
		                    String taxitemid=rs.getCell(j++, i).getContents();
		                    list.add(new Catetax(entid, cateid,catename, Double.valueOf(taxrate), taxitemid));
		                }
		            }
		            catetaxService.addCatetaxList(list);
		            returnJson.put("code", "0");
		            returnJson.put("status", true);
		        } catch (Exception e) {
		        	returnJson.put("code", "-1");
					returnJson.put("msg", e.getMessage());
		            e.printStackTrace();
		        } 
			 return returnJson.toJSONString();
		}
		return null;
	}
	
	/**
	 * 导入excel（企业类目）
	 * @param request
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/uploadCategory", method = RequestMethod.POST)
	public  String uploadCategory(HttpServletRequest request,@RequestParam("file") CommonsMultipartFile file) throws Exception{
		List<Category> list=new ArrayList<Category>();
		if(file != null){
			String realPath = "d://uploadfile/";
			File newfile=new File(realPath+file.getFileItem().getName());
			 if(!newfile.getParentFile().exists()) {
		            //如果目标文件所在的目录不存在，则创建父目录
		            if(!newfile.getParentFile().mkdirs()) {}
		        }
			 file.transferTo(newfile);
			 JSONObject returnJson = new JSONObject();
			 try {
		            Workbook rwb=Workbook.getWorkbook(newfile);
		            Sheet rs=rwb.getSheet(0);
		            int clos=rs.getColumns();
		            int rows=rs.getRows();
		            for (int i = 1; i < rows; i++) {
		                for (int j = 0; j < clos; j++) {
		                    
		                    String entid=rs.getCell(j++, i).getContents();
		                    String categoryid=rs.getCell(j++, i).getContents();
		                    String categoryname=rs.getCell(j++, i).getContents();
		                    String deptlevelid=rs.getCell(j++, i).getContents();
		                    String headcatid=rs.getCell(j++, i).getContents();
		                    String categorystatus=rs.getCell(j++, i).getContents();
		                    list.add(new Category(entid, categoryid, categoryname, Integer.valueOf(deptlevelid), headcatid, Integer.valueOf(categorystatus)));
		                }
		            }
		            catetaxService.addCategoryList(list);
		            returnJson.put("code", "0");
		            returnJson.put("status", true);
		        } catch (Exception e) {
		        	returnJson.put("code", "-1");
					returnJson.put("msg", e.getMessage());
		            e.printStackTrace();
		        } 
			 return returnJson.toJSONString();
		}
		return null;
	}

}

