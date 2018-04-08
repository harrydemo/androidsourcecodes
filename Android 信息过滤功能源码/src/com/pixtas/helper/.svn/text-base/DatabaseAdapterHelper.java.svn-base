package com.pixtas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAdapterHelper {
//	private static final String TAG = "DataBAseAdapter";
	private static final String KEY_ID = "_id";
	private static final String KEY_DATA = "data";
	

	//数据库名
	private static final String DB_NAME = "sms_database";
	
	//基本数据表名
	private static final String DB_TABLE = "sms_table";
	
	//数据库版本
	private static final int DB_VERSION = 1;
	
	//本地Context对象
	private Context mContext = null;
	
	//创建sms_table表
	private static final String DB_CREATE = "CREATE TABLE " + DB_TABLE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATA + " INTEGER)";

	private SQLiteDatabase db_sms = null;
	
	private DatabaseHelper dbHelper_sms = null;

	private static class DatabaseHelper  extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	/*构造函数取得Context*/
	public DatabaseAdapterHelper(Context context){
		this.mContext = context;
	}
	//打开sms数据库，返回数据库对象
	public void open_sms() throws SQLException{
		dbHelper_sms = new DatabaseHelper(mContext);
		db_sms = dbHelper_sms.getWritableDatabase();
	}
	
	//关闭sms数据库
	public void close_sms(){
		db_sms.close();
		dbHelper_sms.close(); 
	}
	

	//插入一条数据
	public long insertData(int data){
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATA, data);
		return db_sms.insert(DB_TABLE, KEY_ID, cv);
	}
	
	//删除一条数据
	public boolean deleteData(long rowId){
		return db_sms.delete(DB_TABLE, KEY_ID + "=" + rowId, null) > 0;
	}
	//删除所有照片数据
	public void deleteAllSmsData(){
		db_sms.delete(DB_TABLE, KEY_ID + "<" + 999999, null);
	}
	//查询所有数据
	public Cursor fetchAllData(){
		Cursor c = db_sms.query(DB_TABLE, new String[] {KEY_ID,KEY_DATA}, null,null,null,null,null);
		if(c != null){
			c.moveToFirst();
		}
		return c;
	}
	//查询指定数据
	public Cursor fetchDataById(long rowId) throws SQLException {
		Cursor cursor = db_sms.query(true, DB_TABLE, new String[]{KEY_ID,KEY_DATA}, KEY_ID + "=" + rowId , null,null,null,null,null);
		if(cursor != null){
			cursor.moveToFirst();
		}
		return cursor;
	}

	//更新一条数据
	public boolean updateSmsData(long rowId,int data){
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATA, data);
		return db_sms.update(DB_TABLE,cv,KEY_ID + "=" + rowId,null) > 0;
	}
	
}
