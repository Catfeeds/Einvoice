package com.invoice.uiservice.service.impl;

import com.invoice.bean.db.CategoryAdd;
import com.invoice.uiservice.dao.CategoryDao;
import com.invoice.uiservice.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {

	@Resource(name = "CategoryDao")
	CategoryDao categoryDao;


	@Override
	public void addCategory(CategoryAdd category) throws Exception {
		categoryDao.insertCategory(category);
	}

	@Override
	public void updateCategory(CategoryAdd category) throws Exception {
		categoryDao.updateCategory(category);
	}

	@Override
	public void deleteCategory(CategoryAdd category) throws Exception {
		categoryDao.deleteCategory(category);
	}

	@Override
	public List<CategoryAdd> getCategory(Map<String, Object> p) throws Exception {
		return categoryDao.getCategory(p);
	}

	@Override
	public CategoryAdd getCategoryById(CategoryAdd category) throws Exception {
		return categoryDao.getCategoryById(category);
	}

	@Override
	public int getCategoryCount(Map<String, Object> p) {
		return categoryDao.getCategoryCount(p);
	}

	@Override
	public int getCategoryByDeptlevelid(CategoryAdd category) throws Exception {
		return categoryDao.getCategoryByDeptlevelid(category);
	}

}
