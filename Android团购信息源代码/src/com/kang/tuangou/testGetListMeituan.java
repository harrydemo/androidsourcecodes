package com.kang.tuangou;
//Download by http://www.codefans.net
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.kang.database.DataIUDS;
import com.kang.database.tuangouData;
import com.kang.meituan.meituan;
import com.kang.meituan.tuan_handler;

public class testGetListMeituan extends AndroidTestCase
{
	public void testGetList() throws Exception{
		
		String path = "http://www.meituan.com/api/v2/beijing/deals";
		URL url = new URL(path);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

		int responseCode = httpConn.getResponseCode();

		if (responseCode == 200)
		{
			InputStream input = httpConn.getInputStream();
			
//			XMLHandler handler = new XMLHandler();
			
			List<meituan> meituans = tuan_handler.getListMeituan(input, tuan_handler.MEITUAN);
			
//			DataIUDS data = new DataIUDS(this.getContext());
//
			for(meituan mei : meituans){
				System.out.println(mei);
			}
		}
		
	}
	
	public void testDatabase()throws Exception{
		tuangouData data = new tuangouData(this.getContext());
		data.getWritableDatabase();
	}
	
	public void testfind() throws Exception{
		DataIUDS data = new DataIUDS(this.getContext());
		meituan mei = data.find(2);
		
		System.out.println(mei);
		
	}
	
	public void testupdate() throws Exception{
		DataIUDS data = new DataIUDS(this.getContext());
		meituan mei = new meituan();
		mei.setCity_name("ÎÞÎý");
		data.update(mei, 1);
		meituan mei1 =data.find(1);
		System.out.println(mei1);
	}
	
	public void testdelete() throws Exception{
		DataIUDS data = new DataIUDS(this.getContext());
		data.delete(1);
		
		meituan mei = data.find(1);
		System.out.println(mei);
		
		
	}
	
	public void testgetListMeituan() throws Exception{
		boolean noData = false;
		DataIUDS data = new DataIUDS(this.getContext());
		
		List<meituan> meituans = new ArrayList<meituan>();
		meituans = data.getListData(0, 10);
		noData = meituans.isEmpty() ? true : false;
		
		for(meituan mei : meituans){
			
			System.out.println(mei);
		}
		
		System.out.println(noData);
		

	}
	
	public void testgetCount() throws Exception{
		
		DataIUDS data = new DataIUDS(this.getContext());
		int count = data.getCount();
		
		System.out.println(count);
		
		
	}
	
//	public void testDataIUDSSave() throws Exception{
//		
//		meituan mei = new meituan();
//		mei.setWebsite("");
//		mei.setCity_name("");
//		mei.setd
//		
//		data.save(mei);
//		
//		
//	}
}
