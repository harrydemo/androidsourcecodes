package com.android.datastore;


/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import java.util.ArrayList;

import com.android.datastruct.ListItemBean;
import com.android.datastruct.TwintterBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager
{
	class DatabaseHelper extends SQLiteOpenHelper
	{

		final DatabaseManager this$0;

		public void onCreate(SQLiteDatabase sqlitedatabase)
		{
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS collection_table (open_url TEXT PRIMARY KEY NOT NULL, img_src TEXT, video_len VARCHAR(200), type VARCHAR(300) );");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS twintter_table (otoken TEXT PRIMARY KEY NOT NULL, otoken_secret TEXT );");
		}

		public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
		{
		}

		public DatabaseHelper(Context context)
		{
			super(context, "sph_database", null, 3);
			this$0 = DatabaseManager.this;
		}
	}

	public static final String TAG = "VLC/DatabaseManager";
	private static DatabaseManager instance;
	private final String COLLECT_TABLE_NAME = "collection_table";
	private final String DB_NAME = "sph_database";
	private final int DB_VERSION = 3;
	private final String IMG_SRC = "img_src";
	private final String OPEN_URL = "open_url";
	private final String OTOKEN = "otoken";
	private final String OTOKEN_SECRET = "otoken_secret";
	private final String TWINTTER_TABLE_NAME = "twintter_table";
	private final String VIDEO_LEN = "video_len";
	private final String VIDEO_TITLE = "type";
	private Context mContext;
	private SQLiteDatabase mDb;

	//数据库的管理函数
	private DatabaseManager(Context context){
		mContext = context;
		mDb = (new DatabaseHelper(context)).
			getWritableDatabase();
	}

	/**
	 * @deprecated Method getInstance is deprecated
	 */

	//获取数据库对象
	public static DatabaseManager getInstance(Context context){
		try {
			if (instance == null) {
				instance = new DatabaseManager(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return instance;
	}

	/**
	 * @deprecated Method SelectAll is deprecated
	 */

	//获取数据
	public ArrayList SelectAll(){
		ArrayList arraylist = new ArrayList();
		String as[] = new String[4];
		as[0] = "open_url";
		as[1] = "img_src";
		as[2] = "video_len";
		as[3] = "type";

		try {
			Cursor cursor = mDb.query("collection_table", as, null, null, null,
					null, null);
			boolean flag = cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					int i = cursor.getColumnCount();
					ListItemBean listitembean = new ListItemBean();
					String s = cursor.getString(0);
					listitembean.setVideoHref(s);
					String s1 = cursor.getString(1);
					listitembean.setVideoImgSrc(s1);
					String s2 = cursor.getString(2);
					listitembean.setVideoLength(s2);
					String s3 = cursor.getString(3);
					listitembean.setVideoTitle(s3);
					boolean flag1 = arraylist.add(listitembean);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return arraylist;

	}

	/**
	 * @deprecated Method SelectTwintterToken is deprecated
	 */

	public TwintterBean SelectTwintterToken(){
		TwintterBean twintterbean = null;
		
		try {
			twintterbean = new TwintterBean();
			String as[] = new String[2];
			as[0] = "otoken";
			as[1] = "otoken_secret";
			Cursor cursor = mDb.query("twintter_table", as, null, null, null,
					null, null);
			boolean flag = cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				if (cursor.moveToNext()) {
				}
				String s = cursor.getString(0);
				twintterbean.SetToken(s);
				String s1 = cursor.getString(1);
				twintterbean.SetTokenSecret(s1);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return twintterbean;
		
	}

	/**
	 * @deprecated Method addListTime is deprecated
	 */

	//添加listtime
	public void addListTime(ListItemBean listitembean){
	
		try {
			ContentValues contentvalues = new ContentValues();
			String open_url = listitembean.getVideoHref();
			contentvalues.put("open_url", open_url);
			String img_src = listitembean.getVideoImgSrc();
			contentvalues.put("img_src", img_src);
			String video_len = listitembean.getVideoLength();
			contentvalues.put("video_len", video_len);
			String type = listitembean.getVideoTitle();
			contentvalues.put("type", type);
			mDb.replace("collection_table", "NULL", contentvalues);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	/**
	 * @deprecated Method addTwintterToken is deprecated
	 */

	public void addTwintterToken(TwintterBean twintterbean){
	
		try {
			int i = mDb.delete("twintter_table", null, null);
			ContentValues contentvalues = new ContentValues();
			String s = twintterbean.GetToken();
			contentvalues.put("otoken", s);
			String s1 = twintterbean.GetTokenSecret();
			contentvalues.put("otoken_secret", s1);
			long l = mDb.replace("twintter_table", "NULL", contentvalues);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	/**
	 * @deprecated Method removeMedia is deprecated
	 */

	public void removeMedia(String s){
		try {
			SQLiteDatabase sqlitedatabase = mDb;
			String as[] = new String[1];
			as[0] = s;
			int i = sqlitedatabase.delete("collection_table", "open_url=?", as);
		} catch (Exception e) {
			e.printStackTrace();
			return;
			// TODO: handle exception
		}
	}

	/**
	 * @deprecated Method removeTwintterToken is deprecated
	 */

	public void removeTwintterToken(){
		try {
			mDb.delete("twintter_table", null, null);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
