package com.airport.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final int VERSION = 1;
	SQLiteDatabase db = null;

	public DBHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	// ��SQLiteOepnHelper�����൱�У������иù��캯��
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// ����ͨ��super���ø��൱�еĹ��캯��
		super(context, name, factory, version);
		// ֻ�е�����DatabaseHelper�����getReadableDatabase()������������getWritableDatabase()����֮�󣬲Żᴴ�������һ�����ݿ�
		// getReadableDatabase();

	}

	public void createOrDelTable(String sql) {
		db = getReadableDatabase();
		db.execSQL(sql);
	}

	public long insert(String table, String column, ContentValues values) {
		db = getWritableDatabase();
		return db.insert(table, column, values);
	}

	public int update(String table, ContentValues values, String where,
			String[] whereArgs) {
		db = getWritableDatabase();
		return db.update(table, values, where, whereArgs);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		db = getWritableDatabase();
		return db.delete(table, whereClause, whereArgs);
	}

	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		db = getReadableDatabase();
	   return db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);
	}

	public void exeecSQL(String sql) {
		db = getWritableDatabase();
		db.execSQL(sql);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table cities (_id INTEGER PRIMARY KEY AUTOINCREMENT,cityname text)";
		db.execSQL(sql);
		Log.i("TAG", "create darabase--------" + sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("TAG", "update Database------------->");
		db.execSQL("delete from cities_db");

	}

}
