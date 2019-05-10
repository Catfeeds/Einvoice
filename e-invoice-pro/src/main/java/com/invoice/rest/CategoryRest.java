package com.invoice.rest;

import com.alibaba.fastjson.JSONObject;
import com.invoice.bean.db.CategoryAdd;
import com.invoice.bean.ui.Token;
import com.invoice.rtn.data.RtnData;
import com.invoice.uiservice.service.CategoryService;
import com.invoice.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/ui")
public class CategoryRest {
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/getCategory", method = RequestMethod.POST)
	@ResponseBody
	public String GetCategory(@RequestBody String data){
		JSONObject jo = JSONObject.parseObject(data);
		try {
			Token token = Token.getToken();
			jo.put("entid", token.getEntid());
			Page.cookPageInfo(jo);
			int count = categoryService.getCategoryCount(jo);
			List<CategoryAdd> tfo = categoryService.getCategory(jo);
			return new RtnData(tfo,count).toString();
		} catch (Exception e) {
			return new RtnData(-1, e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/insertCategory",method = RequestMethod.POST)
	@ResponseBody
	public String addCategory(@RequestBody String data){
		JSONObject jo = new JSONObject();
		CategoryAdd category=JSONObject.parseObject(data, CategoryAdd.class);
		try {
			Token token = Token.getToken();
			category.setEntid(token.getEntid());
			CategoryAdd ct = categoryService.getCategoryById(category);
			if(ct==null){
				if(category.getDeptlevelid()==1){
					categoryService.addCategory(category);
					jo.put("code", "0");
					jo.put("data", category);
				}else{
					int deptlevelid  = categoryService.getCategoryByDeptlevelid(category);
					if(deptlevelid!=0){
						categoryService.addCategory(category);
						jo.put("code", "0");
						jo.put("data", category);
					}else{
						jo.put("code", "-2");
						jo.put("msg", "此商品的品类级别对应的上级品类ID不存在！");
					}

				}

			}else{
				jo.put("code", "-1");
				jo.put("msg", "此商品品类ID已经添加过，如需更改请在编辑模块进行编辑！");
			}

		} catch (Exception e) {
			jo.put("code", "-1");
			jo.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jo.toJSONString();
	}

	@RequestMapping(value = "/getCategoryById", method = RequestMethod.POST)
	@ResponseBody
	public String getCategoryById(@RequestBody String data) {
		JSONObject returnJson = new JSONObject();
		CategoryAdd category=JSONObject.parseObject(data, CategoryAdd.class);
		try {
			Token token = Token.getToken();
			category.setEntid(token.getEntid());

			CategoryAdd ct = categoryService.getCategoryById(category);
			returnJson.put("code", "0");
			returnJson.put("data", ct);
		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}

		return returnJson.toJSONString();
	}


	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	@ResponseBody
	public String updateCategory(@RequestBody String data){
		JSONObject returnJson = new JSONObject();
		CategoryAdd category=JSONObject.parseObject(data, CategoryAdd.class);

		try {
			Token token = Token.getToken();
			category.setEntid(token.getEntid());
			if (category.getDeptlevelid()==1){
				categoryService.updateCategory(category);
				List<CategoryAdd> ct  = categoryService.getCategory(returnJson);
				returnJson.put("code", "0");
				returnJson.put("data", ct);
			}else{
				int deptlevelid  = categoryService.getCategoryByDeptlevelid(category);
				if(deptlevelid!=0){
					categoryService.updateCategory(category);
					List<CategoryAdd> ct  = categoryService.getCategory(returnJson);
					returnJson.put("code", "0");
					returnJson.put("data", ct);
				}else{
					returnJson.put("code", "-2");
					returnJson.put("msg", "此商品的品类级别对应的上级品类ID不存在！");
				}
			}

		} catch (Exception e) {
			returnJson.put("code", "-1");
			returnJson.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return returnJson.toJSONString();
	}

	@RequestMapping(value = "/getCategoryCount")
	@ResponseBody
	public String getCategoryCountCount(@RequestBody String data){

		try {
			JSONObject jo = JSONObject.parseObject(data);
			int count = categoryService.getCategoryCount(jo);
			jo.put("count", count);
			return new RtnData(jo).toString();
		} catch (Exception e) {
			return new RtnData(-1,e.getMessage()).toString();
		}

	}

}
