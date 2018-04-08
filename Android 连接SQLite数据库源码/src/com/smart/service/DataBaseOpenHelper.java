package com.smart.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
	// �������ƣ�
	private static final String DBNAME = "smrtDataBase";
	// ���ݿ�汾
	private static final int version = 1;

	// ���췽��������
	public DataBaseOpenHelper(Context context) {
		super(context, DBNAME, null, version);
	}

	// ���ݿⴴ��������ӡ�
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE person (personid integer primary key autoincrement,name varchar(20),age INTEGER)");

	}

	// ���·���
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("EROP TABLE IF EXISTS person");
		onCreate(db);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
