package com.alex.media;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {
	private Cursor c = null;
	private static final String CREATE_TAB = "create table "
		+ "music(_id integer primary key autoincrement,music_id integer,clicks integer," +
				"latest text)";
	private static final String TAB_NAME = "music";
	private SQLiteDatabase db = null;
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL(CREATE_TAB);
	}
	
	public void insert(ContentValues values){
		SQLiteDatabase db = getWritableDatabase();
		db.insert(TAB_NAME, null, values);
		db.close();
	}

	public void update(ContentValues values,int id){
		SQLiteDatabase db = getWritableDatabase();
		db.update(TAB_NAME, values, "music_id="+id, null);
		db.close();
	}
	
	public void delete(int id){
		if (db == null){
			db = getWritableDatabase();
		}
		db.delete(TAB_NAME, "music_id=?", new String[]{String.valueOf(id)});
	}
	
	public Cursor query(int id){
		SQLiteDatabase db = getReadableDatabase();
		c = db.query(TAB_NAME, null, "music_id=?", new String[]{String.valueOf(id)}, null, null, null);
		db.close();
		return c;
	}
	
	public Cursor queryByClicks(){//按点击量查询
		SQLiteDatabase db = getReadableDatabase();
		c = db.query(TAB_NAME, null, null, null, null, null, "clicks desc");
		return c;
	}
	
	public Cursor queryRecently(){//按时间降序查询
		SQLiteDatabase db = getReadableDatabase();
		c = db.query(TAB_NAME, null, null, null, null, null, "latest desc");
		return c;
	}
	
	public void close(){
		if (db != null){
			db.close();
			db=null;
		}
		if (c!=null){
			c.close();
			c=null;
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
