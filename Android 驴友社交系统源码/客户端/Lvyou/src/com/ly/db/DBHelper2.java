package com.ly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper2 extends SQLiteOpenHelper {
	private static String datebase_name = "memory_datebase";
	public static String table_name = "memory_table2";
	public DBHelper2(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	 public DBHelper2(Context c){
	    	this(c,datebase_name,null,1);
	 }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("create table "+table_name+" (memory_id integer primary key autoincrement,memory_title text,memory_content text,memory_address text,memory_time text,m_u_id text,mtag_title text,mtag_type text,mtag_content text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table "+table_name);
		this.onCreate(db);
	}

}
