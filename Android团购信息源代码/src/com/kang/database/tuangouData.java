package com.kang.database;
//Download by http://www.codefans.net
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class tuangouData extends SQLiteOpenHelper
{
	private final static String DATA_BASE = "tuangou.db"; // 数据库名称
	private final static int DATA_VERSION = 1; // 数据库版本
	private final static String CREATE_TABLE = "create table tuan (_id integer primary key autoincrement,url text, website varchar(20), deal_id varchar(20), city_name text , deal_title text , deal_img text , deal_desc text , price varchar(20), value varchar(20) , rebate varchar(20) , sales_num varchar(20) , start_time integer , end_time integer , shop_name varchar(100), shop_addr varchar(100), shop_area varchar(100), shop_tel varchar(30))";

	public tuangouData(Context context)
	{
		super(context, DATA_BASE, null, DATA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}
}
