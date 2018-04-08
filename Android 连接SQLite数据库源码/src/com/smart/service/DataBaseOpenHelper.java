package com.smart.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
	// 数据名称，
	private static final String DBNAME = "smrtDataBase";
	// 数据库版本
	private static final int version = 1;

	// 构造方法参数，
	public DataBaseOpenHelper(Context context) {
		super(context, DBNAME, null, version);
	}

	// 数据库创建表的名子。
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE person (personid integer primary key autoincrement,name varchar(20),age INTEGER)");

	}

	// 更新方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("EROP TABLE IF EXISTS person");
		onCreate(db);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
