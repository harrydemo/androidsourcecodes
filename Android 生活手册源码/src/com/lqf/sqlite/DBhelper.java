package com.lqf.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
	private final static String DB_NAME = "RiJi.db";// 数据库名
	private final static int DB_VERSION = 1;// 版本号

	private final static String TAB_NAME = "riji";// 表名
	public final static String ID = "_id";// 日记的ID
	public final static String TITLE = "title";// 日记的标题
	public final static String MARCH = "march";// 日记的日期
	public final static String NEIRONG = "neirong";// 日记的内容


	// 重写构造函数
	public DBhelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	// 数据库第一次创建时被调用
	public void onCreate(SQLiteDatabase db) {
		// 创建《日记表》
		db.execSQL("create table " + TAB_NAME + "(" + ID
				+ " integer primary key autoincrement," + TITLE + " text,"
				+ MARCH + " text," + NEIRONG + " text)");

	}

	// 版本升级时被调用
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 日记表的增删改查
	 */
	// 添加
	public long rijiinsert(String title, String neirong, String march) {
		// 调用SQLiteDatabase里的getWritableDatabase()的方法
		SQLiteDatabase db = this.getWritableDatabase();
		// 获取一个数据库实例化对象
		ContentValues cv = new ContentValues();
		// 指定要添加的值
		cv.put("title", title);
		cv.put("march", march);
		cv.put("neirong", neirong);
		// 与数据库绑定
		long row = db.insert(TAB_NAME, null, cv);
		return row;
	}

	// 删除
	public void rijidelete(int id) {
		// 调用SQLiteDatabase里的getWritableDatabase()的方法
		SQLiteDatabase db = this.getWritableDatabase();
		// 写删除的语句
		db.delete(TAB_NAME, ID +"=?", new String[]{String.valueOf(id)});
	}
	// 修改
	
	// 查询
	public Cursor rijiselect(){
		// 调用SQLiteDatabase里的getWritableDatabase()的方法
		SQLiteDatabase db = this.getWritableDatabase();
		//查询语句
		Cursor cursor = db.query(TAB_NAME, null, null, null, null, null, null);
		//返回值
		return cursor;
	}

}
