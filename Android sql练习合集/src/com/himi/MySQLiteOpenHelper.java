
package com.himi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Himi
 * @���� ��������ֻ��Ҫ����һ�����캯�� �Լ���д����������OK����
 * 
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public final static int VERSION = 1;// �汾��
	public final static String TABLE_NAME = "himi";// ����
	public final static String ID = "id";// ����ContentProviderʹ��
	public final static String TEXT = "text";
	public static final String DATABASE_NAME = "Himi.db";

	public MySQLiteOpenHelper(Context context) {
		// ��Android �д����ʹ�һ�����ݿⶼ����ʹ��openOrCreateDatabase ������ʵ�֣�
		// ��Ϊ�����Զ�ȥ����Ƿ����������ݿ⣬���������򿪣������������򴴽�һ�����ݿ⣻
		// �����ɹ��򷵻�һ�� SQLiteDatabase���󣬷����׳��쳣FileNotFoundException��
		// ������������һ����Ϊ"DATABASE_NAME"�����ݿ⣬������һ��SQLiteDatabase���� 
		
		super(context, DATABASE_NAME, null, VERSION); 
	} 
	@Override
	// �����ݿ��һ�����ɵ�ʱ���������������һ�������������������������ݿ��;
	public void onCreate(SQLiteDatabase db) { 
		String str_sql = "CREATE TABLE " + TABLE_NAME + "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT + " text );";
		// CREATE TABLE ����һ�ű� Ȼ����������ǵı���
		// Ȼ�����У���һ����id �����������,int����
		// PRIMARY KEY ��ָ���� ����һ��int��,����Ψһ�ı�ʶһ��;
		// AUTOINCREMENT ��ʾ���ݿ��Ϊÿ����¼��key��һ��ȷ����¼��Ψһ��;
		// ����Ҽ���һ���ı� String����
		// ----------ע�⣺����str_sql��sql��䣬����dos���Ҫע��ո�
		db.execSQL(str_sql);
		// execSQL()������ִ��һ��sql���
		// ��Ȼ�˾�����������һ�����ݿ��Ͱ����ñ��sql.himi�ļ�,
		// ����Ҫע�� ���Ƿ����Ǵ������Ǵ����һ��str_sql���sql����ʾ��������
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// һ��Ĭ������£������ǲ��� ���ݿ����������
		// �����ݿ���Ҫ������ʱ��Android ϵͳ�������ĵ������������
		// һ������������������ɾ�����ݱ��������µ����ݱ�
		// ��Ȼ�Ƿ���Ҫ�������Ĳ�������ȫȡ������Ϸ����
		Log.v("Himi", "onUpgrade");
	} 
}  