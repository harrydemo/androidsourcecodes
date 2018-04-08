package cn.android.mytaobao.dao.biz;

import java.util.List;

import cn.android.mytaobao.dao.IProductService;
import cn.android.mytaobao.daompl.ProductService;
import cn.android.mytaobao.model.Product;

public class ProductManager {
	private IProductService dao = null;

	public ProductManager() {
		dao = new ProductService();
	}

	public List<Product> getProdcutByPager(int pageIndex, int pageSize) {
		return this.dao.getByPager(pageIndex, pageSize);
	}

	public List<Product> getProductByName(String name) {
		return this.dao.getByName(name);
	}

	public Product getProductById(int id) {
		return this.dao.getById(id);
	}

	public boolean AddProduct(Product p) throws Exception {
		try {
			this.dao.Insert(p);
			return true;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public boolean AddProcut(int id, String name, int categoryId, int photo,
			double unitPrice, String note) throws Exception {
		Product p = new Product(id, name, categoryId, photo, unitPrice, note);
		return this.AddProduct(p);
	}

	public boolean ModifyProduct(Product p) throws Exception {
		try {
			this.dao.Modify(p);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean ModifyProduct(int id, String name, int categoryId,
			int photo, double unitPrice, String note) throws Exception {
		// Product p = new Product(id, name, categoryId, photo, unitPrice,
		// note);
		Product p = this.getProductById(id);
		p.setName(name);
		p.setCategoryId(categoryId);
		p.setNote(note);
		p.setPhoto(photo);
		p.setUnitPrice(unitPrice);

		return this.ModifyProduct(p);
	}

	public boolean DelProduct(int id) throws Exception {
		try {
			this.dao.Del(id);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean DelProduct(Product p) throws Exception {
		return this.DelProduct(p.getId());
	}

	/**
	 * ÅúÁ¿É¾³ý
	 * @param ids
	 * @return
	 */
	public boolean DelProduct(List<Integer> ids) {
		try {
			for (Integer id : ids) {
				this.DelProduct(id);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
