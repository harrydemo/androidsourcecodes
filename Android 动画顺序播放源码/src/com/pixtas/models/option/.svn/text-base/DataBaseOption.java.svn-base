package com.pixtas.models.option;

import com.pixtas.models.DatabaseAdapter;

import android.database.Cursor;

public class DataBaseOption {
//	private static final String TAG = "DataBaseOption";
	public static DatabaseAdapter databaseAdapter = null;
	/*判断flash表中是否有数据*/
	public static boolean flashHasData(){
		boolean hasData = false;
		databaseAdapter.openFlashDB();
		Cursor c = databaseAdapter.fetchAllFlashData();
		if(c.getCount() > 0){
			hasData = true;
		}
		databaseAdapter.closeFlashDatabase();
		return hasData;
	}
	/*插入flash表中数据*/
	public static void insertDataToFlashTB(String title1,String title2,String title3,int time,String url){
		databaseAdapter.openFlashDB();
		databaseAdapter.insertFlashData(title1, title2, title3, time, url);
		databaseAdapter.closeFlashDatabase();
	}
	/*删除flash表中的数据*/
	public static void deleteFlashData(){
		databaseAdapter.openFlashDB();
		databaseAdapter.deleteFlashData();
		databaseAdapter.closeFlashDatabase();
	}
	/*查询所有的flash数据*/
	public static Cursor fechtchAllFlashData(){
		databaseAdapter.openFlashDB();
		Cursor c = databaseAdapter.fetchAllFlashData();
		databaseAdapter.closeFlashDatabase();
		return c;
	}
	
	/*判断是否已经有下载完*/
	public static boolean isInstall(){
		boolean isInstall = false;
		databaseAdapter.openInstallDB();
		Cursor c = databaseAdapter.fechAllInstallData();
		if (c.getCount() > 0 && c.getInt(c.getColumnIndex("install")) == 1){
			isInstall = true;
		}
		c.close();
		databaseAdapter.closeInstallDatabase();
		
		return isInstall;
	}
	/*向 Install表插入数据*/
	public static void insertInstallData(String title,String url,int num,int flag,int v){
	
		databaseAdapter.openInstallDB();
		databaseAdapter.insertInstallData(title, url, num, flag, v);
		databaseAdapter.closeInstallDatabase();
	
	}
	/*删除表中的数据*/
	public static void deleteInstallData(){
		databaseAdapter.openInstallDB();
		databaseAdapter.deleteInsatllData();
		databaseAdapter.closeInstallDatabase();
	}
	/*判断表中是否有数据*/
	public static boolean installHasData(){
		boolean hasData = false;

		databaseAdapter.openInstallDB();
//		Cursor c = databaseAdapter.fechInstallDataById(0);
		Cursor c = databaseAdapter.fechAllInstallData();
//		c.moveToFirst();
		if(c.getCount() > 0){
			hasData = true;
		}
		databaseAdapter.closeInstallDatabase();
	
		return hasData;
	}
	
	/*更新Install表中的install字段数据*/
	public static void updateInstallData(int rowId,int flag){
		databaseAdapter.openInstallDB();
		databaseAdapter.updateInsallFlag(rowId, flag);
		databaseAdapter.closeInstallDatabase();
	}
	
	/*更新Install表中的version字段数据*/
	public static void updateInstallVersion(int rowId,int v){
		databaseAdapter.openInstallDB();
		databaseAdapter.updateInsallVersion(rowId, v);
		databaseAdapter.closeInstallDatabase();
	}
	
	/*查询install表中的数据*/
	public static Cursor fetchInstallData(){
		databaseAdapter.openInstallDB();
//		Cursor c = databaseAdapter.fechInstallDataById(0);
		Cursor c = databaseAdapter.fechAllInstallData();
//		c.moveToFirst();
		databaseAdapter.closeInstallDatabase();
		
		return c;
	}
}
