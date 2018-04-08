package cn.android.mytaobao.daompl;

import java.util.ArrayList;
import java.util.List;

import cn.android.mytaobao.dao.ICategoryService;
import cn.android.mytaobao.model.Category;

public class CategoryService implements ICategoryService {

	List<Category> categories = new ArrayList<Category>();
	@Override
	public List<Category> getAllCategory() {
		
	
		categories.add(new Category(1, "�ֻ�"));
		categories.add(new Category(2, "����"));
		categories.add(new Category(3, "��װ"));
		categories.add(new Category(4, "ͼ��"));

		return categories;
	}

	@Override
	public Category getCategoryById(int categoryId) {
		if (categories!=null)
			return this.categories.get(categoryId-1);
		else
			return null;
	}

}
