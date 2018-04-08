package cn.android.mytaobao.dao;

import java.util.List;

import cn.android.mytaobao.model.Product;

public interface IProductService {
	/**
	 * 得到所有商品
	 * 
	 * @return
	 */
	public List<Product> getAll();
	
	public List<Product> getByPager(int pageIndex,int pageSize);

	/**
	 * 根据主键ID获取商品实体
	 * 
	 * @param productId
	 * @return
	 */
	public Product getById(int productId);

	/**
	 * 根据商品名称进行模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public List<Product> getByName(String name);

	/**
	 * 添加一个商品
	 * 
	 * @param product
	 */
	public void Insert(Product product);

	/**
	 * 修改一个商品信息
	 * 
	 * @param product
	 */
	public void Modify(Product product);

	/**
	 * 删除一个商品
	 * 
	 * @param id
	 */
	public void Del(int id);

}
