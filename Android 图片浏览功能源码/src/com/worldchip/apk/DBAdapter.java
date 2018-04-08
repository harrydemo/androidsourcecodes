package com.worldchip.apk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/*
* Database operator class, to create, open, use, and close DB. *
*/
public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ALBUMS = "albums";
    public static final String KEY_PATH = "path";
    public static final String KEY_IMAGE="image";
    private static final String TAG = "images";
    private static final String DATABASE_NAME = "thumbimages";
    private static final String DATABASE_TABLE = "thumbnails";
    private static final int DATABASE_VERSION = 1;
    
    
    /*
    * create table SQL
    */
    private static final String DATABASE_CREATE =
    "create table thumbnails (_id integer primary key autoincrement, "
    + "name text, albums text, "
    + "path text, image blob);";
    
    private final Context context;
    
    //DB assistant instance

    private DatabaseHelper DBHelper;
    
    //DB instance

    private SQLiteDatabase db;
    
    /*
    * DBAdapter constructor
    */
    public DBAdapter(Context ctx)
    {
       this.context = ctx;
       DBHelper = new DatabaseHelper(context);
    }
    
    /*
    * DB help class, it is a DB assistant class
    * You will need to override onCreate() and onUpgrade() method.
    */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
       DatabaseHelper(Context context)
       {
          super(context, DATABASE_NAME, null, DATABASE_VERSION);
       }
    
       @Override
       public void onCreate(SQLiteDatabase db)
       {
    	   Log.i(TAG, "DatabaseHelper onCreated!");
           db.execSQL(DATABASE_CREATE);
       } 
       
       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
       {
           Log.w(TAG, "Upgrading database from version " + oldVersion
           + " to "
           + newVersion + ", which will destroy all old data");
           db.execSQL("DROP TABLE IF EXISTS thumbnails");
           onCreate(db);
       }
    }//end of DatabaseHelper

    
    /*****************************************************
    * Below are all DBAdaptor method: create, open...
    ****************************************************/
    
    /*
    * Open DB
    */
    public DBAdapter open() throws SQLException
    {
        //get a DB through DB assistant

        db = DBHelper.getWritableDatabase();
        return this;
    }

    /*
    * close DB
    */
    public void close()
    {
        //close DB through DB assistant

        DBHelper.close();
    }

    /*
    * Insert one image
    */
    public long insertImage(String name, String albums, String path, Bitmap image)
    {
    	Log.i(TAG, "ready to insert! the name="+name+"; the albums="+albums+"; the path="+path
    			+"; the image Width="+image.getWidth()+"; the imageHeight="+image.getHeight());
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_ALBUMS, albums);
        initialValues.put(KEY_PATH, path);
        //initialValues.put(KEY_IMAGE, getBitmap(image));
        long rowId= db.insert(DATABASE_TABLE, null, initialValues);
        
        //保存缩略图到本地
        Log.i(TAG,"start to saveImage to local media!");
        
        if(ImageCommon.saveBitmap(albums+"_"+name, image))
        {
        	//other's thing
        	Log.i(TAG,"haha~~~~, save Image success!");
        }
        else 
        	Log.i(TAG,"nonono~~~~, save Image had wrong!");
        
        return rowId;
    }

    /*
    * Delete one image
    */
    public boolean deleteImage(long rowId)
    {
         return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    /*
     * Delete all Image
     */
    public void deleteAllImage()
    {
    	//return db.delete(DATABASE_TABLE, null, null)>0;
    	db.execSQL("DROP TABLE IF EXISTS thumbnails");
    	db.execSQL(DATABASE_CREATE);
    }

    /*
    * Query all image
    */
    public Cursor getAllImages()
    {
       return db.query(DATABASE_TABLE, new String[] {
       KEY_ROWID,
       KEY_NAME,
       KEY_ALBUMS,
       KEY_PATH},
       null,
       null,
       null,
       null,
       null);
    }

    /*
     * Query a specified image by id
     */
     public Bitmap getImageById(int id) throws SQLException
     {
    	 Log.i(TAG, "getImageById running! the id="+id);
         Cursor mCursor =db.query(true, DATABASE_TABLE, 
         		new String[] {
                 KEY_IMAGE
                },
                KEY_ROWID + "=" + id,
         null,
         null,
         null,
         null,
         null);
         
         if(mCursor.moveToFirst())
         {
        	 try
        	 {
        	     ByteArrayInputStream stream = new ByteArrayInputStream(mCursor.getBlob(0)); 
        	     mCursor.close();
                 return BitmapFactory.decodeStream(stream);
        	 }
        	 catch(Exception err)
        	 {
        		 mCursor.close();
        		 err.printStackTrace();
        		 Log.i(TAG, "when ByteArrayInputStream , it's wrong!");
        	 }
         }
         
         return null;
     }
     
    /*
    * Query a specified image by albums
    */
    public Cursor getImageByAlbum(String albums) throws SQLException
    {
        Cursor mCursor =db.query(true, DATABASE_TABLE, 
        		new String[] {
        		KEY_NAME,
                KEY_ROWID,
                KEY_PATH,
                KEY_IMAGE
               },
               KEY_ALBUMS + "='" + albums+"'",
        null,
        null,
        null,
        null,
        null);
        return mCursor;
    }

    /*
    * update a image
    */
    public boolean updateTitle(long rowId, String name, String albums, String path, Bitmap icon)
    {
        ContentValues args = new ContentValues();
         args.put(KEY_NAME, name);
         args.put(KEY_ALBUMS, albums);
         args.put(KEY_PATH, path);
         return db.update(DATABASE_TABLE, args,
         KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    //二进制存储照片
    public byte[] getBitmap(Bitmap icon) {   
        if (icon == null) {   
            return null;   
        }   
    
        Log.i(TAG, "byte[] getBitmap runing!");
        
        final ByteArrayOutputStream os = new ByteArrayOutputStream();          
        icon.compress(Bitmap.CompressFormat.PNG, 100, os);   
        
        return os.toByteArray();
    }  

}