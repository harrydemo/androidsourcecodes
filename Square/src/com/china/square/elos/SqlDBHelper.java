package com.china.square.elos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SqlDBHelper extends SQLiteOpenHelper {

	private final static String TABLE_CREATE = "Create table ElosRecord (_id integer primary key autoincrement," +
			" name text not null, cent text not null)";
	private final static int VERSION = 1;

	private final static String SQL ="select _id from table ElosRecord";
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "ElosRecord";
	public static final String NAME = "name";
	public static final String CENT = "cent";
	private SQLiteDatabase mDB = null;
	 
	SqlDBHelper(Context contx){
		  super(contx, DATABASE_NAME, null, VERSION);
		  mDB = this.getWritableDatabase();
		  onCreate(mDB);   
	}  
	@Override
	public void onCreate(SQLiteDatabase db) {
	  // TODO Auto-generated method stub
		 try{
		   db.execSQL(TABLE_CREATE);
		  }catch(SQLException e){     
		  }
	}
	
	@Override
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  // TODO Auto-generated method stub
	  db.execSQL("DROP TABLE IF EXISTS ElosRecord");
	  onCreate(db);
	 } 
	 public Cursor query(){
	  Cursor cursor;
	  cursor = mDB.query(DATABASE_TABLE, new String[]{"_id", "name", "cent"}, null, null, null, null, null);
	  
	  return cursor;
	 } 
	 public long insert(int id, String name, String cent){
	  ContentValues content = new ContentValues();
	  content.put("_id", id);
	  content.put("name", name);
	  content.put("cent", cent);
	  long nRet = 0;
	  nRet = mDB.insert(DATABASE_TABLE, null, content);
	  return nRet;
	 } 
	 public long delete(String id){
	 // ContentValues content = new ContentValues();
	  String sTemp = "_id = " + id;
	  return mDB.delete(DATABASE_TABLE, sTemp, null);
	 } 
	 
	 @Override
	 public void close(){
	  mDB.execSQL("DROP TABLE IF EXISTS ElosRecord");
	  mDB.close();
	  //super.close();
	 } 
	 
}
