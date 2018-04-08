package cn.android.mytaobao.model;

import cn.android.mytaobao.R;

/**
 * 产品实体
 * @author jhao
 * @version 1.0
 */
public class Product {
	private int id;
	private String name;
	private int categoryId;
	private int photo;
	private double unitPrice;
	private String note;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getPhoto() {
		if (this.id==1)
		   return R.drawable.p1;
		else if (this.id==2)
		   return R.drawable.p2;
		else if (this.id==3)
			   return R.drawable.p3;
		else if (this.id==4)
			   return R.drawable.p4;
		else if (this.id==5)
			   return R.drawable.p5;
		else if (this.id==6)
			   return R.drawable.p6;
		else if (this.id==7)
			   return R.drawable.p7;
		else if (this.id==8)
			   return R.drawable.p8;
		else if (this.id==9)
			   return R.drawable.p9;
		else if (this.id==10)
			   return R.drawable.p10;
		else if (this.id==11)
			   return R.drawable.p11;
		else if (this.id==12)
			   return R.drawable.p12;
		else if  (this.id==13)
			   return R.drawable.p13;
		return R.drawable.icon;
		   
	}

	public void setPhoto(int photo) {
		this.photo = photo;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param id
	 * @param name
	 * @param categoryId
	 * @param photo
	 * @param unitPrice
	 * @param note
	 */
	public Product(int id, String name, int categoryId, int photo,
			double unitPrice, String note) {
		super();
		this.id = id;
		this.name = name;
		this.categoryId = categoryId;
		this.photo = photo;
		this.unitPrice = unitPrice;
		this.note = note;
	}
	
	public Product(){}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", categoryId="
				+ categoryId + ", photo=" + photo + ", unitPrice=" + unitPrice
				+ ", note=" + note + "]";
	}
	

}
