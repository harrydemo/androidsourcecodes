package com.ldci.t56.mobile.db;



import com.ldci.t56.mobile.info.Call_Forbid_Info;
import com.ldci.t56.mobile.info.Call_Record_Info;
import com.ldci.t56.mobile.info.Message_Forbid_Info;
import com.ldci.t56.mobile.info.Message_Rubbish_Info;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
	
	private static final String TAG = "DbAdapter";
	private DatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mSQLiteDatabase;
	private Context mContext;
	private static final String DB = "db.db";
	private static final int DB_VERSION = 1;
	
	//	5个表：短信黑名单表，短信垃圾箱表，来电黑名单表，来电拦截记录表
	public static final String MESSAGE_FORBID_TABLE_NAME = "message_forbid_table";
	public final static String MESSAGE_RUBBISH_TABLE_NAME = "message_rubbish_table";
	public final static String CALL_FORBID_TABLE_NAME = "call_forbid_table";
	public final static String CALL_RECORD_TABLE_NAME = "call_record_table";
	//	公用id字段
	public static final String TABLE_ID = "_id";
	// 短信黑名单表字段：来电号码
	public final static String MESSAGE_FORBID_PHONE = "message_forbid_phone";
	//	短信垃圾箱表字段：垃圾短信号码，收件时间，短信内容
	public static final String MESSAGE_RUBBISH_PHONE = "message_rubbish_phone";
	public static final String MESSAGE_RUBBISH_TIME = "message_rubbish_time";
	public final static String MESSAGE_RUBBISH_CONTENT = "message_rubbish_content";
	//	来电黑名单表字段：黑名单号码		
	public static final String CALL_FORBID_PHONE = "call_forbid_phone";
	//	来电拦截记录表字段：黑名单号码：来电时间，来电归属地
	public static final String CALL_RECORD_PHONE = "call_record_phone";
	public static final String CALL_RECORD_TIME = "call_record_time";
	public static final String CALL_RECORD_AREA = "call_record_area";
	
	public DbAdapter(Context mCtx){
		Log.d(TAG, "mCtxmTab");
		mContext=mCtx;
	}
	
	public void close(){
		Log.d(TAG, "close");
		mDatabaseHelper.close();
	}
	
	public DbAdapter open(){
		Log.d(TAG, "open");
		mDatabaseHelper=new DatabaseHelper(mContext);
		mSQLiteDatabase=mDatabaseHelper.getWritableDatabase();
		return this;
	}
	
	public Cursor getAll(String table){
		Log.d(TAG, "getAll");
		return mSQLiteDatabase.rawQuery("select * from "+table, null);
	}
	public Cursor getPhone(String phone,int i){
		Cursor mReturn = null;
		switch(i){
		case 1:
			mReturn= mSQLiteDatabase.rawQuery("select * from "+MESSAGE_RUBBISH_TABLE_NAME+" where "+MESSAGE_RUBBISH_PHONE+"="+"\'"+phone+"\'", null);
			break;
		case 2:
			mReturn= mSQLiteDatabase.rawQuery("select * from "+MESSAGE_FORBID_TABLE_NAME+" where "+MESSAGE_FORBID_PHONE+"="+"\'"+phone+"\'", null);
			break;
		case 3:
			mReturn =mSQLiteDatabase.rawQuery("select * from "+CALL_RECORD_TABLE_NAME+" where "+CALL_RECORD_PHONE+"="+"\'"+phone+"\'", null);
			break;
		case 4:
			mReturn= mSQLiteDatabase.rawQuery("select * from "+CALL_FORBID_TABLE_NAME+" where "+CALL_FORBID_PHONE+"="+"\'"+phone+"\'", null);
			break;
		}
		return mReturn;
	}
	
	public Cursor getTime(String tableName,String columnName,String recordTime){
		return mSQLiteDatabase.rawQuery("select * from "+tableName+" where "+columnName+" = "+"\'"+recordTime+"\'", null);
	}
	
	public Cursor getId(long id,String tableName){
		return mSQLiteDatabase.rawQuery("select * from "+tableName+" where "+TABLE_ID+" = "+ id, null);
	}
	
	public Long getAdd(Message_Rubbish_Info mInfo){
		ContentValues mValues=new ContentValues();
		mValues.put(MESSAGE_RUBBISH_CONTENT,mInfo.getMessage_rubbish_content() );
		mValues.put(MESSAGE_RUBBISH_PHONE, mInfo.getMessage_rubbish_phone());
		mValues.put(MESSAGE_RUBBISH_TIME, mInfo.getMessage_rubbish_time());
		long rowId=mSQLiteDatabase.insert(MESSAGE_RUBBISH_TABLE_NAME, null,mValues);
		return rowId;	
	}
	
	public Long getAdd(Message_Forbid_Info mInfo){
		ContentValues mValues=new ContentValues();
		mValues.put(MESSAGE_FORBID_PHONE,mInfo.getMessage_forbid_phone() );
		long rowId=mSQLiteDatabase.insert(MESSAGE_FORBID_TABLE_NAME, null,mValues);
		return rowId;	
	}
	
	public Long getAdd(Call_Forbid_Info mInfo){
		ContentValues mValues=new ContentValues();
		mValues.put(CALL_FORBID_PHONE,mInfo.getCall_forbid_phone() );
		long rowId=mSQLiteDatabase.insert(CALL_FORBID_TABLE_NAME, null,mValues);
		return rowId;	
	}
	
	public Long getAdd(Call_Record_Info mInfo){
		ContentValues mValues=new ContentValues();
		mValues.put(CALL_RECORD_AREA,mInfo.getCall_record_area() );
		mValues.put(CALL_RECORD_PHONE, mInfo.getCall_record_phone());
		mValues.put(CALL_RECORD_TIME, mInfo.getCall_record_time());
		long rowId=mSQLiteDatabase.insert(CALL_RECORD_TABLE_NAME, null,mValues);
		return rowId;	
	}
	
	public boolean getDel(long rowId,String table){
		return mSQLiteDatabase.delete(table, TABLE_ID+"="+rowId, null) > 0;	
	}
	
	public boolean getDel(String table, String column, String phone){
		return mSQLiteDatabase.delete(table, column+"=?", new String[]{phone}) > 0;	
	}
	
	public void deleteTable(String tableName){
		mSQLiteDatabase.execSQL("delete from "+tableName);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		StringBuilder mStringBuilder_1;
		StringBuilder mStringBuilder_2;
		StringBuilder mStringBuilder_3;
		StringBuilder mStringBuilder_4;
		
		public DatabaseHelper(Context context) {
			super(context, DB, null, DB_VERSION);
		}
		
		public void initCreateSQL(){
					mStringBuilder_1=new StringBuilder();
					mStringBuilder_1.delete(0, mStringBuilder_1.length());
					Log.d(TAG, "initCreateSQL");
					mStringBuilder_1.append("create table ");
					mStringBuilder_1.append(MESSAGE_RUBBISH_TABLE_NAME );
					mStringBuilder_1.append("(");
					mStringBuilder_1.append(TABLE_ID );	
					mStringBuilder_1.append(" integer primary key AUTOINCREMENT,");
					mStringBuilder_1.append(MESSAGE_RUBBISH_PHONE);
					mStringBuilder_1.append(" text ,");
					mStringBuilder_1.append(MESSAGE_RUBBISH_TIME);
					mStringBuilder_1.append(" text ,");
					mStringBuilder_1.append(MESSAGE_RUBBISH_CONTENT);
					mStringBuilder_1.append(" text ");
					mStringBuilder_1.append(" );");
					Log.d(TAG, "MESSAGE_RUBBISH_TABLE_NAME");
					mStringBuilder_2=new StringBuilder();
					mStringBuilder_2.append("create table ");	
					mStringBuilder_2.append(MESSAGE_FORBID_TABLE_NAME );
					mStringBuilder_2.append("( ");
					mStringBuilder_2.append(TABLE_ID);	
					mStringBuilder_2.append(" integer primary key AUTOINCREMENT,");
					mStringBuilder_2.append(MESSAGE_FORBID_PHONE);
					mStringBuilder_2.append(" text");
					mStringBuilder_2.append(" );");
					Log.d(TAG, "MESSAGE_FORBID_TABLE_NAME");
					mStringBuilder_3=new StringBuilder();
					mStringBuilder_3.append("create table ");	
					mStringBuilder_3.append(CALL_RECORD_TABLE_NAME );
					mStringBuilder_3.append("( ");
					mStringBuilder_3.append(TABLE_ID );	
					mStringBuilder_3.append(" integer primary key AUTOINCREMENT,");
					mStringBuilder_3.append(CALL_RECORD_PHONE);
					mStringBuilder_3.append(" text,");
					mStringBuilder_3.append(CALL_RECORD_TIME);
					mStringBuilder_3.append(" text,");
					mStringBuilder_3.append(CALL_RECORD_AREA);
					mStringBuilder_3.append(" text ");
					mStringBuilder_3.append(" );");
					Log.d(TAG, "CALL_RECORD_TABLE_NAME");
					mStringBuilder_4=new StringBuilder();
					mStringBuilder_4.append("create table ");	
					mStringBuilder_4.append(CALL_FORBID_TABLE_NAME );
					mStringBuilder_4.append("( ");
					mStringBuilder_4.append(TABLE_ID );	
					mStringBuilder_4.append(" integer primary key AUTOINCREMENT,");
					mStringBuilder_4.append(CALL_FORBID_PHONE);
					mStringBuilder_4.append(" text");	
					mStringBuilder_4.append(" );");	
					Log.d(TAG, "CALL_FORBID_TABLE_NAME");
				
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "onCreate");
			Log.d(TAG, "mStringBuilder");
			
			initCreateSQL();
			db.execSQL(mStringBuilder_1.toString());
			Log.d(TAG, mStringBuilder_1.toString());
			db.execSQL(mStringBuilder_2.toString());
			Log.d(TAG, mStringBuilder_2.toString());
			db.execSQL(mStringBuilder_3.toString());
			Log.d(TAG, mStringBuilder_3.toString());
			db.execSQL(mStringBuilder_4.toString());
			Log.d(TAG, mStringBuilder_4.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists"+MESSAGE_RUBBISH_TABLE_NAME, null);
			Log.d(TAG, "onUpgrade");
		}
		
	}
}
