package com.feicong.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class PageUtil {
	public static int pagesize = 20;
	public int currentpage;
	private IQQService service;
	private int count;
	private int pagecount;
	
	public PageUtil(IQQService service){
		super();
		this.service = service;
		currentpage = 1;
		count = new Long(service.queryCount()).intValue();
		pagecount = count / pagesize;
		if(count % pagesize != 0) pagecount++;
		Log.i("QQMsgLook", String.valueOf(currentpage) + ' '
				+ String.valueOf(count) + ' ' 
				+ String.valueOf(pagecount));
	}
	
	public int getPagecount(){
		return pagecount;
	}

	public int getCount(){
		return count;
	}

	public int getCurrentpage(){
		return currentpage;
	}
	
	public void firstPage(){
		currentpage = 1;
	}
	
	public void nextPage(){
		currentpage++;
	}
	
	public void prevPage(){
		currentpage--;
	}
	
	public void endPage(){
		currentpage = pagecount;
	}
	public void setCurrentpage(int currentpage){
		this.currentpage = currentpage;
	}

	public ArrayList<HashMap<String, String>> getList() {
		if (currentpage == pagecount) {
			return service.setAdapterListData((pagecount - 1) * pagesize, pagesize);
		} else if (currentpage == 1){
			return service.setAdapterListData(0, pagesize);
		}else {
			return service.setAdapterListData((currentpage - 1) * pagesize, pagesize);
		}
	}
}
