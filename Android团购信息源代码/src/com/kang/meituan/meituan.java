package com.kang.meituan;

import java.io.Serializable;

public class meituan implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String url;
	
	private String deal_id;
	private String website;
	private String city_name;
	private String deal_title;
	private String deal_img;
	private String deal_desc;
	private String price;
	private String value;
	private String rebate;
	
	private String sales_num;
	private long start_time;
	private long end_time;
	
	private String shop_name;
	private String shop_addr;
	private String shop_area;
	private String shop_tel;
	
	
	
	public void setId(int id)
	{
		this.id = id;
	}
	public int getId()
	{
		return id;
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getDeal_id()
	{
		return deal_id;
	}
	public void setDeal_id(String deal_id)
	{
		this.deal_id = deal_id;
	}
	public String getWebsite()
	{
		return website;
	}
	public void setWebsite(String website)
	{
		this.website = website;
	}
	public String getCity_name()
	{
		return city_name;
	}
	public void setCity_name(String city_name)
	{
		this.city_name = city_name;
	}
	public String getDeal_title()
	{
		return deal_title;
	}
	public void setDeal_title(String deal_title)
	{
		this.deal_title = deal_title;
	}
	public String getDeal_img()
	{
		return deal_img;
	}
	public void setDeal_img(String deal_img)
	{
		this.deal_img = deal_img;
	}
	public String getDeal_desc()
	{
		return deal_desc;
	}
	public void setDeal_desc(String deal_desc)
	{
		this.deal_desc = deal_desc;
	}
	public String getPrice()
	{
		return price;
	}
	public void setPrice(String price)
	{
		this.price = price;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getRebate()
	{
		return rebate;
	}
	public void setRebate(String rebate)
	{
		this.rebate = rebate;
	}
	public String getSales_num()
	{
		return sales_num;
	}
	public void setSales_num(String sales_num)
	{
		this.sales_num = sales_num;
	}
	public long getStart_time()
	{
		return start_time;
	}
	public void setStart_time(long start_time)
	{
		this.start_time = start_time;
	}
	public long getEnd_time()
	{
		return end_time;
	}
	public void setEnd_time(long end_time)
	{
		this.end_time = end_time;
	}
	public String getShop_name()
	{
		return shop_name;
	}
	public void setShop_name(String shop_name)
	{
		this.shop_name = shop_name;
	}
	public String getShop_addr()
	{
		return shop_addr;
	}
	public void setShop_addr(String shop_addr)
	{
		this.shop_addr = shop_addr;
	}
	public String getShop_area()
	{
		return shop_area;
	}
	public void setShop_area(String shop_area)
	{
		this.shop_area = shop_area;
	}
	public String getShop_tel()
	{
		return shop_tel;
	}
	public void setShop_tel(String shop_tel)
	{
		this.shop_tel = shop_tel;
	}
	@Override
	public String toString()
	{
		return "meituan [id=" + id + ", url=" + url + ", deal_id=" + deal_id
				+ ", website=" + website + ", city_name=" + city_name
				+ ", deal_title=" + deal_title + ", deal_img=" + deal_img
				+ ", deal_desc=" + deal_desc + ", price=" + price + ", value="
				+ value + ", rebate=" + rebate + ", sales_num=" + sales_num
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", shop_name=" + shop_name + ", shop_addr=" + shop_addr
				+ ", shop_area=" + shop_area + ", shop_tel=" + shop_tel + "]";
	}
	
	
	

	
	
	
	
}