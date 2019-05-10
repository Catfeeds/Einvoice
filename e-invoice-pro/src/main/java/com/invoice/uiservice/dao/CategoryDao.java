package com.invoice.uiservice.dao;

import com.invoice.bean.db.CategoryAdd;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("CategoryDao")
public interface CategoryDao {

    //新增分类
    public void insertCategory(CategoryAdd category) throws Exception;

    //编辑分类
    public void updateCategory(CategoryAdd category) throws Exception;

    //删除分类
    public void deleteCategory(CategoryAdd category) throws Exception;

    //界面
    List<CategoryAdd> getCategory(Map<String, Object> p);

    //修改
    CategoryAdd getCategoryById(CategoryAdd category) throws Exception;

	int getCategoryCount(Map<String, Object> p);

	//查询上下级关系
    int getCategoryByDeptlevelid(CategoryAdd category) throws Exception;
}
