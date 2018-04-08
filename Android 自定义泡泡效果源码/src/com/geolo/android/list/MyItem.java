package com.geolo.android.list;

import android.graphics.Bitmap;

public class MyItem
{
	private String name;
	private String infomation;
	private String age;
	private int icon;
	private int id;
	private int layout;
    private Bitmap img;
	public MyItem(int id, String name,String a,String info, int icon,int lay) {
		this.setCaption(name);
		this.setAge(a);
		this.setInfo(info);
		this.setIcon(icon);
		this.setId(id);
		this.setLay(lay);
	}
	public void setCaption(String caption) {
		this.name = caption;
	}
	public String getName() {
		return name;
	}
	public void  setAge(String a)
	{
		age = a;
	}
	public String getAge()
	{
		return age;
	}
	public void setInfo(String info)
	{
		infomation = info;
	}
	public String getInfo()
	{
		return infomation;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public void setLay(int layout)
	{
		this.layout = layout;
	}
	public int getIcon() {
		return icon;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setBitMap(Bitmap bitImg)
	{
		img = bitImg;
	}
	public Bitmap  getBitMap()
	{
		return img;
	}
}
