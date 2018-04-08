package cn.android.mytaobao.dao.biz;

import java.util.List;

import cn.android.mytaobao.dao.ICategoryService;
import cn.android.mytaobao.daompl.CategoryService;
import cn.android.mytaobao.model.Category;

public class CategoryManager {
    private ICategoryService dao;
    public CategoryManager(){
    	dao = new CategoryService();
    }
    
    public List<Category> getAllCategory() {
	   return dao.getAllCategory();
	}


	public Category getCategoryById(int categoryId) {
		return dao.getCategoryById(categoryId);
	}
}
