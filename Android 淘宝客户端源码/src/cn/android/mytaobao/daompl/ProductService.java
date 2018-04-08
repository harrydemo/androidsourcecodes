package cn.android.mytaobao.daompl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.android.mytaobao.R;

import cn.android.mytaobao.dao.IProductService;
import cn.android.mytaobao.model.Product;

public class ProductService implements IProductService{
	/*Goods goods1 = new Goods("���� GT-S5830", "�ֻ�", R.drawable.menu_new_user,
			"1630Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods2 = new Goods("HTC A510e", "�ֻ�", R.drawable.menu_new_user,
			"1514Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods3 = new Goods("���� I9100", "�ֻ�", R.drawable.menu_new_user,
			"3266Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods4 = new Goods("���� U880��TD�棩", "�ֻ�",
			R.drawable.menu_new_user, "989Ԫ",
			"��������:TD-SCDMA(3G) ����ϵͳ:ANDROID  ");
	Goods goods5 = new Goods("Sony Ericsson���ᰮ���� ", "�ֻ�",
			R.drawable.menu_new_user, "2584Ԫ",
			"��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods6 = new Goods("Ħ������ Defy", "�ֻ�", R.drawable.menu_new_user,
			"1851Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods7 = new Goods("���� V880", "�ֻ�", R.drawable.menu_new_user,
			"957Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods8 = new Goods("HTC S710e", "�ֻ�", R.drawable.menu_new_user,
			"2671Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods9 = new Goods("Ħ������ ME525", "�ֻ�", R.drawable.menu_new_user,
			"1853Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods10 = new Goods("G12", "�ֻ�", R.drawable.menu_new_user,
			"2470Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods11 = new Goods("Ħ������ ME525+", "�ֻ�",
			R.drawable.menu_new_user, "1923Ԫ",
			"��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods12 = new Goods("Sony Ericsson", "�ֻ�",
			R.drawable.menu_new_user, "2231Ԫ",
			"��������:WCDMA(3G) ����ϵͳ:ANDROID ");
	Goods goods13 = new Goods("Lenovo", "�ֻ�", R.drawable.menu_new_user,
			"997Ԫ", "��������:WCDMA(3G) ����ϵͳ:ANDROID ");*/
	private Product[] products={
			new Product(1,"���� GT-S5830", 1, R.drawable.p1,
					1630, "��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(2,"HTC A510e", 1, R.drawable.p2,
							1514, "��������:WCDMA(3G) ����ϵͳ:ANDROID ")	,	
			new Product(3,"���� I9100", 1, R.drawable.p3,
					3266, "��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(4,"���� U880��TD�棩", 1,
					R.drawable.p4, 989,
							"��������:TD-SCDMA(3G) ����ϵͳ:ANDROID  "),
			new Product(5,"Sony Ericsson���ᰮ���� ", 1,
					R.drawable.p5, 2584,
									"��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(6,"Ħ������ Defy", 1, R.drawable.p6,
				1851, "��������:WCDMA(3G) ����ϵͳ:ANDROID "),
				new Product(7,"���� V880", 1, R.drawable.p7,
						957, "��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(8,"HTC S710e", 1, R.drawable.p8,
								2671, "��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(9,"Ħ������ ME525",1, R.drawable.p9,
										1853, "��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(10,"G12", 1, R.drawable.p10,
												2470, "��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(11,"Ħ������ ME525+", 1,
					R.drawable.p11, 1923,
														"��������:WCDMA(3G) ����ϵͳ:ANDROID "),	
			new Product(12,"Sony Ericsson", 1,
					R.drawable.p12, 2231,"��������:WCDMA(3G) ����ϵͳ:ANDROID "),
			new Product(13,"Lenovo", 1, R.drawable.p13,997, "��������:WCDMA(3G) ����ϵͳ:ANDROID ")		
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
	 * ��ҳ��ȡ����
	 * params:
	 * pageIndex:�����ǵڼ�ҳ(��ҳ��0��ʼ)
	 * pageSize:����ÿҳ��������¼
	 * return:���صľ��Ƿ�ҳ֮�������
	 */
	@Override
	public List<Product> getByPager(int pageIndex, int pageSize) {	
		if (pageIndex<0) pageIndex=0; 
		int totalCount = ps.size();  //������
		int pageCount=1; //��ҳ��
		if (totalCount % pageSize==0){
			pageCount = totalCount/pageSize;
		}else{
			pageCount = (totalCount/pageSize)+1;
		}
		
		if (pageIndex>pageCount-1)//˵�������һҳ
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
