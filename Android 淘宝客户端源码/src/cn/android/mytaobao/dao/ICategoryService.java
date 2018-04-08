package cn.android.mytaobao.dao;

import java.util.List;
import cn.android.mytaobao.model.Category;

public interface ICategoryService {
	/**
	 * 得到系统所有类别
	 * @return 所有类别
	 */
	public List<Category> getAllCategory();

	/**
	 * 根据类别Id获取类别实体
	 * @param categoryId 类别ID
	 * @return 类别实体
	 */
	public Category getCategoryById(int categoryId);
}
