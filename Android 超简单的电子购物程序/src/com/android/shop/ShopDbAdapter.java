package com.android.shop;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class ShopDbAdapter {
	    public static final String KEY_NAME = "name";
	    public static final String KEY_ROWID = "_id";
	    
	    public static final String COLLECTION_NAME = "name";
	    public static final String COLLECTION_ROWID = "_id";
	    
	    private static final String TAG = "NotesDbAdapter";
	    private final Context mCtx;
	    
        private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;
	    private static final String DATABASE_NAME = "data";
	    private static final String STORE_TABLE = "store";
	    private static final String COLLECTION_TABLE = "collection";
	    private static final int DATABASE_VERSION = 2;
	    
	    private static final String DATABASE_CREATED01="create table "+ STORE_TABLE+" ("+ KEY_ROWID+" integer primary key" +
	    		" autoincrement, "+KEY_NAME+"  text not null) ";
	    private static final String DATABASE_CREATED02="create table "+ COLLECTION_TABLE+" ("+ COLLECTION_ROWID+" integer primary key" +
		" autoincrement, "+ COLLECTION_NAME+"  text not null) ";
	    
	    private static class DatabaseHelper extends SQLiteOpenHelper{

			public DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
				// TODO Auto-generated constructor stub
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				// TODO Auto-generated method stub
				db.execSQL(DATABASE_CREATED01);
				db.execSQL(DATABASE_CREATED02);
				
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				 db.execSQL("drop table if exists data");
			}
	    } 
         public ShopDbAdapter(Context ctx) {
	        this.mCtx = ctx;
	    }
         public ShopDbAdapter open() throws SQLException {
             mDbHelper = new DatabaseHelper(mCtx);
             mDb = mDbHelper.getWritableDatabase();
             return this;
         }
         public void close() {
             mDbHelper.close();
         }
         /**
          * 根据商品名字存入购物车
          * @param title
          * @param body
          * @return
          */
         public long store_createNote(String name) {
             ContentValues initialValues = new ContentValues();
             initialValues.put(KEY_NAME, name);
             Log.i("name======", name);
             return mDb.insert(STORE_TABLE, null, initialValues);
         }
         /**
          * 根据商品名称存入收藏夹
          * @param name
          * @return
          */
         public long collection_createNote(String name) {
             ContentValues initialValues = new ContentValues();
             initialValues.put(COLLECTION_NAME, name);
             return mDb.insert(COLLECTION_TABLE, null, initialValues);
         }
        /**
         * 查询购物车数据
         * @return
         */
         public Cursor store_fetchAllNotes() {

             return mDb.query(STORE_TABLE, new String[] {KEY_ROWID, 
                     KEY_NAME}, null, null, KEY_NAME, null, KEY_ROWID);
         }
         /**
          * 查询收藏夹数据
          * @return
          */
         public Cursor collection_fetchAllNotes() {
        	 return  mDb.query(COLLECTION_TABLE, new String[] {COLLECTION_ROWID, 
            		 COLLECTION_NAME}, null, null, COLLECTION_NAME, null, COLLECTION_ROWID);
            
         }
         public void drop_Dababase(){
        	 mDb.execSQL("drop table store if exists data");
        	 mDb.execSQL("drop table collection if exists data");
         }
       




}
