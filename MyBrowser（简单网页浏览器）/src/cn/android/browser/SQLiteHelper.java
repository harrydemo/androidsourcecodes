package cn.android.browser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * ʵ�ֶԱ�Ĵ��������¡������������
 * @author ytm0220@163.com
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "historyDB";
	public static final String TB_NAME = "historyTB";
	
	public SQLiteHelper(Context context)
	{
		super(context, DB_NAME, null, 1);
	}
	
	/**
	 * �����±�
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_NAME + "(" +
				//HistoryBean.ID + " integer primary key," +
				HistoryBean.URL + " varchar," + 
				HistoryBean.TIME + " integer,"+
				HistoryBean.NAME + " varchar"+
				")");
	}
	
	/**
	 * �������ǰһ�δ������ݿ�汾��һ��ʱ����ɾ�����ٴ����±�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}
	
	/**
	 * �������
	 * @param db
	 * @param oldColumn
	 * @param newColumn
	 * @param typeColumn
	 */
	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
		try{
			db.execSQL("ALTER TABLE " +
					TB_NAME + " CHANGE " +
					oldColumn + " "+ newColumn +
					" " + typeColumn
			);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
