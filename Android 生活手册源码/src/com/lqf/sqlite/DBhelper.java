package com.lqf.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
	private final static String DB_NAME = "RiJi.db";// ���ݿ���
	private final static int DB_VERSION = 1;// �汾��

	private final static String TAB_NAME = "riji";// ����
	public final static String ID = "_id";// �ռǵ�ID
	public final static String TITLE = "title";// �ռǵı���
	public final static String MARCH = "march";// �ռǵ�����
	public final static String NEIRONG = "neirong";// �ռǵ�����


	// ��д���캯��
	public DBhelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	// ���ݿ��һ�δ���ʱ������
	public void onCreate(SQLiteDatabase db) {
		// �������ռǱ�
		db.execSQL("create table " + TAB_NAME + "(" + ID
				+ " integer primary key autoincrement," + TITLE + " text,"
				+ MARCH + " text," + NEIRONG + " text)");

	}

	// �汾����ʱ������
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * �ռǱ����ɾ�Ĳ�
	 */
	// ���
	public long rijiinsert(String title, String neirong, String march) {
		// ����SQLiteDatabase���getWritableDatabase()�ķ���
		SQLiteDatabase db = this.getWritableDatabase();
		// ��ȡһ�����ݿ�ʵ��������
		ContentValues cv = new ContentValues();
		// ָ��Ҫ��ӵ�ֵ
		cv.put("title", title);
		cv.put("march", march);
		cv.put("neirong", neirong);
		// �����ݿ��
		long row = db.insert(TAB_NAME, null, cv);
		return row;
	}

	// ɾ��
	public void rijidelete(int id) {
		// ����SQLiteDatabase���getWritableDatabase()�ķ���
		SQLiteDatabase db = this.getWritableDatabase();
		// дɾ�������
		db.delete(TAB_NAME, ID +"=?", new String[]{String.valueOf(id)});
	}
	// �޸�
	
	// ��ѯ
	public Cursor rijiselect(){
		// ����SQLiteDatabase���getWritableDatabase()�ķ���
		SQLiteDatabase db = this.getWritableDatabase();
		//��ѯ���
		Cursor cursor = db.query(TAB_NAME, null, null, null, null, null, null);
		//����ֵ
		return cursor;
	}

}
