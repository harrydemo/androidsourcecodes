package cn.android.mytaobao.daompl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.android.mytaobao.R;

import cn.android.mytaobao.dao.IProductService;
import cn.android.mytaobao.model.Product;

public class ProductService implements IProductService{
	/*Goods goods1 = new Goods("三星 GT-S5830", "手机", R.drawable.menu_new_user,
			"1630元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods2 = new Goods("HTC A510e", "手机", R.drawable.menu_new_user,
			"1514元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods3 = new Goods("三星 I9100", "手机", R.drawable.menu_new_user,
			"3266元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods4 = new Goods("中兴 U880（TD版）", "手机",
			R.drawable.menu_new_user, "989元",
			"网络类型:TD-SCDMA(3G) 操作系统:ANDROID  ");
	Goods goods5 = new Goods("Sony Ericsson索尼爱立信 ", "手机",
			R.drawable.menu_new_user, "2584元",
			"网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods6 = new Goods("摩托罗拉 Defy", "手机", R.drawable.menu_new_user,
			"1851元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods7 = new Goods("中兴 V880", "手机", R.drawable.menu_new_user,
			"957元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods8 = new Goods("HTC S710e", "手机", R.drawable.menu_new_user,
			"2671元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods9 = new Goods("摩托罗拉 ME525", "手机", R.drawable.menu_new_user,
			"1853元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods10 = new Goods("G12", "手机", R.drawable.menu_new_user,
			"2470元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods11 = new Goods("摩托罗拉 ME525+", "手机",
			R.drawable.menu_new_user, "1923元",
			"网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods12 = new Goods("Sony Ericsson", "手机",
			R.drawable.menu_new_user, "2231元",
			"网络类型:WCDMA(3G) 操作系统:ANDROID ");
	Goods goods13 = new Goods("Lenovo", "手机", R.drawable.menu_new_user,
			"997元", "网络类型:WCDMA(3G) 操作系统:ANDROID ");*/
	private Product[] products={
			new Product(1,"三星 GT-S5830", 1, R.drawable.p1,
					1630, "网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(2,"HTC A510e", 1, R.drawable.p2,
							1514, "网络类型:WCDMA(3G) 操作系统:ANDROID ")	,	
			new Product(3,"三星 I9100", 1, R.drawable.p3,
					3266, "网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(4,"中兴 U880（TD版）", 1,
					R.drawable.p4, 989,
							"网络类型:TD-SCDMA(3G) 操作系统:ANDROID  "),
			new Product(5,"Sony Ericsson索尼爱立信 ", 1,
					R.drawable.p5, 2584,
									"网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(6,"摩托罗拉 Defy", 1, R.drawable.p6,
				1851, "网络类型:WCDMA(3G) 操作系统:ANDROID "),
				new Product(7,"中兴 V880", 1, R.drawable.p7,
						957, "网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(8,"HTC S710e", 1, R.drawable.p8,
								2671, "网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(9,"摩托罗拉 ME525",1, R.drawable.p9,
										1853, "网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(10,"G12", 1, R.drawable.p10,
												2470, "网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(11,"摩托罗拉 ME525+", 1,
					R.drawable.p11, 1923,
														"网络类型:WCDMA(3G) 操作系统:ANDROID "),	
			new Product(12,"Sony Ericsson", 1,
					R.drawable.p12, 2231,"网络类型:WCDMA(3G) 操作系统:ANDROID "),
			new Product(13,"Lenovo", 1, R.drawable.p13,997, "网络类型:WCDMA(3G) 操作系统:ANDROID ")		
	};
	
	private List<Product> ps =null; 
	
	public ProductService(){
		ps = new ArrayList<Product>();
		for(Product p:products){
			ps.add(p);
		}
	}
	@Override
	public List<Product> getAll() {
		return this.ps;
	}
	
	
	

	/**
	 * 分页存取数据
	 * params:
	 * pageIndex:代表是第几页(首页从0开始)
	 * pageSize:代表每页多少条记录
	 * return:返回的就是分页之后的数据
	 */
	@Override
	public List<Product> getByPager(int pageIndex, int pageSize) {	
		if (pageIndex<0) pageIndex=0; 
		int totalCount = ps.size();  //总条数
		int pageCount=1; //总页数
		if (totalCount % pageSize==0){
			pageCount = totalCount/pageSize;
		}else{
			pageCount = (totalCount/pageSize)+1;
		}
		
		if (pageIndex>pageCount-1)//说明是最后一页
		{
			//pageIndex = pageCount-1;
			return null;
		}
		
		Object[] source = this.ps.toArray();
		
		 int endIndex=(pageIndex+1)*pageSize;
		if (endIndex>totalCount)
			endIndex=totalCount;
		
	
		List<Product> result = new ArrayList<Product>();
		for(int i=pageIndex*pageSize;i<endIndex;i++){
			result.add((Product)source[i]);
		}
		return result;
	}


	@Override
	public Product getById(int productId) {
		for(Product p:this.ps){
			if (p.getId()==productId){
				return p;
			}
		}	
		return null;
	}

	@Override
	public List<Product> getByName(String name) {
		List<Product> result = new ArrayList<Product>();
		for(Product p:this.ps){
			if (p.getName().indexOf(name)!=-1)
				result.add(p);
		}
		
		return result;
	}

	@Override
	public void Insert(Product p) {
		this.ps.add(p);
	}

	@Override
	public void Modify(Product product) {
		for(int i=0;i<this.ps.size();i++){
			if (product.getId()==ps.get(i).getId()){
				ps.set(i, product);
				break;
			}
		}
	}

	@Override
	public void Del(int id) {
		Product p= this.getById(id);
		if (p!=null)
			this.ps.remove(p);
	}


}
