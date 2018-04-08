package com.pixtas.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAdapter {
//	private static final String TAG = "DataBAseAdapter";
	private static final String keyId = "_id";
	private static final String T1 = "title1";
	private static final String T2 = "title2";
	private static final String T3 = "title3";
	private static final String photoUrl = "photoUrl";
	private static final String cTime = "cTime";
	
	//��ʶ�Ƿ��Ѿ�����
	private static final String audioTitle = "audioTitle";
	private static final String audioUrl = "audioUrl";
	private static final String chapterNum = "chapterNum";
	private static final String install = "install";
	private static final String version = "version";
	//���ݿ���
	private static final String DBName = "database";
	
	//���ݿ�汾
	private static final int DBVersion = 1;
	
	//flash ��
	private static final String flashTB = "flashTB";
	//��ʾ flash ����״̬��
	private static final String installTB = "installTB";
	
	
	//����Context����
	private Context mContext = null;
	//����һ��flash��
	private static final String flashTBCreate = "CREATE TABLE "
												+ flashTB + "(" 
												+ keyId + " INTEGER PRIMARY KEY,"
												+ T1 + " VARCHAR," 
												+ T2 + " VARCHAR,"
												+ T3 + " VARCHAR,"
												+ cTime + " INTEGER,"
												+ photoUrl + " VARCHAR)";
	//����һ��install��
	private static final String installTBCreate = "CREATE TABLE "
												+ installTB + "("
												+ keyId + " INTEGER PRIMARY KEY,"
												+ audioTitle + " VARCHAR,"
												+ audioUrl + " VARCHAR,"
												+ chapterNum + " INTEGER,"
												+ install + " INTEGER,"
												+ version + " INTEGER)";
	
	private SQLiteDatabase flashDB = null;
	
	private DatabaseHelper flashDBHelper = null;
	
	private SQLiteDatabase installDB = null;
	
	private DatabaseHelper installDBHelper = null;

	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DBName, null, DBVersion);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			//���ݿ�û�б�ʱ����һ��
			db.execSQL(flashTBCreate);
			db.execSQL(installTBCreate);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
	/*���캯��ȡ��Context*/
	public DatabaseAdapter(Context context){
		this.mContext = context;
	}
	//��flash���ݿ⣬�������ݿ����
	public void openFlashDB() throws SQLException{
		flashDBHelper = new DatabaseHelper(mContext);
		flashDB = flashDBHelper.getWritableDatabase();
	}
	//�ر����ݿ�
	public void closeFlashDatabase(){
		flashDB.close();
		flashDBHelper.close(); 
	}

	//����һ��flash����
	public long insertFlashData(String title1,String title2,String title3,int time,String url){
		ContentValues cv = new ContentValues();
		cv.put(T1, title1);
		cv.put(T2, title2);
		cv.put(T3, title3);
		cv.put(cTime, time);
		cv.put(photoUrl, url);
		return flashDB.insert(flashTB, keyId, cv);
	}
	
	//ɾ��flash������������
	public void deleteFlashData(){
		flashDB.delete(flashTB, keyId + "<" + 999999, null);
	}
	
	//��ѯ����flash��������
	public Cursor fetchAllFlashData(){
		Cursor c = flashDB.query(flashTB, new String[] {keyId,T1,T2,T3,cTime,photoUrl}, null,null,null,null,null);
		if(c != null){
			c.moveToFirst();
		}
		return c;
	}	
	
	//�����ݿ⣬�������ݿ����
	public void openInstallDB() throws SQLException{
		installDBHelper = new DatabaseHelper(mContext);
		installDB = installDBHelper.getWritableDatabase();
	}
	//�ر����ݿ�
	public void closeInstallDatabase(){
		installDB.close();
		installDBHelper.close(); 
	}
	
	//����һ��install����
	public long insertInstallData(String title,String url,int num,int flag,int v){
		ContentValues cv = new ContentValues();
		cv.put(audioTitle, title);
		cv.put(audioUrl, url);
		cv.put(chapterNum, num);
		cv.put(install, flag);
		cv.put(version, v);
		return installDB.insert(installTB, keyId, cv);
	}
	//��ѯinstallָ��id����
	public Cursor fechInstallDataById(long rowId){
		Cursor c = installDB.query(true,installTB,new String[]{keyId,audioTitle,audioUrl,chapterNum,install,version},keyId + "=" + rowId, null, null, null,null,null);
		if(c != null){
			c.moveToFirst();
		}
		return c;
	}
	//��ѯ����install����
	public Cursor fechAllInstallData(){
		Cursor c = installDB.query(installTB, new String[] {keyId,audioTitle,audioUrl,chapterNum,install,version}, null,null,null,null,null);
		if(c != null){
			c.moveToFirst();
		}
		return c;
	}
	//ɾ��install������������
	public void deleteInsatllData(){
		installDB.delete(installTB, keyId + "<" + 999999, null);
	}
	//����install�ı�ʶ
	public boolean updateInsallFlag (long rowId,int flag){
		ContentValues cv = new ContentValues();
		cv.put(install, flag);
		return installDB.update(installTB, cv, keyId + "=" + rowId,null) > 0;
	}
	//����version�ı�ʶ
	public boolean updateInsallVersion (long rowId,int v){
		ContentValues cv = new ContentValues();
		cv.put(version, v);
		return installDB.update(installTB, cv, keyId + "=" + rowId,null) > 0;
	}
}
