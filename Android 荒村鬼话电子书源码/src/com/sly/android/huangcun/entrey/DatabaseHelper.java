package com.sly.android.huangcun.entrey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private String TAG = "DatabaseHelper";
	private Context ctx = null;

	private String TABLE_NAME = "bookmark";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, "ebook", null,1);
		ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		if (db == null)
//		db.openOrCreateDatabase("ebook.db", null);
		creatNewIDcard(db);
	}

	private void creatNewIDcard(SQLiteDatabase db) {
		Log.i(TAG, "--------database is create  ---------");
		db.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME +";");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ "bookmarkID" + " Integer," + "bookmarkName" + " TEXT" + ","
				+ "bookPage" + " TEXT,"+"bookName" + " Integer" + ");");
		
		System.out.println("Ω®±Ì”Ôæ‰========================"+"CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ "bookmarkID" + " Integer," + "bookmarkName" + " TEXT" + ","
				+ "bookPage" + " TEXT,"+"bookName" + " Integer" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
}