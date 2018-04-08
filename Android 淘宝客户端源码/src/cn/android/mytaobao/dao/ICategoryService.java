package cn.android.mytaobao.dao;

import java.util.List;
import cn.android.mytaobao.model.Category;

public interface ICategoryService {
	/**
	 * �õ�ϵͳ�������
	 * @return �������
	 */
	public List<Category> getAllCategory();

	/**
	 * �������Id��ȡ���ʵ��
	 * @param categoryId ���ID
	 * @return ���ʵ��
	 */
	public Category getCategoryById(int categoryId);
}
