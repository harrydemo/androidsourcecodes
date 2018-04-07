package com.flygis.gold;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelp extends SQLiteOpenHelper{
	private static final String DBNAME = "gold.db";
	private static final int VERSION = 1;
	
	public DBhelp(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS filedown (id integer primary key autoincrement, downpath varchar(100)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS filedown");
		onCreate(db);
	}
	
	public void AddRec(SQLiteDatabase db,  String downpath){
		db.execSQL("insert into filedown(downpath) values(?)",
				new Object[]{downpath});
	}
	
	public void delall(SQLiteDatabase db){
		db.execSQL("delete from filedown");		
	}
	
	public Map<Integer, String> Queryall(SQLiteDatabase db){
		int i = 0;
		Cursor cursor = db.rawQuery("select downpath from ? order by id",new String[]{"filedown"});
		Map<Integer, String> data = new HashMap<Integer, String>();
		while(cursor.moveToNext()){
			data.put(i, cursor.getString(0));
			i++;
		}
		cursor.close();
		db.close();
		return data;
	}
}
