package com.ly.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AddPlaceHelper extends SQLiteOpenHelper{
	private static String db_name="dibiao";
	public static String table_name="place_table";

	public AddPlaceHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public  AddPlaceHelper(Context c){
		this(c,db_name,null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table "+table_name+" (user_id integer primary key autoincrement,title text,type text,content text,id text)");
		}	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String user_name=null;
		// TODO Auto-generated method stub
		db.execSQL("drop table "+user_name);
		this.onCreate(db);
	}

}
