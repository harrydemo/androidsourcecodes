package com.sly.android.huangcun.entrey;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ConnectionProvider {
	private static SQLiteDatabase db = null;

	private Context ctx = null;

	private String TAG = "ConnectionProvider";
	private SQLiteDatabase database;

	public ConnectionProvider(Context ctx) {
		this.ctx = ctx;
//		if (db == null) {
			Log.v("ConnectionProvider", "ctx=!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + ctx.toString());
			DatabaseHelper dbHelper = new DatabaseHelper(ctx, "ebook", null, 1);
			db = dbHelper.getWritableDatabase();
//		}
	}

	public SQLiteDatabase getConnection() {
		return db;
	}

	public void closeConnection() {
		db.close();
	}

	public boolean isTableExits(String tablename) {
		boolean result = false;// 表示不存在
		String str = "select count(*) xcount  from sqlite_master where table='"
				+ tablename + "'";
		Cursor cursor = db.rawQuery(str, null);
		int xcount = cursor.getColumnIndex("xcount");
		if (xcount != 0) {
			result = true; // 表存在
		}
		if (cursor != null) {
			cursor.close();
		}

		return result;
	}

}