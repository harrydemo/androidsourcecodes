package com.pixtas.models.option;

import com.pixtas.models.DatabaseAdapter;

import android.database.Cursor;

public class DataBaseOption {
//	private static final String TAG = "DataBaseOption";
	public static DatabaseAdapter databaseAdapter = null;
	/*�ж�flash�����Ƿ�������*/
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
	/*����flash��������*/
	public static void insertDataToFlashTB(String title1,String title2,String title3,int time,String url){
		databaseAdapter.openFlashDB();
		databaseAdapter.insertFlashData(title1, title2, title3, time, url);
		databaseAdapter.closeFlashDatabase();
	}
	/*ɾ��flash���е�����*/
	public static void deleteFlashData(){
		databaseAdapter.openFlashDB();
		databaseAdapter.deleteFlashData();
		databaseAdapter.closeFlashDatabase();
	}
	/*��ѯ���е�flash����*/
	public static Cursor fechtchAllFlashData(){
		databaseAdapter.openFlashDB();
		Cursor c = databaseAdapter.fetchAllFlashData();
		databaseAdapter.closeFlashDatabase();
		return c;
	}
	
	/*�ж��Ƿ��Ѿ���������*/
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
	/*�� Install���������*/
	public static void insertInstallData(String title,String url,int num,int flag,int v){
	
		databaseAdapter.openInstallDB();
		databaseAdapter.insertInstallData(title, url, num, flag, v);
		databaseAdapter.closeInstallDatabase();
	
	}
	/*ɾ�����е�����*/
	public static void deleteInstallData(){
		databaseAdapter.openInstallDB();
		databaseAdapter.deleteInsatllData();
		databaseAdapter.closeInstallDatabase();
	}
	/*�жϱ����Ƿ�������*/
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
	
	/*����Install���е�install�ֶ�����*/
	public static void updateInstallData(int rowId,int flag){
		databaseAdapter.openInstallDB();
		databaseAdapter.updateInsallFlag(rowId, flag);
		databaseAdapter.closeInstallDatabase();
	}
	
	/*����Install���е�version�ֶ�����*/
	public static void updateInstallVersion(int rowId,int v){
		databaseAdapter.openInstallDB();
		databaseAdapter.updateInsallVersion(rowId, v);
		databaseAdapter.closeInstallDatabase();
	}
	
	/*��ѯinstall���е�����*/
	public static Cursor fetchInstallData(){
		databaseAdapter.openInstallDB();
//		Cursor c = databaseAdapter.fechInstallDataById(0);
		Cursor c = databaseAdapter.fechAllInstallData();
//		c.moveToFirst();
		databaseAdapter.closeInstallDatabase();
		
		return c;
	}
}
