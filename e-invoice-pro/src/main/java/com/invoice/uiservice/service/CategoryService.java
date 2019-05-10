package com.invoice.uiservice.service;


import com.invoice.bean.db.CategoryAdd;

import java.util.List;
import java.util.Map;

public interface CategoryService {
	
	void addCategory(CategoryAdd category) throws Exception;
	
	void updateCategory(CategoryAdd category) throws Exception;
	
	void deleteCategory(CategoryAdd category) throws Exception;
	
	 List<CategoryAdd> getCategory(Map<String, Object> p)throws Exception;

	CategoryAdd getCategoryById(CategoryAdd category) throws Exception;

    int getCategoryCount(Map<String, Object> p);

	int getCategoryByDeptlevelid(CategoryAdd category) throws Exception;
}
