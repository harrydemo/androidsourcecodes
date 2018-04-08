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
	

	//���ݿ���
	private static final String DB_NAME = "sms_database";
	
	//�������ݱ���
	private static final String DB_TABLE = "sms_table";
	
	//���ݿ�汾
	private static final int DB_VERSION = 1;
	
	//����Context����
	private Context mContext = null;
	
	//����sms_table��
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
	/*���캯��ȡ��Context*/
	public DatabaseAdapterHelper(Context context){
		this.mContext = context;
	}
	//��sms���ݿ⣬�������ݿ����
	public void open_sms() throws SQLException{
		dbHelper_sms = new DatabaseHelper(mContext);
		db_sms = dbHelper_sms.getWritableDatabase();
	}
	
	//�ر�sms���ݿ�
	public void close_sms(){
		db_sms.close();
		dbHelper_sms.close(); 
	}
	

	//����һ������
	public long insertData(int data){
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATA, data);
		return db_sms.insert(DB_TABLE, KEY_ID, cv);
	}
	
	//ɾ��һ������
	public boolean deleteData(long rowId){
		return db_sms.delete(DB_TABLE, KEY_ID + "=" + rowId, null) > 0;
	}
	//ɾ��������Ƭ����
	public void deleteAllSmsData(){
		db_sms.delete(DB_TABLE, KEY_ID + "<" + 999999, null);
	}
	//��ѯ��������
	public Cursor fetchAllData(){
		Cursor c = db_sms.query(DB_TABLE, new String[] {KEY_ID,KEY_DATA}, null,null,null,null,null);
		if(c != null){
			c.moveToFirst();
		}
		return c;
	}
	//��ѯָ������
	public Cursor fetchDataById(long rowId) throws SQLException {
		Cursor cursor = db_sms.query(true, DB_TABLE, new String[]{KEY_ID,KEY_DATA}, KEY_ID + "=" + rowId , null,null,null,null,null);
		if(cursor != null){
			cursor.moveToFirst();
		}
		return cursor;
	}

	//����һ������
	public boolean updateSmsData(long rowId,int data){
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATA, data);
		return db_sms.update(DB_TABLE,cv,KEY_ID + "=" + rowId,null) > 0;
	}
	
}
