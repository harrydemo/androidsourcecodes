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
	
	// ���ݿ�汾
	private static final int	DB_VERSION		= 1;

	// ����Context����
	private Context				mContext		= null;
	
	

	// ִ��open���������ݿ�ʱ�����淵�ص����ݿ����
	private SQLiteDatabase		mSQLiteDatabase	= null;

	// ��SQLiteOpenHelper�̳й���
	private DatabaseHelper		mDatabaseHelper	= null;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		/* ���캯��-����һ�����ݿ� */
		DatabaseHelper(Context context)
		{
			//������getWritableDatabase() 
			//�� getReadableDatabase()����ʱ
			//�򴴽�һ�����ݿ�
			super(context, DATABASE_NAME, null, DB_VERSION);
			
			
		}

		/* ����һ���� */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// ���ݿ�û�б�ʱ����һ��
			db.execSQL(CREATE_TABLE);
		}

		/* �������ݿ� */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	
	/* ���캯��-ȡ��Context */
	public MyDataBaseAdapter(Context context)
	{
		mContext = context;
	}


	// �����ݿ⣬�������ݿ����
	public void open() throws SQLException
	{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}


	// �ر����ݿ�
	public void close()
	{
		mDatabaseHelper.close();
	}

	/* ����һ������ */
	public long insertData(String name,String path)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(TABLE_rNAME, name);
		initialValues.put(TABLE_PATH, path);

		return mSQLiteDatabase.insert(TABLE_NAME, TABLE_ID, initialValues);
	}

	/* ɾ��һ������ */
	public boolean deleteData(long rowId)
	{
		return mSQLiteDatabase.delete(TABLE_NAME, TABLE_ID + "=" + rowId, null) > 0;
	}

	/* ͨ��Cursor��ѯ�������� */
	public Cursor fetchAllData()
	{
		return mSQLiteDatabase.query(TABLE_NAME, new String[] {TABLE_ID, TABLE_rNAME, TABLE_PATH }, null, null, null, null, null);
	}

	/* ��ѯָ������ */
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

	/* ����һ������ */
	public boolean updateData(int id, String name, String path)
	{
		ContentValues args = new ContentValues();
		args.put(TABLE_ID, id);
		args.put(TABLE_rNAME, name);
		args.put(TABLE_PATH, path);
		
		return mSQLiteDatabase.update(TABLE_NAME, args, TABLE_ID + "=" + id, null) > 0;
	}
	
	//�õ��ļ�·��
	public String getPath(int id){
		
		Cursor c = 
		mSQLiteDatabase.query(true, TABLE_NAME, new String[] {TABLE_ID, TABLE_rNAME, TABLE_PATH }, TABLE_ID + "=" + Integer.toString(id), null, null, null, null, null);
		if(c.moveToFirst()){
		return c.getString(2);
		}
		else return "failed...";
		
	}
	
	//_id�Զ���ǰ
	public void updateID(int id)
	{	
		//��һ��ID
		int nextID = id+1;
		//�õ����ݿ���α�
		Cursor mCursor = fetchAllData();
		//�õ����ݿ�һ��������
		int nums = mCursor.getCount();
		
		for(int i=nums-id;i>=0;i--){
			ContentValues args = new ContentValues();
			//���ú�һ��ID��һ����ǰһλ
			args.put(TABLE_ID, nextID-1);
			mSQLiteDatabase.update(TABLE_NAME, args, TABLE_ID + "=" + nextID, null);
			nextID++;

		}
		
		
	}
}

