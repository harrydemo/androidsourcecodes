package mif.apps.ringtonepro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseAdapter
{

	private final static String DATABASE_NAME = "RingLib";
	private final static String TABLE_NAME = "list";
	public final static String TABLE_ID = "_id";
	public final static String TABLE_rNAME = "name";
	public final static String TABLE_PATH = "path";
	private final static String CREATE_TABLE = "CREATE TABLE list (_id INTEGER PRIMARY KEY,name TEXT,path TEXT UNIQUE)";
	
	// 数据库版本
	private static final int	DB_VERSION		= 1;

	// 本地Context对象
	private Context				mContext		= null;
	
	

	// 执行open（）打开数据库时，保存返回的数据库对象
	private SQLiteDatabase		mSQLiteDatabase	= null;

	// 由SQLiteOpenHelper继承过来
	private DatabaseHelper		mDatabaseHelper	= null;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		/* 构造函数-创建一个数据库 */
		DatabaseHelper(Context context)
		{
			//当调用getWritableDatabase() 
			//或 getReadableDatabase()方法时
			//则创建一个数据库
			super(context, DATABASE_NAME, null, DB_VERSION);
			
			
		}

		/* 创建一个表 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// 数据库没有表时创建一个
			db.execSQL(CREATE_TABLE);
		}

		/* 升级数据库 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	
	/* 构造函数-取得Context */
	public MyDataBaseAdapter(Context context)
	{
		mContext = context;
	}


	// 打开数据库，返回数据库对象
	public void open() throws SQLException
	{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}


	// 关闭数据库
	public void close()
	{
		mDatabaseHelper.close();
	}

	/* 插入一条数据 */
	public long insertData(String name,String path)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(TABLE_rNAME, name);
		initialValues.put(TABLE_PATH, path);

		return mSQLiteDatabase.insert(TABLE_NAME, TABLE_ID, initialValues);
	}

	/* 删除一条数据 */
	public boolean deleteData(long rowId)
	{
		return mSQLiteDatabase.delete(TABLE_NAME, TABLE_ID + "=" + rowId, null) > 0;
	}

	/* 通过Cursor查询所有数据 */
	public Cursor fetchAllData()
	{
		return mSQLiteDatabase.query(TABLE_NAME, new String[] {TABLE_ID, TABLE_rNAME, TABLE_PATH }, null, null, null, null, null);
	}

	/* 查询指定数据 */
	public Cursor fetchData(long rowId) throws SQLException
	{

		Cursor mCursor =

		mSQLiteDatabase.query(true, TABLE_NAME, new String[] { TABLE_ID, TABLE_rNAME, TABLE_PATH }, TABLE_ID + "=" + rowId, null, null, null, null, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/* 更新一条数据 */
	public boolean updateData(int id, String name, String path)
	{
		ContentValues args = new ContentValues();
		args.put(TABLE_ID, id);
		args.put(TABLE_rNAME, name);
		args.put(TABLE_PATH, path);
		
		return mSQLiteDatabase.update(TABLE_NAME, args, TABLE_ID + "=" + id, null) > 0;
	}
	
	//得到文件路径
	public String getPath(int id){
		
		Cursor c = 
		mSQLiteDatabase.query(true, TABLE_NAME, new String[] {TABLE_ID, TABLE_rNAME, TABLE_PATH }, TABLE_ID + "=" + Integer.toString(id), null, null, null, null, null);
		if(c.moveToFirst()){
		return c.getString(2);
		}
		else return "failed...";
		
	}
	
	//_id自动提前
	public void updateID(int id)
	{	
		//下一个ID
		int nextID = id+1;
		//得到数据库的游标
		Cursor mCursor = fetchAllData();
		//得到数据库一共的项数
		int nums = mCursor.getCount();
		
		for(int i=nums-id;i>=0;i--){
			ContentValues args = new ContentValues();
			//让让后一个ID减一，提前一位
			args.put(TABLE_ID, nextID-1);
			mSQLiteDatabase.update(TABLE_NAME, args, TABLE_ID + "=" + nextID, null);
			nextID++;

		}
		
		
	}
}

