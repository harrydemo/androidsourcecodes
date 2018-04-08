package com.lfp.busactivity;

import com.lfp.service.ImportDBFile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class Database extends SQLiteOpenHelper {

	public Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	private static final String NAME="android.db";
	private static final int version=1;
	
	public Database(Context context) {
		super(context, NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//db = SQLiteDatabase.openOrCreateDatabase(ImportDBFile.DB_PATH + "/" + ImportDBFile.DB_NAME, null);
		db.execSQL("CREATE TABLE bus_line(id integer primary key autoincrement,line varchar(20),station varcahr(500))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS bus_line");
		onCreate(db);

	}
	
	@Override    
	 public void onOpen(SQLiteDatabase db) {     
	         super.onOpen(db);       
	         // TODO 每次成功打开数据库后首先被执行     
	     }   


}
