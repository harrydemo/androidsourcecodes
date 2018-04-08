package com.android.datastruct;
/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import java.util.ArrayList;
import java.util.Iterator;

/*
 * list数据页接口
 * */
public class ListPagesBean
{

	//焦点索引
	private int focusIndex;
	//页面数组
	private ArrayList pageArray;

	//构造函数
	public ListPagesBean(){
		focusIndex = 0;
		pageArray = new ArrayList();
	}

	//像pageArray中添加数据
	public void Add(String Url, String PageIndex){
		PageBean pagebean =  new PageBean();
		boolean flag = true;
		Iterator iterator = pageArray.iterator();
		
		pagebean.SetUrl(Url);
		pagebean.SetPageIndex(PageIndex);
		
		while(iterator.hasNext()){
			
			PageBean mPageBean = ((PageBean)iterator.next());
			
			if (mPageBean.GetUrl().compareTo(Url) != 0){
				flag = true;
			}else{
				flag = false;
				break;
			}
		}
		if(flag){
			pageArray.add(pagebean);
		}
		
	}

	//获取上一页
	public PageBean GetBackPageBean(){
		if (focusIndex > 0){
			focusIndex  -=   1;
		} else{
			focusIndex = 0;
		}
		
		return (PageBean)pageArray.get(focusIndex);
	}

	public int GetIndex()
	{
		return focusIndex;
	}

	//获取list的长度
	public int GetListLength(){
		return pageArray.size();
	}

	//获取下一页
	public PageBean GetNextPageBean(){
		int length = GetListLength();
		
		if (focusIndex < length){
			focusIndex +=  1;
		} else{
			focusIndex = pageArray.size() - 1;
		}

		return (PageBean)pageArray.get(focusIndex);
	}
}
