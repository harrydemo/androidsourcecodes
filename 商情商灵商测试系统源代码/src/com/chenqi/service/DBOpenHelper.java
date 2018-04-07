package com.chenqi.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "3QTest.db"; //数据库名称
	private static final int DATABASEVERSION = 3;//数据库版本

	public DBOpenHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE userinfo (userid integer primary key autoincrement, username varchar(20), pwd varchar(20),sex char(2),age integer,telephone char(11))");//执行有更改的sql语句
		db.execSQL("CREATE TABLE shengxiao (_id integer primary key autoincrement, shengxiaoname char(2), tedian varchar(200))");//执行有更改的sql语句
		db.execSQL("CREATE TABLE xingzuo (_id integer primary key autoincrement, xingzuoname varchar(10), tedian varchar(200),timerange varchar(200))");//执行有更改的sql语句
		db.execSQL("CREATE TABLE xuexing (_id integer primary key autoincrement, xuexingname varchar(10), tedian varchar(200))");//执行有更改的sql语句
		db.execSQL("CREATE TABLE peidui (_id integer primary key autoincrement, nanxingzuo varchar(10), nvxingzuo varchar(10),tedian varchar(200))");//执行有更改的sql语句
		db.execSQL("insert into userinfo ( username , pwd ,sex ,age ,telephone ) values('chenqi','123456','男',23,'15879171693')");//执行有更改的sql语句
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS userinfo");
		onCreate(db);
	}

}
